package com.nucleus.floracestore.service.impl;

import com.nucleus.floracestore.model.entity.StorageEntity;
import com.nucleus.floracestore.repository.StorageRepository;
import com.nucleus.floracestore.service.StorageService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StorageServiceImpl implements StorageService {
    private final Path fileStorageLocation;
    private final StorageRepository storageRepository;
    private final ResourceLoader resourceLoader;
    private final ModelMapper modelMapper;

    @Autowired
    public StorageServiceImpl(Environment environment,
                              StorageRepository storageRepository,
                              ResourceLoader resourceLoader,
                              ModelMapper modelMapper) {
        this.fileStorageLocation = Paths.get(environment.getProperty("app.file.upload-dir", "./src/main/resources/static/images/uploads"));
        this.storageRepository = storageRepository;
        this.resourceLoader = resourceLoader;
        this.modelMapper = modelMapper;
        this.init();
    }

    public void init() {
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException(
                    "Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    @Override
    public void save(StorageEntity storageEntity) {
        storageRepository.save(storageEntity);
    }

    @Override
    public StorageEntity getByName(String name) {
        return storageRepository.findByFileName(name);
    }

    private String getFileExtension(String fileName) {
        if (fileName == null) {
            return null;
        }
        String[] fileNameParts = fileName.split("\\.");

        return fileNameParts[fileNameParts.length - 1];
    }

    @Override
    public StorageEntity storeFile(MultipartFile file) {
        // Normalize file name
        String fileName =
                new SimpleDateFormat("yyyy-MM-dd'_'hh-mm-ss-SSS").format(new Date()) + "-file." + getFileExtension(file.getOriginalFilename());

        try {
            // Check if the filename contains invalid characters
            if (fileName.contains("..")) {
                throw new RuntimeException(
                        "Sorry! Filename contains invalid path sequence " + fileName);
            }

            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            StorageEntity entity = new StorageEntity();
            entity.setFileName(fileName);
            entity.setFileUrl(this.fileStorageLocation.resolve(fileName).toString());
            entity.setSize(Files.size(targetLocation));
            storageRepository.save(entity);

            return storageRepository.findByFileName(entity.getFileName());
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    @Override
    public void storeMultipleResources(MultipartFile[] files) {
        try {
            for (MultipartFile file : files) {
                storeFile(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Resource load(String filename) {
        try {
            Path file = fileStorageLocation
                    .resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(fileStorageLocation
                .toFile());
    }

    @Override
    public List<StorageEntity> getAllStorageEntities() {

        return storageRepository.findAll();
    }

    @Override
    public List<Path> loadAllFilesPaths() {
        try {
            Path root = fileStorageLocation;
            if (Files.exists(root)) {
                return Files.walk(root, 1)
                        .filter(path -> !path.equals(root))
                        .collect(Collectors.toList());
            }

            return Collections.emptyList();
        } catch (IOException e) {
            throw new RuntimeException("Could not list the files!");
        }
    }

    @Override
    public void storeResourceBySource(MultipartFile file, String sourceType, String sourceName) {
    }

    public StorageEntity getById(Long id) {
        return storageRepository.findById(id).get();
    }

    public Resource[] loadResources(String pattern) throws IOException {
        return ResourcePatternUtils.getResourcePatternResolver(resourceLoader).getResources(pattern);
    }

    /**
     * Create directory to save files, if not exist
     */
    private void createDirIfNotExist(String upload_dir) {
        //create directory to save the files
        File directory = new File(upload_dir);
        if (!directory.exists()) {
            directory.mkdir();
        }
    }

    @Override
    public void initializeStorage() throws IOException {
        Resource[] resources = loadResources("classpath*:/static/images/uploads/*.jpg");
        for (Resource r : resources
        ) {
            String[] arrOfStr = r.getURL().toString().split("static", 0);
            System.out.println(arrOfStr[1]);
            String resourceType = r.getFilename().split("\\.", 0)[1];
            System.out.println(resourceType);
            StorageEntity storageEntity = new StorageEntity();
            storageEntity.setFileName(r.getFilename());
            storageEntity.setFileUrl(arrOfStr[1]);
            storageEntity.setSize(Files.size(Path.of(r.getURI().getPath())));
            storageRepository.save(storageEntity);
        }
    }
}