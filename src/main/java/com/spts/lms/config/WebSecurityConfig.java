package com.spts.lms.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import com.spts.lms.auth.CustomAuthenticationProvider;
import com.spts.lms.filter.XSSFilter;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	private Logger logger = LoggerFactory.getLogger(WebSecurityConfig.class);

	@Autowired
	CustomAuthenticationProvider authenticationProvider;

	@Override
	protected void configure(AuthenticationManagerBuilder auth)
			throws Exception {
		logger.info("configure AuthenticationManagerBuilder");
		auth.authenticationProvider(authenticationProvider);
	}

	/*
	 * @Configuration
	 * 
	 * @Order(1) public static class ApiWebSecurityConfigurationAdapter extends
	 * WebSecurityConfigurerAdapter { protected void configure(HttpSecurity
	 * http) throws Exception { http.authorizeRequests()
	 * .antMatchers("/handShake") .permitAll() .antMatchers("/", "/homepage")
	 * .access(
	 * "hasRole('ROLE_ADMIN') or hasRole('ROLE_FACULTY') or hasRole('ROLE_STUDENT') or hasRole('ROLE_PARENT') "
	 * ) .and().httpBasic(); } }
	 */
	
	@Bean
	public FilterRegistrationBean xssPreventFilter() {
	    FilterRegistrationBean registrationBean = new FilterRegistrationBean();

	    registrationBean.setFilter(new XSSFilter());
	    registrationBean.addUrlPatterns("/*");

	    return registrationBean;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		logger.info("configure");
		http.csrf().disable();
		http.authorizeRequests().antMatchers("/handShake").permitAll()
				.antMatchers("/resetPasswordForm", "/resetPassword")
				.permitAll().antMatchers("/profileDetails").permitAll()
				.antMatchers("/loggedout").permitAll();

		http.authorizeRequests()
				.antMatchers("/resources/**", "/registration","/api/**", "/getCourseByUsernameForApp", "/getProgramsByUsernameForApp", "/getCourseByUsernameAndProgramForApp", "/getStudentsByCourseForApp","/insertStudentAttendanceForApp","/showStudentAttendanceForApp","/sendTimetableNotificationForApp","/insertUserPlayerIdForApp","/samlRequestTCS","/redirect","/getTimetableByCourseForApp","/deleteUserPlayerIdForApp","/getAnnouncementListForApp", "/getNewsListForApp", "/getEventsListForApp", "/getAttendanceStatForApp","/downloadFileForApp","/submitAssignmentForApp","/getAssignmentListForApp","/updateStudentAttendanceForApp","/submitAssignmentByOneInGroupForApp","/submitAssignmentByIdForApp","/sendAnnouncementFileForApp","/downloadAttendanceReport","/featureWiseSummaryUtilityReport","/sendAttendanceDataBySAP","/updateProfileForApp","/changePasswordForApp","/showTrainingSession","/insertTrainigAttendance","/showInternalTotalMarksForApp","/showInternalComponentMarksForApp","/showExamTimetableForApp","/getTimetableByCourseForAndroidApp","/getStudentsByCourseForAndroidApp","/showStudentAttendanceForAndroidApp","/showStudentAttendanceStatusForAndroidApp","/insertStudentAttendanceForAndroidApp","/updateStudentAttendanceForAndroidApp","/getAttendanceDataSentToSapForApp","/sendAttendanceDataToSapDemo","/getTestDetailsForAndroidApp","/sendNotificationForAttendanceSync","/sendNotificationForAttendanceSyncService","/getStudentsByCourseForAndroidAppNew","/showStudentAttendanceForAndroidAppNew","/getCompleteLectureAndStudentListCourseWise","/sendAttendanceDataToSapByApp","/updateProfileForAppUatDev")
				.permitAll()
				.antMatchers("/createAssignmentFromJson/**",
						"/allocateAssignmentToStudentsByParam/**",
						"/getCourses/**", "/createTestFromJson/**",
						"/allocateTestToStudentsByParam/**",
						"/getCoursesForBB/**", "/uploadQuestionForTest/**",
						"/createContentFromJSON/**",
						"/createNewContentFromJSON/**", 
						"/getStudentFeedbackReportByFacultyId/**",
						"/getStudentFeedbackReportBySchoolInBulk/**")
				.permitAll()
				.antMatchers("/", "/homepage")
				.access("hasRole('ROLE_ADMIN') or hasRole('ROLE_FACULTY') or hasRole('ROLE_DEAN') or hasRole('ROLE_HOD') or hasRole('ROLE_STUDENT') or hasRole('ROLE_PARENT') or hasRole('ROLE_CORD') or hasRole('ROLE_AREA_INCHARGE') or hasRole('ROLE_AR') or hasRole('ROLE_EXAM') or hasRole('ROLE_LIBRARIAN') or hasRole('ROLE_COUNSELOR') or hasRole('ROLE_SUPPORT_ADMIN') or hasRole('ROLE_STAFF') or hasRole('ROLE_INTL') or hasRole('ROLE_EXAM_ADMIN') or hasRole('ROLE_IT') or hasRole('ROLE_SUPPORT_ADMIN_REPORT')")
				.anyRequest().authenticated().and().formLogin()
				.loginPage("/login").permitAll().and().logout().permitAll();

		http.exceptionHandling().accessDeniedPage("/403");

	}

}
