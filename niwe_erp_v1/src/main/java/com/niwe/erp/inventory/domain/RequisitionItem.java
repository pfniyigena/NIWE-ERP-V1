package com.niwe.erp.inventory.domain;

import java.math.BigDecimal;

import com.niwe.erp.common.domain.AbstractEntity;
import com.niwe.erp.core.domain.CoreItem;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
@Table(name = "INVENTORY_STOCK_REQUISITION_ITEM")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class RequisitionItem extends AbstractEntity {
	/**
	 * The serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The requestedQuantity
	 */
	@Column(name = "REQUESTED_QUANTITY", nullable = true)
	@Builder.Default
	private BigDecimal requestedQuantity = BigDecimal.ZERO;
	/**
	 * The approvedQuantity
	 */
	@Column(name = "APPROVED_QUANTITY", nullable = true)
	@Builder.Default
	private BigDecimal approvedQuantity = BigDecimal.ZERO;
	/**
	 * The issuedQuantity
	 */
	@Column(name = "ISSUED_QUANTITY", nullable = true)
	@Builder.Default
	private BigDecimal issuedQuantity = BigDecimal.ZERO;
	/**
	 * The item
	 */
	@ManyToOne
	@JoinColumn(name = "ITEM_ID", nullable = false)
	private CoreItem item;
}
