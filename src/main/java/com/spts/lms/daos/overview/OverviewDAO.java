package com.spts.lms.daos.overview;
 
import java.util.List;
 
import org.springframework.stereotype.Repository;
 
import com.spts.lms.beans.group.Groups;
import com.spts.lms.beans.overview.Overview;
import com.spts.lms.daos.BaseDAO;
 
@Repository("overviewDAO")
public class OverviewDAO extends BaseDAO<Overview> {
 
          @Override
          protected String getTableName() {
 
                   return "overview";
          }
 
          @Override
          protected String getInsertSql() {
 
                   String sql = " INSERT INTO  " + getTableName() + " ( "
                                      + " overviewid, " + " city, " + " persontocontact, "
                                      + " emailid, " + " number, " + " dept " + " VALUES " + " ( "
                                      + " :overviewid, " + " :city, " + " :persontocontact, "
                                      + " :emailid, " + " :dept " + ") ";
 
                   return sql;
          }
 
          @Override
          protected String getUpdateSql() {
 
                   String sql = " UPDATE  " + getTableName() + " SET "
                                      + " overviewid = :overviewid, " + " city = :city, "
                                      + " persontocontact = :persontocontact, "
                                      + " emailid = :emailid, " + " number = :number, "
                                      + " dept = :dept " + " WHERE id = :id ";
 
                   return sql;
          }
 
          @Override
          protected String getUpsertSql() {
                   return null;
          }
 
          public List<String> getOverviewDeptList() {
                   final String sql = " SELECT distinct dept FROM  " + getTableName();
 
                   return getListOfString(sql, new Object[] {});
          }
         
          public List<Overview> listOfOverViewRecords(String sql,Object [] params){
                   return findAllSQL(sql, params);
          }
         
 
}
 