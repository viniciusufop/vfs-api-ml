package br.com.vfs.api.ml.question;

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
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/question")
public class QuestionController {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final EmailNotifyService emailNotifyService;
    
    @ResponseStatus(OK)
    @PostMapping
    @Transactional
    public List<QuestionResponse> create(@RequestBody @Valid final NewQuestion newQuestion,
                                 @AuthenticationPrincipal final UserLogged userLogged) {
        log.info("M=create, newQuestion={}, user={}", newQuestion, userLogged.getUsername());
        final var product = newQuestion.toProduct(productRepository);
        product.addQuestion(newQuestion, userLogged, userRepository, emailNotifyService);
        return productRepository.save(product).getQuestions()
                .stream()
                .map(q -> new QuestionResponse(q, product))
                .collect(Collectors.toList());
    }
}
