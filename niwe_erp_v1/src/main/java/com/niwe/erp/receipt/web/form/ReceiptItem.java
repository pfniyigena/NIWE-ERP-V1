package com.niwe.erp.receipt.web.form;
public record ReceiptItem(
	    String description,
	    int qty,
	    double unitPrice,
	    double total
	) {}
