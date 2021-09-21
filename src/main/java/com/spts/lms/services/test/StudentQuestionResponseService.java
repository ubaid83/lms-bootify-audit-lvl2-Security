package com.spts.lms.services.test;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spts.lms.beans.test.StudentQuestionResponse;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.daos.test.StudentQuestionResponseDAO;
import com.spts.lms.services.BaseService;

@Service("studentQuestionResponseService")
@Transactional
public class StudentQuestionResponseService extends
		BaseService<StudentQuestionResponse> {

	@Autowired
	private StudentQuestionResponseDAO studentQuestionResponseDAO;

	@Override
	protected BaseDAO<StudentQuestionResponse> getDAO() {
		return studentQuestionResponseDAO;
	}

	public List<StudentQuestionResponse> findByStudentTestId(String studTestId) {
		return studentQuestionResponseDAO.findByStudentTestId(studTestId);
	}

	public StudentQuestionResponse findByStudentUsernameAndTestQuestnId(
			String username, String questnId) {
		return studentQuestionResponseDAO.findByStudentUsernameAndTestQuestnId(
				username, questnId);
	}

	public List<StudentQuestionResponse> findByQId(String qId) {
		return studentQuestionResponseDAO.findByQId(qId);
	}

	public StudentQuestionResponse findTotalAnsweredQueByStudentTestId(
			String studTestId) {
		return studentQuestionResponseDAO
				.findTotalAnsweredQueByStudentTestId(studTestId);
	}
	
	public StudentQuestionResponse findTotalAnsweredTempQueByStudentTestId(
			String studTestId) {
		return studentQuestionResponseDAO
				.findTotalAnsweredTempQueByStudentTestId(studTestId);
	}

	public int deleteFacultyTestResponse(String testId, String username) {
		return studentQuestionResponseDAO.deleteFacultyTestResponse(testId,
				username);
	}

	public int insertIntoResponseTemp(StudentQuestionResponse bean) {
		return studentQuestionResponseDAO.insertIntoResponseTemp(bean);
	}

	public int[] insertBatchIntoTemp(final List<StudentQuestionResponse> beans)

	{
		return studentQuestionResponseDAO.insertBatchIntoTemp(beans);
	}

	public List<StudentQuestionResponse> findByStudentTestIdTemp(
			String studTestId) {
		return studentQuestionResponseDAO.findByStudentTestIdTemp(studTestId);
	}

	public int[] deleteResponseTempBatch(
			final List<StudentQuestionResponse> beans) {

		return studentQuestionResponseDAO.deleteResponseTempBatch(beans);
	}

	public List<StudentQuestionResponse> findByQIdNew(String qId) {
		return studentQuestionResponseDAO.findByQIdNew(qId);
	}
	public void updateStudentQuesRespMarks(Double marks,Long id){
        studentQuestionResponseDAO.updateStudentQuesRespMarks(marks,id);
  }

}
