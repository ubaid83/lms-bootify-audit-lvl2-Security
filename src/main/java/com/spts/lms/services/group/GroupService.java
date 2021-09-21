package com.spts.lms.services.group;

import java.util.List;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import com.spts.lms.beans.group.Groups;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.daos.groups.GroupDAO;
import com.spts.lms.services.BaseService;

@Service("groupService")
public class GroupService extends BaseService<Groups> {

	@Autowired
	private GroupDAO groupDAO;

	@Override
	public BaseDAO<Groups> getDAO() {
		return groupDAO;
	}

	public List<Groups> findAllGroup(String userName) {
		String sql = "SELECT sg.*, g.*, c.courseName FROM student_group sg "
				+ " inner join groups g on sg.groupId = g.id "
				+ " inner join course c on sg.courseId = c.id "
				+ " where sg.username = ? ";

		return findAllSQL(sql, new Object[] { userName });
	}

	public List<Groups> findByUser(String username, String acadMonth,
			String acadYear) {
		return groupDAO.findByUser(username, acadMonth, acadYear);
	}

	public List<Groups> findByUser(String username) {
		return groupDAO.findByUser(username);
	}

	public void deleteSoftById(final String id) {
		getDAO().deleteSoftById(id);
	}

	public List<Groups> findByUserAndCourse(String username, Long courseId,
			String acadMonth, String acadYear) {
		return groupDAO.findByUserAndCourse(username, courseId, acadMonth,
				acadYear);
	}

	public List<Groups> findByUserAndCourse(String username, Long courseId) {
		return groupDAO.findByUserAndCourse(username, courseId);
	}

	public List<Groups> findByCourse(Long courseId) {
		return groupDAO.findByCourse(courseId);
	}

	public void updateFacultyAssigned(String facultyId, Long groupId) {
		groupDAO.updateFacultyAssigned(facultyId, groupId);
	}

	@Async
	public Future<List<Groups>> findByCourseAsync(Long courseId,
			String acadMonth, String acadYear) {
		return new AsyncResult<List<Groups>>(groupDAO.findByCourse(courseId));
	}

	public List<Groups> findByFaculty(String username, String acadMonth,
			String acadYear) {
		return groupDAO.findByFaculty(username, acadMonth, acadYear);
	}

	public List<Groups> findByFaculty(String username) {
		return groupDAO.findByFaculty(username);
	}

	public List<Groups> findByFacultyAndCourse(String username, Long courseId,
			String acadMonth, String acadYear) {
		return groupDAO.findByFacultyAndCourse(username, courseId, acadMonth,
				acadYear);
	}

	public List<Groups> findByFacultyAndCourse(String username, Long courseId) {
		return groupDAO.findByFacultyAndCourse(username, courseId);
	}

	public List<Groups> findByFacultyAndCourseWithNonEmptyGroups(
			String username, Long courseId, String acadMonth, String acadYear) {
		return groupDAO.findByFacultyAndCourseNonEmpty(username, courseId,
				acadMonth, acadYear);
	}

	public List<Groups> findByFacultyAndCourseActiveWithNonEmptyGroups(
			String username, Long courseId) {
		return groupDAO.findByFacultyAndCourseActiveWithNonEmptyGroups(
				username, courseId);
	}

	public List<Groups> findAllGroupsByFaculty(String username,
			String acadMonth, String acadYear) {
		return groupDAO.findAllGroupsByFaculty(username, acadMonth, acadYear);
	}

	public List<Groups> findAllGroupsByFaculty(String username, Long programId) {
		return groupDAO.findAllGroupsByFaculty(username, programId);
	}

	public List<Groups> findByFacultyAndCourseWithGroupCourse(String username,
			String courseId) {
		return groupDAO.findByFacultyAndCourseWithGroupCourse(username,
				courseId);
	}

	public List<Groups> findByFacultyAndCourseWithGroupCourseNew(String username,String courseId) {
		return groupDAO.findByFacultyAndCourseWithGroupCourseNew(username, courseId);
	}
}
