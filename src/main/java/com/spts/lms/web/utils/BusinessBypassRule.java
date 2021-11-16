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
		//Allows Only Alpha Numeric values except _ and -
	     if (s == null || s.trim().isEmpty()) {
	    	 throw new ValidationException("Input field cannot be empty");
	     }

	     Pattern p = Pattern.compile("[^A-Za-z0-9\\s\\-,_&]");

	     Matcher m = p.matcher(s);
	     boolean b = m.find();
	     
	     
	     if(b) {
	    	 throw new ValidationException("Special characters are not allowed to enter except underscore(_) and hyphen(-).");
	     }
	 }
	public static void validateYOrN(String flag) throws ValidationException {
		logger.info("flag-"+flag);
	logger.info(!flag.equals("Yes"));
	logger.info(!flag.equals("No"));
	
	if(!flag.equals("Yes") && !flag.equals("No") )
	{
		throw new ValidationException("Input should be Yes or No");
	}
	
	}
	public static void validateYesOrNo(String s) throws ValidationException{
		logger.info("String is " + s);
		if (s == null || s.trim().isEmpty()) {
	    	 throw new ValidationException("Input field cannot be empty");
	     }
		Pattern p = Pattern.compile("[^nyNY]");
	     Matcher m = p.matcher(s);
	     boolean b = m.find();
	     logger.info("boolean is " + b);
	     if(b) {
	    	 throw new ValidationException("Input should be Y or N.");
	     }
	 }
	public static void validateNumericNotAZero(String s) throws ValidationException{
		//Allows Only Double Positive Numbers as String, Zero not allowed
		if (s == null || s.trim().isEmpty()) {
	    	 throw new ValidationException("Input field cannot be empty");
	     }
		Pattern p = Pattern.compile("[^0-9.]");
	     Matcher m = p.matcher(s);
	     boolean b = m.find();
		if(b || Double.valueOf(s) <= 0.0) {
	    	 throw new ValidationException("Input number should be a positive number and non zero number.");
	     }  
	 }
	public static void validateNumeric(String s) throws ValidationException{
		//Allows Only Double Positive Numbers as String, Zero allowed
		if (s == null || s.trim().isEmpty()) {
			logger.info("empty------");
	    	 throw new ValidationException("Input field cannot be empty");
	     }
		Pattern p = Pattern.compile("[^0-9.]");
	     Matcher m = p.matcher(s);
	     boolean b = m.find();
	     if(b || Double.valueOf(s) < 0.0) {
	    	 throw new ValidationException("Input number should be a positive number.");
	     }
	     System.out.println("b---"+b);
	 }

	public static void validateNumericNotAZero(double d) throws ValidationException{
		//Allows Only Double Positive Numbers as double, Zero not allowed
		if(d <= 0.0) {
	    	 throw new ValidationException("Input number should be a positive number and non zero number.");
	     }  
	 }
	public static void validateNumeric(double d) throws ValidationException{
		//Allows Only Double Positive Numbers as double, Zero allowed
		if(d < 0.0) {
	    	 throw new ValidationException("Input number should be a positive number.");
	     }  
	 }
	public static void validateNumericNotAZero(long d) throws ValidationException{
		//Allows Only long Positive Numbers as long, Zero not allowed
		if(d <= 0) {
	    	 throw new ValidationException("Input number should be a positive number and non zero number.");
	     }  
	 }
	public static void validateNumeric(long d) throws ValidationException{
		//Allows Only Double Positive Numbers as long, Zero allowed
		if(d < 0) {
	    	 throw new ValidationException("Input number should be a positive number.");
	     }  
	 }
	
	
	public void validateaccesstype(String s) throws ValidationException{
		if (s == null || s.trim().isEmpty()) {
			 throw new ValidationException("Input field cannot be empty");
		 }
		if(!s.equals("Public") && !s.equals("Private") && !s.equals("Everyone")  ) 
		{
			throw new ValidationException("Invalid Announcement SubType.");
		}
	}
}
