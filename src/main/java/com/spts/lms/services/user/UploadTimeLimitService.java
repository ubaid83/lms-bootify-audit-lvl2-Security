package com.spts.lms.services.user;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



import com.spts.lms.beans.user.UploadTimeLimitSession;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.daos.user.UploadTimeLimitSessionDao;
import com.spts.lms.services.BaseService;
@Service("uploadTimeLimitService")
public class UploadTimeLimitService extends BaseService<UploadTimeLimitSession>{
	
@Autowired UploadTimeLimitSessionDao  uploadTimeLimitSessionDao;

@Override
protected BaseDAO<UploadTimeLimitSession> getDAO() {
	// TODO Auto-generated method stub
	//return uploadTimeLimitSessionDao;
	return null;
}


public void insertTimeLimit(List<UploadTimeLimitSession> TimeLimitList) {
	uploadTimeLimitSessionDao.insertBatch(TimeLimitList);
}


}
