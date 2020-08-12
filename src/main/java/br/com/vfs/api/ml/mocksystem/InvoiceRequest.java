package br.com.vfs.api.ml.mocksystem;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class InvoiceRequest {
    @NotNull
    private Long idBuyer;
    @NotNull
    private Long idPurchase;
}
