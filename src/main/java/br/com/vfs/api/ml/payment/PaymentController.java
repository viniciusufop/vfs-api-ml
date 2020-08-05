package br.com.vfs.api.ml.payment;

import br.com.vfs.api.ml.payment.notify.InvoiceNotify;
import br.com.vfs.api.ml.payment.notify.RankingVendorNotify;
import br.com.vfs.api.ml.purchase.PurchaseRepository;
import br.com.vfs.api.ml.question.EmailNotifyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PurchaseRepository purchaseRepository;
    private final PaymentRepository paymentRepository;
    private final EmailNotifyService emailNotifyService;
    private final InvoiceNotify invoiceNotify;
    private final RankingVendorNotify rankingVendorNotify;
    @ResponseStatus(OK)
    @PostMapping
    public void create(@RequestBody @Valid final NewPayment newPayment){
        log.info("M=create, newPayment={}", newPayment);
        final var payment = newPayment.toModel(purchaseRepository);
        paymentRepository.save(payment);
        payment.process(emailNotifyService, List.of(invoiceNotify, rankingVendorNotify));
    }
}
