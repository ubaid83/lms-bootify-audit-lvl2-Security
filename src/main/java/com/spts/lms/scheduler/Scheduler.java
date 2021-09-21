package com.spts.lms.scheduler;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import com.spts.lms.beans.StudentService.BonafideForm;
import com.spts.lms.beans.StudentService.RailwayForm;
import com.spts.lms.services.StudentService.BonafideFormService;
import com.spts.lms.services.StudentService.RailwayFormService;
import com.spts.lms.services.StudentService.StudentService;
import com.spts.lms.services.attendance.StudentCourseAttendanceService;
import com.spts.lms.services.notification.Notifier;
import com.spts.lms.web.controllers.LoginController;
import com.spts.lms.web.utils.Utils;

@Service
@EnableScheduling
@RestController
public class Scheduler {

	static Logger logger = LoggerFactory.getLogger(Scheduler.class);
	@Autowired
	BonafideFormService bonafideFormServcie;

	@Autowired
	StudentService studentWsService;
	
	@Autowired
	RailwayFormService railwayFormService;

	@Autowired
	Notifier notifier;
	
	@Autowired
	LoginController loginController;

	@Autowired
	StudentCourseAttendanceService studentCourseAttendanceService;
	



	/*@Scheduled(cron = "0 0 10-20/2 * * ?", zone = "IST")

	public void cronTask() throws SQLException {

		studentCourseAttendanceService.sendAttendanceToSAP();
	}
	
	@Scheduled(cron = "0 30 23 * * ?", zone = "IST")
	public void sendFinalAttendanceCron() throws SQLException {
		logger.info("Started 11--->" + Utils.getInIST());
		studentCourseAttendanceService.sendLastAttendanceToSAP();
		logger.info("Ended 11--->" + Utils.getInIST());
	}*/
	
	/*@Scheduled(cron = "0 0 * * * ?", zone = "IST")
	public void cronTask() throws SQLException {

		bonafideFormServcie.pendingBonafideEmail();
		
		railwayFormService.pendingRCEmail();
	}*/
	
	//@Scheduled(cron = "0 */15 * * * ?", zone = "IST")
	/*public void sendNotificationCron() throws SQLException {
		
		loginController.sendTimetableNotificationForApp();
		
	}*/

}
