package com.spts.lms.services.attendance;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spts.lms.beans.attendance.Attendance;
import com.spts.lms.beans.attendance.RtasAttendance;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.daos.assignment.AssignmentDAO;
import com.spts.lms.daos.attendance.AttendanceDAO;
import com.spts.lms.daos.attendance.RtasAttendanceDAO;
import com.spts.lms.daos.message.StudentMessageDAO;
import com.spts.lms.services.BaseService;


@Service("rtasAttendanceService")
public class RtasAttendanceService extends BaseService<RtasAttendance> {

	@Autowired
	private RtasAttendanceDAO rtasAttendanceDAO;
	

	@Override
	public BaseDAO<RtasAttendance> getDAO() {
		return rtasAttendanceDAO;
	}
	
	public List<RtasAttendance> findByUser(String username) {
        return rtasAttendanceDAO.findByUser(username);
}

	/*
	 * public List<Attendance> getStudentsForAttendance(Long courseId, String
	 * facultyId) { return attendanceDAO.getStudentsForAttendance(courseId,
	 * facultyId); }
	 */

}
