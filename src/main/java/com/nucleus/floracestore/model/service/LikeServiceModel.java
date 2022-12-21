package com.nucleus.floracestore.model.service;

import lombok.Data;

@Data
public class LikeServiceModel {
    private Long likeId;
    private UserServiceModel user;
    private QuestionServiceModel question;
}
