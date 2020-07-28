package br.com.vfs.api.ml.product;

import br.com.vfs.api.ml.category.Category;
import br.com.vfs.api.ml.user.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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
}
