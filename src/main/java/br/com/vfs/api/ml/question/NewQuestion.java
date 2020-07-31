package br.com.vfs.api.ml.question;

import br.com.vfs.api.ml.product.Product;
import br.com.vfs.api.ml.product.ProductRepository;
import br.com.vfs.api.ml.shared.annotations.ExistElement;
import br.com.vfs.api.ml.shared.security.UserLogged;
import br.com.vfs.api.ml.user.UserRepository;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class NewQuestion {

    @NotBlank
    private String title;
    @NotNull
    @ExistElement(domainClass = Product.class)
    private Long idProduct;

    public Question toModel(final UserLogged userLogged, final UserRepository userRepository){
        final var user = userRepository.findByLogin(userLogged.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("user not found"));
        return new Question(title, user);
    }

    public Product toProduct(ProductRepository productRepository) {
        return productRepository.findById(idProduct)
                .orElseThrow(() -> new IllegalArgumentException("product not found"));
    }
}
