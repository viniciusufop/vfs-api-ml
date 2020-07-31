package br.com.vfs.api.ml.question;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class QuestionResponse {

    private String title;
    private LocalDateTime createAt;
    private Long idProduct;
    private String nameProduct;
    private String nameUser;

    public QuestionResponse(final Question question){
        this.title = question.getTitle();
        this.createAt = question.getCreateAt();
        this.idProduct = question.getProduct().getId();
        this.nameProduct = question.getProduct().getName();
        this.nameUser = question.getUser().getLogin();
    }
}
