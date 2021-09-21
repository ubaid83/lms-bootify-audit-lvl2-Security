package com.spts.lms.beans.test;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;

import com.spts.lms.beans.BaseBean;


/**
 * The persistent class for the student_question_response database table.
 * 
 */
public class StudentQuestionResponseAudit extends BaseBean {
	private static final long serialVersionUID = 1L;

	private String answer;

	
	//Changes By Akshay From Integer To Double
	private Double marks;
	
	//End
	
	
	private Long questionId;
	
	private Long studentTestId;
	
	private String username;
	
	/**
	 * Non persistent fields
	 */
	private String[] answers;
	/**
	 * 
	 */
	
	private String attempts;
	
	public StudentQuestionResponseAudit() {
	}

	public String getAttempts() {
		return attempts;
	}

	public void setAttempts(String attempts) {
		this.attempts = attempts;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
		if(null != answer)
			answers = answer.split(",");
	}

	public Double getMarks() {
		return marks;
	}

	public void setMarks(Double marks) {
		this.marks = marks;
	}

	public Long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Long questionId) {
		this.questionId = questionId;
	}

	public Long getStudentTestId() {
		return studentTestId;
	}

	public void setStudentTestId(Long studentTestId) {
		this.studentTestId = studentTestId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String[] getAnswers() {
		return answers;
	}

	public void setAnswers(String[] answers) {
		this.answers = answers;
		if(null != answers)
			answer = StringUtils.join(answers, ',');
	}

	@Override
	public String toString() {
		return "StudentQuestionResponseAudit [answer=" + answer + ", marks="
				+ marks + ", questionId=" + questionId + ", studentTestId="
				+ studentTestId + ", username=" + username + ", answers="
				+ Arrays.toString(answers) + ", attempts=" + attempts + "]";
	}
}