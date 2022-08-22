package com.nucleus.floracestore.repository;

import com.nucleus.floracestore.model.entity.StorageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("storageRepository")
public interface StorageRepository extends JpaRepository<StorageEntity, Long> {
    StorageEntity findByFileName(String fileName);


}
