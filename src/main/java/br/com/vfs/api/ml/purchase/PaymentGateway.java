package br.com.vfs.api.ml.purchase;

import lombok.RequiredArgsConstructor;
import org.springframework.util.Assert;

import java.util.Objects;

@RequiredArgsConstructor
public enum PaymentGateway {
    PAYPAL("paypal.com/%d?redirectUrl=http:/localhost:8080/api/payment-paypal"),
    PAGSEGURO("pagseguro.com?returnId=%d&redirectUrl=http:/localhost:8080/api/payment-pagseguro");

    private final String url;

    public String redirectURL(final Purchase purchase) {
        Assert.isTrue(Objects.nonNull(purchase.getId()), "purchase is required");
        return String.format(url, purchase.getId());
    }
}
