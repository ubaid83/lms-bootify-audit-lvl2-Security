package com.spts.lms.web.utils;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

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

	     Pattern p = Pattern.compile("[^A-Za-z0-9\\s,_&\\-.]");

	     
	     Matcher m = p.matcher(s);
	     boolean b = m.find();
	     
	     
	     if(b) {
	    	 throw new ValidationException("Special characters are not allowed to enter except underscore(_) and hyphen(-).");
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
	    	 
	    	 if(!s.equals("Yes") && !s.equals("No") )
	    		{
	    			throw new ValidationException("Input should be Yes or No");
	    		}
	    		
	    	 
	    	 
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
	    	 throw new ValidationException("Input field cannot be empty");
	     }

		Pattern p = Pattern.compile("[^0-9.]");

	     Matcher m = p.matcher(s);
	     boolean b = m.find();
	     if(b || Double.valueOf(s) < 0.0) {

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
	

	
	public void validateaccesstype(String s) throws ValidationException{
		if (s == null || s.trim().isEmpty()) {
			 throw new ValidationException("Input field cannot be empty");
		 }
		if(!s.equals("Public") && !s.equals("Private") && !s.equals("Everyone")  ) 
		{
			throw new ValidationException("Invalid Announcement  Access type.");
		}
	}


	public static void validateUrl(String url) throws ValidationException{
		System.out.println("link is "  + url);
	        try {
	            new URL(url).toURI();
	        }
	        catch (Exception e) {
	        	 throw new ValidationException("Invalid Url");
	        }
	    }
	
	public static void validateOnlyDate(String date) throws ValidationException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date d1 = null;
		Date d2 = Utils.getInIST();
		String date2 = format.format(d2);
		try {
		d1 = format.parse(date);
		d2 = format.parse(date2);
		if(d1.before(d2)) {
		throw new ValidationException("Invalid date selected.");
		}
		} catch (Exception e) {
		e.printStackTrace();
		throw new ValidationException("Invalid date selected.");
		}
		}

	/*******By sandip 25/10/2021*******/

	public void validateFile(MultipartFile file) throws ValidationException {
		// TODO Auto-generated method stub
		if (file == null || file.isEmpty()) {
	    	 throw new ValidationException("Input field cannot be empty");
	     }
	}
	

	public static void validateRemarks(String s) throws ValidationException{
	     if (s == null || s.trim().isEmpty()) {
	    	 throw new ValidationException("Input field cannot be empty");
	     }
	     s = s.replaceAll("-", "");
	     s = s.replaceAll("_", "");
	     s = s.replaceAll("\\+", "");
	     Pattern p = Pattern.compile("[^A-Za-z0-9-+ ]");
	     Matcher m = p.matcher(s);
	     boolean b = m.find();
	     if(b) {
	    	 throw new ValidationException("Special characters are not allowed.");
	     }
	 }
	
	
}

