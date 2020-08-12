package br.com.vfs.api.ml.payment;

import br.com.vfs.api.ml.purchase.PurchaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Objects;

@RequiredArgsConstructor
public class PaymentSuccessExistValidator implements Validator {

    private final PurchaseRepository purchaseRepository;
    @Override
    public boolean supports(final Class<?> clazz) {
        return NewPayment.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(final Object target, final Errors errors) {
        final var newPayment = (NewPayment) target;
        if(Objects.nonNull(newPayment.getIdPurchase())
            && Objects.nonNull(newPayment.getCodePayment())){
            purchaseRepository.findById(newPayment.getIdPurchase())
                    .ifPresent(purchase -> {
                        if(purchase.containsCodePayment(newPayment.getCodePayment())) {
                            errors.rejectValue("codePayment",
                                    "br.com.vfs.api.ml.bean-validation.payment-already-successfully-processed",
                                    "Payment exists in purchase");
                        }
                    });
        }
    }
}
