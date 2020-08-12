package br.com.vfs.api.ml.payment;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PaymentStatus {
    ERROR, SUCCESS;

    public boolean isPay() {
        return this.equals(SUCCESS);
    }
}
