package com.nucleus.floracestore.repository;

import com.nucleus.floracestore.model.entity.AnswerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("answer-repository")
public interface AnswerRepository extends JpaRepository<AnswerEntity, Long> {

    @Query("select a from AnswerEntity a JOIN FETCH a.question q where q.questionId = :questionId")
    List<AnswerEntity> findAllByQuestionId(Long questionId);
    @Query("select a from AnswerEntity a JOIN FETCH a.user u where u.username = :username")
    List<AnswerEntity> finAllByUsername(String username);
}
