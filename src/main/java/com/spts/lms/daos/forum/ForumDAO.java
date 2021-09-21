package com.spts.lms.daos.forum;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.springframework.stereotype.Repository;

import com.spts.lms.beans.forum.Forum;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.web.utils.Utils;

@Repository("forumDAO")
public class ForumDAO extends BaseDAO<Forum> {

	@Override
	protected String getTableName() {
		return "forum";
	}

	@Override
	protected String getInsertSql() {
		return "INSERT INTO "
				+ getTableName()
				+ "(courseId,topic,createdBy,createdDate,lastModifiedBy,lastModifiedDate,question)"

				+ " VALUES(:courseId,:topic,:createdBy,:createdDate,:lastModifiedBy,:lastModifiedDate,:question)";

	}

	@Override
	protected String getUpdateSql() {
		final String sql = "Update " + getTableName() + " set "
				+ "courseId = :courseId , " + "createdBy = :createdBy ,"
				+ "createdDate = :createdDate ,"
				+ "lastModifiedBy = :lastModifiedBy ,"
				+ "lastModifiedDate = :lastModifiedDate ," + "topic = :topic,"
				+ "question = :question " + "where id = :id ";
		return sql;
	}

	@Override
	protected String getUpsertSql() {
		return null;
	}

	public List<Forum> upsertForum(Forum forum) {
		final String sql = "Update " + getTableName() + " set"
				+ "question = :question," + "topic = :topic,"

				+ "lastModifiedBy = :lastModifiedBy ,"
				+ "lastModifiedDate = :lastModifiedDate " + "where id = :id ";
		return findAllSQL(sql, new Object[] {});
	}

	public List<Forum> findByUser(String username) {
		Date dt = Utils.getInIST();
		final String sql = "SELECT *, c.courseName FROM forum f "

		+ " inner join course c on f.courseId = c.id "
				+ " where f.createdBy = ? "

				+ " order by createdDate desc";
		return findAllSQL(sql, new Object[] { username, dt });
	}

	public List<Forum> findByCourse(Long courseId) {
		final String sql = "SELECT *, c.courseName,f.id as id, f.createdBy as createdBy, f.createdDate as createdDate FROM forum f"
				+ " inner join course c on f.courseId = c.id "
				+ " where f.courseId = ?  and f.active='Y' order by f.id desc";
		return findAllSQL(sql, new Object[] { courseId });
	}

	public List<Forum> findByCourseId(Long courseId) {
		final String sql = "SELECT * from " + getTableName()
				+ " where courseId = ? ";
		return findAllSQL(sql, new Object[] { courseId });
	}

	public List<Forum> countReplies(Long questionId) {
		final String sql = "SELECT COUNT(id) from forum_reply where questionId = ?";
		// Integer count = Integer.valueOf(sql);
		return findAllSQL(sql, new Object[] { questionId });
	}

	public int updateTopic(Long id) {
		return 0;
	}
	/*
	 * public Long countAll(Long questionId) { final String sql =
	 * "SELECT COUNT(*) FROM  forum_reply f " + " where f.questionId = ?";
	 * return findOneSQL(sql, new Object[] { questionId }); }
	 */

	public List<Forum> getNoOfForumStats(String fromDate, String toDate) {

		String sql = "select * from forum f where f.lastModifiedDate BETWEEN ? and ? and f.active='Y' ";

		return findAllSQL(sql, new Object[] { fromDate, toDate });
	}
}
