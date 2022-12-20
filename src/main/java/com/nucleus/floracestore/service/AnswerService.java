package com.nucleus.floracestore.service;

import com.nucleus.floracestore.model.service.AnswerServiceModel;

import java.util.List;

public interface AnswerService {
    AnswerServiceModel getAnswerById(Long answerId);
    List<AnswerServiceModel> getAllAnswersByQuestionId(Long questionId);
    List<AnswerServiceModel> getAllAnswersByUsername(String username);

    AnswerServiceModel createAnswer(AnswerServiceModel answerServiceModel, Long questionId,String username);

    AnswerServiceModel updateAnswer(AnswerServiceModel answerServiceModel, Long answerId);

    AnswerServiceModel deleteAnswerById(Long answerId);
}
