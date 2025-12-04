package com.niwe.erp.sale.view;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public interface SaleListView {
	UUID getSaleId();

	String getExternalCode();

	String getInternalCode();

	String getCustomerName();

	String getcustomerTin();

	Instant getSaleDate();

	String getTransactionType();

	String getStatus();

	BigDecimal getTotalAmountToPay();
}
