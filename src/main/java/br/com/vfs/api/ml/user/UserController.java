package br.com.vfs.api.ml.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import javax.validation.Valid;

import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/user")
public class UserController {

    private final UserRepository userRepository;

    @ResponseStatus(OK)
    @PostMapping
    @Transactional
    public void create(@RequestBody @Valid final NewUser newUser){
        log.info("M=create, newUser={}", newUser);
        final var user = newUser.toModel();
        userRepository.save(user);
        log.info("M=create, user={}", user);
    }
}
