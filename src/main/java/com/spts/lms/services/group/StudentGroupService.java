package com.spts.lms.services.group;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spts.lms.beans.group.StudentGroup;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.daos.assignment.AssignmentDAO;
import com.spts.lms.daos.groups.StudentGroupDao;
import com.spts.lms.services.BaseService;

@Service("studentGroupService")
public class StudentGroupService extends BaseService<StudentGroup> {

	@Autowired
	private StudentGroupDao studentGroupDAO;

	@Autowired
	private AssignmentDAO assigmentDao;

	@Override
	public BaseDAO<StudentGroup> getDAO() {
		return studentGroupDAO;
	}

	public List<StudentGroup> getStudentsForGroup(Long groupId, Long courseId,
			String acadMonth, String acadYear) {
		return studentGroupDAO.getStudentsForGroupForAllocation(groupId,
				courseId, acadMonth, acadYear);
	}

	public List<StudentGroup> getStudentsForGroup(Long groupId, Long courseId) {
		return studentGroupDAO.getStudentsForGroupForAllocation(groupId,
				courseId);
	}

	public List<StudentGroup> findStudentsByGroupId(Long groupId) {
		return studentGroupDAO.findStudentsByGroupId(groupId);
	}

	public void removeStudentsFromGroup(Long id) {
		studentGroupDAO.removeStudentsFromGroup(id);
	}

	public int getNoOfStudentsAllocated(Long id) {
		return studentGroupDAO.getNoOfStudentsAllocated(id);
	}

	public List<StudentGroup> getStudentsBasedOnGroups(Long groupId) {
		return studentGroupDAO.getStudentsBasedOnGroups(groupId);
	}

	public List<StudentGroup> getStudentsForGroup(Long courseId) {
		return studentGroupDAO.getStudentsForGroup(courseId);
	}

	public List<StudentGroup> getAllGroups(Long courseId) {
		return studentGroupDAO.getAllGroups(courseId);
	}

	public List<StudentGroup> getStudentsForGroupAndCampusId(
			Long courseId, Long campusId) {
		return studentGroupDAO.getStudentsForGroupForAllocationAndCampusId(
				 courseId, campusId);
	}
	
	public List<StudentGroup> getStudentsForGroupAndCourseId(
			Long courseId) {
		return studentGroupDAO.getStudentsForGroupForAllocationAndCourseId(
				 courseId);
	}

}
