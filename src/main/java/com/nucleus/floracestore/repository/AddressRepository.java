package com.nucleus.floracestore.repository;

import com.nucleus.floracestore.model.entity.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("address-repository")
public interface AddressRepository extends JpaRepository<AddressEntity, Long> {
}
