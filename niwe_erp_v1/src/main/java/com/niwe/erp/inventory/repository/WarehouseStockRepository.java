package com.niwe.erp.inventory.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.niwe.erp.core.domain.CoreItem;
import com.niwe.erp.inventory.domain.Warehouse;
import com.niwe.erp.inventory.domain.WarehouseStock;
import com.niwe.erp.inventory.web.dto.ProductStockValuationDto;
import com.niwe.erp.inventory.web.dto.StockValuationProjection;
import com.niwe.erp.inventory.web.view.InflowItemListView;

import jakarta.persistence.LockModeType;

public interface WarehouseStockRepository extends JpaRepository<WarehouseStock, UUID> {
	Optional<WarehouseStock> findByWarehouseAndItem(Warehouse warehouse, CoreItem item);

	Optional<WarehouseStock> findByWarehouseIdAndItemId(UUID warehouseId, UUID itemId);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("select ws from WarehouseStock ws where ws.warehouse.id = :warehouseId and ws.item.id = :itemId")
	Optional<WarehouseStock> findByWarehouseAndItemForUpdate(UUID warehouseId, UUID itemId);

	@Query("SELECT s FROM WarehouseStock s WHERE s.item.id = :itemId")
	List<WarehouseStock> findByItemId(@Param("itemId") UUID itemId);

	@Query("""
			    SELECT
			        i.id AS itemId,
			        i.itemCode AS itemCode,
			        i.itemName AS itemName,
			        SUM(ws.quantity) AS totalQuantity,
			        i.unitCost AS unitCost
			    FROM WarehouseStock ws
			    JOIN ws.item i
			    GROUP BY i.id, i.itemCode, i.itemName, i.unitCost
			""")
	List<StockValuationProjection> findValuationSummary();

	@Query(value = """
			SELECT
			    i.id AS itemId,
			    i.item_code AS itemCode,
			    i.item_name AS itemName,
			    SUM(ws.totalquantity) AS totalQuantity,
			    i.unit_cost AS unitCost
			FROM mv_stock_valuation ws
			JOIN core_item i ON i.id = ws.itemid
			GROUP BY i.id, i.item_code, i.item_name, i.unit_cost
			ORDER BY i.item_name
			""", nativeQuery = true)

	List<StockValuationProjection> findValuationSummaryV2();

	// This does the grouping + sum in DATABASE → returns only 1 row per product
	@Query("""
			SELECT new com.niwe.erp.inventory.web.dto.ProductStockValuationDto(
			    i.id,
			    i.itemCode,
			    i.itemName,
			    SUM(s.quantity),
			    i.unitCost,
			    i.unitCost * SUM(s.quantity)
			)
			FROM WarehouseStock s
			JOIN s.item i
			GROUP BY i.id, i.itemCode, i.itemName, i.unitCost
			""")
	Page<ProductStockValuationDto> aggregateStockValuation(Pageable pageable);

	// This does the grouping + sum in DATABASE → returns only 1 row per product
	@Query("""
			SELECT new com.niwe.erp.inventory.web.dto.ProductStockValuationDto(
			    i.id,
			    i.itemCode,
			    i.itemName,
			    SUM(s.quantity),
			    i.unitCost,
			    i.unitCost * SUM(s.quantity)
			)
			FROM WarehouseStock s
			JOIN s.item i
			WHERE (:name IS NULL OR LOWER(i.itemName) LIKE LOWER(CONCAT('%', CAST(:name AS string), '%')))
			OR (:code IS NULL OR LOWER(i.itemCode) LIKE LOWER(CONCAT('%', CAST(:code AS string), '%')))
			GROUP BY i.id, i.itemCode, i.itemName, i.unitCost
			""")
	Page<ProductStockValuationDto> aggregateStockValuation(@Param("name") String name, @Param("code") String code,
			Pageable pageable);

	// For correct pagination total count
	@Query("SELECT COUNT(DISTINCT s.item) FROM WarehouseStock s")
	long countDistinctProducts();

	@Query(value = """
			SELECT
			    p.id AS itemId,
			    p.item_name AS itemName,
			    p.internal_code AS itemCode,
			    p.barcode AS barcode,
			    p.unit_price AS unitPrice,
			    p.unit_cost AS unitCost,
			    COALESCE(ws.quantity, 0) AS quantity,
			    ws.warehouse_id AS warehouseId
			FROM core_item p
			LEFT JOIN inventory_warehouse_stock ws
			    ON ws.item_id = p.id
			    AND ws.warehouse_id = :warehouseId
			    WHERE (:itemName IS NULL OR LOWER(p.item_name) LIKE LOWER(CONCAT('%', :itemName, '%')))
			       OR (:itemCode IS NULL OR LOWER(p.item_code) LIKE LOWER(CONCAT('%', :itemCode, '%')))
			       OR (:internalCode IS NULL OR LOWER(p.internal_code) LIKE LOWER(CONCAT('%', :internalCode, '%')))
			       OR (:barcode IS NULL OR LOWER(p.barcode) LIKE LOWER(CONCAT('%', :barcode, '%')))

			ORDER BY p.item_name ASC
			""", countQuery = """
			     SELECT COUNT(*)
			     FROM core_item p
			LEFT JOIN inventory_warehouse_stock ws
			    ON ws.item_id = p.id
			    AND ws.warehouse_id = :warehouseId
			    WHERE (:itemName IS NULL OR LOWER(p.item_name) LIKE LOWER(CONCAT('%', :itemName, '%')))
			       OR (:itemCode IS NULL OR LOWER(p.item_code) LIKE LOWER(CONCAT('%', :itemCode, '%')))
			       OR (:internalCode IS NULL OR LOWER(p.internal_code) LIKE LOWER(CONCAT('%', :internalCode, '%')))
			       OR (:barcode IS NULL OR LOWER(p.barcode) LIKE LOWER(CONCAT('%', :barcode, '%')))
			     """, nativeQuery = true)

	Page<InflowItemListView> findAllItemsWithWarehouseStock(UUID warehouseId, String itemName, String itemCode,
			String internalCode, String barcode, Pageable pageable);

}
