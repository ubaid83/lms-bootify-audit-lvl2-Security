<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@page import="java.util.*"%>
<%@page import="com.spts.lms.beans.dashboard.DashBoard"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%
	String activeMenu = (String) request.getParameter("activeMenu");
	String dashboard = "";
	String messageMenuClass = "";
	String calendarMenuClass = "";
	String grievanceMenuClass = "";
	String quicklinkMenuClass = "";
%>


<%
	if (activeMenu != null) {

		if ("Message".equals(activeMenu)) {
			messageMenuClass = "on";
		}

		if ("Calendar".equals(activeMenu)) {
			calendarMenuClass = "on";
		}

		if ("Grievance".equals(activeMenu)) {
			grievanceMenuClass = "on";
		}

		if ("Quicklink".equals(activeMenu)) {
			quicklinkMenuClass = "on";
		}
	} else {
		dashboard = "on";
	}
%>


<li class="top_right_menu">
	<button type="button" class="navbar-toggle collapsed"
		data-toggle="collapse" data-target="#navbar" aria-expanded="false"
		aria-controls="navbar">
		<span class="icon-bar"></span> <span class="icon-bar"></span> <span
			class="icon-bar"></span>
	</button>

	<div id="navbar" class="navbar-collapse collapse">
		<ul class="nav navbar-nav">




			<!-- Student Menu -->


			<li class="active"><a href="<c:url value="homepage" />"
				class="<%=dashboard%>"><i class="fa fa-line-chart"></i>Dashboard</a></li>
			<li><a href="<c:url value="visitExamApp" />"
				target="_blank"><i class="fa fa-desktop"></i>Practical Exam</a></li>


			<sec:authorize access="hasRole('ROLE_STUDENT')">

				<li class="dropdown menu_scrollbar"><a href="#"
					class="dropdown-toggle" data-toggle="dropdown" role="button"
					aria-haspopup="true" aria-expanded="false"> <i
						class="fa fa-wpforms"></i> My Courses
				</a>



					<ul class="dropdown-menu">


						<c:forEach items="${courseDetailList}" var="courseDetail">
							<li><a href="viewCourse?id=${courseDetail.course.id}">${courseDetail.course.courseName}</a></li>
						</c:forEach>




					</ul></li>

			</sec:authorize>

			<li class="dropdown menu_scrollbar"><a href="#"
				class="dropdown-toggle" data-toggle="dropdown" role="button"
				aria-haspopup="true" aria-expanded="false"> <i
					class="fa fa-wpforms"></i> Library
			</a>



				<ul class="dropdown-menu">


					<li><a href="viewLibraryAnnouncements">View Library</a></li>

					<!-- <li><a href="searchAnnouncementForLibrarian">View Library
                                                Announcement</a></li>
-->

				</ul></li>


			<sec:authorize access="hasRole('ROLE_STUDENT')">

				<li class="dropdown"><a href="#"
					class="dropdown-toggle<%=messageMenuClass%>" data-toggle="dropdown"
					role="button" aria-haspopup="true" aria-expanded="false"> <i
						class="fa fa-comment-o"></i> Message
				</a>
					<ul class="dropdown-menu">
						<li><a href="<c:url value="createMessageForm" />">Create
								Message</a></li>
						<li><a href="<c:url value="viewMyMessage" />">View
								Message </a></li>
					</ul></li>

			</sec:authorize>
			<%-- 
							<sec:authorize access="hasRole('ROLE_STUDENT')">

								<li class="dropdown"><a href="#" class="dropdown-toggle<%=messageMenuClass%>"
									data-toggle="dropdown" role="button" aria-haspopup="true"
									aria-expanded="false"> <i class="fa fa-comment-o"></i>
										Attendance</a>
									<ul class="dropdown-menu">
										 <li><a href="<c:url value="viewDailyAttendance" />">View 
												Attendance</a></li>
										
									</ul></li>

							</sec:authorize> --%>

			<li class="dropdown"><a href="#"
				class="dropdown-toggle<%=calendarMenuClass%>" data-toggle="dropdown"
				role="button" aria-haspopup="true" aria-expanded="false"> <i
					class="fa fa-calendar"></i> Calendar
			</a>
				<ul class="dropdown-menu">
					<li><a href="<c:url value="viewEvents" />">View Event</a></li>
				</ul></li>


			<sec:authorize access="hasRole('ROLE_STUDENT')">

				<li class="dropdown"><a href="#"
					class="dropdown-toggle<%=grievanceMenuClass%>"
					data-toggle="dropdown" role="button" aria-haspopup="true"
					aria-expanded="false"> <i class="fa fa-support"></i> Support
				</a>
					<ul class="dropdown-menu">
						<%-- <li><a href="<c:url value="grievanceForm" />">Raise
								Grievance </a></li> --%>

						<li><a
							href="<c:url value="overview?courseId=${course.id}" />">Overview
						</a></li>
						<%-- <li><a href="<c:url value="viewGrievanceResponse" />">Preview
								Grievances Response List </a></li> --%>
						<li><a href="<c:url value="createQueryForm" />">Raise A
								Query </a></li>
						<li><a href="<c:url value="viewQueryResponse" />">View My
								Query Respone </a></li>
						<li><a href="<c:url value="viewFAQ" />">View FAQs </a></li>
					</ul></li>

			</sec:authorize>


			<sec:authorize access="hasRole('ROLE_STUDENT')">
				<li class="dropdown"><a href="#"
					class="dropdown-toggle<%=quicklinkMenuClass%>"
					data-toggle="dropdown" role="button" aria-haspopup="true"
					aria-expanded="false"> <img
						src="resources/images/quicklink.png" height="30%" alt="Quicklink"
						style="border-radius: 10px; height: 26px; width: 23px; padding: 1px;">Quicklinks

				</a>
					<ul class="dropdown-menu" role="menu">
						<li><a href="<c:url value="viewFeedbackDetails" />">View
								Feedback</a></li>


						<%-- <li><a href="<c:url value="http://192.168.2.116:8080/plagcheck/checkFileForm" />">Plagiarism Check
								</a></li> --%>
						<%--  <li><a href="<c:url value="http://localhost:8009/checkFileForm" />">Plagiarism Check
								</a></li> --%>
						<%-- <li><a href="<c:url value="viewRtasAttendance" />">View RTAS Attendance</a></li> --%>
						<li><a href="<c:url value="viewDailyAttendanceByStudent" />">View
								Daily Attendance</a></li>

						<li><a href="<c:url value="gradeWieghtage" />">View Grade
								Wieghtage</a></li>
						<li><a href="<c:url value="testList" />">View Test</a></li>

						<li><a href="<c:url value="createForumForm" />">Create
								Forum</a></li>
						<%-- <li><a href="<c:url value="viewLibrary" />">Non-Course
								Content</a></li>
 --%>
						<li><a href="<c:url value="searchAssignmentTestForm" />">Search
								All Assignments/Tests </a></li>
						<li><a href="http://ezproxy.svkm.ac.in:2048/login">Library
								Link</a></li>

					</ul></li>
				<%-- <li class="dropdown"><a href="#"
                        class="dropdown-toggle" data-toggle="dropdown" role="button"
                        aria-haspopup="true" aria-expanded="false"> <i
                              class="fa fa-universal-access"></i>Student Services
                  </a>



                        <ul class="dropdown-menu" role="menu">
                              <li><a href="<c:url value="viewServices" />">Services
                                    </a></li>

                              

                        

                        </ul></li> --%>


			</sec:authorize>


		</ul>
	</div>
</li>


