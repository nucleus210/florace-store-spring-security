package com.nucleus.floracestore.service;


import com.nucleus.floracestore.model.entity.StorageEntity;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface StorageService {
    void init();

    void initializeStorage() throws IOException;

    void save(StorageEntity storageEntity);

    StorageEntity getByName(String name);

    StorageEntity storeFile(MultipartFile file);

    Resource load(String filename);

    void deleteAll();

    List<StorageEntity> getAllStorageEntities();

    List<Path> loadAllFilesPaths();

    void storeResourceBySource(MultipartFile file, String sourceType, String sourceName);

    void storeMultipleResources(MultipartFile[] files);

    Resource[] loadResources(String s) throws IOException;

    StorageEntity getById(Long storageId);
}
