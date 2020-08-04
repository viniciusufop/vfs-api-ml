package br.com.vfs.api.ml.product.detail;

import br.com.vfs.api.ml.question.Question;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class QuestionResponse implements Serializable {

    private final String title;
    private final LocalDateTime createAt;
    private final String user;

    public QuestionResponse(final Question question) {
        this.title = question.getTitle();
        this.createAt = question.getCreateAt();
        this.user = question.getUser().getLogin();
    }
}
