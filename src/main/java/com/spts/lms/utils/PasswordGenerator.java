package com.spts.lms.utils;
import org.apache.log4j.Logger;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;



public class PasswordGenerator {
	 // Keep same as configured in applicationContext-security.xml file
	private final static int STRENGTH = 11;
	private final static BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(STRENGTH);
	
	private static final Logger logger = Logger
			.getLogger(PasswordGenerator.class);
	public static String generatePassword(String pass) {
		return passwordEncoder.encode(pass);
	}
	
	public static boolean matches(String rawPassword, String encodedPassword) {
		return passwordEncoder.matches(rawPassword, encodedPassword);
	}
	
	public static void main(String[] args) {
		
		logger.info(generatePassword("pass@123"));
	}

}
