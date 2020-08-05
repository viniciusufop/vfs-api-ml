package br.com.vfs.api.ml.payment;

import br.com.vfs.api.ml.purchase.Purchase;
import br.com.vfs.api.ml.purchase.PurchaseRepository;
import br.com.vfs.api.ml.shared.annotations.ExistElement;
import br.com.vfs.api.ml.shared.annotations.UniqueValue;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class NewPayment {

    @NotNull
    @ExistElement(domainClass = Purchase.class)
    private Long idPurchase;
    @NotBlank
    @UniqueValue(domainClass = Payment.class, fieldName = "code")
    private String codePayment;
    @NotNull
    private PaymentStatus status;

    public Payment toModel(final PurchaseRepository purchaseRepository){
        final var purchase = purchaseRepository.findById(idPurchase).orElseThrow();
        return new Payment(purchase, codePayment, status);
    }
}
