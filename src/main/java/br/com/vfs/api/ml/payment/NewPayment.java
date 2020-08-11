package br.com.vfs.api.ml.payment;

import br.com.vfs.api.ml.purchase.Purchase;
import br.com.vfs.api.ml.purchase.PurchaseRepository;
import br.com.vfs.api.ml.shared.annotations.ExistElement;
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
    private String codePayment;
    @NotNull
    private PaymentStatus status;

    public Payment toModel(final PurchaseRepository purchaseRepository, PaymentRepository paymentRepository){
        final var payment =
                paymentRepository.findByCodeAndPurchase_Id(codePayment, idPurchase)
                .map(p -> setNewStatus(p, status))
                .orElseGet(() -> toPayment(purchaseRepository));
        return payment;
    }

    private Payment setNewStatus(final Payment payment, final PaymentStatus status){
        payment.setNewStatus(status);
        return payment;
    }

    private Payment toPayment(final PurchaseRepository purchaseRepository) {
        final var purchase = purchaseRepository.findById(idPurchase).orElseThrow();
        return new Payment(purchase, codePayment, status);
    }
}
