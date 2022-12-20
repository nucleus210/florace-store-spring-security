package com.nucleus.floracestore.service.impl;

import com.nucleus.floracestore.error.QueryRuntimeException;
import com.nucleus.floracestore.model.entity.SliderItemEntity;
import com.nucleus.floracestore.model.service.SliderItemServiceModel;
import com.nucleus.floracestore.model.service.UserServiceModel;
import com.nucleus.floracestore.repository.SliderItemRepository;
import com.nucleus.floracestore.service.SliderItemService;
import com.nucleus.floracestore.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
public class SliderItemServiceImpl implements SliderItemService {

    private final ModelMapper modelMapper;
    private final UserService userService;
    private final SliderItemRepository sliderItemRepository;

    @Autowired
    public SliderItemServiceImpl(ModelMapper modelMapper,
                                 UserService userService,
                                 SliderItemRepository sliderItemRepository) {
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.sliderItemRepository = sliderItemRepository;
    }

    @Override
    public SliderItemServiceModel createSlide(SliderItemServiceModel sliderItemServiceModel, String username) {
        UserServiceModel userServiceModel = userService.findByUsername(username)
                .orElseThrow(() -> new QueryRuntimeException("Could not find user " + username));
        sliderItemServiceModel.setUser(userServiceModel);
       SliderItemEntity sliderItemEntity = sliderItemRepository.save(modelMapper.map(sliderItemServiceModel, SliderItemEntity.class));
        log.info("SliderItemController: created slide with id: " + sliderItemEntity.getSliderItemId());
        return mapToService(sliderItemEntity);
    }

    @Override
    public SliderItemServiceModel updateSlideById(Long sliderItemId, SliderItemServiceModel sliderItemServiceModel, String username) {
        UserServiceModel userServiceModel = userService.findByUsername(username)
                .orElseThrow(() -> new QueryRuntimeException("Could not find user " + username));
        // Todo user check permissions
        SliderItemEntity sliderItemEntity = sliderItemRepository.save(modelMapper.map(sliderItemServiceModel, SliderItemEntity.class));
        return mapToService(sliderItemEntity);
    }

    @Override
    public SliderItemServiceModel deleteSlideById(Long sliderItemId, String username) {
        SliderItemEntity sliderItemEntity = sliderItemRepository.findBySliderItemId(sliderItemId)
                .orElseThrow(() -> new QueryRuntimeException("Could not find slide entity with id " + sliderItemId));
        sliderItemRepository.delete(sliderItemEntity);
        log.info("SliderItemController: deleted slide with id: " + sliderItemId);
        return mapToService(sliderItemEntity);
    }

    @Override
    public SliderItemServiceModel getSlideById(Long sliderItemId) {
        SliderItemEntity sliderItemEntity = sliderItemRepository.findBySliderItemId(sliderItemId)
                .orElseThrow(() -> new QueryRuntimeException("Could not find slide entity with id " + sliderItemId));
        log.info("SliderItemController: found slide item with id " + sliderItemId);
        return mapToService(sliderItemEntity);
    }

    @Override
    public SliderItemServiceModel getSlideByTitle(String sliderItemTitle) {
        log.info(String.valueOf(sliderItemTitle));
        SliderItemEntity sliderItemEntity = sliderItemRepository.findBySliderItemTitle(sliderItemTitle)
                .orElseThrow(() -> new QueryRuntimeException("Could not find slide entity with title " + sliderItemTitle));
        log.info("SliderItemController: found slide item with title " + sliderItemTitle);
        return mapToService(sliderItemEntity);
    }
    @Override
    public List<SliderItemServiceModel> getAllSlides() {
        log.info("SliderItemController: get all slider items");
        return sliderItemRepository
                .findAll()
                .stream()
                .map(this::mapToService)
                .collect(Collectors.toList());
    }
    private SliderItemServiceModel mapToService(SliderItemEntity sliderItemEntity) {
        return modelMapper.map(sliderItemEntity, SliderItemServiceModel.class);
    }
}
