package com.spts.lms.web.utils;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

public class HtmlValidation {
	
	private static final Logger logger = Logger.getLogger(HtmlValidation.class);
	
	public static void validateHtml(Object obj,List<String> fieldsNotToValidate) throws ValidationException{  
		
		ObjectMapper map = new ObjectMapper();
		Map<String,Object> propMap = map.convertValue(obj, Map.class);
		
		logger.info("prop map"+propMap);
		
		
	    if(checkHtmlCode(propMap,fieldsNotToValidate)){  
	  
	        // throw an object of user defined exception  
	        throw new ValidationException("Html/Scripts are not allowed in Form");    
	    }  
	       else {   
	        System.out.println("welcome to vote");   
	        }   
	     } 
	
	
	public static boolean checkHtmlCode(
			 Map<String,Object> mapper,List<String> fieldsNotToValidate) {
		//<("[^"]*"|'[^']*'|[^'">])*>
		
		Pattern pattern = Pattern.compile("<?[a-z^A-Z][\\s\\S]*>");
		for(String s: mapper.keySet()){
			if(fieldsNotToValidate.contains(s) || mapper.get(s)==null){
				continue;
			}
			else{
				Matcher matcher = pattern.matcher(String.valueOf(mapper.get(s)));
				
				if(matcher.matches()){
					return true;
				}
			}
		}
		
		return false;
	}
	
	public static boolean checkHtmlCode(String s) throws ValidationException{
		
		Pattern pattern = Pattern.compile("<?[a-z^A-Z][\\s\\S]*>");
		Matcher matcher = pattern.matcher(s);
		if(matcher.matches()){
			throw new ValidationException("Html/Scripts are not allowed in Form"); 
		}else{
			return false;
		}
	}

}
