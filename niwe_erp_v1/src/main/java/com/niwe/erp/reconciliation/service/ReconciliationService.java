package com.niwe.erp.reconciliation.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.niwe.erp.reconciliation.domain.SaleDeclared;
import com.niwe.erp.reconciliation.domain.SaleGenerated;

import com.niwe.erp.reconciliation.repository.SaleDeclaredRepository;
import com.niwe.erp.reconciliation.repository.SaleGeneratedRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReconciliationService {
	private final SaleDeclaredRepository saleDeclaredRepository;
	private final SaleGeneratedRepository saleGeneratedRepository;

	public ByteArrayInputStream exportMissingGenerated() throws IOException {
		List<SaleGenerated> missing = saleGeneratedRepository.findMissingGeneratedRecords();
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Missing Records Generated");

		Row header = sheet.createRow(0);
		header.createCell(0).setCellValue("Buyer TIN");
		header.createCell(1).setCellValue("Buyer Name");
		header.createCell(2).setCellValue("Nature of Good");
		header.createCell(3).setCellValue("Receipt Number");
		header.createCell(4).setCellValue("Invoice Date");
		header.createCell(5).setCellValue("Total Amount of Sales (VAT Exclusive)");
		header.createCell(6).setCellValue("Exempted Sales Amount");
		header.createCell(7).setCellValue("Zero rated Sales Amount");
		header.createCell(8).setCellValue("Exports Amount");
		header.createCell(9).setCellValue("Taxble Sales");
		header.createCell(10).setCellValue("VAT");
		// add other columns similarly

		int rowIdx = 1;
		for (SaleGenerated s : missing) {
			Row row = sheet.createRow(rowIdx++);
			row.createCell(0).setCellValue(s.getBuyerTin());
			row.createCell(1).setCellValue(s.getBuyerName());
			row.createCell(2).setCellValue(s.getNatureOfGood());
			row.createCell(3).setCellValue(s.getReceiptNumber());
			row.createCell(4).setCellValue(s.getInvoiceDate().toString());
			row.createCell(5).setCellValue(s.getTotalAmountTaxExclusive().doubleValue());
			row.createCell(6).setCellValue(s.getTotalAmountExampted().doubleValue());
			row.createCell(7).setCellValue(s.getTotalAmountZeroRelated().doubleValue());
			row.createCell(8).setCellValue(s.getTotalAmountExport().doubleValue());
			row.createCell(9).setCellValue(s.getTotalAmountTaxable().doubleValue());
			row.createCell(10).setCellValue(s.getTotalAmountVat().doubleValue());
			// add other columns similarly
		}

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		workbook.write(out);
		workbook.close();
		return new ByteArrayInputStream(out.toByteArray());
	}

	@Transactional
	public void saveSaleDeclaredExcel(MultipartFile file) throws IOException {
		try {
			Workbook workbook = new XSSFWorkbook(file.getInputStream());
			Sheet sheet = workbook.getSheetAt(0);
			List<SaleDeclared> list = new ArrayList<>();

			for (Row row : sheet) {
				if (row.getRowNum() == 0)
					continue; // skip header

				SaleDeclared sale = SaleDeclared.builder().buyerTin(getStringValue(row.getCell(0)))
						.buyerName(getStringValue(row.getCell(1))).natureOfGood(getStringValue(row.getCell(2)))
						.receiptNumber(getStringValue(row.getCell(3))).invoiceDate(getLocalDateValue(row.getCell(4)))
						.totalAmountTaxExclusive(getBigDecimalFromDeclared(row.getCell(5)))
						.totalAmountExampted(getBigDecimalFromDeclared(row.getCell(6)))
						.totalAmountZeroRelated(getBigDecimalFromDeclared(row.getCell(7)))
						.totalAmountExport(getBigDecimalFromDeclared(row.getCell(8)))
						.totalAmountTaxable(getBigDecimalFromDeclared(row.getCell(9)))
						.totalAmountVat(getBigDecimalFromDeclared(row.getCell(10))).build();
				list.add(sale);
			}

			workbook.close();
			saleDeclaredRepository.saveAll(list);
		} catch (Exception e) {
			log.error("Error:{}", e);
		}

	}

	@Transactional
	public void saveSaleGeneratedExcel(MultipartFile file) throws IOException {
		Workbook workbook = new XSSFWorkbook(file.getInputStream());
		Sheet sheet = workbook.getSheetAt(0);
		List<SaleGenerated> list = new ArrayList<>();

		for (Row row : sheet) {
			if (row.getRowNum() == 0)
				continue; // skip header

			SaleGenerated sale = SaleGenerated.builder().buyerTin(getStringValue(row.getCell(0)))
					.buyerName(getStringValue(row.getCell(1))).natureOfGood(getStringValue(row.getCell(2)))
					.receiptNumber(getStringValue(row.getCell(3))).invoiceDate(getLocalDateValue(row.getCell(4)))
					.totalAmountTaxExclusive(getBigDecimalValue(row.getCell(5)))
					.totalAmountExampted(getBigDecimalValue(row.getCell(6)))
					.totalAmountZeroRelated(getBigDecimalValue(row.getCell(7)))
					.totalAmountExport(getBigDecimalValue(row.getCell(8)))
					.totalAmountTaxable(getBigDecimalValue(row.getCell(9)))
					.totalAmountVat(getBigDecimalValue(row.getCell(10))).build();
			list.add(sale);
		}

		workbook.close();
		saleGeneratedRepository.saveAll(list);
	}

	private LocalDate getLocalDateValue(Cell cell) {

		LocalDate invoiceDate = null;

		if (cell != null) {
			if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
				invoiceDate = cell.getLocalDateTimeCellValue().toLocalDate();
			} else if (cell.getCellType() == CellType.STRING) {
				// parse from string if user typed "31/10/2025"
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
				invoiceDate = LocalDate.parse(cell.getStringCellValue(), formatter);
			}
		}
		return invoiceDate;
	}

	private static String getStringValue(Cell cell) {
		if (cell == null)
			return null;
		DataFormatter formatter = new DataFormatter();
		String value = formatter.formatCellValue(cell);
		
		return value.trim();
	}

	private static BigDecimal getBigDecimalValue(Cell cell) {
		if (cell == null)
			return BigDecimal.ZERO;

		if (cell.getCellType() == CellType.NUMERIC) {
			return BigDecimal.valueOf(cell.getNumericCellValue());
		} else if (cell.getCellType() == CellType.STRING) {
			String value = cell.getStringCellValue().trim();
			return new BigDecimal(value.isEmpty() ? "0" : value);
		}
		return BigDecimal.ZERO;
	}

	public BigDecimal getBigDecimalFromDeclared(Cell cell) {
		if (cell == null)
			return BigDecimal.ZERO;

		switch (cell.getCellType()) {

		case NUMERIC:
			return BigDecimal.valueOf(cell.getNumericCellValue());

		case STRING:
			String value = cell.getStringCellValue().trim();
			if (value.isEmpty())
				return BigDecimal.ZERO;

			// remove commas e.g. 38,900 → 38900
			value = value.replace(",", "");

			return new BigDecimal(value);

		default:
			return BigDecimal.ZERO;
		}
	}

	public ByteArrayInputStream exportMissingDeclared() throws IOException {
		List<SaleDeclared> missing = saleDeclaredRepository.findMissingDeclaredRecords();
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Missing Records Declared");

		Row header = sheet.createRow(0);
		header.createCell(0).setCellValue("Buyer TIN");
		header.createCell(1).setCellValue("Buyer Name");
		header.createCell(2).setCellValue("Nature of Good");
		header.createCell(3).setCellValue("Receipt Number");
		header.createCell(4).setCellValue("Invoice Date");
		header.createCell(5).setCellValue("Total Amount of Sales (VAT Exclusive)");
		header.createCell(6).setCellValue("Exempted Sales Amount");
		header.createCell(7).setCellValue("Zero rated Sales Amount");
		header.createCell(8).setCellValue("Exports Amount");
		header.createCell(9).setCellValue("Taxble Sales");
		header.createCell(10).setCellValue("VAT");
		// add other columns similarly

		int rowIdx = 1;
		for (SaleDeclared s : missing) {
			Row row = sheet.createRow(rowIdx++);
			row.createCell(0).setCellValue(s.getBuyerTin());
			row.createCell(1).setCellValue(s.getBuyerName());
			row.createCell(2).setCellValue(s.getNatureOfGood());
			row.createCell(3).setCellValue(s.getReceiptNumber());
			row.createCell(4).setCellValue(s.getInvoiceDate().toString());
			row.createCell(5).setCellValue(s.getTotalAmountTaxExclusive().doubleValue());
			row.createCell(6).setCellValue(s.getTotalAmountExampted().doubleValue());
			row.createCell(7).setCellValue(s.getTotalAmountZeroRelated().doubleValue());
			row.createCell(8).setCellValue(s.getTotalAmountExport().doubleValue());
			row.createCell(9).setCellValue(s.getTotalAmountTaxable().doubleValue());
			row.createCell(10).setCellValue(s.getTotalAmountVat().doubleValue());
			// add other columns similarly
		}

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		workbook.write(out);
		workbook.close();
		return new ByteArrayInputStream(out.toByteArray());
	}

}
