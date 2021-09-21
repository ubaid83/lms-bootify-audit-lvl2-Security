package com.spts.lms.daos.ica;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.spts.lms.beans.ica.IcaBean;
import com.spts.lms.beans.ica.IcaTotalMarks;
import com.spts.lms.beans.ica.NsBean;
import com.spts.lms.beans.user.Role;
import com.spts.lms.daos.BaseDAO;

@Repository("nsDAO")
public class NsDAO extends BaseDAO<NsBean> {

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	protected String getTableName() {

		return "nc_ica";
	}

	@Override
	protected String getInsertSql() {

		String sql = "Insert into "
				+ getTableName()
				+ " ( acadYear,active,"
				+ " programId,moduleId,icaName,isSubmitted,isPublished,showToStudents,acadSession,campusId,createdBy,createdDate,lastModifiedBy,"
				+ " lastModifiedDate) values"
				+ " (:acadYear,:active,"
				+ " :programId,:moduleId,:icaName,:isSubmitted,:isPublished,:showToStudents,:acadSession,:campusId,:createdBy,:createdDate,:lastModifiedBy,"
				+ " :lastModifiedDate) ";
		return sql;
	}

	@Override
	protected String getUpdateSql() {

		String sql = " update "
				+ getTableName()
				+ " set icaName=:icaName , lastModifiedBy=:lastModifiedBy,isPublished=:isPublished,isSubmitted=:isSubmitted,showToStudents=:showToStudents,"
				+ " lastModifiedDate=:lastModifiedDate " + " where id=:id  ";
		return sql;
	}

	@Override
	protected String getUpsertSql() {

		return null;
	}

	public List<NsBean> findNsListByProgram(String username) {

       /* String sql = " select distinct n.`*`,m.module_description,p.programName, pc.campusName from module m,program p,"
        		+  " nc_ica n,program_campus pc where"
                    + " (n.createdBy =? or n.lastModifiedBy=?) and n.active='Y' and m.module_id=n.moduleId"
                    + " and n.programId=p.id and n.campusId=pc.campusId group by n.id";*/
		
		String sql = " select distinct n.*,m.module_description,p.programName, pc.campusName from "
     		   +" nc_ica n inner join module m on n.moduleId=m.module_id inner join program p on n.programId=p.id "
     		   +" left outer join program_campus pc on pc.campusId=n.campusId and pc.programId=n.programId "
					
     		   +" where   (n.createdBy =? or n.lastModifiedBy=?) and n.active='Y' group by n.id order by n.id desc";

        return findAllSQL(sql, new Object[] { username, username });
  }


	public int updateNCIcaToPublished(String icaId, Date lastModifiedDate) {

		String sql = " update " + getTableName()
				+ " set isPublished = 'Y',lastModifiedDate = ?  where id = ? ";

		return getJdbcTemplate().update(sql,
				new Object[] { lastModifiedDate, icaId });

	}

	public NsBean checkAlreadyExistNS(String moduleId, String acadYear,
			String programId, String campusId, String acadSession) {

		if (campusId != null) {
			
				String sql = " select * from "
						+ getTableName()
						+ " where moduleId = ?  and acadYear =? and programId=? and campusId = ? and acadSession = ? and active = 'Y' limit 1";
				return findOneSQL(sql, new Object[] { moduleId, acadYear,
						programId, campusId, acadSession });
			
		} else {
			String sql = " select * from "
					+ getTableName()
					+ " where moduleId = ?  and acadYear =?  and programId=?  and acadSession = ? and active = 'Y' limit 1";
			return findOneSQL(sql, new Object[] { moduleId, acadYear,
					programId, acadSession });
		}
	}
	
	public NsBean actionNS(Long id) {
		String sql= "select * from nc_ica where isSubmitted='Y' and isPublished='Y' and id=?";
		return findOneSQL(sql, new Object[] {id});
	}
	
	public List<NsBean> getNcGradedByUser(String username) {

		String sql = " select distinct m.module_description,n.acadYear,n.acadSession,s.grade from nc_ica n, student_nc_ica s,module m "
				+ "where n.id = s.icaId and m.module_id=n.moduleId and n.active='Y' and n.showToStudents='Y' and n.isPublished = 'Y' and s.username=?";
		
		return findAllSQL(sql, new Object[]{username});
	}

}
