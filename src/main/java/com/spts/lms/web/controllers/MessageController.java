package com.spts.lms.web.controllers;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.spts.lms.auth.Token;
import com.spts.lms.beans.assignment.StudentAssignment;
import com.spts.lms.beans.message.Message;
import com.spts.lms.beans.message.StudentMessage;
import com.spts.lms.beans.user.Role;
import com.spts.lms.beans.user.User;
import com.spts.lms.daos.message.MessageDAO;
import com.spts.lms.daos.message.StudentMessageDAO;
import com.spts.lms.services.course.CourseService;
import com.spts.lms.services.message.MessageService;
import com.spts.lms.services.message.StudentMessageService;
import com.spts.lms.services.user.UserService;
import com.spts.lms.utils.LMSHelper;
import com.spts.lms.web.helper.WebPage;
import com.spts.lms.web.utils.Utils;

@Controller
public class MessageController extends BaseController {
	private static final Logger logger = Logger
			.getLogger(MessageController.class);

	@Autowired
	MessageService messageService;

	@Autowired
	CourseService courseService;

	@Autowired
	StudentMessageService studentMessageService;

	@Autowired
	MessageDAO messageDAO;

	@Autowired
	StudentMessageDAO studentMessageDAO;

	@Autowired
	UserService userService;

	@Secured({ "ROLE_USER" })
	@RequestMapping(value = "/createMessageForm", method = { RequestMethod.GET })
	public String createMessageForm(
			@RequestParam(required = false) Long courseId,
			@RequestParam(required = false) Long id, Model m,
			@ModelAttribute Message message, Principal principal,
			HttpServletRequest request) {
		m.addAttribute("webPage", new WebPage("message", "Create message",
				true, false));
		m.addAttribute("courseId", courseId);
		Token userDetails = (Token) principal;
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u1 = userService.findByUserName(username);
		

		String acadSession = u1.getAcadSession();
		
		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		if (request.getSession().getAttribute("courseRecord") == null
				|| request.getSession().getAttribute("courseRecord").equals("")) {
			
		} else {
			request.getSession().removeAttribute("courseRecord");
		}

		if (id != null) {
			message = messageService.findByID(id);
			m.addAttribute("edit", "true");
		}
		// String username = principal.getName();
		if (courseId == null) {

			if (userDetails.getAuthorities().contains(Role.ROLE_FACULTY)
					|| userDetails.getAuthorities().contains(Role.ROLE_STUDENT)) {

				m.addAttribute(
						"courses",
						courseService.findByUserActive(username,
								userDetails.getProgramName()));
			}

			if (userDetails.getAuthorities().contains(Role.ROLE_ADMIN)) {
				String progName = userDetails.getProgramName();
				m.addAttribute("allCourses",
						courseService.findByCoursesBasedOnProgramName(progName));
			}
			// m.addAttribute("courses", courseService.findByUser(username));
			m.addAttribute("showDropDown", true);
			m.addAttribute("message", message);
		} else {
			message.setCourseId(courseId);
			m.addAttribute("showDropDown", false);
			message.setCourse(courseService.findByID(message.getCourseId()));
			m.addAttribute("courseId", courseId);
			m.addAttribute("message", message);
		}

		m.addAttribute("message", message);
		return "message/createMessage";
	}

	@Secured({ "ROLE_USER" })
	@RequestMapping(value = "/createMessage", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String createMessage(@ModelAttribute Message message, Long courseId,
			RedirectAttributes redirectAttrs, Model m, Principal principal) {
		m.addAttribute("webPage", new WebPage("message", "Message Details",
				true, false));
		String username = principal.getName();
		UsernamePasswordAuthenticationToken userDeatils = (UsernamePasswordAuthenticationToken) principal;
		m.addAttribute("message", message);
		try {

			message.setCreatedBy(username);
			message.setLastModifiedBy(username);
			message.setFacultyId(username);

			String idForCourse = message.getIdForCourse();
			
			message.setCourseId(Long.valueOf(idForCourse));
			
			if (idForCourse != null) {
				message.setCourse(courseService.findByID(Long
						.valueOf(idForCourse)));
				message.setCourseId(Long.valueOf(idForCourse));
			} else
				message.setCourse(courseService.findByID(message.getCourseId()));
			messageService.insertWithIdReturn(message);

			setSuccess(m, "Message created successfully");

			List<StudentMessage> students = studentMessageService
					.getStudentsForMessage(message.getId(),
							message.getCourseId());

			for (StudentMessage uc : students) {
				User u1 = userService.findByUserName(uc.getUsername());
				uc.setRollNo(u1.getRollNo());
				students.set(students.indexOf(uc), uc);
			}
			m.addAttribute("students", students);
			

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(m, "Error in creating message");
			m.addAttribute("webPage", new WebPage("message", "Create Message",
					false, false));
			return "message/createMessage";
		}

		return "message/message";
	}

	@Secured({ "ROLE_USER" })
	@RequestMapping(value = "/updateMessage", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String updateMessage(@ModelAttribute Message message,
			RedirectAttributes redirectAttrs, Model m, Principal principal) {
		redirectAttrs.addFlashAttribute("message", message);
		m.addAttribute("webPage", new WebPage("updateMessage",
				"Message Details", false, false));
		try {
			String username = principal.getName();
			Message messageDb = messageService.findByID(message.getId());
			messageDb = LMSHelper.copyNonNullFields(messageDb, message);
			messageDb.setLastModifiedBy(username);

			messageService.update(messageDb);

			setSuccess(redirectAttrs, "Message updated successfully");
			redirectAttrs.addFlashAttribute("message", messageDb);

		} catch (Exception e) {

			logger.error(e.getMessage(), e);
			setError(redirectAttrs, "Error in updating message");
			return "redirect:/createMessageForm";
		}
		return "message/message";
	}

	@Secured({ "ROLE_ADMIN", "ROLE_FACULTY" })
	@RequestMapping(value = "/saveStudentMessageAllocation", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String saveStudentMessageAllocation(@ModelAttribute Message message,
			Model m, Principal principal,
			@RequestParam(required = false) Long courseId,
			@RequestParam(required = false) Long idForCourse) {
		m.addAttribute("webPage", new WebPage("message", "Create Message",
				true, false));
		String username = principal.getName();
		
		ArrayList<StudentMessage> studentMessageMappingList = new ArrayList<StudentMessage>();
		try {
			
			List<String> msg = message.getStudents();
			
			if (msg != null && msg.size() > 0) {
				for (String studentname : message.getStudents()) {
					StudentMessage bean = new StudentMessage();
					bean.setMessageId(message.getId());
					bean.setCourseId(courseId);
					bean.setDescription(message.getDescription());
					bean.setSubject(message.getSubject());
					bean.setUsername(studentname);
					bean.setCreatedBy(username);
					bean.setLastModifiedBy(username);
					studentMessageMappingList.add(bean);
				}
				

				studentMessageService.insertBatch(studentMessageMappingList);
				setSuccess(m, "Message created for "
						+ message.getStudents().size()
						+ " students successfully");

				return viewMessage(message.getId(), principal, m);
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(m, "Error in allocating message");
			m.addAttribute("webPage", new WebPage("message", "Create Message",
					false, false));
			return "message/createMessage";
		}
		m.addAttribute("message", message);
		return "message/createMessage";
	}

	@Secured({ "ROLE_ADMIN", "ROLE_FACULTY" })
	@RequestMapping(value = "/saveAllStudentMessage", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String saveAllStudentMessage(@ModelAttribute Message message,
			Model m, Principal principal) {
		m.addAttribute("webPage", new WebPage("message", "Create Message",
				true, false));
		String username = principal.getName();
		ArrayList<StudentMessage> studentMessageMappingList = new ArrayList<StudentMessage>();
		try {
			
			List<String> msg = message.getStudents();
			if (msg != null && msg.size() > 0) {
				for (String studentname : message.getStudents()) {
					StudentMessage bean = new StudentMessage();
					bean.setMessageId(message.getId());
					bean.setDescription(message.getDescription());
					bean.setCourseId(message.getCourseId());
					bean.setSubject(message.getSubject());
					bean.setUsername(studentname);
					bean.setCreatedBy(username);
					bean.setLastModifiedBy(username);
					studentMessageMappingList.add(bean);
				}
				

				studentMessageService.insertBatch(studentMessageMappingList);
				setSuccess(m, "Message created for "
						+ message.getStudents().size()
						+ " students successfully");

				return viewMessage(message.getId(), principal, m);
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(m, "Error in allocating message");
			m.addAttribute("webPage", new WebPage("message", "Create Message",
					false, false));
			return "message/createMessage";
		}
		m.addAttribute("message", message);
		return "message/createMessage";
	}

	@Secured({ "ROLE_USER" })
	@RequestMapping(value = "/viewMessage", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String viewMessage(@RequestParam(required = false) Long id,
			Principal principal, Model m) {
		m.addAttribute("webPage", new WebPage("message", "View message", true,
				false));
		String username = principal.getName();

		UsernamePasswordAuthenticationToken userDetails = (UsernamePasswordAuthenticationToken) principal;
		m.addAttribute("message", new Message());
		Message message = new Message();
		if (id != null) {
			message = messageService.findByID(id);
			message.setCourse(courseService.findByID(message.getCourseId()));
			List<StudentMessage> students = studentMessageService
					.getUsersForMessageByCourse(message.getCourseId());

			for (StudentMessage uc : students) {
				User u1 = userService.findByUserName(uc.getUsername());
				uc.setRollNo(u1.getRollNo());
				students.set(students.indexOf(uc), uc);
			}
			
			m.addAttribute("students", students);

		}
		m.addAttribute("message", message);
		return "message/message";
	}

	/*
	 * @RequestMapping(value = "/viewMyMessage", method = { RequestMethod.GET,
	 * RequestMethod.POST }) public String viewMyMessage(Model m,
	 * 
	 * @ModelAttribute Message message, Principal principal) {
	 * m.addAttribute("webPage", new WebPage("message",
	 * "View Messages recieved", true, false)); String username =
	 * principal.getName(); logger.info("username " + username); StudentMessage
	 * studentMessage=new StudentMessage();
	 * studentMessage.setSubject(message.getSubject());
	 * studentMessage.setDescription(message.getDescription());
	 * logger.info("Subject"+ message.getSubject()); m.addAttribute("message",
	 * studentMessage); List<StudentMessage> listOfAllMessages =
	 * studentMessageService .findByUserMessage(username);
	 * logger.info(listOfAllMessages); m.addAttribute("listOfAllMessages",
	 * listOfAllMessages); return "message/recievedMessage";
	 * 
	 * 
	 * }
	 */
	
	@Secured({ "ROLE_USER" })
	@RequestMapping(value = "/viewMyMessage", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String viewMyMessage(Model m, Principal principal) {
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u1 = userService.findByUserName(username);
		

		String acadSession = u1.getAcadSession();
		
		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		m.addAttribute("webPage", new WebPage("viewMyMessage", "View Message",
				true, true, true, true, false));
		m.addAttribute("message", new Message());
		List<StudentMessage> listOfAllMessages = studentMessageService
				.findByUserMessage(username);
		for (StudentMessage msg : listOfAllMessages) {
			if (!msg.getCreatedBy().equals(username)) {
				m.addAttribute("showReplyLink", true);

			} else {
				m.addAttribute("showReplyLink", false);

			}
		}
		List<StudentMessage> listOfSentMessages = studentMessageService
				.findByCreatedBy(username, username);
		for (StudentMessage sm : listOfSentMessages) {
			if(sm.getMessageRepliedBy()!=null){
			if (sm.getMessageRepliedBy().equalsIgnoreCase(username)) {
				sm.setUsername(sm.getCreatedBy());
			} else {

			}
			}
			else{
				
			}
			listOfSentMessages.set(listOfSentMessages.indexOf(sm), sm);
		}
		m.addAttribute("listOfSentMessages", listOfSentMessages);
		List<Message> messageReplyList = messageService.findAll();

		m.addAttribute("listOfAllMessages", listOfAllMessages);
		return "message/viewMyMessage";
	}

	@Secured({ "ROLE_USER" })
	@RequestMapping(value = "/giveResponseToMessage", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String giveResponseToMessage(Model m,
			@RequestParam("id") Long messageId) {
		m.addAttribute("webPage", new WebPage("assignment", "View Message",
				true, false));

		Message msg = messageService.findByID(messageId);
		StudentMessage message = studentMessageService.findByID(messageId);
		Document doc = Jsoup.parse(message.getDescription());
		String cQuestion = doc.text();
		message.setDescription(cQuestion);
		m.addAttribute("message", message);
		m.addAttribute("messageId", message.getId());
		// m.addAttribute("messageCourse", message.getCourseId());
		Date dt = Utils.getInIST();
		message.setMessageRepliedDate(dt);
		m.addAttribute("messageReplyTimeStamp", message.getMessageRepliedDate());
		

		return "message/responseToMessage";

	}

	@Secured({ "ROLE_USER" })
	@RequestMapping(value = "/viewOutboxMessage", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String viewOutboxMessage(Model m, @RequestParam("id") Long messageId) {
		m.addAttribute("webPage", new WebPage("assignment", "View Message",
				true, false));

		Message msg = messageService.findByID(messageId);
		StudentMessage message = studentMessageService.findByID(messageId);
		Document doc = Jsoup.parse(message.getDescription());
		String cQuestion = doc.text();
		message.setDescription(cQuestion);

		m.addAttribute("message", message);
		m.addAttribute("messageId", message.getId());
		// m.addAttribute("messageCourse", message.getCourseId());
		if (message.getMessageReply() != null) {
			m.addAttribute("showReply", true);
		} else {
			m.addAttribute("showReply", false);
		}
		Date dt = Utils.getInIST();
		message.setMessageRepliedDate(dt);
		m.addAttribute("messageReplyTimeStamp", message.getMessageRepliedDate());

		return "message/viewOutboxMessage";

	}

	@Secured({ "ROLE_USER" })
	@RequestMapping(value = "/saveMessageReply", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String saveMessageReply(Model m,
			@ModelAttribute StudentMessage message, Principal principal,
			@RequestParam Long id) {
		m.addAttribute("webPage", new WebPage("assignment", "View Message",
				true, false));
		String username = principal.getName();
		try {
			message.setLastModifiedBy(username);
			message.setMessageRepliedBy(username);
			Date dt = Utils.getInIST();
			message.setMessageRepliedDate(dt);
			studentMessageDAO.update(message);
			
		} catch (Exception e) {
			logger.error("Exception", e);
		}
		return viewMyMessage(m, principal);

	}

	/*
	 * @RequestMapping(value = "/viewMessageResponse", method = {
	 * RequestMethod.GET, RequestMethod.POST }) public String
	 * viewMessageResponse(Model m, Principal principal) {
	 * m.addAttribute("webPage", new WebPage("assignment",
	 * "View Message Response List", true, false)); String username =
	 * principal.getName(); logger.info("username " + username);
	 * m.addAttribute("grievances", new Grievances()); List<Grievances>
	 * listofAllGrievances = grievancesService .findByUserGrievances(username);
	 * m.addAttribute("listofAllGrievances", listofAllGrievances); return
	 * "grievances/viewGrievanceResponse"; }
	 */
}