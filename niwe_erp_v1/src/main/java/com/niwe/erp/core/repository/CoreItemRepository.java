package com.niwe.erp.core.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.niwe.erp.core.domain.CoreItem;
import com.niwe.erp.core.form.CoreItemForm;
import com.niwe.erp.core.dto.CoreItemListDTO;
import com.niwe.erp.core.view.CoreItemListView;
import com.niwe.erp.core.view.DeadStockItem;
import com.niwe.erp.core.view.FastMoveItem;

public interface CoreItemRepository extends JpaRepository<CoreItem, UUID> {

	Optional<CoreItem> findByInternalCode(String internalCode);
	boolean existsByBarcodeAndDeletedFalse(String barcode);
	List<CoreItem> findByItemNameContainingIgnoreCaseOrItemCodeContainingIgnoreCaseOrBarcodeContainingIgnoreCase(
			String itemName, String itemCode, String barcode);

	@Query("""
			    SELECT new com.niwe.erp.core.form.CoreItemForm(
			    b.id,
			    b.internalCode,
			    b.itemName,
				b.itemCode,
				b.barcode,
				b.externalItemCode,
				b.unitPrice,
				b.unitCost,
			    t.taxCode,
			    t.taxValue
			    )
			    FROM CoreItem b
			    JOIN b.tax t where b.deleted=false
			""")
	List<CoreItemForm> findAllAsForm();

	@Query("""
			    SELECT new com.niwe.erp.core.form.CoreItemForm(
			    b.id,
			    b.internalCode,
			    b.itemName,
				b.itemCode,
				b.barcode,
				b.externalItemCode,
				b.unitPrice,
				b.unitCost,
			    t.taxCode,
			    t.taxValue
			    )
			    FROM CoreItem b
			    JOIN b.tax t WHERE b.deleted=false
			""")
	Page<CoreItemForm> findAllAsForm(Pageable pageable);

	@Query("""
			     SELECT new com.niwe.erp.core.form.CoreItemForm(
			    b.id,
			    b.internalCode,
			    b.itemName,
				b.itemCode,
				b.barcode,
				b.externalItemCode,
				b.unitPrice,
				b.unitCost,
			    t.taxCode,
			    t.taxValue
			    )
			    FROM CoreItem b
			    JOIN b.tax t
			    WHERE ((:itemName IS NULL OR LOWER(b.itemName) LIKE LOWER(CONCAT('%', :itemName, '%')))
			       OR (:itemCode IS NULL OR LOWER(b.itemCode) LIKE LOWER(CONCAT('%', :itemCode, '%')))
			       OR (:internalCode IS NULL OR LOWER(b.internalCode) LIKE LOWER(CONCAT('%', :internalCode, '%')))
			       OR (:barcode IS NULL OR LOWER(b.barcode) LIKE LOWER(CONCAT('%', :barcode, '%')))) AND
			       b.deleted=false
			""")
	List<CoreItemForm> findAllAsFormByItemNameContainingIgnoreCaseOrItemCodeContainingIgnoreCaseOrBarcodeContainingIgnoreCase(
			@Param("itemName") String itemName, @Param("itemCode") String itemCode,
			@Param("internalCode") String internalCode, @Param("barcode") String barcode);

	@Query("""
			SELECT i FROM CoreItem i
			WHERE
			    LOWER(i.itemName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
			    OR LOWER(i.itemCode) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
			    OR LOWER(i.barcode) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
			""")
	Page<CoreItem> searchItems(@Param("searchTerm") String searchTerm, Pageable pageable);

	@Query("""
			SELECT new com.niwe.erp.core.view.CoreItemListView(
			       i.id, i.itemName, i.itemCode,i.barcode, i.unitPrice,i.unitCost, i.tax.id,i.tax.taxCode,i.tax.taxValue, i.nature.id, i.classification.id,i.modifiedAt)
			FROM CoreItem i
			WHERE (:name IS NULL OR LOWER(i.itemName) LIKE LOWER(CONCAT('%', CAST(:name AS string), '%')))
			OR (:code IS NULL OR LOWER(i.itemCode) LIKE LOWER(CONCAT('%', CAST(:code AS string), '%')))
			""")
	Page<CoreItemListView> findAllFiltered(@Param("name") String name, @Param("code") String code, Pageable pageable);

	@Query("""
			    SELECT new com.niwe.erp.core.dto.CoreItemListDTO(
			    b.internalCode,
				b.itemCode,
				b.externalItemCode,
				b.itemName,
				b.barcode,
				b.unitPrice,
				b.unitCost,
			    t.taxCode,
			    t.taxValue,
			    c.code,
			    u.code,
			    p.code
			    )
			    FROM CoreItem b
			    JOIN b.tax t JOIN b.classification c JOIN b.unit u JOIN b.country p
			    WHERE b.deleted=false
			""")
	Page<CoreItemListDTO> findAllAsDto(Pageable pageable);

	@Query("""
			    SELECT new com.niwe.erp.core.dto.CoreItemListDTO(
			        b.internalCode,
			        b.itemCode,
			        b.externalItemCode,
			        b.itemName,
			        b.barcode,
			        b.unitPrice,
			        b.unitCost,
			        t.taxCode,
			        t.taxValue,
			        c.code,
			        u.code,
			        p.code
			    )
			    FROM CoreItem b
			    JOIN b.tax t
			    JOIN b.classification c
			    JOIN b.unit u
			    JOIN b.country p
			    WHERE b.lastUpdated > :lastUpdated AND b.deleted=false
			""")
	Page<CoreItemListDTO> findByLastUpdatedAfter(@Param("lastUpdated") LocalDateTime lastUpdated, Pageable pageable);

	@Query(value = "SELECT p.ITEM_NAME AS itemName, SUM(ii.QUANTITY) AS sold FROM SALE_SALE_ITEM ii JOIN CORE_ITEM p ON ii.ITEM_ID = p.id GROUP BY p.ITEM_NAME ORDER BY sold DESC LIMIT 10", nativeQuery = true)
	List<FastMoveItem> findFastMoveItems();
	@Query(value = "SELECT p.ITEM_NAME AS itemName, MAX(i.SALE_DATE) AS lastSold FROM SALE_SALE_ITEM ii JOIN SALE_SALE i ON ii.SALE_ID = i.id JOIN CORE_ITEM p ON ii.ITEM_ID = p.id GROUP BY p.ITEM_NAME HAVING MAX(i.SALE_DATE) < CURRENT_DATE - INTERVAL '6 days'", nativeQuery = true)
	List<DeadStockItem> findDeadStockItems();
}
