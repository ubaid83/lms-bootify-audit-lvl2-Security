package com.spts.lms.services.selectives;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spts.lms.beans.selectives.SelectiveEvents;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.daos.selectives.SelectiveEventsDAO;
import com.spts.lms.services.BaseService;

@Service("selectiveEventsService")
@Transactional
public class SelectiveEventsService extends BaseService<SelectiveEvents> {

	@Autowired
	SelectiveEventsDAO selectiveEventsDAO;
	
	@Override
	protected BaseDAO<SelectiveEvents> getDAO() {
		
		return selectiveEventsDAO;
	}
	
	public List<SelectiveEvents> getSelectiveEventsList(String role,String username) {
		return selectiveEventsDAO.getSelectiveEventsList(role, username);

	}

}
