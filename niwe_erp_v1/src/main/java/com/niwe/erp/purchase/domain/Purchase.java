package com.niwe.erp.purchase.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.niwe.erp.common.domain.AbstractEntity;
import com.niwe.erp.core.domain.CoreTaxpayer;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@Table(name = "PURCHASE_PURCHASE")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Purchase extends AbstractEntity {
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
	 * The supplierName
	 */
	@Column(name = "SUPPLIER_NAME")
	private String supplierName;

	/**
	 * The confirmedBy
	 */
	@Column(name = "CONFIRMED_BY")
	private String confirmedBy;

	/**
	 * The confirmedAt
	 */
	@Column(name = "CONFIRMED_AT")
	private LocalDateTime confirmedAt;

	/**
	 * The purchaseDate
	 */
	@Column(name = "PURCHASE_DATE")
	private LocalDateTime purchaseDate;

	/**
	 * The items
	 */
	@OneToMany(mappedBy = "purchase", cascade = CascadeType.ALL)
	@Builder.Default
	@ToString.Exclude
	private List<PurchaseItem> items = new ArrayList<>();
	/**
	 * The received
	 */
	@Column(name = "RECEIVED")
	@Builder.Default
	private Boolean received = Boolean.FALSE; // to track if converted to received goods

	/**
	 * The Taxpayer
	 */
	@ManyToOne
	@JoinColumn(name = "TAXPAYER_ID", nullable = false)
	private CoreTaxpayer taxpayer;
	/**
	 * The status
	 */
	@Column(name = "PURCHASE_STATUS", nullable = true)
	@Builder.Default
	private PurchaseStatus status = PurchaseStatus.DRAFT;
}
