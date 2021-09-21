package com.spts.lms.web.controllers;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.spts.lms.auth.Token;
import com.spts.lms.beans.user.Role;
import com.spts.lms.beans.variables.LmsVariables;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.services.BaseService;
import com.spts.lms.services.variables.LmsVariablesService;
import com.spts.lms.web.helper.WebPage;

@Controller
public class LmsVariablesController {
	
	@Autowired
	LmsVariablesService lmsVariableService;
	
	@RequestMapping(value = "/lmsVariablesListBySupportAdmin", method = { RequestMethod.GET, RequestMethod.POST })
	public String lmsVariablesListBySupportAdmin(Principal principal, Model m) {
		List<LmsVariables> lmsVariablesList = new ArrayList<>();
		Token userdetails = (Token) principal;
		if((userdetails.getAuthorities().contains(Role.ROLE_SUPPORT_ADMIN))) {
			lmsVariablesList = lmsVariableService.findAll(); //.findLmsVariablesListForSupportAdmin();
		}
		m.addAttribute("lmsVariablesList", lmsVariablesList);
		m.addAttribute("webPage", new WebPage("searchLmsVariables", "Search LMS Variables", true, false));
		return "variables/lmsVariablesListSupportAdmin";
	}
	
	@RequestMapping(value = "/updateLmsVariableBySupportAdminForm", method = { RequestMethod.GET, RequestMethod.POST })
	public String updateVariableBySupportAdminForm(Model m, @RequestParam Long lmsVariableId) {
		LmsVariables lmsVariable = lmsVariableService.findByID(lmsVariableId); //.findLmsVariableByIdForSupportAdmin(lmsVariableId);
		m.addAttribute("lmsVariable",lmsVariable);
		m.addAttribute("webPage", new WebPage("updateLmsVariables", "Update LMS Variables", true, false));
		return "variables/updateLmsVariableBySupportAdmin";
	}
	
	@RequestMapping(value = "/updateLmsVariableBySupportAdmin", method = { RequestMethod.GET, RequestMethod.POST })
	public String updateVariableBySupportAdmin(@ModelAttribute LmsVariables lmsVariables, Model m) {
		lmsVariableService.updateLmsVariable(lmsVariables.getId(),lmsVariables.getValue(),lmsVariables.getActive());
		return "redirect:/lmsVariablesListBySupportAdmin";
	}
}
