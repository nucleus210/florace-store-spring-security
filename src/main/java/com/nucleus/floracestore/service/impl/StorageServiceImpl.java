package com.nucleus.floracestore.service.impl;

import com.nucleus.floracestore.error.QueryRuntimeException;
import com.nucleus.floracestore.model.entity.StorageEntity;
import com.nucleus.floracestore.model.service.StorageServiceModel;
import com.nucleus.floracestore.repository.StorageRepository;
import com.nucleus.floracestore.service.StorageService;
import com.nucleus.floracestore.utils.ImageUtils;
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

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
@Service
public class StorageServiceImpl implements StorageService {
    private static final  String DOMAIN_URL = "http://localhost:8080";
    private static final  int PRODUCT_WIDTH = 960;
    private static final  int PRODUCT_HEIGHT = 997;

    private final Path fileStorageLocation;
    private final StorageRepository storageRepository;
    private final ResourceLoader resourceLoader;
    private final ModelMapper modelMapper;
    private final ImageUtils imageUtils;

    @Autowired
    public StorageServiceImpl(Environment environment,
                              StorageRepository storageRepository,
                              ResourceLoader resourceLoader,
                              ModelMapper modelMapper, ImageUtils imageUtils) {
        this.fileStorageLocation = Paths.get(environment.getProperty("app.file.upload-dir", "./src/main/resources/static/images/uploads"));
        this.storageRepository = storageRepository;
        this.resourceLoader = resourceLoader;
        this.modelMapper = modelMapper;
        this.imageUtils = imageUtils;
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

        String fileName = new SimpleDateFormat("yyyy-MM-dd'_'hh-mm-ss-SSS")
                .format(new Date()) + "-file." + getFileExtension(file.getOriginalFilename());
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

            // create storage entity and populate file information
            StorageEntity entity = new StorageEntity();
            entity.setFileName(fileName);
            entity.setFileUrl(DOMAIN_URL + arrOfStr[1]);
            entity.setSize(Files.size(targetLocation));
            storageRepository.save(entity);

            URL domain = new URL(DOMAIN_URL);
           System.out.println("Path" + DOMAIN_URL);
            System.out.println("Path" + filePath);
            System.out.println("Path" +  fileStorageLocation + targetLocation);

//            Path targetLocation2 = this.fileStorageLocation.resolve("test01.png");
//            BufferedImage originalImage = ImageUtils.cropImg(PRODUCT_WIDTH,PRODUCT_HEIGHT, file.getInputStream());
//            File outputfile = new File(targetLocation2.toUri());
//            ImageIO.write(originalImage, "png", outputfile);

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