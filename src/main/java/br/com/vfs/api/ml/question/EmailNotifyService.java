package br.com.vfs.api.ml.question;

import br.com.vfs.api.ml.payment.Payment;
import br.com.vfs.api.ml.product.Product;
import br.com.vfs.api.ml.purchase.Purchase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailNotifyService {
    public void send(final Question question, final Product product){
      log.info("M=send, question={}, from user={}", question.getTitle(), question.getUser().getLogin());
      final var bodyEmail = String.format(
              "Hi %s, \n" +
              "\t You received a new question: \n" +
              "Title: %s \n" +
              "Product: %s \n" +
              "Url: http://my-url-ml/produtcs/%d \n" +
              "From: %s"
              , product.getUser().getLogin()
              , question.getTitle()
              , product.getName()
              , product.getId()
              , question.getUser().getLogin());
      log.info("M=send, email body={}", bodyEmail);
    }

    public void send(final Purchase purchase) {
        log.info("M=send, purchase={} product={}, from user={}", purchase.getId(), purchase.getProduct().getId(),
                purchase.getProduct().getUser().getLogin());
        final var bodyEmail = String.format(
                "Hi %s, \n" +
                        "\t You received a new purchase by product: \n" +
                        "Purchase: %s \n" +
                        "Quantity: %s \n" +
                        "Product: %s \n" +
                        "From: %s"
                , purchase.getProduct().getUser().getLogin()
                , purchase.getId()
                , purchase.getQuantity()
                , purchase.getProduct().getName()
                , purchase.getBuyer().getLogin());
        log.info("M=send, email body={}", bodyEmail);
    }

    public void send(final Payment payment) {
        log.info("M=send, email payment={}", payment);
        final var bodyEmail = payment.getStatus().isPay() ?
                paymentSuccessBody(payment) :
                paymentErrorBody(payment);
        log.info("M=send, email body={}", bodyEmail);
    }

    private String paymentErrorBody(final Payment payment) {
        return String.format(
                "Hi %s, \n" +
                        "\t Your payment failed, please try again: \n" +
                        "Payment: %s \n" +
                        "Purchase: %s \n" +
                        "Quantity: %s \n" +
                        "Product: %s \n" +
                        "link: http://my-url-ml/produtcs/%s"
                , payment.getPurchase().getBuyer().getLogin()
                , payment.getCode()
                , payment.getPurchase().getId()
                , payment.getPurchase().getQuantity()
                , payment.getPurchase().getProduct().getName()
                , payment.getPurchase().getProduct().getId());
    }

    private String paymentSuccessBody(final Payment payment) {
        return String.format(
                "Hi %s, \n" +
                        "\t Your payment was successful: \n" +
                        "Payment: %s \n" +
                        "Purchase: %s \n" +
                        "Quantity: %s \n" +
                        "Product: %s"
                , payment.getPurchase().getBuyer().getLogin()
                , payment.getCode()
                , payment.getPurchase().getId()
                , payment.getPurchase().getQuantity()
                , payment.getPurchase().getProduct().getName());
    }
}
