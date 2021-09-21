package com.spts.lms.web.helper;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.spts.lms.web.controllers.ContentController;
import com.spts.lms.web.utils.Utils;

@Service
public class InsertLibrary {

	@Value("${database.url}")
	private String databaseURL;
	@Value("${database.password}")
	private String password;
	@Value("${database.username}")
	private String databaseusername;
	private static final Logger logger = Logger.getLogger(InsertLibrary.class);

	// static String libraryName = "MPSTM Library ";

	public String insertLibrary(String metatag, String folderPath,
			String contentType, String createdBy, String lastModifiedBy,
			String filePath, String linkUrl, String contentDescription,
			String contentName, String parentId) {
		String id = null;
		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String strDate = dateFormat.format(date);
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://10.25.10.50:3306/lms_mpstme_nm_m", databaseusername, password);
			Statement stmt = con.createStatement();

			String query = "INSERT INTO library (`createdBy`, `createdDate`, `lastModifiedBy`, `lastModifiedDate`, `folderPath`, `contentDescription`, `contentName`, `filePath`, `linkUrl`, `parentId`, `active`, `contentType`) values ('"
					+ createdBy
					+ "', '"
					+ strDate
					+ "', '"
					+ lastModifiedBy
					+ "', '"
					+ strDate
					+ "', '"
					+ folderPath
					+ "', '"
					+ contentDescription
					+ "', '"
					+ contentName
					+ "', '"
					+ filePath
					+ "', '"
					+ linkUrl
					+ "', '"
					+ parentId
					+ "', '"
					+ "Y" + "', '" + contentType + "')";
			stmt.executeUpdate(query);
			ResultSet rs = stmt
					.executeQuery("select id from library where folderPath = "
							+ "'" + folderPath + "'");
			while (rs.next()) {
				id = rs.getString(1);
			}

			con.close();
		} catch (Exception e) {
			logger.error(e);
		}
		return id;
	}

	/*
	 * public void main(String args[]) { String filePath1 =
	 * "E:/LibraryApp/data/JDSOLA/"; File currentDir = new File(filePath1);
	 * readLibrary(currentDir, "");
	 * 
	 * }
	 */
	public void readLibrary(File dir, String pId, String username) {
		File[] files = dir.listFiles();

		for (File f : files) {
			String parentId = null;
			String contentType = null;
			String newPath = f.getAbsolutePath();
			String fileName = f.getName();

			if (f.isDirectory()) {
				
				/*
				 * if (f.getName().contains("__xid")) { fileName =
				 * fileName.substring(0, fileName.length() - 12); System.out
				 * .println("fileName ---------------------------------- " +
				 * fileName); }
				 */
				contentType = "Folder";

				newPath = f.getAbsolutePath().replaceAll("\\\\", "/");
				// newPath = newPath.substring(13);
				// newPath = newPath.substring(0, newPath.length() - 12);

				parentId = insertLibrary(f.getName(), newPath, contentType,
						username, username, newPath, "", "", f.getName(), pId);
				readLibrary(f, parentId, username);
			} else {
				if (!FilenameUtils.getExtension(f.getName()).equals("xml")) {

				
					contentType = "File";

					newPath = f.getAbsolutePath().replaceAll("\\\\", "/");
					// newPath = newPath.substring(13);
					// newPath = newPath.substring(0, newPath.length() - 17);
					/*
					 * System.out.println("newPath --------------------------- "
					 * + newPath);
					 */

					/*
					 * if (f.getName().contains("__xid")) { fileName = fileName
					 * .substring(0, fileName.length() - 17); System.out
					 * .println("fileName ---------------------------------- " +
					 * fileName); }
					 */

					insertLibrary(fileName, newPath, contentType, username,
							username, newPath, "", "", fileName, pId);

				}
			}
		}

	}
}
