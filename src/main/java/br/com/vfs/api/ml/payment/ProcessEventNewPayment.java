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

    public void process(final Purchase purchase){
        if(purchase.isFinally()){
            eventPaymentNotifies.forEach(eventPaymentNotify -> eventPaymentNotify.notify(purchase));
            emailNotifyService.send(bodySuccess(purchase), purchase.getBuyer());
        } else {
            emailNotifyService.send(bodyError(purchase), purchase.getBuyer());
        }
    }

    private String bodyError(final Purchase purchase) {
        return String.format(
                "Hi %s, \n" +
                        "\t Your payment failed, please try again: \n" +
                        "Purchase: %s \n" +
                        "Quantity: %s \n" +
                        "Product: %s \n" +
                        "link: http://my-url-ml/produtcs/%s"
                , purchase.getBuyer().getLogin()
                , purchase.getId()
                , purchase.getQuantity()
                , purchase.getProduct().getName()
                , purchase.getProduct().getId());
    }

    private String bodySuccess(final Purchase purchase) {
        return String.format(
                "Hi %s, \n" +
                        "\t Your payment was successful: \n" +
                        "Purchase: %s \n" +
                        "Quantity: %s \n" +
                        "Product: %s"
                , purchase.getBuyer().getLogin()
                , purchase.getId()
                , purchase.getQuantity()
                , purchase.getProduct().getName());
    }
}
