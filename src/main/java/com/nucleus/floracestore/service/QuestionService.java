package com.nucleus.floracestore.service;

import com.nucleus.floracestore.model.service.QuestionServiceModel;

import java.util.List;

public interface QuestionService {
    QuestionServiceModel getQuestionById(Long questionId);

    List<QuestionServiceModel> getAllQuestionsByProductId(Long productId);

    List<QuestionServiceModel> getAllQuestionByUsername(String username);

    QuestionServiceModel createQuestion(QuestionServiceModel questionServiceModel, Long productId, String username);

    QuestionServiceModel updateQuestion(QuestionServiceModel questionServiceModel, Long questionId);

    QuestionServiceModel deleteQuestionById(Long questionId);
}
