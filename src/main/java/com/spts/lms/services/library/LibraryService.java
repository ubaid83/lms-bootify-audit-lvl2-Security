package com.spts.lms.services.library;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spts.lms.beans.announcement.Announcement;
import com.spts.lms.beans.content.Content;
import com.spts.lms.beans.library.Library;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.daos.library.LibraryDAO;
import com.spts.lms.helpers.PaginationHelper.Page;
import com.spts.lms.services.BaseService;

@Service("libraryService")
@Transactional
public class LibraryService extends BaseService<Library> {

	@Autowired
	private LibraryDAO libraryDAO;

	@Override
	public BaseDAO<Library> getDAO() {
		return libraryDAO;
	}

	public List<Library> getELibraries() {
		return libraryDAO.getELibraries();
	}

	public Library findOneContent(String folderPath) {
		return libraryDAO.findOneContent(folderPath);
	}

	/*
	 * //Pending logic for public List<Library> getContentUnderAPath(Content
	 * content) { final String sql =
	 * "Select * from Content where acadMonth = ? and acadYear = ?  and courseId = ? and folderPath like ? and active = 'Y' order by parentContentId"
	 * ;
	 * 
	 * List<Library> contentList = getDAO().findAllSQL(sql, new Object[]{
	 * content.getAcadMonth(), content.getAcadYear(), content.getCourseId(),
	 * content.getFolderPath()+"%" }); for (int i = 0; i < contentList.size();
	 * i++) {
	 * 
	 * Library c = contentList.get(i);
	 * 
	 * ArrayList<Content> children = getChildren(c.getId(), contentList); for
	 * (Content child : children) { contentList.remove(child); contentList.add(i
	 * + 1, child); } c.setChildrenList(children); }
	 * 
	 * return contentList; }
	 * 
	 * public List<Library> getContentUnderAFolder(Content content) { final
	 * String sql =
	 * "Select * from Content where acadMonth = ? and acadYear = ? " +
	 * " and courseId = ? and folderPath like ? and active = 'Y' order by parentContentId"
	 * ;
	 * 
	 * List<Library> contentList = getDAO().findAllSQL(sql, new Object[]{
	 * content.getAcadMonth(), content.getAcadYear(), content.getCourseId(),
	 * content.getFilePath()+"%" });
	 * 
	 * for (int i = 0; i < contentList.size(); i++) {
	 * 
	 * Content c = contentList.get(i);
	 * 
	 * ArrayList<Content> children = getChildren(c.getId(), contentList); for
	 * (Content child : children) { contentList.remove(child); contentList.add(i
	 * + 1, child); } c.setChildrenList(children); }
	 * 
	 * return contentList; }
	 */
	/*
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * public List<Library> getSharedFolders(String userName, Library content) {
	 * return contentDAO.getSharedFolders(userName, content); }
	 */
	//Hiren 27-11-2019
	public List<Library> getItemsUnderAPath(Library library) {
		final String sql = "Select id,createdBy,folderPath,contentDescription,contentName,filePath,linkUrl,parentId,active,contentType from library l where folderPath like ? and active = 'Y' order by  id,parentId";

		String newFolderPath = library.getFolderPath();
		if (!newFolderPath.endsWith("/")) {
			newFolderPath = (newFolderPath + "/%");
		}
		List<Library> allItems = getDAO().findAllSQL(sql,
				new Object[] { newFolderPath });

		/*for (int i = 0; i < allItems.size(); i++) {

			Library c = allItems.get(i);

			ArrayList<Library> children = getChildren(c.getId(), allItems);
			// System.out.println("children -------- " + children);
			for (Library child : children) {
				allItems.remove(child);
				allItems.add(i + 1, child);

			}

			c.setChildrenList(children);
		}*/
		createChildrenList(allItems);

		System.out.println("allItems ------ " + allItems);
		return allItems;
	}
	//Hiren 27-11-2019
	public List<Library> createChildrenList(List<Library> allItems){
		 
		for (int i = 0; i < allItems.size(); i++) {
	              Library l = allItems.get(i);
	              ArrayList<Library> children = getChildren(l.getId(), allItems);
	              l.setChildrenList(children);
	     }
		
		return allItems;
	}

	public List<Library> getFoldersUnderAPath(Library library) {
		final String sql = "Select id,createdBy,folderPath,contentDescription,contentName,filePath,linkUrl,parentId,active,contentType from library l where folderPath like ? and active = 'Y' and contentType = 'Folder' order by parentId";

		List<Library> allItems = getDAO().findAllSQL(sql,
				new Object[] { library.getFolderPath() + "/%" });
		for (int i = 0; i < allItems.size(); i++) {

			Library c = allItems.get(i);

			ArrayList<Library> children = getChildren(c.getId(), allItems);
			for (Library child : children) {
				allItems.remove(child);
				allItems.add(i + 1, child);
			}
			c.setChildrenList(children);
		}

		return allItems;
	}

	/*public List<Library> getParentFoldersUnderAPath(Library library) {
		final String sql = "Select id,createdBy,folderPath,contentDescription,contentName,filePath,linkUrl,parentId,active,contentType from library l where folderPath like ? and active = 'Y' and (parentId = '' or parentId is null) order by parentId,l.id desc";

		List<Library> allItems = getDAO().findAllSQL(sql,
				new Object[] { library.getFolderPath() + "%" });
		for (int i = 0; i < allItems.size(); i++) {

			Library c = allItems.get(i);

			ArrayList<Library> children = getChildren(c.getId(), allItems);
			for (Library child : children) {
				allItems.remove(child);
				allItems.add(i + 1, child);
			}
			c.setChildrenList(children);
		}

		return allItems;
	}*/
	//Hiren 27-11-2019
	public List<Library> getParentFoldersUnderAPath(Library library,String username) {
		final String sql = "Select l.id,l.createdBy,l.folderPath,l.contentDescription,l.contentName,l.filePath,l.linkUrl,l.parentId,l.active,l.contentType, "
				+ "lcsr.username,lcsr.createOnly,lcsr.editOnly from library l left outer join library_content_shared_rights lcsr on l.id=lcsr.libraryId "
				+ "and lcsr.username = ? "
				+ "where  l.active = 'Y' "
				+ "and (l.parentId = '' or l.parentId is null)";

		List<Library> allItems = getDAO().findAllSQL(sql,
				new Object[] {username });
		for (int i = 0; i < allItems.size(); i++) {

			Library c = allItems.get(i);

			ArrayList<Library> children = getChildren(c.getId(), allItems);
			for (Library child : children) {
				allItems.remove(child);
				allItems.add(i + 1, child);
			}
			c.setChildrenList(children);
		}

		return allItems;
	}
	//Hiren 27-11-2019
	public List<Library> getLibraryUnderAPath(Library library, String username,String parentId) {
		
		String libFolderPath = library.getFolderPath();
		if (libFolderPath.startsWith("/")) {

			libFolderPath = StringUtils.substring(libFolderPath, 1);

		}
		
		final String sql = "Select l.id,l.createdBy,l.folderPath,l.contentDescription,l.contentName,l.filePath,l.linkUrl,l.parentId,l.active,l.contentType, "
				+ "lcsr.username,lcsr.createOnly,lcsr.editOnly from library l left outer join library_content_shared_rights lcsr on l.id=lcsr.libraryId and lcsr.username = ? "
				+ "where folderPath like ? and parentId = ? and active = 'Y' order by folderPath";

		return findAllSQL(sql, new Object[] {username, "%" + libFolderPath + "%", parentId });
//		return findAllSQL(sql, new Object[] {username, library.getFolderPath() + "%", parentId });
	}

	public Library getParentFolders(Library library) {
		final String sql = "Select id,createdBy,folderPath,contentDescription,contentName,filePath,linkUrl,parentId,active,contentType from library l where folderPath = ? and active = 'Y'  order by parentId,l.id desc";

		return libraryDAO.findOneSQL(sql,
				new Object[] { library.getFolderPath() });
	}

	public List<Library> getParentFolder(Library library) {
		String sql = "Select id,createdBy,folderPath,contentDescription,contentName,filePath,linkUrl,parentId,active,contentType from library l where folderPath = ? and active = 'Y' ";
		// String countSql =
		// "Select * from library l where folderPath = ? and active = 'Y'  ";

		ArrayList<Object> parameters = new ArrayList<Object>();
		parameters.add(library.getFolderPath());
		String str = StringUtils.substringBeforeLast(library.getFolderPath(),
				"/");

		if (str != null && str.length() != 0) {
			// for (int i = 0; i < str.length(); i++) {
			sql = sql + " or folderPath = ? ";
			// countSql = countSql + " and contentName like ? ";
			parameters.add(str);
			// }
		}

		sql = sql + " order by parentId,l.id desc ";

		Object[] args = parameters.toArray();

		return findAllSQL(sql, args);
	}
	
	public List<Library> getParentFolder(Library library,String username) {
		String sql = "Select l.id,l.createdBy,l.folderPath,l.contentDescription,l.contentName,l.filePath,l.linkUrl,l.parentId,l.active,l.contentType,lcsr.username,lcsr.createOnly,lcsr.editOnly "
				+ "from library l left outer join library_content_shared_rights lcsr on l.id=lcsr.libraryId and lcsr.username = ? "
				+ "where l.folderPath = ? and l.active = 'Y' ";
		// String countSql =
		// "Select * from library l where folderPath = ? and active = 'Y'  ";

		ArrayList<Object> parameters = new ArrayList<Object>();
		parameters.add(username);
		parameters.add(library.getFolderPath());
		String str = StringUtils.substringBeforeLast(library.getFolderPath(),
				"/");

		if (str != null && str.length() != 0) {
			// for (int i = 0; i < str.length(); i++) {
			sql = sql + " or l.folderPath = ? ";
			// countSql = countSql + " and contentName like ? ";
			parameters.add(str);
			// }
		}

		sql = sql + "group by l.contentName order by l.parentId,l.id desc ";

		Object[] args = parameters.toArray();

		return findAllSQL(sql, args);
	}

	public ArrayList<Library> getChildren(Long parentId, List<Library> allItems) {
		ArrayList<Library> children = new ArrayList<Library>();
		for (Library item : allItems) {
			if (item.getParentId() != null
					&& item.getParentId().longValue() == parentId.longValue()) {
				children.add(item);
			}
		}

		return children;
	}

//	public void updateChildrenDetails(String newFolderPath, String oldFolderPath) {
//		libraryDAO.updateChildrenDetails(newFolderPath, oldFolderPath);
//	}
	//12-12-2020 for update
		public void updateChildrenDetails(String newFolderPath, String oldFolderPath,String oldFolderPathWOS) {
			libraryDAO.updateChildrenDetails(newFolderPath, oldFolderPath,oldFolderPathWOS);
		}
		//for delete
		public void updateChildrenDetails(String newFolderPath, String oldFolderPath) {
			libraryDAO.updateChildrenDetails(newFolderPath, oldFolderPath);
		}

	public void updateFolder(Library content) {
		libraryDAO.updateFolderDetails(content);

	}

	public void updateLink(Library content) {
		libraryDAO.updateLink(content);

	}

	public void updateFile(Library content) {
		libraryDAO.updateFile(content);

	}

	public void softDeleteFolder(Library content) {
		libraryDAO.softDeleteFolder(content);

	}

	public List<Library> findByParentId(Long parentId) {
		return libraryDAO.findByParentId(parentId);
	}

	/*
	 * public void readLibrary(File dir, String pId) { File[] files =
	 * dir.listFiles();
	 * 
	 * for (File f : files) { String parentId = null; String contentType = null;
	 * String newPath = f.getAbsolutePath(); String fileName = f.getName();
	 * 
	 * if (f.isDirectory()) { System.out.println(f.getAbsolutePath());
	 * 
	 * if (f.getName().contains("__xid")) { fileName = fileName.substring(0,
	 * fileName.length() - 12); System.out
	 * .println("fileName ---------------------------------- " + fileName); }
	 * 
	 * contentType = "Folder";
	 * 
	 * newPath = f.getAbsolutePath().replaceAll("\\\\", "/"); // newPath =
	 * newPath.substring(13); // newPath = newPath.substring(0, newPath.length()
	 * - 12); Library library = new Library();
	 * library.setContentName(f.getName()); library.setFolderPath(newPath);
	 * library.setFilePath(newPath); library.setContentType(contentType);
	 * library.setCreatedBy(username); library.set
	 * 
	 * parentId = insertLibrary(f.getName(), newPath, contentType, "CA", "CA",
	 * newPath, "", "", f.getName(), pId); parentId =
	 * libraryDAO.insert(library);
	 * 
	 * readLibrary(f, parentId); } else { if
	 * (!FilenameUtils.getExtension(f.getName()).equals("xml")) {
	 * 
	 * System.out.println(f.getAbsolutePath()); contentType = "File";
	 * 
	 * newPath = f.getAbsolutePath().replaceAll("\\\\", "/"); // newPath =
	 * newPath.substring(13); // newPath = newPath.substring(0, newPath.length()
	 * - 17);
	 * 
	 * System.out.println("newPath --------------------------- " + newPath);
	 * 
	 * 
	 * 
	 * if (f.getName().contains("__xid")) { fileName = fileName .substring(0,
	 * fileName.length() - 17); System.out
	 * .println("fileName ---------------------------------- " + fileName); }
	 * 
	 * 
	 * insertLibrary(fileName, newPath, contentType, "CA", "CA", newPath, "",
	 * "", fileName, pId);
	 * 
	 * } } }
	 * 
	 * }
	 */

	
	  public void readLibrary(File dir, Long pId, String username) { System.out
	  .println("Inside readLibrary-------------------------------------------------------------------"
	  ); libraryDAO.readLibrary(dir, pId, username); }
	 
	
	public void readLibraryS3(File dir, Long pId, String username,String libraryrRootPath,String destFolder) {
		System.out
				.println("Inside readLibrary-------------------------------------------------------------------");
		libraryDAO.readLibraryS3(dir, pId, username,libraryrRootPath,destFolder);
	}
	//Hiren
	public void readLibraryForZipS3(File dir, Long pId, String username,String shareToSchools,String folderPathS3) {
		System.out
				.println("Inside readLibrary-------------------------------------------------------------------");
		libraryDAO.readLibraryForZipS3(dir, pId, username,shareToSchools,folderPathS3);
	}
	//Hiren 27-11-2019
	public List<Library> getLibraryContentUserRights(Long libraryId,String username){
		return libraryDAO.getLibraryContentUserRights(libraryId,username);
	}
	//Hiren 27-11-2019
	public void insertLibraryContentUserRights(List<Library> library){
		libraryDAO.insertLibraryContentUserRights(library);
	}
	//Hiren 27-11-2019
	public List<Library> getItemsToShare(Library library) {
		final String sql = "Select id,createdBy,folderPath,contentDescription,contentName,filePath,linkUrl,parentId,active,contentType,shareToSchools from library l where filePath like ? and active = 'Y' order by  id,parentId";

		String newFilePath = library.getFilePath();
		newFilePath = (newFilePath + "%");
		
		List<Library> allItems = getDAO().findAllSQL(sql,
				new Object[] { newFilePath });

		createChildrenList(allItems);

		System.out.println("allItems ------ " + allItems);
		return allItems;
	}
	//Hiren 27-11-2019
	public Library findOneContent(String contentName,String folderPath) {
		return libraryDAO.findOneContent(contentName,folderPath);
	}
	//Hiren 27-11-2019
	public void updateShareToSchool(String schoolName,String filePath) {
		libraryDAO.updateShareToSchool(schoolName,filePath);
	}
	public List<Library> getLibraryContentRightsById(String id){
		return libraryDAO.getLibraryContentRightsById(id);
	}
	
	
	public List<Library> findLibraryByFileName(String filepath) {
		return libraryDAO.findLibraryByFileName(filepath);
	}
}
