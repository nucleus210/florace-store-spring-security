package com.nucleus.floracestore.model.service;

import lombok.Data;


@Data
public class UserActivityLoggerServiceModel {
    private Long userLogId;
    private String remoteUserName;
    private String requestUrl;
    private String protocol;
}
