package com.nucleus.floracestore.repository;

import com.nucleus.floracestore.model.entity.CountryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CountryRepository extends JpaRepository<CountryEntity, Long> {
    Optional<CountryEntity> findCountryEntityByCountryName(String countryName);
    Optional<CountryEntity> findCountryEntityByCountryCode(String countryCode);
}
