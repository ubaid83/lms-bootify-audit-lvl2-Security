package com.spts.lms.web.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;
import org.apache.poi.xssf.usermodel.XSSFDataValidationConstraint;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.glassfish.jersey.client.ClientConfig;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.pdf.PdfWriter;
import com.spts.lms.beans.FacultyLectureReschedule.FacultyLectureReschedule;
import com.spts.lms.beans.InfraKeys.InfraKeys;
import com.spts.lms.beans.amazon.AmazonS3ClientService;
import com.spts.lms.beans.assignment.Assignment;
import com.spts.lms.beans.assignment.StudentAssignment;
import com.spts.lms.beans.content.Content;
import com.spts.lms.beans.content.StudentContent;
import com.spts.lms.beans.copyleaksAudit.CopyleaksAudit;
import com.spts.lms.beans.course.Course;
import com.spts.lms.beans.library.Library;
import com.spts.lms.beans.program.Program;
import com.spts.lms.beans.studentParent.StudentParent;
import com.spts.lms.beans.test.StudentQuestionResponse;
import com.spts.lms.beans.test.StudentQuestionResponseAudit;
import com.spts.lms.beans.test.StudentTest;
import com.spts.lms.beans.test.Test;
import com.spts.lms.beans.test.TestQuestion;
import com.spts.lms.beans.user.User;
import com.spts.lms.beans.variables.LmsVariables;
import com.spts.lms.daos.library.LibraryDAO;
import com.spts.lms.plagscan.CopyLeaks;
import com.spts.lms.plagscan.PlagScanner;
import com.spts.lms.services.FacultyLectureRescheduleServices.FacultyLectureRescheduleService;
import com.spts.lms.services.InfraKeysService.InfraKeysService;
import com.spts.lms.services.assignment.AssignmentService;
import com.spts.lms.services.assignment.StudentAssignmentAuditService;
import com.spts.lms.services.assignment.StudentAssignmentService;
import com.spts.lms.services.attendance.AttendanceService;
import com.spts.lms.services.content.ContentService;
import com.spts.lms.services.content.StudentContentService;
import com.spts.lms.services.copyleaksAudit.CopyleaksAuditService;
import com.spts.lms.services.course.CourseService;
import com.spts.lms.services.course.UserCourseService;
import com.spts.lms.services.library.LibraryService;
import com.spts.lms.services.notification.Notifier;
import com.spts.lms.services.program.ProgramService;
import com.spts.lms.services.studentParent.StudentParentService;
import com.spts.lms.services.test.StudentQuestionResponseAuditService;
import com.spts.lms.services.test.StudentQuestionResponseService;
import com.spts.lms.services.test.StudentTestService;
import com.spts.lms.services.test.TestQuestionService;
import com.spts.lms.services.test.TestService;
import com.spts.lms.services.user.UserService;
import com.spts.lms.services.variables.LmsVariablesService;
import com.spts.lms.web.helper.CopyCaseHelper;
import com.spts.lms.web.utils.RSAConfig;
import com.spts.lms.web.utils.Utils;

@RestController
@RequestMapping("/api")
public class RestApiController {


	@Autowired
	AttendanceService attendanceService;

	@Autowired
	UserService userService;

	@Autowired
	CourseService courseService;
	
	@Autowired
	UserCourseService userCourseService;

	@Autowired
	LibraryService libraryService;

	@Autowired
	LibraryDAO libraryDAO;

	@Autowired
	ContentService contentService;

	@Autowired
	StudentTestService studentTestService;

	@Autowired
	TestService testService;

	@Autowired
	ProgramService programService;

	@Autowired
	StudentQuestionResponseService studentQuestionResponseService;

	@Autowired
	StudentQuestionResponseAuditService studentQuestionResponseAuditService;

	@Autowired
	TestQuestionService testQuestionService;

	@Autowired
	AssignmentService assignmentService;

	@Autowired
	StudentAssignmentService studentAssignmentService;

	@Autowired
	CopyleaksAuditService copyleaksAuditService;

	@Autowired
	PlagScanner submit;

	@Autowired
	CopyLeaks copyLeaks;
	
	@Autowired
	InfraKeysService infraKeysService;

	@Autowired
	StudentAssignmentAuditService studentAssignmentAuditService;
	
	@Autowired
	FacultyLectureRescheduleService facultyLectureRescheduleService;

	@Value("${copyleaskKey:''}")
	private String copyleaskKey;

	@Value("${copyleaksId:''}")
	private String copyleaksEmail;

	@Value("${file.base.directory.s3}")
	private String baseDirS3;

	@Value("${file.base.directory.test}")
	private String testBaseDir;

	@Value("${app}")
	private String app;

	@Value("${lms.assignment.questionFolder}")
	private String assignmentFolder;

	@Value("${JWT_SECRET}")
	private String JWT_SECRET;
	
	@Autowired
	CopyCaseHelper copyCaseHelper;

	@Autowired
	AmazonS3ClientService amazonS3ClientService;

	@Autowired
	LmsVariablesService lmsVariablesService;
	
	@Autowired
	StudentContentService studentContentService;

	@Autowired
	StudentParentService studentParentService;

	@Autowired
	Notifier notifier;

	@Value("${lms.assignment.downloadAllFolder}")
	private String downloadAllFolder;

	private static final Logger logger = Logger.getLogger(RestApiController.class);
	ObjectMapper mapper = new ObjectMapper();

	@RequestMapping(value = { "/getFacultyWorkload" }, method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String getFacultyWorkload(@RequestParam(name = "eventId") String eventId,
			@RequestParam(name = "facultyId") String facultyId) {
		logger.info("eventId " + eventId + " facultyId " + facultyId);
		String json = attendanceService.pullFacultyWorkload(eventId, facultyId);
		logger.info("json--->"+json);
		return json;
	}

	@RequestMapping(value = { "/getCourseStatiticsByUsernameForApp" }, method = { RequestMethod.GET,
			RequestMethod.POST })
	public @ResponseBody String getCourseStatiticsByUsernameForApp(@RequestBody User users, HttpServletResponse resp) {
		try {

			String username = users.getUsername();

			User user = userService.findByUserName(username);

			if (user != null) {

				/*
				 * UserRole roles = userRoleService.findRoleByUsername(username); List<Role>
				 * role = new ArrayList<>(); role.add(roles.getRole()); user.setRoles(role);
				 */
				List<Course> CourseStatiticsList = courseService.findCourseStatiticsByUsernameForApp(username);

				String json = new Gson().toJson(CourseStatiticsList);
				logger.info("CourseStatiticsList " + CourseStatiticsList);

				return json;

			} else {
				return "{}";
			}

		} catch (Exception e) {
			logger.error("Exception", e);
			return "{}";

		}
	}

	// Hiren 27-11-2019
	@RequestMapping(value = "/insertShareLibraryContent", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String insertShareLibraryContent(@RequestBody String libJson) {

		logger.info("JSON---->" + libJson);
		logger.info("---------------------------inside insertShareLibraryContent---------------------------");
		// String[] str = content.getCourseIdToExport().split(",");

		// Object libShareObject;
		try {
			// libShareObject = new JSONParser().parse(libJson);
			// JSONArray libShareArray = (JSONArray) libShareObject;
			// logger.info("libRightsArraySize---->"+libShareArray.size());
			List<Library> allItems = mapper.readValue(libJson, new TypeReference<List<Library>>() {
			});
			// List<Library> allItems = libShareArray ;
			logger.info("allItems--->" + allItems);
			logger.info("allItems Size--->" + allItems.size());

			for (Library l : allItems) {
				logger.info("------inside for ------>" + l.getFilePath());

				String folderPath = l.getFolderPath();
				String filePath = l.getFilePath();
				l.setLastModifiedBy(l.getCreatedBy());
				logger.info("Insert Main--->" + l.getFilePath());
				if (null != l.getParentId()) {
					File parentFolderPath = new File(folderPath);
					String parentFPath = parentFolderPath.getParent();
					File parentFPathFile = new File(parentFPath);
					Library findZipFileRoot = libraryService.findOneContent(new File(parentFPath).getName(),
							parentFPathFile.getPath().replace("\\", "/"));
					logger.info("findZipFileRoot--->" + findZipFileRoot.getId());
					l.setParentId(findZipFileRoot.getId());
				}
				libraryService.insertWithIdReturn(l);

				// setSuccess(redirectAttributes,"Content Exported successfully");
				logger.info("Item Shared successfully--------------->" + l);

				Library con = libraryService.findOneContent(l.getContentName(), folderPath);

				logger.info("con------->" + con);
				logger.info("ChildrenList------->" + l.getChildrenList());
				logger.info("ChildrenList Size------->" + l.getChildrenList().size());
				if (l.getChildrenList().size() != 0) {
					Long parentId = con.getId();
					logger.info("con------->" + con);
					// for (Content cn : c.getChildrenList()) {
					insertShareLibraryContent(l.getChildrenList(), parentId);
					// }

				}

				break;
			}
		} catch (Exception e) {
			logger.error("Exception", e);
			return "{\"Status\":\"Fail\"}";

		}

		return "{\"Status\":\"Success\"}";
	}

	// Hiren 27-11-2019
	public void insertShareLibraryContent(List<Library> allItems, Long parentId) {

		for (Library l : allItems) {

			l.setLastModifiedBy(l.getCreatedBy());
			l.setParentId(parentId);
			logger.info("Insert Child--->" + l.getFilePath());
			libraryService.insertWithIdReturn(l);

			Library con = libraryService.findOneContent(l.getContentName(), l.getFolderPath());
			/*
			 * if (content.getAllocateToStudents().equals("Y")) {
			 * 
			 * saveStudentContentAllocationForAllStudents(con, m, principal); }
			 */
			logger.info("child con------->" + con.getFilePath());
			logger.info("child ChildrenList------->" + l.getChildrenList());
			logger.info("child ChildrenList Size------->" + l.getChildrenList().size());
			if (l.getChildrenList().size() != 0) {

				// parentId = con.getId();
				insertShareLibraryContent(l.getChildrenList(), con.getId());
			}
		}
	}

	// Hiren 29-11-2019
	@RequestMapping(value = "/addLibraryFileToShareContent", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String addLibraryFileToShareContent(@RequestBody String libJson) {

		try {
			Library library = mapper.readValue(libJson, new TypeReference<Library>() {
			});
			if (library.getFolderPath().endsWith("/")) {
				String folderPath = library.getFolderPath().substring(0, library.getFolderPath().length() - 1);
				Library l = libraryService.findOneContent(folderPath);
				List<Library> item = new ArrayList<>();
				item.add(library);
				insertShareLibraryContent(item, l.getId());
			}

		} catch (Exception e) {
			logger.error("Exception", e);
			return "{\"Status\":\"Fail\"}";
		}

		return "{\"Status\":\"Success\"}";
	}

	// Hiren 29-11-2019
	@RequestMapping(value = "/addLibraryFolderToShareContent", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String addLibraryFolderToShareContent(@RequestBody String libJson) {

		try {
			Library library = mapper.readValue(libJson, new TypeReference<Library>() {
			});

			String folderPath = library.getFolderPath().substring(0, library.getFolderPath().lastIndexOf("/"));
			Library l = libraryService.findOneContent(folderPath);
			List<Library> item = new ArrayList<>();
			item.add(library);
			insertShareLibraryContent(item, l.getId());

		} catch (Exception e) {
			logger.error("Exception", e);
			return "{\"Status\":\"Fail\"}";
		}

		return "{\"Status\":\"Success\"}";
	}

	// Hiren 29-11-2019
	@RequestMapping(value = "/addLibraryLinkToShareContent", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String addLibraryLinkToShareContent(@RequestBody String libJson) {

		try {
			Library library = mapper.readValue(libJson, new TypeReference<Library>() {
			});

			String folderPath = library.getFolderPath().substring(0, library.getFolderPath().lastIndexOf("/"));
			Library l = libraryService.findOneContent(folderPath);
			List<Library> item = new ArrayList<>();
			item.add(library);
			insertShareLibraryContent(item, l.getId());

		} catch (Exception e) {
			logger.error("Exception", e);
			return "{\"Status\":\"Fail\"}";
		}

		return "{\"Status\":\"Success\"}";
	}



	// Hiren 02-12-2019
	@RequestMapping(value = "/deleteItemToShareContent", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String deleteItemToShareContent(@RequestBody String libJson) {

		try {
			Library library = mapper.readValue(libJson, new TypeReference<Library>() {
			});
			Library newItem = library.getNewLibraryItem();
			String folderPath = library.getFolderPath();

			Library l = new Library();
			if (library.getOldLibraryFolderPath() != null) {
				l = libraryService.findOneContent(library.getContentName(), library.getOldLibraryFolderPath());
			} else {
				l = libraryService.findOneContent(library.getContentName(), folderPath);
			}
			// Long parentId = l.getParentId();
			if (Library.FOLDER.equalsIgnoreCase(l.getContentType())) {

				l.setFilePath(library.getFilePath() + "_Deleted");
				l.setFolderPath(library.getFolderPath() + "_Deleted");
				l.setActive("N");
				List<Library> libList = libraryService.findByParentId(l.getId());
				for (Library lib : libList) {
					File f = new File(lib.getFilePath());
					lib.setFolderPath(library.getFolderPath());
					lib.setFilePath(library.getFolderPath() + "/" + f.getName());
				}
				libList.add(l);

				libraryService.updateBatch(libList);

				// update query daal dao me

			} else if (Library.FILE.equalsIgnoreCase(l.getContentType())) {
				// FileUtils.deleteQuietly(new File(library.getFilePath()));

				// FileUtils.deleteQuietly(new File(library.getFilePath()));
				l.setFilePath(library.getFilePath());
				l.setFolderPath(library.getFolderPath());
				l.setActive("N");
				libraryService.update(l);

			} else if (Library.LINK.equalsIgnoreCase(l.getContentType())) {
				libraryService.deleteSoftById(l.getId() + "");
			} else {
			}

		} catch (Exception e) {
			logger.error("Exception", e);
			return "{\"Status\":\"Fail\"}";
		}

		return "{\"Status\":\"Success\"}";

	}

	// Hiren 02-12-2019
	@RequestMapping(value = "/updateLibraryFolderToShareContent", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String updateLibraryFolderToShareContent(@RequestBody String libJson) {
		try {
			Library library = mapper.readValue(libJson, new TypeReference<Library>() {
			});
			Library newLibraryItem = library.getNewLibraryItem();
			String folderPath = library.getFolderPath();
			Library l = libraryService.findOneContent(library.getContentName(), folderPath);

			l.setFolderPath(newLibraryItem.getFolderPath());
			l.setContentName(newLibraryItem.getContentName());
			l.setContentDescription(newLibraryItem.getContentDescription());
			l.setFilePath(newLibraryItem.getFilePath());
			l.setLastModifiedBy(newLibraryItem.getLastModifiedBy());
			l.setLastModifiedDate(newLibraryItem.getLastModifiedDate());

			libraryService.updateFolder(l);
			libraryService.updateChildrenDetails(newLibraryItem.getFolderPath(), library.getFolderPath());
		} catch (Exception e) {
			logger.error("Exception", e);
			return "{\"Status\":\"Fail\"}";
		}
		return "{\"Status\":\"Success\"}";
	}

	// Hiren 02-12-2019
	@RequestMapping(value = "/updateLibraryFileToShareContent", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String updateLibraryFileToShareContent(@RequestBody String libJson) {
		try {
			Library library = mapper.readValue(libJson, new TypeReference<Library>() {
			});
			Library newItem = library.getNewLibraryItem();
			Library l = libraryService.findOneContent(library.getContentName(), library.getFolderPath());
			l.setContentName(newItem.getContentName());
			l.setContentDescription(newItem.getContentDescription());
			l.setFilePath(newItem.getFilePath());
			l.setLastModifiedBy(newItem.getLastModifiedBy());
			l.setLastModifiedDate(newItem.getLastModifiedDate());
			libraryService.updateFile(l);

		} catch (Exception e) {

			logger.error("Exception", e);
			return "{\"Status\":\"Fail\"}";
		}
		return "{\"Status\":\"Success\"}";
	}

	// Hiren 09-12-2019
	@RequestMapping(value = "/updateLibraryLinkToShareContent", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String updateLibraryLinkToShareContent(@RequestBody String libJson) {
		try {
			Library library = mapper.readValue(libJson, new TypeReference<Library>() {
			});
			Library newItem = library.getNewLibraryItem();
			Library l = libraryService.findOneContent(library.getContentName(), library.getFolderPath());
			l.setContentName(newItem.getContentName());
			l.setContentDescription(newItem.getContentDescription());
			l.setLinkUrl(newItem.getLinkUrl());
			l.setLastModifiedBy(newItem.getLastModifiedBy());
			l.setLastModifiedDate(newItem.getLastModifiedDate());
			libraryService.updateLink(l);

		} catch (Exception e) {

			logger.error("Exception", e);
			return "{\"Status\":\"Fail\"}";
		}
		return "{\"Status\":\"Success\"}";
	}

	

	// shubham 18feb 2020

	@RequestMapping(value = { "/getUnsentFacultyAttendanceData" }, method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String getUnsentFacultyAttendanceData(@RequestBody String playerId) {

		String jsonResponse = "";
		String playerID = "";
		JSONObject jsonObj = null;
		try {
			jsonObj = new JSONObject(playerId);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (jsonObj.has("playerId")) {
			try {
				playerID = jsonObj.getString("playerId");
				logger.info("playerID==============> " + playerID);
				Client clientWS = null;
				ClientConfig clientConfig = null;
				WebTarget webTarget = null;
				Invocation.Builder invocationBuilder = null;
				Response response = null;

				String msg = "Uploading Pending Attendance";
				String json = "{" + "\"app_id\": \"2d46e24e-fb33-43aa-ad6e-fde6940c28ff\","
						+ "\"include_player_ids\": [\"" + playerID + "\"]," + "\"data\": {\"foo\": \"bar\"},"
						+ "\"contents\": {\"en\": \"" + msg + "\"}" + "}";

				if (playerID.endsWith("_____lecture")) {
					msg = "Get All Lecture";
					json = "{" + "\"app_id\": \"2d46e24e-fb33-43aa-ad6e-fde6940c28ff\"," + "\"include_player_ids\": [\""
							+ playerID.replace("_____lecture", "") + "\"]," + "\"data\": {\"foo\": \"bar\"},"
							+ "\"contents\": {\"en\": \"" + msg + "\"}" + "}";
				}
				int responseCode;
				clientConfig = new ClientConfig();

				clientWS = ClientBuilder.newClient(clientConfig);
				webTarget = clientWS.target("https://onesignal.com/api/v1/notifications");

				invocationBuilder = webTarget.request().header(HttpHeaders.AUTHORIZATION,
						"MDI5OTI2ZTUtYzQ4Ni00MGU5LTk2YjQtMjI4NzA1OGM0ZjBl");

				response = invocationBuilder.post(Entity.entity(json.toString(), MediaType.APPLICATION_JSON));

				responseCode = response.getStatus();

				logger.info("responseCode: " + responseCode);

				jsonResponse += response.readEntity(String.class);

				logger.info("jsonResponse:\n" + jsonResponse);

				return jsonResponse;

			} catch (JSONException e) {
				e.printStackTrace();
				return "{\"jsonResponse\":\"Failed with Exception\"}";
			}
		} else {
			return "{\"jsonResponse\":\"Playerid is null\"}";
		}
	}

	@RequestMapping(value = { "/deleteUserDataUsingPlayerId" }, method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String deleteUserDataUsingPlayerId(@RequestBody String playerId) {

		String jsonResponse = "";
		String playerID = "";
		JSONObject jsonObj = null;
		try {
			jsonObj = new JSONObject(playerId);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (jsonObj.has("playerId")) {
			try {
				playerID = jsonObj.getString("playerId");
				logger.info("playerID==============> " + playerID);
				Client clientWS = null;
				ClientConfig clientConfig = null;
				WebTarget webTarget = null;
				Invocation.Builder invocationBuilder = null;
				Response response = null;

				String msg = "Delete Android Database";

				String json = "{" + "\"app_id\": \"2d46e24e-fb33-43aa-ad6e-fde6940c28ff\","
						+ "\"include_player_ids\": [\"" + playerID + "\"]," + "\"data\": {\"foo\": \"bar\"},"
						+ "\"contents\": {\"en\": \"" + msg + "\"}" + "}";

				int responseCode;
				clientConfig = new ClientConfig();

				clientWS = ClientBuilder.newClient(clientConfig);
				webTarget = clientWS.target("https://onesignal.com/api/v1/notifications");

				invocationBuilder = webTarget.request().header(HttpHeaders.AUTHORIZATION,
						"MDI5OTI2ZTUtYzQ4Ni00MGU5LTk2YjQtMjI4NzA1OGM0ZjBl");

				response = invocationBuilder.post(Entity.entity(json.toString(), MediaType.APPLICATION_JSON));

				responseCode = response.getStatus();

				logger.info("responseCode: " + responseCode);

				jsonResponse += response.readEntity(String.class);

				logger.info("jsonResponse:\n" + jsonResponse);

				return jsonResponse;

			} catch (JSONException e) {
				e.printStackTrace();
				return "{\"jsonResponse\":\"Failed with Exception\"}";
			}
		} else {
			return "{\"jsonResponse\":\"Playerid is null\"}";
		}
	}

	@RequestMapping(value = "/deleteShareContent", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String deleteShareContent(@RequestBody String oldContent) {
		try {
			Content content = mapper.readValue(oldContent, new TypeReference<Content>() {});
			List<Content> contentList = contentService.findOneContentForMultipleCourse(content.getContentName(), content.getFolderPath());
			for(Content c: contentList) {
				studentContentService.deleteByContentId(c.getId());
				contentService.deleteSoftById(String.valueOf(c.getId())); 
			}
		} catch (Exception e) {
			logger.error("Exception", e);
			return "{\"Status\":\"Fail\"}";
		}	
		return "{\"Status\":\"Success\"}";
	}


	@RequestMapping(value = "/insertShareContent", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String insertShareContent(@RequestBody String contentJson) {

		logger.info("JSON---->" + contentJson);
		logger.info("---------------------------inside insertShareContent---------------------------");

		try {
			Map<String, List<Content>> objJson = mapper.readValue(contentJson,
					new TypeReference<Map<String, List<Content>>>() {
					});
			List<Content> itemsUnderPath = objJson.get("items");
			List<Content> courseIds = objJson.get("courseIds");
			String acadYear = "";
			String[] str = null;
			String courseIdToExport = "";
			for (Content cids : courseIds) {
				acadYear = cids.getAcadYearToExport();
				courseIdToExport = cids.getCourseIdToExport();
				str = cids.getFacultyId().split(",");
			}

			if (null != str) {
				for (int i = 0; i < str.length; i++) {
					for (Content c : itemsUnderPath) {
						Content conCheck = contentService.findOneContentByCourseId(c.getContentName(),
								c.getFolderPath(), courseIdToExport, str[i]);
						if (null != conCheck) {
							List<Content> itemsToDelete = contentService.getItemsToShare(conCheck, courseIdToExport);
							for (Content cd : itemsToDelete) {
								studentContentService.deleteByContentId(cd.getId());
								contentService.deleteSoftById(String.valueOf(cd.getId()));
							}
						}

						String folderPath = c.getFolderPath();
						String filePath = c.getFilePath();
						c.setCourseId(Long.valueOf(courseIdToExport));
						c.setFacultyId(str[i]);
						c.setModuleId(null);
						c.setAccessType("Everyone");
						c.setAcadYear(Integer.valueOf(acadYear));
						c.setLastModifiedBy(str[i]);
						c.setParentContentId(null);

						contentService.insertWithIdReturn(c);
						saveStudentContentAllocationForAllStudents(c);
						Content con = contentService.findOneContentByCourseId(c.getContentName(), folderPath,
								courseIdToExport, str[i]);
						if (c.getChildrenList().size() != 0) {
							Long parentId = con.getId();
							insertShareContent(c.getChildrenList(), parentId, acadYear, courseIdToExport, str[i]);

						}
						break;
					}
				}
			}

		} catch (Exception e) {
			logger.error("Exception", e);

			return "{\"Status\":\"Fail\"}";

		}

		return "{\"Status\":\"Success\"}";
	}

	// Hiren 27-11-2019
	public void insertShareContent(List<Content> allItems, Long parentId, String acadYear, String courseId,
			String facultyId) {

		for (Content c : allItems) {

			c.setLastModifiedBy(facultyId);
			c.setFacultyId(facultyId);
			c.setParentContentId(parentId);
			c.setCourseId(Long.valueOf(courseId));
			c.setModuleId(null);
			c.setAccessType("Everyone");
			c.setAcadYear(Integer.valueOf(acadYear));

			Content conCheck = contentService.findOneContentByCourseId(c.getContentName(), c.getFolderPath(), courseId,
					facultyId);

			if (null != conCheck) {
				studentContentService.deleteByContentId(conCheck.getId());
				contentService.deleteSoftById(String.valueOf(conCheck.getId()));
			}
			contentService.insertWithIdReturn(c);
			saveStudentContentAllocationForAllStudents(c);
			Content con = contentService.findOneContentByCourseId(c.getContentName(), c.getFolderPath(), courseId,
					facultyId);
			if (c.getChildrenList().size() != 0) {

				insertShareContent(c.getChildrenList(), con.getId(), acadYear, courseId, facultyId);
			}
		}

	}

	public void saveStudentContentAllocationForAllStudents(Content content) {
		Content contentDb = contentService.findByID(content.getId());

		List<String> allocateContent = new ArrayList<String>();
		List<String> parentList = new ArrayList<String>();
		String subject = "Content with name  " + contentDb.getContentName();

		try {
			List<StudentContent> studentContent = studentContentService
					.getStudentsForContentCoursewise(content.getCourseId());

			for (StudentContent sg : studentContent) {
				allocateContent.add(sg.getUsername());

			}
			content.setStudents(allocateContent);

			Course course = content.getCourseId() != null ? courseService.findByID(Long.valueOf(content.getCourseId()))
					: null;
			StringBuffer buff = new StringBuffer(subject);

			if (course != null) {
				buff.append(" for Course ");
				buff.append(course.getCourseName());
			}
			buff.append(" shared with you ");
			subject = buff.toString();
			ArrayList<StudentContent> studentContentMappingList = new ArrayList<StudentContent>();
			List<String> studentList = new ArrayList<String>();
			if (content.getStudents() != null && content.getStudents().size() > 0) {

				for (String studentUsername : content.getStudents()) {
					StudentContent bean = new StudentContent();
					bean.setAcadMonth(content.getAcadMonth());
					bean.setAcadYear(content.getAcadYear());
					bean.setContentId(content.getId());
					bean.setCourseId(content.getCourseId());
					bean.setUsername(studentUsername);
					bean.setCreatedBy(content.getCreatedBy());
					bean.setLastModifiedBy(content.getCreatedBy());
					studentList.add(studentUsername);
					List<StudentContent> studentAssList = studentContentService.getStudentUsername(bean.getContentId(),
							bean.getCourseId());

					if (studentAssList.isEmpty()) {
						studentContentMappingList.add(bean);
					} else {
						List<String> names = new ArrayList<String>();

						for (StudentContent ass : studentAssList) {
							names.add(ass.getUsername());
						}

						if (!names.contains(bean.getUsername())) {
							studentContentMappingList.add(bean);
						}
					}
				}

				studentContentService.insertBatch(studentContentMappingList);

				Map<String, Map<String, String>> result = null;
				if (!studentList.isEmpty()) {
					if ("Y".equals(contentDb.getSendEmailAlertToParents())) {
						for (String s : studentList) {

							StudentParent sp = studentParentService.findParentByStudent(s);
							if (sp != null) {
								parentList.add(sp.getParent_username());
							}
						}
						if (parentList.size() != 0 || !parentList.isEmpty()) {
							studentList.addAll(parentList);
						}

					}

					if ("Y".equals(contentDb.getSendSmsAlertToParents())) {
						for (String s : studentList) {

							StudentParent sp = studentParentService.findParentByStudent(s);
							if (sp != null) {
								parentList.add(sp.getParent_username());
							}
						}
						if (parentList.size() != 0 || !parentList.isEmpty()) {
							studentList.addAll(parentList);
						}

					}
					if ("Y".equals(contentDb.getSendEmailAlert())) {

						result = userService.findEmailByUserName(studentList);
						Map<String, String> email = result.get("emails");
						Map<String, String> mobiles = new HashMap();
						notifier.sendEmail(email, mobiles, subject, subject);
					}

					if ("Y".equals(contentDb.getSendSmsAlert())) {
						if (result != null)

							result = userService.findEmailByUserName(studentList);
						Map<String, String> email = new HashMap();
						Map<String, String> mobiles = result.get("mobiles");
						notifier.sendEmail(email, mobiles, subject, subject);
					}
				}
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}


	@RequestMapping(value = "/reAllocateTestById", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String reAllocateTestById(@RequestParam(name = "id") String id) {

		Test testDB = testService.findByID(Long.valueOf(id));

		List<StudentTest> studentTestList = studentTestService.findByTestId(testDB.getId());

		if (studentTestList.size() > 0) {

			logger.info("file creation entered");
			String testSDate = Utils.formatDate("yyyy-MM-dd'T'HH:mm:ss", "dd-MM-yyyy", testDB.getStartDate());
			String testEDate = Utils.formatDate("yyyy-MM-dd'T'HH:mm:ss", "dd-MM-yyyy", testDB.getEndDate());
			String testDate = testSDate + "-" + testEDate;

			String rootFolder = testBaseDir + "/" + app;
			File folderR = new File(rootFolder);
			folderR.setExecutable(true, false);
			folderR.setWritable(true, false);
			folderR.setReadable(true, false);

			String folderPath = testBaseDir + "/" + app + "/" + "Tests";
			File folderP = new File(folderPath);
			if (!folderP.exists()) {
				folderP.mkdirs();

				logger.info("folder created");
			}
			folderP.setExecutable(true, false);
			folderP.setWritable(true, false);
			folderP.setReadable(true, false);

			String subFolderPath = folderPath + "/" + testDB.getId() + "-" + testDate;

			File subFolderP = new File(subFolderPath);

			if (!subFolderP.exists()) {
				subFolderP.mkdir();
				logger.info("subfolder created");
			}
			subFolderP.setExecutable(true, false);
			subFolderP.setWritable(true, false);
			subFolderP.setReadable(true, false);

			logger.info("subfolder created" + subFolderPath);

			for (StudentTest st : studentTestList) {
				File jsonFilePath = new File(subFolderPath + "/" + st.getUsername() + ".json");
				if (jsonFilePath.exists()) {
					jsonFilePath.delete();
					logger.info("file Deleted----> " + st.getUsername() + ".json");
				}
			}

			studentTestService.removeStudentQRespFile(testDB.getId());

			studentTestService.removeStudentQueResp(testDB.getId());

			studentTestService.allocateTestQuestionsForAllStudent(testDB.getId(), subFolderPath);
		}

		return "Success";
	}

	@RequestMapping(value = "/getProgramsOfSchool", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String getProgramsOfSchool() {
		// String username = p.getName();
		// Token userdetails1 = (Token) p;
		// String ProgramId = userdetails1.getProgramId();
		try {

			List<Program> programs = programService.findPrograms();
			logger.info("programs---->" + programs);
			// m.addAttribute("moduleList", moduleList);
			List<Map<String, String>> res = new ArrayList<Map<String, String>>();

			for (Program p : programs) {
				Map<String, String> returnMap = new HashMap();
				returnMap.put(String.valueOf(p.getId()), p.getProgramName());
				res.add(returnMap);
			}
			ObjectMapper mapper = new ObjectMapper();
			String result = mapper.writeValueAsString(res);
			logger.info("result------->" + result);
			return result;
		} catch (Exception ex) {

			logger.error("Exception", ex);
			return "";
		}

	}

	@RequestMapping(value = "/getCourseByProgramIdOfSchool", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String getCourseByProgramIdOfSchool(@RequestBody String contentJson) {
		// String username = p.getName();
		// Token userdetails1 = (Token) p;
		// String ProgramId = userdetails1.getProgramId();
		try {
			Content cJson = mapper.readValue(contentJson, new TypeReference<Content>() {});
//			logger.info("campus--->"+cJson.getCampusId());
			List<Course> course = courseService.findCoursesByProgramIdAndAcadYear(String.valueOf(cJson.getAcadYear()),
					String.valueOf(cJson.getProgramId()), String.valueOf(cJson.getCampusId()));
			logger.info("programs---->" + course);
			// m.addAttribute("moduleList", moduleList);
			List<Map<String, String>> res = new ArrayList<Map<String, String>>();

			for (Course c : course) {
				Map<String, String> returnMap = new HashMap();
				returnMap.put(String.valueOf(c.getId()), c.getCourseName());
				res.add(returnMap);
			}
			ObjectMapper mapper = new ObjectMapper();
			String result = "";
			result = mapper.writeValueAsString(res);
			logger.info("result------->" + result);

			return result;
		} catch (Exception ex) {

			logger.error("Exception", ex);
			return "";
		}

	}

	@RequestMapping(value = "/completeStudentTestAjaxForSubjectiveExt", method = { RequestMethod.POST,
			RequestMethod.GET })
	public @ResponseBody String completeStudentTestAjaxForSubjectiveExt(@RequestParam(required = false) String testId,
			@RequestParam(required = false) String studentTestId,
			@RequestParam(required = false) String studentFilePath) {

		try {

			if (testId != null) {

				List<StudentTest> st = studentTestService.getStudentTestNotSubmitted(testId);

				for (StudentTest s : st) {

					ObjectMapper mapper = new ObjectMapper();
					String studentFilePathTemp = s.getStudentQRespFilePath().replaceAll(";", "/");

					String studentQRespFiles = FileUtils.readFileToString(new File(studentFilePathTemp));
					List<StudentQuestionResponse> studentQRespList = mapper.readValue(studentQRespFiles,
							new TypeReference<List<StudentQuestionResponse>>() {
							});

					studentQuestionResponseService.insertBatchIntoTemp(studentQRespList);

					StudentTest studentTestDB = studentTestService.callCompleteStudentTest(s.getId().toString(),
							"Subjective");

					List<StudentQuestionResponseAudit> sqrAuditList = new ArrayList<>();
					for (StudentQuestionResponse sqr : studentQRespList) {
						sqr.setAttemptNo(studentTestDB.getAttempt() - 1);
						StudentQuestionResponseAudit sqrAudit = responseBeanToAuditBeanForSubjectiveEvaluation(sqr,
								studentTestDB.getTestId());
						sqrAuditList.add(sqrAudit);

					}
					studentQuestionResponseAuditService.insertBatch(sqrAuditList);

				}

			} else {

				ObjectMapper mapper = new ObjectMapper();
				StudentTest st = studentTestService.findByID(Long.valueOf(studentTestId));
				studentFilePath = st.getStudentQRespFilePath().replaceAll(";", "/");

				String studentQRespFiles = FileUtils.readFileToString(new File(studentFilePath));
				List<StudentQuestionResponse> studentQRespList = mapper.readValue(studentQRespFiles,
						new TypeReference<List<StudentQuestionResponse>>() {
						});

				studentQuestionResponseService.insertBatchIntoTemp(studentQRespList);

				StudentTest studentTestDB = studentTestService.callCompleteStudentTest(studentTestId, "Subjective");

				List<StudentQuestionResponseAudit> sqrAuditList = new ArrayList<>();
				for (StudentQuestionResponse sqr : studentQRespList) {
					sqr.setAttemptNo(studentTestDB.getAttempt() - 1);
					StudentQuestionResponseAudit sqrAudit = responseBeanToAuditBeanForSubjectiveEvaluation(sqr,
							studentTestDB.getTestId());
					sqrAuditList.add(sqrAudit);

				}
				studentQuestionResponseAuditService.insertBatch(sqrAuditList);

			}
			return "Success";

		} catch (Exception e) {
			logger.error("Error", e);
			return "Fail";
		}

	}

	public StudentQuestionResponseAudit responseBeanToAuditBeanForSubjectiveEvaluation(StudentQuestionResponse sqr,
			Long testId) {

		StudentQuestionResponseAudit sqra = new StudentQuestionResponseAudit();
		StudentTest st = studentTestService.findByID(sqr.getStudentTestId());

		testId = st.getTestId();

		StudentTest studentTestDB = studentTestService.findByID(sqr.getStudentTestId());

		sqra.setUsername(sqr.getUsername());
		sqra.setStudentTestId(sqr.getStudentTestId());
		sqra.setQuestionId(sqr.getQuestionId());
		sqra.setAnswer(sqr.getAnswer());
		sqra.setCreatedBy(sqr.getUsername());
		sqra.setLastModifiedBy(sqr.getUsername());
		sqra.setAttempts(String.valueOf(studentTestDB.getAttempt()));

		return sqra;
	}

	@RequestMapping(value = "/evaluateTestExt", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody String evaluateTestExt(@RequestParam(required = false) String testId,
			@RequestParam(required = false) String studentTestId,
			@RequestParam(required = false) String studentFilePath) {

		try {
			// Test testDB = testService.findByID(Long.valueOf(testId));

			List<StudentTest> stList = studentTestService.findByTestId(Long.valueOf(testId));
			List<TestQuestion> tqList = testQuestionService.findByTestId(Long.valueOf(testId));

			for (TestQuestion tq : tqList) {

				List<StudentQuestionResponse> sqrList = studentQuestionResponseService
						.findByQIdNew(String.valueOf(tq.getId()));

				for (StudentQuestionResponse sqr : sqrList) {
					if ("MCQ".equals(tq.getQuestionType())) {
						if (null == sqr.getAnswer()) {
							logger.info("sqr is null--->" + tq.getId() + "-" + sqr.getUsername());
							Double zero = 0.0;
							studentQuestionResponseService.updateStudentQuesRespMarks(zero, sqr.getId());
						} else if (tq.getType().equals("SINGLESELECT")
								&& sqr.getAnswer().equals(tq.getCorrectOption())) {
							logger.info("sqr is correct--->" + tq.getId() + "-" + sqr.getUsername());
							studentQuestionResponseService.updateStudentQuesRespMarks(tq.getMarks(), sqr.getId());
							// studentQuestionResponseAuditService.updateStudentQuesRespMarksAudit(tq.getMarks(),sqr.getId());
						} else if (tq.getType().equals("SINGLESELECT")
								&& !sqr.getAnswer().equals(tq.getCorrectOption())) {
							logger.info("sqr is wrong--->" + tq.getId() + "-" + sqr.getUsername());
							Double zero = 0.0;
							studentQuestionResponseService.updateStudentQuesRespMarks(zero, sqr.getId());
						} else {
							logger.info("sqr is multiselect--->" + tq.getId() + "-" + sqr.getUsername());
							String cOption = tq.getCorrectOption();
							String studentOption = sqr.getAnswer();
							List<String> splittedCorrectOption = new ArrayList<>();
							List<String> splittedAnswers = new ArrayList<>();
							if (tq.getCorrectOption().contains(",")) {
								splittedCorrectOption = Arrays.asList(cOption.split(","));

							}

							if (studentOption.contains(",")) {
								splittedAnswers = Arrays.asList(studentOption.split(","));

								int count = 0;
								if (splittedCorrectOption.size() > 0) {
									if (splittedCorrectOption.size() > splittedAnswers.size()) {
										for (String s : splittedCorrectOption) {

											if (splittedAnswers.contains(s)) {

											} else {
												count++;
											}
										}
									} else {
										for (String s : splittedAnswers) {

											if (splittedCorrectOption.contains(s)) {

											} else {
												count++;
											}
										}
									}
								} else {
									count++;
								}
								if (count > 0) {
									Double zero = 0.0;
									studentQuestionResponseService.updateStudentQuesRespMarks(zero, sqr.getId());
								} else {
									studentQuestionResponseService.updateStudentQuesRespMarks(tq.getMarks(),
											sqr.getId());
								}
							}
						}

					} else if ("Numeric".equals(tq.getQuestionType())) {

						// double correctAnswer = Double.valueOf(cOption);
						Double zero = 0.0;
						if (sqr.getAnswer().trim().isEmpty()) {
							studentQuestionResponseService.updateStudentQuesRespMarks(zero, sqr.getId());
						} else if (!ISVALIDINPUT(sqr.getAnswer())) {
							studentQuestionResponseService.updateStudentQuesRespMarks(zero, sqr.getId());
						} else {

							double studentAnswer = Double.valueOf(sqr.getAnswer());
							double rangeFrom = Double.valueOf(tq.getAnswerRangeFrom());
							double rangeTo = Double.valueOf(tq.getAnswerRangeTo());

							if (studentAnswer >= rangeFrom && studentAnswer <= rangeTo) {
								studentQuestionResponseService.updateStudentQuesRespMarks(tq.getMarks(), sqr.getId());

							} else {
								studentQuestionResponseService.updateStudentQuesRespMarks(zero, sqr.getId());
							}
						}

					}
				}
			}

			for (StudentTest st : stList) {
				studentTestService.calculateMarks(st);
			}

			return "Success";

		} catch (Exception e) {
			logger.error("Error", e);
			return "Fail";
		}

	}

	public static boolean ISVALIDINPUT(String input) {
		if (input != null) {
			if (input.matches("[0-9.]") || NumberUtils.isNumber(input)

			) {
				return true;
			}
		}
		return false;
	}

	@RequestMapping(value = "/completeStudentTestAjaxForObjectiveExt", method = { RequestMethod.POST,
			RequestMethod.GET })
	public @ResponseBody String completeStudentTestAjaxForObjectiveExt(@RequestParam(required = false) String testId,
			@RequestParam(required = false) String studentTestId,
			@RequestParam(required = false) String studentFilePath) {

		try {

			if (testId != null) {

				List<StudentTest> st = studentTestService.getStudentTestNotSubmitted(testId);

				for (StudentTest s : st) {

					ObjectMapper mapper = new ObjectMapper();
					String studentFilePathTemp = s.getStudentQRespFilePath().replaceAll(";", "/");

					String studentQRespFiles = FileUtils.readFileToString(new File(studentFilePathTemp));
					List<StudentQuestionResponse> studentQRespList = mapper.readValue(studentQRespFiles,
							new TypeReference<List<StudentQuestionResponse>>() {
							});

					studentQuestionResponseService.insertBatchIntoTemp(studentQRespList);

					StudentTest studentTestDB = studentTestService.callCompleteStudentTest(s.getId().toString(),
							"Objective");

					List<StudentQuestionResponseAudit> sqrAuditList = new ArrayList<>();
					for (StudentQuestionResponse sqr : studentQRespList) {
						sqr.setAttemptNo(studentTestDB.getAttempt() - 1);
						StudentQuestionResponseAudit sqrAudit = responseBeanToAuditBeanForSubjectiveEvaluation(sqr,
								studentTestDB.getTestId());
						sqrAuditList.add(sqrAudit);
					}
					studentQuestionResponseAuditService.insertBatch(sqrAuditList);

				}

			} else {
				logger.info("StudentTestId--->" + studentTestId);
				ObjectMapper mapper = new ObjectMapper();
				StudentTest st = studentTestService.findByID(Long.valueOf(studentTestId));
				studentFilePath = st.getStudentQRespFilePath().replaceAll(";", "/");

				String studentQRespFiles = FileUtils.readFileToString(new File(studentFilePath));
				List<StudentQuestionResponse> studentQRespList = mapper.readValue(studentQRespFiles,
						new TypeReference<List<StudentQuestionResponse>>() {
						});

				studentQuestionResponseService.insertBatchIntoTemp(studentQRespList);

				StudentTest studentTestDB = studentTestService.callCompleteStudentTest(studentTestId, "Objective");

				List<StudentQuestionResponseAudit> sqrAuditList = new ArrayList<>();
				for (StudentQuestionResponse sqr : studentQRespList) {
					sqr.setAttemptNo(studentTestDB.getAttempt() - 1);
					StudentQuestionResponseAudit sqrAudit = responseBeanToAuditBeanForSubjectiveEvaluation(sqr,
							studentTestDB.getTestId());
					sqrAuditList.add(sqrAudit);

				}
				studentQuestionResponseAuditService.insertBatch(sqrAuditList);

			}
			return "Success";

		} catch (Exception e) {
			logger.error("Error", e);
			return "Fail";
		}
	}

	@RequestMapping(value = "/checkPlagForAssignment", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody String checkPlagForAssignment(@RequestParam(required = false) String assignmentId,
			@RequestParam(required = false) String studentAssignmentId) {
		if (null != assignmentId && !"".equals(assignmentId)) {
			Assignment assignment = assignmentService.findByID(Long.valueOf(assignmentId));
			List<StudentAssignment> students = studentAssignmentService
					.findStudentsForPlagiarismCheck(assignment.getId());

			CopyleaksAudit cplAudit = copyleaksAuditService.getRecordByUsername(assignment.getFacultyId(),
					assignment.getId());

			boolean isPresent = false;
			if (cplAudit == null) {
				isPresent = false;
			} else {
				isPresent = true;
			}

			Integer totalCreditUsed = 0;

			for (StudentAssignment sa : students) {

				Map<String, Integer> plagValueMap = new HashMap<String, Integer>();

				StudentAssignment assignmentSubmission = studentAssignmentService.findByID(sa.getId());

				try {
					if (assignmentSubmission.getThreshold() == null) {

						File storedFile = submit.multipartToFileForS3(assignmentSubmission.getStudentFilePath());
						plagValueMap = copyLeaks.scan("demoacccountngsc@gmail.com",
								"7D1C0749-39A2-4529-AB12-958961224DE1", storedFile);

						String url = "";
						Integer plagValue = 0;
						Integer creditUsed = 0;
						for (String key : plagValueMap.keySet()) {
							url = key;
							if (url != null) {

							} else {
								url = "Copyleaks internal database";
							}

							plagValue = plagValueMap.get(key);

							if (url == "creditUsed") {

								creditUsed = plagValue;
								totalCreditUsed = totalCreditUsed + creditUsed;

							} else {
								assignmentSubmission.setUrl(url);
								assignmentSubmission.setThreshold(plagValue);
							}

						}

						studentAssignmentService.update(assignmentSubmission);

					} else {

					}

				} catch (Exception e) {
					logger.error("Error in checkPlagForAssignment--->" + e.getMessage());
					return "error";
				}

			}

			if (isPresent == false) {

				CopyleaksAudit cpl = new CopyleaksAudit();
				cpl.setAssignmentId(assignment.getId());
				cpl.setUsername(assignment.getFacultyId());
				cpl.setCount(1);
				cpl.setCreditUsed(totalCreditUsed);
				cpl.setCreatedBy(assignment.getFacultyId());
				cpl.setLastModifiedBy(assignment.getFacultyId());

				copyleaksAuditService.insert(cpl);

			} else {

				cplAudit.setCount(cplAudit.getCount() + 1);
				cplAudit.setCreditUsed(cplAudit.getCreditUsed() + totalCreditUsed);

				copyleaksAuditService.update(cplAudit);

			}

		} else if (null != studentAssignmentId && !"".equals(studentAssignmentId)) {
			StudentAssignment studentAssignment = studentAssignmentService.findByID(Long.valueOf(studentAssignmentId));
			Assignment assignment = assignmentService.findByID(studentAssignment.getAssignmentId());

			CopyleaksAudit cplAudit = copyleaksAuditService.getRecordByUsername(studentAssignment.getUsername(),
					assignment.getId());

			boolean isPresent = false;
			if (cplAudit == null) {
				isPresent = false;
			} else {
				isPresent = true;
			}

			Integer totalCreditUsed = 0;

			Map<String, Integer> plagValueMap = new HashMap<String, Integer>();

			StudentAssignment assignmentSubmission = studentAssignmentService.findByID(studentAssignment.getId());

			try {
				if (assignmentSubmission.getThreshold() == null) {

					File storedFile = submit.multipartToFileForS3(assignmentSubmission.getStudentFilePath());
					plagValueMap = copyLeaks.scan("demoacccountngsc@gmail.com", "7D1C0749-39A2-4529-AB12-958961224DE1",
							storedFile);

					String url = "";
					Integer plagValue = 0;
					Integer creditUsed = 0;
					for (String key : plagValueMap.keySet()) {
						url = key;
						if (url != null) {

						} else {
							url = "Copyleaks internal database";
						}

						plagValue = plagValueMap.get(key);

						if (url == "creditUsed") {

							creditUsed = plagValue;
							totalCreditUsed = totalCreditUsed + creditUsed;

						} else {
							assignmentSubmission.setUrl(url);
							assignmentSubmission.setThreshold(plagValue);
						}

					}

					studentAssignmentService.update(assignmentSubmission);

				} else {

				}

			} catch (Exception e) {
				logger.error("Error in checkPlagForAssignment--->" + e.getMessage());
				return "Error";
			}

			if (isPresent == false) {

				CopyleaksAudit cpl = new CopyleaksAudit();
				cpl.setAssignmentId(assignment.getId());
				cpl.setUsername(studentAssignment.getUsername());
				cpl.setCount(1);
				cpl.setCreditUsed(totalCreditUsed);
				cpl.setCreatedBy(studentAssignment.getUsername());
				cpl.setLastModifiedBy(studentAssignment.getUsername());

				copyleaksAuditService.insert(cpl);

			} else {

				cplAudit.setCount(cplAudit.getCount() + 1);
				cplAudit.setCreditUsed(cplAudit.getCreditUsed() + totalCreditUsed);

				copyleaksAuditService.update(cplAudit);

			}
		}
		return "Success";
	}

	@RequestMapping(value = "/downloadStudentAssignmentThresholdReport", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String downloadStudentAssignmentThresholdReport(@RequestParam(name = "assignmentId") String assignmentId,
			HttpServletRequest request, HttpServletResponse response) {
		OutputStream outStream = null;
		FileInputStream inputStream = null;

		try {

			Assignment assignment = assignmentService.findByID(Long.valueOf(assignmentId));
			List<StudentAssignment> assignmentsList = new ArrayList<StudentAssignment>();
			List<Long> childAssignmentId = assignmentService.getIdByParentModuleId(assignment.getId());
			if (childAssignmentId.size() > 0) {

				assignmentsList = studentAssignmentService.getStudentsAssignmentReportForModule(childAssignmentId);

			} else {
				assignmentsList = studentAssignmentService.getStudentsAssignmentReport(Long.valueOf(assignmentId));
			}

			XSSFWorkbook workbook = new XSSFWorkbook();

			XSSFSheet sheet = (XSSFSheet) workbook.createSheet("Student Assignments");

			Row r = sheet.createRow(0);

			r.createCell(0).setCellValue("Sr no.");
			r.createCell(1).setCellValue("Student User Name");
			r.createCell(2).setCellValue("Roll No.");
			r.createCell(3).setCellValue("Faculty Id");
			r.createCell(4).setCellValue("Group Id");
			r.createCell(5).setCellValue("Assignment Name");
			r.createCell(6).setCellValue("Threshold");
			r.createCell(7).setCellValue("Submission Status");
			r.createCell(8).setCellValue("Submission Date");
			r.createCell(9).setCellValue("Evaluation Status");
			r.createCell(10).setCellValue("Save Score");
			r.createCell(11).setCellValue("Save Remarks");
			r.createCell(12).setCellValue("Save Low Score Reason");

			int i = 1;
			for (StudentAssignment sa : assignmentsList) {
				Row row = sheet.createRow(i);
				row.createCell(0).setCellValue(i);
				row.createCell(1).setCellValue(sa.getUsername());
				row.createCell(2).setCellValue(sa.getRollNo());
				row.createCell(3).setCellValue(sa.getFacultyId());
				if (null != sa.getGroupId()) {
					row.createCell(4).setCellValue(sa.getGroupId());
				} else {
					row.createCell(4).setCellValue("");
				}
				row.createCell(5).setCellValue(sa.getAssignmentName());
				if (null != sa.getThreshold()) {
					row.createCell(6).setCellValue(sa.getThreshold());
				} else {
					row.createCell(6).setCellValue("");
				}
				row.createCell(7).setCellValue(sa.getSubmissionStatus());
				if (null != sa.getSubmissionDate()) {
					String submissionDate = Utils.formatDate("yyyy-MM-dd HH:mm:ss", sa.getSubmissionDate());
					row.createCell(8).setCellValue(submissionDate);
				} else {
					row.createCell(8).setCellValue("");
				}

				row.createCell(9).setCellValue(sa.getEvaluationStatus());
				if (null != sa.getScore()) {
					row.createCell(10).setCellValue(sa.getScore());
				} else {
					row.createCell(10).setCellValue("");
				}

				if (null != sa.getRemarks()) {
					row.createCell(11).setCellValue(sa.getRemarks());
				} else {
					row.createCell(11).setCellValue("");
				}

				if (null != sa.getLowScoreReason()) {
					row.createCell(12).setCellValue(sa.getLowScoreReason());

				} else {
					row.createCell(12).setCellValue("");

				}
				XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheet);
				String[] actions = new String[] {
						"Copy Case-Internet,Copy Case-Other Student,Wrong Answers,Other subject Assignment,Scanned/Handwritten Assignment,Only Questions written,Question Paper Uploaded,Blank Assignment,Corrupt file uploaded" };
				XSSFDataValidationConstraint constraint = (XSSFDataValidationConstraint) dvHelper
						.createExplicitListConstraint(actions);
				CellRangeAddressList addressList = new CellRangeAddressList(1, assignmentsList.size(), 12, 12);
				XSSFDataValidation validation = (XSSFDataValidation) dvHelper.createValidation(constraint, addressList);
				validation.setShowErrorBox(true);
				sheet.addValidationData(validation);
				i++;
			}
			// String folderPath = baseDir + "/" + app + "/" + "evaluateAssignmentTemp";
			String folderPath = downloadAllFolder + "evaluateAssignmentTemp";
			File folderP = new File(folderPath);
			if (!folderP.exists()) {
				folderP.mkdir();
			}
			String filePath = folderP.getAbsolutePath() + File.separator + "Evaluate_Students_Assignment_for_"
					+ assignment.getId() + "_" + assignment.getAssignmentName() + ".xlsx";
			File f = new File(filePath);
			if (!f.exists()) {
				FileOutputStream fileOut = new FileOutputStream(filePath);
				workbook.write(fileOut);
				fileOut.close();
			} else {
				FileUtils.deleteQuietly(f);
				FileOutputStream fileOut = new FileOutputStream(filePath);
				workbook.write(fileOut);
				fileOut.close();
				logger.info("File already exist");
			}

			ServletContext context = request.getSession().getServletContext();
			File downloadFile = new File(filePath);

			if (!downloadFile.exists()) {
				return "Error: Dowmload file is not found";
			}
			inputStream = new FileInputStream(downloadFile);

			// get MIME type of the file
			String mimeType = context.getMimeType(filePath);
			if (mimeType == null) {
				// set to binary type if MIME mapping not found
				mimeType = "application/octet-stream";
			}
			logger.info("MIME type: " + mimeType);

			// set content attributes for the response
			/* response.setContentType(mimeType); */
			response.setContentLength((int) downloadFile.length());

			// set headers for the response
			String headerKey = "Content-Disposition";
			String headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName());
			response.setHeader(headerKey, headerValue);

			// get output stream of the response
			outStream = response.getOutputStream();

			IOUtils.copy(inputStream, outStream);
		} catch (Exception ex) {
			logger.error("Exception", ex);
		} finally {
			if (inputStream != null)
				IOUtils.closeQuietly(inputStream);
			if (outStream != null)
				IOUtils.closeQuietly(outStream);

		}
		return null;

	}

	@RequestMapping(value = { "/checkCopiedAssignmentReportExt" }, method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String checkCopiedAssignmentReportExt(@RequestParam String assignmentId,
			HttpServletRequest request, HttpServletResponse response) {

		String filePath = "";
		List<StudentAssignment> studentAssignmentFilesList = studentAssignmentService
				.getStduentFilesForReport(Long.parseLong(assignmentId));
		Assignment assignment = assignmentService.findByID(Long.parseLong(assignmentId));
		Course c = courseService.findByID(assignment.getCourseId());
		StudentAssignment searchBean = new StudentAssignment();
		if (assignment.getThreshold() != null) {
			searchBean.setMinMatchPercent(assignment.getThreshold());
		} else {
			searchBean.setMinMatchPercent(50);
		}
		searchBean.setCourseName(c.getCourseName());
		searchBean.setAssignmentName(assignment.getAssignmentName());

		String projectUrl = "";
		OutputStream outStream = null;
		FileInputStream inputStream = null;
		try {
			StudentAssignment questionFileBean = new StudentAssignment();
			if (assignment.getFilePath() != null) {
				questionFileBean.setFilePath(assignment.getFilePath());
			} else {
				String fileName = assignment.getAssignmentName() + " questionfile.pdf";
				String qFilePath = assignmentFolder + File.separator + fileName;
				File folderPath = new File(assignmentFolder);
				if (!folderPath.exists()) {
					folderPath.mkdirs();
				}
				PdfWriter writer = null;
				Document document = new Document();
				writer = PdfWriter.getInstance(document, new FileOutputStream(qFilePath));

				document.open();
				Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
				Chunk chunk = new Chunk(assignment.getAssignmentText(), font);

				document.add(chunk);
				document.close();

				questionFileBean.setFilePath(qFilePath);
				assignment.setFilePath(qFilePath);
				assignment.setFilePreviewPath(qFilePath);
				assignmentService.update(assignment);

			}
			File ip = new File(copyCaseHelper.checkCopyCases(studentAssignmentFilesList, searchBean, questionFileBean));
			filePath = ip.getAbsolutePath();
			if (StringUtils.isEmpty(filePath)) {
				request.setAttribute("error", "true");
				request.setAttribute("errorMessage", "Error in downloading file.");
			}

			// get absolute path of the application
			ServletContext context = request.getSession().getServletContext();
			File downloadFile = new File(filePath);
			inputStream = new FileInputStream(downloadFile);

			// get MIME type of the file
			String mimeType = context.getMimeType(filePath);
			if (mimeType == null) {
				// set to binary type if MIME mapping not found
				mimeType = "application/octet-stream";
			}

			// set content attributes for the response
			/* response.setContentType(mimeType); */
			response.setContentLength((int) downloadFile.length());

			// set headers for the response
			String headerKey = "Content-Disposition";
			String headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName());
			response.setHeader(headerKey, headerValue);

			// get output stream of the response
			outStream = response.getOutputStream();

			IOUtils.copy(inputStream, outStream);
		} catch (Exception e) {
			logger.error("Error in checkCopiedAssignmentReportExt--->", e);
		}

		return null;
	}

	@RequestMapping(path = "/webhook/{STATUS}/{customId}", method = { RequestMethod.POST, RequestMethod.GET })
	public String getMessage(@PathVariable("STATUS") String STATUS, @PathVariable("customId") String customId,
			@RequestBody(required = false) String Completed, @RequestBody(required = false) String Error,
			@RequestBody(required = false) String CreditsChecked) {
		logger.info("STATUS--->" + STATUS);
		logger.info("customId---->" + customId);
		// Completed =
		// "{\"scannedDocument\":{\"scanId\":\"assignment5\",\"totalWords\":123,\"totalExcluded\":0,\"credits\":1,\"creationTime\":\"2020-04-26T06:25:28\"},\"results\":{\"internet\":[{\"url\":\"https://www2.informatik.hu-berlin.de/~wandelt/SW201213/FOL.pdf\",\"id\":\"1d3f13b205\",\"title\":\"FOL.pdf\",\"introduction\":\"First-Order
		// Logic: Review\\n\\nFirst-order logic\\n• First-order logic (FOL) models the
		// world in terms of\\n–\\n–\\n–\\n–\\n\\nObjects, which are things with
		// individual identities\\nProperties of objects that distinguish them from
		// other objects\\nRelations that hold
		// am...\",\"matchedWords\":41},{\"url\":\"https://www.cs.brandeis.edu/~jamesp/content/courses/LING130/FirstOrderLogic-1.pdf\",\"id\":\"272bf4bff1\",\"title\":\"FirstOrderLogic-1.pdf\",\"introduction\":\"First-order
		// logic\\n• First-order logic (FOL) models the world in terms
		// of\\n–\\n–\\n–\\n–\\n\\nObjects, which are things with individual
		// identities\\nProperties of objects that distinguish them from other
		// objects\\nRelations that hold among sets of
		// objects\\nFunctio...\",\"matchedWords\":25},{\"url\":\"http://www8.cs.umu.se/kurser/5DV122/HT13/material/03-web.pdf\",\"id\":\"63649ff775\",\"title\":\"03-web.pdf\",\"introduction\":\"Artificial
		// Intelligence:\\nMethods and Applications\\nLecture 3: Review of FOPL\\nHenrik
		// Björklund\\nRuvan Weerasinghe\\n\\nUmeå University\\n\\nWhat I’d be doing\\n•
		// Topics in Knowledge Representation\\n– 12th Nov (Tue) – Revision of FOL\\n–
		// 15th Nov (Fri) –
		// Reasoning...\",\"matchedWords\":18},{\"url\":\"https://www.csee.umbc.edu/courses/671/fall16/Slides/19-logic-aiF16.pdf\",\"id\":\"bb539c6b29\",\"title\":\"19-logic-aiF16.pdf\",\"introduction\":\"11/8/16\\n\\nFirst-Order
		// Logic &\\nInference\\nAI Class 19 (Ch. 8.1–8.3, 9 )\\n\\nMaterial from Dr.
		// Marie desJardin, Some material adopted from notes by Andreas Geyer-Schulz and
		// Chuck Dyer\\n\\nBookkeeping\\n• Midterms returned today\\n• HW4 due 11/7 @
		// 11:59\\n\\n1\\n\\n11/8...\",\"matchedWords\":25},{\"url\":\"http://www.cs.toronto.edu/~edelisle/384/f14/Lectures/LogicExample.pdf\",\"id\":\"d3fb61db95\",\"title\":\"LogicExample.pdf\",\"introduction\":\"Resolution
		// Proof Example.\\n(a) Marcus was a man.\\n(b) Marcus was a Roman.\\n(c) All
		// men are people.\\n(d) Caesar was a ruler.\\n(e) All Romans were either loyal
		// to Caesar or hated him (or both).\\n(f) Everyone is loyal to someone.\\n(g)
		// People only try to
		// ass...\",\"matchedWords\":28}],\"database\":[{\"scanId\":\"9a40a632-15c6-4538-a789-11d28e8dbe28\",\"id\":\"138aee878b\",\"title\":\"Copyleaks
		// internal database - Your file (AI_Assignment1_JrzUtGQUP1_GZz3HLgSHy.docx)
		// submitted on (23/04/2020)\",\"introduction\":\"No introduction
		// available.\",\"matchedWords\":120},{\"scanId\":\"7b7ad7ca-4717-4124-a744-077bcda8a7e1\",\"id\":\"3b5f24cf28\",\"title\":\"Copyleaks
		// internal database - Your file (AI_Assignment1_JrzUtGQUP1_GZz3HLgSHy.docx)
		// submitted on (23/04/2020)\",\"introduction\":\"No introduction
		// available.\",\"matchedWords\":120},{\"scanId\":\"1278915d-5c9a-45b8-83b2-56a53dde151e\",\"id\":\"5d03ed054b\",\"title\":\"Copyleaks
		// internal database - Your file (AI_Assignment1_JrzUtGQUP1_GZz3HLgSHy.docx)
		// submitted on (23/04/2020)\",\"introduction\":\"No introduction
		// available.\",\"matchedWords\":120},{\"scanId\":\"ee79e8b4-49ad-44f0-84cd-1935b4301cc9\",\"id\":\"6816c67069\",\"title\":\"Copyleaks
		// internal database - Your file (AI_Assignment1_JrzUtGQUP1_GZz3HLgSHy.docx)
		// submitted on (23/04/2020)\",\"introduction\":\"No introduction
		// available.\",\"matchedWords\":120},{\"scanId\":\"4062bdbc-7f91-482d-b4f4-f36de0af925c\",\"id\":\"7795b19929\",\"title\":\"Copyleaks
		// internal database - Your file (AI_Assignment1_JrzUtGQUP1_2QTSVQuRK6.pdf)
		// submitted on (23/04/2020)\",\"introduction\":\"No introduction
		// available.\",\"matchedWords\":120},{\"scanId\":\"87af317e-e1c3-489b-aab5-2e274fc52049\",\"id\":\"e2bd18e20b\",\"title\":\"Copyleaks
		// internal database - Your file
		// (AI_Assignment1_JrzUtGQUP1_Si9h3BcAq5_ojO7rKihWL.docx) submitted on
		// (22/04/2020)\",\"introduction\":\"No introduction
		// available.\",\"matchedWords\":120},{\"scanId\":\"2fb6e973-d53f-49ca-bfdf-3c3385082b63\",\"id\":\"ed4a7ebd13\",\"title\":\"Copyleaks
		// internal database - Your file (AI_Assignment1_JrzUtGQUP1_2QTSVQuRK6.pdf)
		// submitted on (23/04/2020)\",\"introduction\":\"No introduction
		// available.\",\"matchedWords\":120},{\"scanId\":\"demo123\",\"id\":\"f2711f84d2\",\"title\":\"Copyleaks
		// internal database - Your file (AI_Assignment1_JrzUtGQUP1_2QTSVQuRK6.pdf)
		// submitted on (25/04/2020)\",\"introduction\":\"No introduction
		// available.\",\"matchedWords\":123},{\"scanId\":\"2c95c6f9-492d-4d70-b3bc-4cd11bfe8c1e\",\"id\":\"f35d6ebb1b\",\"title\":\"Copyleaks
		// internal database - Your file (AI_Assignment1_JrzUtGQUP1_2QTSVQuRK6.pdf)
		// submitted on (23/04/2020)\",\"introduction\":\"No introduction
		// available.\",\"matchedWords\":120},{\"scanId\":\"5224f47d-99cd-481a-98b1-199b7c4e93c5\",\"id\":\"fbcc19e0c4\",\"title\":\"Copyleaks
		// internal database - Your file (AI_Assignment1_JrzUtGQUP1_2QTSVQuRK6.pdf)
		// submitted on (23/04/2020)\",\"introduction\":\"No introduction
		// available.\",\"matchedWords\":120}],\"batch\":[],\"score\":{\"identicalWords\":123,\"minorChangedWords\":0,\"relatedMeaningWords\":0,\"aggregatedScore\":100}},\"status\":0,\"error\":null,\"developerPayload\":null}";
		try {
			int internetMatchedCount = 0;
			int databaseMatchedCount = 0;
			long creditUsed = 0;
			int threshold = 0;
			String internetUrl = "";
			String databaseUrl = "";
			if (null != Completed) {
				String assignmentId = customId.split("-")[1];
				String username = customId.split("-")[2];
				String facultyId = customId.split("-")[3];

				logger.info("Completed----->" + Completed);
				JSONObject completedJsonObject = new JSONObject(Completed);

				JSONObject scannedDocument = completedJsonObject.getJSONObject("scannedDocument");
				logger.info("scannedDocument---->" + scannedDocument);

				JSONObject results = completedJsonObject.getJSONObject("results");
				logger.info("results---->" + results);

				JSONArray internet = results.getJSONArray("internet");
				logger.info("internet---->" + internet);

				for (int i = 0; i < internet.length(); i++) {
					JSONObject currObj = internet.getJSONObject(i);
					logger.info("url--->" + currObj.getString("url"));
					logger.info("id--->" + currObj.getString("id"));
					logger.info("title--->" + currObj.getString("title"));
					logger.info("introduction--->" + currObj.getString("introduction"));
					logger.info("matchedWords--->" + currObj.getInt("matchedWords"));
					if (currObj.getInt("matchedWords") > internetMatchedCount) {
						internetMatchedCount = currObj.getInt("matchedWords");
						internetUrl = currObj.getString("url");
					}
				}
				logger.info("internetMatchedCount--->" + internetMatchedCount);
				logger.info("internetUrl--->" + internetUrl);
				JSONArray database = results.getJSONArray("database");
				logger.info("database---->" + database);

//				for(int i=0;i < database.length(); i++) {
//					JSONObject currObj = database.getJSONObject(i);
//					logger.info("scanId--->"+currObj.getString("scanId"));
//					logger.info("id--->"+currObj.getString("id"));
//					logger.info("title--->"+currObj.getString("title"));
//					logger.info("introduction--->"+currObj.getString("introduction"));
//					logger.info("matchedWords--->"+currObj.getInt("matchedWords"));
//					if (currObj.getInt("matchedWords") > databaseMatchedCount) {
//						databaseMatchedCount = currObj.getInt("matchedWords");
//						//databaseUrl = currObj.getString("url");
//					}
//				}
//				logger.info("databaseMatchedCount--->"+databaseMatchedCount);
//				logger.info("databaseUrl--->"+databaseUrl);
//				JSONArray batch = results.getJSONArray("batch");
//				logger.info("batch---->"+batch);
//				for(int i=0;i < database.length(); i++) {
//					JSONObject currObj = database.getJSONObject(i);
//					logger.info("scanId--->"+currObj.getString("scanId"));
//					logger.info("id--->"+currObj.getString("id"));
//					logger.info("title--->"+currObj.getString("title"));
//					logger.info("introduction--->"+currObj.getString("introduction"));
//					logger.info("matchedWords--->"+currObj.getInt("matchedWords"));
//				}
				JSONObject score = results.getJSONObject("score");
				logger.info("score---->" + score);
				logger.info("identicalWords---->" + score.getInt("identicalWords"));
				logger.info("minorChangedWords---->" + score.getInt("minorChangedWords"));
				logger.info("relatedMeaningWords---->" + score.getInt("relatedMeaningWords"));
				logger.info("aggregatedScore---->" + score.getDouble("aggregatedScore"));

				Map<String, Integer> plagValue = calculateCreditUsedAndThreshold(customId, internetMatchedCount);
				if (null != plagValue) {
					creditUsed = plagValue.get("creditsUsed");
					threshold = plagValue.get("threshold");
				}
				if (facultyId.length() > 2) {
					StudentAssignment assignmentSubmission = studentAssignmentService.findAssignmentSubmission(username,
							Long.valueOf(assignmentId));
					if (internet.length() == 0) {
						logger.info("No results.");
						assignmentSubmission.setUrl("Not Copied From Anywhere");
						assignmentSubmission.setThreshold(0);
					} else {
						assignmentSubmission.setUrl(internetUrl);
						assignmentSubmission.setThreshold(threshold);
					}
					studentAssignmentService.update(assignmentSubmission);
					CopyleaksAudit cplAudit = copyleaksAuditService.getRecordByUsername(facultyId,
							Long.valueOf(assignmentId));

					if (cplAudit == null) {
						CopyleaksAudit cpl = new CopyleaksAudit();
						cpl.setAssignmentId(Long.valueOf(assignmentId));
						cpl.setUsername(username);
						cpl.setCount(1);
						cpl.setCreditUsed(creditUsed);
						cpl.setCreatedBy(username);
						cpl.setLastModifiedBy(username);

						copyleaksAuditService.insert(cpl);
					} else {
						cplAudit.setCount(cplAudit.getCount() + 1);
						cplAudit.setCreditUsed(cplAudit.getCreditUsed() + creditUsed);

						copyleaksAuditService.update(cplAudit);
					}
				} else {
					StudentAssignment assignmentSubmissionAudit = studentAssignmentAuditService
							.findAssignmentSubmission(username, Long.valueOf(assignmentId));
					if (internet.length() == 0) {
						logger.info("No results.");
						assignmentSubmissionAudit.setUrl("Not Copied From Anywhere");
						assignmentSubmissionAudit.setThreshold(0);
					} else {
						assignmentSubmissionAudit.setUrl(internetUrl);
						assignmentSubmissionAudit.setThreshold(threshold);
					}
					studentAssignmentAuditService.update(assignmentSubmissionAudit);
					CopyleaksAudit cplAudit = copyleaksAuditService.getRecordByUsername(username,
							Long.valueOf(assignmentId));
					if (cplAudit == null) {
						CopyleaksAudit cpl = new CopyleaksAudit();
						cpl.setAssignmentId(Long.valueOf(assignmentId));
						cpl.setUsername(username);
						cpl.setCount(1);
						cpl.setCreditUsed(creditUsed);
						cpl.setCreatedBy(username);
						cpl.setLastModifiedBy(username);

						copyleaksAuditService.insert(cpl);
					} else {
						cplAudit.setCount(cplAudit.getCount() + 1);
						cplAudit.setCreditUsed(cplAudit.getCreditUsed() + creditUsed);

						copyleaksAuditService.update(cplAudit);

					}

				}
			}
			if (null != Error) {
				logger.info("Error----->" + Error);
			}
			if (null != CreditsChecked) {
				logger.info("CreditsChecked----->" + CreditsChecked);
			}
		} catch (Exception e) {
			logger.error("Error in webhookStatus---->", e);
		}
		return null;
	}

	public Map<String, Integer> calculateCreditUsedAndThreshold(String scanId, int internetMatchedCount) {
		Integer creditsUsed = 0;
		Map<String, Integer> plagValueMap = new HashMap<String, Integer>();
		try {
			// downloads
			Client clientWS = null;
			ClientConfig clientConfig = null;
			clientConfig = new ClientConfig();
			clientWS = ClientBuilder.newClient(clientConfig);
			WebTarget webTarget = null;
			Invocation.Builder invocationBuilder = null;
			Response response = null;
			String accessToken = "";
			int responseCode;

			// 1. login and get access token
			String loginJson = "{\"email\": \"" + copyleaksEmail + "\",\"key\": \"" + copyleaskKey + "\"}";
			webTarget = clientWS.target("https://id.copyleaks.com/v3/account/login/api");
			invocationBuilder = webTarget.request();
			response = invocationBuilder.post(Entity.entity(loginJson.toString(), MediaType.APPLICATION_JSON));
			responseCode = response.getStatus();
			logger.info("response--->" + response);
			if (responseCode == 200) {
				String loginJsonResponse = response.readEntity(String.class);
				logger.info("loginJsonResponse--->" + loginJsonResponse);
				JSONObject loginJsonObject = new JSONObject(loginJsonResponse);
				accessToken = loginJsonObject.getString("access_token");
				// cp.setAccess_token(loginJsonObject.getString("access_token"));
			} else {

			}

			// downloads
			webTarget = clientWS.target("https://api.copyleaks.com/v3/downloads/" + scanId);
			invocationBuilder = webTarget.request().header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
			response = invocationBuilder.get();
			responseCode = response.getStatus();
			if (responseCode == 200) {
				String downloadsResponse = response.readEntity(String.class);
				logger.info("downloadsResponse--->" + downloadsResponse);
				JSONObject downloadJsonObject = new JSONObject(downloadsResponse);
				JSONObject metadataJson = downloadJsonObject.getJSONObject("metadata");
				if (metadataJson.getInt("words") >= 250) {
					creditsUsed = metadataJson.getInt("words") / 250;
					if ((metadataJson.getInt("words") % 250) == 0) {
						plagValueMap.put("creditsUsed", creditsUsed);
					} else {
						plagValueMap.put("creditsUsed", creditsUsed + 1);
					}
				} else {
					plagValueMap.put("creditsUsed", 1);
				}
				int threshold = (internetMatchedCount * 100) / metadataJson.getInt("words");
				plagValueMap.put("threshold", threshold);
			} else if (responseCode == 401) {
				logger.error("Authorization has been denied for this request.");
			}

		} catch (Exception e) {
			logger.error("Error in calculateCreditUsed---->", e);
		}
		return plagValueMap;
	}

	@RequestMapping(value = { "/uploadImageFileAndGenerateLink" }, method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String uploadImageFileAndGenerateLink(@RequestParam("file") MultipartFile file,
			HttpServletResponse resp) {
		String returnMsg = null;
		if (null != file) {
			String fileName = file.getOriginalFilename();
			fileName = fileName.replaceAll(" ", "_");
			logger.info("fileName--->" + fileName);
			try {
				String filePath = baseDirS3 + "/testQuesImages";
				LmsVariables demoBucketName = lmsVariablesService.getLmsVariableBykeyword("demoS3BucketName");
				Map<String, String> s3FileNameMap = amazonS3ClientService
						.uploadFileToS3BucketAndGetUrl(demoBucketName.getValue(), file, filePath, true);
				logger.info("map--->" + s3FileNameMap);
				String objectUrl = "";
				if (s3FileNameMap.containsKey("objectUrl")) {
					objectUrl = s3FileNameMap.get("objectUrl");
				}
				returnMsg = objectUrl;
			} catch (Exception e) {
				returnMsg = "Error in uploading file : " + e.getMessage();
				logger.error("Exception", e);
			}
		}
		return returnMsg;
	}

	@RequestMapping(value = { "/sendNotificationToFacultyForLectureRescheduling" }, method = { RequestMethod.GET,
			RequestMethod.POST })
	public @ResponseBody String sendNotificationToFacultyForLectureRescheduling(@RequestBody String timetableJson,
			HttpServletResponse resp, @RequestHeader Map<String, String> headers) {
		try {
			String encryptedKey = headers.get("token");
			Map<String, Object> map = new HashMap<String, Object>();
			if (authRSA(encryptedKey)) {
				map.put("status", "success");
				logger.info("timetableJson :::::>>>>>> " + timetableJson);
				JSONObject jsonObj = new JSONObject(timetableJson);
				String courseId = jsonObj.optString("courseId");
				String details = jsonObj.optString("details");
				String msg = jsonObj.optString("msg");
				List<String> facultyList = new ArrayList<String>();
				facultyList = userCourseService.getFacultyByCourse(courseId);
				String[] playerId = new String[facultyList.size()];
				List<FacultyLectureReschedule> flrList = new ArrayList<FacultyLectureReschedule>();
				int l = 0;
				for (String f : facultyList) {
					FacultyLectureReschedule bean = new FacultyLectureReschedule();
					bean.setFacultyId(f);
					bean.setDetails(details);
					bean.setMsg(msg);
					bean.setCreatedBy("INFRA_RESCHEDULE");
					String playerid = facultyLectureRescheduleService.getPlayerIdForFaculty(f);
					playerId[l] = playerid;
					flrList.add(bean);
					l++;
					logger.info("playerid ===> " + playerid);
				}
				logger.info("FacultyForLectureReschedule flrList :::::>>>>>> " + flrList);
				facultyLectureRescheduleService.insertBatch(flrList);
				if (playerId.length > 0) {
					sendNotificationToAndroidApp(playerId, msg);
				}
				return "{\"status\":\"success\"}";
			} else {
				map.put("status", "failed");
				map.put("msg", "server key disabled");
			}
			return new Gson().toJson(map);
		} catch (Exception e) {
			logger.error("Exception", e);
			e.printStackTrace();
			return "{\"status\":\"failed\"}";
		}
	}

	private boolean authRSA(String encryptedKey) {
		encryptedKey = RSAConfig.jwtTokenValidator(encryptedKey, JWT_SECRET);
		if (encryptedKey.startsWith("\"")) {
			encryptedKey = encryptedKey.substring(1, encryptedKey.length());
		}
		if (encryptedKey.endsWith("\"")) {
			encryptedKey = encryptedKey.substring(0, encryptedKey.length() - 1);
		}
		boolean result = false;
		try {
			InfraKeys infraKeys = infraKeysService.findKeys();
			if (null != infraKeys) {
				String privateKey = infraKeys.getPrivate_key();
				String decrypted = RSAConfig.decrypt(encryptedKey, privateKey);
				boolean auth = decrypted.trim().equals(infraKeys.getSecret_token());
				logger.info("auth Check==> " + auth);
				result = auth;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public String sendNotificationToAndroidApp(String[] playerId, String message) {
		String jsonResponse = "";
		if (null != playerId && null != message) {
			logger.info("message==============> " + message);
			Client clientWS = null;
			ClientConfig clientConfig = null;
			WebTarget webTarget = null;
			Invocation.Builder invocationBuilder = null;
			Response response = null;
			Map<String, Object> finalMap = new HashMap<String, Object>();
			Map<String, Object> dataMap = new HashMap<String, Object>();
			dataMap.put("foo", "bar");
			Map<String, Object> contentMap = new HashMap<String, Object>();
			contentMap.put("en", message);
			finalMap.put("app_id", "2d46e24e-fb33-43aa-ad6e-fde6940c28ff");
			finalMap.put("include_player_ids", playerId);
			finalMap.put("data", dataMap);
			finalMap.put("contents", contentMap);
			String json = new Gson().toJson(finalMap);
			logger.info("notification_json --> " + json);
			int responseCode;
			clientConfig = new ClientConfig();
			clientWS = ClientBuilder.newClient(clientConfig);
			webTarget = clientWS.target("https://onesignal.com/api/v1/notifications");
			invocationBuilder = webTarget.request().header(HttpHeaders.AUTHORIZATION,
					"MDI5OTI2ZTUtYzQ4Ni00MGU5LTk2YjQtMjI4NzA1OGM0ZjBl");
			response = invocationBuilder.post(Entity.entity(json.toString(), MediaType.APPLICATION_JSON));
			responseCode = response.getStatus();
			logger.info("responseCode: " + responseCode);
			jsonResponse += response.readEntity(String.class);
			logger.info("jsonResponse:\n" + jsonResponse);
			return jsonResponse;
		} else {
			return "{\"jsonResponse\":\"Playerid is null\"}";
		}
	}

	@RequestMapping(value = "/getFacultyOfCourseFromOtherSchool", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String getFacultyOfCourseFromOtherSchool(@RequestBody String contentJson) {
		try {
			Content cJson = mapper.readValue(contentJson, new TypeReference<Content>() {
			});
			List<User> facultyList = userService.getFacultyByCourse(cJson.getCourseId());
			List<Map<String, String>> res = new ArrayList<Map<String, String>>();

			ObjectMapper mapper = new ObjectMapper();
			String result = "";
			result = mapper.writeValueAsString(facultyList);
			logger.info("result------->" + result);

			return result;
		} catch (Exception ex) {

			logger.error("Exception", ex);
			return "";
		}
	}
	
	@RequestMapping(value = "/getProgramCampusOfSchool", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String getProgramCampusOfSchool() {
		try {
			List<User> campusList = userService.findCampus();
			List<Map<String, String>> res = new ArrayList<Map<String, String>>();

			for (User c : campusList) {
				Map<String, String> returnMap = new HashMap();
				returnMap.put(String.valueOf(c.getCampusId()), c.getCampusName());
				res.add(returnMap);
			}
			ObjectMapper mapper = new ObjectMapper();
			String result = "";
			result = mapper.writeValueAsString(res);
			logger.info("result------->" + result);

			return result;
		} catch (Exception ex) {

			logger.error("Exception", ex);
			return "";
		}
	}

}

