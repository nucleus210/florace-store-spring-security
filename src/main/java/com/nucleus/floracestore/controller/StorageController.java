package com.nucleus.floracestore.controller;

import com.nucleus.floracestore.error.StorageFileNotFoundException;
import com.nucleus.floracestore.hateoas.StorageAssembler;
import com.nucleus.floracestore.model.dto.UploadDto;
import com.nucleus.floracestore.model.service.StorageServiceModel;
import com.nucleus.floracestore.model.view.SingleUploadResponseMessage;
import com.nucleus.floracestore.model.view.StorageViewModel;
import com.nucleus.floracestore.service.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.Resource;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
public class StorageController {
    private static final String UPLOADED_FOLDER = "./resource-collector/";
    private final StorageService storageService;
    private final ModelMapper modelMapper;
    private final StorageAssembler assembler;

    public StorageController(StorageService storageService,
                             ModelMapper modelMapper,
                             StorageAssembler assembler) {
        this.storageService = storageService;
        this.modelMapper = modelMapper;
        this.assembler = assembler;
    }
    @PreAuthorize("hasRole('ROLE_STAFF') or hasRole('ROLE_ADMIN') or hasRole('ROLE_USER') or hasRole('ROLE_FACEBOOK_USER')")
    @PostMapping(path = "/storages/uploads", consumes = {"multipart/form-data" }, produces = "application/json")
    public ResponseEntity<UploadDto> multiUploadFileModel(@RequestBody UploadDto uploadFileModel) {
        System.out.println("Upload Files: " + uploadFileModel);
//        try {
//            Arrays.stream(uploadFileModel.getFiles()).forEach(file -> {
//                try {
//                    saveUploadedFile(file);
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//            });
//
//            // Save as you want as per requiremens
//            saveUploadedFile(uploadFileModel.getFile());
//        } catch (IOException e) {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }

        return new ResponseEntity("Successfully uploaded!", HttpStatus.OK);
    }
    private void saveUploadedFile(MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
            Files.write(path, bytes);
        }
    }
    @PreAuthorize("hasRole('ROLE_STAFF') or hasRole('ROLE_ADMIN') or hasRole('ROLE_USER') or hasRole('ROLE_FACEBOOK_USER')")
    @PostMapping("/storages/files")
    public ResponseEntity<EntityModel<SingleUploadResponseMessage>> uploadMultipleStorages(@RequestParam("files") MultipartFile[] files) {
        List<StorageViewModel> storages = new ArrayList<>();

        try {
            Arrays.stream(files).forEach(f -> {
                StorageViewModel storage = mapToView(storageService.storeFile(f));
                storages.add(storage);
            });
            System.out.println(storages);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(assembler.toModel(new SingleUploadResponseMessage("Uploaded the file successfully: "
                            ,null, files,null, storages)));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
                    .body(assembler.toModel(new SingleUploadResponseMessage("Could not upload the file: " ,
            null, files,null, storages)));
        }
    }
    @PreAuthorize("hasRole('ROLE_STAFF') or hasRole('ROLE_ADMIN') or hasRole('ROLE_USER') or hasRole('ROLE_FACEBOOK_USER')")
    @PostMapping(value = "/storages/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<EntityModel<SingleUploadResponseMessage>> uploadFileToStorages(@RequestParam("file") MultipartFile file) {
        try {
            StorageViewModel storage = mapToView(storageService.storeFile(file));
            return ResponseEntity.status(HttpStatus.OK)
                    .body(assembler.toModel(new SingleUploadResponseMessage("Uploaded the file successfully: "
                            + file.getOriginalFilename(), file, null, storage, null)));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
                    .body(assembler.toModel(new SingleUploadResponseMessage("Could not upload the file: "
                            + file.getOriginalFilename() + "!", file, null, null, null)));
        }
    }

    @GetMapping("/storages/list")
    public ResponseEntity<List<StorageViewModel>> getListFiles() {
        List<StorageViewModel> fileInfos = storageService.loadAllFilesPaths()
                .stream()
                .map(this::pathToFileData)
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK)
                .body(fileInfos);
    }

    @GetMapping("/storage/entities")
    public ResponseEntity<List<StorageViewModel>> getListEntities() {

        List<StorageViewModel> fileInfos = new ArrayList<>();
        for (StorageServiceModel storage : storageService.getAllStorageEntities()) {
            fileInfos.add(modelMapper.map(storage, StorageViewModel.class));
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
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        Resource file = storageService.load(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"file\"" + file.getFilename() + "\"")
                .body(file);
    }


    @GetMapping("/storages/files")
    public String listUploadedFiles(Model model) throws IOException {

        model.addAttribute("allfiles", storageService.loadAllFilesPaths().stream().map(
                        path -> MvcUriComponentsBuilder.fromMethodName(StorageController.class,
                                "serveFile", path.getFileName().toString()).build().toUri().toString())
                .collect(Collectors.toList()));

        return "resources";
    }

    @GetMapping("/files/{filename:.+}")
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = storageService.load(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @PostMapping("/storages/upload/profile")
    public String uploadProfilePhoto(@RequestParam("file") MultipartFile file, Model model) {
        try {
            // Saving all the list item into database
            storageService.storeFile(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/storage/upload";
    }


    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

    private StorageViewModel mapToView(StorageServiceModel model) {
        return modelMapper.map(model, StorageViewModel.class);
    }
}