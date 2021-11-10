package com.spts.lms.web.utils;

import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.google.gson.Gson;

@RestControllerAdvice
public class ExceptionResolver {
	
	@Value("#{'${aes.secretKey}'}")
	String secretKey;

	@Value("#{'${aes.saltKey}'}")
	String salt;

    @ExceptionHandler(Exception.class)
    public String handleException(HttpServletRequest request, Exception e) {
        HashMap<String, String> response = new HashMap<>();
        response.put("message", "Seems like request body/param has been hampered.");
        String json = new Gson().toJson(response);
		json = encryptResponseBody(json);
        return json;
    }

    @ExceptionHandler(MissingPathVariableException.class)
    public String handleMissingPathVariableException(HttpServletRequest request, MissingPathVariableException e) {
        HashMap<String, String> response = new HashMap<>();
        response.put("message", "Required path variable is missing in this request. Please add it to your request.");
        String json = new Gson().toJson(response);
		json = encryptResponseBody(json);
        return json;
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public String handleNotFoundResourceException(HttpServletRequest request, NoHandlerFoundException e) {
        HashMap<String, String> response = new HashMap<>();
        response.put("message", "Requested resource wasn't found on the server.");
        String json = new Gson().toJson(response);
		json = encryptResponseBody(json);
        return json;
    }
    
    private String encryptResponseBody(String json) {
		String encryptedStr = "";
		try {
			AESEncryption aes = new AESEncryption(secretKey, salt);
			encryptedStr = aes.encrypt(json);
		} catch (Exception e) {
			// e.printStackTrace();
		}
		return encryptedStr;
	}
}