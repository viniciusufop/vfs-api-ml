package br.com.vfs.api.ml.product.detail;


import br.com.vfs.api.ml.opinion.Opinion;
import br.com.vfs.api.ml.product.Product;
import br.com.vfs.api.ml.question.Question;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@ToString
public class ProductDetailResponse implements Serializable {

    private final Long id;
    private final String name;
    private final String description;
    private final BigDecimal price;
    private final List<FeatureResponse> features;
    private Set<String> images;
    private List<QuestionResponse> questions;
    private List<OpinionResponse> opinions;
    private Integer averageOpinion;
    private Integer totalOpinion = 0;

    public ProductDetailResponse(final Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.features = product.getFeatures().stream().map(FeatureResponse::new).collect(Collectors.toList());
    }

    public void setImages(final Collection<String> images) {
        Assert.isTrue(CollectionUtils.isNotEmpty(images), "images not empty value");
        this.images = new HashSet<>(images);
    }

    public void setQuestions(final Collection<Question> questions) {
        Assert.isTrue(CollectionUtils.isNotEmpty(questions), "questions not empty value");
        this.questions = questions.stream().map(QuestionResponse::new).collect(Collectors.toList());
    }

    public void setOpinions(final Collection<Opinion> opinions) {
        Assert.isTrue(CollectionUtils.isNotEmpty(opinions), "opinions not empty value");
        this.opinions = opinions.stream().map(OpinionResponse::new).collect(Collectors.toList());
        this.totalOpinion = this.opinions.size();
        this.averageOpinion = this.opinions.stream().map(OpinionResponse::getEvaluation).reduce(Integer::sum).orElse(0) / totalOpinion;
    }
}