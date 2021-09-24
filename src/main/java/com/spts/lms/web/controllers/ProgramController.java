package com.spts.lms.web.controllers;


import java.security.Principal;
import java.util.List;

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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.spts.lms.beans.program.Program;
import com.spts.lms.helpers.PaginationHelper.Page;
import com.spts.lms.helpers.excel.ProgramExcelHelper;
import com.spts.lms.services.program.ProgramService;
import com.spts.lms.utils.LMSHelper;
import com.spts.lms.web.helper.WebPage;


@Controller
public class ProgramController extends BaseController {

	@Autowired
	ApplicationContext act;

	@Autowired
	ProgramService programService;
	
	
	private static final Logger logger = Logger.getLogger(ProgramController.class);

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/addProgramForm", method = RequestMethod.GET)
	public String addProgramForm(@RequestParam(required = false) String programId, Model m) {
		
		m.addAttribute("webPage",new WebPage("addProgram", "Add a new Program", false, false));
		
		Program program = new Program();
		if(programId != null && !programId.equals("")) {
			program = programService.findByID(Long.parseLong(programId));
			m.addAttribute("edit", "true");
		}
		m.addAttribute("program",program);
		
		return "program/addProgram";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/addProgram", method = RequestMethod.POST)
	public String addProgram(@ModelAttribute Program program, RedirectAttributes redirectAttrs, Principal principal){
		String username=principal.getName();
		try{
			program.setCreatedBy(username);
			program.setLastModifiedBy(username);

			programService.insertWithIdReturn(program);
			
			setSuccess(redirectAttrs, "Program added successfully");

		}catch(Exception e){
			logger.error(e.getMessage(), e);
			setError(redirectAttrs,"Error in adding Program");
			return "redirect:/addProgramForm";
		}
		return "redirect:/searchProgram";
	}
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/updateProgram", method = RequestMethod.POST)
	public String updateProgram(@ModelAttribute String userId, @ModelAttribute Program program, RedirectAttributes redirectAttrs , Principal principal){
		String username=principal.getName();
		try{
			Program programFromDb = programService.findByID(program.getId());
			programFromDb = LMSHelper.copyNonNullFields(programFromDb, program);
			
			programFromDb.setLastModifiedBy(username);

			programService.update(programFromDb);
			
			
			
			setSuccess(redirectAttrs, "Program saved successfully");

		}catch(Exception e){
			
			logger.error(e.getMessage(), e);
			setError(redirectAttrs,"Error in saving Program");
			return "redirect:/addProgramForm";
		}
		return "redirect:/searchProgram";
	}
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/searchProgram", method = {RequestMethod.GET, RequestMethod.POST})
	public String searchProgram(@RequestParam(required=false, defaultValue="1") int pageNo, Model m, @ModelAttribute Program program){
		
		m.addAttribute("webPage", new WebPage("programList", "View Programs", false, false));
		
		try{			
			Page<Program> page = programService.searchActiveByExactMatch(program, pageNo, pageSize);
			
			List<Program> programList = page.getPageItems();

			m.addAttribute("page", page);
			m.addAttribute("q", getQueryString(program));
			

			if(programList == null || programList.size() == 0){
				setSuccess(m,"No Programs found");
			}

		}catch(Exception e){
			
			logger.error(e.getMessage(), e);
			setError(m, "Error in getting Program List");
		}
		return "program/searchProgram";
	}
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/deleteProgram", method = {RequestMethod.GET, RequestMethod.POST})
	public String deleteProgram(@RequestParam Integer programId, RedirectAttributes redirectAttrs){
		try{
			programService.deleteSoftById(String.valueOf(programId));
			setSuccess(redirectAttrs,"Program deleted successfully");
			
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			setError(redirectAttrs, "Error in deleting Exam Center.");
		}
		
		return "redirect:/searchProgram";
	}
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/uploadProgramForm", method = {RequestMethod.GET, RequestMethod.POST})
	public String uploadProgramForm(Model m) {
		m.addAttribute("webPage",new WebPage("uploadProgram", "Upload Prorams available in Institute", false, false, true, true, false));
		m.addAttribute("program",new Program());
		return "program/uploadProgram";
	}
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/uploadProgram", method = RequestMethod.POST)
	public String uploadProgram(@ModelAttribute Program program, Principal principal , @RequestParam("file") MultipartFile file, RedirectAttributes redirectAttrs){
		String username=principal.getName();
		ProgramExcelHelper programExcelHelper = getProgramExcelHelper();
		try{
			if(!file.isEmpty()){
				program.setCreatedBy(username);
				programExcelHelper.initHelper(program);
				programExcelHelper.readExcel((MultipartFile)file);
			}
			
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			setError(redirectAttrs,"Error in uploading File: "+e.getMessage());
			return "course/uploadProgramForm";
		}
		
		if(programExcelHelper.getErrorList().isEmpty()){
			setSuccess(redirectAttrs, "File uploaded successfully");
			return "redirect:/searchProgram";
		}else{
			setErrorList(redirectAttrs, "Errors encountered uploading file: No Records Added",programExcelHelper.getErrorList());
			return "redirect:/uploadProgramForm";
		}
		
	}

	private ProgramExcelHelper getProgramExcelHelper() {
		return (ProgramExcelHelper)act.getBean("programExcelHelper");
	}

}

