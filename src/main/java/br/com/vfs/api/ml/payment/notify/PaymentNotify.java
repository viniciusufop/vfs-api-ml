package br.com.vfs.api.ml.payment.notify;

import br.com.vfs.api.ml.payment.Payment;

@FunctionalInterface
public interface PaymentNotify {
    void notify(final Payment payment);
}
