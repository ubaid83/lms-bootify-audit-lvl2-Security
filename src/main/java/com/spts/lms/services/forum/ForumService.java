package com.spts.lms.services.forum;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spts.lms.beans.forum.Forum;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.daos.forum.ForumDAO;
import com.spts.lms.services.BaseService;
import com.spts.lms.web.utils.Utils;

@Service("forumService")
@Transactional
public class ForumService extends BaseService<Forum> {

	@Autowired
	private ForumDAO forumDAO;

	@Override
	protected BaseDAO<Forum> getDAO() {
		return forumDAO;

	}

	public List<Forum> findByUser(String username) {
		return forumDAO.findByUser(username);
	}

	public List<Forum> findByCourse(Long courseId) {
		return forumDAO.findByCourse(courseId);
	}

	/*
	 * public List<Forum> countReplies(Long questionId){ return
	 * forumDAO.countReplies(questionId); } public Long countAll(Long
	 * questionId){ return forumDAO.countAll(questionId); }
	 */

	public Forum createForum(Long courseId, String topic, String question,
			String username) {
		Forum bean = new Forum();
		bean.setCourseId(courseId);
		bean.setTopic(topic);
		bean.setQuestion(question);
		bean.setCreatedBy(username);
		Date dt = Utils.getInIST();
		bean.setCreatedDate(dt);
		bean.setLastModifiedBy(username);
		bean.setLastModifiedDate(dt);
		forumDAO.insertWithIdReturn(bean);
		return bean;
	}
	
	public List<Forum> getNoOfForumStats(String fromDate, String toDate){
		return forumDAO.getNoOfForumStats(fromDate, toDate);
	}

}
