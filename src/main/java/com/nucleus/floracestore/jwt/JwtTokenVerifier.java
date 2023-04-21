package com.nucleus.floracestore.jwt;

import com.google.common.base.Strings;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class JwtTokenVerifier extends OncePerRequestFilter {

    private final SecretKey secretKey;
    private final JwtConfiguration jwtConfiguration;

    @Autowired
    public JwtTokenVerifier(SecretKey secretKey, JwtConfiguration jwtConfiguration) {
        this.secretKey = secretKey;
        this.jwtConfiguration = jwtConfiguration;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authorizationHeader = request.getHeader("Authorization");

        if (Strings.isNullOrEmpty(authorizationHeader) || !authorizationHeader.startsWith(jwtConfiguration.getTokenPrefix())) {
            filterChain.doFilter(request, response);
            return;
        }

        Jws<Claims> jws;
        String token = authorizationHeader.replace(jwtConfiguration.getTokenPrefix(), "");

        try {

            jws = Jwts.parserBuilder()  // (1)
                    .setSigningKey(secretKey)         // (2)
                    .build()                    // (3)
                    .parseClaimsJws(token); // (4)

            // we can safely trust the JWT

            Claims body = jws.getBody();
            String username = body.getSubject();
            var authorities = (List<Map<String, String>>) body.get("authorities");
            Set<SimpleGrantedAuthority> simpleGrantedAuthorities = authorities
                    .stream()
                    .map(auth -> new SimpleGrantedAuthority(auth.get("authority")))
                    .collect(Collectors.toSet());
            Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, simpleGrantedAuthorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (JwtException ex) {       // (5)
            throw new IllegalStateException(String.format("Token %s cannot be trusted", token));
            // we *cannot* use the JWT as intended by its creator
        }
        filterChain.doFilter(request, response);
    }
}
