package com.spts.lms.daos.library;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Repository;

import com.spts.lms.beans.content.Content;
import com.spts.lms.beans.course.Course;
import com.spts.lms.beans.library.Library;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.web.utils.Utils;

@Repository("libraryDAO")
public class LibraryDAO extends BaseDAO<Library> {

	@Override
	protected String getTableName() {
		return "library";
	}

	@Override
	protected String getInsertSql() {
		final String sql = "INSERT INTO library ( contentType, createdBy, createdDate, lastModifiedBy, lastModifiedDate, folderPath, "
				+ " filePath, linkUrl, contentDescription, contentName, parentId, shareToSchools) "
				+ "VALUES ( :contentType, :createdBy, :createdDate, :lastModifiedBy, :lastModifiedDate, :folderPath, "
				+ " :filePath, :linkUrl, :contentDescription, :contentName, :parentId, :shareToSchools) ";
		return sql;
	}

	@Override
	protected String getUpdateSql() {
		final String sql = "   Update library set " + "contentType = :contentType," + "folderPath = :folderPath ,"
				+ " filePath = :filePath ," + "linkUrl = :linkUrl ," + "shareToSchools =:shareToSchools,"

				+ " contentDescription = :contentDescription ," + "contentName = :contentName, active=:active "
				+ " where id = :id ";
		return sql;
	}
	//12-12-2020 for update
		public void updateChildrenDetails(String newFolderPath, String oldFolderPath,String oldFolderPathWOS) {
			
					final String sql = "update library set filePath = "
							+ "if(substring(filePath,1,1) = '/',"
								+ " concat(? ,substring(filePath, char_length(?) + 1)),"
								+ " concat(? , substring(filePath, char_length(?) + 1))),"
							+ " folderPath = if(substring(folderPath,1,1) = '/',"
								+ " concat(? ,substring(folderPath, char_length(?) + 1)), "
								+ "concat(? , substring(folderPath, char_length(?) + 1))) "
							+ "where folderPath like ? or folderPath like ?";
					
					getJdbcTemplate().update(
							sql,
							new Object[] { newFolderPath, oldFolderPath, newFolderPath,
									oldFolderPathWOS,newFolderPath, oldFolderPath, newFolderPath,
									oldFolderPathWOS, "%"+oldFolderPathWOS + "/%", "%"+oldFolderPathWOS });
			
				}
		//For delete
		public void updateChildrenDetails(String newFolderPath, String oldFolderPath) {

			final String sql = "update library set "
					+ " filePath = concat(? ,substring(filePath, char_length(?) + 1)) ," 
					+ " folderPath = concat(? ,substring(folderPath, char_length(?) + 1)) "
					+ " where folderPath like ? or folderPath like ?";
			System.out.println("SQL _______--------------- " + sql);

			getJdbcTemplate().update(
					sql,
					new Object[] { newFolderPath, oldFolderPath, newFolderPath,
							oldFolderPath, "%"+oldFolderPath + "/%", "%"+oldFolderPath });

		}
//	public void updateChildrenDetails(String newFolderPath, String oldFolderPath) {
//
//		final String sql = "update library set "
//				+ " filePath = concat(? ,substring(filePath, char_length(?) + 1)) ," // Remove
//																						// earlier
//																						// folder
//																						// path
//																						// and
//																						// add
//																						// new
//																						// folder
//																						// path
//				+ " folderPath = concat(? ,substring(folderPath, char_length(?) + 1)) "
//				+ " where folderPath like ? ";
//		System.out.println("SQL _______--------------- " + sql);
//
//		getJdbcTemplate().update(
//				sql,
//				new Object[] { newFolderPath, oldFolderPath, newFolderPath,
//						oldFolderPath, "%"+oldFolderPath + "%" });
//
//	}

	public void updateChildrenDetailstest(String newFolderPath,
			String oldFolderPath) {

		final String sql = "update library set " + " filePath = ? ," // Remove
																		// earlier
																		// folder
																		// path
																		// and
																		// add
																		// new
																		// folder
																		// path
				+ " folderPath = ?" + " where folderPath like ? ";
		System.out.println("SQL _______--------------- " + sql);

		getJdbcTemplate().update(
				sql,
				new Object[] { newFolderPath, newFolderPath,
						oldFolderPath + "%" });

	}

	public void updateFolderDetails(Library content) {
		final String sql = "update library set " + " filePath = :filePath,"
				+ " contentName = :contentName,"
				+ " contentDescription = :contentDescription, "
				+ " lastModifiedBy = :lastModifiedBy, "
				+ " lastModifiedDate = :lastModifiedDate " + " where id = :id";

		updateSQL(content, sql);
	}

	public List<Library> getELibraries() {
		String sql = "select * from e_libraries where active='Y'";
		return findAllSQL(sql, new Object[] {});
	}

	public void updateLink(Library content) {
		final String sql = "update library set " + " linkUrl = :linkUrl,"
				+ " contentName = :contentName,"
				+ " contentDescription = :contentDescription, "
				+ " lastModifiedBy = :lastModifiedBy, "
				+ " lastModifiedDate = :lastModifiedDate " + " where id = :id";

		updateSQL(content, sql);

	}

	public void updateFile(Library content) {
		final String sql = "update library set " + " filePath = :filePath,"
				+ " contentName = :contentName,"
				+ " contentDescription = :contentDescription, "
				+ " lastModifiedBy = :lastModifiedBy, "
				+ " lastModifiedDate = :lastModifiedDate " + " where id = :id";

		updateSQL(content, sql);

	}

	public void softDeleteFolder(Library library) {
		final String sql = "update library set " + " active = 'N',lastModifiedDate=? "
				+ " where folderPath like ? or id = ?";
		String newFolderPath = library.getFolderPath();
		if (!newFolderPath.endsWith("/")) {
			newFolderPath = (newFolderPath + "/%");
		}
		getJdbcTemplate().update(sql,
				new Object[] { Utils.getInIST(),newFolderPath, library.getId() });
	}

	public List<Library> findByParentId(Long parentId) {
		
		//String sql = "select id,createdBy,folderPath,contentDescription,contentName,filePath,linkUrl,parentId,active,contentType from library where parentId = ? and active ='Y' ";
		
		String sql = "select id,createdBy,folderPath,contentDescription,contentName,filePath,linkUrl,parentId,active,contentType from "
				+ " library where parentId = ? and active ='Y' order by id ";
		return findAllSQL(sql, new Object[] { parentId });
	}

	@Override
	protected String getUpsertSql() {
		return null;
	}

	public Long insertLibrary(Library library) {
		Long id = null;

		try {

			insertWithIdReturn(library);
			String sql = "select id from library where folderPath = ?";
			Library lib = findOneSQL(sql,
					new Object[] { library.getFolderPath() });

			id = lib.getId();

		} catch (Exception e) {
			logger.error(e);
		}
		return id;
	}

	public void readLibrary(File dir, Long pId, String username) {
		System.out.println("Inside readLibrary---- " + dir.getAbsolutePath());
		File[] files = dir.listFiles();
		if (files.length != 0) {

			for (File f : files) {
				logger.info("File name is ------------------ " + f.getName());
				Long parentId = null;
				String contentType = null;
				String newPath = f.getAbsolutePath();
				String fileName = f.getName();

				if (f.isDirectory()) {
					logger.info(f.getAbsolutePath());

					contentType = "Folder";

					newPath = f.getAbsolutePath().replaceAll("\\\\", "/");
					Library l = new Library();
					l.setContentName(f.getName());
					l.setFolderPath(newPath);
					l.setFilePath(newPath);
					l.setContentType(contentType);
					l.setCreatedBy(username);
					l.setLastModifiedBy(username);
					l.setParentId(pId);

					parentId = insertLibrary(l);
					readLibrary(f, parentId, username);
				} else {

					logger.info(f.getAbsolutePath());
					logger.info("READ LIBRARY ----------- " + f.getParent());
					contentType = "File";

					newPath = f.getAbsolutePath().replaceAll("\\\\", "/");
					Library l = new Library();
					l.setContentName(fileName);
					l.setFolderPath(f.getParent().replaceAll("\\\\", "/") + "/");
					l.setFilePath(newPath);
					l.setContentType(contentType);
					l.setCreatedBy(username);
					l.setLastModifiedBy(username);
					l.setParentId(pId);

					insertLibrary(l);

				}
			}
		}

	}
	//Read Lib for s3
	public void readLibraryS3(File dir, Long pId, String username, String libraryRootPath, String destFolder) {
		System.out.println("Inside readLibrary---- " + dir.getAbsolutePath());
		System.out.println("destFolder-----" + destFolder);
		File[] files = dir.listFiles();
		if (files != null) {
			if (files.length != 0) {

				for (File f : files) {
					logger.info("File name is ------------------ " + f.getName());
					Long parentId = null;
					String contentType = null;

					String newPath = destFolder;
					String fileName = f.getName();

					if (f.isDirectory()) {
						logger.info(f.getAbsolutePath());
						newPath = destFolder + "/" + f.getName();
						contentType = "Folder";
						// newPath = newPath +"/"+f.getName();
						// newPath = newPath;
						Library l = new Library();
						l.setContentName(f.getName());
						l.setFolderPath(newPath);
						l.setFilePath(newPath);
						l.setContentType(contentType);
						l.setCreatedBy(username);
						l.setLastModifiedBy(username);
						l.setParentId(pId);

						parentId = insertLibrary(l);
						readLibraryS3(f, parentId, username, newPath, destFolder);
					} else {

						logger.info(f.getAbsolutePath());
						logger.info("READ LIBRARY ----------- " + f.getParent());
						contentType = "File";

						String fileNewPath = newPath + "/" + f.getName();
						Library l = new Library();
						l.setContentName(fileName);
						l.setFolderPath(newPath);
						l.setFilePath(fileNewPath);
						l.setContentType(contentType);
						l.setCreatedBy(username);
						l.setLastModifiedBy(username);
						l.setParentId(pId);

						insertLibrary(l);

					}
				}
			}
		}

	}
	
	
	//changes by hiren 2020-05-05
	public void readLibraryForZipS3(File dir, Long pId, String username,String shareToSchools,String folderPathS3 ) {
		System.out.println("Inside readLibrary---- " + dir.getAbsolutePath());
		folderPathS3 = folderPathS3.substring(0, folderPathS3.lastIndexOf("/")+1);
		logger.info("folderPathS3--->"+folderPathS3);
		
		File[] files = dir.listFiles();
		if (files.length != 0) {
			for (File f : files) {
				logger.info("File name is ------------------ " + f.getName());
				Long parentId = null;
				String contentType = null;
				String newPath = f.getAbsolutePath();
				String fileName = f.getName();

				if (f.isDirectory()) {
					logger.info(f.getAbsolutePath());

					contentType = "Folder";

					newPath = f.getPath().replaceAll("\\\\", "/");
					Library l = new Library();
					l.setContentName(f.getName());
					logger.info("newPath---->"+newPath);
					newPath = newPath.replace("/data/temp/", folderPathS3);
					logger.info("newPath--->"+newPath);
					l.setFolderPath(newPath);
					l.setFilePath(newPath);
					l.setContentType(contentType);
					l.setCreatedBy(username);
					l.setLastModifiedBy(username);
					l.setParentId(pId);
					l.setShareToSchools(shareToSchools);

					parentId = insertLibrary(l);
					readLibraryForZipS3(f, parentId, username,shareToSchools,folderPathS3);
				} else {

					logger.info(f.getPath());
					logger.info("READ LIBRARY ----------- " + f.getParent());
					contentType = "File";

					newPath = f.getPath().replaceAll("\\\\", "/");
					logger.info("newPath---->"+newPath);
					newPath = newPath.replace("/data/temp/", folderPathS3);
					Library l = new Library();
					l.setContentName(fileName);
					String parentPath = f.getParent().replaceAll("\\\\", "/");
					parentPath = parentPath.replace("/data/temp/", folderPathS3);
					logger.info("newPath--->"+newPath);
					logger.info("parentPath--->"+parentPath);
					l.setFolderPath(parentPath + "/");
					l.setFilePath(newPath);
					l.setContentType(contentType);
					l.setCreatedBy(username);
					l.setLastModifiedBy(username);
					l.setParentId(pId);
					l.setShareToSchools(shareToSchools);
					insertLibrary(l);

				}
			}
		}

	}

	public void readFolder(File dir) {
		File[] files = dir.listFiles();
		if (files.length != 0) {

			for (File f : files) {
				logger.info("File name is ------------------ " + f.getName());
				String contentType = null;
				String newPath = f.getAbsolutePath();
				String fileName = f.getName();

				if (f.isDirectory()) {
					logger.info(f.getAbsolutePath());

					contentType = "Folder";

					newPath = f.getAbsolutePath().trim()
							.replaceAll("\\\\", "/");

					readFolder(f);
				} else {

					logger.info(f.getAbsolutePath());
					logger.info("READ LIBRARY ----------- " + f.getParent());
					contentType = "File";

					newPath = f.getAbsolutePath().replaceAll("\\\\", "/");

				}
			}
		}

	}
	//Hiren
	//remove order by id after testing
	public Library findOneContent(String folderPath) {
		//String sql = "select id,createdBy,folderPath,contentDescription,contentName,filePath,linkUrl,parentId,active,contentType from library where  folderPath = ? order by id desc limit 1";
		String sql = "select id,createdBy,folderPath,contentDescription,contentName,filePath,linkUrl,parentId,active,contentType "
				+ " from library where  folderPath = ? and contentType = 'Folder' order by id desc limit 1";
		
		return findOneSQL(sql, new Object[] { folderPath });
	}
	//Hiren 27-11-2019
	public List<Library> getLibraryContentUserRights(Long libraryId,String username){
		String sql = "select u.username,u.firstname,u.lastname,lcsr.libraryId,lcsr.createOnly,lcsr.editOnly from users u "
				+ "left outer join library_content_shared_rights lcsr on u.username=lcsr.username and lcsr.libraryId = ? "
				+ "where u.username like '%_LIBRARIAN%' and u.enabled ='1' and u.username <> ? order by u.username";
		return findAllSQL(sql, new Object[] { libraryId, username});
	}
	//Hiren 27-11-2019
	public void insertLibraryContentUserRights(List<Library> library){
		
		final String sql = "INSERT INTO library_content_shared_rights ( libraryId, username, createOnly, editOnly, createdBy, createdDate, lastModifiedBy, lastModifiedDate) "
				+ "VALUES ( :libraryId, :username, :createOnly, :editOnly, :createdBy, :createdDate, "
				+ " :lastModifiedBy, :lastModifiedDate) ON DUPLICATE KEY UPDATE "
				+ " createOnly=:createOnly, editOnly=:editOnly, lastModifiedBy=:lastModifiedBy, lastModifiedDate=:lastModifiedDate ";
		
		SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(library
				.toArray());
		int[] updateCounts = getNamedParameterJdbcTemplate().batchUpdate(sql,
				batch);
		
	}
	//Hiren 27-11-2019
	public Library findOneContent(String contentName, String folderPath) {
		String sql = "select * from library where contentName = ? and folderPath = ? and active = 'Y' order by id desc limit 1";
		return findOneSQL(sql, new Object[] { contentName, folderPath });
	}
	//Hiren 27-11-2019
	public void updateShareToSchool(String schoolName,String filePath) {

		final String sql = "update library set shareToSchools = CONCAT(IFNULL(CONCAT(shareToSchools,','),''),?) where filePath like ? and (shareToSchools not like ? or shareToSchools is null)";
		getJdbcTemplate().update(sql,new Object[] { schoolName, filePath+"%", "%"+schoolName+"%"});

	}
	
	public List<Library> getLibraryContentRightsById(String id){
		String sql="select * from library_content_shared_rights where libraryId = ?";
		return findAllSQL(sql, new Object[] { id});
	}
	
	public List<Library> findLibraryByFileName(String filepath) {

		String sql = "SELECT * FROM library where filePath like ? AND active = 'Y'";
		return findAllSQL(sql, new Object[] { "%" + filepath + "%" });
	}
	
}
