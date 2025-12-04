package com.niwe.erp.inventory.domain;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.niwe.erp.common.domain.AbstractEntity;
import com.niwe.erp.core.domain.CoreTaxpayer;
import com.niwe.erp.purchase.domain.Purchase;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
@Table(name = "INVENTORY_GOOD_RECEIVED_NOTE")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class GoodReceivedNote extends AbstractEntity {
	/**
	 * The serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * The itemNumber
	 */
	@Column(name = "ITEM_NUMBER", nullable = false)
	private Integer itemNumber;
	/**
	 * The internalCode
	 */
	@Column(name = "INTERNAL_CODE", nullable = false, unique = true)
	private String internalCode;
	/**
	 * The receivedDate
	 */
	@Column(name = "RECEIVED_DATE")
	@Builder.Default
	private Instant receivedDate = Instant.now();
	/**
	 * The receivedBy
	 */
	@Column(name = "RECEIVED_BY")
	private String receivedBy;
	/**
	 * The receivedBy
	 */
	@Column(name = "SUPPLIER_NAME")
	private String supplierName;

	/**
	 * The items
	 */
	@OneToMany(mappedBy = "receivedGood", cascade = CascadeType.ALL)
	@Builder.Default
	@ToString.Exclude
	private List<ReceivedItem> items = new ArrayList<>();
	/**
	 * The warehouse
	 */
	@ManyToOne
	@JoinColumn(name = "WAREHOUSE_ID", nullable = true)
	private Warehouse warehouse;
	/**
	 * The purchase
	 */
	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "PURCHASE_ID", referencedColumnName = "ID")
	private Purchase purchase;
	
	/**
	 * The stockOperation
	 */
	@Column(name = "STOCK_STATUS")
	@lombok.ToString.Include
	@Builder.Default
	private EStockReceivedStatus status=EStockReceivedStatus.WAITING;
	
	/**
	 * The Taxpayer
	 */
	@ManyToOne
	@JoinColumn(name = "TAXPAYER_ID", nullable = false)
	private CoreTaxpayer taxpayer;
}
