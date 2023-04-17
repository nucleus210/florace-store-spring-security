package com.nucleus.floracestore.repository;

import com.nucleus.floracestore.model.entity.UserActivityLogger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserActivityLoggerRepository extends JpaRepository<UserActivityLogger, Long> {
}
