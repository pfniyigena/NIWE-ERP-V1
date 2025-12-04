package com.niwe.erp.core.domain;

import com.niwe.erp.common.domain.AbstractEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "CORE_QUANTITY_UNIT")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class CoreQuantityUnit extends AbstractEntity{/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * The code
	 */
	@Column(name = "CODE", nullable = false)
	private String code;
	 
	/**
	 * The name
	 */
	@Column(name = "NAME", nullable = false)
	private String name;
	
	/**
	 * The isDefault
	 */
	@Column(name = "IS_DEFAULT", nullable = false)
	@Builder.Default
	private Boolean isDefault=Boolean.FALSE;
}
