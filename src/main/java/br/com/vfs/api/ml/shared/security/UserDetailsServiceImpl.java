package br.com.vfs.api.ml.shared.security;

import br.com.vfs.api.ml.user.User;
import br.com.vfs.api.ml.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByLogin(username)
                .map(this::toUserDetails)
                .orElse(null);
    }

    private UserLogged toUserDetails(final User user) {
        return new UserLogged(user);
    }
}

