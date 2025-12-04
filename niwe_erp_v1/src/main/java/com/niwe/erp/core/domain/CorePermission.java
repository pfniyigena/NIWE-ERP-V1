package com.niwe.erp.core.domain;

import com.niwe.erp.common.domain.AbstractEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;
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
@Table(name = "CORE_PERMISSION")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class CorePermission extends AbstractEntity {
	/**
	 * The serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * The name
	 */
	@Column(name = "NAME", unique = true, nullable = false)
	@Pattern(regexp = "^[A-Za-z0-9]+_[A-Za-z09]+$", message = "{permission.name.invalidFormat}")
	private String name; // e.g. "PRODUCT_VIEW", "INVOICE_EDIT"
	/**
	 * The description
	 */
	@Column(name = "DESCRIPTION")
	private String description;

}
