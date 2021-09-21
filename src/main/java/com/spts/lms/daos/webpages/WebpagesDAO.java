package com.spts.lms.daos.webpages;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.spts.lms.beans.LmsDb;
import com.spts.lms.beans.webpages.Webpages;
import com.spts.lms.daos.BaseDAO;

@Repository("webpagesDAO")
public class WebpagesDAO extends BaseDAO<Webpages> {

	@Override
	protected String getTableName() {
		// TODO Auto-generated method stub
		return "webpages";
	}

	private static String tableName;

	public void getLmsDb(LmsDb bean) {
		tableName = bean.getLmsDb() + ".webpages";
		System.out.println("table name is" + tableName);
	}

	@Override
	protected String getInsertSql() {
		final String sql = "INSERT INTO "
				+ getTableName()
				+ "(name,content,startDate,endDate,filePath,active,description,dbListName,"
				+ "makeAvailable,type,createdBy,createdDate,lastModifiedBy,lastModifiedDate) "
				+ "VALUES (:name,:content,:startDate,:endDate,:filePath,'Y',:description,:dbListName,:makeAvailable,:type,:createdBy,:createdDate,:lastModifiedBy,:lastModifiedDate)";
		return sql;
	}

	@Override
	protected String getUpdateSql() {
		final String sql = "Update " + getTableName() + " set "
				+ "name = :name," + "content = :content ,"
				+ "description = :description ," + "startDate = :startDate ,"
				+ "endDate = :endDate ," + "filePath = :filePath,"
				+ "active = :active," + "makeAvailable = :makeAvailable ,"
				+ "type = :type ," + "lastModifiedBy = :lastModifiedBy ,"
				+ "active = :active ,"
				+ "lastModifiedDate = :lastModifiedDate " + "where id = :id ";
		return sql;
	}

	@Override
	protected String getUpsertSql() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Webpages> findWebpagesForTypeAndCreatedBy(String type,
			String username) {
		String sql = "select * from " + getTableName()
				+ " where type = ? and createdBy = ? and active = 'Y'";
		return findAllSQL(sql, new Object[] { type, username });
	}

	public List<Webpages> findAvailWebpages() {
		String sql = "select * from "
				+ getTableName()
				+ " where makeAvailable = 'Y' and startDate  <= sysdate() and endDate  >= sysdate() and active = 'Y' and type = 'LIBRARY'";
		return findAllSQL(sql, new Object[] {});
	}

	public List<Webpages> findAvailArticles() {
		String sql = "select * from "
				+ getTableName()
				+ " where makeAvailable = 'Y' and startDate  <= sysdate() and endDate  >= sysdate() and active = 'Y' and type = 'ARTICLE'";
		return findAllSQL(sql, new Object[] {});
	}

	public List<Webpages> findAllArticles() {
		String sql = "select * from " + getTableName()
				+ " where active = 'Y' and type = 'ARTICLE'";
		return findAllSQL(sql, new Object[] {});
	}

	public List<Webpages> findSchoolWithCollegeName() {
		String sql = "select DISTINCT(s.abbr), p.collegeName from schoolinstmap s , programs p where p.abbr=s.abbr";
		return findAllSQL(sql, new Object[] {});
	}

	public List<Webpages> findAvailLibraryResources() {
		String sql = "select * from " + getTableName()
				+ " where active = 'Y' and type = 'LIBRARY'";
		return findAllSQL(sql, new Object[] {});
	}

}
