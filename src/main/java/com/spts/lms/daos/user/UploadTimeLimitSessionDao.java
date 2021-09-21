package com.spts.lms.daos.user;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.spts.lms.beans.course.UserCourse;
import com.spts.lms.beans.user.UploadTimeLimitSession;
import com.spts.lms.daos.BaseDAO;

//@Component("UploadTimeLimitSessionDao")
@Repository("uploadTimeLimitSessionDao")
public class UploadTimeLimitSessionDao extends BaseDAO<UploadTimeLimitSession> {

	@Override
	protected String getTableName() {

		return "upload_time_limit_session";
	}

	

	@Override
	protected String getInsertSql() {
		final String sql = "INSERT INTO upload_time_limit_session (session, startDate, endDate,createdBy,lastModifiedBy,createdDate,active,lastModifiedDate) values(:session,:startDate,:endDate,:createdBy,:lastModifiedBy,:createdDate,:active,:lastModifiedDate)";
		
		return sql;
	}

	@Override
	protected String getUpdateSql() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getUpsertSql() {
	
		return null;
	}

}
