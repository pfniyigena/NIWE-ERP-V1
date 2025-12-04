package com.niwe.erp.sale.service.pdf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.niwe.erp.common.util.DataParserUtil;
import com.niwe.erp.sale.domain.DailySalesSummary;
import com.niwe.erp.sale.domain.PaymentMethod;
import com.niwe.erp.sale.domain.Sale;
import com.niwe.erp.sale.domain.TransactionType;
import com.niwe.erp.sale.service.PageNumberEventHandler;
import com.niwe.erp.sale.service.SaleService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SalePdfExportService {

	private final SaleService saleService;

	public ByteArrayInputStream exportSalesToPdf(DailySalesSummary summary) {
		List<Sale> sales = saleService.findByDailySalesSummary(summary);
		try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			PdfWriter writer = new PdfWriter(out);
			PdfDocument pdf = new PdfDocument(writer);
			Document document = new Document(pdf);
			pdf.addEventHandler(PdfDocumentEvent.END_PAGE, new PageNumberEventHandler());

			Table companyHeader = PdfHeaderCommon.companyHeader(summary.getTaxpayer());
			// Add header to PDF
			document.add(companyHeader);

			// Add small spacing before title
			document.add(new Paragraph("\n"));
			document.add(new Paragraph("Sales Report on " + summary.getSummaryDate().toString()).setBold()
					.setFontSize(14).setTextAlignment(TextAlignment.CENTER));

			float[] columnWidths = { 4, 4, 4, 2, 3, 3 };
			Table table = new Table(columnWidths);
			table.setWidth(UnitValue.createPercentValue(100));

			String[] headers = { "Sale Date", "Internal Code", "Customer Name", "Unit Price", "Total Amount",
					"Payment Method" };
			for (String h : headers) {
				table.addHeaderCell(new Cell().add(new Paragraph(h)));
			}
			BigDecimal totalAmount = BigDecimal.ZERO;
			// Data rows
			for (Sale sale : sales) {
				BigDecimal amount = sale.getTotalAmountToPay() != null ? sale.getTotalAmountToPay() : BigDecimal.ZERO;
				if (sale.getTransactionType() == TransactionType.REFUND) {
					amount = amount.negate(); // negative for refunds
				}
				table.addCell(DataParserUtil.dateFromInstant(sale.getSaleDate()));
				table.addCell(sale.getInternalCode());
				table.addCell(sale.getCustomerName() != null ? sale.getCustomerName() : "");
				table.addCell(String.valueOf(sale.getItemNumber()));
				table.addCell(amount.toString());
				table.addCell(sale.getPaymentMethod().getName());
				totalAmount = totalAmount.add(amount); // add negative for refunds
			}
			// Total row
			table.addCell(new Cell(1, 4).add(new Paragraph("TOTAL")));
			table.addCell(new Cell().add(new Paragraph(totalAmount.toString())));
			table.addCell(new Cell()); // empty for Payment Status
			document.add(table);
			// summaryTable
			Table summaryTable = new Table(new float[] { 3, 2 });
			summaryTable.setWidth(UnitValue.createPercentValue(100));

			// Header row
			summaryTable.addCell(new Cell(1, 2).add(new Paragraph("Payment Summary").setBold())
					.setBackgroundColor(new DeviceRgb(220, 220, 220)));

			// Column titles
			summaryTable.addCell(new Cell().add(new Paragraph("Payment Method").setBold()));
			summaryTable.addCell(
					new Cell().add(new Paragraph("Total Amount").setBold()).setTextAlignment(TextAlignment.RIGHT));

			// Add each payment row
			summary.getPayments().forEach(payment -> {
				summaryTable.addCell(new Cell().add(new Paragraph(payment.getPaymentMethod().getName())));

				summaryTable.addCell(new Cell().add(new Paragraph(payment.getTotalPaidAmount().toPlainString()))
						.setTextAlignment(TextAlignment.RIGHT));
			});

			// Compute total amount
			BigDecimal totalPaid = summary.getPayments().stream().map(p -> p.getTotalPaidAmount())
					.reduce(BigDecimal.ZERO, BigDecimal::add);

			// Add TOTAL row
			summaryTable.addCell(
					new Cell().add(new Paragraph("TOTAL").setBold()).setBackgroundColor(new DeviceRgb(230, 230, 230)));

			summaryTable.addCell(new Cell().add(new Paragraph(totalPaid.toPlainString()).setBold())
					.setBackgroundColor(new DeviceRgb(230, 230, 230)).setTextAlignment(TextAlignment.RIGHT));

			// document.add(summaryTable);
			// dailySummaryTable

			Table dailySummaryTable = new Table(new float[] { 3, 2 });
			dailySummaryTable.setWidth(UnitValue.createPercentValue(100));

			Cell header = new Cell(1, 2).add(new Paragraph("Daily Summary").setBold())
					.setBackgroundColor(new DeviceRgb(220, 220, 220));
			dailySummaryTable.addCell(header);

			// helper method for row
			BiConsumer<String, BigDecimal> addRow = (label, value) -> {
				dailySummaryTable.addCell(new Cell().add(new Paragraph(label)));
				dailySummaryTable.addCell(
						new Cell().add(new Paragraph(value.toPlainString())).setTextAlignment(TextAlignment.RIGHT));
			};

			addRow.accept("Total HT", summary.getTotalAmountHorsTax());
			addRow.accept("Total TTC", summary.getTotalAmountInclusiveTax());
			addRow.accept("Total Discount", summary.getTotalDiscountAmount());
			addRow.accept("Total Tax", summary.getTotalTaxAmount());
			addRow.accept("Total Gross", summary.getTotalGrossAmount());
			addRow.accept("Total Extra", summary.getTotalExtraAmount());
			addRow.accept("Total To Pay", summary.getTotalAmountToPay());
			addRow.accept("Total Cost", summary.getTotalCost());
			addRow.accept("Total Profit", summary.getTotalProfit());

			// Add number of receipts
			dailySummaryTable.addCell(new Cell().add(new Paragraph("Number of Receipts")));
			dailySummaryTable.addCell(new Cell().add(new Paragraph(summary.getNumberOfReceipts().toString()))
					.setTextAlignment(TextAlignment.RIGHT));

			Table sideBySide = new Table(new float[] { 1, 1 });
			sideBySide.setWidth(UnitValue.createPercentValue(100));

			// LEFT → Payment Summary Table
			sideBySide.addCell(new Cell().add(summaryTable).setBorder(Border.NO_BORDER));

			// RIGHT → Daily Summary Table
			sideBySide.addCell(new Cell().add(dailySummaryTable).setBorder(Border.NO_BORDER));

			document.add(sideBySide);

			document.close();

			return new ByteArrayInputStream(out.toByteArray());
		} catch (Exception e) {
			throw new RuntimeException("Failed to export PDF: " + e.getMessage());
		}
	}

	public void generateMonthlyPdf(OutputStream out, List<Sale> sales, YearMonth month) throws Exception {
		try {
			PdfWriter writer = new PdfWriter(out);
			PdfDocument pdf = new PdfDocument(writer);
			Document document = new Document(pdf);
			pdf.addEventHandler(PdfDocumentEvent.END_PAGE, new PageNumberEventHandler());

			Table companyHeader = PdfHeaderCommon.companyHeader(sales.get(0).getTaxpayer());
			// Add header to PDF
			document.add(companyHeader);

			// Add small spacing before title
			document.add(new Paragraph("\n"));
			document.add(new Paragraph("Monthly Sales Report of " + month.toString()).setBold().setFontSize(14)
					.setTextAlignment(TextAlignment.CENTER));

			float[] columnWidths = { 4, 4, 4, 2, 3, 3 };
			Table table = new Table(columnWidths);
			table.setWidth(UnitValue.createPercentValue(100));

			String[] headers = { "Sale Date", "Internal Code", "Customer Name", "Item Number", "Total Amount",
					"Payment Method" };
			for (String h : headers) {
				table.addHeaderCell(new Cell().add(new Paragraph(h)));
			}
			BigDecimal totalAmount = BigDecimal.ZERO;
			// Data rows
			for (Sale sale : sales) {
				BigDecimal amount = sale.getTotalAmountToPay() != null ? sale.getTotalAmountToPay() : BigDecimal.ZERO;
				if (sale.getTransactionType() == TransactionType.REFUND) {
					amount = amount.negate(); // negative for refunds
				}
				table.addCell(DataParserUtil.dateFromInstant(sale.getSaleDate()));
				table.addCell(sale.getInternalCode());
				table.addCell(sale.getCustomerName() != null ? sale.getCustomerName() : "");
				table.addCell(String.valueOf(sale.getItemNumber()));
				table.addCell(amount.toString());
				table.addCell(sale.getPaymentMethod().getName());
				totalAmount = totalAmount.add(amount); // add negative for refunds
			}
			// Total row
			table.addCell(new Cell(1, 4).add(new Paragraph("TOTAL")));
			table.addCell(new Cell().add(new Paragraph(totalAmount.toString())));
			table.addCell(new Cell()); // empty for Payment Status
			document.add(table);

			document.add(new Paragraph("\nPayment Summary").setBold().setFontSize(12));

			// --- Payment Summary Table ---
			Map<PaymentMethod, BigDecimal> paymentTotals = sales.stream()
					.collect(Collectors.groupingBy(Sale::getPaymentMethod, Collectors.mapping(
							s -> s.getTransactionType() == TransactionType.REFUND ? s.getTotalAmountToPay().negate()
									: s.getTotalAmountToPay(),
							Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))));
			Table paymentTable = new Table(UnitValue.createPercentArray(new float[] { 4, 3 })).useAllAvailableWidth();
			paymentTable.addHeaderCell(new Cell().add(new Paragraph("Payment Method").setBold()));
			paymentTable.addHeaderCell(new Cell().add(new Paragraph("Total Amount").setBold()));

			BigDecimal paymentTotalSum = BigDecimal.ZERO;
			for (Map.Entry<PaymentMethod, BigDecimal> entry : paymentTotals.entrySet()) {
				paymentTable.addCell(new Cell().add(new Paragraph(entry.getKey().getName())));
				paymentTable.addCell(new Cell().add(new Paragraph(entry.getValue().toString())));
				paymentTotalSum = paymentTotalSum.add(entry.getValue());
			}

			// Total row
			Cell payTotalLabel = new Cell().add(new Paragraph("TOTAL").setBold())
					.setBackgroundColor(new DeviceRgb(230, 230, 230));
			Cell payTotalValue = new Cell().add(new Paragraph(paymentTotalSum.toString()).setBold())
					.setBackgroundColor(new DeviceRgb(230, 230, 230));
			paymentTable.addCell(payTotalLabel);
			paymentTable.addCell(payTotalValue);

			document.add(paymentTable);
			document.close();

		} catch (Exception e) {
			throw new RuntimeException("Failed to export PDF: " + e.getMessage());
		}
	}

	public ByteArrayInputStream exportDailySalesSummaryToPdf(DailySalesSummary summary) {
		try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			PdfWriter writer = new PdfWriter(out);
			PdfDocument pdf = new PdfDocument(writer);
			Document document = new Document(pdf);
			Table companyHeader = PdfHeaderCommon.companyHeader(summary.getTaxpayer());
			// Add header to PDF
			document.add(companyHeader);

			// Add small spacing before title
			document.add(new Paragraph("\n"));
			document.add(new Paragraph("Sales Report on " + summary.getSummaryDate().toString()).setBold()
					.setFontSize(14).setTextAlignment(TextAlignment.CENTER));
			// summaryTable
			Table summaryTable = new Table(new float[] { 3, 2 });
			summaryTable.setWidth(UnitValue.createPercentValue(100));

			// Header row
			summaryTable.addCell(new Cell(1, 2).add(new Paragraph("Payment Summary").setBold())
					.setBackgroundColor(new DeviceRgb(220, 220, 220)));

			// Column titles
			summaryTable.addCell(new Cell().add(new Paragraph("Payment Method").setBold()));
			summaryTable.addCell(
					new Cell().add(new Paragraph("Total Amount").setBold()).setTextAlignment(TextAlignment.RIGHT));

			// Add each payment row
			summary.getPayments().forEach(payment -> {
				summaryTable.addCell(new Cell().add(new Paragraph(payment.getPaymentMethod().getName())));

				summaryTable.addCell(new Cell().add(new Paragraph(payment.getTotalPaidAmount().toPlainString()))
						.setTextAlignment(TextAlignment.RIGHT));
			});

			// Compute total amount
			BigDecimal totalPaid = summary.getPayments().stream().map(p -> p.getTotalPaidAmount())
					.reduce(BigDecimal.ZERO, BigDecimal::add);

			// Add TOTAL row
			summaryTable.addCell(
					new Cell().add(new Paragraph("TOTAL").setBold()).setBackgroundColor(new DeviceRgb(230, 230, 230)));

			summaryTable.addCell(new Cell().add(new Paragraph(totalPaid.toPlainString()).setBold())
					.setBackgroundColor(new DeviceRgb(230, 230, 230)).setTextAlignment(TextAlignment.RIGHT));

			// document.add(summaryTable);
			// dailySummaryTable

			Table dailySummaryTable = new Table(new float[] { 3, 2 });
			dailySummaryTable.setWidth(UnitValue.createPercentValue(100));

			Cell header = new Cell(1, 2).add(new Paragraph("Daily Summary").setBold())
					.setBackgroundColor(new DeviceRgb(220, 220, 220));
			dailySummaryTable.addCell(header);

			// helper method for row
			BiConsumer<String, BigDecimal> addRow = (label, value) -> {
				dailySummaryTable.addCell(new Cell().add(new Paragraph(label)));
				dailySummaryTable.addCell(
						new Cell().add(new Paragraph(value.toPlainString())).setTextAlignment(TextAlignment.RIGHT));
			};

			addRow.accept("Total HT", summary.getTotalAmountHorsTax());
			addRow.accept("Total TTC", summary.getTotalAmountInclusiveTax());
			addRow.accept("Total Discount", summary.getTotalDiscountAmount());
			addRow.accept("Total Tax", summary.getTotalTaxAmount());
			addRow.accept("Total Gross", summary.getTotalGrossAmount());
			addRow.accept("Total Extra", summary.getTotalExtraAmount());
			addRow.accept("Total To Pay", summary.getTotalAmountToPay());
			addRow.accept("Total Cost", summary.getTotalCost());
			addRow.accept("Total Profit", summary.getTotalProfit());

			// Add number of receipts
			dailySummaryTable.addCell(new Cell().add(new Paragraph("Number of Receipts")));
			dailySummaryTable.addCell(new Cell().add(new Paragraph(summary.getNumberOfReceipts().toString()))
					.setTextAlignment(TextAlignment.RIGHT));

			Table sideBySide = new Table(new float[] { 1, 1 });
			sideBySide.setWidth(UnitValue.createPercentValue(100));

			// LEFT → Payment Summary Table
			sideBySide.addCell(new Cell().add(summaryTable).setBorder(Border.NO_BORDER));

			// RIGHT → Daily Summary Table
			sideBySide.addCell(new Cell().add(dailySummaryTable).setBorder(Border.NO_BORDER));

			document.add(sideBySide);

			document.close();

			return new ByteArrayInputStream(out.toByteArray());
		} catch (Exception e) {
			throw new RuntimeException("Failed to export PDF: " + e.getMessage());
		}
	}

}
