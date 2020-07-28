package br.com.vfs.api.ml.shared.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.experimental.UtilityClass;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@UtilityClass
public class TokenAuthenticationService {

    private final String SECRET = "MySecret";
    private final String TOKEN_PREFIX = "Bearer";
    private final String HEADER_STRING = "Authorization";

    void addAuthentication(final HttpServletResponse response, final String username) {
        final var expiration = LocalDateTime.now().plusMinutes(20);
        final var JWT = Jwts.builder()
                .setSubject(username)
                .setExpiration(Date.from(expiration.atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
        response.addHeader(HEADER_STRING, TOKEN_PREFIX + " " + JWT);
    }

    Authentication getAuthentication(final HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(HEADER_STRING))
                .map(TokenAuthenticationService::toUser)
                .map(TokenAuthenticationService::toUsernamePasswordAuthenticationToken)
                .orElse(null);
    }

    private UserLogged toUser(final String token){
        final var username = Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                .getBody()
                .getSubject();
        final var userLogged = new UserLogged();
        userLogged.setUsername(username);
        return userLogged;
    }

    private UsernamePasswordAuthenticationToken toUsernamePasswordAuthenticationToken(final UserLogged user){
        return new UsernamePasswordAuthenticationToken(user, null, List.of());
    }
}
