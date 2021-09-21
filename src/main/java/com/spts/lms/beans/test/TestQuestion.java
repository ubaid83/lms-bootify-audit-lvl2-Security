package com.spts.lms.beans.test;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;

import com.spts.lms.beans.BaseBean;



public class TestQuestion extends BaseBean {

	private static final long serialVersionUID = 1067122965896106688L;

	private String active;

	private String correctOption;

	private String description;
	
	private String type;

	
	//Changes By Akshay From Integer To Double
	
	private Double marks;

	//End
	
	
	private String option1;

	private String option2;

	private String option3;

	private String option4;

	private String option5;

	private String option6;

	private String option7;

	private String option8;

	private Long testId;
	
	private String testTypeInTestQuestion;
	
	private String campusName;
	private Long campusId;
	
	
	//Added on extract field By Akshay
	
	private String optionShuffle;
	private String questionType;
	private String answerRangeFrom;
	private String answerRangeTo;
	private String testPoolId;
	private String attempts;
	private String correctAnswerNum;
	
	//End
	
	

	public String getCampusName() {
		return campusName;
	}

	public String getCorrectAnswerNum() {
		return correctAnswerNum;
	}

	public void setCorrectAnswerNum(String correctAnswerNum) {
		this.correctAnswerNum = correctAnswerNum;
	}

	public String getAttempts() {
		return attempts;
	}

	public void setAttempts(String attempts) {
		this.attempts = attempts;
	}

	public String getQuestionType() {
		return questionType;
	}

	public void setQuestionType(String questionType) {
		this.questionType = questionType;
	}

	public String getAnswerRangeFrom() {
		return answerRangeFrom;
	}

	public void setAnswerRangeFrom(String answerRangeFrom) {
		this.answerRangeFrom = answerRangeFrom;
	}

	public String getAnswerRangeTo() {
		return answerRangeTo;
	}

	public void setAnswerRangeTo(String answerRangeTo) {
		this.answerRangeTo = answerRangeTo;
	}

	public String getTestPoolId() {
		return testPoolId;
	}

	public void setTestPoolId(String testPoolId) {
		this.testPoolId = testPoolId;
	}

	public String getOptionShuffle() {
		return optionShuffle;
	}

	public void setOptionShuffle(String optionShuffle) {
		this.optionShuffle = optionShuffle;
	}

	public void setCampusName(String campusName) {
		this.campusName = campusName;
	}

	public Long getCampusId() {
		return campusId;
	}

	public void setCampusId(Long campusId) {
		this.campusId = campusId;
	}
	

	/**
	 * Non persistent fields
	 */
	private String[] correctOptions;
	private StudentQuestionResponse studentQuestionResponse = new StudentQuestionResponse();
	/**
	 * 
	 */

	
	
	
	public TestQuestion() {
	}

	public String getTestTypeInTestQuestion() {
		return testTypeInTestQuestion;
	}

	public void setTestTypeInTestQuestion(String testTypeInTestQuestion) {
		this.testTypeInTestQuestion = testTypeInTestQuestion;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public String getCorrectOption() {
		return correctOption;
	}

	public void setCorrectOption(String correctOption) {
		if(null != correctOption)
			correctOptions = correctOption.split(",");
		this.correctOption = correctOption;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getMarks() {
		return marks;
	}

	public void setMarks(Double marks) {
		this.marks = marks;
	}

	public String getOption1() {
		return option1;
	}

	public void setOption1(String option1) {
		this.option1 = option1;
	}

	public String getOption2() {
		return option2;
	}

	public void setOption2(String option2) {
		this.option2 = option2;
	}

	public String getOption3() {
		return option3;
	}

	public void setOption3(String option3) {
		this.option3 = option3;
	}

	public String getOption4() {
		return option4;
	}

	public void setOption4(String option4) {
		this.option4 = option4;
	}

	public String getOption5() {
		return option5;
	}

	public void setOption5(String option5) {
		this.option5 = option5;
	}

	public String getOption6() {
		return option6;
	}

	public void setOption6(String option6) {
		this.option6 = option6;
	}

	public String getOption7() {
		return option7;
	}

	public void setOption7(String option7) {
		this.option7 = option7;
	}

	public String getOption8() {
		return option8;
	}

	public void setOption8(String option8) {
		this.option8 = option8;
	}

	public Long getTestId() {
		return testId;
	}

	public void setTestId(Long testId) {
		this.testId = testId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String[] getCorrectOptions() {
		return correctOptions;
	}

	public void setCorrectOptions(String[] correctOptions) {
		this.correctOptions = correctOptions;
		if(null != correctOptions)
			correctOption = StringUtils.join(correctOptions, ',');
	}

	

	@Override
	public String toString() {
		return "TestQuestion [active=" + active + ", correctOption="
				+ correctOption + ", description=" + description + ", type="
				+ type + ", marks=" + marks + ", option1=" + option1
				+ ", option2=" + option2 + ", option3=" + option3
				+ ", option4=" + option4 + ", option5=" + option5
				+ ", option6=" + option6 + ", option7=" + option7
				+ ", option8=" + option8 + ", testId=" + testId
				+ ", testTypeInTestQuestion=" + testTypeInTestQuestion
				+ ", campusName=" + campusName + ", campusId=" + campusId
				+ ", correctOptions=" + Arrays.toString(correctOptions)
				+ ", studentQuestionResponse=" + studentQuestionResponse + "]";
	}

	public StudentQuestionResponse getStudentQuestionResponse() {
		return studentQuestionResponse;
	}

	public void setStudentQuestionResponse(StudentQuestionResponse studentQuestionResponse) {
		this.studentQuestionResponse = studentQuestionResponse;
		if(null != studentQuestionResponse)
			this.studentQuestionResponse.setQuestionId(getId());
	}

	public String getAnswer() {
		return studentQuestionResponse.getAnswer();
	}

	public void setAnswer(String answer) {
		studentQuestionResponse.setAnswer(answer);
	}
	
	public String[] getAnswers() {
		return studentQuestionResponse.getAnswers();
	}

	public void setAnswers(String[] answers) {
		studentQuestionResponse.setAnswers(answers);
	}

	public Long getStudentTestId() {
		return studentQuestionResponse.getStudentTestId();
	}

	public void setStudentTestId(Long studentTestId) {
		studentQuestionResponse.setStudentTestId(studentTestId);
	}
	
	public Double getStudentMarks() {
		return studentQuestionResponse.getMarks();
	}

	public void setStudentMarks(Double studentMarks) {
		studentQuestionResponse.setMarks(studentMarks);
	}
	
	public String getUsername(){
		return studentQuestionResponse.getUsername();
	}
	
	public void setUsername(String username){
		studentQuestionResponse.setUsername(username);
	}
	
	public String getQuestionId(){
		return studentQuestionResponse.getUsername();
	}
	
	public void setQuestionId(long questtionId){
		studentQuestionResponse.setQuestionId(questtionId);
	}

}