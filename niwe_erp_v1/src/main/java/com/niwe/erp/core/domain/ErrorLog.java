package com.niwe.erp.core.domain;

import com.niwe.erp.common.domain.AbstractEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "CORE_ERROR_LOG")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ErrorLog extends AbstractEntity {
	/**
	 * The serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	@Column(name = "ERROR_NAME", columnDefinition = "TEXT", unique = true)
	private String errorName;
	@Column(name = "ERROR_DESCRIPTION", columnDefinition = "TEXT")
	private String errorDescription;
	@Column(name = "ERROR_TYPE")
	@Enumerated(EnumType.STRING)
	@lombok.ToString.Include
	private ErrorLogType errorType;

}
