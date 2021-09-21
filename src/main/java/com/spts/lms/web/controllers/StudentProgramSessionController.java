package com.spts.lms.web.controllers;

import java.security.Principal;

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

import com.spts.lms.beans.student.StudentProgramSession;
import com.spts.lms.helpers.excel.StudentProgramSessionExcelHelper;
import com.spts.lms.web.helper.WebPage;

@Secured("ROLE_ADMIN")
@Controller
public class StudentProgramSessionController extends BaseController {
	
	@Autowired
	ApplicationContext act;
	
	private StudentProgramSessionExcelHelper getStudentProgramSessionExcelHelper() {
		return (StudentProgramSessionExcelHelper)act.getBean("studentProgramSessionExcelHelper");
	}
	
	private static final Logger logger = Logger.getLogger(StudentProgramSessionController.class);
	
	
	@RequestMapping(value = "/uploadStudentProgramSessionForm", method = {RequestMethod.GET, RequestMethod.POST})
	public String uploadStudentProgramSessionForm(Model m) {
		m.addAttribute("webPage",new WebPage("uploadStudentProgramSession", "Upload Student Program Sessions", false, false));
		m.addAttribute("studentProgramSession",new StudentProgramSession());
		return "program/uploadStudentProgramSession";
	}
	
	
	@RequestMapping(value = "/uploadStudentProgramSession", method = RequestMethod.POST)
	public String uploadStudentProgramSession(@ModelAttribute StudentProgramSession studentProgramSession,Principal principal, @RequestParam("file") MultipartFile file, RedirectAttributes redirectAttrs){
		StudentProgramSessionExcelHelper studentProgramSessionExcelHelper = getStudentProgramSessionExcelHelper();
		String username=principal.getName();
		try{
			if(!file.isEmpty()){
				studentProgramSession.setCreatedBy(username);
				studentProgramSessionExcelHelper.initHelper(studentProgramSession);
				studentProgramSessionExcelHelper.readExcel((CommonsMultipartFile)file);
			}
			
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			setError(redirectAttrs,"Error in adding Program");
			return "redirect:/uploadStudentProgramSessionForm";
		}
		if(studentProgramSessionExcelHelper.getErrorList().isEmpty())
			setSuccess(redirectAttrs, "File uploaded successfully");
		else
			setErrorList(redirectAttrs, "Errors encountered uploading file: No Records Added",studentProgramSessionExcelHelper.getErrorList());
		return "redirect:/uploadStudentProgramSessionForm";
	}
	
}
