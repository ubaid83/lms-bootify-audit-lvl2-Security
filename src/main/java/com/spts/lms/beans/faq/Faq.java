package com.spts.lms.beans.faq;

import java.io.Serializable;

import com.spts.lms.beans.BaseBean;

public class Faq extends BaseBean implements Serializable{
	
	private String question;
	private String answer;
	private String questionType;
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public String getQuestionType() {
		return questionType;
	}
	public void setQuestionType(String questionType) {
		this.questionType = questionType;
	}
	@Override
	public String toString() {
		return "Faq [question=" + question + ", answer=" + answer
				+ ", questionType=" + questionType + "]";
	}
	
	

}
