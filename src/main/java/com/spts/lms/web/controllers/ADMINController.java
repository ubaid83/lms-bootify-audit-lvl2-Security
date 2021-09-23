package com.spts.lms.web.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.ValidationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.xml.ws.Holder;

import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.util.URIUtil;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.util.CellRangeAddressList;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.omg.PortableServer.REQUEST_PROCESSING_POLICY_ID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spts.lms.beans.BaseBean;
import com.spts.lms.beans.announcement.Announcement;
import com.spts.lms.beans.announcement.Announcement.AnnouncementType;
import com.spts.lms.beans.programCampus.ProgramCampus;
import com.spts.lms.beans.user.UploadTimeLimitSession;
import com.spts.lms.daos.MultipleDBReferenceDAO;
import com.spts.lms.daos.announcement.AnnouncementDAO;
import com.spts.lms.daos.user.UploadTimeLimitSessionDao;
import com.spts.lms.helpers.excel.UploadTimeLimitHelper;
import com.spts.lms.services.announcement.AnnouncementService;
import com.spts.lms.services.programCampus.ProgramCampusService;
import com.spts.lms.services.user.UploadTimeLimitService;
import com.spts.lms.services.user.UserService;
import com.spts.lms.web.helper.WebPage;

@Controller
public class ADMINController extends BaseController{

	@Value("${userMgmtCrudUrl}")
	private String userRoleMgmtCrudUrl;

	Client client = ClientBuilder.newClient();

	@Autowired
	ProgramCampusService programCampusService;
	
	@Autowired
	MultipleDBReferenceDAO multipleDBReferenceDAO;
	
	@Autowired
	AnnouncementService announcementService;
	
	
	@Autowired
    ApplicationContext act;

	
	@Autowired
    UserService userService;
	

	@ModelAttribute("campusList")
	public List<ProgramCampus> getCampusList() {

		return programCampusService.getCampusList();
	}
    
	
	
    @Value("${lms.assignment.downloadAllFolder}")
    private String downloadAllFolder;
    
    private UploadTimeLimitHelper UploadTimeLimitSessionHelper() {
          return (UploadTimeLimitHelper) act.getBean("UploadTimeLimitHelper");
    }


	@Autowired
	UploadTimeLimitSessionDao uploadTimeLimitSessionDao;
	
	@Autowired UploadTimeLimitService uploadTimeLimitService;

	@Autowired

	AnnouncementDAO announcementDAO;

	private static final Logger logger = Logger.getLogger(ADMINController.class);

	@Secured({ "ROLE_SUPPORT_ADMIN" })
	@RequestMapping(value = "/addNewFeatures", method = { RequestMethod.GET, RequestMethod.POST })
	public String addNewFeatures(Principal principal, Model m, @ModelAttribute Announcement announcement) {

		return "user/addNewFeatures";

	}
	
	@Secured({ "ROLE_SUPPORT_ADMIN" })
	@RequestMapping(value = "/addNewFeaturesAdvance", method = { RequestMethod.GET, RequestMethod.POST })
	public String addNewFeaturesAdvance(Principal principal, Model m, @ModelAttribute Announcement announcement)

	{
		
		try {
			
		m.addAttribute("webPage", new WebPage("assignment", "Create Assignment", true, false));
		String subject = announcement.getSubject();
		String startDate = announcement.getStartDate();
		String endDate = announcement.getEndDate();
		
		String accessType=  announcement.getAccessType();
		
		announcement.setAccessType(accessType);

		announcement.setAnnouncementType(AnnouncementType.NOTIFICATION);
		announcement.setCreatedBy(principal.getName());

		announcement.setLastModifiedBy(principal.getName());

		
		String description = announcement.getDescription();

		
		ObjectMapper mapper = new ObjectMapper();

		String json;
		
			json = mapper.writeValueAsString(announcement);
		
		/* logger.info("encoded json--->" + URIUtil.encodeQuery(json));
		logger.info("userRoleMgmtCrudUrl" + userRoleMgmtCrudUrl);
		logger.info(" json--->" + (json)); */
		WebTarget webTarget = client

				.target(URIUtil.encodeQuery(userRoleMgmtCrudUrl + "/getSchoolsListWithDetailsForAnnouncement?json=" + json));

		/* logger.info("webTarget" + webTarget); */
		Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);

		/* logger.info("invocationBuilder" + invocationBuilder); */
		String resp = invocationBuilder.get(String.class);
		

	/* 	logger.info("Menu added successfully"); */
		

	//	announcementDAO.insert(announcement);
		
		
		} catch (JsonProcessingException | URIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "user/addNewFeaturesNew";

	}

	/*@RequestMapping(value = "/customizeMenuUser", method = { RequestMethod.GET, RequestMethod.POST })
	public String customizeMenuForm(Principal principal, Model m)

	{
		m.addAttribute("webPage", new WebPage("customizeMenuForm", "Add new Admin", true, true, true, true, false));

		logger.info("Inside / customizeMenu");

		List<ProgramCampus> listSchool = programCampusService.getSchoolName();

		logger.info("listSchool------------>" + listSchool);

		List<MasterMenu> listMaster = masterMenuDAO.getallMenus();

		logger.info("listMaster is" + listMaster);
		List<String> list2 = new ArrayList<String>();

		for (int i = 0; i < listMaster.size(); i++) {

			logger.info("Inside For loop");
			list2.add(listMaster.get(i).getMenu());

		}

		logger.info("listMaster is" + listMaster);
		logger.info("List of Menus is" + list2);

		
		 * List<ProgramCampus> listmenu=programCampusService.getAllMenus();
		 * 
		 * logger.info("menulist----------->"+listmenu);
		 

		m.addAttribute("listSchool", listSchool);

		m.addAttribute("list2", list2);
		m.addAttribute("programCampus", new ProgramCampus());

		return "user/customizeMenu";
	}

	@RequestMapping(value = "/customizeMenuAdvance", method = { RequestMethod.GET, RequestMethod.POST })
	public String customizeMenuAdvance(Principal principal, Model m, @RequestParam(required = false) String campusName,
			@ModelAttribute ProgramCampus programCampus, HttpServletRequest request, RedirectAttributes redirectAttrs) {

		logger.info("campusName--------------->" + campusName);
		logger.info("programCampus ------- " + programCampus);

		String menuRight = programCampus.getMenuRight();

		logger.info("Selected menuright is :" + menuRight);

		String campusname = programCampus.getCampusName();

		logger.info("campusname" + campusname);

		try {

			School_Menu s = new School_Menu();
			s.setSchoolName(campusname);
			s.setMenu(menuRight);
			s.setIsActive("Y");

			logger.info(s.toString());

			// schoolMenuDAO.insert(s);
			logger.info("inserted successfully");

			ObjectMapper mapper = new ObjectMapper();

			String json = mapper.writeValueAsString(s);
			logger.info("encoded json--->" + URIUtil.encodeQuery(json));
			logger.info("userRoleMgmtCrudUrl" + userRoleMgmtCrudUrl);
			logger.info(" json--->" + (json));
			WebTarget webTarget = client

					.target(URIUtil.encodeQuery(userRoleMgmtCrudUrl + "/addMenuSchool?json=" + json));

			logger.info("webTarget" + webTarget);
			Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);

			logger.info("invocationBuilder" + invocationBuilder);
			String resp = invocationBuilder.get(String.class);
			logger.info("resp" + resp);

			logger.info("Menu added successfully");

		}

		catch (Exception e) {

			System.out.println(e);
			
			 * logger.error(e.getMessage(), e);
			 * logger.info("Error in adding menu "); return
			 * "redirect:/customizeMenuUser";
			 
		}

		// loggers giving NULL //
		
		 * logger.info("custom from jsp" + customMenuRight);
		 * 
		 * request.getAttribute("campusName");
		 * request.getAttribute("customMenuuRight");
		 * 
		 * logger.info("campusName" + campusName);
		 * 
		 * logger.info("customMenuuRight" + customMenuuRight);
		 * 
		 * m.addAttribute("webPage", new WebPage("customizeMenuForm",
		 * "Add new Admin", true, true, true, true, false));
		 * 
		 * logger.info("customizeMenuAdvance landed here........");
		 
		logger.info("Inside / customizeMenu");

		// loggers giving NULL //

		return "user/customizeMenuAdvance";
	}*/
	
	
	@Secured({ "ROLE_SUPPORT_ADMIN" })
	@RequestMapping(value = "/uploadTimeLimitSession", method = {
            RequestMethod.GET, RequestMethod.POST })
public String downloadExamReport(Principal principal, Model m) {
      m.addAttribute("webPage", new WebPage("uploadTimeLimitSession",
                  "Add new Librarian", true, true, true, true, false));

      return "homepage/uploadTimeLimitSession";
    
      //return "library/libraryPage";
}
	@Secured({ "ROLE_SUPPORT_ADMIN" })
    @RequestMapping(value = "/downloadUploadTemp", method = RequestMethod.GET)
    public void downloadUploadTemp(HttpServletResponse response,
                Principal principal) {

          String username = principal.getName();
          InputStream is = null;
          String filePath = "";

          try {


        	  	List<String> sList = userService.getSessionMasterListTest();
        	  	 
              logger.info("-----------List AA++"+sList);
              
              filePath = getNSTemplate(sList);

               
                is = new FileInputStream(filePath);
                response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
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
    
    public String getNSTemplate(List<String> sList) {
        String filePath = downloadAllFolder + File.separator
                    + "NSTemplate.xlsx";
        String h[] = { "Session", "Start Date", "End Date"};
        List<String> header = Arrays.asList(h);
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Student Report Sheet");
        Row headerRow = sheet.createRow(0);
        for (int colNum = 0; colNum < header.size(); colNum++) {
              Cell cell = headerRow.createCell(colNum);
              cell.setCellValue(header.get(colNum));
        }
        
int rowNum=1;
    
       for (String s : sList) {
        	 Row row = sheet.createRow(rowNum);
              row.createCell(0).setCellValue(s);
              row.createCell(1).setCellValue("");
              row.createCell(2).setCellValue("");
             // row.createCell(3).setCellValue("");
              rowNum++;
        }
        
        XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheet);
       
        String[] str1 = sList.toArray(new String[sList.size()]);
        int i=0;
        for(String str : sList){
        	
        	str1[i]=str;
        	logger.info("str1"+str1[i]);
        	i++;
        }
        
        DataValidation dataValidation = null;
		DataValidationConstraint constraint = null;
		DataValidationHelper validationHelper = null;
		validationHelper = new XSSFDataValidationHelper(sheet);

		CellRangeAddressList addressList = new CellRangeAddressList(1,
				sList.size(), 0, 0);

		constraint = validationHelper
				.createExplicitListConstraint(str1);
		dataValidation = validationHelper.createValidation(constraint,
				addressList);
		dataValidation.setSuppressDropDownArrow(true);
		sheet.addValidationData(dataValidation);
		
		
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
    
    
    @Secured({ "ROLE_SUPPORT_ADMIN" })
	@RequestMapping(value = "/uploadTimeLimit", method = RequestMethod.POST)
	public String uploadStudent(@ModelAttribute UploadTimeLimitSession timeSession, Principal principal,
			@RequestParam("file") MultipartFile  file,
			RedirectAttributes redirectAttrs) {
		UploadTimeLimitHelper uploadTimeLimite = UploadTimeLimitSessionHelper();
		
		String username = principal.getName();
		
		List<UploadTimeLimitSession> uploadDetailsList = new ArrayList<>();
	

		
		try {
			if (!file.isEmpty()) {
		
				
				uploadTimeLimite.initHelper(timeSession);
				uploadTimeLimite.readExcel(file);
				
				List<UploadTimeLimitSession> userList = uploadTimeLimite.getSuccessList();
				
				logger.info("uploadTimeLimite -->"+uploadTimeLimite);
				
				logger.info("userList0000000000---------->"+userList);
				
				
				ObjectMapper mapper = new ObjectMapper();

		for(UploadTimeLimitSession m : userList)
			
		{
			UploadTimeLimitSession bean = new UploadTimeLimitSession();
			bean.setSession(m.getSession());
			bean.setStartDate(m.getStartDate());
			bean.setEndDate(m.getEndDate());
			bean.setLastModifiedBy(principal.getName());
			bean.setCreatedBy(principal.getName());
			bean.setActive("Y");
			uploadDetailsList.add(bean);
		
		}
	
		logger.info("UploadDetails size--->"+ uploadDetailsList.size());
	if (uploadDetailsList.size() > 0) {

		uploadTimeLimitService.insertTimeLimit(uploadDetailsList);
	
		}

	}

		} catch (Exception e) {
			e.printStackTrace();
			//model.addAttribute("error",error);
			setError(redirectAttrs,
					"Error in uploading File: " + e.getMessage());
			//return "course/uploadTimeLimitSession";
			return "redirect:/uploadTimeLimitSession";
		}
		//model.addAttribute("seccess",Success);
		setSuccess(redirectAttrs, "File uploaded successfully");
		
		return "redirect:/uploadTimeLimitSession";
		//return "homepage/uploadTimeLimitSession";
	

	}
    
    @Secured({ "ROLE_LIBRARIAN", "ROLE_EXAM" })
	@RequestMapping(value = "/downloadReportLinkExam", method = {
            RequestMethod.GET, RequestMethod.POST })
public String downloadReportLinkExam(Principal principal, Model m) {
      m.addAttribute("webPage", new WebPage("downloadReportLinkExam",
                  "Add new Librarian", true, true, true, true, false));

      return "homepage/downloadReportLinkExam";
    
      //return "library/libraryPage";
}
	

}
