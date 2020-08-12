package br.com.vfs.api.ml.mocksystem;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RestController
public class OtherSystemController {

    @PostMapping
    @ResponseStatus(OK)
    @RequestMapping("/mock/invoice")
    public void invoice(@RequestBody @Valid final InvoiceRequest request) throws InterruptedException {
      log.info("M=invoice, check invoice from purchase={} and buyer={}"
              , request.getIdPurchase()
              , request.getIdBuyer());
      Thread.sleep(150);
    }

    @PostMapping
    @ResponseStatus(OK)
    @RequestMapping("/mock/ranking-vendor")
    public void rankingVendor(@RequestBody @Valid final RankingVendorRequest request) throws InterruptedException {
        log.info("M=rankingVendor, check ranking vendor from purchase={} and vendor={}"
                , request.getIdPurchase()
                , request.getIdVendor());
        Thread.sleep(150);
    }
}
