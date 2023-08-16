package com.nucleus.floracestore.model.view;

import lombok.Data;

@Data
public class SearchViewModel {
    private Long searchId;
    private String name;
    private String alias;
    private String shortDescription;
    private String fullDescription;
}
