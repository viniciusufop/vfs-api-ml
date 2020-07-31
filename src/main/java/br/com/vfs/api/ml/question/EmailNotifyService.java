package br.com.vfs.api.ml.question;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailNotifyService {
    public void send(final Question question){
      log.info("M=send, question={}, from user={}", question.getTitle(), question.getUser().getLogin());
      final var bodyEmail = String.format(
              "Hi %s, \n" +
              "\t You received a new question: \n" +
              "Title: %s \n" +
              "Product: %s \n" +
              "Url: http://my-url-ml/produtcs/%d \n" +
              "From: %s"
              , question.getProduct().getUser().getLogin()
              , question.getTitle()
              , question.getProduct().getName()
              , question.getProduct().getId()
              , question.getUser().getLogin());
      log.info("M=send, email body={}", bodyEmail);
    }
}
