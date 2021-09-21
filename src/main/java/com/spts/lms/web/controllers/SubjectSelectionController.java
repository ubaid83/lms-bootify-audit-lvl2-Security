package com.spts.lms.web.controllers;

import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.spts.lms.auth.Token;
import com.spts.lms.beans.selectives.SelectiveCourses;
import com.spts.lms.beans.selectives.SelectiveEvents;
import com.spts.lms.beans.selectives.SelectiveUserCourse;
import com.spts.lms.beans.user.Role;
import com.spts.lms.services.selectives.SelectiveCoursesService;
import com.spts.lms.services.selectives.SelectiveEventsService;
import com.spts.lms.services.selectives.SelectiveStudentsService;
import com.spts.lms.services.selectives.SelectiveUserCourseService;
import com.spts.lms.web.utils.Utils;

@Controller
public class SubjectSelectionController extends BaseController {

	private static final Logger logger = Logger.getLogger(SubjectSelectionController.class);

	@Autowired
	SelectiveStudentsService selectiveStudentsService;

	@Autowired
	SelectiveUserCourseService selectiveUserCourseService;

	@Autowired
	SelectiveEventsService selectiveEventsService;

	@Autowired
	SelectiveCoursesService selectiveCoursesService;

	@RequestMapping(value = "/viewSelectiveEvents", method = { RequestMethod.GET, RequestMethod.POST })
	public String viewSelectiveEvents(Principal principal, Model m) {
		try {
			String username = principal.getName();

			com.spts.lms.auth.Token userDetails = (com.spts.lms.auth.Token) principal;

			List<SelectiveEvents> eventsList = new ArrayList<>();
			String role = "";
			if (userDetails.getAuthorities().contains(Role.ROLE_ADMIN)) {
				role = "ROLE_ADMIN";
			} else {
				role = "ROLE_STUDENT";
			}
			eventsList = selectiveEventsService.getSelectiveEventsList(role, username);

			m.addAttribute("eventsList", eventsList);
		} catch (Exception ex) {
			logger.error("Exception", ex);
		}
		return "selectives/viewSelectiveEvents";
	}

	@RequestMapping(value = "/studentRegisterForm", method = { RequestMethod.GET, RequestMethod.POST })
	public String studentRegisterForm(@RequestParam String eventId, Principal principal, Model m) {

		String username = principal.getName();

		Token userDetails = (Token) principal;
		SelectiveEvents event = selectiveEventsService.findByID(Long.valueOf(eventId));
		Date nowDate = Utils.getInIST();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date startDate = df.parse(event.getStartDate());
			Date endDate = df.parse(event.getEndDate());
			if (startDate.before(nowDate) && endDate.after(nowDate)) {
				m.addAttribute("showSubmit", true);
			} else {
				m.addAttribute("showSubmit", false);
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			logger.error("Exception", e);
		}
		List<SelectiveCourses> coursesList = selectiveCoursesService.getCoursesByEventId(eventId);

		List<SelectiveUserCourse> selectiveUserCourseList = selectiveUserCourseService.getStudentsList(eventId,
				username);
		SelectiveUserCourse selectiveUserCourse = new SelectiveUserCourse();

		selectiveUserCourse.setEventId(eventId);
		m.addAttribute("selectiveUserCourse", selectiveUserCourse);
		m.addAttribute("event", event);
		m.addAttribute("selectionList", selectiveUserCourseList);
		m.addAttribute("coursesList", coursesList);
		
		m.addAttribute("course_id_sel",null);
		m.addAttribute("course_name_sel",null);
		if (selectiveUserCourseList.size() > 0) {
			m.addAttribute("course_id_sel", selectiveUserCourseList.get(0).getCourse_id());
			m.addAttribute("course_name_sel", selectiveUserCourseList.get(0).getCourse_name());
			m.addAttribute("isCourseSelected", true);
		}

		return "selectives/studentRegisterForm";
	}

	@RequestMapping(value = "/submitStudentRegistration", method = { RequestMethod.POST })
	public String submitStudentRegistration(Principal principal, Model m,
			@ModelAttribute SelectiveUserCourse selectiveUserCourse, RedirectAttributes redirectAttrrs) {

		String username = principal.getName();

		Token userDetails = (Token) principal;
		try {
			List<SelectiveUserCourse> selectiveUserCourseList = selectiveUserCourseService
					.getStudentsList(selectiveUserCourse.getEventId(), username);

			if (selectiveUserCourseList.size() > 0) {

				// make inactive previous selection
				logger.info("selectiveUserCourseList  entered");
				for (SelectiveUserCourse sc : selectiveUserCourseList) {
					sc.setActive("N");
				}
			}

			selectiveUserCourse.setUsername(username);
			selectiveUserCourse.setCreatedBy(username);
			selectiveUserCourse.setLastModifiedBy(username);
			selectiveUserCourse.setActive("Y");

			logger.info("selective user course list"+selectiveUserCourseList);
			selectiveUserCourseService.updateBatch(selectiveUserCourseList);
			selectiveUserCourseService.insert(selectiveUserCourse);

			setSuccess(redirectAttrrs, "Registration Successful!!");

		} catch (Exception ex) {

			logger.error(ex);
			setError(redirectAttrrs, "Error in Registration");
		}

		redirectAttrrs.addAttribute("eventId", selectiveUserCourse.getEventId());
		return "redirect:/studentRegisterForm";

	}

}
