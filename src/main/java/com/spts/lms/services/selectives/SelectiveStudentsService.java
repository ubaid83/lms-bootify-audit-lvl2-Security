package com.spts.lms.services.selectives;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spts.lms.beans.selectives.SelectiveStudents;

import com.spts.lms.daos.BaseDAO;
import com.spts.lms.daos.selectives.SelectiveStudentsDAO;

import com.spts.lms.services.BaseService;

@Service("selectiveStudentsService")
public class SelectiveStudentsService extends BaseService<SelectiveStudents>{
	
	@Autowired
	SelectiveStudentsDAO selectiveStudentsDAO;
	
	@Override
	protected BaseDAO<SelectiveStudents> getDAO() {
		// TODO Auto-generated method stub
		return selectiveStudentsDAO;
	}

}
