package br.com.vfs.api.ml.product;

import br.com.vfs.api.ml.shared.security.UserLogged;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/product/{id}/image")
public class ImageProductController {

    private final ProductRepository productRepository;
    private final ImageStoreService imageStoreService;
    @ResponseStatus(OK)
    @PostMapping
    @Transactional
    public void addFileInProduct(@PathVariable("id") @NotNull @Valid  final Long id,
                                 @RequestPart("file") @NotNull @Valid final MultipartFile file,
                       @AuthenticationPrincipal final UserLogged userLogged) throws IllegalAccessException {
        log.info("M=addFileInProduct, idProduct={}, fileName={}, user={}", id, file.getName(), userLogged.getUsername());
        final var product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        product.addImage(file, userLogged, imageStoreService);
    }
}
