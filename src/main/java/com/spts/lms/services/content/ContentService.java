package com.spts.lms.services.content;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spts.lms.beans.content.Content;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.daos.content.ContentDAO;
import com.spts.lms.services.BaseService;

@Service("contentService")
@Transactional
public class ContentService extends BaseService<Content> {

	@Autowired
	private ContentDAO contentDAO;

	@Override
	public BaseDAO<Content> getDAO() {
		return contentDAO;
	}

	// Pending logic for
/*	public List<Content> getContentUnderAPath(Content content) {
		String sql = "Select * from content  where " + " 1=1  ";

		if (content.getCourseId() != null) {
			sql = sql + " and courseId = ? ";
		}

		if (content.getCreatedBy() != null) {
			sql = sql + " and createdBy = ? ";
		}

		sql = sql
				+ "  and folderPath like ? and active = 'Y' order by parentContentId, id desc";

		ArrayList<Object> parameters = new ArrayList<Object>();

		if (content.getCourseId() != null) {
			parameters.add(content.getCourseId());
		}
		if (content.getCreatedBy() != null) {
			parameters.add(content.getCreatedBy());
		}

		parameters.add(content.getFolderPath() + "%");

		List<Content> contentList = getDAO().findAllSQL(sql,
				parameters.toArray());

		for (int i = 0; i < contentList.size(); i++) {

			Content c = contentList.get(i);

			ArrayList<Content> children = getChildren(c.getId(), contentList);
			for (Content child : children) {
				contentList.remove(child);
				contentList.add(i + 1, child);
			}
			c.setChildrenList(children);
		}

		return contentList;
	}*/

	
	public List<Content> getContentUnderAPath(Content content) {
        String sql = "Select * from content  where " + " 1=1  ";

        if (content.getCourseId() != null) {
                        sql = sql + " and courseId = ? and moduleId is null and parentContentId = ?";
        }

        if (content.getCreatedBy() != null) {
                        sql = sql + " and createdBy = ? ";
        }

        sql = sql
                                        + "  and folderPath like ? and active = 'Y' order by parentContentId, id desc";

        ArrayList<Object> parameters = new ArrayList<Object>();

        if (content.getCourseId() != null) {
                        parameters.add(content.getCourseId());
                        parameters.add(content.getParentContentId());
        }
        if (content.getCreatedBy() != null) {
                        parameters.add(content.getCreatedBy());
        }

        parameters.add(content.getFolderPath() + "%");

        List<Content> contentList = getDAO().findAllSQL(sql,
                                        parameters.toArray());

        for (int i = 0; i < contentList.size(); i++) {

                        Content c = contentList.get(i);

                        ArrayList<Content> children = getChildren(c.getId(), contentList);
                        for (Content child : children) {
                                        contentList.remove(child);
                                        contentList.add(i + 1, child);
                        }
                        c.setChildrenList(children);
        }

        return contentList;
}

	
	
/*	public List<Content> getContentUnderAPathContentName(Content content) {
		String sql = "Select * from content  where " + " 1=1  ";

		if (content.getCourseId() != null) {
			sql = sql + " and courseId = ? ";
		}

		if (content.getCreatedBy() != null) {
			sql = sql + " and createdBy = ? ";
		}

		
		 * if (content.getContentName() != null) { sql = sql +
		 * " and contentName = ? "; }
		 

		sql = sql
				+ "  and folderPath like ? and filePath like ? and active = 'Y' order by parentContentId, id desc";

		ArrayList<Object> parameters = new ArrayList<Object>();

		if (content.getCourseId() != null) {
			parameters.add(content.getCourseId());
		}
		if (content.getCreatedBy() != null) {
			parameters.add(content.getCreatedBy());
		}
		if (content.getContentName() != null) {
			parameters.add(content.getContentName());
		}

		parameters.add(content.getFolderPath() + "%");
		
		parameters.add(content.getFilePath() + "%");

		List<Content> contentList = getDAO().findAllSQL(sql,
				parameters.toArray());

		for (int i = 0; i < contentList.size(); i++) {

			Content c = contentList.get(i);

			ArrayList<Content> children = getChildren(c.getId(), contentList);
			for (Content child : children) {
				contentList.remove(child);
				contentList.add(i + 1, child);
			}
			c.setChildrenList(children);
		}

		return contentList;
	}*/

	
/*	public List<Content> getContentUnderAPathContentName(Content content) {
		String sql = "Select * from content  where " + " 1=1  ";

		if (content.getCourseId() != null) {
			sql = sql + " and courseId = ? ";
		}

		if (content.getCreatedBy() != null) {
			sql = sql + " and createdBy = ? ";
		}

		
		// if (content.getContentName() != null) { sql = sql +" and contentName = ? "; }
		 

		sql = sql
				+ "  and folderPath like ? or filePath like ? and active = 'Y' order by parentContentId, id desc";

		ArrayList<Object> parameters = new ArrayList<Object>();

		if (content.getCourseId() != null) {
			parameters.add(content.getCourseId());
		}
		if (content.getCreatedBy() != null) {
			parameters.add(content.getCreatedBy());
		}
		if (content.getContentName() != null) {
			parameters.add(content.getContentName());
		}

		parameters.add(content.getFolderPath() + "%");

		List<Content> contentList = getDAO().findAllSQL(sql,
				parameters.toArray());

		for (int i = 0; i < contentList.size(); i++) {

			Content c = contentList.get(i);

			ArrayList<Content> children = getChildren(c.getId(), contentList);
			for (Content child : children) {
				contentList.remove(child);
				contentList.add(i + 1, child);
			}
			c.setChildrenList(children);
		}

		return contentList;
	}*/
	
	
	/*public List<Content> getContentUnderAPathContentName(Content content) {
        String sql = "Select * from content  where " + " 1=1  ";

        if (content.getCourseId() != null) {
              sql = sql + " and courseId = ? ";
        }

        if (content.getCreatedBy() != null) {
              sql = sql + " and createdBy = ? ";
        }

        
        * if (content.getContentName() != null) { sql = sql +
        * " and contentName = ? "; }
        

        sql = sql
                    + "  and folderPath like ? and filePath like ? and active = 'Y' order by parentContentId, id desc";

        ArrayList<Object> parameters = new ArrayList<Object>();

        if (content.getCourseId() != null) {
              parameters.add(content.getCourseId());
        }
        if (content.getCreatedBy() != null) {
              parameters.add(content.getCreatedBy());
        }
        if (content.getContentName() != null) {
              parameters.add(content.getContentName());
        }

        parameters.add(content.getFolderPath() + "%");
        
        parameters.add(content.getFilePath() + "%");

        List<Content> contentList = getDAO().findAllSQL(sql,
                    parameters.toArray());

        for (int i = 0; i < contentList.size(); i++) {

              Content c = contentList.get(i);

              ArrayList<Content> children = getChildren(c.getId(), contentList);
              for (Content child : children) {
                    contentList.remove(child);
                    contentList.add(i + 1, child);
              }
              c.setChildrenList(children);
        }

        return contentList;
  }
*/
	
	public List<Content> getContentUnderAPathContentName(Content content) {
        String sql = "Select * from content  where " + " 1=1  ";

        if (content.getCourseId() != null) {
              sql = sql + " and courseId = ? ";
        }

        if (content.getCreatedBy() != null) {
              sql = sql + " and createdBy = ? ";
        }

        /*
        * if (content.getContentName() != null) { sql = sql +
        * " and contentName = ? "; }
        */
        if(content.getFilePath()!=null){
        	sql = sql + "and filePath like ? ";
        }

        sql = sql
                    + "  and folderPath like ? and active = 'Y' order by parentContentId, id desc";

        ArrayList<Object> parameters = new ArrayList<Object>();

        if (content.getCourseId() != null) {
              parameters.add(content.getCourseId());
        }
        if (content.getCreatedBy() != null) {
              parameters.add(content.getCreatedBy());
        }
        /*if (content.getContentName() != null) {
              parameters.add(content.getContentName());
        }*/

        if(content.getFilePath()!=null){
        	parameters.add(content.getFilePath() + "%");
        }
        parameters.add(content.getFolderPath() + "%");
        
        List<Content> contentList = getDAO().findAllSQL(sql,
                    parameters.toArray());
        
        createChildrenList(contentList);
       /*for (int i = 0; i < contentList.size(); i++) {
      
              Content c = contentList.get(i);

              ArrayList<Content> children = getChildren(c.getId(), contentList);
              for (Content child : children) {
            	  contentList.remove(child);
                           
                    //contentList.add(i + 1, child);
              }
              c.setChildrenList(children);
        }*/

        return contentList;
  }
	
	
	public List<Content> createChildrenList(List<Content> contentList){
		 
		for (int i = 0; i < contentList.size(); i++) {
	              Content c = contentList.get(i);
	              ArrayList<Content> children = getChildren(c.getId(), contentList);
	              c.setChildrenList(children);
	     }
		
		return contentList;
	}

	
	public List<Content> getContentUnderAFolder(Content content) {
		final String sql = "Select * from content where  "
				+ "  courseId = ? and folderPath like ? and active = 'Y' order by parentContentId";

		List<Content> contentList = getDAO().findAllSQL(
				sql,
				new Object[] { content.getCourseId(),
						content.getFilePath() + "%" });

		for (int i = 0; i < contentList.size(); i++) {

			Content c = contentList.get(i);

			ArrayList<Content> children = getChildren(c.getId(), contentList);
			for (Content child : children) {
				contentList.remove(child);
				contentList.add(i + 1, child);
			}
			c.setChildrenList(children);
		}

		return contentList;
	}

	public ArrayList<Content> getChildren(Long parentId,
			List<Content> contentList) {
		ArrayList<Content> children = new ArrayList<Content>();
		for (Content content : contentList) {
			if (content.getParentContentId() != null
					&& content.getParentContentId().longValue() == parentId
							.longValue()) {
				children.add(content);
			}
		}

		return children;
	}

	public void updateChildrenDetails(String newFolderPath, String oldFolderPath) {
		contentDAO.updateChildrenDetails(newFolderPath, oldFolderPath);
	}

	public void updateFolder(Content content) {
		contentDAO.updateFolderDetails(content);

	}

	public List<Content> getFoldersForFaculty(String facultyId) {
		return contentDAO.getFoldersForFaculty(facultyId);
	}

	public List<Content> findByCourse(Long courseId) {
		return contentDAO.findByCourse(courseId);
	}

	public List<Content> getFoldersByCourseForFaculty(String facultyId,
			Long courseId) {
		return contentDAO.getFoldersByCourseForFaculty(facultyId, courseId);
	}

	public void keepCount(Long id) {
		contentDAO.keepCount(id);
	}
	public void keepCount(Long id,int count) {
		contentDAO.keepCount(id, count);
	}

	public void updateLink(Content content) {
		contentDAO.updateLink(content);

	}

	public void updateFile(Content content) {
		contentDAO.updateFile(content);

	}

	public void softDeleteFolder(Content content) {
		contentDAO.softDeleteFolder(content);

	}
	
	public void softDeleteFolderForModule(Content content) {
		contentDAO.softDeleteFolderForModule(content);

	}

	public List<Content> getSharedFolders(String userName, String acadMonth,
			String acadYear) {
		return contentDAO.getSharedFolders(userName, acadMonth, acadYear);
	}

	public List<Content> getSharedFoldersByCourse(String userName,
			Long courseId, String acadMonth, String acadYear) {
		return contentDAO.getSharedFoldersByCourse(userName, courseId,
				acadMonth, acadYear);
	}

	public List<Content> findAllContentForFaculty(String acadMonth,
			String acadYear, String facultyId) {
		return contentDAO.findAllContentForFaculty(acadMonth, acadYear,
				facultyId);
	}

	public List<Content> findAllStudentContent(String acadMonth,
			String acadYear, String facultyId) {
		return contentDAO.findAllStudentContent(acadMonth, acadYear, facultyId);
	}

	public List<Content> findAllContent(String acadMonth, String acadYear) {
		return contentDAO.findAllContent(acadMonth, acadYear);
	}

	public List<Content> getSharedFolders(String userName) {
		return contentDAO.getSharedFolders(userName);
	}

	public List<Content> getSharedFoldersByCourse(String userName, Long courseId) {
		return contentDAO.getSharedFoldersByCourse(userName, courseId);
	}

	public List<Content> findContentsBySessionAndYearForProgram(

	String acadSession, Integer acadYear, Long programId) {

		return contentDAO.findContentsBySessionAndYearForProgram(acadSession,

		acadYear, programId);

	}

	public List<Content> findContentsBySessionAndYearForCollege(

	String acadSession, Integer acadYear) {

		return contentDAO.findContentsBySessionAndYearForCollege(acadSession,

		acadYear);

	}

	public List<Content> findAllConetntsByFaculty(String username) {

		return contentDAO.findAllConetntsByFaculty(username);

	}

	public List<Content> findAllContentForProgram(Long programId) {

		return contentDAO.findAllContentForProgram(programId);

	}

	public List<Content> findAllContentByUsernameAndCourseId(String username,
			String courseId) {

		return contentDAO.findAllContentByUsernameAndCourseId(username,
				courseId);
	}

	public List<Content> findAllContentByUsername(String username) {

		return contentDAO.findAllContentByUsername(username);
	}

	public List<Content> findAllContentByCourseId(String courseId) {

		return contentDAO.findAllContentByCourseId(courseId);
	}

	public List<Content> findAllContent() {
		return contentDAO.findAllContent();
	}

	public List<Content> getSharedFoldersAndFilesByCourse(String userName,
			Long courseId) {
		return contentDAO.getSharedFoldersAndFilesByCourse(userName, courseId);
	}

	public List<Content> getSharedFoldersAndFile(String username) {
		return contentDAO.getSharedFoldersAndFile(username);
	}

	public List<Content> findAllEveryoneContentForStudent(String userName) {
		return contentDAO.findAllEveryoneContentForStudent(userName);
	}

	public Content findOneContent(String contentName, String folderPath) {
		return contentDAO.findOneContent(contentName, folderPath);
	}

	public List<Content> findEveryoneContentByCourseId(Long courseId) {
		return contentDAO.findEveryoneContentByCourseId(courseId);
	}

	public List<Content> findEveryoneContentByUC(String username) {
		return contentDAO.findEveryoneContentByUC(username);
	}
	
	public List<Content> getContentByParentModuleId(Long id){
		 return contentDAO.getContentByParentModuleId(id);
	}
/*	public List<Content> getContentUnderAPathForModule(Content content) {
        String sql = "Select * from content  where " + " 1=1  ";

        if(content.getParentContentId() != null){
        if (content.getCourseId() != null) {
              sql = sql + " and courseId = ? and parentModuleId is null and parentContentId = ?";
        } else if(content.getModuleId() != null) {
              sql = sql + " and moduleId = ? and courseId is null and parentContentId = ?";
        }else{
              sql = sql + " and parentModuleId is null and parentContentId is null";
        }
        }else{
        	System.out.println("COURSEID=>"+content.getCourseId());
              if (content.getCourseId() != null) {
                    sql = sql + " and courseId = ? and parentModuleId is null and parentContentId is null ";
              } else if(content.getModuleId() != null) {
                    sql = sql + " and moduleId = ? and courseId is null and parentContentId is null";
              }else{	
                    sql = sql + " and parentModuleId is null and parentContentId is null";
              }
        }
        if (content.getCreatedBy() != null) {
              sql = sql + " and createdBy = ? ";
        }
        if (content.getAcadYear() != null) {
            sql = sql + " and acadYear = ? ";
        }

        sql = sql
                    + "  and folderPath like ? and active = 'Y' order by parentContentId, id desc";

        ArrayList<Object> parameters = new ArrayList<Object>();
        if(content.getParentContentId() != null){
        if (content.getCourseId() != null) {
              parameters.add(content.getCourseId());
              parameters.add(content.getParentContentId());
        } else if(content.getModuleId() != null) {
              parameters.add(content.getModuleId());
              parameters.add(content.getParentContentId());
        } 
        }else
        {if (content.getCourseId() != null) {
              parameters.add(content.getCourseId());
        } else if(content.getModuleId() != null) {
              parameters.add(content.getModuleId());
        } 
              
        }
        if (content.getCreatedBy() != null) {
              parameters.add(content.getCreatedBy());
        }
        if (content.getAcadYear() != null) {
        	parameters.add(content.getAcadYear());
        }

        parameters.add(content.getFolderPath() + "%");

        List<Content> contentList = getDAO().findAllSQL(sql,
                    parameters.toArray());

        for (int i = 0; i < contentList.size(); i++) {

              Content c = contentList.get(i);

              ArrayList<Content> children = getChildren(c.getId(), contentList);
              for (Content child : children) {
                    contentList.remove(child);
                    contentList.add(i + 1, child);
              }
              c.setChildrenList(children);
        }

        return contentList;
  }*/
	//Hiren 26-02-2020
	public List<Content> getContentUnderAPathForModule(Content content) {
        String sql = "Select * from content  where " + " 1=1  ";

        if(content.getParentContentId() != null){
        if (content.getCourseId() != null) {
              sql = sql + " and courseId = ? and parentModuleId is null and parentContentId = ?";
        } else if(content.getModuleId() != null) {
              sql = sql + " and moduleId = ? and courseId is null and parentContentId = ?";
        }else{
              sql = sql + " and parentModuleId is null and parentContentId is null";
        }
        }else{
        	System.out.println("COURSEID=>"+content.getCourseId());
              if (content.getCourseId() != null) {
                    sql = sql + " and courseId = ? and parentModuleId is null and parentContentId is null ";
              } else if(content.getModuleId() != null) {
                    sql = sql + " and moduleId = ? and courseId is null and parentContentId is null";
              }else{	
                    sql = sql + " and parentModuleId is null and parentContentId is null";
              }
        }
        if (content.getCreatedBy() != null) {
              sql = sql + " and createdBy = ? ";
        }
        if (content.getAcadYear() != null) {
            sql = sql + " and acadYear = ? ";
        }

        sql = sql + "  and folderPath like ? and active = 'Y' and contentType <> 'Private' order by parentContentId, id desc";
        //sql = sql + "  and active = 'Y' and contentType <> 'Private' order by parentContentId, id desc";
        ArrayList<Object> parameters = new ArrayList<Object>();
        if(content.getParentContentId() != null){
        if (content.getCourseId() != null) {
              parameters.add(content.getCourseId());
              parameters.add(content.getParentContentId());
        } else if(content.getModuleId() != null) {
              parameters.add(content.getModuleId());
              parameters.add(content.getParentContentId());
        } 
        }else
        {if (content.getCourseId() != null) {
              parameters.add(content.getCourseId());
        } else if(content.getModuleId() != null) {
              parameters.add(content.getModuleId());
        } 
              
        }
        if (content.getCreatedBy() != null) {
              parameters.add(content.getCreatedBy());
        }
        if (content.getAcadYear() != null) {
        	parameters.add(content.getAcadYear());
        }
        String folderPath = content.getFolderPath();
		if(folderPath.startsWith("/")) {
			folderPath = StringUtils.substring(folderPath, 1);
		}
        parameters.add("%"+folderPath + "%");

        List<Content> contentList = getDAO().findAllSQL(sql,
                    parameters.toArray());

        for (int i = 0; i < contentList.size(); i++) {

              Content c = contentList.get(i);

              ArrayList<Content> children = getChildren(c.getId(), contentList);
              for (Content child : children) {
                    contentList.remove(child);
                    contentList.add(i + 1, child);
              }
              c.setChildrenList(children);
        }

        return contentList;
  }
	
	public List<Content> getContentUnderAPathForStudent(Content content) {
        String sql = "Select * from content  where " + " 1=1  ";

        if (content.getCourseId() != null) {
    
        		//sql = sql + " and courseId = ?  and parentContentId = ? AND parentModuleId IS NULL";
        		if(content.getParentContentId() != null && content.getParentModuleId() == null || content.getModuleId().isEmpty())
        		{
        			sql = sql + " and courseId = ?  and parentContentId = ? AND parentModuleId IS NULL";
        		}
        		else 
        		{
        		
        			sql = sql + " and courseId = ?  and parentContentId = ? ";
        			//sql = sql + " and courseId = ?  and parentContentId = ? AND parentModuleId IS NULL";
        		}
        	/*else
        	{
        		sql = sql + " and courseId = ? and moduleId is null and parentContentId is null ";
        	}*/
        }

        if (content.getCreatedBy() != null) {
                        sql = sql + " and createdBy = ? ";
        }

     // sql = sql + "  and folderPath like ? and active = 'Y' and accessType <> 'Private' order by parentContentId, id desc";
        sql = sql + "  and active = 'Y' and accessType <> 'Private' order by parentContentId, id desc";
       
        ArrayList<Object> parameters = new ArrayList<Object>();

        if (content.getCourseId() != null) {
        	
        	if(content.getParentContentId() != null && content.getParentModuleId() == null || content.getModuleId().isEmpty())
        	{
        	   parameters.add(content.getCourseId());
               parameters.add(content.getParentContentId());
               
        	}else
        	{
        		parameters.add(content.getCourseId());
        		if(content.getParentModuleId()!=null || content.getParentModuleId().isEmpty()){
                parameters.add(content.getParentModuleId());
        		}else{
        			  parameters.add(content.getParentContentId());
        		}
        		}
        				
        }
        if (content.getCreatedBy() != null) {
                        parameters.add(content.getCreatedBy());
        }

		/* parameters.add(content.getFolderPath() + "%"); */

        List<Content> contentList = getDAO().findAllSQL(sql,
                                        parameters.toArray());

        for (int i = 0; i < contentList.size(); i++) {

                        Content c = contentList.get(i);

                        ArrayList<Content> children = getChildren(c.getId(), contentList);
                        for (Content child : children) {
                                        contentList.remove(child);
                                        contentList.add(i + 1, child);
                        }
                        c.setChildrenList(children);
        }

        return contentList;
}
	
	public void deleteSoftByParentModuleId(String id) {
		contentDAO.deleteSoftByParentModuleId(id);
	}
	
	public List<Content> findparentBychildId(Long moduleId)
	{
		return contentDAO.findparentBychildId(moduleId);
	}
	
	public List<Content> getSharedFoldersByCourseForModule(String userName, Long courseId ,String moduleId) {
		
		return contentDAO.getSharedFoldersByCourseForModule(userName, courseId,moduleId);
	}
	
	public Content findIdByCourseIdAndParentModuleId(String courseId, String parentModuleId)
	{
		 return contentDAO.findIdByCourseIdAndParentModuleId(courseId,parentModuleId);
	}
	public List<Content> findByParentModuleId(Long parentModuleId) {
		return contentDAO.findByParentModuleId(parentModuleId);
	}
	public List<Long> getIdByParentModuleId(Long id){
		 return contentDAO.getIdByParentModuleId(id);
		}
	public List<Content> findEveryoneContentByCourseIdPrentNull(Long courseId) {
		return contentDAO.findEveryoneContentByCourseIdPrentNull(courseId);
	}
	
	public List<Content> getContentUnderAPathForStudentForModule(Content content) {
        String sql = "Select * from content  where " + " 1=1  ";

        if (content.getModuleId()!= null) {
    
        		sql = sql + " and moduleId = ?  and parentContentId is null AND parentModuleId IS NULL";
        		if(content.getParentContentId() != null)
        		{
        			sql = sql + " and moduleId = ?  and parentContentId = ? AND parentModuleId IS NULL";
        		}
        	else
        	{
        		sql = sql + " and moduleId = ?  and parentContentId =? AND parentModuleId IS NULL";
        	}
        }

        if (content.getCreatedBy() != null) {
                        sql = sql + " and createdBy = ? ";
        }

        sql = sql
                                        + "  and folderPath like ? and active = 'Y' order by parentContentId, id desc";

        ArrayList<Object> parameters = new ArrayList<Object>();

        if (content.getModuleId() != null) {
        	parameters.add(content.getModuleId());
        	if(content.getParentContentId() != null)
        	{
        	   parameters.add(content.getModuleId());
               parameters.add(content.getParentContentId());
               
                if(content.getParentContentId()!=null)
                {
                	parameters.add(content.getModuleId());
                    parameters.add(content.getParentContentId());
                }
        	}/*else
        	{
        		parameters.add(content.getCourseId());
                parameters.add(content.getParentContentId());
        	}*/
        				
        }
        if (content.getCreatedBy() != null) {
                        parameters.add(content.getCreatedBy());
        }

        parameters.add(content.getFolderPath() + "%");

        List<Content> contentList = getDAO().findAllSQL(sql,
                                        parameters.toArray());

        for (int i = 0; i < contentList.size(); i++) {

                        Content c = contentList.get(i);

                        ArrayList<Content> children = getChildren(c.getId(), contentList);
                        for (Content child : children) {
                                        contentList.remove(child);
                                        contentList.add(i + 1, child);
                        }
                        c.setChildrenList(children);
        }

        return contentList;
}
	
	public Content findOneContentForModule(String contentName, String folderPath) {
	return contentDAO.findOneContentForModule(contentName, folderPath);
}
	
	public List<Content> getContentUnderAPathContentNameForModule(Content content) {
        String sql = "Select * from content  where " + " 1=1  and courseId is null";

        if (content.getModuleId() != null) {
              sql = sql + " and moduleId = ? ";
        }

        if (content.getCreatedBy() != null) {
              sql = sql + " and createdBy = ? ";
        }
        if(content.getFilePath() != null){
        	sql = sql + "and filePath like ? ";
        }
   

        sql = sql
                    + "  and folderPath like ? and active = 'Y' order by parentContentId, id desc";

        ArrayList<Object> parameters = new ArrayList<Object>();

        if (content.getModuleId() != null) {
              parameters.add(content.getModuleId());
        }
        if (content.getCreatedBy() != null) {
              parameters.add(content.getCreatedBy());
        }
        if(content.getFilePath() !=null ){
        	parameters.add(content.getFilePath() + "%");
        }
        

        parameters.add(content.getFolderPath() + "%");
        
       

        List<Content> contentList = getDAO().findAllSQL(sql,
                    parameters.toArray());
        
        createChildrenList(contentList);


        return contentList;
  }
	
	public List<Content> findParentAndChildContentForModule(){
		return contentDAO.findParentAndChildContentForModule();
	}
	
	public String getParentModuleIdById(Long id){
		return contentDAO.getParentModuleIdById(id);
	}
	public Content getStudentViewCountForParent(List<Long> parentModuleId){
		return contentDAO.getStudentViewCountForParent(parentModuleId);
	}
//	public List<Content> getItemsToShare(Content content) {
//		final String sql = "Select * from content where filePath like ? and active = 'Y' group by filePath order by id,parentContentId";
//
//		String newFilePath = content.getFilePath();
//		newFilePath = (newFilePath + "%");
//		
//		List<Content> allItems = getDAO().findAllSQL(sql,
//				new Object[] { newFilePath });
//
//		createChildrenList(allItems);
//
//		System.out.println("allItems ------ " + allItems);
//		return allItems;
//	}
	public List<Content> getItemsToShare(Content content,String courseId) {
		final String sql = "Select * from content where filePath like ? and courseId = ? and active = 'Y' group by filePath order by id,parentContentId";

		String newFilePath = content.getFilePath();
		newFilePath = (newFilePath + "%");
		
		List<Content> allItems = getDAO().findAllSQL(sql,
				new Object[] { newFilePath, courseId });

		createChildrenList(allItems);

		System.out.println("allItems ------ " + allItems);
		return allItems;
	}
	public Content findOneContentByCourseId(String contentName, String folderPath,String courseId, String facultyId) {
		return contentDAO.findOneContentByCourseId(contentName, folderPath,courseId, facultyId);
	}
//	public Content findOneContentByCourseId(String contentName, String folderPath,String courseId) {
//		return contentDAO.findOneContentByCourseId(contentName, folderPath,courseId);
//	}
	
	//Hiren 31-03-2020
	public void updateShareToSchool(String schoolName,String filePath) {
		contentDAO.updateShareToSchool(schoolName,filePath);
	}
	public List<Content> findContentByNameAndFilelPath(String contentName, String filePath) {
		return contentDAO.findContentByNameAndFilelPath(contentName, filePath);
	}
	public void deleteSoftByParentContentId(String id) {
		contentDAO.deleteSoftByParentContentId(id);
	}
	public List<Content> findContentByFileName(String filePath) {
		return contentDAO.findContentByFileName(filePath);
	}
	public List<Content> findAllByParentContentId(Content content) {
		return contentDAO.findAllByParentContentId(content);
	}
	
	public List<Content> findAllContentsByParams(String programId,String acadYear,String campusId){
		return contentDAO.findAllContentsByParams(programId, acadYear, campusId);
	}
	
	public List<Content> findByContentId(String contentId){
		return contentDAO.findByContentId(contentId);
	}
	public void updateAccessTypeById(String accessType, String contentId){
		contentDAO.updateAccessTypeById(accessType, contentId);
	}
	
	public List<Content> findOneContentForMultipleCourse(String contentName, String folderPath) {
		return contentDAO.findOneContentForMultipleCourse(contentName,folderPath);
	}
}
