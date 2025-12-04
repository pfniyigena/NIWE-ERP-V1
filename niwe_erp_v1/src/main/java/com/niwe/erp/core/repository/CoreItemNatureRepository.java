package com.niwe.erp.core.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.niwe.erp.core.domain.CoreItemNature;
public interface CoreItemNatureRepository  extends JpaRepository<CoreItemNature, UUID>{
	
	List<CoreItemNature> findByIsDefault(Boolean isDefault);

}
