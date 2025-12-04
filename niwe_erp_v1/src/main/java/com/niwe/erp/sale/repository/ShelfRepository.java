package com.niwe.erp.sale.repository;
 

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.niwe.erp.sale.domain.Shelf;


public interface ShelfRepository extends JpaRepository<Shelf, UUID> {

	Optional<Shelf> findByInternalCode(String shelfCode);
    
}

