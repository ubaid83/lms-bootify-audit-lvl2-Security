package com.spts.lms.web.controllers;



import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
 
import java.util.HashMap;
import java.util.Map;
 
@ControllerAdvice
public class ExceptionHandler {
 
    @org.springframework.web.bind.annotation.ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    Map<String,String> showCustomMessage(Exception e){
 
 
        Map<String,String> response = new HashMap<>();
        response.put("status","Your input is invalid");
 
        return response;
    }
}