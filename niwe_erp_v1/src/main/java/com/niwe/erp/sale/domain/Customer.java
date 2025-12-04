package com.niwe.erp.sale.domain;

import com.niwe.erp.common.domain.AbstractEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
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
@Table(name = "SALE_CUSTOMER")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Customer extends AbstractEntity {
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
	@Column(name = "CUSTOMER_NAME", nullable = false)
	private String customerName;
	/**
	 * The customerTin
	 */
	@Column(name = "CUSTOMER_TIN", unique = true, nullable = true)
	private String customerTin;
	/**
	 * The customerPhone
	 */
	@Column(name = "CUSTOMER_PHONE", nullable = true)
	private String customerPhone;
}
