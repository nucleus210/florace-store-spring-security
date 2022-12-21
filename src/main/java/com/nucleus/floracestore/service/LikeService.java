package com.nucleus.floracestore.service;


import com.nucleus.floracestore.model.service.LikeServiceModel;

import java.util.List;

public interface LikeService {
    LikeServiceModel createLike(Long questionId, String username);

    LikeServiceModel getLikeById(Long likeId);

    List<LikeServiceModel> getLikesByQuestionId(Long questionId);

    List<LikeServiceModel> getLikesByUsername(String username);

    LikeServiceModel deleteLikeById(Long likeId);

}
