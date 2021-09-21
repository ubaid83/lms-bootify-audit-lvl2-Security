package com.spts.lms.helpers.excel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


import com.spts.lms.beans.studentConfirmation.studentDetailConfirmation;
import com.spts.lms.beans.user.User;
import com.spts.lms.services.notification.Notifier;
import com.spts.lms.services.studentConfirmation.studentDetailConfirmationPeriodService;
import com.spts.lms.services.studentConfirmation.studentDetailConfirmationService;
import com.spts.lms.services.user.UserService;


@Component("StudentMasterExcelHelper")
@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
public class StudentMasterExcelHelper extends
		BaseExcelHelper<studentDetailConfirmation> {

	@Autowired
	UserService userService;

	@Autowired
	Notifier notifier;

	@Autowired
	studentDetailConfirmationPeriodService studentDetailConfirmationPeriodService;

	@Autowired
	studentDetailConfirmationService studentDetailConfirmationService;

	private static int indx = 0;

	private static enum ExcelFields {
		Username(indx++, Cell.CELL_TYPE_STRING), Status(indx++,
				Cell.CELL_TYPE_STRING), Remarks(indx++, Cell.CELL_TYPE_STRING);

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

	@Override
	protected void visitRow(Row row, int rowNum) {
		if (row != null) {
			setAllCellTypes(row);
			studentDetailConfirmation bean = createBean(row);
			// validateBean(bean, rowNum);
		}

	}

	@Override
	public void initHelper(studentDetailConfirmation bean) {
		
		System.err.println("Bean Value Evaludated--------->"+bean);

	}

	private void setAllCellTypes(final Row row) {
		for (ExcelFields field : ExcelFields.values()) {
			setCellType(row, field.getIndex(), field.getCellType());
		}
	}

	private studentDetailConfirmation createBean(final Row row) {
		studentDetailConfirmation bean = new studentDetailConfirmation();

		bean.setUsername(getCellValueAsString(row,
				ExcelFields.Username.getIndex()));
		System.out.println("setting" + ExcelFields.Username.getIndex());
		bean.setStatus(getCellValueAsString(row, ExcelFields.Status.getIndex()));
		bean.setRemarks(getCellValueAsString(row,
				ExcelFields.Remarks.getIndex()));
		System.out.println(bean.getStatus());
		User u = new User();
		System.err.println("Get Status -------------->" + bean.getStatus()
				+ " " + bean.getRemarks() + bean.getUsername()
				+ bean.getCreatedBy()+u.getUsername());

		studentDetailConfirmationService.updateMasterStatus(bean.getUsername(),
				bean.getStatus(), bean.getRemarks());

		// Email Sending while uploading student remarks
		
		// Download Sample Template for Master Validation
		
	

		Map<String, Map<String, String>> result = null;
		List<String> studentList = new ArrayList<String>();
		studentList.add(bean.getUsername());
		String subject = "Status of Student Master Validation";

		String msg = bean.getRemarks();

		System.out.println("Student List For Sending Email ----------------->"
				+ studentList);

		if ("Y".equals(bean.getStatus()) || "N".equals(bean.getStatus())) {
			result = userService.findEmailByUserName(studentList);
			System.out
					.println("Student result For Sending Email ----------------->"
							+ result);
			Map<String, String> email = result.get("emails");
			Map<String, String> mobiles = new HashMap();
			notifier.sendEmail(email, mobiles, subject, msg);
		}

		/*
		 * }else { setErrorRecord(bean, " Row " + bean.getUsername() +
		 * " not found in system."); }
		 */

		getSuccessList().add(bean);

		System.err.println("studentMasterUpList------------->"
				+ getSuccessList());
		return bean;
	}

	@Override
	public void saveRows() {
		if (getErrorList().isEmpty()) {
			System.err.println("Printing Error List Here---------------->"
					+ getErrorList());
			// studentDetailConfirmationPeriodService.insertBatch(getSuccessList());
			System.err.println("getSuccessList()t------------->"
					+ getSuccessList());
			// studentDetailConfirmationService.updateMaste(getSuccessList());
			// studentDetailConfirmationService.updateMasterStatus(bean.get);

		}

	}



	/*
	 * private void validateBean(final studentDetailConfirmation bean, final int
	 * rowNum) { if (null == bean.getUsername() || bean.getUsername().isEmpty())
	 * { setErrorRecord(bean, " Row : " + rowNum + " UserId is mandatory "); }
	 * 
	 * 
	 * studentDetailConfirmation user =
	 * studentDetailConfirmationService.findAllUserName(bean.getUsername());
	 * 
	 * if (user != null) { setErrorRecord(bean, " Row : " + rowNum + " User " +
	 * bean.getUsername() + " not found in system."); } if
	 * (bean.isErrorRecord()) { getErrorList().add(bean); } else {
	 * getSuccessList().add(bean); } }
	 */

}
