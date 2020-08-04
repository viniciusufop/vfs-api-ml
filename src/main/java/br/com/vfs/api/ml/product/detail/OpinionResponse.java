package br.com.vfs.api.ml.product.detail;

import br.com.vfs.api.ml.opinion.Opinion;
import lombok.Data;

import java.io.Serializable;

@Data
public class OpinionResponse implements Serializable {

    private final String title;
    private final String description;
    private final Integer evaluation;
    private final String user;

    public OpinionResponse(final Opinion opinion) {
        this.title = opinion.getTitle();
        this.description = opinion.getDescription();
        this.evaluation = opinion.getEvaluation();
        this.user = opinion.getUser().getLogin();
    }
}
