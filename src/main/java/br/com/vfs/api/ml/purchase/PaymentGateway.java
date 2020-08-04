package br.com.vfs.api.ml.purchase;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.util.Objects;

@RequiredArgsConstructor
public enum PaymentGateway {
    PAYPAL("paypal.com/%d?redirectUrl=%s"),
    PAGSEGURO("pagseguro.com?returnId=%d&redirectUrl=%s");

    private final String url;

    public String redirectURL(final Purchase purchase, final String urlRedirectConfirm) {
        Assert.isTrue(Objects.nonNull(purchase.getId()), "purchase is required");
        Assert.isTrue(StringUtils.isNoneBlank(urlRedirectConfirm), "urlRedirectConfirm is required");
        return String.format(url, purchase.getId(), urlRedirectConfirm);
    }
}
