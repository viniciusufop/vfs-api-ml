package br.com.vfs.api.ml.payment;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Objects;

@RequiredArgsConstructor
public class PaymentSuccessExistValidator implements Validator {

    private final PaymentRepository paymentRepository;
    @Override
    public boolean supports(final Class<?> clazz) {
        return NewPayment.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(final Object target, final Errors errors) {
        final var newPayment = (NewPayment) target;
        if(Objects.isNull(newPayment.getCodePayment()) ||
                Objects.isNull(newPayment.getIdPurchase())) return;
        paymentRepository.findByCodeAndPurchase_Id(newPayment.getCodePayment(), newPayment.getIdPurchase())
                .ifPresent(payment -> {
                    if(payment.isSuccess()){
                        errors.rejectValue("codePayment",
                                "br.com.vfs.api.ml.bean-validation.payment-already-successfully-processed",
                                "Payment already successfully processed");
                    }});
    }
}
