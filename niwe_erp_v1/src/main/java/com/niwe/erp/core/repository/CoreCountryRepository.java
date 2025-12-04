package com.niwe.erp.core.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.niwe.erp.core.domain.CoreCountry;

public interface CoreCountryRepository  extends JpaRepository<CoreCountry, UUID>{
	List<CoreCountry> findByIsDefault(Boolean isDefault);
}
