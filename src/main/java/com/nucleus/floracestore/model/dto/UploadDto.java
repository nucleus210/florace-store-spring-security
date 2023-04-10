package com.nucleus.floracestore.model.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UploadDto {
    private final MultipartFile[] files;
}
