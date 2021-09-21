package com.spts.lms.web.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.EventReminder;
import com.google.api.services.calendar.model.Events;
import com.google.gson.Gson;
import com.spts.lms.auth.Token;
import com.spts.lms.beans.calender.Calender;
import com.spts.lms.beans.calender.StudentEvent;
import com.spts.lms.beans.course.Course;
import com.spts.lms.beans.user.Role;
import com.spts.lms.beans.user.User;
import com.spts.lms.services.calender.CalenderService;
import com.spts.lms.services.calender.StudentEventService;
import com.spts.lms.services.course.CourseService;
import com.spts.lms.services.course.UserCourseService;
import com.spts.lms.services.user.UserService;
import com.spts.lms.web.helper.WebPage;
import com.spts.lms.web.utils.Utils;

@Secured("ROLE_USER")
@Controller
@SessionAttributes("userId")
public class CalenderController extends BaseController {

	@Autowired
	CalenderService calenderService;

	@Autowired
	CourseService courseService;

	@Autowired
	UserCourseService userCourseService;

	@Autowired
	UserService userService;
	@Autowired
	StudentEventService studentEventService;

	@Value("${googleCredFile}")
	String googleCredFile;

	@Value("${googleCredJson}")
	String googleCredJson;

	static SimpleDateFormat parseFormat = new SimpleDateFormat(
			"yyyy-MM-dd hh:mm:ss.S");

	static SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
	// -------------------
	/** Application name. */
	private static final String APPLICATION_NAME = "Google Calendar API Java Quickstart";

	/** Global instance of the {@link FileDataStoreFactory}. */
	private static FileDataStoreFactory DATA_STORE_FACTORY = null;

	/** Global instance of the JSON factory. */
	private static final JsonFactory JSON_FACTORY = JacksonFactory
			.getDefaultInstance();

	/** Global instance of the HTTP transport. */
	private static HttpTransport HTTP_TRANSPORT = null;

	/**
	 * Global instance of the scopes required by this quickstart.
	 *
	 * If modifying these scopes, delete your previously saved credentials at
	 * ~/.credentials/calendar-java-quickstart
	 */
	private static final List<String> SCOPES = Arrays
			.asList(CalendarScopes.CALENDAR);

	void initFolder() {

		if (HTTP_TRANSPORT == null) {
			try {
				/* logger.info("googleCredFile" + googleCredFile); */
				HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
				DATA_STORE_FACTORY = new FileDataStoreFactory(new java.io.File(
						googleCredFile));
			} catch (Throwable t) {

				logger.error("Ëxception e", t);
			}
		}
	}

	/**
	 * Creates an authorized Credential object.
	 * 
	 * @return an authorized Credential object.
	 * @throws IOException
	 */
	public Credential authorize() throws IOException {
		// Load client secrets.
		InputStream in = new FileInputStream(googleCredJson + File.separator
				+ "client_secret.json");
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(
				JSON_FACTORY, new InputStreamReader(in));

		// Build flow and trigger user authorization request.
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
				HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)

		.setDataStoreFactory(DATA_STORE_FACTORY).setAccessType("offline")
				.build();
		File DATA_STORE_DIR = new java.io.File(googleCredFile);
		Credential credential = new AuthorizationCodeInstalledApp(flow,
				new LocalServerReceiver()).authorize("user");
		
		return credential;
	}

	/**
	 * Build and return an authorized Calendar client service.
	 * 
	 * @return an authorized Calendar client service
	 * @throws IOException
	 */
	public com.google.api.services.calendar.Calendar getCalendarService()
			throws IOException {
		initFolder();

		Credential credential = authorize();
		return new com.google.api.services.calendar.Calendar.Builder(
				HTTP_TRANSPORT, JSON_FACTORY, credential).setApplicationName(
				APPLICATION_NAME).build();
	}

	// -------------------

	private static final Logger logger = Logger
			.getLogger(CalenderController.class);

	@ModelAttribute("courseList")
	public List<Course> getCourseList() {
		return courseService.findAllActive();
	}

	@RequestMapping(value = "/addCalenderEventForm", method = RequestMethod.GET)
	public String addCourseForm(
			@RequestParam(required = false) String courseId,
			@RequestParam(required = false) Long id, Model m,
			@ModelAttribute Calender calender, Principal principal) {
		
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);
		

		String acadSession = u.getAcadSession();
		
		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		m.addAttribute("webPage", new WebPage("addCalenderEvent",
				"Add a new Event", false, false));
		// Calender calenderDB = calenderService.findByID(calendarEventId);
		if (id != null) {

			
			calender = calenderService.findByID(id);
			calender.setStartDateToFormat(calender.getStartDate());
			calender.setEndDateToFormat(calender.getEndDate());
			m.addAttribute("edit", "true");

			if (calender == null) {
				setError(m, "Test " + id + " does not exist");
				calender.setId(null);
				return "calender/addCalenderEvent";

			}

		} else {
			
		}

		m.addAttribute("calender", calender);
		return "calender/addCalenderEvent";

	}

	@RequestMapping(value = "/addCalenderEvent", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String addFeedback(@ModelAttribute Calender calender,
			RedirectAttributes redirectAttrs, Principal principal, Model m) {
		try {
			String username = principal.getName();

			Token userdetails1 = (Token) principal;
			String ProgramName = userdetails1.getProgramName();
			User u = userService.findByUserName(username);
			

			String acadSession = u.getAcadSession();
			
			m.addAttribute("Program_Name", ProgramName);
			m.addAttribute("AcadSession", acadSession);
			calender.setCreatedBy(username);
			calender.setLastModifiedBy(username);
			calender.setUsername(username);
			calender.setActive("Y");
			

			calenderService.insertWithIdReturn(calender);
			redirectAttrs.addFlashAttribute("calender", calender);
			setSuccess(redirectAttrs, "Event added successfully");

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(redirectAttrs, "Error in adding Event");
			return "redirect:/viewEvents";
		}
		return "redirect:/viewEvents";
	}

	@RequestMapping(value = "/addCalenderEvent1", method = { RequestMethod.GET,
			RequestMethod.POST })
	public @ResponseBody String addFeedback1(@ModelAttribute Calender calender,
			@RequestBody Calender calender1, RedirectAttributes redirectAttrs,
			@RequestParam Integer programId, Principal principal) {
		try {

			String username = principal.getName();

			calender1.setUsername(username);
			calender1.setCreatedBy(username);
			calender1.setLastModifiedBy(username);
			calender1.setUsername(username);

			calenderService.insertWithIdReturn(calender1);

			setSuccess(redirectAttrs, "Event added successfully");

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(redirectAttrs, "Error in adding Event");
			return "redirect:/viewEvents";
		}
		return "redirect:/viewEvents";
	}

	@RequestMapping(value = "/updateCalenderEvent", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String updateCalenderEvent(@ModelAttribute Calender calender,
			Principal principal, Model m, RedirectAttributes redirectAttrs) {
		m.addAttribute("webPage", new WebPage("addAnnouncement",
				"Calender Event Details", false, false));
		Map<String, Object> mapOfCalenderParam = new HashMap<>();
		try {

			String username = principal.getName();

			Token userdetails1 = (Token) principal;
			String ProgramName = userdetails1.getProgramName();
			User u = userService.findByUserName(username);
			

			String acadSession = u.getAcadSession();
			
			m.addAttribute("Program_Name", ProgramName);
			m.addAttribute("AcadSession", acadSession);

			Calender calenderDB = calenderService.findByID(calender.getId());

			calender.setTitle(calender.getEvent());
			calender.setStart(calender.getStartDate());
			calender.setEnd(calender.getEndDate());
			calender.setShowStatus(calender.getShowStatus());
			calender.setUsername(username);
			calender.setCreatedBy(calenderDB.getCreatedBy());
			calender.setCreatedDate(calenderDB.getCreatedDate());
			calender.setActive("Y");

			calender.setLastModifiedBy(username);
			Date dt = Utils.getInIST();
			calender.setLastModifiedDate(dt);
			calenderService.update(calender);

			setSuccess(redirectAttrs, "Calender Event updated successfully");
			m.addAttribute("calender", calender);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(m, "Error in updating Event");
			m.addAttribute("calender", calender);
			return "redirect:/viewEvents";
		}
		return "redirect:/viewEvents";
	}

	@RequestMapping(value = "/viewEvents", method = RequestMethod.GET)
	public String viewEvents(@RequestParam(required = false) String courseId,
			Model m, Principal principal, @ModelAttribute Calender calender) {
		
		m.addAttribute("webPage", new WebPage("addCourse", "Add a new Course",
				false, false));
		Token userDetails = (Token) principal;
		String userName = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(userName);
		

		String acadSession = u.getAcadSession();
		
		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		StringBuffer buffer = new StringBuffer();
		String json = "";
		List<Calender> getAllEvents = null;
		List<Calender> eventsToBeConvertedToJson = new ArrayList<Calender>();
		Course course = new Course();
		if (!StringUtils.isEmpty(courseId)) {
			course = courseService.findByID(Long.parseLong(courseId));
			getAllEvents = calenderService.getAllEventsCourseWise(userName,
					courseId);
			m.addAttribute("edit", "true");
		} else if (userDetails.getAuthorities().contains(Role.ROLE_ADMIN)) {

			

			getAllEvents = calenderService.getAllEventsForAdmin(userName,
					Long.parseLong(userDetails.getProgramId()));

		} else {
			getAllEvents = calenderService.getAllEvents(userName,
					Long.parseLong(userDetails.getProgramId()));
		}
		buffer.append("[");
		for (Calender c : getAllEvents) {
			c.setEnd(c.getEndDate());
			c.setStart(c.getStartDate());
			c.setStartDateTFormat(c.getStartDate());
			c.setEndDateTFormat(c.getEndDate());
			c.setShowStatus(c.getShowStatus());
			buffer.append(c).append(",");
		}
		buffer.append("]");
		json = StringUtils.removeEnd(buffer.toString(), "");

	

		
		/* logger.info("JSON **" + json); */

		m.addAttribute("json", json);
		m.addAttribute("calender", calender);
		if (userDetails.getAuthorities().contains(Role.ROLE_FACULTY)
				|| userDetails.getAuthorities().contains(Role.ROLE_STUDENT)) {

			m.addAttribute("courses", courseService.findByUserActive(
					principal.getName(), userDetails.getProgramName()));
		}

		if (userDetails.getAuthorities().contains(Role.ROLE_ADMIN)) {
			String progName = userDetails.getProgramName();
			m.addAttribute("courses",
					courseService.findByCoursesBasedOnProgramName(progName));
		}
		// m.addAttribute("courses",courseService.findByUserActive(principal.getName()));

		return "calender/viewCalenderForm";
	}

	@RequestMapping(value = "/viewCalenderEventDetails", method = RequestMethod.GET)
	public String viewCalenderEventDetails(@ModelAttribute Calender calender,
			@RequestParam(required = false) String eventId, Principal p, Model m) {
		m.addAttribute("webPage", new WebPage("viewTest", "Test Details", true,
				false));
		try {
			UsernamePasswordAuthenticationToken userDetails = (UsernamePasswordAuthenticationToken) p;

			Calender calenderFromDb = calenderService.findByID(Long
					.valueOf(eventId));
			
			Course course = courseService.findByID(Long.valueOf(calenderFromDb
					.getCourseId()));
			m.addAttribute("courseName", course.getCourseName());
			m.addAttribute("calender", calenderFromDb);

			StudentEvent studentEvent = studentEventService
					.findByEventIdAndUsername(eventId, p.getName());

			List<StudentEvent> studentsForEvents = studentEventService
					.getStudentForEvents(calenderFromDb.getId(),
							Long.valueOf(calenderFromDb.getCourseId()));

			m.addAttribute("studentsForEvents", studentsForEvents);

			m.addAttribute("eventId", eventId);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(m, "Error in updating Test");
			return "calender/eventDetails";
		}
		return "calender/eventDetails";
	}

	@RequestMapping(value = "/saveStudentEvents", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String saveStudentEvents(@ModelAttribute Calender calender,
			RedirectAttributes redirectAttr, Principal principal, Model m) {
		String username = principal.getName();
		redirectAttr.addFlashAttribute("calender", calender);

		try {
			
			// logger.info("event id---"+eventId);
			Calender calenderFromDb = calenderService.findByID(Long
					.valueOf(calender.getId()));

			List<StudentEvent> studentsForEvents = studentEventService
					.getStudentForEvents(calenderFromDb.getId(),
							Long.valueOf(calenderFromDb.getCourseId()));

			calender.setStudentEvents(studentsForEvents);

			for (StudentEvent studentEvent : calender.getStudentEvents()) {

				studentEvent.setCreatedBy(username);
				studentEvent.setLastModifiedBy(username);
				User user = userService.findByUserName(studentEvent
						.getUsername());
				studentEvent.setEventId(String.valueOf(calenderFromDb.getId()));
				studentEvent.setUser_email(user.getEmail());

				studentEvent
						.setCourseId(calenderFromDb.getCourseId() == null ? ""
								: calenderFromDb.getCourseId() + "");
			}
			m.addAttribute("calender", calender);
			m.addAttribute("eventId", calender.getId());
			studentEventService.insertBatch(calender.getStudentEvents());

			List<StudentEvent> studentEvents = studentEventService
					.getStudentsByEventId(calender.getId());
			
			Calender calenderDB = calenderService.findByID(calender.getId());

			// --------- google Event code-------------

			com.google.api.services.calendar.Calendar service = getCalendarService();

			// List the next 10 events from the primary calendar.
			DateTime now = new DateTime(System.currentTimeMillis());
			Events events = service.events().list("primary").setMaxResults(10)
					.setTimeMin(now).setOrderBy("startTime")
					.setSingleEvents(true).execute();
			List<Event> items = events.getItems();
			if (items.size() == 0) {
				
			} else {
				
				for (Event event : items) {
					DateTime start = event.getStart().getDateTime();
					if (start == null) {
						start = event.getStart().getDate();
					}
					

				}
			}
			// ---------------------------------

			Event event = new Event()
					.setSummary(calenderDB.getEvent())
					.setLocation(
							"NMIMS Mumbai, Gulmohar Road, Mumbai, Maharashtra, India")
					.setDescription(calenderDB.getDescription());

			Calendar cal = Calendar.getInstance();
			Calendar cal2 = Calendar.getInstance();

			Date date = parseFormat.parse(calenderDB.getStartDate());
			cal.setTime(date);
			cal.add(Calendar.MINUTE, -330);
			String startDate = sdf2.format(cal.getTimeInMillis());

			Date date2 = parseFormat.parse(calenderDB.getEndDate());
			cal2.setTime(date2);
			cal2.add(Calendar.MINUTE, -330);
			String endDate = sdf2.format(cal2.getTimeInMillis());

			// "2017-07-21T09:00:00-07:00"

			// -------------
			/*
			 * DateFormat utcFormat = new
			 * SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
			 * utcFormat.setTimeZone(TimeZone.getTimeZone("IST")); DateFormat
			 * indianFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
			 * utcFormat.setTimeZone(TimeZone.getTimeZone("GMT")); Date
			 * timestamp = utcFormat.parse("2017-07-16T02:00:00"); String output
			 * = indianFormat.format(timestamp);
			 * 
			 * logger.info("-----IST --- OUTPUT ---->>>"+output);
			 */
			// --------------

			DateTime startDateTime = new DateTime(startDate);
		
			EventDateTime start = new EventDateTime()
					.setDateTime(startDateTime).setTimeZone("Asia/Kolkata");

			event.setStart(start);

			DateTime endDateTime = new DateTime(endDate);
			EventDateTime end = new EventDateTime().setDateTime(endDateTime)
					.setTimeZone("Asia/Kolkata");
			event.setEnd(end);

			String[] recurrence = new String[] { "RRULE:FREQ=DAILY;COUNT=1" };
			event.setRecurrence(Arrays.asList(recurrence));

			/*
			 * EventAttendee[] attendees = new EventAttendee[] { new
			 * EventAttendee().setEmail("milind.betkar.07@gmail.com"), new
			 * EventAttendee().setEmail("gadeakshay94@gmail.com"),
			 * };event.setAttendees(Arrays.asList(attendees));
			 */
			EventAttendee[] attendees;

			EventAttendee emailList = null;
			for (StudentEvent se : studentEvents) {
				

				if (!StringUtils.isEmpty(se.getUser_email())
						&& !StringUtils.isWhitespace(se.getUser_email())) {

					emailList = new EventAttendee()
							.setEmail(se.getUser_email());

				}
				// emailList = new EventAttendee().setEmail(se.getUser_email());

				// attendees[studentEvents.indexOf(se)] = new
				// EventAttendee().setEmail(se.getUser_email());
			}
			attendees = new EventAttendee[] { emailList };

			

			event.setAttendees(Arrays.asList(attendees));

			EventReminder[] reminderOverrides = new EventReminder[] {
					new EventReminder().setMethod("email").setMinutes(24 * 60),
					new EventReminder().setMethod("popup").setMinutes(10), };
			Event.Reminders reminders = new Event.Reminders().setUseDefault(
					false).setOverrides(Arrays.asList(reminderOverrides));
			event.setReminders(reminders);

			String calendarId = "primary";
			event = service.events().insert(calendarId, event).execute();
			

			// --------- google Event code End -------------

			setSuccess(redirectAttr,
					"Events allocated to Students successfully");

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(redirectAttr, "Error in allocating feedback");
			return "redirect:/viewEvents";
		}
		return "redirect:/viewEvents";
	}

/*	@RequestMapping(value = "/deleteCalenderEvent", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String deleteCourse(@RequestParam Integer programId,
			RedirectAttributes redirectAttrs) {
		try {
			calenderService.deleteSoftById(String.valueOf(programId));
			setSuccess(redirectAttrs, "Event deleted successfully");

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(redirectAttrs, "Error in deleting Event.");
		}

		return "redirect:/viewEvents";
	}*/
	
	@RequestMapping(value = "/deleteCalenderEvent", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String deleteCalenderEvent(@ModelAttribute Calender calender,
			Principal principal, Model m, RedirectAttributes redirectAttrs) {
		try {
			
			calenderService.deleteSoftById(String.valueOf(calender.getId()));
			setSuccess(redirectAttrs, "Event deleted successfully");

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(redirectAttrs, "Error in deleting Event.");
		}

		return "redirect:/viewEvents";
	}

}
