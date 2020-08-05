package br.com.vfs.api.ml.payment;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PaymentStatus {
    ERRO, SUCESSO;

    public boolean isPay() {
        return this.equals(SUCESSO);
    }
}
