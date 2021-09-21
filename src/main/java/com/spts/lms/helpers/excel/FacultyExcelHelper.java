package com.spts.lms.helpers.excel;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.spts.lms.beans.user.User;
import com.spts.lms.beans.user.UserRole;
import com.spts.lms.services.user.UserService;
import com.spts.lms.utils.PasswordGenerator;

@Component("facultyExcelHelper")
@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
public class FacultyExcelHelper extends BaseExcelHelper<User> {

	private static int indx = 0;
	private static enum ExcelFields {
		USERNAME(indx++, Cell.CELL_TYPE_STRING),
		PASSWORD(indx++, Cell.CELL_TYPE_STRING),
		FIRST_NAME(indx++, Cell.CELL_TYPE_STRING),
		LAST_NAME(indx++, Cell.CELL_TYPE_STRING),
		EMAIL(indx++, Cell.CELL_TYPE_STRING),
		MOBILE(indx++, Cell.CELL_TYPE_STRING),
		TYPE(indx++, Cell.CELL_TYPE_STRING);

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
	UserService userService;

	private String createdBy;

	private Long programId;

	private boolean upsert;


	public FacultyExcelHelper() {
		super();
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public boolean isUpsert() {
		return upsert;
	}

	public void setUpsert(boolean upsert) {
		this.upsert = upsert;
	}

	public Long getProgramId() {
		return programId;
	}

	public void setProgramId(Long programId) {
		this.programId = programId;
	}



	//Set up values that are common across Excel Rows
	@Override
	public void initHelper(User bean) {
		setCreatedBy(bean.getCreatedBy());
		setUpsert(bean.isUpsert());
	}

	@Override
	protected void visitRow(final Row row, final int rowNum) {
		if(row!=null){
			setAllCellTypes(row);
			User bean = createBean(row);
			validateBean(bean, rowNum);
		}
	}

	private void setAllCellTypes(final Row row) {
		for(ExcelFields field : ExcelFields.values()) {
			setCellType(row, field.getIndex(), field.getCellType());
		}
	}

	private User createBean(final Row row) {
		User bean = new User();
		bean.setUsername(getCellValueAsString(row, ExcelFields.USERNAME.getIndex()));
		bean.setPassword(getCellValueAsString(row, ExcelFields.PASSWORD.getIndex()));

		bean.setPassword(PasswordGenerator.generatePassword(bean.getPassword()));

		bean.setFirstname(getCellValueAsString(row, ExcelFields.FIRST_NAME.getIndex()));
		bean.setLastname(getCellValueAsString(row, ExcelFields.LAST_NAME.getIndex()));
		bean.setEmail(getCellValueAsString(row, ExcelFields.EMAIL.getIndex()));
		bean.setMobile(getCellValueAsString(row, ExcelFields.MOBILE.getIndex()));
		bean.setType(getCellValueAsString(row, ExcelFields.TYPE.getIndex()));
		bean.setEnabled(true);
		bean.addFacultyRoles();

		bean.setCreatedBy(createdBy);
		bean.setLastModifiedBy(createdBy);

		return bean;
	}

	private void validateBean(final User bean, final int rowNum) {
		if(null == bean.getUsername() || bean.getUsername().isEmpty()) {
			setErrorRecord(bean, " Row : "+ rowNum +" User Name is mandatory ");
		}

		if(null == bean.getPassword() || bean.getPassword().isEmpty()) {
			setErrorRecord(bean, " Row : "+ rowNum +" Password is mandatory ");
		}

		if(null == bean.getFirstname() || bean.getFirstname().isEmpty()) {
			setErrorRecord(bean, " Row : "+ rowNum +" First Name is mandatory ");
		}

		if(!upsert){
			//If upsert is not allowed then check if user names already exists and throw error.
			User user = userService.findByUserName(bean.getUsername());
			if(user  != null ) {
				setErrorRecord(bean, " Row : "+ rowNum +" Faculty ID "+bean.getUsername()+" Already exists in system");
			}
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
			//Upsert records in User table
			userService.upsertBatch(getSuccessList());

			//Upsert records in User_Role Table
			List<User> users = getSuccessList();
			List<UserRole> userRoles = new ArrayList<UserRole>();
			for (User user : users) {
				userRoles.addAll(user.getUserRoles());
			}

			for (UserRole userRole : userRoles) {
				userRole.setCreatedBy(createdBy);
				userRole.setLastModifiedBy(createdBy);
			}
			userService.upsertRolesBatch(userRoles);
		}
	}

}
