package com.niwe.erp.core.domain;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.SQLRestriction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.niwe.erp.common.domain.AbstractEntity;
import com.niwe.erp.invoicing.domain.TaxType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
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
@Table(name = "CORE_ITEM")
@SQLRestriction("deleted = false")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class CoreItem extends AbstractEntity {
	/**
	 * The serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * The externalId
	 */
	@Column(name = "EXTERNAL_ID")
	private int externalId;
	/**
	 * The itemName
	 */
	@Column(name = "ITEM_NAME", nullable = false)
	private String itemName;

	@Column(name = "ITEM_CODE", nullable = true)
	private String itemCode;

	/**
	 * The externalItemCode
	 */
	@Column(name = "EXTERNAL_ITEM_CODE", nullable = true, length = 50)
	private String externalItemCode;
	/**
	 * The internalCode
	 */
	@Column(name = "INTERNAL_CODE", unique = true, nullable = false)
	private String internalCode;
	/**
	 * The barcode
	 */
	@Column(name = "BARCODE")
	private String barcode;

	/**
	 * The sku
	 */
	@Column(name = "SKU")
	private String sku;
	/**
	 * The description
	 */
	@Column(name = "ITEM_DESCRITION", nullable = true)
	private String description;
	/**
	 * The unitPrice
	 */
	@Column(name = "UNIT_PRICE", nullable = true)
	@Builder.Default
	private BigDecimal unitPrice = BigDecimal.ZERO;
	/**
	 * The unitCost
	 */
	@Column(name = "UNIT_COST", nullable = true)
	@Builder.Default
	private BigDecimal unitCost = BigDecimal.ZERO;
	/**
	 * The unitPrice
	 */
	@Column(name = "QUANTITY_INITIAL", nullable = true)
	@Builder.Default
	private BigDecimal quanityInitial = BigDecimal.ZERO;
	/**
	 * The tax
	 */
	@ManyToOne
	@JoinColumn(name = "TAX_TYPE_ID")
	private TaxType tax;
	/**
	 * The itemNature
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ITEM_NATURE_ID")
	@JsonIgnore
	private CoreItemNature nature;
	/**
	 * The classification
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ITEM_CLASSIFICATION_ID")
	@JsonIgnore
	private CoreItemClassification classification;

	/**
	 * The taxpayer
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "QUANTITY_UNIT_ID", nullable = true)
	@JsonIgnore
	private CoreQuantityUnit unit;

	/**
	 * The country
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COUNTY_ID", nullable = true)
	@JsonIgnore
	private CoreCountry country;

	/**
	 * The brand
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CATEGORY_ID", nullable = true)
	@JsonIgnore
	private ItemCategory category;
	/**
	 * The brand
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BRAND_ID", nullable = true)
	@JsonIgnore
	private ItemBrand brand;

	/**
	 * The taxpayer
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TAXPAYER_ID", nullable = true)
	@JsonIgnore
	private CoreTaxpayer taxpayer;
	/**
	 * The lastUpdated
	 */
	@Column(name = "LAST_UPDATED")
	private LocalDateTime lastUpdated;
	/**
	 * The expirationDate
	 */
	@Column(name = "EXPIRATION_DATE")
	private LocalDate expirationDate;
	/**
	 * The stockLevel
	 */
	@Column(name = "STOCK_LEVEL")
	@Builder.Default
	private Integer stockLevel = 0;
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

	// Copy constructor
	public CoreItem(CoreItem copy) {
		this.itemName = copy.getItemName();
		this.externalItemCode = copy.getExternalItemCode();
		this.classification = copy.getClassification();
		this.unitPrice = copy.getUnitPrice();
		this.unitCost = copy.getUnitCost();
		this.nature = copy.getNature();
		this.unit = copy.getUnit();
		this.country = copy.getCountry();
		this.taxpayer = copy.getTaxpayer();
		this.tax = copy.getTax();

	}

	@PrePersist
	@PreUpdate
	public void updateTimestamp() {
		this.lastUpdated = LocalDateTime.now();
	}
}
