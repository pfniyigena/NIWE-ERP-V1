package com.niwe.erp.sale.service.excel;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.niwe.erp.inventory.web.dto.ProductStockAgingDto;
import com.niwe.erp.inventory.web.dto.ProductStockSummaryDto;
import com.niwe.erp.inventory.web.dto.ProductStockValuationDto;

@Service
public class WarehouseStockExcelExportService {

	public ByteArrayInputStream exportSalesToExcel(List<ProductStockSummaryDto> sales) throws IOException {
		try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			Sheet sheet = workbook.createSheet("Sales");

			// Header
			Row headerRow = sheet.createRow(0);
			String[] headers = { "Item Code", "Item Name", "Total Quantity" };
			for (int i = 0; i < headers.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(headers[i]);
			}
			// Data rows
			int rowIdx = 1;
			for (ProductStockSummaryDto stock : sales) {
				Row row = sheet.createRow(rowIdx++);
			    row.createCell(0).setCellValue(stock.getProductCode());
				row.createCell(1).setCellValue(stock.getProductName());
				row.createCell(2).setCellValue(stock.getTotalQuantity().toPlainString());
			}
			workbook.write(out);
			return new ByteArrayInputStream(out.toByteArray());
		}
	}
	public ByteArrayInputStream exportStockAgingToExcel(List<ProductStockAgingDto> stockSummary) throws IOException {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Stock Summary");

            // Header row
            Row header = sheet.createRow(0);
            int col = 0;
            header.createCell(col++).setCellValue("Product ID");
            header.createCell(col++).setCellValue("Product Code");
            header.createCell(col++).setCellValue("Product Name");
            header.createCell(col++).setCellValue("Total Quantity");

            // Aging buckets
            String[] buckets = {"0-30", "31-60", "61-90", "91-180", "181-365", "365+"};
            for (String bucket : buckets) {
                header.createCell(col++).setCellValue(bucket + " Days");
            }

            // Data rows
            int rowIdx = 1;
            for (ProductStockAgingDto dto : stockSummary) {
                Row row = sheet.createRow(rowIdx++);
                col = 0;
                row.createCell(col++).setCellValue(dto.getItemId().toString());
                row.createCell(col++).setCellValue(dto.getProductCode());
                row.createCell(col++).setCellValue(dto.getProductName());
                row.createCell(col++).setCellValue(dto.getTotalQuantity().doubleValue());

                Map<String, BigDecimal> aging = dto.getAgingBuckets();
                for (String bucket : buckets) {
                    row.createCell(col++).setCellValue(
                            aging.getOrDefault(bucket, BigDecimal.ZERO).doubleValue()
                    );
                }
            }

            // Auto-size columns
            for (int i = 0; i < col; i++) {
                sheet.autoSizeColumn(i);
            }
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }
	public ByteArrayInputStream exportEvaluationToExcel(List<ProductStockValuationDto> sales) throws IOException {
		try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			Sheet sheet = workbook.createSheet("Stock Evaluation");

			// Header
			Row headerRow = sheet.createRow(0);
			String[] headers = { "Item Code", "Item Name", "Total Quantity","Cost","Value" };
			for (int i = 0; i < headers.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(headers[i]);
			}
			// Data rows
			int rowIdx = 1;
			BigDecimal totalAmount = BigDecimal.ZERO;
			for (ProductStockValuationDto stock : sales) {
				Row row = sheet.createRow(rowIdx++);
			    row.createCell(0).setCellValue(stock.getProductCode());
				row.createCell(1).setCellValue(stock.getProductName());
				row.createCell(2).setCellValue(stock.getTotalQuantity().toPlainString());
				row.createCell(3).setCellValue(stock.getUnitCost().toPlainString());
				row.createCell(4).setCellValue(stock.getTotalValue().toPlainString());
				 totalAmount = totalAmount.add(stock.getTotalValue());
			}
			// Total row
						Row totalRow = sheet.createRow(rowIdx);
						totalRow.createCell(3).setCellValue("TOTAL");
						totalRow.createCell(4).setCellValue(totalAmount.doubleValue());
			workbook.write(out);
			return new ByteArrayInputStream(out.toByteArray());
		}
	}
}
