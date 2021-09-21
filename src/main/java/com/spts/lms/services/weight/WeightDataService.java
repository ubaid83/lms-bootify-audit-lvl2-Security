package com.spts.lms.services.weight;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spts.lms.beans.weight.WeightData;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.daos.weight.WeightDataDAO;
import com.spts.lms.services.BaseService;

@Service("weightDataService")
@Transactional
public class WeightDataService extends BaseService<WeightData> {

	@Autowired
	WeightDataDAO weightDataDAO;

	@Override
	protected BaseDAO<WeightData> getDAO() {
		// TODO Auto-generated method stub
		return weightDataDAO;
	}

	/*
	 * public List<WeightData> findByWeightId(Long weightId) { return
	 * weightDataDAO.findByWeightId(weightId); }
	 */

	public List<WeightData> findByCourseIds(Long courseId) {
		return weightDataDAO.findByCourseId(courseId);
	}

	public WeightData findByWeightTypeAndCourseId(String weightType,
			Long courseId) {
		return weightDataDAO.findByWeightTypeAndCourseId(weightType, courseId);
	}
}
