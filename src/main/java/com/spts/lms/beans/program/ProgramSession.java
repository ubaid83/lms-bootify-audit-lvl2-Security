package com.spts.lms.beans.program;

import java.io.Serializable;
import java.util.Set;

import com.spts.lms.beans.BaseBean;


/**
 * The persistent class for the program_session database table.
 * 
 */
public class ProgramSession extends BaseBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private short sessionNumber;

	private Long programId;

	//bi-directional many-to-one association to Program
	private Program program;
	
	private String active;

	//bi-directional many-to-one association to ProgramSessionCourse
	private Set<ProgramSessionCourse> programSessionCourses;

	public ProgramSession() {
	}

	public short getSessionNumber() {
		return this.sessionNumber;
	}

	public void setSessionNumber(short sessionNumber) {
		this.sessionNumber = sessionNumber;
	}

	public Long getProgramId() {
		return programId;
	}

	public void setProgramId(Long programId) {
		this.programId = programId;
	}

	public Program getProgram() {
		return this.program;
	}

	public void setProgram(Program program) {
		this.program = program;
	}

	public Set<ProgramSessionCourse> getProgramSessionCourses() {
		return this.programSessionCourses;
	}

	public void setProgramSessionCourses(Set<ProgramSessionCourse> programSessionCourses) {
		this.programSessionCourses = programSessionCourses;
	}

	public ProgramSessionCourse addProgramspecializationsessionCours(ProgramSessionCourse programspecializationsessionCours) {
		getProgramSessionCourses().add(programspecializationsessionCours);
	//	programspecializationsessionCours.setProgramSessionId(this);

		return programspecializationsessionCours;
	}

	public ProgramSessionCourse removeProgramspecializationsessionCours(ProgramSessionCourse programspecializationsessionCours) {
		getProgramSessionCourses().remove(programspecializationsessionCours);
	//	programspecializationsessionCours.setProgramSession(null);

		return programspecializationsessionCours;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	@Override
	public String toString() {
		return "ProgramSession [sessionNumber=" + sessionNumber
				+ ", programId=" + programId + ", program=" + program.getId()
				+ ", active=" + active + "]";
	}

}