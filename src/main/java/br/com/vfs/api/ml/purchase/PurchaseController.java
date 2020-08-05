package br.com.vfs.api.ml.purchase;

import br.com.vfs.api.ml.product.ProductRepository;
import br.com.vfs.api.ml.question.EmailNotifyService;
import br.com.vfs.api.ml.shared.security.UserLogged;
import br.com.vfs.api.ml.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import javax.validation.Valid;

import static org.springframework.http.HttpStatus.FOUND;

@Slf4j
@RestController
@RequestMapping("/api/purchase")
@RequiredArgsConstructor
public class PurchaseController {

    private static final String URL_REDIRECT_CONFIRM = "http:/localhost:8080/api/payment";

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final PurchaseRepository purchaseRepository;
    private final EmailNotifyService emailNotifyService;

    @InitBinder("newPurchase")
    public void init(final WebDataBinder dataBinder){
        dataBinder.addValidators(new QuantityByProductValidator(productRepository));
    }

    @ResponseStatus(FOUND)
    @PostMapping
    @Transactional
    public String create(@RequestBody @Valid final NewPurchase newPurchase,
                       @AuthenticationPrincipal final UserLogged userLogged){
        log.info("M=create, newPurchase={} userLogged={}", newPurchase, userLogged.getUsername());
        final var purchase = newPurchase.toModel(userLogged, userRepository, productRepository);
        purchaseRepository.save(purchase);
        emailNotifyService.send(purchase);
        return purchase.redirectURL(URL_REDIRECT_CONFIRM);
    }
}
