package com.niwe.erp.sale.service.excel;

import com.niwe.erp.common.util.DataParserUtil;
import com.niwe.erp.sale.domain.Sale;
import com.niwe.erp.sale.domain.TransactionType;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;

@Service
public class SaleExcelExportService {

	public ByteArrayInputStream exportSalesToExcel(List<Sale> sales) throws IOException {
		try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			Sheet sheet = workbook.createSheet("Sales");

			// Header
			Row headerRow = sheet.createRow(0);
			String[] headers = { "Sale Date", "Internal Code", "Customer Name", "Item Number", "Total Amount",
					"Payment Mode" };
			for (int i = 0; i < headers.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(headers[i]);
			}
			// Before returning
			BigDecimal totalAmount = BigDecimal.ZERO;
			// Data rows
			int rowIdx = 1;
			for (Sale sale : sales) {
				Row row = sheet.createRow(rowIdx++);
				BigDecimal amount = sale.getTotalAmountToPay() != null ? sale.getTotalAmountToPay() : BigDecimal.ZERO;
			    if (sale.getTransactionType() == TransactionType.REFUND) {
			        amount = amount.negate(); // Make it negative for refunds
			    }
			    row.createCell(0).setCellValue(DataParserUtil.dateFromInstant(sale.getSaleDate()));
				row.createCell(1).setCellValue(sale.getInternalCode());
				row.createCell(2).setCellValue(sale.getCustomerName());
				row.createCell(3).setCellValue(sale.getItemNumber());
				row.createCell(4).setCellValue(amount.doubleValue());
				row.createCell(5).setCellValue(sale.getPaymentMethod().getName());
				 totalAmount = totalAmount.add(amount);
			}
			// Total row
			Row totalRow = sheet.createRow(rowIdx);
			totalRow.createCell(3).setCellValue("TOTAL");
			totalRow.createCell(4).setCellValue(totalAmount.doubleValue());
			workbook.write(out);
			return new ByteArrayInputStream(out.toByteArray());
		}
	}
	public void exportMonthlySalesToExcel(OutputStream out, List<Sale> sales, YearMonth month) throws IOException {
		try (Workbook workbook = new XSSFWorkbook()) {
			 Sheet sheet = workbook.createSheet("Monthly Sales of "+month.toString());
			// Header
			Row headerRow = sheet.createRow(0);
			String[] headers = { "Sale Date", "Internal Code", "Customer Name", "Item Number", "Total Amount",
					"Payment Method" };
			for (int i = 0; i < headers.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(headers[i]);
			}
			// Before returning
			BigDecimal totalAmount = BigDecimal.ZERO;
			// Data rows
			int rowIdx = 1;
			for (Sale sale : sales) {
				Row row = sheet.createRow(rowIdx++);
				BigDecimal amount = sale.getTotalAmountToPay() != null ? sale.getTotalAmountToPay() : BigDecimal.ZERO;
			    if (sale.getTransactionType() == TransactionType.REFUND) {
			        amount = amount.negate(); // Make it negative for refunds
			    }
			    row.createCell(0).setCellValue(DataParserUtil.dateFromInstant(sale.getSaleDate()));
				row.createCell(1).setCellValue(sale.getInternalCode());
				row.createCell(2).setCellValue(sale.getCustomerName());
				row.createCell(3).setCellValue(sale.getItemNumber());
				row.createCell(4).setCellValue(amount.doubleValue());
				row.createCell(5).setCellValue(sale.getPaymentMethod().getName());
				 totalAmount = totalAmount.add(amount);
			}
			// Total row
			Row totalRow = sheet.createRow(rowIdx);
			totalRow.createCell(3).setCellValue("TOTAL");
			totalRow.createCell(4).setCellValue(totalAmount.doubleValue());
			workbook.write(out);
		}catch (Exception e) {
			throw new RuntimeException("Failed to export Excel: " + e.getMessage());
		}
	}
}
