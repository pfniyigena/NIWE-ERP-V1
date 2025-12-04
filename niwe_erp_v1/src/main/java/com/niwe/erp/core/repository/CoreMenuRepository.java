package com.niwe.erp.core.repository;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.niwe.erp.core.domain.CoreMenu;

public interface  CoreMenuRepository extends JpaRepository<CoreMenu, UUID> {
	 List<CoreMenu> findAllByActiveTrueOrderBySortOrderAsc();

}
