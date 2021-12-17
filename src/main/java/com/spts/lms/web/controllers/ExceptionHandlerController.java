package com.spts.lms.web.controllers;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class ExceptionHandlerController {
	
	 	@ExceptionHandler(Exception.class)
	    public String handleException(HttpServletRequest request,HttpServletResponse response,Model m) 
	    {
			 m.addAttribute("status", "Exception - 500");
			 return "error";
	    }

	 	@ExceptionHandler(BindException.class)
	    public String bindException(HttpServletRequest request,HttpServletResponse response,Model m) 
	    {
			 m.addAttribute("status", "BindException - 400");
			 return "error";
	    }
	 	@ExceptionHandler(ConversionNotSupportedException.class)
	    public String conversionNotSupportedException(HttpServletRequest request,HttpServletResponse response,Model m) 
	    {
			 m.addAttribute("status", "ConversionNotSupportedException - 500");
			 return "error";
	    }
	 	@ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
	    public String httpMediaTypeNotAcceptableException(HttpServletRequest request,HttpServletResponse response,Model m) 
	    {
			 m.addAttribute("status", "HttpMediaTypeNotAcceptableException - 406");
			 return "error";
	    }
	 	@ExceptionHandler(HttpMediaTypeNotSupportedException.class)
	    public String httpMediaTypeNotSupportedException(HttpServletRequest request,HttpServletResponse response,Model m) 
	    {
			 m.addAttribute("status", "HttpMediaTypeNotSupportedException - 415");
			 return "error";
	    }
	 	@ExceptionHandler(HttpMessageNotReadableException.class)
	    public String httpMessageNotReadableException(HttpServletRequest request,HttpServletResponse response,Model m) 
	    {
			 m.addAttribute("status", "HttpMessageNotReadableException - 400");
			 return "error";
	    }
	 	@ExceptionHandler(HttpMessageNotWritableException.class)
	    public String httpMessageNotWritableException(HttpServletRequest request,HttpServletResponse response,Model m) 
	    {
			 m.addAttribute("status", "HttpMessageNotWritableException - 500");
			 return "error";
	    }
	 	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	    public String httpRequestMethodNotSupportedException(HttpServletRequest request,HttpServletResponse response,Model m) 
	    {
			 m.addAttribute("status", "HttpRequestMethodNotSupportedException - 405");
			 return "error";
	    }
	 	@ExceptionHandler(MethodArgumentNotValidException.class)
	    public String methodArgumentNotValidException(HttpServletRequest request,HttpServletResponse response,Model m) 
	    {
			 m.addAttribute("status", "MethodArgumentNotValidException - 400");
			 return "error";
	    }
	 	
	 	@ExceptionHandler(MissingServletRequestParameterException.class)
	    public String missingServletRequestParameterException(HttpServletRequest request,HttpServletResponse response,Model m) 
	    {
			 m.addAttribute("status", "MissingServletRequestParameterException - 400");
			 return "error";
	    }
	 	@ExceptionHandler(MissingServletRequestPartException.class)
	    public String missingServletRequestPartException(HttpServletRequest request,HttpServletResponse response,Model m) 
	    {
			 m.addAttribute("status", "MissingServletRequestPartException - 400");
			 return "error";
	    }
	 	@ExceptionHandler(NoHandlerFoundException.class)
	    public String noSuchRequestHandlingMethodException(HttpServletRequest request,HttpServletResponse response,Model m) 
	    {
			 m.addAttribute("status", "NoSuchRequestHandlingMethodException - 404");
			 return "error";
	    }
	 	@ExceptionHandler(TypeMismatchException.class)
	    public String typeMismatchException(HttpServletRequest request,HttpServletResponse response,Model m) 
	    {
			 m.addAttribute("status", "TypeMismatchException - 400");
			 return "error";
	    }
	 	@ExceptionHandler(IllegalArgumentException.class)
	    public String IllegalArgumentException(HttpServletRequest request,HttpServletResponse response,Model m) 
	    {
			 m.addAttribute("status", "IllegalArgumentException - 400");
			 return "error";
	    }
}
