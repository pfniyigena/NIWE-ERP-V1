package com.niwe.erp.core.domain;

import com.niwe.erp.common.domain.AbstractEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "CORE_ITEM_CATEGORY")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ItemCategory  extends AbstractEntity{/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * The internalCode
	 */
	@Column(name = "INTERNAL_CODE", nullable = false, unique = true)
	private String internalCode;
	/**
	 * The internalCode
	 */
	@Column(name = "EXTERNAL_ID",unique = true)
	private int externalId;
	/**
	 * The tinNumber
	 */
	@Column(name = "CATEGORY_NAME", nullable = false,unique = true)
	private String categoryName;

}
