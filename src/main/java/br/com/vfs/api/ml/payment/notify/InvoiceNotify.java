package br.com.vfs.api.ml.payment.notify;

import br.com.vfs.api.ml.purchase.Purchase;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.Serializable;

@Component
public class InvoiceNotify implements EventPaymentNotify {
    @Override
    public void notify(Purchase purchase) {
        final var invoice = new Invoice(purchase.getBuyer().getId(), purchase.getId());
        final var restTemplate = new RestTemplate();
        restTemplate.postForEntity("http://localhost:8080/mock/invoice", invoice, Void.class);
    }
}

@Getter
@RequiredArgsConstructor
class Invoice implements Serializable {
    private final Long idBuyer;
    private final Long idPurchase;
}
