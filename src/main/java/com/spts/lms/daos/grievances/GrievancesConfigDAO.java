package com.spts.lms.daos.grievances;
 
import java.util.List;

import org.springframework.stereotype.Repository;

import com.spts.lms.beans.grievances.GrievancesConfig;
import com.spts.lms.daos.BaseDAO;
 
@Repository("grievancesConfigDAO")
 
public class GrievancesConfigDAO extends BaseDAO<GrievancesConfig> {
 
                @Override
                protected String getTableName() {
                                return "grievanceconfig";
                }
               
                @Override
                protected String getInsertSql() {
                                final String sql = " INSERT INTO grievanceconfig "
                                                                + " ( "
                                                                + " typeOfGrievance, "
                                                                + " grievanceCase  "
                                                                + " )"
                                                                + " VALUES "
                                                                + " ( "
                                                                + " :typeOfGrievance, "
                                                                + " :grievanceCase "
                                                                +")";
                                return sql;
                }
 
                @Override
                protected String getUpdateSql() {
                                final String sql = " UPDATE grievanceconfig "
                                                                + " SET "
                                                                + " typeOfGrievance = :typeOfGrievance, "
                                                                + " grievanceCase = :grievanceCase "
                                                                + " WHERE id = :id ";
                               
                                return sql;
                }
 
 
                @Override
                protected String getUpsertSql() {
                                return null;
                }
               
                public List<String> findCaseBasedOnType(String typeOfGrievance)  {
                                final String sql = "   select grievanceCase from grievanceconfig lgc "
                                                                + "  where lgc.typeOfGrievance=?  "
                                                                + "  GROUP BY lgc.id ";
 
 
                                return getListOfString(sql, new Object[] { typeOfGrievance});
                }
 
                public List<GrievancesConfig> findByType(String username,String grievanceCase) {
                                final String sql = "SELECT lgc.* FROM grievanceconfig lgc "
                                                                + " where lgc.grievanceCase= ? "
                                                                + " order by id desc";
                               
                                return findAllSQL(sql, new Object[] { username, grievanceCase });
                }
               
                public List<String> findUniqueGrievances() {
                                final String sql = " select distinct typeOfGrievance from  "+getTableName();
                                return getListOfString(sql, new Object[] { });
                }
}