package com.spts.lms.services.newsEvents;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spts.lms.beans.newsEvents.NewsEvents;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.daos.newsEvents.NewsEventsDAO;
import com.spts.lms.services.BaseService;

@Service("newsEventsService")
@Transactional
public class NewsEventsService extends BaseService<NewsEvents> {

	@Autowired
	NewsEventsDAO newsEventsDAO;
	
	@Override
	protected BaseDAO<NewsEvents> getDAO() {
		return newsEventsDAO;
	}
	
	public List<NewsEvents> getAllActiveNews() {
		
		return newsEventsDAO.getAllActiveNews();
	}
	
	public List<NewsEvents> getAllActiveEvents() {
		
		return newsEventsDAO.getAllActiveEvents();
	}

}
