package com.nucleus.floracestore.repository;

import com.nucleus.floracestore.model.entity.ContactEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("contact-repository")
public interface ContactRepository extends JpaRepository<ContactEntity, Long> {
}
