package com.spts.lms.services.grievances;
 
import java.util.List;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
 
import com.spts.lms.beans.assignment.Assignment;
import com.spts.lms.beans.assignment.StudentAssignment;
import com.spts.lms.beans.grievances.GrievancesConfig;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.daos.grievances.GrievancesConfigDAO;
import com.spts.lms.helpers.PaginationHelper.Page;
import com.spts.lms.services.BaseService;
 
@Service("grievancesConfigService")
@Transactional
public class GrievancesConfigService extends BaseService<GrievancesConfig> {
                @Autowired
                private GrievancesConfigDAO grievancesConfigDAO;
 
                @Override
                public BaseDAO<GrievancesConfig> getDAO() {
                                return grievancesConfigDAO;
                }
 
                public List<String> findCaseBasedOnType(String typeOfGrievance) {
                                return grievancesConfigDAO.findCaseBasedOnType(typeOfGrievance);
                }
               
                public List<String> findUniqueGrievances() {
                                return grievancesConfigDAO.findUniqueGrievances();
                }
 
                public List<GrievancesConfig> findByType(String username,
                                                String grievanceCase) {
                                return grievancesConfigDAO.findByType(username, grievanceCase);
                }
 
}