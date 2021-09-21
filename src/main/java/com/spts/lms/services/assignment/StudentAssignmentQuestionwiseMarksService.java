package com.spts.lms.services.assignment;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spts.lms.beans.assignment.StudentAssignmentQuestionwiseMarks;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.daos.assignment.StudentAssignmentQuestionwiseMarksDAO;
import com.spts.lms.services.BaseService;

@Service("studentAssignmentQuestionwiseMarksService")
@Transactional
public class StudentAssignmentQuestionwiseMarksService  extends BaseService<StudentAssignmentQuestionwiseMarks>{

	@Autowired
	StudentAssignmentQuestionwiseMarksDAO studentAssignmentQuestionwiseMarksDao;
	
	@Override
	protected BaseDAO<StudentAssignmentQuestionwiseMarks> getDAO() {
		// TODO Auto-generated method stub
		return studentAssignmentQuestionwiseMarksDao;
	}

	public List<StudentAssignmentQuestionwiseMarks> getStudentsMarksQuestionwise(long assignmentId){
		return studentAssignmentQuestionwiseMarksDao.getStudentsMarksQuestionwise(assignmentId);
	}
}
