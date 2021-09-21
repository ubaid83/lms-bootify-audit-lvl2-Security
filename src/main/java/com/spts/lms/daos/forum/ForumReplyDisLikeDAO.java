package com.spts.lms.daos.forum;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.spts.lms.beans.forum.Forum;
import com.spts.lms.beans.forum.ForumReply;
import com.spts.lms.beans.forum.ForumReplyDisLike;
import com.spts.lms.beans.forum.ForumReplyLike;
import com.spts.lms.daos.BaseDAO;

@Repository("forumReplyDisLikeDAO")
public class ForumReplyDisLikeDAO extends BaseDAO<ForumReplyDisLike> {

	@Override
	protected String getTableName() {
		return "forum_reply_dislike";
	}

	@Override
	protected String getInsertSql() {
		return "INSERT INTO "
				+ getTableName()
				+ " (replyId,username,createdBy,createdDate,"
				+ "lastModifiedBy,lastModifiedDate)"

				+ " VALUES(:replyId,:username,:createdBy,:createdDate,:lastModifiedBy,:lastModifiedDate)";
	}

	@Override
	protected String getUpdateSql() {
		final String sql = "Update " + getTableName() + " set "
				+ " replyId = :replyId," + " username =:username,"
				+ " createdBy =:createdBy,"

				+ " createdDate = :createdDate ,"
				+ " lastModifiedBy = :lastModifiedBy,"
				+ " lastModifiedDate =:lastModifiedDate,"

				+ " where id = :id  ";
		return sql;
	}

	/*
	 * public String updateVote(Long id){ final String sql =
	 * "Update lms.forum_reply set " + "vote = :vote" + "where id = :id"; return
	 * findAllSQL(sql, new Object[]{Lonf id});
	 * 
	 * }
	 */

	@Override
	protected String getUpsertSql() {
		return null;
	}

	public List<ForumReplyDisLike> getDisLikeCount(Long replyId){
		final String sql = "select * from forum_reply_dislike frl "
		 + "inner join forum_reply fr on fr.id = frl.replyId  where fr.id = ?";
		return findAllSQL(sql, new Object[]{replyId});
	}

	public List<ForumReplyDisLike> findByReplyAndUsernameForDisLike(Long replyId, String username) {
		final String sql = "SELECT * FROM forum_reply_dislike f "

		+ " where f.replyId = ? and f.username = ?";

		return findAllSQL(sql, new Object[] { replyId, username});
	}
}
