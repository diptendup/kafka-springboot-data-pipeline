package com.mp.fintech.repository;

import com.mp.fintech.entity.AuthorizationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorizationStatusRepository extends JpaRepository<AuthorizationStatus, Integer> {
}
