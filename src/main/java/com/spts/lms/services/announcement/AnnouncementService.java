package com.spts.lms.services.announcement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spts.lms.beans.announcement.Announcement;
import com.spts.lms.beans.announcement.Announcement.AnnouncementType;
import com.spts.lms.beans.course.Course;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.daos.announcement.AnnouncementDAO;
import com.spts.lms.helpers.PaginationHelper.Page;
import com.spts.lms.services.BaseService;

@Service("announcementService")
@Transactional
public class AnnouncementService extends BaseService<Announcement> {

	@Autowired
	private AnnouncementDAO announcementDAO;

	@Override
	public BaseDAO<Announcement> getDAO() {
		return announcementDAO;
	}

	
	
	public Announcement getFacultyNotificationAnnouncement()
    {
          
          return announcementDAO.getFacultyNotificationAnnouncement();
    }

    
    public Announcement getStudentNotificationAnnouncement()
    {
          
          return announcementDAO.getStudentNotificationAnnouncement();
    }
    
    public Announcement getAdminNotificationAnnouncement()
    {
          
          return announcementDAO.getAdminNotificationAnnouncement();
    }

	public Page<Announcement> findAnnouncements(Announcement announcement,
			int pageNo, int pageSize) {
		String sql = "Select * from announcement a  where 1 = 1 and active='Y' ";
		String countSql = "Select count(*) from announcement where 1 = 1 and active='Y' ";

		ArrayList<Object> parameters = new ArrayList<Object>();

		if (announcement.getProgramId() != null) {
			sql = sql + " and programId like ? ";
			countSql = countSql + " and programId like ? ";
			parameters.add("%" + announcement.getProgramId() + "%");
		}

		if (announcement.getCourseId() != null) {
			sql = sql + " and courseId like ? ";
			countSql = countSql + " and courseId like ? ";
			parameters.add("%" + announcement.getCourseId() + "%");
		}

		if (announcement.getSubject() != null
				&& !announcement.getSubject().isEmpty()) {
			sql = sql + " and subject like ? ";
			countSql = countSql + " and subject like ? ";
			parameters.add("%" + announcement.getSubject() + "%");
		}

		if (announcement.getStartDate() != null
				&& !announcement.getStartDate().isEmpty()) {
			sql = sql + " and startDate >= ? ";
			countSql = countSql + " and startDate >= ? ";
			parameters.add(announcement.getStartDate());
		}

		if (announcement.getEndDate() != null
				&& !announcement.getEndDate().isEmpty()) {
			sql = sql + " and endDate <= ? ";
			countSql = countSql + " and endDate <= ? ";
			parameters.add(announcement.getEndDate());
		}

		if (announcement.getAnnouncementType() != null
				&& !announcement.getAnnouncementType().isEmpty()) {
			sql = sql + " and announcementType = ? ";
			countSql = countSql + " and announcementType = ? ";
			parameters.add(announcement.getAnnouncementType());
		}

		if (announcement.getAnnouncementSubType() != null
				&& !announcement.getAnnouncementSubType().isEmpty()) {
			sql = sql + " and announcementSubType = ? ";
			countSql = countSql + " and announcementSubType = ? ";
			parameters.add(announcement.getAnnouncementSubType());
		}

		sql = sql + " order by a.id desc ";

		Object[] args = parameters.toArray();

		return findAllSQL(sql, countSql, args, pageNo, pageSize);
	}

	public Page<Announcement> findLibraryCounselorAnnouncements(
			Announcement announcement, int pageNo, int pageSize) {
		String sql = "Select * from announcement a  where 1 = 1 and active='Y' ";
		String countSql = "Select count(*) from announcement where 1 = 1 and active='Y' ";

		ArrayList<Object> parameters = new ArrayList<Object>();

		if (announcement.getCourseId() != null) {
			sql = sql + " and courseId like ? ";
			countSql = countSql + " and courseId like ? ";
			parameters.add("%" + announcement.getCourseId() + "%");
		}

		if (announcement.getSubject() != null
				&& !announcement.getSubject().isEmpty()) {
			sql = sql + " and subject like ? ";
			countSql = countSql + " and subject like ? ";
			parameters.add("%" + announcement.getSubject() + "%");
		}

		if (announcement.getStartDate() != null
				&& !announcement.getStartDate().isEmpty()) {
			sql = sql + " and startDate >= ? ";
			countSql = countSql + " and startDate >= ? ";
			parameters.add(announcement.getStartDate());
		}

		if (announcement.getEndDate() != null
				&& !announcement.getEndDate().isEmpty()) {
			sql = sql + " and endDate <= ? ";
			countSql = countSql + " and endDate <= ? ";
			parameters.add(announcement.getEndDate());
		}

		if (announcement.getAnnouncementType() != null
				&& !announcement.getAnnouncementType().isEmpty()) {
			sql = sql + " and announcementType = ? ";
			countSql = countSql + " and announcementType = ? ";
			parameters.add(announcement.getAnnouncementType());
		}

		if (announcement.getAnnouncementSubType() != null
				&& !announcement.getAnnouncementSubType().isEmpty()) {
			sql = sql + " and announcementSubType = ? ";
			countSql = countSql + " and announcementSubType = ? ";
			parameters.add(announcement.getAnnouncementSubType());
		}

		sql = sql + " order by a.id desc ";

		Object[] args = parameters.toArray();

		return findAllSQL(sql, countSql, args, pageNo, pageSize);
	}

	public Page<Announcement> getAnnouncementsByStudentCoursesAndSubType(
			List<Long> cId, int pageNo, int pageSize, String subtype) {
		return announcementDAO.getAnnouncementsByStudentCoursesAndSubtype(cId,
				pageNo, pageSize, subtype);
	}

	public List<Announcement> getAnnouncementsByAcadSessionYear(Long programId,
			String acadSession) {
		return announcementDAO.getAnnouncementsByAcadSessionYear(programId,
				acadSession);
	}

	public Page<Announcement> findAnnouncementsReplacement(Long programId,
			Long courseId, int pageNo, int pageSize) {
		return announcementDAO.findAnnouncementsReplacement(programId,
				courseId, pageNo, pageSize);

	}

	public Page<Announcement> findAnnouncementsForLibrary(
			Announcement announcement, int pageNo, int pageSize) {
		String sql = "Select * from announcement a  where 1 = 1 and active='Y' ";
		String countSql = "Select count(*) from announcement where 1 = 1 and  active='Y' ";

		ArrayList<Object> parameters = new ArrayList<Object>();

		if (announcement.getCourseId() != null) {
			sql = sql + " and courseId like ? ";
			countSql = countSql + " and courseId like ? ";
			parameters.add("%" + announcement.getCourseId() + "%");
		}

		if (announcement.getSubject() != null
				&& !announcement.getSubject().isEmpty()) {
			sql = sql + " and subject like ? ";
			countSql = countSql + " and subject like ? ";
			parameters.add("%" + announcement.getSubject() + "%");
		}

		if (announcement.getStartDate() != null
				&& !announcement.getStartDate().isEmpty()) {
			sql = sql + " and startDate >= ? ";
			countSql = countSql + " and startDate >= ? ";
			parameters.add(announcement.getStartDate());
		}

		if (announcement.getEndDate() != null
				&& !announcement.getEndDate().isEmpty()) {
			sql = sql + " and endDate <= ? ";
			countSql = countSql + " and endDate <= ? ";
			parameters.add(announcement.getEndDate());
		}

		if (announcement.getAnnouncementType() != null
				&& !announcement.getAnnouncementType().isEmpty()) {
			sql = sql + " and announcementType = ? ";
			countSql = countSql + " and announcementType = ? ";
			parameters.add(announcement.getAnnouncementType());
		}

		if (announcement.getAnnouncementSubType() != null
				&& !announcement.getAnnouncementSubType().isEmpty()) {
			sql = sql + " and announcementSubType = ? ";
			countSql = countSql + " and announcementSubType = ? ";
			parameters.add(announcement.getAnnouncementSubType());
		}

		sql = sql + " order by a.id desc ";

		Object[] args = parameters.toArray();

		return findAllSQL(sql, countSql, args, pageNo, pageSize);
	}

	public Page<Announcement> findAnnouncementsForCounselor(
			Announcement announcement, int pageNo, int pageSize) {
		String sql = "Select * from announcement a  where 1 = 1 and announcementType = 'Counselor'  and active='Y' ";
		String countSql = "Select count(*) from announcement where 1 = 1 and announcementType = 'Counselor'  and active='Y' ";

		ArrayList<Object> parameters = new ArrayList<Object>();

		if (announcement.getCourseId() != null) {
			sql = sql + " and courseId like ? ";
			countSql = countSql + " and courseId like ? ";
			parameters.add("%" + announcement.getCourseId() + "%");
		}

		if (announcement.getSubject() != null
				&& !announcement.getSubject().isEmpty()) {
			sql = sql + " and subject like ? ";
			countSql = countSql + " and subject like ? ";
			parameters.add("%" + announcement.getSubject() + "%");
		}

		if (announcement.getStartDate() != null
				&& !announcement.getStartDate().isEmpty()) {
			sql = sql + " and startDate >= ? ";
			countSql = countSql + " and startDate >= ? ";
			parameters.add(announcement.getStartDate());
		}

		if (announcement.getEndDate() != null
				&& !announcement.getEndDate().isEmpty()) {
			sql = sql + " and endDate <= ? ";
			countSql = countSql + " and endDate <= ? ";
			parameters.add(announcement.getEndDate());
		}

		/*
		 * if (announcement.getAnnouncementType() != null &&
		 * !announcement.getAnnouncementType().isEmpty()) { sql = sql +
		 * " and announcementType = ? "; countSql = countSql +
		 * " and announcementType = ? ";
		 * parameters.add(announcement.getAnnouncementType()); }
		 * 
		 * if (announcement.getAnnouncementSubType() != null &&
		 * !announcement.getAnnouncementSubType().isEmpty()) { sql = sql +
		 * " and announcementSubType = ? "; countSql = countSql +
		 * " and announcementSubType = ? ";
		 * parameters.add(announcement.getAnnouncementSubType()); }
		 */

		sql = sql + " order by a.id desc ";

		Object[] args = parameters.toArray();

		return findAllSQL(sql, countSql, args, pageNo, pageSize);
	}

	public Page<Announcement> getAnnouncementsByStudentCourses(List<Long> cId,
			int pageNo, int pageSize) {
		return announcementDAO.getAnnouncementsByStudentCourses(cId, pageNo,
				pageSize);
	}

	public List<Announcement> findAnnouncmentsByProgramId(Long programId) {
		return announcementDAO.findAnnouncmentsByProgramId(programId);
	}

	public Page<Announcement> getInstitutionalAnnouncement(int pageNo,
			int pageSize) {
		return announcementDAO.getInstitutionalAnnouncement(pageNo, pageSize);
	}

	public Page<Announcement> getInstitutionalAnnouncementBySubtype(int pageNo,
			int pageSize, String announcementSubType) {
		return announcementDAO.getInstitutionalAnnouncementBySubtype(pageNo,
				pageSize, announcementSubType);
	}

	public List<Announcement> findAnnouncementsByUser(String username) {

		String sql = "select * from announcement a inner join user_course uc on uc.username = ?  "
				+ " and a.courseId = uc.courseId "
				+ " and sysdate() >= a.startDate "
				+ " and sysdate() <= a.endDate and a.active = 'Y' "
				+ " order by a.startDate desc ";
		List<Announcement> courseAnnouncements = announcementDAO.findAllSQL(
				sql, new Object[] { username });

		sql = "Select * from announcement a where announcementType = ? "
				+ " and sysdate() >= a.startDate "
				+ " and sysdate() <= a.endDate and a.active = 'Y' "
				+ " order by a.startDate desc";
		List<Announcement> instituteAnnouncements = announcementDAO.findAllSQL(
				sql, new Object[] { AnnouncementType.INSTITUTE });

		List<Announcement> allAnnouncements = new ArrayList<Announcement>();
		allAnnouncements.addAll(courseAnnouncements);
		allAnnouncements.addAll(instituteAnnouncements);

		return allAnnouncements;
	}

	public List<Announcement> getAnnouncementCountDashboard(String username,
			String acadMonth, String acadYear) {
		return announcementDAO.getAnnouncementCountDashboard(username,
				acadMonth, acadYear);
	}

	public List<Announcement> getAnnouncementCountDashboard(String username) {
		return announcementDAO.getAnnouncementCountDashboard(username);
	}

	public List<Announcement> getAnnouncementsByCoursesAndInstitute(
			Set<Long> courseIdList, String userName, String programId) {

		return announcementDAO.getAnnouncementsByCoursesAndInstitute(
				courseIdList, userName, programId);
	}

	public List<Announcement> getAnnouncementsByCoursesAndAcadSessionAndInstitute(
			Set<Long> courseIdList, String userName, String programId,
			String acadSession) {

		return announcementDAO
				.getAnnouncementsByCoursesAndAcadSessionAndInstitute(
						courseIdList, userName, programId, acadSession);
	}

	public List<Announcement> getAnnouncementsByCoursesList(
			Set<Long> courseIdList, String userName) {

		return announcementDAO.announcementsByCoursesList(courseIdList,
				userName);
	}

	public List<Announcement> findByUserAndCourse(String username, Long courseId) {
		String sql = "select * from announcement a inner join user_course uc on uc.username = ? and uc.courseId = ? "
				+ " and a.courseId = uc.courseId "
				+ " and sysdate() >= a.startDate "
				+ " and sysdate() <= a.endDate "
				+ " order by a.startDate desc ";
		List<Announcement> courseAnnouncements = announcementDAO.findAllSQL(
				sql, new Object[] { username, courseId });

		return courseAnnouncements;
	}

	public Page<Announcement> findByUserAndCourseP(String username,
			Long courseId, int pageNo, int pageSize) {
		String sql = "select * from announcement a inner join user_course uc on uc.username = ? and uc.courseId = ? "
				+ " and a.courseId = uc.courseId "
				+ " and sysdate() >= a.startDate "
				+ " and sysdate() <= a.endDate and a.active='Y'"
				+ " order by a.startDate desc ";

		String countsql = "select count(*) from announcement a inner join user_course uc on uc.username = ? and uc.courseId = ? "

				+ " and a.courseId = uc.courseId "
				+ " and sysdate() >= a.startDate "
				+ " and sysdate() <= a.endDate and a.active='Y' "
				+ " order by a.startDate desc ";

		Page<Announcement> courseAnnouncements = announcementDAO.findAllSQL(
				sql, countsql, new Object[] { username, courseId }, pageNo,
				pageSize);
		return courseAnnouncements;
	}

	public Page<Announcement> getLibraryAnnouncement(int pageNo, int pageSize) {
		return announcementDAO.getLibraryAnnouncement(pageNo, pageSize);
	}

	public Page<Announcement> getCounselorAnnouncement(int pageNo, int pageSize) {
		return announcementDAO.getCounselorAnnouncement(pageNo, pageSize);
	}

	public List<Announcement> getAnnouncementsByCourses(List<Long> cId) {
		return announcementDAO.getAnnouncementsByCourses(cId);
	}

	public List<Announcement> getAnnouncementsByCoursesLIMIT(List<Long> cId) {

		return announcementDAO.getAnnouncementsByCoursesLIMIT(cId);
	}

	public Page<Announcement> getAnnouncementsByCoursesP(List<Long> cId,
			int pageNo, int pageSize) {
		return announcementDAO
				.getAnnouncementsByCoursesP(cId, pageNo, pageSize);
	}

	public List<Announcement> findByCourse(Long courseId) {
		return announcementDAO.findByCourse(courseId);
	}

	public Page<Announcement> findAnnouncementsReplacement(Long programId,
			int pageNo, int pageSize) {
		return announcementDAO.findAnnouncementsReplacement(programId, pageNo,
				pageSize);

	}

	public List<Announcement> getNoOfAnnouncementStats(String fromDate,
			String toDate) {

		return announcementDAO.getNoOfAnnouncementStats(fromDate, toDate);
	}

	public Page<Announcement> getAnnouncementsByCoursesAndAcadSessionAndInstitute(
			List<String> courseIdList, String userName, String programId,
			int pageNo, int pageSize) {
		return announcementDAO
				.getAnnouncementsByCoursesAndAcadSessionAndInstitute(
						courseIdList, userName, programId, pageNo, pageSize);
	}
	
	public List<Announcement> getAnnouncementByUsernameForApp(String username) {
		return announcementDAO.getAnnouncementByUsernameForApp(username);
	}
	
	public List<Announcement> getInstituteAnnouncement() {
		return announcementDAO.getInstituteAnnouncement();
	}
	
	public List<Announcement> getLibraryAnnouncement() {
		return announcementDAO.getLibraryAnnouncement();
	}
	
	public List<Announcement> getCounselorAnnouncement() {
		return announcementDAO.getCounselorAnnouncement();
	}
	
	public List<Announcement> getProgramAnnouncementByProgramId(String programId) {
		return announcementDAO.getProgramAnnouncementByProgramId(programId);
	}
	public List<Announcement> getTimeTableByProgramId(String acadSession ,String programId) {
		return announcementDAO.getTimeTableByProgramId(acadSession,programId);
	}
	
	public List<Announcement> getCourseAnnouncementByCourseIds(List<Long> cId) {
		return announcementDAO.getCourseAnnouncementByCourseIds(cId);
	}
	public int insertTimeTableStudent(String username,String announcement_id){
		return announcementDAO.insertTimeTableStudent(username,announcement_id);
		
	}
	public Announcement getTimeTableStudent(String username,String announcement_id) {
		return announcementDAO.getTimeTableStudent(username,announcement_id);
	}
	public List<Announcement> getStudentReport(Long id) {
		return announcementDAO.getStudentReport(id);
	}

	public List<Announcement> getExamTimetableForApp(String username){
		return announcementDAO.getExamTimetableForApp(username);
	}
	
	public void addNotification(Announcement announcement) {
		announcementDAO.addNotification(announcement);
		}
	
public List<Announcement> getAnnouncementBySupportAdmin() {
 		
 		return announcementDAO.getAnnouncementBySupportAdmin();
 		
 	}

}
