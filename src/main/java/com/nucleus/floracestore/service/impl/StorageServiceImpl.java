package com.nucleus.floracestore.service.impl;

import com.nucleus.floracestore.error.QueryRuntimeException;
import com.nucleus.floracestore.model.entity.StorageEntity;
import com.nucleus.floracestore.model.service.StorageServiceModel;
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
import java.util.*;
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
    public void save(StorageServiceModel storageServiceModel) {
        storageRepository.save(modelMapper.map(storageServiceModel, StorageEntity.class));
    }

    @Override
    public StorageServiceModel getByName(String fileName) {
        StorageEntity storageEntity = storageRepository.findByFileName(fileName)
                .orElseThrow(() -> new QueryRuntimeException("Could not find storage with filename " + fileName));
        return mapToService(storageEntity);
    }

    private String getFileExtension(String fileName) {
        if (fileName == null) {
            return null;
        }
        String[] fileNameParts = fileName.split("\\.");
        return fileNameParts[fileNameParts.length - 1];
    }

    @Override
    public StorageServiceModel storeFile(MultipartFile file) {

        // create a string of uppercase and lowercase characters and numbers
        String upperAlphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerAlphabet = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";

        // combine all strings
        String alphaNumeric = upperAlphabet + lowerAlphabet + numbers;

        // create random string builder
        StringBuilder sb = new StringBuilder();

        // create an object of Random class
        Random random = new Random();

        // specify length of random string
        int length = 10;

        for(int i = 0; i < length; i++) {

            // generate random index number
            int index = random.nextInt(alphaNumeric.length());

            // get character specified by index
            // from the string
            char randomChar = alphaNumeric.charAt(index);

            // append the character to string builder
            sb.append(randomChar);
        }

        String randomString = sb.toString();




//        File.createTempFile(String prefix,
//                String suffix,
//                File director);
        // Normalize file name
        String fileName = randomString + "." + getFileExtension(file.getOriginalFilename());
//                new SimpleDateFormat("yyyy-MM-dd'_'hh-mm-ss-SSS")
//                        .format(new Date()) + "-file." + getFileExtension(file.getOriginalFilename());
        try {
            // Check if the filename contains invalid characters
            if (fileName.contains("..")) {
                throw new RuntimeException(
                        "Sorry! Filename contains invalid path sequence " + fileName);
            }

            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            String filePath = this.fileStorageLocation.resolve(fileName).toString();
            filePath = filePath.replace("\\", "/");
            String[] arrOfStr = filePath.split("static", 0);
            System.out.println("File path: " + arrOfStr[1]);

            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            StorageEntity entity = new StorageEntity();
            entity.setFileName(fileName);
            entity.setFileUrl("http://localhost:8080" + arrOfStr[1]);
            entity.setSize(Files.size(targetLocation));
            storageRepository.save(entity);
            StorageEntity storageEntity = storageRepository.findByFileName(fileName)
                    .orElseThrow(() -> new QueryRuntimeException("Could not find storage with filename " + fileName));
            return mapToService(storageEntity);
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    @Override
    public List<StorageServiceModel> storeMultipleResources(MultipartFile[] files) {
        List<StorageServiceModel> storages = new ArrayList<>();
        try {
            for (MultipartFile file : files) {
                StorageServiceModel storageServiceModel = storeFile(file);
                storages.add(storageServiceModel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return storages;
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
    public List<StorageServiceModel> getAllStorageEntities() {
        return storageRepository.findAll()
                .stream()
                .map(this::mapToService)
                .collect(Collectors.toList());
    }

    private StorageServiceModel mapToService(StorageEntity storage) {
        return this.modelMapper.map(storage, StorageServiceModel.class);
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

    public StorageServiceModel getById(Long id) {
        StorageEntity storageEntity = storageRepository.findById(id)
                .orElseThrow(() -> new QueryRuntimeException("Could not find storage with id " + id));
        return mapToService(storageEntity);
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

}