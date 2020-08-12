package br.com.vfs.api.ml.payment;

import br.com.vfs.api.ml.purchase.PurchaseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import javax.validation.Valid;

import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PaymentController {

    private final PurchaseRepository purchaseRepository;
    private final ProcessEventNewPayment processEventNewPayment;

    @InitBinder
    public void init(final WebDataBinder dataBinder){
        dataBinder.addValidators(new PurchasePaymentValidator(purchaseRepository));
        dataBinder.addValidators(new PaymentSuccessExistValidator(purchaseRepository));
    }
    @ResponseStatus(OK)
    @PostMapping
    @Transactional
    @RequestMapping("/api/payment-paypal")
    public void create(@RequestBody @Valid final NewPaymentPayPal newPayment){
        process(newPayment);
    }

    @ResponseStatus(OK)
    @PostMapping
    @Transactional
    @RequestMapping("/api/payment-pagseguro")
    public void create(@RequestBody @Valid final NewPaymentPagSeguro newPayment){
        process(newPayment);

    }

    private void process(final NewPayment newPayment){
        log.info("M=process, newPayment={}", newPayment);
        final var purchase = purchaseRepository.findById(newPayment.getIdPurchase()).orElseThrow();
        purchase.addNewPayment(newPayment);
        purchaseRepository.save(purchase);
        processEventNewPayment.process(purchase);
    }
}
