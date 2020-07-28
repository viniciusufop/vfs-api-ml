package br.com.vfs.api.ml.product;

import br.com.vfs.api.ml.category.Category;
import br.com.vfs.api.ml.category.CategoryRepository;
import br.com.vfs.api.ml.shared.annotations.ExistElement;
import br.com.vfs.api.ml.shared.security.UserLogged;
import br.com.vfs.api.ml.user.UserRepository;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class NewProduct {
    @NotBlank
    private String name;
    @NotNull
    @Size(max = 1000)
    private String description;
    @NotNull
    @Size(min = 3)
    private List<NewFeature> features;
    @NotNull
    @Positive
    private BigDecimal price;
    @NotNull
    @Positive
    private Integer quantity;
    @NotNull
    @ExistElement(domainClass = Category.class)
    private Long idCategory;

    public Product toModel(final UserLogged userLogged, final CategoryRepository categoryRepository,
                           final UserRepository userRepository){
        final var finalFeatures = features.stream()
                .map(NewFeature::toModel)
                .collect(Collectors.toList());
        final var category = categoryRepository.findById(idCategory)
                .orElseThrow(() -> new IllegalArgumentException("invalid category"));
        final var user = userRepository.findByLogin(userLogged.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("invalid user"));
        return new Product(name, description, finalFeatures, price, quantity, category, user);
    }
}
