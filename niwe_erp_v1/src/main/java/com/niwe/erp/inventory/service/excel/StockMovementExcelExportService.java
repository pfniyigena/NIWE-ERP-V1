package com.niwe.erp.inventory.service.excel;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.niwe.erp.common.util.DataParserUtil;
import com.niwe.erp.inventory.web.view.StockMovementListView;

@Service
public class StockMovementExcelExportService {
	public ByteArrayInputStream exportMovementsToExcel(List<StockMovementListView> movements) throws IOException {
		try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			Sheet sheet = workbook.createSheet("Stock Movements");

			// Header
			Row header = sheet.createRow(0);
			String[] columns = { "Date", "Movement Type", "Item","Purchase Price","Selling Price", "Quantity", "Manager" };

			for (int i = 0; i < columns.length; i++) {
				header.createCell(i).setCellValue(columns[i]);
			}

			// Rows
			int rowIdx = 1;
			for (StockMovementListView dto : movements) {
				Row row = sheet.createRow(rowIdx++);
				row.createCell(0).setCellValue(DataParserUtil.dateTimeFromInstant(dto.getMovementDate()));
				row.createCell(1).setCellValue(dto.getMovementType());
				row.createCell(2).setCellValue(dto.getItemName());
				row.createCell(3).setCellValue(dto.getUnitCost().doubleValue());
				row.createCell(4).setCellValue(dto.getUnitPrice().doubleValue());
				row.createCell(5).setCellValue(dto.getMovedQuantity().doubleValue());
				row.createCell(6).setCellValue(dto.getManagerName());
			}
			// Autosize
			for (int i = 0; i < columns.length; i++) {
				sheet.autoSizeColumn(i);
			}
			workbook.write(out);
			return new ByteArrayInputStream(out.toByteArray());
		}
	}

}
