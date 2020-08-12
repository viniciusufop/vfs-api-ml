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
        if(Objects.isNull(newPayment.getCodePayment())) return;
        if(paymentRepository.existsByCodeAndStatus(newPayment.getCodePayment(),
                PaymentStatus.SUCESSO)) {
            errors.rejectValue("codePayment",
                    "br.com.vfs.api.ml.bean-validation.payment-already-successfully-processed",
                    "Payment already successfully processed");
        }
    }
}
