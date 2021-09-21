package com.spts.lms.helpers.excel;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.spts.lms.beans.course.Course;
import com.spts.lms.beans.user.UploadTimeLimitSession;
import com.spts.lms.daos.user.UploadTimeLimitSessionDao;

import com.spts.lms.services.user.UploadTimeLimitService;



@Component("UploadTimeLimitHelper")
@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
public class UploadTimeLimitHelper extends
		BaseExcelHelper<UploadTimeLimitSession> {
	
	@Autowired
	UploadTimeLimitService uploadTimeLimitService;
	
	private static final Logger logger = Logger
			.getLogger(UploadTimeLimitHelper.class);

	private static int indx = 0;

	private static enum ExcelFields {
		SESSION(indx++, Cell.CELL_TYPE_STRING), STARTDATE(indx++,
				Cell.CELL_TYPE_NUMERIC), ENDDATE(indx++, Cell.CELL_TYPE_NUMERIC);

		private int index;
		private int cellType;

		public int getIndex() {
			return index;
		}

		public int getCellType() {
			return cellType;
		}

		ExcelFields(int index, int cellType) {
			this.index = index;
			this.cellType = cellType;
		}
	}

	private int index;
	private int cellType;

	public int getIndex() {
		return index;
	}

	public int getCellType() {
		return cellType;
	}

	private UploadTimeLimitSession creatBean(final Row row) {
		UploadTimeLimitSession bean = new UploadTimeLimitSession();
		


		bean.setSession(getCellValueAsString(row,ExcelFields.SESSION.getIndex()));

		//bean.setStartDate(getCellValueAsString(row, ExcelFields.STARTDATE.getIndex()));
		//bean.setEndDate(getCellValueAsString(row, ExcelFields.ENDDATE.getIndex()));
		bean.setStartDate(getCellValueAsDate(row, ExcelFields.STARTDATE.getIndex()));
		
		bean.setEndDate(getCellValueAsDate(row, ExcelFields.ENDDATE.getIndex()));
		
		
	
		
		
		
		return bean;
	}

	@Autowired
	UploadTimeLimitSessionDao uploadTimelimitDao;

	@Override
	protected void visitRow(Row row, int rowNum) {
		if (row != null) {
			setAllCellTypes(row);
			UploadTimeLimitSession bean = creatBean(row);
			validateBean(bean, rowNum);
		}

	}
	
	private void setAllCellTypes(final Row row) {
		for (ExcelFields field : ExcelFields.values()) {
			setCellType(row, field.getIndex(), field.getCellType());
		}
	}

	@Override
	public void initHelper(UploadTimeLimitSession bean) {
		// TODO Auto-generated method stub

	}
	
	
	private void validateBean(final UploadTimeLimitSession bean, final int rowNum) {


		if (null == bean.getSession()) {
			setErrorRecord(bean, " Row : " + rowNum
					+ " Session  is mandatory ");
		}
		
		
	


		if (bean.isErrorRecord()) {
			getErrorList().add(bean);
		} else {
			getSuccessList().add(bean);
		}
	}



	@Override
	public void saveRows() {
		try{
		
		if (getErrorList().isEmpty()) {
			System.out.println("Hello World---------->");
			uploadTimeLimitService.insertBatch(getSuccessList());
		}
	}
	catch(Exception ex)
	{
		logger.info("+++++++++++"+ex.getMessage());
	}

}
}
