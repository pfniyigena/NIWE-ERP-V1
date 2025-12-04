package com.niwe.erp.core.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.niwe.erp.core.domain.CoreItemClassification;

public interface CoreItemClassificationRepository  extends JpaRepository<CoreItemClassification, UUID>{
	List<CoreItemClassification> findByIsDefault(Boolean isDefault);
}
