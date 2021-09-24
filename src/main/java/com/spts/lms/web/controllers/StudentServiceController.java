package com.spts.lms.web.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.util.URIUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;
import org.apache.poi.xssf.usermodel.XSSFDataValidationConstraint;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spts.lms.auth.Token;
import com.spts.lms.beans.LmsDb;
import com.spts.lms.beans.Status;
import com.spts.lms.beans.StatusType;
import com.spts.lms.beans.StudentService.BonafideForm;
import com.spts.lms.beans.StudentService.HostelForm;
import com.spts.lms.beans.StudentService.RailwayForm;
import com.spts.lms.beans.StudentService.StudentHostelForm;
import com.spts.lms.beans.StudentService.StudentServiceBean;
import com.spts.lms.beans.portalFeedback.PortalFeedback;
import com.spts.lms.beans.portalFeedback.PortalFeedbackQuery;
import com.spts.lms.beans.portalFeedback.PortalFeedbackResponse;
import com.spts.lms.beans.portalFeedback.SortByCreatedDate;
import com.spts.lms.beans.user.FacultyDetails;
import com.spts.lms.beans.user.Role;
import com.spts.lms.beans.user.User;
import com.spts.lms.beans.user.UserRole;
import com.spts.lms.beans.user.UserTo;
import com.spts.lms.helpers.excel.ExcelReader;
import com.spts.lms.services.StudentService.BonafideFormService;
import com.spts.lms.services.StudentService.HostelFormService;
import com.spts.lms.services.StudentService.RailwayFormService;
import com.spts.lms.services.StudentService.StudentHostelFormService;
import com.spts.lms.services.StudentService.StudentService;
import com.spts.lms.services.user.UserRoleService;
import com.spts.lms.services.user.UserService;
import com.spts.lms.utils.MultipleDBConnection;
import com.spts.lms.web.helper.WebPage;

@Controller
public class StudentServiceController extends BaseController {

	private static final Logger logger = Logger
			.getLogger(StudentServiceController.class);

	@Autowired
	UserService userService;

	@Autowired
	StudentService studentWsService;

	@Autowired
	BonafideFormService bonafideFormService;

	@Autowired
	RailwayFormService railwayFormService;

	@Autowired
	HostelFormService hostelFormService;

	@Autowired
	StudentHostelFormService studentHostelFormService;

	@Autowired
	ExcelReader excelReader;

	@Autowired
	UserRoleService userRoleService;

	@Value("${lms.assignment.downloadAllFolder}")
	private String downloadAllFolder;

	@Value("${userMgmtCrudUrl}")
	private String userRoleMgmtCrudUrl;

	@Value("${app}")
	private String app;

	Client client = ClientBuilder.newClient();
	@Secured({ "ROLE_ADMIN" ,"ROLE_STUDENT" })
	@RequestMapping(value = "/createBFServiceForm", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String createBFServiceForm(Model m, Principal principal,
			@RequestParam Long serviceId) {
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);
		

		if (serviceId != null) {
			StudentServiceBean ss = studentWsService.findByID(serviceId);
			m.addAttribute("studentServiceBean", ss);
		}
		m.addAttribute("username", username);

		return "studentService/createBFService";

	}
	@Secured({ "ROLE_ADMIN" ,"ROLE_STUDENT" })
	@RequestMapping(value = "/createBFService", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String createBFService(Model m, Principal principal,
			@ModelAttribute StudentServiceBean studentServiceBean) {
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);
		
		try {
			studentServiceBean.setActive("Y");
			studentServiceBean.setCreatedBy(username);
			studentServiceBean.setLastModifiedBy(username);
			studentServiceBean.setLevel3(studentServiceBean.getLevel1());
			/*
			 * SimpleDateFormat dateFormat = new SimpleDateFormat( "HH:mm:ss");
			 * new Date().getTime()
			 * studentServiceBean.setDuration(studentServiceBean
			 * .getDuration().getTime()); cellValue = dateFormat.format(cell
			 * .getDateCellValue());
			 */
			studentServiceBean.setDuration(studentServiceBean.getDuration());
			studentWsService.insertWithIdReturn(studentServiceBean);
			setSuccess(m, "Service created successfully!");
		} catch (Exception e) {
			setError(m, "Error in creating service!");
			logger.error(e);
			return "redirect:/createServiceForm";

		}

		return "studentService/createBFService";
	}

	/*
	 * @RequestMapping(value = "/updateBFService", method = { RequestMethod.GET,
	 * RequestMethod.POST }) public String updateBFService(Model m, Principal
	 * principal,
	 * 
	 * @ModelAttribute StudentServiceBean studentServiceBean) { String username
	 * = principal.getName();
	 * 
	 * Token userdetails1 = (Token) principal; String ProgramName =
	 * userdetails1.getProgramName(); User u =
	 * userService.findByUserName(username);
	 * logger.info("ACAD SESSION------------------------->" +
	 * u.getAcadSession()); StudentServiceBean dbBean = studentWsService
	 * .findByID(studentServiceBean.getId()); try { if (studentServiceBean !=
	 * null) { studentServiceBean.setActive("Y"); //
	 * studentServiceBean.setCreatedBy(username);
	 * studentServiceBean.setLastModifiedBy(username); //
	 * studentWsService.insertWithIdReturn(studentServiceBean);
	 * studentServiceBean.setDuration(studentServiceBean.getDuration() +
	 * ":00:00"); studentServiceBean.setLevel3(studentServiceBean.getLevel1());
	 * studentWsService.update(studentServiceBean); setSuccess(m,
	 * "Service created successfully!"); } } catch (Exception e) { setError(m,
	 * "Error in creating service!"); logger.error(e); return
	 * "redirect:/createBFServiceForm";
	 * 
	 * }
	 * 
	 * return "studentService/createBFService"; }
	 */
	@Secured({ "ROLE_ADMIN" ,"ROLE_STUDENT" })
	@RequestMapping(value = "/updateBFService", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String updateBFService(Model m, Principal principal,
			@ModelAttribute StudentServiceBean studentServiceBean,
			RedirectAttributes redirectAttributes) {
		String username = principal.getName();
		

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);
		
		StudentServiceBean dbBean = studentWsService
				.findByID(studentServiceBean.getId());
		try {
			List<User> userList = new ArrayList<User>();
			if (studentServiceBean != null) {
				studentServiceBean.setActive("Y");
				studentServiceBean.setLastModifiedBy(username);
				studentServiceBean.setDuration(studentServiceBean.getDuration()
						+ ":00:00");
				String level2 = null;
				User user2 = new User();
				User user3 = new User();
				// studentServiceBean.setLevel3(studentServiceBean.getLevel1());
				if (studentServiceBean.getLevel2() != null) {
					if (!studentServiceBean.getLevel2().endsWith("STAFF")) {
						level2 = studentServiceBean.getLevel2();
						studentServiceBean.setLevel2(studentServiceBean
								.getLevel2().trim() + "_STAFF");

					} else {
						level2 = studentServiceBean.getLevel2().substring(0,
								studentServiceBean.getLevel2().length() - 6);
					}
					user2.setUsername(level2);
					user2.setRole("ROLE_STAFF");
					user2.setSchoolAbbr(app);
					userList.add(user2);
				}
				if (studentServiceBean.getLevel2() != null) {
					String level3 = studentServiceBean.getLevel3();
					if (!studentServiceBean.getLevel3().endsWith("STAFF")) {
						level3 = studentServiceBean.getLevel3();
						studentServiceBean.setLevel3(studentServiceBean
								.getLevel3().trim() + "_STAFF");

					} else {
						level3 = studentServiceBean.getLevel3().substring(0,
								studentServiceBean.getLevel3().length() - 6);
					}

					user3.setUsername(level3);
					user3.setRole("ROLE_STAFF");

					user3.setSchoolAbbr(app);

					userList.add(user3);
				}

				/*
				 * User user3 = new User();
				 * 
				 * if (userService.findByUserName(level3) == null) { if
				 * (level3.endsWith("ADMIN")) { user3.setUsername(level3);
				 * user3.setSchoolAbbr(app); user3.setRole("ROLE_ADMIN"); } else
				 * if (level3.endsWith("STAFF")) { user3.setUsername(level3);
				 * user3.setSchoolAbbr(app); user3.setRole("ROLE_STAFF");
				 * 
				 * } else { user3.setUsername(level3);
				 * //user3.setUsername(level3 + "_STAFF");
				 * user3.setSchoolAbbr(app); user3.setRole("ROLE_STAFF");
				 * 
				 * } userList.add(user3); }
				 */

				List<UserRole> userRoleList = new ArrayList<UserRole>();
				// User user = new User();

				ObjectMapper mapper = new ObjectMapper();
				/*
				 * User user = new User(); user.setUsername(level2.trim());
				 * user.setSchoolAbbr(app); user.setRole("ROLE_STAFF");
				 */

				// userList.add(user);
				if (userList.size() != 0) {
					for (User uzr : userList) {
						String json = mapper.writeValueAsString(uzr);
						
						// logger.info(" json--->" + (json));
						WebTarget webTarget = client.target(URIUtil
								.encodeQuery(userRoleMgmtCrudUrl
										+ "/addOtherUserStaffService?json="
										+ json));
						Invocation.Builder invocationBuilder = webTarget
								.request(MediaType.APPLICATION_JSON);
						String resp = invocationBuilder.get(String.class);
						
						ObjectMapper objMapper = new ObjectMapper();
						Status status = objMapper.readValue(resp, Status.class);
						
						if (status.getStatus().SUCCESS != null) {
							UserTo userTo;

							// for (User uzr : userList) {
							
							User user = new User();
							User newUser = userService.findByUserName(uzr
									.getUsername());
							if (newUser != null) {

								user = newUser;
								user.setUsername(uzr.getUsername() + "_STAFF");
							} else {
								

								user.setUsername(uzr.getUsername() + "_STAFF");
								if (status.getUser() != null) {
									// for (UserTo uz : status.getUserList()) {
									// user.convert(uz);

									// }
									userTo = status.getUser();
									user.convert(userTo);

								}
								user.setPassword("$2a$11$PSt4aIR0G2VQhHKqqLwFTeETkXAtS8fXs/2CfNnWykl6KbxNe.l1q");
								user.setCreatedBy("CA");
								user.setLastModifiedBy("CA");
								user.setEnabled(true);
								user.setAttempts(0);
								user.setAcadSession(null);
								user.setRollNo(null);
								user.setCampusId(null);
								user.setCampusName(null);
							}
							UserRole userRole = new UserRole();
							userRole.setUsername(user.getUsername());
							userRole.setRole(Role.ROLE_STAFF);
							userRole.setCreatedBy("CA");
							userRole.setLastModifiedBy("CA");
							userRole.setUser(user);

							UserRole userRole1 = new UserRole();
							userRole1.setUsername(user.getUsername());
							userRole1.setRole(Role.ROLE_USER);
							userRole1.setCreatedBy("CA");
							userRole1.setLastModifiedBy("CA");
							userRole1.setUser(user);
							userRoleList.add(userRole);
							userRoleList.add(userRole1);

							

							userService.upsert(user);
							userRoleService.upsertBatch(userRoleList);
							// studentWsService.update(studentServiceBean);
							// }

						} else {
							setError(m, "Error in creating service!");
							return "redirect:/createBFServiceForm?serviceId="
									+ studentServiceBean.getId();
						}
					}
				}
				studentWsService.update(studentServiceBean);
				setSuccess(redirectAttributes, "Service created successfully!");
				// setSuccess(m, "Service created successfully!");
				m.addAttribute("studentServiceBean", studentServiceBean);

			}
		} catch (Exception e) {
			setError(redirectAttributes, "Error in creating service!");
			logger.error(e);
			return "redirect:/createBFServiceForm?serviceId="
					+ studentServiceBean.getId();

		}

		return "redirect:/createBFServiceForm?serviceId="
				+ studentServiceBean.getId();
	}
	@Secured({ "ROLE_ADMIN" ,"ROLE_STUDENT" })
	@RequestMapping(value = "/viewBFServices", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String viewBFServices(Model m, Principal principal) {
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);
		
		List<StudentServiceBean> serviceList = studentWsService
				.findServiceByName("Bonafide Service");
		m.addAttribute("serviceList", serviceList);
		if (serviceList.size() != 0)
			if (userdetails1.getAuthorities().contains(Role.ROLE_STUDENT)) {
				for (StudentServiceBean sb : serviceList) {
					BonafideForm bonafide = bonafideFormService
							.findStudentBonafide(username, sb.getId());
					if (bonafide != null) {
						User user1 = userService.findByUserName(bonafide
								.getLevel1());
						User user2 = userService.findByUserName(bonafide
								.getLevel2());
						User user3 = userService.findByUserName(bonafide
								.getLevel3());
						
						bonafide.setUser1(user1.getFirstname() + " "
								+ user1.getLastname());
						bonafide.setUser2(user2.getFirstname() + " "
								+ user2.getLastname());
						bonafide.setUser3(user3.getFirstname() + " "
								+ user3.getLastname());
						

						sb.setBonafide(bonafide);
					}

					serviceList.set(serviceList.indexOf(sb), sb);
				}
			}
		return "studentService/viewBFServices";

	}
	@Secured({ "ROLE_ADMIN" ,"ROLE_STUDENT" })
	@RequestMapping(value = "/viewServices", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String viewServices(Model m, Principal principal) {
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);
		
		List<StudentServiceBean> serviceList = studentWsService.findAllAvail();
		m.addAttribute("serviceList", serviceList);
		/*
		 * if (serviceList.size() != 0) if
		 * (userdetails1.getAuthorities().contains(Role.ROLE_STUDENT)) { for
		 * (StudentServiceBean sb : serviceList) {
		 * 
		 * RailwayForm railway = railwayFormService.findStudentRC( username,
		 * sb.getId()); if (railway != null) { User user1 =
		 * userService.findByUserName(railway .getLevel1()); User user2 =
		 * userService.findByUserName(railway .getLevel2()); User user3 =
		 * userService.findByUserName(railway .getLevel3());
		 * logger.info("user1 --- " + user1);
		 * railway.setUser1(user1.getFirstname() + " " + user1.getLastname());
		 * railway.setUser2(user2.getFirstname() + " " + user2.getLastname());
		 * railway.setUser3(user3.getFirstname() + " " + user3.getLastname());
		 * logger.info("railway ----- " + railway);
		 * 
		 * sb.setRailway(railway); }
		 * 
		 * serviceList.set(serviceList.indexOf(sb), sb); } }
		 */
		return "studentService/viewServices";

	}
	@Secured({ "ROLE_ADMIN" ,"ROLE_STUDENT" })
	@RequestMapping(value = "/viewRCServices", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String viewRCServices(Model m, Principal principal) {
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);
		
		List<StudentServiceBean> serviceList = studentWsService
				.findServiceByName("Railway Service");
		m.addAttribute("serviceList", serviceList);
		if (serviceList.size() != 0)
			if (userdetails1.getAuthorities().contains(Role.ROLE_STUDENT)) {
				for (StudentServiceBean sb : serviceList) {

					RailwayForm railway = railwayFormService.findStudentRC(
							username, sb.getId());
					if (railway != null) {
						User user1 = userService.findByUserName(railway
								.getLevel1());
						User user2 = userService.findByUserName(railway
								.getLevel2());
						User user3 = userService.findByUserName(railway
								.getLevel3());
						
						railway.setUser1(user1.getFirstname() + " "
								+ user1.getLastname());
						railway.setUser2(user2.getFirstname() + " "
								+ user2.getLastname());
						railway.setUser3(user3.getFirstname() + " "
								+ user3.getLastname());
						

						sb.setRailway(railway);
					}

					serviceList.set(serviceList.indexOf(sb), sb);
				}
			}
		return "studentService/viewRCServices";

	}
	@Secured({ "ROLE_ADMIN" ,"ROLE_STUDENT" })
	@RequestMapping(value = "/viewCreateServiceForm", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String viewCreateServiceForm(Model m, Principal principal) {
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);
		

		StudentServiceBean studentServiceBean = new StudentServiceBean();
		m.addAttribute("studentServiceBean", studentServiceBean);
		List<StudentServiceBean> serviceList = studentWsService.findAllActive();
		m.addAttribute("serviceList", serviceList);
		m.addAttribute("username", username);
		return "studentService/viewCreateServices";

	}

	@RequestMapping(value = "/viewBonafideServiceForStaff", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String viewServiceForStaff(Model m, Principal principal,
			@RequestParam Long serviceId) {
		/*
		 * m.addAttribute("webPage", new WebPage("viewAssignment",
		 * "Bonafide Service", true, false));
		 */
		m.addAttribute("webPage", new WebPage("evaluateAssignment",
				"Bonafide Service", true, false));
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);
		

		StudentServiceBean studentServiceBean = studentWsService
				.findByID(serviceId);
		m.addAttribute("serviceId", serviceId);
		m.addAttribute("studentServiceBean", studentServiceBean);
		List<BonafideForm> bList = new ArrayList<BonafideForm>();
		int levelNo = 1;
		if (studentServiceBean.getLevel2().equalsIgnoreCase(username)) {
			levelNo = 2;
			bList = bonafideFormService
					.findAllSubmittedStudentsByServiceIdFlag2(serviceId);
		} else {
			bList = bonafideFormService
					.findAllSubmittedStudentsByServiceIdFlag1(serviceId);
		}

		m.addAttribute("bList", bList);
		if (bList.isEmpty() || bList.size() == 0) {
			setNote(m, "no Records Found!");
		}
		Integer levelNum = 1;
		/*
		 * if (studentServiceBean.getLevel1().equalsIgnoreCase(username)) { for
		 * (BonafideForm bf : bList) { if (bf.isFlag1()) {
		 * bf.setApprovedLevel1("true"); bf.setApprovedLevel("true");
		 * bList.set(bList.indexOf(bf), bf); } } m.addAttribute("showActions",
		 * true); } else if
		 * (studentServiceBean.getLevel2().equalsIgnoreCase(username)) { for
		 * (BonafideForm bf : bList) { if (bf.isFlag1()) { if (bf.isFlag2()) {
		 * bf.setApprovedLevel2("true"); bf.setApprovedLevel("true"); } else {
		 * bf.setAllowLevel2("true"); } bList.set(bList.indexOf(bf), bf); }
		 * 
		 * } } else if
		 * (studentServiceBean.getLevel3().equalsIgnoreCase(username)) { for
		 * (BonafideForm bf : bList) { if (bf.isFlag1() && bf.isFlag2()) { if
		 * (bf.isFlag3()) { bf.setApprovedLevel3("true");
		 * bf.setApprovedLevel("true"); } else { bf.setAllowLevel3("true"); }
		 * bList.set(bList.indexOf(bf), bf); }
		 * 
		 * } }
		 */
		/*
		 * if (studentServiceBean.getLevel1().equalsIgnoreCase(username)) {
		 * levelNum = 1; for (BonafideForm bf : bList) { if
		 * (bf.getFlag1().equals("APPROVED")) { bf.setApprovedLevel1("true");
		 * bf.setApprovedLevel("true"); bList.set(bList.indexOf(bf), bf);
		 * 
		 * } if (bf.getFlag1().equals("PENDING")) {
		 * 
		 * m.addAttribute("showRemark", true); }
		 * 
		 * }
		 * 
		 * m.addAttribute("showActions", true); } else
		 */if (studentServiceBean.getLevel2().equalsIgnoreCase(username)) {
			levelNum = 2;
			for (BonafideForm bf : bList) {
				if (bf.getFlag1().equals("APPROVED")) {
					if (bf.getFlag2().equals("APPROVED")) {
						bf.setApprovedLevel2("true");
						bf.setApprovedLevel("true");
					} else if (bf.getFlag2().equals("PENDING")) {
						bf.setAllowLevel2("true");
						bf.setAllowRemark2("true");
					}
					bList.set(bList.indexOf(bf), bf);
				}

			}
		} else if (studentServiceBean.getLevel3().equalsIgnoreCase(username)) {
			levelNum = 3;
			for (BonafideForm bf : bList) {
				if (bf.getFlag1().equals("APPROVED")
						&& bf.getFlag2().equals("APPROVED")) {
					if (bf.getFlag3().equals("APPROVED")) {
						bf.setApprovedLevel3("true");
						bf.setApprovedLevel("true");
					} else if (bf.getFlag3().equals("PENDING")) {
						bf.setAllowLevel3("true");
						bf.setAllowRemark3("true");
					}
					bList.set(bList.indexOf(bf), bf);
				}

			}
		}
		m.addAttribute("levelNum", levelNum);
		m.addAttribute("bonafide", new BonafideForm());

		return "studentService/viewServiceForStaff";

	}

	@RequestMapping(value = "/approveBonafideApplication", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String approveBonafideApplication(Model m, Principal principal,
			@RequestParam Long id, @RequestParam String status) {
		m.addAttribute("webPage", new WebPage("viewAssignment",
				"Bonafide Service", true, false));
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);
		
		BonafideForm bonafide = bonafideFormService.findByID(id);

		try {

			Integer flagNo = null;
			if (bonafide.getLevel1().equalsIgnoreCase(username)) {
				flagNo = 1;
			} else if (bonafide.getLevel2().equalsIgnoreCase(username)) {
				flagNo = 2;
			} else if (bonafide.getLevel3().equalsIgnoreCase(username)) {
				flagNo = 3;
			}

			bonafideFormService.updateFlags(flagNo, status, id);

		} catch (Exception e) {

		}
		return "redirect:/viewBonafideServiceForStaff?serviceId="
				+ bonafide.getServiceId();

	}
	@Secured({ "ROLE_ADMIN" ,"ROLE_STUDENT" })
	@RequestMapping(value = "/downloadStudentsBonafideForms", method = RequestMethod.GET)
	public void downloadStudentsBonafideForms(HttpServletResponse response,
			Principal principal, @RequestParam Long serviceId,
			@RequestParam String levelNum) {
		
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);
		
		InputStream is = null;
		String filePath = "";
		StudentServiceBean studentServiceBean = studentWsService
				.findByID(serviceId);
		try {
			// get your file as InputStream
			List<BonafideForm> bList = new ArrayList<BonafideForm>();
			int levelNo = 1;
			if (studentServiceBean.getLevel2().equalsIgnoreCase(username)) {
				levelNo = 2;
				bList = bonafideFormService
						.findAllSubmittedStudentsByServiceIdFlag2(serviceId);
			} /*
			 * else { bList = bonafideFormService
			 * .findAllSubmittedStudentsByServiceIdFlag1(serviceId); }
			 */
			

			filePath = getBonafideApplications(bList, username);
			is = new FileInputStream(filePath);
			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			// copy it to response's OutputStream
			org.apache.commons.io.IOUtils.copy(is, response.getOutputStream());
			response.flushBuffer();

			response.flushBuffer();
		} catch (Exception ex) {
			logger.info(
					"Error writing file to output stream. Filename was '{}'",
					ex);
			throw new RuntimeException("IOError writing file to output stream");
		} finally {
			if (is != null) {
				org.apache.commons.io.IOUtils.closeQuietly(is);
			}

			FileUtils.deleteQuietly(new File(filePath));
		}

	}

	public String getRCApplications(List<RailwayForm> bList, String username) {
		String filePath = downloadAllFolder + File.separator
				+ "BonafideStudentApplications.xlsx";
		String h[] = { "Application ID", "Date", "Sex", "Year", "SAPId",
				"Name", "DOB", "Class & Monthly /Quarterly", "From Station",
				"To Station", "Address", "Status", "Remarks" };
		List<String> header = Arrays.asList(h);
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("Railway Concession Sheet");
		Row headerRow = sheet.createRow(0);
		for (int colNum = 0; colNum < header.size(); colNum++) {
			Cell cell = headerRow.createCell(colNum);
			cell.setCellValue(header.get(colNum));
		}
		int rowNum = 1;

		for (RailwayForm bf : bList) {
			Row row = sheet.createRow(rowNum);
			row.createCell(0).setCellValue(bf.getId());
			row.createCell(1).setCellValue(bf.getDate());
			row.createCell(2).setCellValue(bf.getSex());
			row.createCell(3).setCellValue(bf.getYear());
			row.createCell(4).setCellValue(bf.getUsername());
			// row.createCell(5).setCellValue(bf.getMotherName());
			row.createCell(5).setCellValue(
					bf.getFirstname() + " " + bf.getLastname());
			row.createCell(6).setCellValue(bf.getDob());
			row.createCell(7).setCellValue(bf.getType());
			row.createCell(8).setCellValue(bf.getFromStation());
			row.createCell(9).setCellValue(bf.getToStation());
			row.createCell(10).setCellValue(bf.getAddress());
			/*
			 * if (bf.getLevel1().equalsIgnoreCase(username)) {
			 * row.createCell(11).setCellValue(bf.getFlag1());
			 * row.createCell(12).setCellValue(bf.getRemark1()); } else
			 */if (bf.getLevel2().equalsIgnoreCase(username)) {
				row.createCell(11).setCellValue(bf.getFlag2());
				row.createCell(12).setCellValue(bf.getRemark2());
			} else if (bf.getLevel3().equalsIgnoreCase(username)) {
				row.createCell(11).setCellValue(bf.getFlag2());
				row.createCell(12).setCellValue(bf.getRemark2());
			}

			rowNum++;

		}
		XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheet);
		String[] actions = new String[] { "PENDING,APPROVED,REJECTED" };
		XSSFDataValidationConstraint constraint = (XSSFDataValidationConstraint) dvHelper
				.createExplicitListConstraint(actions);
		CellRangeAddressList addressList = new CellRangeAddressList(1, 100, 11,
				11);
		XSSFDataValidation validation = (XSSFDataValidation) dvHelper
				.createValidation(constraint, addressList);
		validation.setShowErrorBox(true);
		sheet.addValidationData(validation);
		try {
			FileOutputStream outputStream = new FileOutputStream(filePath);
			workbook.write(outputStream);

			workbook.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return filePath;

	}

	@RequestMapping(value = "/uploadStudentsBonafideForms", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String uploadStudentsBonafideForms(Model m, Principal principal,
			RedirectAttributes redirectAttributes,
			@ModelAttribute StudentServiceBean studentServiceBean,
			@RequestParam("file") MultipartFile file) {
		
		m.addAttribute("webPage", new WebPage("viewAssignment",
				"Bonafide Service", true, false));
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);
		
		if (file.isEmpty()) {
			setNote(redirectAttributes, "File is empty!");

		} else {

			StudentServiceBean serviceBean = studentWsService
					.findByID(studentServiceBean.getId());

			String statusOfUpload = uploadFileAndReturnStatus(file, username,
					serviceBean);
			if (statusOfUpload.equalsIgnoreCase("SUCCESS")) {
				setSuccess(redirectAttributes, "File uploaded successfully!");
			} else {
				setError(redirectAttributes, "Error while uploading file!");
			}
		}

		return "redirect:/viewBonafideServiceForStaff?serviceId="
				+ studentServiceBean.getId();

	}

	private String uploadFileAndReturnStatus(MultipartFile file,
			String username, StudentServiceBean serviceBean) {

		InputStream inputStream = null;

		String fileName = file.getOriginalFilename();

		fileName = RandomStringUtils.randomAlphanumeric(10)
				+ fileName.substring(fileName.lastIndexOf("."),
						fileName.length());
		List<BonafideForm> bList = new ArrayList<BonafideForm>();
		try {

			inputStream = file.getInputStream();
			String filePath = downloadAllFolder + File.separator + fileName;

			File folderPath = new File(downloadAllFolder);
			if (!folderPath.exists()) {
				boolean created = folderPath.mkdirs();
			}

			File dest = new File(filePath);

			FileUtils.copyInputStreamToFile(inputStream, dest);
			List<Map<String, String>> listCells = excelReader
					.readXLSXFileForBonafide(dest.getAbsolutePath());
			List<String> ids = new ArrayList<String>();
			for (Map<String, String> m : listCells) {
				
				BonafideForm bean = new BonafideForm();
				/*
				 * bean.setId(Long.valueOf(m.get("Application ID")));
				 * bean.setUsername(m.get("SAPId"));
				 * bean.setFirstname(m.get("First Name"));
				 * bean.setLastname(m.get("Last Name"));
				 * bean.setFatherName(m.get("Father Name"));
				 * bean.setMotherName(m.get("Mother Name"));
				 * bean.setRollNo(m.get("Roll No"));
				 * bean.setAcadYear(m.get("Acad Year"));
				 * bean.setProgramName(m.get("Program Name"));
				 * bean.setStudyClass(m.get("Class"));
				 * bean.setDivision(m.get("Division"));
				 * bean.setReason(m.get("Reason"));
				 */
				Integer flagNo = 1;
				/*
				 * if (serviceBean.getLevel1().equalsIgnoreCase(username)) {
				 * bean.setFlag1(m.get("Status"));
				 * bean.setRemark1(m.get("Remarks")); } else
				 */if (serviceBean.getLevel2().equalsIgnoreCase(username)) {
					bean.setFlag2(m.get("Status"));
					bean.setRemark2(m.get("Remarks"));
					flagNo = 2;
				} else if (serviceBean.getLevel3().equalsIgnoreCase(username)) {
					bean.setFlag3(m.get("Status"));
					bean.setRemark3(m.get("Remarks"));
					flagNo = 3;
				}

				// bList.add(bean);
				
				bonafideFormService.updateFlagsAndRemarks(
						String.valueOf(flagNo), (String) m.get("Status"),
						(String) m.get("Remarks"),
						(String) m.get("Application ID"));
			}

			return "SUCCESS";

		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
			return "ERROR";

		}

	}

	/*
	 * @RequestMapping(value = "/displayStudentServiceList", method = {
	 * RequestMethod.GET, RequestMethod.POST }) public String
	 * displayStudentServiceList(Model m, Principal principal) { String username
	 * = principal.getName(); Token userdetails1 = (Token) principal; String
	 * ProgramName = userdetails1.getProgramName(); List<StudentServiceBean>
	 * serviceList = studentWsService.findAllActive();
	 * m.addAttribute("serviceList", serviceList); return
	 * "studentService/viewServices"; }
	 */
	@Secured({ "ROLE_STUDENT" })
	@RequestMapping(value = "/applyForBonafide", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String applyForBonafide(Model m, Principal principal,
			@RequestParam(required = false) String serviceId) {
		String username = principal.getName();
	
		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		
		Date dt = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		String strDate = formatter.format(dt);
		m.addAttribute("currentDate", strDate);
		User u = userService.findUserProgramAcadYearByusername(username);
		
		m.addAttribute("serviceId", serviceId);
		m.addAttribute("student", u);
		m.addAttribute("bonafideForm", new BonafideForm());
		return "studentService/studentBonafide";
	}

	@RequestMapping(value = "/sendStudentBonafideData", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String sendStudentBonafideData(Model m, Principal principal,
			@ModelAttribute BonafideForm bonafideForm) {
		String username = bonafideForm.getUsername();
		BonafideForm bfd = bonafideFormService
				.findByUsernameAndSubmitted(username);
		if (bfd != null) {
			bonafideFormService.deleteSoftById(String.valueOf(bfd.getId()));
		}

		if (bonafideForm.getFatherName() == null) {
			bonafideForm.setFatherName("NA");
		}
		bonafideForm.setIsSubmitted("Y");
		bonafideForm.setActive("Y");
		bonafideForm.setFlag1("APPROVED");
		bonafideForm.setFlag2("PENDING");
		bonafideForm.setFlag3("0");
		bonafideForm.setCreatedBy(username);
		bonafideForm.setLastModifiedBy(username);
		
		StudentServiceBean service = studentWsService.findByID(bonafideForm
				.getServiceId());
		
		bonafideForm.setPayment(service.getPayment());
		if (service.getLevel1() != null || !"".equals(service.getLevel1())) {
			bonafideForm.setLevel1(service.getLevel1());
		}
		if (service.getLevel2() != null || !"".equals(service.getLevel2())) {
			bonafideForm.setLevel2(service.getLevel2());
		}
		if (service.getLevel3() != null || !"".equals(service.getLevel3())) {
			bonafideForm.setLevel3(service.getLevel3());
		}
		/*
		 * BonafideForm b =
		 * bonafideFormService.findAllByUsernameAndSubmitted(username); if(b !=
		 * null || !"".equals(b)){ bonafideFormService.updateSubmit(b.getId());
		 * 
		 * bonafideFormService.insert(bonafideForm);
		 * logger.info("data upsert manual"); }else{
		 * bonafideFormService.insert(bonafideForm);
		 * logger.info("data inserted"); }
		 */
		bonafideFormService.insert(bonafideForm);
		
		BonafideForm bf = bonafideFormService
				.findByUsernameAndSubmitted(username);
		
		m.addAttribute("formId", bf.getId());
		return "studentService/bonafideFormSubmitted";
	}

	@RequestMapping(value = "/viewStudentBonafideApplication", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String viewStudentBonafideApplication(Model m, Principal principal,
			@RequestParam(required = false) String id) {
		BonafideForm bf = bonafideFormService.getSubmittedStudentById(Long
				.valueOf(id));
		

		m.addAttribute("student", bf);
		return "studentService/viewStudentBonafide";
	}

	@RequestMapping(value = "/viewBonafideApplication", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String viewBonafideApplication(Model m, Principal principal) {
		String username = principal.getName();
		BonafideForm bf = bonafideFormService
				.findByUsernameAndSubmitted(username);

		return "studentService/viewBonafideFormStatus";
	}

	@RequestMapping(value = "/saveBonafideRemarks", method = {
			RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String saveBonafideRemarks(@RequestParam String value,
			@RequestParam Long pk, @RequestParam String remark,
			Principal principal) {
		String username = principal.getName();
		try {
			
			bonafideFormService.saveBonafideRemarks(value, remark, pk);
			return "{\"status\": \"success\", \"msg\": \"Remarks saved successfully!\"}";
		} catch (Exception e) {
			logger.error("Exception", e);
			return "{\"status\": \"error\", \"msg\": \"Error in saving Remarks!\"}";
		}

	}

	@RequestMapping(value = "/saveBonafideStatus", method = {
			RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String saveBonafideStatus(@RequestParam String value,
			@RequestParam Long pk, @RequestParam String flag,
			Principal principal) {
		String username = principal.getName();
		try {
			
			bonafideFormService.saveBonafideStatus(value, flag, pk);
			return "{\"status\": \"success\", \"msg\": \"Flags saved successfully!\"}";
		} catch (Exception e) {
			logger.error("Exception", e);
			return "{\"status\": \"error\", \"msg\": \"Error in saving Flags!\"}";
		}

	}

	@RequestMapping(value = "/viewEscalatedServiceForLevel3", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String viewEscalatedServiceForLevel3(Model m, Principal principal) {
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);
		
		if ((userdetails1.getAuthorities().contains(Role.ROLE_ADMIN))
				|| (userdetails1.getAuthorities().contains(Role.ROLE_STAFF))) {
			List<StudentServiceBean> serviceList = studentWsService
					.findAllActive();
			m.addAttribute("serviceList", serviceList);
		}
		m.addAttribute("username", username);
		return "studentService/viewEscalatedServices";

	}

	@RequestMapping(value = "/viewBonafideServiceForLevel3", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String viewBonafideServiceForLevel3(Model m, Principal principal,
			@RequestParam Long serviceId) {
		m.addAttribute("webPage", new WebPage("evaluateAssignment",
				"Bonafide Service", true, false));
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		StudentServiceBean studentServiceBean = studentWsService
				.findByID(serviceId);
		m.addAttribute("serviceId", serviceId);
		m.addAttribute("studentServiceBean", studentServiceBean);

		List<BonafideForm> bf = bonafideFormService
				.findAllPendingBonafidesForLevel3(username);
		
		m.addAttribute("pendingList", bf);
		if (bf.isEmpty() || bf.size() == 0) {
			setNote(m, "no Records Found!");
		}
		if (studentServiceBean.getLevel3().equalsIgnoreCase(username)) {
			for (BonafideForm b : bf) {
				if (b.getFlag2().equals("APPROVED")) {
					b.setApprovedLevel1("true");
					b.setApprovedLevel("true");
					bf.set(bf.indexOf(b), b);

				}

				bf.set(bf.indexOf(b), b);
			}
			m.addAttribute("showRemark", true);
			m.addAttribute("showActions", true);
		}
		m.addAttribute("bonafide", new BonafideForm());
		return "studentService/viewBonafideLevel3";
	}

	@RequestMapping(value = "/saveBonafideStatusForLevel3", method = {
			RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String saveBonafideStatusForLevel3(
			@RequestParam String value, @RequestParam Long pk,
			@RequestParam String flag, Principal principal) {
		String username = principal.getName();
		try {
			
			bonafideFormService.saveBonafideStatusForLevel3(value, flag,
					String.valueOf(pk));
			return "{\"status\": \"success\", \"msg\": \"Flags saved successfully!\"}";
		} catch (Exception e) {
			logger.error("Exception", e);
			return "{\"status\": \"error\", \"msg\": \"Error in saving Flags!\"}";
		}

	}

	@RequestMapping(value = "/saveBonafideRemarksForLevel3", method = {
			RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String saveBonafideRemarksForLevel3(
			@RequestParam String value, @RequestParam Long pk,
			@RequestParam String remark, Principal principal) {
		String username = principal.getName();
		try {
			
			bonafideFormService.saveBonafideRemarksForLevel3(value, remark,
					String.valueOf(pk));
			return "{\"status\": \"success\", \"msg\": \"Remarks saved successfully!\"}";
		} catch (Exception e) {
			logger.error("Exception", e);
			return "{\"status\": \"error\", \"msg\": \"Error in saving Remarks!\"}";
		}

	}

	@RequestMapping(value = "/createBFServiceSupportForm", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String createBFServiceSupportForm(Model m, Principal principal) {
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);
		

		m.addAttribute("studentServiceBean", new StudentServiceBean());
		m.addAttribute("username", username);

		return "studentService/createBFServiceSupport";

	}

	@RequestMapping(value = "/downloadStudentsBonafideFormsForLevel3", method = RequestMethod.GET)
	public void downloadStudentsBonafideFormsForLevel3(
			HttpServletResponse response, Principal principal,
			@RequestParam Long serviceId) {
		

		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);
		
		InputStream is = null;
		String filePath = "";
		StudentServiceBean studentServiceBean = studentWsService
				.findByID(serviceId);
		try {
			// get your file as InputStream
			List<BonafideForm> bList = bonafideFormService
					.findAllPendingBonafidesForLevel3(username);

			

			filePath = getBonafideApplications(bList, username);
			is = new FileInputStream(filePath);
			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			// copy it to response's OutputStream
			org.apache.commons.io.IOUtils.copy(is, response.getOutputStream());
			response.flushBuffer();

			response.flushBuffer();
		} catch (Exception ex) {
			logger.info(
					"Error writing file to output stream. Filename was '{}'",
					ex);
			throw new RuntimeException("IOError writing file to output stream");
		} finally {
			if (is != null) {
				org.apache.commons.io.IOUtils.closeQuietly(is);
			}

			FileUtils.deleteQuietly(new File(filePath));
		}

	}

	@RequestMapping(value = "/uploadStudentsBonafideFormsForLevel3", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String uploadStudentsBonafideFormsForLevel3(Model m,
			Principal principal, RedirectAttributes redirectAttributes,
			@ModelAttribute StudentServiceBean studentServiceBean,
			@RequestParam("file") MultipartFile file) {
		
		m.addAttribute("webPage", new WebPage("viewAssignment",
				"Bonafide Service", true, false));
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);
		
		if (file.isEmpty()) {
			setNote(redirectAttributes, "File is empty!");

		} else {

			StudentServiceBean serviceBean = studentWsService
					.findByID(studentServiceBean.getId());

			String statusOfUpload = uploadFileAndReturnStatusForLevel3(file,
					username, serviceBean);
			if (statusOfUpload.equalsIgnoreCase("SUCCESS")) {
				setSuccess(redirectAttributes, "File uploaded successfully!");
			} else {
				setError(redirectAttributes, "Error while uploading file!");
			}
		}

		return "redirect:/viewBonafideServiceForLevel3?serviceId="
				+ studentServiceBean.getId();

	}

	private String uploadFileAndReturnStatusForLevel3(MultipartFile file,
			String username, StudentServiceBean serviceBean) {

		InputStream inputStream = null;

		String fileName = file.getOriginalFilename();

		fileName = RandomStringUtils.randomAlphanumeric(10)
				+ fileName.substring(fileName.lastIndexOf("."),
						fileName.length());
		List<BonafideForm> bList = new ArrayList<BonafideForm>();
		try {

			inputStream = file.getInputStream();
			String filePath = downloadAllFolder + File.separator + fileName;

			File folderPath = new File(downloadAllFolder);
			if (!folderPath.exists()) {
				boolean created = folderPath.mkdirs();
			}

			File dest = new File(filePath);

			FileUtils.copyInputStreamToFile(inputStream, dest);
			List<Map<String, String>> listCells = excelReader
					.readXLSXFileForBonafide(dest.getAbsolutePath());
			List<String> ids = new ArrayList<String>();
			for (Map<String, String> m : listCells) {
				
				BonafideForm bean = new BonafideForm();
				/*
				 * bean.setId(Long.valueOf(m.get("Application ID")));
				 * bean.setUsername(m.get("SAPId"));
				 * bean.setFirstname(m.get("First Name"));
				 * bean.setLastname(m.get("Last Name"));
				 * bean.setFatherName(m.get("Father Name"));
				 * bean.setMotherName(m.get("Mother Name"));
				 * bean.setRollNo(m.get("Roll No"));
				 * bean.setAcadYear(m.get("Acad Year"));
				 * bean.setProgramName(m.get("Program Name"));
				 * bean.setStudyClass(m.get("Class"));
				 * bean.setDivision(m.get("Division"));
				 * bean.setReason(m.get("Reason"));
				 */
				Integer flagNo = 1;
				bean.setFlag1(m.get("Status"));
				bean.setRemark1(m.get("Remarks"));
				bean.setFlag2(m.get("Status"));
				bean.setRemark2(m.get("Remarks"));

				// bList.add(bean);
				
				/*
				 * bonafideFormService.updateFlagsAndRemarks(
				 * String.valueOf(flagNo), (String) m.get("Status"), (String)
				 * m.get("Remarks"), (String) m.get("Application ID"));
				 */
				bonafideFormService.saveBonafideStatusForLevel3(
						m.get("Status"), "flag2", m.get("Application ID"));
				bonafideFormService.saveBonafideRemarksForLevel3(
						m.get("Remarks"), "remark2", m.get("Application ID"));
			}

			return "SUCCESS";

		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
			return "ERROR";

		}

	}

	/*
	 * @RequestMapping(value = "/downloadStudentBonafideForm", method = {
	 * 
	 * RequestMethod.POST, RequestMethod.GET })
	 * 
	 * public String downloadStudentBonafideForm(
	 * 
	 * @RequestParam(name = "id") String id, Model m,
	 * 
	 * HttpServletResponse response) throws FileNotFoundException {
	 * 
	 * InputStream is = null;
	 * 
	 * BonafideForm bf = bonafideFormService.findByID(Long.valueOf(id));
	 * 
	 * 
	 * 
	 * String filePath = downloadAllFolder + File.separator + "Bonafide " +
	 * 
	 * "-" + bf.getUsername() + Instant.now().toEpochMilli() + ".pdf";
	 * 
	 * logger.info("filePath --- " + filePath);
	 * 
	 * 
	 * 
	 * m.addAttribute("bf", bf);
	 * 
	 * 
	 * 
	 * return "studentService/downloadStudentBonafide";
	 * 
	 * 
	 * 
	 * }
	 */
	@RequestMapping(value = "/applyForRC", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String applyForRC(Model m, Principal principal,
			@RequestParam(required = false) String serviceId) {
		String username = principal.getName();
		
		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		
		Date dt = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		String strDate = formatter.format(dt);
		m.addAttribute("currentDate", strDate);
		User u = userService.findUserProgramAcadYearByusername(username);
		
		m.addAttribute("serviceId", serviceId);
		m.addAttribute("student", u);
		m.addAttribute("railwayForm", new RailwayForm());
		return "studentService/studentRC";
	}

	@RequestMapping(value = "/sendStudentRCData", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String sendStudentRCData(Model m, Principal principal,
			@ModelAttribute RailwayForm railwayForm) {
		String username = railwayForm.getUsername();
		RailwayForm bfd = railwayFormService
				.findByUsernameAndSubmitted(username);
		if (bfd != null) {
			railwayFormService.deleteSoftById(String.valueOf(bfd.getId()));
		}

		railwayForm.setIsSubmitted("Y");
		railwayForm.setActive("Y");
		railwayForm.setFlag1("APPROVED");
		railwayForm.setFlag2("PENDING");
		railwayForm.setFlag3("0");
		railwayForm.setCreatedBy(username);
		railwayForm.setLastModifiedBy(username);
		
		StudentServiceBean service = studentWsService.findByID(railwayForm
				.getServiceId());
		
		railwayForm.setPayment(service.getPayment());
		if (service.getLevel1() != null || !"".equals(service.getLevel1())) {
			railwayForm.setLevel1(service.getLevel1());
		}
		if (service.getLevel2() != null || !"".equals(service.getLevel2())) {
			railwayForm.setLevel2(service.getLevel2());
		}
		if (service.getLevel3() != null || !"".equals(service.getLevel3())) {
			railwayForm.setLevel3(service.getLevel3());
		}
		/*
		 * BonafideForm b =
		 * bonafideFormService.findAllByUsernameAndSubmitted(username); if(b !=
		 * null || !"".equals(b)){ bonafideFormService.updateSubmit(b.getId());
		 * 
		 * bonafideFormService.insert(bonafideForm);
		 * logger.info("data upsert manual"); }else{
		 * bonafideFormService.insert(bonafideForm);
		 * logger.info("data inserted"); }
		 */
		railwayFormService.insert(railwayForm);
		
		RailwayForm bf = railwayFormService
				.findByUsernameAndSubmitted(username);
		
		m.addAttribute("formId", bf.getId());
		return "studentService/RCFormSubmitted";
	}

	@RequestMapping(value = "/viewRCServiceForStaff", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String viewRCServiceForStaff(Model m, Principal principal,
			@RequestParam Long serviceId) {
		/*
		 * m.addAttribute("webPage", new WebPage("viewAssignment",
		 * "Bonafide Service", true, false));
		 */
		m.addAttribute("webPage", new WebPage("evaluateAssignment",
				"Bonafide Service", true, false));
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);
		

		StudentServiceBean studentServiceBean = studentWsService
				.findByID(serviceId);
		m.addAttribute("serviceId", serviceId);
		m.addAttribute("studentServiceBean", studentServiceBean);
		List<RailwayForm> rList = new ArrayList<RailwayForm>();
		int levelNo = 1;
		if (studentServiceBean.getLevel2().equalsIgnoreCase(username)) {
			levelNo = 2;
			rList = railwayFormService
					.findAllSubmittedStudentsByServiceIdFlag2(serviceId);
		} else {
			rList = railwayFormService
					.findAllSubmittedStudentsByServiceIdFlag1(serviceId);
		}

		m.addAttribute("rList", rList);
		if (rList.isEmpty() || rList.size() == 0) {
			setNote(m, "no Records Found!");
		}
		Integer levelNum = 1;
		/*
		 * if (studentServiceBean.getLevel1().equalsIgnoreCase(username)) { for
		 * (BonafideForm bf : bList) { if (bf.isFlag1()) {
		 * bf.setApprovedLevel1("true"); bf.setApprovedLevel("true");
		 * bList.set(bList.indexOf(bf), bf); } } m.addAttribute("showActions",
		 * true); } else if
		 * (studentServiceBean.getLevel2().equalsIgnoreCase(username)) { for
		 * (BonafideForm bf : bList) { if (bf.isFlag1()) { if (bf.isFlag2()) {
		 * bf.setApprovedLevel2("true"); bf.setApprovedLevel("true"); } else {
		 * bf.setAllowLevel2("true"); } bList.set(bList.indexOf(bf), bf); }
		 * 
		 * } } else if
		 * (studentServiceBean.getLevel3().equalsIgnoreCase(username)) { for
		 * (BonafideForm bf : bList) { if (bf.isFlag1() && bf.isFlag2()) { if
		 * (bf.isFlag3()) { bf.setApprovedLevel3("true");
		 * bf.setApprovedLevel("true"); } else { bf.setAllowLevel3("true"); }
		 * bList.set(bList.indexOf(bf), bf); }
		 * 
		 * } }
		 */
		/*
		 * if (studentServiceBean.getLevel1().equalsIgnoreCase(username)) {
		 * levelNum = 1; for (BonafideForm bf : bList) { if
		 * (bf.getFlag1().equals("APPROVED")) { bf.setApprovedLevel1("true");
		 * bf.setApprovedLevel("true"); bList.set(bList.indexOf(bf), bf);
		 * 
		 * } if (bf.getFlag1().equals("PENDING")) {
		 * 
		 * m.addAttribute("showRemark", true); }
		 * 
		 * }
		 * 
		 * m.addAttribute("showActions", true); } else
		 */if (studentServiceBean.getLevel2().equalsIgnoreCase(username)) {
			levelNum = 2;
			for (RailwayForm rf : rList) {
				if (rf.getFlag1().equals("APPROVED")) {
					if (rf.getFlag2().equals("APPROVED")) {
						rf.setApprovedLevel2("true");
						rf.setApprovedLevel("true");
					} else if (rf.getFlag2().equals("PENDING")) {
						rf.setAllowLevel2("true");
						rf.setAllowRemark2("true");
					}
					rList.set(rList.indexOf(rf), rf);
				}

			}
		} else if (studentServiceBean.getLevel3().equalsIgnoreCase(username)) {
			levelNum = 3;
			for (RailwayForm rf : rList) {
				if (rf.getFlag1().equals("APPROVED")
						&& rf.getFlag2().equals("APPROVED")) {
					if (rf.getFlag3().equals("APPROVED")) {
						rf.setApprovedLevel3("true");
						rf.setApprovedLevel("true");
					} else if (rf.getFlag3().equals("PENDING")) {
						rf.setAllowLevel3("true");
						rf.setAllowRemark3("true");
					}
					rList.set(rList.indexOf(rf), rf);
				}

			}
		}
		m.addAttribute("levelNum", levelNum);
		m.addAttribute("railway", new RailwayForm());

		return "studentService/viewRCServiceForStaff";

	}

	@RequestMapping(value = "/viewStudentRCApplication", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String viewStudentRCApplication(Model m, Principal principal,
			@RequestParam(required = false) String id) {
		RailwayForm rf = railwayFormService.getSubmittedStudentById(Long
				.valueOf(id));
		

		m.addAttribute("student", rf);
		return "studentService/viewStudentRC";
	}

	@RequestMapping(value = "/saveRCRemarks", method = { RequestMethod.GET,
			RequestMethod.POST })
	public @ResponseBody String saveRCRemarks(@RequestParam String value,
			@RequestParam Long pk, @RequestParam String remark,
			Principal principal) {
		String username = principal.getName();
		try {
			
			railwayFormService.saveRCRemarks(value, remark, pk);
			return "{\"status\": \"success\", \"msg\": \"Remarks saved successfully!\"}";
		} catch (Exception e) {
			logger.error("Exception", e);
			return "{\"status\": \"error\", \"msg\": \"Error in saving Remarks!\"}";
		}

	}

	@RequestMapping(value = "/saveRCStatus", method = { RequestMethod.GET,
			RequestMethod.POST })
	public @ResponseBody String saveRCStatus(@RequestParam String value,
			@RequestParam Long pk, @RequestParam String flag,
			Principal principal) {
		String username = principal.getName();
		try {
			
			railwayFormService.saveRCStatus(value, flag, pk);
			return "{\"status\": \"success\", \"msg\": \"Flags saved successfully!\"}";
		} catch (Exception e) {
			logger.error("Exception", e);
			return "{\"status\": \"error\", \"msg\": \"Error in saving Flags!\"}";
		}

	}

	@RequestMapping(value = "/downloadStudentsRCForms", method = RequestMethod.GET)
	public void downloadStudentsRCForms(HttpServletResponse response,
			Principal principal, @RequestParam Long serviceId,
			@RequestParam String levelNum) {
		
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);
		
		InputStream is = null;
		String filePath = "";
		StudentServiceBean studentServiceBean = studentWsService
				.findByID(serviceId);
		try {
			// get your file as InputStream
			List<RailwayForm> bList = new ArrayList<RailwayForm>();
			int levelNo = 1;
			if (studentServiceBean.getLevel2().equalsIgnoreCase(username)) {
				levelNo = 2;
				bList = railwayFormService
						.findAllSubmittedStudentsByServiceIdFlag2(serviceId);
			} /*
			 * else { bList = bonafideFormService
			 * .findAllSubmittedStudentsByServiceIdFlag1(serviceId); }
			 */
			

			filePath = getRCApplications(bList, username);
			is = new FileInputStream(filePath);
			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			// copy it to response's OutputStream
			org.apache.commons.io.IOUtils.copy(is, response.getOutputStream());
			response.flushBuffer();

			response.flushBuffer();
		} catch (Exception ex) {
			logger.info(
					"Error writing file to output stream. Filename was '{}'",
					ex);
			throw new RuntimeException("IOError writing file to output stream");
		} finally {
			if (is != null) {
				org.apache.commons.io.IOUtils.closeQuietly(is);
			}

			FileUtils.deleteQuietly(new File(filePath));
		}

	}

	public String getBonafideApplications(List<BonafideForm> bList,
			String username) {
		String filePath = downloadAllFolder + File.separator
				+ "BonafideStudentApplications.xlsx";
		String h[] = { "Application ID", "SAPId", "First Name", "Last Name",
				"Father Name", "Roll No", "Acad Year", "Program Name", "Class",
				"Division", "Reason", "Status", "Remarks" };
		List<String> header = Arrays.asList(h);
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("Bonafide Sheet");
		Row headerRow = sheet.createRow(0);
		for (int colNum = 0; colNum < header.size(); colNum++) {
			Cell cell = headerRow.createCell(colNum);
			cell.setCellValue(header.get(colNum));
		}
		int rowNum = 1;

		for (BonafideForm bf : bList) {
			Row row = sheet.createRow(rowNum);
			row.createCell(0).setCellValue(bf.getId());
			row.createCell(1).setCellValue(bf.getUsername());
			row.createCell(2).setCellValue(bf.getFirstname());
			row.createCell(3).setCellValue(bf.getLastname());
			row.createCell(4).setCellValue(bf.getFatherName());
			// row.createCell(5).setCellValue(bf.getMotherName());
			row.createCell(5).setCellValue(bf.getRollNo());
			row.createCell(6).setCellValue(bf.getAcadYear());
			row.createCell(7).setCellValue(bf.getProgramName());
			row.createCell(8).setCellValue(bf.getStudyClass());
			row.createCell(9).setCellValue(bf.getDivision());
			row.createCell(10).setCellValue(bf.getReason());
			/*
			 * if (bf.getLevel1().equalsIgnoreCase(username)) {
			 * row.createCell(11).setCellValue(bf.getFlag1());
			 * row.createCell(12).setCellValue(bf.getRemark1()); } else
			 */if (bf.getLevel2().equalsIgnoreCase(username)) {
				row.createCell(11).setCellValue(bf.getFlag2());
				row.createCell(12).setCellValue(bf.getRemark2());
			} else if (bf.getLevel3().equalsIgnoreCase(username)) {
				row.createCell(11).setCellValue(bf.getFlag2());
				row.createCell(12).setCellValue(bf.getRemark2());
			}

			rowNum++;

		}
		XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheet);
		String[] actions = new String[] { "PENDING,APPROVED,REJECTED" };
		XSSFDataValidationConstraint constraint = (XSSFDataValidationConstraint) dvHelper
				.createExplicitListConstraint(actions);
		CellRangeAddressList addressList = new CellRangeAddressList(1, 100, 11,
				11);
		XSSFDataValidation validation = (XSSFDataValidation) dvHelper
				.createValidation(constraint, addressList);
		validation.setShowErrorBox(true);
		sheet.addValidationData(validation);
		try {
			FileOutputStream outputStream = new FileOutputStream(filePath);
			workbook.write(outputStream);

			workbook.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return filePath;

	}

	@RequestMapping(value = "/uploadStudentsRCForms", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String uploadStudentsRCForms(Model m, Principal principal,
			RedirectAttributes redirectAttributes,
			@ModelAttribute StudentServiceBean studentServiceBean,
			@RequestParam("file") MultipartFile file) {
		
		m.addAttribute("webPage", new WebPage("viewAssignment",
				"Railway Service", true, false));
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);
		
		if (file.isEmpty()) {
			setNote(redirectAttributes, "File is empty!");

		} else {

			StudentServiceBean serviceBean = studentWsService
					.findByID(studentServiceBean.getId());

			String statusOfUpload = uploadFileAndReturnStatusForRC(file,
					username, serviceBean);
			if (statusOfUpload.equalsIgnoreCase("SUCCESS")) {
				setSuccess(redirectAttributes, "File uploaded successfully!");
			} else {
				setError(redirectAttributes, "Error while uploading file!");
			}
		}

		return "redirect:/viewRCServiceForStaff?serviceId="
				+ studentServiceBean.getId();

	}

	private String uploadFileAndReturnStatusForRC(MultipartFile file,
			String username, StudentServiceBean serviceBean) {

		InputStream inputStream = null;

		String fileName = file.getOriginalFilename();

		fileName = RandomStringUtils.randomAlphanumeric(10)
				+ fileName.substring(fileName.lastIndexOf("."),
						fileName.length());
		List<BonafideForm> bList = new ArrayList<BonafideForm>();
		try {

			inputStream = file.getInputStream();
			String filePath = downloadAllFolder + File.separator + fileName;

			File folderPath = new File(downloadAllFolder);
			if (!folderPath.exists()) {
				boolean created = folderPath.mkdirs();
			}

			File dest = new File(filePath);

			FileUtils.copyInputStreamToFile(inputStream, dest);
			List<Map<String, String>> listCells = excelReader
					.readXLSXFileForBonafide(dest.getAbsolutePath());
			List<String> ids = new ArrayList<String>();
			for (Map<String, String> m : listCells) {
				
				RailwayForm bean = new RailwayForm();
				/*
				 * bean.setId(Long.valueOf(m.get("Application ID")));
				 * bean.setUsername(m.get("SAPId"));
				 * bean.setFirstname(m.get("First Name"));
				 * bean.setLastname(m.get("Last Name"));
				 * bean.setFatherName(m.get("Father Name"));
				 * bean.setMotherName(m.get("Mother Name"));
				 * bean.setRollNo(m.get("Roll No"));
				 * bean.setAcadYear(m.get("Acad Year"));
				 * bean.setProgramName(m.get("Program Name"));
				 * bean.setStudyClass(m.get("Class"));
				 * bean.setDivision(m.get("Division"));
				 * bean.setReason(m.get("Reason"));
				 */
				Integer flagNo = 1;
				/*
				 * if (serviceBean.getLevel1().equalsIgnoreCase(username)) {
				 * bean.setFlag1(m.get("Status"));
				 * bean.setRemark1(m.get("Remarks")); } else
				 */if (serviceBean.getLevel2().equalsIgnoreCase(username)) {
					bean.setFlag2(m.get("Status"));
					bean.setRemark2(m.get("Remarks"));
					flagNo = 2;
				} else if (serviceBean.getLevel3().equalsIgnoreCase(username)) {
					bean.setFlag3(m.get("Status"));
					bean.setRemark3(m.get("Remarks"));
					flagNo = 3;
				}

				// bList.add(bean);
				
				railwayFormService.updateFlagsAndRemarks(
						String.valueOf(flagNo), (String) m.get("Status"),
						(String) m.get("Remarks"),
						(String) m.get("Application ID"));
			}

			return "SUCCESS";

		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
			return "ERROR";

		}

	}

	@RequestMapping(value = "/viewRCServiceForLevel3", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String viewRCServiceForLevel3(Model m, Principal principal,
			@RequestParam Long serviceId) {
		m.addAttribute("webPage", new WebPage("evaluateAssignment",
				"Bonafide Service", true, false));
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		StudentServiceBean studentServiceBean = studentWsService
				.findByID(serviceId);
		m.addAttribute("serviceId", serviceId);
		m.addAttribute("studentServiceBean", studentServiceBean);

		List<RailwayForm> rf = railwayFormService
				.findAllPendingRCForLevel3(username);
		
		m.addAttribute("pendingList", rf);
		if (rf.isEmpty() || rf.size() == 0) {
			setNote(m, "no Records Found!");
		}
		if (studentServiceBean.getLevel3().equalsIgnoreCase(username)) {
			for (RailwayForm r : rf) {
				if (r.getFlag2().equals("APPROVED")) {
					r.setApprovedLevel1("true");
					r.setApprovedLevel("true");
					rf.set(rf.indexOf(r), r);

				}

				rf.set(rf.indexOf(r), r);
			}
			m.addAttribute("showRemark", true);
			m.addAttribute("showActions", true);
		}
		m.addAttribute("railway", new RailwayForm());
		return "studentService/viewRCLevel3";
	}

	@RequestMapping(value = "/saveRCStatusForLevel3", method = {
			RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String saveRCStatusForLevel3(
			@RequestParam String value, @RequestParam Long pk,
			@RequestParam String flag, Principal principal) {
		String username = principal.getName();
		try {
			
			railwayFormService.saveRCStatusForLevel3(value, flag,
					String.valueOf(pk));
			return "{\"status\": \"success\", \"msg\": \"Flags saved successfully!\"}";
		} catch (Exception e) {
			logger.error("Exception", e);
			return "{\"status\": \"error\", \"msg\": \"Error in saving Flags!\"}";
		}

	}

	@RequestMapping(value = "/saveRCRemarksForLevel3", method = {
			RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String saveRCRemarksForLevel3(
			@RequestParam String value, @RequestParam Long pk,
			@RequestParam String remark, Principal principal) {
		String username = principal.getName();
		try {
			
			railwayFormService.saveRCRemarksForLevel3(value, remark,
					String.valueOf(pk));
			return "{\"status\": \"success\", \"msg\": \"Remarks saved successfully!\"}";
		} catch (Exception e) {
			logger.error("Exception", e);
			return "{\"status\": \"error\", \"msg\": \"Error in saving Remarks!\"}";
		}

	}

	@RequestMapping(value = "/downloadStudentsRCFormsForLevel3", method = RequestMethod.GET)
	public void downloadStudentsRCFormsForLevel3(HttpServletResponse response,
			Principal principal, @RequestParam Long serviceId) {
		

		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);
		
		InputStream is = null;
		String filePath = "";
		StudentServiceBean studentServiceBean = studentWsService
				.findByID(serviceId);
		try {
			// get your file as InputStream
			List<RailwayForm> bList = railwayFormService
					.findAllPendingRCForLevel3(username);

			

			filePath = getRCApplications(bList, username);
			is = new FileInputStream(filePath);
			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			// copy it to response's OutputStream
			org.apache.commons.io.IOUtils.copy(is, response.getOutputStream());
			response.flushBuffer();

			response.flushBuffer();
		} catch (Exception ex) {
			logger.info(
					"Error writing file to output stream. Filename was '{}'",
					ex);
			throw new RuntimeException("IOError writing file to output stream");
		} finally {
			if (is != null) {
				org.apache.commons.io.IOUtils.closeQuietly(is);
			}

			FileUtils.deleteQuietly(new File(filePath));
		}

	}

	@RequestMapping(value = "/uploadStudentsRCFormsForLevel3", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String uploadStudentsRCFormsForLevel3(Model m, Principal principal,
			RedirectAttributes redirectAttributes,
			@ModelAttribute StudentServiceBean studentServiceBean,
			@RequestParam("file") MultipartFile file) {
		
		m.addAttribute("webPage", new WebPage("viewAssignment",
				"Bonafide Service", true, false));
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);
		
		if (file.isEmpty()) {
			setNote(redirectAttributes, "File is empty!");

		} else {

			StudentServiceBean serviceBean = studentWsService
					.findByID(studentServiceBean.getId());

			String statusOfUpload = uploadFileAndReturnStatusForLevel3RC(file,
					username, serviceBean);
			if (statusOfUpload.equalsIgnoreCase("SUCCESS")) {
				setSuccess(redirectAttributes, "File uploaded successfully!");
			} else {
				setError(redirectAttributes, "Error while uploading file!");
			}
		}

		return "redirect:/viewRCServiceForLevel3?serviceId="
				+ studentServiceBean.getId();

	}

	private String uploadFileAndReturnStatusForLevel3RC(MultipartFile file,
			String username, StudentServiceBean serviceBean) {

		InputStream inputStream = null;

		String fileName = file.getOriginalFilename();

		fileName = RandomStringUtils.randomAlphanumeric(10)
				+ fileName.substring(fileName.lastIndexOf("."),
						fileName.length());
		List<BonafideForm> bList = new ArrayList<BonafideForm>();
		try {

			inputStream = file.getInputStream();
			String filePath = downloadAllFolder + File.separator + fileName;

			File folderPath = new File(downloadAllFolder);
			if (!folderPath.exists()) {
				boolean created = folderPath.mkdirs();
			}

			File dest = new File(filePath);

			FileUtils.copyInputStreamToFile(inputStream, dest);
			List<Map<String, String>> listCells = excelReader
					.readXLSXFileForBonafide(dest.getAbsolutePath());
			List<String> ids = new ArrayList<String>();
			for (Map<String, String> m : listCells) {
				
				BonafideForm bean = new BonafideForm();
				/*
				 * bean.setId(Long.valueOf(m.get("Application ID")));
				 * bean.setUsername(m.get("SAPId"));
				 * bean.setFirstname(m.get("First Name"));
				 * bean.setLastname(m.get("Last Name"));
				 * bean.setFatherName(m.get("Father Name"));
				 * bean.setMotherName(m.get("Mother Name"));
				 * bean.setRollNo(m.get("Roll No"));
				 * bean.setAcadYear(m.get("Acad Year"));
				 * bean.setProgramName(m.get("Program Name"));
				 * bean.setStudyClass(m.get("Class"));
				 * bean.setDivision(m.get("Division"));
				 * bean.setReason(m.get("Reason"));
				 */
				Integer flagNo = 1;
				bean.setFlag1(m.get("Status"));
				bean.setRemark1(m.get("Remarks"));
				bean.setFlag2(m.get("Status"));
				bean.setRemark2(m.get("Remarks"));

				// bList.add(bean);
				
				/*
				 * bonafideFormService.updateFlagsAndRemarks(
				 * String.valueOf(flagNo), (String) m.get("Status"), (String)
				 * m.get("Remarks"), (String) m.get("Application ID"));
				 */
				railwayFormService.saveRCStatusForLevel3(m.get("Status"),
						"flag2", m.get("Application ID"));
				railwayFormService.saveRCRemarksForLevel3(m.get("Remarks"),
						"remark2", m.get("Application ID"));
			}

			return "SUCCESS";

		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
			return "ERROR";

		}

	}

	// ============================================HOSTEL FORM
	// SERVICE===============================================================

	/*
	 * @RequestMapping(value = "/applyForHostel", method = { RequestMethod.GET,
	 * RequestMethod.POST }) public String applyForHostel(Model m, Principal
	 * principal,
	 * 
	 * @RequestParam(required = false) String serviceId) { String username =
	 * principal.getName(); logger.info("sap name" + username); Token
	 * userdetails1 = (Token) principal; String ProgramName =
	 * userdetails1.getProgramName(); logger.info("program name" + ProgramName);
	 * Date dt = new Date(); SimpleDateFormat formatter = new
	 * SimpleDateFormat("dd/MM/yyyy"); String strDate = formatter.format(dt);
	 * m.addAttribute("currentDate", strDate); User u =
	 * userService.findUserProgramAcadYearByusername(username);
	 * logger.info("user" + u); m.addAttribute("serviceId", serviceId);
	 * m.addAttribute("student", u); m.addAttribute("hostelForm", new
	 * HostelForm()); return "studentService/studentHostel"; }
	 * 
	 * @RequestMapping(value = "/sendStudentHostelData", method = {
	 * RequestMethod.GET, RequestMethod.POST }) public String
	 * sendStudentHostelData(Model m, Principal principal,
	 * 
	 * @ModelAttribute HostelForm hostelForm) { String username =
	 * principal.getName(); HostelForm bfd =
	 * hostelFormService.findByUsernameAndSubmitted(username); if (bfd != null)
	 * { hostelFormService.deleteSoftById(String.valueOf(bfd.getId())); }
	 * hostelForm.setUsername(username); hostelForm.setIsSubmitted("Y");
	 * hostelForm.setActive("Y"); hostelForm.setFlag1("APPROVED");
	 * hostelForm.setFlag2("PENDING"); hostelForm.setFlag3("0");
	 * hostelForm.setCreatedBy(username);
	 * hostelForm.setLastModifiedBy(username); logger.info("Student serviceid" +
	 * hostelForm.getServiceId()); StudentServiceBean service =
	 * studentWsService.findByID(hostelForm .getServiceId());
	 * logger.info("Student Data" + hostelForm);
	 * hostelForm.setPayment(service.getPayment()); if (service.getLevel1() !=
	 * null || !"".equals(service.getLevel1())) {
	 * hostelForm.setLevel1(service.getLevel1()); } if (service.getLevel2() !=
	 * null || !"".equals(service.getLevel2())) {
	 * hostelForm.setLevel2(service.getLevel2()); } if (service.getLevel3() !=
	 * null || !"".equals(service.getLevel3())) {
	 * hostelForm.setLevel3(service.getLevel3()); }
	 * 
	 * BonafideForm b =
	 * bonafideFormService.findAllByUsernameAndSubmitted(username); if(b != null
	 * || !"".equals(b)){ bonafideFormService.updateSubmit(b.getId());
	 * 
	 * bonafideFormService.insert(bonafideForm);
	 * logger.info("data upsert manual"); }else{
	 * bonafideFormService.insert(bonafideForm); logger.info("data inserted"); }
	 * 
	 * hostelFormService.insert(hostelForm); logger.info("data inserted");
	 * HostelForm bf = hostelFormService.findByUsernameAndSubmitted(username);
	 * logger.info("formId" + bf.getId()); m.addAttribute("formId", bf.getId());
	 * return "studentService/hostelFormSubmitted"; }
	 * 
	 * @RequestMapping(value = "/viewHostelServices", method = {
	 * RequestMethod.GET, RequestMethod.POST }) public String
	 * viewHostelServices(Model m, Principal principal) { String username =
	 * principal.getName();
	 * 
	 * Token userdetails1 = (Token) principal; String ProgramName =
	 * userdetails1.getProgramName(); User u =
	 * userService.findByUserName(username);
	 * logger.info("ACAD SESSION------------------------->" +
	 * u.getAcadSession()); List<StudentServiceBean> serviceList =
	 * studentWsService .findServiceByName("Hostel Service");
	 * m.addAttribute("serviceList", serviceList); if (serviceList.size() != 0)
	 * if (userdetails1.getAuthorities().contains(Role.ROLE_STUDENT)) { for
	 * (StudentServiceBean sb : serviceList) { HostelForm hostel =
	 * hostelFormService.findStudentHostel( username, sb.getId()); if (hostel !=
	 * null) { User user1 = userService.findByUserName(hostel .getLevel1());
	 * User user2 = userService.findByUserName(hostel .getLevel2()); User user3
	 * = userService.findByUserName(hostel .getLevel3());
	 * logger.info("user1 --- " + user1); hostel.setUser1(user1.getFirstname() +
	 * " " + user1.getLastname()); hostel.setUser2(user2.getFirstname() + " " +
	 * user2.getLastname()); hostel.setUser3(user3.getFirstname() + " " +
	 * user3.getLastname()); logger.info("hostel ----- " + hostel);
	 * 
	 * sb.setHostel(hostel); }
	 * 
	 * serviceList.set(serviceList.indexOf(sb), sb); } } return
	 * "studentService/viewHostelServices";
	 * 
	 * }
	 * 
	 * @RequestMapping(value = "/viewHostelServiceForStaff", method = {
	 * RequestMethod.GET, RequestMethod.POST }) public String
	 * viewHostelServiceForStaff(Model m, Principal principal,
	 * 
	 * @RequestParam Long serviceId,
	 * 
	 * @RequestParam(required = false) String campusLocation) {
	 * 
	 * m.addAttribute("webPage", new WebPage("evaluateAssignment",
	 * "Hostel Service", true, false)); String username = principal.getName();
	 * ObjectMapper mapper = new ObjectMapper(); Token userdetails1 = (Token)
	 * principal; String ProgramName = userdetails1.getProgramName(); User u =
	 * userService.findByUserName(username);
	 * logger.info("ACAD SESSION------------------------->" +
	 * u.getAcadSession());
	 * 
	 * StudentServiceBean studentServiceBean = studentWsService
	 * .findByID(serviceId); m.addAttribute("serviceId", serviceId);
	 * m.addAttribute("studentServiceBean", studentServiceBean);
	 * List<HostelForm> rList = new ArrayList<HostelForm>(); if (campusLocation
	 * != null) {
	 * 
	 * try { String json; json = mapper.writeValueAsString(campusLocation);
	 * logger.info("encoded json--->" + URIUtil.encodeQuery(json));
	 * logger.info("userRoleMgmtCrudUrl" + userRoleMgmtCrudUrl);
	 * logger.info(" json--->" + (json)); WebTarget webTarget =
	 * client.target(URIUtil .encodeQuery(userRoleMgmtCrudUrl +
	 * "/addOtherUserStaffService?json=" + json)); Invocation.Builder
	 * invocationBuilder = webTarget .request(MediaType.APPLICATION_JSON);
	 * String resp = invocationBuilder.get(String.class); logger.info("resp" +
	 * resp); if (resp == null) { setError(m, "Error in getting school list"); }
	 * else {
	 * 
	 * rList = mapper.readValue(resp, new TypeReference<List<HostelForm>>() {
	 * }); logger.info("rList --------- " + rList); m.addAttribute("rList",
	 * rList);
	 * 
	 * } } catch (IOException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); }
	 * 
	 * } m.addAttribute("rList", rList);
	 * 
	 * if (rList.isEmpty() || rList.size() == 0) { setNote(m,
	 * "no Records Found!"); } Integer levelNum = 1;
	 * 
	 * m.addAttribute("levelNum", levelNum); m.addAttribute("hostel", new
	 * HostelForm());
	 * 
	 * return "studentService/viewHostelServiceForStaff";
	 * 
	 * }
	 * 
	 * @RequestMapping(value = "/downloadStudentsHostelForms", method =
	 * RequestMethod.GET) public void
	 * downloadStudentsHostelForms(HttpServletResponse response, Principal
	 * principal, @RequestParam Long serviceId,
	 * 
	 * @RequestParam String levelNum) { logger.info("serviceId ==== " +
	 * serviceId); logger.info("levelNum ----------- " + levelNum); String
	 * username = principal.getName();
	 * 
	 * Token userdetails1 = (Token) principal; String ProgramName =
	 * userdetails1.getProgramName(); User u =
	 * userService.findByUserName(username);
	 * logger.info("ACAD SESSION------------------------->" +
	 * u.getAcadSession()); InputStream is = null; String filePath = "";
	 * StudentServiceBean studentServiceBean = studentWsService
	 * .findByID(serviceId); try { // get your file as InputStream
	 * List<HostelForm> bList = new ArrayList<HostelForm>(); int levelNo = 1; if
	 * (studentServiceBean.getLevel2().equalsIgnoreCase(username)) { levelNo =
	 * 2; bList = hostelFormService
	 * .findAllSubmittedStudentsByServiceIdFlag2(serviceId); } else { bList =
	 * bonafideFormService .findAllSubmittedStudentsByServiceIdFlag1(serviceId);
	 * }
	 * 
	 * logger.info("blist -------------- " + bList);
	 * 
	 * filePath = getHostelApplications(bList, username); is = new
	 * FileInputStream(filePath); response.setContentType(
	 * "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"); //
	 * copy it to response's OutputStream org.apache.commons.io.IOUtils.copy(is,
	 * response.getOutputStream()); response.flushBuffer();
	 * 
	 * response.flushBuffer(); } catch (Exception ex) { logger.info(
	 * "Error writing file to output stream. Filename was '{}'", ex); throw new
	 * RuntimeException("IOError writing file to output stream"); } finally { if
	 * (is != null) { org.apache.commons.io.IOUtils.closeQuietly(is); }
	 * 
	 * FileUtils.deleteQuietly(new File(filePath)); }
	 * 
	 * }
	 * 
	 * public String getHostelApplications(List<HostelForm> bList, String
	 * username) { String filePath = downloadAllFolder + File.separator +
	 * "BonafideStudentApplications.xlsx"; String h[] = { "Application ID",
	 * "Date", "Sex", "Year", "SAPId", "Name", "DOB",
	 * "Class & Monthly /Quarterly", "From Station", "To Station", "Address",
	 * "Status", "Remarks" }; List<String> header = Arrays.asList(h);
	 * XSSFWorkbook workbook = new XSSFWorkbook(); XSSFSheet sheet =
	 * workbook.createSheet("Railway Concession Sheet"); Row headerRow =
	 * sheet.createRow(0); for (int colNum = 0; colNum < header.size();
	 * colNum++) { Cell cell = headerRow.createCell(colNum);
	 * cell.setCellValue(header.get(colNum)); } int rowNum = 1;
	 * 
	 * for (HostelForm bf : bList) { Row row = sheet.createRow(rowNum);
	 * row.createCell(0).setCellValue(bf.getId());
	 * row.createCell(1).setCellValue(bf.getDate());
	 * 
	 * if (bf.getLevel2().equalsIgnoreCase(username)) {
	 * row.createCell(11).setCellValue(bf.getFlag2());
	 * row.createCell(12).setCellValue(bf.getRemark2()); } else if
	 * (bf.getLevel3().equalsIgnoreCase(username)) {
	 * row.createCell(11).setCellValue(bf.getFlag2());
	 * row.createCell(12).setCellValue(bf.getRemark2()); }
	 * 
	 * rowNum++;
	 * 
	 * } XSSFDataValidationHelper dvHelper = new
	 * XSSFDataValidationHelper(sheet); String[] actions = new String[] {
	 * "PENDING,APPROVED,REJECTED" }; XSSFDataValidationConstraint constraint =
	 * (XSSFDataValidationConstraint) dvHelper
	 * .createExplicitListConstraint(actions); CellRangeAddressList addressList
	 * = new CellRangeAddressList(1, 100, 11, 11); XSSFDataValidation validation
	 * = (XSSFDataValidation) dvHelper .createValidation(constraint,
	 * addressList); validation.setShowErrorBox(true);
	 * sheet.addValidationData(validation); try { FileOutputStream outputStream
	 * = new FileOutputStream(filePath); workbook.write(outputStream);
	 * 
	 * workbook.close(); } catch (FileNotFoundException e) {
	 * e.printStackTrace(); } catch (IOException e) { e.printStackTrace(); }
	 * return filePath;
	 * 
	 * }
	 * 
	 * @RequestMapping(value = "/uploadStudentsHostelForms", method = {
	 * RequestMethod.GET, RequestMethod.POST }) public String
	 * uploadStudentsHostelForms(Model m, Principal principal,
	 * RedirectAttributes redirectAttributes,
	 * 
	 * @ModelAttribute StudentServiceBean studentServiceBean,
	 * 
	 * @RequestParam("file") MultipartFile file) {
	 * logger.info("uploadStudentsBonafideForms ---------------------------" +
	 * studentServiceBean); m.addAttribute("webPage", new
	 * WebPage("viewAssignment", "Hostel Service", true, false)); String
	 * username = principal.getName();
	 * 
	 * Token userdetails1 = (Token) principal; String ProgramName =
	 * userdetails1.getProgramName(); User u =
	 * userService.findByUserName(username);
	 * logger.info("ACAD SESSION------------------------->" +
	 * u.getAcadSession()); if (file.isEmpty()) { setNote(redirectAttributes,
	 * "File is empty!");
	 * 
	 * } else {
	 * 
	 * StudentServiceBean serviceBean = studentWsService
	 * .findByID(studentServiceBean.getId());
	 * 
	 * String statusOfUpload = uploadFileAndReturnStatusForHostel(file,
	 * username, serviceBean); if (statusOfUpload.equalsIgnoreCase("SUCCESS")) {
	 * setSuccess(redirectAttributes, "File uploaded successfully!"); } else {
	 * setError(redirectAttributes, "Error while uploading file!"); } }
	 * 
	 * return "redirect:/viewHostelServiceForStaff?serviceId=" +
	 * studentServiceBean.getId();
	 * 
	 * }
	 * 
	 * private String uploadFileAndReturnStatusForHostel(MultipartFile file,
	 * String username, StudentServiceBean serviceBean) {
	 * 
	 * InputStream inputStream = null;
	 * 
	 * String fileName = file.getOriginalFilename();
	 * 
	 * fileName = RandomStringUtils.randomAlphanumeric(10) +
	 * fileName.substring(fileName.lastIndexOf("."), fileName.length());
	 * List<BonafideForm> bList = new ArrayList<BonafideForm>(); try {
	 * 
	 * inputStream = file.getInputStream(); String filePath = downloadAllFolder
	 * + File.separator + fileName;
	 * 
	 * File folderPath = new File(downloadAllFolder); if (!folderPath.exists())
	 * { boolean created = folderPath.mkdirs(); }
	 * 
	 * File dest = new File(filePath);
	 * 
	 * FileUtils.copyInputStreamToFile(inputStream, dest); List<Map<String,
	 * String>> listCells = excelReader
	 * .readXLSXFileForBonafide(dest.getAbsolutePath()); List<String> ids = new
	 * ArrayList<String>(); for (Map<String, String> m : listCells) {
	 * logger.info("M----------------------------- " + m); HostelForm bean = new
	 * HostelForm();
	 * 
	 * bean.setId(Long.valueOf(m.get("Application ID")));
	 * bean.setUsername(m.get("SAPId")); bean.setFirstname(m.get("First Name"));
	 * bean.setLastname(m.get("Last Name"));
	 * bean.setFatherName(m.get("Father Name"));
	 * bean.setMotherName(m.get("Mother Name"));
	 * bean.setRollNo(m.get("Roll No")); bean.setAcadYear(m.get("Acad Year"));
	 * bean.setProgramName(m.get("Program Name"));
	 * bean.setStudyClass(m.get("Class")); bean.setDivision(m.get("Division"));
	 * bean.setReason(m.get("Reason"));
	 * 
	 * Integer flagNo = 1;
	 * 
	 * if (serviceBean.getLevel1().equalsIgnoreCase(username)) {
	 * bean.setFlag1(m.get("Status")); bean.setRemark1(m.get("Remarks")); } else
	 * if (serviceBean.getLevel2().equalsIgnoreCase(username)) {
	 * bean.setFlag2(m.get("Status")); bean.setRemark2(m.get("Remarks")); flagNo
	 * = 2; } else if (serviceBean.getLevel3().equalsIgnoreCase(username)) {
	 * bean.setFlag3(m.get("Status")); bean.setRemark3(m.get("Remarks")); flagNo
	 * = 3; }
	 * 
	 * // bList.add(bean); logger.info("Status ---- " + m.get("Status"));
	 * logger.info("Remarks ---- " + m.get("Remarks"));
	 * hostelFormService.updateFlagsAndRemarks(String.valueOf(flagNo), (String)
	 * m.get("Status"), (String) m.get("Remarks"), (String)
	 * m.get("Application ID")); }
	 * 
	 * return "SUCCESS";
	 * 
	 * } catch (Exception e) { e.printStackTrace(); logger.error(e); return
	 * "ERROR";
	 * 
	 * }
	 * 
	 * }
	 */
	/*
	 * ------------------------------------------- New Hostel Service Changes
	 * ---------------------------------------
	 */

	@RequestMapping(value = "/createHostelServiceForm", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String createHostelServiceForm(Model m, Principal principal,
			@RequestParam(required = false) Long id) {
		m.addAttribute("webPage", new WebPage("evaluateAssignment",
				"Hostel Service", true, false));
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);
		
		if (id != null) {
			HostelForm hostelForm = hostelFormService.findByID(id);
			m.addAttribute("hostelForm", hostelForm);
		} else {
			m.addAttribute("hostelForm", new HostelForm());
		}
		List<String> mumbaiHostelList = hostelFormService.findHostelNamesByLocation("Mumbai");
		List<String> nmHostelList = hostelFormService.findHostelNamesByLocation("NaviMumbai");
		List<String> hydHostelList = hostelFormService.findHostelNamesByLocation("Hyderabad");
		List<String> bngHostelList = hostelFormService.findHostelNamesByLocation("Banglore");
		m.addAttribute("mumbaiHostelList", mumbaiHostelList);
		m.addAttribute("nmHostelList", nmHostelList);
		m.addAttribute("hydHostelList", hydHostelList);
		m.addAttribute("bngHostelList", bngHostelList);
		
		m.addAttribute("username", username);
		return "hostelService/createHostelService";

	}
	
	@RequestMapping(value = "/saveHostelDetails", method = { RequestMethod.GET,
			RequestMethod.POST })
	public @ResponseBody String saveHostelDetails(@RequestParam String value,
			@RequestParam String pk, @RequestParam String dbCol, @RequestParam String location,
			Principal principal) {
		String username = principal.getName();
		try {
			
			HostelForm hostelForm = new HostelForm();
			hostelForm.setHostelName(pk);
			hostelForm.setLocation(location);
			/*hostelForm.setTotalSeats(value);*/
			hostelForm.setLastModifiedBy(username);
			hostelFormService.saveHostelDetails(hostelForm,dbCol,value);
			return "{\"status\": \"success\", \"msg\": \"TotalSeats saved successfully!\"}";
		} catch (Exception e) {
			logger.error("Exception", e);
			return "{\"status\": \"error\", \"msg\": \"Error in saving TotalSeats!\"}";
		}

	}
	
	

	@RequestMapping(value = "/createHostelService", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String createHostelService(Model m, Principal principal,
			@ModelAttribute HostelForm hostelForm) {
		m.addAttribute("webPage", new WebPage("viewAssignment",
				"Hostel Service", false, false));
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);
		
		ObjectMapper mapper = new ObjectMapper();

		if (hostelForm != null) {
			
			/*hostelForm.setCreatedBy(username);
			hostelForm.setLastModifiedBy(username);*/
			/*hostelFormService.upsert(hostelForm);*/
		
			List<HostelForm> hList = hostelFormService.findByLocation(hostelForm.getLocation());
			if(hList.size()>0){
			hostelFormService.updateByLocation(username, hostelForm.getYear(), hostelForm.getLocation());
			setSuccess(m, "Hostel Service Created Successfully!");
			}else{
				setError(m, "You haven't added hostel details");
			}
			String json;
			/*try {
				
				json = mapper.writeValueAsString(hostelForm);
				logger.info("encoded json--->" + URIUtil.encodeQuery(json));
				logger.info("userRoleMgmtCrudUrl" + userRoleMgmtCrudUrl);
				logger.info(" json--->" + (json));
				WebTarget webTarget = client.target(URIUtil
						.encodeQuery(userRoleMgmtCrudUrl
								+ "/createHostelService?json=" + json));
				Invocation.Builder invocationBuilder = webTarget
						.request(MediaType.APPLICATION_JSON);
				String resp = invocationBuilder.get(String.class);
				logger.info("resp" + resp);
				ObjectMapper objMapper = new ObjectMapper();
				Status status = objMapper.readValue(resp, Status.class);
				if (status.getStatus().SUCCESS != null) {
					setSuccess(m, "Hostel Service Created Successfully!");
				} else {
					setError(m, "Error!");
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			
		}

		return "hostelService/createHostelService";
	}

	@RequestMapping(value = "/viewHostelServices", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String viewHostelServices(Model m, Principal principal) {
		m.addAttribute("webPage", new WebPage("viewAssignment",
				"Hostel Service", false, false));
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);
		

		List<HostelForm> hfList = hostelFormService.findAllActive();
		
		m.addAttribute("hfList", hfList);
		return "hostelService/viewHostelServices";
	}

	@RequestMapping(value = "/viewEachHostelService", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String viewEachHostelService(Model m, Principal principal,
			@RequestParam Long id) {
		m.addAttribute("webPage", new WebPage("viewAssignment",
				"Hostel Service", false, false));
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);
		List<StudentHostelForm> studentList = new ArrayList<StudentHostelForm>();
		ObjectMapper mapper = new ObjectMapper();
		if (id != null) {
			String json;
			try {
				json = mapper.writeValueAsString(id);
				
				// logger.info(" json--->" + (json));
				WebTarget webTarget = client.target(URIUtil
						.encodeQuery(userRoleMgmtCrudUrl
								+ "/findStudentHostelByLocation?json=" + json));
				Invocation.Builder invocationBuilder = webTarget
						.request(MediaType.APPLICATION_JSON);
				String resp = invocationBuilder.get(String.class);
				// logger.info("resp" + resp);
				ObjectMapper objMapper = new ObjectMapper();
				studentList = objMapper.readValue(resp,
						new TypeReference<List<StudentHostelForm>>() {
						});
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			m.addAttribute("studentList", studentList);
		}
		return "hostelService/viewEachHostelService";
	}

	@RequestMapping(value = "/getHostelNamesByLocation", method = {
			RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String getHostelNamesByLocation(
			@RequestParam(name = "location") String location,
			Principal principal) {
		
		String json = "";
		String username = principal.getName();
		StringBuilder sb = new StringBuilder();
		if (location != null) {
			try {

				List<String> hostelsList = hostelFormService
						.findHostelNamesByLocation(location);
				

				int count = 1;
				if (hostelsList.size() != 0) {
					sb.append("<table><tr>");
					sb.append("<th>Hostel Name</th><th>Total Seats</th><th>Hostel Fees</th><th>Refundable Security Deposit</th><th>Total Hostel Fees</th>");
					sb.append("</tr>");
					for (String hostel : hostelsList) {
						sb.append("<tr><td>" + hostel + "</td>");
						sb.append("<td><input name='totalSeats' class='form-control' value='${hostelForm.totalSeats}' type='number' required='required' /></td>");
						sb.append("<td><input name='hostelFees' class='form-control' value='${hostelForm.hostelFees}' type='number' required='required' /></td>");
						sb.append("<td><input name='refundDeposit' class='form-control' value='${hostelForm.refundDeposit}' type='number' required='required' /></td>");
						sb.append("<td><input name='totalFees' class='form-control' value='${hostelForm.totalFees}' type='number' required='required' /></td></tr>");
						count++;
					}
				}

			} catch (Exception ex) {
				logger.error("Exception", ex);
			}
		}

		ObjectMapper mapper = new ObjectMapper();
		try {
			json = mapper.writeValueAsString(sb);
		} catch (JsonProcessingException e) {
			logger.error("Exception" + e);
		}
		return json;

	}
	
	@RequestMapping(value = "/viewNewHostel", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String viewHostels(Model m) {
		
		return "studentService/newHostelService";
	}
}
