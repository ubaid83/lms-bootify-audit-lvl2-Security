<%@page import="java.util.*"%>
<%@page import="com.spts.lms.beans.dashboard.DashBoard"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>


<%-- <!doctype html>
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=0, minimal-ui" />

<!-- <title>Svkm's NMIMS dcemed to be University</title> -->
<title>${webPage.title}</title>

<link rel="shortcut icon" href="favicon.ico">
<link rel="icon" type="image/png" href="favicon.png">

<jsp:include page="headerCss.jsp" />
<!-- Bootstrap -->
<link href="<c:url value="/resources/css/bootstrap.min.css" />"
	rel="stylesheet">
<!-- Font Awesome -->
<link href="<c:url value="/resources/css/font-awesome.min.css" />"
	rel="stylesheet">
<!-- bootstrap-progressbar -->
<link
	href="<c:url value="/resources/css/bootstrap-progressbar-3.3.4.min.css" /> "
	rel="stylesheet">


<!-- Custom Toggle Style -->
<link href=" <c:url value="/resources/css/toggle.css" />"
	rel="stylesheet">

<!-- Custom menu Style -->
<link href=" <c:url value="/resources/css/menu.css" />" rel="stylesheet">


<!-- Custom Theme Style -->
<link href="<c:url value="/resources/css/custom.css" /> "
	rel="stylesheet">
<link href="<c:url value="/resources/css/responsive.css" />"
	rel="stylesheet">

<script type="text/javascript">
	var headerCourseId = null;
	function theFunction(obj) {

		return true;
	}
</script>

</head>

<body class="nav-md ">


	<div class="container body">
		<div class="main_container">

     
     <jsp:include page="headerSidebar.jsp" />

			
 --%>

<!-- top navigation -->
<div class="top_nav">
	<div class="nav_menu">
		<nav>

			<div class="navbar nav_title" style="border: 0;">
				<a href="<c:url value="/homepage" />"><img
					src="<c:url value="/resources/images/logo.gif" />" alt=""></a>
			</div>

			<div class="nav toggle">
				<a id="menu_toggle"><i class="fa fa-bars"></i></a>
			</div>

			<ul class="nav navbar-nav navbar-right">
				<li class=""><a href="javascript:;"
					class="user-profile dropdown-toggle" data-toggle="dropdown"
					aria-expanded="false"> <img
						src="<c:url value="/resources/images/login-img.jpg"/>" alt="">
						${userBean.firstname} ${userBean.lastname}<span class=" fa fa-angle-down"></span>
				</a>
					<ul class="dropdown-menu">

						<li><a href="#"><i class="fa fa-sign-out"></i> PGDFM ID:
								${userBean.username}</a></li>
						<li><a href="mailto:${userBean.email}"><i
								class="fa fa-envelope"></i> ${userBean.email}</a></li>
						<li><a href=""><i class="fa fa-phone"></i>
								${userBean.mobile}</a></li>
						<li><a href=""><i class="fa fa-user"></i> Profile</a></li>
						<li><a href="${pageContext.request.contextPath}/loggedout"><i
								class="fa fa-sign-out"></i> Logout</a></li>

					</ul></li>

				<li role="presentation" class="dropdown"><a href="javascript:;"
					class="dropdown-toggle info-number" data-toggle="dropdown"
					aria-expanded="false"> <i class="fa fa-bell-o"></i> <span
						class="badge bg-red">${announcementForTopMenu.size()}</span>
				</a>
					<ul id="menu1" class="dropdown-menu list-unstyled msg_list"
						role="menu">
						<c:forEach items="${announcementForTopMenu}" var="announcement">
							<li><a>
                            <span class="image"><img src="<c:url value="/resources/images/img.jpg"/>" alt="Profile Image" /></span>
                            <span>
                                <span>${announcement.subject}</span>
                                <span class="time">${announcement.createdDate}</span>
                            </span>
                            <span class="message">
                                 ${announcement.description}
                            </span>
                        </a></li>
						
						
						
						</c:forEach>
						
		
						<li>
							<div class="text-center">
								<a href="<c:url value="/viewUserAnnouncements"/>"> <strong>See All Alerts</strong> <i
									class="fa fa-angle-right"></i>
								</a>
							</div>
						</li>
					</ul></li>
					
					



				<li class="rsdesmenu">
					<button type="button" class="navbar-toggle collapsed"
						data-toggle="collapse" data-target="#navbar" aria-expanded="false"
						aria-controls="navbar">
						<span class="icon-bar"></span> <span class="icon-bar"></span> <span
							class="icon-bar"></span>
					</button>

					<div id="navbar" class="navbar-collapse collapse">
						<ul class="nav navbar-nav">

							<!-- Parent Menu -->



							<sec:authorize access="hasRole('ROLE_PARENT')">

								<li class="dropdown"><a href="#" class="dropdown-toggle"
									data-toggle="dropdown" role="button" aria-haspopup="true"
									aria-expanded="false"> <i class="fa fa-comment-o"></i>Announcement</a>
									<ul class="dropdown-menu">
										<li><a
											href="<c:url value="/announcementFormForParents" />">View
												Announcements</a></li>

									</ul></li>

							</sec:authorize>

							<sec:authorize access="hasRole('ROLE_PARENT')">

								<li class="dropdown"><a href="#" class="dropdown-toggle"
									data-toggle="dropdown" role="button" aria-haspopup="true"
									aria-expanded="false"> <i class="fa fa-comment-o"></i>Pending
										Task</a>
									<ul class="dropdown-menu">
										<li><a
											href="<c:url value="/pendingTaskFormForParents" />">View
												Pending Task</a></li>

									</ul></li>

							</sec:authorize>

							<sec:authorize access="hasRole('ROLE_PARENT')">

								<li class="dropdown"><a href="#" class="dropdown-toggle"
									data-toggle="dropdown" role="button" aria-haspopup="true"
									aria-expanded="false"> <i class="fa fa-comment-o"></i>Grades</a>
									<ul class="dropdown-menu">
										<li><a
											href="<c:url value="/gradeCenterFormForParents" />">View
												Grades</a></li>

									</ul></li>

							</sec:authorize>


							<!-- Admin Menu -->

							<sec:authorize access="hasRole('ROLE_ADMIN')">

								<li class="dropdown"><a href="#" class="dropdown-toggle"
									data-toggle="dropdown" role="button" aria-haspopup="true"
									aria-expanded="false"> <i class="fa fa-comment-o"></i>
										Feedback </a>
									<ul class="dropdown-menu">
										<li><a href="<c:url value="/addFeedbackForm" />">Add
												Feedback </a></li>
										<li><a href="<c:url value="/searchFeedback" />">View
												Feedback </a></li>
										<li><a href="<c:url value="/addStudentFeedbackForm" />">Allocate
												Feedback </a></li>

									</ul></li>

							</sec:authorize>

							<sec:authorize access="hasRole('ROLE_ADMIN')">

								<li class="dropdown"><a href="#" class="dropdown-toggle"
									data-toggle="dropdown" role="button" aria-haspopup="true"
									aria-expanded="false"> <i class="fa fa-comment-o"></i>Course</a>
									<ul class="dropdown-menu">
										<li><a href="<c:url value="/addCourseForm" />">Add
												Single Course </a></li>
										<li><a href="<c:url value="/uploadCourseForm" />">Upload
												Single Course </a></li>
										<li><a href="<c:url value="/searchCourse" />">Search
												Courses </a></li>
										<li><a href="<c:url value="/uploadUserCourseForm" />">Course
												User Enrollment </a></li>
										<li><a href="<c:url value="/searchUserCourse" />">Search
												Course User Enrollment</a></li>
									</ul></li>

							</sec:authorize>

							<sec:authorize access="hasRole('ROLE_ADMIN')">

								<li class="dropdown"><a href="#" class="dropdown-toggle"
									data-toggle="dropdown" role="button" aria-haspopup="true"
									aria-expanded="false"> <i class="fa fa-comment-o"></i>Manage
										Users</a>
									<ul class="dropdown-menu">
										<li><a href="<c:url value="/addUserForm" />">Create
												Single User </a></li>
										<li><a href="<c:url value="/uploadStudentForm" />">Upload
												Student Excel </a></li>
										<li><a href="<c:url value="/uploadFacultyForm" />">Upload
												Faculty Excel </a></li>

									</ul></li>

							</sec:authorize>


							<sec:authorize access="hasAnyRole('ROLE_ADMIN', 'ROLE_DEAN')">

								<li class="dropdown"><a href="#" class="dropdown-toggle"
									data-toggle="dropdown" role="button" aria-haspopup="true"
									aria-expanded="false"> <i class="fa fa-comment-o"></i>Message</a>
									<ul class="dropdown-menu">
										<li><a href="<c:url value="/createMessageForm" />">Create
												Message </a></li>
										<li><a href="<c:url value="/viewMyMessage" />">View
												Message </a></li>

									</ul></li>

							</sec:authorize>

							<sec:authorize access="hasRole('ROLE_ADMIN')">

								<li class="dropdown"><a href="#" class="dropdown-toggle"
									data-toggle="dropdown" role="button" aria-haspopup="true"
									aria-expanded="false"> <i class="fa fa-comment-o"></i>Grievances</a>
									<ul class="dropdown-menu">
										<li><a href="<c:url value="/viewAllGrievances" />">View
												Grievances </a></li>

									</ul></li>

							</sec:authorize>

							<sec:authorize access="hasRole('ROLE_ADMIN')">

								<li class="dropdown"><a href="#" class="dropdown-toggle"
									data-toggle="dropdown" role="button" aria-haspopup="true"
									aria-expanded="false"> <i class="fa fa-comment-o"></i>Set Up</a>
									<ul class="dropdown-menu">
										<li><a href="<c:url value="/addInstituteCycleForm" />">Make
												Academic Cycle LIVE </a></li>

									</ul></li>

							</sec:authorize>


							<!-- Faculty Menu -->

							<sec:authorize access="hasRole('ROLE_FACULTY')">

								<li class="dropdown"><a href="#" class="dropdown-toggle"
									data-toggle="dropdown" role="button" aria-haspopup="true"
									aria-expanded="false"> <i class="fa fa-comment-o"></i> My
										Courses</a>
									<ul class="dropdown-menu">

										<li><a href="viewCourse?id=99">Engineering Physics</a></li>
										<li><a href="<c:url value="viewCourse?id=100" />">Basic
												Electrical Engineering</a></li>
										<li><a href="<c:url value="viewCourse?id=101" />">Engineering
												Drawing - I</a></li>
										<li><a href="<c:url value="viewCourse?id=102" />">Engineering
												Machanics - I</a></li>
										<li><a href="<c:url value="viewCourse?id=103" /> ">Computer
												Programming - I</a></li>

									</ul></li>
							</sec:authorize>


							<sec:authorize access="hasRole('ROLE_FACULTY')">

							<li class="dropdown"><a href="#" class="dropdown-toggle"
									data-toggle="dropdown" role="button" aria-haspopup="true"
									aria-expanded="false"> <i class="fa fa-comment-o"></i>
										Assignment</a>
									<ul class="dropdown-menu">

										<li><a href="createAssignmentFromMenu">Create
												Assignments </a></li>
										<li><a
											href="<c:url value="/createAssignmentFromGroup" />">Create
												Assignments for groups </a></li>
										<li><a href="<c:url value="/searchFacultyAssignment" />">View
												Assignments</a></li>
										<li><a
											href="<c:url value="/searchAssignmentToEvaluate" />">Evaluate
												Assignments with Advanced search </a></li>
										<li><a href="<c:url value="/evaluateByStudentForm" /> ">Evaluate
												Assignments fro Students </a></li>
										<li><a
											href="<c:url value="/lateSubmissionApprovalForm" /> ">Late
												Submitted Assignments</a></li>
									</ul></li>
							</sec:authorize>


							<sec:authorize access="hasRole('ROLE_FACULTY')">

								<li class="dropdown"><a href="#" class="dropdown-toggle"
									data-toggle="dropdown" role="button" aria-haspopup="true"
									aria-expanded="false"> <i class="fa fa-comment-o"></i>
										Announcements</a>
									<ul class="dropdown-menu">

										<li><a href="<c:url value="/addAnnouncementForm" />">Create
												Announcements </a></li>
										<li><a href="<c:url value="/searchAnnouncement" />">Search
												Announcements </a></li>

									</ul></li>
							</sec:authorize>

							<sec:authorize access="hasRole('ROLE_FACULTY')">

								<li class="dropdown"><a href="#" class="dropdown-toggle"
									data-toggle="dropdown" role="button" aria-haspopup="true"
									aria-expanded="false"> <i class="fa fa-comment-o"></i>
										Message</a>
									<ul class="dropdown-menu">
										<li><a href="<c:url value="createMessageForm" />">Create
												Message</a></li>
										<li><a href="<c:url value="/viewMyMessage" />">View
												Message </a></li>
									</ul></li>

							</sec:authorize>

							<sec:authorize access="hasRole('ROLE_FACULTY')">

								<li class="dropdown"><a href="#" class="dropdown-toggle"
									data-toggle="dropdown" role="button" aria-haspopup="true"
									aria-expanded="false"> <i class="fa fa-comment-o"></i> Group</a>
									<ul class="dropdown-menu">
										<li><a
											href="<c:url value="/createGroupForm?courseId=${course.id}" />">Create
												Group</a></li>
										<li><a href="<c:url value="/searchFacultyGroups" />">View
												Group </a></li>
									</ul></li>

							</sec:authorize>


							<sec:authorize access="hasRole('ROLE_FACULTY')">

								<li class="dropdown"><a href="#" class="dropdown-toggle"
									data-toggle="dropdown" role="button" aria-haspopup="true"
									aria-expanded="false"> <i class="fa fa-comment-o"></i> Test</a>
									<ul class="dropdown-menu">
										<li><a href="<c:url value="/addTestFromMenu" />">Create
												Test </a></li>
										<li><a href="<c:url value="/testList" />">View Test </a></li>
										<li><a href="<c:url value="/searchTest" />">Update
												Test </a></li>
										<li><a href="<c:url value="/configureQuestions" />">Configure
												Questions </a></li>
									</ul></li>

							</sec:authorize>


							<sec:authorize access="hasRole('ROLE_FACULTY')">

								<li class="dropdown"><a href="#" class="dropdown-toggle"
									data-toggle="dropdown" role="button" aria-haspopup="true"
									aria-expanded="false"> <i class="fa fa-comment-o"></i>
										Discussion Forum </a>
									<ul class="dropdown-menu">
										<li><a href="<c:url value="/createForumForm" />">Create
												Forum </a></li>
										<li><a href="<c:url value="/viewForum" />">View Forum
										</a></li>

									</ul></li>

							</sec:authorize>


							<!-- Student Menu -->


							<li class="active"><a href="<c:url value="homepage" />"><i
									class="fa fa-line-chart"></i>Dashboard</a></li>


							<sec:authorize access="hasRole('ROLE_STUDENT')">

								<li class="dropdown"><a href="#" class="dropdown-toggle"
									data-toggle="dropdown" role="button" aria-haspopup="true"
									aria-expanded="false"> <i class="fa fa-comment-o"></i> My
										Courses
								</a>
								
									<ul class="dropdown-menu">

										<li><a href="<c:url value="viewCourse?id=1" />">Accounting Paper I</a></li>
										<li><a href="<c:url value="viewCourse?id=2" />">Business Communication-I</a></li>
										<li><a href="<c:url value="viewCourse?id=3" />">Information Technology</a></li>
										<li><a href="<c:url value="viewCourse?id=4" /> ">Modern Commercial Practices-I</a></li>

									</ul></li>

							</sec:authorize>

							<sec:authorize access="hasRole('ROLE_STUDENT')">

								<li class="dropdown"><a href="#" class="dropdown-toggle"
									data-toggle="dropdown" role="button" aria-haspopup="true"
									aria-expanded="false"> <i class="fa fa-comment-o"></i>
										Message</a>
									<ul class="dropdown-menu">
										<li><a href="<c:url value="createMessageForm" />">Create
												Message</a></li>
										<li><a href="<c:url value="/viewMyMessage" />">View
												Message </a></li>
									</ul></li>

							</sec:authorize>

							<li class="dropdown"><a href="#" class="dropdown-toggle"
									data-toggle="dropdown" role="button" aria-haspopup="true"
									aria-expanded="false"> <i class="fa fa-calendar"></i>
									Calendar</a>
								<ul class="dropdown-menu">

									<li><a href="<c:url value="addCalenderEventForm" />">Add
											Event</a></li>
									<li><a href="<c:url value="viewEvents" />">View Event</a></li>
								</ul></li>


							<sec:authorize access="hasRole('ROLE_STUDENT')">

								<li class="dropdown"><a href="#" class="dropdown-toggle"
									data-toggle="dropdown" role="button" aria-haspopup="true"
									aria-expanded="false"> <i class="fa fa-support"></i> Support</a>
									<ul class="dropdown-menu">
										<li><a href="<c:url value="grievanceForm" />">Raise
												Grievance </a></li>

										<li><a
											href="<c:url value="overview?courseId=${course.id}" />">Overview
										</a></li>
										<li><a href="<c:url value="viewGrievanceResponse" />">Preview
												Grievances Response List </a></li>
									</ul></li>

							</sec:authorize>


							<sec:authorize access="hasRole('ROLE_STUDENT')">
								<li class="dropdown"><a href="#" class="dropdown-toggle"
									data-toggle="dropdown" role="button" aria-haspopup="true"
									aria-expanded="false">  <img
										src="resources/images/quicklink.png" height="30%"
										alt="Quicklink"
										style="border-radius: 10px; height: 26px; width: 23px; padding: 1px;">Quicklinks
										
								</a>
									<ul class="dropdown-menu" role="menu">
										<li><a href="<c:url value="searchStudentFeedback" />">View
												Feedback</a></li>
										<li><a href="<c:url value="testList" />">View Test</a></li>
										<li><a href="<c:url value="viewGrievanceResponse" />">Preview
												Grievances Response List </a></li>
										<li><a href="<c:url value="overview" />"> Overview</a></li>

									</ul></li>

							</sec:authorize>


						</ul>
					</div>
				</li>




			</ul>
		</nav>
	</div>
</div>
<!-- top navigation -->

