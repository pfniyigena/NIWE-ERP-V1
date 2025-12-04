package com.niwe.erp.inventory.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.niwe.erp.inventory.domain.GoodReceivedNote;

public interface ReceivedGoodRepository extends JpaRepository<GoodReceivedNote, UUID> {
	
}
