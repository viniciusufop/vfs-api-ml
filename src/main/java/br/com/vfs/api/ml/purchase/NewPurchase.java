package br.com.vfs.api.ml.purchase;

import br.com.vfs.api.ml.product.Product;
import br.com.vfs.api.ml.product.ProductRepository;
import br.com.vfs.api.ml.shared.annotations.ExistElement;
import br.com.vfs.api.ml.shared.security.UserLogged;
import br.com.vfs.api.ml.user.UserRepository;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.io.Serializable;

@Data
@NoArgsConstructor
public class NewPurchase implements Serializable {

    @NotNull
    @ExistElement(domainClass = Product.class)
    private Long idProduct;
    @NotNull
    @Positive
    private Integer quantity;
    @NotNull
    private PaymentGateway gateway;

    public Purchase toModel(final UserLogged userLogged, final UserRepository userRepository, final ProductRepository productRepository) {
        final var buyer = userRepository.findByLogin(userLogged.getUsername()).orElseThrow();
        final var product = productRepository.findById(idProduct).orElseThrow();
        final var purchase = new Purchase(product, quantity, gateway, buyer);
        purchase.removeFromStock();
        return purchase;
    }
}
