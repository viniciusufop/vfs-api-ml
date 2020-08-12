package br.com.vfs.api.ml.shared.integration;

import br.com.vfs.api.ml.user.User;

public interface EmailNotifyService {

    void send(final String body, final User userSend);
}
