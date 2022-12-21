package com.nucleus.floracestore.repository;

import com.nucleus.floracestore.model.entity.BlogPostCommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("BlogPostCommentRepository")
public interface BlogPostCommentRepository extends JpaRepository<BlogPostCommentEntity, Long> {
    @Query("select c from BlogPostCommentEntity c JOIN c.blogPost b JOIN c.user u where b.blogPostId = :blogPostId and u.username = :username")
    Optional<BlogPostCommentEntity> findBlogPostCommentByBlogPostIdAndUsername(Long blogPostId, String username);

    @Query("select c from BlogPostCommentEntity c JOIN c.user u where u.username = :username")
    List<BlogPostCommentEntity> findAllBlogPostCommentsByUsername(String username);
}
