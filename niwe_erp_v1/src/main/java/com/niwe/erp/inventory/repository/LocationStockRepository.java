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
import com.niwe.erp.inventory.domain.InventoryLocation;
import com.niwe.erp.inventory.domain.LocationStock;
import com.niwe.erp.inventory.web.view.LocationStockView;
import com.niwe.erp.inventory.web.view.OutflowItemListView;

import jakarta.persistence.LockModeType;

public interface LocationStockRepository extends JpaRepository<LocationStock, UUID> {
	Optional<LocationStock> findByLocationAndItem(InventoryLocation location, CoreItem item);
	Optional<LocationStock> findByLocationIdAndItemId(UUID locationId, UUID itemId);
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("select ls from LocationStock ls where ls.location.id = :locationId and ls.item.id = :itemId")
	Optional<LocationStock> findByLocationAndItemForUpdate(UUID locationId, UUID itemId);
	@Query("""
			    SELECT ls
			    FROM LocationStock ls
			    WHERE ls.location.warehouse.id = :warehouseId
			      AND ls.item.id = :itemId
			      AND ls.quantity > 0
			    ORDER BY ls.modifiedAt ASC
			""")
	List<LocationStock> findLocationsFIFO(UUID warehouseId, UUID itemId);
	@Query("""
			SELECT ls FROM LocationStock ls
			WHERE ls.item.id = :itemId
			  AND ls.location.warehouse.id = :warehouseId
			  AND ls.quantity > 0
			ORDER BY ls.createdAt DESC
			""")
	List<LocationStock> findAvailableByWarehouseAndItem(UUID warehouseId, UUID itemId);
	@Query("SELECT s FROM LocationStock s WHERE s.item.id = :itemId")
	List<LocationStock> findByItemId(@Param("itemId") UUID itemId);
	@Query(value = """
			SELECT
			    p.id AS itemId,
			    p.item_name AS itemName,
			    p.internal_code AS itemCode,
			    p.barcode AS barcode,
			    p.unit_price AS unitPrice,
			    p.unit_cost AS unitCost,
			    COALESCE(ls.quantity, 0) AS quantity,
			    ls.location_id AS locationId,
			    ls.location_name AS locationName
			FROM core_item p
			LEFT JOIN inventory_location_stock ls
			    ON ls.item_id = p.id
			    WHERE (:itemName IS NULL OR LOWER(p.item_name) LIKE LOWER(CONCAT('%', :itemName, '%')))
			       OR (:itemCode IS NULL OR LOWER(p.item_code) LIKE LOWER(CONCAT('%', :itemCode, '%')))
			       OR (:internalCode IS NULL OR LOWER(p.internal_code) LIKE LOWER(CONCAT('%', :internalCode, '%')))
			       OR (:barcode IS NULL OR LOWER(p.barcode) LIKE LOWER(CONCAT('%', :barcode, '%')))

			ORDER BY p.item_name ASC
			""", countQuery = """
			     SELECT COUNT(*)
			     FROM core_item p
			LEFT JOIN inventory_location_stock ls
			    ON ws.item_id = p.id
			    WHERE (:itemName IS NULL OR LOWER(p.item_name) LIKE LOWER(CONCAT('%', :itemName, '%')))
			       OR (:itemCode IS NULL OR LOWER(p.item_code) LIKE LOWER(CONCAT('%', :itemCode, '%')))
			       OR (:internalCode IS NULL OR LOWER(p.internal_code) LIKE LOWER(CONCAT('%', :internalCode, '%')))
			       OR (:barcode IS NULL OR LOWER(p.barcode) LIKE LOWER(CONCAT('%', :barcode, '%')))
			     """, nativeQuery = true)

	Page<OutflowItemListView> findAllItemsWithLocationStock(String itemName, String itemCode, String internalCode,
			String barcode, Pageable pageable);

	@Query(value = """
			SELECT
			    ci.id                    AS itemId,
			    ci.item_name             AS itemName,
			    ci.internal_code         AS itemCode,
			    ci.barcode               AS barcode,
			    ci.unit_price            AS unitPrice,
			    ci.unit_cost             AS unitCost,
			    COALESCE(ls.quantity, 0) AS quantity,
			    loc.id                   AS locationId,
			    wh.id                    AS warehouseId,
			    loc.location_code        AS locationCode,
			    loc.location_name        AS locationName,
			    loc.priority             AS priority
			FROM core_item ci
			CROSS JOIN inventory_location loc
			JOIN inventory_warehouse wh ON loc.warehouse_id = wh.id        -- 99% of projects
			-- JOIN warehouse wh ON loc.warehouse_id = wh.id               -- try this if above fails
			LEFT JOIN inventory_location_stock ls
			    ON ls.item_id = ci.id
			   AND ls.location_id = loc.id
			WHERE wh.id = :warehouseId
			  AND (
			        :search IS NULL
			     OR LOWER(ci.item_name)     LIKE LOWER(CONCAT('%', :search, '%'))
			     OR LOWER(ci.internal_code) LIKE LOWER(CONCAT('%', :search, '%'))
			     OR LOWER(ci.barcode)       LIKE LOWER(CONCAT('%', :search, '%'))
			  )
			ORDER BY loc.priority ASC, loc.location_code ASC, ci.item_name ASC
			""", countQuery = """
			SELECT COUNT(*)
			FROM core_item ci
			CROSS JOIN inventory_location loc
			JOIN inventory_warehouse wh ON loc.warehouse_id = wh.id
			LEFT JOIN inventory_location_stock ls
			    ON ls.item_id = ci.id AND ls.location_id = loc.id
			WHERE wh.id = :warehouseId
			  AND (:search IS NULL
			    OR LOWER(ci.item_name)     LIKE LOWER(CONCAT('%', :search, '%'))
			    OR LOWER(ci.internal_code) LIKE LOWER(CONCAT('%', :search, '%'))
			    OR LOWER(ci.barcode)       LIKE LOWER(CONCAT('%', :search, '%')))
			""", nativeQuery = true)
	Page<OutflowItemListView> findOutflowItems(@Param("warehouseId") UUID warehouseId, @Param("search") String search,
			Pageable pageable);

	@Query("""
			    SELECT l.id AS locationId,
			           ls.quantity AS availableQty,
			           l.createdAt AS createdAt
			    FROM InventoryLocation l
			    JOIN LocationStock ls ON ls.location.id = l.id
			    WHERE l.warehouse.id = :warehouseId
			      AND ls.item.id = :itemId
			    ORDER BY l.createdAt ASC
			""")
	List<LocationStockView> getAvailableLocationsFIFO(UUID warehouseId, UUID itemId);
}
