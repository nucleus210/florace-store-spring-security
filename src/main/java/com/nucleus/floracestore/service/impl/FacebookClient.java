package com.nucleus.floracestore.service.impl;

import com.nucleus.floracestore.model.facebook.FacebookUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class FacebookClient {

    private final RestTemplate restTemplate;

    @Autowired
    public FacebookClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public FacebookUser getUser(String accessToken) {
        var path = "/me?fields={fields}&redirect={redirect}&access_token={access_token}";
        var fields = "email,first_name,last_name,id,picture.width(720).height(720)";
        final Map<String, String> variables = new HashMap<>();
        variables.put("fields", fields);
        variables.put("redirect", "false");
        variables.put("access_token", accessToken);
        String FACEBOOK_GRAPH_API_BASE = "https://graph.facebook.com";
        return restTemplate
                .getForObject(FACEBOOK_GRAPH_API_BASE + path, FacebookUser.class, variables);
    }
}