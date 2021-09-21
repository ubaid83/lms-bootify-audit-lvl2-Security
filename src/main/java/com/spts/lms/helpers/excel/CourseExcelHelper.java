package com.spts.lms.helpers.excel;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.spts.lms.beans.course.Course;
import com.spts.lms.services.course.CourseService;

@Component("courseExcelHelper")
@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
public class CourseExcelHelper extends BaseExcelHelper<Course>{

	private static final Logger logger = Logger
			.getLogger(CourseExcelHelper.class);

	private static int indx = 0;

	private static enum ExcelFields {
		/* COURSE_ABBRVIATION(indx++, Cell.CELL_TYPE_STRING), */
		COURSE_NAME(indx++, Cell.CELL_TYPE_STRING), COURSE_CUSTOM_PROPERTY1(
				indx++, Cell.CELL_TYPE_STRING), COURSE_CUSTOM_PROPERTY2(indx++,
				Cell.CELL_TYPE_STRING), ACAD_MONTH(indx++,
				Cell.CELL_TYPE_STRING), COURSE_CUSTOM_PROPERTY3(indx++,
				Cell.CELL_TYPE_STRING), ACAD_SESSION(indx++,
				Cell.CELL_TYPE_STRING), MODULE_ID(indx++,
				Cell.CELL_TYPE_STRING), MODULE_NAME(indx++,
				Cell.CELL_TYPE_STRING), CAMPUS_ID(indx++,
				Cell.CELL_TYPE_STRING), MODULE_CODE_ABBR(indx++,
				Cell.CELL_TYPE_STRING), DEPARTMENT_CODE(indx++,
				Cell.CELL_TYPE_STRING), DEPARTMENT(indx++,
				Cell.CELL_TYPE_STRING), MODULE_CATOGORY_CODE(indx++,
				Cell.CELL_TYPE_STRING), MODULE_CATOGORY_NAME(indx++,
				Cell.CELL_TYPE_STRING),	ACAD_YEAR_CODE(indx++,
				Cell.CELL_TYPE_STRING);
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
	CourseService courseService;

	private String createdBy;

	private String programId;

	public CourseExcelHelper() {
		super();
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setProgramId(String programId) {
		this.programId = programId;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	@Override
	public void initHelper(Course bean) {
		setCreatedBy(bean.getCreatedBy());
		setProgramId(String.valueOf(bean.getProgramId()));
	}

	@Override
	protected void visitRow(final Row row, final int rowNum) {
		if (row != null) {
			setAllCellTypes(row);
			Course bean = createBean(row);
			validateBean(bean, rowNum);
		}

	}

	private void setAllCellTypes(final Row row) {
		for (ExcelFields field : ExcelFields.values()) {
			setCellType(row, field.getIndex(), field.getCellType());
		}
	}

	List<String> courseIdList = new ArrayList<>();

	public String autoGenerateCourseId() {
		long timeSeed = System.nanoTime(); // to get the current date
		// time value

		double randSeed = Math.random() * 1000;

		// number
		// generation

		long midSeed = (long) (timeSeed * randSeed); // mixing up the
		// time and
		// rand number.

		// variable timeSeed
		// will be unique

		// variable rand will
		// ensure no relation
		// between the numbers

		String s = midSeed + "";
		String subStr = s.substring(0, 10);
		String autoGenCourseId = subStr + programId;

		return autoGenCourseId;
	}

	private Course createBean(final Row row) {
		Course bean = new Course();

		boolean isCourseUnique = false;
		/*
		 * bean.setAbbr(getCellValueAsString(row,
		 * ExcelFields.COURSE_ABBRVIATION.getIndex()));
		 */
		// --Akshay Changes---//
		String autoGenCourseId = "";

		while (isCourseUnique == false) {

			autoGenCourseId = autoGenerateCourseId();
			Course c = courseService.findByID(Long.valueOf(autoGenCourseId));

			if (c != null || courseIdList.contains(autoGenCourseId)) {

				isCourseUnique = false;
			} else {
				isCourseUnique = true;
				courseIdList.add(autoGenCourseId);
			}
		}

		bean.setId(Long.valueOf(autoGenCourseId));

		// --Akshay Changes---//
		/*
		 * bean.setId(Long.valueOf(getCellValueAsString(row,
		 * ExcelFields.COURSE_ID.getIndex())));
		 */
		bean.setCourseName(getCellValueAsString(row,
				ExcelFields.COURSE_NAME.getIndex()));
		bean.setProperty1(getCellValueAsString(row,
				ExcelFields.COURSE_CUSTOM_PROPERTY1.getIndex()));
		bean.setProperty2(getCellValueAsString(row,
				ExcelFields.COURSE_CUSTOM_PROPERTY2.getIndex()));
		bean.setProperty3(getCellValueAsString(row,
				ExcelFields.COURSE_CUSTOM_PROPERTY3.getIndex()));
		bean.setAcadSession(getCellValueAsString(row,
				ExcelFields.ACAD_SESSION.getIndex()));
		bean.setAbbr(bean.getProperty2());
		bean.setAcadYear(getCellValueAsString(row,
				ExcelFields.COURSE_CUSTOM_PROPERTY3.getIndex()));
		bean.setDept(bean.getProperty1());
		bean.setAcadMonth(getCellValueAsString(row,
				ExcelFields.ACAD_MONTH.getIndex()));
		
		bean.setModuleId(getCellValueAsString(row,
				ExcelFields.MODULE_ID.getIndex()));
		String campusId = getCellValueAsString(row,
				ExcelFields.CAMPUS_ID.getIndex());
				
		if(!campusId.trim().isEmpty()){
		bean.setCampusId(Long.valueOf(getCellValueAsString(row,
				ExcelFields.CAMPUS_ID.getIndex())));
		}else{
			bean.setCampusId(null);
		}
		
		bean.setModuleAbbr(getCellValueAsString(row,
				ExcelFields.MODULE_CODE_ABBR.getIndex()));
		bean.setDeptCode(getCellValueAsString(row,
				ExcelFields.DEPARTMENT_CODE.getIndex()));
		bean.setDept(getCellValueAsString(row,
				ExcelFields.DEPARTMENT.getIndex()));
		bean.setModuleCategoryCode(getCellValueAsString(row,
				ExcelFields.MODULE_CATOGORY_CODE.getIndex()));
		bean.setModuleCategoryName(getCellValueAsString(row,
				ExcelFields.MODULE_CATOGORY_NAME.getIndex()));
		bean.setModuleName(getCellValueAsString(row,
				ExcelFields.MODULE_NAME.getIndex()));
		bean.setAcadYearCode(getCellValueAsString(row,
				ExcelFields.ACAD_YEAR_CODE.getIndex()));
		bean.setActive("Y");
		bean.setCreatedBy(createdBy);
		bean.setLastModifiedBy(createdBy);
		bean.setProgramId(Long.valueOf(programId));

		return bean;
	}

	
	private void validateBean(final Course bean, final int rowNum) {
		/*
		 * if(null == bean.getAbbr() || bean.getAbbr().isEmpty()) {
		 * setErrorRecord(bean, " Row : "+ rowNum
		 * +" Course Abbreviation is mandatory "); }
		 */

		if (null == bean.getCourseName() || bean.getCourseName().isEmpty()) {
			setErrorRecord(bean, " Row : " + rowNum
					+ " Course Name is mandatory ");
		}
		System.out.println("Hello World---------->");
		
		System.out.println("bean.getModuleId()---------->"+bean.getModuleId());
		String moduleId =bean.getModuleId().trim();
		
		if(!moduleId.isEmpty())
		{
			String moduleName = bean.getModuleName().trim();
			String moduleAbbr = bean.getModuleAbbr().trim();
			System.out.println("bean.getModuleId()---------->"+bean.getModuleId());
			if(moduleName.isEmpty() || moduleAbbr.isEmpty())
			{
				setErrorRecord(bean," Row : " + rowNum+ "Module Name And  Module Abbr Must Not be Empty");	
			}
		}


		if (bean.isErrorRecord()) {
			getErrorList().add(bean);
		} else {
			getSuccessList().add(bean);
		}
	}

	@Override
	public void saveRows() {
		if (getErrorList().isEmpty()) {
			courseService.insertBatch(getSuccessList());
			courseIdList.clear();
		}
	}

}