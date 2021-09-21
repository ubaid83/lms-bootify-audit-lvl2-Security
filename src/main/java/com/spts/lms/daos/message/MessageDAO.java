package com.spts.lms.daos.message;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;
import com.spts.lms.beans.grievances.Grievances;
import com.spts.lms.beans.message.Message;
import com.spts.lms.beans.message.StudentMessage;
import com.spts.lms.daos.BaseDAO;

@Repository("messageDAO")
public class MessageDAO extends BaseDAO<Message> {

	@Override
	protected String getTableName() {
		return "message";
	}

	@Override
	protected String getInsertSql() {

		String sql = "insert into "
				+ getTableName()
				+ "(courseId, facultyId, acadYear, acadMonth, "
				+ " createdBy, createdDate, lastModifiedBy, lastModifiedDate, subject,description) values "
				+ "(:courseId, :facultyId, :acadYear,  :acadMonth,"
				+ " :createdBy, :createdDate,  :lastModifiedBy, :lastModifiedDate, :subject, :description ) ";
		return sql;
	}

	@Override
	protected String getUpdateSql() {
		final String sql = "Update message set " + "courseId = :courseId,"
				 + "where id = :id ";
		return sql;
	}

	@Override
	protected String getUpsertSql() {
		return null;
	}
	
	 public void updateMessage(Long id,Message messageReply){
         String sql = " update message set messageReply = ?   , messageReplyTimeStamp= sysdate() where id = ? ";
         executeUpdateSql(sql,new Object[]{messageReply.getMessageReply(),messageReply.getId()});
}
	
	/*  public void updateMessage(Long id,StudentMessage messageReply, String username){
          String sql = " update student_message set messageReply = ? ,messageRepliedBy = ?, messageRepliedDate= sysdate() where id = ? ";
          executeUpdateSql(sql,new Object[]{messageReply.getMessageReply(),messageReply.getId(),username});
}*/
	 
	 public List<Message> getReceivedMessage(String userName){

         return findAllSQL(" select * from student_message sm inner join users u on u.username = sm.createdBy where sm.username = ? ", new Object[]{userName});

   }
	  

}