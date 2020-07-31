package br.com.vfs.api.ml.product;

import br.com.vfs.api.ml.category.Category;
import br.com.vfs.api.ml.question.EmailNotifyService;
import br.com.vfs.api.ml.question.NewQuestion;
import br.com.vfs.api.ml.question.Question;
import br.com.vfs.api.ml.shared.security.UserLogged;
import br.com.vfs.api.ml.user.User;
import br.com.vfs.api.ml.user.UserRepository;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@EntityListeners(AuditingEntityListener.class)
public class Product implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @CreatedDate
    @NotNull
    @Column(updatable = false)
    private LocalDateTime createAt;
    @NotBlank
    @Column(nullable = false)
    private String name;
    @NotNull
    @Size(max = 1000)
    @Column(nullable = false)
    private String description;
    @NotNull
    @Size(min = 3)
    @ElementCollection
    @CollectionTable
    private List<Feature> features;
    @NotNull
    @Positive
    @Column(nullable = false)
    private BigDecimal price;
    @NotNull
    @Positive
    @Column(nullable = false)
    private Integer quantity;
    @NotNull
    @ManyToOne(optional = false)
    private Category category;
    @NotNull
    @ManyToOne(optional = false)
    private User user;
    @ElementCollection
    @CollectionTable
    private Set<String> images = new HashSet<>();
    @ElementCollection
    @CollectionTable
    private List<Question> questions = new ArrayList<>();

    public Product(@NotBlank final String name, @NotNull @Size(max = 1000)final String description,
                   @NotNull @Size(min = 3) final List<Feature> features, @NotNull @Positive final BigDecimal price,
                   @NotNull @Positive final Integer quantity, @NotNull final Category category,
                   @NotNull final User user) {
        this.name = name;
        this.description = description;
        this.features = features;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
        this.user = user;
    }

    public void addImage(final MultipartFile file, final UserLogged userLogged, final ImageStoreService imageStoreService) throws IllegalAccessException {
        if(!user.isUser(userLogged.getUsername()))
            throw new IllegalAccessException("Product does not belong to the user!");
        final var url = imageStoreService.upload(file);
        images.add(url);
    }

    public void addQuestion(final NewQuestion newQuestion, final UserLogged userLogged, final UserRepository userRepository,
                            final EmailNotifyService emailNotifyService) {
        final var question = newQuestion.toModel(userLogged, userRepository);
        question.notify(emailNotifyService, this);
        questions.add(question);
    }
}
