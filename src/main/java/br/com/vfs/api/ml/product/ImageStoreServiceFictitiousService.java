package br.com.vfs.api.ml.product;

import org.springframework.web.multipart.MultipartFile;

public interface ImageStoreServiceFictitious {

    String upload(final MultipartFile file);
}
