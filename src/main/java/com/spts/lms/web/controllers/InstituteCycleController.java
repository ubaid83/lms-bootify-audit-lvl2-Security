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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.spts.lms.beans.instituteCycle.InstituteCycle;
import com.spts.lms.beans.instituteCycle.InstituteCycle.InstituteCycleType;
import com.spts.lms.services.instituteCycle.InstituteCycleService;
import com.spts.lms.utils.LMSHelper;
import com.spts.lms.web.helper.WebPage;


@Controller
public class InstituteCycleController extends BaseController {

	@Autowired
	ApplicationContext act;

	@Autowired
	InstituteCycleService instituteCycleService;

	private static final Logger logger = Logger
			.getLogger(InstituteCycleController.class);

	@Secured("ROLE_USER")
	@RequestMapping(value = "/addInstituteCycleForm", method = RequestMethod.GET)
	public String addInstituteCycleForm(
			@ModelAttribute InstituteCycle instituteCycle, Model m) {
		m.addAttribute("webPage", new WebPage("addInstituteCycle",
				"Add/Update Academic Cycle", false, false));
		getAllInstituteCycles(m);

		if (InstituteCycleType.ACADS.toString().equalsIgnoreCase(
				instituteCycle.getCycleType())) {
			instituteCycle.setCycleType(InstituteCycleType.ACADS);
		} else if (InstituteCycleType.EXAMS.toString().equalsIgnoreCase(
				instituteCycle.getCycleType())) {
			instituteCycle.setCycleType(InstituteCycleType.EXAMS);
		} else {
			instituteCycle.setCycleType(InstituteCycleType.ACADS);
		}

		if (instituteCycle.getId() != null
				&& !instituteCycle.getId().equals("")) {
			instituteCycle = instituteCycleService.findByID(instituteCycle
					.getId());
			m.addAttribute("edit", "true");
		}
		m.addAttribute("instituteCycle", instituteCycle);

		return "instituteCycle/addInstituteCycle";
	}

	private void getAllInstituteCycles(Model m) {
		List<InstituteCycle> allCycles = instituteCycleService.findAll();
		m.addAttribute("allCycles", allCycles);
		m.addAttribute("noOfCycles", allCycles.size());

	}

	@Secured("ROLE_USER")
	@RequestMapping(value = "/addInstituteCycle", method = RequestMethod.POST)
	public String addInstituteCycle(
			@ModelAttribute InstituteCycle instituteCycle,
			RedirectAttributes redirectAttrs, Principal principal) {
		String username = principal.getName();
		try {
			instituteCycle.setCreatedBy(username);
			instituteCycle.setLastModifiedBy(username);

			List<InstituteCycle> existingRecords = instituteCycleService
					.findIfCycleExists(instituteCycle);
			if (existingRecords != null && existingRecords.size() > 0) {
				setError(
						redirectAttrs,
						"This cycle already exists. Please edit existing record instead of creating new.");
				return "redirect:/addInstituteCycleForm";
			}

			if ("Y".equalsIgnoreCase(instituteCycle.getLive())) {
				instituteCycleService.makeCyclesInactive(instituteCycle);
			}

			instituteCycleService.insertWithIdReturn(instituteCycle);
			setSuccess(redirectAttrs, "Cycle added successfully");

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(redirectAttrs, "Error in adding Cycle");
			return "redirect:/addInstituteCycleForm";
		}
		return "redirect:/addInstituteCycleForm";
	}

	@Secured("ROLE_USER")
	@RequestMapping(value = "/updateInstituteCycle", method = RequestMethod.POST)
	public String updateInstituteCycle(@ModelAttribute String userId,
			@ModelAttribute InstituteCycle instituteCycle,
			RedirectAttributes redirectAttrs, Principal principal) {

		String username = principal.getName();
		try {
			InstituteCycle instituteCycleDb = instituteCycleService
					.findByID(instituteCycle.getId());
			instituteCycleDb = LMSHelper.copyNonNullFields(instituteCycleDb,
					instituteCycle);
			instituteCycleDb.setLastModifiedBy(username);

			if ("Y".equalsIgnoreCase(instituteCycle.getLive())) {
				instituteCycleService.makeCyclesInactive(instituteCycle);
			}
			instituteCycleService.updateInstituteCycle(instituteCycleDb);

			setSuccess(redirectAttrs, "Institute Cycle updated successfully");

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(redirectAttrs, "Error in saving Institute Cycle ");
			return "redirect:/addInstituteCycleForm";
		}
		return "redirect:/addInstituteCycleForm";
	}

	/*
	 * @RequestMapping(value = "/addProgram", method = RequestMethod.POST)
	 * public String addProgram(@ModelAttribute Program program,
	 * RedirectAttributes redirectAttrs){ try{ program.setCreatedBy(username);
	 * program.setLastModifiedBy(username);
	 * 
	 * programService.insertWithIdReturn(program);
	 * 
	 * setSuccess(redirectAttrs, "Program added successfully");
	 * 
	 * }catch(Exception e){ logger.error(e.getMessage(), e);
	 * setError(redirectAttrs,"Error in adding Program"); return
	 * "redirect:/addProgramForm"; } return "redirect:/searchProgram"; }
	 * 
	 * 
	 * 
	 * 
	 * @RequestMapping(value = "/searchProgram", method = {RequestMethod.GET,
	 * RequestMethod.POST}) public String
	 * searchProgram(@RequestParam(required=false, defaultValue="1") int pageNo,
	 * Model m, @ModelAttribute Program program){
	 * logger.debug("searchProgramList called." + program);
	 * m.addAttribute("webPage", new WebPage("programList", "View Programs",
	 * false, false));
	 * 
	 * try{ Page<Program> page =
	 * programService.searchActiveByExactMatch(program, pageNo, pageSize);
	 * 
	 * List<Program> programList = page.getPageItems();
	 * 
	 * m.addAttribute("page", page); m.addAttribute("q",
	 * getQueryString(program));
	 * 
	 * 
	 * if(programList == null || programList.size() == 0){
	 * setSuccess(m,"No Programs found"); }
	 * 
	 * }catch(Exception e){ e.printStackTrace(); logger.error(e.getMessage(),
	 * e); setError(m, "Error in getting Program List"); } return
	 * "program/programList"; }
	 * 
	 * @RequestMapping(value = "/deleteProgram", method = {RequestMethod.GET,
	 * RequestMethod.POST}) public String deleteProgram(@RequestParam Integer
	 * programId, RedirectAttributes redirectAttrs){ try{
	 * programService.deleteSoftById(String.valueOf(programId));
	 * setSuccess(redirectAttrs,"Program deleted successfully");
	 * 
	 * }catch(Exception e){ logger.error(e.getMessage(), e);
	 * setError(redirectAttrs, "Error in deleting Exam Center."); }
	 * 
	 * return "redirect:/searchProgram"; }
	 * 
	 * @RequestMapping(value = "/uploadProgramForm", method =
	 * {RequestMethod.GET, RequestMethod.POST}) public String
	 * uploadProgramForm(Model m) { m.addAttribute("webPage",new
	 * WebPage("uploadProgram", "Upload Prorams available in Institute", false,
	 * false, true, true, false)); m.addAttribute("program",new Program());
	 * return "program/uploadProgram"; }
	 * 
	 * @RequestMapping(value = "/uploadProgram", method = RequestMethod.POST)
	 * public String uploadProgram(@ModelAttribute Program program,
	 * @RequestParam("file") MultipartFile file, RedirectAttributes
	 * redirectAttrs){
	 * 
	 * ProgramExcelHelper programExcelHelper = getProgramExcelHelper(); try{
	 * if(!file.isEmpty()){ program.setCreatedBy(username);
	 * programExcelHelper.initHelper(program);
	 * programExcelHelper.readExcel((CommonsMultipartFile)file); }
	 * 
	 * }catch(Exception e){ logger.error(e.getMessage(), e);
	 * setError(redirectAttrs,"Error in uploading File: "+e.getMessage());
	 * return "course/uploadProgramForm"; }
	 * 
	 * if(programExcelHelper.getErrorList().isEmpty()){
	 * setSuccess(redirectAttrs, "File uploaded successfully"); return
	 * "redirect:/searchProgram"; }else{ setErrorList(redirectAttrs,
	 * "Errors encountered uploading file: No Records Added"
	 * ,programExcelHelper.getErrorList()); return
	 * "redirect:/uploadProgramForm"; }
	 * 
	 * }
	 * 
	 * private ProgramExcelHelper getProgramExcelHelper() { return
	 * (ProgramExcelHelper)act.getBean("programExcelHelper"); }
	 */

}
