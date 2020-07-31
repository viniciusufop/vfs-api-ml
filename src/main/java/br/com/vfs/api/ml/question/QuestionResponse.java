package br.com.vfs.api.ml.question;

import br.com.vfs.api.ml.product.Product;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class QuestionResponse {

    private String title;
    private LocalDateTime createAt;
    private Long idProduct;
    private String nameProduct;
    private String nameUser;

    public QuestionResponse(final Question question, final Product product){
        this.title = question.getTitle();
        this.createAt = question.getCreateAt();
        this.idProduct = product.getId();
        this.nameProduct = product.getName();
        this.nameUser = question.getUser().getLogin();
    }
}
