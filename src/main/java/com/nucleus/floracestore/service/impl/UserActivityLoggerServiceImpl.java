package com.nucleus.floracestore.service.impl;

import com.nucleus.floracestore.error.QueryRuntimeException;
import com.nucleus.floracestore.model.entity.UserActivityLogger;
import com.nucleus.floracestore.model.service.UserActivityLoggerServiceModel;
import com.nucleus.floracestore.repository.UserActivityLoggerRepository;
import com.nucleus.floracestore.service.UserActivityLoggerService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserActivityLoggerServiceImpl implements UserActivityLoggerService {
    private final ModelMapper modelMapper;
    private final UserActivityLoggerRepository userActivityLoggerRepository;

    @Autowired
    public UserActivityLoggerServiceImpl(ModelMapper modelMapper, UserActivityLoggerRepository userActivityLoggerRepository) {
        this.modelMapper = modelMapper;
        this.userActivityLoggerRepository = userActivityLoggerRepository;
    }

    @Override
    public UserActivityLoggerServiceModel getUserActivityLoggerById(Long UserActivityLoggerId) {
        return userActivityLoggerRepository.findById(UserActivityLoggerId).map(this::mapToService)
                .orElseThrow(() -> new QueryRuntimeException("Could not find UserActivityLogger with id " + UserActivityLoggerId));
    }

    @Override
    public UserActivityLoggerServiceModel createUserActivityLogger(UserActivityLoggerServiceModel userActivityLoggerServiceModel) {
        UserActivityLogger userActivityLogger = userActivityLoggerRepository
                .save(modelMapper.map(userActivityLoggerServiceModel, UserActivityLogger.class));
        return mapToService(userActivityLogger);
    }

    @Override
    public UserActivityLoggerServiceModel deleteUserActivityLoggerById(Long userLoggerId) {
        UserActivityLogger UserActivityLogger = userActivityLoggerRepository.findById(userLoggerId)
                .orElseThrow(() -> new QueryRuntimeException("Could not find UserActivityLogger with Id " + userLoggerId));
        userActivityLoggerRepository.delete(UserActivityLogger);
        return mapToService(UserActivityLogger);
    }

    @Override
    public List<UserActivityLoggerServiceModel> getAllActivityLogs() {
        return userActivityLoggerRepository
                .findAll()
                .stream()
                .map(this::mapToService)
                .collect(Collectors.toList());
    }

    private UserActivityLoggerServiceModel mapToService(UserActivityLogger UserActivityLogger) {
        return this.modelMapper.map(UserActivityLogger, UserActivityLoggerServiceModel.class);
    }
}

