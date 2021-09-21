package com.spts.lms.web.controllers;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.spts.lms.auth.Token;
import com.spts.lms.beans.BaseBean;
import com.spts.lms.beans.course.Course;
import com.spts.lms.beans.user.Role;
import com.spts.lms.beans.user.User;
import com.spts.lms.services.course.CourseService;

public class BaseController {

	@Autowired
	CourseService courseService;
	private static final Logger logger = Logger.getLogger(BaseController.class);
	protected final int pageSize = 20;

	protected HashMap<Long, String> courseIdNameMap = new HashMap<Long, String>();

	@Value("${lms.acad.years}")
	private String[] acadYears;

	@Value("${lms.acad.months}")
	private String[] acadMonths;

	@Value("${lms.acad.sessions}")
	private String[] sessionList;

	@Value("${lms.institute.name}")
	private String instituteName;

	@ModelAttribute("acadYears")
	public String[] getAcadYears() {
		return acadYears;
	}

	@ModelAttribute("acadMonths")
	public String[] getAcadMonths() {
		return acadMonths;
	}

	@ModelAttribute("sessionList")
	public String[] getSessionList() {
		return sessionList;
	}

	@ModelAttribute("instituteName")
	public String getInstituteName() {
		return instituteName;
	}

	public String getUserName() {
		if (getUser() == null)
			return null;
		return getUser().getUsername();
	}

	public boolean hasRole(Role role) {
		return getUser() == null ? false : getUser().hasRole(role);
	}

	@ModelAttribute("currentUser")
	public User getUser() {
		return null;
	}

	@ModelAttribute("currentCourses")
	public List<Course> getCurrentCourses(Principal principal) {
		if (principal != null) {
			String username = principal.getName();
			Token userDetails = (Token) principal;

			// UsernamePasswordAuthenticationToken userDetails =
			// (UsernamePasswordAuthenticationToken) principal;

			if (userDetails.getProgramName().equals("INTDR")) {
				return courseService
						.findByUserActiveInterdesciplinary(username);
			} else {
				return courseService.findByUserActive(username,
						userDetails.getProgramName());
			}
		}
		return null;
	}

	@ModelAttribute("courseIdNameMap")
	public HashMap<Long, String> getCourseIdNameMap() {
		if (this.courseIdNameMap.size() == 0) {
			courseIdNameMap = courseService.getCourseIdNameMap();
		}
		return this.courseIdNameMap;
	}

	public boolean checkSession(HttpServletRequest request,
			HttpServletResponse respnse) {
		String userId = (String) request.getSession().getAttribute("userId");
		if (userId != null) {
			return true;
		} else {
			request.setAttribute("error", "true");
			request.setAttribute("errorMessage",
					"Session Expired! Please login again.");
			return false;
		}
	}

	public void setError(HttpServletRequest request, String errorMessage) {
		request.setAttribute("error", "true");
		request.setAttribute("errorMessage", errorMessage);
	}

	public void setSuccess(HttpServletRequest request, String successMessage) {
		request.setAttribute("success", "true");
		request.setAttribute("successMessage", successMessage);
	}

	public void setSuccess(Model m, String successMessage) {
		m.addAttribute("success", "true");
		m.addAttribute("successMessage", successMessage);
	}

	public void setNote(HttpServletRequest request, String errorMessage) {
		request.setAttribute("note", "true");
		request.setAttribute("noteMessage", errorMessage);
	}

	public void setNote(Model m, String successMessage) {
		m.addAttribute("note", "true");
		m.addAttribute("noteMessage", successMessage);
	}

	public void setNote(RedirectAttributes redirectAttrs, String successMessage) {
		redirectAttrs.addFlashAttribute("note", "true");
		redirectAttrs.addFlashAttribute("noteMessage", successMessage);
	}

	public void setError(Model m, String errorMessage) {
		m.addAttribute("error", "true");
		m.addAttribute("errorMessage", errorMessage);
	}

	public void setErrorList(Model m, String errorMessage,
			List<? extends BaseBean> errorList) {
		m.addAttribute("error", "true");
		StringBuilder sb = new StringBuilder(errorMessage);
		for (BaseBean bean : errorList) {
			sb.append("<br/>").append(bean.getErrorMessage());
		}
		m.addAttribute("errorMessage", sb.toString());
	}

	public void setSuccess(RedirectAttributes redirectAttrs,
			String successMessage) {
		redirectAttrs.addFlashAttribute("success", "true");
		redirectAttrs.addFlashAttribute("successMessage", successMessage);
	}

	public void setError(RedirectAttributes redirectAttrs, String errorMessage) {
		redirectAttrs.addFlashAttribute("error", "true");
		redirectAttrs.addFlashAttribute("errorMessage", errorMessage);
	}

	public void setErrorList(RedirectAttributes redirectAttrs,
			String errorMessage, List<? extends BaseBean> errorList) {
		redirectAttrs.addFlashAttribute("error", "true");
		StringBuilder sb = new StringBuilder(errorMessage + "<br/>");
		for (BaseBean bean : errorList) {
			sb.append("<br/>").append(bean.getErrorMessage());
		}
		redirectAttrs.addFlashAttribute("errorMessage", sb.toString());
	}

	protected <T> String getQueryString(final T bean) {
		StringBuilder query = new StringBuilder();

		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
			PropertyDescriptor[] pdList = beanInfo.getPropertyDescriptors();
			for (PropertyDescriptor pd : pdList) {
				Method readMethod = null;
				try {
					readMethod = pd.getReadMethod();
				} catch (Exception e) {
				}

				if (readMethod == null) {
					continue;
				}

				String propertyName = pd.getName();
				Object val = readMethod.invoke(bean);
				if ("class".equals(propertyName)) {
					continue;
				}
				if (val instanceof String || val instanceof Short
						|| val instanceof Integer || val instanceof Long
						|| val instanceof Double) {
					if (val == null || val.equals(readMethod.getDefaultValue())) {
						continue;
					}

					if (val instanceof String && ((String) val).equals("")) {
						continue;
					}
					if (val instanceof Short
							&& ((Short) val).equals(Short.valueOf("0"))) {
						continue;
					}
					if (val instanceof Integer
							&& ((Integer) val).equals(Integer.valueOf("0"))) {
						continue;
					}
					if (val instanceof Long
							&& ((Long) val).equals(Long.valueOf("0"))) {
						continue;
					}
					if (val instanceof Double
							&& ((Double) val).equals(Double.valueOf("0"))) {
						continue;
					}

					query.append(propertyName);
					query.append("=");
					query.append(val);
					query.append("&");

				}

			}
		} catch (IntrospectionException ite) {
			logger.error("Exception", ite);
		} catch (IllegalAccessException e) {
			logger.error("Exception", e);
		} catch (IllegalArgumentException e) {
			logger.error("Exception", e);
		} catch (InvocationTargetException e) {
			logger.error("Exception", e);
		}

		return query.length() > 0 ? query.substring(0, query.length() - 1)
				: query.toString();
	}

	public List<Course> getCourseBasedOnUser(
			UsernamePasswordAuthenticationToken userDetails, Principal p) {
		Token userdetails1 = (Token) p;

		List<Course> listOfCourse = null;
		if (userDetails.getAuthorities().contains(Role.ROLE_FACULTY)) {
			listOfCourse = courseService.findByUserActive(p.getName(),
					userdetails1.getProgramName());
		} else if (userDetails.getAuthorities().contains(Role.ROLE_ADMIN)) {
			listOfCourse = courseService.getModulesForTest(p.getName(), userdetails1.getProgramId());
		} else if (userDetails.getAuthorities().contains(Role.ROLE_DEAN)) {
			listOfCourse = courseService.findAll();
		} else if (userDetails.getAuthorities().contains(Role.ROLE_STUDENT)) {
			String acadYear = "";
			String acadMonth = "";
			listOfCourse = courseService.findByUserActive(p.getName(),
					userdetails1.getProgramName());
		}

		return listOfCourse;

	}

}
