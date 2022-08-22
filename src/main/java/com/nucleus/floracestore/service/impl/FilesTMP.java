//package com.nucleus.floracestore.service.impl;
//
//import com.nucleus.floracestore.FloraceStoreApplication;
//import com.nucleus.floracestore.error.StorageException;
//import com.nucleus.floracestore.error.StorageFileNotFoundException;
//import com.nucleus.floracestore.model.entity.StorageEntity;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.core.io.Resource;
//import org.springframework.core.io.UrlResource;
//import org.springframework.core.io.support.ResourcePatternUtils;
//import org.springframework.util.FileSystemUtils;
//import org.springframework.util.StringUtils;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.File;
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.MalformedURLException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.nio.file.StandardCopyOption;
//import java.util.stream.Stream;
//
//public class FilesTMP {
//    @Override
//    public StorageEntity getById(Long id) {
//        return storageRepository.findById(id).get();
//    }
//
//    @Override
//    public void storeResourceBySource(MultipartFile file, String sourceType, String sourceName) {
//        String filename = StringUtils.cleanPath(file.getOriginalFilename());
//        String fileNameNormalize = "";
//        String[] tokens;
//        if (file.isEmpty()) {
//            throw new StorageException("Failed to store empty file : " + filename);
//        }
//        if (filename.contains("..")) {
//            throw new StorageException("Cannot store file with relative path outside current directory" + filename);
//        }
//        if (filename.contains("/")) {
//            tokens = filename.split("/");
//            fileNameNormalize = tokens[tokens.length - 1];
//            createDirIfNotExist(rootLocation + "/");
//        } else {
//            fileNameNormalize = filename;
//        }
//
//        try (InputStream inputStream = file.getInputStream()) {
//            System.out.println(
//                    "Number of bytes copied: "
//                            +   Files.copy(inputStream, rootLocation.resolve(fileNameNormalize), StandardCopyOption.REPLACE_EXISTING));
//        } catch (IOException e) {
//            throw new StorageException("Failed to store file : " + filename, e);
//        } finally {
//
//            StorageEntity resource = new StorageEntity();
//            resource.setFileName(filename);
//            resource.setFileUrl(rootLocation.toString() + "/" + fileNameNormalize);
//            resource.setResourceType(file.getContentType());
//            resource.setResource_description("Bla Bla");
//            storageRepository.save(resource);
//        }
//    }
//
//    @Override
//    public void storeResource(MultipartFile file) {
//        String filename = StringUtils.cleanPath(file.getOriginalFilename());
//        String fileNameNormalize = "";
//        String[] tokens;
//        if (file.isEmpty()) {
//            throw new StorageException("Failed to store empty file : " + filename);
//        }
//        if (filename.contains("..")) {
//            throw new StorageException("Cannot store file with relative path outside current directory" + filename);
//        }
//        if (filename.contains("/")) {
//            tokens = filename.split("/");
//            fileNameNormalize = tokens[tokens.length - 1];
//            createDirIfNotExist(rootLocation + "/");
//        } else {
//            fileNameNormalize = filename;
//        }
//
//        try (InputStream inputStream = file.getInputStream()) {
//            System.out.println(
//                    "Number of bytes copied: "
//                            +   Files.copy(inputStream, rootLocation.resolve(fileNameNormalize), StandardCopyOption.REPLACE_EXISTING));
//        } catch (IOException e) {
//            throw new StorageException("Failed to store file : " + filename, e);
//        } finally {
//
//            StorageEntity resource = new StorageEntity();
//            resource.setFileName(filename);
//            resource.setFileUrl(rootLocation.toString() + "/" + fileNameNormalize);
//            resource.setResourceType(file.getContentType());
//            resource.setResource_description("Bla Bla");
//            storageRepository.save(resource);
//        }
//    }
//
//    @Override
//    public void storeMultipleResources(MultipartFile[] files) {
//        try {
//            for (MultipartFile file : files) {
//                storeResource(file);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public Path getResourceByFilename(String filename) {
//        return null;
//    }
//
//    @Override
//    public Stream<Path> getAllResources() throws IOException {
//        ClassPathResource resource = new ClassPathResource("/static/images/resource-collector/", FloraceStoreApplication.class);
//
//        String directory = resource.getPath();
//        System.out.println(directory);
//        Path dir = Path.of(resource.toString());
////        try {
////            return Files.walk(dir, 1)
////                    .filter(path -> !path.equals(dir))
////                    .map(dir::relativize);
////        } catch (IOException e) {
////            throw new StorageException("Failed to read stored file.", e);
////        }
//
//        Stream<Path> paths = Files.walk(Paths.get(directory));
//        paths.filter(Files::isDirectory)
//                .forEach(System.out::println);
//        return paths;
//
//    }
//    public Resource[] loadResources(String pattern) throws IOException {
//        return ResourcePatternUtils.getResourcePatternResolver(resourceLoader).getResources(pattern);
//    }
//
//    public Path load(String filename) {
//        return rootLocation.resolve(filename);
//    }
//
//    public UrlResource loadAsResource(String filename) {
//        try {
//            Path file = load(filename);
//            UrlResource resource = new UrlResource(file.toUri());
//            if (resource.exists() || resource.isReadable()) {
//                return resource;
//            } else {
//                throw new StorageFileNotFoundException("Could not read file: " + filename);
//            }
//        } catch (MalformedURLException e) {
//            throw new StorageFileNotFoundException("Could not read file : " + filename, e);
//        }
//    }
//
//    @Override
//    public void deleteAllResourcesByEntityPathAndId(String entityPath, Long entityId) {
//
//    }
//    public void deleteAll() {
//        FileSystemUtils.deleteRecursively(rootLocation.toFile());
//    }
//
//    /**
//     * Create directory to save files, if not exist
//     */
//    private void createDirIfNotExist(String upload_dir) {
//        //create directory to save the files
//        File directory = new File(upload_dir);
//        if (!directory.exists()) {
//            directory.mkdir();
//        }
//    }
//}


//
//import com.nucleus.floracestore.model.entity.StorageEntity;
//import org.springframework.core.io.Resource;
//import org.springframework.core.io.UrlResource;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.nio.file.Path;
//import java.util.stream.Stream;
//
//public interface StorageService {
//    void init();
//
//
//    StorageEntity getById(Long id);
//
//    void storeResourceBySource(MultipartFile file, String sourceType, String sourceName);
//
//    void storeResource(MultipartFile file);
//
//    void storeMultipleResources(MultipartFile[] files);
//
//    Path getResourceByFilename(String filename);
//
//    Stream<Path> getAllResources() throws IOException;
//
//    UrlResource loadAsResource(String filename);
//
//    void deleteAllResourcesByEntityPathAndId(String entityPath, Long entityId);
//
//    Resource[]  loadResources(String s) throws IOException;
//}
