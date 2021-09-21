package com.spts.lms.services.faq;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spts.lms.beans.faq.Faq;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.daos.faq.FaqDAO;
import com.spts.lms.services.BaseService;

@Service("faqService")
public class FaqService extends BaseService<Faq>{
	
	@Autowired
	FaqDAO faqDAO;

	@Override
	protected BaseDAO<Faq> getDAO() {
		// TODO Auto-generated method stub
		return faqDAO;
	}
	
	public List<Faq> getAdmissionsFAQs(){
		return faqDAO.getAdmissionsFAQs();
	}
	public List<Faq> getAcademicsFAQs(){
		return faqDAO.getAcademicsFAQs();
	}
	public List<Faq> getExamsFAQs(){
		return faqDAO.getExamsFAQs();
	}
	public List<Faq> getOthersFAQs(){
		return faqDAO.getOthersFAQs();
	}
	public List<Faq> getSupportFAQs(){
		return faqDAO.getSupportFAQs();
	}

}
