package com.niwe.erp.inventory.web.form;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import com.niwe.erp.purchase.domain.PurchaseStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class GoodForm {
	private UUID id;
	private String invoiceNumber;
	private LocalDate invoiceDate;
	private LocalDate dueDate;
	@Builder.Default
	private List<GoodLineForm> goodLines = new ArrayList<>();
	private BigDecimal totalAmount;
	private BigDecimal taxAmount;
	@Builder.Default
	private PurchaseStatus status = PurchaseStatus.DRAFT;

}
