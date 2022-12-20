package com.nucleus.floracestore.repository;

import com.nucleus.floracestore.model.entity.StorageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("storage-repository")
public interface StorageRepository extends JpaRepository<StorageEntity, Long> {
    @Override
    Optional<StorageEntity> findById(Long aLong);

    Optional<StorageEntity> findByFileName(String fileName);


}
