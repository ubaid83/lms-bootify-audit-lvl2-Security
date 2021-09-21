package com.spts.lms.daos.newsEvents;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.spts.lms.beans.announcement.Announcement;
import com.spts.lms.beans.newsEvents.NewsEvents;
import com.spts.lms.daos.BaseDAO;

@Repository("newsEventsDAO")
public class NewsEventsDAO extends BaseDAO<NewsEvents>{

	@Override
	protected String getTableName() {
		return "news_events";
	}

	@Override
	protected String getInsertSql() {
		String sql = " INSERT INTO "+ getTableName() +" (subject, description, type, startTime, "
				+ " endTime, createdBy, createdDate, lastModifiedBy, lastModifiedDate) "
				+ " VALUES (:subject, :description, :type, :startTime, :endTime, :createdBy, "
				+ " :createdDate, :lastModifiedBy, :lastModifiedDate) ";
		return sql;
	}

	@Override
	protected String getUpdateSql() {
		String sql = " UPDATE "+ getTableName() +" SET "
				+ " subject=:subject, description=:description, type=:type, startTime=:startTime, "
				+ " endTime=:endTime, createdBy=:createdBy, createdDate=:createdDate, "
				+ " lastModifiedBy=:lastModifiedBy, lastModifiedDate=:lastModifiedDate, active=:active "
				+ " WHERE id=:id ";
		return sql;
	}

	@Override
	protected String getUpsertSql() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public List<NewsEvents> getAllActiveNews() {

		String sql = " select * from news_events n where n.active = 'Y' and n.type = 'NEWS' and ( now() between n.startTime and n.endTime ) ";

		return findAllSQL(sql, new Object[] { });
	}

	public List<NewsEvents> getAllActiveEvents() {

		String sql = " select * from news_events n where n.active = 'Y' and n.type = 'EVENTS' and ( now() between n.startTime and n.endTime ) ";

		return findAllSQL(sql, new Object[] { });
	}
	
}
