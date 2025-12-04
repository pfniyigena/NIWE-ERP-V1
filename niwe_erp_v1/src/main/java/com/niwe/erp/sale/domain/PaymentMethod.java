package com.niwe.erp.sale.domain;

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
@Table(name = "PAYMENT_METHOD")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class PaymentMethod extends AbstractEntity {
	/**
	 * The serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	@Column(name = "NAME", nullable = false, unique = true)
	private String name; // EG: CASH, MOMO, VISA etc

	@Column(name = "INTERNAL_CODE", nullable = false, unique = true)
	private String internalCode;
	@Column(name = "ACTIVE")
	@Builder.Default
	private Boolean active = Boolean.TRUE;
}
