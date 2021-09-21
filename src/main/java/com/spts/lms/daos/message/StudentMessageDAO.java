package com.spts.lms.daos.message;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.spts.lms.beans.message.StudentMessage;
import com.spts.lms.beans.user.Role;
import com.spts.lms.daos.BaseDAO;

@Repository("studentMessageDAO")
public class StudentMessageDAO extends BaseDAO<StudentMessage> {

	@Override
	protected String getTableName() {
		return "student_message";
	}

	@Override
	protected String getInsertSql() {

		String sql = "insert into "
				+ getTableName()
				+ "(messageId, username, courseId, "
				+ " createdBy, createdDate, lastModifiedBy, lastModifiedDate, acadYear, acadMonth,subject,description,messageReply,messageRepliedBy,messageRepliedDate ) values "
				+ "(:messageId, :username, :courseId ,  "
				+ " :createdBy, :createdDate,  :lastModifiedBy, :lastModifiedDate, :acadYear,:acadMonth, :subject, :description,:messageReply,:messageRepliedBy,:messageRepliedDate ) ";
		return sql;
	}

	public int getNoOfStudentsAllocated(Long id) {
		String sql = " select count(*) from student_message where messageId = ?";

		return getJdbcTemplate().queryForObject(sql, Integer.class,
				new Object[] { id });
	}

	@Override
	protected String getUpdateSql() {
		final String sql = "Update student_message set "

		+ "messageReply =:messageReply ,"
				+ "messageRepliedBy = :messageRepliedBy ,"
				+ "messageRepliedDate = :messageRepliedDate ,"

				+ "lastModifiedBy = :lastModifiedBy,"
				+ "lastModifiedDate = :lastModifiedDate "

				+ "where id = :id ";
		return sql;
	}

	@Override
	protected String getUpsertSql() {
		return null;
	}

	public List<StudentMessage> getStudentsForMessageForAllocation(
			Long messageId, Long courseId) {
		String sql = "select uc.username, p.programName, u.firstname, u.lastname, sm.messageId, sm.id "
				+ " from user_course uc"
				+ "                          inner join users u on uc.username = u.username"
				+ "                          inner join program p on u.programId = p.id"
				+ "                          left outer  join student_message sm on sm.username = u.username and sm.courseId = uc.courseId and sm.messageId = ? "
				+ " where"
				+ " uc.role = ? and uc.courseId = ?  order by sm.lastModifiedDate desc ";
		return findAllSQL(sql,
				new Object[] { messageId, Role.ROLE_STUDENT.name(), courseId });

	}

	public List<StudentMessage> getStudentsForMessage(Long messageId,
			Long courseId, String acadMonth, Integer acadYear) {
		String sql = "select uc.username, p.programName, u.firstname, u.lastname, sm.messageId, sm.id "
				+ " from user_course uc"
				+ "                          inner join users u on uc.username = u.username"
				+ "                          inner join program p on u.programId = p.id"
				+ "                          inner  join student_message sm on sm.username = u.username and sm.courseId = uc.courseId and sm.messageId = ? "
				+ " where"
				+ " uc.role = ? and uc.courseId = ? and uc.acadMonth = ? and uc.acadYear = ? order by sm.id asc ";
		return findAllSQL(sql,
				new Object[] { messageId, Role.ROLE_STUDENT.name(), courseId,
						acadMonth, acadYear });

	}

	public List<StudentMessage> getUsersForMessage(Long messageId) {
		String sql = "select u.username, u.firstname, u.lastname, sm.messageId, sm.id  from users u"

				+ "     left outer  join student_message sm on sm.username = u.username and sm.messageId = ? "
				+ "  order by sm.id asc ";
		return findAllSQL(sql, new Object[] { messageId });

	}

	public List<StudentMessage> getUsersForMessageByCourse(
			Long courseId) {
		String sql = "select u.username,uc.username, p.programName, u.firstname, u.lastname,c.courseName "
				+ " from user_course uc "
				+ "                          inner join users u on uc.username = u.username "
				+ "                          inner join program p on u.programId = p.id "
				+ "                        inner join course c on c.id = uc.courseId "

				+ "  where uc.courseId = ?";

		return findAllSQL(sql, new Object[] { courseId });
	}

	public List<StudentMessage> findByUserMessage(String username) {
		final String sql = "select * " + " from student_message  "
				+ " where username= ? and active = 'Y'";
		return findAllSQL(sql, new Object[] { username });
	}

	public List<StudentMessage> findByCreatedBy(String username,
			String messageRepliedBy) {
		String sql = "select m.* from student_message m where (createdBy = ? or messageRepliedBy = ?) and active='Y'";
		return findAllSQL(sql, new Object[] { username, messageRepliedBy });
	}

	public void updateMessage(Long id, StudentMessage messageReply,
			String username) {
		logger.info("ID--------------------->" + id);
		String sql = " update student_message set messageReply = ? , messageRepliedBy = ? , messageRepliedDate= sysdate() where id = ? ";
		executeUpdateSql(sql, new Object[] { messageReply.getMessageReply(),
				messageReply.getId(), username });
	}
}