package br.com.vfs.api.ml.category;

import br.com.vfs.api.ml.shared.security.UserLogged;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;

import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/category")
public class CategoryController {

    private final CategoryRepository categoryRepository;
    @ResponseStatus(OK)
    @PostMapping
    @Transactional
    public void create(@RequestBody @Valid final NewCategory newCategory,
                       @AuthenticationPrincipal final UserLogged userLogged){
        log.info("M=create, newCategory={}", newCategory);
        final var category = newCategory.toModel(categoryRepository);
        categoryRepository.save(category);
        log.info("M=create, category={}", category);
    }
}
