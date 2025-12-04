package com.niwe.erp.inventory.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.niwe.erp.inventory.domain.InventoryLocation;

public interface InventoryLocationRepository extends JpaRepository<InventoryLocation, UUID> {

	List<InventoryLocation> findByWarehouseId(UUID warehouseId);

}
