package com.nucleus.floracestore.model.view;

import com.nucleus.floracestore.model.entity.AnswerEntity;
import com.nucleus.floracestore.model.entity.LikeEntity;
import lombok.Data;

import java.util.Set;

@Data
public class QuestionViewModel {
    private Long questionId;
    private String question;
    //    private UserEntity user;
    private ProductViewModel product;
    private Set<LikeEntity> likes;
    private Set<AnswerEntity> answers;
}
