package br.com.vfs.api.ml.purchase;

import br.com.vfs.api.ml.payment.NewPayment;
import br.com.vfs.api.ml.payment.Payment;
import br.com.vfs.api.ml.product.Product;
import br.com.vfs.api.ml.user.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
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
import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.Objects;
import java.util.Set;

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
    private PaymentGateway gateway;
    @NotNull
    @ManyToOne
    private User buyer;
    @ToString.Exclude
    @OneToMany(mappedBy = "purchase", cascade = CascadeType.MERGE)
    private Set<Payment> payments;

    public Purchase(@NotNull Product product, @NotNull @Positive Integer quantity,
                    @NotNull PaymentGateway gateway, @NotNull User buyer) {
        this.product = product;
        this.quantity = quantity;
        this.gateway = gateway;
        this.buyer = buyer;
    }

    public void removeFromStock() {
        product.removeStock(quantity);
    }

    public String redirectURL() {
        return gateway.redirectURL(this);
    }

    public boolean isFinally() {
        return payments.stream().anyMatch(Payment::isSuccess);
    }

    public void addNewPayment(final @Valid NewPayment newPayment) {
        Assert.isTrue(!containsCodePayment(newPayment.getCodePayment()), "Payment existing");
        Assert.isTrue(!isFinally(), "Purchase finally");
        final var payment = newPayment.toModel(this);
        this.payments.add(payment);
    }

    public boolean containsCodePayment(final String codePayment) {
        Assert.isTrue(Objects.nonNull(codePayment), "Code payment not null value");
        return payments.stream().map(Payment::getCode).anyMatch(codePayment::equals);
    }
}
