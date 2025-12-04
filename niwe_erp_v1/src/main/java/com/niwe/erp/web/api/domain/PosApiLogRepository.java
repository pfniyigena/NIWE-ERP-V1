package com.niwe.erp.web.api.domain;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PosApiLogRepository extends JpaRepository<PosApiLog, UUID> {
    List<PosApiLog> findByDeviceIdOrderByRequestTimeDesc(String deviceId);
    Page<PosApiLog> findAll(Pageable pageable);
}