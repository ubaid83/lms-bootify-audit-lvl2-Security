package com.spts.lms.services.assignment;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spts.lms.beans.assignment.GroupAssignment;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.daos.assignment.AssignmentDAO;
import com.spts.lms.daos.assignment.GroupAssignmentDAO;
import com.spts.lms.daos.assignment.StudentAssignmentDAO;
import com.spts.lms.daos.groups.GroupDAO;
import com.spts.lms.services.BaseService;

@Service("groupAssignmentService")
@Transactional
public class GroupAssignmentService extends BaseService<GroupAssignment> {
	@Autowired
	private GroupDAO groupsDAO;
	@Autowired
	private StudentAssignmentDAO studentAssignmentDAO;
	@Autowired
	private GroupAssignmentDAO groupAssignmentDAO;
	@Autowired
	AssignmentDAO assignmentDAO;

	@Override
	public BaseDAO<GroupAssignment> getDAO() {
		return groupAssignmentDAO;
	}

	public List<GroupAssignment> getGroupsForAssignment(Long assignmentId,
			Long courseId, String acadMonth, String acadYear) {
		return groupAssignmentDAO.getGroupsForAssignment(assignmentId,
				courseId, acadMonth, acadYear);
	}

	

	public ArrayList<GroupAssignment> getGroupsAssociatedToFaculty(
			String userName) {
		return groupAssignmentDAO.getGroupListAssociatedToFaculty(userName);
	}
	/*
	 * public List<StudentAssignment> getGroupsForAssignment(Long groupId, Long
	 * courseId, String acadMonth, Integer acadYear) { return
	 * studentAssignmentDAO.getGroupsForAssignment(groupId, courseId, acadMonth,
	 * acadYear); }
	 */

}
