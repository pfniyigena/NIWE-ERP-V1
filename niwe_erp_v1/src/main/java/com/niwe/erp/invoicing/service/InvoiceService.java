package com.niwe.erp.invoicing.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.niwe.erp.common.service.SequenceNumberService;
import com.niwe.erp.core.domain.CoreItem;
import com.niwe.erp.core.domain.EItemNature;
import com.niwe.erp.core.service.CoreItemService;
import com.niwe.erp.ebm.InvoiceCalculationService;
import com.niwe.erp.invoicing.domain.ETransactionType;
import com.niwe.erp.invoicing.domain.Invoice;
import com.niwe.erp.invoicing.domain.InvoiceLine;
import com.niwe.erp.invoicing.domain.InvoiceStatus;
import com.niwe.erp.invoicing.domain.TaxType;
import com.niwe.erp.invoicing.mapper.InvoiceManualMapper;
import com.niwe.erp.invoicing.repository.InvoiceRepository;
import com.niwe.erp.invoicing.web.form.InvoiceForm;
import com.niwe.erp.invoicing.web.form.InvoiceLineForm;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class InvoiceService {
	private final InvoiceRepository invoiceRepository;
	private final CoreItemService coreItemService;
	private final SequenceNumberService sequenceNumberService;
	private final TaxTypeService taxTypeService;
	private final InvoiceTaxSummaryService invoiceTaxSummaryService;

	/**
	 * @param invoice
	 * @return Invoice
	 */
	/*
	 * public Invoice saveInvoiceAndGenerateDailyReport(final Invoice invoice) { try
	 * { SdcDailyReport updatedateReport =
	 * sdcDailyReportService.updateDailyReport(invoice.getSdcInformation(),
	 * LocalDate.now(), invoice); if (updatedateReport != null &&
	 * updatedateReport.getId() == null) {
	 * 
	 * updatedateReport.setSdcInformation(invoice.getSdcInformation()); }
	 * sdcDailyReportService.createSdcDailyReport(updatedateReport); if
	 * (updatedateReport != null) { invoice.setSdcDailyReport(updatedateReport); }
	 * return saveInvoice(invoice); } catch (Exception e) { return null; } }
	 */

	public Invoice saveInvoice(Invoice invoice) {

		return invoiceRepository.save(invoice);
	}

	public List<Invoice> findAll() {

		return invoiceRepository.findAll();
	}

	public Invoice findById(String id) {

		return invoiceRepository.getReferenceById(UUID.fromString(id));
	}

	public void saveDraftInvoiceForm(InvoiceForm invoiceForm) {
		Invoice invoice = mapToInvoice(invoiceForm);
		invoice.setTaxPayerName("1");
		invoice.setSdcNumber("1");
		invoice.setRegisteredTinNumber("1");
		invoice.setMachineRegistrationCode("1");
		invoice.setCisVersion("1");
		invoice.setInvoiceNumber(1L);
		invoice.setInternalCode(sequenceNumberService.getNextInvoiceCode());
		// List<InvoiceLine> lines =
		// invoiceForm.getInvoiceLines().stream().map(this::mapToInvoiceLine).toList();
		AtomicInteger counter = new AtomicInteger(1);
		List<InvoiceLine> lines = invoiceForm.getInvoiceLines().stream().map(formLine -> {

			InvoiceCalculationService invoiceCalculationService = new InvoiceCalculationService(formLine.getQuantity(),
					formLine.getUnitPrice(), formLine.getTaxType(), new BigDecimal("0.00"), new BigDecimal("0.00"));

			invoice.setTotalTaxAmount(invoice.getTotalTaxAmount().add(invoiceCalculationService.getTaxAmount()));
			invoice.setTotalAPayer(invoice.getTotalAPayer().add(invoiceCalculationService.getAmountToPay()));
			invoice.setTotalDiscountAmount(
					invoice.getTotalDiscountAmount().add(invoiceCalculationService.getDiscountAmount()));
			invoice.setGrossAmount(invoice.getGrossAmount().add(invoiceCalculationService.getGrossAmount()));
			invoice.setAmountWithoutTax(
					invoice.getAmountWithoutTax().add(invoiceCalculationService.getAmountHorsTax()));
			invoice.setTotalAmountIncludingTax(invoice.getAmountWithoutTax().add(invoice.getTotalTaxAmount()));
			formLine.setAmountToPay(invoiceCalculationService.getAmountToPay());
			formLine.setGrossAmount(invoiceCalculationService.getGrossAmount());
			formLine.setTaxAmount(invoiceCalculationService.getTaxAmount());
			InvoiceLine line = mapToInvoiceLine(formLine);
			line.setItemSeq(counter.getAndIncrement());
			return line;
		}).toList();
		lines.forEach((n) -> n.setInvoice(invoice));
		invoice.setLines(lines);
		invoice.setItemNumber(lines.size());
		invoiceRepository.save(invoice);
	}

	private Invoice mapToInvoice(InvoiceForm invoiceForm) {
		return Invoice.builder().build();
	}

	private InvoiceLine mapToInvoiceLine(InvoiceLineForm invoiceLineForm) {
		CoreItem coreItem = coreItemService.findByInternalCode(invoiceLineForm.getItemCode());
		TaxType taxType = taxTypeService.findByCode(invoiceLineForm.getTaxCode());
		return InvoiceLine.builder().item(coreItem).itemName(invoiceLineForm.getItemName())
				.itemNature(EItemNature.valueOf(coreItem.getNature().getCode())).quantity(invoiceLineForm.getQuantity())
				.unitPrice(invoiceLineForm.getUnitPrice()).taxCode(invoiceLineForm.getTaxCode())
				.taxName(taxType.getTaxName()).taxType(invoiceLineForm.getTaxType())
				.transactionType(ETransactionType.TRANSACTION_TYPE_SALE).grossAmount(invoiceLineForm.getGrossAmount())
				.totalAPayer(invoiceLineForm.getAmountToPay()).taxAmount(invoiceLineForm.getTaxAmount()).build();
	}

	public List<InvoiceForm> getAllInvoicesForm() {
		List<Invoice> invoices = invoiceRepository.findAll();
		return InvoiceManualMapper.toDtoList(invoices);
	}

	public InvoiceForm findInvoiceFormById(String id) {
		Invoice invoice = findById(id);
		return InvoiceManualMapper.toDto(invoice);
	}

	public Invoice confirm(String id) {
		Invoice invoice = findById(id);
		invoice.setStatus(InvoiceStatus.DONE);
		invoice = invoiceRepository.save(invoice);
		handleInvoiceSummary(invoice);
		return invoice;
	}

	private void handleInvoiceSummary(Invoice invoice) {
		invoiceTaxSummaryService.save(invoice);
	}
}
