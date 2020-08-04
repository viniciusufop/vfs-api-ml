package br.com.vfs.api.ml.purchase;

import br.com.vfs.api.ml.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Objects;

@RequiredArgsConstructor
public class QuantityByProductValidator implements Validator {

    private final ProductRepository productRepository;

    @Override
    public boolean supports(final Class<?> clazz) {
        return NewPurchase.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(final Object target, final Errors errors) {
        final var newPurchase = (NewPurchase) target;
        if(Objects.nonNull(newPurchase.getIdProduct())){
            productRepository.findById(newPurchase.getIdProduct())
                    .ifPresent(product -> {
                        if(!product.hasStock(newPurchase.getQuantity()))
                            errors.rejectValue("quantity", "br.com.vfs.api.ml.bean-validation.product-quantity-not-stock",
                                    "product not have quantity in stock");
                    });
        }
    }
}
