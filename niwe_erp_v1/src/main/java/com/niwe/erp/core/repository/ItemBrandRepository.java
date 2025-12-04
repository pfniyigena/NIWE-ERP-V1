package com.niwe.erp.core.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.niwe.erp.core.domain.ItemBrand;

public interface ItemBrandRepository  extends JpaRepository<ItemBrand, UUID>{
}
