package com.spts.lms.services.weight;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spts.lms.beans.weight.Weight;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.daos.weight.WeightDAO;
import com.spts.lms.services.BaseService;

@Service("weightService")
@Transactional
public class WeightService extends BaseService<Weight> {

	@Autowired
	WeightDAO weightDAO;

	@Override
	protected BaseDAO<Weight> getDAO() {
		// TODO Auto-generated method stub
		return weightDAO;
	}

/*	public List<Weight> findByCourseIds(Long courseId) {
		return weightDAO.findByCourseId(courseId);
	}*/

	/*
	 * public List<Weight> findByCourseName(String courseName) { return
	 * weightDAO.findByCourseName(courseName); }
	 */
	public Weight findByCourseName(String courseName) {
		return weightDAO.findByCourseName(courseName);
	}

	public List<String> findWeightType() {
		return weightDAO.findWeightType();
	}
}
