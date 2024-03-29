package com.nucleus.floracestore.model.service;

import lombok.Data;

import java.util.Set;

@Data
public class QuestionServiceModel {
    private Long questionId;
    private String question;
    private UserServiceModel user;
    private Long productId;
    Set<AnswerServiceModel> answers;

}
