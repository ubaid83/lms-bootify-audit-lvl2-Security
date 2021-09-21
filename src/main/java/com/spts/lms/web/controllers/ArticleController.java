package com.spts.lms.web.controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.util.URIUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spts.lms.auth.Token;
import com.spts.lms.beans.LmsDb;
import com.spts.lms.beans.MultipleDBReference;
import com.spts.lms.beans.Status;
import com.spts.lms.beans.StatusType;
import com.spts.lms.beans.user.Role;
import com.spts.lms.beans.user.User;
import com.spts.lms.beans.webpages.Webpages;
import com.spts.lms.daos.MultipleDBReferenceDAO;
import com.spts.lms.daos.webpages.LmsWebpagesDAO;
import com.spts.lms.daos.webpages.WebpagesDAO;
import com.spts.lms.services.program.ProgramService;
import com.spts.lms.services.user.UserService;
import com.spts.lms.services.webpages.WebpagesService;
import com.spts.lms.utils.MultipleDBConnection;
import com.spts.lms.web.helper.WebPage;

@Controller
public class ArticleController extends BaseController {

	@Autowired
	ApplicationContext act;

	@Autowired
	UserService userService;

	@Autowired
	WebpagesService webpagesService;

	@Autowired
	MultipleDBReferenceDAO multipleDBReferenceDAO;

	@Autowired
	ProgramService programService;

	// @Autowired
	// WebpagesDAO webpagesDAO;
	@Autowired
	LmsWebpagesDAO lmsWebpagesDAO;

	@Value("${lms.webpagesFolder}")
	private String webpagesFolder;

	@Value("${userMgmtCrudUrl}")
	private String userRoleMgmtCrudUrl;

	private static final Logger logger = Logger
			.getLogger(ArticleController.class);
	Client client = ClientBuilder.newClient();

	@RequestMapping(value = "/createArticleForm", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String createArticleForm(Model m, Principal principal,
			@RequestParam(required = false) Long id) {
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);


		String acadSession = u.getAcadSession();
		
		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		m.addAttribute("webPage", new WebPage("assignment", "Create Tab", true,
				false));

		Webpages webpages = new Webpages();
		if (id != null) {
			webpages = webpagesService.findByID(id);
			m.addAttribute("edit", "true");

		}
		/*
		 * m.addAttribute("allSchools",
		 * webpagesService.findSchoolWithCollegeName());
		 */

		WebTarget webTarget;
		Invocation.Builder invocationBuilder;
		String resp = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			webTarget = client.target(URIUtil.encodeQuery(userRoleMgmtCrudUrl
					+ "/getSchoolWithCollegeNameFromUsermgmt"));
			invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
			resp = invocationBuilder.get(String.class);
			if (resp == null) {
				setError(m, "Error in getting school list");
			} else {

				List<Map<String, String>> listOfSchoolAndCampus = mapper
						.readValue(resp, List.class);
				
				m.addAttribute("listOfSchoolAndCampus", listOfSchoolAndCampus);

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

		m.addAttribute("webpages", webpages);
      if(userdetails1.getAuthorities().contains(Role.ROLE_COUNSELOR)) {
    	  return "webpages/createArticleCo";
      }
		return "webpages/createArticle";
	}

	@RequestMapping(value = "createArticle", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String createArticle(Model m,
			@RequestParam("file") MultipartFile file, Principal principal,
			@RequestParam(required = false) Long id,
			@ModelAttribute Webpages webpages,
			RedirectAttributes redirectAttributes) {
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);
		

		String acadSession = u.getAcadSession();
		
		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		m.addAttribute("webPage", new WebPage("assignment", "Create Tab", true,
				false));
		String type = "ARTICLE";
		
		List<String> schoolList = new ArrayList<String>();
		List<String> campusList = new ArrayList<String>();

		try {
			WebTarget webTarget = null;
			Invocation.Builder invocationBuilder;
			String resp = null;
			ObjectMapper mapper = new ObjectMapper();
			if (webpages.getSchoolObjId() != null) {
				schoolList = Arrays
						.asList(webpages.getSchoolObjId().split(","));
				
				String json = mapper.writeValueAsString(schoolList);
				webTarget = client.target(URIUtil
						.encodeQuery(userRoleMgmtCrudUrl
								+ "/getdbNameFromUsermgmt?schoolList=" + json));
			}
			if (webpages.getCampusName() != null) {
				campusList = Arrays.asList(webpages.getCampusName().split(","));
				
				String json = mapper.writeValueAsString(campusList);
				webTarget = client.target(URIUtil
						.encodeQuery(userRoleMgmtCrudUrl
								+ "/getdbNameFromUsermgmt?campusList=" + json));
			}

			invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
			resp = invocationBuilder.get(String.class);
			
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
						dbListName = dbListName.concat("," + db.getDbName());
					}

				}
				
				m.addAttribute("dbList", dbList);
				for (MultipleDBReference dbName : dbList) {
					if (!file.isEmpty()) {
						String errorMessage = uploadArticleFile(webpages, file);
					}
					if (webpages.getMakeAvailable() == null) {
						webpages.setMakeAvailable("N");
					}
					webpages.setType(type);
					webpages.setCreatedBy(username);
					webpages.setLastModifiedBy(username);
					webpages.setDbListName(dbListName);
					// webpagesService.insertWithIdReturn(webpages);
					Status status = insertArticle(dbName, webpages);

				}

				setSuccess(redirectAttributes, "Article created successfully!");

			}

		} catch (Exception e) {
			logger.error("Exception", e);
			setError(redirectAttributes, "Error while creating article");
		}

		m.addAttribute("webpages", webpages);

		return "redirect:/viewArticles";
	}

	public Status insertArticle(MultipleDBReference multipleDBReference,
			Webpages article) {

		Status status = new Status();
		try {
			MultipleDBConnection multipleDBConnection = new MultipleDBConnection();

			
			DriverManagerDataSource dataSourceLms = multipleDBConnection
					.createConnectionByDS(multipleDBReference.getUrl(),
							multipleDBReference.getUsername(),
							multipleDBReference.getPassword(),
							multipleDBReference.getDbName());

			lmsWebpagesDAO.setDS(dataSourceLms);
			LmsDb lmsDb = new LmsDb();
			lmsDb.setLmsDb(multipleDBReference.getDbName());
			lmsWebpagesDAO.getLmsDb(lmsDb);

			if (article != null) {
				lmsWebpagesDAO.insert(article);
			}
			status.setStatus(StatusType.SUCCESS);
		} catch (Exception ex) {
			status.setStatus(StatusType.ERROR);
			logger.error("Exception", ex);
		}

		return status;
	}

	public Status deleteArticle(MultipleDBReference multipleDBReference,
			Webpages article) {

		Status status = new Status();
		try {
			MultipleDBConnection multipleDBConnection = new MultipleDBConnection();

			
			DriverManagerDataSource dataSourceLms = multipleDBConnection
					.createConnectionByDS(multipleDBReference.getUrl(),
							multipleDBReference.getUsername(),
							multipleDBReference.getPassword(),
							multipleDBReference.getDbName());

			lmsWebpagesDAO.setDS(dataSourceLms);
			LmsDb lmsDb = new LmsDb();
			lmsDb.setLmsDb(multipleDBReference.getDbName());
			lmsWebpagesDAO.getLmsDb(lmsDb);

			if (article != null) {
				lmsWebpagesDAO.softDeleteArticle(article.getName());
			}
			status.setStatus(StatusType.SUCCESS);
		} catch (Exception ex) {
			status.setStatus(StatusType.ERROR);
			logger.error("Exception", ex);
		}

		return status;
	}

	@RequestMapping(value = "deleteArticle", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String deleteArticle(Model m, Principal principal,
			@RequestParam(required = false) Long id,
			RedirectAttributes redirectAttributes) {
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);
		

		String acadSession = u.getAcadSession();
		
		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		m.addAttribute("webPage", new WebPage("assignment", "Create Tab", true,
				false));
		String type = "ARTICLE";
		Webpages webpages = webpagesService.findByID(id);
		

		try {
			WebTarget webTarget = null;
			Invocation.Builder invocationBuilder;
			String resp = null;
			ObjectMapper mapper = new ObjectMapper();
			List<String> dbListName = Arrays.asList(webpages.getDbListName()
					.split(","));
			
			
			if (dbListName.size() != 0) {
				String json = mapper.writeValueAsString(dbListName);
				webTarget = client.target(URIUtil
						.encodeQuery(userRoleMgmtCrudUrl
								+ "/getdbNameFromUsermgmt?dbListName=" + json));
			}
			invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
			resp = invocationBuilder.get(String.class);
			if (resp == null) {
				setError(m, "Error in getting school list");
			} else {
				List<MultipleDBReference> dbList = new ArrayList<MultipleDBReference>();
				dbList = mapper.readValue(resp,
						new TypeReference<List<MultipleDBReference>>() {
						});
				
				m.addAttribute("dbList", dbList);
				for (MultipleDBReference dbName : dbList) {

					webpages.setActive("N");
					webpages.setCreatedBy(username);
					webpages.setLastModifiedBy(username);
					// webpagesService.insertWithIdReturn(webpages);
					Status status = deleteArticle(dbName, webpages);

				}

				setSuccess(redirectAttributes, "Article deleted successfully!");
			}

		} catch (Exception e) {
			logger.error("Exception", e);
			setError(redirectAttributes, "Error while deleting article");
		}

		m.addAttribute("webpages", webpages);

		return "redirect:/viewArticles";
	}

	/*
	 * @RequestMapping(value = "updateArticle", method = { RequestMethod.GET,
	 * RequestMethod.POST }) public String updateArticle(Model m,
	 * 
	 * @RequestParam("file") MultipartFile file, Principal principal,
	 * 
	 * @ModelAttribute Webpages webpages, RedirectAttributes redirectAttributes)
	 * { String username = principal.getName();
	 * 
	 * Token userdetails1 = (Token) principal; String ProgramName =
	 * userdetails1.getProgramName(); User u =
	 * userService.findByUserName(username);
	 * logger.info("ACAD SESSION------------------------->" +
	 * u.getAcadSession());
	 * 
	 * String acadSession = u.getAcadSession();
	 * logger.info("Program NAme--------------------> " + ProgramName);
	 * m.addAttribute("Program_Name", ProgramName);
	 * m.addAttribute("AcadSession", acadSession); m.addAttribute("webPage", new
	 * WebPage("assignment", "Create Tab", true, false)); String type =
	 * "ARTICLE"; try { if (!file.isEmpty()) { String errorMessage =
	 * uploadArticleFile(webpages, file); } if (webpages.getMakeAvailable() ==
	 * null) { webpages.setMakeAvailable("N"); } webpages.setActive("Y");
	 * webpages.setType(type); webpages.setCreatedBy(username);
	 * webpages.setLastModifiedBy(username); webpagesService.update(webpages);
	 * setSuccess(redirectAttributes, "Article updated successfully!"); } catch
	 * (Exception e) { logger.error("Exception", e);
	 * setError(redirectAttributes, "Error while updating article"); }
	 * 
	 * m.addAttribute("webpages", webpages);
	 * 
	 * return "redirect:/viewArticles"; }
	 */

	private String uploadArticleFile(Webpages bean, MultipartFile file) {

		String errorMessage = null;
		InputStream inputStream = null;
		OutputStream outputStream = null;

		// CommonsMultipartFile file = bean.getFileData();
		String fileName = file.getOriginalFilename();

		// Replace special characters in file
		fileName = fileName.replaceAll("'", "_");
		fileName = fileName.replaceAll(",", "_");
		fileName = fileName.replaceAll("&", "and");
		fileName = fileName.replaceAll(" ", "_");

		fileName = fileName.substring(0, fileName.lastIndexOf("."))
				+ "_"
				+ RandomStringUtils.randomAlphanumeric(10)
				+ fileName.substring(fileName.lastIndexOf("."),
						fileName.length());

		

		try {
			inputStream = file.getInputStream();
			String filePath = webpagesFolder + File.separator + fileName;
			bean.setFilePath(filePath);
			
			File folderPath = new File(webpagesFolder);
			if (!folderPath.exists()) {
				folderPath.mkdirs();
			}
			File newFile = new File(filePath);
			outputStream = new FileOutputStream(newFile);
			IOUtils.copy(inputStream, outputStream);

		} catch (IOException e) {
			errorMessage = "Error in uploading file : " + e.getMessage();
			logger.error("Exception" + errorMessage, e);
		} finally {

			if (inputStream != null)
				IOUtils.closeQuietly(inputStream);

			if (outputStream != null)
				IOUtils.closeQuietly(outputStream);

		}

		return errorMessage;
	}

	@RequestMapping(value = "/viewArticles", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String viewArticles(
			@RequestParam(required = false, defaultValue = "1") int pageNo,
			Model m, Principal p) {

		m.addAttribute("webPage",
				new WebPage("library", "Library", true, false));
		String username = p.getName();

		Token userdetails1 = (Token) p;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);
		

		String acadSession = u.getAcadSession();
		
		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		try {

			List<Webpages> tabsList = new ArrayList<Webpages>();

			if (userdetails1.getAuthorities().contains(Role.ROLE_COUNSELOR)) {
				tabsList = webpagesService.findAllArticles();
			} else {

				tabsList = webpagesService.findAvailArticles();
			}

			if (tabsList.isEmpty() || tabsList.size() != 0) {
				for (Webpages w : tabsList) {
					if (!w.getCreatedBy().isEmpty()) {
						User user = userService
								.findByUserName(w.getCreatedBy());
						w.setFirstname(user.getFirstname());
						w.setLastname(user.getLastname());
						tabsList.set(tabsList.indexOf(w), w);
					}
				}
			}

			m.addAttribute("tabsList", tabsList);
			m.addAttribute("username", username);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(m, "Error in getting content");
		}
		if(userdetails1.getAuthorities().contains(Role.ROLE_COUNSELOR)){
			return "webpages/viewArticlesCo";
		}
		return "webpages/viewArticles";
	}
}
