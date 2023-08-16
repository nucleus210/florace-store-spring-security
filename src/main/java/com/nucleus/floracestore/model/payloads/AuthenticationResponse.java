package com.nucleus.floracestore.model.payloads;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

    @JsonProperty("access_token")
    private String accessToken;
    private String tokenType = "Bearer";
    @JsonProperty("refresh_token")
    private String refreshToken;

    public AuthenticationResponse(String token) {
        this.accessToken = token;
    }
}