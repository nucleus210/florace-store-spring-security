package com.nucleus.floracestore.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageProperties {
    private String location = "./resource-collector/";

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}