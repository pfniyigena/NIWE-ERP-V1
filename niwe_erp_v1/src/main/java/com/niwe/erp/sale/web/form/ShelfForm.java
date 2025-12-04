package com.niwe.erp.sale.web.form;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
public class ShelfForm {
	private UUID id;
	private String shelfId;
	private String shelCode;
	private String shelName;
	private String warehouseName;
	private String warehouseCode;
	private String invoiceNumber;
	private LocalDate invoiceDate;
	private LocalDate dueDate;
	@Builder.Default
	private List<ShelfLineForm> shelfLines = new ArrayList<>();
	private BigDecimal totalAmount;
	private BigDecimal taxAmount;
}
