package br.com.vfs.api.ml.user;

import br.com.vfs.api.ml.shared.annotations.UniqueValue;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class NewUser {
    @NotBlank
    @Email
    @UniqueValue(fieldName = "login", domainClass = User.class)
    private String login;
    @NotBlank
    @Size(min = 6)
    private String password;

    public User toModel(){
        return new User(login, password);
    }
}
