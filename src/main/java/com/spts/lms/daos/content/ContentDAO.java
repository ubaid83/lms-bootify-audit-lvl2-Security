package com.spts.lms.daos.content;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import com.spts.lms.beans.content.Content;
import com.spts.lms.beans.user.User;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.web.utils.Utils;

@Repository("contentDAO")
public class ContentDAO extends BaseDAO<Content> {

	@Override
	protected String getTableName() {
		return "content";
	}

	@Override
	protected String getInsertSql() {
		final String sql = "INSERT INTO content ( acadMonth, acadYear, courseId,moduleId, contentType, accessType, startDate, examViewType, "
				+ "endDate, createdBy, createdDate, lastModifiedBy, lastModifiedDate, folderPath, filePath, linkUrl, isCourseRootFolder, "
				+ "facultyId, sendEmailAlert, sendSmsAlert, contentDescription, contentName, parentContentId,parentModuleId, sendEmailAlertToParents , sendSmsAlertToParents)"
				+ "VALUES ( :acadMonth, :acadYear, :courseId,:moduleId, :contentType, :accessType, :startDate, :examViewType, "
				+ ":endDate, :createdBy, :createdDate, :lastModifiedBy, :lastModifiedDate, :folderPath, :filePath, :linkUrl, :isCourseRootFolder, "
				+ ":facultyId, :sendEmailAlert, :sendSmsAlert, :contentDescription, :contentName, :parentContentId,:parentModuleId, :sendEmailAlertToParents, :sendSmsAlertToParents) ";
		return sql;
	}

	@Override
	protected String getUpdateSql() {
/*		final String sql = "Update content set " + "name = :name,"
				+ "acadYear = :acadYear ," + "acadMonth = :acadMonth ,"
				+ "startDate = :startDate ," + "endDate = :endDate ,"
				+ "filePath = :filePath," + "linkUrl = :linkUrl,"
				+ "filePreviewPath = :filePreviewPath,"
				+ "examViewType = :examViewType,"
				+ "showResultsToStudents = :showResultsToStudents ,"
				+ "active = :active,"
				+ "allowAfterEndDate = :allowAfterEndDate ,"
				+ "sendEmailAlert = :sendEmailAlert ,"
				+ "sendEmailAlertToParents = :sendEmailAlertToParents ,"
				+ "sendSmsAlertToParents = :sendSmsAlertToParents ,"
				+ "sendSmsAlert = :sendSmsAlert ," + "maxScore = :maxScore ,"
				+ "lastModifiedBy = :lastModifiedBy ,"
				+ "lastModifiedDate = :lastModifiedDate " + "where id = :id ";
		return sql;*/
		
		final String sql = "Update content set " + "name = :name,"
				+ "acadYear = :acadYear ," + "acadMonth = :acadMonth ,"
				+ "startDate = :startDate ," + "endDate = :endDate ,"
				+ "filePath = :filePath," + "linkUrl = :linkUrl,"
				+ "filePreviewPath = :filePreviewPath,"
				+ "examViewType = :examViewType,"
				+ "showResultsToStudents = :showResultsToStudents ,"
				+ "active = :active,"
				+ "allowAfterEndDate = :allowAfterEndDate ,"
				+ "sendEmailAlert = :sendEmailAlert ,"
				+ "sendEmailAlertToParents = :sendEmailAlertToParents ,"
				+ "sendSmsAlertToParents = :sendSmsAlertToParents ,"
				+ "sendSmsAlert = :sendSmsAlert ," + "maxScore = :maxScore ,"
				+ "lastModifiedBy = :lastModifiedBy ,"
				+ "lastModifiedDate = :lastModifiedDate,"
				+ "moduleId =:moduleId,"
				+ "parentModuleId=:parentModuleId " + "where id = :id ";
		return sql;
	}

	public void updateChildrenDetails(String newFolderPath, String oldFolderPath) {

		final String sql = "update content set "
				+ " filePath = concat(? ,substring(filePath, char_length(?) + 1)) ," // Remove
																						// earlier
																						// folder
																						// path
																						// and
																						// add
																						// new
																						// folder
																						// path
				+ " folderPath = concat(? ,substring(folderPath, char_length(?) + 1)) "
				+ " where folderPath like ? ";

		getJdbcTemplate().update(
				sql,
				new Object[] { newFolderPath, oldFolderPath, newFolderPath,
						oldFolderPath, "%" + oldFolderPath + "%" });

	}

	public List<Content> findByCourse(Long courseId) {
		final String sql = "SELECT a.*, c.courseName FROM content a "
				+ " inner join course c on a.courseId = c.id "
				+ " where a.courseId = ? " + " " + " order by a.id";
		return findAllSQL(sql, new Object[] { courseId });
	}

	public void updateFolderDetails(Content content) {
		final String sql = "update content set " + " acadMonth = :acadMonth, "
				+ " acadYear = :acadYear," + " accessType = :accessType, "
				+ " startDate = :startDate," + " endDate = :endDate,"
				+ " filePath = :filePath," + " contentName = :contentName,"
				+ " contentDescription = :contentDescription, "
				+ " sendEmailAlert = :sendEmailAlert, "
				+ " sendSmsAlert = :sendSmsAlert,"
				+ " examViewType = :examViewType,"
				+ " sendEmailAlertToParents = :sendEmailAlertToParents ,"
				+ " sendSmsAlertToParents = :sendSmsAlertToParents ,"
				+ " lastModifiedBy = :lastModifiedBy, "
				+ " lastModifiedDate = :lastModifiedDate " + " where id = :id";

		updateSQL(content, sql);
	}

	public void updateLink(Content content) {
		final String sql = "update content set " + " acadMonth = :acadMonth, "
				+ " acadYear = :acadYear," + " accessType = :accessType, "
				+ " startDate = :startDate," + " endDate = :endDate,"
				+ " linkUrl = :linkUrl," + " contentName = :contentName,"
				+ " contentDescription = :contentDescription, "
				+ " sendEmailAlert = :sendEmailAlert, "
				+ " sendSmsAlert = :sendSmsAlert,"
				+ " examViewType = :examViewType,"
				+ " sendEmailAlertToParents = :sendEmailAlertToParents ,"
				+ " sendSmsAlertToParents = :sendSmsAlertToParents ,"
				+ " lastModifiedBy = :lastModifiedBy, "
				+ " lastModifiedDate = :lastModifiedDate " + " where id = :id";

		updateSQL(content, sql);

	}

	public void updateFile(Content content) {
		final String sql = "update content set " + " acadMonth = :acadMonth, "
				+ " acadYear = :acadYear," + " accessType = :accessType, "
				+ " startDate = :startDate," + " endDate = :endDate,"
				+ " filePath = :filePath," + " contentName = :contentName,"
				+ " contentDescription = :contentDescription, "
				+ " sendEmailAlert = :sendEmailAlert, "
				+ " sendSmsAlert = :sendSmsAlert,"
				+ " examViewType = :examViewType,"
				+ " sendEmailAlertToParents = :sendEmailAlertToParents ,"
				+ " sendSmsAlertToParents = :sendSmsAlertToParents ,"
				+ " lastModifiedBy = :lastModifiedBy, "
				+ " lastModifiedDate = :lastModifiedDate " + " where id = :id";

		updateSQL(content, sql);

	}

	public void softDeleteFolder(Content content) {
		final String sql = "update content set " + " active = 'N' "
				+ " where folderPath like ? or id = ?";

		getJdbcTemplate().update(sql,
				new Object[] { content.getFilePath() + "%", content.getId() });
	}
	
	public void softDeleteFolderForModule(Content content) {
		final String sql = "update content set " + " active = 'N' "
				+ " where folderPath like ? or id = ? or parentModuleId = ?";

		getJdbcTemplate().update(sql,
				new Object[] { content.getFilePath() + "%", content.getId() ,content.getId()});
			
	}

	public List<Content> findAllContentForFaculty(String acadMonth,
			String acadYear, String facultyId) {
		String sql = " select c.* from content c "
				+ " where c.acadMonth = ? and c.acadYear = ? and c.facultyId = ?";
		return findAllSQL(sql, new Object[] { acadMonth, acadYear, facultyId });

	}

	public List<Content> findAllStudentContent(String acadMonth,
			String acadYear, String facultyId) {
		String sql = " select c.*, sc.* from content c "
				+ " inner join student_content sc on sc.contentId = c.id "
				+ " where c.acadMonth = ? and c.acadYear = ?  and sc.username = ?";
		return findAllSQL(sql, new Object[] { acadMonth, acadYear, facultyId });

	}

	public List<Content> findAllContent(String acadMonth, String acadYear) {
		String sql = " select c.* from content c "
				+ " where c.acadMonth = ? and c.acadYear = ?";
		return findAllSQL(sql, new Object[] { acadMonth, acadYear });

	}

	public List<Content> getSharedFolders(String userName, String acadMonth,
			String acadYear) {
		final String sql = "select * from student_content sc , content c "
				+ " where sc.username = ?   "
				+ " and sc.acadMonth = ? and sc.acadYear = ? "
				+ " and sc.contentId = c.id and c.active = 'Y' ";

		return findAllSQL(sql, new Object[] { userName, acadMonth, acadYear });
	}

	public List<Content> getSharedFolders(String userName) {
		final String sql = "select * from student_content sc , content c "
				+ " where sc.username = ?   "
				+ " and sc.contentId = c.id and c.active = 'Y' and sc.active='Y' and c.startDate <= SYSDATE() and c.endDate >= SYSDATE() and parentContentId is null order by sc.id desc";

		return findAllSQL(sql, new Object[] { userName });
	}

	@Override
	protected String getUpsertSql() {
		return null;
	}

	public List<Content> getSharedFoldersByCourse(String userName,
			Long courseId, String acadMonth, String acadYear) {
		final String sql = "select * from student_content sc , content c "
				+ " where sc.username = ? and sc.courseId = ?  "
				+ " and sc.acadMonth = ? and sc.acadYear = ? "
				+ " and sc.contentId = c.id and c.active = 'Y' ";

		return findAllSQL(sql, new Object[] { userName, courseId, acadMonth,
				acadYear });
	}

	public List<Content> getSharedFoldersByCourse(String userName, Long courseId) {
		/*final String sql = "select * from student_content sc , content c "
				+ " where sc.username = ? and sc.courseId = ?  AND "
				+ " and sc.contentId = c.id and c.active = 'Y' and c.startDate <= ? and c.endDate >= SYSDATE() order by sc.id desc";*/
		final String sql = "select * from student_content sc , content c "
				+ " where sc.username = ? and sc.courseId = ? "
				+ " and sc.contentId = c.id and c.active = 'Y' and sc.active='Y' and c.startDate <= ? and c.endDate >= SYSDATE() and parentContentId is null  order by sc.id desc";
		Date dt = Utils.getInIST();
		return findAllSQL(sql, new Object[] { userName, courseId, dt });
	}
	

	
	

	public List<Content> getFoldersByCourseForFaculty(String facultyId,
			Long courseId) {
		final String sql = "select * from content c"
				+ " where c.createdBy = ? and c.courseId = ?  "
				+ " and c.active = 'Y' ";

		return findAllSQL(sql, new Object[] { facultyId, courseId });
	}

	public List<Content> getFoldersForFaculty(String userName) {
		final String sql = "select * from content c where c.active = 'Y' and c.courseId = ? and c.startDate <= ? and c.endDate >= SYSDATE() and c.accessType = 'Everyone' order by c.id desc "
				+ " where c.createdBy = ?   " + " and c.active = 'Y' ";

		return findAllSQL(sql, new Object[] { userName });
	}

	public void keepCount(Long id) {
		logger.info("Update calling");
		executeUpdateSql("update content set count = count+1 where id = ?",
				new Object[] { id });
	}
	public void keepCount(Long id, int count) {
		logger.info("Update calling");
		executeUpdateSql("update content set count = ? where id = ?",
				new Object[] { count, id });
	}

	public List<Content> findContentsBySessionAndYearForProgram(

	String acadSession, Integer acadYear, Long programId) {

		String sql = "select a.* from content a "

		+ " inner join course c on c.id = a.courseId "

		+ " inner join program p on p.id = c.programId "

		+ " where c.acadSession = ? and c.acadYear = ? and programId = ? ";

		return findAllSQL(sql,

		new Object[] { acadSession, acadYear, programId });

	}

	public List<Content> findContentsBySessionAndYearForCollege(

	String acadSession, Integer acadYear) {

		String sql = "select a.* from content a "

		+ " inner join course c on c.id = a.courseId "

		+ " inner join program p on p.id = c.programId "

		+ " where c.acadSession = ? and c.acadYear = ?";

		return findAllSQL(sql, new Object[] { acadSession, acadYear });

	}

	public List<Content> findAllConetntsByFaculty(String username) {

		final String sql = "SELECT c.* FROM content c "

		+ " where c.facultyId = ?  and  c.active='Y' "

		+ " order by c.endDate desc";

		return findAllSQL(sql, new Object[] { username });

	}

	public List<Content> findAllContentForProgram(Long programId) {

		String sql = "select a.* from content a,course c , program p where a.courseId= c.id and c.programId = p.id and "

				+ " p.id=? and a.active ='Y' order by a.createdDate desc";

		return findAllSQL(sql, new Object[] { programId });

	}

	public List<Content> findAllContentByUsernameAndCourseId(String username,
			String courseId) {
		String sql = " select c.*,cr.courseName from content c,course cr where courseId = ? and (c.createdBy = ? or facultyId = ?) and (contentType = ? or contentType = ?) "
				+ " and c.courseId=cr.id and c.active='Y' and cr.active='Y'";

		return findAllSQL(sql, new Object[] { courseId, username, username,
				Content.FILE, Content.FOLDER });
	}

	public List<Content> findAllContentByUsername(String username) {
		String sql = " select c.*,cr.courseName from content c,course cr where (c.createdBy = ? or facultyId = ?) and (contentType = ? or contentType = ?) "
				+ " and c.courseId=cr.id and c.active='Y' and cr.active='Y'";

		return findAllSQL(sql, new Object[] { username, username, Content.FILE,
				Content.FOLDER });
	}

	public List<Content> findAllContentByCourseId(String courseId) {
		String sql = " select c.*,cr.courseName from content c,course cr where courseId = ?  and (contentType = ? or contentType = ?) "
				+ " and c.courseId=cr.id and c.active='Y' and cr.active='Y'";
		return findAllSQL(sql, new Object[] { courseId, Content.FILE,
				Content.FOLDER });
	}

	public List<Content> findAllContent() {
		String sql = " select c.*,cr.courseName from content c,course cr where (contentType = ? or contentType= ?) "
				+ " and c.courseId=cr.id";

		return findAllSQL(sql, new Object[] { Content.FILE, Content.FOLDER });
	}

	public List<Content> getSharedFoldersAndFilesByCourse(String userName,
			Long courseId) {
		final String sql = "select *,cr.courseName from student_content sc , content c,course cr "
				+ " where sc.username = ? and sc.courseId = ?  "
				+ " and sc.contentId = c.id and sc.active = 'Y' and  c.active='Y' and c.startDate <= ? and c.endDate >= SYSDATE() "
				+ "  and sc.courseId=cr.id "
				+ " and (c.contentType = ? or c.contentType = ? ) order by sc.id desc";
		Date dt = Utils.getInIST();

		return findAllSQL(sql, new Object[] { userName, courseId, dt,
				Content.FILE, Content.FOLDER });
	}

	public List<Content> getSharedFoldersAndFile(String userName) {
		final String sql = "select *,cr.courseName from student_content sc , content c,course cr "
				+ " where sc.username = ?   "
				+ " and sc.contentId = c.id and sc.active = 'Y' and c.active='Y' and c.startDate <= SYSDATE() and c.endDate >= SYSDATE() "
				+ "  and sc.courseId=cr.id "
				+ " and (c.contentType = ? or c.contentType = ? ) order by sc.id desc";

		return findAllSQL(sql, new Object[] { userName, Content.FILE,
				Content.FOLDER });
	}

	public List<Content> findAllEveryoneContentForStudent(String userName) {
		String sql = "select  c.*, cr.courseName from user_course uc , content c, course cr "
				+ " where uc.courseId=c.courseId and c.courseId=cr.id and uc.active = 'Y' and c.active = 'Y' and cr.active = 'Y' and "
				+ " c.startDate <= SYSDATE() and c.endDate >= SYSDATE() and uc.username = ? and c.accessType = 'Everyone' "
				+ " and (c.contentType = ? or c.contentType = ? ) order by c.id desc";
		return findAllSQL(sql, new Object[] { userName, Content.FILE,
				Content.FOLDER });
	}

	public Content findOneContent(String contentName, String folderPath) {
		String sql = "select * from content where contentName = ? and folderPath = ? and active = 'Y' ";
		return findOneSQL(sql, new Object[] { contentName, folderPath });
	}


	
/*	public List<Content> findEveryoneContentByCourseId(Long courseId) {
		
		String sql = "select c.*, cr.courseName from content c,course cr where c.active = 'Y' and c.courseId=cr.id and c.courseId = ?  and c.startDate <= SYSDATE() and c.endDate >= SYSDATE() and c.accessType = 'Everyone' AND c.parentContentId IS NULL order by c.id desc";
		
		return findAllSQL(sql, new Object[] { courseId });
	}*/
	
	public List<Content> findEveryoneContentByCourseId(Long courseId) {
		
		String sql = "select c.*, cr.courseName from content c,course cr where c.active = 'Y' and c.courseId=cr.id and c.courseId = ?  and c.startDate <= SYSDATE() and c.endDate >= SYSDATE() and c.accessType = 'Everyone' AND c.parentContentId IS NULL order by c.id desc";
		
		return findAllSQL(sql, new Object[] { courseId });
	}

/*	public List<Content> findEveryoneContentByUC(String username) {
		String sql = "select c.*, cr.courseName from content c,course cr , user_course uc "
				+ " where c.active = 'Y' and c.courseId=cr.id and uc.username = ? and c.startDate <= SYSDATE() "
				+ "and c.endDate >= SYSDATE() and c.accessType = 'Everyone' and uc.courseId=cr.id order by c.id desc";
		return findAllSQL(sql, new Object[] { username });

	}*/
	
	public List<Content> findPublicContentByCourseId(Long courseId) {
		String sql = "select c.*, cr.courseName from content c,course cr where c.active = 'Y' and c.courseId=cr.id and c.courseId = ?  and c.startDate <= SYSDATE() and c.endDate >= SYSDATE() and c.accessType = 'Public' AND c.parentContentId IS NULL order by c.id desc";
		return findAllSQL(sql, new Object[] { courseId });
	}
	
	/*	public List<Content> findEveryoneContentByCourseId(Long courseId) {
	String sql = "select c.*, cr.courseName from content c,course cr where c.active = 'Y' and c.courseId=cr.id and c.courseId = ?  and c.startDate <= SYSDATE() and c.endDate >= SYSDATE() and c.accessType = 'Everyone'  order by c.id desc";
	return findAllSQL(sql, new Object[] { courseId,parentContentId });
}*/
	
	public List<Content> findPublicContentByCourseId(Long courseId,Long parentContentId) {
		String sql = "select c.*, cr.courseName from content c,course cr where c.active = 'Y' and c.courseId=cr.id and c.courseId = ?  and c.startDate <= SYSDATE() and c.endDate >= SYSDATE() and c.accessType = 'Public' AND c.parentContentId = ?  order by c.id desc";
		return findAllSQL(sql, new Object[] { courseId,parentContentId });
	}
	

	
	public List<Content> findEveryoneContentByUC(String username) {
		String sql = "select c.*, cr.courseName from content c,course cr , user_course uc "
				+ " where c.active = 'Y' and c.courseId=cr.id and uc.username = ? and c.startDate <= SYSDATE() "
				+ "and c.endDate >= SYSDATE() and c.accessType = 'Everyone' AND c.parentContentId IS NOT NULL and uc.courseId=cr.id order by c.id desc";
		return findAllSQL(sql, new Object[] { username });

	}
	
public List<Content> findByParentModuleId(Long parentModuleId){
		
		String sql = "select * from content where parentModuleId = ?";
		
		return findAllSQL(sql, new Object[] { parentModuleId });
	}





	public List<Content> findparentBychildId(Long moduleId) {
		String sql = " select * from content where moduleId =  ?";
		
		return findAllSQL(sql, new Object[] { moduleId});
	}

	
	public List<Content> getSharedFoldersByCourseForModule(String userName, Long courseId,String moduleId) {
		/*final String sql = "select * from student_content sc , content c "
				+ " where sc.username = ? and sc.courseId = ?  AND c.parentContentId IS NULL "
				+ " and sc.contentId = c.id and c.active = 'Y' and c.startDate <= ? and c.endDate >= SYSDATE() order by sc.id desc";
		Date dt = Utils.getInIST();*/
		
		final String sql = "select * from student_content sc , content c "
				+ " where sc.username = ? and (sc.courseId = ?  OR (c.moduleId = ? AND c.parentModuleId IS NULL) ) AND c.parentContentId IS NULL "
				+ " and sc.contentId = c.id and c.active = 'Y' and c.startDate <= ? and c.endDate >= SYSDATE() order by sc.id desc";
		Date dt = Utils.getInIST();

		return findAllSQL(sql, new Object[] { userName, courseId,moduleId, dt });
	}
	
	public List<Content> getSharedFoldersByModule(String userName,String moduleId) {
		final String sql = "select * from student_content sc , content c "
				+ " where sc.username = ? and c.moduleId = ?  AND c.parentContentId IS NULL "
				+ " and sc.contentId = c.id and c.active = 'Y' and c.startDate <= ? and c.endDate >= SYSDATE() order by sc.id desc";
		Date dt = Utils.getInIST();

		return findAllSQL(sql, new Object[] { userName,moduleId, dt });
	}
	
	

public void deleteSoftByParentModuleId(String id) {
	final String sql = "Update " + getTableName()
			+ " set active = 'N' WHERE parentModuleId = ?";
	getJdbcTemplate().update(sql, new Object[] { id });
}

public Content findAcadMonthByModuleId(String moduleId)
{
	String sql="SELECT DISTINCT acadMonth FROM content WHERE moduleId = ? AND acadMonth IS NOT NULL";
	return findOneSQL(sql, new Object[]{moduleId});

}

public List<Long> getIdByParentModuleId(Long parentModuleId){
	 String sql = "select id from content where parentModuleId = ?";
	 return getJdbcTemplate().queryForList(sql, new Object[] { parentModuleId },
	   Long.class); 
	}

public List<Content> getContentByParentModuleId(Long parentModuleId){
	 String sql = "select * from content where parentModuleId = ?";
	 return findAllSQL(sql, new Object[]{parentModuleId}); 
	}


public Content findIdByCourseIdAndParentModuleId(String courseId, String parentModuleId){
	 String sql = "select * from content where courseId = ? and parentModuleId = ? ";
	 return findOneSQL(sql, new Object[]{courseId, parentModuleId});
	}

public Content findByModuleID(String moduleId,Integer acadYear) {
	String sql = " SELECT * FROM course WHERE moduleId = ?  AND acadYear = ? and moduleName is not null";
	return findOneSQL(sql, new Object[]{moduleId,acadYear});
	
	//return findAllSQL(sql, new Object[] { moduleId,acadYear});
}
public List<Content> findEveryoneContentByCourseIdPrentNull(Long courseId) {
	String sql = "select c.*, cr.courseName from content c,course cr where c.active = 'Y' and c.courseId=cr.id and c.courseId = ?  and c.startDate <= SYSDATE() and c.endDate >= SYSDATE() and c.accessType = 'Everyone'  order by c.id desc";
	return findAllSQL(sql, new Object[] { courseId});
}

public Content findOneContentForModule(String contentName, String folderPath) {
String sql = "select * from content where contentName = ? and folderPath = ? and active = 'Y'  and parentModuleId is null";
return findOneSQL(sql, new Object[] { contentName, folderPath });
}

	
	public List<Content> findParentAndChildContentForModule(){
		String sql ="select * from content where id = ?";
		return findAllSQL(sql, new Object[]{}); 
	}
	
	public String getParentModuleIdById(Long id){
		String sql ="Select parentModuleId from content where id =?";
		return getJdbcTemplate().queryForObject(sql, new Object[] { id },
                String.class);

	}
	public Content getStudentViewCountForParent(List<Long> parentModuleId){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", parentModuleId);
		String sql = "select sum(count) as count from content where id in (:id)";
		return getNamedParameterJdbcTemplate().queryForObject(sql, params,
				   BeanPropertyRowMapper.newInstance(Content.class));
	}
//	public Content findOneContentByCourseId(String contentName, String folderPath,String courseId) {
//		String sql = "select * from content where contentName = ? and folderPath = ? and active = 'Y' and courseId= ?";
//		return findOneSQL(sql, new Object[] { contentName, folderPath, courseId });
//	}
	public Content findOneContentByCourseId(String contentName, String folderPath,String courseId,String facultyId) {
		String sql = "select * from content where contentName = ? and folderPath = ? and active = 'Y' and courseId= ? and facultyId= ?";
		return findOneSQL(sql, new Object[] { contentName, folderPath, courseId, facultyId });
	}
	//Hiren 31-03-2020
	public List<Content> findContentByNameAndFilelPath(String contentName, String filePath) {
		String sql = "SELECT * FROM content " + 
					"WHERE contentName = ? AND filePath = ? AND active = 'Y' ";
		return findAllSQL(sql, new Object[] { contentName, filePath });
	}

	public void updateShareToSchool(String schoolName,String filePath) {

		final String sql = "update content set schoolToExport = CONCAT(IFNULL(CONCAT(schoolToExport,','),''),?) where filePath like ? and (schoolToExport not like ? or schoolToExport is null)";
		getJdbcTemplate().update(sql,new Object[] { schoolName, filePath+"%", "%"+schoolName+"%"});

	}
	public void deleteSoftByParentContentId(String id) {
		final String sql = "Update " + getTableName()
				+ " set active = 'N' WHERE parentContentId = ?";
		getJdbcTemplate().update(sql, new Object[] { id });
	}

	public List<Content> findContentByFileName(String filePath) {
		String sql = "SELECT * FROM content where filePath like ? AND active = 'Y'";
		return findAllSQL(sql, new Object[] {  "%"+filePath+"%" });
	}
	
	public List<Content> findAllByParentContentId(Content content) {
		String sql = "SELECT * from content where folderPath like ? or id = ? or parentModuleId = ? and active ='Y'";
		return findAllSQL(sql, new Object[] {  content.getFilePath() + "%", content.getId() ,content.getId() });
	}
	
	public List<Content> findAllContentsByParams(String programId, String acadYear, String campusId) {
		if (campusId != null) {
			String sql = " SELECT concat(u.firstName,' ',u.lastName) AS facultyName,cc.facultyId,"
					+ " (case when cc.moduleId IS NOT NULL then c.moduleName ELSE c.courseName END)"
					+ " as courseName,cc.moduleId,cc.courseId, cc.contentName,cc.createdDate,cc.id"
					+ " FROM users u,course c,content cc WHERE (c.id=cc.courseId"
					+ " OR c.moduleId=cc.moduleId ) AND  cc.facultyId=u.username"
					+ " AND   cc.active='Y' AND c.programId=? and c.acadYear=? and c.campusId =? " + " GROUP BY cc.id";
			return findAllSQL(sql, new Object[] { programId, acadYear, campusId });
		} else {
			String sql = " SELECT concat(u.firstName,' ',u.lastName) AS facultyName,"
					+ " (case when cc.moduleId IS NOT NULL then c.moduleName ELSE c.courseName END)"
					+ " as courseName,cc.moduleId,cc.courseId, cc.contentName,cc.createdDate,cc.id"
					+ " FROM users u,course c,content cc WHERE (c.id=cc.courseId"
					+ " OR c.moduleId=cc.moduleId ) AND  cc.facultyId=u.username"
					+ " AND   cc.active='Y' AND c.programId=? and c.acadYear=? " + " GROUP BY cc.id";
			return findAllSQL(sql, new Object[] { programId, acadYear });
		}
	}
	public List<Content> findByContentId(String contentId){
		String sql = "SELECT * FROM content where parentContentId=? and active = 'Y'";
		return findAllSQL(sql, new Object[] { contentId });
	}
	
	public void updateAccessTypeById(String accessType, String contentId){
		String sql = "UPDATE content set accessType=? where parentContentId=? and active = 'Y'";
		getJdbcTemplate().update(sql, new Object[] { accessType, contentId });
	}
	
	public List<Content> findOneContentForMultipleCourse(String contentName, String folderPath) {
		String sql = "select * from content where contentName = ? and folderPath = ? and active = 'Y' ";
		return findAllSQL(sql, new Object[] { contentName, folderPath });
	}
}
