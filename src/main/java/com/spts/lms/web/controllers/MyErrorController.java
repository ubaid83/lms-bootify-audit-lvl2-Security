package com.spts.lms.web.controllers;



import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;



@Controller
public class MyErrorController implements ErrorController {

	private static final Logger logger = Logger
			.getLogger(MyErrorController.class);
	
	@RequestMapping("/error")
	public String handleError(HttpServletRequest request,Model m) {
		// do something like logging
		 Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
		 int statusCode = 0;
		 if (status != null) {
		         statusCode = Integer.valueOf(status.toString());
		 }
		 m.addAttribute("status", String.valueOf(statusCode));
		logger.info("error called");
		return "error";
	}

	@Override
	public String getErrorPath() {
		return "/error";
	}

}
