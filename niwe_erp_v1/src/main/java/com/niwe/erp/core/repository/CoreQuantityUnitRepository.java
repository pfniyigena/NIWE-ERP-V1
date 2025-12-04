package com.niwe.erp.core.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.niwe.erp.core.domain.CoreQuantityUnit;

public interface CoreQuantityUnitRepository  extends JpaRepository<CoreQuantityUnit, UUID>{

	List<CoreQuantityUnit> findByIsDefault(Boolean isDefault);
}
