package br.com.vfs.api.ml.category;

import br.com.vfs.api.ml.shared.annotations.UniqueValue;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

@Data
@NoArgsConstructor
public class NewCategory {

    @NotBlank
    @UniqueValue(fieldName = "name", domainClass = Category.class)
    private String name;

    private Long idCategoryParent;

    public Category toModel(final CategoryRepository categoryRepository) {
        final var category = new Category(name);
        if(Objects.nonNull(idCategoryParent)){
            final var categoryParent = categoryRepository.findById(idCategoryParent)
                    .orElseThrow(() -> new IllegalArgumentException("not existing category"));
            category.setCategory(categoryParent);
        }
        return category;
    }
}
