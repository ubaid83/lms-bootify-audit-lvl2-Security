package com.spts.lms.web.controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Produces;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.spts.lms.beans.course.Course;
import com.spts.lms.beans.course.CourseDetail;
import com.spts.lms.beans.course.UserCourse;
import com.spts.lms.beans.test.StudentTest;
import com.spts.lms.beans.test.Test;
import com.spts.lms.beans.test.TestQuestion;
import com.spts.lms.daos.test.TestDAO;
import com.spts.lms.services.course.UserCourseService;
import com.spts.lms.services.test.StudentTestService;
import com.spts.lms.services.test.TestQuestionService;
import com.spts.lms.services.test.TestService;
import com.spts.lms.web.utils.Utils;

@Controller
public class BlackBoardTestWsController extends BaseController {

	@Autowired
	TestService testService;

	@Autowired
	StudentTestService studentTestService;

	@Autowired
	UserCourseService userCourseService;

	@Autowired
	TestDAO testDao;

	@Autowired
	TestQuestionService testQuestionService;

	private static final Logger logger = Logger
			.getLogger(BlackBoardTestWsController.class);

	@RequestMapping(value = "/createTestFromJson", method = {
			RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody Test createTestFromJson(
			@RequestParam Map<String, String> testFields) {
	
		/*
		 * logger.info(""); logger.info("");
		 */

		String testName = testFields.get("testName");
		
		testName = StringUtils.trim(testName);
		List<Test> tests = testDao.findAllSQL(
				"Select * from test where testName = ?",
				new Object[] { testName });
		if (tests.size() != 0) {
		
			return tests.get(0);
		}
		Test test = new Test();
		test.setTestName(testName);
		test.setAcadMonth(testFields.get("acadMonth"));
		test.setAcadYear(Integer.parseInt(testFields.get("acadYear")));
		test.setCourseId(Long.valueOf(testFields.get("courseId")));
		test.setStartDate(Utils.converFormats(testFields.get("startDate")));
		test.setEndDate(Utils.converFormats(testFields.get("endDate")));
		
		test.setShowResultsToStudents("Y");
		test.setActive("Y");
		test.setFacultyId(testFields.get("facultyId"));
		test.setAllowAfterEndDate("N");
		test.setSendEmailAlert("N");
		test.setSendSmsAlert("N");
		test.setTestName(testFields.get("testName"));
		test.setMaxScore((Double.parseDouble(testFields.get("maxScore"))));
		test.setDuration(Integer.parseInt(testFields.get("duration")));
		test.setPassScore(Integer.parseInt(testFields.get("passScore")));
		test.setCreatedBy("CA");
		test.setLastModifiedBy("CA");
		try {
			testService.insertWithIdReturn(test);
			
		} catch (Exception ex) {
			logger.error("Exception", ex);
		}
		return test;
	}

	@RequestMapping(value = "/allocateTestToStudentsByParam", method = {
			RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody Test allocateTestToStudentsByParam(
			@RequestParam Map<String, String> allRequestParams) {
		String testId = allRequestParams.get("testId");
		
		String username = allRequestParams.get("username");

		Test testDB = testService.findByID(Long.valueOf(testId));
		try {
			StudentTest studTest = new StudentTest();
			studTest.setUsername(username);
			studTest.setTestId(testDB.getId());
			studTest.setAttempt(0);
			studTest.setGroupId(testDB.getGroupId());
			studTest.setTestCompleted(testDB.getTestCompleted());
			studTest.setCreatedBy(testDB.getFacultyId());
			studTest.setLastModifiedBy(testDB.getFacultyId());
			studTest.setCourseId(String.valueOf(testDB.getCourseId()));
			studentTestService.insertBatch(Arrays.asList(studTest));
			
		} catch (Exception ex) {
			logger.error("Exception while uploading " + testId + "username"
					+ username, ex);
		}
		return testDB;

	}

	@RequestMapping(value = "/uploadQuestionForTest", method = {
			RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody Test uploadQuestionForTest(
			@RequestParam String testId,
			@RequestParam Map<String, String> allRequestParams) {
	

		Test testDB = testService.findByID(Long.valueOf(testId));

		try {

			TestQuestion testQuestion = new TestQuestion();
			testQuestion.setTestId(Long.valueOf(testId));
			testQuestion.setDescription(allRequestParams.get("question"));
			testQuestion.setOption1(allRequestParams.get("option1"));
			testQuestion.setOption2(allRequestParams.get("option2"));
			testQuestion.setOption3(allRequestParams.get("option3"));
			testQuestion.setOption4(allRequestParams.get("option4"));
			testQuestion.setOption5(allRequestParams.get("option5"));
			testQuestion.setOption6(allRequestParams.get("option6"));
			testQuestion.setOption7(allRequestParams.get("option7"));
			testQuestion.setOption8(allRequestParams.get("option8"));
			testQuestion.setCorrectOption(allRequestParams.get("correct"));
			testQuestion.setMarks(Double.parseDouble(allRequestParams.get("marks")));
			testQuestion.setActive("Y");
			testQuestion.setCreatedBy("Admin");
			testQuestion.setLastModifiedBy("Admin");
			testQuestion.setCreatedDate(new Date());
			testQuestion.setLastModifiedDate(new Date());
			testQuestionService.insert(testQuestion);
			
		} catch (Exception ex) {
			logger.error("Exception", ex);
		}
		return testDB;

	}

}
