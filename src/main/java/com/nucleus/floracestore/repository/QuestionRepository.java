package com.nucleus.floracestore.repository;

import com.nucleus.floracestore.model.entity.QuestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("questions-repository")
public interface QuestionRepository extends JpaRepository<QuestionEntity, Long> {
    @Query("select q from QuestionEntity q JOIN FETCH q.product p where p.productId = :productId")
    List<QuestionEntity> findAllByProductId(Long productId);

    @Query("select q from QuestionEntity q JOIN FETCH q.user u where u.username = :username")
    List<QuestionEntity> findAllByUsername(String username);

}
