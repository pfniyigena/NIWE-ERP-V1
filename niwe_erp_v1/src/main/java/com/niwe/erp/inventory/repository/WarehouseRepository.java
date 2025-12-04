package com.niwe.erp.inventory.repository;
 

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.niwe.erp.inventory.domain.Warehouse;

public interface WarehouseRepository extends JpaRepository<Warehouse, UUID> {
    List<Warehouse> findByBranchId(UUID branchId);
    Optional <Warehouse> findByIsMain(Boolean isMain);
    List<Warehouse>  findByIdNotIn(List<UUID> ids);
}

