package com.niwe.erp.inventory.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

 
import com.niwe.erp.inventory.domain.StockRequisition;

public interface StockRequisitionRepository  extends JpaRepository<StockRequisition, UUID>{

}
