package com.niwe.erp.core.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.niwe.erp.core.domain.ErrorLog;

public interface ErrorLogRepository extends JpaRepository<ErrorLog, UUID> {
	
	Optional<ErrorLog> findByErrorName(String errorName);
}
