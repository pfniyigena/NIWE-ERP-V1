package com.niwe.erp.invoicing.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.niwe.erp.invoicing.domain.Invoice;
import com.niwe.erp.invoicing.domain.InvoiceLine;
import com.niwe.erp.invoicing.domain.InvoiceTaxSummary;
import com.niwe.erp.invoicing.domain.TaxType;
import com.niwe.erp.invoicing.repository.InvoiceTaxSummaryRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class InvoiceTaxSummaryService {
	private TaxTypeService taxTypeService;
	private InvoiceTaxSummaryRepository invoiceTaxSummaryRepository;

	public void save(Invoice invoice) {

		Map<TaxType, BigDecimal> map = new HashMap<>();

		for (InvoiceLine line : invoice.getLines()) {
			TaxType taxType = taxTypeService.findByCode(line.getTaxCode());
			BigDecimal taxAmount = map.get(taxType);
			if (taxAmount == null) {
				taxAmount = line.getTaxAmount();
			} else {
				taxAmount = taxAmount.add(line.getTaxAmount());
			}
			map.put(taxType, taxAmount);
		}
		List<InvoiceTaxSummary> summary = new ArrayList<InvoiceTaxSummary>();
		for (Map.Entry<TaxType, BigDecimal> entry : map.entrySet()) {
			summary.add(InvoiceTaxSummary.builder().tax(entry.getKey()).invoice(invoice).totalAmount(entry.getValue())
					.taxCode(entry.getKey().getTaxCode()).taxName(entry.getKey().getTaxName())
					.taxValue(entry.getKey().getTaxValue()).build());
		}
		invoiceTaxSummaryRepository.saveAll(summary);
	}

	public List<InvoiceTaxSummary> findByInvoice(Invoice invoice) {

		return invoiceTaxSummaryRepository.findByInvoiceIdOrderByTaxDisplayLevelAsc(invoice.getId());
	}
}
