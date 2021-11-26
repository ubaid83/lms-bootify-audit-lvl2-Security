package com.spts.lms.web.controllers;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.httpclient.util.URIUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.util.IOUtils;
import org.apache.tika.Tika;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.spts.lms.auth.Token;
import com.spts.lms.beans.amazon.AmazonS3ClientService;
import com.spts.lms.beans.announcement.Announcement;
import com.spts.lms.beans.announcement.Announcement.AnnouncementType;
import com.spts.lms.beans.library.Library;
import com.spts.lms.beans.user.Role;
import com.spts.lms.beans.user.User;
import com.spts.lms.beans.webpages.Webpages;
import com.spts.lms.daos.library.LibraryDAO;
import com.spts.lms.helpers.PaginationHelper.Page;
import com.spts.lms.services.announcement.AnnouncementService;
import com.spts.lms.services.content.StudentContentService;
import com.spts.lms.services.library.LibraryService;
import com.spts.lms.services.user.UserService;
import com.spts.lms.services.webpages.WebpagesService;
import com.spts.lms.web.helper.ExtractZipFileWithSubdirectories;
import com.spts.lms.web.helper.InsertLibrary;
import com.spts.lms.web.helper.UnzipFiles;
import com.spts.lms.web.helper.WebPage;
import com.spts.lms.web.utils.BusinessBypassRule;
import com.spts.lms.web.utils.Utils;
import com.spts.lms.web.utils.ValidationException;


@Controller
@SessionAttributes("userId")
public class LibraryController extends BaseController {

	@Autowired
	ApplicationContext act;

	@Autowired
	LibraryService libraryService;

	@Autowired
	StudentContentService studentContentService;

	@Autowired
	UserService userService;

	@Autowired
	AmazonS3ClientService s3Service;

	/*
	 * @Value("${lms.library.libraryRootFolder}") private String libraryRootFolder;
	 */

	@Value("${lms.library.libraryRootFolderS3}")
	private String libraryRootFolder;

	@Value("${lms.assignment.downloadAllFolder}")
	private String downloadAllFolder;

	/*
	 * @Value("${lms.webpagesFolder}") private String webpagesFolder;
	 */

	@Value("${lms.webpagesFolderS3}")
	private String webpagesFolder;

	/*
	 * @Value("${userMgmtCrudUrlNew}") private String userMgmtCrudUrlNew;
	 */

	@Value("${app}")
	private String appName;

	@Value("${userMgmtCrudUrl}")
	private String userMgmtCrudUrl;

	@Autowired
	WebpagesService webpagesService;

	@Autowired
	UnzipFiles unzipFiles;

	@Autowired
	ExtractZipFileWithSubdirectories extractZip;

	@Autowired
	InsertLibrary insertLibrary;

	@Autowired
	LibraryDAO libraryDAO;

	@Autowired
	AmazonS3ClientService amazonS3ClientService;

	@Autowired
	AnnouncementService announcementService;
	ObjectMapper mapper = new ObjectMapper();

	Client client = ClientBuilder.newClient();
	private static final Logger logger = Logger.getLogger(LibraryController.class);
	private static final String FILE_SEPARATOR = "/";

	private static final String serverURL = "http://localhost:8085/"; // "http://localhost:8085/"
																		// "http://192.168.2.116:8443/"
	private static final String serverCrudURL = "http://localhost:8082/"; // "http://localhost:8082/"
																						// "http://192.168.2.116:8443/usermgmtcrud/"

	@Secured({ "ROLE_FACULTY" })
	@RequestMapping(value = "viewExtLibraries", method = { RequestMethod.GET, RequestMethod.POST })
	public String viewExtLibraries(Principal principal, Model m) {
		String username = principal.getName();
		List<Library> librayList = libraryService.getELibraries();

		m.addAttribute("librayList", librayList);
		return "library/viewExtLibraries";
	}

	@Secured({ "ROLE_USER" })
	@RequestMapping(value = "/viewLibraryAnnouncements", method = { RequestMethod.GET, RequestMethod.POST })
	public String viewLibraryAnnouncements(@ModelAttribute Library library,
			@RequestParam(required = false) String announcementType,
			@RequestParam(required = false, defaultValue = "1") int pageNo, @ModelAttribute Announcement announcement,
			@RequestParam(required = false) String rightsStatus, Model m, Principal p) {

		m.addAttribute("webPage", new WebPage("library", "Library", true, false));
		m.addAttribute("library", library);
		String username = p.getName();

		Token userdetails1 = (Token) p;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		if (library.getFolderPath() == null) {
			// If path is not present
			library.setFolderPath(libraryRootFolder);
		}

		try {
			logger.info("URL--->" + serverCrudURL + "getSchoolsListForLibrary");
			WebTarget webTarget = client.target(URIUtil.encodeQuery(serverCrudURL + "getSchoolsListForLibrary"));

			Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);

			String resp = invocationBuilder.get(String.class);
			logger.info("resp School List--->" + resp);
			List<Map<String, Object>> schoolMap = mapper.readValue(resp,
					new TypeReference<List<Map<String, Object>>>() {
					});
			/*
			 * if(null != library.getShareToSchools()){
			 * 
			 * if(library.getShareToSchools().contains(",")) { String schoolName[] =
			 * library.getShareToSchools().split(","); for(int i=0;i<schoolName.length; i++)
			 * { logger.info("schoolName--->"+schoolName[i]); for(Map<String, Object> s :
			 * schoolMap ) { if(s.containsKey(schoolName[i])){ schoolMap.remove(s);
			 * 
			 * } }
			 * 
			 * } }else{ String schoolName = library.getShareToSchools();
			 * logger.info("schoolName--->"+schoolName); for(Map<String, Object> s :
			 * schoolMap ) { if(s.containsKey(schoolName)){ schoolMap.remove(s);
			 * 
			 * } } }
			 * 
			 * }
			 */

			m.addAttribute("schoolListMap", schoolMap);
			m.addAttribute("appName", appName.trim());
		} catch (Exception ex) {

			logger.error("Exception", ex);
		}

		try {
			List<Library> allItems = libraryService.getParentFoldersUnderAPath(library, username);
			if (allItems == null || allItems.size() == 0) {
				m.addAttribute("No Content available under this Folder");
				m.addAttribute("size", 0);
			} else {

				m.addAttribute("allContent", allItems);
				m.addAttribute("size", allItems.size());
				// List<Library> librarianList = new ArrayList<>();
				for (Library l : allItems) {
					// List<String> strlst = new ArrayList<String>();
					List<Library> librarianList1 = libraryService.getLibraryContentUserRights(l.getId(), username);
					l.setLibrarianList(librarianList1);
					// librarianList.addAll(librarianList1);
				}
				// m.addAttribute("librarianList", librarianList);

			}
			if (announcementType != null) {
				announcement.setAnnouncementType(announcementType);
			} else {
				announcement.setAnnouncementType(AnnouncementType.LIBRARY);
			}

			Page<Announcement> page = announcementService.findAnnouncementsForLibrary(announcement, pageNo, pageSize);
			List<Announcement> announcementList = page.getPageItems();

			m.addAttribute("page", page);
			m.addAttribute("q", getQueryString(announcement));

			if (announcementList == null || announcementList.size() == 0) {
				setNote(m, "No Announcement found");
			}

			List<Webpages> tabsList = new ArrayList<Webpages>();

			if (userdetails1.getAuthorities().contains(Role.ROLE_LIBRARIAN)) {
				tabsList = webpagesService.findAvailLibraryResources();
			} else {

				tabsList = webpagesService.findAvailWebpages();
			}

			if (tabsList.isEmpty() || tabsList.size() != 0) {
				for (Webpages w : tabsList) {

					if (!w.getCreatedBy().isEmpty()) {
						User user = userService.findByUserName(w.getCreatedBy());
						w.setFirstname(user.getFirstname());
						w.setLastname(user.getLastname());
						tabsList.set(tabsList.indexOf(w), w);
					}
				}
			}

			m.addAttribute("tabsList", tabsList);
			m.addAttribute("username", username);

			if (rightsStatus == "Success") {
				setSuccess(m, "Rights saved Successfully.");
			} else if (rightsStatus == "Fail") {
				setError(m, "Failed to save rights");
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(m, "Error in getting content");
		}
		if (userdetails1.getAuthorities().contains(Role.ROLE_ADMIN)) {
			return "library/libraryPageAdmin";
		} else if (userdetails1.getAuthorities().contains(Role.ROLE_LIBRARIAN)) {
			return "library/librarianPage";
		} else {
			return "library/libraryPage";
		}
	}

	/*
	 * @RequestMapping(value = "/downloadAllFileForLibrary", method = {
	 * RequestMethod.GET, RequestMethod.POST }) public void
	 * downloadAllFileForLibrary(HttpServletRequest request, HttpServletResponse
	 * response, Long libraryId) throws ServletException, IOException {
	 * 
	 * // Set the content type based to zip
	 * response.setContentType("Content-type: text/zip"); Library library =
	 * libraryService.findByID(libraryId); String filename =
	 * library.getContentName() + ".zip"; response.setHeader("Content-Disposition",
	 * "attachment; filename=" + filename + "");
	 * 
	 * // List of files to be downloaded List<File> files = new ArrayList();
	 * List<String> fileName = new ArrayList<String>();
	 * 
	 * // Getting students' usernames list List<Library> libraryList =
	 * libraryService.findByParentId(libraryId); for (Library sa : libraryList) {
	 * 
	 * files.add(new File(sa.getFilePath())); fileName.add(sa.getContentName());
	 * 
	 * }
	 * 
	 * ServletOutputStream out = response.getOutputStream();
	 * 
	 * Integer index = 0; File folderPath = new File(downloadAllFolder +
	 * File.separator + org.apache.commons.lang.RandomStringUtils.random(10)); if
	 * (!folderPath.exists()) { folderPath.mkdirs(); } for (File file : files) {
	 * 
	 * try { String ext1 = FilenameUtils.getExtension(file.getName()); File newfile
	 * = new File(folderPath.getAbsolutePath() + File.separator +
	 * fileName.get(index) + "." + ext1); index++; if (file.isDirectory()) {
	 * FileUtils.copyDirectory(file, newfile); } else {
	 * 
	 * FileUtils.copyFile(file, newfile); }
	 * 
	 * } catch (Exception e) { logger.error("Ëxception", e); }
	 * 
	 * }
	 * 
	 * pack(folderPath.getAbsolutePath(), out);
	 * FileUtils.deleteDirectory(folderPath);
	 * 
	 * }
	 */
	
	
	//Working for s3 bucket for deleting download issue commented

	/*
	 * @RequestMapping(value = "/downloadAllFileForLibrary", method = {
	 * RequestMethod.GET, RequestMethod.POST }) public
	 * ResponseEntity<ByteArrayResource>
	 * downloadAllFileForLibrary(HttpServletRequest request, HttpServletResponse
	 * response, Long libraryId) throws ServletException, IOException {
	 * 
	 * // Set the content type based to zip
	 * response.setContentType("Content-type: text/zip"); Library library =
	 * libraryService.findByID(libraryId); String filename =
	 * library.getContentName() + ".zip"; response.setHeader("Content-Disposition",
	 * "attachment; filename=" + filename + ""); String filePath =
	 * library.getFilePath(); File folderPath = new File("/data/" + filename);
	 * String folderPathStr = "/data/" + filename; logger.info("folderPath-------->"
	 * + folderPath); if (!folderPath.exists()) { folderPath.mkdir(); }
	 * List<Library> libraryList = libraryService.findByParentId(library.getId());
	 * checkMultiFolderLibrary(libraryList, folderPathStr);
	 * 
	 * ServletOutputStream out = response.getOutputStream();
	 * response.setHeader("Content-Disposition", "attachment; filename=" + filename
	 * + ""); String projectUrl1 = "/" + "data" + "/" + folderPath.getName() +
	 * ".zip"; pack(folderPath.getAbsolutePath(), out);
	 * FileUtils.deleteDirectory(folderPath); return null; }
	 */

	
	//Download Zipp chnage code Suraj
	@Secured({ "ROLE_USER" })
	@RequestMapping(value = "/downloadAllFileForLibrary", method = { RequestMethod.GET, RequestMethod.POST })
	public ResponseEntity<ByteArrayResource> downloadAllFileForLibrary(HttpServletRequest request,
			HttpServletResponse response, Long libraryId) throws ServletException, IOException {

		// Set the content type based to zip
		response.setContentType("Content-type: text/zip");
		Library library = libraryService.findByID(libraryId);

		String filename = library.getContentName() + ".zip";
		//response.setHeader("Content-Disposition", "attachment; filename=" + filename + "");

		String filePath = library.getFilePath();

		//File folderPath = new File("/data/" + filename);
		File folderPath = new File(downloadAllFolder + "/" + library.getContentName());
		
		//String folderPathStr = "/data/" + filename;
		String folderPathStr = downloadAllFolder +"/"+ library.getContentName();

		logger.info("folderPath-------->" + folderPath);

		if (!folderPath.exists()) {
			folderPath.mkdir();
		}
		List<Library> libraryList = libraryService.findByParentId(library.getId());
		logger.info("folderPathStrForZip----"+folderPathStr);
		logger.info("libraryList----"+libraryList);
		
		checkMultiFolderLibrary(libraryList, folderPathStr);

		
		ServletOutputStream out = response.getOutputStream();

		response.setHeader("Content-Disposition", "attachment; filename=" + filename + "");
		//String projectUrl1 = "/" + "data" + "/" + folderPath.getName() + ".zip";
		String projectUrl1 =  downloadAllFolder +"/" + folderPath.getName() + ".zip";
		pack(folderPath.getAbsolutePath(), out);
		FileUtils.deleteDirectory(folderPath);
		return null;

	}
	
	
	public static void pack(String sourceDirPath, ServletOutputStream out) throws IOException {

		try (ZipOutputStream zs = new ZipOutputStream(new BufferedOutputStream(out))) {
			Path pp = Paths.get(sourceDirPath);
			Files.walk(pp).filter(path -> !Files.isDirectory(path)).forEach(path -> {
				ZipEntry zipEntry = new ZipEntry(pp.relativize(path).toString());
				try {
					zs.putNextEntry(zipEntry);
					zs.write(Files.readAllBytes(path));
					zs.closeEntry();
				} catch (Exception e) {
					logger.error("Ëxception", e);
				}
			});
		}
	}

	@Secured({ "ROLE_LIBRARIAN", "ROLE_FACULTY" })
	@RequestMapping(value = "createWebpageForm", method = { RequestMethod.GET, RequestMethod.POST })
	public String createWebpageForm(Model m, Principal principal, @RequestParam(required = false) Long id) {
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		m.addAttribute("webPage", new WebPage("assignment", "Create Tab", true, false));

		Webpages webpages = new Webpages();
		if (id != null) {
			webpages = webpagesService.findByID(id);
			m.addAttribute("edit", "true");

		}
		m.addAttribute("webpages", webpages);
		if (userdetails1.getAuthorities().contains(Role.ROLE_LIBRARIAN)) {
			return "webpages/createWebpageCo";
		}
		return "webpages/createWebpage";
	}

	/*
	 * @RequestMapping(value = "createWebpageForLibrary", method = {
	 * RequestMethod.GET, RequestMethod.POST }) public String
	 * createWebpageForLibrary(Model m,
	 * 
	 * @RequestParam("file") MultipartFile file, Principal principal,
	 * 
	 * @ModelAttribute Webpages webpages, RedirectAttributes redirectAttributes) {
	 * String username = principal.getName();
	 * 
	 * Token userdetails1 = (Token) principal; String ProgramName =
	 * userdetails1.getProgramName(); User u = userService.findByUserName(username);
	 * 
	 * String acadSession = u.getAcadSession();
	 * 
	 * m.addAttribute("Program_Name", ProgramName); m.addAttribute("AcadSession",
	 * acadSession); m.addAttribute("webPage", new WebPage("assignment",
	 * "Create Tab", true, false)); String type = "LIBRARY"; try { if
	 * (!file.isEmpty()) { String errorMessage = uploadWebpageFile(webpages, file);
	 * } if (webpages.getMakeAvailable() == null) { webpages.setMakeAvailable("N");
	 * } webpages.setType(type); webpages.setCreatedBy(username);
	 * webpages.setLastModifiedBy(username);
	 * webpagesService.insertWithIdReturn(webpages); setSuccess(redirectAttributes,
	 * "Page created successfully!"); } catch (Exception e) {
	 * logger.error("Exception", e); setError(redirectAttributes,
	 * "Error while creating webpage"); }
	 * 
	 * m.addAttribute("webpages", webpages);
	 * 
	 * return "redirect:/viewLibraryAnnouncements"; }
	 */

	@Secured({ "ROLE_LIBRARIAN" })
	@RequestMapping(value = "createWebpageForLibrary", method = { RequestMethod.GET, RequestMethod.POST })
	public String createWebpageForLibrary(Model m, @RequestParam("file") MultipartFile file, Principal principal,
			@ModelAttribute Webpages webpages, RedirectAttributes redirectAttributes) {
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		m.addAttribute("webPage", new WebPage("assignment", "Create Tab", true, false));
		String type = "LIBRARY";
		try {
			if (!file.isEmpty()) {
				Tika tika = new Tika();
				  String detectedType = tika.detect(file.getBytes());
				if (file.getOriginalFilename().contains(".")) {
					Long count = file.getOriginalFilename().chars().filter(c -> c == ('.')).count();
					logger.info("length--->"+count);
					if (count > 1 || count == 0) {
						setError(redirectAttributes, "File uploaded is invalid!");
						return "redirect:/viewLibraryAnnouncements";
					}else {
						String extension = FilenameUtils.getExtension(file.getOriginalFilename());
						logger.info("extension--->"+extension);
						if(extension.equalsIgnoreCase("exe") || ("application/x-msdownload").equals(detectedType) || ("application/x-sh").equals(detectedType)) {
							setError(redirectAttributes, "File uploaded is invalid!");
							return "redirect:/viewLibraryAnnouncements";
						}else {
							byte [] byteArr=file.getBytes();
							if((Byte.toUnsignedInt(byteArr[0]) == 0xFF && Byte.toUnsignedInt(byteArr[1]) == 0xD8) || 
																(Byte.toUnsignedInt(byteArr[0]) == 0x89 && Byte.toUnsignedInt(byteArr[1]) == 0x50) || 
																(Byte.toUnsignedInt(byteArr[0]) == 0x25 && Byte.toUnsignedInt(byteArr[1]) == 0x50) || 
																(Byte.toUnsignedInt(byteArr[0]) == 0x42 && Byte.toUnsignedInt(byteArr[1]) == 0x4D) || 
																(Byte.toUnsignedInt(byteArr[0]) == 0x47 && Byte.toUnsignedInt(byteArr[1]) == 0x49) || 
																(Byte.toUnsignedInt(byteArr[0]) == 0x49 && Byte.toUnsignedInt(byteArr[1]) == 0x49) || 
																(Byte.toUnsignedInt(byteArr[0]) == 0x38 && Byte.toUnsignedInt(byteArr[1]) == 0x42) || 
																(Byte.toUnsignedInt(byteArr[0]) == 0x50 && Byte.toUnsignedInt(byteArr[1]) == 0x4B) || 
																(Byte.toUnsignedInt(byteArr[0]) == 0x1F && Byte.toUnsignedInt(byteArr[1]) == 0x8B) || 
																(Byte.toUnsignedInt(byteArr[0]) == 0x75 && Byte.toUnsignedInt(byteArr[1]) == 0x73) || 
																(Byte.toUnsignedInt(byteArr[0]) == 0x52 && Byte.toUnsignedInt(byteArr[1]) == 0x61) || 
																(Byte.toUnsignedInt(byteArr[0]) == 0xD0 && Byte.toUnsignedInt(byteArr[1]) == 0xCF) || 
																(Byte.toUnsignedInt(byteArr[0]) == 0x50 && Byte.toUnsignedInt(byteArr[1]) == 0x4B) || 
																("text/plain").equals(detectedType)) {
							String errorMessage = uploadWebpageFile(webpages, file);
							} else {
								setError(redirectAttributes, "File uploaded is invalid!");
								return "redirect:/viewLibraryAnnouncements";
							}
						}
					}
				}else {
					setError(redirectAttributes, "File uploaded is invalid!");
					return "redirect:/viewLibraryAnnouncements";
				}
			}
			if (webpages.getMakeAvailable() == null) {
				webpages.setMakeAvailable("N");
			}
			webpages.setType(type);
			webpages.setCreatedBy(username);
			webpages.setLastModifiedBy(username);
			webpagesService.insertWithIdReturn(webpages);
			setSuccess(redirectAttributes, "Page created successfully!");
		} catch (Exception e) {
			logger.error("Exception", e);
			setError(redirectAttributes, "Error while creating webpage");
		}

		m.addAttribute("webpages", webpages);

		return "redirect:/viewLibraryAnnouncements";
	}

	/*
	 * @RequestMapping(value = "updateWebpageForLibrary", method = {
	 * RequestMethod.GET, RequestMethod.POST }) public String
	 * updateWebpageForLibrary(Model m,
	 * 
	 * @RequestParam("file") MultipartFile file, Principal principal,
	 * 
	 * @ModelAttribute Webpages webpages, RedirectAttributes redirectAttributes) {
	 * String username = principal.getName();
	 * 
	 * Token userdetails1 = (Token) principal; String ProgramName =
	 * userdetails1.getProgramName(); User u = userService.findByUserName(username);
	 * 
	 * String acadSession = u.getAcadSession();
	 * 
	 * m.addAttribute("Program_Name", ProgramName); m.addAttribute("AcadSession",
	 * acadSession); m.addAttribute("webPage", new WebPage("assignment",
	 * "Create Tab", true, false)); String type = "LIBRARY"; try { if
	 * (!file.isEmpty()) { String errorMessage = uploadWebpageFile(webpages, file);
	 * } if (webpages.getMakeAvailable() == null) { webpages.setMakeAvailable("N");
	 * } webpages.setActive("Y"); webpages.setType(type);
	 * webpages.setCreatedBy(username); webpages.setLastModifiedBy(username);
	 * webpagesService.update(webpages); setSuccess(redirectAttributes,
	 * "Page updated successfully!"); } catch (Exception e) {
	 * logger.error("Exception", e); setError(redirectAttributes,
	 * "Error while updating webpage"); }
	 * 
	 * m.addAttribute("webpages", webpages);
	 * 
	 * return "redirect:/viewLibraryAnnouncements"; }
	 */

	@Secured({ "ROLE_LIBRARIAN" })
	@RequestMapping(value = "updateWebpageForLibrary", method = { RequestMethod.GET, RequestMethod.POST })
	public String updateWebpageForLibrary(Model m, @RequestParam(name="file",required=false) MultipartFile file, Principal principal,
			@ModelAttribute Webpages webpages, RedirectAttributes redirectAttributes) {
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		m.addAttribute("webPage", new WebPage("assignment", "Create Tab", true, false));
		String type = "LIBRARY";
		try {
			
			Webpages filePathupdate = webpagesService.findByID(webpages.getId());
			
			logger.info("filePathupdate-------------->"+filePathupdate.getFilePath());
			
			if (!file.isEmpty() && file != null) {
				Tika tika = new Tika();
				  String detectedType = tika.detect(file.getBytes());
				if (file.getOriginalFilename().contains(".")) {
					Long count = file.getOriginalFilename().chars().filter(c -> c == ('.')).count();
					logger.info("length--->"+count);
					if (count > 1 || count == 0) {
						setError(redirectAttributes, "File uploaded is invalid!");
						return "redirect:/viewLibraryAnnouncements";
					}else {
						String extension = FilenameUtils.getExtension(file.getOriginalFilename());
						logger.info("extension--->"+extension);
						if(extension.equalsIgnoreCase("exe") || ("application/x-msdownload").equals(detectedType) || ("application/x-sh").equals(detectedType)) {
							setError(redirectAttributes, "File uploaded is invalid!");
							return "redirect:/viewLibraryAnnouncements";
						}else {
							byte [] byteArr=file.getBytes();
							if((Byte.toUnsignedInt(byteArr[0]) == 0xFF && Byte.toUnsignedInt(byteArr[1]) == 0xD8) || 
																(Byte.toUnsignedInt(byteArr[0]) == 0x89 && Byte.toUnsignedInt(byteArr[1]) == 0x50) || 
																(Byte.toUnsignedInt(byteArr[0]) == 0x25 && Byte.toUnsignedInt(byteArr[1]) == 0x50) || 
																(Byte.toUnsignedInt(byteArr[0]) == 0x42 && Byte.toUnsignedInt(byteArr[1]) == 0x4D) || 
																(Byte.toUnsignedInt(byteArr[0]) == 0x47 && Byte.toUnsignedInt(byteArr[1]) == 0x49) || 
																(Byte.toUnsignedInt(byteArr[0]) == 0x49 && Byte.toUnsignedInt(byteArr[1]) == 0x49) || 
																(Byte.toUnsignedInt(byteArr[0]) == 0x38 && Byte.toUnsignedInt(byteArr[1]) == 0x42) || 
																(Byte.toUnsignedInt(byteArr[0]) == 0x50 && Byte.toUnsignedInt(byteArr[1]) == 0x4B) || 
																(Byte.toUnsignedInt(byteArr[0]) == 0x1F && Byte.toUnsignedInt(byteArr[1]) == 0x8B) || 
																(Byte.toUnsignedInt(byteArr[0]) == 0x75 && Byte.toUnsignedInt(byteArr[1]) == 0x73) || 
																(Byte.toUnsignedInt(byteArr[0]) == 0x52 && Byte.toUnsignedInt(byteArr[1]) == 0x61) || 
																(Byte.toUnsignedInt(byteArr[0]) == 0xD0 && Byte.toUnsignedInt(byteArr[1]) == 0xCF) || 
																(Byte.toUnsignedInt(byteArr[0]) == 0x50 && Byte.toUnsignedInt(byteArr[1]) == 0x4B) || 
																("text/plain").equals(detectedType)) {
							String errorMessage = uploadWebpageFile(webpages, file);
							} else {
								setError(redirectAttributes, "File uploaded is invalid!");
								return "redirect:/viewLibraryAnnouncements";
							}
						}
					}
				}else {
					setError(redirectAttributes, "File uploaded is invalid!");
					return "redirect:/viewLibraryAnnouncements";
				}
			}else
			{
				webpages.setFilePath(filePathupdate.getFilePath());
			}
			if (webpages.getMakeAvailable() == null) {
				webpages.setMakeAvailable("N");
			}
			webpages.setActive("Y");
			webpages.setType(type);
			webpages.setCreatedBy(username);
			webpages.setLastModifiedBy(username);
			webpagesService.update(webpages);
			setSuccess(redirectAttributes, "Page updated successfully!");
		} catch (Exception e) {
			logger.error("Exception", e);
			setError(redirectAttributes, "Error while updating webpage");
		}

		m.addAttribute("webpages", webpages);

		return "redirect:/viewLibraryAnnouncements";
	}

	private String uploadWebpageFile(Webpages bean, MultipartFile file) {

		String errorMessage = null;
		InputStream inputStream = null;
		// CommonsMultipartFile file = bean.getFileData();
		String fileName = file.getOriginalFilename();

		try {
			inputStream = file.getInputStream();
			// String filePath = webpagesFolder + File.separator + fileName;

			logger.info("fileName----------->" + fileName);

			String filePath = webpagesFolder;
			// bean.setFilePath(filePath);

			logger.info("filePath WebForm--------->" + filePath);

			logger.info("filePathWev--------------->" + bean.getFilePath());
			Map<String, String> resultMap = s3Service.uploadFileToS3BucketWithRandomString(file, filePath);

			if (resultMap.containsKey("SUCCESS")) {

				// bean.setFileName(fileName);
				bean.setFilePath(filePath + "/" + resultMap.get("SUCCESS"));
				logger.info("filePathWev--------------->" + bean.getFilePath());
			}

		} catch (IOException e) {
			errorMessage = "Error in uploading file : " + e.getMessage();
			logger.error("Exception" + errorMessage, e);
		}
		return errorMessage;
	}

	/*
	 * private String uploadWebpageFile(Webpages bean, MultipartFile file) {
	 * 
	 * String errorMessage = null; InputStream inputStream = null; OutputStream
	 * outputStream = null;
	 * 
	 * // CommonsMultipartFile file = bean.getFileData(); String fileName =
	 * file.getOriginalFilename();
	 * 
	 * // Replace special characters in file fileName = fileName.replaceAll("'",
	 * "_"); fileName = fileName.replaceAll(",", "_"); fileName =
	 * fileName.replaceAll("&", "and"); fileName = fileName.replaceAll(" ", "_");
	 * 
	 * fileName = fileName.substring(0, fileName.lastIndexOf(".")) + "_" +
	 * RandomStringUtils.randomAlphanumeric(10) +
	 * fileName.substring(fileName.lastIndexOf("."), fileName.length());
	 * 
	 * try { inputStream = file.getInputStream(); String filePath = webpagesFolder +
	 * File.separator + fileName; bean.setFilePath(filePath);
	 * 
	 * File folderPath = new File(webpagesFolder); if (!folderPath.exists()) {
	 * folderPath.mkdirs(); } File newFile = new File(filePath); outputStream = new
	 * FileOutputStream(newFile); IOUtils.copy(inputStream, outputStream);
	 * 
	 * } catch (IOException e) { errorMessage = "Error in uploading file : " +
	 * e.getMessage(); logger.error("Exception" + errorMessage, e); } finally {
	 * 
	 * if (inputStream != null) IOUtils.closeQuietly(inputStream);
	 * 
	 * if (outputStream != null) IOUtils.closeQuietly(outputStream);
	 * 
	 * }
	 * 
	 * return errorMessage; }
	 */

	@Secured({ "ROLE_USER" })
	@RequestMapping(value = "/viewLibraryTabs", method = { RequestMethod.GET, RequestMethod.POST })
	public String viewLibraryTabs(@ModelAttribute Library library,
			@RequestParam(required = false, defaultValue = "1") int pageNo, @ModelAttribute Announcement announcement,
			Model m, Principal p) {

		m.addAttribute("webPage", new WebPage("library", "Library", true, false));
		m.addAttribute("library", library);
		String username = p.getName();

		Token userdetails1 = (Token) p;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		if (library.getFolderPath() == null) {
			// If path is not present
			library.setFolderPath(libraryRootFolder);
		}
		try {
			List<Library> allItems = libraryService.getItemsUnderAPath(library);
			if (allItems == null || allItems.size() == 0) {
				m.addAttribute("No Content available under this Folder");
				m.addAttribute("size", 0);
			} else {

				m.addAttribute("allContent", allItems);
				m.addAttribute("size", allItems.size());
			}

			Page<Announcement> page = announcementService.findAnnouncementsForLibrary(announcement, pageNo, pageSize);
			List<Announcement> announcementList = page.getPageItems();

			m.addAttribute("page", page);
			m.addAttribute("q", getQueryString(announcement));

			if (announcementList == null || announcementList.size() == 0) {
				setNote(m, "No Announcement found");
			}
			List<Webpages> tabsList = new ArrayList<Webpages>();
			if (userdetails1.getAuthorities().contains(Role.ROLE_LIBRARIAN)) {

				tabsList = webpagesService.findWebpagesForTypeAndCreatedBy("LIBRARY", username);
			} else {
				tabsList = webpagesService.findAvailWebpages();

			}
			m.addAttribute("tabsList", tabsList);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(m, "Error in getting content");
		}
		return "library/libraryTabsPage";
	}

	/*
	 * @RequestMapping(value = "/downloadWebpageFile", method = { RequestMethod.GET,
	 * RequestMethod.POST }) public ModelAndView downloadWebpageFile(
	 * 
	 * @RequestParam(required = false, name = "id", defaultValue = "") String id,
	 * 
	 * @RequestParam(required = false, name = "filePath", defaultValue = "") String
	 * filePath, HttpServletRequest request, HttpServletResponse response) {
	 * 
	 * OutputStream outStream = null; FileInputStream inputStream = null;
	 * 
	 * try {
	 * 
	 * if (StringUtils.isEmpty(filePath)) { if (!StringUtils.isEmpty(id)) { Webpages
	 * webpages = webpagesService.findByID(Long .valueOf(id)); if (webpages != null)
	 * filePath = webpages.getFilePath(); } else {
	 * 
	 * } }
	 * 
	 * if (StringUtils.isEmpty(filePath)) { request.setAttribute("error", "true");
	 * request.setAttribute("errorMessage", "Error in downloading file."); }
	 * 
	 * // get absolute path of the application ServletContext context =
	 * request.getSession().getServletContext(); File downloadFile = new
	 * File(filePath); inputStream = new FileInputStream(downloadFile);
	 * 
	 * // get MIME type of the file String mimeType = context.getMimeType(filePath);
	 * if (mimeType == null) { // set to binary type if MIME mapping not found
	 * mimeType = "application/octet-stream"; }
	 * 
	 * // set content attributes for the response response.setContentType(mimeType);
	 * response.setContentLength((int) downloadFile.length());
	 * 
	 * // set headers for the response String headerKey = "Content-Disposition";
	 * String headerValue = String.format("attachment; filename=\"%s\"",
	 * downloadFile.getName()); response.setHeader(headerKey, headerValue);
	 * 
	 * // get output stream of the response outStream = response.getOutputStream();
	 * 
	 * IOUtils.copy(inputStream, outStream); } catch (Exception e) {
	 * logger.error("Exception", e); request.setAttribute("error", "true");
	 * request.setAttribute("errorMessage", "Error in downloading file."); } finally
	 * { if (inputStream != null) IOUtils.closeQuietly(inputStream); if (outStream
	 * != null) IOUtils.closeQuietly(outStream);
	 * 
	 * } return null; }
	 */

	@Secured({ "ROLE_USER" })
	@RequestMapping(value = "/downloadWebpageFile", method = { RequestMethod.GET, RequestMethod.POST })
	public ResponseEntity<ByteArrayResource> downloadWebpageFile(
			@RequestParam(required = false, name = "id", defaultValue = "") String id,
			@RequestParam(required = false, name = "filePath", defaultValue = "") String filePath,
			HttpServletRequest request, HttpServletResponse response) {

		OutputStream outStream = null;
		FileInputStream inputStream = null;

		try {

			if (StringUtils.isEmpty(filePath)) {
				if (!StringUtils.isEmpty(id)) {
					Webpages webpages = webpagesService.findByID(Long.valueOf(id));
					if (webpages != null)
						filePath = webpages.getFilePath();
				} else {

				}
			}

			if (StringUtils.isEmpty(filePath)) {
				request.setAttribute("error", "true");
				request.setAttribute("errorMessage", "Error in downloading file.");
			}

			// get absolute path of the application
			ServletContext context = request.getSession().getServletContext();
			File downloadFile = new File(filePath);

			// get MIME type of the file
			String mimeType = context.getMimeType(filePath);
			if (mimeType == null) {
				// set to binary type if MIME mapping not found
				mimeType = "application/octet-stream";
			}

			final byte[] data = s3Service.getFile(filePath);
			final ByteArrayResource resource = new ByteArrayResource(data);
			response.setHeader("Content-Disposition",
					String.format("inline; filename=\"" + downloadFile.getName() + "\""));
			return ResponseEntity.ok().contentLength(data.length).header("Content-type", "application/octet-stream")
					.header("Content-disposition", "attachment; filename=\"" + downloadFile + "\"").body(resource);

		} catch (Exception e) {
			logger.error("Exception", e);
			request.setAttribute("error", "true");
			request.setAttribute("errorMessage", "Error in downloading file.");
		}
		return null;
	}

	@Secured({ "ROLE_LIBRARIAN", "ROLE_FACULTY" })
	@RequestMapping(value = "/deleteWebpage", method = { RequestMethod.GET, RequestMethod.POST })
	public String deleteWebpage(@RequestParam Integer id, RedirectAttributes redirectAttrs) {
		try {
			webpagesService.deleteSoftById(String.valueOf(id));
			setSuccess(redirectAttrs, "Page deleted successfully");

		} catch (Exception e) {
			logger.error("Exception", e);
			setError(redirectAttrs, "Error in deleting Page.");
		}

		return "redirect:/viewLibraryAnnouncements";
	}

	@Secured({ "ROLE_LIBRARIAN", "ROLE_FACULTY" })
	@RequestMapping(value = "/addLibraryItemForm", method = { RequestMethod.GET, RequestMethod.POST })
	public String addLibraryItemForm(@ModelAttribute Library library, Model m, Principal principal) {
		m.addAttribute("webPage", new WebPage("library", "Create Library Item", false, false));

		String contentType = library.getContentType();
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		if (library != null && library.getId() != null) {

			library = libraryService.findByID(library.getId());
			m.addAttribute("edit", "true");

		}

		m.addAttribute("library", library);

		if (Library.FOLDER.equalsIgnoreCase(contentType)) {
			return "library/addLibraryFolder";
		} else if (Library.FILE.equalsIgnoreCase(contentType)) {
			return "library/addLibraryFile";
		} else if (Library.LINK.equalsIgnoreCase(contentType)) {
			return "library/addLibraryLink";
		} else if (Library.ZIP.equalsIgnoreCase(contentType)) {
			return "library/addLibraryZip";
		} else if (Library.YOUTUBE_VIDEO.equalsIgnoreCase(contentType)) {
			return "library/addLibraryVideo";
		} else {
			return "library/addLibraryFolder";
		}

	}

	/*
	 * @RequestMapping(value = "/addLibraryZip", method = { RequestMethod.GET,
	 * RequestMethod.POST }) public String addLibraryZip(@ModelAttribute Library
	 * library, Principal principal,
	 * 
	 * @RequestParam("file") MultipartFile file, RedirectAttributes redirectAttrs,
	 * Model m) { String username = principal.getName();
	 * 
	 * Token userdetails1 = (Token) principal; String ProgramName =
	 * userdetails1.getProgramName(); User u = userService.findByUserName(username);
	 * 
	 * String acadSession = u.getAcadSession();
	 * 
	 * m.addAttribute("Program_Name", ProgramName); m.addAttribute("AcadSession",
	 * acadSession);
	 * 
	 * redirectAttrs.addFlashAttribute("library", library);
	 * 
	 * Long parentId = library.getParentId(); String folderPath =
	 * library.getFolderPath();
	 * 
	 * List<Library> libraryItemsList = new ArrayList<Library>(); Library pId =
	 * libraryService.findByID(parentId); try {
	 * 
	 * performFolderPathCheck(library); // for (MultipartFile file : files) {
	 * 
	 * if (!file.isEmpty()) { if (!file.getOriginalFilename().contains(".zip")) {
	 * setError(redirectAttrs, "Selected File is not a zip file."); return
	 * "redirect:/addLibraryItemForm"; } String errorMessage1 =
	 * uploadLibraryFileForS3(library, library.getFolderPath(), file); String
	 * filePathS3 = library.getFilePath();
	 * 
	 * String tempFolderPath = downloadAllFolder;
	 * 
	 * String errorMessage = uploadLibraryZipFile(library, downloadAllFolder + "/",
	 * file, errorMessage1); String filePathTemp = library.getFilePath();
	 * logger.info("errorMessage------------- " + errorMessage); String newFileName
	 * = library.getFileName().substring(0, library.getFileName().length() - 4);
	 * 
	 * if (errorMessage == null) { File dest = new File(library.getFolderPath() +
	 * File.separator + newFileName);
	 * 
	 * if (!dest.exists()) dest.mkdirs();
	 * 
	 * 
	 * 
	 * boolean isSuccess = extractZipContents.unzip( library.getFilePath(),
	 * dest.getAbsolutePath());
	 * 
	 * File tempFolder = new File(tempFolderPath + "/" + newFileName);
	 * extractZip.unzip(library.getFilePath()); boolean renamed =
	 * amazonS3ClientService.createFolder(newFileName, library.getFolderPath());
	 * boolean upload = false; if (renamed) { if (tempFolder.listFiles().length > 0)
	 * { upload = amazonS3ClientService.uploadDir(tempFolderPath + "/" +
	 * newFileName, library.getFolderPath() + "/" + newFileName, true); } else {
	 * upload = true; } } // if (isSuccess) {
	 * library.setContentType(Library.FOLDER);
	 * library.setFilePath(filePathS3.substring(0, filePathS3.length() - 4));
	 * library.setFolderPath(library.getFolderPath() + "/" + newFileName);
	 * library.setContentName(newFileName); library.setCreatedBy(username);
	 * library.setLastModifiedBy(username); if (null != parentId && null !=
	 * pId.getShareToSchools()) {
	 * library.setShareToSchools(pId.getShareToSchools()); } if (upload) {
	 * libraryService.insertWithIdReturn(library);
	 * libraryService.readLibraryForZipS3(new File(tempFolderPath + "/" +
	 * newFileName), library.getId(), username, library.getShareToSchools(),
	 * library.getFolderPath()); } // File currentDir = new
	 * File(dest.getAbsolutePath());
	 * 
	 * // // List<Library> itemsUnderPath = libraryService.getItemsToShare(library);
	 * // // if(null!=parentId && null != pId.getShareToSchools()){ //
	 * library.setShareToSchools(null); // String schoolName[] =
	 * pId.getShareToSchools().split(","); // for(int i=0;i<schoolName.length; i++){
	 * // String apiUrl = serverURL
	 * +schoolName[i].replace(" ","")+"/api/insertShareLibraryContent"; //
	 * logger.info("apiUrl---->"+apiUrl); // // try { // ObjectMapper Objmapper =
	 * new ObjectMapper(); // String libJson =
	 * Objmapper.writeValueAsString(itemsUnderPath); //
	 * logger.info("libJson---->"+libJson); // WebTarget webTarget =
	 * client.target(URIUtil.encodeQuery(apiUrl)); // Invocation.Builder
	 * invocationBuilder = webTarget.request(); // Response response =
	 * invocationBuilder.post(Entity.entity(libJson.toString(),
	 * MediaType.APPLICATION_JSON)); // logger.info("response---->"+response); // }
	 * catch (Exception e) { // // TODO Auto-generated catch block //
	 * logger.error("Exception", e); // } // } // } // // } // List<Library>
	 * givenRights =
	 * libraryService.getLibraryContentRightsById(String.valueOf(parentId)); //
	 * if(givenRights.size() > 0){ //
	 * giveLibrarianRights(String.valueOf(library.getId()),
	 * String.valueOf(parentId), principal); // } //
	 * library.setContentName(file.getName());
	 * 
	 * setSuccess(redirectAttrs, "File Created Successfully"); } else {
	 * setError(redirectAttrs, "Error in uploading file " + errorMessage); return
	 * "redirect:/addLibraryItemForm"; } } if (file == null || file.isEmpty()) {
	 * setError(redirectAttrs, "No file selected. Please select a file to upload.");
	 * return "redirect:/addLibraryItemForm"; } // }
	 * 
	 * } catch (Exception e) { logger.error(e.getMessage(), e);
	 * setError(redirectAttrs, "Error in creating file"); return
	 * "redirect:/addLibraryItemForm"; } if (parentId != null &&
	 * !folderPath.isEmpty()) { return "redirect:/viewLibrary?parentId=" + parentId
	 * + "&folderPath=" + folderPath + "&createdBy=" + username +
	 * "&createOnly=&editOnly="; } else { return
	 * "redirect:/viewLibraryAnnouncements"; } }
	 */

	@Secured({ "ROLE_LIBRARIAN", "ROLE_FACULTY" })
	@RequestMapping(value = "/addLibraryZip", method = { RequestMethod.GET, RequestMethod.POST })
	public String addLibraryZip(@ModelAttribute Library library, Principal principal,
			@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttrs, Model m) {
		String username = principal.getName();
		
		try {
			if(!library.getContentType().isEmpty()) {
				BusinessBypassRule.validateAlphaNumeric(library.getContentType());
			}
			if(library.getParentId() != null) {
				BusinessBypassRule.validateNumeric(library.getParentId());
			}
			if(!library.getContentDescription().isEmpty()) {
				BusinessBypassRule.validateAlphaNumeric(library.getContentDescription());
			}
		} catch (ValidationException ve) {
			ve.printStackTrace();
			setError(redirectAttrs,ve.getMessage());
			return "redirect:/viewLibraryAnnouncements";
		}

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		redirectAttrs.addFlashAttribute("library", library);

		Long parentId = library.getParentId();
		String folderPath = library.getFolderPath();

		List<Library> libraryItemsList = new ArrayList<Library>();
		Library pId = libraryService.findByID(parentId);
		try {

			performFolderPathCheck(library);
			//performFolderPathCheckLocal(library);
			// for (MultipartFile file : files) {

			if (!file.isEmpty()) {
				Tika tika = new Tika();
				  String detectedType = tika.detect(file.getBytes());
				if (file.getOriginalFilename().contains(".")) {
					Long count = file.getOriginalFilename().chars().filter(c -> c == ('.')).count();
					logger.info("length--->"+count);
					if (count > 1 || count == 0) {
						setError(redirectAttrs, "File uploaded is invalid!");
						return "redirect:/addLibraryItemForm";
					}else {
						String extension = FilenameUtils.getExtension(file.getOriginalFilename());
						logger.info("extension--->"+extension);
						if(extension.equalsIgnoreCase("exe") || ("application/x-msdownload").equals(detectedType) || ("application/x-sh").equals(detectedType)) {
							setError(redirectAttrs, "File uploaded is invalid!");
							return "redirect:/addLibraryItemForm";
						}else {
							byte [] byteArr=file.getBytes();
							if((Byte.toUnsignedInt(byteArr[0]) == 0xFF && Byte.toUnsignedInt(byteArr[1]) == 0xD8) || 
																(Byte.toUnsignedInt(byteArr[0]) == 0x89 && Byte.toUnsignedInt(byteArr[1]) == 0x50) || 
																(Byte.toUnsignedInt(byteArr[0]) == 0x25 && Byte.toUnsignedInt(byteArr[1]) == 0x50) || 
																(Byte.toUnsignedInt(byteArr[0]) == 0x42 && Byte.toUnsignedInt(byteArr[1]) == 0x4D) || 
																(Byte.toUnsignedInt(byteArr[0]) == 0x47 && Byte.toUnsignedInt(byteArr[1]) == 0x49) || 
																(Byte.toUnsignedInt(byteArr[0]) == 0x49 && Byte.toUnsignedInt(byteArr[1]) == 0x49) || 
																(Byte.toUnsignedInt(byteArr[0]) == 0x38 && Byte.toUnsignedInt(byteArr[1]) == 0x42) || 
																(Byte.toUnsignedInt(byteArr[0]) == 0x50 && Byte.toUnsignedInt(byteArr[1]) == 0x4B) || 
																(Byte.toUnsignedInt(byteArr[0]) == 0x1F && Byte.toUnsignedInt(byteArr[1]) == 0x8B) || 
																(Byte.toUnsignedInt(byteArr[0]) == 0x75 && Byte.toUnsignedInt(byteArr[1]) == 0x73) || 
																(Byte.toUnsignedInt(byteArr[0]) == 0x52 && Byte.toUnsignedInt(byteArr[1]) == 0x61) || 
																(Byte.toUnsignedInt(byteArr[0]) == 0xD0 && Byte.toUnsignedInt(byteArr[1]) == 0xCF) || 
																(Byte.toUnsignedInt(byteArr[0]) == 0x50 && Byte.toUnsignedInt(byteArr[1]) == 0x4B) || 
																("text/plain").equals(detectedType)) {
								logger.info("file is valid--->");
							} else {
								setError(redirectAttrs, "File uploaded is invalid!");
								return "redirect:/addLibraryItemForm";
							}
						}
					}
				}else {
					setError(redirectAttrs, "File uploaded is invalid!");
					return "redirect:/addLibraryItemForm";
				}
				if (!file.getOriginalFilename().contains(".zip")) {
					setError(redirectAttrs, "Selected File is not a zip file.");
					return "redirect:/addLibraryItemForm";
				}

				// AwsZipUpload
				// String errorMessage = uploadLibraryFile(library, library.getFolderPath(),
				// file);

				// String errorMessage = uploadLibraryFileLocalServer(library,
				// library.getFolderPath(), file);
				String errorMessage1 = uploadLibraryFileForS3(library, library.getFolderPath(), file);
				String filePathS3 = library.getFilePath();

				String tempFolderPath = downloadAllFolder;

				String errorMessage = uploadLibraryZipFile(library, downloadAllFolder + "/", file, errorMessage1);
				String filePathTemp = library.getFilePath();
				logger.info("errorMessage------------- " + errorMessage);
				logger.info("library.getFileName()." + library.getFileName());
				String newFileName = library.getFileName().substring(0, library.getFileName().length() - 4);

				if (errorMessage == null) {
					File dest = new File(library.getFolderPath() + File.separator + newFileName);
					/*
					 * if (!dest.exists()) dest.mkdirs();
					 */

					/*
					 * boolean isSuccess = extractZipContents.unzip( library.getFilePath(),
					 * dest.getAbsolutePath());
					 */

//				extractZip.unzip(library.getFilePath());
//				logger.info("Unzip----------->" + library.getFilePath());
//				// if (isSuccess) {
//				library.setContentType(Library.FOLDER);
//				library.setFilePath(library.getFilePath().substring(0, library.getFilePath().length() - 4));
//				library.setFolderPath(library.getFolderPath() + newFileName);
//				library.setContentName(newFileName);
//				library.setCreatedBy(username);
//				library.setLastModifiedBy(username);
//				if (null != parentId && null != pId.getShareToSchools()) {
//					library.setShareToSchools(pId.getShareToSchools());
//				}
//				libraryService.insertWithIdReturn(library);
					File tempFolder = new File(tempFolderPath + "/" + newFileName);
					extractZip.unzip(library.getFilePath());
					boolean renamed = s3Service.createFolder(newFileName, library.getFolderPath());
					boolean upload = false;
					if (renamed) {
						if (tempFolder.listFiles().length > 0) {
							upload = s3Service.uploadDir(tempFolderPath + "/" + newFileName,
									library.getFolderPath() + "/" + newFileName, true);
						} else {
							upload = true;
						}
					}
					// if (isSuccess) {
					library.setContentType(Library.FOLDER);
					library.setFilePath(filePathS3.substring(0, filePathS3.length() - 4));
					library.setFolderPath(library.getFolderPath() + "/" + newFileName);
					library.setContentName(newFileName);
					library.setCreatedBy(username);
					library.setLastModifiedBy(username);
					if (null != parentId && null != pId.getShareToSchools()) {
						library.setShareToSchools(pId.getShareToSchools());
					}
					if (upload) {
						libraryService.insertWithIdReturn(library);
						libraryService.readLibraryForZipS3(new File(tempFolderPath + "/" + newFileName),
								library.getId(), username, library.getShareToSchools(), library.getFolderPath());

						// File currentDir = new File(dest.getAbsolutePath());
						logger.info("ReadLibrary------------------->>" + library.getFolderPath() + " " + library.getId()
								+ " " + username + " " + library.getShareToSchools());
						// libraryService.readLibraryForZip(new File(library.getFolderPath()),
						// library.getId(), username,library.getShareToSchools());

						List<Library> itemsUnderPath = libraryService.getItemsToShare(library);

						if (null != parentId && null != pId.getShareToSchools()) {
							library.setShareToSchools(null);
							String schoolName[] = pId.getShareToSchools().split(",");
							for (int i = 0; i < schoolName.length; i++) {
								String apiUrl = serverURL + schoolName[i].replace(" ", "")
										+ "/api/insertShareLibraryContent";
								logger.info("apiUrl---->" + apiUrl);

								try {
									ObjectMapper Objmapper = new ObjectMapper();
									String libJson = Objmapper.writeValueAsString(itemsUnderPath);
									logger.info("libJson---->" + libJson);
									WebTarget webTarget = client.target(URIUtil.encodeQuery(apiUrl));
									Invocation.Builder invocationBuilder = webTarget.request();
									Response response = invocationBuilder
											.post(Entity.entity(libJson.toString(), MediaType.APPLICATION_JSON));
									logger.info("response---->" + response);
								} catch (Exception e) {
									// TODO Auto-generated catch block
									logger.error("Exception", e);
								}
							}
						}
						// }
						List<Library> givenRights = libraryService
								.getLibraryContentRightsById(String.valueOf(parentId));
						if (givenRights.size() > 0) {
							giveLibrarianRights(String.valueOf(library.getId()), String.valueOf(parentId), principal);
						}
						// library.setContentName(file.getName());
						FileUtils.deleteDirectory(tempFolder);// delete folder from temp
						setSuccess(redirectAttrs, "File Created Successfully");
					}
				} else {
					setError(redirectAttrs, "Error in uploading file " + errorMessage);
					return "redirect:/addLibraryItemForm";
				}
			}
			if (file == null || file.isEmpty()) {
				setError(redirectAttrs, "No file selected. Please select a file to upload.");
				return "redirect:/addLibraryItemForm";
			}
			// }

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(redirectAttrs, "Error in creating file");
			return "redirect:/addLibraryItemForm";
		}
		if (parentId != null && !folderPath.isEmpty()) {
			return "redirect:/viewLibrary?parentId=" + parentId + "&folderPath=" + folderPath + "&createdBy=" + username
					+ "&createOnly=&editOnly=";
		} else {
			return "redirect:/viewLibraryAnnouncements";
		}
	}

	private String uploadLibraryZipFile(Library library, String folderPath, MultipartFile file, String fileName) {

		String errorMessage = null;
		InputStream inputStream = null;
		OutputStream outputStream = null;

		// CommonsMultipartFile file = bean.getFileData();
//		String fileName = file.getOriginalFilename();
//
//		// Replace special characters in file
//		fileName = fileName.replaceAll("'", "_");
//		fileName = fileName.replaceAll(",", "_");
//		fileName = fileName.replaceAll("&", "and");
//		fileName = fileName.replaceAll(" ", "_");
//
//		fileName = fileName.substring(0, fileName.lastIndexOf("."))
//				+ "_" + RandomStringUtils.randomAlphanumeric(10) 
//				+ fileName.substring(fileName.lastIndexOf("."),
//						fileName.length());
		library.setContentName(file.getOriginalFilename());
		logger.info("folderPath--->" + folderPath);
		logger.info("fileName--->" + fileName);
		try {

			inputStream = file.getInputStream();
			String filePath = folderPath + fileName;
			logger.info("FilePath--------->" + filePath);
			// Check if Folder exists. If not then create
			File directory = new File(folderPath);
			if (!directory.exists()) {
				directory.mkdirs();
			}

			File newFile = new File(filePath);

			outputStream = new FileOutputStream(newFile);
			int read = 0;
			byte[] bytes = new byte[1024];

			while ((read = inputStream.read(bytes)) != -1) {
				outputStream.write(bytes, 0, read);
			}
			library.setFileName(fileName);
			library.setFilePath(filePath);
			outputStream.close();
			inputStream.close();
		} catch (IOException e) {
			errorMessage = "Error in uploading Content file : " + e.getMessage();

		}

		return errorMessage;
	}

	private String uploadLibraryFileForS3(Library library, String folderPath, MultipartFile file) {

		String errorMessage = null;
		try {
			if (folderPath.startsWith("/")) {
				folderPath = StringUtils.substring(folderPath, 1);
			}
			if (folderPath.endsWith("/")) {
				folderPath = folderPath.substring(0, folderPath.length() - 1);
			}
			logger.info("filePath--->" + folderPath);

			Map<String, String> s3FileNameMap = amazonS3ClientService.uploadFileToS3BucketWithRandomString(file,
					folderPath);
			logger.info("map--->" + s3FileNameMap);
			if (s3FileNameMap.containsKey("SUCCESS")) {
				String s3FileName = s3FileNameMap.get("SUCCESS");
				String filePath = folderPath + "/" + s3FileName;
				library.setFolderPath(folderPath);
				library.setFileName(s3FileName);
				library.setFilePath(filePath);
				errorMessage = s3FileName;
			} else {
				throw new Exception("Error in uploading file");
			}
		} catch (Exception e) {
			errorMessage = "Error in uploading file : " + e.getMessage();
			logger.error("Exception" + errorMessage, e);
		}

		return errorMessage;
	}

	/*
	 * @RequestMapping(value = "/addLibraryFolder", method = { RequestMethod.GET,
	 * RequestMethod.POST }) public String addLibraryFolder(@ModelAttribute Library
	 * library, RedirectAttributes redirectAttrs, Principal principal, Model m) {
	 * String username = principal.getName();
	 * redirectAttrs.addFlashAttribute("content", library);
	 * 
	 * Token userdetails1 = (Token) principal; String ProgramName =
	 * userdetails1.getProgramName(); User u = userService.findByUserName(username);
	 * 
	 * String acadSession = u.getAcadSession();
	 * 
	 * m.addAttribute("Program_Name", ProgramName); m.addAttribute("AcadSession",
	 * acadSession); File file = null; Long parentId = library.getParentId(); String
	 * folderPath = library.getFolderPath(); Library redLibrary = new Library(); if
	 * (parentId != null) { redLibrary = libraryService.findByID(parentId);
	 * redLibrary.setParentId(parentId);
	 * 
	 * } else { redLibrary.setFolderPath(libraryRootFolder);
	 * 
	 * String pId = ""; redLibrary.setParentId(Long.valueOf(pId));
	 * 
	 * }
	 * 
	 * redirectAttrs.addFlashAttribute("library", redLibrary); try {
	 * 
	 * performFolderPathCheck(library);
	 * library.setContentName(library.getContentName().trim());
	 * 
	 * String completFolderPath = library.getFolderPath() +
	 * library.getContentName();
	 * 
	 * file = new File(completFolderPath); boolean created = false;
	 * 
	 * Library pId = libraryService.findByID(parentId); if (!file.exists()) { //
	 * Create Folder if it does not exists created = file.mkdirs();
	 * library.setCreatedBy(username); library.setLastModifiedBy(username);
	 * library.setFilePath(completFolderPath);
	 * library.setFolderPath(completFolderPath); if (null != parentId && null !=
	 * pId.getShareToSchools()) {
	 * library.setShareToSchools(pId.getShareToSchools()); } // Create entry in DB
	 * logger.info("library.getShareToSchools---->" + library.getShareToSchools());
	 * libraryService.insertWithIdReturn(library);
	 * 
	 * // logger.info("getShareToSchools---->"+pId.getShareToSchools()); if (null !=
	 * parentId && null != pId.getShareToSchools()) {
	 * library.setShareToSchools(null); String schoolName[] =
	 * pId.getShareToSchools().split(","); for (int i = 0; i < schoolName.length;
	 * i++) { String apiUrl = serverURL + schoolName[i].replace(" ", "") +
	 * "/api/addLibraryFolderToShareContent"; logger.info("apiUrl---->" + apiUrl);
	 * 
	 * try { ObjectMapper Objmapper = new ObjectMapper(); String libJson =
	 * Objmapper.writeValueAsString(library); WebTarget webTarget =
	 * client.target(URIUtil.encodeQuery(apiUrl)); Invocation.Builder
	 * invocationBuilder = webTarget.request(); Response response =
	 * invocationBuilder .post(Entity.entity(libJson.toString(),
	 * MediaType.APPLICATION_JSON)); logger.info("response---->" + response); }
	 * catch (Exception e) { // TODO Auto-generated catch block
	 * logger.error("Exception", e); } } }
	 * 
	 * List<Library> givenRights =
	 * libraryService.getLibraryContentRightsById(String.valueOf(parentId)); if
	 * (givenRights.size() > 0) {
	 * giveLibrarianRights(String.valueOf(library.getId()),
	 * String.valueOf(parentId), principal); } if (created) {
	 * setSuccess(redirectAttrs, "Folder Created Successfully"); } else {
	 * setError(redirectAttrs, "Error in creating folder"); return
	 * "redirect:/addLibraryItemForm"; } } else { setError(redirectAttrs,
	 * "Folder already exists with name " + library.getContentName()); return
	 * "redirect:/addLibraryItemForm"; }
	 * 
	 * } catch (Exception e) { logger.error(e.getMessage(), e);
	 * setError(redirectAttrs, "Error in creating folder"); // Delete folder if it
	 * was created successfully, but DB operation // failed. if (file != null &&
	 * file.list().length == 0) { file.delete(); } return
	 * "redirect:/addLibraryItemForm"; }
	 * 
	 * if (parentId != null && !folderPath.isEmpty()) { return
	 * "redirect:/viewLibrary?parentId=" + parentId + "&folderPath=" + folderPath +
	 * "&createdBy=" + username + "&createOnly=&editOnly="; } else { //
	 * redirectAttrs.addFlashAttribute("library", library); return
	 * "redirect:/viewLibraryAnnouncements"; } }
	 */

	@Secured({ "ROLE_LIBRARIAN", "ROLE_FACULTY" })
	@RequestMapping(value = "/addLibraryFolder", method = { RequestMethod.GET, RequestMethod.POST })
	public String addLibraryFolder(@ModelAttribute Library library, RedirectAttributes redirectAttrs,
			Principal principal, Model m) {
		
		try {
			if(!library.getContentType().isEmpty()) {
				BusinessBypassRule.validateAlphaNumeric(library.getContentType());
			}
			if(library.getParentId() != null) {
				BusinessBypassRule.validateNumeric(library.getParentId());
			}
			BusinessBypassRule.validateAlphaNumeric(library.getContentName());
			if(!library.getContentDescription().isEmpty()) {
				BusinessBypassRule.validateAlphaNumeric(library.getContentDescription());
			}
		} catch (ValidationException ve) {
			ve.printStackTrace();
			setError(redirectAttrs,ve.getMessage());
			return "redirect:/addLibraryItemForm";
		}
		
		
		String username = principal.getName();
		redirectAttrs.addFlashAttribute("content", library);

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		File file = null;
		Long parentId = library.getParentId();
		String folderPath = library.getFolderPath();
		Library redLibrary = new Library();
		if (parentId != null) {
			redLibrary = libraryService.findByID(parentId);
			redLibrary.setParentId(parentId);

		} else {
			redLibrary.setFolderPath(libraryRootFolder);
			/*
			 * String pId = ""; redLibrary.setParentId(Long.valueOf(pId));
			 */
		}

		redirectAttrs.addFlashAttribute("library", redLibrary);
		try {

			performFolderPathCheck(library);
			library.setContentName(library.getContentName().trim());

			String completFolderPath = library.getFolderPath() + "/" + library.getContentName();
			
			if(completFolderPath.startsWith("/")) {
				completFolderPath = StringUtils.substring(completFolderPath, 1);
			}
			
			
			logger.info("completFolderPath-------->>>>" + completFolderPath);
			logger.info("Logger -------->>>>" + library.getFolderPath() + " " + library.getContentName());
			boolean fileExist = false;
			try {
				byte[] fileByte = s3Service.getFile(completFolderPath);
				fileExist = true;

			} catch (Exception ex) {
				fileExist = false;
			}

			// file = new File(completFolderPath);
			boolean created = false;
			
			String libraryFolderPath = library.getFolderPath();
			
			if(libraryFolderPath.startsWith("/"))
			{
				libraryFolderPath = StringUtils.substring(libraryFolderPath, 1);
			}
			
			

			Library pId = libraryService.findByID(parentId);
			if (!fileExist) {
				// Create Folder if it does not exists
				logger.info("Logger-------->>>>" + library.getFolderPath() + " " + library.getContentName());
				//created = s3Service.createFolder(library.getContentName(), library.getFolderPath());
				created = s3Service.createFolder(library.getContentName(), libraryFolderPath);
				library.setCreatedBy(username);
				library.setLastModifiedBy(username);
				library.setFilePath(completFolderPath);
				library.setFolderPath(completFolderPath);
				if (null != parentId && null != pId.getShareToSchools()) {
					library.setShareToSchools(pId.getShareToSchools());
				}
				// Create entry in DB
				logger.info("library.getShareToSchools---->" + library.getShareToSchools());
				libraryService.insertWithIdReturn(library);

				// logger.info("getShareToSchools---->"+pId.getShareToSchools());
				if (null != parentId && null != pId.getShareToSchools()) {
					library.setShareToSchools(null);
					String schoolName[] = pId.getShareToSchools().split(",");
					for (int i = 0; i < schoolName.length; i++) {
						String apiUrl = serverURL + schoolName[i].replace(" ", "")
								+ "/api/addLibraryFolderToShareContent";
						logger.info("apiUrl---->" + apiUrl);

						try {
							ObjectMapper Objmapper = new ObjectMapper();
							String libJson = Objmapper.writeValueAsString(library);
							WebTarget webTarget = client.target(URIUtil.encodeQuery(apiUrl));
							Invocation.Builder invocationBuilder = webTarget.request();
							Response response = invocationBuilder
									.post(Entity.entity(libJson.toString(), MediaType.APPLICATION_JSON));
							logger.info("response---->" + response);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							logger.error("Exception", e);
						}
					}
				}

				List<Library> givenRights = libraryService.getLibraryContentRightsById(String.valueOf(parentId));
				if (givenRights.size() > 0) {
					giveLibrarianRights(String.valueOf(library.getId()), String.valueOf(parentId), principal);
				}
				if (created) {
					setSuccess(redirectAttrs, "Folder Created Successfully");
				} else {

					setError(redirectAttrs, "Error in creating folder");
					return "redirect:/addLibraryItemForm";
				}
			} else {
				setError(redirectAttrs, "Folder already exists with name " + library.getContentName());
				return "redirect:/addLibraryItemForm";
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(redirectAttrs, "Error in creating folder");
			// Delete folder if it was created successfully, but DB operation
			// failed.
			if (file != null && file.list().length == 0) {
				file.delete();
			}
			return "redirect:/addLibraryItemForm";
		}

		if (parentId != null && !folderPath.isEmpty()) {
			return "redirect:/viewLibrary?parentId=" + parentId + "&folderPath=" + folderPath + "&createdBy=" + username
					+ "&createOnly=&editOnly=";
		} else {
			// redirectAttrs.addFlashAttribute("library", library);
			return "redirect:/viewLibraryAnnouncements";
		}
	}

	/*
	 * // Hiren 27-11-2019
	 * 
	 * @RequestMapping(value = "/updateLibraryFolder", method = { RequestMethod.GET,
	 * RequestMethod.POST }) public String updateLibraryFolder(@ModelAttribute
	 * Library library, RedirectAttributes redirectAttrs, Principal principal, Model
	 * m) { String username = principal.getName();
	 * redirectAttrs.addFlashAttribute("library", library); Long parentId =
	 * library.getParentId(); String folderPath = library.getFolderPath(); try {
	 * 
	 * Token userdetails1 = (Token) principal; String ProgramName =
	 * userdetails1.getProgramName(); User u = userService.findByUserName(username);
	 * 
	 * String acadSession = u.getAcadSession();
	 * 
	 * m.addAttribute("Program_Name", ProgramName); m.addAttribute("AcadSession",
	 * acadSession); performFolderPathCheck(library); File f = new
	 * File(library.getFolderPath()); String newFileParent = f.getParent();
	 * 
	 * newFileParent = newFileParent.replaceAll("\\\\", "/");
	 * logger.info("newFileParent" + newFileParent); File parentFile = new
	 * File(newFileParent);
	 * 
	 * String completFolderPath = newFileParent + "/" + library.getContentName();
	 * library.setLastModifiedBy(username);
	 * 
	 * library.setFilePath(completFolderPath);
	 * 
	 * File file = new File(completFolderPath); Library redLibrary = new Library();
	 * if (parentId != null) { redLibrary =
	 * libraryService.findByID(library.getParentId());
	 * redLibrary.setParentId(parentId); } Library oldItem =
	 * libraryService.findByID(library.getId()); if (!file.exists()) { // Means
	 * Folder Name changed, Rename folder String newFolderName =
	 * library.getContentName(); // Library oldItem =
	 * libraryService.findByID(library.getId()); File existingFolder = new
	 * File(oldItem.getFilePath());
	 * 
	 * File newFolder = new File(existingFolder.getParent() + FILE_SEPARATOR +
	 * newFolderName);
	 * 
	 * boolean renamed = existingFolder.renameTo(newFolder);
	 * 
	 * if (!renamed) { setError(redirectAttrs, "Error in Renaming folder");
	 * redirectAttrs.addFlashAttribute("edit", "true"); return
	 * "redirect:/addLibraryItemForm"; } else { logger.info("AbsolutePath--->" +
	 * newFolder.getAbsolutePath()); logger.info("Path--->" + newFolder.getPath());
	 * libraryService.updateChildrenDetails(newFolder.getPath().replaceAll("\\\\", "
	 * /"), oldItem.getFilePath().replaceAll("\\\\", "/"));
	 * library.setFolderPath(newFolder.getPath().replaceAll("\\\\", "/"));
	 * 
	 * redirectAttrs.addFlashAttribute("library", redLibrary);
	 * 
	 * } }
	 * 
	 * libraryService.updateFolder(library); oldItem.setNewLibraryItem(library); if
	 * (null != oldItem.getShareToSchools()) { String schoolName[] =
	 * oldItem.getShareToSchools().split(","); for (int i = 0; i <
	 * schoolName.length; i++) { String apiUrl = serverURL +
	 * schoolName[i].replace(" ", "") + "/api/updateLibraryFolderToShareContent";
	 * logger.info("apiUrl---->" + apiUrl);
	 * 
	 * try { ObjectMapper Objmapper = new ObjectMapper(); String libJson =
	 * Objmapper.writeValueAsString(oldItem); // String updatedItem =
	 * Objmapper.writeValueAsString(library); WebTarget webTarget =
	 * client.target(URIUtil.encodeQuery(apiUrl)); Invocation.Builder
	 * invocationBuilder = webTarget.request();
	 * 
	 * Response response = invocationBuilder .post(Entity.entity(libJson.toString(),
	 * MediaType.APPLICATION_JSON)); logger.info("response---->" + response); }
	 * catch (Exception e) { // TODO Auto-generated catch block
	 * logger.error("Exception", e); } } } setSuccess(redirectAttrs,
	 * "Folder Details updated successfully");
	 * redirectAttrs.addFlashAttribute("library", redLibrary);
	 * 
	 * } catch (Exception e) {
	 * 
	 * logger.error(e.getMessage(), e); setError(redirectAttrs,
	 * "Error in creating folder"); redirectAttrs.addFlashAttribute("edit", "true");
	 * return "redirect:/addLibraryItemForm"; }
	 * 
	 * 
	 * if (parentId != null && !folderPath.isEmpty()) { return
	 * "redirect:/viewLibrary?parentId=" + parentId + "&folderPath=" +
	 * library.getFolderPath(); } else { return "redirect:/viewLibrary"; }
	 * 
	 * return "redirect:/viewLibraryAnnouncements"; }
	 */

	// Hiren 27-11-2019
//	@RequestMapping(value = "/updateLibraryFolder", method = { RequestMethod.GET, RequestMethod.POST })
//	public String updateLibraryFolder(@ModelAttribute Library library, RedirectAttributes redirectAttrs,
//			Principal principal, Model m) {
//		String username = principal.getName();
//		redirectAttrs.addFlashAttribute("library", library);
//		Long parentId = library.getParentId();
//		String folderPath = library.getFolderPath();
//		try {
//
//			Token userdetails1 = (Token) principal;
//			String ProgramName = userdetails1.getProgramName();
//			User u = userService.findByUserName(username);
//
//			String acadSession = u.getAcadSession();
//
//			m.addAttribute("Program_Name", ProgramName);
//			m.addAttribute("AcadSession", acadSession);
//			performFolderPathCheck(library);
//			File f = new File(library.getFolderPath());
//			String newFileParent = f.getParent();
//			logger.info("Update Library get Folder Path" + library.getFolderPath());
//			newFileParent = newFileParent.replaceAll("\\\\", "/");
//			logger.info("newFileParent" + newFileParent);
//			File parentFile = new File(newFileParent);
//
//			String completFolderPath = newFileParent + "/" + library.getContentName();
//			// String completFolderPath = library.getFolderPath() + "/" +
//			// library.getContentName();
//			library.setLastModifiedBy(username);
//			logger.info("newFileParent--------------->" + newFileParent);
//			library.setFilePath(completFolderPath);
//
//			Library libDB = libraryService.findByID(library.getId());
//
//			Library oldItem = libraryService.findByID(library.getId());
//
//			if (!s3Service.exists(completFolderPath)) {
//				String downloadDirTemp = downloadAllFolder + "/" + library.getContentName();
//				File downloadFolder = new File(downloadAllFolder + "/" + library.getContentName());
//				String sourcePath = downloadDirTemp + "/" + libDB.getFilePath();
//				logger.info("source path dire " + sourcePath);
//				if (!downloadFolder.exists()) {
//					downloadFolder.mkdir();
//					logger.info("created-->");
//				}
//				// setnew folder name
//				String newFolderName = library.getContentName();
//
//				File existingFolder = new File(oldItem.getFilePath());
//				File newFolder = new File(existingFolder.getParent() + FILE_SEPARATOR + newFolderName);
//				logger.info("old--->" + oldItem.getFilePath());
//				logger.info("new--->" + completFolderPath);
//
//				boolean renamed = s3Service.createFolder(library.getContentName(), libraryRootFolder);
//				logger.info("downloadDirTemp----------->" + downloadDirTemp);
//
//				boolean download = s3Service.downloadDir(libDB.getFolderPath(), downloadDirTemp);
//
//				logger.info("renamed---" + renamed);
//				logger.info("renamed Len---" + downloadFolder.listFiles().length);
//				boolean upload = false;
//				if (download) {
//					if (renamed) {
//						if (downloadFolder.listFiles().length > 0) {
//							File fileSouce = new File(downloadFolder.getAbsolutePath() + "/" + oldItem.getFilePath());
//							logger.info("completFolderPath00000--------->" + downloadDirTemp + " "
//									+ oldItem.getFilePath() + " " + completFolderPath);
//							upload = s3Service.uploadLibDir(fileSouce, completFolderPath, true);
//						} else {
//							upload = true;
//						}
//					}
//
//				}
//				if (!upload) {
//					setError(redirectAttrs, "Error in Renaming folder");
//					redirectAttrs.addFlashAttribute("edit", "true");
//					return "redirect:/addContentForm";
//				} else {
//					// amazonS3ClientService.deleteFileFromS3Bucket(oldContent.getFilePath().substring(0,oldContent.getFilePath().length()
//					// - 1));
//					s3Service.deleteDir(oldItem.getFilePath());
//					libDB.setFilePath(completFolderPath);
//					setSuccess(redirectAttrs, "Updated SuccessFully");
//					libraryService.updateChildrenDetails(library.getFilePath(), oldItem.getFilePath());
//				}
//				FileUtils.deleteDirectory(downloadFolder);
//
//			} else {
//				if (!library.getContentName().equals(oldItem.getContentName())) {
//					setError(redirectAttrs, "Folder Already Exists with name " + library.getContentName());
//					redirectAttrs.addFlashAttribute("edit", "true");
//					return "redirect:/addContentForm";
//				}
//			}
//			libraryService.updateFolder(library);
//
//			setSuccess(redirectAttrs, "Folder Details updated successfully");
//
//		} catch (Exception e) {
//
//			logger.error(e.getMessage(), e);
//			setError(redirectAttrs, "Error in creating folder");
//			redirectAttrs.addFlashAttribute("edit", "true");
//			return "redirect:/addLibraryItemForm";
//		}
//
//		/*
//		 * if (parentId != null && !folderPath.isEmpty()) { return
//		 * "redirect:/viewLibrary?parentId=" + parentId + "&folderPath=" +
//		 * library.getFolderPath(); } else { return "redirect:/viewLibrary"; }
//		 */
//		return "redirect:/viewLibraryAnnouncements";
//	}
	
	@Secured({ "ROLE_LIBRARIAN", "ROLE_FACULTY" })
	@RequestMapping(value = "/updateLibraryFolder", method = { RequestMethod.GET, RequestMethod.POST })
	public String updateLibraryFolder(@ModelAttribute Library library, RedirectAttributes redirectAttrs,
			Principal principal, Model m) {
		String username = principal.getName();
		redirectAttrs.addFlashAttribute("library", library);
		Long parentId = library.getParentId();
		String folderPath = library.getFolderPath();
		try {

			Token userdetails1 = (Token) principal;
			String ProgramName = userdetails1.getProgramName();
			User u = userService.findByUserName(username);

			String acadSession = u.getAcadSession();

			m.addAttribute("Program_Name", ProgramName);
			m.addAttribute("AcadSession", acadSession);
			performFolderPathCheck(library);
			File f = new File(library.getFolderPath());
			String newFileParent = f.getParent();
			newFileParent = newFileParent.replaceAll("\\\\", "/");
			//File parentFile = new File(newFileParent);

			String completeFolderPath = newFileParent + "/" + library.getContentName();
			library.setLastModifiedBy(username);
			library.setFilePath(completeFolderPath);
			//12-12-2020
			library.setFolderPath(completeFolderPath);
			Library libDB = libraryService.findByID(library.getId());

			Library oldItem = libraryService.findByID(library.getId());

			if (!s3Service.exists(completeFolderPath)) {
				String downloadDirTemp = downloadAllFolder + "/" + library.getContentName();
				File downloadFolder = new File(downloadAllFolder + "/" + library.getContentName());
				
				
				if (!downloadFolder.exists()) {
					downloadFolder.mkdir();
				}
				String newFolderName = library.getContentName();

				File existingFolder = new File(oldItem.getFilePath());
				File newFolder = new File(existingFolder.getParent() + FILE_SEPARATOR + newFolderName);
				String folderPathToDownload = libDB.getFolderPath();
				boolean renamed = s3Service.createFolder(library.getContentName(), newFileParent);
				if(libDB.getFolderPath().startsWith("/")) {
					folderPathToDownload = folderPathToDownload.substring(1, folderPathToDownload.length());
				}
				boolean download = s3Service.downloadDir(folderPathToDownload, downloadDirTemp);

				boolean upload = false;
				String oldFilePath = oldItem.getFilePath();
				if(oldFilePath.startsWith("/")) {
					oldFilePath = oldFilePath.substring(1, oldFilePath.length());
				}
				if (download) {
					if(renamed) {
						if (downloadFolder.listFiles().length > 0) {
							
							File fileSouce = new File(downloadFolder.getAbsolutePath() + "/" + oldFilePath);
							upload = s3Service.uploadLibDir(fileSouce, completeFolderPath, true);
						} else {
							upload = true;
						}
					}
				}
				logger.info("upload---->"+upload);
				if (!upload) {
					setError(redirectAttrs, "Error in Renaming folder");
					redirectAttrs.addFlashAttribute("edit", "true");
					return "redirect:/addLibraryItemForm";
				} else {
					logger.info("Delete---->"+oldFilePath);
					s3Service.deleteDir(oldFilePath);
					libDB.setFilePath(completeFolderPath);
					setSuccess(redirectAttrs, "Updated SuccessFully");
					//12-12-2020
					String newFolderPath = library.getFilePath();
					if(newFolderPath.startsWith("/")) {
						newFolderPath = newFolderPath.substring(1, newFolderPath.length());
					}
					//libraryService.updateChildrenDetails(library.getFilePath(), oldFilePath);
					libraryService.updateChildrenDetails(newFolderPath, oldItem.getFilePath(),oldFilePath);
				}
				//12-12-2020
				//FileUtils.deleteDirectory(downloadFolder);

			} else {
				if (!library.getContentName().equals(oldItem.getContentName())) {
					setError(redirectAttrs, "Folder Already Exists with name " + library.getContentName());
					redirectAttrs.addFlashAttribute("edit", "true");
					return "redirect:/addLibraryItemForm";
				}
			}
			libraryService.updateFolder(library);

			setSuccess(redirectAttrs, "Folder Details updated successfully");

		} catch (Exception e) {

			logger.error(e.getMessage(), e);
			setError(redirectAttrs, "Error in creating folder");
			redirectAttrs.addFlashAttribute("edit", "true");
			return "redirect:/addLibraryItemForm";
		}
		return "redirect:/viewLibraryAnnouncements";
	}

	private void performFolderPathCheck(Library library) {
		if (library.getFolderPath() == null || "".equals(library.getFolderPath())) {
			// If path is not present, start with Course Root Folder
			library.setFolderPath(libraryRootFolder);
		}

		if (!library.getFolderPath().endsWith(FILE_SEPARATOR)) {
			library.setFolderPath(library.getFolderPath());
		}

	}

	/*
	 * private void performFolderPathCheck(Library library) { if
	 * (library.getFolderPath() == null || "".equals(library.getFolderPath())) { //
	 * If path is not present, start with Course Root Folder
	 * library.setFolderPath(libraryRootFolder); }
	 * 
	 * if (!library.getFolderPath().endsWith(FILE_SEPARATOR)) {
	 * library.setFolderPath(library.getFolderPath() + FILE_SEPARATOR); }
	 * 
	 * }
	 */

	@Secured({ "ROLE_LIBRARIAN", "ROLE_FACULTY", "ROLE_ADMIN", "ROLE_STUDENT" })
	@RequestMapping(value = "/viewLibrary", method = { RequestMethod.GET, RequestMethod.POST })
	public String viewLibrary(@ModelAttribute Library library, Model m, RedirectAttributes redirectAttributes,
			Principal p) {

		m.addAttribute("webPage", new WebPage("library", "Library", true, false));
		m.addAttribute("library", library);
		String username = p.getName();
		if (null != library.getCreatedBy()) {
			logger.info("CreateBy1--->" + library.getCreatedBy());
			m.addAttribute("createdBy", library.getCreatedBy());
		}
		if (null != library.getCreateOnly()) {
			logger.info("Create1--->" + library.getCreateOnly());
			m.addAttribute("createOnly", library.getCreateOnly());
		}
		if (null != library.getEditOnly()) {
			logger.info("Edit1--->" + library.getEditOnly());
			m.addAttribute("editOnly", library.getEditOnly());
		}
		Token userdetails1 = (Token) p;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		if (library.getFolderPath() == null) {
			// If path is not present
			library.setFolderPath(libraryRootFolder);
		}
		try {
			if (library.getFolderPath().endsWith("/")) {

				library.setFolderPath(library.getFolderPath().substring(0, library.getFolderPath().length() - 1));
			}

			// List<Library> allItems =
			// libraryService.getItemsUnderAPath(library);
			// List<Library> allItems = libraryService.getLibraryUnderAPath(library);
			logger.info("URL--->" + serverCrudURL + "getSchoolsListForLibrary");
			WebTarget webTarget = client.target(URIUtil.encodeQuery(serverCrudURL + "getSchoolsListForLibrary"));

			Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);

			String resp = invocationBuilder.get(String.class);
			logger.info("resp School List--->" + resp);
			List<Map<String, Object>> schoolMap = mapper.readValue(resp,
					new TypeReference<List<Map<String, Object>>>() {
					});

			m.addAttribute("schoolListMap", schoolMap);
			m.addAttribute("appName", appName.trim());
			List<Library> allItems = libraryService.getLibraryUnderAPath(library, username,
					String.valueOf(library.getParentId()));
			if (allItems == null || allItems.size() == 0) {
				m.addAttribute("No Content available under this Folder");
				m.addAttribute("size", 0);
			} else {

				/*
				 * for(Library l:allItems){ //List<String> strlst = new ArrayList<String>();
				 * List<Library> librarianList1 =
				 * libraryService.getLibraryContentUserRights(l.getId(),username);
				 * l.setLibrarianList(librarianList1); //librarianList.addAll(librarianList1); }
				 */
				m.addAttribute("allContent", allItems);
				m.addAttribute("size", allItems.size());
			}

			String str = StringUtils.remove(library.getFolderPath(), libraryRootFolder);

			String strarray[] = str.split("/");
			List<Library> fList = libraryService.getParentFolder(library, username);

			// fList.add(folder);

			m.addAttribute("fList", fList);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(m, "Error in getting content");
		}
		m.addAttribute("content", library);
		if (userdetails1.getAuthorities().contains(Role.ROLE_FACULTY)) {
			return "library/libraryItemListFaculty";
		} else {
			return "library/libraryItemList";
		}
	}

	@Secured({ "ROLE_LIBRARIAN", "ROLE_FACULTY" })
	@RequestMapping(value = "/viewLibraryForCopy", method = { RequestMethod.GET, RequestMethod.POST })
	public String viewLibraryForCopy(@ModelAttribute Library library, Model m, Principal p) {

		m.addAttribute("webPage", new WebPage("library", "Library", true, false));
		m.addAttribute("library", library);
		String username = p.getName();

		Token userdetails1 = (Token) p;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		if (library.getFolderPath() == null) {
			// If path is not present
			library.setFolderPath(libraryRootFolder);
		}
		try {
			if (library.getFolderPath().endsWith("/")) {

				library.setFolderPath(library.getFolderPath().substring(0, library.getFolderPath().length() - 1));
			}

			List<Library> allItems = libraryService.getFoldersUnderAPath(library);
			if (allItems == null || allItems.size() == 0) {
				m.addAttribute("No Content available under this Folder");
				m.addAttribute("size", 0);
			} else {

				m.addAttribute("allContent", allItems);
				m.addAttribute("size", allItems.size());
			}
			String str = StringUtils.remove(library.getFolderPath(), libraryRootFolder);

			String strarray[] = str.split("/");
			List<Library> fList = libraryService.getParentFolder(library);

			// fList.add(folder);

			m.addAttribute("fList", fList);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(m, "Error in getting content");
		}
		m.addAttribute("content", library);
		return "library/libraryItems";
	}

	public Library findParentFolder(Library library) {
		return library;

	}

	/*
	 * @RequestMapping(value = "/addLibraryFile", method = { RequestMethod.GET,
	 * RequestMethod.POST }) public String addLibraryFile(@ModelAttribute Library
	 * library, Principal principal,
	 * 
	 * @RequestParam("file") List<MultipartFile> files, RedirectAttributes
	 * redirectAttrs, Model m) { String username = principal.getName();
	 * 
	 * Token userdetails1 = (Token) principal; String ProgramName =
	 * userdetails1.getProgramName(); User u = userService.findByUserName(username);
	 * 
	 * String acadSession = u.getAcadSession();
	 * 
	 * m.addAttribute("Program_Name", ProgramName); m.addAttribute("AcadSession",
	 * acadSession);
	 * 
	 * redirectAttrs.addFlashAttribute("library", library);
	 * 
	 * Long parentId = library.getParentId(); String folderPath =
	 * library.getFolderPath();
	 * 
	 * try {
	 * 
	 * performFolderPathCheck(library); for (MultipartFile file : files) { if
	 * (!file.isEmpty()) {
	 * 
	 * String errorMessage = uploadLibraryFile(library, library.getFolderPath(),
	 * file);
	 * 
	 * if (errorMessage == null) {
	 * 
	 * // library.setContentName(file.getName()); library.setCreatedBy(username);
	 * library.setLastModifiedBy(username);
	 * 
	 * libraryService.insertWithIdReturn(library); setSuccess(redirectAttrs,
	 * "File Created Successfully"); } else { setError(redirectAttrs,
	 * "Error in uploading file " + errorMessage); return
	 * "redirect:/addLibraryItemForm"; } } if (file == null || file.isEmpty()) {
	 * setError(redirectAttrs, "No file selected. Please select a file to upload.");
	 * return "redirect:/addLibraryItemForm"; } }
	 * 
	 * } catch (Exception e) { logger.error(e.getMessage(), e);
	 * setError(redirectAttrs, "Error in creating file"); return
	 * "redirect:/addLibraryItemForm"; } if (parentId != null &&
	 * !folderPath.isEmpty()) { return "redirect:/viewLibrary?parentId=" + parentId
	 * + "&folderPath=" + folderPath; } else { return "redirect:/viewLibrary"; } }
	 */
	// Hiren 29-11-2019

	/*
	 * @RequestMapping(value = "/addLibraryFile", method = { RequestMethod.GET,
	 * RequestMethod.POST }) public String addLibraryFile(@ModelAttribute Library
	 * library, Principal principal,
	 * 
	 * @RequestParam("file") List<MultipartFile> files, RedirectAttributes
	 * redirectAttrs, Model m) { String username = principal.getName();
	 * 
	 * Token userdetails1 = (Token) principal; String ProgramName =
	 * userdetails1.getProgramName(); User u = userService.findByUserName(username);
	 * 
	 * String acadSession = u.getAcadSession();
	 * 
	 * m.addAttribute("Program_Name", ProgramName); m.addAttribute("AcadSession",
	 * acadSession);
	 * 
	 * redirectAttrs.addFlashAttribute("library", library);
	 * 
	 * Long parentId = library.getParentId(); String folderPath =
	 * library.getFolderPath(); Library pId = libraryService.findByID(parentId); try
	 * {
	 * 
	 * performFolderPathCheck(library); for (MultipartFile file : files) { if
	 * (!file.isEmpty()) {
	 * 
	 * String errorMessage = uploadLibraryFile(library, library.getFolderPath(),
	 * file);
	 * 
	 * if (errorMessage == null) {
	 * 
	 * // library.setContentName(file.getName()); library.setCreatedBy(username);
	 * library.setLastModifiedBy(username); if (null != parentId && null !=
	 * pId.getShareToSchools()) {
	 * library.setShareToSchools(pId.getShareToSchools()); }
	 * 
	 * libraryService.insertWithIdReturn(library); // Library pId =
	 * libraryService.findByID(parentId); //
	 * logger.info("getShareToSchools---->"+pId.getShareToSchools()); if (null !=
	 * parentId && null != pId.getShareToSchools()) {
	 * library.setShareToSchools(null); String schoolName[] =
	 * pId.getShareToSchools().split(","); for (int i = 0; i < schoolName.length;
	 * i++) { String apiUrl = serverURL + schoolName[i].replace(" ", "") +
	 * "/api/addLibraryFileToShareContent"; logger.info("apiUrl---->" + apiUrl);
	 * 
	 * try { ObjectMapper Objmapper = new ObjectMapper(); String libJson =
	 * Objmapper.writeValueAsString(library); WebTarget webTarget =
	 * client.target(URIUtil.encodeQuery(apiUrl)); Invocation.Builder
	 * invocationBuilder = webTarget.request();
	 * 
	 * Response response = invocationBuilder .post(Entity.entity(libJson.toString(),
	 * MediaType.APPLICATION_JSON)); logger.info("response---->" + response); }
	 * catch (Exception e) { // TODO Auto-generated catch block
	 * logger.error("Exception", e); } } } List<Library> givenRights =
	 * libraryService .getLibraryContentRightsById(String.valueOf(parentId)); if
	 * (givenRights.size() > 0) {
	 * giveLibrarianRights(String.valueOf(library.getId()),
	 * String.valueOf(parentId), principal); } setSuccess(redirectAttrs,
	 * "File Created Successfully"); } else { setError(redirectAttrs,
	 * "Error in uploading file " + errorMessage); return
	 * "redirect:/addLibraryItemForm"; } } if (file == null || file.isEmpty()) {
	 * setError(redirectAttrs, "No file selected. Please select a file to upload.");
	 * return "redirect:/addLibraryItemForm"; } }
	 * 
	 * } catch (Exception e) { logger.error(e.getMessage(), e);
	 * setError(redirectAttrs, "Error in creating file"); return
	 * "redirect:/addLibraryItemForm"; } if (parentId != null &&
	 * !folderPath.isEmpty()) { return "redirect:/viewLibrary?parentId=" + parentId
	 * + "&folderPath=" + folderPath + "&createdBy=" + username +
	 * "&createOnly=&editOnly="; } else { return
	 * "redirect:/viewLibraryAnnouncements"; } }
	 */

	@Secured({ "ROLE_LIBRARIAN", "ROLE_FACULTY" })
	@RequestMapping(value = "/addLibraryFile", method = { RequestMethod.GET, RequestMethod.POST })
	public String addLibraryFile(@ModelAttribute Library library, Principal principal,
			@RequestParam("file") List<MultipartFile> files, RedirectAttributes redirectAttrs, Model m) {
		String username = principal.getName();
		
		try {
			if(!library.getContentType().isEmpty()) {
				BusinessBypassRule.validateAlphaNumeric(library.getContentType());
			}
			if(library.getParentId() != null) {
				BusinessBypassRule.validateNumeric(library.getParentId());
			}
			if(!library.getContentDescription().isEmpty()) {
				BusinessBypassRule.validateAlphaNumeric(library.getContentDescription());
			}
		} catch (ValidationException ve) {
			ve.printStackTrace();
			setError(redirectAttrs,ve.getMessage());
			return "redirect:/viewLibraryAnnouncements";
		}

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		redirectAttrs.addFlashAttribute("library", library);

		Long parentId = library.getParentId();
		String folderPath = library.getFolderPath();
		Library pId = libraryService.findByID(parentId);
		logger.info("library folder path is--------------------" + library.getFolderPath());
		try {

			performFolderPathCheck(library);

			for (MultipartFile file : files) {
				if (!file.isEmpty()) {
					//Audit change start
					String errorMessage = "";
					Tika tika = new Tika();
					  String detectedType = tika.detect(file.getBytes());
					if (file.getOriginalFilename().contains(".")) {
						Long count = file.getOriginalFilename().chars().filter(c -> c == ('.')).count();
						logger.info("length--->"+count);
						if (count > 1 || count == 0) {
							setError(redirectAttrs, "File uploaded is invalid!");
							return "redirect:/addLibraryItemForm";
						}else {
							String extension = FilenameUtils.getExtension(file.getOriginalFilename());
							logger.info("extension--->"+extension);
							if(extension.equalsIgnoreCase("exe") || ("application/x-msdownload").equals(detectedType) || ("application/x-sh").equals(detectedType)) {
								setError(redirectAttrs, "File uploaded is invalid!");
								return "redirect:/addLibraryItemForm";
							}else {
								byte [] byteArr=file.getBytes();
								if((Byte.toUnsignedInt(byteArr[0]) == 0xFF && Byte.toUnsignedInt(byteArr[1]) == 0xD8) || 
																	(Byte.toUnsignedInt(byteArr[0]) == 0x89 && Byte.toUnsignedInt(byteArr[1]) == 0x50) || 
																	(Byte.toUnsignedInt(byteArr[0]) == 0x25 && Byte.toUnsignedInt(byteArr[1]) == 0x50) || 
																	(Byte.toUnsignedInt(byteArr[0]) == 0x42 && Byte.toUnsignedInt(byteArr[1]) == 0x4D) || 
																	(Byte.toUnsignedInt(byteArr[0]) == 0x47 && Byte.toUnsignedInt(byteArr[1]) == 0x49) || 
																	(Byte.toUnsignedInt(byteArr[0]) == 0x49 && Byte.toUnsignedInt(byteArr[1]) == 0x49) || 
																	(Byte.toUnsignedInt(byteArr[0]) == 0x38 && Byte.toUnsignedInt(byteArr[1]) == 0x42) || 
																	(Byte.toUnsignedInt(byteArr[0]) == 0x50 && Byte.toUnsignedInt(byteArr[1]) == 0x4B) || 
																	(Byte.toUnsignedInt(byteArr[0]) == 0x1F && Byte.toUnsignedInt(byteArr[1]) == 0x8B) || 
																	(Byte.toUnsignedInt(byteArr[0]) == 0x75 && Byte.toUnsignedInt(byteArr[1]) == 0x73) || 
																	(Byte.toUnsignedInt(byteArr[0]) == 0x52 && Byte.toUnsignedInt(byteArr[1]) == 0x61) || 
																	(Byte.toUnsignedInt(byteArr[0]) == 0xD0 && Byte.toUnsignedInt(byteArr[1]) == 0xCF) || 
																	(Byte.toUnsignedInt(byteArr[0]) == 0x50 && Byte.toUnsignedInt(byteArr[1]) == 0x4B) || 
																	("text/plain").equals(detectedType)) {
								errorMessage = uploadLibraryFile(library, library.getFolderPath(), file);
								} else {
									setError(redirectAttrs, "File uploaded is invalid!");
									return "redirect:/addLibraryItemForm";
								}
							}
						}
					}else {
						setError(redirectAttrs, "File uploaded is invalid!");
						return "redirect:/addLibraryItemForm";
					}
					//Audit change end

					if (errorMessage == null) {

						// library.setContentName(file.getName());
						library.setCreatedBy(username);
						library.setLastModifiedBy(username);
						if (null != parentId && null != pId.getShareToSchools()) {
							library.setShareToSchools(pId.getShareToSchools());
						}

						libraryService.insertWithIdReturn(library);
						// Library pId = libraryService.findByID(parentId);
						// logger.info("getShareToSchools---->"+pId.getShareToSchools());
						if (null != parentId && null != pId.getShareToSchools()) {
							library.setShareToSchools(null);
							String schoolName[] = pId.getShareToSchools().split(",");
							for (int i = 0; i < schoolName.length; i++) {
								String apiUrl = serverURL + schoolName[i].replace(" ", "")
										+ "/api/addLibraryFileToShareContent";
								logger.info("apiUrl---->" + apiUrl);

								try {
									ObjectMapper Objmapper = new ObjectMapper();
									String libJson = Objmapper.writeValueAsString(library);
									WebTarget webTarget = client.target(URIUtil.encodeQuery(apiUrl));
									Invocation.Builder invocationBuilder = webTarget.request();

									Response response = invocationBuilder
											.post(Entity.entity(libJson.toString(), MediaType.APPLICATION_JSON));
									logger.info("response---->" + response);
								} catch (Exception e) {
									// TODO Auto-generated catch block
									logger.error("Exception", e);
								}
							}
						}
						List<Library> givenRights = libraryService
								.getLibraryContentRightsById(String.valueOf(parentId));
						if (givenRights.size() > 0) {
							giveLibrarianRights(String.valueOf(library.getId()), String.valueOf(parentId), principal);
						}
						setSuccess(redirectAttrs, "File Created Successfully");
					} else {
						setError(redirectAttrs, "Error in uploading file " + errorMessage);
						return "redirect:/addLibraryItemForm";
					}
				}
				if (file == null || file.isEmpty()) {
					setError(redirectAttrs, "No file selected. Please select a file to upload.");
					return "redirect:/addLibraryItemForm";
				}
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(redirectAttrs, "Error in creating file");
			return "redirect:/addLibraryItemForm";
		}
		if (parentId != null && !folderPath.isEmpty()) {
			return "redirect:/viewLibrary?parentId=" + parentId + "&folderPath=" + folderPath + "&createdBy=" + username
					+ "&createOnly=&editOnly=";
		} else {
			return "redirect:/viewLibraryAnnouncements";
		}
	}

	/*
	 * @RequestMapping(value = "/updateLibraryFile", method = { RequestMethod.GET,
	 * RequestMethod.POST }) public String updateLibraryFile(@ModelAttribute Library
	 * library, Principal principal,
	 * 
	 * @RequestParam("file") MultipartFile file, RedirectAttributes redirectAttrs) {
	 * String username = principal.getName();
	 * redirectAttrs.addFlashAttribute("content", library);
	 * 
	 * Long parentId = library.getParentId(); String folderPath =
	 * library.getFolderPath();
	 * 
	 * Library data = libraryService.findByID(library.getId()); String
	 * libraryFileName = data.getContentName();
	 * 
	 * if (libraryFileName != null) if
	 * (library.getFolderPath().endsWith(libraryFileName)) {
	 * 
	 * String newFolderPath = library.getFolderPath().substring(0,
	 * library.getFolderPath().indexOf(libraryFileName));
	 * 
	 * library.setFolderPath(newFolderPath); } try {
	 * performFolderPathCheck(library);
	 * 
	 * if (file != null && !file.isEmpty()) { String errorMessage =
	 * uploadLibraryFile(library, library.getFolderPath(), file);
	 * 
	 * logger.info("errorMessage -- " + errorMessage); if (errorMessage != null) {
	 * setError(redirectAttrs, "Error in uploading file " + errorMessage);
	 * redirectAttrs.addFlashAttribute("edit", "true"); return
	 * "redirect:/addLibraryItemForm"; } } library.setLastModifiedBy(username); if
	 * (library.getContentName() == null) {
	 * library.setContentName(data.getContentName()); }
	 * 
	 * libraryService.updateFile(library); data.setNewLibraryItem(library); if (null
	 * != data.getShareToSchools()) { String schoolName[] =
	 * data.getShareToSchools().split(","); for (int i = 0; i < schoolName.length;
	 * i++) { String apiUrl = serverURL + schoolName[i].replace(" ", "") +
	 * "/api/updateLibraryFileToShareContent"; logger.info("apiUrl---->" + apiUrl);
	 * 
	 * try { ObjectMapper Objmapper = new ObjectMapper(); String libJson =
	 * Objmapper.writeValueAsString(data); WebTarget webTarget =
	 * client.target(URIUtil.encodeQuery(apiUrl)); Invocation.Builder
	 * invocationBuilder = webTarget.request();
	 * 
	 * Response response = invocationBuilder .post(Entity.entity(libJson.toString(),
	 * MediaType.APPLICATION_JSON)); logger.info("response---->" + response); }
	 * catch (Exception e) { // TODO Auto-generated catch block
	 * logger.error("Exception", e); } } } setSuccess(redirectAttrs,
	 * "File Updated Successfully");
	 * 
	 * } catch (Exception e) { logger.error(e.getMessage(), e);
	 * setError(redirectAttrs, "Error in updating file");
	 * redirectAttrs.addFlashAttribute("edit", "true"); return
	 * "redirect:/addLibraryItemForm"; }
	 * 
	 * if (parentId != null && !folderPath.isEmpty()) { return
	 * "redirect:/viewLibrary?parentId=" + parentId + "&folderPath=" + folderPath; }
	 * else { return "redirect:/viewLibrary"; }
	 * 
	 * redirectAttrs.addFlashAttribute("library", library); return
	 * "redirect:/viewLibrary"; }
	 */

	@Secured({ "ROLE_LIBRARIAN", "ROLE_FACULTY" })
	@RequestMapping(value = "/updateLibraryFile", method = { RequestMethod.GET, RequestMethod.POST })
	public String updateLibraryFile(@ModelAttribute Library library, Principal principal,
			@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttrs) {
		String username = principal.getName();
		redirectAttrs.addFlashAttribute("content", library);

		Long parentId = library.getParentId();
		String folderPath = library.getFolderPath();

		Library data = libraryService.findByID(library.getId());
		String libraryFileName = data.getContentName();

		if (libraryFileName != null)
			if (library.getFolderPath().endsWith(libraryFileName)) {

				String newFolderPath = library.getFolderPath().substring(0,
						library.getFolderPath().indexOf(libraryFileName));

				library.setFolderPath(newFolderPath);
			}
		try {
			performFolderPathCheck(library);

			if (file != null && !file.isEmpty()) {
				//Audit change start
				String errorMessage = "";
				Tika tika = new Tika();
				  String detectedType = tika.detect(file.getBytes());
				if (file.getOriginalFilename().contains(".")) {
					Long count = file.getOriginalFilename().chars().filter(c -> c == ('.')).count();
					logger.info("length--->"+count);
					if (count > 1 || count == 0) {
						setError(redirectAttrs, "File uploaded is invalid!");
						redirectAttrs.addAttribute("id", library.getId());
						redirectAttrs.addAttribute("contentType", library.getContentType());
						redirectAttrs.addFlashAttribute("edit", "true");
						return "redirect:/addLibraryItemForm";
					}else {
						String extension = FilenameUtils.getExtension(file.getOriginalFilename());
						logger.info("extension--->"+extension);
						if(extension.equalsIgnoreCase("exe") || ("application/x-msdownload").equals(detectedType) || ("application/x-sh").equals(detectedType)) {
							setError(redirectAttrs, "File uploaded is invalid!");
							redirectAttrs.addAttribute("id", library.getId());
							redirectAttrs.addAttribute("contentType", library.getContentType());
							redirectAttrs.addFlashAttribute("edit", "true");
							return "redirect:/addLibraryItemForm";
						}else {
							byte [] byteArr=file.getBytes();
							if((Byte.toUnsignedInt(byteArr[0]) == 0xFF && Byte.toUnsignedInt(byteArr[1]) == 0xD8) || 
																(Byte.toUnsignedInt(byteArr[0]) == 0x89 && Byte.toUnsignedInt(byteArr[1]) == 0x50) || 
																(Byte.toUnsignedInt(byteArr[0]) == 0x25 && Byte.toUnsignedInt(byteArr[1]) == 0x50) || 
																(Byte.toUnsignedInt(byteArr[0]) == 0x42 && Byte.toUnsignedInt(byteArr[1]) == 0x4D) || 
																(Byte.toUnsignedInt(byteArr[0]) == 0x47 && Byte.toUnsignedInt(byteArr[1]) == 0x49) || 
																(Byte.toUnsignedInt(byteArr[0]) == 0x49 && Byte.toUnsignedInt(byteArr[1]) == 0x49) || 
																(Byte.toUnsignedInt(byteArr[0]) == 0x38 && Byte.toUnsignedInt(byteArr[1]) == 0x42) || 
																(Byte.toUnsignedInt(byteArr[0]) == 0x50 && Byte.toUnsignedInt(byteArr[1]) == 0x4B) || 
																(Byte.toUnsignedInt(byteArr[0]) == 0x1F && Byte.toUnsignedInt(byteArr[1]) == 0x8B) || 
																(Byte.toUnsignedInt(byteArr[0]) == 0x75 && Byte.toUnsignedInt(byteArr[1]) == 0x73) || 
																(Byte.toUnsignedInt(byteArr[0]) == 0x52 && Byte.toUnsignedInt(byteArr[1]) == 0x61) || 
																(Byte.toUnsignedInt(byteArr[0]) == 0xD0 && Byte.toUnsignedInt(byteArr[1]) == 0xCF) || 
																(Byte.toUnsignedInt(byteArr[0]) == 0x50 && Byte.toUnsignedInt(byteArr[1]) == 0x4B) || 
																("text/plain").equals(detectedType)) {
								errorMessage = uploadLibraryFile(library, library.getFolderPath(), file);
							} else {
								setError(redirectAttrs, "File uploaded is invalid!");
								redirectAttrs.addAttribute("id", library.getId());
								redirectAttrs.addAttribute("contentType", library.getContentType());
								redirectAttrs.addFlashAttribute("edit", "true");
								return "redirect:/addLibraryItemForm";
							}
						}
					}
				}else {
					setError(redirectAttrs, "File uploaded is invalid!");
					redirectAttrs.addAttribute("id", library.getId());
					redirectAttrs.addAttribute("contentType", library.getContentType());
					redirectAttrs.addFlashAttribute("edit", "true");
					return "redirect:/addLibraryItemForm";
				}
				//Audit change end
				logger.info("errorMessage -- " + errorMessage);
				if (errorMessage != null) {
					setError(redirectAttrs, "Error in uploading file " + errorMessage);
					redirectAttrs.addFlashAttribute("edit", "true");
					return "redirect:/addLibraryItemForm";
				}
			}
			library.setLastModifiedBy(username);
			if (library.getContentName() == null) {
				library.setContentName(data.getContentName());
			}

			libraryService.updateFile(library);
			data.setNewLibraryItem(library);
			if (null != data.getShareToSchools()) {
				String schoolName[] = data.getShareToSchools().split(",");
				for (int i = 0; i < schoolName.length; i++) {
					String apiUrl = serverURL + schoolName[i].replace(" ", "") + "/api/updateLibraryFileToShareContent";
					logger.info("apiUrl---->" + apiUrl);

					try {
						ObjectMapper Objmapper = new ObjectMapper();
						String libJson = Objmapper.writeValueAsString(data);
						WebTarget webTarget = client.target(URIUtil.encodeQuery(apiUrl));
						Invocation.Builder invocationBuilder = webTarget.request();

						Response response = invocationBuilder
								.post(Entity.entity(libJson.toString(), MediaType.APPLICATION_JSON));
						logger.info("response---->" + response);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logger.error("Exception", e);
					}
				}
			}
			setSuccess(redirectAttrs, "File Updated Successfully");

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(redirectAttrs, "Error in updating file");
			redirectAttrs.addFlashAttribute("edit", "true");
			return "redirect:/addLibraryItemForm";
		}
		/*
		 * if (parentId != null && !folderPath.isEmpty()) { return
		 * "redirect:/viewLibrary?parentId=" + parentId + "&folderPath=" + folderPath; }
		 * else { return "redirect:/viewLibrary"; }
		 */
		redirectAttrs.addFlashAttribute("library", library);
		return "redirect:/viewLibrary";
	}

	/*
	 * private String uploadLibraryFile(Library library, String folderPath,
	 * MultipartFile file) {
	 * 
	 * String errorMessage = null; InputStream inputStream = null; OutputStream
	 * outputStream = null;
	 * 
	 * // CommonsMultipartFile file = bean.getFileData(); String fileName =
	 * file.getOriginalFilename();
	 * 
	 * // Replace special characters in file fileName = fileName.replaceAll("'",
	 * "_"); fileName = fileName.replaceAll(",", "_"); fileName =
	 * fileName.replaceAll("&", "and"); fileName = fileName.replaceAll(" ", "_");
	 * 
	 * fileName = fileName.substring(0, fileName.lastIndexOf(".")) + "_" +
	 * RandomStringUtils.randomAlphanumeric(10) +
	 * fileName.substring(fileName.lastIndexOf("."), fileName.length());
	 * library.setContentName(file.getOriginalFilename());
	 * logger.info("folderPath--->" + folderPath); logger.info("fileName--->" +
	 * fileName); try {
	 * 
	 * inputStream = file.getInputStream(); String filePath = folderPath + fileName;
	 * logger.info("FilePath--------->" + filePath); // Check if Folder exists. If
	 * not then create File directory = new File(folderPath); if
	 * (!directory.exists()) { directory.mkdirs(); }
	 * 
	 * File newFile = new File(filePath);
	 * 
	 * outputStream = new FileOutputStream(newFile); int read = 0; byte[] bytes =
	 * new byte[1024];
	 * 
	 * while ((read = inputStream.read(bytes)) != -1) { outputStream.write(bytes, 0,
	 * read); } library.setFileName(fileName); library.setFilePath(filePath);
	 * outputStream.close(); inputStream.close(); } catch (IOException e) {
	 * errorMessage = "Error in uploading Content file : " + e.getMessage();
	 * 
	 * }
	 * 
	 * return errorMessage; }
	 */

	// ye s3 wala hai upload ka
	private String uploadLibraryFile(Library library, String folderPath, MultipartFile file) {
		// method copyn
		String errorMessage = null;
		InputStream inputStream = null;
		// old
		// CommonsMultipartFile file = bean.getFileData();
		String fileName = file.getOriginalFilename();

		library.setContentName(file.getOriginalFilename());
		logger.info("folderPath--->" + folderPath);
		logger.info("fileName--->" + fileName);
		try {

			inputStream = file.getInputStream();

			logger.info(folderPath + "recieved");
			logger.info(fileName + "recieved");
			String filePath = folderPath + fileName;
			logger.info("FilePath--------->" + filePath);
			// Check if Folder exists. If not then create
			/*
			 * File directory = new File(folderPath); if (!directory.exists()) {
			 * directory.mkdirs(); }
			 */
			
			if(folderPath.startsWith("/")) {
				folderPath = StringUtils.substring(folderPath, 1);
			}
			if(folderPath.endsWith("/")) {
				folderPath = folderPath.substring(0, folderPath.length() - 1);
			}

			logger.info("folderPath New --------->" + folderPath);


			Map<String, String> resultMap = s3Service.uploadFileToS3BucketWithRandomString(file, folderPath);
			// s3Service.uploadFileToS3Bucket(file, filePath);
			if (resultMap.containsKey("SUCCESS")) {

				library.setFileName(resultMap.get("SUCCESS"));
				library.setFilePath(folderPath + "/" + resultMap.get("SUCCESS"));
				//12-12-2020
				library.setFolderPath(folderPath+"/");
			}

			inputStream.close();
		} catch (IOException e) {
			errorMessage = "Error in uploading Content file : " + e.getMessage();

		}

		return errorMessage;
	}

	@Secured({ "ROLE_LIBRARIAN", "ROLE_FACULTY" })
	@RequestMapping(value = "/addLibraryLink", method = { RequestMethod.GET, RequestMethod.POST })
	public String addLibraryLink(@ModelAttribute Library library, RedirectAttributes redirectAttrs,
			Principal principal) {
		String username = principal.getName();
		
		redirectAttrs.addFlashAttribute("library", library);
		
		try {
			if(!library.getContentType().isEmpty()) {
				BusinessBypassRule.validateAlphaNumeric(library.getContentType());
			}
			if(library.getParentId() != null) {
				BusinessBypassRule.validateNumeric(library.getParentId());
			}
			BusinessBypassRule.validateAlphaNumeric(library.getContentName());
			if(!library.getContentDescription().isEmpty()) {
				BusinessBypassRule.validateAlphaNumeric(library.getContentDescription());
			}
			
			BusinessBypassRule.validateUrl(library.getLinkUrl());
		} catch (ValidationException ve) {
			ve.printStackTrace();
			setError(redirectAttrs,ve.getMessage());
			return "redirect:/viewLibraryAnnouncements";
		}

		Long parentId = library.getParentId();
		String folderPath = library.getFolderPath();
		try {

			performFolderPathCheck(library);

			library.setCreatedBy(username);
			library.setLastModifiedBy(username);
			Library pId = libraryService.findByID(parentId);
			if (null != parentId && null != pId.getShareToSchools()) {
				library.setShareToSchools(pId.getShareToSchools());
			}
			libraryService.insertWithIdReturn(library);
			if (null != parentId && null != pId.getShareToSchools()) {
				library.setShareToSchools(null);
				String schoolName[] = pId.getShareToSchools().split(",");
				for (int i = 0; i < schoolName.length; i++) {
					String apiUrl = serverURL + schoolName[i].replace(" ", "") + "/api/addLibraryLinkToShareContent";
					logger.info("apiUrl---->" + apiUrl);

					try {
						ObjectMapper Objmapper = new ObjectMapper();
						String libJson = Objmapper.writeValueAsString(library);
						WebTarget webTarget = client.target(URIUtil.encodeQuery(apiUrl));
						Invocation.Builder invocationBuilder = webTarget.request();
						Response response = invocationBuilder
								.post(Entity.entity(libJson.toString(), MediaType.APPLICATION_JSON));
						logger.info("response---->" + response);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logger.error("Exception", e);
					}
				}
			}
			List<Library> givenRights = libraryService.getLibraryContentRightsById(String.valueOf(parentId));
			if (givenRights.size() > 0) {
				giveLibrarianRights(String.valueOf(library.getId()), String.valueOf(parentId), principal);
			}
			setSuccess(redirectAttrs, "Link added Successfully");

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(redirectAttrs, "Error in creating Link");
			return "redirect:/addLibraryItemForm";
		}
		if (parentId != null && !folderPath.isEmpty()) {
			return "redirect:/viewLibrary?parentId=" + parentId + "&folderPath=" + folderPath + "&createdBy=" + username
					+ "&createOnly=&editOnly=";
		} else {
			return "redirect:/viewLibraryAnnouncements";
		}
	}

	@Secured({ "ROLE_LIBRARIAN", "ROLE_FACULTY" })
	@RequestMapping(value = "/updateLibraryLink", method = { RequestMethod.GET, RequestMethod.POST })
	public String updateLibraryLink(@ModelAttribute Library library, RedirectAttributes redirectAttrs,
			Principal principal) {
		String username = principal.getName();
		redirectAttrs.addFlashAttribute("library", library);

		Library oldItem = libraryService.findByID(library.getId());
		Long parentId = library.getParentId();
		String folderPath = library.getFolderPath();
		try {
			performFolderPathCheck(library);
			library.setLastModifiedBy(username);
			libraryService.updateLink(library);
			oldItem.setNewLibraryItem(library);
			if (null != oldItem.getShareToSchools()) {
				String schoolName[] = oldItem.getShareToSchools().split(",");
				for (int i = 0; i < schoolName.length; i++) {
					String apiUrl = serverURL + schoolName[i].replace(" ", "") + "/api/updateLibraryFileToShareContent";
					logger.info("apiUrl---->" + apiUrl);

					try {
						ObjectMapper Objmapper = new ObjectMapper();
						String libJson = Objmapper.writeValueAsString(oldItem);
						WebTarget webTarget = client.target(URIUtil.encodeQuery(apiUrl));
						Invocation.Builder invocationBuilder = webTarget.request();

						Response response = invocationBuilder
								.post(Entity.entity(libJson.toString(), MediaType.APPLICATION_JSON));
						logger.info("response---->" + response);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logger.error("Exception", e);
					}
				}
			}

			setSuccess(redirectAttrs, "Link updated Successfully");

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(redirectAttrs, "Error in updating Link");
			redirectAttrs.addFlashAttribute("edit", "true");
			return "redirect:/addLibraryItemForm";
		}
		/*
		 * if (parentId != null && !folderPath.isEmpty()) { return
		 * "redirect:/viewLibrary?parentId=" + parentId + "&folderPath=" + folderPath; }
		 * else { return "redirect:/viewLibrary"; }
		 */
		redirectAttrs.addFlashAttribute("library", library);
		return "redirect:/viewLibrary";
	}

	/*
	 * @RequestMapping(value = "/deleteLibraryItem", method = { RequestMethod.GET,
	 * RequestMethod.POST }) public String deleteLibraryItem(@ModelAttribute Library
	 * library, RedirectAttributes redirectAttrs) {
	 * 
	 * // redirectAttrs.addFlashAttribute("content", library);
	 * 
	 * Long parentId = library.getParentId(); String folderPath =
	 * library.getFolderPath(); try { Library oldItem =
	 * libraryService.findByID(library.getId()); library =
	 * libraryService.findByID(library.getId()); parentId = library.getParentId();
	 * folderPath = library.getFolderPath(); if
	 * (Library.FOLDER.equalsIgnoreCase(library.getContentType())) { // Mark Folder
	 * and its Children Inactive in DB libraryService.softDeleteFolder(library);
	 * 
	 * // Rename folder with _Deleted in the name File existingFolder = new
	 * File(library.getFilePath());
	 * 
	 * File newFolder = new File(library.getFilePath() + "_Deleted");
	 * existingFolder.renameTo(newFolder);
	 * 
	 * // FileUtils.deleteDirectory(existingFolder);
	 * 
	 * // Change Folder and FilePath of children as per Renamed Folder // Path
	 * libraryService.updateChildrenDetails(library.getFilePath() + "_Deleted",
	 * library.getFilePath());
	 * 
	 * // Change File Path of Folder to Renamed Folder
	 * library.setFilePath(library.getFilePath() + "_Deleted");
	 * libraryService.updateFolder(library); oldItem.setNewLibraryItem(library); if
	 * (null != library.getShareToSchools()) { String schoolName[] =
	 * library.getShareToSchools().split(","); logger.info("school Name Delete--->"
	 * + schoolName[0] + " length " + schoolName.length); for (int i = 0; i <
	 * schoolName.length; i++) { String apiUrl = serverURL +
	 * schoolName[i].replace(" ", "") + "/api/deleteItemToShareContent";
	 * logger.info("apiUrl---->" + apiUrl);
	 * 
	 * try { ObjectMapper Objmapper = new ObjectMapper(); String libJson =
	 * Objmapper.writeValueAsString(oldItem); WebTarget webTarget =
	 * client.target(URIUtil.encodeQuery(apiUrl)); Invocation.Builder
	 * invocationBuilder = webTarget.request(); Response response =
	 * invocationBuilder .post(Entity.entity(libJson.toString(),
	 * MediaType.APPLICATION_JSON)); logger.info("response---->" + response); }
	 * catch (Exception e) { // TODO Auto-generated catch block
	 * logger.error("Exception", e); } } }
	 * 
	 * } else if (Library.FILE.equalsIgnoreCase(library.getContentType())) { //
	 * FileUtils.deleteQuietly(new File(library.getFilePath()));
	 * 
	 * if (null != library.getShareToSchools()) { String schoolName[] =
	 * library.getShareToSchools().split(","); logger.info("school Name Delete--->"
	 * + schoolName[0] + " length " + schoolName.length); for (int i = 0; i <
	 * schoolName.length; i++) { String apiUrl = serverURL +
	 * schoolName[i].replace(" ", "") + "/api/deleteItemToShareContent";
	 * logger.info("apiUrl---->" + apiUrl);
	 * 
	 * try { ObjectMapper Objmapper = new ObjectMapper(); String libJson =
	 * Objmapper.writeValueAsString(oldItem); WebTarget webTarget =
	 * client.target(URIUtil.encodeQuery(apiUrl)); Invocation.Builder
	 * invocationBuilder = webTarget.request(); Response response =
	 * invocationBuilder .post(Entity.entity(libJson.toString(),
	 * MediaType.APPLICATION_JSON)); logger.info("response---->" + response); }
	 * catch (Exception e) { // TODO Auto-generated catch block
	 * logger.error("Exception", e); } } } String libraryFolderPath =
	 * libraryRootFolder + library.getLastModifiedDate().toString().split(" ")[0] +
	 * "_DeletedFiles"; // libraryFolderPath = libraryFolderPath.split(" ")[0]; File
	 * file = new File(libraryFolderPath); logger.info("libraryFolderPath--->" +
	 * libraryFolderPath); if (!file.exists()) { // file.mkdirs(); Boolean created =
	 * file.mkdirs(); logger.info("created--->" + created); } File srcFolder = new
	 * File(library.getFilePath()); File destFolder = new File(libraryFolderPath);
	 * logger.info("srcFolder--->" + srcFolder.getAbsolutePath());
	 * logger.info("fileName--->" + srcFolder.getName()); // FileInputStream input =
	 * new FileInputStream(srcFolder); // MultipartFile multipartFile = new //
	 * MockMultipartFile("file",srcFolder.getName(), "text/plain", //
	 * IOUtils.toByteArray(input)); FileUtils.copyFileToDirectory(new
	 * File(library.getFilePath()), new File(libraryFolderPath)); // MultipartFile
	 * multipartFile = new MockMultipartFile(srcFolder.getName(), new //
	 * FileInputStream(srcFolder)); // String errorMessage =
	 * uploadLibraryFile(library,libraryFolderPath, // multipartFile);
	 * libraryService.deleteSoftById(library.getId() + ""); //
	 * FileUtils.deleteQuietly(srcFolder); // File absOldFile = new
	 * File(srcFolder.getAbsolutePath());
	 * 
	 * FileUtils.deleteQuietly(new File(library.getFilePath()));
	 * 
	 * 
	 * Boolean deleted = srcFolder.delete();
	 * logger.info("File Exist0120s--->"+deleted); if(srcFolder.exists()){
	 * logger.info("File Exists--->"); try{ srcFolder.deleteOnExit();
	 * }catch(Exception e){ logger.error("Error while deleting file "+e); }
	 * 
	 * if(srcFolder.delete()){
	 * 
	 * logger.info("File Deleted--->"); }else{ logger.info("File Not Deleted--->");
	 * } }else{ logger.info("File Deleted--->"); }
	 * 
	 * } else if (Library.LINK.equalsIgnoreCase(library.getContentType())) {
	 * libraryService.deleteSoftById(library.getId() + ""); if (null !=
	 * library.getShareToSchools()) { String schoolName[] =
	 * library.getShareToSchools().split(","); logger.info("school Name Delete--->"
	 * + schoolName[0] + " length " + schoolName.length); for (int i = 0; i <
	 * schoolName.length; i++) { String apiUrl = serverURL +
	 * schoolName[i].replace(" ", "") + "/api/deleteItemToShareContent";
	 * logger.info("apiUrl---->" + apiUrl);
	 * 
	 * try { ObjectMapper Objmapper = new ObjectMapper(); String libJson =
	 * Objmapper.writeValueAsString(oldItem); WebTarget webTarget =
	 * client.target(URIUtil.encodeQuery(apiUrl)); Invocation.Builder
	 * invocationBuilder = webTarget.request(); Response response =
	 * invocationBuilder .post(Entity.entity(libJson.toString(),
	 * MediaType.APPLICATION_JSON)); logger.info("response---->" + response); }
	 * catch (Exception e) { // TODO Auto-generated catch block
	 * logger.error("Exception", e); } } } }
	 * 
	 * setSuccess(redirectAttrs, "Content Deleted Successfully");
	 * 
	 * } catch (Exception e) { logger.error(e.getMessage(), e);
	 * setError(redirectAttrs, "Error in deleting Content"); }
	 * 
	 * if (parentId != null && !folderPath.isEmpty()) { return
	 * "redirect:/viewLibrary?parentId=" + parentId + "&folderPath=" + folderPath; }
	 * else { return "redirect:/viewLibrary"; }
	 * 
	 * Library redLib = new Library(); if (parentId != null) { redLib =
	 * libraryService.findByID(parentId); redLib.setParentId(parentId);
	 * redirectAttrs.addFlashAttribute(redLib); return "redirect:/viewLibrary"; }
	 * 
	 * return "redirect:/viewLibraryAnnouncements"; }
	 */
	
	
	
	//Deleted LibraryItem for s3
	@Secured({ "ROLE_LIBRARIAN", "ROLE_FACULTY" })
	@RequestMapping(value = "/deleteLibraryItem", method = { RequestMethod.GET, RequestMethod.POST })
	public String deleteLibraryItem(@ModelAttribute Library library, RedirectAttributes redirectAttrs) {

		// redirectAttrs.addFlashAttribute("content", library);

		Long parentId = library.getParentId();
		String folderPath = library.getFolderPath();
		try {
			Library oldItem = libraryService.findByID(library.getId());
			library = libraryService.findByID(library.getId());
			parentId = library.getParentId();
			folderPath = library.getFolderPath();
			if (Library.FOLDER.equalsIgnoreCase(library.getContentType())) {
				// Mark Folder and its Children Inactive in DB
				libraryService.softDeleteFolder(library);

				// Rename folder with _Deleted in the name
				File existingFolder = new File(library.getFilePath());
				/*
				 * File newFolder = new File(library.getFilePath() + "_Deleted");
				 * existingFolder.renameTo(newFolder);
				 */
				// FileUtils.deleteDirectory(existingFolder);

				// Change Folder and FilePath of children as per Renamed Folder
				// Path
				libraryService.updateChildrenDetails(library.getFilePath() + "_Deleted", library.getFilePath());
				

				// Change File Path of Folder to Renamed Folder
				library.setFilePath(library.getFilePath() + "_Deleted");
				libraryService.updateFolder(library);
				
				
				//s3 download code
				
				String destDownloadDir = "/"+libraryRootFolder+"/"+library.getContentName() + "_Deleted";
				File fileDest = new File(destDownloadDir);
				if(!fileDest.exists()) {
					fileDest.mkdir();
				}
				
				s3Service.downloadDir(oldItem.getFolderPath(), destDownloadDir);
				
				File file = new File(destDownloadDir+"/"+oldItem.getFilePath());
				
				if(file.exists()) {
					file.renameTo(new File(file.getAbsolutePath()+"_Deleted"));
				}
				//renameFiles(destDownloadDir+"/"+library.getFilePath());
				
				logger.info(destDownloadDir+"/"+libraryRootFolder+"/"+library.getFilePath()+"file we got");
				//exicute kar ke dekh
				s3Service.uploadDir(destDownloadDir+"/"+library.getFilePath(), libraryRootFolder+"/"+file.getName()+"_Deleted", true);
				logger.info(oldItem.getFilePath()+"old pathwe got siis");
				s3Service.deleteDir(oldItem.getFilePath());
				FileUtils.deleteQuietly(fileDest);
				oldItem.setNewLibraryItem(library);
				if (null != library.getShareToSchools()) {
					String schoolName[] = library.getShareToSchools().split(",");
					logger.info("school Name Delete--->" + schoolName[0] + " length " + schoolName.length);
					for (int i = 0; i < schoolName.length; i++) {
						String apiUrl = serverURL + schoolName[i].replace(" ", "") + "/api/deleteItemToShareContent";
						logger.info("apiUrl---->" + apiUrl);

						try {
							ObjectMapper Objmapper = new ObjectMapper();
							String libJson = Objmapper.writeValueAsString(oldItem);
							WebTarget webTarget = client.target(URIUtil.encodeQuery(apiUrl));
							Invocation.Builder invocationBuilder = webTarget.request();
							Response response = invocationBuilder
									.post(Entity.entity(libJson.toString(), MediaType.APPLICATION_JSON));
							logger.info("response---->" + response);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							logger.error("Exception", e);
						}
					}
				}

			} else if (Library.FILE.equalsIgnoreCase(library.getContentType())) {
				// FileUtils.deleteQuietly(new File(library.getFilePath()));
	
				String libraryFolderPath = libraryRootFolder +"/"+ library.getLastModifiedDate().toString().split(" ")[0]
						+ "_DeletedFiles";
				
				String destDownloadDir = "/"+libraryRootFolder+"/"+library.getContentName() + "_DeletedFiles";
				
				File fileDest = new File(destDownloadDir);
				File oldFile = new File(oldItem.getFilePath());
				if(!fileDest.exists()) {
					fileDest.mkdir();
				}
				
				logger.info(destDownloadDir+"old file path");
				//s3Service.downloadDir(oldItem.getFolderPath(), destDownloadDir);
				File filene = new File(library.getContentName());
				File file = new File(libraryFolderPath);
			InputStream stream =s3Service.getFileForDownloadS3Object(oldFile.getName(),oldItem.getFolderPath());
			File fileDestImg = new File(destDownloadDir+"/"+oldFile.getName());
				FileUtils.copyInputStreamToFile(stream, fileDestImg);
				logger.info("fileDestImgABS---------->"+file.getAbsoluteFile());
				logger.info("fileDestImg---------->"+fileDestImg);
				
				s3Service.uploadFile(fileDestImg, libraryFolderPath);
				s3Service.deleteFile(oldItem.getFilePath());
				FileUtils.deleteQuietly(fileDest);
				//File file = new File(destDownloadDir+"/"+oldItem.getFilePath());
				
				
				// libraryFolderPath = libraryFolderPath.split(" ")[0];
				
				logger.info("libraryFolderPath--->" + libraryFolderPath);
				if (!file.exists()) {
					// file.mkdirs();
					Boolean created = file.mkdirs();
					logger.info("created--->" + created);
				}
				File srcFolder = new File(library.getFilePath());
				
				logger.info("srcFolder--->" + srcFolder.getAbsolutePath());
				logger.info("fileName--->" + srcFolder.getName());
			
				oldItem.setOldLibraryFolderPath(oldItem.getFolderPath());
				oldItem.setOldLibraryPath(oldItem.getFilePath());
				oldItem.setFilePath(libraryFolderPath+"/"+fileDestImg.getName());
				oldItem.setActive("N");
				oldItem.setFolderPath(libraryFolderPath);
				logger.info(oldItem+"-------------->got     ttt");
				
				libraryService.update(oldItem);
				
				
				
				if (null != library.getShareToSchools()) {
					String schoolName[] = library.getShareToSchools().split(",");
					logger.info("school Name Delete--->" + schoolName[0] + " length " + schoolName.length);
					for (int i = 0; i < schoolName.length; i++) {
						String apiUrl = serverURL + schoolName[i].replace(" ", "") + "/api/deleteItemToShareContent";
						logger.info("apiUrl---->" + apiUrl);

						try {
														
							ObjectMapper Objmapper = new ObjectMapper();
							String libJson = Objmapper.writeValueAsString(oldItem);
							WebTarget webTarget = client.target(URIUtil.encodeQuery(apiUrl));
							Invocation.Builder invocationBuilder = webTarget.request();
							Response response = invocationBuilder
									.post(Entity.entity(libJson.toString(), MediaType.APPLICATION_JSON));
							logger.info("response---->" + response);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							logger.error("Exception", e);
						}
					}
				}
			
			} else if (Library.LINK.equalsIgnoreCase(library.getContentType())) {
				libraryService.deleteSoftById(library.getId() + "");
				if (null != library.getShareToSchools()) {
					String schoolName[] = library.getShareToSchools().split(",");
					logger.info("school Name Delete--->" + schoolName[0] + " length " + schoolName.length);
					for (int i = 0; i < schoolName.length; i++) {
						String apiUrl = serverURL + schoolName[i].replace(" ", "") + "/api/deleteItemToShareContent";
						logger.info("apiUrl---->" + apiUrl);

						try {
							ObjectMapper Objmapper = new ObjectMapper();
							String libJson = Objmapper.writeValueAsString(oldItem);
							WebTarget webTarget = client.target(URIUtil.encodeQuery(apiUrl));
							Invocation.Builder invocationBuilder = webTarget.request();
							Response response = invocationBuilder
									.post(Entity.entity(libJson.toString(), MediaType.APPLICATION_JSON));
							logger.info("response---->" + response);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							logger.error("Exception", e);
						}
					}
				}
			}

			setSuccess(redirectAttrs, "Content Deleted Successfully");

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(redirectAttrs, "Error in deleting Content");
		}
		/*
		 * if (parentId != null && !folderPath.isEmpty()) { return
		 * "redirect:/viewLibrary?parentId=" + parentId + "&folderPath=" + folderPath; }
		 * else { return "redirect:/viewLibrary"; }
		 */
		Library redLib = new Library();
		if (parentId != null) {
			redLib = libraryService.findByID(parentId);
			redLib.setParentId(parentId);
			redirectAttrs.addFlashAttribute(redLib);
			return "redirect:/viewLibrary";
		}

		return "redirect:/viewLibraryAnnouncements";
	}
	
	
	
	
	@Secured({ "ROLE_LIBRARIAN", "ROLE_FACULTY" })
	@RequestMapping(value = "/copyLibraryItemForm", method = { RequestMethod.GET, RequestMethod.POST })
	public String copyLibraryItemForm(Model m, @ModelAttribute Library library, @RequestParam String action,
			@RequestParam Long id, Principal principal) {
		m.addAttribute("webPage", new WebPage("library", "Create Library Item", false, false));

		String contentType = library.getContentType();
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		library = libraryService.findByID(id);

		if (library.getFolderPath() == null) {
			// If path is not present
			library.setFolderPath(libraryRootFolder);
		}

		m.addAttribute("content", library);
		m.addAttribute("libraryRootFolder", libraryRootFolder);
		m.addAttribute("action", action);
		return "library/copyLibraryItem";

	}

	/*
	 * @RequestMapping(value = "/copyLibraryItem", method = { RequestMethod.GET,
	 * RequestMethod.POST }) public String copyLibraryItem(Model m, @ModelAttribute
	 * Library library, RedirectAttributes redirectAttributes, Principal principal)
	 * { m.addAttribute("webPage", new WebPage("library", "Create Library Item",
	 * false, false));
	 * 
	 * String username = principal.getName();
	 * 
	 * Token userdetails1 = (Token) principal; String ProgramName =
	 * userdetails1.getProgramName(); User u = userService.findByUserName(username);
	 * 
	 * String acadSession = u.getAcadSession();
	 * 
	 * m.addAttribute("Program_Name", ProgramName); m.addAttribute("AcadSession",
	 * acadSession);
	 * 
	 * String srcPath = library.getFolderPath(); String destPath =
	 * library.getCopyPath(); File srcFolder = new File(srcPath); File destFolder =
	 * new File(destPath);
	 * 
	 * try {
	 * 
	 * Library lib = libraryService.findOneContent(srcPath); Library destLib =
	 * libraryService.findOneContent(destPath); if
	 * (libraryService.findOneContent(destPath + "/" + lib.getContentName()) ==
	 * null) {
	 * 
	 * if (library.getContentType().equalsIgnoreCase("Folder")) {
	 * FileUtils.copyDirectoryToDirectory(srcFolder, destFolder);
	 * lib.setFolderPath(destPath + "/" + lib.getContentName());
	 * lib.setFilePath(destPath + "/" + lib.getContentName());
	 * lib.setParentId(destLib.getId()); libraryService.insertWithIdReturn(lib); lib
	 * = libraryService.findOneContent(lib.getFolderPath());
	 * 
	 * libraryService.readLibrary(new File(lib.getFolderPath()), lib.getId(),
	 * username);
	 * 
	 * } setSuccess(redirectAttributes, "Docuement Copied successfully!"); } else {
	 * setNote(redirectAttributes, lib.getContentName() +
	 * " already exists in destination folder!"); } } catch (IOException e) {
	 * logger.error("Exception", e); }
	 * 
	 * m.addAttribute("content", library); return
	 * "redirect:/viewLibraryAnnouncements";
	 * 
	 * }
	 */

	// S3 CopyItem

	@Secured({ "ROLE_LIBRARIAN", "ROLE_FACULTY" })
	@RequestMapping(value = "/copyLibraryItem", method = { RequestMethod.GET, RequestMethod.POST })
	public String copyLibraryItem(Model m, @ModelAttribute Library library, RedirectAttributes redirectAttributes,
			Principal principal) {
		m.addAttribute("webPage", new WebPage("library", "Create Library Item", false, false));

		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		String srcPath = library.getFolderPath();
		String destPath = library.getCopyPath();
		File srcFolder = new File(srcPath);
		File destFolder = new File(destPath);

		String sourceLocalDirStr = "/" + libraryRootFolder + "/" + library.getContentName();
		File sourceLocalDir = new File("/" + libraryRootFolder + "/" + library.getContentName());
		if (!sourceLocalDir.exists()) {
			sourceLocalDir.exists();
		}

		s3Service.downloadDir(srcPath, sourceLocalDirStr);
		logger.info("jdjdjdjd");

		s3Service.uploadDir(sourceLocalDirStr + "/" + libraryRootFolder + "/" + library.getContentName(),
				library.getCopyPath() + "/" + library.getContentName(), true);
		logger.info("srcFolder Move--------------->" + srcPath);
		logger.info("destPath Move---------------" + destPath);
		try {

			Library lib = libraryService.findOneContent(srcPath);
			Library destLib = libraryService.findOneContent(destPath);
			if (libraryService.findOneContent(destPath + "/" + lib.getContentName()) == null) {

				if (library.getContentType().equalsIgnoreCase("Folder")) {
					// FileUtils.copyDirectoryToDirectory(srcFolder, destFolder);
					logger.info("foler created entered");
					lib.setFolderPath(destPath + "/" + lib.getContentName());
					lib.setFilePath(destPath + "/" + lib.getContentName());
					lib.setParentId(destLib.getId());
					logger.info("lib folder and file path" + destPath + "/" + lib.getContentName());
					libraryService.insertWithIdReturn(lib);
					logger.info("inserted successfully");
					lib = libraryService.findOneContent(lib.getFolderPath());

					libraryService.readLibraryS3(
							new File(sourceLocalDirStr + "/" + libraryRootFolder + "/" + library.getContentName()),
							lib.getId(), username, libraryRootFolder + "/" + library.getContentName(),
							lib.getFolderPath());
					FileUtils.deleteDirectory(sourceLocalDir);
				}
				setSuccess(redirectAttributes, "Docuement Copied successfully!");
			} else {
				setNote(redirectAttributes, lib.getContentName() + " already exists in destination folder!");
			}
		} catch (Exception e) {
			logger.error("Exception", e);
		}

		m.addAttribute("content", library);
		return "redirect:/viewLibraryAnnouncements";

	}

	/*
	 * @RequestMapping(value = "/moveLibraryItem", method = { RequestMethod.GET,
	 * RequestMethod.POST }) public String moveLibraryItem(Model m, @ModelAttribute
	 * Library library, RedirectAttributes redirectAttributes, Principal principal)
	 * { m.addAttribute("webPage", new WebPage("library", "Create Library Item",
	 * false, false));
	 * 
	 * String username = principal.getName();
	 * 
	 * Token userdetails1 = (Token) principal; String ProgramName =
	 * userdetails1.getProgramName(); User u = userService.findByUserName(username);
	 * 
	 * String acadSession = u.getAcadSession();
	 * 
	 * m.addAttribute("Program_Name", ProgramName); m.addAttribute("AcadSession",
	 * acadSession);
	 * 
	 * String srcPath = library.getFolderPath(); String destPath =
	 * library.getCopyPath(); File srcFolder = new File(srcPath); File destFolder =
	 * new File(destPath);
	 * 
	 * try {
	 * 
	 * Library lib = libraryService.findOneContent(srcPath); Library delLib =
	 * libraryService.findOneContent(srcPath);
	 * 
	 * Library destLib = libraryService.findOneContent(destPath); if
	 * (libraryService.findOneContent(destPath + "/" + lib.getContentName()) ==
	 * null) {
	 * 
	 * if (library.getContentType().equalsIgnoreCase("Folder")) {
	 * 
	 * FileUtils.copyDirectoryToDirectory(srcFolder, destFolder);
	 * lib.setFolderPath(destPath + "/" + lib.getContentName());
	 * lib.setFilePath(destPath + "/" + lib.getContentName());
	 * lib.setParentId(destLib.getId()); libraryService.insertWithIdReturn(lib); lib
	 * = libraryService.findOneContent(lib.getFolderPath());
	 * 
	 * libraryService.readLibrary(new File(lib.getFolderPath()), lib.getId(),
	 * username);
	 * 
	 * } deleteLibraryItem(delLib, redirectAttributes);
	 * setSuccess(redirectAttributes, "Docuement Copied successfully!"); } else {
	 * setNote(redirectAttributes, lib.getContentName() +
	 * " already exists in destination folder!"); } } catch (IOException e) {
	 * logger.error("Exception", e); }
	 * 
	 * m.addAttribute("content", library); return
	 * "redirect:/viewLibraryAnnouncements";
	 * 
	 * }
	 */

	// Move Itmes3

	@Secured({ "ROLE_LIBRARIAN", "ROLE_FACULTY" })
	@RequestMapping(value = "/moveLibraryItem", method = { RequestMethod.GET, RequestMethod.POST })
	public String moveLibraryItem(Model m, @ModelAttribute Library library, RedirectAttributes redirectAttributes,
			Principal principal) {
		m.addAttribute("webPage", new WebPage("library", "Create Library Item", false, false));

		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		String srcPath = library.getFolderPath();
		String destPath = library.getCopyPath();
		File srcFolder = new File(srcPath);
		File destFolder = new File(destPath);

		String sourceLocalDirStr = "/" + libraryRootFolder + "/" + library.getContentName();
		File sourceLocalDir = new File("/" + libraryRootFolder + "/" + library.getContentName());
		if (!sourceLocalDir.exists()) {
			sourceLocalDir.exists();
		}

		s3Service.downloadDir(srcPath, sourceLocalDirStr);
		logger.info("jdjdjdjd");

		s3Service.uploadDir(sourceLocalDirStr + "/" + libraryRootFolder + "/" + library.getContentName(),
				library.getCopyPath() + "/" + library.getContentName(), true);
		logger.info("srcFolder Move--------------->" + srcPath);
		logger.info("destPath Move---------------" + destPath);
		try {

			Library lib = libraryService.findOneContent(srcPath);
			List<Library> childLib = libraryService.findByParentId(lib.getId());

			childLib.forEach(i -> i.setActive("N"));
			lib.setActive("N");

			libraryService.updateBatch(childLib);
			libraryService.deleteSoftById(String.valueOf(lib.getId()));
			Library destLib = libraryService.findOneContent(destPath);
			if (libraryService.findOneContent(destPath + "/" + lib.getContentName()) == null) {

				if (library.getContentType().equalsIgnoreCase("Folder")) {
					// FileUtils.copyDirectoryToDirectory(srcFolder, destFolder);
					logger.info("foler created entered");
					lib.setFolderPath(destPath + "/" + lib.getContentName());
					lib.setFilePath(destPath + "/" + lib.getContentName());
					lib.setParentId(destLib.getId());
					logger.info("lib folder and file path" + destPath + "/" + lib.getContentName());
					libraryService.insertWithIdReturn(lib);
					logger.info("inserted successfully");
					lib = libraryService.findOneContent(lib.getFolderPath());

					libraryService.readLibraryS3(
							new File(sourceLocalDirStr + "/" + libraryRootFolder + "/" + library.getContentName()),
							lib.getId(), username, libraryRootFolder + "/" + library.getContentName(),
							lib.getFolderPath());
					FileUtils.deleteDirectory(sourceLocalDir);
					s3Service.deleteDir(library.getFolderPath());
				}
				setSuccess(redirectAttributes, "Docuement Moved successfully!");
			} else {
				setNote(redirectAttributes, lib.getContentName() + " already exists in destination folder!");
			}
		} catch (Exception e) {
			logger.error("Exception", e);
		}

		m.addAttribute("content", library);
		return "redirect:/viewLibraryAnnouncements";

	}

	// Hiren 27-11-2019
	@Secured({ "ROLE_ADMIN", "ROLE_SUPPORT_ADMIN", "ROLE_FACULTY" })
	@RequestMapping(value = "/getLibrarianListById", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String getLibrarianListById(@RequestParam(name = "libId") String libId, Principal principal) {

		String json = "";
		String username = principal.getName();
		List<Library> librarianList1 = libraryService.getLibraryContentUserRights(Long.valueOf(libId), username);
		json = new Gson().toJson(librarianList1);
		logger.info("JSON---->" + json);
		return json;
	}

	// Hiren 27-11-2019
	@Secured({ "ROLE_ADMIN", "ROLE_SUPPORT_ADMIN", "ROLE_FACULTY" })
	@RequestMapping(value = "/saveLibrarianRights", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String saveLibrarianRights(@RequestParam(name = "libId") String libId,
			@RequestParam(name = "librarianRightsJson") String librarianRightsJson, Principal principal) {

		String username = principal.getName();
		try {
			Object libRightsObject;
			libRightsObject = new JSONParser().parse(librarianRightsJson);
			JSONArray libRightsArray = (JSONArray) libRightsObject;
			logger.info("libRightsArraySize---->" + libRightsArray.size());

			List<Library> libRightsList = new ArrayList<>();
			for (int i = 0; i < libRightsArray.size(); i++) {
				JSONObject jsonObject = new JSONObject();
				jsonObject = (JSONObject) libRightsArray.get(i);
				Library l = new Library();
				l.setUsername(jsonObject.get("username").toString());
				l.setLibraryId(libId);
				l.setCreateOnly(jsonObject.get("createOnly").toString());
				l.setEditOnly(jsonObject.get("editOnly").toString());
				l.setCreatedBy(username);
				l.setCreatedDate(Utils.getInIST());
				l.setLastModifiedBy(username);
				l.setLastModifiedDate(Utils.getInIST());
				libRightsList.add(l);
				logger.info("jsonObject---->" + jsonObject.get("username").toString());
			}
			libraryService.insertLibraryContentUserRights(libRightsList);

			List<Library> lib = libraryService.findByParentId(Long.valueOf(libId));
			for (Library l : lib) {
				if (null != l.getParentId()) {
					logger.info("ID--->" + l.getId());
					saveLibrarianRights(String.valueOf(l.getId()), librarianRightsJson, principal);
				}

			}

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "{\"Status\":\"Fail\"}";
		}

		return "{\"Status\":\"Success\"}";
	}

	// Hiren 27-11-2019
	@Secured({ "ROLE_ADMIN", "ROLE_SUPPORT_ADMIN", "ROLE_FACULTY" })
	@RequestMapping(value = "/shareLibraryContent", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String shareLibraryContent(@RequestParam(name = "libId") String libId,
			@RequestParam(name = "schoolName") String schoolName, Principal principal) {

		String username = principal.getName();
		Library library = libraryService.findByID(Long.valueOf(libId));

		List<Library> itemsUnderPath = libraryService.getItemsToShare(library);
		logger.info("itemsUnderPath--->" + itemsUnderPath);
		logger.info("schoolName--->" + schoolName);
		logger.info("ShareToSchools--->" + library.getShareToSchools());
		// int index =0;
		if (null != library.getShareToSchools()) {
			for (Library l : itemsUnderPath) {
				if (l.getShareToSchools().contains(schoolName)) {
					l.setShareToSchools(l.getShareToSchools().replace(schoolName, ""));
					if (l.getShareToSchools().contains(",,")) {
						l.setShareToSchools(l.getShareToSchools().replace(",,", ","));
					}
					/*
					 * if(l.getShareToSchools().startsWith(",")){
					 * l.setShareToSchools(l.getShareToSchools().substring(1)); }
					 */
					// itemsUnderPath.remove(index);
					// return "{\"Status\":\"Exist\"}";
				}
				logger.info("parentId---->" + l.getParentId());
				if (null != l.getParentId()) {
					l.setParentId(null);
				}
				// index++;
			}
			/*
			 * if(library.getShareToSchools().contains(schoolName)){ return
			 * "{\"Status\":\"Exist\"}"; }
			 */
		} else {
			for (Library l : itemsUnderPath) {
				logger.info("parentId---->" + l.getParentId());
				if (null != l.getParentId()) {
					l.setParentId(null);
				}
			}

		}
		libraryService.updateShareToSchool(schoolName, library.getFilePath());

		String apiUrl = serverURL + schoolName.replace(" ", "") + "/api/insertShareLibraryContent";
		logger.info("apiUrl---->" + apiUrl);

		try {
			ObjectMapper Objmapper = new ObjectMapper();
			String libJson = Objmapper.writeValueAsString(itemsUnderPath);
			logger.info("libJson---->" + libJson);
			WebTarget webTarget = client.target(URIUtil.encodeQuery(apiUrl));
			Invocation.Builder invocationBuilder = webTarget.request();

			Response response = invocationBuilder.post(Entity.entity(libJson.toString(), MediaType.APPLICATION_JSON));
			logger.info("response---->" + response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("Exception", e);
		}

		return "{\"Status\":\"Success\"}";
	}

	@Secured({ "ROLE_ADMIN", "ROLE_SUPPORT_ADMIN", "ROLE_FACULTY" })
	@RequestMapping(value = "/giveLibrarianRights", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String giveLibrarianRights(@RequestParam(name = "libId") String libId,
			@RequestParam(name = "parentId") String parentId, Principal principal) {

		String username = principal.getName();
		try {

			List<Library> libRightsList = libraryService.getLibraryContentUserRights(Long.valueOf(parentId), username);
			List<Library> libRightsListNew = new ArrayList<>();
			for (Library l : libRightsList) {
				l.setLibraryId(libId);
				l.setCreatedBy(username);
				l.setCreatedDate(Utils.getInIST());
				l.setLastModifiedBy(username);
				l.setLastModifiedDate(Utils.getInIST());
				libRightsListNew.add(l);
			}
			// List<Library> libRightsList = new ArrayList<>();
			/*
			 * for(int i=0;i<libRightsArray.size();i++){ JSONObject jsonObject = new
			 * JSONObject(); jsonObject = (JSONObject) libRightsArray.get(i); Library l =
			 * new Library(); l.setUsername(jsonObject.get("username").toString());
			 * l.setLibraryId(libId);
			 * l.setCreateOnly(jsonObject.get("createOnly").toString());
			 * l.setEditOnly(jsonObject.get("editOnly").toString());
			 * l.setCreatedBy(username); l.setCreatedDate(Utils.getInIST());
			 * l.setLastModifiedBy(username); l.setLastModifiedDate(Utils.getInIST());
			 * libRightsList.add(l);
			 * logger.info("jsonObject---->"+jsonObject.get("username").toString()); }
			 */
			libraryService.insertLibraryContentUserRights(libRightsList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "{\"Status\":\"Fail\"}";
		}

		return "{\"Status\":\"Success\"}";
	}

	public void checkDirectoryOrNoForLibrary(File file, File folderPath, String tempFilePath, InputStream is) {
		logger.info("Absolute Path---->" + folderPath.getAbsolutePath());
		File newfile = new File(folderPath.getAbsolutePath() + File.separator + file.getName());
		if (!folderPath.exists()) {
			logger.info("Folder Exists---->" + folderPath);
			folderPath.mkdirs();
		}
		/*
		 * if (!newfile.exists()) { newfile.mkdirs(); }
		 */

		try {
			if (file.isDirectory()) {
				logger.info("listFile Length---->" + file.listFiles().length);

				if (file.listFiles().length == 0) {

					if (!newfile.exists()) {
						logger.info("newfile---->" + newfile.getAbsolutePath());
						newfile.mkdir();
					}

				} else {
					for (File f : file.listFiles()) {

						File newFolder = new File(folderPath.getAbsolutePath() + File.separator + file.getName()
								+ File.separator + f.getName());
						File newFolder1 = new File(folderPath.getAbsolutePath() + File.separator + file.getName());

						logger.info("Get File Name -----------> " + file.getName());
						if (!f.getName().contains("_Deleted")) {
							List<Library> checkForDown = libraryService.findLibraryByFileName(f.getName());
							if (checkForDown.size() > 0) {
								if (!f.isDirectory()) {
									logger.info("newFolder---->" + newFolder.getAbsolutePath());
									FileUtils.copyInputStreamToFile(is, f);
									FileUtils.copyFile(f, newFolder);

								} else {
									logger.info("newFolder1---->" + newFolder1.getAbsolutePath());
									String tempFileStr = "/" + downloadAllFolder + "/" + newFolder1.getName();
									File tempF = new File("/" + downloadAllFolder + "/" + newFolder1.getName());
									InputStream insp = s3Service.getFileForDownloadS3Object(newFolder1.getName(),
											folderPath.getAbsolutePath());
									FileUtils.copyInputStreamToFile(insp, tempF);
									FileUtils.copyFile(tempF, newFolder1);
									checkDirectoryOrNoForLibrary(newFolder1, folderPath, tempFileStr, insp);
									// checkDirectoryOrNoForLibrary(newfile, folderPath, tempFilePath, is);
								}
							}
						}
					}
				}
			} else {
				FileUtils.copyInputStreamToFile(is, file);
				FileUtils.copyFile(file, newfile);
			}
		} catch (Exception ex) {
			logger.error("Exception", ex);
		}
	}
	
	
	//Updated Code for download Zipp from container
	public void checkMultiFolderLibrary(List<Library> libraryList, String folderPath) {
		try {
			// dekh abhi
			for (Library lib : libraryList) {
				logger.info("parent id " + lib.getParentId());
				logger.info("folder path" + lib.getFolderPath());
				if (lib.getParentId() != null && lib.getContentType().equals(Library.FOLDER)) {
					logger.info("contn satisfies");
					String libFolderPathStr = folderPath + "/" + lib.getContentName();
					File libFolderPath = new File(folderPath + "/" + lib.getContentName());
					if (!libFolderPath.exists()) {
						logger.info("lib made" + libFolderPath.getAbsolutePath());
						libFolderPath.mkdir();
					}
					/*
					 * File childFolder = new File("/"+libFolderPath.getAbsolutePath() + "/" +
					 * lib.getLibraryName()); if (!childFolder.exists()) {
					 * logger.info("child lib made"+childFolder.getAbsolutePath());
					 * childFolder.mkdir(); }
					 */

					List<Library> library = libraryService.findByParentId(lib.getId());
					if (library.size() > 0) {
						logger.info("folder exist--");
						checkMultiFolderLibrary(library, libFolderPathStr);
					}
					logger.info("folder" + lib.getFolderPath() + "---" + lib.getFilePath());

				} else {
					if (!lib.getContentType().equals(Library.FOLDER)) {
							//lms band kar ke chalu kar

						String libfolderPath = lib.getFolderPath();
					//	String rem= removeLastCharacter(lib.getFolderPath());
						
						if(libfolderPath.endsWith("/"))
						{
							libfolderPath = libfolderPath.substring(0,libfolderPath.length() - 1);
						}
						
								
						logger.info("rem------------->"+libfolderPath);
						
						File f = new File(lib.getFilePath());
						logger.info("path_suraj1----> " + libfolderPath);
						
						logger.info("DOWNLOAD ERROR REC----------"+f.getName()+" --- "+lib.getFolderPath());
												
						String[] filePAthArr = libfolderPath.split("/");
						logger.info("filePAthArr---->" + filePAthArr.length);
						if (filePAthArr[filePAthArr.length - 1].contains(".")) {
							libfolderPath = libfolderPath.replace(filePAthArr[filePAthArr.length - 1], "");
							logger.info("path_suraj5---->" + libfolderPath);
							if (libfolderPath.endsWith("/")) {
								libfolderPath = libfolderPath.substring(0, libfolderPath.length() - 1);
								logger.info("path_suraj2---->" + libfolderPath);
							}
							logger.info("path_suraj3---->" + libfolderPath);
						}
								
						
						logger.info("path_suraj4---->" + libfolderPath);
													
						//String sourcePathS3="data/MPSTME-NM-M/library/QUESTIONS PAPERS /B TECH/1st YEAR/APPLIED CHEMISTRY/";
						//s3Service.downloadDir(sourcePathS3, downloadAllFolder);
						InputStream inpStream = s3Service.getFileForDownloadS3Object(f.getName(), libfolderPath);
					
						

						logger.info("jjkjkjkjkj" + folderPath+ "/" + f.getName());
						File fiewNewDest = new File(folderPath + "/" + f.getName());
						
						
						String tobedeleted= fiewNewDest.getName();
						if(tobedeleted.endsWith("/"))
						{
							tobedeleted = tobedeleted.substring(0,tobedeleted.length() - 1);
							IOUtils.copy(inpStream, new FileOutputStream(tobedeleted));
						}
						logger.info("fiewNewDest----------<<" + fiewNewDest);
						
						IOUtils.copy(inpStream, new FileOutputStream(fiewNewDest));
						
					}
				}
			}
		} catch (Exception ex) {
			logger.error("exception", ex);
		}
	}
	
	
	
	
	
//Working due to folder path issue commented
	/*
	 * public void checkMultiFolderLibrary(List<Library> libraryList, String
	 * folderPath) { try { // dekh abhi for (Library lib : libraryList) {
	 * logger.info("parent id " + lib.getParentId()); logger.info("folder path" +
	 * lib.getFolderPath()); if (lib.getParentId() != null &&
	 * lib.getContentType().equals(Library.FOLDER)) {
	 * logger.info("contn satisfies"); String libFolderPathStr = folderPath + "/" +
	 * lib.getContentName(); File libFolderPath = new File(folderPath + "/" +
	 * lib.getContentName()); if (!libFolderPath.exists()) { logger.info("lib made"
	 * + libFolderPath.getAbsolutePath()); libFolderPath.mkdir(); }
	 * 
	 * 
	 * List<Library> library = libraryService.findByParentId(lib.getId()); if
	 * (library.size() > 0) { logger.info("folder exist--");
	 * checkMultiFolderLibrary(library, libFolderPathStr); } logger.info("folder" +
	 * lib.getFolderPath() + "---" + lib.getFilePath());
	 * 
	 * } else { if (!lib.getContentType().equals(Library.FOLDER)) {
	 * 
	 * 
	 * File f = new File(lib.getFilePath()); logger.info("folder path" +
	 * folderPath); String libfolderPath=lib.getFolderPath();
	 * 
	 * if(libfolderPath.endsWith("/")) { libfolderPath =
	 * libfolderPath.substring(0,libfolderPath.length() - 1); }
	 * 
	 * //InputStream inpStream = s3Service.getFileForDownloadS3Object(f.getName(),
	 * lib.getFolderPath()); InputStream inpStream =
	 * s3Service.getFileForDownloadS3Object(f.getName(), libfolderPath);
	 * 
	 * logger.info(inpStream); logger.info("jjkjkjkjkj" + folderPath + "/" +
	 * f.getName()); File fiewNewDest = new File(folderPath + "/" + f.getName());
	 * IOUtils.copy(inpStream, new FileOutputStream(fiewNewDest));
	 * 
	 * } } } } catch (Exception ex) { logger.error("exception", ex); } }
	 */

}
