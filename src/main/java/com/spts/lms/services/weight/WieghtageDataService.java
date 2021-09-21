/*package com.spts.lms.services.wieghtage;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spts.lms.beans.assignment.Assignment;
import com.spts.lms.beans.wieghtage.Wieghtage;
import com.spts.lms.beans.wieghtage.WieghtageData;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.daos.wieghtage.WieghtageDAO;
import com.spts.lms.daos.wieghtage.WieghtageDataDAO;
import com.spts.lms.services.BaseService;
import com.spts.lms.web.controllers.BaseController;

@Service("wieghtageDataService")
@Transactional
public class WieghtageDataService extends BaseService<WieghtageData> {

	@Autowired
	WieghtageDataDAO wieghtageDataDAO;

	@Override
	protected BaseDAO<WieghtageData> getDAO() {
		// TODO Auto-generated method stub
		return wieghtageDataDAO;
	}

	public List<WieghtageData> findWieghtageByCourse(Long courseId) {
		return wieghtageDataDAO.findWieghtageByCourse(courseId);
	}

	public String showWieghtage(Long courseId) {
		return wieghtageDataDAO.showWieghtage(courseId);
	}

	public List<WieghtageData> showWieghtageForAssignment(Long courseId) {
		return wieghtageDataDAO.showWieghtageForAssignment(courseId);
	}

	public List<String> showWieghtageForCP(Long courseId) {
		return wieghtageDataDAO.showWieghtageForCP(courseId);
	}

	public List<WieghtageData> findAddedWeightageByCourseIdAndWeightageType(
			Long courseId, String weightageType) {
		return wieghtageDataDAO.findAddedWeightageByCourseIdAndWeightageType(
				courseId, weightageType);
	}
}
*/