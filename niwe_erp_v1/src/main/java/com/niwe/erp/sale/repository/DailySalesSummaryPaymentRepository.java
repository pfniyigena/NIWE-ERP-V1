package com.niwe.erp.sale.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.niwe.erp.sale.domain.DailySalesSummaryPayment;

public interface DailySalesSummaryPaymentRepository extends JpaRepository<DailySalesSummaryPayment, UUID>{

	Optional<DailySalesSummaryPayment> findBySummaryIdAndPaymentMethodId(
            UUID summaryId, UUID paymentMethodId
    );

}
