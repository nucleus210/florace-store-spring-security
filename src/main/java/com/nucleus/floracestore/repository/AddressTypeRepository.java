package com.nucleus.floracestore.repository;

import com.nucleus.floracestore.model.entity.AddressTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressTypeRepository extends JpaRepository<AddressTypeEntity, Long> {
    Optional<AddressTypeEntity> findAddressTypeByAddressTypeName(String addressTypeName);
}
