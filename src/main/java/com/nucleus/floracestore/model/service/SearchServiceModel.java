package com.nucleus.floracestore.model.service;

import lombok.Data;

@Data
public class SearchServiceModel {
    private Long searchId;
    private String name;
    private String alias;
    private String shortDescription;
    private String fullDescription;
}
