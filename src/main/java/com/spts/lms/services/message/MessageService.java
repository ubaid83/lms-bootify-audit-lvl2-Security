package com.spts.lms.services.message;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;




import com.spts.lms.beans.grievances.Grievances;
import com.spts.lms.beans.message.Message;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.daos.message.MessageDAO;
import com.spts.lms.services.BaseService;


@Service("messageService")
public class MessageService extends BaseService<Message>{
	
@Autowired 
private MessageDAO messageDAO;

@Override
public BaseDAO<Message> getDAO() {
	return messageDAO;
}


public List<Message> getReceivedMessage(String userName) {

    return messageDAO.getReceivedMessage(userName);

}



}
