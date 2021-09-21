package com.spts.lms.services.tee;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spts.lms.beans.tee.TeeStudentBatchwise;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.daos.tee.TeeStudentBatchwiseDAO;
import com.spts.lms.services.BaseService;

@Service("teeStudentBatchwiseService")
@Transactional
public class TeeStudentBatchwiseService extends BaseService<TeeStudentBatchwise>{

	@Autowired
	TeeStudentBatchwiseDAO teeStudentBatchwiseDAO;
	
	@Override
	protected BaseDAO<TeeStudentBatchwise> getDAO() {
		// TODO Auto-generated method stub
		return teeStudentBatchwiseDAO;
	}
	
	public List<TeeStudentBatchwise> getAllByActiveTeeId(String teeId){
		return teeStudentBatchwiseDAO.getAllByActiveTeeId(teeId);
	}

	public List<String> getDistinctUsernamesByActiveTeeIdAndFaculty(Long teeId, String facultyId){
		return teeStudentBatchwiseDAO.getDistinctUsernamesByActiveTeeIdAndFaculty(teeId, facultyId);
	}
	
	public void deleteAllActiveByTeeId(String teeId){
		teeStudentBatchwiseDAO.deleteAllActiveByTeeId(teeId);
	}
	
	public void deleteSoftByTeeId(String teeId){
		teeStudentBatchwiseDAO.deleteSoftByTeeId(teeId);
	}
}
