package com.niwe.erp.core.helper;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.niwe.erp.core.domain.CoreItem;
import com.niwe.erp.core.domain.ItemBrand;
import com.niwe.erp.core.domain.ItemCategory;

public class ItemExcelHelper {

	public static List<CoreItem> excelToProducts(InputStream is) {
		try (Workbook workbook = new XSSFWorkbook(is)) {
			Sheet sheet = workbook.getSheetAt(0);
			Iterator<Row> rows = sheet.iterator();
			List<CoreItem> products = new ArrayList<>();

			int rowNumber = 0;
			while (rows.hasNext()) {
				Row currentRow = rows.next();
				// Skip header row
				if (rowNumber++ == 0)
					continue;

				CoreItem p = new CoreItem();
				// Read String columns safely
				p.setItemName(getStringValue(currentRow.getCell(0)));
				p.setExternalItemCode(getStringValue(currentRow.getCell(1)));
				p.setBarcode(getStringValue(currentRow.getCell(2)));
				// Read numeric cells as BigDecimal
				p.setUnitPrice(getBigDecimalValue(currentRow.getCell(3)));
				p.setUnitCost(getBigDecimalValue(currentRow.getCell(4)));
				p.setQuanityInitial(getBigDecimalValue(currentRow.getCell(5)));
				products.add(p);
			}
			return products;
		} catch (Exception e) {
			throw new RuntimeException("Failed to parse Excel file: " + e.getMessage(), e);
		}
	}

	public static List<ItemCategory> excelToItemCategories(InputStream is) {
		try (Workbook workbook = new XSSFWorkbook(is)) {
			Sheet sheet = workbook.getSheetAt(0);
			Iterator<Row> rows = sheet.iterator();
			List<ItemCategory> products = new ArrayList<>();

			int rowNumber = 0;
			while (rows.hasNext()) {
				Row currentRow = rows.next();
				// Skip header row
				if (rowNumber++ == 0)
					continue;

				ItemCategory p = new ItemCategory();
				// Read String columns safely
				p.setExternalId(getIntegerValue(currentRow.getCell(0)));
				p.setCategoryName(getStringValue(currentRow.getCell(1)));
				products.add(p);
			}
			return products;
		} catch (Exception e) {
			throw new RuntimeException("Failed to parse Excel file: " + e.getMessage(), e);
		}
	}

	public static List<ItemBrand> excelToBrands(InputStream is) {
		try (Workbook workbook = new XSSFWorkbook(is)) {
			Sheet sheet = workbook.getSheetAt(0);
			Iterator<Row> rows = sheet.iterator();
			List<ItemBrand> products = new ArrayList<>();

			int rowNumber = 0;
			while (rows.hasNext()) {
				Row currentRow = rows.next();
				// Skip header row
				if (rowNumber++ == 0)
					continue;

				ItemBrand p = new ItemBrand();
				// Read String columns safely
				p.setExternalId(getIntegerValue(currentRow.getCell(0)));
				p.setBrandName(getStringValue(currentRow.getCell(1)));
				products.add(p);
			}
			return products;
		} catch (Exception e) {
			throw new RuntimeException("Failed to parse Excel file: " + e.getMessage(), e);
		}
	}

	private static String getStringValue(Cell cell) {
		if (cell == null)
			return null;
		return cell.getStringCellValue().trim();
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

	public static Integer getIntegerValue(Cell cell) {
		if (cell == null)
			return 0;

		if (cell.getCellType() == CellType.NUMERIC) {
			return (int) cell.getNumericCellValue();
		} else if (cell.getCellType() == CellType.STRING) {
			String value = cell.getStringCellValue().trim();
			return value.isEmpty() ? 0 : Integer.parseInt(value);
		}
		return 0;
	}
}
