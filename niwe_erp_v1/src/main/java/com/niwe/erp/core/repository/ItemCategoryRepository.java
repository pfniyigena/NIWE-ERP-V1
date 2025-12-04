package com.niwe.erp.core.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.niwe.erp.core.domain.ItemCategory;

public interface ItemCategoryRepository  extends JpaRepository<ItemCategory, UUID>{
	 Optional<ItemCategory> findByCategoryName(String categoryName);
}
