package br.com.vfs.api.ml.product;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = "key")
public class NewFeature {
    @NotBlank
    private String name;
    @NotBlank
    private String value;

    public Feature toModel(){
        return new Feature(name, value);
    }
}
