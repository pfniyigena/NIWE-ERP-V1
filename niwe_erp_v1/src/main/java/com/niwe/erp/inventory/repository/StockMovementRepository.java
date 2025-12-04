package com.niwe.erp.inventory.repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.niwe.erp.inventory.domain.StockMovement;
import com.niwe.erp.inventory.web.view.StockMovementListView;

public interface StockMovementRepository extends JpaRepository<StockMovement, UUID> {

	List<StockMovement> findByMovementDateBetween(Instant start, Instant end);

	@Query(value = """
			SELECT
			    sm.item_id AS itemId,
			    i.item_name AS itemName,
			    i.internal_code AS itemCode,
			    i.barcode AS barcode,
			    i.unit_price AS unitPrice,
			    i.unit_cost AS unitCost,
			    sm.MOVED_QUANTITY AS movedQuantity,
			    sm.PREVIOUS_WAREHOUSE_QUANTITY AS prevWarehouseQuantity,
			    sm.CURRENT_WAREHOUSE_QUANTITY AS currentWarehouseQuantity,
			    sm.PREVIOUS_LOCATION_QUANTITY AS prevLocationQuantity,
			    sm.CURRENT_LOCATION_QUANTITY AS currentLocationQuantity,
			    sm.movement_date AS movementDate,
			    sm.movement_type AS movementType,
			    COALESCE(sm.to_warehouse_id, sm.from_warehouse_id) AS warehouseId,
			             COALESCE(sm.to_location_id, sm.from_location_id) AS locationId
			FROM INVENTORY_STOCK_MOVEMENT sm
			JOIN core_item i ON sm.item_id = i.id
			WHERE (sm.from_warehouse_id = :warehouseId
			          OR sm.to_warehouse_id = :warehouseId)
			AND (:itemId IS NULL OR sm.item_id = :itemId)
			         AND (
			         :search IS NULL
			         OR LOWER(i.item_name) LIKE LOWER(CONCAT('%', :search, '%'))
			         OR LOWER(i.internal_code) LIKE LOWER(CONCAT('%', :search, '%'))
			         OR LOWER(i.barcode) LIKE LOWER(CONCAT('%', :search, '%'))
			        )
			ORDER BY sm.movement_date DESC
			""", countQuery = """
			SELECT COUNT(*)
			FROM INVENTORY_STOCK_MOVEMENT sm
			JOIN core_item i ON sm.item_id = i.id
			WHERE (
			             sm.from_warehouse_id = :warehouseId
			          OR sm.to_warehouse_id = :warehouseId
			         )
			    AND (:itemId IS NULL OR sm.item_id = :itemId)
			    AND (
			            :search IS NULL
			         OR LOWER(i.item_name) LIKE LOWER(CONCAT('%', :search, '%'))
			         OR LOWER(i.internal_code) LIKE LOWER(CONCAT('%', :search, '%'))
			         OR LOWER(i.barcode) LIKE LOWER(CONCAT('%', :search, '%'))
			        )
			""", nativeQuery = true)
	Page<StockMovementListView> findMovements(@Param("itemId") UUID itemId, @Param("warehouseId") UUID warehouseId,
			@Param("search") String search, Pageable pageable);
	@Query(value = """
			SELECT
			    sm.item_id AS itemId,
			    i.item_name AS itemName,
			    i.internal_code AS itemCode,
			    i.barcode AS barcode,
			    i.unit_price AS unitPrice,
			    i.unit_cost AS unitCost,
			    sm.MOVED_QUANTITY AS movedQuantity,
			    sm.PREVIOUS_WAREHOUSE_QUANTITY AS prevWarehouseQuantity,
			    sm.CURRENT_WAREHOUSE_QUANTITY AS currentWarehouseQuantity,
			    sm.PREVIOUS_LOCATION_QUANTITY AS prevLocationQuantity,
			    sm.CURRENT_LOCATION_QUANTITY AS currentLocationQuantity,
			    sm.movement_date AS movementDate,
			    sm.movement_type AS movementType,
			    COALESCE(sm.to_warehouse_id, sm.from_warehouse_id) AS warehouseId,
			    COALESCE(sm.to_location_id, sm.from_location_id) AS locationId
			FROM INVENTORY_STOCK_MOVEMENT sm
			JOIN core_item i ON sm.item_id = i.id
			WHERE (sm.FROM_LOCATION_ID = :locationId
			          OR sm.TO_LOCATION_ID = :locationId)
			AND (:itemId IS NULL OR sm.item_id = :itemId)
			         AND (
			         :search IS NULL
			         OR LOWER(i.item_name) LIKE LOWER(CONCAT('%', :search, '%'))
			         OR LOWER(i.internal_code) LIKE LOWER(CONCAT('%', :search, '%'))
			         OR LOWER(i.barcode) LIKE LOWER(CONCAT('%', :search, '%'))
			        )
			ORDER BY sm.movement_date DESC
			""", countQuery = """
			SELECT COUNT(*)
			FROM INVENTORY_STOCK_MOVEMENT sm
			JOIN core_item i ON sm.item_id = i.id
			WHERE (
			          sm.FROM_LOCATION_ID = :locationId
			          OR sm.TO_LOCATION_ID = :locationId
			         )
			    AND (:itemId IS NULL OR sm.item_id = :itemId)
			    AND (
			            :search IS NULL
			         OR LOWER(i.item_name) LIKE LOWER(CONCAT('%', :search, '%'))
			         OR LOWER(i.internal_code) LIKE LOWER(CONCAT('%', :search, '%'))
			         OR LOWER(i.barcode) LIKE LOWER(CONCAT('%', :search, '%'))
			        )
			""", nativeQuery = true)
	Page<StockMovementListView> findMovementByItemAndLocations(@Param("itemId") UUID itemId, @Param("locationId") UUID locationId,
			@Param("search") String search, Pageable pageable);

}
