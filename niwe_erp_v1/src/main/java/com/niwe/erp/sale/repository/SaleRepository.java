package com.niwe.erp.sale.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.niwe.erp.sale.domain.Sale;
import com.niwe.erp.sale.view.SaleListView;

public interface SaleRepository extends JpaRepository<Sale, UUID> {

	Optional<Sale> findByExternalCode(String externalCode);

	List<Sale> findBySummaryIdOrderBySaleDateDesc(UUID summaryId);

	List<Sale> findBySaleDateBetween(Instant start, Instant end);

	@Query(value = """
			SELECT
			    s.ID AS saleId,
			    s.EXTERNAL_CODE AS externalCode,
			    s.INTERNAL_CODE AS internalCode,
			    s.CUSTOMER_NAME  AS customerName,
			    s.CUSTOMER_TIN AS customerTin,
			    s.SALE_DATE AS saleDate,
			    s.TRANSACTION_TYPE AS transactionType,
			    s.SALE_STATUS AS status,
			    COALESCE(s.TOTAL_AMOUNT_TO_PAY, 0) AS totalAmountToPay
			FROM SALE_SALE s
			    WHERE (:externalCode IS NULL OR LOWER(s.EXTERNAL_CODE) LIKE LOWER(CONCAT('%', :externalCode, '%')))
			       OR (:internalCode IS NULL OR LOWER(s.INTERNAL_CODE) LIKE LOWER(CONCAT('%', :internalCode, '%')))
			       OR (:customerName IS NULL OR LOWER(s.CUSTOMER_NAME) LIKE LOWER(CONCAT('%', :customerName, '%')))
			       OR (:customerTin IS NULL OR LOWER(s.CUSTOMER_TIN) LIKE LOWER(CONCAT('%', :customerTin, '%')))

			ORDER BY s.SALE_DATE ASC
			""", countQuery = """
			 SELECT COUNT(*)
			FROM SALE_SALE s
			WHERE (:externalCode IS NULL OR LOWER(s.EXTERNAL_CODE) LIKE LOWER(CONCAT('%', :externalCode, '%')))
			   OR (:internalCode IS NULL OR LOWER(s.INTERNAL_CODE) LIKE LOWER(CONCAT('%', :internalCode, '%')))
			   OR (:customerName IS NULL OR LOWER(s.CUSTOMER_NAME) LIKE LOWER(CONCAT('%', :customerName, '%')))
			   OR (:customerTin IS NULL OR LOWER(s.CUSTOMER_TIN) LIKE LOWER(CONCAT('%', :customerTin, '%')))
			 """, nativeQuery = true)

	Page<SaleListView> findAllSales(@Param("externalCode") String externalCode,
			@Param("internalCode") String internalCode, @Param("customerName") String customerName,
			@Param("customerTin") String customerTin, Pageable pageable);

}
