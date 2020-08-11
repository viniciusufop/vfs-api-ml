package br.com.vfs.api.ml.payment;

import br.com.vfs.api.ml.purchase.PurchaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Objects;

@RequiredArgsConstructor
public class PurchasePaymentValidator implements Validator {

    private final PurchaseRepository purchaseRepository;
    @Override
    public boolean supports(final Class<?> clazz) {
        return NewPayment.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(final Object target, final Errors errors) {
        final var newPayment = (NewPayment) target;
        if(Objects.isNull(newPayment.getIdPurchase())) return;
        purchaseRepository.findById(newPayment.getIdPurchase())
                .ifPresent(purchase -> {
                    if(purchase.isFinally()){
                        errors.rejectValue("idPurchase", "br.com.vfs.api.ml.bean-validation.purchase-is-finalized", "Purchase is finalized");
                    }});
    }
}
