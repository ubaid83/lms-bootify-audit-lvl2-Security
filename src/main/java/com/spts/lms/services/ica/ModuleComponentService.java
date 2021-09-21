package com.spts.lms.services.ica;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spts.lms.beans.ica.ModuleComponent;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.daos.ica.ModuleComponentDAO;
import com.spts.lms.services.BaseService;

@Service("moduleComponentService")
@Transactional
public class ModuleComponentService extends BaseService<ModuleComponent> {

	@Autowired
	private ModuleComponentDAO moduleComponentDAO;

	@Override
	protected BaseDAO<ModuleComponent> getDAO() {

		return moduleComponentDAO;
	}

	public List<ModuleComponent> moduleComponentListByAcadYearAndCampus(
			String programId, String acadYear, String campusId) {

		return moduleComponentDAO.moduleComponentListByAcadYearAndCampus(
				programId, acadYear, campusId);

	}

	public List<ModuleComponent> moduleComponentListByModuleId(
			String moduleId,String programId, String acadYear, String campusId) {

		return moduleComponentDAO.moduleComponentListByModuleId(
				moduleId,programId, acadYear, campusId);

	}
}
