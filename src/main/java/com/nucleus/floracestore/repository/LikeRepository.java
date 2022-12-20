package com.nucleus.floracestore.repository;

import com.nucleus.floracestore.model.entity.LikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("likes-repository")
public interface LikeRepository extends JpaRepository<LikeEntity, Long> {
    @Query("select l from LikeEntity l JOIN FETCH l.user u where u.username = :username")
    List<LikeEntity> findAllByUsername(String username);
    @Query("select l from LikeEntity l JOIN FETCH l.question q where q.questionId = :questionId")
    List<LikeEntity> findAllByQuestionId(Long questionId);
    @Query("select l from LikeEntity l JOIN l.user u JOIN l.question q where u.username = :username and q.questionId = :questionId")
    Optional<LikeEntity> findByUsernameAndQuestionId(String username, Long questionId);
}
