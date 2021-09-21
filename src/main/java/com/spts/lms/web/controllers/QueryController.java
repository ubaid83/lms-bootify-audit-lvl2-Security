package com.spts.lms.web.controllers;

import java.security.Principal;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.spts.lms.auth.Token;
import com.spts.lms.beans.query.Query;
import com.spts.lms.beans.query.QueryResponse;
import com.spts.lms.beans.user.Role;
import com.spts.lms.beans.user.User;
import com.spts.lms.daos.query.QueryDAO;
import com.spts.lms.daos.query.QueryResponseDAO;
import com.spts.lms.services.course.CourseService;
import com.spts.lms.services.course.UserCourseService;
import com.spts.lms.services.query.QueryResponseService;
import com.spts.lms.services.query.QueryService;
import com.spts.lms.services.user.UserService;
import com.spts.lms.web.helper.WebPage;

@Controller
public class QueryController extends BaseController {
	private static final Logger logger = Logger
			.getLogger(QueryController.class);
	@Autowired
	QueryService queryService;

	@Autowired
	QueryResponseService queryResponseService;

	@Autowired
	UserCourseService userCourseService;

	@Autowired
	CourseService courseService;

	@Autowired
	QueryResponseDAO queryResponseDAO;

	@Autowired
	QueryDAO queryDAO;

	@Autowired
	QueryResponse queryResponse;

	@Autowired
	UserService userService;

	@RequestMapping(value = "/createQueryForm", method = { RequestMethod.GET })
	public String createMessageForm(@RequestParam(required = false) Long id,
			Model m, @ModelAttribute Query queryBean, Principal principal,
			HttpServletRequest request) {
		m.addAttribute("webPage", new WebPage("query", "Create Query", true,
				false));
		m.addAttribute("id", id);
		
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);
		

		String acadSession = u.getAcadSession();
		
		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		m.addAttribute("queryBean", queryBean);
		return "query/createQuery";
	}

	@RequestMapping(value = "/createQuery", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String createQuery(@ModelAttribute Query queryBean, Long id,
			@RequestParam(required = false) String queryDesc,
			RedirectAttributes redirectAttrs, Model m, Principal principal) {
		m.addAttribute("webPage", new WebPage("query", "Query Details", true,
				false));
		String username = principal.getName();
		UsernamePasswordAuthenticationToken userDeatils = (UsernamePasswordAuthenticationToken) principal;
		try {

			queryBean.setUsername(username);

			Token userdetails1 = (Token) principal;
			String ProgramName = userdetails1.getProgramName();
			User u = userService.findByUserName(username);
			

			String acadSession = u.getAcadSession();
			
			m.addAttribute("Program_Name", ProgramName);
			m.addAttribute("AcadSession", acadSession);

			queryBean.setQueryDesc(queryDesc);
			
			queryBean.setQueryCreatedTime(new Date());
			queryService.insertWithIdReturn(queryBean);

			setSuccess(m, "Query created successfully");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(m, "Error in creating message");
			m.addAttribute("webPage", new WebPage("query", "Create Query",
					false, false));
			return "query/createQuery";
		}
		m.addAttribute("queryBean", queryBean);
		return "query/createQuery";
	}

	@RequestMapping(value = "/viewQuery", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String viewQuery(@RequestParam(required = false) Long id,
			Principal principal, Model m) {
		m.addAttribute("webPage", new WebPage("query", "View Query", true,
				false));
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);
		

		String acadSession = u.getAcadSession();
		
		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		UsernamePasswordAuthenticationToken userDetails = (UsernamePasswordAuthenticationToken) principal;
		m.addAttribute("query", new Query());
		Query query = new Query();

		query = queryService.findByID(id);
		
		List<Query> listOfQueries = queryService.findAllQueries();
		for (Query q : listOfQueries) {

			Document doc = Jsoup.parse(q.getQueryDesc());
			String cQuestion = doc.text();
			q.setQueryDesc(cQuestion);
			listOfQueries.set(listOfQueries.indexOf(q), q);

		}
		m.addAttribute("coursesForQuery", courseService.findAllActive());
		
		m.addAttribute("listOfQueries", listOfQueries);

		m.addAttribute("query", query);
		if (userdetails1.getAuthorities().contains(Role.ROLE_ADMIN)) {
			return "query/queryAdmin";
		} else {
		return "query/query";
		}
	}

	@RequestMapping(value = "/giveResponseToQuery", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String giveResponseToQuery(Model m, Principal principal,
			@RequestParam("id") Long queryId, @ModelAttribute Query query) {
		m.addAttribute("webPage", new WebPage("query", "View Query", true,
				false));
		Token userdetails1 = (Token) principal;
		try {
			String username = principal.getName();

			
			String ProgramName = userdetails1.getProgramName();
			User u = userService.findByUserName(username);
			

			String acadSession = u.getAcadSession();
			
			m.addAttribute("Program_Name", ProgramName);
			m.addAttribute("AcadSession", acadSession);
			query = queryService.findByID(queryId);
			if (queryResponseService.findByQueryId(queryId) != null) {
				QueryResponse qr = queryResponseService.findByQueryId(queryId);
				
				if (qr.getQueryResponse() != null) {
					query.setQueryResponse(qr.getQueryResponse());
					Document doc = Jsoup.parse(query.getQueryDesc());
					String cQuestion = doc.text();
					query.setQueryDesc(cQuestion);
				} else {

				}
			} else {
				Document doc = Jsoup.parse(query.getQueryDesc());
				String cQuestion = doc.text();
				query.setQueryDesc(cQuestion);

			}
			m.addAttribute("query", query);
			m.addAttribute("queryId", query.getId());

		} catch (Exception e) {
			logger.error("Error", e);

		}
		 if (userdetails1.getAuthorities().contains(Role.ROLE_ADMIN)) {
			 return "query/responseToQueryAdmin";
		 } else {
		return "query/responseToQuery";
		 }

	}
	

	@RequestMapping(value = "/saveQueryReply", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String saveQueryReply(Model m,
			@ModelAttribute QueryResponse queryResponse, Principal principal,
			@RequestParam Long id) {
		m.addAttribute("webPage", new WebPage("query", "View Query", true,
				false));
		String username = principal.getName();
		try {
			queryResponse.setQueryId(id);
			queryResponse.setQueryRespondedBy(username);
			queryResponse.setQueryRespondedTime(new Date());
			queryResponseDAO.insert(queryResponse);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return viewQuery(id, principal, m);

	}

	@RequestMapping(value = "/viewQueryResponse", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String viewQueryResponse(Model m, Principal principal) {
		m.addAttribute("webPage", new WebPage("query",
				"View Query Response List", true, false));
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);
		

		String acadSession = u.getAcadSession();
		
		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		
		m.addAttribute("queryResponse", new QueryResponse());
		List<QueryResponse> listOfMyQueries = queryResponseService
				.findMyQuery(username);
		m.addAttribute("listOfMyQueries", listOfMyQueries);
		return "query/viewQueryResponse";
	}

}
