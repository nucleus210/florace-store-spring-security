package com.nucleus.floracestore.service.impl;

import com.nucleus.floracestore.error.QueryRuntimeException;
import com.nucleus.floracestore.model.entity.LikeEntity;
import com.nucleus.floracestore.model.service.LikeServiceModel;
import com.nucleus.floracestore.model.service.QuestionServiceModel;
import com.nucleus.floracestore.model.service.UserServiceModel;
import com.nucleus.floracestore.repository.LikeRepository;
import com.nucleus.floracestore.service.LikeService;
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
public class LikeServiceImpl implements LikeService {

    private final ModelMapper modelMapper;
    private final UserService userService;
    private final QuestionService questionService;
    private final LikeRepository likeRepository;

    @Autowired
    public LikeServiceImpl(ModelMapper modelMapper,
                           UserService userService,
                           QuestionService questionService,
                           LikeRepository likeRepository) {
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.questionService = questionService;
        this.likeRepository = likeRepository;
    }


    @Override
    public LikeServiceModel createLike(Long questionId, String username) {
        UserServiceModel user = userService.findByUsername(username)
                .orElseThrow(() -> new QueryRuntimeException("Could not find username " + username));
        QuestionServiceModel questionServiceModel = questionService.getQuestionById(questionId);
        LikeServiceModel likeServiceModel = new LikeServiceModel();
        likeServiceModel.setUser(user);
        likeServiceModel.setQuestion(questionServiceModel);
        likeRepository.save(modelMapper.map(likeServiceModel, LikeEntity.class));
        return mapToService(likeRepository
                .findByUsernameAndQuestionId(username, likeServiceModel.getQuestion().getQuestionId())
                .orElseThrow(() ->
                        new QueryRuntimeException(String
                                .format("Could not find like for user %s and question %d",
                                        username,
                                        likeServiceModel.getQuestion().getQuestionId()))));
    }

    @Override
    public LikeServiceModel getLikeById(Long likeId) {
        log.info("likeService: " + likeId);
        return likeRepository.findById(likeId).map(this::mapToService)
                .orElseThrow(() -> new QueryRuntimeException("Could not find like with id " + likeId));
    }

    @Override
    public List<LikeServiceModel> getLikesByQuestionId(Long questionId) {
        return likeRepository.findAllByQuestionId(questionId)
                .stream()
                .map(this::mapToService)
                .collect(Collectors.toList());
    }

    @Override
    public List<LikeServiceModel> getLikesByUsername(String username) {
        return likeRepository.findAllByUsername(username)
                .stream()
                .map(this::mapToService)
                .collect(Collectors.toList());
    }

    @Override
    public LikeServiceModel deleteLikeById(Long likeId) {
        LikeEntity likeEntity = likeRepository.findById(likeId)
                .orElseThrow(() -> new QueryRuntimeException("Could not find like with id " + likeId));
        likeRepository.delete(likeEntity);
        return mapToService(likeEntity);
    }

    private LikeServiceModel mapToService(LikeEntity likeEntity) {
        return this.modelMapper.map(likeEntity, LikeServiceModel.class);
    }

}
