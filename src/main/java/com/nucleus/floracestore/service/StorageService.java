package com.nucleus.floracestore.service;


import com.nucleus.floracestore.model.service.StorageServiceModel;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface StorageService {
    void init();

    void save(StorageServiceModel storageEntity);

    StorageServiceModel getByName(String name);

    StorageServiceModel storeFile(MultipartFile file);

    List<StorageServiceModel> storeMultipleResources(MultipartFile[] files);

    Resource load(String filename);

    void deleteAll();

    List<StorageServiceModel> getAllStorageEntities();

    List<Path> loadAllFilesPaths();

    void storeResourceBySource(MultipartFile file, String sourceType, String sourceName);

    Resource[] loadResources(String s) throws IOException;

    StorageServiceModel getById(Long storageId);
}