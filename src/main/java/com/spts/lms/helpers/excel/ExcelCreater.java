package com.spts.lms.helpers.excel;

import java.awt.Color;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelCreater {

	private static final Logger logger = Logger.getLogger(ExcelCreater.class);

	private static final DecimalFormat df2 = new DecimalFormat(".##");

	public static void CreateExcelFile(List<Map<String, Object>> lstExcelData,
			List<String> header, String filePath) {
		XSSFWorkbook workbook = new XSSFWorkbook();
		try {
			XSSFSheet sheet = workbook.createSheet("My Course Student Sheet");

			// skip first row as it contains headers so start enter value from
			// second row onward
			int rowNum = 1;

			Row headerRow = sheet.createRow(0);
			for (int colNum = 0; colNum < header.size(); colNum++) {
				Cell cell = headerRow.createCell(colNum);
				cell.setCellValue(header.get(colNum));
			}

			for (Map<String, Object> map : lstExcelData) {
				Row row = sheet.createRow(rowNum++);

				for (int colNum = 0; colNum < header.size(); colNum++) {
					Cell cell = row.createCell(colNum);
					cell.setCellValue(String.valueOf(map.get(header.get(colNum))));
				}
			}

			FileOutputStream outputStream = new FileOutputStream(filePath);
			workbook.write(outputStream);
			workbook.close();
		} catch (Exception ex) {
			logger.error("Exception", ex);
		}

	}

	public static void createExcelFile(
			Map<String, List<Map<String, Object>>> lstExcelData,
			List<String> header, String filePath) {
		XSSFWorkbook workbook = new XSSFWorkbook();
		try {

			for (Map.Entry<String, List<Map<String, Object>>> entry : lstExcelData
					.entrySet()) {
				XSSFSheet sheet = workbook.createSheet(entry.getKey());

				// skip first row as it contains headers so start enter value
				// from
				// second row onward
				int rowNum = 1;

				Row headerRow = sheet.createRow(0);
				for (int colNum = 0; colNum < header.size(); colNum++) {
					Cell cell = headerRow.createCell(colNum);
					cell.setCellValue(header.get(colNum));
				}

				for (Map<String, Object> map : entry.getValue()) {
					Row row = sheet.createRow(rowNum++);

					for (int colNum = 0; colNum < header.size(); colNum++) {
						Cell cell = row.createCell(colNum);
						cell.setCellValue(String.valueOf(map.get(header
								.get(colNum))));
					}
				}
			}
			FileOutputStream outputStream = new FileOutputStream(filePath);
			workbook.write(outputStream);
			workbook.close();
		} catch (Exception ex) {
			logger.error("Exception", ex);
		}

	}

	/*
	 * public static void CreateExcelFile(List<Map<String, Object>>
	 * lstExcelData, List<String> header, String filePath, Long courseQCount,
	 * Long facultyQCount) { logger.info("header -------- " + header);
	 * logger.info("facultyQCount --- " + facultyQCount); XSSFWorkbook workbook
	 * = new XSSFWorkbook(); try { XSSFSheet sheet =
	 * workbook.createSheet("My Course Student Sheet");
	 * 
	 * // skip first row as it contains headers so start enter value from //
	 * second row onward int rowNum = 1; XSSFFont headerFont =
	 * workbook.createFont(); headerFont.setFontHeightInPoints((short) 11);
	 * headerFont.setFontName("Calibri");
	 * headerFont.setColor(IndexedColors.WHITE.getIndex());
	 * headerFont.setBold(true);
	 * 
	 * XSSFFont font = workbook.createFont(); font.setFontHeightInPoints((short)
	 * 10); font.setFontName("Calibri");
	 * font.setColor(IndexedColors.BLACK.getIndex());
	 * 
	 * XSSFFont font1 = workbook.createFont();
	 * font1.setFontHeightInPoints((short) 11); font1.setFontName("Calibri");
	 * font1.setColor(IndexedColors.BLACK.getIndex());
	 * 
	 * Row headerRow1 = sheet.createRow(0); Row headerRow2 = sheet.createRow(1);
	 * XSSFCellStyle headerStyle = workbook.createCellStyle(); XSSFColor color =
	 * new XSSFColor(new Color(51, 133, 255));
	 * headerStyle.setFillForegroundColor(color);
	 * headerStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
	 * headerStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
	 * headerStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
	 * headerStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
	 * headerStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
	 * headerStyle.setFont(headerFont);
	 * headerStyle.setAlignment(HorizontalAlignment.CENTER);
	 * 
	 * XSSFCellStyle headerStyle1 = workbook.createCellStyle(); XSSFColor color1
	 * = new XSSFColor(new Color(230, 230, 230));
	 * headerStyle1.setFillForegroundColor(color1);
	 * headerStyle1.setFillPattern(CellStyle.SOLID_FOREGROUND);
	 * headerStyle1.setBorderBottom(XSSFCellStyle.BORDER_THIN);
	 * headerStyle1.setBorderTop(XSSFCellStyle.BORDER_THIN);
	 * headerStyle1.setBorderRight(XSSFCellStyle.BORDER_THIN);
	 * headerStyle1.setBorderLeft(XSSFCellStyle.BORDER_THIN);
	 * headerStyle1.setFont(font1);
	 * headerStyle1.setAlignment(HorizontalAlignment.CENTER);
	 * 
	 * XSSFCellStyle style1 = workbook.createCellStyle(); //
	 * style1.setFillBackgroundColor(IndexedColors.LIGHT_BLUE.getIndex());
	 * style1.setBorderBottom(XSSFCellStyle.BORDER_THIN);
	 * style1.setBorderTop(XSSFCellStyle.BORDER_THIN);
	 * style1.setBorderRight(XSSFCellStyle.BORDER_THIN);
	 * style1.setBorderLeft(XSSFCellStyle.BORDER_THIN);
	 * style1.setAlignment(HorizontalAlignment.CENTER); style1.setFont(font);
	 * 
	 * XSSFCellStyle style2 = workbook.createCellStyle();
	 * style2.setFillForegroundColor(new XSSFColor( new Color(230, 242, 255)));
	 * 
	 * style2.setFillPattern(CellStyle.SOLID_FOREGROUND);
	 * style2.setBorderBottom(XSSFCellStyle.BORDER_THIN);
	 * style2.setBorderTop(XSSFCellStyle.BORDER_THIN);
	 * style2.setBorderRight(XSSFCellStyle.BORDER_THIN);
	 * style2.setBorderLeft(XSSFCellStyle.BORDER_THIN);
	 * style2.setAlignment(HorizontalAlignment.CENTER); style2.setFont(font);
	 * 
	 * for (int colNum = 0; colNum < header.size(); colNum++) {
	 * logger.info("header cell value --- " + header.get(colNum)); //
	 * sheet.setColumnWidth(colNum, 4000); sheet.autoSizeColumn(colNum); if
	 * (colNum < 12) {
	 * 
	 * Cell cell1 = headerRow1.createCell(colNum); Cell cell2 =
	 * headerRow2.createCell(colNum); cell1.setCellValue(header.get(colNum));
	 * cell1.setCellStyle(headerStyle); cell2.setCellStyle(headerStyle);
	 * 
	 * // logger.info("column width ----- "+sheet.getColumnWidth(colNum));
	 * 
	 * } else { if (colNum == 12) { Cell cell1 = headerRow1.createCell(colNum);
	 * cell1.setCellValue("Sr. No. "); cell1.setCellStyle(headerStyle); Cell
	 * cell2 = headerRow2.createCell(colNum); cell2.setCellValue("Course ");
	 * cell2.setCellStyle(headerStyle); } else { if
	 * (header.get(colNum).contains("CourseAverage")) { Cell cell1 =
	 * headerRow1.createCell(colNum); Cell cell2 =
	 * headerRow2.createCell(colNum); cell1.setCellValue("Course Average");
	 * cell1.setCellStyle(headerStyle); cell2.setCellStyle(headerStyle); } else
	 * if (colNum == 12 + courseQCount + 1) { Cell cell1 =
	 * headerRow1.createCell(colNum); cell1.setCellValue("Sr. No. ");
	 * cell1.setCellStyle(headerStyle1); Cell cell2 =
	 * headerRow2.createCell(colNum); cell2.setCellValue("Faculty ");
	 * cell2.setCellStyle(headerStyle1); } else if (colNum == 12 + courseQCount
	 * + 1 + facultyQCount + 1 || colNum == 12 + courseQCount + 1 +
	 * facultyQCount + 2) { Cell cell1 = headerRow1.createCell(colNum); Cell
	 * cell2 = headerRow2.createCell(colNum);
	 * cell1.setCellValue(header.get(colNum)); cell1.setCellStyle(headerStyle);
	 * cell2.setCellStyle(headerStyle); } else if (header.get(colNum).contains(
	 * "CommentsByStudents")) { Cell cell1 = headerRow1.createCell(colNum); Cell
	 * cell2 = headerRow2.createCell(colNum);
	 * cell1.setCellValue("CommentsByStudents");
	 * cell1.setCellStyle(headerStyle); cell2.setCellStyle(headerStyle); } else
	 * {
	 * 
	 * Cell cell1 = headerRow1.createCell(colNum); if
	 * (header.get(colNum).contains("Faculty:")) { cell1.setCellValue(colNum -
	 * 12 - 10); } else { cell1.setCellValue(colNum - 12); }
	 * 
	 * cell1.setCellStyle(headerStyle1); Cell cell2 =
	 * headerRow2.createCell(colNum); cell2.setCellValue(header.get(colNum));
	 * cell2.setCellStyle(style1); } }
	 * 
	 * } sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 0));
	 * sheet.addMergedRegion(new CellRangeAddress(0, 1, 1, 1));
	 * sheet.addMergedRegion(new CellRangeAddress(0, 1, 2, 2));
	 * sheet.addMergedRegion(new CellRangeAddress(0, 1, 3, 3));
	 * sheet.addMergedRegion(new CellRangeAddress(0, 1, 4, 4));
	 * sheet.addMergedRegion(new CellRangeAddress(0, 1, 5, 5));
	 * sheet.addMergedRegion(new CellRangeAddress(0, 1, 6, 6));
	 * sheet.addMergedRegion(new CellRangeAddress(0, 1, 7, 7));
	 * sheet.addMergedRegion(new CellRangeAddress(0, 1, 8, 8));
	 * sheet.addMergedRegion(new CellRangeAddress(0, 1, 9, 9));
	 * sheet.addMergedRegion(new CellRangeAddress(0, 1, 10, 10));
	 * sheet.addMergedRegion(new CellRangeAddress(0, 1, 11, 11));
	 * sheet.addMergedRegion(new CellRangeAddress(0, 1, 22, 22));
	 * sheet.addMergedRegion(new CellRangeAddress(0, 1, 29, 29));
	 * sheet.addMergedRegion(new CellRangeAddress(0, 1, 30, 30));
	 * sheet.addMergedRegion(new CellRangeAddress(0, 1, 31, 31));
	 * 
	 * }
	 * 
	 * for (Map<String, Object> map : lstExcelData) {
	 * 
	 * Row row = sheet.createRow(sheet.getLastRowNum() + 1);
	 * 
	 * for (int colNum = 0; colNum < header.size(); colNum++) {
	 * 
	 * // sheet.setColumnWidth(colNum, 4000);
	 * 
	 * if(colNum<10){ sheet.autoSizeColumn(colNum); }
	 * 
	 * Cell cell = row.createCell(colNum); logger.info("cell value -------- " +
	 * map.get(header.get(colNum)));
	 * 
	 * if (String.valueOf(map.get(header.get(colNum))).equals( "null")) {
	 * cell.setCellValue(""); } else
	 * 
	 * if (map.get(header.get(colNum)).getClass() .equals(Double.class) ||
	 * map.get(header.get(colNum)).getClass() .equals(Float.class)) {
	 * logger.info("cell type ------ " +
	 * map.get(header.get(colNum)).getClass());
	 * cell.setCellValue(df2.format(map.get(header.get(colNum))));
	 * 
	 * } else { cell.setCellValue(String.valueOf(map.get(header .get(colNum))));
	 * } if (colNum % 2 != 0) { cell.setCellStyle(style1); } else {
	 * cell.setCellStyle(style2); }
	 * 
	 * if (colNum == 2) { sheet.setColumnWidth(colNum, 3500); } else if (colNum
	 * >= 6 && colNum <= 10) { sheet.setColumnWidth(colNum, 3500); } else if
	 * (colNum >= 13 && colNum <= 30) {
	 * 
	 * sheet.setColumnWidth(colNum, 5000); } else {
	 * sheet.autoSizeColumn(colNum); }
	 * 
	 * } }
	 * 
	 * FileOutputStream outputStream = new FileOutputStream(filePath);
	 * workbook.write(outputStream); workbook.close(); } catch (Exception ex) {
	 * logger.error("Exception", ex); }
	 * 
	 * System.out.println("Done"); }
	 */

	public static void CreateExcelFile(List<Map<String, Object>> lstExcelData,
			List<String> header, String filePath, Long courseQCount,
			Long facultyQCount) {

		XSSFWorkbook workbook = new XSSFWorkbook();
		try {
			XSSFSheet sheet = workbook.createSheet("My Course Student Sheet");

			// skip first row as it contains headers so start enter value from
			// second row onward
			int rowNum = 1;
			XSSFFont headerFont = workbook.createFont();
			headerFont.setFontHeightInPoints((short) 11);
			headerFont.setFontName("Calibri");
			headerFont.setColor(IndexedColors.WHITE.getIndex());
			headerFont.setBold(true);

			XSSFFont font = workbook.createFont();
			font.setFontHeightInPoints((short) 10);
			font.setFontName("Calibri");
			font.setColor(IndexedColors.BLACK.getIndex());

			XSSFFont font1 = workbook.createFont();
			font1.setFontHeightInPoints((short) 11);
			font1.setFontName("Calibri");
			font1.setColor(IndexedColors.BLACK.getIndex());

			Row headerRow1 = sheet.createRow(0);
			Row headerRow2 = sheet.createRow(1);
			XSSFCellStyle headerStyle = workbook.createCellStyle();
			XSSFColor color = new XSSFColor(new Color(51, 133, 255));
			headerStyle.setFillForegroundColor(color);
			headerStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
			headerStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
			headerStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
			headerStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
			headerStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
			headerStyle.setFont(headerFont);
			headerStyle.setAlignment(HorizontalAlignment.CENTER);

			XSSFCellStyle headerStyle1 = workbook.createCellStyle();
			XSSFColor color1 = new XSSFColor(new Color(230, 230, 230));
			headerStyle1.setFillForegroundColor(color1);
			headerStyle1.setFillPattern(CellStyle.SOLID_FOREGROUND);
			headerStyle1.setBorderBottom(XSSFCellStyle.BORDER_THIN);
			headerStyle1.setBorderTop(XSSFCellStyle.BORDER_THIN);
			headerStyle1.setBorderRight(XSSFCellStyle.BORDER_THIN);
			headerStyle1.setBorderLeft(XSSFCellStyle.BORDER_THIN);
			headerStyle1.setFont(font1);
			headerStyle1.setAlignment(HorizontalAlignment.CENTER);

			XSSFCellStyle style1 = workbook.createCellStyle();
			// style1.setFillBackgroundColor(IndexedColors.LIGHT_BLUE.getIndex());
			style1.setBorderBottom(XSSFCellStyle.BORDER_THIN);
			style1.setBorderTop(XSSFCellStyle.BORDER_THIN);
			style1.setBorderRight(XSSFCellStyle.BORDER_THIN);
			style1.setBorderLeft(XSSFCellStyle.BORDER_THIN);
			style1.setAlignment(HorizontalAlignment.CENTER);
			style1.setFont(font);

			XSSFCellStyle style2 = workbook.createCellStyle();
			style2.setFillForegroundColor(new XSSFColor(
					new Color(230, 242, 255)));

			style2.setFillPattern(CellStyle.SOLID_FOREGROUND);
			style2.setBorderBottom(XSSFCellStyle.BORDER_THIN);
			style2.setBorderTop(XSSFCellStyle.BORDER_THIN);
			style2.setBorderRight(XSSFCellStyle.BORDER_THIN);
			style2.setBorderLeft(XSSFCellStyle.BORDER_THIN);
			style2.setAlignment(HorizontalAlignment.CENTER);
			style2.setFont(font);

			for (int colNum = 0; colNum < header.size(); colNum++) {

				// sheet.setColumnWidth(colNum, 4000);
				sheet.autoSizeColumn(colNum);
				if (colNum < 13) {

					Cell cell1 = headerRow1.createCell(colNum);
					Cell cell2 = headerRow2.createCell(colNum);
					cell1.setCellValue(header.get(colNum));
					cell1.setCellStyle(headerStyle);
					cell2.setCellStyle(headerStyle);

					// logger.info("column width ----- "+sheet.getColumnWidth(colNum));

				} else {
					if (colNum == 13) {
						Cell cell1 = headerRow1.createCell(colNum);
						cell1.setCellValue("Sr. No. ");
						cell1.setCellStyle(headerStyle);
						Cell cell2 = headerRow2.createCell(colNum);
						cell2.setCellValue("Course ");
						cell2.setCellStyle(headerStyle);
					} else {
						if (header.get(colNum).contains("CourseAverage")) {
							Cell cell1 = headerRow1.createCell(colNum);
							Cell cell2 = headerRow2.createCell(colNum);
							cell1.setCellValue("Course Average");
							cell1.setCellStyle(headerStyle);
							cell2.setCellStyle(headerStyle);
						} else if (colNum == 13 + courseQCount + 1) {
							Cell cell1 = headerRow1.createCell(colNum);
							cell1.setCellValue("Sr. No. ");
							cell1.setCellStyle(headerStyle1);
							Cell cell2 = headerRow2.createCell(colNum);
							cell2.setCellValue("Faculty ");
							cell2.setCellStyle(headerStyle1);
						} else if (colNum == 13 + courseQCount + 1
								+ facultyQCount + 1
								|| colNum == 13 + courseQCount + 1
										+ facultyQCount + 2) {
							Cell cell1 = headerRow1.createCell(colNum);
							Cell cell2 = headerRow2.createCell(colNum);
							cell1.setCellValue(header.get(colNum));
							cell1.setCellStyle(headerStyle);
							cell2.setCellStyle(headerStyle);
						} else if (header.get(colNum).contains(
								"CommentsByStudents")) {
							Cell cell1 = headerRow1.createCell(colNum);
							Cell cell2 = headerRow2.createCell(colNum);
							cell1.setCellValue("CommentsByStudents");
							cell1.setCellStyle(headerStyle);
							cell2.setCellStyle(headerStyle);
						} else {

							Cell cell1 = headerRow1.createCell(colNum);
							if (header.get(colNum).contains("Faculty:")) {
								cell1.setCellValue(colNum - 13 - courseQCount
										- 1);
							} else {
								cell1.setCellValue(colNum - 13);
							}

							cell1.setCellStyle(headerStyle1);
							Cell cell2 = headerRow2.createCell(colNum);
							cell2.setCellValue(header.get(colNum));
							cell2.setCellStyle(style1);
						}
					}

				}
				sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 0));
				sheet.addMergedRegion(new CellRangeAddress(0, 1, 1, 1));
				sheet.addMergedRegion(new CellRangeAddress(0, 1, 2, 2));
				sheet.addMergedRegion(new CellRangeAddress(0, 1, 3, 3));
				sheet.addMergedRegion(new CellRangeAddress(0, 1, 4, 4));
				sheet.addMergedRegion(new CellRangeAddress(0, 1, 5, 5));
				sheet.addMergedRegion(new CellRangeAddress(0, 1, 6, 6));
				sheet.addMergedRegion(new CellRangeAddress(0, 1, 7, 7));
				sheet.addMergedRegion(new CellRangeAddress(0, 1, 8, 8));
				sheet.addMergedRegion(new CellRangeAddress(0, 1, 9, 9));
				sheet.addMergedRegion(new CellRangeAddress(0, 1, 10, 10));
				sheet.addMergedRegion(new CellRangeAddress(0, 1, 11, 11));
				// sheet.addMergedRegion(new CellRangeAddress(0, 1, 23, 23));
				sheet.addMergedRegion(new CellRangeAddress(0, 1, 30, 30));
				sheet.addMergedRegion(new CellRangeAddress(0, 1, 31, 31));
				sheet.addMergedRegion(new CellRangeAddress(0, 1, 32, 32));

			}

			for (Map<String, Object> map : lstExcelData) {

				Row row = sheet.createRow(sheet.getLastRowNum() + 1);

				for (int colNum = 0; colNum < header.size(); colNum++) {

					// sheet.setColumnWidth(colNum, 4000);
					/*
					 * if(colNum<10){ sheet.autoSizeColumn(colNum); }
					 */
					Cell cell = row.createCell(colNum);

					if (String.valueOf(map.get(header.get(colNum))).equals(
							"null")) {
						cell.setCellValue("");
					} else

					if (map.get(header.get(colNum)).getClass()
							.equals(Double.class)
							|| map.get(header.get(colNum)).getClass()
									.equals(Float.class)) {

						cell.setCellValue(df2.format(map.get(header.get(colNum))));

					} else {
						cell.setCellValue(String.valueOf(map.get(header
								.get(colNum))));
					}
					if (colNum % 2 != 0) {
						cell.setCellStyle(style1);
					} else {
						cell.setCellStyle(style2);
					}

					if (colNum == 2) {
						sheet.setColumnWidth(colNum, 3500);
					} else if (colNum >= 6 && colNum <= 10) {
						sheet.setColumnWidth(colNum, 3500);
					} else if (colNum >= 13 && colNum <= 30) {

						sheet.setColumnWidth(colNum, 5000);
					} else {
						sheet.autoSizeColumn(colNum);
					}

				}
			}

			FileOutputStream outputStream = new FileOutputStream(filePath);
			workbook.write(outputStream);
			workbook.close();
		} catch (Exception ex) {
			logger.error("Exception", ex);
		}

	}

	// new changes on 10-09-2020
	public static void CreateExcelFileChanged(
			List<Map<String, Object>> lstExcelData, List<String> header,
			String filePath, Long courseQCount, Long facultyQCount) {

		XSSFWorkbook workbook = new XSSFWorkbook();
		try {
			XSSFSheet sheet = workbook.createSheet("My Course Student Sheet");

			// skip first row as it contains headers so start enter value from
			// second row onward
			int rowNum = 1;
			XSSFFont headerFont = workbook.createFont();
			headerFont.setFontHeightInPoints((short) 11);
			headerFont.setFontName("Calibri");
			headerFont.setColor(IndexedColors.WHITE.getIndex());
			headerFont.setBold(true);

			XSSFFont font = workbook.createFont();
			font.setFontHeightInPoints((short) 10);
			font.setFontName("Calibri");
			font.setColor(IndexedColors.BLACK.getIndex());

			XSSFFont font1 = workbook.createFont();
			font1.setFontHeightInPoints((short) 11);
			font1.setFontName("Calibri");
			font1.setColor(IndexedColors.BLACK.getIndex());

			Row headerRow1 = sheet.createRow(0);
			Row headerRow2 = sheet.createRow(1);
			XSSFCellStyle headerStyle = workbook.createCellStyle();
			XSSFColor color = new XSSFColor(new Color(51, 133, 255));
			headerStyle.setFillForegroundColor(color);
			headerStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
			headerStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
			headerStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
			headerStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
			headerStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
			headerStyle.setFont(headerFont);
			headerStyle.setAlignment(HorizontalAlignment.CENTER);

			XSSFCellStyle headerStyle1 = workbook.createCellStyle();
			XSSFColor color1 = new XSSFColor(new Color(230, 230, 230));
			headerStyle1.setFillForegroundColor(color1);
			headerStyle1.setFillPattern(CellStyle.SOLID_FOREGROUND);
			headerStyle1.setBorderBottom(XSSFCellStyle.BORDER_THIN);
			headerStyle1.setBorderTop(XSSFCellStyle.BORDER_THIN);
			headerStyle1.setBorderRight(XSSFCellStyle.BORDER_THIN);
			headerStyle1.setBorderLeft(XSSFCellStyle.BORDER_THIN);
			headerStyle1.setFont(font1);
			headerStyle1.setAlignment(HorizontalAlignment.CENTER);

			XSSFCellStyle style1 = workbook.createCellStyle();
			// style1.setFillBackgroundColor(IndexedColors.LIGHT_BLUE.getIndex());
			style1.setBorderBottom(XSSFCellStyle.BORDER_THIN);
			style1.setBorderTop(XSSFCellStyle.BORDER_THIN);
			style1.setBorderRight(XSSFCellStyle.BORDER_THIN);
			style1.setBorderLeft(XSSFCellStyle.BORDER_THIN);
			style1.setAlignment(HorizontalAlignment.CENTER);
			style1.setFont(font);

			XSSFCellStyle style2 = workbook.createCellStyle();
			style2.setFillForegroundColor(new XSSFColor(
					new Color(230, 242, 255)));

			style2.setFillPattern(CellStyle.SOLID_FOREGROUND);
			style2.setBorderBottom(XSSFCellStyle.BORDER_THIN);
			style2.setBorderTop(XSSFCellStyle.BORDER_THIN);
			style2.setBorderRight(XSSFCellStyle.BORDER_THIN);
			style2.setBorderLeft(XSSFCellStyle.BORDER_THIN);
			style2.setAlignment(HorizontalAlignment.CENTER);
			style2.setFont(font);

			for (int colNum = 0; colNum < header.size(); colNum++) {

				// sheet.setColumnWidth(colNum, 4000);

				sheet.autoSizeColumn(colNum);
				if (colNum < 13) {

					Cell cell1 = headerRow1.createCell(colNum);
					Cell cell2 = headerRow2.createCell(colNum);
					cell1.setCellValue(header.get(colNum));
					cell1.setCellStyle(headerStyle);
					cell2.setCellStyle(headerStyle);

					// logger.info("column width ----- "+sheet.getColumnWidth(colNum));

				} else {
					if (colNum == 13) {
						Cell cell1 = headerRow1.createCell(colNum);
						cell1.setCellValue("Sr. No. ");
						cell1.setCellStyle(headerStyle);
						Cell cell2 = headerRow2.createCell(colNum);
						cell2.setCellValue("Course ");
						cell2.setCellStyle(headerStyle);
					} else {
						if (colNum == 13 + courseQCount + 1) {
							Cell cell1 = headerRow1.createCell(colNum);
							Cell cell2 = headerRow2.createCell(colNum);
							cell1.setCellValue("Course Average");
							cell1.setCellStyle(headerStyle);
							cell2.setCellStyle(headerStyle);
						} else if (colNum == 13 + courseQCount + 2) {
							Cell cell1 = headerRow1.createCell(colNum);
							cell1.setCellValue("Sr. No. ");
							cell1.setCellStyle(headerStyle1);
							Cell cell2 = headerRow2.createCell(colNum);
							cell2.setCellValue("Faculty ");
							cell2.setCellStyle(headerStyle1);
						} else if (colNum == 13 + courseQCount + 2
								+ facultyQCount + 1
								|| colNum == 13 + courseQCount + 2
										+ facultyQCount + 2
								|| colNum == 13 + courseQCount + 2
										+ facultyQCount + 3) {
							Cell cell1 = headerRow1.createCell(colNum);
							Cell cell2 = headerRow2.createCell(colNum);
							cell1.setCellValue(header.get(colNum));
							cell1.setCellStyle(headerStyle);
							cell2.setCellStyle(headerStyle);
						} else {

							Cell cell1 = headerRow1.createCell(colNum);
							if (header.get(colNum).contains("Faculty:")) {
								cell1.setCellValue(colNum - 13 - courseQCount
										- 2);
							} else {
								cell1.setCellValue(colNum - 13);
							}

							cell1.setCellStyle(headerStyle1);
							Cell cell2 = headerRow2.createCell(colNum);
							cell2.setCellValue(header.get(colNum));
							cell2.setCellStyle(style1);
						}
					}

				}
				/*
				 * sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 0));
				 * sheet.addMergedRegion(new CellRangeAddress(0, 1, 1, 1));
				 * sheet.addMergedRegion(new CellRangeAddress(0, 1, 2, 2));
				 * sheet.addMergedRegion(new CellRangeAddress(0, 1, 3, 3));
				 * sheet.addMergedRegion(new CellRangeAddress(0, 1, 4, 4));
				 * sheet.addMergedRegion(new CellRangeAddress(0, 1, 5, 5));
				 * sheet.addMergedRegion(new CellRangeAddress(0, 1, 6, 6));
				 * sheet.addMergedRegion(new CellRangeAddress(0, 1, 7, 7));
				 * sheet.addMergedRegion(new CellRangeAddress(0, 1, 8, 8));
				 * sheet.addMergedRegion(new CellRangeAddress(0, 1, 9, 9));
				 * sheet.addMergedRegion(new CellRangeAddress(0, 1, 10, 10));
				 * sheet.addMergedRegion(new CellRangeAddress(0, 1, 11, 11)); //
				 * sheet.addMergedRegion(new CellRangeAddress(0, 1, 23, 23));
				 * sheet.addMergedRegion(new CellRangeAddress(0, 1, 30, 30));
				 * sheet.addMergedRegion(new CellRangeAddress(0, 1, 31, 31));
				 * sheet.addMergedRegion(new CellRangeAddress(0, 1, 32, 32));
				 */

			}

			for (Map<String, Object> map : lstExcelData) {

				Row row = sheet.createRow(sheet.getLastRowNum() + 1);

				for (int colNum = 0; colNum < header.size(); colNum++) {

					if (colNum == 0) {
						logger.info("mapper value is at 0" + map);
					}
					// sheet.setColumnWidth(colNum, 4000);
					/*
					 * if(colNum<10){ sheet.autoSizeColumn(colNum); }
					 */

					Cell cell = row.createCell(colNum);

					if (String.valueOf(map.get(header.get(colNum))).equals(
							"null")) {
						cell.setCellValue("");
					} else

					if (map.get(header.get(colNum)).getClass()
							.equals(Double.class)
							|| map.get(header.get(colNum)).getClass()
									.equals(Float.class)) {

						cell.setCellValue(df2.format(map.get(header.get(colNum))));

					} else {
						cell.setCellValue(String.valueOf(map.get(header
								.get(colNum))));
					}
					if (colNum % 2 != 0) {
						cell.setCellStyle(style1);
					} else {
						cell.setCellStyle(style2);
					}

					if (colNum == 2) {
						sheet.setColumnWidth(colNum, 3500);
					} else if (colNum >= 6 && colNum <= 10) {
						sheet.setColumnWidth(colNum, 3500);
					} else if (colNum >= 13 && colNum <= 30) {

						sheet.setColumnWidth(colNum, 5000);
					} else {
						sheet.autoSizeColumn(colNum);
					}

				}
			}

			FileOutputStream outputStream = new FileOutputStream(filePath);
			workbook.write(outputStream);
			workbook.close();
		} catch (Exception ex) {
			logger.error("Exception", ex);
		}

	}

	// 10-03-2021

	public static void CreateExcelFileOnlyFaculty(
			List<Map<String, Object>> lstExcelData, List<String> header,
			String filePath, Long courseQCount, Long facultyQCount) {
		XSSFWorkbook workbook = new XSSFWorkbook();
		try {
			XSSFSheet sheet = workbook.createSheet("My Course Student Sheet");

			// skip first row as it contains headers so start enter value from
			// second row onward
			int rowNum = 1;
			XSSFFont headerFont = workbook.createFont();
			headerFont.setFontHeightInPoints((short) 11);
			headerFont.setFontName("Calibri");
			headerFont.setColor(IndexedColors.WHITE.getIndex());
			headerFont.setBold(true);

			XSSFFont font = workbook.createFont();
			font.setFontHeightInPoints((short) 10);
			font.setFontName("Calibri");
			font.setColor(IndexedColors.BLACK.getIndex());

			XSSFFont font1 = workbook.createFont();
			font1.setFontHeightInPoints((short) 11);
			font1.setFontName("Calibri");
			font1.setColor(IndexedColors.BLACK.getIndex());

			Row headerRow1 = sheet.createRow(0);
			Row headerRow2 = sheet.createRow(1);
			XSSFCellStyle headerStyle = workbook.createCellStyle();
			XSSFColor color = new XSSFColor(new Color(51, 133, 255));
			headerStyle.setFillForegroundColor(color);
			headerStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
			headerStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
			headerStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
			headerStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
			headerStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
			headerStyle.setFont(headerFont);
			headerStyle.setAlignment(HorizontalAlignment.CENTER);

			XSSFCellStyle headerStyle1 = workbook.createCellStyle();
			XSSFColor color1 = new XSSFColor(new Color(230, 230, 230));
			headerStyle1.setFillForegroundColor(color1);
			headerStyle1.setFillPattern(CellStyle.SOLID_FOREGROUND);
			headerStyle1.setBorderBottom(XSSFCellStyle.BORDER_THIN);
			headerStyle1.setBorderTop(XSSFCellStyle.BORDER_THIN);
			headerStyle1.setBorderRight(XSSFCellStyle.BORDER_THIN);
			headerStyle1.setBorderLeft(XSSFCellStyle.BORDER_THIN);
			headerStyle1.setFont(font1);
			headerStyle1.setAlignment(HorizontalAlignment.CENTER);

			XSSFCellStyle style1 = workbook.createCellStyle();
			// style1.setFillBackgroundColor(IndexedColors.LIGHT_BLUE.getIndex());
			style1.setBorderBottom(XSSFCellStyle.BORDER_THIN);
			style1.setBorderTop(XSSFCellStyle.BORDER_THIN);
			style1.setBorderRight(XSSFCellStyle.BORDER_THIN);
			style1.setBorderLeft(XSSFCellStyle.BORDER_THIN);
			style1.setAlignment(HorizontalAlignment.CENTER);
			style1.setFont(font);

			XSSFCellStyle style2 = workbook.createCellStyle();
			style2.setFillForegroundColor(new XSSFColor(
					new Color(230, 242, 255)));

			style2.setFillPattern(CellStyle.SOLID_FOREGROUND);
			style2.setBorderBottom(XSSFCellStyle.BORDER_THIN);
			style2.setBorderTop(XSSFCellStyle.BORDER_THIN);
			style2.setBorderRight(XSSFCellStyle.BORDER_THIN);
			style2.setBorderLeft(XSSFCellStyle.BORDER_THIN);
			style2.setAlignment(HorizontalAlignment.CENTER);
			style2.setFont(font);

			for (int colNum = 0; colNum < header.size(); colNum++) {
				// sheet.setColumnWidth(colNum, 4000);
				sheet.autoSizeColumn(colNum);
				if (colNum < 13) {

					Cell cell1 = headerRow1.createCell(colNum);
					Cell cell2 = headerRow2.createCell(colNum);
					cell1.setCellValue(header.get(colNum));
					cell1.setCellStyle(headerStyle);
					cell2.setCellStyle(headerStyle);

					// logger.info("column width ----- "+sheet.getColumnWidth(colNum));

				} else {
					if (colNum == 13) {
						Cell cell1 = headerRow1.createCell(colNum);
						cell1.setCellValue("Sr. No. ");
						cell1.setCellStyle(headerStyle);
						Cell cell2 = headerRow2.createCell(colNum);
						cell2.setCellValue("Faculty ");
						cell2.setCellStyle(headerStyle);
					} else {
						if (header.get(colNum).contains("FacultyAverage")) {
							Cell cell1 = headerRow1.createCell(colNum);
							Cell cell2 = headerRow2.createCell(colNum);
							cell1.setCellValue("Faculty Average");
							cell1.setCellStyle(headerStyle);
							cell2.setCellStyle(headerStyle);
						} /*
						 * else if (colNum == 13 + courseQCount + 1) { Cell
						 * cell1 = headerRow1.createCell(colNum);
						 * cell1.setCellValue("Sr. No. ");
						 * cell1.setCellStyle(headerStyle1); Cell cell2 =
						 * headerRow2.createCell(colNum);
						 * cell2.setCellValue("Faculty ");
						 * cell2.setCellStyle(headerStyle1); }
						 */else if (colNum == 13 + courseQCount + 1
								+ facultyQCount + 1
								|| colNum == 13 + courseQCount + 1
										+ facultyQCount + 2) {
							Cell cell1 = headerRow1.createCell(colNum);
							Cell cell2 = headerRow2.createCell(colNum);
							cell1.setCellValue(header.get(colNum));
							cell1.setCellStyle(headerStyle);
							cell2.setCellStyle(headerStyle);
						} else if (header.get(colNum).contains(
								"CommentsByStudents")) {
							Cell cell1 = headerRow1.createCell(colNum);
							Cell cell2 = headerRow2.createCell(colNum);
							cell1.setCellValue("CommentsByStudents");
							cell1.setCellStyle(headerStyle);
							cell2.setCellStyle(headerStyle);
						} else {

							Cell cell1 = headerRow1.createCell(colNum);
							if (header.get(colNum).contains("Faculty:")) {
								cell1.setCellValue(colNum - 13 - courseQCount);
							} else {
								cell1.setCellValue(colNum - 13);
							}

							cell1.setCellStyle(headerStyle1);
							Cell cell2 = headerRow2.createCell(colNum);
							cell2.setCellValue(header.get(colNum));
							cell2.setCellStyle(style1);
						}
					}

				}
				sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 0));
				sheet.addMergedRegion(new CellRangeAddress(0, 1, 1, 1));
				sheet.addMergedRegion(new CellRangeAddress(0, 1, 2, 2));
				sheet.addMergedRegion(new CellRangeAddress(0, 1, 3, 3));
				sheet.addMergedRegion(new CellRangeAddress(0, 1, 4, 4));
				sheet.addMergedRegion(new CellRangeAddress(0, 1, 5, 5));
				sheet.addMergedRegion(new CellRangeAddress(0, 1, 6, 6));
				sheet.addMergedRegion(new CellRangeAddress(0, 1, 7, 7));
				sheet.addMergedRegion(new CellRangeAddress(0, 1, 8, 8));
				sheet.addMergedRegion(new CellRangeAddress(0, 1, 9, 9));
				sheet.addMergedRegion(new CellRangeAddress(0, 1, 10, 10));
				sheet.addMergedRegion(new CellRangeAddress(0, 1, 11, 11));
				// sheet.addMergedRegion(new CellRangeAddress(0, 1, 23, 23));
				sheet.addMergedRegion(new CellRangeAddress(0, 1, 30, 30));
				sheet.addMergedRegion(new CellRangeAddress(0, 1, 31, 31));
				sheet.addMergedRegion(new CellRangeAddress(0, 1, 32, 32));

			}

			for (Map<String, Object> map : lstExcelData) {

				Row row = sheet.createRow(sheet.getLastRowNum() + 1);

				for (int colNum = 0; colNum < header.size(); colNum++) {

					// sheet.setColumnWidth(colNum, 4000);
					/*
					 * if(colNum<10){ sheet.autoSizeColumn(colNum); }
					 */
					Cell cell = row.createCell(colNum);
					if (String.valueOf(map.get(header.get(colNum))).equals(
							"null")) {
						cell.setCellValue("");
					} else

					if (map.get(header.get(colNum)).getClass()
							.equals(Double.class)
							|| map.get(header.get(colNum)).getClass()
									.equals(Float.class)) {
						cell.setCellValue(df2.format(map.get(header.get(colNum))));

					} else {
						cell.setCellValue(String.valueOf(map.get(header
								.get(colNum))));
					}
					if (colNum % 2 != 0) {
						cell.setCellStyle(style1);
					} else {
						cell.setCellStyle(style2);
					}

					if (colNum == 2) {
						sheet.setColumnWidth(colNum, 3500);
					} else if (colNum >= 6 && colNum <= 10) {
						sheet.setColumnWidth(colNum, 3500);
					} else if (colNum >= 13 && colNum <= 30) {

						sheet.setColumnWidth(colNum, 5000);
					} else {
						sheet.autoSizeColumn(colNum);
					}

				}
			}

			FileOutputStream outputStream = new FileOutputStream(filePath);
			workbook.write(outputStream);
			workbook.close();
		} catch (Exception ex) {
			logger.error("Exception", ex);
		}

	}

}
