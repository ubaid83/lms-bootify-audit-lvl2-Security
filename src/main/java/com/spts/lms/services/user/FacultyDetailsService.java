package com.spts.lms.services.user;

import java.util.List;

import javax.websocket.server.ServerEndpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spts.lms.beans.user.FacultyDetails;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.daos.user.FacultyDetailsDAO;
import com.spts.lms.services.BaseService;


@Service("facultyDetailsService")
public class FacultyDetailsService extends BaseService<FacultyDetails> {

	@Autowired
	FacultyDetailsDAO facultyDetailsDAO;
	
	
	@Override
	protected BaseDAO<FacultyDetails> getDAO() {
		
		return facultyDetailsDAO;
	}
	
	public List<FacultyDetails> findMyFacultyByCourse(Long courseId){
		return facultyDetailsDAO.findMyFacultyByCourse(courseId);
	}

}
