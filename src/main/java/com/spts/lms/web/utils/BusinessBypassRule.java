package com.spts.lms.web.utils;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
//import org.springframework.web.multipart.MultipartFile;

//import org.springframework.stereotype.Component;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;

//import com.spts.lms.beans.course.Course;
//import com.spts.lms.services.course.CourseService;

@Component
public class BusinessBypassRule {
	
	private static final Logger logger = Logger.getLogger(BusinessBypassRule.class);
	
	
	
	public static void validateAlphaNumeric(String s) throws ValidationException{
		//Allows Only Alpha Numeric values except _ and -
	     if (s == null || s.trim().isEmpty()) {
	    	 throw new ValidationException("Input field cannot be empty");
	     }

	     Pattern p = Pattern.compile("[^A-Za-z0-9\\s,_&\\-.()+]");
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
			throw new ValidationException("Invalid  Access type.");
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
			if (d1.before(d2)) {
				throw new ValidationException("Invalid date selected.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new ValidationException("Invalid date selected.");
		}
	}
	
	
	//update by sandip
	public static void validateFile(MultipartFile file) throws ValidationException {
		if (file == null || file.isEmpty()) {
	    	 throw new ValidationException("File cannot be empty!");
	     }

	}
	

	public static void validateEmail(String s) throws ValidationException{
	     if (s == null || s.trim().isEmpty()) {
	    	 throw new ValidationException("Input field cannot be empty");
	     }
	     Pattern p = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$");
	     Matcher m = p.matcher(s);
	     boolean b = m.find();
	     if(b) {
	    	 throw new ValidationException("Invalid Email Address");
	     }
	 }
	
	public static void validateQuestion(String s) throws ValidationException{
	     if (s == null || s.trim().isEmpty()) {
	    	 throw new ValidationException("Input field cannot be empty");
	     }
	     Pattern p = Pattern.compile("[^A-Za-z0-9\\s,_&\\-.?/\\(\\)]");
	     Matcher m = p.matcher(s);
	     boolean b = m.find();
	     if(b) {
	    	 throw new ValidationException("Special characters are not allowed to enter except (),?,/");
	     }
	 }

	public static void validateRatings(String s) throws ValidationException {
		if (s == null || s.trim().isEmpty()) {
	    	 throw new ValidationException("Input field cannot be empty");
	    }
		Integer rating = Integer.valueOf(s);
		if(rating>7 || rating <1){
			throw new ValidationException("Please Rate Between 1 to 7");
		}
		
	}
	
	//Peter 05/12/2021
	public static void validateStartAndEndDatesToUpdate(String date1, String date2) throws ValidationException {

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d1 = null;
		Date d2 = null;
		Date d3 = Utils.getInIST();
		String date3 = format.format(d3);
		date3 = date3.split(" ")[0].concat(" 00:00:00");
		try {
			if (date1.contains("T")) {
				date1 = date1.replace("T", " ");
			}
			if (date2.contains("T")) {
				date2 = date2.replace("T", " ");
			}
			d1 = format.parse(date1);
			d2 = format.parse(date2);
			d3 = format.parse(date3);
			if (d1.after(d2)) {
//				System.out.println("False - startDate after endDate");
				throw new ValidationException("Invalid Start date and End date.");
			}
			if (d1.compareTo(d2) == 0) {
//				System.out.println("False - startDate equals endDate");
				throw new ValidationException("Invalid Start date and End date.");
			}
			if (d2.before(d3)) {
//				System.out.println("False - endDate before currentDate");
				throw new ValidationException("Invalid Start date and End date.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new ValidationException("Invalid Start date and End date.");
		}
	}
	
	//Peter 05/12/2021
	public static void validateMarks(String marks, String marks2) throws ValidationException{
		
		if (marks == null || marks2 == null|| marks.trim().isEmpty() || marks2.trim().isEmpty()) {
	    	 throw new ValidationException("Input field cannot be empty");
	    }
		
		Integer m1 = Integer.valueOf(marks); //Marks
		Integer m2 = Integer.valueOf(marks2); //Pass Marks
		
		if (m1 <= 0 || m2 <= 0) {
	    	 throw new ValidationException("Input number should be a positive number");
	    }
		
		if (m1<m2) {
			//Internal/External Marks is Less than Pass Marks
	    	 throw new ValidationException("Marks greater than Pass Marks");
	    }
		if (m1==m2) {
			//Internal/External Marks is Less than Pass Marks
	    	 throw new ValidationException("Marks are Equal to Pass Marks");
	    }
	 }
	
	//Peter 05/12/2021
	public static void validateMarks(String marks1, String marks2, String marks3, String marks4, String marks5) throws ValidationException{
		
		if (marks1 == null || marks2 == null || marks3 == null || marks3 == null || marks4 == null || marks5 == null ||
				marks1.trim().isEmpty() || marks2.trim().isEmpty() || marks3.trim().isEmpty() || marks4.trim().isEmpty() || marks5.trim().isEmpty()) {
	    	 throw new ValidationException("Input field cannot be empty");
	    }
		Integer m1 = Integer.valueOf(marks1); //Internal Marks
		Integer m2 = Integer.valueOf(marks2); //Internal Pass Marks
		
		Integer m3 = Integer.valueOf(marks3); //External Marks
		Integer m4 = Integer.valueOf(marks4); //External Pass Marks
		
		Integer m5 = Integer.valueOf(marks5); //Total Marks
		
		if (m1 <= 0 || m2 <= 0 || m3 <= 0 || m4 <= 0 || m5 <= 0) {
	    	 throw new ValidationException("Input number should be a positive number");
	    }
		
		if(m1<m2 || m3<m4) {
	    	 throw new ValidationException("Marks is Less than Pass Marks");
	    }
		if(m2>m5 || m4>m5) {
	    	 throw new ValidationException("Pass Marks Greater than Total Marks");
	    }
		if(m5<m1 || m5<m3) {
			throw new ValidationException("Total Marks less than Marks");
		}
		if(m1+m3!=m5) {
			logger.info("Invalid Total Marks");
			throw new ValidationException("Internal & External Marks does not equal Total Marks");
		}
		if(m1==m2 || m3==m4){
			throw new ValidationException("Marks are equal to Pass Marks");
		}
	 }

}

