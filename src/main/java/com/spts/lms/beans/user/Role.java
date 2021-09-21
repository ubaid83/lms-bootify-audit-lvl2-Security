package com.spts.lms.beans.user;

import org.springframework.security.core.GrantedAuthority;

/**
 * The persistent class for the roles database table.
 * 
 */
public enum Role implements GrantedAuthority {
	ROLE_USER, ROLE_STUDENT, ROLE_FACULTY, ROLE_ADMIN, ROLE_PARENT, ROLE_CORD, ROLE_AREA_INCHARGE, ROLE_AR, ROLE_DEAN, ROLE_HOD, ROLE_COUNSELOR, ROLE_STAFF, ROLE_LIBRARIAN, ROLE_EXAM, ROLE_SUPPORT_ADMIN, ROLE_EXAM_ADMIN, ROLE_IT, ROLE_SUPPORT_ADMIN_REPORT,ROLE_INTL;

	public String getAuthority() {
		return this.toString();
	}

}