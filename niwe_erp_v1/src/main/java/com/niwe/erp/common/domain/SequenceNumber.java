package com.niwe.erp.common.domain;

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
@Table(name = "COMMON_SEQUENCE_NUMBER")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class SequenceNumber extends AbstractEntity{
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The type. */
	@Enumerated(EnumType.STRING)
	@Column(name = "TYPE", nullable = false)
	private ESequenceType type;

	/** The sequence. */
	@Column(name = "SEQUENCE", nullable = false)
	private String sequence;
}
