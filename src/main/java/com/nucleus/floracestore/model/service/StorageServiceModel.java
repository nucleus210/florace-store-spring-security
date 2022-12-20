package com.nucleus.floracestore.model.service;

import lombok.Data;

@Data
public class StorageServiceModel {
    private Long resourceId;
    private String fileName;
    private String fileUrl;
    private Long size;
//    MultipartFile[] files;
}
