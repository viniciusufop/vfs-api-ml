package br.com.vfs.api.ml.purchase;

import br.com.vfs.api.ml.product.ProductRepository;
import br.com.vfs.api.ml.shared.integration.EmailNotifyService;
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
        final var bodyEmail = String.format(
                "Hi %s, \n" +
                        "\t You received a new purchase by product: \n" +
                        "Purchase: %s \n" +
                        "Quantity: %s \n" +
                        "Product: %s \n" +
                        "From: %s"
                , purchase.getProduct().getUser().getLogin()
                , purchase.getId()
                , purchase.getQuantity()
                , purchase.getProduct().getName()
                , purchase.getBuyer().getLogin());
        emailNotifyService.send(bodyEmail, purchase.getProduct().getUser());
        return purchase.redirectURL();
    }
}
