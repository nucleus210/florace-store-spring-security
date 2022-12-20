package com.nucleus.floracestore.model.view;

import lombok.Data;

@Data
public class AnswerViewModel {
    private Long answerId;
    private String answer;
    private String username;
    private QuestionViewModel question;
}
