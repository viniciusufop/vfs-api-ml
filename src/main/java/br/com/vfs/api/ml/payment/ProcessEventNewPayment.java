package br.com.vfs.api.ml.payment;

import br.com.vfs.api.ml.payment.notify.EventPaymentNotify;
import br.com.vfs.api.ml.purchase.Purchase;
import br.com.vfs.api.ml.question.EmailNotifyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProcessEventNewPayment {

    private final Set<EventPaymentNotify> eventPaymentNotifies;
    private final EmailNotifyService emailNotifyService;
    public void process(final Purchase purchase, final Payment payment){
        if(purchase.isFinally()){
            eventPaymentNotifies.forEach(eventPaymentNotify -> eventPaymentNotify.notify(purchase));
        }
        emailNotifyService.send(payment);
    }
}
