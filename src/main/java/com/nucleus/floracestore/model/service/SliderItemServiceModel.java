package com.nucleus.floracestore.model.service;

import lombok.Data;

@Data
public class SliderItemServiceModel {
    private Long slideItemId;
    private StorageServiceModel storage;
    private UserServiceModel user;
    private String sliderItemTitle;
    private String sliderItemContent;
}
