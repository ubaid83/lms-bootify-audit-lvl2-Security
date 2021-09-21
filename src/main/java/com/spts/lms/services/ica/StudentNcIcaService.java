package com.spts.lms.services.ica;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spts.lms.beans.ica.StudentNcIca;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.daos.ica.StudentNcIcaDAO;
import com.spts.lms.services.BaseService;


@Service("studentNcIcaService")
@Transactional
public class StudentNcIcaService extends BaseService<StudentNcIca> {

	@Autowired
	StudentNcIcaDAO studentNcIcaDAO;

	@Override
	protected BaseDAO<StudentNcIca> getDAO() {

		return studentNcIcaDAO;
	}
	
	public List<StudentNcIca> getUpdatedGrade(String  icaId) {
		return	studentNcIcaDAO.getUpdatedGrade(icaId);
	}
	

}
