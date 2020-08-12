package br.com.vfs.api.ml.payment.notify;

import br.com.vfs.api.ml.purchase.Purchase;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.Serializable;

@Component
public class InvoiceNotify implements EventPaymentNotify {

    @Value("${url.invoice}")
    private String url;

    @Override
    public void notify(Purchase purchase) {
        final var invoice = new Invoice(purchase.getBuyer().getId(), purchase.getId());
        final var restTemplate = new RestTemplate();
        restTemplate.postForEntity(url, invoice, Void.class);
    }
}

@Getter
@RequiredArgsConstructor
class Invoice implements Serializable {
    private final Long idBuyer;
    private final Long idPurchase;
}
