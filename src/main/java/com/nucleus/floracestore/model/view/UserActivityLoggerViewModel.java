package com.nucleus.floracestore.model.view;

import lombok.Data;

@Data
public class UserActivityLoggerViewModel {
    private Long userLogId;
    private String remoteUserName;
    private String requestUrl;
    private String protocol;
}
