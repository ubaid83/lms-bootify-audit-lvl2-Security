package com.spts.lms.web.controllers;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;



import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spts.lms.auth.Token;
import com.spts.lms.beans.assignment.StudentAssignment;
import com.spts.lms.beans.course.Course;
import com.spts.lms.beans.course.UserCourse;
import com.spts.lms.beans.group.GroupCourse;
import com.spts.lms.beans.group.Groups;
import com.spts.lms.beans.group.StudentGroup;
import com.spts.lms.beans.user.Role;
import com.spts.lms.beans.user.User;
import com.spts.lms.helpers.PaginationHelper.Page;
import com.spts.lms.services.assignment.StudentAssignmentService;
import com.spts.lms.services.course.CourseService;
import com.spts.lms.services.course.UserCourseService;
import com.spts.lms.services.group.GroupCourseService;
import com.spts.lms.services.group.GroupService;
import com.spts.lms.services.group.StudentGroupService;
import com.spts.lms.services.user.UserService;
import com.spts.lms.utils.LMSHelper;
import com.spts.lms.web.helper.WebPage;
import com.spts.lms.web.utils.BusinessBypassRule;
import com.spts.lms.web.utils.ValidationException;

@Controller
@SessionAttributes("userId")
public class GroupController extends BaseController {
	@Autowired
	ApplicationContext act;

	@Autowired
	GroupService groupService;

	@Autowired
	StudentGroupService studentGroupService;

	@Autowired
	UserService userService;

	@Autowired
	CourseService courseService;

	@Autowired
	UserCourseService userCourseService;

	@Autowired
	StudentAssignmentService studentAssignmentService;

	@Autowired
	GroupCourseService groupCourseService;

	@Autowired
	BusinessBypassRule businessBypassRule;

	protected static final int BUFFER_SIZE = 4096;

	private static final Logger logger = Logger
			.getLogger(GroupController.class);

	@Secured("ROLE_FACULTY")
	@RequestMapping(value = "/createGroupForm", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String createGroupForm(
			@RequestParam(required = false) Long courseId,
			@RequestParam(required = false) Long id, Model m,
			@ModelAttribute Groups groups, Principal principal,
			HttpServletRequest request,RedirectAttributes redirectAttrs) {
		m.addAttribute("webPage", new WebPage("groups", "Create group", true,
				false));

		String username = principal.getName();
		
		//update by sandip on 01/12/2021
		logger.info("info---"+id);
		logger.info("info---"+studentGroupService.getNoOfStudentsAllocated(id));
		System.out.println("Number of students allocated : "+studentGroupService.getNoOfStudentsAllocated(id));

		
		if(studentGroupService.getNoOfStudentsAllocated(id)>0){
			setError(redirectAttrs, "This group is created you cannot edit!");
			return "redirect:/viewGroup?id="+groups.getId();
		}
		//update by sandip on 01/12/2021
		
		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		if (courseId == null) {
			m.addAttribute("courseId", courseId);
			if (request.getSession().getAttribute("courseRecord") == null
					|| request.getSession().getAttribute("courseRecord")
							.equals("")) {

			} else {
				request.getSession().removeAttribute("courseRecord");
			}
		}

		List<String> students = new ArrayList<String>();
		List<Course> courses = new ArrayList<Course>();

		if (id != null) {
			List<Course> courseList = new ArrayList<Course>();
			groups = groupService.findByID(id);
			students = userCourseService.findStudentUsernamesForFaculty(groups
					.getCourseId());
			courseList = courseService.findByUserActive(username,
					userdetails1.getProgramName());
			for (Course c : courseList) {
				List<String> studentList = userCourseService
						.findStudentUsernamesForFaculty(c.getId());
				
				/*
				 * if (studentList.containsAll(students)) {
				 * logger.info("course - " + c.getCourseName() +
				 * " same students "); courses.add(c);
				 * 
				 * }
				 */
				

				if (students != null && studentList != null
						&& (students.size() == studentList.size())) {
					students.removeAll(studentList);
					if (students.isEmpty()) {

						courses.add(c);
					} else {

					}
				}

			}
			m.addAttribute("edit", "true");
			m.addAttribute("courses", courses);
		}
		// String username = principal.getName();
		if (courseId == null) {
			m.addAttribute(
					"courses",
					courseService.findByUserActive(username,
							userdetails1.getProgramName()));
			m.addAttribute("showDropDown", true);
			m.addAttribute("showRandom", false);
		} else {
			m.addAttribute("courseId", "true");
			groups.setCourseId(courseId);
			m.addAttribute("showDropDown", false);
			m.addAttribute("showRandom", true);
		}
		groups.setCourse(courseService.findByID(groups.getCourseId()));

		m.addAttribute("courseId", courseId);
		m.addAttribute("groups", groups);
		return "group/createGroup";
	}

	/*
	 * @RequestMapping(value = "/deleteGroup", method = { RequestMethod.GET,
	 * RequestMethod.POST }) public String deleteGroup(@RequestParam Integer id,
	 * 
	 * @RequestParam String courseId, RedirectAttributes redirectAttrs) { try {
	 * groupService.deleteSoftById(String.valueOf(id));
	 * studentGroupService.deleteBatch(studentGroupService
	 * .findStudentsByGroupId(Long.valueOf(id)));
	 * 
	 * // studentAssignmentService.deleteBatch(studentAssignmentService.
	 * findStudentsByGroupId(Long.valueOf(id)));
	 * 
	 * // studentAssignmentService.setInActive(Long.valueOf(id));
	 * List<StudentAssignment> students = studentAssignmentService
	 * .findStudentsByGroupId(Long.valueOf(id));
	 * studentAssignmentService.deleteBatch(students); setSuccess(redirectAttrs,
	 * "Group deleted successfully");
	 * 
	 * } catch (Exception e) { logger.error(e.getMessage(), e);
	 * setError(redirectAttrs, "Error in deleting Group."); }
	 * 
	 * return "redirect:/searchFacultyGroups?courseId=" + courseId; }
	 */

	@Secured("ROLE_FACULTY")
	@RequestMapping(value = "/deleteGroup", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String deleteGroup(@RequestParam Integer id,
			@RequestParam String courseId, RedirectAttributes redirectAttrs) {
		try {
			groupService.deleteSoftById(String.valueOf(id));
			studentGroupService.deleteBatch(studentGroupService
					.findStudentsByGroupId(Long.valueOf(id)));

			// studentAssignmentService.deleteBatch(studentAssignmentService.findStudentsByGroupId(Long.valueOf(id)));

			// studentAssignmentService.setInActive(Long.valueOf(id));
			List<StudentAssignment> students = studentAssignmentService
					.findStudentsByGroupId(Long.valueOf(id));
			studentAssignmentService.deleteBatch(students);
			List<GroupCourse> gcList = groupCourseService
					.findbyGroupId(new Long(id));
			groupCourseService.deleteBatch(gcList);
			setSuccess(redirectAttrs, "Group deleted successfully");

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(redirectAttrs, "Error in deleting Group.");
		}

		return "redirect:/searchFacultyGroups?courseId=" + courseId;
	}

	@Secured("ROLE_FACULTY")
	@RequestMapping(value = "/createGroup", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String createGroup(@ModelAttribute Groups groups, Long courseId, 
			@RequestParam(required = false) Long myId,
			RedirectAttributes redirectAttrs, Model m, Principal principal) {
		m.addAttribute("webPage", new WebPage("groups", "Group Details", true,
				false));
		String username = principal.getName();

		try {
			groups.setCreatedBy(username);
			groups.setLastModifiedBy(username);
			groups.setFacultyId(username);
			groups.setActive("Y");

            /*update by sandip(22/10/2021)*/
			 
	
			 Course courseI2 = courseService.checkIfCourseId(groups.getCourseId());
			 System.out.println("Course Id 2:  "+groups.getCourseId());
				if(null != groups.getCourseId() && null != courseI2)
				{
				   System.out.println("course id is valid");
				}
				else
				{
					throw new ValidationException("Invalid course ID");
				}
		

			
			System.out.println("course id 1 : "+courseId);
	        
			String grouptitle = groups.getGroupName();
			BusinessBypassRule.validateAlphaNumeric(grouptitle);
			String noOfstudent = groups.getNoOfStudents();
			BusinessBypassRule.validateNumericNotAZero(noOfstudent);
			
			
			/*update by sandip(22/10/2021)*/
			
			String idForCourse = groups.getIdForCourse();
			System.out.println("course id 2 : "+idForCourse); //print null
			

			if (idForCourse != null) {
				groups.setCourse(courseService.findByID(Long
						.valueOf(idForCourse)));
				groups.setCourseId(Long.valueOf(idForCourse));
				
				
			} else
				groups.setCourse(courseService.findByID(groups.getCourseId()));
			Course c = courseService.findByID(groups.getCourseId());
			groups.setAcadMonth(c.getAcadMonth());
			groups.setAcadYear(Integer.valueOf(c.getAcadYear()));
			groupService.insertWithIdReturn(groups);
			GroupCourse gp = new GroupCourse();
			gp.setGroupId(groups.getId());
			gp.setCourseId(String.valueOf(groups.getCourseId()));
			gp.setActive("Y");
			gp.setCreatedBy(username);
			gp.setLastModifiedBy(username);

			groupCourseService.insert(gp);

			setSuccess(m, "Group created successfully");

			List<StudentGroup> students = studentGroupService
					.getStudentsForGroup(groups.getCourseId());
			m.addAttribute("students", students);
			myId = groups.getMyId();
			groups.setMyId(groups.getId());

			redirectAttrs.addAttribute("id", groups.getMyId());
			m.addAttribute("id", groups.getMyId());

		}

        /*by sandip(22/10/2021)*/
		catch (ValidationException er) { 
			// print the stack trace
			logger.error(er.getMessage(), er);
			setError(m,er.getMessage());
            m.addAttribute("webPage", new WebPage("groups", "Create Group", false, false));
			return "group/createGroup";
			//String json = "{\"Status\":\"Fail\", \"msg\":\""+er.getMessage()+"\"}";
			//return json;
		}
	  /*by sandip(22/10/2021)*/
		
		catch (Exception e) {
			logger.error(e.getMessage(), e);
			//setError(m, "Error in creating group");
			setError(m, e.getMessage());
	        m.addAttribute("webPage", new WebPage("groups", "Create Group", false, false));
			return "group/createGroup";
		}

		return "redirect:/viewGroup";
	}

	/*
	 * @RequestMapping(value = "/updateGroup", method = { RequestMethod.GET,
	 * RequestMethod.POST }) public String updateGroup(@ModelAttribute Groups
	 * groups, RedirectAttributes redirectAttrs, Model m, Principal principal) {
	 * redirectAttrs.addFlashAttribute("groups", groups);
	 * m.addAttribute("webPage", new WebPage("groups", "Group Details", true,
	 * false)); try { String username = principal.getName();
	 * 
	 * Token userdetails1 = (Token) principal; String ProgramName =
	 * userdetails1.getProgramName(); User u =
	 * userService.findByUserName(username);
	 * logger.info("ACAD SESSION------------------------->" +
	 * u.getAcadSession());
	 * 
	 * String acadSession = u.getAcadSession();
	 * 
	 * m.addAttribute("Program_Name", ProgramName);
	 * m.addAttribute("AcadSession", acadSession);
	 * 
	 * Groups groupDb = groupService.findByID(groups.getId()); groupDb =
	 * LMSHelper.copyNonNullFields(groupDb, groups);
	 * groupDb.setLastModifiedBy(username); Course c =
	 * courseService.findByID(groupDb.getCourseId());
	 * groupDb.setAcadMonth(c.getAcadMonth());
	 * groupDb.setAcadYear(Integer.valueOf(c.getAcadYear()));
	 * 
	 * groupService.update(groupDb);
	 * 
	 * setSuccess(redirectAttrs, "Group updated successfully");
	 * List<StudentGroup> students = studentGroupService
	 * .getStudentsForGroup(groups.getId(), groups.getCourseId());
	 * m.addAttribute("students", students);
	 * redirectAttrs.addFlashAttribute("groups", groupDb);
	 * 
	 * } catch (Exception e) {
	 * 
	 * logger.error(e.getMessage(), e); setError(redirectAttrs,
	 * "Error in updating group"); return "redirect:/createGroupForm"; } return
	 * "group/group"; }
	 */

	@Secured("ROLE_FACULTY")
	@RequestMapping(value = "/updateGroup", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String updateGroup(@ModelAttribute Groups groups,
			RedirectAttributes redirectAttrs, Model m, Principal principal) {
		redirectAttrs.addFlashAttribute("groups", groups);
		m.addAttribute("webPage", new WebPage("viewGroup", "Group Details",
				true, false));
		try {

			String username = principal.getName();
			
			Token userdetails1 = (Token) principal;
			String ProgramName = userdetails1.getProgramName();
			User u = userService.findByUserName(username);

			String acadSession = u.getAcadSession();

			m.addAttribute("Program_Name", ProgramName);
			m.addAttribute("AcadSession", acadSession);

			Groups groupDb = groupService.findByID(groups.getId());
			groupDb = LMSHelper.copyNonNullFields(groupDb, groups);
			groupDb.setLastModifiedBy(username);
			Course c = courseService.findByID(groupDb.getCourseId());
			groupDb.setAcadMonth(c.getAcadMonth());
			groupDb.setAcadYear(Integer.valueOf(c.getAcadYear()));

			groupService.update(groupDb);
			// GroupCourse gc =
			// groupCourseService.findbyGroupId(groups.getId());
			String courseIDS = groups.getCourseIDS();
			List<String> courseIDsLsit = Arrays.asList(courseIDS.split(","));
			List<GroupCourse> gcList = new ArrayList<GroupCourse>();
			for (String s : courseIDsLsit) {
				if (groupCourseService.findbyGroupIdAndCourseId(groups.getId(),
						s) == null) {

					GroupCourse gc = new GroupCourse();
					gc.setGroupId(groups.getId());
					gc.setCourseId(s);
					gc.setLastModifiedBy(username);
					gc.setActive("Y");
					gc.setCreatedBy(username);
					gcList.add(gc);

				}

			}

			groupCourseService.insertBatch(gcList);

			setSuccess(redirectAttrs, "Group updated successfully");
			List<StudentGroup> students = studentGroupService
					.getStudentsForGroup(groups.getId(), groups.getCourseId());
			
			
			
			m.addAttribute("students", students);
			redirectAttrs.addFlashAttribute("groups", groupDb);

		}
		catch (Exception e) {

			logger.error(e.getMessage(), e);
			setError(redirectAttrs, "Error in updating group");
			return "redirect:/createGroupForm";
		}
		return "group/group";
	}

	@Secured("ROLE_FACULTY")
	@RequestMapping(value = "/saveStudentGroupAllocationSelectAll", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String saveStudentGroupAllocationSelectAll(
			@ModelAttribute Groups groups, Model m, Principal principal,RedirectAttributes RedirectAttributes) {
		m.addAttribute("webPage", new WebPage("groups", "Create Group", true,
				true));
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();
		
		System.out.println("acadSession : "+acadSession);

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		Groups grpDB = new Groups();
		grpDB = groupService.findByID(groups.getId());

		List<String> allocatestudents = new ArrayList<String>();
		ArrayList<StudentGroup> studentGroupMappingList = new ArrayList<StudentGroup>();
		try {
		
			List<StudentGroup> stu = studentGroupService
					.getStudentsForGroup(groups.getCourseId());

			for (StudentGroup sg : stu) {
				allocatestudents.add(sg.getUsername());
			}
			groups.setStudents(allocatestudents);

			if (stu != null && stu.size() > 0) {
				for (String studentUsername : groups.getStudents()) {
					StudentGroup bean = new StudentGroup();
					bean.setAcadMonth(groups.getAcadMonth());
					bean.setAcadYear(groups.getAcadYear());
					bean.setGroupId(groups.getId());
					bean.setCourseId(groups.getCourseId());
					bean.setUsername(studentUsername);
					bean.setCreatedBy(username);
					bean.setLastModifiedBy(username);
					studentGroupMappingList.add(bean);
				}

				return viewGroup(groups.getId(), m, null, principal,RedirectAttributes);
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(m, "Error in allocating group");
			m.addAttribute("webPage", new WebPage("groups", "Create Group",
					false, false));
			return "group/createGroup";
		}
		m.addAttribute("groups", groups);
		return "group/createGroup";
	}

	@Secured("ROLE_FACULTY")
	@RequestMapping(value = "/saveStudentGroupAllocation", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String saveStudentGroupAllocation(@ModelAttribute Groups groups,
			Model m, Principal principal,RedirectAttributes redirectAttributes) {
		m.addAttribute("webPage", new WebPage("groups", "Create Group", true,
				true));
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		Groups grpDB = new Groups();
		grpDB = groupService.findByID(groups.getId());

		ArrayList<StudentGroup> studentGroupMappingList = new ArrayList<StudentGroup>();
		try {
			
			//Sandip 06/12/2021

			UserCourse facultyId = userCourseService.checkIfFacultyCourseAcadYear(groups
					.getFacultyId(), groups.getCourseId(), groups.getAcadYear());
			if (null != groups.getFacultyId() && null != facultyId) {
				System.out.println("Faculty ID is valid!");
			} else {
				throw new ValidationException("Invalid Faculty ID / Course ID / Acad Year!");
				
			}

			System.out.println("groupdetails: " + groups.getAcadYear());

			List<String> stu = groups.getStudents();

			for (String student : stu) {
				BusinessBypassRule.validateNumeric(student);
				if (student != null) {
					User students = userService.checkIfExistsInDB(student);
					if (students == null) {
						throw new ValidationException(
								"Invalid Students SAP ID!");
					}
				}
			}

			
			//Sandip 06/12/2021
			
			if (stu != null && stu.size() > 0) {
				for (String studentUsername : groups.getStudents()) {
					StudentGroup bean = new StudentGroup();
					bean.setAcadMonth(groups.getAcadMonth());
					bean.setAcadYear(groups.getAcadYear());
					bean.setGroupId(groups.getId());
					bean.setCourseId(groups.getCourseId());
					bean.setUsername(studentUsername);
					bean.setCreatedBy(username);
					bean.setLastModifiedBy(username);
					studentGroupMappingList.add(bean);
				}

				int noOfStudentsAllocated = studentGroupService
						.getNoOfStudentsAllocated(grpDB.getId());
				int noOfStudentSelected = stu.size();
				int noOfStudent = Integer.parseInt(grpDB.getNoOfStudents());
				int totalLimit = noOfStudent - noOfStudentsAllocated;

				if (totalLimit >= noOfStudentSelected) {
					studentGroupService.insertBatch(studentGroupMappingList);
					setSuccess(m, "Group created for "
							+ groups.getStudents().size()
							+ " students successfully ");
				} else {
					setError(m, "no. of students exceed ");
				}

				return viewGroup(groups.getId(), m, null, principal,redirectAttributes);
			}

		}
		catch (ValidationException er) {
			logger.info("temp-----------");
			logger.error(er.getMessage(), er);
			setError(redirectAttributes, er.getMessage());
			m.addAttribute("webPage", new WebPage("groups", "Create Group",
					false, false));
			return "redirect:/viewGroup?id=" + groups.getId();
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(m, "Error in allocating group");
			m.addAttribute("webPage", new WebPage("groups", "Create Group",
					false, false));
			return "redirect:/viewGroup?id=" + groups.getId();
		}
		m.addAttribute("groups", groups);
		return "redirect:/viewGroup?id=" + groups.getId();
	}

	/*
	 * @RequestMapping(value = "/saveStudentGroupAllocation", method = {
	 * RequestMethod.GET, RequestMethod.POST }) public String
	 * saveStudentGroupAllocation(@ModelAttribute Groups groups, Model m,
	 * Principal principal) { m.addAttribute("webPage", new WebPage("groups",
	 * "Create Group", true, true)); String username = principal.getName();
	 * ArrayList<StudentGroup> studentGroupMappingList = new
	 * ArrayList<StudentGroup>(); try { List<String> stu = groups.getStudents();
	 * if (stu != null && stu.size() > 0) { for (String studentUsername :
	 * groups.getStudents()) { StudentGroup bean = new StudentGroup();
	 * bean.setAcadMonth(groups.getAcadMonth());
	 * bean.setAcadYear(groups.getAcadYear()); bean.setGroupId(groups.getId());
	 * bean.setCourseId(groups.getCourseId());
	 * bean.setUsername(studentUsername); bean.setCreatedBy(username);
	 * bean.setLastModifiedBy(username); studentGroupMappingList.add(bean); }
	 * logger.info(studentGroupMappingList);
	 * 
	 * studentGroupService.insertBatch(studentGroupMappingList); setSuccess( m,
	 * "Group created for " + groups.getStudents().size() +
	 * " students successfully , Please Check Allocated Students In the End Of List"
	 * );
	 * 
	 * return viewGroup(groups.getId(), m); }
	 * 
	 * } catch (Exception e) { logger.error(e.getMessage(), e); setError(m,
	 * "Error in allocating group"); m.addAttribute("webPage", new
	 * WebPage("groups", "Create Group", false, false)); return
	 * "group/createGroup"; } m.addAttribute("groups", groups); return
	 * "group/createGroup"; }
	 */

	/*
	 * @RequestMapping(value = "/viewGroup", method = { RequestMethod.GET,
	 * RequestMethod.POST }) public String viewGroup(@RequestParam(required =
	 * false) Long id, Model m) { m.addAttribute("webPage", new
	 * WebPage("groups", "View group", true, false)); m.addAttribute("groups",
	 * new Groups()); Groups groups = new Groups(); if (id != null) { groups =
	 * groupService.findByID(id);
	 * groups.setCourse(courseService.findByID(groups.getCourseId()));
	 * List<StudentGroup> students = studentGroupService
	 * .getStudentsForGroup(groups.getId(), groups.getCourseId(),
	 * groups.getAcadMonth(), groups.getAcadYear()); m.addAttribute("students",
	 * students);
	 * 
	 * } m.addAttribute("groups", groups); return "group/group"; }
	 */

	
	@Secured("ROLE_FACULTY")
	@RequestMapping(value = "/viewGroup", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String viewGroup(@RequestParam(required = false) Long id, Model m,
			@RequestParam(required = false) Long campusId, Principal principal,RedirectAttributes RedirectAttributes) {
		m.addAttribute("webPage", new WebPage("viewGroup", "View group", true,
				false));

		String username = principal.getName();
		

//		String noOfStu = groups.getNoOfStudents();
//		System.out.println("no of students"+noOfStu);
//		logger.info("noOfStu is " + noOfStu);
//
//		businessBypassRule.validateNumericNotAZero(noOfStu);

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		m.addAttribute("groups", new Groups());
		Groups groups = new Groups();
		if (id != null) {

			groups = groupService.findByID(id);

			m.addAttribute("noOfStudents", groups.getNoOfStudents());
			m.addAttribute("noOfStudentAllocated",
					studentGroupService.getNoOfStudentsAllocated(id));
			
			logger.info(studentGroupService.getNoOfStudentsAllocated(id));
		
			groups.setCourse(courseService.findByID(groups.getCourseId()));
			List<StudentGroup> students = new ArrayList<StudentGroup>();

			if (campusId != null) {
				groups.setCampusId(campusId);
				students = studentGroupService.getStudentsForGroupAndCampusId(
						groups.getCourseId(), campusId);
			} else {
				students = studentGroupService
						.getStudentsForGroupAndCourseId(groups.getCourseId());
			}

			for (StudentGroup uc : students) {
				User u1 = userService.findByUserName(uc.getUsername());
				uc.setRollNo(u1.getRollNo());
				students.set(students.indexOf(uc), uc);
			}
			m.addAttribute("students", students);

		}
		m.addAttribute("allCampuses", userService.findCampus());
		m.addAttribute("id", id);
		m.addAttribute("groups", groups);
		
		return "group/group";
	}

	@Secured("ROLE_USER")
	@RequestMapping(value = "/searchGroupForm", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String searchGroupForm(Model m, @ModelAttribute Groups groups,
			Principal principal) {
		m.addAttribute("webPage", new WebPage("groupList", "Search Groups",
				false, false));
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		return "group/searchGroup";
	}

	@Secured("ROLE_USER")
	@RequestMapping(value = "/searchGroup", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String searchGroup(
			@RequestParam(required = false, defaultValue = "1") int pageNo,
			Model m, @ModelAttribute Groups groups) {

		m.addAttribute("webPage", new WebPage("groupList", "Search Groups",
				true, false));

		try {
			Page<Groups> page = groupService.searchActiveByExactMatch(groups,
					pageNo, pageSize);

			List<Groups> groupList = page.getPageItems();

			m.addAttribute("page", page);
			m.addAttribute("q", getQueryString(groups));

			if (groupList == null || groupList.size() == 0) {
				setSuccess(m, "No Groups found");
			}

		} catch (Exception e) {

			logger.error(e.getMessage(), e);
			setError(m, "Error in getting Groups List");
		}
		return "group/searchGroup";
	}

	/*
	 * @RequestMapping(value = "/searchFacultyGroups", method = {
	 * RequestMethod.GET, RequestMethod.POST }) public String
	 * searchFacultyGroups(
	 * 
	 * @RequestParam(required = false, defaultValue = "1") int pageNo, Model m,
	 * 
	 * @ModelAttribute Groups groups, Principal principal, RedirectAttributes
	 * redirectAttributes,
	 * 
	 * @RequestParam(required = false, defaultValue = "") String courseId,
	 * HttpServletRequest request) {
	 * logger.info("searchFacultyGroups(): groups = " + groups);
	 * m.addAttribute("webPage", new WebPage("groupList", "Search Groups", true,
	 * false));
	 * 
	 * try {
	 * 
	 * if (courseId == null || courseId.isEmpty()) { if
	 * (request.getSession().getAttribute("courseRecord") == null ||
	 * request.getSession().getAttribute("courseRecord") .equals("")) {
	 * logger.info("session courseId not present------"); } else {
	 * request.getSession().removeAttribute("courseRecord"); }
	 * 
	 * }
	 * 
	 * UsernamePasswordAuthenticationToken userDetails =
	 * (UsernamePasswordAuthenticationToken) principal; if
	 * (userDetails.getAuthorities().contains(Role.ROLE_FACULTY)) {
	 * groups.setFacultyId(principal.getName()); } logger.info("acad year" +
	 * groups.getAcadYear() + "acad month" + groups.getAcadMonth());
	 * Page<Groups> page = groupService.searchActiveByExactMatch(groups, pageNo,
	 * pageSize);
	 * 
	 * List<Groups> groupList = page.getPageItems(); for (Groups g : groupList)
	 * { Long cId = g.getCourseId(); Course course =
	 * courseService.findByID(cId); String courseName = course.getCourseName();
	 * 
	 * g.setCourseName(courseName);
	 * 
	 * groupList.set(groupList.indexOf(g), g); m.addAttribute("courseName",
	 * courseName); } m.addAttribute("allCourses",
	 * courseService.findByUserActive(principal.getName()));
	 * m.addAttribute("page", page); m.addAttribute("q",
	 * getQueryString(groups));
	 * 
	 * if (groupList == null || groupList.size() == 0) { setNote(m,
	 * "No Groups found"); }
	 * 
	 * } catch (Exception e) {
	 * 
	 * logger.error(e.getMessage(), e); setError(m,
	 * "Error in getting Group List"); } return "group/facultyGroupList"; }
	 */

	@Secured("ROLE_FACULTY")
	@RequestMapping(value = "/searchFacultyGroups", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String searchFacultyGroups(Principal principal, Model m,
			@RequestParam(required = false) String courseId,
			RedirectAttributes redirectAttributes, HttpServletRequest request,
			@ModelAttribute Groups groups) {
		m.addAttribute("webPage", new WebPage("viewGroup", "View Groups", true,
				false));
		logger.info("groupCourse ---------->" + groups.getCourseId());
		String username = principal.getName();
		Token userDetails = (Token) principal;

		String ProgramName = userDetails.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		List<Groups> groupList = new ArrayList<Groups>();
		if (userDetails.getAuthorities().contains(Role.ROLE_FACULTY)) {
			groups.setFacultyId(principal.getName());

		}
		if (courseId == null || courseId.isEmpty()) {
			if (request.getSession().getAttribute("courseRecord") == null
					|| request.getSession().getAttribute("courseRecord")
							.equals("")) {
				groupList = groupService.findAllGroupsByFaculty(username,
						Long.parseLong(userDetails.getProgramId()));

			} else {
				groupList = groupService.findAllGroupsByFaculty(username,
						Long.parseLong(userDetails.getProgramId()));

				request.getSession().removeAttribute("courseRecord");
			}

		} else {
			groupList = groupService.findByFacultyAndCourse(
					principal.getName(), Long.parseLong(courseId));
		}

		if (groupList == null || groupList.size() == 0) {
			setNote(m, "No Groups found");
		}
		for (Groups g : groupList) {
			logger.info("CourseID ------------>" + g.getCourseId());
			Long cId = g.getCourseId();

			Course course = courseService.findByID(cId);
			String courseName = course.getCourseName();

			g.setCourseName(courseName);

			groupList.set(groupList.indexOf(g), g);
			m.addAttribute("courseName", courseName);
		}
		m.addAttribute("groupList", groupList);
		m.addAttribute(
				"allCourses",
				courseService.findByUserActive(principal.getName(),
						userDetails.getProgramName()));

		return "group/facultyGroupList";

	}

	@RequestMapping(value = "/getGroupDetails", method = { RequestMethod.GET,
			RequestMethod.POST })
	public @ResponseBody String getGroupDetails(@RequestParam Long id, Model m) {
		Groups groups = new Groups();

		if (id != null) {
			groups = groupService.findByID(id);
			return groups.getGroup_details();

		} else {
			return "Error in retrieving group: No Id in request";
		}
	}

	@Secured("ROLE_FACULTY")
	@RequestMapping(value = "/searchFacultyGroupAllocationForm", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String searchFacultyGroupAllocationForm(
			RedirectAttributes redirectAttributes,
			Model m,
			Long id,
			@ModelAttribute Groups groups,
			Principal principal,
			@RequestParam(name = "courseId", required = false, defaultValue = "") String courseId,
			HttpServletRequest request) {
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		m.addAttribute("webPage", new WebPage("assignmentList",
				"Search groups", false, false));
		m.addAttribute("groups", groups);

		if (courseId == null || courseId.isEmpty()) {
			if (request.getSession().getAttribute("courseRecord") == null
					|| request.getSession().getAttribute("courseRecord")
							.equals("")) {
			} else {
				request.getSession().removeAttribute("courseRecord");
			}

		}

		m.addAttribute(
				"allCourses",
				courseService.findByUserActive(username,
						userdetails1.getProgramName()));
		m.addAttribute("preAssigment", new ArrayList());

		m.addAttribute("allGroups", groupService.findAll());
		return "group/facultyGroupAllocation";
	}

	@Secured("ROLE_FACULTY")
	@RequestMapping(value = "/searchFacultyGroupAllocation", method = { RequestMethod.POST })
	public String searchFacultyGroupAllocation(Model m, Principal principal,
			@RequestParam(required = false) Long courseId,
			@ModelAttribute Groups groups, @RequestParam Long id) {
		m.addAttribute("webPage", new WebPage("assignment",
				"Faculty Allocation", true, false));
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		// List<Assignment> list1 =
		// assignmentService.getAssignmentBasedOnCourse(courseId);
		groups = groupService.findByID(id);
		m.addAttribute("allFaculty", userService.findAllFaculty());
		List<Groups> list1 = new ArrayList<Groups>();
		list1.add(groups);

		if (list1 != null && list1.size() > 0) {
			m.addAttribute("list1", list1);
			m.addAttribute("rowCount", list1.size());
			m.addAttribute("groups", groups);
		} else {
			setNote(m, "No Students found for this course");
		}
		return "group/facultyGroupAssigned";

	}
	

	@Secured("ROLE_FACULTY")
	@RequestMapping(value = "/saveFacultyGroupAllocation", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String saveFacultyAllocation(@ModelAttribute Groups groups, Model m,
			Principal principal,
			@RequestParam(required = false) String facultyId,
			RedirectAttributes redirectAttributes) {
		m.addAttribute("webPage", new WebPage("groups", "Faculty Allocate",
				true, true, true, true, false));
		Token userdetails1 = (Token) principal;
		String username = principal.getName();
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		List<User> listOfFaculty = new ArrayList<User>();
		listOfFaculty = userService.findAllFaculty();
		m.addAttribute("listOfFaculty", listOfFaculty);
		/* assignment.setCreatedBy(assignment.getFacultyId()); */
		groupService.updateFacultyAssigned(facultyId, groups.getId());

		if (groups.getFacultyId().equalsIgnoreCase(facultyId)) {
			setSuccess(redirectAttributes, "Faculty Changed successfully!");
		} else {
			setError(m, "Error!");
		}

		// assignmentService.update(assignment);
		m.addAttribute("groups", groups);
		return "redirect:/searchFacultyGroupAllocationForm";
	}

	@RequestMapping(value = "/getGroupsByCourseOnly", method = {
			RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String getGroupsByCourseOnly(
			@RequestParam(name = "courseId") String courseId,
			Principal principal) {
		String json = "";
		String username = principal.getName();

		List<Groups> groups = groupService.findByCourse(Long.valueOf(courseId));

		List<Map<String, String>> res = new ArrayList<Map<String, String>>();

		for (Groups g : groups) {
			Map<String, String> returnMap = new HashMap();
			returnMap.put(String.valueOf(g.getId()), g.getGroupName());
			res.add(returnMap);
		}
		ObjectMapper mapper = new ObjectMapper();
		try {
			json = mapper.writeValueAsString(res);

		} catch (JsonProcessingException e) {
			logger.error("Exception", e);
		}

		/* logger.info("json" + json); */
		return json;

	}

	@Secured("ROLE_FACULTY")
	@RequestMapping(value = "/createRandomGroupsForm", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String createRandomGroupsForm(@ModelAttribute Groups groups,
			Model m, Principal principal, @RequestParam Long courseId) {
		m.addAttribute("webPage", new WebPage("viewGroup", "Create Groups",
				true, false));
		String username = principal.getName();

		List<UserCourse> totalStudentsList = userCourseService
				.findStudentsForFaculty(courseId);

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		m.addAttribute("totalStudentsList", totalStudentsList);

		m.addAttribute("totalStudents", totalStudentsList.size());
		groups.setCourse(courseService.findByID(courseId));
		List<Groups> groupList = groupService.findByFacultyAndCourse(username,
				courseId);
		for (Groups g : groupList) {
			Long cId = g.getCourseId();
			Course course = courseService.findByID(cId);
			String courseName = course.getCourseName();

			g.setCourseName(courseName);

			groupList.set(groupList.indexOf(g), g);
			m.addAttribute("courseName", courseName);
		}
		m.addAttribute("groupList", groupList);
		m.addAttribute("groups", groups);

		return "group/createRandomGroups";
	}

	@Secured("ROLE_FACULTY")
	@RequestMapping(value = "/createRandomGroups", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String createRandomGroups(@ModelAttribute Groups groups, Model m,
			Principal principal, @RequestParam Long courseId,
			RedirectAttributes redirectAttributes) {
		m.addAttribute("webPage", new WebPage("groups", "Create Groups", true,
				false));
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		List<UserCourse> totalStudentsList = userCourseService
				.findStudentsForFaculty(courseId);

		Integer noOfStudents = Integer.valueOf(groups.getNoOfStudents());

		Integer totalStudents = totalStudentsList.size();

		Integer noOfGroups = totalStudents / noOfStudents;

		Integer studentLeft = totalStudents % noOfStudents;

		Integer index = 0;
		Boolean flag = false;
		try {
			if (noOfGroups == 1 && studentLeft >= (totalStudents / 2)) {
				noOfGroups = noOfGroups + 1;

				flag = true;
			}
			for (int i = 1; i <= noOfGroups; i++) {
				groups.setCreatedBy(username);
				groups.setLastModifiedBy(username);
				groups.setFacultyId(username);
				groups.setCourse(courseService.findByID(groups.getCourseId()));
				Course c = courseService.findByID(groups.getCourseId());
				groups.setAcadMonth(c.getAcadMonth());
				groups.setAcadYear(Integer.valueOf(c.getAcadYear()));
				// groups.setGroupName(c.getCourseName() + " Group" + i);
				String groupName = c.getCourseName() + " Group" + i + " "
						+ RandomStringUtils.randomAlphabetic(2);
				groups.setGroupName(groupName);
				groupService.insertWithIdReturn(groups);

				List<StudentGroup> allstudents = studentGroupService
						.getStudentsForGroup(groups.getCourseId());

				groups.setMyId(groups.getId());

				List<StudentGroup> randomStudents = new ArrayList<StudentGroup>();

				index = index + noOfStudents;

				if (allstudents.size() - index == studentLeft && flag == false) {
					// if(allstudents.size() - index == studentLeft){
					Integer index1 = index + studentLeft;

					randomStudents = allstudents.subList(index - noOfStudents,
							index1);

					groups.setNoOfStudents(String.valueOf(noOfStudents
							+ studentLeft));
					groupService.update(groups);
				} else {
					if (index > allstudents.size() && flag == true) {
						randomStudents = allstudents.subList(index
								- noOfStudents, allstudents.size());
						groups.setNoOfStudents(String.valueOf(studentLeft));
						groupService.update(groups);
					} else {
						randomStudents = allstudents.subList(index
								- noOfStudents, index);

					}
				}
				List<String> students = new ArrayList<String>();
				for (StudentGroup sg : randomStudents) {
					students.add(sg.getUsername());

				}

				groups.setStudents(students);
				saveStudentGroupAllocation(groups, m, principal,redirectAttributes);

			}
			setSuccess(redirectAttributes, noOfGroups
					+ " groups created Suucessfully!");
		} catch (Exception e) {

			logger.error(e.getMessage(), e);
			setError(redirectAttributes, "Error in creating groups!");
			setError(m, "Error in creating group");
			return "redirect:/createGroupForm";
		}

		m.addAttribute("groups", groups);

		return "redirect:/searchFacultyGroups";
	}

	@Secured("ROLE_FACULTY")
	@RequestMapping(value = "/viewGroupStudents", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String viewGroupStudents(@ModelAttribute Groups groups,
			Long courseId, @RequestParam(required = false) Long id, Model m,
			Principal principal) {
		m.addAttribute("webPage", new WebPage("groups", "Group Details", true,
				false));
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		List<StudentGroup> students = studentGroupService
				.getStudentsBasedOnGroups(id);

		for (StudentGroup uc : students) {
			User u1 = userService.findByUserName(uc.getUsername());
			uc.setRollNo(u1.getRollNo());
			students.set(students.indexOf(uc), uc);
		}
		Groups g = groupService.findByID(id);
		groups.setGroupName(g.getGroupName());
		m.addAttribute("students", students);
		m.addAttribute("groups", groups);
		return "group/viewGroupMemebers";

	}

	@RequestMapping(value = "/createRandomGroupsJson", method = {
			RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String createRandomGroupsJson(
			@ModelAttribute Groups groups, Model m, Principal principal,
			@RequestParam Long courseId, RedirectAttributes redirectAttributes) {
		m.addAttribute("webPage", new WebPage("groups", "Create Groups", true,
				false));

		logger.info("courseId---->" + String.valueOf(courseId));
		String username = principal.getName();
		List<UserCourse> totalStudentsList = userCourseService
				.findStudentsForFaculty(courseId);
		// List<Groups> createdGroups = new ArrayList<Groups>();
		Map<String, String> map = new HashMap<String, String>();
		List<Groups> createdGroups = new ArrayList<Groups>();

		String json = "";
		Integer noOfStudents = Integer.valueOf(groups.getNoOfStudents());

		Integer totalStudents = totalStudentsList.size();

		Integer noOfGroups = totalStudents / noOfStudents;
		logger.info("noOfStudents---->" + String.valueOf(noOfStudents));

		Integer studentLeft = totalStudents % noOfStudents;
		Integer index = 0;
		Boolean flag = false;
		try {
			if (noOfGroups == 1 && studentLeft >= (totalStudents / 2)) {
				noOfGroups = noOfGroups + 1;

				flag = true;
			}
			for (int i = 1; i <= noOfGroups; i++) {
				groups.setCreatedBy(username);
				groups.setLastModifiedBy(username);
				groups.setFacultyId(username);
				groups.setCourse(courseService.findByID(groups.getCourseId()));
				Course c = courseService.findByID(groups.getCourseId());
				groups.setAcadMonth(c.getAcadMonth());
				groups.setAcadYear(Integer.valueOf(c.getAcadYear()));
				// groups.setGroupName(c.getCourseName() + " Group" + i);
				String groupName = c.getCourseName() + " Group" + i + " "
						+ RandomStringUtils.randomAlphabetic(2);
				groups.setGroupName(groupName);
				groupService.insertWithIdReturn(groups);

				map.put(String.valueOf(groups.getId()), groups.getGroupName());
				List<StudentGroup> allstudents = studentGroupService
						.getStudentsForGroup(groups.getCourseId());

				groups.setMyId(groups.getId());

				List<StudentGroup> randomStudents = new ArrayList<StudentGroup>();

				index = index + noOfStudents;

				if (allstudents.size() - index == studentLeft/* && flag==false */) {
					Integer index1 = index + studentLeft;
					randomStudents = allstudents.subList(index - noOfStudents,
							index1);
					groups.setNoOfStudents(String.valueOf(noOfStudents
							+ studentLeft));
					groupService.update(groups);
				} else {
					if (index > allstudents.size() && flag == true) {
						randomStudents = allstudents.subList(index
								- noOfStudents, allstudents.size());
						groups.setNoOfStudents(String.valueOf(studentLeft));
						groupService.update(groups);
					} else {
						randomStudents = allstudents.subList(index
								- noOfStudents, index);

					}
				}
				List<String> students = new ArrayList<String>();
				for (StudentGroup sg : randomStudents) {
					students.add(sg.getUsername());

				}

				groups.setStudents(students);

				// m.addAttribute("createdGroups", createdGroups);
				saveStudentGroupRandomly(groups, createdGroups, m, principal);

			}
			List<Map<String, String>> res = new ArrayList<Map<String, String>>();

			res.add(map);

			ObjectMapper mapper = new ObjectMapper();
			try {
				json = mapper.writeValueAsString(res);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				setError(m, "Error in creating group");
			}
			setSuccess(redirectAttributes, noOfGroups
					+ " groups created Suucessfully!");
			return json;

		} catch (Exception e) {

			logger.error(e.getMessage(), e);
			setError(redirectAttributes, "Error in creating groups!");
			setError(m, "Error in creating group");
			// return "redirect:/createGroupForm";
		}

		m.addAttribute("groups", groups);
		// m.addAttribute("courseId", groups.getCourseId());
		// return "redirect:/createAssignmentFromGroup?courseId="+courseId;
		return json;

	}

	@Secured("ROLE_FACULTY")
	@RequestMapping(value = "/saveStudentGroupRandomly", method = {
			RequestMethod.GET, RequestMethod.POST })
	public List<Groups> saveStudentGroupRandomly(@ModelAttribute Groups groups,
			List<Groups> createdGroups, Model m, Principal principal) {
		m.addAttribute("webPage", new WebPage("groups", "Create Group", true,
				false));
		Long courseId = groups.getCourseId();
		String username = principal.getName();
		ArrayList<StudentGroup> studentGroupMappingList = new ArrayList<StudentGroup>();
		// List<Groups> createdGroups = new ArrayList<Groups>();
		// createdGroups.add(groups);
		try {
			List<String> stu = groups.getStudents();
			if (stu != null && stu.size() > 0) {
				for (String studentUsername : groups.getStudents()) {
					StudentGroup bean = new StudentGroup();
					bean.setAcadMonth(groups.getAcadMonth());
					bean.setAcadYear(groups.getAcadYear());
					bean.setGroupId(groups.getId());
					bean.setCourseId(groups.getCourseId());
					bean.setUsername(studentUsername);
					bean.setCreatedBy(username);
					bean.setLastModifiedBy(username);
					studentGroupMappingList.add(bean);
				}

				studentGroupService.insertBatch(studentGroupMappingList);
				setSuccess(m, "Group created for "
						+ groups.getStudents().size()
						+ " students successfully");
				// return
				// "redirect:/createAssignmentFromGroup?courseId="+courseId;

				return createdGroups;

			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(m, "Error in allocating group");
			m.addAttribute("webPage", new WebPage("groups", "Create Group",
					false, false));

		}
		m.addAttribute("groups", groups);
		m.addAttribute("courseId", groups.getCourseId());
		m.addAttribute("createdGroups", createdGroups);
		// return "redirect:/createAssignmentFromGroup?courseId="+courseId;
		return createdGroups;
	}

	@Secured("ROLE_FACULTY")
	@RequestMapping(value = "/removeStudentsFromGroupForm", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String removeStudentsFromGroupForm(
			@ModelAttribute Groups groups,
			Model m,
			Principal principal,
			@RequestParam(name = "courseId", required = false, defaultValue = "") String courseId,
			HttpServletRequest request) {
		m.addAttribute("webPage", new WebPage("groups", "Create Group", true,
				false));

		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		if (courseId == null || courseId.isEmpty()) {
			if (request.getSession().getAttribute("courseRecord") == null
					|| request.getSession().getAttribute("courseRecord")
							.equals("")) {

			} else {
				request.getSession().removeAttribute("courseRecord");
			}

		}

		m.addAttribute("allCourses", courseService.findByUserActive(
				principal.getName(), userdetails1.getProgramName()));

		m.addAttribute("preGroup", new ArrayList());
		m.addAttribute("groups", groups);
		m.addAttribute("courseId", courseId);

		/*
		 * Assignment assignments = assignmentService.findByID(id);
		 * assignment.setCourse
		 * (courseService.findByID(assignments.getCourseId()));
		 */
		m.addAttribute(
				"allGroups",
				groupService.findAllGroupsByFaculty(principal.getName(),
						Long.parseLong(userdetails1.getProgramId())));
		return "group/removeStudentsFromGroup";
	}

	@Secured("ROLE_FACULTY")
	@RequestMapping(value = "/removeStudentsFromGroup", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String removeStudentsFromGroup(@ModelAttribute Groups groups,
			Model m, Principal principal, @RequestParam String id) {
		m.addAttribute("webPage", new WebPage("groups", "Create Group", true,
				false));

		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		List<StudentGroup> students = studentGroupService
				.getStudentsBasedOnGroups(Long.valueOf(id));
		if (students.isEmpty() || students.size() == 0) {
			setNote(m, "No students allocated to group!");
		}

		for (StudentGroup uc : students) {
			User u1 = userService.findByUserName(uc.getUsername());
			uc.setRollNo(u1.getRollNo());
			students.set(students.indexOf(uc), uc);
		}

		m.addAttribute("students", students);
		m.addAttribute("preGroup", new ArrayList());

		m.addAttribute(
				"allGroups",
				groupService.findAllGroupsByFaculty(principal.getName(),
						Long.parseLong(userdetails1.getProgramId())));
		m.addAttribute(
				"allCourses",
				courseService.findByUserActive(username,
						userdetails1.getProgramName()));
		return "group/removeStudentsFromGroup";
	}

	@Secured("ROLE_FACULTY")
	@RequestMapping(value = "/removeStudents", method = { RequestMethod.GET,
			RequestMethod.POST })
	public @ResponseBody String removeStudents(@RequestParam("id") Long id,
			Model m, Principal principal, @ModelAttribute StudentGroup groups) {

		StudentGroup sg = studentGroupService.findByID(id);
		String username = principal.getName();
		try {
			studentGroupService.removeStudentsFromGroup(id);
			// studentAssignmentService.setStudentInActive(sg.getGroupId(),sg.getUsername());
			studentAssignmentService.deleteStudent(sg.getGroupId(),
					sg.getUsername());
			Groups g = groupService.findByID(sg.getGroupId());
			Integer noOfStudents = Integer.valueOf(g.getNoOfStudents());
			g.setNoOfStudents(String.valueOf(noOfStudents - 1));
			groupService.update(g);
			return "Success";
		} catch (Exception e) {

			logger.error("Error " + e.getMessage());
			return "Error";
		}

	}

	@RequestMapping(value = "/getGroupsByCourseForReAllocation", method = {
			RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String getGroupsByCourseForReAllocation(
			@RequestParam(name = "courseId") String courseId,
			Principal principal) {
		String json = "";
		String username = principal.getName();

		List<Groups> groups = groupService.findByFacultyAndCourse(username,
				Long.valueOf(courseId));

		List<Map<String, String>> res = new ArrayList<Map<String, String>>();

		for (Groups g : groups) {
			Map<String, String> returnMap = new HashMap();
			returnMap.put(String.valueOf(g.getId()), g.getGroupName());
			res.add(returnMap);
		}
		ObjectMapper mapper = new ObjectMapper();
		try {
			json = mapper.writeValueAsString(res);

		} catch (JsonProcessingException e) {
			logger.error("Exception", e);
		}

		/* logger.info("json" + json); */
		return json;

	}

}
