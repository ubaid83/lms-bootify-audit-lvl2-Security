package com.spts.lms.beans.student;

import com.spts.lms.beans.BaseBean;
import com.spts.lms.beans.program.Program;

public class StudentProgramSession extends BaseBean {

	private static final long serialVersionUID = -775013177467916264L;

	private String username;
	private Long programId;
	private Integer session;
	private String acadMonth;
	private Integer acadYear;
	
	private Program program;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Long getProgramId() {
		if(null != program)
			return program.getId();
		return programId;
	}

	public void setProgramId(Long programId) {
		if(null == program)
			program = new Program();
		program.setId(programId);
		this.programId = programId;
	}

	public Integer getSession() {
		return session;
	}

	public void setSession(Integer session) {
		this.session = session;
	}

	public String getAcadMonth() {
		return acadMonth;
	}

	public void setAcadMonth(String acadMonth) {
		this.acadMonth = acadMonth;
	}

	public Integer getAcadYear() {
		return acadYear;
	}

	public void setAcadYear(Integer acadYear) {
		this.acadYear = acadYear;
	}
	
	public Program getProgram() {
		return this.program;
	}

	public void setProgram(Program program) {
		this.program = program;
	}

	@Override
	public String toString() {
		return "StudentProgramSession [username=" + username + ", programId="
				+ programId + ", session=" + session + ", acadMonth="
				+ acadMonth + ", acadYear=" + acadYear + "]";
	}
}
