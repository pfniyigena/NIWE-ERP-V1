package com.niwe.erp.sale.service.pdf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;

import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.niwe.erp.common.util.DataParserUtil;
import com.niwe.erp.core.domain.CoreTaxpayer;
import com.niwe.erp.core.repository.CoreItemRepository;
import com.niwe.erp.inventory.web.dto.ProductStockSummaryDto;
import com.niwe.erp.inventory.web.dto.ProductStockValuationDto;
import com.niwe.erp.sale.service.PageNumberEventHandler;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WarehouseStockPdfExportService {

	private final CoreItemRepository coreItemRepository;

	public ByteArrayInputStream exportStockSummaryToPdf(List<ProductStockSummaryDto> sales) {
		try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			CoreTaxpayer coreTaxpayer = coreItemRepository.getReferenceById(sales.get(0).getItemId()).getTaxpayer();
			PdfWriter writer = new PdfWriter(out);
			PdfDocument pdf = new PdfDocument(writer);
			Document document = new Document(pdf);
			pdf.addEventHandler(PdfDocumentEvent.END_PAGE, new PageNumberEventHandler());

			Table companyHeader = PdfHeaderCommon.companyHeader(coreTaxpayer);
			// Add header to PDF
			document.add(companyHeader);

			// Add small spacing before title
			document.add(new Paragraph("\n"));
			document.add(new Paragraph("Stock Summary Report on " + DataParserUtil.dateFromInstant(Instant.now()))
					.setBold().setFontSize(14).setTextAlignment(TextAlignment.CENTER));

			float[] columnWidths = { 2, 4, 8, 4, };
			Table table = new Table(columnWidths);
			table.setWidth(UnitValue.createPercentValue(100));

			String[] headers = { "No", "Item Code", "Internal Name", "Quantity" };
			for (String h : headers) {
				table.addHeaderCell(new Cell().add(new Paragraph(h)));
			}
			int no = 1;
			for (ProductStockSummaryDto sale : sales) {
				table.addCell(String.valueOf(no));
				table.addCell(sale.getProductCode());
				table.addCell(sale.getProductName());
				table.addCell(sale.getTotalQuantity().toPlainString());
				no++;
			}
			document.add(table);
			document.close();

			return new ByteArrayInputStream(out.toByteArray());
		} catch (Exception e) {
			throw new RuntimeException("Failed to export PDF: " + e.getMessage());
		}
	}

	public ByteArrayInputStream exportEvaluationToPdf(List<ProductStockValuationDto> sales) {
		try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			CoreTaxpayer coreTaxpayer = coreItemRepository.getReferenceById(sales.get(0).getItemId()).getTaxpayer();

			PdfWriter writer = new PdfWriter(out);
			PdfDocument pdf = new PdfDocument(writer);
			Document document = new Document(pdf);
			pdf.addEventHandler(PdfDocumentEvent.END_PAGE, new PageNumberEventHandler());

			Table companyHeader = PdfHeaderCommon.companyHeader(coreTaxpayer);
			// Add header to PDF
			document.add(companyHeader);

			// Add small spacing before title
			document.add(new Paragraph("\n"));
			document.add(new Paragraph("Stock Evaluation Report on " + DataParserUtil.dateFromInstant(Instant.now()))
					.setBold().setFontSize(14).setTextAlignment(TextAlignment.CENTER));

			float[] columnWidths = { 2, 4, 8, 4, 4, 4 };
			Table table = new Table(columnWidths);
			table.setWidth(UnitValue.createPercentValue(100));

			String[] headers = { "No", "Item Code", "Internal Name", "Quantity", "Cost", "Value" };
			for (String h : headers) {
				table.addHeaderCell(new Cell().add(new Paragraph(h)));
			}
			int no = 1;
			for (ProductStockValuationDto sale : sales) {
				table.addCell(String.valueOf(no));
				table.addCell(sale.getProductCode());
				table.addCell(sale.getProductName());
				table.addCell(sale.getTotalQuantity().toPlainString());
				table.addCell(sale.getUnitCost().toPlainString());
				table.addCell(sale.getTotalValue().toPlainString());
				no++;
			}
			document.add(table);
			document.close();

			return new ByteArrayInputStream(out.toByteArray());
		} catch (Exception e) {
			throw new RuntimeException("Failed to export PDF: " + e.getMessage());
		}
	}

}
