package com.nucleus.floracestore.model.payloads;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserSummary {

    private Long id;
    private String username;
    private String name;
    //private String profilePicture;
}
