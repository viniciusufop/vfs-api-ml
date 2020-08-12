package br.com.vfs.api.ml.payment;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentPagSeguroStatus {
    SUCESSO(PaymentStatus.SUCCESS), ERRO(PaymentStatus.ERROR);
    private final PaymentStatus status;

}
