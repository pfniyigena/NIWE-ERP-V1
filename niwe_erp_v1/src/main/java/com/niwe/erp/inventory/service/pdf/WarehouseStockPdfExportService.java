package com.niwe.erp.inventory.service.pdf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.niwe.erp.common.util.DataParserUtil;
import com.niwe.erp.common.util.PdfHeaderCommon;
import com.niwe.erp.core.domain.CoreTaxpayer;
import com.niwe.erp.core.repository.CoreItemRepository;
import com.niwe.erp.inventory.web.dto.ProductStockSummaryDto;
import com.niwe.erp.inventory.web.dto.ProductStockValuationDto;
import com.niwe.erp.inventory.web.view.InflowItemListView;
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
			document.add(new Paragraph("Stock Summary Report on " + DataParserUtil.dateTimeFromInstant(Instant.now()))
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
			document.add(
					new Paragraph("Stock Evaluation Report on " + DataParserUtil.dateTimeFromInstant(Instant.now()))
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

	public ByteArrayInputStream exportMovementsToPdf(List<InflowItemListView> movements, CoreTaxpayer taxpayer) {
		try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			PdfFont regularFont = PdfFontFactory.createFont(StandardFonts.HELVETICA);
			PdfFont boldFont = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);

			float headerFontSize = 12f;
			float cellFontSize = 10f;

			PdfWriter writer = new PdfWriter(out);
			PdfDocument pdf = new PdfDocument(writer);
			Document document = new Document(pdf, PageSize.A4.rotate());
			pdf.addEventHandler(PdfDocumentEvent.END_PAGE, new PageNumberEventHandler());
			document.setMargins(15, 15, 12, 12);
			Table companyHeader = PdfHeaderCommon.companyHeader(taxpayer);
			// Add header to PDF
			document.add(companyHeader);

			// Add small spacing before title
			document.add(new Paragraph("\n"));
			document.add(
					new Paragraph("Stock Report").setBold().setFontSize(14).setTextAlignment(TextAlignment.CENTER));
			float[] columnWidths = { 4.0f, // Date
					2.5f, // Type
					8.0f, // Item
					2.0f, // Unit Cost
					2.0f, // Unit Price
					2.0f, // Qty
					5.5f // Manager
			};
			Table table = new Table(columnWidths);
			table.setWidth(UnitValue.createPercentValue(100));
			String[] headers = { "Name","Purchase Price","Selling Price", "Current Quantity", "Min Stock", "Category", "Brand" };
			for (String h : headers) {
				table.addHeaderCell(
						new Cell().add(new Paragraph(h).setFont(boldFont).setFontSize(headerFontSize)).setPadding(4)
								.setBackgroundColor(ColorConstants.LIGHT_GRAY).setTextAlignment(TextAlignment.CENTER));
			}
			// Data rows
			for (InflowItemListView dto : movements) {

				table.addCell(compactCell(dto.getItemName(), regularFont, cellFontSize));
				table.addCell(compactCell(dto.getUnitCost().toString(), regularFont, cellFontSize));
				table.addCell(compactCell(dto.getUnitPrice().toString(), regularFont, cellFontSize));

				table.addCell(compactCell(dto.getQuantity().toString(), regularFont, cellFontSize));

				table.addCell(compactCell(dto.getStockLevel() == null ? "" : dto.getStockLevel().toString(),
						regularFont, cellFontSize));

				table.addCell(compactCell(dto.getCategoryName() == null ? "" : dto.getCategoryName(), regularFont,
						cellFontSize));
				table.addCell(
						compactCell(dto.getBrandName() == null ? "" : dto.getBrandName(), regularFont, cellFontSize));
			}

			document.add(table);
			document.close();

			return new ByteArrayInputStream(out.toByteArray());
		} catch (Exception e) {
			throw new RuntimeException("Failed to export PDF: " + e.getMessage());
		}
	}

	private Cell compactCell(String text, PdfFont font, float size) {
		return new Cell().add(new Paragraph(text).setFont(font).setFontSize(size)).setPaddingTop(2).setPaddingBottom(2)
				.setPaddingLeft(3).setPaddingRight(3);
	}

}
