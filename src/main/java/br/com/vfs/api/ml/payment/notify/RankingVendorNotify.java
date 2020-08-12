package br.com.vfs.api.ml.payment.notify;

import br.com.vfs.api.ml.purchase.Purchase;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.Serializable;

@Slf4j
@Component
public class RankingVendorNotify implements EventPaymentNotify {
    @Override
    public void notify(final Purchase purchase) {
        final var rankingVendor = new RankingVendor(purchase.getProduct().getUser().getId(), purchase.getId());
        final var restTemplate = new RestTemplate();
        restTemplate.postForEntity("http://localhost:8080/mock/ranking-vendor", rankingVendor, Void.class);
    }
}

@Getter
@RequiredArgsConstructor
class RankingVendor implements Serializable {
    private final Long idVendor;
    private final Long idPurchase;
}
