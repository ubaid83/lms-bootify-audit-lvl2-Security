package com.spts.lms.helpers.excel;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import com.spts.lms.beans.course.Course;

public class ReadWeightExcelFile {
	public List<List<String>> readWeightFile(MultipartFile file,
			List<Course> allCourses) throws IOException {
		// String excelFilePath = "E://workDir//weightTemplate.xlsx";
		InputStream inputStream = new BufferedInputStream(file.getInputStream());

		/*
		 * FileInputStream fileInputStream = new FileInputStream(new File(
		 * excelFilePath));
		 */
		Workbook workbook = getWorkbook(inputStream, file.getOriginalFilename());
		// Workbook workbook = new XSSFWorkbook(inputStream);
		MissingCellPolicy missCell = workbook.getMissingCellPolicy();
		workbook.setMissingCellPolicy(missCell);
		Sheet firstSheet = workbook.getSheetAt(0);
		Iterator<Row> iterator = firstSheet.iterator();
		List<String> header = new ArrayList<String>();
		Map<String, Object> valueMap = null;

		List<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();
		List<List<String>> finalList = new ArrayList<List<String>>();

		while (iterator.hasNext()) {
			List<String> dataList = new ArrayList<String>();
			Row nextRow = iterator.next();

			for (int i = 0; i < nextRow.getLastCellNum(); i++) {
				if (nextRow.getRowNum() == 0) {
					int headerColumn = nextRow.getLastCellNum();
					Iterator HeadercellIterator = nextRow.cellIterator();
					for (int j = 0; j < headerColumn; j++) {
						Cell nextCell = (Cell) HeadercellIterator.next();
						header.add(nextCell.getStringCellValue());
					}

				} else {

					Cell cell = nextRow.getCell(i);
					
					if (cell == null) {
						dataList.add(null);
					} else {

						if (cell.getCellType() == 1) {
							dataList.add(cell.getStringCellValue());
						}

						if (cell.getCellType() == 0) {
							dataList.add(String.valueOf(cell
									.getNumericCellValue()));
						}
					}

					/*
					 * switch (cell.getCellType()) {
					 * 
					 * case Cell.CELL_TYPE_STRING: //
					 * System.out.print(cell.getStringCellValue());
					 * dataList.add(cell.getStringCellValue()); break; case
					 * Cell.CELL_TYPE_BOOLEAN: //
					 * System.out.print(cell.getBooleanCellValue()); break; case
					 * Cell.CELL_TYPE_NUMERIC:
					 * dataList.add(String.valueOf(cell.getNumericCellValue()));
					 * // System.out.print(cell.getNumericCellValue()); break;
					 * 
					 * case Cell.CELL_TYPE_BLANK:
					 * System.out.println("Cell TYPE BLANK");
					 * 
					 * dataList.add(null);
					 * 
					 * }
					 */
				}

			}
			
			List<String> listWithoutDuplicates = header.stream()
				     .distinct()
				     .collect(Collectors.toList());
			
			List<String> courseList = new ArrayList<String>();
			courseList.add("WeightType");
			for (Course c : allCourses) {
				
				courseList.add(c.getCourseName());
			}

			

			Iterator<Cell> cellIterator = nextRow.cellIterator();

			if (courseList.equals(listWithoutDuplicates)) {
				
				if (!dataList.isEmpty())
					finalList.add(dataList);
			}
			

		}

		workbook.close();
		inputStream.close();
		
		return finalList;
	}

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
}
