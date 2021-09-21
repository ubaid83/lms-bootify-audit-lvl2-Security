package com.spts.lms.beans.InfraKeys;

import com.spts.lms.beans.BaseBean;

public class InfraKeys extends BaseBean {
	private static final long serialVersionUID = 1L;

	private String secret_token;
	private String public_key;
	private String private_key;
	private String abbr;
	private String slug;

	public String getAbbr() {
		return abbr;
	}

	public void setAbbr(String abbr) {
		this.abbr = abbr;
	}

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	public String getSecret_token() {
		return secret_token;
	}

	public void setSecret_token(String secret_token) {
		this.secret_token = secret_token;
	}

	public String getPublic_key() {
		return public_key;
	}

	public void setPublic_key(String public_key) {
		this.public_key = public_key;
	}

	public String getPrivate_key() {
		return private_key;
	}

	public void setPrivate_key(String private_key) {
		this.private_key = private_key;
	}

	@Override
	public String toString() {
		return "InfraKeys [secret_token=" + secret_token + ", public_key=" + public_key + ", private_key=" + private_key
				+ ", abbr=" + abbr + ", slug=" + slug + "]";
	}

}
