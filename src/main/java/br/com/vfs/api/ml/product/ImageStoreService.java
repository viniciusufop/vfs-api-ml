package br.com.vfs.api.ml.product;

import org.springframework.web.multipart.MultipartFile;

public interface ImageStoreService {

    String upload(final MultipartFile file);
}
