package com.nucleus.floracestore.repository;

import com.nucleus.floracestore.model.entity.PhonePrefixEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PhonePrefixRepository extends JpaRepository<PhonePrefixEntity, Long> {

    Optional<PhonePrefixEntity> findPhonePrefixEntityByCountryName(String countryName);
    Optional<PhonePrefixEntity> findPhonePrefixEntityByPrefix(String prefix);
}
