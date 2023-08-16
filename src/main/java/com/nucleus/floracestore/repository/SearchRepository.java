package com.nucleus.floracestore.repository;

import com.nucleus.floracestore.model.entity.SearchEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("search-repository")
public interface SearchRepository extends JpaRepository<SearchEntity, Long> {

    @Query(value = "SELECT * FROM Products WHERE MATCH(product_name, product_description) AGAINST (?1)", nativeQuery = true)
    List<?> findByTitleLike(String title);

}
