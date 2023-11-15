package com.example.RideShare.jwt;

import com.example.RideShare.controller.exceptions.UserNoLongerExistsOnAuthException;
import com.example.RideShare.model.repository.UserRepository;
import com.google.common.base.Strings;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class JwTokenVerifier extends OncePerRequestFilter {
    private final UserRepository repository;

    private final SecretKey secretKey;
    private final JwtConfig jwtConfig;

    public JwTokenVerifier(UserRepository repository, SecretKey secretKey, JwtConfig jwtConfig) {
        this.repository = repository;
        this.secretKey = secretKey;
        this.jwtConfig = jwtConfig;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader(jwtConfig.getAuthorizationHeader());

        if(Strings.isNullOrEmpty(authorizationHeader) || !authorizationHeader.startsWith(jwtConfig.getJwtProperties().getTokenPrefix())) {
            //rejects request when token cannot be found incoming request header
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorizationHeader.replace(jwtConfig.getJwtProperties().getTokenPrefix(), "");

        try {
            //decrypt our jwt token into json
            Jws<Claims> claimsJws = Jwts.parser()
                    .setSigningKey(secretKey).build()
                    .parseClaimsJws(token);

            Claims body = claimsJws.getBody();

            //email/username
            String username = body.getSubject();
            if (!repository.existsById(username))
                throw new UserNoLongerExistsOnAuthException(username);
            //authorities and permissions
            var authorization = (List<Map<String, String>>) body.get("authorities");

            Set<SimpleGrantedAuthority> simpleGrantedAuthority = authorization.stream()
                    .map(m->new SimpleGrantedAuthority(m.get("authority")))
                    .collect(Collectors.toSet());

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    username,
                null,
                    simpleGrantedAuthority

            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (JwtException e) {
            throw new IllegalStateException(String.format("Token %s cannot be trusted", token));
        }
        filterChain.doFilter(request, response);
    }
}
