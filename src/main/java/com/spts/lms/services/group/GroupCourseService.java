package com.spts.lms.services.group;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spts.lms.beans.group.GroupCourse;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.daos.groups.GroupCourseDAO;
import com.spts.lms.services.BaseService;

@Service("groupCourseService")
public class GroupCourseService extends BaseService<GroupCourse> {

	@Autowired
	private GroupCourseDAO groupCourseDAO;

	@Override
	protected BaseDAO<GroupCourse> getDAO() {
		// TODO Auto-generated method stub
		return groupCourseDAO;
	}

	public List<GroupCourse> findbyGroupId(Long groupId) {
		return groupCourseDAO.findbyGroupId(groupId);
	}

	public GroupCourse findbyGroupIdAndCourseId(Long groupId, String courseId) {
		return groupCourseDAO.findbyGroupIdAndCourseId(groupId, courseId);
	}
}
