package com.spts.lms.helpers.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.spts.lms.beans.program.Program;
import com.spts.lms.services.program.ProgramService;

@Component("programExcelHelper")
@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
public class ProgramExcelHelper extends BaseExcelHelper<Program> {
	
	private static int indx = 0;
	private static enum ExcelFields {
		PROGRAM_ABBRVIATION(indx++, Cell.CELL_TYPE_STRING),
		PROGRAM_NAME(indx++, Cell.CELL_TYPE_STRING),
		PROGRAM_DURATION(indx++, Cell.CELL_TYPE_STRING),
		PROGRAM_MAX_DURATION(indx++, Cell.CELL_TYPE_STRING),
		PROGRAM_SESSION_TYPE(indx++, Cell.CELL_TYPE_STRING),
		PROGRAM_REVISION_MONTH(indx++, Cell.CELL_TYPE_STRING),
		PROGRAM_REVISION_YEAR(indx++, Cell.CELL_TYPE_STRING);
		
		
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
		
	@Autowired
	ProgramService programService;
	
	private String createdBy;
	
	public ProgramExcelHelper() {
		super();
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}


	@Override
	public void initHelper(Program bean) {
		setCreatedBy(bean.getCreatedBy());
	}

	@Override
	protected void visitRow(final Row row, final int rowNum) {
		if(row!=null){
				setAllCellTypes(row);
				Program bean = createBean(row);
				validateBean(bean, rowNum);
		}
		
	}
	
	private void setAllCellTypes(final Row row) {
		for(ExcelFields field : ExcelFields.values()) {
			setCellType(row, field.getIndex(), field.getCellType());
		}
	}
	
	private Program createBean(final Row row) {
		Program bean = new Program();
		bean.setAbbr(getCellValueAsString(row, ExcelFields.PROGRAM_ABBRVIATION.getIndex()));
		bean.setProgramName(getCellValueAsString(row, ExcelFields.PROGRAM_NAME.getIndex()));
		bean.setDurationInMonths(getCellValueAsShort(row, ExcelFields.PROGRAM_DURATION.getIndex()));
		bean.setMaxDurationInMonths(getCellValueAsShort(row, ExcelFields.PROGRAM_MAX_DURATION.getIndex()));
		bean.setSessionType(getCellValueAsString(row, ExcelFields.PROGRAM_SESSION_TYPE.getIndex()));
		bean.setRevisedFromMonth(getCellValueAsString(row, ExcelFields.PROGRAM_REVISION_MONTH.getIndex()));
		bean.setRevisedFromYear(getCellValueAsString(row, ExcelFields.PROGRAM_REVISION_YEAR.getIndex()));
		
		bean.setCreatedBy(createdBy);
		bean.setLastModifiedBy(createdBy);
		
		return bean;
	}

	private void validateBean(final Program bean, final int rowNum) {
		if(null == bean.getAbbr() || bean.getAbbr().isEmpty()) {
			setErrorRecord(bean, " Row : "+ rowNum +" Program Abbreviation is mandatory ");
		}
		
		if(null == bean.getProgramName() || bean.getProgramName().isEmpty()) {
			setErrorRecord(bean, " Row : "+ rowNum +" Program Name is mandatory ");
		}
		
		if(bean.isErrorRecord()) {
			getErrorList().add(bean);
		} else {
			getSuccessList().add(bean);
		}
	}

	@Override
	public void saveRows() {
		if(getErrorList().isEmpty()){
			programService.insertBatch(getSuccessList());
		}
	}

}
