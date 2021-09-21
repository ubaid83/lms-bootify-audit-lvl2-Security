/*package com.spts.lms.web.controllers;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.spts.lms.beans.announcement.Announcement;
import com.spts.lms.beans.assignment.Assignment;
import com.spts.lms.beans.course.UserCourse;
import com.spts.lms.beans.course.Course;
import com.spts.lms.beans.test.PendingTest;
import com.spts.lms.helpers.PaginationHelper.Page;
import com.spts.lms.services.announcement.AnnouncementService;
import com.spts.lms.services.assignment.AssignmentService;
import com.spts.lms.services.course.CourseService;
import com.spts.lms.services.dashborad.DashBoard;
import com.spts.lms.services.feedback.FeedbackService;
import com.spts.lms.services.test.StudentTestService;
import com.spts.lms.web.helper.WebPage;

@Secured("ROLE_ADMIN")
@Controller
public class DashboardController extends BaseController {

	@Autowired
	CourseService courseService;

	@Autowired
	AssignmentService assignmentService;

	@Autowired
	StudentTestService studentTestService;

	@Autowired
	AnnouncementService announcementService;

	private static final Logger logger = Logger
			.getLogger(FeedbackController.class);

	
	@RequestMapping(value = "/studentDashboardDetails", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String studentDashboardDetails(@ModelAttribute Course course, Model m,
			Principal principal) {
		
		try {
			logger.info("called ");

			String username = principal.getName();
			
			List<Course> courseDB = courseService.findByUser(username);
			
			List<Dashboard> listOfDashBoardElements = 
			
			m.addAttribute("dashboard", dashBoradItems);
			
			
			logger.info(dashBoradItems.get(0));

			// ---------------------------------

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(m, "Error in updating Feedback");
			return "homepage/login";
		}
		return "homepage/dashboard";
	}

}
*/