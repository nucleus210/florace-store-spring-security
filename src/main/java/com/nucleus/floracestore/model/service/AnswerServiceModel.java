package com.nucleus.floracestore.model.service;

import lombok.Data;

@Data
public class AnswerServiceModel {
    private Long answerId;
    private String answer;
    private UserServiceModel user;
    private Long questionId;
}
