package com.spts.lms.services.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spts.lms.beans.user.TrainingProgram;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.daos.user.TrainingProgramDAO;
import com.spts.lms.services.BaseService;
@Service("trainingProgramService")
@Transactional
public class TrainingProgramService extends BaseService<TrainingProgram>{

	
	@Autowired
	TrainingProgramDAO trainingProgramDAO;
	
	@Override
	protected BaseDAO<TrainingProgram> getDAO() {
		// TODO Auto-generated method stub
		return trainingProgramDAO;
	}
	
	public List<TrainingProgram> findTrainingProgramList()
	{
		return trainingProgramDAO.findTrainingProgramList();
	}
	
	
	public List<TrainingProgram> getOnGoingTraining()
	{
		return trainingProgramDAO.getOnGoingTraining();
	}
	
	public TrainingProgram getMarkedTrainingAttendance(String username, Long trainingProgramId)
	{
		return trainingProgramDAO.getMarkedTrainingAttendance(username,trainingProgramId);
	}
	public void insertTrainingAttendance(String username, String trainingProgramId) {
		trainingProgramDAO.insertTrainingAttendance(username, trainingProgramId);
	}	

}
