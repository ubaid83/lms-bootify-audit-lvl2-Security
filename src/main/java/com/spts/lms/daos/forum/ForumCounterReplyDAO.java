package com.spts.lms.daos.forum;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.spts.lms.beans.forum.Forum;
import com.spts.lms.beans.forum.ForumReply;
import com.spts.lms.daos.BaseDAO;

@Repository("forumCounterReplyDAO")
public class ForumCounterReplyDAO extends BaseDAO<ForumReply> {

	@Override
	protected String getTableName() {
		return "counter_reply";
	}

	@Override
	protected String getInsertSql() {
		return "INSERT INTO "
				+ getTableName()
				+ " (replyId,answer,createdBy,createdDate,"
				+ "lastModifiedBy,lastModifiedDate)"

				+ " VALUES(:replyId,:answer,:createdBy,:createdDate,:lastModifiedBy,:lastModifiedDate)";
	}

	@Override
	protected String getUpdateSql() {
		final String sql = "Update " + getTableName() + " set "
				+ " answer =:answer," + " createdBy =:createdBy,"
				+ " vote =:vote," + " createdDate = :createdDate ,"
				+ " lastModifiedBy = :lastModifiedBy,"
				+ " lastModifiedDate =:lastModifiedDate,"

				+ " courseId =:courseId"

				+ " where id = :id ";
		return sql;
	}

	/*
	 * public String updateVote(Long id){ final String sql =
	 * "Update forum_reply set " + "vote = :vote" + "where id = :id"; return
	 * findAllSQL(sql, new Object[]{Lonf id});
	 * 
	 * }
	 */

	@Override
	protected String getUpsertSql() {
		return null;
	}

	public List<ForumReply> findCounterReplies(Long replyId) {
		String sql = "select * from " + getTableName()
				+ " where replyId = ? and active = 'Y'";
		return findAllSQL(sql, new Object[] { replyId });
	}
}
