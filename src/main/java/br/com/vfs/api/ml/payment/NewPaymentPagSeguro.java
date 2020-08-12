package br.com.vfs.api.ml.payment;

import br.com.vfs.api.ml.purchase.Purchase;
import br.com.vfs.api.ml.shared.annotations.ExistElement;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class NewPaymentPagSeguro implements NewPayment{
    @NotNull
    @ExistElement(domainClass = Purchase.class)
    private Long idPurchase;
    @NotBlank
    private String codePayment;
    @NotNull
    private PaymentPagSeguroStatus status;

    public Payment toModel(final Purchase purchase){
        return new Payment(purchase, codePayment, status.getStatus());
    }
}
