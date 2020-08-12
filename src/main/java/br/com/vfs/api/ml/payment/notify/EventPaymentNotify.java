package br.com.vfs.api.ml.payment.notify;

import br.com.vfs.api.ml.purchase.Purchase;

public interface EventPaymentNotify {
    void notify(final Purchase purchase);
}
