package br.com.vfs.api.ml.product.detail;

import br.com.vfs.api.ml.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import java.util.Collection;

import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/product")
public class ProductDetailController {

    private final ProductRepository productRepository;

    @ResponseStatus(OK)
    @GetMapping("/{id}")
    public ProductDetailResponse findById(@PathVariable("id") @NotNull @Valid final Long id){
        log.info("M=findById, id={}", id);
        final var product = productRepository.findById(id).orElseThrow();
        final var productDetail = new ProductDetailResponse(product);

        setCollectionAttribute(product.getImages(), productDetail::setImages);
        setCollectionAttribute(product.getQuestions(), productDetail::setQuestions);
        setCollectionAttribute(product.getOpinions(), productDetail::setOpinions);

        log.info("M=findById, productDetail={}", productDetail);
        return productDetail;
    }

    private <T> void setCollectionAttribute(final Collection<T> list, final SetCollection<T> setter) {
        if(CollectionUtils.isNotEmpty(list)) setter.set(list);
    }
}

@FunctionalInterface
interface SetCollection<T extends Object>{
    void set(final Collection<T> list);
}
