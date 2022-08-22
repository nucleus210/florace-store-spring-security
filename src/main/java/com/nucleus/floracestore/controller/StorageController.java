package com.nucleus.floracestore.controller;

import com.google.gson.Gson;
import com.nucleus.floracestore.error.StorageFileNotFoundException;
import com.nucleus.floracestore.model.entity.StorageEntity;
import com.nucleus.floracestore.model.view.StorageViewModel;
import com.nucleus.floracestore.model.view.UploadResponseMessage;
import com.nucleus.floracestore.service.StorageService;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@SessionAttributes("projectModel")
public class StorageController {
    private final StorageService storageService;
    private final ModelMapper modelMapper;
    private final Gson gson;

    public StorageController(StorageService storageService, ModelMapper modelMapper, Gson gson) {
        this.storageService = storageService;
        this.modelMapper = modelMapper;
        this.gson = gson;
    }

    @ModelAttribute("uploadModel")
    public StorageViewModel uploadModel() {
        return new StorageViewModel();
    }

    @ResponseBody
    @CrossOrigin(origins = "http://localhost:8080/storage/upload")
    @PostMapping("/storage/upload")
    public ResponseEntity<UploadResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            StorageViewModel entity = modelMapper.map(storageService.storeFile(file), StorageViewModel.class);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(new UploadResponseMessage("Uploaded the file successfully: "
                            + file.getOriginalFilename(), entity));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
                    .body(new UploadResponseMessage("Could not upload the file: "
                            + file.getOriginalFilename() + "!", null));
        }
    }

    @ResponseBody
    @GetMapping("/storage/list")
    public ResponseEntity<List<StorageViewModel>> getListFiles() {
        List<StorageViewModel> fileInfos = storageService.loadAllFilesPaths()
                .stream()
                .map(this::pathToFileData)
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK)
                .body(fileInfos);
    }

    @ResponseBody
    @GetMapping("/storage/entities")
    public ResponseEntity<List<StorageViewModel>> getListEntities() {

        List<StorageViewModel> fileInfos = new ArrayList<>();
        for (StorageEntity entity : storageService.getAllStorageEntities()) {
            fileInfos.add(modelMapper.map(entity, StorageViewModel.class));
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(fileInfos);
    }

    private StorageViewModel pathToFileData(Path path) {
        StorageViewModel fileData = new StorageViewModel();
        String filename = path.getFileName().toString();
        fileData.setFileName(filename);
        fileData.setFileUrl(MvcUriComponentsBuilder.fromMethodName(StorageController.class, "getFile", filename)
                .build()
                .toString());

        try {
            fileData.setSize(Files.size(path));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error: " + e.getMessage());
        }

        return fileData;
    }

    @GetMapping("/file/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        Resource file = storageService.load(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"file\"" + file.getFilename() + "\"")
                .body(file);
    }


    @GetMapping("/storage/files")
    public String listUploadedFiles(Model model) throws IOException {

        model.addAttribute("allfiles", storageService.loadAllFilesPaths().stream().map(
                        path -> MvcUriComponentsBuilder.fromMethodName(StorageController.class,
                                "serveFile", path.getFileName().toString()).build().toUri().toString())
                .collect(Collectors.toList()));

        return "resources";
    }

    @GetMapping("/files/{filename:.+}")
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = (Resource) storageService.load(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @PostMapping("/storage/upload/profile")
    public String uploadProfilePhoto(@RequestParam("file") MultipartFile file, Model model) {
        try {
            // Saving all the list item into database
            storageService.storeFile(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/storage/upload";
    }


    @PostMapping("/storage/uploads")
    public String uploadMultipartFile(@RequestParam("files") MultipartFile[] files, Model model) {
        try {
            // Saving all the list item into database
            storageService.storeMultipleResources(files);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/storage/upload";
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }
}
