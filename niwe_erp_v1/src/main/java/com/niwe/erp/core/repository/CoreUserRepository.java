package com.niwe.erp.core.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.niwe.erp.core.domain.CoreUser;

public interface CoreUserRepository  extends JpaRepository<CoreUser, UUID>{
	 Optional<CoreUser> findByUsername(String username);

}
