package com.nucleus.floracestore.model.view;

import lombok.Data;

import java.util.Set;

@Data
public class QuestionViewModel {
    private Long questionId;
    private String question;
    private Long productId;
    Set<AnswerViewModel> answers;
}
