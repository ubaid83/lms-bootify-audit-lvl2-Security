package com.spts.lms.beans.test;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;

import com.spts.lms.beans.BaseBean;

public class TestQuestionPools extends BaseBean{
	
	private static final long serialVersionUID = 1067122965896106688L;
	private String active;

	private String correctOption;

	private String description;
	
	private String type;
	private String testType;

	
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

	private Long testPoolId;
	
	private String testTypeInTestQuestion;
	
	private String campusName;
	private Long campusId;
	
	
	//Added on extract field By Akshay
	
	private String optionShuffle;
	
	private String testQuestionId;
	
	
	private String questionType;
	private String answerRangeFrom;
	private String answerRangeTo;
	
	
	private String[] correctOptions;
	private String correctAnswerNum;
	
	private String assignmentQuestionId;

	
	public String getAssignmentQuestionId() {
		return assignmentQuestionId;
	}


	public void setAssignmentQuestionId(String assignmentQuestionId) {
		this.assignmentQuestionId = assignmentQuestionId;
	}


	public String getCorrectAnswerNum() {
		return correctAnswerNum;
	}


	public void setCorrectAnswerNum(String correctAnswerNum) {
		this.correctAnswerNum = correctAnswerNum;
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


	public String getTestQuestionId() {
		return testQuestionId;
	}


	public void setTestQuestionId(String testQuestionId) {
		this.testQuestionId = testQuestionId;
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
	
	public String[] getCorrectOptions() {
		return correctOptions;
	}

	public void setCorrectOptions(String[] correctOptions) {
		this.correctOptions = correctOptions;
		if(null != correctOptions)
			correctOption = StringUtils.join(correctOptions, ',');
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public String getTestType() {
		return testType;
	}


	public void setTestType(String testType) {
		this.testType = testType;
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


	public Long getTestPoolId() {
		return testPoolId;
	}


	public void setTestPoolId(Long testPoolId) {
		this.testPoolId = testPoolId;
	}


	public String getTestTypeInTestQuestion() {
		return testTypeInTestQuestion;
	}


	public void setTestTypeInTestQuestion(String testTypeInTestQuestion) {
		this.testTypeInTestQuestion = testTypeInTestQuestion;
	}


	public String getCampusName() {
		return campusName;
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


	public String getOptionShuffle() {
		return optionShuffle;
	}


	public void setOptionShuffle(String optionShuffle) {
		this.optionShuffle = optionShuffle;
	}


	@Override
	public String toString() {
		return "TestQuestionPools [active=" + active + ", correctOption="
				+ correctOption + ", description=" + description + ", type="
				+ type + ", testType=" + testType + ", marks=" + marks
				+ ", option1=" + option1 + ", option2=" + option2
				+ ", option3=" + option3 + ", option4=" + option4
				+ ", option5=" + option5 + ", option6=" + option6
				+ ", option7=" + option7 + ", option8=" + option8
				+ ", testPoolId=" + testPoolId + ", testTypeInTestQuestion="
				+ testTypeInTestQuestion + ", campusName=" + campusName
				+ ", campusId=" + campusId + ", optionShuffle=" + optionShuffle
				+ ", testQuestionId=" + testQuestionId + ", questionType="
				+ questionType + ", answerRangeFrom=" + answerRangeFrom
				+ ", answerRangeTo=" + answerRangeTo + ", correctOptions="
				+ Arrays.toString(correctOptions) + ", correctAnswerNum="
				+ correctAnswerNum + "]";
	}
	
	

}
