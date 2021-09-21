package com.spts.lms.beans.user;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.spts.lms.beans.BaseBean;
import com.spts.lms.beans.course.Course;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FacultyDetails extends BaseBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		return "FacultyDetails [username=" + username + ", firstName="
				+ firstName + ", lastName=" + lastName + ", imagePath="
				+ imagePath + ", imagePreviewPath=" + imagePreviewPath
				+ ", courseId=" + courseId + ", experience=" + experience
				+ ", overview=" + overview + ", age=" + age + ", designation="
				+ designation + ", dob=" + dob + ", address=" + address
				+ ", mobile=" + mobile + ", email=" + email + ", course="
				+ course + "]";
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	private String username;

	private String firstName;

	private String lastName;

	private String imagePath;

	private String imagePreviewPath;
	private String position;

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getImagePreviewPath() {
		return imagePreviewPath;
	}

	public void setImagePreviewPath(String imagePreviewPath) {
		this.imagePreviewPath = imagePreviewPath;
	}

	private Long courseId;

	private String experience;

	private String overview;

	private Integer age;

	private String designation;

	private String dob;

	private String address;

	private String mobile;

	private String email;

	Course course = new Course();

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Long getCourseId() {
		return courseId;
	}

	public String getCourseName() {
		return course.getCourseName();
	}

	public void setCourseName(String courseName) {
		course.setCourseName(courseName);
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}

	public String getExperience() {
		return experience;
	}

	public void setExperience(String experience) {
		this.experience = experience;
	}

	public String getOverview() {
		return overview;
	}

	public void setOverview(String overview) {
		this.overview = overview;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

}
