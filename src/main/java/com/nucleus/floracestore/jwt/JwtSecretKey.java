package com.nucleus.floracestore.jwt;

import com.nucleus.floracestore.config.JwtConfig;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;

@Configuration
public class JwtSecretKey {

    private final JwtConfig jwtConfiguration;

    @Autowired
    public JwtSecretKey(JwtConfig jwtConfiguration) {
        this.jwtConfiguration = jwtConfiguration;
    }

    @Bean
    public SecretKey secretKey() {
        return Keys.hmacShaKeyFor(jwtConfiguration.getSecretKey().getBytes());
    }
}
