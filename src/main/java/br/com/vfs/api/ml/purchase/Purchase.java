package br.com.vfs.api.ml.purchase;

import br.com.vfs.api.ml.product.Product;
import br.com.vfs.api.ml.user.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@Entity
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @ManyToOne(optional = false, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Product product;
    @NotNull
    @Positive
    @Column(nullable = false)
    private Integer quantity;
    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PurchaseStatus status;
    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentGateway gateway;
    @NotNull
    @ManyToOne
    private User buyer;

    public Purchase(@NotNull Product product, @NotNull @Positive Integer quantity,
                    @NotNull PaymentGateway gateway, @NotNull User buyer) {
        this.product = product;
        this.quantity = quantity;
        this.status = PurchaseStatus.STARTED;
        this.gateway = gateway;
        this.buyer = buyer;
    }

    public void removeFromStock() {
        product.removeStock(quantity);
    }

    public String redirectURL(final String urlRedirectConfirm) {
        Assert.isTrue(StringUtils.isNoneBlank(urlRedirectConfirm), "urlRedirectConfirm is required");
        return gateway.redirectURL(this, urlRedirectConfirm);
    }

    public boolean isFinally() {
        return status.isFinally();
    }

    public void finalized() {
        this.status = PurchaseStatus.FINALLY;
    }
}
