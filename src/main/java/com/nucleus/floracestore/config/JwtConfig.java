package com.nucleus.floracestore.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@Component
public class JwtConfig {

    @Value("${application.security.jwt.uri}")
    private String Uri;

    @Value("${application.security.jwt.header}")
    private String header;

    @Value("${application.security.jwt.tokenPrefix}")
    private String prefix;

    @Value("${application.security.jwt.expiration}")
    private int expiration;

    @Value("${application.security.jwt.refreshTokenExpiration}")
    private int refreshExpiration;

    @Value("${application.security.jwt.secretKey}")
    private String secretKey;
}