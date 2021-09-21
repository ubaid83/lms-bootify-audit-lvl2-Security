package com.spts.lms.beans.ica;

import java.util.Date;

import com.spts.lms.beans.BaseBean;

public class NonCreditIcaModule extends BaseBean {

	private String module_id;
	private String acadyear;
	private String end_date;
	private String sem_total;
	private String oral_total;
	private String prac_total;
	private String start_date;
	private String module_abbr;
	private String session_code;
	private String ica_weightage;
	private String module_credit;
	private String internal_total;
	private String oral_prac_total;
	private String term_work_total;
	private String module_object_id;
	private String module_description;
	private String term_end_weightage;
	private String createdBy;
	private Date createdDate;
	private String lastModifiedBy;
	private Date lastModifiedDate;
	private String program_id;
	private String module_descipline_code;
	private String module_descipline_name;
	private String module_category_code;
	private String module_category_name;

	public String getModule_id() {
		return module_id;
	}

	public void setModule_id(String module_id) {
		this.module_id = module_id;
	}

	public String getAcadyear() {
		return acadyear;
	}

	public void setAcadyear(String acadyear) {
		this.acadyear = acadyear;
	}

	public String getEnd_date() {
		return end_date;
	}

	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}

	public String getSem_total() {
		return sem_total;
	}

	public void setSem_total(String sem_total) {
		this.sem_total = sem_total;
	}

	public String getOral_total() {
		return oral_total;
	}

	public void setOral_total(String oral_total) {
		this.oral_total = oral_total;
	}

	public String getPrac_total() {
		return prac_total;
	}

	public void setPrac_total(String prac_total) {
		this.prac_total = prac_total;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getStart_date() {
		return start_date;
	}

	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}

	public String getModule_abbr() {
		return module_abbr;
	}

	public void setModule_abbr(String module_abbr) {
		this.module_abbr = module_abbr;
	}

	public String getSession_code() {
		return session_code;
	}

	public void setSession_code(String session_code) {
		this.session_code = session_code;
	}

	public String getIca_weightage() {
		return ica_weightage;
	}

	public void setIca_weightage(String ica_weightage) {
		this.ica_weightage = ica_weightage;
	}

	public String getModule_credit() {
		return module_credit;
	}

	public void setModule_credit(String module_credit) {
		this.module_credit = module_credit;
	}

	public String getInternal_total() {
		return internal_total;
	}

	public void setInternal_total(String internal_total) {
		this.internal_total = internal_total;
	}

	public String getOral_prac_total() {
		return oral_prac_total;
	}

	public void setOral_prac_total(String oral_prac_total) {
		this.oral_prac_total = oral_prac_total;
	}

	public String getTerm_work_total() {
		return term_work_total;
	}

	public void setTerm_work_total(String term_work_total) {
		this.term_work_total = term_work_total;
	}

	public String getModule_object_id() {
		return module_object_id;
	}

	public void setModule_object_id(String module_object_id) {
		this.module_object_id = module_object_id;
	}

	public String getModule_description() {
		return module_description;
	}

	public void setModule_description(String module_description) {
		this.module_description = module_description;
	}

	public String getTerm_end_weightage() {
		return term_end_weightage;
	}

	public void setTerm_end_weightage(String term_end_weightage) {
		this.term_end_weightage = term_end_weightage;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	

	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	
	public String getProgram_id() {
		return program_id;
	}

	public void setProgram_id(String program_id) {
		this.program_id = program_id;
	}

	public String getModule_descipline_code() {
		return module_descipline_code;
	}

	public void setModule_descipline_code(String module_descipline_code) {
		this.module_descipline_code = module_descipline_code;
	}

	public String getModule_descipline_name() {
		return module_descipline_name;
	}

	public void setModule_descipline_name(String module_descipline_name) {
		this.module_descipline_name = module_descipline_name;
	}

	public String getModule_category_code() {
		return module_category_code;
	}

	public void setModule_category_code(String module_category_code) {
		this.module_category_code = module_category_code;
	}

	public String getModule_category_name() {
		return module_category_name;
	}

	public void setModule_category_name(String module_category_name) {
		this.module_category_name = module_category_name;
	}

	@Override
	public String toString() {
		return "NonCreditModuleBean [module_id=" + module_id + ", acadyear="
				+ acadyear + ", end_date=" + end_date + ", sem_total="
				+ sem_total + ", oral_total=" + oral_total + ", prac_total="
				+ prac_total + ", start_date=" + start_date + ", module_abbr="
				+ module_abbr + ", session_code=" + session_code
				+ ", ica_weightage=" + ica_weightage + ", module_credit="
				+ module_credit + ", internal_total=" + internal_total
				+ ", oral_prac_total=" + oral_prac_total + ", term_work_total="
				+ term_work_total + ", module_object_id=" + module_object_id
				+ ", module_description=" + module_description
				+ ", term_end_weightage=" + term_end_weightage + ", createdBy="
				+ createdBy + ", createdDate=" + createdDate
				+ ", lastModifiedBy=" + lastModifiedBy + ", lastModifiedDate="
				+ lastModifiedDate + ", program_id=" + program_id
				+ ", module_descipline_code=" + module_descipline_code
				+ ", module_descipline_name=" + module_descipline_name
				+ ", module_category_code=" + module_category_code
				+ ", module_category_name=" + module_category_name + "]";
	}

}
