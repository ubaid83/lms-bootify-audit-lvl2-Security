package com.spts.lms.beans.calender;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.spts.lms.beans.BaseBean;
import com.spts.lms.beans.course.Course;
import com.spts.lms.web.controllers.AssignmentController;

public class Calender extends BaseBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String username;
	public String event;
	public String startDate;
	public String endDate;
	public String description;
	public String courseId;
	public String active;

	public String start;
	public String end;
	public String startDateTFormat;
	public String endDateTFormat;
	public String title;
	public String showStatus;

	private StudentEvent studentEvent = new StudentEvent();

	private List<StudentEvent> studentEvents = new ArrayList<StudentEvent>();

	private List<String> students = new ArrayList<String>();

	private Course courses = new Course();
	private static final Logger logger = Logger.getLogger(Calender.class);
	static SimpleDateFormat parseFormat = new SimpleDateFormat(
			"yyyy-MM-dd hh:mm:ss.S");
	static SimpleDateFormat sdf = new SimpleDateFormat(
			"EEE MMM dd  yyyy HH:mm:ss 'GMT'Z '('z')'", Locale.ENGLISH);

	static SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
	public static final long HOUR = 3600 * 1000;
	public String getShowStatus() {
		return showStatus;
	}

	public void setShowStatus(String showStatus) {
		this.showStatus = showStatus;
	}

	public String getStart() {

		return start;
	}

	public void setStart(String start) {
		this.start = start;

		try {
			Date date = parseFormat.parse(start);
			start = sdf.format(date);
			this.start = "new Date(" + date.getTime() + ")";
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public String getEnd() {
		return end;
	}

	@Override
	public String toString() {

		return "{\"event\":\"" + event + "\", \"description\":\"" + description

		+ "\", \"start\":" + start + ", \"end\":" + end

		+ ",\"startDate\":\"" + startDate + "\",\"endDate\":\"" + endDate
				+ "\", \"title\":\"" + title + "\", \"course\":\"" + courseId

				+ "\", \"id\":\"" + getId() + "\",\"showStatus\":\""
				+ showStatus + "\"}";

	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	/*
	 * public void setStudents(List<String> students) { this.students =
	 * students; for (String username : students) { StudentTest studentTest =
	 * new StudentTest(); studentTest.setUsername(username);
	 * studentTest.setTestId(getId()); studentTests.add(studentTest); } }
	 */

	/*
	 * public void setEnd(String end) { this.end = end;
	 * 
	 * try { Date date = parseFormat.parse(end); end = sdf.format(date);
	 * this.end = "new Date(" + date.getTime() + ")"; ; } catch (Exception e) {
	 * // TODO Auto-generated catch block logger.error("Exception", e); }
	 * 
	 * }
	 */

	public void setEnd(String end) {
		this.end = end;

		try {
			Date date = parseFormat.parse(end);
			System.out.println("date is--->" + date);
			Date newDate = new Date(date.getTime() + 24 * HOUR);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			int hours = calendar.get(Calendar.HOUR_OF_DAY);
			System.out.println("hours is -->" + hours);
			end = sdf.format(date);
			System.out.println("end -->" + end);
			System.out.println("new Date -->" + newDate);
			if (hours == 0) {
				System.out.println("12 or 00 time added");
				this.end = "new Date(" + newDate.getTime() + ")";
			}

			else {
				this.end = "new Date(" + date.getTime() + ")";
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("Exception", e);
		}

	}

	public StudentEvent getStudentEvent() {
		return studentEvent;
	}

	public void setStudentEvent(StudentEvent studentEvent) {
		this.studentEvent = studentEvent;
	}

	public List<StudentEvent> getStudentEvents() {
		return studentEvents;
	}

	public void setStudentEvents(List<StudentEvent> studentEvents) {
		this.studentEvents = studentEvents;
	}

	public List<String> getStudents() {
		return students;
	}

	public void setStudents(List<String> students) {
		this.students = students;
		for (String username : students) {
			StudentEvent studentEvent = new StudentEvent();
			studentEvent.setUsername(username);
			studentEvent.setEventId(String.valueOf(getId()));
			studentEvents.add(studentEvent);
		}
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@JsonIgnore
	private Course course = new Course();

	public Course getCourses() {
		return course;
	}

	public void setCourses(Course course) {
		this.course = course;
	}

	public String getCourseName() {
		return courses.getCourseName();
	}

	public void setCourseName(String courseName) {
		course.setCourseName(courseName);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDateToFormat(String startDate) {

		// this.startDate = startDate;
		try {
			Date date = parseFormat.parse(startDate);
			startDate = sdf2.format(date);
			this.startDate = startDate;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("Exception", e);
		}

	}

	public String getEndDate() {
		return endDate;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public void setEndDateToFormat(String endDate) {

		// this.endDate = endDate;

		try {
			Date date = parseFormat.parse(endDate);
			endDate = sdf2.format(date);
			this.endDate = endDate;
			;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("Exception", e);
		}

	}

	public String getDescription() {
		return description;
	}

	public void setStartDate(String startDate) {

		this.startDate = startDate;

		/*
		 * try { Date date = parseFormat.parse(startDate); startDate =
		 * sdf2.format(date); this.startDate = startDate;
		 * 
		 * } catch (ParseException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 */
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;

		/*
		 * try { Date date = parseFormat.parse(endDate); endDate =
		 * sdf2.format(date); this.endDate = endDate;
		 * 
		 * 
		 * } catch (ParseException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 */
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public String getStartDateTFormat() {
		return startDateTFormat;
	}

	public void setStartDateTFormat(String startDateTFormat) {
		// this.startDateTFormat = startDateTFormat;

		try {
			Date date = parseFormat.parse(startDateTFormat);
			startDateTFormat = sdf2.format(date);
			this.startDateTFormat = startDateTFormat;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("Exception", e);
		}

	}

	public String getEndDateTFormat() {
		return endDateTFormat;
	}

	public void setEndDateTFormat(String endDateTFormat) {

		try {
			Date date = parseFormat.parse(endDateTFormat);
			endDateTFormat = sdf2.format(date);
			this.endDateTFormat = endDateTFormat;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("Exception", e);
		}

	}

	/*
	 * public String getStatus() { return status; }
	 * 
	 * public void setStatus(String status) { this.status = status; }
	 */

}
