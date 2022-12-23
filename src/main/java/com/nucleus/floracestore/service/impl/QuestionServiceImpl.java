package com.nucleus.floracestore.service.impl;

import com.nucleus.floracestore.error.QueryRuntimeException;
import com.nucleus.floracestore.model.entity.QuestionEntity;
import com.nucleus.floracestore.model.service.ProductServiceModel;
import com.nucleus.floracestore.model.service.QuestionServiceModel;
import com.nucleus.floracestore.model.service.UserServiceModel;
import com.nucleus.floracestore.repository.QuestionRepository;
import com.nucleus.floracestore.service.ProductService;
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
public class QuestionServiceImpl implements QuestionService {
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final ProductService productService;
    private final QuestionRepository questionRepository;

    @Autowired
    public QuestionServiceImpl(ModelMapper modelMapper,
                               UserService userService,
                               ProductService productService,
                               QuestionRepository questionRepository) {
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.productService = productService;
        this.questionRepository = questionRepository;
    }

    @Override
    public QuestionServiceModel getQuestionById(Long questionId) {
        log.info("answerService: " + questionId);
        return questionRepository.findById(questionId).map(this::mapToService)
                .orElseThrow(() -> new QueryRuntimeException("Could not find question with id " + questionId));
    }

    @Override
    public List<QuestionServiceModel> getAllQuestionByUsername(String username) {
        log.info("answerService: " + username);
        return questionRepository.findAllByUsername(username)
                .stream()
                .map(this::mapToService)
                .collect(Collectors.toList());
    }

    @Override
    public QuestionServiceModel createQuestion(QuestionServiceModel questionServiceModel, Long productId, String username) {
        UserServiceModel userServiceModel = userService.findByUsername(username);
        ProductServiceModel product = productService.getProductById(productId);
        questionServiceModel.setProduct(product);
        questionServiceModel.setUser(userServiceModel);
        QuestionEntity questionEntity = questionRepository.save(modelMapper.map(questionServiceModel, QuestionEntity.class));
        return mapToService(questionEntity);
    }

    @Override
    public QuestionServiceModel updateQuestion(QuestionServiceModel questionServiceModel, Long questionId) {
        QuestionEntity questionEntity = questionRepository.findById(questionId)
                .orElseThrow(() -> new QueryRuntimeException("Could not find question with Id " + questionId));
        questionEntity.setQuestion(questionServiceModel.getQuestion());
//        questionEntity.setLikes(questionServiceModel.getLikes());
        return mapToService(questionRepository.save(questionEntity));
    }

    @Override
    public QuestionServiceModel deleteQuestionById(Long questionId) {
        QuestionEntity questionEntity = questionRepository.findById(questionId)
                .orElseThrow(() -> new QueryRuntimeException("Could not find question with Id " + questionId));

        questionRepository.delete(questionEntity);
        return mapToService(questionEntity);
    }

    @Override
    public List<QuestionServiceModel> getAllQuestionsByProductId(Long productId) {
        return questionRepository.findAllByProductId(productId)
                .stream()
                .map(this::mapToService)
                .collect(Collectors.toList());
    }

    private QuestionServiceModel mapToService(QuestionEntity questionEntity) {
        return this.modelMapper.map(questionEntity, QuestionServiceModel.class);
    }
}
