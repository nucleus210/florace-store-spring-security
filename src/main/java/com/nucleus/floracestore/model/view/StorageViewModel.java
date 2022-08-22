package com.nucleus.floracestore.model.view;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class StorageViewModel {
    private Long resourceId;
    @NotNull
    private String fileName;
    private String fileUrl;
    private Long size;
}













