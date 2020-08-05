package br.com.vfs.api.ml.payment.notify;

import br.com.vfs.api.ml.payment.Payment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RankingVendorNotify implements PaymentNotify {
    @Override
    public void notify(Payment payment) {
        log.info("M=notify, payment={} vendor={}", payment.getId(),
                payment.getPurchase().getProduct().getUser().getId());
            /*
            TODO
            - comunicar com o sistema de ranking dos vendedores (id da compra e o id do vendedor).
                    Ideia: criar um endpoint fake para receber essa comunicacao
             */
    }
}
