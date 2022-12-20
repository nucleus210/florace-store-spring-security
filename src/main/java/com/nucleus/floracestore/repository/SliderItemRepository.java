package com.nucleus.floracestore.repository;

import com.nucleus.floracestore.model.entity.SliderItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("sliderItemRepository")
public interface SliderItemRepository extends JpaRepository<SliderItemEntity, Long> {

    Optional<SliderItemEntity> findBySliderItemId(Long sliderItemId);
    Optional<SliderItemEntity> findBySliderItemTitle(String sliderItemTitle);

}
