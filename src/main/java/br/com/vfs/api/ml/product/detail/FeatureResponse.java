package br.com.vfs.api.ml.product.detail;

import br.com.vfs.api.ml.product.Feature;
import lombok.Data;

import java.io.Serializable;

@Data
public class FeatureResponse implements Serializable {
    private final String name;
    private final String value;

    public FeatureResponse(final Feature feature) {
        this.name = feature.getName();
        this.value = feature.getValue();
    }
}
