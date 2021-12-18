package com.spts.lms.services.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import javax.validation.ValidationException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;





import com.spts.lms.beans.ica.IcaBean;
import com.spts.lms.beans.course.Course;
import com.spts.lms.beans.course.UserCourse;
import com.spts.lms.beans.user.Role;
import com.spts.lms.beans.user.User;
import com.spts.lms.beans.user.UserRole;
import com.spts.lms.daos.user.UserDAO;
import com.spts.lms.daos.user.UserRoleDAO;
import com.spts.lms.helpers.ApplicationMailer;
import com.spts.lms.helpers.PaginationHelper.Page;
import com.spts.lms.services.BaseService;
import com.spts.lms.utils.PasswordGenerator;

@Service("userService")
@Transactional
public class UserService extends BaseService<User> {
	@Autowired
	private ApplicationMailer mailService;

	@Autowired
	private UserRoleDAO userRoleDAO;

	@Autowired
	private UserDAO userDAO;

	@Override
	protected UserDAO getDAO() {
		return userDAO;
	}

	public User forgotPassword(final String userName) {
		final User user = userDAO.findCredsByUserName(userName);
		if (null == user) {
			throw new ValidationException(
					"Oops we were unable to find the email. Kindly verify and retry.");
		}
		userDAO.resetPassword(user);

		/*
		 * final String subject = "Your new password for LMS"; final String body
		 * = "Dear "+user.getFirstname()+",\n\n" +
		 * "As you requested, your password has now been reset. Your new details are as follows:\n"
		 * +"Username: "+user.getUsername()+"\n"
		 * +"Password: "+newPassword+"\n\n" +"All the best,\n" +"LMS Team.";
		 * mailService.sendMail(email, subject, body);
		 */
		return user;
	}

	public List<User> findCampus() {
		return userDAO.findCampus();
	}

	public boolean authenticate(String username, String password) {
		User user = userDAO.findCredsByUserName(username);
		return PasswordGenerator.matches(password, user.getPassword());
	}

	public int makeUserIncactive(User user) {
		return userDAO.makeUserInactive(user);
	}

	public String getFacultyType(String username) {
		return userDAO.findFacultyType(username);
	}

	public List<User> findAllFacultyByProgramId(Long programId) {
		return userDAO.findAllFacultyByProgramId(programId);
	}

	public List<User> findAllFacultiesByDept(String department) {
		return userDAO.findAllFacultiesByDept(department);
	}

	public Page<User> findUsers(User user, int pageNo, int pageSize) {
		String sql = "select distinct u.* from users u,course c,user_course uc,program p  "
				+ " where u.username=uc.username and c.id=uc.courseId and c.programId=p.id and u.enabled=1 ";
		String countSql = "select count(distinct (u.username)) from users u,course c,user_course uc,program p  "
				+ " where u.username=uc.username and c.id=uc.courseId and c.programId=p.id and u.enabled=1 ";

		ArrayList<Object> parameters = new ArrayList<Object>();

		if (user.getProgramId() != null) {
			sql = sql + " and p.id like ? ";
			countSql = countSql + " and p.id like ? ";
			parameters.add("%" + user.getProgramId() + "%");
		}

		if (user.getUsername() != null && !user.getUsername().isEmpty()) {
			sql = sql + " and u.username like ? ";
			countSql = countSql + " and u.username like ? ";
			parameters.add("%" + user.getUsername() + "%");
		}

		sql = sql + " order by username desc ";

		Object[] args = parameters.toArray();

		return findAllSQL(sql, countSql, args, pageNo, pageSize);
	}

	public List<User> findAllStudents(Long programId) {
		return userDAO.findAllStudents(programId);
	}

	/*
	public void changePasswordForStudentByAdmin(final User user) {
		
		if (!user.getNewPassword().equals(user.getReenterPassword())) {
			throw new ValidationException(
					"Unable to change the password. Kindly verify the the new password and reentered password");
		} else {
			System.out.println("password matched");
		}
		user.setPassword(PasswordGenerator.generatePassword(user
				.getNewPassword()));
		int updated = userDAO.changePassword(user);
	

		System.out.println(" rows Updated");

	}*/
	
	public void changePasswordForStudentByAdmin(final User user) {
		
		if (!user.getNewPassword().equals(user.getReenterPassword())) {
			throw new ValidationException(
					"Unable to change the password. Kindly verify the the new password and reentered password");
		} else {
			System.out.println("password matched");
		}
		user.setPassword(user
				.getNewPassword());
		int updated = userDAO.changePassword(user);
	

		System.out.println(" rows Updated");

	}

	/*
	 * public void changePassword(final User user) { if
	 * (!authenticate(user.getUsername(), user.getOldPassword())) { throw new
	 * ValidationException(
	 * "Unable to change the password. Kindly verify the current password and retry."
	 * ); }
	 * user.setPassword(PasswordGenerator.generatePassword(user.getPassword()));
	 * int updated = userDAO.changePassword(user); if (updated == 0) { throw new
	 * ValidationException(
	 * "Unable to change the password. Kindly verify the current password and retry."
	 * ); } }
	 */

	
	//commented on 24-09-2021
	/*
	public void changePassword(final User user) {
		if (!authenticateAlt(user.getPassword(), user.getOldPassword())) {
			throw new ValidationException(
					"Unable to change the password. Kindly verify the current password and retry.");
		} else if (!user.getNewPassword().equals(user.getReenterPassword())) {
			throw new ValidationException(
					"Unable to change the password. Kindly verify the the new password and reentered password");
		} else {
			System.out.println("password matched");
		}
		user.setPassword(PasswordGenerator.generatePassword(user
				.getNewPassword()));
		int updated = userDAO.changePassword(user);
		

		System.out.println(" rows Updated");

	}*/
	public void changePassword(final User user) {
		//if (!authenticateAlt(user.getPassword(), user.getOldPassword())) {
		if (!user.getPassword().equals(user.getOldPassword())) {
			throw new ValidationException(
					"Unable to change the password. Kindly verify the current password and retry.");
		} else if (!user.getNewPassword().equals(user.getReenterPassword())) {
			throw new ValidationException(
					"Unable to change the password. Kindly verify the the new password and reentered password");
		} else if (user.getPassword().equals(user.getNewPassword())) {
			throw new ValidationException(
					"Unable to change the password. The Password is previously used!");
		} else {
			System.out.println("password matched");
		}
		//temporarily commented
		/*
		user.setPassword(PasswordGenerator.generatePassword(user
				.getNewPassword()));
				*/
		user.setPassword(user.getNewPassword());
		int updated = userDAO.changePassword(user);
		/*
		 * if (updated == 0) {
		 * 
		 * throw new ValidationException(
		 * "Unable to change the password. Kindly verify the current password and retry."
		 * );
		 * 
		 * }
		 */

		System.out.println(" rows Updated");

	}

	public boolean authenticateAlt(String passwordFromUsermgmt, String password) {
		return PasswordGenerator.matches(password, passwordFromUsermgmt);
	}

	public void registerUser(final User user) {
		user.setPassword(PasswordGenerator.generatePassword(user.getUsername()));
		user.setEnabled(true);
		user.setAttempts(0);
		userDAO.insert(user);
		user.updateUserInUserRoles();
		userRoleDAO.insertBatch(user.getUserRoles());
	}

	public User findByUserName(final String username) {
		return userDAO.findByUserName(username);
	}
	
	public User findDetailsUserName(final String username) {
		return userDAO.findDetailsUserName(username);
	}

	public User findStudentObjectId(final String username) {
		return userDAO.findStudentObjectId(username);
	}

	public List<User> findAllByCourse(Long courseId) {
		return userDAO
				.findAllSQL(
						"select u.* from users u, user_course uc, course c where u.username = uc.username "
								+ "  and uc.courseId = c.id and u.active = 'Y' and u.enabled = 1 and c.active = 'Y' and c.id = ? ",
						new Object[] { courseId });
	}

	public List<User> findByProgramIdAndSemester(Long programId,
			String acadSession) {
		return userDAO
				.findAllSQL(
						"select u.* from users u where u.acadSession=? and u.programId= ? and u.enabled = 1 and u.active = 'Y' ",
						new Object[] { acadSession, programId });
	}

	public Map<String, Map<String, String>> findEmailByUserName(
			List<String> username) {
		Map<String, Map<String, String>> result = new HashMap<String, Map<String, String>>();
		List<User> userList = userDAO.findUsers(username);
		Map<String, String> emails = new HashMap<String, String>();
		Map<String, String> mobiles = new HashMap<String, String>();
		for (User u : userList) {
			String email = u.getEmail();
			String mobile = u.getMobile();
			if (email != null && !email.isEmpty()) {
				email = email.trim();
				if (!StringUtils.isEmpty(email)
						&& !StringUtils.isWhitespace(email))
					emails.put(u.getUsername(), email);
			}
			if (mobile != null && !mobile.isEmpty()) {
				mobile = mobile.trim();
				if (!StringUtils.isEmpty(mobile)
						&& !StringUtils.isWhitespace(mobile))
					mobiles.put(u.getUsername(), mobile);
			}
		}

		result.put("emails", emails);
		result.put("mobiles", mobiles);
		return result;

	}

	public String findMyChild(String username) {
		return userDAO.findMyChild(username);

	}

	public User findUserAndProgramByUserName(final String username) {
		return userDAO.findUserAndProgramByUserName(username);
	}

	public void userDAO(List<UserRole> userRoles) {
		userRoleDAO.upsertBatch(userRoles);
	}

	public void upsertRolesBatch(List<UserRole> userRoles) {
		userRoleDAO.upsertBatch(userRoles);
	}

	public int updateProfile(User user) {
		return userDAO.updateProfile(user);

	}

	public List<User> findAllTestScoreByCourse(Long courseId, String acadMonth,
			String acadYear) {
		return userDAO.findAllTestScoreByCourse(courseId, acadMonth, acadYear);
	}

	public List<User> findAllAssignmentScoreByCourse(Long courseId,
			String acadMonth, String acadYear) {
		return userDAO.findAllAssignmentScoreByCourse(courseId, acadMonth,
				acadYear);
	}

	@Async
	public Future<List<User>> findAllTestScoreByCourseAsync(Long courseId,
			String acadMonth, String acadYear) {
		return new AsyncResult<List<User>>(userDAO.findAllTestScoreByCourse(
				courseId, acadMonth, acadYear));
	}

	@Async
	public Future<List<Map<String, Object>>> findAllAssignmentScoreByCourseAsyncList(
			Long courseId, String acadMonth, String acadYear) {
		return new AsyncResult<List<Map<String, Object>>>(
				userDAO.findAllAssignmentScoreByCourseList(courseId, acadMonth,
						acadYear));
	}

	@Async
	public Future<List<Map<String, Object>>> findAllTestScorAndAssigmenteByCourseAsyncList(
			Long courseId, String acadMonth, String acadYear) {
		return new AsyncResult<List<Map<String, Object>>>(
				userDAO.findAllTestScoreAndAssigmentByCourseList(courseId,
						acadMonth, acadYear));
	}

	@Async
	public Future<List<Map<String, Object>>> findAllTestScorAndAssigmenteByCourseAsyncList(
			Long courseId) {
		return new AsyncResult<List<Map<String, Object>>>(
				userDAO.findAllTestScoreAndAssigmentByCourseList(courseId));
	}
	
	@Async
	public Future<List<Map<String, Object>>> findAllTestScoreAndAssigmentByCourseListCreatedByAdminForFaculty(
			List<String> courseIds) {
		return new AsyncResult<List<Map<String, Object>>>(
				userDAO.findAllTestScoreAndAssigmentByCourseListCreatedByAdminForFaculty(courseIds));
	}
	
	
	@Async
	public Future<List<Map<String, Object>>> findAllTestScoreAndAssigmentByCourseListAdmin(String moduleId) {
		return new AsyncResult<List<Map<String, Object>>>(
				userDAO.findAllTestScoreAndAssigmentByCourseListAdmin(moduleId));
	}
	
	@Async
	public Future<List<Map<String, Object>>> findAllTestScoreAndAssigmentByCourseListCreatedByAdminForStudent(String username,String moduleId) {
		return new AsyncResult<List<Map<String, Object>>>(
				userDAO.findAllTestScoreAndAssigmentByCourseListCreatedByAdminForStudent(username,moduleId));
	}


	@Async
	public Future<List<Map<String, Object>>> findAllTestScorAndAssigmenteByCourseAsyncList(
			Long courseId, String username, String acadMonth, String acadYear) {
		return new AsyncResult<List<Map<String, Object>>>(
				userDAO.findTestScoreAndAssigmentByCourseListByUsername(
						username, courseId, acadMonth, acadYear));
	}

	@Async
	public Future<List<Map<String, Object>>> findAllTestScorAndAssigmenteByCourseAsyncList(
			Long courseId, String username) {
		return new AsyncResult<List<Map<String, Object>>>(
				userDAO.findTestScoreAndAssigmentByCourseListByUsername(
						username, courseId));
	}

	@Async
	public Future<List<User>> findAllAssignmentScoreByCourseAsync(
			Long courseId, String acadMonth, String acadYear) {
		return new AsyncResult<List<User>>(
				userDAO.findAllAssignmentScoreByCourse(courseId, acadMonth,
						acadYear));
	}

	public List<User> findAllFaculty() {
		return userDAO.findByRole(Role.ROLE_FACULTY);
	}

	public List<User> getUserDetails(String username) {
		return userDAO.getUserDetails(username);
	}

	public List<User> findAllUsersByProgramId(String role, Long programId) {

		return userDAO.findAllUsersByProgramId(role, programId);

	}

	public String findCampusName(Long campusId) {
		return userDAO.findCampusName(campusId);
	}
	
	public List<User> findAllActiveUsers(String campusId){
		return userDAO.findAllActiveUsers(campusId);
	}
	
	public List<User> findByProgramIdAndYear(Long programId,
			String acadYear,String campusId) {
		if (!campusId.equals("null") ) {
			return userDAO
					.findAllSQL(
							" select distinct u.* from users u,user_course uc,course c,program p where "
									+ " u.username=uc.username and c.id = uc.courseId and c.programId=p.id "

									+ "  and u.enabled=1 and uc.active = 'Y' "
									+ " and p.id=?  and c.acadYear = ? and c.campusId = ? ",
							new Object[] { programId, acadYear, campusId });
		}

		else {
			return userDAO
					.findAllSQL(
							" select distinct u.* from users u,user_course uc,course c,program p where "
									+ " u.username=uc.username and c.id = uc.courseId and c.programId=p.id "

									+ "  and u.enabled=1 and uc.active = 'Y' "
									+ " and p.id=?  and c.acadYear = ? ",
							new Object[] { programId, acadYear });
		}
	}
	
	public List<User> findByProgramIdAndSemesterAndYear(Long programId,
			String acadSession,String acadYear,String campusId) {
		if (!campusId.equals("null") ) {
			return userDAO
					.findAllSQL(
							" select distinct u.* from users u,user_course uc,course c,program p where "
									+ " u.username=uc.username and c.id = uc.courseId and c.programId=p.id "

									+ "  and u.enabled=1 and uc.active = 'Y' "
									+ " and p.id=? and c.acadSession = ? and c.acadYear = ? and c.campusId = ? ",
							new Object[] { programId, acadSession, acadYear,
									campusId });
		} else {

			return userDAO
					.findAllSQL(
							" select distinct u.* from users u,user_course uc,course c,program p where "
									+ " u.username=uc.username and c.id = uc.courseId and c.programId=p.id "

									+ "  and u.enabled=1 and uc.active = 'Y' "
									+ " and p.id=? and c.acadSession = ? and c.acadYear = ? ",
							new Object[] { programId, acadSession, acadYear });
		}
	}
	
	public List<User> findStudentByProgramIdAndSemesterAndYear(Long programId,
			String acadSession,String acadYear) {
		
			return userDAO
					.findAllSQL(
							" select distinct u.* from users u,user_course uc,course c,program p where "
									+ " u.username=uc.username and c.id = uc.courseId and c.programId=p.id "
									+ "  and u.enabled=1 and uc.active = 'Y' and uc.role = 'ROLE_STUDENT'"
									+ " and p.id=? and c.acadSession = ? and c.acadYear = ? ",
							new Object[] { programId, acadSession, acadYear });
	}
	
	
	public Map<String ,List<User>> checkCreatedByRoleStatsMap(String campus, Role role , String fromDate ,String toDate,String type) {
		
		List<String> featureName  = new ArrayList<String>();
		featureName.add("announcement");featureName.add("forum");featureName.add("content");featureName.add("assessment");featureName.add("feedback");featureName.add("query");
		Map<String ,List<User>> userWiseFeature = new HashMap<String, List<User>>();
		
		for(String tblName : featureName){
			if(tblName.equals("assessment") ){
				List<User> assessment  = new ArrayList<User>();
				assessment = userDAO.checkCreatedByRoleStats(campus, "test", role, fromDate, toDate,type);
				/*logger.info("assessment list------>" + assessment);*/
				assessment.addAll(userDAO.checkCreatedByRoleStats(campus, "assignment",role, fromDate, toDate,type));
				/*logger.info("assessment list------>" + assessment);*/
				userWiseFeature.put("assessment",assessment);
			}else if( tblName.equals("feedback") ){
				List<User> feedback  = new ArrayList<User>();
				feedback = userDAO.checkCreatedByRoleStatsNonCourse("feedback", role, fromDate, toDate, type);
				/*logger.info("feedback list---->" + feedback + " size----->" + feedback.size());*/
				userWiseFeature.put("feedback", feedback);
			}else if( tblName.equals("query") ){
				List<User> query  = new ArrayList<User>();
				query = userDAO.checkCreatedByRoleStatsNonCourse("queryresponse", role, fromDate, toDate, type);
				/*logger.info("query list---->" + query + " size----->" + query.size());*/
				userWiseFeature.put("query", query);
			}else{
				userWiseFeature.put(tblName, userDAO.checkCreatedByRoleStats(campus, tblName, role, fromDate, toDate,type));
			}
			 
		}
		
		return userWiseFeature;
		
	}
	
	public List<User> getTotalUserStats(String campus, Role role,String type){
		return userDAO.getTotalUserStats(campus,role,type);
	}
	

	public Map<String ,List<User>> checkAllRoleStatsMap(Role role , String fromDate ,String toDate,String type) {
		
		Map<String, List<User>> userWiseFeature = new HashMap<String, List<User>>();
		List<String> featureName  = new ArrayList<String>();
		if(role.equals(Role.ROLE_FACULTY)){
			featureName.add("Assignment");featureName.add("Test");featureName.add("Announcement");featureName.add("Forum");featureName.add("Content");featureName.add("Evaluation");
		}else if(role.equals(Role.ROLE_STUDENT)){
			featureName.add("Assignment");featureName.add("Test");featureName.add("Forum");featureName.add("Content");featureName.add("Feedback");featureName.add("Query");
		}else if(role.equals(Role.ROLE_ADMIN)){
			featureName.add("Feedback");featureName.add("Announcement");featureName.add("Query");
		}
		
		for(String tblName : featureName){
			if(tblName.equals("Assignment") ){
				
				List<User> assignment  = new ArrayList<User>();
				if(role.equals(Role.ROLE_FACULTY)){
					assignment = userDAO.checkRoleStats("assignment", role, "facultyId", fromDate, toDate, type);
					
				}else if(role.equals(Role.ROLE_STUDENT)){
					assignment = userDAO.checkRoleStats("student_assignment", role, "username", fromDate, toDate, type);
					
				}
				userWiseFeature.put("Assignment",assignment);
				
			}else if( tblName.equals("Test") ){
				
				List<User> test  = new ArrayList<User>();
				if(role.equals(Role.ROLE_FACULTY)){
					test = userDAO.checkRoleStats("test", role, "facultyId", fromDate, toDate, type);
					
				}else if(role.equals(Role.ROLE_STUDENT)){
					test = userDAO.checkRoleStats("student_test", role, "username", fromDate, toDate, type);
					
				}
				userWiseFeature.put("Test",test);
				
			}else if( tblName.equals("Announcement") ){
				
				List<User> announcement  = new ArrayList<User>();
				announcement = userDAO.checkRoleStats("announcement", role, "createdBy", fromDate, toDate, type);
				userWiseFeature.put("Announcement",announcement);
				
			}else if( tblName.equals("Forum") ){
				
				List<User> forum  = new ArrayList<User>();
				if(role.equals(Role.ROLE_FACULTY)){
					forum = userDAO.checkRoleStats("forum", role, "createdBy", fromDate, toDate, type);
					
				}else if(role.equals(Role.ROLE_STUDENT)){
					forum = userDAO.checkRoleStats("forum_reply", role, "createdBy", fromDate, toDate, type);
					
				}
				userWiseFeature.put("Forum",forum);
				
			}else if( tblName.equals("Content") ){
				
				List<User> content  = new ArrayList<User>();
				if(role.equals(Role.ROLE_FACULTY)){
					content = userDAO.checkRoleStats("content", role, "facultyId", fromDate, toDate, type);
					
				}else if(role.equals(Role.ROLE_STUDENT)){
					content = userDAO.checkRoleStats("student_content", role, "username", fromDate, toDate, type);
					
				}
				userWiseFeature.put("Content",content);
				
			}else if( tblName.equals("Evaluation") ){
				
				List<User> evaluation  = new ArrayList<User>();
				evaluation = userDAO.checkCreatedByRoleStatsForEvalution("student_assignment", role, "createdBy", fromDate, toDate, type);
				userWiseFeature.put("Evaluation",evaluation);
				
			}else if( tblName.equals("Feedback") ){
				
				List<User> feedback  = new ArrayList<User>();
				if(role.equals(Role.ROLE_ADMIN)){
					feedback = userDAO.checkAdminRoleStats("feedback", role, "lastModifiedBy", fromDate, toDate, type);
					
				}else if(role.equals(Role.ROLE_STUDENT)){
					feedback = userDAO.checkRoleStats("student_feedback", role, "username", fromDate, toDate, type);
					
				}
				userWiseFeature.put("Feedback",feedback);
				
			}else if( tblName.equals("Query") ){
				
				List<User> query  = new ArrayList<User>();
				if(role.equals(Role.ROLE_ADMIN)){
					query = userDAO.checkAdminRoleStats("queryresponse", role, "queryRespondedBy", fromDate, toDate, type);
					
				}else if(role.equals(Role.ROLE_STUDENT)){
					query = userDAO.checkAdminRoleStats("query", role, "username", fromDate, toDate, type);
					
				}
				userWiseFeature.put("Query",query);
				
			}
			 
		}
		
		return userWiseFeature;
	}
	
	public List<User> findByRole(Role role){
		return userDAO.findByRole(role);
	}
	

	public int insertStudentMapping(User bean) {
		return userDAO.insertStudentMapping(bean);
	}
	
	public List<User> findActiveUsersByRole(Role role){
		return userDAO.findActiveUsersByRole(role);
	}
	
	
	public List<User> searchEvauatedAssignmentFacultyStat(String type ,String fromDate ,String toDate ){
		return userDAO.searchEvauatedAssignmentFacultyStat(type,fromDate,toDate);
	}
	
	public List<User> getUserByUsername(String username){
		return userDAO.getUserByUsername(username);
	}
	
	public User findUserProgramAcadYearByusername(String username){
		return userDAO.findUserProgramAcadYearByusername(username);
	}
	
	public User findPlayerIdByUserName(String username) {
		return userDAO.findPlayerIdByUserName(username);
	}
	
	public void insertUserPlayerId(String username, String playerId) {
		userDAO.insertUserPlayerId(username, playerId);
	}
	
	public void updateUserPlayerId(String username, String playerId) {
		userDAO.updateUserPlayerId(username, playerId);
	}
	public User findActivePlayerIdByUserName(String username) {
		return userDAO.findActivePlayerIdByUserName(username);
	}
	public void deleteUserPlayerId(String username) {
		userDAO.deleteUserPlayerId(username);
	}
	public List<String> findFacultyUsernamesForAttendanceReport() {
		return userDAO.findFacultyUsernamesForAttendanceReport();
	}
	public User findUserWithCampusByUserName(final String username) {
		return userDAO.findUserWithCampusByUserName(username);
	}
	
	
	public void changePasswordForApp(final User user) {
		/*if (!authenticateAlt(user.getPassword(), user.getOldPassword())) {
			throw new ValidationException(
					"Unable to change the password. Kindly verify the current password and retry.");
		} else */
		if (!user.getNewPassword().equals(user.getReenterPassword())) {
			throw new ValidationException(
					"Unable to change the password. Kindly verify the the new password and reentered password");
		} else {
			System.out.println("password matched");
		}
		user.setPassword(PasswordGenerator.generatePassword(user
				.getNewPassword()));
		int updated = userDAO.changePassword(user);
		/*
		 * if (updated == 0) {
		 * 
		 * throw new ValidationException(
		 * "Unable to change the password. Kindly verify the current password and retry."
		 * );
		 * 
		 * }
		 */

		System.out.println(" rows Updated");

	}

	
	
	public List<String> getUserByProgramsAndRole(List<String> programIds, String userType) {
	
		return userDAO.getUserByProgramsAndRole(programIds, userType);
	}
	public List<String> getUserByProgramAndRole(String programId, String userType) {
		
		return userDAO.getUserByProgramAndRole(programId, userType);
	}
	
	public  List<String> getSessionMasterListTest() {
	      return userDAO.getSessionMasterListTest();
	}
	
	public void changePasswordBySupportAdmin(final User user) {
		int updated = userDAO.changePasswordBySupportAdmin(user);
	}
	
	public  List<User> findUsername( final User user) {
	      return userDAO.findUsername(user);
	}
	
	public List<String> findActivePlayerIdForActiveFaculties() {
		return userDAO.findActivePlayerIdForActiveFaculties();
	}
	
	//Email Sending Limit Add Below  3 query 
	public List<User> findUserByProgramIdForMasterEmail(String programId,List<String> semMailList,String campusId) {
		return userDAO.findUserByProgramIdForMasterEmail(programId,semMailList,campusId);
	}
	
	public List<User> findUserForMasterEmail(String username){
		return userDAO
				.findAllSQL(
						" select * from users where  enabled=1 and username = ? ",
						new Object[] { username});
	}
	
	public List<User> findUserForDatewiseForMasterEmail(){
		return userDAO
				.findAllSQL(
						" SELECT u.username,u.email FROM users u ,user_roles ur WHERE u.username=ur.username AND role='ROLE_STUDENT' AND u.enabled=1 ",
						new Object[] {});
	}
	
	// Coursera chnages
	
	public List<String> findstudentForCoursera(Long programId, String acadSession) {
		
		return userDAO.findstudentForCoursera(programId,acadSession);
	}

	//02-07-2020
	public List<User> findAllFacultyForUpload() {
		return userDAO.findAllFaculty();
	}

	public List<User> findNameOfFaculty(List<String> username) {
		return userDAO.findNameOfFaculty(username);
	}
	
	public User findByUserNameLike(String username) {
		return userDAO.findByUserNameLike(username);
	}

	public List<User> getFacultyByCourse(Long courseId) {
		return userDAO.getFacultyByCourse(courseId);
	}
	
	public void changeEmailMobileByApp(String email,String mobile,String username) {
		userDAO.changeEmailMobileByApp(email, mobile, username);
	}
	

	//Peter 04/12/2021
	public User checkIfExistsInDB(String username) {
		return userDAO.checkIfExistsInDB(username);
	}

	
	
	
}
