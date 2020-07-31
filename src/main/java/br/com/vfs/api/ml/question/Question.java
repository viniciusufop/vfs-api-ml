package br.com.vfs.api.ml.question;

import br.com.vfs.api.ml.product.Product;
import br.com.vfs.api.ml.user.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EntityListeners;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Embeddable
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Question implements Serializable {

    @NotBlank
    @Column(nullable = false)
    private String title;
    @CreatedDate
    @PastOrPresent
    private LocalDateTime createAt;
    @NotNull
    @ManyToOne(optional = false)
    private Product product;
    @NotNull
    @ManyToOne(optional = false)
    private User user;

    public Question(@NotBlank String title, @NotNull Product product, @NotNull User user) {
        this.title = title;
        this.product = product;
        this.user = user;
    }

    public void notify(final EmailNotifyService emailNotifyService) {
        emailNotifyService.send(this);
    }
}
