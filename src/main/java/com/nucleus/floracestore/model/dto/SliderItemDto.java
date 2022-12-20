package com.nucleus.floracestore.model.dto;

import com.nucleus.floracestore.model.service.StorageServiceModel;
import lombok.Data;

@Data
public class SliderItemDto {
    private Long slideItemId;
    private StorageServiceModel storage;
    private String sliderItemTitle;
    private String sliderItemContent;
}
