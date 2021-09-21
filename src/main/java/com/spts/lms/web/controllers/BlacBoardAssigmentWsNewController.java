package com.spts.lms.web.controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.spts.lms.auth.Token;
import com.spts.lms.beans.assignment.Assignment;
import com.spts.lms.beans.assignment.StudentAssignment;
import com.spts.lms.beans.content.Content;
import com.spts.lms.beans.content.StudentContent;
import com.spts.lms.beans.course.Course;
import com.spts.lms.beans.course.CourseDetail;
import com.spts.lms.beans.course.UserCourse;
import com.spts.lms.beans.user.User;
import com.spts.lms.daos.assignment.AssignmentDAO;
import com.spts.lms.daos.content.ContentDAO;
import com.spts.lms.services.assignment.AssignmentService;
import com.spts.lms.services.assignment.StudentAssignmentService;
import com.spts.lms.services.content.ContentService;
import com.spts.lms.services.content.StudentContentService;
import com.spts.lms.services.course.CourseService;
import com.spts.lms.services.course.UserCourseService;
import com.spts.lms.services.group.GroupService;
import com.spts.lms.services.group.StudentGroupService;
import com.spts.lms.services.user.UserService;
import com.spts.lms.web.utils.Utils;

@Controller
public class BlacBoardAssigmentWsNewController extends BaseController {

	@Autowired
	AssignmentService assignmentService;

	@Autowired
	StudentAssignmentService studentAssignmentService;

	@Autowired
	UserCourseService userCourseService;

	@Autowired
	StudentGroupService studentGroupService;

	@Autowired
	GroupService groupService;

	@Autowired
	UserService userService;

	@Autowired
	CourseService courseService;

	@Autowired
	AssignmentDAO assignmentDao;

	@Autowired
	ContentDAO contentDao;

	@Autowired
	ContentService contentService;

	@Autowired
	StudentContentService studentContentService;

	@Value("${lms.assignment.submissionFolder}")
	private String submissionFolder;

	@Value("${max_page_size:50}")
	private int max_page_size;

	@Value("${workStoreDir:''}")
	private String workDir;

	@Value("${lms.assignment.questionFolder}")
	private String assignmentFolder;

	@Value("${lms.content.courseRootFolder}")
	private String contentFolder;

	private static final Logger logger = Logger
			.getLogger(BlacBoardAssigmentWsController.class);

	@RequestMapping(value = "/createAssignmentNewFromJson", method = {
			RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody Assignment createAssignmentFromJson(
			@RequestParam Map<String, String> assignmentFields,
			@RequestParam("file") MultipartFile file,
			@RequestParam(name = "users", required = false) List<String> userList) {

		String assigmentName = assignmentFields.get("assignmentName");
		assigmentName = StringUtils.trim(assigmentName);

		
		List<Assignment> assigments = assignmentDao.findAllSQL(
				"Select * from assignment where assignmentName = ?",
				new Object[] { assigmentName });
		if (assigments.size() != 0) {
			return assigments.get(0);
		}
		Assignment assignment = new Assignment();
		assignment.setAssignmentName(assigmentName);
		assignment.setAcadMonth(assignmentFields.get("acadMonth"));
		assignment.setAcadYear(Integer.parseInt(assignmentFields
				.get("acadYear")));
		assignment.setCourseId(Long.valueOf(assignmentFields.get("courseId")));

		assignment.setStartDate(Utils.converFormats(assignmentFields
				.get("startDate")));
		assignment.setEndDate(Utils.converFormats(assignmentFields
				.get("endDate")));
		
		String path = uploadAssignmentFile(assignment, file);
		assignment.setFilePath(path);
		assignment.setFilePreviewPath(path);
		assignment.setShowResultsToStudents("Y");
		assignment.setActive("Y");
		assignment.setFacultyId(assignmentFields.get("facultyId"));
		assignment.setAllowAfterEndDate("N");
		assignment.setSendEmailAlert("N");
		assignment.setSendSmsAlert("N");
		assignment.setAssignmentText(assignmentFields.get("assignmentName"));
		assignment.setMaxScore(assignmentFields.get("maxScore"));

		assignment.setCreatedBy("CA");
		assignment.setLastModifiedBy("CA");
		try {
			assignmentService.insertWithIdReturn(assignment);
			
			List<StudentAssignment> insertLst = new ArrayList<StudentAssignment>();
			List<StudentAssignment> updateLst = new ArrayList<StudentAssignment>();

			if (userList != null && !userList.isEmpty())
				for (String username : userList) {
					try {

						StudentAssignment studAssignment = null;

						StudentAssignment fetchedStud = studentAssignmentService
								.findAssignmentSubmission(username,
										Long.valueOf(assignment.getId()));

						if (userService.findByUserName(username) == null) {
							continue;

						}
						if (fetchedStud == null) {

							studAssignment = new StudentAssignment();

						} else {
							studAssignment = fetchedStud;
						}

						studAssignment.setUsername(username);
						studAssignment.setAcadMonth(assignment.getAcadMonth());
						studAssignment.setAcadYear(assignment.getAcadYear());
						studAssignment.setCourseId(assignment.getCourseId());
						studAssignment.setActive("Y");
						studAssignment.setAttempts(0);
						studAssignment.setStartDate(assignment.getStartDate());
						/* studAssignment.setDueDate(assignmentDB.getEndDate()); */
						studAssignment.setEndDate(assignment.getEndDate());
						studAssignment.setAssignmentId(Long.valueOf(assignment
								.getId()));
						studAssignment.setCreatedBy(assignment.getFacultyId());
						studAssignment.setSubmissionStatus("Y");
						studAssignment.setEvaluationStatus("Y");

						if (fetchedStud == null) {
							insertLst.add(studAssignment);
						} else {
							updateLst.add(studAssignment);
						}

					} catch (Exception e) {
						logger.error("Exception inside users" + username, e);
					}

				}
			
			if (insertLst != null && !insertLst.isEmpty()) {
				studentAssignmentService.insertAssigments(insertLst);
				
			}

			if (updateLst != null && !updateLst.isEmpty()) {
				studentAssignmentService.updateBatch(updateLst);
				
			}

		} catch (Exception ex) {
			logger.error("Exception", ex);
		}
		return assignment;

	}

	@RequestMapping(value = "/allocateAssignmentNewToStudentsByParam", method = {
			RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody Assignment allocateAssignmentToStudentsByParam(
			@RequestParam Map<String, String> allRequestParams,
			@RequestParam("file") MultipartFile file) {

		String assignmentId = allRequestParams.get("assigmentId");
		String username = allRequestParams.get("username");
		String score = allRequestParams.get("score");
		String submissionDate = allRequestParams.get("submissionDate");
		Assignment assignmentDB = assignmentService.findByID(Long
				.valueOf(assignmentId));

		
		boolean isInsert = true;

		try {

			StudentAssignment studAssignment = null;
			List<StudentAssignment> studLst = null;

			try {
				studLst = studentAssignmentService.findAssignmentSubmissionLst(
						username, Long.valueOf(assignmentId));

			} catch (Exception e) {

				logger.error("Exception", e);
			}
			if (studLst == null || studLst.isEmpty()) {
				studAssignment = new StudentAssignment();
				
			} else {
				isInsert = false;
				studAssignment = studLst.get(0);
				
			}

			studAssignment.setUsername(username);
			studAssignment.setAcadMonth(assignmentDB.getAcadMonth());
			studAssignment.setAcadYear(assignmentDB.getAcadYear());
			studAssignment.setCourseId(assignmentDB.getCourseId());
			studAssignment.setActive("Y");
			studAssignment.setAttempts(0);
			studAssignment.setStartDate(assignmentDB.getStartDate());
			/* studAssignment.setDueDate(assignmentDB.getEndDate()); */
			studAssignment.setEndDate(assignmentDB.getEndDate());
			studAssignment.setAssignmentId(Long.valueOf(assignmentId));
			studAssignment.setCreatedBy(assignmentDB.getFacultyId());
			studAssignment.setSubmissionStatus("Y");
			studAssignment.setEvaluationStatus("Y");
			if (!StringUtils.isEmpty(score))
				studAssignment.setScore(score);
			else
				studAssignment.setScore(null);
			studAssignment.setEvaluatedBy(assignmentDB.getFacultyId());
			studAssignment.setSubmissionDate(Utils
					.converFormatsDate(submissionDate));
			studAssignment.setLastModifiedBy(assignmentDB.getFacultyId());

			String path = uploadAssignmentSubmissionFile(studAssignment, file);
			
			if (path != null) {
				// studAssignment.setStudentFilePath(path);
				// studAssignment.setFilePreviewPath(path);
				studAssignment.setSubmissionStatus("Y");
				studAssignment.setEvaluationStatus("Y");
				// studentAssignmentService.update(studAssignment);

			}
			if (isInsert)
				studentAssignmentService.insertBatch(Arrays
						.asList(studAssignment));
			else
				studentAssignmentService.updateBatch(Arrays
						.asList(studAssignment));

			
		} catch (Exception ex) {
			logger.error("Exception while uploading " + assignmentId
					+ "username" + username, ex);
		}
		return assignmentDB;

	}

	@RequestMapping(value = "/getNewCourses", method = { RequestMethod.GET,
			RequestMethod.POST })
	public @ResponseBody List<CourseDetail> getCoursesForStudent(
			@RequestParam(name = "username", required = false) String username,
			Principal principal) {
		List<CourseDetail> courseList = new ArrayList();
		Token userDetails = (Token) principal;
		try {
			List<Course> course = courseService.findByUser(username,
					Long.parseLong(userDetails.getProgramId()));
			for (Course c : course) {
				CourseDetail detail = new CourseDetail();

				BeanUtils.copyProperties(c, detail);

				List<UserCourse> uc = userCourseService.findFacultyByCourseID(c
						.getId());
				if (uc != null && !uc.isEmpty()) {
					detail.setFacultyId(uc.get(0).getUsername());
				}

				courseList.add(detail);
			}

		} catch (Exception ex) {
			logger.error("Exception while fetching course for username"
					+ "username" + username, ex);
		}
		return courseList;

	}

	/*
	 * @RequestMapping(value = "/getNewCoursesForBB", method = {
	 * RequestMethod.GET, RequestMethod.POST }) public @ResponseBody
	 * List<CourseDetail> getCoursesForBB(
	 * 
	 * @RequestParam(name = "username", required = false) String username) {
	 * List<CourseDetail> courseList = new ArrayList();
	 * 
	 * try { User u = userService.findByUserName(username);
	 * 
	 * List<Course> course = u == null ? new ArrayList<Course>() :
	 * courseService.findByUser(username); for (Course c : course) {
	 * CourseDetail detail = new CourseDetail();
	 * 
	 * BeanUtils.copyProperties(c, detail);
	 * 
	 * List<UserCourse> uc = userCourseService.findFacultyByCourseID(c
	 * .getId()); if (uc != null && !uc.isEmpty()) {
	 * detail.setFacultyId(uc.get(0).getUsername()); }
	 * 
	 * courseList.add(detail); }
	 * 
	 * } catch (Exception ex) {
	 * logger.error("Exception while fetching course for username" + "username"
	 * + username, ex); } return courseList;
	 * 
	 * }
	 */

	private String uploadAssignmentSubmissionFile(StudentAssignment bean,
			MultipartFile file) {

		String errorMessage = null;
		InputStream inputStream = null;
		OutputStream outputStream = null;

		// CommonsMultipartFile file = bean.getFileData();
		String fileName = file.getOriginalFilename();
		

		// Replace special characters in file
		fileName = fileName.replaceAll("'", "_");
		fileName = fileName.replaceAll(",", "_");
		fileName = fileName.replaceAll("&", "and");
		fileName = fileName.replaceAll(" ", "_");
		if (fileName.lastIndexOf(".") != -1)
			fileName = fileName.substring(0, fileName.lastIndexOf("."))
					+ "_"
					+ RandomStringUtils.randomAlphanumeric(10)
					+ fileName.substring(fileName.lastIndexOf("."),
							fileName.length());

		try {
			inputStream = file.getInputStream();
			String filePath = submissionFolder + File.separator + fileName;

			File folderPath = new File(submissionFolder);
			if (!folderPath.exists()) {
				folderPath.mkdirs();
			}

			File newFile = new File(filePath);

			outputStream = new FileOutputStream(newFile);
			IOUtils.copy(inputStream, outputStream);
			errorMessage = newFile.getAbsolutePath();

			bean.setFilePath(newFile.getAbsolutePath());
			bean.setFilePreviewPath(newFile.getAbsolutePath());

			bean.setStudentFilePath(newFile.getAbsolutePath());
			bean.setFilePreviewPath(newFile.getAbsolutePath());

		} catch (Exception e) {
			errorMessage = null;
			logger.error("Exception", e);
		} finally {
			IOUtils.closeQuietly(inputStream);
			IOUtils.closeQuietly(outputStream);
		}
		/* logger.info("Path----" + errorMessage); */

		return errorMessage;
	}

	private String uploadAssignmentFile(Assignment bean, MultipartFile file) {

		String errorMessage = null;
		InputStream inputStream = null;
		OutputStream outputStream = null;

		// CommonsMultipartFile file = bean.getFileData();
		String fileName = file.getOriginalFilename();

		// Replace special characters in file
		fileName = fileName.replaceAll("'", "_");
		fileName = fileName.replaceAll(",", "_");
		fileName = fileName.replaceAll("&", "and");
		fileName = fileName.replaceAll(" ", "_");

		fileName = fileName.substring(0, fileName.lastIndexOf("."))
				+ "_"
				+ RandomStringUtils.randomAlphanumeric(10)
				+ fileName.substring(fileName.lastIndexOf("."),
						fileName.length());

		

		try {
			inputStream = file.getInputStream();
			String filePath = assignmentFolder + File.separator + fileName;

			
			File folderPath = new File(assignmentFolder);
			if (!folderPath.exists()) {
				folderPath.mkdirs();
			}
			File newFile = new File(filePath);
			outputStream = new FileOutputStream(newFile);
			IOUtils.copy(inputStream, outputStream);
			bean.setFilePath(newFile.getAbsolutePath());
			bean.setFilePreviewPath(newFile.getAbsolutePath());
			errorMessage = newFile.getAbsolutePath();
		} catch (IOException e) {
			errorMessage = "Error in uploading Assignment file : "
					+ e.getMessage();
			logger.error("Exception" + errorMessage, e);
		} finally {

			if (inputStream != null)
				IOUtils.closeQuietly(inputStream);

			if (outputStream != null)
				IOUtils.closeQuietly(outputStream);

		}

		return errorMessage;
	}

	private String uploadContentFile(Content bean, MultipartFile file) {
		
		String courseFolder = null;
		String errorMessage = null;

		InputStream inputStream = null;

		OutputStream outputStream = null;

		// CommonsMultipartFile file = bean.getFileData();

		String fileName = file.getOriginalFilename();

		// Replace special characters in file

		fileName = fileName.replaceAll("'", "_");

		fileName = fileName.replaceAll(",", "_");

		fileName = fileName.replaceAll("&", "and");

		fileName = fileName.replaceAll(" ", "_");

		fileName = fileName.substring(0, fileName.lastIndexOf("."))

		+ "_"

		+ RandomStringUtils.randomAlphanumeric(10)

		+ fileName.substring(fileName.lastIndexOf("."),

		fileName.length());

		

		try {

			inputStream = file.getInputStream();
			courseFolder = contentFolder + bean.getCourseId();
			String filePath = courseFolder + File.separator + fileName;


			File folderPath = new File(courseFolder);

			if (!folderPath.exists()) {

				folderPath.mkdirs();

			}

			File newFile = new File(filePath);

			outputStream = new FileOutputStream(newFile);

			IOUtils.copy(inputStream, outputStream);

			bean.setFilePath(newFile.getAbsolutePath());

			bean.setFolderPath(newFile.getAbsolutePath());

			errorMessage = newFile.getAbsolutePath();

		} catch (IOException e) {

			errorMessage = "Error in uploading Assignment file : "

			+ e.getMessage();

			logger.error("Exception" + errorMessage, e);

		} finally {

			if (inputStream != null)

				IOUtils.closeQuietly(inputStream);

			if (outputStream != null)

				IOUtils.closeQuietly(outputStream);

		}

		return errorMessage;

	}

	@RequestMapping(value = "/createNewContentFromJSON", method = {

	RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody Content createNewContentFromJSON(

			@RequestParam Map<String, String> contentFields,

			@RequestParam("file") MultipartFile file,
			@RequestParam(name = "users", required = false) List<String> userList) {

		

		String contentName = contentFields.get("contentName");

		contentName = StringUtils.trim(contentName);

		

		List<Content> contents = contentDao.findAllSQL(

		"Select * from content where contentName = ?",

		new Object[] { contentName });

		if (contents.size() != 0) {

			return contents.get(0);

		}

		Content content = new Content();

		/*
		 * content.setAcadMonth(contentFields.get("acadMonth"));
		 * 
		 * content.setAcadYear(Integer.parseInt(contentFields
		 * 
		 * .get("acadYear")));
		 */

		content.setCourseId(Long.valueOf(contentFields.get("courseId")));

		content.setStartDate(Utils.converFormats(contentFields.get("startDate")));

		content.setEndDate(Utils.converFormats(contentFields.get("endDate")));

		String path = uploadContentFile(content, file);

		content.setContentType("File");
		content.setAccessType("Public");

		content.setFilePath(path);

		content.setFolderPath(path);

		content.setFacultyId(contentFields.get("facultyId"));

		content.setSendEmailAlert("N");

		content.setSendSmsAlert("N");

		content.setContentDescription(contentFields.get("contentDescription"));

		content.setAcadMonth(contentFields.get("acadMonth"));
		if (contentFields.containsKey("acadYear"))
			content.setAcadYear(Integer.parseInt(contentFields.get("acadYear")));

		content.setCreatedBy(contentFields.get("facultyId"));

		content.setLastModifiedBy(contentFields.get("facultyId"));

		content.setContentName(contentName);

		try {
			List<StudentContent> inserts = new ArrayList<StudentContent>();
			List<StudentContent> updates = new ArrayList<StudentContent>();

			contentService.insertWithIdReturn(content);
			String contentId = content.getId() + "";
		
			if (userList != null)
				for (String username : userList) {
					boolean isInsert = true;

					try {

						StudentContent studentContent = null;

						try {

							studentContent = studentContentService
									.findStudentContent(username, contentId);

						} catch (Exception e) {

							logger.error("Exception", e);

						}

						if (studentContent == null) {

							studentContent = new StudentContent();

							

						} else {

							isInsert = false;

							

						}

						studentContent.setUsername(username);

						studentContent.setAcadMonth(content.getAcadMonth());

						studentContent.setAcadYear(content.getAcadYear());

						studentContent.setCourseId(content.getCourseId());

						/* studAssignment.setDueDate(assignmentDB.getEndDate()); */

						studentContent.setCreatedBy(content.getFacultyId());

						studentContent.setLastModifiedBy(content
								.getLastModifiedBy());

						studentContent.setContentId(content.getId());

						studentContent.setCount(0);

						if (isInsert)
							inserts.add(studentContent);
						else
							updates.add(studentContent);

						

					} catch (Exception ex) {

						logger.error("Exception while uploading " + contentId
								+ "username" + username, ex);

					}
				}
			if (inserts != null && !inserts.isEmpty())
				studentContentService.insertBatch(inserts);
			if (updates != null && !updates.isEmpty())
				studentContentService.updateBatch(updates);

			
		} catch (Exception ex) {
			logger.error("Exception", ex);

		}

		return content;

	}

	@RequestMapping(value = "/allocateNewContentToStudentsByParam", method = {

	RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody Content allocateContentToStudentsByParam(

	@RequestParam Map<String, String> allRequestParams,

	@RequestParam("file") MultipartFile file) {

		String contentId = allRequestParams.get("contentId");

		String username = allRequestParams.get("username");

		Content contentDB = contentService.findByID(Long

		.valueOf(contentId));

		boolean isInsert = true;

		try {

			StudentContent studentContent = null;

			try {

				studentContent = studentContentService.findStudentContent(
						username, contentId);

			} catch (Exception e) {

				logger.error("Exception", e);

			}

			if (studentContent == null) {

				studentContent = new StudentContent();

				

			} else {

				isInsert = false;

				

			}

			studentContent.setUsername(username);

			studentContent.setAcadMonth(contentDB.getAcadMonth());

			studentContent.setAcadYear(contentDB.getAcadYear());

			studentContent.setCourseId(contentDB.getCourseId());

			/* studAssignment.setDueDate(assignmentDB.getEndDate()); */

			studentContent.setCreatedBy(contentDB.getFacultyId());

			studentContent.setLastModifiedBy(contentDB.getLastModifiedBy());

			studentContent.setContentId(contentDB.getId());

			studentContent.setCount(0);

			if (isInsert)

				studentContentService.insertBatch(Arrays

				.asList(studentContent));

			else

				studentContentService.updateBatch(Arrays

				.asList(studentContent));

			
		} catch (Exception ex) {

			logger.error("Exception while uploading " + contentId

			+ "username" + username, ex);

		}

		return contentDB;

	}

}
