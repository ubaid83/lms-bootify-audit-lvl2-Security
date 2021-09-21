package com.spts.lms.services.timetable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spts.lms.beans.timetable.Timetable;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.daos.timetable.TimetableDAO;
import com.spts.lms.services.BaseService;

@Service("timetableService")
@Transactional
public class TimetableService extends BaseService<Timetable> {

	@Autowired
	TimetableDAO timetableDAO;
	
	@Override
	protected BaseDAO<Timetable> getDAO() {
		
		return timetableDAO;
	}

}
