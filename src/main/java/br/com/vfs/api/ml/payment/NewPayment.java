package br.com.vfs.api.ml.payment;

import br.com.vfs.api.ml.purchase.Purchase;

public interface NewPayment {
    Long getIdPurchase();
    String getCodePayment();
    Payment toModel(final Purchase purchase);
}
