package com.spts.lms.services.report;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spts.lms.beans.report.DownloadReportLinks;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.daos.report.DownloadReportLinkDAO;
import com.spts.lms.services.BaseService;

@Service("downloadReportLinkService")
@Transactional
public class DownloadReportLinkService extends BaseService<DownloadReportLinks> {
	@Autowired
	private DownloadReportLinkDAO downloadReportLinkDAO;

	@Override
	public BaseDAO<DownloadReportLinks> getDAO() {
		return downloadReportLinkDAO;
	}
	
	public List<DownloadReportLinks> findAllForAdmin(){
		return downloadReportLinkDAO.findAllForAdmin();
	}
	
	public List<DownloadReportLinks> findAllForFaculty(){
		return downloadReportLinkDAO.findAllForFaculty();
	}

}
