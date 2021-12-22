package com.spts.lms.helpers.excel;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class ExcelReader {

	private static Workbook getWorkbook(InputStream inputStream,
			String excelFilePath) throws IOException {
		Workbook workbook = null;

		if (excelFilePath.endsWith("xlsx")) {
			workbook = new XSSFWorkbook(inputStream);
		} else if (excelFilePath.endsWith("xls")) {
			workbook = new HSSFWorkbook(inputStream);
		} else {
			throw new IllegalArgumentException(
					"The specified file is not Excel file");
		}

		return workbook;
	}

	public static List<Map<String, Object>> readExcelFileUsingColumnNum(
			String excelFilePath) throws IOException {

		FileInputStream inputStream = new FileInputStream(new File(
				excelFilePath));
		List<String> header = new ArrayList<String>();
		Map<String, Object> valueMap = null;
		List<Map<String, Object>> lstExcelData = new ArrayList<Map<String, Object>>();

		Workbook workbook = getWorkbook(inputStream, excelFilePath);
		Sheet firstSheet = workbook.getSheetAt(0);
		Iterator iterator = firstSheet.iterator();

		// set Header column no as Key
		Row headers = firstSheet.getRow(0);
		int headerColumn = headers.getLastCellNum();
		for (int i = 0; i < headerColumn; i++) {
			header.add(String.valueOf(i));
		}

		// reading 2nd rows onwards
		int rowNum = 0;
		while (iterator.hasNext()) {
			Row nextRow = (Row) iterator.next();
			int count = 0;
			valueMap = new HashMap<String, Object>();
			if (rowNum != 0) {
				for (int cellNumber = 0; cellNumber < headerColumn; cellNumber++) {
					Cell cell = nextRow.getCell(cellNumber);
					if (cell == null
							|| cell.getCellType() == Cell.CELL_TYPE_BLANK) {
						valueMap.put(header.get(count), null);
					} else {
						valueMap.put(header.get(count), getCellValue(cell));
					}
					count++;
				}
			}
			rowNum++;
			if (!valueMap.isEmpty()) {
				lstExcelData.add(valueMap);
			}
		}
		workbook.close();
		inputStream.close();
		return lstExcelData;
	}

	public static List<Map<String, Object>> readExcelFileUsingColumnHeader(
			MultipartFile file, List<String> ValidateHeader) throws IOException {

		// FileInputStream inputStream = (FileInputStream) file;
		InputStream inputStream = new BufferedInputStream(file.getInputStream());
		List<String> header = new ArrayList<String>();
		Map<String, Object> valueMap = null;
		List<Map<String, Object>> lstExcelData = new ArrayList<Map<String, Object>>();

		Workbook workbook = getWorkbook(inputStream, file.getOriginalFilename());
		Sheet firstSheet = workbook.getSheetAt(0);
		Iterator iterator = firstSheet.iterator();
		/*
		 * StreamingReader reader = StreamingReader.builder() .rowCacheSize(100)
		 * // number of rows to keep in memory (defaults to 10)
		 * .bufferSize(4096) // buffer size to use when reading InputStream to
		 * file (defaults to 1024) .sheetIndex(0) // index of sheet to use
		 * (defaults to 0) .read(inputStream); // InputStream or File for XLSX
		 * file (required) reader.close();
		 */
		// set Header column Header value as Key
		Row headers = firstSheet.getRow(0);
		int headerColumn = headers.getLastCellNum();
		Iterator HeadercellIterator = headers.cellIterator();
		for (int i = 0; i < headerColumn; i++) {
			Cell nextCell = (Cell) HeadercellIterator.next();
			header.add((String) getCellValue(nextCell));
		}
		
		// reading 2nd rows onwards
		int rowNum = 0;

		while (iterator.hasNext()) {
			Row nextRow = (Row) iterator.next();
			int count = 0;
			String ErrorMessage = "";
			valueMap = new HashMap<String, Object>();
			if (rowNum != 0) {
				if (ValidateHeader.size() != header.size()) {
					//ErrorMessage = "excel format is wrong ";
					throw new IllegalArgumentException(
							"Excel format is wrong");
					
				}
				for (int cellNumber = 0; cellNumber < headerColumn; cellNumber++) {
					Cell cell = nextRow.getCell(cellNumber);
					if (cell == null
							|| cell.getCellType() == Cell.CELL_TYPE_BLANK) {
						
						valueMap.put(header.get(count), "");

						// validate Headers value as Null or not
						
						if (ValidateHeader.contains(header.get(count))) {
							
							/*
							 * if (ErrorMessage != null && ErrorMessage != "") {
							 * 
							 * ErrorMessage = ErrorMessage + header.get(count) +
							 * " Is Blank RowNum:ColumnNum " + rowNum + "," +
							 * count + "|"; System.out.println("Error Message" +
							 * ErrorMessage); } else { ErrorMessage =
							 * header.get(count) + " Is Blank RowNum:ColumnNum "
							 * + rowNum + "," + count + "|";
							 * System.out.println("Error Message" +
							 * ErrorMessage); }
							 */
						} else {
							ErrorMessage = "excel format is wrong ";
						
						}
					} else {
						//valueMap.put(header.get(count), getCellValue(cell));
						if("option1".equals(header.get(count)) || "option2".equals(header.get(count)) ||
								"option3".equals(header.get(count)) || "option4".equals(header.get(count)) ||
								"option5".equals(header.get(count)) || "option6".equals(header.get(count)) ||
								"option7".equals(header.get(count)) || "option8".equals(header.get(count))) {
							DataFormatter fmt = new DataFormatter();
							String valueAsSeenInExcel = fmt.formatCellValue(cell);
							valueMap.put(header.get(count), valueAsSeenInExcel);
						}else {
							valueMap.put(header.get(count), getCellValue(cell));
						}
						if (valueMap.containsKey("correctOption")) {
							
							if (!ISVALIDINPUT((String) valueMap
									.get("correctOption"))) {
								if (ErrorMessage != null && ErrorMessage != "") {
									ErrorMessage = ErrorMessage
											+ header.get(count)
											+ " Not Numeric RowNum:ColumnNum "
											+ rowNum + "," + count + " |";
								} else {
									ErrorMessage = header.get(count)
											+ " Not Numeric RowNum:ColumnNum "
											+ rowNum + "," + count + " |";
								}
							}
						}
						if (valueMap.containsKey("marks")) {
						
							if (!ISVALIDINPUT((String) valueMap.get("marks"))) {
								if (ErrorMessage != null && ErrorMessage != "") {
									ErrorMessage = ErrorMessage
											+ header.get(count)
											+ " Not Numeric RowNum:ColumnNum "
											+ rowNum + "," + count + " |";
								} else {
									ErrorMessage = header.get(count)
											+ " Not Numeric RowNum:ColumnNum "
											+ rowNum + "," + count + " |";
								}
							}
						}
						
						
						
						
						if (valueMap.containsKey("answerRangeFrom")) {

							if (!ISVALIDINPUT((String) valueMap
									.get("answerRangeFrom"))) {
								if (ErrorMessage != null && ErrorMessage != "") {
									ErrorMessage = ErrorMessage
											+ header.get(count)
											+ " Not Numeric RowNum:ColumnNum "
											+ rowNum + "," + count + " |";
								} else {
									ErrorMessage = header.get(count)
											+ " Not Numeric RowNum:ColumnNum "
											+ rowNum + "," + count + " |";
								}
							}
						}

						if (valueMap.containsKey("answerRangeTo")) {

							if (!ISVALIDINPUT((String) valueMap
									.get("answerRangeTo"))) {
								if (ErrorMessage != null && ErrorMessage != "") {
									ErrorMessage = ErrorMessage
											+ header.get(count)
											+ " Not Numeric RowNum:ColumnNum "
											+ rowNum + "," + count + " |";
								} else {
									ErrorMessage = header.get(count)
											+ " Not Numeric RowNum:ColumnNum "
											+ rowNum + "," + count + " |";
								}
							}
						}

                        
						if (valueMap.containsKey("ASSIGNED SCORE")) {
							
							if (!ISVALIDINPUT((String) valueMap.get("ASSIGNED SCORE"))) {
								if (ErrorMessage != null && ErrorMessage != "") {
									ErrorMessage = ErrorMessage
											+ header.get(count)
											+ " Not Numeric RowNum:ColumnNum "
											+ rowNum + "," + count + " |";
								} else {
									ErrorMessage = header.get(count)
											+ " Not Numeric RowNum:ColumnNum "
											+ rowNum + "," + count + " |";
								}
							}
						}
						
					
						// validate Headers value as Number or not
						if (ValidateHeader.contains(header.get(count))) {
							/*
							 * if (!ISNUMBER((String) getCellValue(cell))) { if
							 * (ErrorMessage != null && ErrorMessage != "") {
							 * ErrorMessage = ErrorMessage + header.get(count) +
							 * " Not Numeric RowNum:ColumnNum " + rowNum + "," +
							 * count + " |"; } else { ErrorMessage =
							 * header.get(count) +
							 * " Not Numeric RowNum:ColumnNum " + rowNum + "," +
							 * count + " |"; } }
							 */
						} else {
							ErrorMessage = "excel format is wrong ";
							
						}
					}

					valueMap.put("Error", removeLastChar(ErrorMessage));
					count++;
				}
			}
			rowNum++;
			if (!valueMap.isEmpty()) {
				lstExcelData.add(valueMap);
				

			}
		}
		workbook.close();
		inputStream.close();

		// ExcelCreater.CreateExcelFile(lstExcelData,header);
		
		return lstExcelData;
	}

	public static List<Map<String, Object>> readExcelFileUsingColumnHeaderForMultipleSheets(
			MultipartFile file, List<String> ValidateHeader) throws IOException {

		// FileInputStream inputStream = (FileInputStream) file;
		InputStream inputStream = new BufferedInputStream(file.getInputStream());
		List<String> header = new ArrayList<String>();
		Map<String, Object> valueMap = null;
		List<Map<String, Object>> lstExcelData = new ArrayList<Map<String, Object>>();

		Workbook workbook = getWorkbook(inputStream, file.getOriginalFilename());
		int noOfSheets = workbook.getNumberOfSheets();
		for (int n = 0; n < noOfSheets; n++) {
			Sheet firstSheet = workbook.getSheetAt(n);
			Iterator iterator = firstSheet.iterator();
			/*
			 * StreamingReader reader = StreamingReader.builder()
			 * .rowCacheSize(100) // number of rows to keep in memory (defaults
			 * to 10) .bufferSize(4096) // buffer size to use when reading
			 * InputStream to file (defaults to 1024) .sheetIndex(0) // index of
			 * sheet to use (defaults to 0) .read(inputStream); // InputStream
			 * or File for XLSX file (required) reader.close();
			 */
			// set Header column Header value as Key
			Row headers = firstSheet.getRow(0);
			int headerColumn = headers.getLastCellNum();
			Iterator HeadercellIterator = headers.cellIterator();
			for (int i = 0; i < headerColumn; i++) {
				Cell nextCell = (Cell) HeadercellIterator.next();
				header.add((String) getCellValue(nextCell));
			}
			
			// reading 2nd rows onwards
			int rowNum = 0;

			while (iterator.hasNext()) {
				Row nextRow = (Row) iterator.next();
				int count = 0;
				String ErrorMessage = "";
				valueMap = new HashMap<String, Object>();
				if (rowNum != 0) {
					for (int cellNumber = 0; cellNumber < headerColumn; cellNumber++) {
						Cell cell = nextRow.getCell(cellNumber);
						if (cell == null
								|| cell.getCellType() == Cell.CELL_TYPE_BLANK) {
							
							valueMap.put(header.get(count), "");

							// validate Headers value as Null or not
							
							if (ValidateHeader.contains(header.get(count))) {
								
								/*
								 * if (ErrorMessage != null && ErrorMessage !=
								 * "") {
								 * 
								 * ErrorMessage = ErrorMessage +
								 * header.get(count) +
								 * " Is Blank RowNum:ColumnNum " + rowNum + ","
								 * + count + "|";
								 * System.out.println("Error Message" +
								 * ErrorMessage); } else { ErrorMessage =
								 * header.get(count) +
								 * " Is Blank RowNum:ColumnNum " + rowNum + ","
								 * + count + "|";
								 * System.out.println("Error Message" +
								 * ErrorMessage); }
								 */
							} else {
								ErrorMessage = "excel format is wrong ";
								
							}
						} else {
							valueMap.put(header.get(count), getCellValue(cell));

							if (valueMap.containsKey("correctOption")) {
								
								if (!ISVALIDINPUT((String) valueMap
										.get("correctOption"))) {
									if (ErrorMessage != null
											&& ErrorMessage != "") {
										ErrorMessage = ErrorMessage
												+ header.get(count)
												+ " Not Numeric RowNum:ColumnNum "
												+ rowNum + "," + count + " |";
									} else {
										ErrorMessage = header.get(count)
												+ " Not Numeric RowNum:ColumnNum "
												+ rowNum + "," + count + " |";
									}
								}
							}
							if (valueMap.containsKey("marks")) {
								
								if (!ISVALIDINPUT((String) valueMap
										.get("marks"))) {
									if (ErrorMessage != null
											&& ErrorMessage != "") {
										ErrorMessage = ErrorMessage
												+ header.get(count)
												+ " Not Numeric RowNum:ColumnNum "
												+ rowNum + "," + count + " |";
									} else {
										ErrorMessage = header.get(count)
												+ " Not Numeric RowNum:ColumnNum "
												+ rowNum + "," + count + " |";
									}
								}
							}

							
							// validate Headers value as Number or not
							if (ValidateHeader.contains(header.get(count))) {
								/*
								 * if (!ISNUMBER((String) getCellValue(cell))) {
								 * if (ErrorMessage != null && ErrorMessage !=
								 * "") { ErrorMessage = ErrorMessage +
								 * header.get(count) +
								 * " Not Numeric RowNum:ColumnNum " + rowNum +
								 * "," + count + " |"; } else { ErrorMessage =
								 * header.get(count) +
								 * " Not Numeric RowNum:ColumnNum " + rowNum +
								 * "," + count + " |"; } }
								 */
							} else {
								ErrorMessage = "excel format is wrong ";
								
							}
						}

						valueMap.put("Error", removeLastChar(ErrorMessage));
						count++;
					}
				}
				rowNum++;
				if (!valueMap.isEmpty()) {
					lstExcelData.add(valueMap);
					

				}
			}
		}
		workbook.close();
		inputStream.close();

		// ExcelCreater.CreateExcelFile(lstExcelData,header);
		
		return lstExcelData;
	}

	private static Object getCellValue(Cell cell) {
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_STRING:
			return cell.getStringCellValue();

		case Cell.CELL_TYPE_BOOLEAN:
			return cell.getBooleanCellValue();

		case Cell.CELL_TYPE_NUMERIC:
			if (DateUtil.isCellDateFormatted(cell)) {
				return cell.getDateCellValue();
			} else {
				cell.setCellType(Cell.CELL_TYPE_STRING);
				return cell.getStringCellValue();
			}

		case Cell.CELL_TYPE_BLANK:
			return null;
		}
		return null;
	}

	// removing Last Character from ErrorMessage Mobile Number Not Numeric Row
	// Num:Column Num 4,4 |
	private static String removeLastChar(String str) {
		if (str != null && str != "") {
			return str.substring(0, str.length() - 1);
		}
		return null;
	}

	// checking given String is Number or not
	public static boolean ISNUMBER(String input) {
		if (input != null) {
			if (input.matches("[0-9]+")) {
				return true;
			}
		}
		return false;
	}

	public static boolean ISVALIDINPUT(String input) {
		if (input != null) {
			if (input.matches("[0-9.]+") || input.matches("[0-9.,]+")
					|| input.contains(" ") || input.trim().isEmpty() || NumberUtils.isNumber(input)) {
				return true;
			}
		}
		return false;
	}

	// checking Particular String Contains Numeric Value
	public final boolean containsDigit(String s) {

		boolean containsDigit = false;
		if (s != null && !s.isEmpty()) {
			for (char c : s.toCharArray()) {
				if (containsDigit = Character.isDigit(c)) {
					return true;
				}
			}
		}
		return containsDigit;
	}

	public static List<Map<String, String>> readXLSXFile(String filePath) {
		List<Map<String, String>> mapList = new ArrayList<Map<String, String>>();
		InputStream excelToRead;
		try {
			XSSFRow row;
			XSSFCell cell;
			excelToRead = new FileInputStream(filePath);
			XSSFWorkbook wb = new XSSFWorkbook(excelToRead);
			XSSFSheet sheet = wb.getSheetAt(0);
			Row firstRow = sheet.getRow(0);
			int length = firstRow.getLastCellNum();

			Cell temp = null;
			List<String> header = new ArrayList<String>();

			for (int i = 0; i < length; i++) {
				temp = firstRow.getCell(i);
				header.add(temp.getStringCellValue());
			}
			

			Iterator rows = sheet.rowIterator();
			int rownum = 0;
			while (rows.hasNext()) {
				Map<String, String> valueMap = new HashMap();
				row = (XSSFRow) rows.next();
				int count = 0;
				if (rownum != 0) {
					Iterator cells = row.cellIterator();
					while (cells.hasNext()) {

						String cellValue = "";
						cell = (XSSFCell) cells.next();
						switch (cell.getCellType()) {

						case XSSFCell.CELL_TYPE_STRING:
							cellValue = cell.getStringCellValue();
							break;
						case XSSFCell.CELL_TYPE_NUMERIC:

							if (DateUtil.isCellDateFormatted(cell)) {
								SimpleDateFormat dateFormat = new SimpleDateFormat(
										"dd-MM-yyyy");
								cellValue = dateFormat.format(cell
										.getDateCellValue());
							} else
								cellValue = NumberToTextConverter.toText(cell
										.getNumericCellValue());
							break;
						case XSSFCell.CELL_TYPE_BOOLEAN:
							cellValue = cell.getBooleanCellValue() + "";
							break;
						}
					
						valueMap.put(header.get(count), cellValue);
						count++;
					}
				}
				rownum++;
				if (!valueMap.isEmpty())
					mapList.add(valueMap);

			}
		} catch (Exception e) {
		}
		return mapList;

	}

	public static List<Map<String, String>> readXLSXFileForBonafide(
			String filePath) {
		List<Map<String, String>> mapList = new ArrayList<Map<String, String>>();
		InputStream excelToRead;
		try {
			XSSFRow row;
			XSSFCell cell;
			excelToRead = new FileInputStream(filePath);
			XSSFWorkbook wb = new XSSFWorkbook(excelToRead);
			XSSFSheet sheet = wb.getSheetAt(0);
			Row firstRow = sheet.getRow(0);
			int length = firstRow.getLastCellNum();

			Cell temp = null;
			List<String> header = new ArrayList<String>();

			for (int i = 0; i < length; i++) {
				temp = firstRow.getCell(i);
				header.add(temp.getStringCellValue());
			}
			

			Iterator rows = sheet.rowIterator();
			int rownum = 0;
			while (rows.hasNext()) {
				Map<String, String> valueMap = new HashMap();
				row = (XSSFRow) rows.next();
				int count = 0;
				if (rownum != 0) {
					Iterator cells = row.cellIterator();
					while (cells.hasNext()) {

						String cellValue = "";
						cell = (XSSFCell) cells.next();
						switch (cell.getCellType()) {
						case XSSFCell.CELL_TYPE_BLANK:
							
							cellValue = "";
							break;
						case XSSFCell.CELL_TYPE_STRING:
							cellValue = cell.getStringCellValue();
							break;
						case XSSFCell.CELL_TYPE_NUMERIC:

							if (DateUtil.isCellDateFormatted(cell)) {
								SimpleDateFormat dateFormat = new SimpleDateFormat(
										"dd-MM-yyyy");
								cellValue = dateFormat.format(cell
										.getDateCellValue());
							} else
								cellValue = NumberToTextConverter.toText(cell
										.getNumericCellValue());
							break;
						case XSSFCell.CELL_TYPE_BOOLEAN:
							cellValue = cell.getBooleanCellValue() + "";
							break;
						}
						
						valueMap.put(header.get(count), cellValue);
						count++;
					}
				}
				rownum++;
				if (!valueMap.isEmpty())
					mapList.add(valueMap);

			}
		} catch (Exception e) {
		}
		return mapList;

	}
	
	public static List<Map<String, Object>> readIcaExcelFileUsingColumnHeader(
			MultipartFile file, List<String> ValidateHeader) throws IOException {

		// FileInputStream inputStream = (FileInputStream) file;
		InputStream inputStream = new BufferedInputStream(file.getInputStream());
		List<String> header = new ArrayList<String>();
		Map<String, Object> valueMap = null;
		List<Map<String, Object>> lstExcelData = new ArrayList<Map<String, Object>>();

		XSSFWorkbook  workbook = getXssfWorkbook(inputStream, file.getOriginalFilename());
		XSSFSheet firstSheet = workbook.getSheetAt(0);
		Iterator iterator = firstSheet.iterator();
		/*
		 * StreamingReader reader = StreamingReader.builder() .rowCacheSize(100)
		 * // number of rows to keep in memory (defaults to 10)
		 * .bufferSize(4096) // buffer size to use when reading InputStream to
		 * file (defaults to 1024) .sheetIndex(0) // index of sheet to use
		 * (defaults to 0) .read(inputStream); // InputStream or File for XLSX
		 * file (required) reader.close();
		 */
		// set Header column Header value as Key
		Row headers = firstSheet.getRow(0);
		int headerColumn = headers.getLastCellNum();
		Iterator HeadercellIterator = headers.cellIterator();
		for (int i = 0; i < headerColumn; i++) {
			Cell nextCell = (Cell) HeadercellIterator.next();
			header.add((String) getCellValue(nextCell));
		}
		
		// reading 2nd rows onwards
		int rowNum = 0;

		while (iterator.hasNext()) {
			Row nextRow = (Row) iterator.next();
			int count = 0;
			String ErrorMessage = "";
			valueMap = new HashMap<String, Object>();
			if (rowNum != 0) {
				if (ValidateHeader.size() != header.size()) {
					ErrorMessage = "excel format is wrong ";
					
				}
				for (int cellNumber = 0; cellNumber < headerColumn; cellNumber++) {
					Cell cell = nextRow.getCell(cellNumber);
					if (cell == null
							|| cell.getCellType() == Cell.CELL_TYPE_BLANK) {
						
						valueMap.put(header.get(count), "");

						// validate Headers value as Null or not
						
						if (ValidateHeader.contains(header.get(count))) {
							
							/*
							 * if (ErrorMessage != null && ErrorMessage != "") {
							 * 
							 * ErrorMessage = ErrorMessage + header.get(count) +
							 * " Is Blank RowNum:ColumnNum " + rowNum + "," +
							 * count + "|"; System.out.println("Error Message" +
							 * ErrorMessage); } else { ErrorMessage =
							 * header.get(count) + " Is Blank RowNum:ColumnNum "
							 * + rowNum + "," + count + "|";
							 * System.out.println("Error Message" +
							 * ErrorMessage); }
							 */
						} else {
							ErrorMessage = "excel format is wrong ";
						
						}
					} else {
						valueMap.put(header.get(count), getCellValue(cell));

						if (valueMap.containsKey("correctOption")) {
							
							if (!ISVALIDINPUT((String) valueMap
									.get("correctOption"))) {
								if (ErrorMessage != null && ErrorMessage != "") {
									ErrorMessage = ErrorMessage
											+ header.get(count)
											+ " Not Numeric RowNum:ColumnNum "
											+ rowNum + "," + count + " |";
								} else {
									ErrorMessage = header.get(count)
											+ " Not Numeric RowNum:ColumnNum "
											+ rowNum + "," + count + " |";
								}
							}
						}
						if (valueMap.containsKey("marks")) {
						
							if (!ISVALIDINPUT((String) valueMap.get("marks"))) {
								if (ErrorMessage != null && ErrorMessage != "") {
									ErrorMessage = ErrorMessage
											+ header.get(count)
											+ " Not Numeric RowNum:ColumnNum "
											+ rowNum + "," + count + " |";
								} else {
									ErrorMessage = header.get(count)
											+ " Not Numeric RowNum:ColumnNum "
											+ rowNum + "," + count + " |";
								}
							}
						}
						
						
						if (valueMap.containsKey("answerRangeFrom")) {

							if (!ISVALIDINPUT((String) valueMap
									.get("answerRangeFrom"))) {
								if (ErrorMessage != null && ErrorMessage != "") {
									ErrorMessage = ErrorMessage
											+ header.get(count)
											+ " Not Numeric RowNum:ColumnNum "
											+ rowNum + "," + count + " |";
								} else {
									ErrorMessage = header.get(count)
											+ " Not Numeric RowNum:ColumnNum "
											+ rowNum + "," + count + " |";
								}
							}
						}

						if (valueMap.containsKey("answerRangeTo")) {

							if (!ISVALIDINPUT((String) valueMap
									.get("answerRangeTo"))) {
								if (ErrorMessage != null && ErrorMessage != "") {
									ErrorMessage = ErrorMessage
											+ header.get(count)
											+ " Not Numeric RowNum:ColumnNum "
											+ rowNum + "," + count + " |";
								} else {
									ErrorMessage = header.get(count)
											+ " Not Numeric RowNum:ColumnNum "
											+ rowNum + "," + count + " |";
								}
							}
						}


					
						// validate Headers value as Number or not
						if (ValidateHeader.contains(header.get(count))) {
							/*
							 * if (!ISNUMBER((String) getCellValue(cell))) { if
							 * (ErrorMessage != null && ErrorMessage != "") {
							 * ErrorMessage = ErrorMessage + header.get(count) +
							 * " Not Numeric RowNum:ColumnNum " + rowNum + "," +
							 * count + " |"; } else { ErrorMessage =
							 * header.get(count) +
							 * " Not Numeric RowNum:ColumnNum " + rowNum + "," +
							 * count + " |"; } }
							 */
						} else {
							ErrorMessage = "excel format is wrong ";
							
						}
					}

					valueMap.put("Error", removeLastChar(ErrorMessage));
					count++;
				}
			}
			rowNum++;
			if (!valueMap.isEmpty()) {
				lstExcelData.add(valueMap);
				

			}
		}
		workbook.close();
		inputStream.close();

		// ExcelCreater.CreateExcelFile(lstExcelData,header);
		
		return lstExcelData;
	}
	
	private static XSSFWorkbook getXssfWorkbook(InputStream inputStream,
			String excelFilePath) throws IOException {
		XSSFWorkbook workbook = null;

		if (excelFilePath.endsWith("xlsx")) {
			workbook = new XSSFWorkbook(inputStream);
		} else if (excelFilePath.endsWith("xls")) {
			workbook = new XSSFWorkbook(inputStream);
		} else {
			throw new IllegalArgumentException(
					"The specified file is not Excel file");
		}

		return workbook;
	}

}
