package com.spts.lms.web.controllers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.spts.lms.beans.StudentFeedbackInputParam;
import com.spts.lms.beans.feedback.FeedbackQuestion;
import com.spts.lms.beans.feedback.StudentFeedback;
import com.spts.lms.beans.feedback.StudentFeedbackResponse;
import com.spts.lms.beans.test.Test;
import com.spts.lms.daos.feedback.StudentFeedbackDAO;
import com.spts.lms.daos.user.UserCourseDAO;
import com.spts.lms.services.course.CourseService;
import com.spts.lms.services.course.UserCourseService;
import com.spts.lms.services.feedback.FeedbackQuestionService;
import com.spts.lms.services.feedback.StudentFeedbackResponseService;
import com.spts.lms.services.feedback.StudentFeedbackService;
import com.spts.lms.web.utils.Utils;

@RestController
public class StudentFeedbackReportWsController {
	private static final Logger logger = Logger
			.getLogger(StudentFeedbackReportWsController.class);
	@Autowired
	StudentFeedbackDAO studentFeedbackDao;

	@Autowired
	UserCourseDAO userCourseDao;

	@Autowired
	StudentFeedbackService studentFeedbackService;

	@Autowired
	CourseService courseService;

	@Autowired
	UserCourseService userCourseService;

	@Autowired
	FeedbackQuestionService feedbackQuestionService;

	@Autowired
	StudentFeedbackResponseService studentFeedbackResponseService;

	@RequestMapping(value = "/getStudentFeedbackReportByFacultyId", method = {
			RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String getStudentFeedbackReportByFacultyId(
			@RequestParam String json) {
		
		String outPutJson = "";
		List<Map<String, Object>> listOfMapOfStudentFeedbackReport = new ArrayList<>();

		ObjectMapper objMapper = new ObjectMapper();
		try {
			StudentFeedbackInputParam studentFeedbackInputParam = objMapper
					.readValue(json, StudentFeedbackInputParam.class);
			List<StudentFeedback> studentFeedbackCompleteListByInputParam = studentFeedbackService
					.getstudentFeedbackCompleteListByInputParam(
							studentFeedbackInputParam.getFromDate(),
							studentFeedbackInputParam.getToDate(),
							studentFeedbackInputParam.getFacultyId());
			List<Map<String, Object>> mapOfCourseAndNumberOfStudents = userCourseService
					.getNumberOfStudentsPerCourse();
			Map<String, Object> mapOfCourseIdAndNumberOfStudents = new HashMap<>();
			mapOfCourseIdAndNumberOfStudents = getMapOfCourseIdAndNumberOfStudents(mapOfCourseAndNumberOfStudents);
			Map<String, List<StudentFeedback>> mapOfCourseAndStudentFeedbackList = new HashMap<>();
			Map<String, Object> mapOfFeedbackReportFieldsToData = new HashMap<>();
			Set<String> courseListOfFaculty = new HashSet<>();
			for (StudentFeedback sf : studentFeedbackCompleteListByInputParam) {
				courseListOfFaculty.add(String.valueOf(sf.getCourseId()));
			}
			mapOfCourseAndStudentFeedbackList = getMapOfCourseAndStudentFeedbackList(
					courseListOfFaculty,
					studentFeedbackCompleteListByInputParam);

			for (String c : courseListOfFaculty) {
				mapOfFeedbackReportFieldsToData = getMapOfFeedbackReportFieldsToData(
						c, mapOfCourseAndStudentFeedbackList,
						mapOfCourseIdAndNumberOfStudents);
				listOfMapOfStudentFeedbackReport
						.add(mapOfFeedbackReportFieldsToData);
			}
		} catch (Exception ex) {

			logger.error("Exception", ex);
		}
		outPutJson = new Gson().toJson(listOfMapOfStudentFeedbackReport);
		return outPutJson;
	}

	private Map<String, Object> getMapOfFeedbackReportFieldsToData(
			String courseId,
			Map<String, List<StudentFeedback>> mapOfCourseAndStudentFeedbackList,
			Map<String, Object> mapOfCourseIdAndNumberOfStudents) {
		Map<String, Object> mapOfFeedbackReportFieldsToData = new HashMap<>();
		Long noOfStudentsInTheCourse = (Long) mapOfCourseIdAndNumberOfStudents
				.get(courseId);
		
		List<StudentFeedback> studentFeedbackByCourse = mapOfCourseAndStudentFeedbackList
				.get(courseId);
		List<String> listOfStudentFeedbackId = new ArrayList<>();
		List<String> listOfFeedbackQuestionId = new ArrayList<>();
		for (StudentFeedback sf : studentFeedbackByCourse) {
			listOfStudentFeedbackId.add(String.valueOf(sf.getId()));

		}
		List<FeedbackQuestion> feedbackQuestionListByCourse = feedbackQuestionService
				.getFeedbackQuestionListByCourseForWs(courseId);
		for (FeedbackQuestion fq : feedbackQuestionListByCourse) {
			listOfFeedbackQuestionId.add(String.valueOf(fq.getId()));

		}
		int grandTotal = 0;
		Double grandAverage = 0.0;
		Double average = 0.0;
		Double totalAverage = 0.0;
		List<StudentFeedbackResponse> studentFeedbackResponseListByStudentFeedbackId = new ArrayList<>();
		if (listOfStudentFeedbackId.size() > 0) {
			
			studentFeedbackResponseListByStudentFeedbackId = studentFeedbackResponseService
					.getstudentFeedbackResponseListByStudentFeedbackId(listOfStudentFeedbackId);
		}
		grandTotal = studentFeedbackResponseService
				.getGrandTotal(listOfStudentFeedbackId);
		grandAverage = (Double.valueOf(grandTotal) / studentFeedbackResponseListByStudentFeedbackId
				.size());
		List<StudentFeedbackResponse> averageByQuestionId = studentFeedbackResponseService
				.getAverageByQuestionId(listOfFeedbackQuestionId);
		for (StudentFeedbackResponse sfr : averageByQuestionId) {
			totalAverage = (totalAverage + Double.valueOf(sfr.getAverage()));
		}
		average = totalAverage / averageByQuestionId.size();

		mapOfFeedbackReportFieldsToData.put("[OPERATOR]", "");
		mapOfFeedbackReportFieldsToData.put("externalCode",
				studentFeedbackByCourse.get(0).getFacultyId());
		mapOfFeedbackReportFieldsToData.put("effectiveStartDate",
				new SimpleDateFormat("MM/dd/yyyy").format(new Date()));
		mapOfFeedbackReportFieldsToData.put(
				"cust_studentFeedbackDetails.externalCode", "");
		mapOfFeedbackReportFieldsToData.put(
				"cust_studentFeedbackDetails.externalName",
				studentFeedbackByCourse.get(0).getProgramName());
		mapOfFeedbackReportFieldsToData.put(
				"cust_studentFeedbackDetails.cust_academicYear",
				String.valueOf(studentFeedbackByCourse.get(0).getAcadYear()));
		mapOfFeedbackReportFieldsToData.put(
				"cust_studentFeedbackDetails.cust_Trimester",
				studentFeedbackByCourse.get(0).getAcadSession());
		mapOfFeedbackReportFieldsToData.put(
				"cust_studentFeedbackDetails.cust_FacultyName",
				studentFeedbackByCourse.get(0).getFacultyName());
		mapOfFeedbackReportFieldsToData.put(
				"cust_studentFeedbackDetails.cust_FacultyType.externalCode",
				studentFeedbackByCourse.get(0).getType());
		mapOfFeedbackReportFieldsToData.put(
				"cust_studentFeedbackDetails.cust_Area", "NA");
		mapOfFeedbackReportFieldsToData.put(
				"cust_studentFeedbackDetails.cust_division", "NA");
		
		mapOfFeedbackReportFieldsToData.put(
				"cust_studentFeedbackDetails.cust_CourseName",
				studentFeedbackByCourse.get(0).getCourseNameforFeedback());
		mapOfFeedbackReportFieldsToData.put(
				"cust_studentFeedbackDetails.cust_TotalNofStudents",
				noOfStudentsInTheCourse);
		mapOfFeedbackReportFieldsToData.put(
				"cust_studentFeedbackDetails.cust_NoofStudentsGaveFeedback",
				studentFeedbackByCourse.size());
		mapOfFeedbackReportFieldsToData.put(
				"cust_studentFeedbackDetails.cust_Average", average);
		mapOfFeedbackReportFieldsToData.put(
				"cust_studentFeedbackDetails.cust_GrandAverage", grandAverage);

		return mapOfFeedbackReportFieldsToData;

	}

	private Map<String, List<StudentFeedback>> getMapOfCourseAndStudentFeedbackList(
			Set<String> courseList,
			List<StudentFeedback> studentFeedbackCompleteListByInputParam) {
		Map<String, List<StudentFeedback>> mapOfCourseAndStudentFeedbackList = new HashMap<>();
		for (String courseIds : courseList) {
			List<StudentFeedback> studentFeedbackCourseWise = new ArrayList<>();
			for (StudentFeedback sf : studentFeedbackCompleteListByInputParam) {
				if (courseIds.equals(String.valueOf(sf.getCourseId()))) {
					studentFeedbackCourseWise.add(sf);
				}
			}
			mapOfCourseAndStudentFeedbackList.put(courseIds,
					studentFeedbackCourseWise);
		}
		return mapOfCourseAndStudentFeedbackList;

	}

	private Map<String, Object> getMapOfCourseIdAndNumberOfStudents(
			List<Map<String, Object>> mapOfCourseAndNumberOfStudents) {
		Map<String, Object> mapOfCourseIdAndNumberOfStudents = new HashMap<>();
		for (Map<String, Object> map : mapOfCourseAndNumberOfStudents) {
			mapOfCourseIdAndNumberOfStudents.put(String.valueOf( map.get("cId")),
					map.get("numOfStudentsInTheCourse"));

		}
		return mapOfCourseIdAndNumberOfStudents;
	}

	@RequestMapping(value = "/getStudentFeedbackReportBySchoolInBulk", method = {
			RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String getStudentFeedbackReportBySchoolInBulk(
			@RequestParam String json) {
		
		String outPutJson = "";
		List<Map<String, Object>> listOfMapOfStudentFeedbackReport = new ArrayList<>();
		Map<String, Object> mapOfFeedbackReportFieldsToData = new HashMap<>();
		ObjectMapper objMapper = new ObjectMapper();

		try {
			StudentFeedbackInputParam studentFeedbackInputParam = objMapper
					.readValue(json, StudentFeedbackInputParam.class);
			List<StudentFeedback> studentFeedbackCompleteListByInputParam = studentFeedbackService
					.getstudentFeedbackCompleteListByInputParam(
							studentFeedbackInputParam.getFromDate(),
							studentFeedbackInputParam.getToDate());
			Map<String, List<StudentFeedback>> mapOfCourseAndStudentFeedbackList = new HashMap<>();
			Set<String> courseListOfFaculty = new HashSet<>();
			for (StudentFeedback sf : studentFeedbackCompleteListByInputParam) {
				courseListOfFaculty.add(String.valueOf(sf.getCourseId()));
			}
			mapOfCourseAndStudentFeedbackList = getMapOfCourseAndStudentFeedbackList(
					courseListOfFaculty,
					studentFeedbackCompleteListByInputParam);
			List<Map<String, Object>> mapOfCourseAndNumberOfStudents = userCourseDao
					.getNumberOfStudentsPerCourse();
			Map<String, Object> mapOfCourseIdAndNumberOfStudents = new HashMap<>();
			mapOfCourseIdAndNumberOfStudents = getMapOfCourseIdAndNumberOfStudents(mapOfCourseAndNumberOfStudents);

			for (String c : courseListOfFaculty) {

				List<StudentFeedback> studentFeedbackByCourse = mapOfCourseAndStudentFeedbackList
						.get(c);
				Map<String, List<StudentFeedback>> mapOfCourseAndStudentFeedbackListByFaculty = new HashMap<>();

				Set<String> facultyList = new HashSet<>();
				for (StudentFeedback sf : studentFeedbackByCourse) {

					facultyList.add(sf.getFacultyId());
				}

				if (facultyList.size() == 1) {
					
					mapOfFeedbackReportFieldsToData = getMapOfFeedbackReportFieldsToData(
							c, mapOfCourseAndStudentFeedbackList,
							mapOfCourseIdAndNumberOfStudents);
					listOfMapOfStudentFeedbackReport
							.add(mapOfFeedbackReportFieldsToData);

				} else {

					
					for (String facultyId : facultyList) {
						Map<String, Object> mapOfFeedbackReportFieldsToDataByFaculty = new HashMap<>();
						List<StudentFeedback> studentFeedbackList = new ArrayList<>();
						for (StudentFeedback sf : studentFeedbackByCourse) {
							if (sf.getFacultyId().equals(facultyId)) {

								studentFeedbackList.add(sf);
							}

						}
						mapOfCourseAndStudentFeedbackListByFaculty.put(c,
								studentFeedbackList);
						mapOfFeedbackReportFieldsToDataByFaculty = getMapOfFeedbackReportFieldsToData(
								c, mapOfCourseAndStudentFeedbackListByFaculty,
								mapOfCourseIdAndNumberOfStudents);
						listOfMapOfStudentFeedbackReport
								.add(mapOfFeedbackReportFieldsToDataByFaculty);
					}

				}

			}
		} catch (Exception ex) {

			logger.error("Exception ", ex);
		}
		outPutJson = new Gson().toJson(listOfMapOfStudentFeedbackReport);
		return outPutJson;

	}

}
