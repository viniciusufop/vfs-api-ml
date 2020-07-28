package br.com.vfs.api.ml.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"name","value"})
public class Feature implements Serializable {
    private String name;
    private String value;
}
