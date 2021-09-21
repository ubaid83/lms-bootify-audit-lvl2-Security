package com.spts.lms.web.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.util.URIUtil;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spts.lms.auth.Token;
import com.spts.lms.beans.LmsDb;
import com.spts.lms.beans.MultipleDBReference;
import com.spts.lms.beans.Status;
import com.spts.lms.beans.StatusType;
import com.spts.lms.beans.portalFeedback.PortalFeedback;
import com.spts.lms.beans.portalFeedback.PortalFeedbackQuery;
import com.spts.lms.beans.portalFeedback.PortalFeedbackQuestion;
import com.spts.lms.beans.portalFeedback.PortalFeedbackResponse;
import com.spts.lms.beans.portalFeedback.SortByCreatedDate;
import com.spts.lms.beans.user.Role;
import com.spts.lms.beans.user.User;
import com.spts.lms.daos.portalFeedback.LmsPortalFeedbackDAO;
import com.spts.lms.daos.portalFeedback.LmsPortalFeedbackQueryDAO;
import com.spts.lms.daos.portalFeedback.LmsPortalFeedbackResponseDAO;
import com.spts.lms.daos.user.LmsUserDAO;
import com.spts.lms.services.notification.Notifier;
import com.spts.lms.services.portalFeedback.PortalFeedbackQueryService;
import com.spts.lms.services.portalFeedback.PortalFeedbackQuestionService;
import com.spts.lms.services.portalFeedback.PortalFeedbackResponseService;
import com.spts.lms.services.portalFeedback.PortalFeedbackService;
import com.spts.lms.services.user.UserService;
import com.spts.lms.utils.MultipleDBConnection;
import com.spts.lms.web.helper.WebPage;

@Secured("ROLE_USER")
@Controller
public class PortalFeedbackController extends BaseController {

	private static final Logger logger = Logger
			.getLogger(PortalFeedbackController.class);

	@Autowired
	UserService userService;

	@Autowired
	PortalFeedbackQuestionService portalFeedbackQuestionService;

	@Autowired
	PortalFeedbackService portalFeedbackService;

	@Autowired
	PortalFeedbackResponseService portalFeedbackResponseService;

	@Autowired
	PortalFeedbackQueryService portalFeedbackQueryService;

	@Autowired
	LmsPortalFeedbackDAO lmsportalFeedbackDAO;

	@Autowired
	LmsPortalFeedbackQueryDAO lmsPortalFeedbackQueryDAO;

	@Autowired
	LmsPortalFeedbackResponseDAO lmsPortalFeedbackResponseDAO;

	@Autowired
	LmsUserDAO lmsUserDAO;

	@Autowired
	HttpSession session;

	@Autowired
	Notifier notifier;

	@Value("${lms.assignment.downloadAllFolder}")
	private String downloadAllFolder;

	Client client = ClientBuilder.newClient();

	@Value("${userMgmtCrudUrl}")
	private String userRoleMgmtCrudUrl;

	@Value("${app}")
	private String app;

	@RequestMapping("/portalFeedbackForm")
	public String portalFeedbackForm(Model m, Principal principal) {
		m.addAttribute("webPage", new WebPage("portalFeedback",
				"PortalFeedback", false, true, true, true, false));

		String username = principal.getName();
		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String role = userdetails1.getAuthorities().toString();
		

		PortalFeedback pf = portalFeedbackService.findUserByUsername(username);
		

		if (pf == null) {

			pf = new PortalFeedback();

			List<PortalFeedbackQuestion> portalFeedbackQuestionList = portalFeedbackQuestionService
					.findAll();

			List<PortalFeedbackQuestion> yesNoQuestionList = new ArrayList<PortalFeedbackQuestion>();
			List<PortalFeedbackQuestion> ratingQuestionList = new ArrayList<PortalFeedbackQuestion>();
			List<PortalFeedbackQuestion> commentQuestionList = new ArrayList<PortalFeedbackQuestion>();

			if (userdetails1.getAuthorities().contains(Role.ROLE_FACULTY)) {
				for (PortalFeedbackQuestion pfb : portalFeedbackQuestionList) {

					if (pfb.getQuestionRole().equalsIgnoreCase("ROLE_FACULTY")
							|| pfb.getQuestionRole().equalsIgnoreCase(
									"ROLE_USER")) {

						if (pfb.getType().equalsIgnoreCase("YesNo")) {
							yesNoQuestionList.add(pfb);
						} else if (pfb.getType().equalsIgnoreCase("Rating")) {
							ratingQuestionList.add(pfb);
						} else if (pfb.getType().equalsIgnoreCase("Comment")) {
							commentQuestionList.add(pfb);
						}

					}

				}
			} else if (userdetails1.getAuthorities()
					.contains(Role.ROLE_STUDENT)) {
				for (PortalFeedbackQuestion pfb : portalFeedbackQuestionList) {

					if (pfb.getQuestionRole().equalsIgnoreCase("ROLE_STUDENT")
							|| pfb.getQuestionRole().equalsIgnoreCase(
									"ROLE_USER")) {

						if (pfb.getType().equalsIgnoreCase("YesNo")) {
							yesNoQuestionList.add(pfb);
						} else if (pfb.getType().equalsIgnoreCase("Rating")) {
							ratingQuestionList.add(pfb);
						} else if (pfb.getType().equalsIgnoreCase("Comment")) {
							commentQuestionList.add(pfb);
						}

					}

				}
			}
			
			pf.setYesNoQuestionList(yesNoQuestionList);
			pf.setRatingQuestionList(ratingQuestionList);
			pf.setCommentQuestionList(commentQuestionList);

			m.addAttribute("portalFeedback", pf);
			return "portalFeedback/portalFeedback";
		} else {

			/*
			 * List<PortalFeedbackQuestion> portalFeedbackQuestionList =
			 * portalFeedbackQuestionService
			 * .findAllQuestionsWithResponses(username);
			 */

			List<PortalFeedbackQuery> portalQueryList = portalFeedbackQueryService
					.findQueryByPortalFeedbackId(pf.getId());

			List<PortalFeedbackQuery> queryList = new ArrayList<PortalFeedbackQuery>();

			/*
			 * if (userdetails1.getAuthorities().contains(Role.ROLE_FACULTY)) {
			 * for (PortalFeedbackQuestion pfb : portalFeedbackQuestionList) {
			 * 
			 * if (pfb.getQuestionRole().equalsIgnoreCase("ROLE_FACULTY") ||
			 * pfb.getQuestionRole().equalsIgnoreCase( "ROLE_USER")) {
			 * 
			 * if (pfb.getType().equalsIgnoreCase("YesNo")) {
			 * yesNoQuestionList.add(pfb); } else if
			 * (pfb.getType().equalsIgnoreCase("Rating")) {
			 * ratingQuestionList.add(pfb); } else if
			 * (pfb.getType().equalsIgnoreCase("Comment")) {
			 * commentQuestionList.add(pfb); }
			 * 
			 * }
			 * 
			 * } } else if (userdetails1.getAuthorities()
			 * .contains(Role.ROLE_STUDENT)) {
			 */

			for (PortalFeedbackQuery pfb : portalQueryList) {

				PortalFeedbackQuery portal = portalFeedbackQueryService
						.findQuery(pfb.getId());
				// queryList.add(portal);
				if (portal != null) {

					if (!userdetails1.getAuthorities().contains(
							Role.ROLE_SUPPORT_ADMIN)) {
						queryList = findAllQueriesStudents(portal,
								new ArrayList<PortalFeedbackQuery>());
					} else {
						queryList = findAllQueries(portal,
								new ArrayList<PortalFeedbackQuery>());
					}
				} else {
					// queryList.addAll(portalQueryList);
				}

				
				/*
				 * for (int i = 0; i < list.size(); i++) {
				 * 
				 * PortalFeedbackQuery c = list.get(i);
				 * 
				 * ArrayList<PortalFeedbackQuery> children = getChildren(
				 * c.getId(), list); for (PortalFeedbackQuery child : children)
				 * { list.remove(child); list.add(i + 1, child); }
				 * c.setChildrenList(children); }
				 */

				// queryList.add(q);
			}
			queryList.addAll(portalQueryList);

			// }

			
			if (queryList.size() != 0) {
				for (PortalFeedbackQuery pfq : queryList) {

					String answer = pfq.getAnswer();
					Document doc = Jsoup.parse(answer);
					String ans = doc.text();
					ans.trim();
					pfq.setAnswer(ans);
					if (pfq.getCreatedBy() != null) {
						User user = userService.findByUserName(pfq
								.getCreatedBy());
						if (user != null) {
							pfq.setFirstname(user.getFirstname());
							pfq.setLastname(user.getLastname());
						}
					}
					Date date = new Date();
					SimpleDateFormat formatter = new SimpleDateFormat(
							"dd/MM/yyyy");
					String strDate = formatter.format(pf.getCreatedDate());
					pfq.setCreatedOn(strDate);
					

					queryList.set(queryList.indexOf(pfq), pfq);

				}
			}

			Collections.sort(queryList, new SortByCreatedDate());
			m.addAttribute("portalFeedbackQuery", new PortalFeedbackQuery());
			m.addAttribute("queryList", queryList);
			m.addAttribute("portalFeedback", pf);
			m.addAttribute("feedbackNote", "true");
			m.addAttribute("feedbackNoteMessage",
					"You have already submitted your feedback");
			// return "portalFeedback/feedbackAlert";
			m.addAttribute("portalFeedbackQuery", new PortalFeedbackQuery());
			m.addAttribute("username", username);
			return "portalFeedback/portalFeedbackSubmitted";

		}
	}

	public List<PortalFeedbackQuery> findAllQueries(PortalFeedbackQuery portal,
			List<PortalFeedbackQuery> list) {
		Long pId = portal.getId();

		list.add(portal);
		portal = lmsPortalFeedbackQueryDAO.findQuery(pId);

		if (portal != null) {
			findAllQueries(portal, list);
		} else {
			return list;
		}
		return list;

	}

	public List<PortalFeedbackQuery> findAllQueriesStudents(
			PortalFeedbackQuery portal, List<PortalFeedbackQuery> list) {
		Long pId = portal.getId();

		list.add(portal);
		portal = portalFeedbackQueryService.findQuery(pId);

		if (portal != null) {
			findAllQueriesStudents(portal, list);
		} else {
			return list;
		}
		return list;

	}

	public boolean isNullOrEmpty(String str) {
		if (str != null && !str.isEmpty())
			return true;
		return false;
	}

	@RequestMapping(value = "/createPortalFeedbackResponse", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String createPortalFeedbackResponse(
			@ModelAttribute PortalFeedback portalFeedback,
			RedirectAttributes redirectAttr, Principal principal, Model m) {

		String username = principal.getName();
		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);
		portalFeedback.setUsername(username);

		String role = userdetails1.getAuthorities().toString();

		if (role.contains("ROLE_FACULTY")) {
			role = "ROLE_FACULTY";
			portalFeedback.setUserRole(role);
		} else if (role.contains("ROLE_STUDENT")) {
			role = "ROLE_STUDENT";
			portalFeedback.setUserRole(role);
		}
		
		portalFeedback.setCreatedBy(username);
		portalFeedback.setLastModifiedBy(username);

		portalFeedbackService.insertWithIdReturn(portalFeedback);
		

		List<PortalFeedbackResponse> portalFeedbackResponseList = new ArrayList<PortalFeedbackResponse>();

		List<PortalFeedbackQuestion> yesNoQuestionList = portalFeedback
				.getYesNoQuestionList();
		List<PortalFeedbackQuestion> ratingQuestionList = portalFeedback
				.getRatingQuestionList();
		List<PortalFeedbackQuestion> commentQuestionList = portalFeedback
				.getCommentQuestionList();
		String subject = "";

		for (PortalFeedbackQuestion pfq : yesNoQuestionList) {

			PortalFeedbackResponse pfr = new PortalFeedbackResponse();
			pfr.setPortalFeedbackId(portalFeedback.getId());
			pfr.setPortalFeedbackQuestionId(pfq.getId());
			pfr.setAnswer(pfq.getPortalFeedbackResponse().getAnswer());
			portalFeedbackResponseList.add(pfr);
		}
		for (PortalFeedbackQuestion pfq : ratingQuestionList) {

			PortalFeedbackResponse pfr = new PortalFeedbackResponse();
			pfr.setPortalFeedbackId(portalFeedback.getId());
			pfr.setPortalFeedbackQuestionId(pfq.getId());
			pfr.setAnswer(pfq.getPortalFeedbackResponse().getAnswer());
			portalFeedbackResponseList.add(pfr);
		}
		for (PortalFeedbackQuestion pfq : commentQuestionList) {

			PortalFeedbackResponse pfr = new PortalFeedbackResponse();
			pfr.setPortalFeedbackId(portalFeedback.getId());
			pfr.setPortalFeedbackQuestionId(pfq.getId());
			pfr.setAnswer(pfq.getPortalFeedbackResponse().getAnswer());
			portalFeedbackResponseList.add(pfr);
			PortalFeedbackQuery query = new PortalFeedbackQuery();
			query.setAnswer(pfq.getPortalFeedbackResponse().getAnswer());
			subject = pfq.getPortalFeedbackResponse().getAnswer();
			query.setCreatedBy(username);
			query.setLastModifiedBy(username);
			query.setPortalFeedbackId(portalFeedback.getId());
			portalFeedbackQueryService.insertWithIdReturn(query);
		}

		portalFeedbackResponseService.insertBatch(portalFeedbackResponseList);
		List<String> studentList = new ArrayList<String>();
		if (!role.contains("ROLE_SUPPORT_ADMIN")) {

			studentList = portalFeedbackQueryService
					.findSupportAdminUsernames();

		}
		if (role.contains("ROLE_SUPPORT_ADMIN")) {

			studentList.add(username);
		}
		
		// String answer = subject;
		Document doc = Jsoup.parse(subject);
		String ans = doc.text();
		ans.trim();
		// pfq.setAnswer(ans);
		subject = ans;
		// StringBuffer buff = new StringBuffer(subject);
		// buff.append(portalFeedbackQuery.getAnswer());

		// subject = buff.toString();
		// StringUtils.isEmpty(subject.trim());
		if (isNullOrEmpty(subject.trim())) {
			if (!studentList.isEmpty() || studentList.size() != 0) {
				Map<String, Map<String, String>> result = null;
				if (!role.contains("ROLE_SUPPORT_ADMIN")) {
					subject = subject
							.concat("New Portal Feedback Created By - "
									+ username + " School - " + app);

					result = userService.findEmailByUserName(studentList);
				} else {
					result = lmsUserDAO.findEmailByUserName(studentList);
				}
				

				Map<String, String> email = result.get("emails");
				email.put("Samir Singh", "Samir.Singh@nmims.edu");
				Map<String, String> mobiles = new HashMap();
				notifier.sendEmail(email, mobiles, subject, subject);
			}
		}
		m.addAttribute("feedbackSuccess", "true");
		m.addAttribute("feedbackSuccessMessage",
				"Feedback Submitted Successfully");
		return "portalFeedback/feedbackAlert";
	}

	@RequestMapping(value = "/replyToQueryAnswer", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String replyToQueryAnswer(Model m, Principal principal,
			RedirectAttributes redirectAttrs, @RequestParam Long qId,
			/*
			 * @RequestParam String url, @RequestParam String dbUsername,
			 * 
			 * @RequestParam String password, @RequestParam String dbName,
			 */
			@ModelAttribute PortalFeedbackQuery portalFeedbackQuery) {
		String username = principal.getName();
		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);
		m.addAttribute("webPage", new WebPage("contentList", "Portal Feedback",
				true, false));
		String role = userdetails1.getAuthorities().toString();
		
		/*
		 * logger.info("url ------ " + url); logger.info("dbUsername ------ " +
		 * dbUsername); logger.info("password ------ " + password);
		 * logger.info("dbName ------ " + dbName); logger.info("qId ------ " +
		 * qId);
		 */

		List<String> studentList = new ArrayList<String>();
		// studentList.add("portal_app_team@svkm.ac.in");
		
		try {
			Status status = new Status();
			MultipleDBConnection multipleDBConnection = new MultipleDBConnection();

			
			DriverManagerDataSource dataSourceLms = multipleDBConnection
					.createConnectionByDS((String) session.getAttribute("url"),
							(String) session.getAttribute("dbUsername"),
							(String) session.getAttribute("password"),
							(String) session.getAttribute("dbName"));

			lmsportalFeedbackDAO.setDS(dataSourceLms);
			lmsPortalFeedbackQueryDAO.setDS(dataSourceLms);
			lmsPortalFeedbackResponseDAO.setDS(dataSourceLms);
			lmsUserDAO.setDS(dataSourceLms);

			LmsDb lmsDb = new LmsDb();
			lmsDb.setLmsDb((String) session.getAttribute("dbName"));
			lmsportalFeedbackDAO.getLmsDb(lmsDb);
			lmsPortalFeedbackQueryDAO.getLmsDb(lmsDb);
			lmsPortalFeedbackResponseDAO.getLmsDb(lmsDb);
			lmsUserDAO.getLmsDb(lmsDb);

			status.setStatus(StatusType.SUCCESS);
			PortalFeedbackQuery portal = new PortalFeedbackQuery();

			if (!role.contains("ROLE_SUPPORT_ADMIN")) {
				portal = portalFeedbackQueryService.findQueryById(qId);
				
				studentList = portalFeedbackQueryService
						.findSupportAdminUsernames();

			}
			if (role.contains("ROLE_SUPPORT_ADMIN")) {
				portal = lmsPortalFeedbackQueryDAO.findQueryById(qId);
				
				studentList.add(portal.getCreatedBy());
			}
			
			portalFeedbackQuery.setAnswer(portalFeedbackQuery.getAnswer()
					.trim());
			portalFeedbackQuery.setCreatedBy(username);
			portalFeedbackQuery.setLastModifiedBy(username);
			portalFeedbackQuery.setParentId(qId);
			portalFeedbackQuery.setPortalFeedbackId(portal
					.getPortalFeedbackId());
			if (!role.contains("ROLE_SUPPORT_ADMIN")) {
				portalFeedbackQueryService
						.insertWithIdReturn(portalFeedbackQuery);
			} else {
				lmsPortalFeedbackQueryDAO
						.insertWithIdReturn(portalFeedbackQuery);
			}

			String answer = portalFeedbackQuery.getAnswer();
			Document doc = Jsoup.parse(answer);
			String ans = doc.text();
			ans.trim();
			// pfq.setAnswer(ans);
			String subject = ans;
			// StringBuffer buff = new StringBuffer(subject);
			// buff.append(portalFeedbackQuery.getAnswer());

			// subject = buff.toString();
			if (!studentList.isEmpty() || studentList.size() != 0) {
				Map<String, Map<String, String>> result = null;
				if (!role.contains("ROLE_SUPPORT_ADMIN")) {
					subject = subject.concat(" By - " + username);

					result = userService.findEmailByUserName(studentList);
				} else {
					result = lmsUserDAO.findEmailByUserName(studentList);
				}
				

				Map<String, String> email = result.get("emails");
				email.put("Samir Singh", "Samir.Singh@nmims.edu");
				Map<String, String> mobiles = new HashMap();
				notifier.sendEmail(email, mobiles, subject, subject);
			}
			setSuccess(redirectAttrs, "Replied Successfully!");
		} catch (Exception e) {
			logger.error("Error! ", e);
			setError(redirectAttrs, "Error while replying!");

		}

		if (!role.contains("ROLE_SUPPORT_ADMIN")) {
			return "redirect:/portalFeedbackForm";
		}
		if (role.contains("ROLE_SUPPORT_ADMIN")) {
			return "redirect:/viewPortalFeedbacks?schoolObjId="
					+ session.getAttribute("schoolObjId");
		}
		return "redirect:/portalFeedbackForm";

	}

	@RequestMapping(value = "/viewPortalFeedbacks", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String viewPortalFeedbacks(Principal principal, Model m,
			@RequestParam String schoolObjId) {
		String username = principal.getName();
		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);
		m.addAttribute("webPage", new WebPage("viewPortalFeedbacks",
				"Portal Feedback", true, false));
		String role = userdetails1.getAuthorities().toString();
		List<PortalFeedback> feedbackList = new ArrayList<PortalFeedback>();
		List<String> schoolList = new ArrayList<String>();
		MultipleDBReference multipleDBReference = new MultipleDBReference();
		PortalFeedback portalFeedback = new PortalFeedback();
		session.setAttribute("schoolObjId", schoolObjId);
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		if (role.contains("ROLE_SUPPORT_ADMIN")) {

			try {
				WebTarget webTarget = null;
				Invocation.Builder invocationBuilder;
				String resp = null;
				ObjectMapper mapper = new ObjectMapper();
				if (schoolObjId != null) {
					schoolList = Arrays.asList(schoolObjId.split(","));
					
					String json = mapper.writeValueAsString(schoolList);
					webTarget = client.target(URIUtil
							.encodeQuery(userRoleMgmtCrudUrl
									+ "/getdbNameFromUsermgmt?schoolList="
									+ json));
				}

				invocationBuilder = webTarget
						.request(MediaType.APPLICATION_JSON);
				resp = invocationBuilder.get(String.class);
				// logger.info("resp from uname" + resp);
				if (resp == null) {
					setError(m, "Error in getting school list");
				} else {

					// List<MultipleDBReference> listOfSchoolAndCampus =
					// (List<MultipleDBReference>) mapper
					// .readValue(resp, MultipleDBReference.class);
					List<MultipleDBReference> dbList = new ArrayList<MultipleDBReference>();
					dbList = mapper.readValue(resp,
							new TypeReference<List<MultipleDBReference>>() {
							});
					
					String dbListName = "";
					for (MultipleDBReference db : dbList) {
						if (dbListName.length() == 0) {
							dbListName = dbListName.concat(db.getDbName());
						} else {
							dbListName = dbListName
									.concat("," + db.getDbName());
						}

					}
					
					m.addAttribute("dbListName", dbListName);
					m.addAttribute("dbList", dbList);
					for (MultipleDBReference dbName : dbList) {
						session.setAttribute("url", dbName.getUrl());
						session.setAttribute("dbUsername", dbName.getUsername());
						session.setAttribute("password", dbName.getPassword());
						session.setAttribute("dbName", dbName.getDbName());
						portalFeedback.setUrl(dbName.getUrl());
						portalFeedback.setDbName(dbName.getDbName());
						portalFeedback.setDbUsername(dbName.getUsername());
						portalFeedback.setPassword(dbName.getPassword());
						// multipleDBReference = dbName;

						// webpagesService.insertWithIdReturn(webpages);
						// Status status = insertArticle(dbName, webpages);
						feedbackList = getPortalFeedbacksForSchoolWithDiscussion(dbName);

					}

					for (PortalFeedback pf : feedbackList) {
						List<PortalFeedbackQuery> queryList = pf
								.getPortalFeedbackQueryList();
						if (queryList != null) {
							if (queryList.size() != 0) {
								
								
								if (queryList.get(0).getCreatedBy()
										.contains("SUPPORT_ADMIN")) {
									
									pf.setRepliedByAdmin("REPLIED");
								} else {
									
									pf.setRepliedByAdmin("NOT REPLIED");
								}
							} else {
								
								pf.setRepliedByAdmin("NOT REPLIED");
							}
							pf.setPortalFeedbackQueryList(queryList);
						} else {
							pf.setRepliedByAdmin("NOT REPLIED");
						}
						String createdOn = formatter
								.format(pf.getCreatedDate());
						
						pf.setCreatedOn(createdOn);

						feedbackList.set(feedbackList.indexOf(pf), pf);
					}

				}

			} catch (Exception e) {
				logger.error("Exception", e);
				setError(m, "Exception" + e);
			}

		}
		
		m.addAttribute("multipleDBReference", multipleDBReference);
		m.addAttribute("feedbackList", feedbackList);
		m.addAttribute("portalFeedback", portalFeedback);
		return "portalFeedback/feedbackList";
	}

	public List<PortalFeedback> getPortalFeedbacksForSchool(
			MultipleDBReference multipleDBReference) {
		List<PortalFeedback> feedbackList = new ArrayList<PortalFeedback>();

		Status status = new Status();
		try {
			MultipleDBConnection multipleDBConnection = new MultipleDBConnection();

			
			DriverManagerDataSource dataSourceLms = multipleDBConnection
					.createConnectionByDS(multipleDBReference.getUrl(),
							multipleDBReference.getUsername(),
							multipleDBReference.getPassword(),
							multipleDBReference.getDbName());

			lmsportalFeedbackDAO.setDS(dataSourceLms);
			lmsPortalFeedbackQueryDAO.setDS(dataSourceLms);
			LmsDb lmsDb = new LmsDb();
			lmsDb.setLmsDb(multipleDBReference.getDbName());
			lmsportalFeedbackDAO.getLmsDb(lmsDb);
			lmsPortalFeedbackQueryDAO.getLmsDb(lmsDb);

			feedbackList = lmsportalFeedbackDAO.findAllFeedbacks();
			for (PortalFeedback pf : feedbackList) {
				
				List<PortalFeedbackQuery> portalQueryList = lmsPortalFeedbackQueryDAO
						.findQueryByPortalFeedbackId(pf.getId());
				List<PortalFeedbackQuery> queryList = new ArrayList<PortalFeedbackQuery>();
				if (portalQueryList.size() != 0) {

					for (PortalFeedbackQuery pfb : portalQueryList) {
						
						PortalFeedbackQuery portal = lmsPortalFeedbackQueryDAO
								.findQuery(pfb.getId());
						if (portal != null) {

							queryList = findAllQueries(portal,
									new ArrayList<PortalFeedbackQuery>());
							
						}
					}
				}
				pf.setPortalFeedbackQueryList(queryList);
				feedbackList.set(feedbackList.indexOf(pf), pf);

			}
			status.setStatus(StatusType.SUCCESS);
		} catch (Exception ex) {
			status.setStatus(StatusType.ERROR);
			logger.error("Exception", ex);
		}

		return feedbackList;
	}

	public List<PortalFeedback> getPortalFeedbacksForSchoolWithDiscussion(
			MultipleDBReference multipleDBReference) {
		List<PortalFeedback> feedbackList = new ArrayList<PortalFeedback>();

		Status status = new Status();
		try {
			MultipleDBConnection multipleDBConnection = new MultipleDBConnection();

			
			DriverManagerDataSource dataSourceLms = multipleDBConnection
					.createConnectionByDS(multipleDBReference.getUrl(),
							multipleDBReference.getUsername(),
							multipleDBReference.getPassword(),
							multipleDBReference.getDbName());

			lmsportalFeedbackDAO.setDS(dataSourceLms);
			lmsPortalFeedbackQueryDAO.setDS(dataSourceLms);
			LmsDb lmsDb = new LmsDb();
			lmsDb.setLmsDb(multipleDBReference.getDbName());
			lmsportalFeedbackDAO.getLmsDb(lmsDb);
			lmsPortalFeedbackQueryDAO.getLmsDb(lmsDb);

			feedbackList = lmsportalFeedbackDAO.findAllFeedbacks();
			for (PortalFeedback pf : feedbackList) {
			
				List<PortalFeedbackQuery> portalQueryList = lmsPortalFeedbackQueryDAO
						.findQueryByPortalFeedbackId(pf.getId());
				List<PortalFeedbackQuery> queryList = new ArrayList<PortalFeedbackQuery>();
				if (portalQueryList.size() != 0) {

					for (PortalFeedbackQuery pfb : portalQueryList) {
						
						PortalFeedbackQuery portal = lmsPortalFeedbackQueryDAO
								.findQuery(pfb.getId());
						if (portal != null) {

							queryList = findAllQueries(portal,
									new ArrayList<PortalFeedbackQuery>());
							
							Collections
									.sort(queryList, new SortByCreatedDate());
							pf.setPortalFeedbackQueryList(queryList);
						}
					}
				} else {
				}

				feedbackList.set(feedbackList.indexOf(pf), pf);

			}
			status.setStatus(StatusType.SUCCESS);
		} catch (Exception ex) {
			status.setStatus(StatusType.ERROR);
			logger.error("Exception", ex);
		}

		return feedbackList;
	}

	@RequestMapping("/viewEachPortalFeedback")
	public String viewEachPortalFeedback(Model m, Principal principal,
			@ModelAttribute PortalFeedback portalFeedback,
			RedirectAttributes redirectAttributes,
			/*
			 * @RequestParam String url, @RequestParam String dbUsername,
			 * 
			 * @RequestParam String password, @RequestParam String dbName,
			 */
			@RequestParam Long portalFeedbackId, @RequestParam String uname) {
		m.addAttribute("webPage", new WebPage("portalFeedback",
				"PortalFeedback", false, true, true, true, false));

		String username = principal.getName();
		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String role = userdetails1.getAuthorities().toString();
		
		if (portalFeedback != null) {
			Status status = new Status();
			try {
				MultipleDBConnection multipleDBConnection = new MultipleDBConnection();

				
				DriverManagerDataSource dataSourceLms = multipleDBConnection
						.createConnectionByDS(
								(String) session.getAttribute("url"),
								(String) session.getAttribute("dbUsername"),
								(String) session.getAttribute("password"),
								(String) session.getAttribute("dbName"));

				lmsportalFeedbackDAO.setDS(dataSourceLms);
				lmsPortalFeedbackQueryDAO.setDS(dataSourceLms);
				lmsPortalFeedbackResponseDAO.setDS(dataSourceLms);

				LmsDb lmsDb = new LmsDb();
				lmsDb.setLmsDb((String) session.getAttribute("dbName"));
				lmsportalFeedbackDAO.getLmsDb(lmsDb);
				lmsPortalFeedbackQueryDAO.getLmsDb(lmsDb);
				lmsPortalFeedbackResponseDAO.getLmsDb(lmsDb);

				status.setStatus(StatusType.SUCCESS);
				List<PortalFeedback> pfList = lmsportalFeedbackDAO.findAll();
				PortalFeedback pf = lmsportalFeedbackDAO
						.findOne(portalFeedbackId);
				

				/*
				 * List<PortalFeedbackQuestion> portalFeedbackQuestionList =
				 * portalFeedbackQuestionService
				 * .findAllQuestionsWithResponses(username);
				 */
				if (pf != null) {

					List<PortalFeedbackQuery> portalQueryList = lmsPortalFeedbackQueryDAO
							.findQueryByPortalFeedbackId(pf.getId());
					

					List<PortalFeedbackQuery> queryList = new ArrayList<PortalFeedbackQuery>();
					if (portalQueryList.size() == 0) {

						List<PortalFeedbackResponse> pfrList = lmsPortalFeedbackResponseDAO
								.findByPortalFeedbackId(pf.getId());
						if (pfrList.size() != 0) {
							
							for (PortalFeedbackResponse pfr : pfrList) {
								
								
								if (!pfr.getAnswer().equalsIgnoreCase("")) {
									
									PortalFeedbackQuery pfq = new PortalFeedbackQuery();
									pfq.setPortalFeedbackId(pf.getId());
									pfq.setAnswer(pfr.getAnswer());
									pfq.setActive("Y");
									pfq.setCreatedDate(pf.getCreatedDate());
									pfq.setCreatedBy(pf.getUsername());
									pfq.setLastModifiedBy(pf.getUsername());
									lmsPortalFeedbackQueryDAO
											.insertWithIdReturn(pfq);
								} else {
									setNote(redirectAttributes,
											"No Query From the user!");

									return "redirect:/viewSchoolsList";
								}
							}
							return "redirect:/viewEachPortalFeedback?portalFeedbackId="
									+ pf.getId() + "&uname=" + uname;
						}

					} else {
						for (PortalFeedbackQuery pfb : portalQueryList) {

							PortalFeedbackQuery portal = lmsPortalFeedbackQueryDAO
									.findQuery(pfb.getId());
							// queryList.add(portal);
							if (portal != null) {
								queryList = findAllQueries(portal,
										new ArrayList<PortalFeedbackQuery>());
							} else {
								// queryList.addAll(portalQueryList);
							}

							
							/*
							 * for (int i = 0; i < list.size(); i++) {
							 * 
							 * PortalFeedbackQuery c = list.get(i);
							 * 
							 * ArrayList<PortalFeedbackQuery> children =
							 * getChildren( c.getId(), list); for
							 * (PortalFeedbackQuery child : children) {
							 * list.remove(child); list.add(i + 1, child); }
							 * c.setChildrenList(children); }
							 */

							// queryList.add(q);
						}
						queryList.addAll(portalQueryList);
					}
					// }

					
					if (queryList.size() != 0) {
						for (PortalFeedbackQuery pfq : queryList) {
							

							String answer = pfq.getAnswer();
							Document doc = Jsoup.parse(answer);
							String ans = doc.text();
							ans.trim();
							pfq.setAnswer(ans);
							if (pfq.getCreatedBy() != null) {
								User user = userService.findByUserName(pfq
										.getCreatedBy());
								if (user != null) {
									pfq.setFirstname(user.getFirstname());
									pfq.setLastname(user.getLastname());
								}
							}
							Date date = new Date();
							SimpleDateFormat formatter = new SimpleDateFormat(
									"dd/MM/yyyy");
							String strDate = formatter.format(pf
									.getCreatedDate());
							pfq.setCreatedOn(strDate);
							

							queryList.set(queryList.indexOf(pfq), pfq);

						}
					}
					Collections.sort(queryList, new SortByCreatedDate());

					m.addAttribute("queryList", queryList);
				}
				m.addAttribute("portalFeedbackQuery", new PortalFeedbackQuery());

				m.addAttribute("portalFeedback", pf);
				m.addAttribute("feedbackNote", "true");
				m.addAttribute("feedbackNoteMessage",
						"You have already submitted your feedback");
			} catch (Exception ex) {
				status.setStatus(StatusType.ERROR);
				logger.error("Exception", ex);
			}
		}

		// return "portalFeedback/feedbackAlert";
		PortalFeedbackQuery portalFeedbackQuery = new PortalFeedbackQuery();
		/*
		 * portalFeedbackQuery.setUrl(url);
		 * portalFeedbackQuery.setDbName(dbName);
		 * portalFeedbackQuery.setDbUsername(dbUsername);
		 * portalFeedbackQuery.setPassword(password); m.addAttribute("url",
		 * url); m.addAttribute("dbUsername", dbUsername);
		 * m.addAttribute("password", password); m.addAttribute("dbName",
		 * dbName);
		 */
		m.addAttribute("portalFeedbackQuery", portalFeedbackQuery);
		m.addAttribute("username", username);
		return "portalFeedback/portalFeedbackSubmitted";

	}

	/*
	 * @RequestMapping(value = "/viewSchoolsList", method = { RequestMethod.GET,
	 * RequestMethod.POST }) public String viewSchoolsList(Model m, Principal
	 * principal, RedirectAttributes redirectAttributes) {
	 * m.addAttribute("webPage", new WebPage("viewAssignment", "PortalFeedback",
	 * true, true, true, true, false));
	 * 
	 * String username = principal.getName(); Token userdetails1 = (Token)
	 * principal; String ProgramName = userdetails1.getProgramName(); User u =
	 * userService.findByUserName(username);
	 * 
	 * String role = userdetails1.getAuthorities().toString();
	 * logger.info("Role---------->" + role);
	 * 
	 * WebTarget webTarget; Invocation.Builder invocationBuilder; String resp =
	 * null; ObjectMapper mapper = new ObjectMapper(); try { webTarget =
	 * client.target(URIUtil.encodeQuery(userRoleMgmtCrudUrl +
	 * "/getSchoolsListWithDetails")); invocationBuilder =
	 * webTarget.request(MediaType.APPLICATION_JSON); resp =
	 * invocationBuilder.get(String.class); logger.info("resp from uname" +
	 * resp); if (resp == null) { setError(m, "Error in getting school list"); }
	 * else {
	 * 
	 * List<PortalFeedback> listOfSchools = mapper.readValue(resp, new
	 * TypeReference<List<PortalFeedback>>() { });
	 * logger.info("listOfSchools --------- " + listOfSchools);
	 * m.addAttribute("listOfSchools", listOfSchools);
	 * 
	 * } } catch (URIException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } catch (JsonParseException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); } catch
	 * (JsonMappingException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } catch (IOException e) { // TODO Auto-generated
	 * catch block e.printStackTrace(); } return
	 * "portalFeedback/viewSchoolsList"; }
	 */

	@RequestMapping(value = "/getDiscussionByPortalFeedbackId", method = {
			RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String getDiscussionByPortalFeedbackId(
			@RequestParam(name = "id") String id, Principal principal) {
		
		String json = "";
		String username = principal.getName();
		StringBuilder sb = new StringBuilder();
		if (id != null) {
			Status status = new Status();
			try {
				MultipleDBConnection multipleDBConnection = new MultipleDBConnection();

				
				DriverManagerDataSource dataSourceLms = multipleDBConnection
						.createConnectionByDS(
								(String) session.getAttribute("url"),
								(String) session.getAttribute("dbUsername"),
								(String) session.getAttribute("password"),
								(String) session.getAttribute("dbName"));

				lmsportalFeedbackDAO.setDS(dataSourceLms);
				lmsPortalFeedbackQueryDAO.setDS(dataSourceLms);
				lmsPortalFeedbackResponseDAO.setDS(dataSourceLms);
				lmsUserDAO.setDS(dataSourceLms);

				LmsDb lmsDb = new LmsDb();
				lmsDb.setLmsDb((String) session.getAttribute("dbName"));
				lmsportalFeedbackDAO.getLmsDb(lmsDb);
				lmsPortalFeedbackQueryDAO.getLmsDb(lmsDb);
				lmsPortalFeedbackResponseDAO.getLmsDb(lmsDb);
				lmsUserDAO.getLmsDb(lmsDb);

				status.setStatus(StatusType.SUCCESS);
				List<PortalFeedback> pfList = lmsportalFeedbackDAO.findAll();
				PortalFeedback pf = lmsportalFeedbackDAO.findOne(Long
						.valueOf(id));
				

				if (pf != null) {

					List<PortalFeedbackQuery> portalQueryList = lmsPortalFeedbackQueryDAO
							.findQueryByPortalFeedbackId(pf.getId());
					

					List<PortalFeedbackQuery> queryList = new ArrayList<PortalFeedbackQuery>();
					if (portalQueryList.size() == 0) {

						List<PortalFeedbackResponse> pfrList = lmsPortalFeedbackResponseDAO
								.findByPortalFeedbackId(pf.getId());
						if (pfrList.size() != 0) {
							
							for (PortalFeedbackResponse pfr : pfrList) {
								
								if (!pfr.getAnswer().equalsIgnoreCase("")) {
									
									PortalFeedbackQuery pfq = new PortalFeedbackQuery();
									pfq.setPortalFeedbackId(pf.getId());
									pfq.setAnswer(pfr.getAnswer());
									pfq.setActive("Y");
									pfq.setCreatedDate(pf.getCreatedDate());
									pfq.setCreatedBy(pf.getUsername());
									pfq.setLastModifiedBy(pf.getUsername());
									lmsPortalFeedbackQueryDAO
											.insertWithIdReturn(pfq);
								} else {

								}
							}

						}

					} else {
						for (PortalFeedbackQuery pfb : portalQueryList) {

							PortalFeedbackQuery portal = lmsPortalFeedbackQueryDAO
									.findQuery(pfb.getId());
							// queryList.add(portal);
							if (portal != null) {
								queryList = findAllQueries(portal,
										new ArrayList<PortalFeedbackQuery>());
							} else {
								// queryList.addAll(portalQueryList);
							}

							
							/*
							 * for (int i = 0; i < list.size(); i++) {
							 * 
							 * PortalFeedbackQuery c = list.get(i);
							 * 
							 * ArrayList<PortalFeedbackQuery> children =
							 * getChildren( c.getId(), list); for
							 * (PortalFeedbackQuery child : children) {
							 * list.remove(child); list.add(i + 1, child); }
							 * c.setChildrenList(children); }
							 */

							// queryList.add(q);
						}
						queryList.addAll(portalQueryList);
					}
					// }

					
					if (queryList.size() != 0) {
						for (PortalFeedbackQuery pfq : queryList) {

							

							String answer = pfq.getAnswer();
							Document doc = Jsoup.parse(answer);
							String ans = doc.text();
							ans.trim();
							pfq.setAnswer(ans);
							if (pfq.getCreatedBy() != null) {
								User user = lmsUserDAO.findByUserName(pf
										.getUsername());
								if (user != null) {
									pfq.setFirstname(user.getFirstname());
									pfq.setLastname(user.getLastname());
								}
							}
							Date date = new Date();
							SimpleDateFormat formatter = new SimpleDateFormat(
									"dd/MM/yyyy");
							String strDate = formatter.format(pf
									.getCreatedDate());
							pfq.setCreatedOn(strDate);

							queryList.set(queryList.indexOf(pfq), pfq);

						}
					}
					Collections.sort(queryList, new SortByCreatedDate());
					int count = 1;
					for (PortalFeedbackQuery pfq : queryList) {
						if (pfq.getCreatedBy().equalsIgnoreCase(username)) {
							sb.append("<div class='contain' style='text-align: -webkit-right;'>");
							sb.append("<p style='font-size: medium; color: black; overflow: auto;'>");
							sb.append(pfq.getAnswer());
							sb.append("</p>");
							if (count == 1) {
								sb.append("<span class='time-right'>");
								sb.append("<a href='#' title='Reply' class='fa fa-mail-reply' onclick='myFunction()' id="
										+ pfq.getId() + "></a>");
								sb.append("</span>");
							}
							sb.append("You |" + pfq.getCreatedOn());
							sb.append("<div id='replyText" + pfq.getId()
									+ "'style='display: none;'>");
							sb.append("<form:label path='answer'>Enter Your Reply</form:label>");
							sb.append("<textarea rows='4' cols='50' name='answer' path='answer' value='"
									+ pfq.getAnswer() + "'/>");
							sb.append("<button id='submit' class='btn btn-large btn-primary' formaction='replyToQueryAnswer?qId="
									+ pfq.getId() + "'>Reply</button>");
							sb.append("</div>");
							sb.append("</div>");
						} else {
							sb.append("<div class='contain darker'>");
							sb.append("<p style='font-size: medium; color: black; overflow: auto;'>"
									+ pfq.getAnswer() + "</p>");
							if (count == 1) {
								sb.append("<span class='time-right'>");
								sb.append("<a href='#' title='Reply' class='fa fa-mail-reply' onclick='myFunction()' id="
										+ pfq.getId() + "></a>");
								sb.append("</span>");
							}
							sb.append(pfq.getFirstname() + " "
									+ pfq.getLastname() + " "
									+ pfq.getCreatedOn());
							sb.append("<div id='replyText" + pfq.getId()
									+ "'style='display: none;'>");
							sb.append("<form:label path='answer'>Enter Your Reply</form:label>");
							sb.append("<textarea rows='4' cols='50' name='answer' path='answer' value='"
									+ pfq.getAnswer() + "'/>");
							sb.append("<button id='submit' class='btn btn-large btn-primary' formaction='replyToQueryAnswer?qId="
									+ pfq.getId() + "'>Reply</button>");
							sb.append("</div>");
							sb.append("</div>");
						}

						count++;
					}
				}

			} catch (Exception ex) {
				status.setStatus(StatusType.ERROR);
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

	@RequestMapping(value = "/viewSchoolsList", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String viewSchoolsList(Model m, Principal principal,
			RedirectAttributes redirectAttributes) {
		m.addAttribute("webPage", new WebPage("viewAssignment",
				"PortalFeedback", true, true, true, true, false));

		String username = principal.getName();
		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String role = userdetails1.getAuthorities().toString();
		

		WebTarget webTarget;
		Invocation.Builder invocationBuilder;
		String resp = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			webTarget = client.target(URIUtil.encodeQuery(userRoleMgmtCrudUrl
					+ "/getSchoolsListWithDetails"));
			invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
			resp = invocationBuilder.get(String.class);
			// logger.info("resp from uname" + resp);
			if (resp == null) {
				setError(m, "Error in getting school list");
			} else {

				List<PortalFeedback> listOfSchools = mapper.readValue(resp,
						new TypeReference<List<PortalFeedback>>() {
						});
				
				m.addAttribute("listOfSchools", listOfSchools);

			}
		} catch (URIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "portalFeedback/viewSchoolsList";
	}

	@RequestMapping(value = "/getQueryListByQueryId", method = {
			RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String getQueryListByQueryId(
			@RequestParam(name = "id") String id, Principal principal) {
	
		String json = "";
		List<PortalFeedbackQuery> queryList = new ArrayList<PortalFeedbackQuery>();
		if (id != null) {
			Status status = new Status();
			try {
				MultipleDBConnection multipleDBConnection = new MultipleDBConnection();

				
				DriverManagerDataSource dataSourceLms = multipleDBConnection
						.createConnectionByDS(
								(String) session.getAttribute("url"),
								(String) session.getAttribute("dbUsername"),
								(String) session.getAttribute("password"),
								(String) session.getAttribute("dbName"));

				lmsportalFeedbackDAO.setDS(dataSourceLms);
				lmsPortalFeedbackQueryDAO.setDS(dataSourceLms);
				lmsPortalFeedbackResponseDAO.setDS(dataSourceLms);

				LmsDb lmsDb = new LmsDb();
				lmsDb.setLmsDb((String) session.getAttribute("dbName"));
				lmsportalFeedbackDAO.getLmsDb(lmsDb);
				lmsPortalFeedbackQueryDAO.getLmsDb(lmsDb);
				lmsPortalFeedbackResponseDAO.getLmsDb(lmsDb);

				status.setStatus(StatusType.SUCCESS);
				List<PortalFeedback> pfList = lmsportalFeedbackDAO.findAll();
				PortalFeedback pf = lmsportalFeedbackDAO.findOne(Long
						.valueOf(id));
				

				/*
				 * List<PortalFeedbackQuestion> portalFeedbackQuestionList =
				 * portalFeedbackQuestionService
				 * .findAllQuestionsWithResponses(username);
				 */
				if (pf != null) {

					List<PortalFeedbackQuery> portalQueryList = lmsPortalFeedbackQueryDAO
							.findQueryByPortalFeedbackId(pf.getId());
					

					if (portalQueryList.size() == 0) {

						List<PortalFeedbackResponse> pfrList = lmsPortalFeedbackResponseDAO
								.findByPortalFeedbackId(pf.getId());
						if (pfrList.size() != 0) {
							
							for (PortalFeedbackResponse pfr : pfrList) {
								
								if (!pfr.getAnswer().equalsIgnoreCase("")) {
									
									PortalFeedbackQuery pfq = new PortalFeedbackQuery();
									pfq.setPortalFeedbackId(pf.getId());
									pfq.setAnswer(pfr.getAnswer());
									pfq.setActive("Y");
									pfq.setCreatedDate(pf.getCreatedDate());
									pfq.setCreatedBy(pf.getUsername());
									pfq.setLastModifiedBy(pf.getUsername());
									lmsPortalFeedbackQueryDAO
											.insertWithIdReturn(pfq);
								} else {

								}
							}

						}

					} else {
						for (PortalFeedbackQuery pfb : portalQueryList) {

							PortalFeedbackQuery portal = lmsPortalFeedbackQueryDAO
									.findQuery(pfb.getId());
							// queryList.add(portal);
							if (portal != null) {
								queryList = findAllQueries(portal,
										new ArrayList<PortalFeedbackQuery>());
							} else {
								// queryList.addAll(portalQueryList);
							}

							
							/*
							 * for (int i = 0; i < list.size(); i++) {
							 * 
							 * PortalFeedbackQuery c = list.get(i);
							 * 
							 * ArrayList<PortalFeedbackQuery> children =
							 * getChildren( c.getId(), list); for
							 * (PortalFeedbackQuery child : children) {
							 * list.remove(child); list.add(i + 1, child); }
							 * c.setChildrenList(children); }
							 */

							// queryList.add(q);
						}
						queryList.addAll(portalQueryList);
					}
					// }

					
					if (queryList.size() != 0) {
						for (PortalFeedbackQuery pfq : queryList) {
							

							String answer = pfq.getAnswer();
							Document doc = Jsoup.parse(answer);
							String ans = doc.text();
							ans.trim();
							pfq.setAnswer(ans);
							if (pfq.getCreatedBy() != null) {
								User user = userService.findByUserName(pfq
										.getCreatedBy());
								if (user != null) {
									pfq.setFirstname(user.getFirstname());
									pfq.setLastname(user.getLastname());
								}
							}
							Date date = new Date();
							SimpleDateFormat formatter = new SimpleDateFormat(
									"dd/MM/yyyy");
							String strDate = formatter.format(pf
									.getCreatedDate());
							pfq.setCreatedOn(strDate);

							queryList.set(queryList.indexOf(pfq), pfq);

						}
					}
					Collections.sort(queryList, new SortByCreatedDate());

				}

			} catch (Exception ex) {
				status.setStatus(StatusType.ERROR);
				logger.error("Exception", ex);
			}
		}
		List<Map<String, String>> res = new ArrayList<Map<String, String>>();

		for (PortalFeedbackQuery assg : queryList) {
			Map<String, String> returnMap = new HashMap();
			returnMap.put(String.valueOf(assg.getId()), assg.getAnswer());
			res.add(returnMap);
		}
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			json = mapper.writeValueAsString(res);
		} catch (JsonProcessingException e) {
			logger.error("Exception" + e);
		}
		return json;

	}

	@RequestMapping(value = "/downloadPortalFeedbackReport", method = {
			RequestMethod.GET, RequestMethod.POST })
	public ModelAndView downloadFile(@RequestParam String schoolObjId,
			HttpServletRequest request, HttpServletResponse response) {

		OutputStream outStream = null;
		FileInputStream inputStream = null;
		List<String> schoolList = new ArrayList<String>();
		// MultipleDBReference multipleDBReference = new MultipleDBReference();
		String filePath = "";
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		try {
			XSSFWorkbook workbook = new XSSFWorkbook();

			// if (StringUtils.isEmpty(schoolObjId)) {
			// if (role.contains("ROLE_SUPPORT_ADMIN")) {

			WebTarget webTarget = null;
			Invocation.Builder invocationBuilder;
			String resp = null;
			ObjectMapper mapper = new ObjectMapper();
			if (schoolObjId != null) {
				schoolList = Arrays.asList(schoolObjId.split(","));
				
				String json = mapper.writeValueAsString(schoolList);
				webTarget = client.target(URIUtil
						.encodeQuery(userRoleMgmtCrudUrl
								+ "/getdbNameFromUsermgmt?schoolList=" + json));
			}

			invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
			resp = invocationBuilder.get(String.class);
			// logger.info("resp from uname" + resp);
			if (resp == null) {
				// setError(m, "Error in getting school list");
			} else {
				List<MultipleDBReference> dbList = new ArrayList<MultipleDBReference>();
				dbList = mapper.readValue(resp,
						new TypeReference<List<MultipleDBReference>>() {
						});
				
				String dbListName = "";
				for (MultipleDBReference db : dbList) {
					if (dbListName.length() == 0) {
						dbListName = dbListName.concat(db.getDbName());
					} else {
						dbListName = dbListName.concat("," + db.getDbName());
					}

					MultipleDBConnection multipleDBConnection = new MultipleDBConnection();

					
					DriverManagerDataSource dataSourceLms = multipleDBConnection
							.createConnectionByDS(db.getUrl(),
									db.getUsername(), db.getPassword(),
									db.getDbName());

					lmsportalFeedbackDAO.setDS(dataSourceLms);
					lmsPortalFeedbackQueryDAO.setDS(dataSourceLms);
					LmsDb lmsDb = new LmsDb();
					lmsDb.setLmsDb(db.getDbName());
					lmsportalFeedbackDAO.getLmsDb(lmsDb);
					lmsPortalFeedbackQueryDAO.getLmsDb(lmsDb);
					List<PortalFeedback> feedbackList = lmsportalFeedbackDAO
							.findAllFeedbacks();

					XSSFSheet sheet = workbook.createSheet("Comment Report");

					XSSFRow row = sheet.createRow(0);
					XSSFCell cell;
					cell = row.createCell(0);
					cell.setCellValue("Username");
					cell = row.createCell(1);
					cell.setCellValue("FirstName");
					cell = row.createCell(2);
					cell.setCellValue("LastName");
					cell = row.createCell(3);
					cell.setCellValue("Email");
					cell = row.createCell(4);
					cell.setCellValue("Mobile");
					cell = row.createCell(5);
					cell.setCellValue("Comment");
					cell = row.createCell(6);
					cell.setCellValue("Created Date");
					int i = 1;

					for (PortalFeedback pf : feedbackList) {
						XSSFRow r = sheet.createRow(i);
						cell = r.createCell(0);
						cell.setCellValue(pf.getUsername());
						cell = r.createCell(1);
						cell.setCellValue(pf.getFirstname());
						cell = r.createCell(2);
						cell.setCellValue(pf.getLastname());
						cell = r.createCell(3);
						cell.setCellValue(pf.getEmail());
						cell = r.createCell(4);
						cell.setCellValue(pf.getMobile());
						cell = r.createCell(5);
						cell.setCellValue(Jsoup.parse(pf.getAnswer()).text());
						cell = r.createCell(6);
						cell.setCellValue(formatter.format(pf.getCreatedDate()));
						i++;

					}
				}

			}
			// }
			// }
			

			File folderPath = new File(downloadAllFolder);
			if (!folderPath.exists()) {
				folderPath.mkdirs();
			}

			String fileName = schoolObjId + "_Portal_Feedback_Report.xlsx";

			filePath = downloadAllFolder + "/" + fileName;

			FileOutputStream out = new FileOutputStream(new File(filePath));
			workbook.write(out);
			out.close();
			
			// filePath = ip.getAbsolutePath();
			if (StringUtils.isEmpty(filePath)) {
				request.setAttribute("error", "true");
				request.setAttribute("errorMessage",
						"Error in downloading file.");
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
			String headerValue = String.format("attachment; filename=\"%s\"",
					downloadFile.getName());
			response.setHeader(headerKey, headerValue);

			// get output stream of the response
			outStream = response.getOutputStream();

			IOUtils.copy(inputStream, outStream);
		} catch (Exception e) {
			logger.error("Error", e);
		}
		return null;
	}

}
