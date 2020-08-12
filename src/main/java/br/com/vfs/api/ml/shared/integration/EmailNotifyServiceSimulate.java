package br.com.vfs.api.ml.shared.integration;

import br.com.vfs.api.ml.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Profile("!prod")
/**
 * Para ter um notificador real de email basta criar outra classe que vai subir no profile de prod
 */
public class EmailNotifyServiceSimulate implements EmailNotifyService{
    public void send(final String body, final User userSend){
        log.info("M=send, body={}, from userSend={}", body, userSend.getLogin());
    }
}
