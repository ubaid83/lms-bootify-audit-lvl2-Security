package com.spts.lms.services.attendance;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.xml.ws.BindingProvider;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.spts.lms.beans.attendance.Attendance;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.daos.assignment.AssignmentDAO;
import com.spts.lms.daos.attendance.AttendanceDAO;
import com.spts.lms.daos.message.StudentMessageDAO;
import com.spts.lms.sap.facultyWorkload.ZFACULTYWORKLOADWS;
import com.spts.lms.sap.facultyWorkload.ZfacultyWorkloadSd;
import com.spts.lms.sap.facultyWorkload.ZfacultyworkloadS;
import com.spts.lms.sap.facultyWorkload.ZfacultyworkloadTt;
import com.spts.lms.services.BaseService;
import com.spts.lms.web.controllers.RestApiController;
import com.spts.lms.web.utils.Utils;

@Service("attendanceService")
public class AttendanceService extends BaseService<Attendance> {

	@Autowired
	private StudentMessageDAO studentMessageDAO;

	@Autowired
	private AssignmentDAO assignmentDAO;

	@Autowired
	private AttendanceDAO attendanceDAO;

	private static final Logger logger = Logger
			.getLogger(AttendanceService.class);
	
	@Override
	public BaseDAO<Attendance> getDAO() {
		return attendanceDAO;
	}
	
	public List<Attendance> findByUser(String username) {
        return attendanceDAO.findByUser(username);
}

	/*
	 * public List<Attendance> getStudentsForAttendance(Long courseId, String
	 * facultyId) { return attendanceDAO.getStudentsForAttendance(courseId,
	 * facultyId); }
	 */
public String pullFacultyWorkload(String eventId,String facultyId){
		
		Date dt = Utils.getInIST();
		SimpleDateFormat formatDate = new SimpleDateFormat(
				"yyyy-MM-dd");
		String currDate = formatDate.format(dt);
		
		ZFACULTYWORKLOADWS ws = new ZFACULTYWORKLOADWS();
		ZfacultyworkloadTt resp = ws.getZFACULTYWORKLOADBIN().zfacultyWorkload(currDate,eventId,facultyId);
		BindingProvider prov = (BindingProvider)ws;
		prov.getRequestContext().put(BindingProvider.USERNAME_PROPERTY, "basissp");
		prov.getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, "india@123");
		String json="";
		for (ZfacultyworkloadS fwtt : resp.getItem()) {
			
			
			String eDate=fwtt.getEDate();
			String eObjid=fwtt.getEObjid();
			String phObjid=fwtt.getPhObjid();
			String allottedLecture=fwtt.getAllottedLecture().toString();
			String conductedLecture=fwtt.getConductedLecture().toString();
			//logger.info("eDate-->"+eDate);
			logger.info("eObjid-->"+eObjid);
			//logger.info("phObjid-->"+phObjid);
			logger.info("allottedLecture-->"+allottedLecture);
			logger.info("conductedLecture-->"+conductedLecture);
			json = new Gson().toJson(fwtt);
			logger.info("json-->"+json);
		}
		return json;
	}

}
