package com.spts.lms.helpers.excel;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import com.spts.lms.beans.BaseBean;

public abstract class BaseExcelHelper<T extends BaseBean> {

	private List<T> successList = new CopyOnWriteArrayList<T>();
	private List<T> errorList = new CopyOnWriteArrayList<T>();
	private Workbook workbook;

	public List<T> getSuccessList() {
		return successList;
	}

	public List<T> getErrorList() {
		return errorList;
	}

	public Workbook getWorkbook() {
		return workbook;
	}

	protected abstract void visitRow(final Row row, final int rowNum);

	protected void setCellType(Row row, int index, int cellType) {
		row.getCell(index, Row.CREATE_NULL_AS_BLANK).setCellType(cellType);
	}

	protected String getCellValueAsString(Row row, int index) {
		return row.getCell(index, Row.CREATE_NULL_AS_BLANK)
				.getStringCellValue();
	}

	protected Integer getCellValueAsInteger(Row row, int index) {
		return Integer.valueOf(row.getCell(index, Row.CREATE_NULL_AS_BLANK)
				.getStringCellValue());
	}

	protected Short getCellValueAsShort(Row row, int index) {
		return Short.valueOf(row.getCell(index, Row.CREATE_NULL_AS_BLANK)
				.getStringCellValue());
	}
	


	protected Date getCellValueAsDate(Row row, int index) {
		return row.getCell(index, Row.CREATE_NULL_AS_BLANK)
				.getDateCellValue();
	}

	public abstract void initHelper(T bean);

	public abstract void saveRows();

	protected void setErrorRecord(T bean, String errorMessage) {
		String earlierErrors = bean.getErrorMessage() != null ? bean
				.getErrorMessage() + "<br/>" : "";
		bean.setErrorMessage(earlierErrors + errorMessage);
		bean.setErrorRecord(true);
	}

	public Workbook readExcel(MultipartFile fileData) throws Exception {
		ByteArrayInputStream bis = new ByteArrayInputStream(fileData.getBytes());

		try {
			if (fileData.getOriginalFilename().endsWith("xls")) {
				workbook = new HSSFWorkbook(bis);
			} else if (fileData.getOriginalFilename().endsWith("xlsx")) {
				workbook = new XSSFWorkbook(bis);
			} else {
				throw new IllegalArgumentException(
						"Received file does not have a standard excel extension.");
			}

			// Get first/desired sheet from the workbook
			XSSFSheet sheet = (XSSFSheet) workbook.getSheetAt(0);
			Iterator<Row> rowIterator = sheet.iterator();
			final BaseExcelHelper<T> excelHelper = this;
			final List<Callable<Boolean>> futureList = new ArrayList<Callable<Boolean>>();
			int i = 0;
			boolean inData = true;
			// Skip first row since it contains column names, not data.
			while (rowIterator.hasNext() && inData) {
				final int rowNum = i++;
				final Row row = rowIterator.next();
				DataFormatter formatter = new DataFormatter();
				String val = formatter.formatCellValue(row.getCell(0));
				if (row == null
						|| val.isEmpty()) {
					inData = false;
				} else {
					

					if (rowNum == 0)
						continue;
					// Process rows in parallel if row count more than 200
					if (sheet.getPhysicalNumberOfRows() > 1000) {
						futureList.add(new Callable<Boolean>() {

							public Boolean call() throws Exception {
								excelHelper.visitRow(row, rowNum);
								return true;
							}

						});
					} else
						visitRow(row, rowNum);
				}

			}
			/*
			 * if(!futureList.isEmpty())
			 * taskExecutor.getThreadPoolExecutor().invokeAll(futureList);
			 */

			saveRows();

		} catch (Exception e) {
			throw e;
		}

		return workbook;
	}

}
