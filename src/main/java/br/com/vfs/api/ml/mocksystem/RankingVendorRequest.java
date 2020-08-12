package br.com.vfs.api.ml.mocksystem;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class RankingVendorRequest {
    @NotNull
    private Long idVendor;
    @NotNull
    private Long idPurchase;
}
