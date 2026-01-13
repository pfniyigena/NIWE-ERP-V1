package com.niwe.erp.inventory.web.view;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface InflowOutflowSaleView {
	LocalDate getDay();
	BigDecimal getInflow();
	BigDecimal getOutflow();
	BigDecimal getSale();

}
