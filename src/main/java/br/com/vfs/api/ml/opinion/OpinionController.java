package br.com.vfs.api.ml.opinion;

import br.com.vfs.api.ml.product.ProductRepository;
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
@RequestMapping("api/opinion")
public class OpinionController {
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OpinionRepository opinionRepository;
    @ResponseStatus(OK)
    @PostMapping
    @Transactional
    public void create(@RequestBody @Valid final NewOpinion newOpinion,
                       @AuthenticationPrincipal final UserLogged userLogged) {
        log.info("M=create, NewOpinion={}, user={}", newOpinion, userLogged.getUsername());
        final var opinion = newOpinion.toModel(userLogged, userRepository, productRepository);
        log.info("M=create, opinion={}, user={}", newOpinion, userLogged.getUsername());
        opinionRepository.save(opinion);
    }
}
