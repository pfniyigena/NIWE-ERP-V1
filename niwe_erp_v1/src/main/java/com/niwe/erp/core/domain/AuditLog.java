package com.niwe.erp.core.domain;

import java.util.UUID;

import com.niwe.erp.common.domain.AbstractEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name = "CORE_AUDIT_LOG")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class AuditLog extends AbstractEntity {
	/**
	 * The serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	@Column(name = "ENTITY_NAME")
	private String entityName;
	@Column(name = "ENTITY_ID")
	private UUID entityId;
	@Column(name = "ACTION")
	@Enumerated(EnumType.STRING)
	private AuditAction action; // CREATE, UPDATE, DELETE
	@Column(name = "ENTITY_DATA", columnDefinition = "TEXT")
	private String entityData; // JSON snapshot

}
