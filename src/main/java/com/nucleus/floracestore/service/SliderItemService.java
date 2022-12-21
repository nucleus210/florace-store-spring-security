package com.nucleus.floracestore.service;

import com.nucleus.floracestore.model.service.SliderItemServiceModel;

import java.util.List;

public interface SliderItemService {
    SliderItemServiceModel createSlide(SliderItemServiceModel sliderItemServiceModel, String username);

    SliderItemServiceModel updateSlideById(Long sliderItemId, SliderItemServiceModel sliderItemServiceModel, String username);

    SliderItemServiceModel deleteSlideById(Long sliderItemId, String username);

    SliderItemServiceModel getSlideById(Long sliderItemId);

    SliderItemServiceModel getSlideByTitle(String sliderItemTitle);

    List<SliderItemServiceModel> getAllSlides();

}
