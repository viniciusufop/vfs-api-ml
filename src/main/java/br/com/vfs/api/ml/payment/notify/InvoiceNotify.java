package br.com.vfs.api.ml.payment.notify;

import br.com.vfs.api.ml.payment.Payment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class InvoiceNotify implements PaymentNotify {
    @Override
    public void notify(Payment payment) {
        log.info("M=notify, payment={} buyer={}", payment.getId(),
                payment.getPurchase().getBuyer().getId());
            /*
            TODO
            - comunicar com o setor de nota fiscal (id da compra e o id do usuário).
                    Ideia: criar um endpoint fake para receber essa comunicacao
             */
    }
}
