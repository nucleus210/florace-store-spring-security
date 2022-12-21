package com.nucleus.floracestore.model.service;

import com.nucleus.floracestore.model.entity.QuestionEntity;
import lombok.Data;

@Data
public class AnswerServiceModel {
    private Long answerId;
    private String answer;
    private UserServiceModel user;
    private QuestionEntity question;
}
