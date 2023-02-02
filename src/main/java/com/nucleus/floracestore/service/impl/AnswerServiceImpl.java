package com.nucleus.floracestore.service.impl;

import com.nucleus.floracestore.error.QueryRuntimeException;
import com.nucleus.floracestore.model.entity.AnswerEntity;
import com.nucleus.floracestore.model.entity.QuestionEntity;
import com.nucleus.floracestore.model.entity.UserEntity;
import com.nucleus.floracestore.model.service.AnswerServiceModel;
import com.nucleus.floracestore.repository.AnswerRepository;
import com.nucleus.floracestore.service.AnswerService;
import com.nucleus.floracestore.service.QuestionService;
import com.nucleus.floracestore.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AnswerServiceImpl implements AnswerService {
    @Autowired

    private final ModelMapper modelMapper;
    private final AnswerRepository answerRepository;
    private final QuestionService questionService;
    private final UserService userService;

    @Autowired
    public AnswerServiceImpl(ModelMapper modelMapper,
                             AnswerRepository answerRepository,
                             QuestionService questionService,
                             UserService userService) {
        this.modelMapper = modelMapper;
        this.answerRepository = answerRepository;
        this.questionService = questionService;
        this.userService = userService;
    }

    @Override
    public AnswerServiceModel getAnswerById(Long answerId) {
        log.info("answerService: " + answerId);
        return answerRepository.findById(answerId).map(this::mapToService)
                .orElseThrow(() -> new QueryRuntimeException("Could not find answer with id " + answerId));
    }

    @Override
    public List<AnswerServiceModel> getAllAnswersByQuestionId(Long questionId) {
        return answerRepository.findAllByQuestionId(questionId)
                .stream()
                .map(this::mapToService)
                .collect(Collectors.toList());
    }

    @Override
    public List<AnswerServiceModel> getAllAnswersByUsername(String username) {
        return answerRepository.finAllByUsername(username)
                .stream()
                .map(this::mapToService)
                .collect(Collectors.toList());
    }

    @Override
    public AnswerServiceModel createAnswer(AnswerServiceModel answerServiceModel, Long questionId, String username) {
        UserEntity user= modelMapper.map(userService.findByUsername(username), UserEntity.class);
        QuestionEntity question = modelMapper.map(questionService.getQuestionById(questionId), QuestionEntity.class);
        AnswerEntity answer = modelMapper.map(answerServiceModel, AnswerEntity.class);
        answer.setQuestion(question);
        answer.setUser(user);
        return mapToService(answerRepository.save(answer));
    }

    @Override
    public AnswerServiceModel updateAnswer(AnswerServiceModel answerServiceModel, Long answerId) {
        AnswerEntity answerEntity = answerRepository.findById(answerId)
                .orElseThrow(() -> new QueryRuntimeException("Could not find answer with Id " + answerId));
        answerEntity.setAnswer(answerServiceModel.getAnswer());
        return mapToService(answerRepository.save(answerEntity));
    }

    @Override
    public AnswerServiceModel deleteAnswerById(Long answerId) {
        AnswerEntity answerEntity = answerRepository.findById(answerId)
                .orElseThrow(() -> new QueryRuntimeException("Could not find answer with Id " + answerId));
        answerRepository.delete(answerEntity);
        return mapToService(answerEntity);
    }

    private AnswerServiceModel mapToService(AnswerEntity answerEntity) {
        return this.modelMapper.map(answerEntity, AnswerServiceModel.class);
    }
}
