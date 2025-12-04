package com.niwe.erp.inventory.domain;

import java.math.BigDecimal;
import java.time.Instant;

import com.niwe.erp.common.domain.AbstractEntity;
import com.niwe.erp.core.domain.CoreItem;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
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
@Table(name = "INVENTORY_WAREHOUSE_STOCK", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "WAREHOUSE_ID", "ITEM_ID" }) })
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class WarehouseStock extends AbstractEntity {
	/**
	 * The serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	@Column(name = "QUANTITY", nullable = false)
	@Builder.Default
	private BigDecimal quantity = BigDecimal.ZERO;
	/**
	 * The item
	 */
	@ManyToOne
	@JoinColumn(name = "ITEM_ID", nullable = false)
	private CoreItem item;
	/**
	 * The warehouse
	 */
	@ManyToOne
	@JoinColumn(name = "WAREHOUSE_ID", nullable = false)
	private Warehouse warehouse;

	/**
	 * The receivedDate
	 */
	@Column(name = "RECEIVED_DATE")
	private Instant receivedDate;

}
