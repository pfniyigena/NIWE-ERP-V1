package com.niwe.erp.receipt.web.form;

import java.time.LocalDateTime;
import java.util.List;

public record ReceiptDto(
	    String storeName,
	    String storeAddress,
	    String storePhone,
	    String cashierName,
	    LocalDateTime dateTime,
	    String orderId,
	    List<ReceiptItem> items,
	    double subTotal,
	    double discount,
	    double total,
	    double cashReceived,
	    double change
	) {}
