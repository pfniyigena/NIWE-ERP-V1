package com.niwe.erp.core.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.niwe.erp.core.domain.CoreRole;

public interface CoreRoleRepository  extends JpaRepository<CoreRole, UUID>{
	 Optional<CoreRole> findByName(String name);
}
