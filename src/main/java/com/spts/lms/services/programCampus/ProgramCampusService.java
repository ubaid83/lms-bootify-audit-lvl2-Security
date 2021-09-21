package com.spts.lms.services.programCampus;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spts.lms.beans.portalFeedback.PortalFeedbackQuestion;
import com.spts.lms.beans.programCampus.ProgramCampus;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.daos.portalFeedback.PortalFeedbackQuestionDAO;
import com.spts.lms.daos.programCampus.ProgramCampusDAO;
import com.spts.lms.services.BaseService;

@Service("programCampusService")
@Transactional
public class ProgramCampusService extends BaseService<ProgramCampus> {

	@Autowired
	private ProgramCampusDAO programCampusDAO;

	@Override
	protected BaseDAO<ProgramCampus> getDAO() {

		return programCampusDAO;
	}

	public List<ProgramCampus> getCampusListByProgram(String programId) {
		return programCampusDAO.getCampusListByProgram(programId);
	}

	public List<ProgramCampus> getCampusList() {
		return programCampusDAO.getCampusList();
	}
	
	public List<String> getProgramsByCampusId(String campusId){
		return programCampusDAO.getProgramsByCampusId(campusId);
	}
	
	public List<ProgramCampus> getCampusNameDropDown(String campusName)
	{
	 return programCampusDAO.getCampusNameDropDown(campusName);
	}
	
		public List<ProgramCampus> getCampusNameDropDownId(String programId)
	{
	 return programCampusDAO.getCampusNameDropDownId(programId);
	}
		
		public ProgramCampus   getCampusNameByCampusId(String campusid)
		 {
		  return programCampusDAO.getCampusNameByCampusId(campusid);
		 }
		
		public List<ProgramCampus> getProgramCampusName()
		{
		 return programCampusDAO.getProgramCampusName();
		}

		public List<ProgramCampus> getCampusesForSchool() {
			return programCampusDAO.getCampusesForSchool();
		}

		public String getCampusByCampusId(String campusId) {
			return programCampusDAO.getCampusByCampusId(campusId);
		}
		
		public  List<ProgramCampus> getCampusForSMaster() {
			return programCampusDAO.getCampusForSMaster();
		}

		public List<ProgramCampus> getCampusNameSupportAdmin() {
			return programCampusDAO.getCampusNameSupportAdmin();
		}
		
}
