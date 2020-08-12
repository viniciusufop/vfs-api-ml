package br.com.vfs.api.ml.payment;

import br.com.vfs.api.ml.purchase.Purchase;
import br.com.vfs.api.ml.purchase.PurchaseRepository;
import br.com.vfs.api.ml.shared.annotations.ExistElement;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public interface NewPayment {
    Long getIdPurchase();
    String getCodePayment();
    Payment toModel(final Purchase purchase);
}
