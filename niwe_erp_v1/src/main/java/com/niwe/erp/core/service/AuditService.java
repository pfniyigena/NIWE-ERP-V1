package com.niwe.erp.core.service;

import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.niwe.erp.core.domain.AuditAction;
import com.niwe.erp.core.domain.AuditLog;
import com.niwe.erp.core.repository.AuditLogRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuditService {

    private final AuditLogRepository auditLogRepository;
    private final ObjectMapper objectMapper;

    public void logDelete(String entityName, UUID entityId, Object entity, String user) {
        try {
            String json = objectMapper.writeValueAsString(entity);

            AuditLog log = AuditLog.builder()
                    .entityName(entityName)
                    .entityId(entityId)
                    .action(AuditAction.DELETE)
                    .entityData(json)
                    .createdAt(Instant.now())
                    .createdBy(user)
                    .build();

            auditLogRepository.save(log);

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Audit logging failed", e);
        }
    }
}
