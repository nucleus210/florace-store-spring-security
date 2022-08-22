package com.nucleus.floracestore.model.service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class StorageServiceModel {
    private Long resourceId;
    MultipartFile[] files;
}
