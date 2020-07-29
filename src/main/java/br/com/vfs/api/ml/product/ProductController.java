package br.com.vfs.api.ml.product;

import br.com.vfs.api.ml.category.CategoryRepository;
import br.com.vfs.api.ml.shared.security.UserLogged;
import br.com.vfs.api.ml.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import javax.validation.Valid;

import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/product")
public class ProductController {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    @ResponseStatus(OK)
    @PostMapping
    @Transactional
    public void create(@RequestBody @Valid final NewProduct newProduct,
                       @AuthenticationPrincipal final UserLogged userLogged){
        log.info("M=create, newProduct={}", newProduct);
        final var product = newProduct.toModel(userLogged, categoryRepository, userRepository);
        productRepository.save(product);
        log.info("M=create, product={}", product);
    }
}
