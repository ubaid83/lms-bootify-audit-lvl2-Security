package com.spts.lms.daos.forum;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.spts.lms.beans.forum.Forum;
import com.spts.lms.beans.forum.ForumReply;
import com.spts.lms.daos.BaseDAO;

@Repository("forumReplyDAO")
public class ForumReplyDAO extends BaseDAO<ForumReply> {

	@Override
	protected String getTableName() {
		return "forum_reply";
	}

	@Override
	protected String getInsertSql() {
		return "INSERT INTO "
				+ getTableName()
				+ " (questionId,reply,createdBy,createdDate,"
				+ "lastModifiedBy,lastModifiedDate,courseId)"

				+ " VALUES(:questionId,:reply,:createdBy,:createdDate,:lastModifiedBy,:lastModifiedDate,:courseId)";
	}

	@Override
	protected String getUpdateSql() {
		final String sql = "Update " + getTableName() + " set "
				+ " questionId = :questionId," + " reply =:reply,"
				+ " createdBy =:createdBy," + " vote =:vote,"
				+ " createdDate = :createdDate ,"
				+ " lastModifiedBy = :lastModifiedBy,"
				+ " lastModifiedDate =:lastModifiedDate,"

				+ " courseId =:courseId"

				+ " where id = :id and courseId=:courseId ";
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

	public void updateRecentActivityOnReply(Long replyId) {
		executeUpdateSql(
				" update forum_reply set lastModifiedDate = sysdate() where id= ? ",
				new Object[] { replyId });
	}

	public String getMostRecentActivityDateFromQuestionId(Long questionId) {
		return returnSingleColumn(
				" select MAX(lastModifiedDate) from forum_reply where questionId = ? ",
				new Object[] { questionId });
	}

	public List<ForumReply> getRepliesFromQuestion(Long questionId) {

		final String sql = "select * from " + getTableName() + "  where "
				+ "questionId=? and active='Y' order by id desc";
		return findAllSQL(sql, new Object[] { questionId });
	}

	public List<ForumReply> findByCourse(Long courseId) {
		final String sql = "SELECT *, c.courseName,fr.id as id, fr.createdBy as createdBy FROM "
				+ "forum_reply fr"
				+ " inner join course c on fr.courseId = c.id "
				+ " where fr.courseId = ? ";
		return findAllSQL(sql, new Object[] { courseId });
	}

	public void updateLikeForReply(Long replyId, String sapid) {
		executeUpdateSql(
				" insert into forum_reply_like(replyId,username,createdBy,lastModifiedBy,createdDate,lastModifiedDate) VALUES(?,?,?,?,sysdate(),sysdate()) ",
				new Object[] { replyId, sapid, sapid, sapid });
	}

	public void updateDisLikeForReply(Long replyId, String sapid) {
		executeUpdateSql(
				" insert into forum_reply_dislike(replyId,username,createdBy,lastModifiedBy,createdDate,lastModifiedDate) VALUES(?,?,?,?,sysdate(),sysdate()) ",
				new Object[] { replyId, sapid, sapid, sapid });
	}

	public void updateReportAbuse(Long id) {
		executeUpdateSql(
				"update forum_reply set reportAbuse=reportAbuse + 1 where id= ? ",
				new Object[] { id });
	}

	public void setInActive(Long id) {
		executeUpdateSql("update forum_reply set active='N' where id= ? ",
				new Object[] { id });
	}

	public List<String> findStudentsReplied(Long questionId) {
		String sql = "select distinct(fr.createdBy) from forum_reply fr where fr.questionId=?";
		return getJdbcTemplate().queryForList(sql, String.class,
				new Object[] { questionId });
	}

}
