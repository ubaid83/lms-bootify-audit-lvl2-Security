package com.spts.lms.web.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spts.lms.beans.course.Course;
import com.spts.lms.services.course.CourseService;
@Component
public class BusinessBypassRule {
	
	private static final Logger logger = Logger.getLogger(BusinessBypassRule.class);
	
	public static void validateAlphaNumeric(String s) throws ValidationException{
	     if (s == null || s.trim().isEmpty()) {
	    	 throw new ValidationException("Input field cannot be empty");
	     }
	     s = s.replaceAll("-", " ");
	     s = s.replaceAll("_", " ");
	     Pattern p = Pattern.compile("[^A-Za-z0-9]");
	     Matcher m = p.matcher(s);
	     boolean b = m.find();
	     if(b) {
	    	 throw new ValidationException("Special characters are not allowed to enter except underscore(_) and hyphen(-).");
	     }
	 }
	public static void validateNumeric(String s) throws ValidationException{
		if (s == null || s.trim().isEmpty()) {
	    	 throw new ValidationException("Input field cannot be empty");
	     }
	     Pattern p = Pattern.compile("[^0-9]");
	     Matcher m = p.matcher(s);
	     boolean b = m.find();
	     if(b) {
	    	 throw new ValidationException("Input is not a number.");
	     }  
	 }
	
	public static void validateYesOrNo(String s) throws ValidationException{
		if (s == null || s.trim().isEmpty()) {
	    	 throw new ValidationException("Input field cannot be empty");
	     }
		Pattern p = Pattern.compile("[ny]");
	     Matcher m = p.matcher(s);
	     boolean b = m.find();
	     if(b) {
	    	 throw new ValidationException("Input should be Y or N.");
	     }  
	 }
	
}
