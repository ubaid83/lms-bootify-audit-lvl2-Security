package com.spts.lms.services.dashboard;

import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spts.lms.beans.announcement.Announcement;
import com.spts.lms.beans.assignment.Assignment;
import com.spts.lms.beans.calender.Calender;
import com.spts.lms.beans.course.Course;
import com.spts.lms.beans.course.UserCourse;
import com.spts.lms.beans.dashboard.DashBoard;
import com.spts.lms.beans.message.Message;
import com.spts.lms.beans.pending.PendingTask;
import com.spts.lms.beans.test.PendingTest;
import com.spts.lms.beans.user.Role;
import com.spts.lms.beans.user.User;
import com.spts.lms.services.announcement.AnnouncementService;
import com.spts.lms.services.assignment.AssignmentService;
import com.spts.lms.services.calender.CalenderService;
import com.spts.lms.services.course.CourseService;
import com.spts.lms.services.course.UserCourseService;
import com.spts.lms.services.message.MessageService;
import com.spts.lms.services.test.StudentTestService;
import com.spts.lms.services.user.UserService;
import com.spts.lms.web.utils.Utils;

@Service
public class DashboardService {

	@Autowired
	CourseService courseService;

	@Autowired
	MessageService messageService;

	@Autowired
	CalenderService calendarService;

	@Autowired
	UserCourseService userCourseService;

	@Autowired
	AssignmentService assignmentService;

	@Autowired
	StudentTestService studentTestService;

	@Autowired
	AnnouncementService announcementService;

	@Autowired
	UserService userService;

	private static final Logger logger = Logger
			.getLogger(DashboardService.class);

	public List<Message> getReceivedMessage(String userName) {
		return messageService.getReceivedMessage(userName);
	}

	public List<Announcement> listOfAnnouncementsForCourseList(String userName,
			Long courseId, String acadSession, Long programId) {

		Set<Long> listOfCourseId = new HashSet<Long>();
		List<Announcement> announceList = null;
		List<Announcement> acadSessionList = null;
		if (courseId != null) {
			listOfCourseId.add(courseId);
			if (!listOfCourseId.isEmpty()) {
				announceList = announcementService
						.getAnnouncementsByCoursesList(listOfCourseId, userName);
			}
		} else {
			List<UserCourse> listOfCourseForUser = userCourseService
					.findByUsername(userName);
			listOfCourseForUser
					.forEach(s -> listOfCourseId.add(s.getCourseId()));
			if (!listOfCourseId.isEmpty()) {
				if (acadSession == null || acadSession.isEmpty()) {
					logger.info("getAnnouncementsByCoursesAndInstitute");
					announceList = announcementService
							.getAnnouncementsByCoursesAndInstitute(
									listOfCourseId, userName,
									String.valueOf(programId));
				} else {
					logger.info("getAnnouncementsByCoursesAndAcadSessionAndInstitute");
					announceList = announcementService
							.getAnnouncementsByCoursesAndAcadSessionAndInstitute(
									listOfCourseId, userName,
									String.valueOf(programId), acadSession);
				}
			}
		}
		User u = userService.findByUserName(userName);
		if(!Utils.IsNullOrEmpty(announceList)) {
		for (Announcement a : announceList) {
			logger.info("Announcement a ---- "+a);
			if (u.getCampusId() != null) {

				if (a.getCampusId() != null) {
					logger.info("Announcement campus id is not null");
					logger.info("users campus id--------------- "
							+ u.getCampusId());
					logger.info("announcement campus id--------------- "
							+ a.getCampusId());

					if (!u.getCampusId().equals(a.getCampusId())) {
						logger.info("Campus ids are not same"); //
						Integer index = announceList.indexOf(a);
						logger.info("Index of a ------- " + index);
						announceList.remove(index);
						announceList.set(index, null);
					} else {
						logger.info("ELSE");
						announceList.set(announceList.indexOf(a), a);
					}
				} else {

					announceList.set(announceList.indexOf(a), a);
				}
			} else {
				announceList.set(announceList.indexOf(a), a);
			}
		}
		announceList.removeAll(Collections.singleton(null));
		}
		/*
		 * if (acadSession == null) {
		 * 
		 * } else { acadSessionList = announcementService
		 * .getAnnouncementsByAcadSessionYear(programId, acadSession);
		 * 
		 * announceList.addAll(acadSessionList);
		 * 
		 * }
		 */

		return announceList == null ? new ArrayList<Announcement>()
				: announceList;
	}

	public List<PendingTask> getToDoEveryday(String username, String role) {

		boolean isFaculty = "ROLE_FACULTY".equals(role);
		List<Assignment> assignments = assignmentService.findByUserPending(
				username, isFaculty);

		List<PendingTask> pendingTasks = new ArrayList<PendingTask>();
		List<String> ids = new ArrayList<String>();
		try {
			for (Assignment ass : assignments) {
				if (isFaculty && ids.contains("" + ass.getId()))
					continue;
				PendingTask task = new PendingTask();
				task.setTaskName(ass.getAssignmentName());
				task.setType("Assigment");

				// Instant instant = Instant.parse(ass.getStartDate() + "Z");
				// Date startDate = java.util.Date.from(instant);

				// instant = Instant.parse(ass.getEndDate() + "Z");

				SimpleDateFormat format1 = new SimpleDateFormat(
						"yyyy-MM-dd'T'HH:mm:ss");
				Date startDate = format1.parse(ass.getStartDate());

				Date endDate = format1.parse(ass.getEndDate());
				// Date endDate = java.util.Date.from(instant);
				task.setStartDate(startDate);
				task.setEndDate(endDate);
				task.setId("" + ass.getId());
				ids.add("" + ass.getId());
				pendingTasks.add(task);
			}
		} catch (ParseException pe) {
			logger.info("Parsing error!");
		}
		ids.clear();

		if (role.equalsIgnoreCase("ROLE_STUDENT")) {

			List<PendingTest> pendingTest = studentTestService
					.getPendingTest(username);

			for (PendingTest pt : pendingTest) {

				PendingTask task = new PendingTask();
				task.setTaskName(pt.getTestName());
				task.setType("Test");
				/* task.setDueDate(pt.getDueDate()); */
				task.setStartDate(pt.getStartDate());
				task.setEndDate(pt.getEndDate());
				task.setId("" + pt.getTestId());
				pendingTasks.add(task);

			}
		}
		return pendingTasks;

	}

	public List<Announcement> listOfAnnouncementsForCourseListMenu(
			String userName, Long courseId) {

		Set<Long> listOfCourseId = new HashSet<Long>();

		if (courseId != null) {
			listOfCourseId.add(courseId);
		} else {
			List<UserCourse> listOfCourseForUser = userCourseService
					.findByUsername(userName);
			listOfCourseForUser
					.forEach(s -> listOfCourseId.add(s.getCourseId()));
		}

		List<Announcement> announceList = null;
		if (!listOfCourseId.isEmpty())
			announceList = announcementService.getAnnouncementsByCoursesList(
					listOfCourseId, userName);

		if (announceList != null)
			for (Announcement a : announceList) {

				String description = a.getDescription();
				if (description != null) {
					Document doc = Jsoup.parse(description);
					String text = doc.body().text();
					a.setDescription(text);
				}
			}
		return announceList == null ? new ArrayList<Announcement>()
				: announceList;
	}

	public List<Calender> getToDoList(String userName) {
		return calendarService.listOfEventsForUser(userName);
	}

	public List<Calender> getToDoListByCourse(String userName, Long courseId) {

		Set<Long> listOfCourseId = new HashSet<Long>();

		if (courseId != null) {
			listOfCourseId.add(courseId);
		} else {
			List<UserCourse> listOfCourseForUser = userCourseService
					.findByUsername(userName);
			listOfCourseForUser
					.forEach(s -> listOfCourseId.add(s.getCourseId()));
		}

		List<Calender> calebdarEventList = null;
		if (!listOfCourseId.isEmpty())
			calebdarEventList = calendarService.listOfEventsForUserByCourse(
					userName, listOfCourseId);
		return calebdarEventList == null ? new ArrayList<Calender>()
				: calebdarEventList;
	}

	public List<PendingTask> getToDoEveryday(String username, String role,
			Long programId) {

		boolean isFaculty = "ROLE_FACULTY".equals(role);
		List<Assignment> assignments = assignmentService.findByUserPending(
				username, isFaculty, programId);

		List<PendingTask> pendingTasks = new ArrayList<PendingTask>();
		List<String> ids = new ArrayList<String>();
		try {
			for (Assignment ass : assignments) {
				if (isFaculty && ids.contains("" + ass.getId()))
					continue;
				PendingTask task = new PendingTask();
				task.setTaskName(ass.getAssignmentName());
				task.setType("Assigment");

				// Instant instant = Instant.parse(ass.getStartDate() + "Z");
				// Date startDate = java.util.Date.from(instant);

				// instant = Instant.parse(ass.getEndDate() + "Z");

				SimpleDateFormat format1 = new SimpleDateFormat(
						"yyyy-MM-dd'T'HH:mm:ss");
				Date startDate = new Date();
				Date endDate = new Date();

				if (ass.getStartDate() != null) {
					startDate = format1.parse(ass.getStartDate());
				} else {
					continue;
				}
				if (ass.getEndDate() != null) {
					endDate = format1.parse(ass.getEndDate());
				} else {
					continue;
				}
				// Date endDate = java.util.Date.from(instant);
				task.setStartDate(startDate);
				task.setEndDate(endDate);
				task.setId("" + ass.getId());
				ids.add("" + ass.getId());
				pendingTasks.add(task);
			}
		} catch (ParseException pe) {
			logger.info("Parsing error!");
		}
		ids.clear();

		if (role.equalsIgnoreCase("ROLE_STUDENT")) {

			List<PendingTest> pendingTest = studentTestService
					.getPendingTest(username);

			for (PendingTest pt : pendingTest) {

				PendingTask task = new PendingTask();
				task.setTaskName(pt.getTestName());
				task.setType("Test");
				/* task.setDueDate(pt.getDueDate()); */
				if (pt.getStartDate() != null) {
					task.setStartDate(pt.getStartDate());
				} else {
					continue;
				}
				if (pt.getEndDate() != null) {
					task.setEndDate(pt.getEndDate());
				} else {
					continue;
				}
				task.setId("" + pt.getTestId());
				pendingTasks.add(task);

			}
		}
		return pendingTasks;

	}

	public List<PendingTask> getToDoEveryday(String username, String role,
			String courseId) {

		boolean isFaculty = "ROLE_FACULTY".equals(role);
		List<Assignment> assignments = assignmentService
				.findByUserPendingByCourse(username, isFaculty, courseId);
		List<PendingTask> pendingTasks = new ArrayList<PendingTask>();
		List<String> ids = new ArrayList<String>();
		try {
			for (Assignment ass : assignments) {
				if (isFaculty && ids.contains("" + ass.getId()))
					continue;
				PendingTask task = new PendingTask();
				task.setTaskName(ass.getAssignmentName());
				task.setType("Assigment");

				// Instant instant = Instant.parse(ass.getStartDate() + "Z");
				// Date startDate = java.util.Date.from(instant);

				// instant = Instant.parse(ass.getEndDate() + "Z");

				// Date endDate = java.util.Date.from(instant);

				SimpleDateFormat format1 = new SimpleDateFormat(
						"yyyy-MM-dd'T'HH:mm:ss");
				Date startDate = new Date();
				Date endDate = new Date();
				if (ass.getStartDate() != null) {
					startDate = format1.parse(ass.getStartDate());
				} else {
					continue;
				}
				if (ass.getEndDate() != null) {
					endDate = format1.parse(ass.getEndDate());
				} else {
					continue;
				}
				task.setStartDate(startDate);
				task.setEndDate(endDate);
				task.setId("" + ass.getId());
				ids.add("" + ass.getId());
				pendingTasks.add(task);
			}
		} catch (ParseException pe) {
			logger.info("Parsing error!");
		}
		ids.clear();

		if (role.equalsIgnoreCase("ROLE_STUDENT")) {
			List<PendingTest> pendingTest = studentTestService
					.getPendingTestByCourse(username, courseId);

			for (PendingTest pt : pendingTest) {
				PendingTask task = new PendingTask();
				task.setTaskName(pt.getTestName());
				task.setType("Test");
				/* task.setDueDate(pt.getDueDate()); */
				if (pt.getStartDate() != null) {
					task.setStartDate(pt.getStartDate());
				} else {
					continue;
				}
				if (pt.getEndDate() != null) {
					task.setEndDate(pt.getEndDate());
				} else {
					continue;
				}
				task.setId("" + pt.getTestId());
				pendingTasks.add(task);
			}
		}
		return pendingTasks;

	}

	public List<DashBoard> listOfDashBoardElements(String userName,
			String programName, List<Course> courseList) {

		String acadYear = "";
		String acadMonth = "";
		logger.info("userName---" + userName);
		logger.info("programName---" + programName);
		List<Course> courseDB = courseList;
		Map<String, Integer> assigmentMap = new HashMap<String, Integer>();
		Map<String, Integer> testMap = new HashMap<String, Integer>();
		Map<String, Integer> announcementMap = new HashMap<String, Integer>();
		List<Assignment> pendingAssignmentDB = assignmentService
				.findPendingAssignmentCount(userName);
		for (Assignment ass : pendingAssignmentDB) {
			assigmentMap.put(ass.getCourseId() + "", ass.getCount());
		}
		logger.info("---------assignmentmap---------->>>"
				+ assigmentMap.toString());
		List<PendingTest> pendingTestDB = studentTestService
				.getPendingTestCountDashboard(userName);
		for (PendingTest pt : pendingTestDB) {
			testMap.put(pt.getCourseId() + "", pt.getCount());
		}
		logger.info("---------testmap---------->>>" + testMap.toString());
		List<Announcement> announcemnetDB = announcementService
				.getAnnouncementCountDashboard(userName);
		for (Announcement Anoun : announcemnetDB) {
			announcementMap.put(Anoun.getCourseId() + "", Anoun.getCount());
		}
		logger.info("---------announcementmap---------->>>"
				+ announcementMap.toString());

		List<DashBoard> dashBoradItems = new ArrayList<DashBoard>();
		if (courseDB != null)
			for (Course c : courseDB) {
				DashBoard item = new DashBoard();
				item.setCourse(c);
				if (assigmentMap.containsKey(c.getId() + ""))
					item.setPendingAssigmentCount(assigmentMap.get(String
							.valueOf(c.getId())));
				else
					item.setPendingAssigmentCount(0);

				if (testMap.containsKey(c.getId() + ""))
					item.setPendingTestCount(testMap.get(String.valueOf(c
							.getId())));
				else
					item.setPendingTestCount(0);

				if (announcementMap.containsKey(c.getId() + ""))
					item.setAnnouncementCount(announcementMap.get(String
							.valueOf(c.getId())));
				else
					item.setAnnouncementCount(0);

				dashBoradItems.add(item);
			}
		logger.info("dashBoradItems size :" + dashBoradItems.size());
		return dashBoradItems;
	}

	public List<DashBoard> listOfDashBoard() {
		List<Course> courseList = courseService.findAllActive();

		logger.info("under dashboard elements");
		List<DashBoard> dashBoradItem = new ArrayList<DashBoard>();
		if (courseList != null)
			for (Course c : courseList) {
				DashBoard item = new DashBoard();
				item.setCourse(c);

				dashBoradItem.add(item);
			}
		return dashBoradItem;
	}

	public List<DashBoard> listOfDashBoardElements(String userName) {

		String acadYear = "";
		String acadMonth = "";
		List<Course> courseDB = courseService.findByUserActive(userName);
		Map<String, Integer> assigmentMap = new HashMap<String, Integer>();
		Map<String, Integer> testMap = new HashMap<String, Integer>();
		Map<String, Integer> announcementMap = new HashMap<String, Integer>();

		List<Assignment> pendingAssignmentDB = assignmentService
				.findPendingAssignmentCount(userName);
		for (Assignment ass : pendingAssignmentDB) {
			assigmentMap.put(ass.getCourseId() + "", ass.getCount());
		}

		logger.info("---------assignmentmap---------->>>"
				+ assigmentMap.toString());

		List<PendingTest> pendingTestDB = studentTestService
				.getPendingTestCountDashboard(userName);

		for (PendingTest pt : pendingTestDB) {

			testMap.put(pt.getCourseId() + "", pt.getCount());
		}
		logger.info("---------testmap---------->>>" + testMap.toString());

		List<Announcement> announcemnetDB = announcementService
				.getAnnouncementCountDashboard(userName);

		for (Announcement Anoun : announcemnetDB) {

			announcementMap.put(Anoun.getCourseId() + "", Anoun.getCount());
		}
		logger.info("---------announcementmap---------->>>"
				+ announcementMap.toString());

		List<DashBoard> dashBoradItems = new ArrayList<DashBoard>();
		if (courseDB != null)
			for (Course c : courseDB) {
				DashBoard item = new DashBoard();
				item.setCourse(c);
				if (assigmentMap.containsKey(c.getId() + ""))
					item.setPendingAssigmentCount(assigmentMap.get(String
							.valueOf(c.getId())));
				else
					item.setPendingAssigmentCount(0);

				if (testMap.containsKey(c.getId() + ""))
					item.setPendingTestCount(testMap.get(String.valueOf(c
							.getId())));
				else
					item.setPendingTestCount(0);

				if (announcementMap.containsKey(c.getId() + ""))
					item.setAnnouncementCount(announcementMap.get(String
							.valueOf(c.getId())));
				else
					item.setAnnouncementCount(0);

				dashBoradItems.add(item);
			}
		logger.info("dashBoradItems size :" + dashBoradItems.size());
		return dashBoradItems;
	}
}