package com.nucleus.floracestore.monitoring;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Endpoint(id="webTracesId")
public class WebTracesEndpoint {

    @ReadOperation
    public Map<String, String> webTracesEndpoints () {
        Map<String, String> map = new HashMap<String, String>();
        map.put("Key", "Value");
        return map;
    }
}
