package com.niwe.erp.core.domain;

import com.niwe.erp.common.domain.AbstractEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
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
@Table(name = "CORE_COUNTRY")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class CoreCountry extends AbstractEntity {
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;
	/**
	 * The code
	 */
	@NotBlank
	@Column(name = "CODE", nullable = false)
	private String code;
	/**
	 * The displayName
	 */
	@NotBlank
	@Column(name = "DISPLAY_NAME", nullable = true)
	private String displayName;
	/**
	 * The frenchName
	 */
	@Column(name = "FRENCH_NAME", nullable = true)
	private String frenchName;
	/**
	 * The englishName
	 */
	@Column(name = "ENGLISH_NAME", nullable = true)
	private String englishName;
	
	/**
	 * The isDefault
	 */
	@Column(name = "IS_DEFAULT", nullable = false)
	@Builder.Default
	private Boolean isDefault=Boolean.FALSE;

}
