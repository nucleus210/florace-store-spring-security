package com.nucleus.floracestore.service;

import com.nucleus.floracestore.model.service.UserActivityLoggerServiceModel;

import java.util.List;

public interface UserActivityLoggerService {
    UserActivityLoggerServiceModel getUserActivityLoggerById(Long UserActivityLoggerId);
    UserActivityLoggerServiceModel createUserActivityLogger(UserActivityLoggerServiceModel userActivityLoggerServiceModel);
    UserActivityLoggerServiceModel deleteUserActivityLoggerById(Long userLoggerId);
    List<UserActivityLoggerServiceModel> getAllActivityLogs();
}
