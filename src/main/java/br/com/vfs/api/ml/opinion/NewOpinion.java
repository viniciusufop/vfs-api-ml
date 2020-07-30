package br.com.vfs.api.ml.opinion;

import br.com.vfs.api.ml.product.Product;
import br.com.vfs.api.ml.product.ProductRepository;
import br.com.vfs.api.ml.shared.annotations.ExistElement;
import br.com.vfs.api.ml.shared.security.UserLogged;
import br.com.vfs.api.ml.user.UserRepository;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class NewOpinion {
    @NotBlank
    private String title;
    @Size(max = 500)
    @NotBlank
    private String description;
    @Max(5)
    @Min(1)
    @NotNull
    private Integer evaluation;
    @ExistElement(domainClass = Product.class)
    @NotNull
    private Long idProduct;

    public Opinion toModel(UserLogged userLogged, UserRepository userRepository, final ProductRepository productRepository){
        final var product = productRepository.findById(idProduct)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        final var user = userRepository.findByLogin(userLogged.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return new Opinion(title, description, evaluation, product, user);
    }
}
