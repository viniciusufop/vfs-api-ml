package br.com.vfs.api.ml.opinion;

import br.com.vfs.api.ml.product.Product;
import br.com.vfs.api.ml.user.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Opinion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Column(nullable = false)
    private String title;
    @Size(max = 500)
    @NotBlank
    @Column(nullable = false, length = 500)
    private String description;
    @Max(5)
    @Min(1)
    @NotNull
    @Column(nullable = false)
    private Integer evaluation;
    @NotNull
    @ManyToOne(optional = false)
    private Product product;
    @NotNull
    @ManyToOne(optional = false)
    private User user;

    public Opinion(@NotBlank String title, @Size(max = 500) @NotBlank String description,
                   @Max(5) @Min(1) @NotNull Integer evaluation, @NotNull Product product,
                   @NotNull User user) {
        this.title = title;
        this.description = description;
        this.evaluation = evaluation;
        this.product = product;
        this.user = user;
    }
}
