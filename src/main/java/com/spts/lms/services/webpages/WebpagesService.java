package com.spts.lms.services.webpages;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spts.lms.beans.webpages.Webpages;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.daos.webpages.WebpagesDAO;
import com.spts.lms.services.BaseService;

@Service("webpagesServices")
public class WebpagesService extends BaseService<Webpages> {

	@Autowired
	private WebpagesDAO webpageDAO;

	@Override
	protected BaseDAO<Webpages> getDAO() {
		// TODO Auto-generated method stub
		return webpageDAO;
	}

	public List<Webpages> findWebpagesForTypeAndCreatedBy(String type,
			String username) {
		return webpageDAO.findWebpagesForTypeAndCreatedBy(type, username);
	}

	public List<Webpages> findAvailWebpages() {
		return webpageDAO.findAvailWebpages();
	}

	public List<Webpages> findAvailArticles() {
		return webpageDAO.findAvailArticles();
	}

	public List<Webpages> findAllArticles() {
		return webpageDAO.findAllArticles();
	}

	public List<Webpages> findSchoolWithCollegeName() {
		return webpageDAO.findSchoolWithCollegeName();
	}

	public List<Webpages> findAvailLibraryResources() {
		return webpageDAO.findAvailLibraryResources();
	}

}
