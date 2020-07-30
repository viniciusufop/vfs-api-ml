package br.com.vfs.api.ml.product;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
 * desafio extra: essa classe roda em todos os profiles menos prod.
 * Basta criar outra, com o @Profile("prod") que implementa o upload adequado para producao
 */
@Profile("!prod")
@Service
public class ImageStoreServiceFictitiousService implements ImageStoreService {
    private final static String BASE_URL = "http://www.link.fictitious.com?image=%s&uuid=%s";
    @Override
    public String upload(MultipartFile file) {
        final String uuid = UUID.randomUUID().toString()
                .replace("-", "")
                .toLowerCase();
        return String.format(BASE_URL,file.getOriginalFilename(),uuid);
    }
}
