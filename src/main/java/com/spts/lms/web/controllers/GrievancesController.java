package com.spts.lms.web.controllers;

import java.io.File;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.gson.Gson;
import com.spts.lms.auth.Token;
import com.spts.lms.beans.amazon.AmazonS3ClientService;
import com.spts.lms.beans.grievances.Grievances;
import com.spts.lms.beans.grievances.GrievancesConfig;
import com.spts.lms.beans.overview.Overview;
import com.spts.lms.beans.user.Role;
import com.spts.lms.beans.user.User;
import com.spts.lms.beans.variables.LmsVariables;
import com.spts.lms.helpers.excel.ExcelReader;
import com.spts.lms.services.assignment.AssignmentService;
import com.spts.lms.services.grievances.GrievancesConfigService;
import com.spts.lms.services.grievances.GrievancesService;
import com.spts.lms.services.user.UserService;
import com.spts.lms.services.variables.LmsVariablesService;
import com.spts.lms.web.helper.WebPage;
import com.spts.lms.web.utils.Utils;

@Controller
@SessionAttributes("userId")
public class GrievancesController extends BaseController {
	@Value("#{'${overviewCities}'.split(',')}")
	private List<String> overviewCitiesList;

	@Value("${lms.srbFile}")
	private String srbFile;

	@Value("${workStoreDir:''}")
	private String workDir;

	@Autowired
	ApplicationContext act;

	@Autowired
	GrievancesService grievancesService;
	
	@Autowired
	AmazonS3ClientService s3Service;

	@Autowired
	AssignmentService assignmentService;

	@Autowired
	GrievancesConfigService grievancesConfigService;

	@Autowired
	UserService userService;
	
	@Autowired
	LmsVariablesService lmsVariablesService;

	protected static final int BUFFER_SIZE = 4096;

	private static final Logger logger = Logger
			.getLogger(GrievancesController.class);

	@Secured({ "ROLE_ADMIN","ROLE_FACULTY" })
	@RequestMapping(value = "/grievanceForm", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String grievanceForm(@ModelAttribute Grievances grievances,
			@RequestParam(required = false) Long id, Model m,
			Principal principal) {
		String username = principal.getName();
		m.addAttribute("webPage", new WebPage("grievances", " Grievance Form",
				true, false));
		Grievances grievance = new Grievances();

		if (id != null) {
			grievances = grievancesService.findByID(id);
			m.addAttribute("edit", "true");

		}
		m.addAttribute("grievances", grievance);
		m.addAttribute("allTypesOfGrievances",
				grievancesConfigService.findUniqueGrievances());
		return "grievances/grievanceForm";
	}

	@Secured({ "ROLE_ADMIN","ROLE_FACULTY" })
	@RequestMapping(value = "/saveGrievanceForm", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String saveGrievanceForm(@ModelAttribute Grievances grievances,
			Model m, Principal principal) {
		String username = principal.getName();
		m.addAttribute("webPage", new WebPage("grievances", " Grievance Form",
				true, false));

		grievances.setUsername(username);

		try {
			setSuccess(m, "Grievance Raised Successfully");
			grievancesService.insert(grievances);
			m.addAttribute("grievances", new Grievances());

		} catch (Exception e) {

			logger.error("ERROR", e);
			setError(m, "Error in submitting grievance");
			m.addAttribute("grievances", grievances);

		}
		m.addAttribute("allTypesOfGrievances",
				grievancesConfigService.findUniqueGrievances());
		return "grievances/grievanceForm";
	}

	/*
	 * @RequestMapping(value = "/grievance", method = { RequestMethod.GET,
	 * RequestMethod.POST }) public String grievance(Model m,Grievances
	 * grievances) { m.addAttribute("webPage", new WebPage("assignment",
	 * "View Grievance", true, false)); m.addAttribute("grievances",grievances
	 * ); m.addAttribute("allTypesOfGrievances",
	 * grievancesConfigService.findUniqueGrievances());
	 * 
	 * return "grievances/grievanceForm"; }
	 */

	@RequestMapping(value = "/getTypeBasedOnGroup", method = {
			RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String getTypeBasedOnGroup(
			@RequestParam("typeOfGrievance") String typeOfGrievance) {
		Gson gson = new Gson();
		String jsonOutput = "";
		List<String> grievanceConf = grievancesConfigService
				.findCaseBasedOnType(typeOfGrievance);
		logger.error("grievanceConf size" + grievanceConf.size());
		try {
			jsonOutput = gson.toJson(grievanceConf);
			return jsonOutput;
		} catch (Exception e) {
			logger.error("Exception", e);
			return jsonOutput;

		}

	}

	@Secured({ "ROLE_FACULTY" })
	@RequestMapping(value = "/viewAllGrievances", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String viewAllGrievances(Model m) {
		m.addAttribute("webPage", new WebPage("assignment", "View Grievance",
				true, false));
		m.addAttribute("grievances", new Grievances());
		List<Grievances> listofAllGrievances = grievancesService
				.getAllGrievances();
		m.addAttribute("listofAllGrievances", listofAllGrievances);
		return "grievances/grievanceList";
	}

	@Secured({ "ROLE_ADMIN","ROLE_SUPPORT_ADMIN" })
	@RequestMapping(value = "/giveResponseToGrievance", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String giveResponseToGrievance(Model m,
			@RequestParam("id") Long grievanceId) {
		m.addAttribute("webPage", new WebPage("assignment", "View Grievance",
				true, false));

		Grievances grievance = grievancesService.findByID(grievanceId);

		m.addAttribute("grievance", grievance);
		m.addAttribute("grievanceId", grievance.getId());
		return "grievances/responseToGrievance";

	}

	@Secured({ "ROLE_FACULTY" })
	@RequestMapping(value = "/saveGrievanceResponse", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String saveGrievanceResponse(Model m,
			@ModelAttribute Grievances grievance) {
		m.addAttribute("webPage", new WebPage("assignment", "View Grievance",
				true, false));
		try {
			Date dt = Utils.getInIST();
			grievance.setGrievanceResponseTimeStamp(dt);
			grievancesService.update(grievance);
		} catch (Exception e) {
			logger.error("Exception", e);
		}
		return viewAllGrievances(m);

	}

	@Secured({ "ROLE_USER" })
	@RequestMapping(value = "/viewGrievanceResponse", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String viewGrievanceResponse(Model m, Principal principal) {
		m.addAttribute("webPage", new WebPage("assignment",
				"View Grievance Response List", true, false));
		String username = principal.getName();
		
		m.addAttribute("grievances", new Grievances());
		List<Grievances> listofAllGrievances = grievancesService
				.findByUserGrievances(username);
		m.addAttribute("listofAllGrievances", listofAllGrievances);
		return "grievances/viewGrievanceResponse";
	}

	@Secured({"ROLE_STUDENT"})
	@RequestMapping(value = "/overview", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String overview(Model m, Principal principal, Overview overview,
			@RequestParam Long courseId) {
		String username = principal.getName();
		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u1 = userService.findByUserName(username);
		

		String acadSession = u1.getAcadSession();
		String getSrbPath = grievancesService.findSrbPathCheck();
		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		m.addAttribute("webPage", new WebPage("assignment", "Overview", true,
				false));
		List<String> deptList = grievancesService.getOverviewDeptList();
		m.addAttribute("deptList", deptList);
		m.addAttribute("courseId", courseId);
		m.addAttribute("overviewCitiesList", overviewCitiesList);
		m.addAttribute("srbFile", srbFile);
		
		
		if(null != getSrbPath && !getSrbPath.isEmpty())
		{
			m.addAttribute("downloadAvailable", "yes");
		}
		else 
		{
			logger.info("downloadAvailable---------> no filePath found in db");
			m.addAttribute("downloadAvailable", "no");
		}

		return "grievances/overview";

	}

/*	@RequestMapping(value = "/sendSrbFile", method = { RequestMethod.GET,
			RequestMethod.POST })
	public ModelAndView sendSrbFile(
			@RequestParam(name = "filePath") String filePath,
			HttpServletRequest request, HttpServletResponse response) {
		
		String projectUrl = "";
		try {
			File src = new File(filePath);
			File dest = new File(workDir + File.separator + src.getName());

			if (dest.exists())
				dest.delete();
			FileUtils.copyFile(src, dest);

			projectUrl = "/" + "workDir" + "/" + dest.getName();
			

		} catch (Exception e) {
			logger.error("Error", e);
		}

		return new ModelAndView("redirect:" + projectUrl);
	}*/
	
	
	/*
	 * @RequestMapping(value = "/sendSrbFile", method = { RequestMethod.GET,
	 * RequestMethod.POST }) public ModelAndView sendSrbFile(
	 * 
	 * @RequestParam(name = "id") String id, HttpServletRequest request,
	 * HttpServletResponse response,Model m,RedirectAttributes r) {
	 * 
	 * String projectUrl = ""; try {
	 * 
	 * if(id.equals("1")) { String getSrbPath= grievancesService.findSrbPath(id);
	 * logger.info("Srb Path---------->"+getSrbPath);
	 * m.addAttribute("getSrbPath",getSrbPath); File src = new File(getSrbPath);
	 * File dest = new File(workDir + File.separator + src.getName());
	 * logger.info("Dec--->"+dest); if (dest.exists()) dest.delete();
	 * FileUtils.copyFile(src, dest); projectUrl = "/" + "workDir" + "/" +
	 * dest.getName(); } else { r.addAttribute("courseId",""); setError(r,
	 * "File Not Found!!!"); return new ModelAndView("redirect:/overview"); }
	 * 
	 * } catch (Exception e) { logger.error("Error", e); } return new
	 * ModelAndView("redirect:" + projectUrl); }
	 */
	
	@Secured({"ROLE_USER"})
	@RequestMapping(value = "/sendSrbFile", method = { RequestMethod.GET,
            RequestMethod.POST })
public ResponseEntity<ByteArrayResource>  sendSrbFile(
            @RequestParam(name = "id") String id,
            HttpServletRequest request, HttpServletResponse response,Model m,RedirectAttributes r) {
      
      String projectUrl = "";
      try {
            
            if(id.equals("1"))
            {
            String getSrbPath = grievancesService.findSrbPath(id);
            
            if(getSrbPath.startsWith("/"))
            {
            	getSrbPath = getSrbPath.substring(1,getSrbPath.length());
            }
            
            logger.info("Srb Path---------->"+response);
            m.addAttribute("getSrbPath",getSrbPath);
            
            
			/*
			 * File src = new File(getSrbPath);
			 * 
			 * File dest = new File(workDir + src.getName());
			 * 
			 * logger.info("Dec--->"+dest);
			 * 
			 * if (dest.exists()) dest.delete(); FileUtils.copyFile(src, dest);
			 * 
			 * projectUrl = "/" + "workDir" + "/" + dest.getName(); //projectUrl =
			 * projectUrl.replace("\\", "").replaceAll("//","/");
			 */        
            File downloadFile = new File(getSrbPath);
            final byte[] data = s3Service.getFile(getSrbPath);
        	final ByteArrayResource resource = new ByteArrayResource(data);
        	response.setHeader("Content-Disposition", String.format("inline; filename=\"" + downloadFile.getName() + "\""));
        	return ResponseEntity.ok().contentLength(data.length).header("Content-type", "application/octet-stream")
					.header("Content-disposition", "attachment; filename=\"" + downloadFile + "\"").body(resource);
            }
            else
            {
                  
                  r.addAttribute("courseId","");
                  setError(r, "File Not Found!!!");
                 
                //return "redirect:/overview";
            }

      } catch (Exception e) {
            logger.error("Error", e);
      }
	return null;

     // return "redirect:" + projectUrl;
}


	@RequestMapping(value = "/getContactDetails", method = { RequestMethod.GET,
			RequestMethod.POST })
	public @ResponseBody String getContactDetails(
			@RequestParam("cityValue") String cityValue,
			@RequestParam("dept") String dept) {
		Gson gson = new Gson();
		List<Overview> listOfOverView = null;
		listOfOverView = grievancesService.listOfOverViewBasedOnCityAndDept(
				cityValue, "");
		
		String json = gson.toJson(listOfOverView);
		return json;
	}

	@Secured({"ROLE_ADMIN","ROLE_LIBRARIAN"})
	@RequestMapping(value = "/uploadGrievanceConfigItemsForm", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String uploadGrievanceConfigItemsForm(Model m, Principal principal,
			@ModelAttribute GrievancesConfig grievancesConfig) {
		m.addAttribute("webPage", new WebPage("uploadTestQuestion",
				"Upload Test Question", false, false));
		String username = principal.getName();

		m.addAttribute("grievancesConfig", grievancesConfig);
		return "grievances/uploadGrievanceItems";
	}

	@Secured({"ROLE_ADMIN","ROLE_LIBRARIAN"})
	@RequestMapping(value = "/uploadGrievanceItems", method = {
			RequestMethod.POST, RequestMethod.GET })
	public String uploadGrievanceItems(
			@ModelAttribute GrievancesConfig grievancesConfig, Model m,
			@RequestParam(required = false) MultipartFile file,
			RedirectAttributes redirectAttributes, Principal principal) {
		m.addAttribute("webPage", new WebPage("test", "Upload Test Question",
				true, false));
		List<String> validateHeaders = null;
		/*
		 * GrievancesConfig
		 * grievancesConfigDB=grievancesConfigService.findByID(grievancesConfig
		 * .getId());
		 * logger.info("grievancesConfigDB --------- "+grievancesConfigDB);
		 */

		validateHeaders = new ArrayList<String>(Arrays.asList(
				"typeOfGrievance", "grievanceCase"));

		

		String username = principal.getName();
		ExcelReader excelReader = new ExcelReader();

		try {
			List<Map<String, Object>> maps = excelReader
					.readExcelFileUsingColumnHeader(file, validateHeaders);
			

			if (maps.size() == 0) {
				setNote(m, "Excel File is empty");
			} else {
				for (Map<String, Object> mapper : maps) {
					if (mapper.get("Error") != null) {
						
						setNote(m, "Error--->" + mapper.get("Error"));
					} else {
						GrievancesConfig grievanceConfig = new GrievancesConfig();
						grievanceConfig.setTypeOfGrievance((String) mapper
								.get("typeOfGrievance"));
						grievanceConfig.setTypeOfGrievance((String) mapper
								.get("grievanceCase"));

						grievancesConfigService.insertUsingMap(mapper);
						setSuccess(m,
								"test question file uploaded successfully");
					}
				}
			}

			m.addAttribute("grievancesConfig", grievancesConfig);
			
		} catch (Exception ex) {
			setError(m, "Error in uploading file");
			ex.printStackTrace();
		}
		return "grievances/uploadGrievanceItems";
	}
	
	@Secured({ "ROLE_USER" })
	@RequestMapping(value = { "/", "/downloadPortalManuals" }, method = { RequestMethod.GET, RequestMethod.POST })
	public ResponseEntity<ByteArrayResource> downloadPortalManuals(Model m, Principal principal,
			HttpServletRequest request, HttpServletResponse response,RedirectAttributes r,@RequestParam(name="student",required = false) String librariandownload) {
		
		Token userDetails = (Token) principal;
		
		 logger.info("userDetails ---------->"+userDetails.getAuthorities());
		
		 
		 try {
		 
	
//		 List<GrantedAuthority> instListF = userDetails.getAuthorities().stream().distinct().collect(Collectors.toList());
//		 
//		 logger.info("instListF ---------->"+instListF);
//		 String s = instListF.stream().map(Object::toString).collect(Collectors.joining(","));
//		 logger.info("instListF  s---------->"+s);
//		 
//		 List<String> authority = Arrays.asList(s.split(","));
//		 
//		 logger.info("authority  s---------->"+authority);
//		 
		 String getManualPath="";
//		 
//		 List<String> ManualPathList = grievancesService.findPortalManualPath(authority);
//		
//		 logger.info("ManualPathList ---------->"+ManualPathList);
//		 
//		 for (String newPath : ManualPathList)
//		 {
//			 getManualPath += newPath;
//			 logger.info("newPath Path ---------->"+newPath);
//		 }
//		 
//		 logger.info("getManualPath ---------->"+getManualPath);
//		 logger.info("getManualPath ---------->"+response);
			 
		 LmsVariables adminManual = lmsVariablesService.getLmsVariableBykeyword("usermanual_admin"); 
		 LmsVariables facultyManual = lmsVariablesService.getLmsVariableBykeyword("usermanual_faculty"); 
		 LmsVariables studentManual = lmsVariablesService.getLmsVariableBykeyword("usermanual_student"); 
			
		 if (userDetails.getAuthorities().contains(Role.ROLE_ADMIN)) {
			 getManualPath = adminManual.getValue();
		 }else if (userDetails.getAuthorities().contains(Role.ROLE_FACULTY)) {
			 getManualPath = facultyManual.getValue();
		 }else if (userDetails.getAuthorities().contains(Role.ROLE_STUDENT)) {
			 getManualPath = studentManual.getValue();
		 }else if(userDetails.getAuthorities().contains(Role.ROLE_LIBRARIAN))
		 {
			 logger.info("LIBRARIAN--------------- HITTING");
			 logger.info("librariandownload------------>"+librariandownload);
			 if(librariandownload.equals("Student")) {
				 getManualPath = studentManual.getValue();
			 }
			 else
			 {
				 getManualPath = facultyManual.getValue();
			 }
			
		 }
		 
		  File downloadFile = new File(getManualPath);
          final byte[] data = s3Service.getFile(getManualPath);
      	final ByteArrayResource resource = new ByteArrayResource(data);
      	response.setHeader("Content-Disposition", String.format("inline; filename=\"" + downloadFile.getName() + "\""));
      	return ResponseEntity.ok().contentLength(data.length).header("Content-type", "application/octet-stream")
					.header("Content-disposition", "attachment; filename=\"" + downloadFile + "\"").body(resource);
		 }catch (Exception e) {
		logger.info(e);
		}
		return null;
		
	}
	
	

}