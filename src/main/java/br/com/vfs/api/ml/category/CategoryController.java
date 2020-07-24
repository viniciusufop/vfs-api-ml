package br.com.vfs.api.ml.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping("api/category")
public class CategoryController {

    private final CategoryRepository categoryRepository;
    @ResponseStatus(OK)
    @PostMapping
    @Transactional
    public void create(@RequestBody @Valid final NewCategory newCategory){
        log.info("M=create, newCategory={}", newCategory);
        final var category = newCategory.toModel(categoryRepository);
        categoryRepository.save(category);
        log.info("M=create, category={}", category);
    }
}
