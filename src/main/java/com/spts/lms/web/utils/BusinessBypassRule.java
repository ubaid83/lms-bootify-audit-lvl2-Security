package com.spts.lms.web.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.spts.lms.beans.course.Course;
import com.spts.lms.services.course.CourseService;

public class BusinessBypassRule {
	
	private static final Logger logger = Logger.getLogger(BusinessBypassRule.class);
	
	public static void validateAlphaNumeric(String s) throws ValidationException{
		//Allows Only Alpha Numeric values except _ and -
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
	public static void validateNumericNotAZero(String s) throws ValidationException{
		//Allows Only Double Positive Numbers as String, Zero not allowed
		if (s == null || s.trim().isEmpty()) {
	    	 throw new ValidationException("Input field cannot be empty");
	     }
		if(Double.valueOf(s) <= 0.0) {
	    	 throw new ValidationException("Input number should be a positive number and non zero number.");
	     }  
	 }
	public static void validateNumeric(String s) throws ValidationException{
		//Allows Only Double Positive Numbers as String, Zero allowed
		if (s == null || s.trim().isEmpty()) {
	    	 throw new ValidationException("Input field cannot be empty");
	     }
	     
	     if(Double.valueOf(s) < 0.0) {
	    	 throw new ValidationException("Input number should be a positive number.");
	     }  
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
}
