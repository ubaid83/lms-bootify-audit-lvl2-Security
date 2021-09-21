package com.spts.lms.services.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spts.lms.beans.user.Role;
import com.spts.lms.beans.user.User;
import com.spts.lms.beans.user.UserRole;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.daos.user.UserDAO;
import com.spts.lms.daos.user.UserRoleDAO;
import com.spts.lms.helpers.ApplicationMailer;
import com.spts.lms.services.BaseService;
import com.spts.lms.utils.PasswordGenerator;

@Service("userRoleService")
@Transactional
public class UserRoleService extends BaseService<UserRole>{
	
	 @Autowired
     private UserRoleDAO userRoleDao;

     @Override
     public BaseDAO<UserRole> getDAO() {
                     return userRoleDao;
     }

     public UserRole findAdmin() {
 		return userRoleDao.findAdmin();
 	}
     
     public void  softDeleteByUsername(String username){
    	 userRoleDao.softDeleteByUsername(username);
     }

     public UserRole findRoleByUsername(String username){
     	return userRoleDao.findRoleByUsername(username);
      }
     public List<UserRole> findRoles(){
    	 return userRoleDao.findRoles();
     }
     
     public List<UserRole> findUsersByRole(String role){
    	 return userRoleDao.findUsersByRole(role);
     }
}
