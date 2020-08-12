package br.com.vfs.api.ml.payment;

import br.com.vfs.api.ml.purchase.Purchase;
import br.com.vfs.api.ml.shared.annotations.ExistElement;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class NewPaymentPayPal implements NewPayment{

    @NotNull
    @ExistElement(domainClass = Purchase.class)
    private Long idPurchase;
    @NotBlank
    private String codePayment;
    @Min(0)
    @Max(1)
    private int status;

    public Payment toModel(final Purchase purchase) {
        final var paymentStatus = status == 0 ? PaymentStatus.ERROR : PaymentStatus.SUCCESS;
        return new Payment(purchase, codePayment, paymentStatus);
    }
}
