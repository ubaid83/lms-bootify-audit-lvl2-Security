package com.spts.lms.services.forum;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spts.lms.beans.forum.Forum;
import com.spts.lms.beans.forum.ForumReply;
import com.spts.lms.beans.forum.ForumReplyDisLike;
import com.spts.lms.beans.forum.ForumReplyLike;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.daos.forum.ForumDAO;
import com.spts.lms.daos.forum.ForumReplyDAO;
import com.spts.lms.daos.forum.ForumReplyDisLikeDAO;
import com.spts.lms.daos.forum.ForumReplyLikeDAO;
import com.spts.lms.services.BaseService;
import com.spts.lms.web.utils.Utils;

@Service("forumReplyService")
@Transactional
public class ForumReplyService extends BaseService<ForumReply> {

	@Autowired
	private ForumDAO forumDAO;

	@Autowired
	private ForumReplyDAO forumReplyDAO;

	@Autowired
	private ForumReplyLikeDAO forumReplyLikeDAO;
	@Autowired
	private ForumReplyDisLikeDAO forumReplyDisLikeDAO;

	@Override
	protected BaseDAO<ForumReply> getDAO() {
		return forumReplyDAO;

	}

	public ForumReply createForumReply(Long questionId, Long courseId,
			String reply, String username) {
		ForumReply bean = new ForumReply();
		bean.setQuestionId(questionId);
		bean.setCourseId(courseId);
		bean.setReply(reply);
		bean.setCreatedBy(username);
		bean.setLastModifiedBy(username);
		Date dt = Utils.getInIST();
		bean.setCreatedDate(dt);
		bean.setLastModifiedDate(dt);
		forumReplyDAO.insertWithIdReturn(bean);
		return bean;
	}

	public List<ForumReply> getRepliesFromQuestion(Long questionId) {
		return forumReplyDAO.getRepliesFromQuestion(questionId);
	}

	public List<ForumReply> findByCourse(Long courseId) {
		return forumReplyDAO.findByCourse(courseId);
	}

	public void updateLike(Long id, String sapid) {
		forumReplyDAO.updateLikeForReply(id, sapid);
	}

	public void updateDisLike(Long id, String sapid) {
		forumReplyDAO.updateDisLikeForReply(id, sapid);
	}

	public void updateReportAbuse(Long id) {
		forumReplyDAO.updateReportAbuse(id);
	}

	public void setInActive(Long id) {
		forumReplyDAO.setInActive(id);
	}

	public void updateRecentActivityOnReply(Long replyId) {
		forumReplyDAO.updateRecentActivityOnReply(replyId);
	}

	public List<ForumReplyLike> findByReplyAndUsernameForLike(Long replyId,
			String username) {
		return forumReplyLikeDAO.findByReplyAndUsernameForLike(replyId,
				username);
	}

	public List<ForumReplyLike> getLikeCount(Long replyId) {
		return forumReplyLikeDAO.getLikeCount(replyId);
	}

	public List<String> findStudentsReplied(Long questionId) {
		return forumReplyDAO.findStudentsReplied(questionId);
	}

	public HashMap<Long, String> mapOfForumIdAndMostRecentActivityDate(
			Long courseId) {
		List<Forum> listOfAllForumsForCourse = forumDAO
				.findByCourseId(courseId);
		HashMap<Long, String> mapForReturn = new HashMap<Long, String>();
		for (Forum f : listOfAllForumsForCourse) {
			String mostRecentActivity = forumReplyDAO
					.getMostRecentActivityDateFromQuestionId(f.getId());
			if (mostRecentActivity != null) {
				mapForReturn.put(f.getId(), mostRecentActivity);
			} else {
				mapForReturn.put(f.getId(), "No Recent Activity");
			}
		}
		return mapForReturn;
	}

	public List<ForumReplyDisLike> findByReplyAndUsernameForDisLike(
			Long replyId, String username) {
		return forumReplyDisLikeDAO.findByReplyAndUsernameForDisLike(replyId,
				username);
	}

	public List<ForumReplyDisLike> getDisLikeCount(Long replyId) {
		return forumReplyDisLikeDAO.getDisLikeCount(replyId);
	}

	/*
	 * public String updateVote(Long id){ return forumReplyDAO.updateVote(id); }
	 */

}
