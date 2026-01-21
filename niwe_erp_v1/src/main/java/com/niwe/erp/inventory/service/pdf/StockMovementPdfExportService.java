package com.niwe.erp.inventory.service.pdf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
import com.niwe.erp.inventory.web.view.StockMovementListView;
import com.niwe.erp.sale.service.PageNumberEventHandler;


@Service
public class StockMovementPdfExportService {

	public ByteArrayInputStream exportMovementsToPdf(List<StockMovementListView> movements, CoreTaxpayer taxpayer) {
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
					new Paragraph("Movement Report").setBold().setFontSize(14).setTextAlignment(TextAlignment.CENTER));
			float[] columnWidths = { 4.0f, // Date
					2.5f, // Type
					8.0f, // Item
					2.0f, // Qty
					5.5f // Manager
			};
			Table table = new Table(columnWidths);
			table.setWidth(UnitValue.createPercentValue(100));
			String[] headers = { "  Date  ", "Movement Type", "Item", "Quantity", "Manager" };
			for (String h : headers) {
				table.addHeaderCell(
						new Cell().add(new Paragraph(h).setFont(boldFont).setFontSize(headerFontSize)).setPadding(4)
								.setBackgroundColor(ColorConstants.LIGHT_GRAY).setTextAlignment(TextAlignment.CENTER));
			}
			// Data rows
			for (StockMovementListView dto : movements) {

				table.addCell(
						compactCell(DataParserUtil.dateFromInstant(dto.getMovementDate()), regularFont, cellFontSize));

				table.addCell(compactCell(dto.getMovementType(), regularFont, cellFontSize));

				table.addCell(compactCell(dto.getItemName(), regularFont, cellFontSize));

				table.addCell(compactCell(dto.getMovedQuantity().toString(), regularFont, cellFontSize)
						.setTextAlignment(TextAlignment.RIGHT));

				table.addCell(compactCell(dto.getManagerName() == null ? "" : dto.getManagerName(), regularFont,
						cellFontSize));
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
