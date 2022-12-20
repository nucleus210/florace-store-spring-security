package com.nucleus.floracestore.model.view;

import com.nucleus.floracestore.model.service.StorageServiceModel;
import lombok.Data;

@Data
public class SliderItemViewModel {
    private Long slideItemId;
    private StorageServiceModel storage;
    private String sliderItemTitle;
    private String sliderItemContent;
}
