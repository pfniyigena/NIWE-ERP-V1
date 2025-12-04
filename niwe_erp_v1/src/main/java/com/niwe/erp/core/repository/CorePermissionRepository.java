package com.niwe.erp.core.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.niwe.erp.core.domain.CorePermission;

public interface CorePermissionRepository  extends JpaRepository<CorePermission, UUID>{
	Optional<CorePermission> findByName(String name);
	List<CorePermission> findAllByNameIn(Collection<String> names);
}
