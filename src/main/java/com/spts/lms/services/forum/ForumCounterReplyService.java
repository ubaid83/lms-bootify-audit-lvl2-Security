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
import com.spts.lms.daos.forum.ForumCounterReplyDAO;
import com.spts.lms.daos.forum.ForumDAO;
import com.spts.lms.daos.forum.ForumReplyDAO;
import com.spts.lms.daos.forum.ForumReplyDisLikeDAO;
import com.spts.lms.daos.forum.ForumReplyLikeDAO;
import com.spts.lms.services.BaseService;
import com.spts.lms.web.utils.Utils;

@Service("forumCounterReplyService")
@Transactional
public class ForumCounterReplyService extends BaseService<ForumReply> {

	@Autowired
	private ForumDAO forumDAO;

	@Autowired
	private ForumReplyDAO forumReplyDAO;

	@Autowired
	private ForumReplyLikeDAO forumReplyLikeDAO;
	@Autowired
	private ForumReplyDisLikeDAO forumReplyDisLikeDAO;

	@Autowired
	private ForumCounterReplyDAO forumCounterReplyDAO;

	@Override
	protected BaseDAO<ForumReply> getDAO() {
		return forumCounterReplyDAO;

	}

	public List<ForumReply> findCounterReplies(Long replyId) {
		return forumCounterReplyDAO.findCounterReplies(replyId);
	}

}
