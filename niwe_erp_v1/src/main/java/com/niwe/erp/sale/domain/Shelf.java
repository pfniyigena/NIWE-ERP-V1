package com.niwe.erp.sale.domain;

import java.time.LocalDateTime;

import com.niwe.erp.common.domain.AbstractEntity;
import com.niwe.erp.inventory.domain.Warehouse;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
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
@Table(name = "INVENTORY_SHELF")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Shelf extends AbstractEntity {

	/**
	 * The serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * The internalCode
	 */
	@Column(name = "INTERNAL_CODE", nullable = false)
	private String internalCode;
	/**
	 * The name
	 */
	@Column(name = "NAME")
	private String name;
	
	/**
	 * The description
	 */
	@Column(name = "description")
	private String description;
	/**
	 * The warehouse
	 */
	@ManyToOne
	@JoinColumn(name = "WAREHOUSE_ID")
	private Warehouse warehouse;
	/**
	 * The lastSyn
	 */
	@Column(name = "LAST_SYN")
	@Builder.Default
	private LocalDateTime lastSyn=LocalDateTime.now();
	@Transient
	@Builder.Default
	private Long numberOfProductsNotSynchronized=0L;
	
}
