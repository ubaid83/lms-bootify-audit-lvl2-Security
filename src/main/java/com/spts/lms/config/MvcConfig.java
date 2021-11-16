package com.spts.lms.config;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import com.spts.lms.services.interceptor.LmsInterceptor;

@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter {

	private static final Logger logger = Logger.getLogger(MvcConfig.class);

	@Override
	public void configureDefaultServletHandling(
			DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}

	@Value("#{'${spring.datasource.url:nmims}'}")
	String url;

	@Value("#{'${spring.datasource.username:nmims}'}")
	String username;

	@Value("#{'${spring.datasource.password:nmims}'}")
	String password;

	@Value("#{'${imgPath}'}")
	String imgPath;

	@Value("#{'${workDir}'}")
	private String workDir;
	
	

	@Bean
	WebMvcConfigurer configurer() {
		return new WebMvcConfigurerAdapter() {
			@Override
			public void addResourceHandlers(ResourceHandlerRegistry registry) {
				registry.addResourceHandler("/savedImages/**")
						.addResourceLocations(imgPath);

				registry.addResourceHandler("/workDir/**")
						.addResourceLocations(workDir);
			}
		};
	}

	@Bean(name = "dataSource")
	public DriverManagerDataSource dataSource() {
		DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
		driverManagerDataSource.setDriverClassName("com.mysql.jdbc.Driver");

		driverManagerDataSource.setUrl(url);
		driverManagerDataSource.setUsername(username);
		driverManagerDataSource.setPassword(password);

		/*
		 * driverManagerDataSource .setUrl(
		 * "jdbc:mysql://b5b1f0363be955:3d143df7@us-cdbr-iron-east-03.cleardb.net:3306/ad_b122dab8e7705f6"
		 * ); driverManagerDataSource.setUsername("b5b1f0363be955");
		 * driverManagerDataSource.setPassword("3d143df7");
		 */

		return driverManagerDataSource;
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder(11);
	}

	@Bean
	public InternalResourceViewResolver viewResolver() {
		InternalResourceViewResolver resolver = new InternalResourceViewResolver();
		resolver.setViewClass(JstlView.class);
		resolver.setPrefix("WEB-INF/views/");
		resolver.setSuffix(".jsp");
		return resolver;
	}

	@Bean
	public JavaMailSender javaMailSender() {
		JavaMailSender sender = new org.springframework.mail.javamail.JavaMailSenderImpl();
		return sender;
	}

	@Bean
	public SimpleMailMessage getSimpleMailMessage() {
		return new org.springframework.mail.SimpleMailMessage();
	}
	
	@Bean
	public LmsInterceptor lmsInterceptor() {
		return new LmsInterceptor();
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// TODO Auto-generated method stub
		registry.addInterceptor(lmsInterceptor()).excludePathPatterns("/handShake","/login","/error").addPathPatterns("/*");
		super.addInterceptors(registry);
	}
}