package com.spts.lms.daos.ica;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Repository;

import com.spts.lms.beans.ica.IcaTotalMarks;
import com.spts.lms.beans.user.Role;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.web.utils.Utils;

@Repository("icaTotalMarksDAO")
public class IcaTotalMarksDAO extends BaseDAO<IcaTotalMarks> {

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	protected String getTableName() {

		return "ica_total_marks";

	}

	@Override
	protected String getInsertSql() {

		return null;
	}

	@Override
	protected String getUpdateSql() {
		return null;
	}

	@Override
	protected String getUpsertSql() {
		String sql = "Insert into " + getTableName()
				+ " ( username,icaId,icaTotalMarks,icaScaledMarks,saveAsDraft,finalSubmit,"
				+ " active,remarks,isAbsent,isQueryApproved,createdBy,createdDate,lastModifiedBy,lastModifiedDate) values"
				+ " (:username,:icaId,:icaTotalMarks,:icaScaledMarks,:saveAsDraft,:finalSubmit,"
				+ " :active,:remarks,:isAbsent,:isQueryApproved,:createdBy,:createdDate,:lastModifiedBy,:lastModifiedDate) "

				+ "  ON DUPLICATE KEY UPDATE " + "  icaTotalMarks=:icaTotalMarks,icaScaledMarks=:icaScaledMarks,"
				+ " saveAsDraft=:saveAsDraft,finalSubmit=:finalSubmit,"
				+ " active=:active,remarks=:remarks,isAbsent=:isAbsent,isQueryApproved=:isQueryApproved,"
				+ " lastModifiedBy=:lastModifiedBy,lastModifiedDate=:lastModifiedDate ";

		return sql;
	}

	public List<IcaTotalMarks> icaTotalMarksByIcaId(Long icaId) {
		String sql = " select * from " + getTableName() + " where icaId = ? ";
		return findAllSQL(sql, new Object[] { icaId });
	}

	public List<IcaTotalMarks> getIcaTotalMarksByUser(String username) {

		String sql = " select icm.*,i.publishedDate,(case when icm.icaScaledMarks is not null then i.scaledMarks  else i.internalMarks  end)"
				+ "  as internalMarks, (case when icm.icaScaledMarks is not null then CEIL(icm.icaScaledMarks)  else CEIL(icm.icaTotalMarks)  end) "
				+ " as icaTotalMarks, "

				+ " (case when icm.icaScaledMarks is not null then( "
				+ "case when CEIL(icm.icaScaledMarks) >= CONVERT(i.internalPassMarks,decimal) then 'PASS' else 'FAIL' end) "
				+ "	else (case when CEIL(icm.icaTotalMarks) >= CONVERT(i.internalPassMarks,decimal) then 'PASS' else 'FAIL' end) "
				+ " end) as passFailStatus,  "
				+ "c.moduleName,i.acadYear,i.acadSession,i.moduleId from ica i,ica_total_marks icm,course c "
				+ "	where i.id=icm.icaId and icm.username = ? and i.active = 'Y' and i.moduleId=c.moduleId and icm.finalSubmit = 'Y' "
				+ "	AND icm.active= 'Y' and i.isPublished = 'Y' and (i.isNonEventModule <> 'Y'  OR i.isNonEventModule is null) group by i.id ";

		return findAllSQL(sql, new Object[] { username });
	}

	public List<IcaTotalMarks> getIcaTotalMarksByUserForNonEvent(String username) {

		String sql = " select icm.*,i.publishedDate,(case when icm.icaScaledMarks is not null then i.scaledMarks  else i.internalMarks  end)"
				+ "  as internalMarks, (case when icm.icaScaledMarks is not null then CEIL(icm.icaScaledMarks)  else CEIL(icm.icaTotalMarks)  end) "
				+ " as icaTotalMarks, "

				+ " (case when icm.icaScaledMarks is not null then( "
				+ "case when CEIL(icm.icaScaledMarks) >= CONVERT(i.internalPassMarks,decimal) then 'PASS' else 'FAIL' end) "
				+ "	else (case when CEIL(icm.icaTotalMarks) >= CONVERT(i.internalPassMarks,decimal) then 'PASS' else 'FAIL' end) "
				+ " end) as passFailStatus,  "
				+ "m.module_description as moduleName,i.acadYear,i.acadSession,i.moduleId from ica i, ica_total_marks icm, module m "
				+ "	where i.id=icm.icaId and icm.username = ? and i.active = 'Y' and i.moduleId=m.module_id and icm.finalSubmit = 'Y' "
				+ "	AND icm.active= 'Y' and i.isPublished = 'Y' and i.isNonEventModule = 'Y' group by i.id ";

		return findAllSQL(sql, new Object[] { username });
	}

	public List<IcaTotalMarks> getIcaTotalMarksByIca(String icaId) {

		String sql = " select concat(u.firstName,' ',u.lastName) as studentName,u.rollNo, "
				+ " icm.*,i.publishedDate,(case when icm.icaScaledMarks is not null then i.scaledMarks  "
				+ " else i.internalMarks  end) "
				+ "  as internalMarks, (case when icm.icaScaledMarks is not null then CEIL(icm.icaScaledMarks)  "
				+ " else CEIL(icm.icaTotalMarks)  end) " + " as icaTotalMarks, "

				+ " (case when icm.icaScaledMarks is not null then( "
				+ " case when CEIL(icm.icaScaledMarks) >= CONVERT(i.internalPassMarks,decimal) then 'PASS' else 'FAIL' end) "
				+ "	else (case when CEIL(icm.icaTotalMarks) >= CONVERT(i.internalPassMarks,decimal) then 'PASS' else 'FAIL' end) "
				+ " end) as passFailStatus,  "
				+ " c.moduleName,i.acadYear,i.acadSession,i.moduleId from ica i,ica_total_marks icm,course c,users u "
				+ "	where i.id=icm.icaId and icm.icaId = ? and i.active = 'Y' and i.moduleId=c.moduleId and u.username=icm.username"
				+ "	and icm.active= 'Y' group by i.id,icm.username ";

		return findAllSQL(sql, new Object[] { icaId });
	}

	public List<IcaTotalMarks> getIcaTotalMarksByIcaForNonEvent(String icaId) {

		String sql = " select concat(u.firstName,' ',u.lastName) as studentName,u.rollNo, "
				+ " icm.*,i.publishedDate,(case when icm.icaScaledMarks is not null then i.scaledMarks  "
				+ " else i.internalMarks  end) "
				+ "  as internalMarks, (case when icm.icaScaledMarks is not null then CEIL(icm.icaScaledMarks)  "
				+ " else  CEIL(icm.icaTotalMarks)  end) " + " as icaTotalMarks, "

				+ " (case when icm.icaScaledMarks is not null then( "
				+ " case when CEIL(icm.icaScaledMarks) >= CONVERT(i.internalPassMarks,decimal) then 'PASS' else 'FAIL' end) "
				+ "	else (case when CEIL(icm.icaTotalMarks) >= CONVERT(i.internalPassMarks,decimal) then 'PASS' else 'FAIL' end) "
				+ " end) as passFailStatus,  "
				+ " m.module_description as moduleName,i.acadYear,i.acadSession,i.moduleId from ica i,ica_total_marks icm,module m,users u "
				+ "	where i.id=icm.icaId and icm.icaId = ? and i.active = 'Y' and i.moduleId=m.module_id and u.username=icm.username"
				+ "	and icm.active= 'Y' group by i.id,icm.username ";

		return findAllSQL(sql, new Object[] { icaId });
	}

//	public List<IcaTotalMarks> getIcaTotalMarksByParam(String acadYear, String acadSession, String programId,
//			String campusId) {
//
//		Map<String, Object> params = new HashMap<String, Object>();
//
//		if (programId.contains(",")) {
//			List<String> programidsList = Arrays.asList(programId.split(","));
//			params.put("programids", programidsList);
//		} else {
//			params.put("programids", programId);
//
//		}
//
//		if (acadSession.contains(",")) {
//			List<String> acadSessionList = Arrays.asList(acadSession.split(","));
//			params.put("acadSession", acadSessionList);
//		} else {
//			params.put("acadSession", "%"+acadSession+"%");
//
//		}
//		params.put("acadYear", acadYear);
//		params.put("campusId", campusId);
//
//		logger.info("params------------><<" + params);
//
//		if (campusId != null) {
//
//			if (acadSession.contains(",")) {
//				String sql = " select concat(u.firstName,' ',u.lastName) as studentName,u.programId,u.rollNo,icm.*,i.publishedDate, "
//						+ " (case when icm.icaScaledMarks is not null then i.scaledMarks  else i.internalMarks  end) "
//						+ "  as internalMarks, (case when icm.icaScaledMarks is not null then CEIL(icm.icaScaledMarks)   "
//						+ " else CEIL(icm.icaTotalMarks)  end) " + "   as icaTotalMarks, "
//
//						+ "	 (case when icm.icaScaledMarks is not null then( case when CEIL(icm.icaScaledMarks) >= CONVERT(i.internalPassMarks,decimal) then 'PASS' else 'FAIL' end) "
//						+ "		else (case when CEIL(icm.icaTotalMarks) >= CONVERT(i.internalPassMarks,decimal) then 'PASS' else 'FAIL' end) "
//						+ "	 end) as passFailStatus,  "
//						+ "	 c.moduleName,c.acadYearCode as acadYear,c.acadSession,c.moduleId,"
//						+ " c.moduleAbbr from ica i,ica_total_marks icm,course c,users u "
//						+ "		where i.id=icm.icaId  and i.active = 'Y' and i.moduleId=c.moduleId "
//						+ " and c.programId in(:programids) " + " and c.acadSession IN(:acadSession) "
//						+ "	 and i.acadYear=:acadYear and i.campusId=:campusId and i.isSubmitted='Y' "
//						+ " and icm.finalSubmit = 'Y' and u.username=icm.username "
//						+ "	  and c.programId in(:programids) AND i.acadYear=c.acadYearCode "
//						+ "	  and i.moduleId=c.moduleId and i.campusId=c.campusId and c.active = 'Y' "
//						+ "	 AND icm.active= 'Y' group by i.id,icm.username ";
//
//				return getNamedParameterJdbcTemplate().query(sql, params,
//						BeanPropertyRowMapper.newInstance(IcaTotalMarks.class));
//			}else {
//				//acadSession like
//				String sql = " select concat(u.firstName,' ',u.lastName) as studentName,u.programId,u.rollNo,icm.*,i.publishedDate, "
//						+ " (case when icm.icaScaledMarks is not null then i.scaledMarks  else i.internalMarks  end) "
//						+ "  as internalMarks, (case when icm.icaScaledMarks is not null then CEIL(icm.icaScaledMarks)   "
//						+ " else CEIL(icm.icaTotalMarks)  end) " + "   as icaTotalMarks, "
//
//						+ "	 (case when icm.icaScaledMarks is not null then( case when CEIL(icm.icaScaledMarks) >= CONVERT(i.internalPassMarks,decimal) then 'PASS' else 'FAIL' end) "
//						+ "		else (case when CEIL(icm.icaTotalMarks) >= CONVERT(i.internalPassMarks,decimal) then 'PASS' else 'FAIL' end) "
//						+ "	 end) as passFailStatus,  "
//						+ "	 c.moduleName,c.acadYearCode as acadYear,i.acadSession,c.moduleId,"
//						+ " c.moduleAbbr from ica i,ica_total_marks icm,course c,users u "
//						+ "		where i.id=icm.icaId  and i.active = 'Y' and i.moduleId=c.moduleId "
//						+ " and c.programId in(:programids) " + " and i.acadSession like :acadSession "
//						+ "	 and i.acadYear=:acadYear and i.campusId=:campusId and i.isSubmitted='Y' "
//						+ " and icm.finalSubmit = 'Y' and u.username=icm.username "
//						+ "	  and c.programId in(:programids) AND i.acadYear=c.acadYearCode "
//						+ "	  and i.moduleId=c.moduleId and i.campusId=c.campusId and c.active = 'Y' "
//						+ "	 AND icm.active= 'Y' group by i.id,icm.username ";
//
//				return getNamedParameterJdbcTemplate().query(sql, params,
//						BeanPropertyRowMapper.newInstance(IcaTotalMarks.class));
//			}
//		} else {
//
//			if (acadSession.contains(",")) {
//				String sql = " select concat(u.firstName,' ',u.lastName) as studentName,u.rollNo,u.programId,icm.*,i.publishedDate, "
//						+ " (case when icm.icaScaledMarks is not null then i.scaledMarks  else i.internalMarks  end) "
//						+ "  as internalMarks, (case when icm.icaScaledMarks is not null then CEIL(icm.icaScaledMarks)  "
//						+ " else CEIL(icm.icaTotalMarks)  end) " + "   as icaTotalMarks, "
//						+ "	 (case when icm.icaScaledMarks is not null then( case when CEIL(icm.icaScaledMarks) >= CONVERT(i.internalPassMarks,decimal) then 'PASS' else 'FAIL' end) "
//						+ "		else (case when CEIL(icm.icaTotalMarks) >= CONVERT(i.internalPassMarks,decimal) then 'PASS' else 'FAIL' end) "
//						+ "	 end) as passFailStatus,  "
//						+ "	 c.moduleName,c.acadYearCode as acadYear,c.acadSession,c.moduleId,"
//						+ "c.moduleAbbr from ica i,ica_total_marks icm,course c,users u "
//						+ "		where i.id=icm.icaId  and i.active = 'Y' and i.moduleId=c.moduleId "
//						+ " and c.programId in(:programids) " + " and c.acadSession IN(:acadSession) "
//						+ "	and i.acadYear=:acadYear  and i.isSubmitted='Y' and icm.finalSubmit = 'Y' and u.username=icm.username "
//						+ "	  and c.programId in(:programids) AND i.acadYear=c.acadYearCode "
//						+ "	  and i.moduleId=c.moduleId and c.active = 'Y' "
//						+ "	 AND icm.active= 'Y' group by i.id,icm.username ";
//
//				return getNamedParameterJdbcTemplate().query(sql, params,
//						BeanPropertyRowMapper.newInstance(IcaTotalMarks.class));
//				}else {
//					//acadsession like 
//					String sql = " select concat(u.firstName,' ',u.lastName) as studentName,u.rollNo,u.programId,icm.*,i.publishedDate, "
//							+ " (case when icm.icaScaledMarks is not null then i.scaledMarks  else i.internalMarks  end) "
//							+ "  as internalMarks, (case when icm.icaScaledMarks is not null then CEIL(icm.icaScaledMarks)  "
//							+ " else CEIL(icm.icaTotalMarks)  end) " + "   as icaTotalMarks, "
//							+ "	 (case when icm.icaScaledMarks is not null then( case when CEIL(icm.icaScaledMarks) >= CONVERT(i.internalPassMarks,decimal) then 'PASS' else 'FAIL' end) "
//							+ "		else (case when CEIL(icm.icaTotalMarks) >= CONVERT(i.internalPassMarks,decimal) then 'PASS' else 'FAIL' end) "
//							+ "	 end) as passFailStatus,  "
//							+ "	 c.moduleName,c.acadYearCode as acadYear,i.acadSession,c.moduleId,"
//							+ "c.moduleAbbr from ica i,ica_total_marks icm,course c,users u "
//							+ "		where i.id=icm.icaId  and i.active = 'Y' and i.moduleId=c.moduleId "
//							+ " and c.programId in(:programids) " + " and i.acadSession like :acadSession "
//							+ "	and i.acadYear=:acadYear  and i.isSubmitted='Y' and icm.finalSubmit = 'Y' and u.username=icm.username "
//							+ "	  and c.programId in(:programids) AND i.acadYear=c.acadYearCode "
//							+ "	  and i.moduleId=c.moduleId and c.active = 'Y' "
//							+ "	 AND icm.active= 'Y' group by i.id,icm.username ";
//
//					return getNamedParameterJdbcTemplate().query(sql, params,
//							BeanPropertyRowMapper.newInstance(IcaTotalMarks.class));
//				}
//
//		}
//	}
	//26-04-2021
	public List<IcaTotalMarks> getIcaTotalMarksByParamCoursera(String acadYear, String acadSession, String programId,
			String campusId) {
		if (campusId != null) {
			String sql = " select concat(u.firstName,' ',u.lastName) as studentName,u.programId,u.rollNo,icm.*,i.publishedDate, "
					+ " (case when icm.icaScaledMarks is not null then i.scaledMarks  else i.internalMarks  end) "
					+ "  as internalMarks, (case when icm.icaScaledMarks is not null then CEIL(icm.icaScaledMarks)   "
					+ " else CEIL(icm.icaTotalMarks)  end) " + "   as icaTotalMarks, "

					+ "	 (case when icm.icaScaledMarks is not null then( case when CEIL(icm.icaScaledMarks) >= CONVERT(i.internalPassMarks,decimal) then 'PASS' else 'FAIL' end) "
					+ "		else (case when CEIL(icm.icaTotalMarks) >= CONVERT(i.internalPassMarks,decimal) then 'PASS' else 'FAIL' end) "
					+ "	 end) as passFailStatus,  "
					+ "	 c.moduleName,c.acadYearCode as acadYear,c.acadSession,c.moduleId,"
					+ " c.moduleAbbr from ica i,ica_total_marks icm,course c,users u "
					+ "		where i.id=icm.icaId  and i.active = 'Y' and i.moduleId=c.moduleId "
					+ " and i.programId like ? " + " and i.acadSession=? "
					+ "	 and i.acadYear=? and i.campusId=? and i.isSubmitted='Y' "
					+ " and icm.finalSubmit = 'Y' and u.username=icm.username "
					+ "	 AND i.acadYear=c.acadYearCode "
					+ "	  and i.moduleId=c.moduleId and i.campusId=c.campusId and c.active = 'Y' "
					+ "	 AND icm.active= 'Y' and i.isNonEventModule <> 'Y' group by i.id,icm.username ";
			return findAllSQL(sql, new Object [] {"%"+programId+"%",acadSession,acadYear,campusId});
		}else {
			String sql = " select concat(u.firstName,' ',u.lastName) as studentName,u.programId,u.rollNo,icm.*,i.publishedDate, "
					+ " (case when icm.icaScaledMarks is not null then i.scaledMarks  else i.internalMarks  end) "
					+ "  as internalMarks, (case when icm.icaScaledMarks is not null then CEIL(icm.icaScaledMarks)   "
					+ " else CEIL(icm.icaTotalMarks)  end) " + "   as icaTotalMarks, "

					+ "	 (case when icm.icaScaledMarks is not null then( case when CEIL(icm.icaScaledMarks) >= CONVERT(i.internalPassMarks,decimal) then 'PASS' else 'FAIL' end) "
					+ "		else (case when CEIL(icm.icaTotalMarks) >= CONVERT(i.internalPassMarks,decimal) then 'PASS' else 'FAIL' end) "
					+ "	 end) as passFailStatus,  "
					+ "	 c.moduleName,c.acadYearCode as acadYear,c.acadSession,c.moduleId,"
					+ " c.moduleAbbr from ica i,ica_total_marks icm,course c,users u "
					+ "		where i.id=icm.icaId  and i.active = 'Y' and i.moduleId=c.moduleId "
					+ " and i.programId like ? " + " and i.acadSession=? "
					+ "	 and i.acadYear=? and i.isSubmitted='Y' "
					+ " and icm.finalSubmit = 'Y' and u.username=icm.username "
					+ "	 AND i.acadYear=c.acadYearCode "
					+ "	  and i.moduleId=c.moduleId and i.campusId=c.campusId and c.active = 'Y' "
					+ "	 AND icm.active= 'Y' and i.isNonEventModule <> 'Y' group by i.id,icm.username ";
			return findAllSQL(sql, new Object [] {"%"+programId+"%",acadSession,acadYear});
		}
		
	}
	public List<IcaTotalMarks> getIcaTotalMarksByParam(String acadYear, String acadSession, String programId,
			String campusId) {

		Map<String, Object> params = new HashMap<String, Object>();

		if (programId.contains(",")) {
			List<String> programidsList = Arrays.asList(programId.split(","));
			params.put("programids", programidsList);
			params.put("programidsLike","%" +programId+"%");
		} else {
			params.put("programids",programId);
			params.put("programidsLike","%" +programId+"%");

		}

		if (acadSession.contains(",")) {
			List<String> acadSessionList = Arrays.asList(acadSession.split(","));
			params.put("acadSession", acadSessionList);
		} else {
			params.put("acadSessioncomma","%" +acadSession+",%");
			params.put("acadSession","%" +acadSession);

		}
		params.put("acadYear", acadYear);
		params.put("campusId", campusId);

		logger.info("params------------><<" + params);

		if (campusId != null) {

			if (acadSession.contains(",")) {
				String sql = " select concat(u.firstName,' ',u.lastName) as studentName,u.programId,u.rollNo,icm.*,i.publishedDate, "
						+ " (case when icm.icaScaledMarks is not null then i.scaledMarks  else i.internalMarks  end) "
						+ "  as internalMarks, (case when icm.icaScaledMarks is not null then CEIL(icm.icaScaledMarks)   "
						+ " else CEIL(icm.icaTotalMarks)  end) " + "   as icaTotalMarks, "

						+ "	 (case when icm.icaScaledMarks is not null then( case when CEIL(icm.icaScaledMarks) >= CONVERT(i.internalPassMarks,decimal) then 'PASS' else 'FAIL' end) "
						+ "		else (case when CEIL(icm.icaTotalMarks) >= CONVERT(i.internalPassMarks,decimal) then 'PASS' else 'FAIL' end) "
						+ "	 end) as passFailStatus,  "
						+ "	 c.moduleName,c.acadYearCode as acadYear,c.acadSession,c.moduleId,"
						+ " c.moduleAbbr from ica i,ica_total_marks icm,course c,users u "
						+ "		where i.id=icm.icaId  and i.active = 'Y' and i.moduleId=c.moduleId "
						+ " and (i.programId like :programidsLike or c.programId IN(:programids)) " + " and c.acadSession IN(:acadSession) "
						+ "	 and i.acadYear=:acadYear and i.campusId=:campusId and i.isSubmitted='Y' "
						+ " and icm.finalSubmit = 'Y' and u.username=icm.username "
						+ "	 AND i.acadYear=c.acadYearCode "
						+ "	  and i.moduleId=c.moduleId and i.campusId=c.campusId and c.active = 'Y' "
						+ "	 AND icm.active= 'Y' and i.isNonEventModule <> 'Y' group by i.id,icm.username ";

				return getNamedParameterJdbcTemplate().query(sql, params,
						BeanPropertyRowMapper.newInstance(IcaTotalMarks.class));
			}else {
				//acadSession like
				String sql = " select concat(u.firstName,' ',u.lastName) as studentName,u.programId,u.rollNo,icm.*,i.publishedDate, "
						+ " (case when icm.icaScaledMarks is not null then i.scaledMarks  else i.internalMarks  end) "
						+ "  as internalMarks, (case when icm.icaScaledMarks is not null then CEIL(icm.icaScaledMarks)   "
						+ " else CEIL(icm.icaTotalMarks)  end) " + "   as icaTotalMarks, "

						+ "	 (case when icm.icaScaledMarks is not null then( case when CEIL(icm.icaScaledMarks) >= CONVERT(i.internalPassMarks,decimal) then 'PASS' else 'FAIL' end) "
						+ "		else (case when CEIL(icm.icaTotalMarks) >= CONVERT(i.internalPassMarks,decimal) then 'PASS' else 'FAIL' end) "
						+ "	 end) as passFailStatus,  "
						+ "	 c.moduleName,c.acadYearCode as acadYear,i.acadSession,c.moduleId,"
						+ " c.moduleAbbr from ica i,ica_total_marks icm,course c,users u "
						+ "		where i.id=icm.icaId  and i.active = 'Y' and i.moduleId=c.moduleId "
						+ " and (i.programId like :programidsLike or c.programId IN(:programids))" + " and (i.acadSession like :acadSession or i.acadSession like :acadSessioncomma)"
						+ "	 and i.acadYear=:acadYear and i.campusId=:campusId and i.isSubmitted='Y' "
						+ " and icm.finalSubmit = 'Y' and u.username=icm.username "
						+ "	AND i.acadYear=c.acadYearCode "
						+ "	  and i.moduleId=c.moduleId and i.campusId=c.campusId and c.active = 'Y' "
						+ "	 AND icm.active= 'Y' and i.isNonEventModule <> 'Y' group by i.id,icm.username ";

				return getNamedParameterJdbcTemplate().query(sql, params,
						BeanPropertyRowMapper.newInstance(IcaTotalMarks.class));
			}
		} else {

			if (acadSession.contains(",")) {
				String sql = " select concat(u.firstName,' ',u.lastName) as studentName,u.rollNo,u.programId,icm.*,i.publishedDate, "
						+ " (case when icm.icaScaledMarks is not null then i.scaledMarks  else i.internalMarks  end) "
						+ "  as internalMarks, (case when icm.icaScaledMarks is not null then CEIL(icm.icaScaledMarks)  "
						+ " else CEIL(icm.icaTotalMarks)  end) " + "   as icaTotalMarks, "
						+ "	 (case when icm.icaScaledMarks is not null then( case when CEIL(icm.icaScaledMarks) >= CONVERT(i.internalPassMarks,decimal) then 'PASS' else 'FAIL' end) "
						+ "		else (case when CEIL(icm.icaTotalMarks) >= CONVERT(i.internalPassMarks,decimal) then 'PASS' else 'FAIL' end) "
						+ "	 end) as passFailStatus,  "
						+ "	 c.moduleName,c.acadYearCode as acadYear,c.acadSession,c.moduleId,"
						+ "c.moduleAbbr from ica i,ica_total_marks icm,course c,users u "
						+ "		where i.id=icm.icaId  and i.active = 'Y' and i.moduleId=c.moduleId "
						+ " and (i.programId like :programidsLike or c.programId IN(:programids)) " + " and c.acadSession IN(:acadSession) "
						+ "	and i.acadYear=:acadYear  and i.isSubmitted='Y' and icm.finalSubmit = 'Y' and u.username=icm.username "
						+ "	 AND i.acadYear=c.acadYearCode "
						+ "	  and i.moduleId=c.moduleId and c.active = 'Y' "
						+ "	 AND icm.active= 'Y' and i.isNonEventModule <> 'Y' group by i.id,icm.username ";

				return getNamedParameterJdbcTemplate().query(sql, params,
						BeanPropertyRowMapper.newInstance(IcaTotalMarks.class));
				}else {
					//acadsession like 
					String sql = " select concat(u.firstName,' ',u.lastName) as studentName,u.rollNo,u.programId,icm.*,i.publishedDate, "
							+ " (case when icm.icaScaledMarks is not null then i.scaledMarks  else i.internalMarks  end) "
							+ "  as internalMarks, (case when icm.icaScaledMarks is not null then CEIL(icm.icaScaledMarks)  "
							+ " else CEIL(icm.icaTotalMarks)  end) " + "   as icaTotalMarks, "
							+ "	 (case when icm.icaScaledMarks is not null then( case when CEIL(icm.icaScaledMarks) >= CONVERT(i.internalPassMarks,decimal) then 'PASS' else 'FAIL' end) "
							+ "		else (case when CEIL(icm.icaTotalMarks) >= CONVERT(i.internalPassMarks,decimal) then 'PASS' else 'FAIL' end) "
							+ "	 end) as passFailStatus,  "
							+ "	 c.moduleName,c.acadYearCode as acadYear,i.acadSession,c.moduleId,"
							+ "c.moduleAbbr from ica i,ica_total_marks icm,course c,users u "
							+ "		where i.id=icm.icaId  and i.active = 'Y' and i.moduleId=c.moduleId "
							+ " and (i.programId like :programidsLike or c.programId IN(:programids)) " + " and (i.acadSession like :acadSession or i.acadSession like :acadSessioncomma)"
							+ "	and i.acadYear=:acadYear  and i.isSubmitted='Y' and icm.finalSubmit = 'Y' and u.username=icm.username "
							+ "	 AND i.acadYear=c.acadYearCode "
							+ "	  and i.moduleId=c.moduleId and c.active = 'Y' "
							+ "	 AND icm.active= 'Y' and i.isNonEventModule <> 'Y' group by i.id,icm.username ";

					return getNamedParameterJdbcTemplate().query(sql, params,
							BeanPropertyRowMapper.newInstance(IcaTotalMarks.class));
				}

		}
	}
	public List<IcaTotalMarks> getIcaTotalMarksByParamForNonEvent(String acadYear, String acadSession, String programId,
			String campusId) {

		String programIdL = "%" + programId + "%";
		if (campusId != null) {

			String sql = " select concat(u.firstName,' ',u.lastName) as studentName,u.rollNo,u.programId,icm.*,i.publishedDate, "
					+ " (case when icm.icaScaledMarks is not null then i.scaledMarks  else i.internalMarks  end)  "
					+ " as internalMarks, (case when icm.icaScaledMarks is not null then CEIL(icm.icaScaledMarks)   "
					+ " else CEIL(icm.icaTotalMarks)  end)  " + " as icaTotalMarks,  "

					+ "  (case when icm.icaScaledMarks is not null then( case when CEIL(icm.icaScaledMarks) >= CONVERT(i.internalPassMarks,decimal) then 'PASS' else 'FAIL' end) "
					+ " 	else (case when CEIL(icm.icaTotalMarks) >= CONVERT(i.internalPassMarks,decimal) then 'PASS' else 'FAIL' end)  "
					+ " end) as passFailStatus, "
					+ " m.module_description as moduleName,m.acadyear as acadYear,sm.sapSessionText as acadSession,m.module_id as moduleId,m.module_abbr as moduleAbbr from ica i,ica_total_marks icm,module m,session_master sm,users u  "
					+ "	where i.id=icm.icaId  and i.active = 'Y' and i.moduleId=m.module_id and m.session_code=sm.sapSessionCode and i.programId like ?  "
					+ "	and i.acadSession=?   and i.acadYear=? and i.campusId=? and i.isSubmitted='Y'  "
					+ " and icm.finalSubmit = 'Y' and u.username=icm.username  "
					+ "  and m.program_id=? AND i.acadYear=m.acadyear and i.acadSession=sm.sapSessionText   "
					+ "  and i.moduleId=m.module_id and i.isNonEventModule = 'Y' "
					+ " AND icm.active= 'Y' group by i.id,icm.username ";

			return findAllSQL(sql, new Object[] { programIdL, acadSession, acadYear, campusId, programId });
		} else {

			String sql = " select concat(u.firstName,' ',u.lastName) as studentName,u.rollNo,u.programId,icm.*,i.publishedDate, "
					+ " (case when icm.icaScaledMarks is not null then i.scaledMarks  else i.internalMarks  end) "
					+ " as internalMarks, (case when icm.icaScaledMarks is not null then CEIL(icm.icaScaledMarks)   "
					+ " else CEIL(icm.icaTotalMarks)  end)  " + " as icaTotalMarks,  "

					+ " (case when icm.icaScaledMarks is not null then( case when CEIL(icm.icaScaledMarks) >= CONVERT(i.internalPassMarks,decimal) then 'PASS' else 'FAIL' end) "
					+ " else (case when CEIL(icm.icaTotalMarks) >= CONVERT(i.internalPassMarks,decimal) then 'PASS' else 'FAIL' end) "
					+ "  end) as passFailStatus,   "
					+ " m.module_description as moduleName,m.acadyear as acadYear,sm.sapSessionText as acadSession,m.module_id as moduleId,m.module_abbr as moduleAbbr from ica i,ica_total_marks icm,module m,session_master sm,users u  "
					+ " where i.id=icm.icaId  and i.active = 'Y' and i.moduleId=m.module_id and m.session_code=sm.sapSessionCode and i.programId like ?  "
					+ " and i.acadSession=?   and i.acadYear=?  and i.isSubmitted='Y' and icm.finalSubmit = 'Y' and u.username=icm.username  "
					+ " and m.program_id=? AND i.acadYear=m.acadyear and i.acadSession=sm.sapSessionText   "
					+ " and i.moduleId=m.module_id and i.isNonEventModule = 'Y' "
					+ " AND icm.active= 'Y' group by i.id,icm.username ";

			return findAllSQL(sql, new Object[] { programIdL, acadSession, acadYear, programId });
		}
	}

	public List<IcaTotalMarks> getIcaTotalMarksByParamForNonEventDraft(String acadYear, String acadSession,
			String programId, String campusId) {

		String programIdL = "%" + programId + "%";
		if (campusId != null) {

			String sql = " select concat(u.firstName,' ',u.lastName) as studentName,u.rollNo,icm.*,i.publishedDate, "
					+ " (case when icm.icaScaledMarks is not null then i.scaledMarks  else i.internalMarks  end)  "
					+ " as internalMarks, (case when icm.icaScaledMarks is not null then CEIL(icm.icaScaledMarks)   "
					+ " else CEIL(icm.icaTotalMarks)  end)  " + " as icaTotalMarks,  "

					+ "  (case when icm.icaScaledMarks is not null then( case when CEIL(icm.icaScaledMarks) >= CONVERT(i.internalPassMarks,decimal) then 'PASS' else 'FAIL' end) "
					+ " 	else (case when CEIL(icm.icaTotalMarks) >= CONVERT(i.internalPassMarks,decimal) then 'PASS' else 'FAIL' end)  "
					+ " end) as passFailStatus, "
					+ " m.module_description as moduleName,m.acadyear as acadYear,sm.sapSessionText as acadSession,m.module_id as moduleId,m.module_abbr as moduleAbbr from ica i,ica_total_marks icm,module m,session_master sm,users u  "
					+ "	where i.id=icm.icaId  and i.active = 'Y' and i.moduleId=m.module_id and m.session_code=sm.sapSessionCode and i.programId like ?  "
					+ "	and i.acadSession=?   and i.acadYear=? and i.campusId=?   "
					+ " and icm.saveAsDraft = 'Y' and u.username=icm.username  "
					+ "  and m.program_id=? AND i.acadYear=m.acadyear and i.acadSession=sm.sapSessionText   "
					+ "  and i.moduleId=m.module_id and i.isNonEventModule = 'Y' "
					+ " AND icm.active= 'Y' group by i.id,icm.username ";

			return findAllSQL(sql, new Object[] { programIdL, acadSession, acadYear, campusId, programId });
		} else {

			String sql = " select concat(u.firstName,' ',u.lastName) as studentName,u.rollNo,icm.*,i.publishedDate, "
					+ " (case when icm.icaScaledMarks is not null then i.scaledMarks  else i.internalMarks  end) "
					+ " as internalMarks, (case when icm.icaScaledMarks is not null then CEIL(icm.icaScaledMarks)   "
					+ " else CEIL(icm.icaTotalMarks)  end)  " + " as icaTotalMarks,  "

					+ " (case when icm.icaScaledMarks is not null then( case when CEIL(icm.icaScaledMarks) >= CONVERT(i.internalPassMarks,decimal) then 'PASS' else 'FAIL' end) "
					+ " else (case when CEIL(icm.icaTotalMarks) >= CONVERT(i.internalPassMarks,decimal) then 'PASS' else 'FAIL' end) "
					+ "  end) as passFailStatus,   "
					+ " m.module_description as moduleName,m.acadyear as acadYear,sm.sapSessionText as acadSession,m.module_id as moduleId,m.module_abbr as moduleAbbr from ica i,ica_total_marks icm,module m,session_master sm,users u  "
					+ " where i.id=icm.icaId  and i.active = 'Y' and i.moduleId=m.module_id and m.session_code=sm.sapSessionCode and i.programId like ?  "
					+ " and i.acadSession=?   and i.acadYear=?  and icm.saveAsDraft = 'Y' and u.username=icm.username  "
					+ " and m.program_id=? AND i.acadYear=m.acadyear and i.acadSession=sm.sapSessionText   "
					+ " and i.moduleId=m.module_id and i.isNonEventModule = 'Y' "
					+ " AND icm.active= 'Y' group by i.id,icm.username ";

			return findAllSQL(sql, new Object[] { programIdL, acadSession, acadYear, programId });
		}
	}

	public int updateRaiseQuery(String icaId, String username, String query) {

		String sql = " update " + getTableName()
				+ " set isQueryRaise = 'Y',lastModifiedBy = ?,lastModifiedDate=?,query=? "
				+ "  where icaId = ? and username =? ";

		return getJdbcTemplate().update(sql, new Object[] { username, Utils.getInIST(), query, icaId, username });
	}

	public List<IcaTotalMarks> getRaiseQueries(String acadYear, Collection<GrantedAuthority> auth, String username) {

		if (acadYear != null) {
			if (auth.contains(Role.ROLE_ADMIN)) {

				/*
				 * String sql =
				 * " select i.icaName,i.id as icaId,(case when itm.icaScaledMarks is not null then itm.icaScaledMarks  "
				 * + " else itm.icaTotalMarks  end) as icaTotalMarks, " +
				 * " i.acadYear,i.acadSession,concat(u.firstName,' ',u.lastName) as studentName,p.programName, "
				 * + " concat(c.moduleName,'(',c.moduleAbbr,')') as moduleName,itm.`query`," +
				 * " itm.username,c1.courseName as division,concat(u1.firstName,' ',u1.lastName,'(',u1.username,')')"
				 * + " as assignedFaculty " +
				 * " from program p,ica_total_marks itm,users u,course c,users u1, ica i  " +
				 * " left outer join course c1 on c1.id=i.eventId " +
				 * " where itm.icaId=i.id and itm.username=u.username and u1.username=i.assignedFaculty "
				 * +
				 * " and i.moduleId=c.moduleId and i.acadYear=c.acadYear and i.acadSession=c.acadSession "
				 * + " and u.programId=p.id " + " and itm.isQueryRaise= 'Y'  " +
				 * " and (i.createdBy=? or i.lastModifiedBy= ?) and i.acadYear=? AND i.active = 'Y' "
				 * + " group by itm.username,itm.icaId ";
				 */

				/*
				 * String sql =
				 * " select i.icaName,i.id as icaId,(case when itm.icaScaledMarks is not null then itm.icaScaledMarks "
				 * + " else itm.icaTotalMarks  end) as icaTotalMarks, " +
				 * " i.acadYear,i.acadSession,concat(u.firstName,' ',u.lastName) as studentName,u.rollNo,u.email, p.programName, "
				 * +
				 * " concat(c.moduleName,'(',c.moduleAbbr,')') as moduleName,itm.`query`,iq.isApproved,DATE_FORMAT(iq.createdDate, \"%M %e %Y\") as raiseQDate, "
				 * +
				 * " itm.username,c1.courseName as division, GROUP_CONCAT(DISTINCT concat(u1.firstName,' ',u1.lastName,'(',u1.username,')')) "
				 * + " as assignedFaculty " +
				 * " from program p,ica_total_marks itm,users u,course c,users u1, ica i  " +
				 * " left outer join course c1 on c1.id=i.eventId " +
				 * " inner join ica_queries iq on iq.icaId=i.id " +
				 * " where itm.icaId=i.id and itm.username=u.username and FIND_IN_SET(u1.username,i.assignedFaculty) "
				 * +
				 * " and i.moduleId=c.moduleId and i.acadYear=c.acadYearCode and i.acadSession=c.acadSession "
				 * + " and u.programId=p.id " + " and itm.isQueryRaise= 'Y'  " +
				 * " and (i.createdBy=? or i.lastModifiedBy= ?) and i.acadYear = ? AND i.active = 'Y' "
				 * + " group by itm.username,itm.icaId ";
				 */

				String sql = " select i.icaName,i.id as icaId,(case when itm.icaScaledMarks is not null then itm.icaScaledMarks "
						+ " else itm.icaTotalMarks  end) as icaTotalMarks, (case when i.isCourseraIca='Y' then u.acadSession else i.acadSession end) AS acadSession ,"
						+ " i.acadYear,i.acadSession,concat(u.firstName,' ',u.lastName) as studentName,u.rollNo,u.email, p.programName, "
						+ " concat(c.moduleName,'(',c.moduleAbbr,')') as moduleName,itm.`query`,iq.isApproved,iq.createdDate as raiseQDate, "
						+ " itm.username,c1.courseName as division, GROUP_CONCAT(DISTINCT concat(u1.firstName,' ',u1.lastName,'(',u1.username,')')) "
						+ " as assignedFaculty "
						+ " from program p,ica_total_marks itm,users u,course c,users u1, ica i  "
						+ " left outer join course c1 on c1.id=i.eventId "
						+ " inner join ica_queries iq on iq.icaId=i.id "
						+ " where itm.icaId=i.id and itm.username=u.username and FIND_IN_SET(u1.username,i.assignedFaculty) "
						+ " and i.moduleId=c.moduleId and i.acadYear=c.acadYearCode " + " and u.programId=p.id "
						+ " and itm.isQueryRaise= 'Y' and (itm.flagTcs <> 'S' or itm.flagTcs is NULL)  "
						+ " and (i.createdBy=? or i.lastModifiedBy= ?) and i.acadYear = ? AND i.active = 'Y' "
						+ " group by itm.username,itm.icaId ";

				return findAllSQL(sql, new Object[] { username, username, acadYear });

			} else if (auth.contains(Role.ROLE_FACULTY)) {
				/*
				 * String sql =
				 * " select i.icaName,i.id as icaId,(case when itm.icaScaledMarks is not null then itm.icaScaledMarks  "
				 * + " else itm.icaTotalMarks  end) as icaTotalMarks, " +
				 * " i.acadYear,i.acadSession,concat(u.firstName,' ',u.lastName) as studentName,p.programName, "
				 * + " concat(c.moduleName,'(',c.moduleAbbr,')') as moduleName,itm.`query`," +
				 * " itm.username,c1.courseName as division,concat(u1.firstName,' ',u1.lastName,'(',u1.username,')')"
				 * + " as assignedFaculty " +
				 * " from program p,ica_total_marks itm,users u,course c,users u1, ica i  " +
				 * " left outer join course c1 on c1.id=i.eventId " +
				 * " where itm.icaId=i.id and itm.username=u.username and u1.username=i.assignedFaculty "
				 * +
				 * " and i.moduleId=c.moduleId and i.acadYear=c.acadYear and i.acadSession=c.acadSession "
				 * + " and u.programId=p.id " + " and itm.isQueryRaise= 'Y'  " +
				 * " and (i.assignedFaculty=?) and i.acadYear=? AND i.active = 'Y' " +
				 * " group by itm.username,itm.icaId ";
				 */

				/*
				 * String sql =
				 * " select i.icaName,i.id as icaId,(case when itm.icaScaledMarks is not null then itm.icaScaledMarks "
				 * + " else itm.icaTotalMarks  end) as icaTotalMarks, " +
				 * " i.acadYear,i.acadSession,concat(u.firstName,' ',u.lastName) as studentName,u.rollNo,u.email, p.programName, "
				 * +
				 * " concat(c.moduleName,'(',c.moduleAbbr,')') as moduleName,itm.`query`,iq.isApproved,DATE_FORMAT(iq.createdDate, \"%M %e %Y\") as raiseQDate, "
				 * +
				 * " itm.username,c1.courseName as division, GROUP_CONCAT(DISTINCT concat(u1.firstName,' ',u1.lastName,'(',u1.username,')')) "
				 * + " as assignedFaculty " +
				 * " from program p,ica_total_marks itm,users u,course c,users u1, ica i  " +
				 * " left outer join course c1 on c1.id=i.eventId " +
				 * " inner join ica_queries iq on iq.icaId=i.id " +
				 * " where itm.icaId=i.id and itm.username=u.username and FIND_IN_SET(u1.username,i.assignedFaculty) "
				 * +
				 * " and i.moduleId=c.moduleId and i.acadYear=c.acadYearCode and i.acadSession=c.acadSession "
				 * + " and u.programId=p.id " + " and itm.isQueryRaise= 'Y'  " +
				 * " and (FIND_IN_SET(?,i.assignedFaculty)) and i.acadYear = ? AND i.active = 'Y' "
				 * + " group by itm.username,itm.icaId ";
				 */

				String sql = " select i.icaName,i.id as icaId,(case when itm.icaScaledMarks is not null then itm.icaScaledMarks "
						+ " else itm.icaTotalMarks  end) as icaTotalMarks,  (case when i.isCourseraIca='Y' then u.acadSession ELSE i.acadSession END) AS acadSession ,"
						+ " i.acadYear,concat(u.firstName,' ',u.lastName) as studentName,u.rollNo,u.email, p.programName, "
						+ " concat(c.moduleName,'(',c.moduleAbbr,')') as moduleName,itm.`query`,iq.isApproved,DATE_FORMAT(iq.createdDate, \"%M %e %Y\") as raiseQDate, "
						+ " itm.username,c1.courseName as division, GROUP_CONCAT(DISTINCT concat(u1.firstName,' ',u1.lastName,'(',u1.username,')')) "
						+ " as assignedFaculty "
						+ " from program p,ica_total_marks itm,users u,course c,users u1, ica i  "
						+ " left outer join course c1 on c1.id=i.eventId "
						+ " inner join ica_queries iq on iq.icaId=i.id "
						+ " where itm.icaId=i.id and itm.username=u.username and FIND_IN_SET(u1.username,i.assignedFaculty) "
						+ " and i.moduleId=c.moduleId and i.acadYear=c.acadYearCode  " + " and u.programId=p.id "
						+ " and itm.isQueryRaise= 'Y' and (itm.flagTcs <> 'S' or itm.flagTcs is NULL)  "
						+ " and (FIND_IN_SET(?,i.assignedFaculty)) and i.acadYear = ? AND i.active = 'Y' "
						+ " group by itm.username,itm.icaId ";

				return findAllSQL(sql, new Object[] { username, acadYear });

			} else {
				return null;
			}
		} else {

			if (auth.contains(Role.ROLE_ADMIN)) {
				/*
				 * String sql =
				 * " select i.icaName,i.id as icaId,(case when itm.icaScaledMarks is not null then itm.icaScaledMarks  "
				 * + " else itm.icaTotalMarks  end) as icaTotalMarks, " +
				 * " i.acadYear,i.acadSession,concat(u.firstName,' ',u.lastName) as studentName,p.programName, "
				 * + " concat(c.moduleName,'(',c.moduleAbbr,')') as moduleName,itm.`query`," +
				 * " itm.username,c1.courseName as division,concat(u1.firstName,' ',u1.lastName,'(',u1.username,')')"
				 * + " as assignedFaculty " +
				 * " from program p,ica_total_marks itm,users u,course c,users u1, ica i  " +
				 * " left outer join course c1 on c1.id=i.eventId " +
				 * " where itm.icaId=i.id and itm.username=u.username and u1.username=i.assignedFaculty "
				 * +
				 * " and i.moduleId=c.moduleId and i.acadYear=c.acadYear and i.acadSession=c.acadSession "
				 * + " and u.programId=p.id " + " and itm.isQueryRaise= 'Y'  " +
				 * " and (i.createdBy=? or i.lastModifiedBy= ?) AND i.active = 'Y' " +
				 * " group by itm.username,itm.icaId ";
				 */
				/*
				 * String sql =
				 * " select i.icaName,i.id as icaId,(case when itm.icaScaledMarks is not null then itm.icaScaledMarks "
				 * + " else itm.icaTotalMarks  end) as icaTotalMarks, " +
				 * " i.acadYear,i.acadSession,concat(u.firstName,' ',u.lastName) as studentName,u.rollNo,u.email, p.programName, "
				 * +
				 * " concat(c.moduleName,'(',c.moduleAbbr,')') as moduleName,itm.`query`,iq.isApproved,DATE_FORMAT(iq.createdDate, \"%M %e %Y\") as raiseQDate, "
				 * +
				 * " itm.username,c1.courseName as division, GROUP_CONCAT(DISTINCT concat(u1.firstName,' ',u1.lastName,'(',u1.username,')')) "
				 * + " as assignedFaculty " +
				 * " from program p,ica_total_marks itm,users u,course c,users u1, ica i  " +
				 * " left outer join course c1 on c1.id=i.eventId " +
				 * " inner join ica_queries iq on iq.icaId=i.id " +
				 * " where itm.icaId=i.id and itm.username=u.username and FIND_IN_SET(u1.username,i.assignedFaculty) "
				 * +
				 * " and i.moduleId=c.moduleId and i.acadYear=c.acadYearCode and i.acadSession=c.acadSession "
				 * + " and u.programId=p.id " + " and itm.isQueryRaise= 'Y'  " +
				 * " and (i.createdBy=? or i.lastModifiedBy= ?)  AND i.active = 'Y' " +
				 * " group by itm.username,itm.icaId ";
				 */

				String sql = " select i.icaName,i.id as icaId,(case when itm.icaScaledMarks is not null then itm.icaScaledMarks "
						+ " else itm.icaTotalMarks  end) as icaTotalMarks, (case when i.isCourseraIca='Y' then u.acadSession ELSE i.acadSession END) AS acadSession ,"
						+ " i.acadYear,concat(u.firstName,' ',u.lastName) as studentName,u.rollNo,u.email, p.programName, "
						+ " concat(c.moduleName,'(',c.moduleAbbr,')') as moduleName,itm.`query`,iq.isApproved,DATE_FORMAT(iq.createdDate, \"%M %e %Y\") as raiseQDate, "
						+ " itm.username,c1.courseName as division, GROUP_CONCAT(DISTINCT concat(u1.firstName,' ',u1.lastName,'(',u1.username,')')) "
						+ " as assignedFaculty "
						+ " from program p,ica_total_marks itm,users u,course c,users u1, ica i  "
						+ " left outer join course c1 on c1.id=i.eventId "
						+ " inner join ica_queries iq on iq.icaId=i.id "
						+ " where itm.icaId=i.id and itm.username=u.username and FIND_IN_SET(u1.username,i.assignedFaculty) "
						+ " and i.moduleId=c.moduleId and i.acadYear=c.acadYearCode  " + " and u.programId=p.id "
						+ " and itm.isQueryRaise= 'Y' and (itm.flagTcs <> 'S' or itm.flagTcs is NULL)  "
						+ " and (i.createdBy=? or i.lastModifiedBy= ?)  AND i.active = 'Y' "
						+ " group by itm.username,itm.icaId ";

				return findAllSQL(sql, new Object[] { username, username });
			}

			else if (auth.contains(Role.ROLE_FACULTY)) {
				/*
				 * String sql =
				 * " select i.icaName,i.id as icaId,(case when itm.icaScaledMarks is not null then itm.icaScaledMarks  "
				 * + " else itm.icaTotalMarks  end) as icaTotalMarks, " +
				 * " i.acadYear,i.acadSession,concat(u.firstName,' ',u.lastName) as studentName,p.programName, "
				 * + " concat(c.moduleName,'(',c.moduleAbbr,')') as moduleName,itm.`query`," +
				 * " itm.username,c1.courseName as division,concat(u1.firstName,' ',u1.lastName,'(',u1.username,')')"
				 * + " as assignedFaculty " +
				 * " from program p,ica_total_marks itm,users u,course c,users u1, ica i  " +
				 * " left outer join course c1 on c1.id=i.eventId " +
				 * " where itm.icaId=i.id and itm.username=u.username and u1.username=i.assignedFaculty "
				 * +
				 * " and i.moduleId=c.moduleId and i.acadYear=c.acadYear and i.acadSession=c.acadSession "
				 * + " and u.programId=p.id " + " and itm.isQueryRaise= 'Y'  " +
				 * " and (i.assignedFaculty=?) AND i.active = 'Y' " +
				 * " group by itm.username,itm.icaId ";
				 */

				String sql = " select i.icaName,i.id as icaId,(case when itm.icaScaledMarks is not null then itm.icaScaledMarks "
						+ " else itm.icaTotalMarks  end) as icaTotalMarks, "
						+ " i.acadYear,i.acadSession,concat(u.firstName,' ',u.lastName) as studentName,u.rollNo,u.email, p.programName, "
						+ " concat(c.moduleName,'(',c.moduleAbbr,')') as moduleName,itm.`query`,iq.isApproved,DATE_FORMAT(iq.createdDate, \"%M %e %Y\") as raiseQDate, "
						+ " itm.username,c1.courseName as division, GROUP_CONCAT(DISTINCT concat(u1.firstName,' ',u1.lastName,'(',u1.username,')')) "
						+ " as assignedFaculty "
						+ " from program p,ica_total_marks itm,users u,course c,users u1, ica i  "
						+ " left outer join course c1 on c1.id=i.eventId "
						+ " inner join ica_queries iq on iq.icaId=i.id "
						+ " where itm.icaId=i.id and itm.username=u.username and FIND_IN_SET(u1.username,i.assignedFaculty) "
						+ " and i.moduleId=c.moduleId and i.acadYear=c.acadYearCode and i.acadSession=c.acadSession "
						+ " and u.programId=p.id " + " and itm.isQueryRaise= 'Y' and (itm.flagTcs <> 'S' or itm.flagTcs is NULL)  "
						+ " and (FIND_IN_SET(?,i.assignedFaculty))  AND i.active = 'Y' "
						+ " group by itm.username,itm.icaId ";

				return findAllSQL(sql, new Object[] { username });

			} else {
				return null;
			}
		}

	}

	/*
	 * public List<IcaTotalMarks> getIcaTotalMarksByParamForFaculty(String acadYear,
	 * String username) { String
	 * sql="select concat(u.firstName,' ',u.lastName) as studentName,u.rollNo,icm.`*`,i.publishedDate, "
	 * +
	 * "(case when icm.icaScaledMarks is not null then i.scaledMarks  else i.internalMarks  end) "
	 * +
	 * " as internalMarks, (case when icm.icaScaledMarks is not null then CEIL(icm.icaScaledMarks)  "
	 * + "else CEIL(icm.icaTotalMarks)  end) as icaTotalMarks, " +
	 * "(case when icm.icaScaledMarks is not null then( case when CEIL(icm.icaScaledMarks) >= CONVERT(i.internalPassMarks,decimal) then 'PASS' else 'FAIL' end) "
	 * +
	 * "else (case when CEIL(icm.icaTotalMarks) >= CONVERT(i.internalPassMarks,decimal) then 'PASS' else 'FAIL' end) "
	 * + "end) as passFailStatus,  " +
	 * "c.moduleName,c.acadYearCode,c.acadSession,c.moduleId,c.moduleAbbr from ica i,ica_total_marks icm,course c,users u "
	 * + "where i.id=icm.icaId  and i.active = 'Y' and i.moduleId=c.moduleId and  "
	 * 
	 * + "i.acadYear=? and i.assignedFaculty=? and u.username=icm.username " +
	 * "AND i.acadYear=c.acadYear  "
	 * 
	 * + "and i.moduleId=c.moduleId and c.active = 'Y' " +
	 * "AND icm.active= 'Y' group by i.id,icm.username ";
	 * 
	 * return findAllSQL(sql, new Object[] { acadYear ,username }); }
	 */

	public List<IcaTotalMarks> getIcaTotalMarksByParamForFaculty(String acadYear, String username) {
		String sql = "select concat(u.firstName,' ',u.lastName) as studentName,u.rollNo,icm.*,i.publishedDate, "
				+ "(case when icm.icaScaledMarks is not null then i.scaledMarks  else i.internalMarks  end) "
				+ " as internalMarks, (case when icm.icaScaledMarks is not null then CEIL(icm.icaScaledMarks)  "
				+ "else CEIL(icm.icaTotalMarks)  end) as icaTotalMarks, "
				+ "(case when icm.icaScaledMarks is not null then( case when CEIL(icm.icaScaledMarks) >= CONVERT(i.internalPassMarks,decimal) then 'PASS' else 'FAIL' end) "
				+ "else (case when CEIL(icm.icaTotalMarks) >= CONVERT(i.internalPassMarks,decimal) then 'PASS' else 'FAIL' end) "
				+ "end) as passFailStatus,  "
				+ "c.moduleName,c.acadYearCode,c.acadSession,c.moduleId,c.moduleAbbr from ica i,ica_total_marks icm,course c,users u "
				+ "where i.id=icm.icaId  and i.active = 'Y' and i.moduleId=c.moduleId and  "

				+ "i.acadYear=? and FIND_IN_SET(?,i.assignedFaculty) and u.username=icm.username "
				+ "AND i.acadYear=c.acadYearCode  "

				+ "and i.moduleId=c.moduleId and c.active = 'Y' " + "AND icm.active= 'Y' group by i.id,icm.username ";

		return findAllSQL(sql, new Object[] { acadYear, username });
	}

	public List<IcaTotalMarks> getIcaTotalMarksByParamDraft(String acadYear, String acadSession, String programId,
			String campusId) {

		String programIdL = "%" + programId + "%";
		if (campusId != null) {

			/*
			 * String sql =
			 * " select concat(u.firstName,' ',u.lastName) as studentName,u.rollNo,icm.`*`,i.publishedDate, "
			 * +
			 * " (case when icm.icaScaledMarks is not null then i.scaledMarks  else i.internalMarks  end) "
			 * +
			 * "  as internalMarks, (case when icm.icaScaledMarks is not null then CEIL(icm.icaScaledMarks)   "
			 * + " else CEIL(icm.icaTotalMarks)  end) " + "   as icaTotalMarks, "
			 * 
			 * +
			 * "	 (case when icm.icaScaledMarks is not null then( case when CEIL(icm.icaScaledMarks) >= CONVERT(i.internalPassMarks,decimal) then 'PASS' else 'FAIL' end) "
			 * +
			 * "		else (case when CEIL(icm.icaTotalMarks) >= CONVERT(i.internalPassMarks,decimal) then 'PASS' else 'FAIL' end) "
			 * + "	 end) as passFailStatus,  " +
			 * "	 c.moduleName,c.acadYear,c.acadSession,c.moduleId,c.moduleAbbr from ica i,ica_total_marks icm,course c,users u "
			 * +
			 * "		where i.id=icm.icaId  and i.active = 'Y' and i.moduleId=c.moduleId and i.programId like ? "
			 * + "		and i.acadSession=?   and i.acadYear=? and i.campusId=?  " +
			 * "     and icm.saveAsDraft = 'Y' and u.username=icm.username " +
			 * "	  and c.programId=? AND i.acadYear=c.acadYear and i.acadSession=c.acadSession  "
			 * +
			 * "	  and i.moduleId=c.moduleId and i.campusId=c.campusId and c.active = 'Y' "
			 * + "	 AND icm.active= 'Y' group by i.id,icm.username ";
			 */

			// in place to course.AcadAcadYear to acadYearCode
			String sql = " select concat(u.firstName,' ',u.lastName) as studentName,u.rollNo,icm.*,i.publishedDate, "
					+ " (case when icm.icaScaledMarks is not null then i.scaledMarks  else i.internalMarks  end) "
					+ "  as internalMarks, (case when icm.icaScaledMarks is not null then CEIL(icm.icaScaledMarks)   "
					+ " else CEIL(icm.icaTotalMarks)  end) " + "   as icaTotalMarks, "

					+ "	 (case when icm.icaScaledMarks is not null then( case when CEIL(icm.icaScaledMarks) >= CONVERT(i.internalPassMarks,decimal) then 'PASS' else 'FAIL' end) "
					+ "		else (case when CEIL(icm.icaTotalMarks) >= CONVERT(i.internalPassMarks,decimal) then 'PASS' else 'FAIL' end) "
					+ "	 end) as passFailStatus,  "
					+ "	 c.moduleName,c.acadYearCode,c.acadSession,c.moduleId,c.moduleAbbr from ica i,ica_total_marks icm,course c,users u "
					+ "		where i.id=icm.icaId  and i.active = 'Y' and i.moduleId=c.moduleId and i.programId like ? "
					+ "		and i.acadSession=?   and i.acadYear=? and i.campusId=?  "
					+ "     and icm.saveAsDraft = 'Y' and u.username=icm.username "
					+ "	  and c.programId=? AND i.acadYear=c.acadYearCode and i.acadSession=c.acadSession  "
					+ "	  and i.moduleId=c.moduleId and i.campusId=c.campusId and c.active = 'Y' "
					+ "	 AND icm.active= 'Y' group by i.id,icm.username ";

			return findAllSQL(sql, new Object[] { programIdL, acadSession, acadYear, campusId, programId });
		} else {

			String sql = " select concat(u.firstName,' ',u.lastName) as studentName,u.rollNo,icm.*,i.publishedDate, "
					+ " (case when icm.icaScaledMarks is not null then i.scaledMarks  else i.internalMarks  end) "
					+ "  as internalMarks, (case when icm.icaScaledMarks is not null then CEIL(icm.icaScaledMarks)  "
					+ " else CEIL(icm.icaTotalMarks)  end) " + "   as icaTotalMarks, "

					+ "	 (case when icm.icaScaledMarks is not null then( case when CEIL(icm.icaScaledMarks) >= CONVERT(i.internalPassMarks,decimal) then 'PASS' else 'FAIL' end) "
					+ "		else (case when CEIL(icm.icaTotalMarks) >= CONVERT(i.internalPassMarks,decimal) then 'PASS' else 'FAIL' end) "
					+ "	 end) as passFailStatus,  "
					+ "	 c.moduleName,c.acadYearCode,c.acadSession,c.moduleId,c.moduleAbbr from ica i,ica_total_marks icm,course c,users u "
					+ "		where i.id=icm.icaId  and i.active = 'Y' and i.moduleId=c.moduleId and i.programId like ? "
					+ "		and i.acadSession=?   and i.acadYear=?  and icm.saveAsDraft = 'Y' and u.username=icm.username "
					+ "	  and c.programId=? AND i.acadYear=c.acadYearCode and i.acadSession=c.acadSession  "
					+ "	  and i.moduleId=c.moduleId and c.active = 'Y' "
					+ "	 AND icm.active= 'Y' group by i.id,icm.username ";

			return findAllSQL(sql, new Object[] { programIdL, acadSession, acadYear, programId });
		}
	}

	public List<IcaTotalMarks> getIsReevalIcaUsername(String icaId) {

		String sql = "select itm.username,itm.isQueryApproved,itm.remarks from ica_total_marks itm , ica_queries iq where itm.icaId=iq.icaId "
				+ "and itm.icaId=? and itm.isQueryRaise='Y' and iq.isApproved='Y' and (iq.isReEvaluated <> 'Y' or iq.isReEvaluated is null)";
		return findAllSQL(sql, new Object[] { icaId });
	}
	
	

	public List<String> getDistinctUsernamesByActiveIcaId(String icaId) {
		String sql = " select distinct username from " + getTableName() + " where active = 'Y' and icaId = ? ";
		return getJdbcTemplate().queryForList(sql, String.class, new Object[] { icaId });
	}

	public void updateSaveAsDraftOrFinalSubmit(String flag, String icaId, String value) {
		if (flag.equalsIgnoreCase("DRAFT")) {
			String sql = "update " + getTableName() + " set saveAsDraft = ? where icaId = ? and finalSubmit is null";
			executeUpdateSql(sql, new Object[] { value, icaId });
		} else {
			if (value.equalsIgnoreCase("Y")) {
				String sql = "update " + getTableName() + " set finalSubmit = ?, saveAsDraft = NULL where icaId = ?";
				executeUpdateSql(sql, new Object[] { value, icaId });
			} else {
				String sql = "update " + getTableName() + " set finalSubmit = ? where icaId = ?";
				executeUpdateSql(sql, new Object[] { value, icaId });
			}
		}
	}

	public List<String> getFinalSubmitByIcaIdAndUserList(String icaId, List<String> usernames) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("icaId", icaId);
		params.put("usernames", usernames);

		String sql = " select distinct finalSubmit from  " + getTableName()
				+ " where active = 'Y' and icaId = :icaId and username in (:usernames) ";

		return getNamedParameterJdbcTemplate().queryForList(sql, params, String.class);

	}

	public void updateFinalSubmitByUserList(List<String> usernames, String icaId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("icaId", icaId);
		params.put("usernames", usernames);

		String sql = "update " + getTableName() + " set finalSubmit = 'Y', saveAsDraft = NULL where icaId = :icaId "
				+ " and username in (:usernames) ";
		getNamedParameterJdbcTemplate().update(sql, params);
	}

	public List<IcaTotalMarks> getIcaTotalMarksByIcaBatchWise(String icaId, String username) {

		String sql = " select concat(u.firstName,' ',u.lastName) as studentName,u.rollNo, "
				+ " icm.*,i.publishedDate,(case when icm.icaScaledMarks is not null then i.scaledMarks  "
				+ " else i.internalMarks  end) "
				+ "  as internalMarks, (case when icm.icaScaledMarks is not null then CEIL(icm.icaScaledMarks)  "
				+ " else CEIL(icm.icaTotalMarks)  end) " + " as icaTotalMarks, "

				+ " (case when icm.icaScaledMarks is not null then( "
				+ " case when CEIL(icm.icaScaledMarks) >= CONVERT(i.internalPassMarks,decimal) then 'PASS' else 'FAIL' end) "
				+ "          else (case when CEIL(icm.icaTotalMarks) >= CONVERT(i.internalPassMarks,decimal) then 'PASS' else 'FAIL' end) "
				+ " end) as passFailStatus,  "
				+ " c.moduleName,i.acadYear,i.acadSession,i.moduleId from ica i,ica_total_marks icm,course c,users u, ica_student_batchwise isb "
				+ "          where i.id=icm.icaId and icm.icaId = ? and i.active = 'Y' and i.moduleId=c.moduleId and u.username=icm.username and isb.icaId = i.id "
				+ " and isb.username = icm.username and isb.facultyId = ? "
				+ "          and icm.active= 'Y' group by i.id,icm.username ";

		return findAllSQL(sql, new Object[] { icaId, username });
	}

	/*public List<IcaTotalMarks> getIcaTotalMarksByUserCoursera(String username) {

		String sql = " select icm.`*`,i.publishedDate,(case when icm.icaScaledMarks is not null then i.scaledMarks  else i.internalMarks  end)"
				+ "  as internalMarks, (case when icm.icaScaledMarks is not null then CEIL(icm.icaScaledMarks)  else CEIL(icm.icaTotalMarks)  end) "
				+ " as icaTotalMarks, "

				+ " (case when icm.icaScaledMarks is not null then( "
				+ "case when CEIL(icm.icaScaledMarks) >= CONVERT(i.internalPassMarks,decimal) then 'PASS' else 'FAIL' end) "
				+ "	else (case when CEIL(icm.icaTotalMarks) >= CONVERT(i.internalPassMarks,decimal) then 'PASS' else 'FAIL' end) "
				+ " end) as passFailStatus,  "
				+ "c.moduleName,i.acadYear,(case when i.isCourseraIca = 'Y' then c.acadSession "
				+ " else i.acadSession end) as acadSession,i.moduleId from ica i,ica_total_marks icm,course c "
				+ "	where i.id=icm.icaId and icm.username = ? and i.active = 'Y' and i.moduleId=c.moduleId and icm.finalSubmit = 'Y' "
				+ "	AND icm.active= 'Y' and i.isPublished = 'Y' and (i.isNonEventModule <> 'Y'  OR i.isNonEventModule is null) group by i.id ";

		return findAllSQL(sql, new Object[] { username });
	}*/
	
	public List<IcaTotalMarks> getIcaTotalMarksByUserCoursera(String username) {

		String sql = " select icm.*,i.publishedDate,(case when icm.icaScaledMarks is not null then i.scaledMarks  else i.internalMarks  end)"
				+ "  as internalMarks, (case when icm.icaScaledMarks is not null then CEIL(icm.icaScaledMarks)  else CEIL(icm.icaTotalMarks)  end) "
				+ " as icaTotalMarks, "

				+ " (case when icm.icaScaledMarks is not null then( "
				+ "case when CEIL(icm.icaScaledMarks) >= CONVERT(i.internalPassMarks,decimal) then 'PASS' else 'FAIL' end) "
				+ " else (case when CEIL(icm.icaTotalMarks) >= CONVERT(i.internalPassMarks,decimal) then 'PASS' else 'FAIL' end) "
				+ " end) as passFailStatus,  "
				+ "c.moduleName,i.acadYear,(case when i.isCourseraIca = 'Y' then c.acadSession "
				+ " else i.acadSession end) as acadSession,i.moduleId from ica i,ica_total_marks icm,course c,user_course uc "
				+ " where i.id=icm.icaId and icm.username = ? and i.active = 'Y' and i.moduleId=c.moduleId "
				+ " and uc.courseId=c.id and uc.username=icm.username and icm.finalSubmit = 'Y' "
				+ " AND icm.active= 'Y' and i.isPublished = 'Y' "
				+ " and (i.isNonEventModule <> 'Y'  OR i.isNonEventModule is null) group by i.id ";

		return findAllSQL(sql, new Object[] { username });
	}

	public List<IcaTotalMarks> getNonEventRaiseQueries(String acadYear, Collection<GrantedAuthority> auth,
			String username) {
		if (auth.contains(Role.ROLE_ADMIN)) {
			if (null != acadYear) {
				String sql = "select i.icaName,i.id as icaId,(case when itm.icaScaledMarks is not null then itm.icaScaledMarks "
						+ " else itm.icaTotalMarks  end) as icaTotalMarks, i.acadSession AS acadSession ,"
						+ " i.acadYear,i.acadSession,concat(u.firstName,' ',u.lastName) as studentName,u.rollNo,u.email, p.programName, "
						+ " concat(m.module_description,'(',m.module_abbr,')') as moduleName,itm.`query`,iq.isApproved,iq.createdDate as raiseQDate, "
						+ " itm.username " + " from program p,ica_total_marks itm,users u,module m, ica i "
						+ " inner join ica_queries iq on iq.icaId=i.id "
						+ " where itm.icaId=i.id and itm.username=u.username"
						+ " and i.moduleId=m.module_id and i.acadYear=m.acadyear " + " and u.programId=p.id "
						+ " and itm.isQueryRaise= 'Y'  "
						+ " and (i.createdBy=? or i.lastModifiedBy= ?) and i.acadYear = ? AND i.active = 'Y' and i.isNonEventModule = 'Y' "
						+ " group by itm.username,itm.icaId";
				return findAllSQL(sql, new Object[] { username, username, acadYear });
			} else {
				String sql = "select i.icaName,i.id as icaId,(case when itm.icaScaledMarks is not null then itm.icaScaledMarks "
						+ " else itm.icaTotalMarks  end) as icaTotalMarks, i.acadSession AS acadSession ,"
						+ " i.acadYear,i.acadSession,concat(u.firstName,' ',u.lastName) as studentName,u.rollNo,u.email, p.programName, "
						+ " concat(m.module_description,'(',m.module_abbr,')') as moduleName,itm.`query`,iq.isApproved,iq.createdDate as raiseQDate, "
						+ " itm.username " + " from program p,ica_total_marks itm,users u,module m, ica i "
						+ " inner join ica_queries iq on iq.icaId=i.id "
						+ " where itm.icaId=i.id and itm.username=u.username"
						+ " and i.moduleId=m.module_id and i.acadYear=m.acadyear " + " and u.programId=p.id "
						+ " and itm.isQueryRaise= 'Y'  "
						+ " and (i.createdBy=? or i.lastModifiedBy= ?) AND i.active = 'Y' and i.isNonEventModule = 'Y' "
						+ " group by itm.username,itm.icaId";
				return findAllSQL(sql, new Object[] { username, username });
			}
		} else {
			return new ArrayList<>();
		}

	}

	public int deleteIcaTotalMarksByStudents(String icaId, List<String> studList) {

		Map<String, Object> mapper = new HashMap<>();

		mapper.put("icaId", icaId);
		mapper.put("studList", studList);

		String sql = " delete from " + getTableName() + " where icaId =:icaId and username in(:studList) ";
		return getNamedParameterJdbcTemplate().update(sql, mapper);
	}
	
	
	public int getNoOfStudentsForIca(Long icaId) {
		String sql =" select count(*) from "+getTableName()+" where icaId = ? and active ='Y' ";
		return getJdbcTemplate().queryForObject(sql, Integer.class, new Object[] {icaId});
	}
	public int getCountOfTcsFlagSentForIca(Long icaId) {
		String sql =" select count(*) from "+getTableName()+" where icaId = ? and active ='Y' and flagTcs='S' ";
		return getJdbcTemplate().queryForObject(sql, Integer.class, new Object[] {icaId});
	}
	
	
	
	//new query
	
	
	public List<IcaTotalMarks> getComponentQueries(String acadYear, Collection<GrantedAuthority> auth, String username) {

		if (acadYear != null) {
			if (auth.contains(Role.ROLE_ADMIN)) {

				/*
				 * String sql =
				 * " select i.icaName,i.id as icaId,(case when itm.icaScaledMarks is not null then itm.icaScaledMarks  "
				 * + " else itm.icaTotalMarks  end) as icaTotalMarks, " +
				 * " i.acadYear,i.acadSession,concat(u.firstName,' ',u.lastName) as studentName,p.programName, "
				 * + " concat(c.moduleName,'(',c.moduleAbbr,')') as moduleName,itm.`query`," +
				 * " itm.username,c1.courseName as division,concat(u1.firstName,' ',u1.lastName,'(',u1.username,')')"
				 * + " as assignedFaculty " +
				 * " from program p,ica_total_marks itm,users u,course c,users u1, ica i  " +
				 * " left outer join course c1 on c1.id=i.eventId " +
				 * " where itm.icaId=i.id and itm.username=u.username and u1.username=i.assignedFaculty "
				 * +
				 * " and i.moduleId=c.moduleId and i.acadYear=c.acadYear and i.acadSession=c.acadSession "
				 * + " and u.programId=p.id " + " and itm.isQueryRaise= 'Y'  " +
				 * " and (i.createdBy=? or i.lastModifiedBy= ?) and i.acadYear=? AND i.active = 'Y' "
				 * + " group by itm.username,itm.icaId ";
				 */

				/*
				 * String sql =
				 * " select i.icaName,i.id as icaId,(case when itm.icaScaledMarks is not null then itm.icaScaledMarks "
				 * + " else itm.icaTotalMarks  end) as icaTotalMarks, " +
				 * " i.acadYear,i.acadSession,concat(u.firstName,' ',u.lastName) as studentName,u.rollNo,u.email, p.programName, "
				 * +
				 * " concat(c.moduleName,'(',c.moduleAbbr,')') as moduleName,itm.`query`,iq.isApproved,DATE_FORMAT(iq.createdDate, \"%M %e %Y\") as raiseQDate, "
				 * +
				 * " itm.username,c1.courseName as division, GROUP_CONCAT(DISTINCT concat(u1.firstName,' ',u1.lastName,'(',u1.username,')')) "
				 * + " as assignedFaculty " +
				 * " from program p,ica_total_marks itm,users u,course c,users u1, ica i  " +
				 * " left outer join course c1 on c1.id=i.eventId " +
				 * " inner join ica_queries iq on iq.icaId=i.id " +
				 * " where itm.icaId=i.id and itm.username=u.username and FIND_IN_SET(u1.username,i.assignedFaculty) "
				 * +
				 * " and i.moduleId=c.moduleId and i.acadYear=c.acadYearCode and i.acadSession=c.acadSession "
				 * + " and u.programId=p.id " + " and itm.isQueryRaise= 'Y'  " +
				 * " and (i.createdBy=? or i.lastModifiedBy= ?) and i.acadYear = ? AND i.active = 'Y' "
				 * + " group by itm.username,itm.icaId ";
				 */

				String sql = " select i.icaName,i.id as icaId,itm.componentId as compId,itm.marks "
						+ "  as componentMarks, (case when i.isCourseraIca='Y' then u.acadSession else i.acadSession end) AS acadSession ,"
						+ " i.acadYear,i.acadSession,concat(u.firstName,' ',u.lastName) as studentName,u.rollNo,u.email, p.programName, "
						+ " concat(c.moduleName,'(',c.moduleAbbr,')') as moduleName,itm.`query`,iq.isApproved,iq.createdDate as raiseQDate, "
						+ " itm.username,c1.courseName as division, GROUP_CONCAT(DISTINCT concat(u1.firstName,' ',u1.lastName,'(',u1.username,')')) "
						+ " as assignedFaculty "
						+ " from program p,ica_component_marks itm,users u,course c,users u1, ica i  "
						+ " left outer join course c1 on c1.id=i.eventId "
						+ " inner join ica_queries iq on iq.icaId=i.id "
						+ " where itm.icaId=i.id and itm.username=u.username and FIND_IN_SET(u1.username,i.assignedFaculty) "
						+ " and i.moduleId=c.moduleId and i.acadYear=c.acadYearCode " + " and u.programId=p.id "
						+ " and itm.isQueryRaise= 'Y' "
						+ " and (i.createdBy=? or i.lastModifiedBy= ?) and i.acadYear = ? AND i.active = 'Y' "
						+ " group by itm.username,itm.icaId ";

				return findAllSQL(sql, new Object[] { username, username, acadYear });

			} else if (auth.contains(Role.ROLE_FACULTY)) {
				/*
				 * String sql =
				 * " select i.icaName,i.id as icaId,(case when itm.icaScaledMarks is not null then itm.icaScaledMarks  "
				 * + " else itm.icaTotalMarks  end) as icaTotalMarks, " +
				 * " i.acadYear,i.acadSession,concat(u.firstName,' ',u.lastName) as studentName,p.programName, "
				 * + " concat(c.moduleName,'(',c.moduleAbbr,')') as moduleName,itm.`query`," +
				 * " itm.username,c1.courseName as division,concat(u1.firstName,' ',u1.lastName,'(',u1.username,')')"
				 * + " as assignedFaculty " +
				 * " from program p,ica_total_marks itm,users u,course c,users u1, ica i  " +
				 * " left outer join course c1 on c1.id=i.eventId " +
				 * " where itm.icaId=i.id and itm.username=u.username and u1.username=i.assignedFaculty "
				 * +
				 * " and i.moduleId=c.moduleId and i.acadYear=c.acadYear and i.acadSession=c.acadSession "
				 * + " and u.programId=p.id " + " and itm.isQueryRaise= 'Y'  " +
				 * " and (i.assignedFaculty=?) and i.acadYear=? AND i.active = 'Y' " +
				 * " group by itm.username,itm.icaId ";
				 */

				/*
				 * String sql =
				 * " select i.icaName,i.id as icaId,(case when itm.icaScaledMarks is not null then itm.icaScaledMarks "
				 * + " else itm.icaTotalMarks  end) as icaTotalMarks, " +
				 * " i.acadYear,i.acadSession,concat(u.firstName,' ',u.lastName) as studentName,u.rollNo,u.email, p.programName, "
				 * +
				 * " concat(c.moduleName,'(',c.moduleAbbr,')') as moduleName,itm.`query`,iq.isApproved,DATE_FORMAT(iq.createdDate, \"%M %e %Y\") as raiseQDate, "
				 * +
				 * " itm.username,c1.courseName as division, GROUP_CONCAT(DISTINCT concat(u1.firstName,' ',u1.lastName,'(',u1.username,')')) "
				 * + " as assignedFaculty " +
				 * " from program p,ica_total_marks itm,users u,course c,users u1, ica i  " +
				 * " left outer join course c1 on c1.id=i.eventId " +
				 * " inner join ica_queries iq on iq.icaId=i.id " +
				 * " where itm.icaId=i.id and itm.username=u.username and FIND_IN_SET(u1.username,i.assignedFaculty) "
				 * +
				 * " and i.moduleId=c.moduleId and i.acadYear=c.acadYearCode and i.acadSession=c.acadSession "
				 * + " and u.programId=p.id " + " and itm.isQueryRaise= 'Y'  " +
				 * " and (FIND_IN_SET(?,i.assignedFaculty)) and i.acadYear = ? AND i.active = 'Y' "
				 * + " group by itm.username,itm.icaId ";
				 */

				String sql = " select i.icaName,i.id as icaId,itm.componentId as compId,itm.marks "
						+ "  as componentMarks,  (case when i.isCourseraIca='Y' then u.acadSession ELSE i.acadSession END) AS acadSession ,"
						+ " i.acadYear,concat(u.firstName,' ',u.lastName) as studentName,u.rollNo,u.email, p.programName, "
						+ " concat(c.moduleName,'(',c.moduleAbbr,')') as moduleName,itm.`query`,iq.isApproved,DATE_FORMAT(iq.createdDate, \"%M %e %Y\") as raiseQDate, "
						+ " itm.username,c1.courseName as division, GROUP_CONCAT(DISTINCT concat(u1.firstName,' ',u1.lastName,'(',u1.username,')')) "
						+ " as assignedFaculty "
						+ " from program p,ica_component_marks itm,users u,course c,users u1, ica i  "
						+ " left outer join course c1 on c1.id=i.eventId "
						+ " inner join ica_queries iq on iq.icaId=i.id "
						+ " where itm.icaId=i.id and itm.username=u.username and FIND_IN_SET(u1.username,i.assignedFaculty) "
						+ " and i.moduleId=c.moduleId and i.acadYear=c.acadYearCode  " + " and u.programId=p.id "
						+ " and itm.isQueryRaise= 'Y'  "
						+ " and (FIND_IN_SET(?,i.assignedFaculty)) and i.acadYear = ? AND i.active = 'Y' "
						+ " group by itm.username,itm.icaId ";

				return findAllSQL(sql, new Object[] { username, acadYear });

			} else {
				return null;
			}
		} else {

			if (auth.contains(Role.ROLE_ADMIN)) {
				/*
				 * String sql =
				 * " select i.icaName,i.id as icaId,(case when itm.icaScaledMarks is not null then itm.icaScaledMarks  "
				 * + " else itm.icaTotalMarks  end) as icaTotalMarks, " +
				 * " i.acadYear,i.acadSession,concat(u.firstName,' ',u.lastName) as studentName,p.programName, "
				 * + " concat(c.moduleName,'(',c.moduleAbbr,')') as moduleName,itm.`query`," +
				 * " itm.username,c1.courseName as division,concat(u1.firstName,' ',u1.lastName,'(',u1.username,')')"
				 * + " as assignedFaculty " +
				 * " from program p,ica_total_marks itm,users u,course c,users u1, ica i  " +
				 * " left outer join course c1 on c1.id=i.eventId " +
				 * " where itm.icaId=i.id and itm.username=u.username and u1.username=i.assignedFaculty "
				 * +
				 * " and i.moduleId=c.moduleId and i.acadYear=c.acadYear and i.acadSession=c.acadSession "
				 * + " and u.programId=p.id " + " and itm.isQueryRaise= 'Y'  " +
				 * " and (i.createdBy=? or i.lastModifiedBy= ?) AND i.active = 'Y' " +
				 * " group by itm.username,itm.icaId ";
				 */
				/*
				 * String sql =
				 * " select i.icaName,i.id as icaId,(case when itm.icaScaledMarks is not null then itm.icaScaledMarks "
				 * + " else itm.icaTotalMarks  end) as icaTotalMarks, " +
				 * " i.acadYear,i.acadSession,concat(u.firstName,' ',u.lastName) as studentName,u.rollNo,u.email, p.programName, "
				 * +
				 * " concat(c.moduleName,'(',c.moduleAbbr,')') as moduleName,itm.`query`,iq.isApproved,DATE_FORMAT(iq.createdDate, \"%M %e %Y\") as raiseQDate, "
				 * +
				 * " itm.username,c1.courseName as division, GROUP_CONCAT(DISTINCT concat(u1.firstName,' ',u1.lastName,'(',u1.username,')')) "
				 * + " as assignedFaculty " +
				 * " from program p,ica_total_marks itm,users u,course c,users u1, ica i  " +
				 * " left outer join course c1 on c1.id=i.eventId " +
				 * " inner join ica_queries iq on iq.icaId=i.id " +
				 * " where itm.icaId=i.id and itm.username=u.username and FIND_IN_SET(u1.username,i.assignedFaculty) "
				 * +
				 * " and i.moduleId=c.moduleId and i.acadYear=c.acadYearCode and i.acadSession=c.acadSession "
				 * + " and u.programId=p.id " + " and itm.isQueryRaise= 'Y'  " +
				 * " and (i.createdBy=? or i.lastModifiedBy= ?)  AND i.active = 'Y' " +
				 * " group by itm.username,itm.icaId ";
				 */

				String sql = " select i.icaName,i.id as icaId,itm.componentId as compId,itm.marks "
						+ " as componentMarks, (case when i.isCourseraIca='Y' then u.acadSession ELSE i.acadSession END) AS acadSession ,"
						+ " i.acadYear,concat(u.firstName,' ',u.lastName) as studentName,u.rollNo,u.email, p.programName, "
						+ " concat(c.moduleName,'(',c.moduleAbbr,')') as moduleName,itm.`query`,iq.isApproved,DATE_FORMAT(iq.createdDate, \"%M %e %Y\") as raiseQDate, "
						+ " itm.username,c1.courseName as division, GROUP_CONCAT(DISTINCT concat(u1.firstName,' ',u1.lastName,'(',u1.username,')')) "
						+ " as assignedFaculty "
						+ " from program p,ica_component_marks itm,users u,course c,users u1, ica i  "
						+ " left outer join course c1 on c1.id=i.eventId "
						+ " inner join ica_queries iq on iq.icaId=i.id "
						+ " where itm.icaId=i.id and itm.username=u.username and FIND_IN_SET(u1.username,i.assignedFaculty) "
						+ " and i.moduleId=c.moduleId and i.acadYear=c.acadYearCode  " + " and u.programId=p.id "
						+ " and itm.isQueryRaise= 'Y'  "
						+ " and (i.createdBy=? or i.lastModifiedBy= ?)  AND i.active = 'Y' "
						+ " group by itm.username,itm.icaId ";

				return findAllSQL(sql, new Object[] { username, username });
			}

			else if (auth.contains(Role.ROLE_FACULTY)) {
				/*
				 * String sql =
				 * " select i.icaName,i.id as icaId,(case when itm.icaScaledMarks is not null then itm.icaScaledMarks  "
				 * + " else itm.icaTotalMarks  end) as icaTotalMarks, " +
				 * " i.acadYear,i.acadSession,concat(u.firstName,' ',u.lastName) as studentName,p.programName, "
				 * + " concat(c.moduleName,'(',c.moduleAbbr,')') as moduleName,itm.`query`," +
				 * " itm.username,c1.courseName as division,concat(u1.firstName,' ',u1.lastName,'(',u1.username,')')"
				 * + " as assignedFaculty " +
				 * " from program p,ica_total_marks itm,users u,course c,users u1, ica i  " +
				 * " left outer join course c1 on c1.id=i.eventId " +
				 * " where itm.icaId=i.id and itm.username=u.username and u1.username=i.assignedFaculty "
				 * +
				 * " and i.moduleId=c.moduleId and i.acadYear=c.acadYear and i.acadSession=c.acadSession "
				 * + " and u.programId=p.id " + " and itm.isQueryRaise= 'Y'  " +
				 * " and (i.assignedFaculty=?) AND i.active = 'Y' " +
				 * " group by itm.username,itm.icaId ";
				 */

				String sql = " select i.icaName,i.id as icaId,itm.componentId as compId,itm.marks "
						+ " as componentMarks, "
						+ " i.acadYear,i.acadSession,concat(u.firstName,' ',u.lastName) as studentName,u.rollNo,u.email, p.programName, "
						+ " concat(c.moduleName,'(',c.moduleAbbr,')') as moduleName,itm.`query`,iq.isApproved,DATE_FORMAT(iq.createdDate, \"%M %e %Y\") as raiseQDate, "
						+ " itm.username,c1.courseName as division, GROUP_CONCAT(DISTINCT concat(u1.firstName,' ',u1.lastName,'(',u1.username,')')) "
						+ " as assignedFaculty "
						+ " from program p,ica_component_marks itm,users u,course c,users u1, ica i  "
						+ " left outer join course c1 on c1.id=i.eventId "
						+ " inner join ica_queries iq on iq.icaId=i.id "
						+ " where itm.icaId=i.id and itm.username=u.username and FIND_IN_SET(u1.username,i.assignedFaculty) "
						+ " and i.moduleId=c.moduleId and i.acadYear=c.acadYearCode and i.acadSession=c.acadSession "
						+ " and u.programId=p.id " + " and itm.isQueryRaise= 'Y'  "
						+ " and (FIND_IN_SET(?,i.assignedFaculty))  AND i.active = 'Y' "
						+ " group by itm.username,itm.icaId ";

				return findAllSQL(sql, new Object[] { username });

			} else {
				return null;
			}
		}

	}
	
	
	public List<IcaTotalMarks> getComponentQueriesNE(String acadYear, Collection<GrantedAuthority> auth,
			String username) {
		if (auth.contains(Role.ROLE_ADMIN)) {
			if (null != acadYear) {
				String sql = "select i.icaName,i.id as icaId,itm.componentId as compId,itm.marks "
						+ " as componentMarks, i.acadSession AS acadSession ,"
						+ " i.acadYear,i.acadSession,concat(u.firstName,' ',u.lastName) as studentName,u.rollNo,u.email, p.programName, "
						+ " concat(m.module_description,'(',m.module_abbr,')') as moduleName,itm.`query`,iq.isApproved,iq.createdDate as raiseQDate, "
						+ " itm.username " + " from program p,ica_component_marks itm,users u,module m, ica i "
						+ " inner join ica_queries iq on iq.icaId=i.id "
						+ " where itm.icaId=i.id and itm.username=u.username"
						+ " and i.moduleId=m.module_id and i.acadYear=m.acadyear " + " and u.programId=p.id "
						+ " and itm.isQueryRaise= 'Y'  "
						+ " and (i.createdBy=? or i.lastModifiedBy= ?) and i.acadYear = ? AND i.active = 'Y' and i.isNonEventModule = 'Y' "
						+ " group by itm.username,itm.icaId";
				return findAllSQL(sql, new Object[] { username, username, acadYear });
			} else {
				String sql = "select i.icaName,i.id as icaId,itm.componentId as compId,itm.marks "
						+ " as componentMarks, i.acadSession AS acadSession ,"
						+ " i.acadYear,i.acadSession,concat(u.firstName,' ',u.lastName) as studentName,u.rollNo,u.email, p.programName, "
						+ " concat(m.module_description,'(',m.module_abbr,')') as moduleName,itm.`query`,iq.isApproved,iq.createdDate as raiseQDate, "
						+ " itm.username " + " from program p,ica_component_marks itm,users u,module m, ica i "
						+ " inner join ica_queries iq on iq.icaId=i.id "
						+ " where itm.icaId=i.id and itm.username=u.username"
						+ " and i.moduleId=m.module_id and i.acadYear=m.acadyear " + " and u.programId=p.id "
						+ " and itm.isQueryRaise= 'Y'  "
						+ " and (i.createdBy=? or i.lastModifiedBy= ?) AND i.active = 'Y' and i.isNonEventModule = 'Y' "
						+ " group by itm.username,itm.icaId";
				return findAllSQL(sql, new Object[] { username, username });
			}
		} else {
			return new ArrayList<>();
		}

	}

	public List<IcaTotalMarks> getFacultyEvaluationStatus(String icaId){
		String sql ="select itm.icaId,isb.facultyId,itm.saveAsDraft,itm.finalSubmit,GROUP_CONCAT(DISTINCT concat(u.firstName,' ',u.lastName,' (',u.username,')')) as assignedFaculty from "
				+ "ica_student_batchwise isb join users u on u.username=isb.facultyId left join ica_total_marks itm on itm.icaId=isb.icaId and isb.username=itm.username "
				+ "where isb.icaid = ? group by isb.facultyId,itm.saveAsDraft,itm.finalSubmit";
		return findAllSQL(sql, new Object[] { icaId });
	}
	public int checkIfSavedAsDraft(Long icaId) {
		String sql = "select count(*) from ica_total_marks where icaId=? and saveAsDraft='Y'";
		return getJdbcTemplate().queryForObject(sql, Integer.class, new Object[] {icaId});
	}
}
