package com.spts.lms;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class DuplicateQuestionReader {

	public static void main(String args[]) {
		
		String str="4";
		
		List<String> strList = Arrays.asList(str.split(","));
		
		List<String> empList = new ArrayList<>();
		
		System.out.println(empList.contains("6"));
		System.out.println(strList.get(0));
	}

	public static void readXLSXFile(String baseDir, String fileName)
			throws IOException {
		InputStream ExcelFileToRead = new FileInputStream(baseDir
				+ File.separator + fileName);
		XSSFWorkbook wb = new XSSFWorkbook(ExcelFileToRead);
		DataFormatter fmt = new DataFormatter();
		XSSFWorkbook test = new XSSFWorkbook();
		int numberOfSheets = wb.getNumberOfSheets();
		for (int j = 0; j < numberOfSheets; j++) {
			XSSFSheet sheet = wb.getSheetAt(j);
			XSSFSheet writeSheet = test.createSheet(sheet.getSheetName());
			List<String> headers = new ArrayList();
			XSSFRow row;
			XSSFCell cell;
			List<String> questionTexts = new ArrayList();

			Iterator rows = sheet.rowIterator();
			int rowCount = 0;

			while (rows.hasNext()) {
				XSSFRow writeRow = writeSheet.createRow(rowCount);
				row = (XSSFRow) rows.next();
			
				   CellStyle newCellStyle = writeSheet.getWorkbook().createCellStyle();
			      
				//writeRow.getRowStyle().cloneStyleFrom(row.getRowStyle());
				List<String> options = new ArrayList<String>();

				Iterator cells = row.cellIterator();
				if (rowCount == 0) {
					int lastIndex = 0;
					while (cells.hasNext()) {

						cell = (XSSFCell) cells.next();
						lastIndex = cell.getColumnIndex();
						XSSFCell writeCell = writeRow.createCell(lastIndex);
						writeCell.setCellValue(getCellValue(cell));
						if (cell.getCellType() == XSSFCell.CELL_TYPE_STRING) {
							headers.add(cell.getStringCellValue());
						}

					}
					XSSFCell writeCell = writeRow.createCell(++lastIndex);
					writeCell.setCellValue("Remarks");

				} else {
					while (cells.hasNext()) {
						cell = (XSSFCell) cells.next();
						XSSFCell writeCell = writeRow.createCell(cell
								.getColumnIndex());
						
						
						String headerValue = cell.getColumnIndex() < headers
								.size() ? headers.get(cell.getColumnIndex())
								: "";
						String cellValue = "";
						if (cell.getCellType() == XSSFCell.CELL_TYPE_STRING) {
							cellValue = fmt.formatCellValue(cell);//(cell.getStringCellValue() + "");
							writeCell.setCellValue(cellValue);
						//	.cloneStyleFrom(cell.getCellStyle());
						} else if (cell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC) {
							cellValue = fmt.formatCellValue(cell);
							System.out.println("Cell value --->"+cellValue);
							writeCell.setCellValue(cellValue);
							//.cloneStyleFrom(cell.getCellStyle());
							
						}
						  newCellStyle.cloneStyleFrom(cell.getCellStyle());
						  writeCell.setCellStyle(newCellStyle);
						  System.out.println(cell.getCellStyle());
						
						// System.out.println(headerValue);
						// System.out.println(cellValue);
						if (StringUtils.containsIgnoreCase(headerValue,
								"option")) {
							if (!options.isEmpty()) {
								boolean isOptionRepeated = false;
								if (StringUtils.containsIgnoreCase(cellValue,
										"None")) {

									String remarks = ("Option contains NONE");
									XSSFCell remarksCell = writeRow
											.createCell(headers.size());
									remarksCell.setCellValue(remarks);
								} else if (StringUtils.containsIgnoreCase(
										cellValue, "All")) {

									String remarks = ("Option contains All");
									XSSFCell remarksCell = writeRow
											.createCell(headers.size());
									remarksCell.setCellValue(remarks);
								} else if (StringUtils.containsIgnoreCase(
										cellValue, "Both")
										|| StringUtils.containsIgnoreCase(
												cellValue, "Only")
										|| StringUtils.containsIgnoreCase(
												cellValue, "Neither")) {

									String remarks = ("Options contains Both or Neither--may be aor b type or 1 or 2 type");
									XSSFCell remarksCell = writeRow
											.createCell(headers.size());
									remarksCell.setCellValue(remarks);
								} else if (StringUtils.equalsIgnoreCase(
										cellValue, "a")
										|| StringUtils.equalsIgnoreCase(
												cellValue, "b")
										|| StringUtils.containsIgnoreCase(
												cellValue, "a)")
										|| StringUtils.containsIgnoreCase(
												cellValue, "A1")
										|| StringUtils.containsIgnoreCase(
												cellValue, "b)")) {
									String remarks = ("Options are of a or b or A or E type");
									XSSFCell remarksCell = writeRow
											.createCell(headers.size());
									remarksCell.setCellValue(remarks);
								} else if (StringUtils.equalsIgnoreCase(
										cellValue, "I")
										|| StringUtils.equalsIgnoreCase(
												cellValue, " II")
										|| StringUtils.containsIgnoreCase(
												cellValue, " II,")
										|| StringUtils.containsIgnoreCase(
												cellValue, " I,")) {
									String remarks = ("Options are of I or II type");
									XSSFCell remarksCell = writeRow
											.createCell(headers.size());
									remarksCell.setCellValue(remarks);
								} else

									for (int i = 0; i < options.size(); i++) {
										if (StringUtils.containsIgnoreCase(
												cellValue, options.get(i))) {

											String remarks = ("Repeated Options" + cellValue);
											XSSFCell remarksCell = writeRow
													.createCell(headers.size());
											remarksCell.setCellValue(remarks);
											isOptionRepeated = true;
										}
									}
								if (!isOptionRepeated) {
									options.add(cellValue);
								}
							} else {
								if (!StringUtils.isEmpty(cellValue))
									options.add(cellValue);
							}

						}
						if ("question_text".equalsIgnoreCase(headerValue)) {
							if (StringUtils.contains(cellValue, "Draw")
									|| StringUtils.contains(cellValue, "Image")
									|| StringUtils.contains(cellValue,
											"Diagram")
									|| StringUtils.contains(cellValue, "img")
									|| StringUtils.contains(cellValue,
											"Illustra")) {
								String remarks = ("Question contains Image or Draw or Diagram");
								XSSFCell remarksCell = writeRow
										.createCell(headers.size());
								remarksCell.setCellValue(remarks);
							}

							boolean isDuplicate = false;
							if (!questionTexts.isEmpty()) {

								for (int i = 0; i < questionTexts.size(); i++) {
									String v = StringUtils.trim(questionTexts
											.get(i));
									if (StringUtils.containsIgnoreCase(
											cellValue, v)) {
										String remarks = ("Duplicate question");
										isDuplicate = true;

										XSSFCell remarksCell = writeRow
												.createCell(headers.size());
										remarksCell.setCellValue(remarks);
										break;
									}
								}
								if (!isDuplicate) {
									if (!StringUtils.isEmpty(cellValue))
										questionTexts.add(cellValue);
								}

							} else {
								if (!StringUtils.isEmpty(cellValue))
									questionTexts.add(cellValue);
							}

						}

					}

				}
				rowCount++;

			}
		}
		
		/*for(short i=0; i<wb.getNumCellStyles(); i++)
		{
			XSSFCellStyle cs = test.createCellStyle();
			cs.cloneStyleFrom(wb.getCellStyleAt(i));
		}*/
		File f = new File(baseDir + File.separator + "Verified"
				+ File.separator + fileName);// International
												// Business.xlsx");
		if (!f.exists()) {
			f.createNewFile();
		}

		FileOutputStream fileOut = new FileOutputStream(f);
		test.write(fileOut);
		fileOut.close();
	}

	private static String getCellValue(XSSFCell cell) {
		String value = "";
		DataFormatter fmt = new DataFormatter();
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_STRING:
			//value = (cell.getRichStringCellValue().getString());
			value =fmt.formatCellValue(cell);
			break;
		case Cell.CELL_TYPE_NUMERIC:
			if (DateUtil.isCellDateFormatted(cell)) {
				//value = (cell.getDateCellValue() + "");
				value =fmt.formatCellValue(cell);
			} else {
				//value = (cell.getNumericCellValue() + "");
				value =fmt.formatCellValue(cell);
			}
			break;
		case Cell.CELL_TYPE_BOOLEAN:
			//value = (cell.getBooleanCellValue() + "");
			value =fmt.formatCellValue(cell);
			break;
		case Cell.CELL_TYPE_FORMULA:
			//value = (cell.getCellFormula());
			value =fmt.formatCellValue(cell);
			break;
		default:
			value = ("");
		}
		return value;
	}
}

