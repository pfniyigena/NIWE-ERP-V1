package com.niwe.erp.sale.service.pdf;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.niwe.erp.core.domain.CoreTaxpayer;

public class PdfHeaderCommon {
	public static Table companyHeader(CoreTaxpayer coreTaxpayer) {
		// Company Information
		String companyName = coreTaxpayer.getTaxpayerName();
		String tin = "TIN:"+coreTaxpayer.getTinNumber();
		String address = coreTaxpayer.getTaxpayerAddress();
		String phone = "Phone:"+coreTaxpayer.getTaxpayerPhoneNumber();
		String email = "Email:"+coreTaxpayer.getTaxpayerEmail();
		String date = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		// --- Load logo ---
		Image logo = null;
		try {
		    ImageData imageData = ImageDataFactory.create("/path/to/logo.png"); // your logo path
		    logo = new Image(imageData);
		    logo.setWidth(60); // adjust size
		} catch (Exception e) {
		    // Handle missing logo
		    logo = null;
		}
		Table headerTable = new Table(new float[]{1, 3, 2});
		headerTable.setWidth(UnitValue.createPercentValue(100));
		// Logo cell
		Cell logoCell = new Cell();
		logoCell.setBorder(Border.NO_BORDER);
		if (logo != null) {
		    logoCell.add(logo);
		}
		headerTable.addCell(logoCell);
		// Company info cell
		Cell companyCell = new Cell();
		companyCell.setBorder(Border.NO_BORDER);
		companyCell.add(new Paragraph(companyName).setBold().setFontSize(14));
		companyCell.add(new Paragraph(tin));
		companyCell.add(new Paragraph(address));
		companyCell.add(new Paragraph(phone));
		companyCell.add(new Paragraph(email));
		headerTable.addCell(companyCell);

		
		// Date cell (right-aligned)
		Cell dateCell = new Cell();
		dateCell.setBorder(Border.NO_BORDER);
		dateCell.add(new Paragraph("Date: " + date));
		dateCell.setTextAlignment(TextAlignment.RIGHT);
		headerTable.addCell(dateCell);

		return headerTable;
	}
}
