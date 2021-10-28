package com.spts.lms;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.spts.lms.web.utils.ValidationException;

public class demo {

	public static void main(String[] args) {
		try {
			validateAlphaNumeric("Vishal - kadam");
		} catch (ValidationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub

	}
	public static void validateAlphaNumeric(String s) throws ValidationException{
		//Allows Only Alpha Numeric values except _ and -
	     if (s == null || s.trim().isEmpty()) {
	    	 throw new ValidationException("Input field cannot be empty");
	     }

	     Pattern p = Pattern.compile("[^A-Za-z0-9\\s,_&\\-]");


	     Matcher m = p.matcher(s);
	     boolean b = m.find();
	     if(b) {
	    	 throw new ValidationException("Special characters are not allowed to enter except underscore(_) and hyphen(-).");
	     }
	 }
}
