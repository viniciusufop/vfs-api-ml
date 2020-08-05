package br.com.vfs.api.ml.payment;

import br.com.vfs.api.ml.payment.notify.PaymentNotify;
import br.com.vfs.api.ml.purchase.Purchase;
import br.com.vfs.api.ml.question.EmailNotifyService;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@EntityListeners(AuditingEntityListener.class)
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @CreatedDate
    @PastOrPresent
    private LocalDateTime createAt;
    @NotNull
    @ManyToOne(optional = false)
    private Purchase purchase;
    @NotBlank
    @Column(unique = true, nullable = false)
    private String code;
    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    public Payment(@NotNull Purchase purchase, @NotBlank String code, @NotNull PaymentStatus status) {
        this.purchase = purchase;
        this.code = code;
        this.status = status;
    }

    public void process(final EmailNotifyService emailNotifyService, final List<PaymentNotify> paymentNotifies) {
        if(status.isPay()){
            paymentNotifies.forEach(paymentNotify -> paymentNotify.notify(this));
        }
        emailNotifyService.send(this);
    }
}
