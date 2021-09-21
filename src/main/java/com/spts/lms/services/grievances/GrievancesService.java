package com.spts.lms.services.grievances;
import java.util.ArrayList;
import java.util.List;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
 
import com.spts.lms.beans.grievances.Grievances;
import com.spts.lms.beans.overview.Overview;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.daos.grievances.GrievancesDAO;
import com.spts.lms.daos.overview.OverviewDAO;
import com.spts.lms.services.BaseService;
@Service("grievancesService")
@Transactional
public class GrievancesService extends BaseService<Grievances> {
                @Autowired
                private GrievancesDAO grievancesDAO;
               
                @Autowired
                private OverviewDAO overviewDAO;
                @Override
                public BaseDAO<Grievances> getDAO() {
                                return grievancesDAO;
                }
                public List<Grievances> getGrievancesListByUser(Long id) {
                                return grievancesDAO.getGrievancesListByUser(id);
                }
                public List<Grievances> findByUserGrievances(String username)
                {
                                return grievancesDAO.findByUserGrievances(username);
                }
                public List<String> getOverviewDeptList()
                {
                                return overviewDAO.getOverviewDeptList();
                }
                
                
                
              public List<Overview> listOfOverViewBasedOnCityAndDept(String city,String dept){
                  String sql = "  select persontocontact,emailid,number from overview where 1=1 ";
                  StringBuffer strBuffer = new StringBuffer(sql);
                  ArrayList<Object> parameters= new ArrayList<Object>();
                  if(!StringUtils.isEmpty(city)){
                                  strBuffer.append(" and city = ? ");
                                  parameters.add(city);
                  }
                  if(!StringUtils.isEmpty(dept)){
                                  strBuffer.append(" and dept = ? ");
                                  parameters.add(dept);
                  }
                  if(!StringUtils.isEmpty(dept) && !StringUtils.isEmpty(city)){
                                  strBuffer.append(" and dept = ? and city = ? ");
                                  parameters.add(dept);
                                  parameters.add(city);
                  }
                  
                  return overviewDAO.listOfOverViewRecords(strBuffer.toString(),parameters.toArray(new String[parameters.size()]));
                  
              }
              public List<Grievances> getAllGrievances(){
            	  return grievancesDAO.getAllGrievances();
              }
              
              public String   findSrbPath(String id)
              {
                    return grievancesDAO.findSrbPath(id);
              }
              public String findSrbPathCheck() {
 				 return grievancesDAO.findSrbPathCheck();
 			}

              
  			public List<String> findPortalManualPath(List<String> authority) {
  				  return grievancesDAO.findPortalManualPath(authority);
  			}

}
