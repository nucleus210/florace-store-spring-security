package com.nucleus.floracestore.repository;

import com.nucleus.floracestore.model.entity.BlogPostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("BlogPostRepository")
public interface BlogPostRepository extends JpaRepository<BlogPostEntity, Long> {
    Optional<BlogPostEntity> findById(Long id);
    Optional<BlogPostEntity> findByTitle(String title);
    @Query("select b from BlogPostEntity b where b.publishedAt <= :startDate and b.publishedAt >= :endDate")
    List<BlogPostEntity> findBlogPostsByDatePeriod(String startDate, String endDate);

}
