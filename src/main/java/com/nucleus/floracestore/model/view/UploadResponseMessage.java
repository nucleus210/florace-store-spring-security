package com.nucleus.floracestore.model.view;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UploadResponseMessage {
    private final String responseMessage;
    private final StorageViewModel storageEntity;
}
