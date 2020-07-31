package br.com.vfs.api.ml.question;

import br.com.vfs.api.ml.product.Product;
import br.com.vfs.api.ml.user.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Embeddable
@NoArgsConstructor
public class Question implements Serializable {

    @NotBlank
    @Column(nullable = false)
    private String title;
    @PastOrPresent
    private LocalDateTime createAt = LocalDateTime.now();
    @NotNull
    @ManyToOne(optional = false)
    private User user;

    public Question(@NotBlank String title, @NotNull User user) {
        this.title = title;
        this.user = user;
    }

    public void notify(final EmailNotifyService emailNotifyService, final Product product) {
        emailNotifyService.send(this, product);
    }
}
