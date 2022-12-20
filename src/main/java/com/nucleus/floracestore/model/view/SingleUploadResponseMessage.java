package com.nucleus.floracestore.model.view;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@RequiredArgsConstructor
public class SingleUploadResponseMessage {
    private final String responseMessage;
    @JsonIgnore
    private final MultipartFile file;
    private final StorageViewModel storage;
}
