package com.spts.lms.beans.assignment;

public class ResultDomain {

	public int totalStringLength;
	public double matching;
	public int noOfMatches;
	public int blankLines;
	public String firstFile;
	public String secondFile;

	private String matchingFor80to90;
	private String assignmentName;
	private String username1;
	private String lastName1;
	private String firstName1;
	private String program1;
	private String username2;
	private String lastName2;
	private String firstName2;
	private String program2;
	private String centerName1;

	private String centerName2;
	private int maxConseutiveLinesMatched;
	private int numberOfLinesInFirstFile;
	private int numberOfLinesInSecondFile;

	public int getNumberOfLinesInFirstFile() {
		return numberOfLinesInFirstFile;
	}

	public void setNumberOfLinesInFirstFile(int numberOfLinesInFirstFile) {
		this.numberOfLinesInFirstFile = numberOfLinesInFirstFile;
	}

	public int getNumberOfLinesInSecondFile() {
		return numberOfLinesInSecondFile;
	}

	public void setNumberOfLinesInSecondFile(int numberOfLinesInSecondFile) {
		this.numberOfLinesInSecondFile = numberOfLinesInSecondFile;
	}

	public int getMaxConseutiveLinesMatched() {
		return maxConseutiveLinesMatched;
	}

	public void setMaxConseutiveLinesMatched(int maxConseutiveLinesMatched) {
		this.maxConseutiveLinesMatched = maxConseutiveLinesMatched;
	}

	public String getCenterName1() {
		return centerName1;
	}

	public void setCenterName1(String centerName1) {
		this.centerName1 = centerName1;
	}

	public String getCenterName2() {
		return centerName2;
	}

	public void setCenterName2(String centerName2) {
		this.centerName2 = centerName2;
	}

	public String getAssignmentName() {
		return assignmentName;
	}

	public void setAssignmentName(String assignmentName) {
		this.assignmentName = assignmentName;
	}

	public String getLastName1() {
		return lastName1;
	}

	public void setLastName1(String lastName1) {
		this.lastName1 = lastName1;
	}

	public String getFirstName1() {
		return firstName1;
	}

	public void setFirstName1(String firstName1) {
		this.firstName1 = firstName1;
	}

	public String getProgram1() {
		return program1;
	}

	public void setProgram1(String program1) {
		this.program1 = program1;
	}

	public String getUsername1() {
		return username1;
	}

	public void setUsername1(String username1) {
		this.username1 = username1;
	}

	public String getUsername2() {
		return username2;
	}

	public void setUsername2(String username2) {
		this.username2 = username2;
	}

	public String getLastName2() {
		return lastName2;
	}

	public void setLastName2(String lastName2) {
		this.lastName2 = lastName2;
	}

	public String getFirstName2() {
		return firstName2;
	}

	public void setFirstName2(String firstName2) {
		this.firstName2 = firstName2;
	}

	public String getProgram2() {
		return program2;
	}

	public void setProgram2(String program2) {
		this.program2 = program2;
	}

	public String getFirstFile() {
		return firstFile;
	}

	public void setFirstFile(String firstFile) {
		this.firstFile = firstFile;
	}

	public String getSecondFile() {
		return secondFile;
	}

	public void setSecondFile(String secondFile) {
		this.secondFile = secondFile;
	}

	public int getBlankLines() {
		return blankLines;
	}

	public void setBlankLines(int blankLines) {
		this.blankLines = blankLines;
	}

	public int getTotalStringLength() {
		return totalStringLength;
	}

	public void setTotalStringLength(int totalStringLength) {
		this.totalStringLength = totalStringLength;
	}

	public double getMatching() {
		return matching;
	}

	public void setMatching(double matching) {
		this.matching = matching;
	}

	public int getNoOfMatches() {
		return noOfMatches;
	}

	public String getMatchingFor80to90() {
		return matchingFor80to90;
	}

	public void setMatchingFor80to90(String matchingFor80to90) {
		this.matchingFor80to90 = matchingFor80to90;
	}

	public void setNoOfMatches(int noOfMatches) {
		this.noOfMatches = noOfMatches;
	}

	@Override
	public String toString() {
		return "ResultDomain [totalStringLength=" + totalStringLength
				+ ", matching=" + matching + ", noOfMatches=" + noOfMatches
				+ ", blankLines=" + blankLines + ", firstFile=" + firstFile
				+ ", secondFile=" + secondFile + ", matchingFor80to90="
				+ matchingFor80to90 + ", assignmentName=" + assignmentName + ", username1="
				+ username1 + ", lastName1=" + lastName1 + ", firstName1="
				+ firstName1 + ", program1=" + program1 + ", username2="
				+ username2 + ", lastName2=" + lastName2 + ", firstName2="
				+ firstName2 + ", program2=" + program2 + ", centerName1="
				+ centerName1 + ", centerName2=" + centerName2
				+ ", maxConseutiveLinesMatched=" + maxConseutiveLinesMatched
				+ ", numberOfLinesInFirstFile=" + numberOfLinesInFirstFile
				+ ", numberOfLinesInSecondFile=" + numberOfLinesInSecondFile
				+ "]";
	}

}
