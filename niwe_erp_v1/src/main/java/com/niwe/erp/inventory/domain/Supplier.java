package com.niwe.erp.inventory.domain;

import java.time.Instant;

import org.hibernate.annotations.SQLRestriction;

import com.niwe.erp.common.domain.AbstractEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name = "INVENTORY_SUPPLIER")
@SQLRestriction("deleted = false")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Supplier extends AbstractEntity {
	/**
	 * The internalCode
	 */
	@Column(name = "INTERNAL_CODE", unique = true, nullable = false)
	private String internalCode;
	/**
	 * The customerName
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * The customerName
	 */
	@NotBlank
	@Column(name = "SUPPLIER_NAME", nullable = false)
	private String supplierName;
	/**
	 * The customerTin
	 */
	@Column(name = "SUPPLIER_TIN", unique = true, nullable = true)
	private String supplierTin;
	/**
	 * The customerPhone
	 */
	@Column(name = "SUPPLIER_PHONE", nullable = true)
	private String supplierPhone;
	/**
	 * The deleted
	 */
	@Column(name = "DELETED", nullable = false)
	@Builder.Default
	private boolean deleted = false;
	/**
	 * The deletedAt
	 */
	@Column(name = "DELETED_AT")
	private Instant deletedAt;
	/**
	 * The deletedBy
	 */
	@Column(name = "DELETED_BY")
	private String deletedBy;
}
