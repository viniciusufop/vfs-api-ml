package br.com.vfs.api.ml.user;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@EntityListeners(AuditingEntityListener.class)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Email
    @Column(unique = true, nullable = false)
    private String login;
    @NotBlank
    @Size(min = 6)
    @Column(nullable = false)
    private String password;
    @CreatedDate
    @PastOrPresent
    private LocalDateTime createAt;

    public User(@NotBlank @Email String login, @NotBlank @Size(min = 6) String password) {
        this.login = login;
        this.password = new BCryptPasswordEncoder().encode(password);
    }

    public boolean isUser(final String username) {
        return Objects.nonNull(username) && login.equals(username);
    }
}
