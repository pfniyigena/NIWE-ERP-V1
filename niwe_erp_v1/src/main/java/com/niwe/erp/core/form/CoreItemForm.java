package com.niwe.erp.core.form;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class CoreItemForm {
	
private UUID id;
private String internalCode;
private String itemName;
private String itemCode;
private String barcode;
private String externalItemCode;
private BigDecimal unitPrice;
private BigDecimal unitCost;
private String taxCode;
private BigDecimal taxValue;

}
