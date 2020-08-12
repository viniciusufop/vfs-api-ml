package br.com.vfs.api.ml.question;

import br.com.vfs.api.ml.product.Product;
import br.com.vfs.api.ml.purchase.Purchase;
import br.com.vfs.api.ml.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailNotifyService {

    public void send(final String body, final User user){
        log.info("M=send, body={}, from user={}", body, user.getLogin());
    }

    @Deprecated
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
    @Deprecated
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
}
