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

public interface CoreItemRepository extends JpaRepository<CoreItem, UUID> {

	Optional<CoreItem> findByInternalCode(String internalCode);

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
			    JOIN b.tax t
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
			    JOIN b.tax t
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
			    WHERE (:itemName IS NULL OR LOWER(b.itemName) LIKE LOWER(CONCAT('%', :itemName, '%')))
			       OR (:itemCode IS NULL OR LOWER(b.itemCode) LIKE LOWER(CONCAT('%', :itemCode, '%')))
			       OR (:internalCode IS NULL OR LOWER(b.internalCode) LIKE LOWER(CONCAT('%', :internalCode, '%')))
			       OR (:barcode IS NULL OR LOWER(b.barcode) LIKE LOWER(CONCAT('%', :barcode, '%')))
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
			       i.id, i.itemName, i.itemCode,i.barcode, i.unitPrice,i.unitCost, i.tax.id,i.tax.taxCode,i.tax.taxValue, i.nature.id, i.classification.id)
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
			    WHERE b.lastUpdated > :lastUpdated
			""")
	Page<CoreItemListDTO> findByLastUpdatedAfter(@Param("lastUpdated") LocalDateTime lastUpdated, Pageable pageable);

}
