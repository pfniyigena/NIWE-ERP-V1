package com.niwe.erp.inventory.domain;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.niwe.erp.common.domain.AbstractEntity;
import com.niwe.erp.core.domain.CoreTaxpayer;
import com.niwe.erp.core.domain.CoreUser;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "INVENTORY_STOCK_REQUISITION")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class StockRequisition extends AbstractEntity {
	/**
	 * The serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * The status
	 */
	@Enumerated(EnumType.STRING)
	@Column(name = "STATUS")
	@lombok.ToString.Include
	@Builder.Default
	private ERequisitionStatus status = ERequisitionStatus.PENDING;

	/**
	 * The requester
	 */
	@ManyToOne
	@JoinColumn(name = "REQUESTER_ID", nullable = false)
	private CoreUser requester;

	/**
	 * The approver
	 */
	@ManyToOne
	@JoinColumn(name = "APPROVER_ID", nullable = false)
	private CoreUser approver;
	/**
	 * The requestDate
	 */
	@Column(name = "REQUEST_DATE")
	@Builder.Default
	private Instant requestDate = Instant.now();
	/**
	 * The approvedDate
	 */
	@Column(name = "APPROVED_DATE")
	private Instant approvedDate;
	/**
	 * The department
	 */
	@Column(name = "DEPARTMENT")
	private String department;
	/**
	 * The reason
	 */
	@Column(name = "REASON")
	private String reason;
	/**
	 * The items
	 */
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@Builder.Default
	private List<RequisitionItem> items = new ArrayList<>();
	/**
	 * The Taxpayer
	 */
	@ManyToOne
	@JoinColumn(name = "TAXPAYER_ID", nullable = false)
	private CoreTaxpayer taxpayer;
}
