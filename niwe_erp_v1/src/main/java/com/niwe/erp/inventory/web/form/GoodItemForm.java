package com.niwe.erp.inventory.web.form;

import java.math.BigDecimal;

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
public class GoodItemForm {
	private String name;
	private String code;
	private String tax;
	private BigDecimal rate;
	private BigDecimal price;

}
