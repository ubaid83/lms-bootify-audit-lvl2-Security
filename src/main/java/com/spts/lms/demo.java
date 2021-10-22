package com.spts.lms;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.spts.lms.web.utils.ValidationException;

public class demo {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			validateAlphaNumeric("yes");
		} catch (ValidationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void validateAlphaNumeric(String s) throws ValidationException{
		//Allows Only Alpha Numeric values except _ and -
	     s = s.replaceAll("-", " ");
	     s = s.replaceAll("_", " ");
	     Pattern p = Pattern.compile("[^ny]");
	     Matcher m = p.matcher(s);
	     boolean b = m.find();
	     if(b) {
	    	 throw new ValidationException("Special characters are not allowed to enter except underscore(_) and hyphen(-).");
	     }
	 }
}
