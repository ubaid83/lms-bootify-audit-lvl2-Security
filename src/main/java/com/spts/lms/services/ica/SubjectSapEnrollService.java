package com.spts.lms.services.ica;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spts.lms.beans.ica.SubjectSapEnroll;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.daos.ica.SubjectSapEnrollDAO;
import com.spts.lms.services.BaseService;

@Service("subjectSapEnrollService")
@Transactional
public class SubjectSapEnrollService extends BaseService<SubjectSapEnroll> {

	@Autowired
	SubjectSapEnrollDAO subjectSapEnrollDAO;
	
	@Override
	protected BaseDAO<SubjectSapEnroll> getDAO() {
		// TODO Auto-generated method stub
		return subjectSapEnrollDAO;
	}

	public List<SubjectSapEnroll> getNSStudent(String subjectCode,String acadYearCode,String programId, String acadSession,String campusId) {
		return subjectSapEnrollDAO.getNSStudent(subjectCode, acadYearCode, programId, acadSession, campusId);
	}
	
	public List<String> getDistinctNSStudentList(String subjectCode,String acadYearCode,String programId, String acadSession,String campusId) {
		return subjectSapEnrollDAO.getDistinctNSStudentList(subjectCode, acadYearCode, programId, acadSession, campusId);
	}
}
