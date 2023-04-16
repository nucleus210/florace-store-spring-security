package com.nucleus.floracestore.model.view;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class SingleUploadResponseMessage {
    private final String responseMessage;
    @JsonIgnore
    private final MultipartFile file;
    @JsonIgnore
    private final MultipartFile[] files;
    private final StorageViewModel storage;
    private final List<StorageViewModel> storages;

}
