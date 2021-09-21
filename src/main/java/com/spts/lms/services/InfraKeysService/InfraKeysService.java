package com.spts.lms.services.InfraKeysService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.spts.lms.beans.InfraKeys.InfraKeys;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.daos.InfraKeysDao.InfraKeysDao;
import com.spts.lms.services.BaseService;


@Service("infraKeysService")
@Transactional
public class InfraKeysService extends BaseService<InfraKeys> {

	@Autowired
	InfraKeysDao infraKeysDao;

	@Override
	protected BaseDAO<InfraKeys> getDAO() {
		// TODO Auto-generated method stub
		return infraKeysDao;
	}

	public InfraKeys findKeys() {
		return infraKeysDao.findKeys();
	}

	public String findSlugFromAbbr(String abbr) {
		return infraKeysDao.findSlugFromAbbr(abbr);
	}
}