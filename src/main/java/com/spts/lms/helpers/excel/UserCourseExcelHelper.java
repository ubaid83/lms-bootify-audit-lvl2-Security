package com.spts.lms.helpers.excel;

import java.util.List;

import org.apache.commons.lang3.EnumUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.spts.lms.beans.course.Course;
import com.spts.lms.beans.course.UserCourse;
import com.spts.lms.beans.user.Role;
import com.spts.lms.beans.user.User;
import com.spts.lms.services.course.CourseService;
import com.spts.lms.services.course.UserCourseService;
import com.spts.lms.services.user.UserService;

@Component("userCourseExcelHelper")
@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
public class UserCourseExcelHelper extends BaseExcelHelper<UserCourse> {

	private static int indx = 0;

	private static enum ExcelFields {
		USERNAME(indx++, Cell.CELL_TYPE_STRING), ROLE(indx++,
				Cell.CELL_TYPE_STRING),
		AcadYearCode(indx++,Cell.CELL_TYPE_STRING);

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
	UserCourseService userCourseService;

	@Autowired
	CourseService courseService;

	@Autowired
	UserService userService;

	private String createdBy;

	private String acadMonth;
	
	private String acadYearCode;

	@Override
	public String toString() {
		return "UserCourseExcelHelper [acadYearCode=" + acadYearCode + "]";
	}

	public String getAcadYearCode() {
		return acadYearCode;
	}

	public void setAcadYearCode(String acadYearCode) {
		this.acadYearCode = acadYearCode;
	}

	private Integer acadYear;

	private List<Long> courseIds;

	public UserCourseExcelHelper() {
		super();
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

	public List<Long> getCourseIds() {
		return courseIds;
	}

	public void setCourseIds(List<Long> courseIds) {
		this.courseIds = courseIds;
	}

	@Override
	public void initHelper(UserCourse bean) {
		setAcadMonth(bean.getAcadMonth());
		setAcadYear(bean.getAcadYear());
		setCreatedBy(bean.getCreatedBy());
		setCourseIds(bean.getCourseIds());
	}

	@Override
	protected void visitRow(final Row row, final int rowNum) {
		if (row != null) {
			setAllCellTypes(row);
			for (Long courseId : getCourseIds()) {// For reach course create one
													// bean.
				UserCourse bean = createBean(row, rowNum);
				Course courseDB = courseService.findByID(courseId);
				bean.setCourseId(courseId);
				if (courseDB.getAcadSession() != null) {
					bean.setAcadSession(courseDB.getAcadSession());
				}
				validateBean(bean, rowNum);
			}

		}

	}

	private void setAllCellTypes(final Row row) {
		for (ExcelFields field : ExcelFields.values()) {
			setCellType(row, field.getIndex(), field.getCellType());
		}
	}

	private UserCourse createBean(final Row row, final int rowNum) {
		UserCourse bean = new UserCourse();
		bean.setUsername(getCellValueAsString(row,
				ExcelFields.USERNAME.getIndex()));
		String role = getCellValueAsString(row, ExcelFields.ROLE.getIndex());
		bean.setAcadYearCode(getCellValueAsString(row,ExcelFields.AcadYearCode.getIndex()));
		if (!EnumUtils.isValidEnum(Role.class, role)) {// Check if role entered
														// in excel is
														// correct/valid
			setErrorRecord(bean, " Row : " + rowNum + " Role: " + role
					+ " is NOT a valid Role.");
		} else {
			bean.setRole(role);
		}

		bean.setAcadMonth(acadMonth);
		bean.setAcadYear(acadYear);
		bean.setCreatedBy(createdBy);
		bean.setLastModifiedBy(createdBy);

		return bean;
	}

	private void validateBean(final UserCourse bean, final int rowNum) {
		if (null == bean.getUsername() || bean.getUsername().isEmpty()) {
			setErrorRecord(bean, " Row : " + rowNum + " UserId is mandatory ");
		}

		User user = userService.findByUserName(bean.getUsername());

		if (null == user) {
			setErrorRecord(bean,
					" Row : " + rowNum + " User " + bean.getUsername()
							+ " not found in system.");
		}
		if (bean.isErrorRecord()) {
			getErrorList().add(bean);
		} else {
			getSuccessList().add(bean);
		}
	}

	@Override
	public void saveRows() {
		if (getErrorList().isEmpty())
			userCourseService.insertBatch(getSuccessList());
	}

}
