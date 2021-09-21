package com.spts.lms.helpers.excel;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.spts.lms.beans.program.Program;
import com.spts.lms.beans.student.StudentProgramSession;
import com.spts.lms.beans.user.User;
import com.spts.lms.services.program.ProgramService;
import com.spts.lms.services.student.StudentProgramSessionService;
import com.spts.lms.services.user.UserService;

@Component("studentProgramSessionExcelHelper")
@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
public class StudentProgramSessionExcelHelper extends BaseExcelHelper<StudentProgramSession> {
	
	private static int indx = 0;
	private static enum ExcelFields {
		PROGRAM(indx++, Cell.CELL_TYPE_STRING), STUDENT(indx++, Cell.CELL_TYPE_STRING), SESSION(indx++, Cell.CELL_TYPE_STRING);
		
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
	
	private final Set<String> programSet;
	
	@Autowired
	ProgramService programService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	StudentProgramSessionService studentProgramSessionService;
	
	private String createdBy;
	
	private String acadMonth;
	
	private Integer acadYear;

	public StudentProgramSessionExcelHelper() {
		super();
		programSet = new HashSet<String>();
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getAcadMonth() {
		return acadMonth;
	}

	public void setAcadMonth(String acadMonth) {
		this.acadMonth = acadMonth;
	}

	public Integer getAcadYear() {
		return acadYear;
	}

	public void setAcadYear(Integer acadYear) {
		this.acadYear = acadYear;
	}
	
	@Override
	public void initHelper(StudentProgramSession bean) {
		setAcadMonth(bean.getAcadMonth());
		setAcadYear(bean.getAcadYear());
		setCreatedBy(bean.getCreatedBy());
		
	}

	@Override
	protected void visitRow(final Row row, final int rowNum) {
		if(row!=null){
			setAllCellTypes(row);
			StudentProgramSession bean = createBean(row);
			validateBean(bean, rowNum);
		}
		
	}
	
	private void setAllCellTypes(final Row row) {
		for(ExcelFields field : ExcelFields.values()) {
			setCellType(row, field.getIndex(), field.getCellType());
		}
	}
	
	private StudentProgramSession createBean(final Row row) {
		StudentProgramSession bean = new StudentProgramSession();
		Program program = new Program();
		program.setAbbr(getCellValueAsString(row, ExcelFields.PROGRAM.getIndex()));
		bean.setProgram(program);
		bean.setUsername(getCellValueAsString(row, ExcelFields.STUDENT.getIndex()));
		bean.setSession(getCellValueAsInteger(row, ExcelFields.SESSION.getIndex()));
		
		bean.setAcadMonth(acadMonth);
		bean.setAcadYear(acadYear);
		bean.setCreatedBy(createdBy);
		bean.setLastModifiedBy(createdBy);
		
		return bean;
	}

	private void validateBean(final StudentProgramSession bean, final int rowNum) {
		if(null == bean.getUsername() || bean.getUsername().isEmpty()) {
			setErrorRecord(bean, " Row : "+ rowNum +" UserId is mandatory ");
		} else if(null == bean.getSession() || bean.getSession() <= 0) {
			setErrorRecord(bean, " Row : "+ rowNum +" Invalid Session Number(Can only be 1 and above)");
		} else if(null == bean.getProgram().getAbbr() || bean.getProgram().getAbbr().isEmpty()) {
			setErrorRecord(bean, " Row : "+ rowNum +" Program Code is mandatory");
		}
		User user = userService.findUserAndProgramByUserName(bean.getUsername());
		
		if(!programSet.contains(bean.getProgram().getAbbr())) {
			setErrorRecord(bean, " Row : "+ rowNum +" Program code "+bean.getProgram().getAbbr()+" does not exist");
		} else if(null == user) {
			setErrorRecord(bean, " Row : "+ rowNum +" Student "+bean.getUsername()+" not found");
		} else if(! user.getAbbr().equals(bean.getProgram().getAbbr())) {
			setErrorRecord(bean, " Row : "+ rowNum +" Student "+bean.getUsername()+" is not enrolled for program "+bean.getProgram().getAbbr());
		}
		if(bean.isErrorRecord()) {
			getErrorList().add(bean);
		} else {
			bean.getProgram().setId(user.getProgramId());
			getSuccessList().add(bean);
		}
	}
	
	@PostConstruct
	private void loadProgramAbbrMap() {
		List<Program> programs = programService.findAll();
		for(Program program : programs) {
			programSet.add(program.getAbbr());
		}
	}

	@Override
	public void saveRows() {
		if(getErrorList().isEmpty())
			studentProgramSessionService.insertBatch(getSuccessList());
	}

}
