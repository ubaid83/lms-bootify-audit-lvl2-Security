<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page import="java.util.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

<!DOCTYPE html>
<html lang="en">

<head>

<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="author" content="">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>

<title>${webPage.title}</title>

<!--  -->
<%-- <link rel="shortcut icon" href="favicon.ico">
<link rel="icon"
	type="${pageContext.request.contextPath}/resources/images/png"
	href="favicon.png">

<!-- Bootstrap -->

<link href="<c:url value="/resources/css/bootstrap.min.css" />"
	rel="stylesheet">
<!-- Font Awesome -->

<link href="<c:url value="/resources/css/font-awesome.min.css" />"
	rel="stylesheet">
<!-- bootstrap-progressbar -->

<link
	href="<c:url value="/resources/css/bootstrap-progressbar-3.3.4.min.css" />"
	rel="stylesheet">


<!-- Custom Toggle Style -->
<link href="<c:url value="/resources/css/toggle.css" />"
	rel="stylesheet">


<!-- Custom Theme Style -->
<link href="<c:url value="/resources/css/custom.css" />"
	rel="stylesheet">
<link href="<c:url value="/resources/css/responsive.css" />"
	rel="stylesheet">
 --%>
<!--  -->




<!-- Bootstrap Core CSS -->
<link href="<c:url value="resources/css/bootstrap.min.css" />"
	rel="stylesheet">

<!-- Font awesome CSS -->
<link href="<c:url value="resources/css/font-awesome.min.css" />"
	rel="stylesheet">

<!-- UI CSS For components -->
<link href="<c:url value="resources/css/ui.css" />" rel="stylesheet">


<!-- Tiles CSS -->
<link href="<c:url value="resources/css/tiles.css" />" rel="stylesheet">

<!-- Multi Column Select CSS -->
<link href="<c:url value="resources/css/Multi-Column-Select.css" />"
	rel="stylesheet">

<!-- Custom CSS -->
<c:if test="${webPage.header}">
	<link href="<c:url value="resources/css/scrolling-nav.css" />"
		rel="stylesheet">
</c:if>

<!-- Editable element CSS -->
<link href="<c:url value="resources/css/bootstrap-editable.css" />"
	rel="stylesheet">

<!-- Table Tree CSS -->
<link href="<c:url value="resources/css/jquery.treetable.css" />"
	rel="stylesheet">
<link
	href="<c:url value="resources/css/jquery.treetable.theme.default.css" />"
	rel="stylesheet">

<!-- Countdown plugin CSS -->
<link href="<c:url value="resources/css/TimeCircles.css" />"
	rel="stylesheet">

<!-- DataTables plugin CSS -->
<link href="<c:url value="resources/css/dataTables.bootstrap.css" />"
	rel="stylesheet">

<!-- jBox Modal and Tooltip plugin -->
<link href="<c:url value="resources/css/jBox.css" />" rel="stylesheet">

<!-- Bootstrap based Checkbox to Toggle plugin -->
<link href="<c:url value="resources/css/bootstrap-toggle.min.css" />"
	rel="stylesheet">

<!-- Calender plugin -->
<link
	href="https://cdnjs.cloudflare.com/ajax/libs/fullcalendar/2.6.1/fullcalendar.min.css"
	type="text/css" rel="stylesheet" />
<link href="https://unpkg.com/bootstrap@3/dist/css/bootstrap.css"
	rel="stylesheet">
<link
	href="https://unpkg.com/angular-bootstrap-colorpicker@3/css/colorpicker.min.css"
	rel="stylesheet">
<link
	href="https://unpkg.com/angular-bootstrap-calendar/dist/css/angular-bootstrap-calendar.min.css"
	rel="stylesheet">

<script type="text/javascript">
	var headerCourseId = null;
	function theFunction(obj) {

		return true;
	}
</script>

<!-- Footer CSS -->
<c:if test="${webPage.footer}">
	<link href="<c:url value="resources/css/custom/footer.css" />"
		rel="stylesheet">
</c:if>

<c:if test="${webPage.css}">
	<link href="<c:url value="resources/css/custom/${webPage.name}.css" />"
		rel="stylesheet"></link>
</c:if>
<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>
<%
	Map<String, Set<String>> menuRights = (Map<String, Set<String>>) session
			.getAttribute("menuRights");
System.out.print("menuRights"+menuRights);
	boolean showHome = (menuRights.containsKey("Home"));
	boolean showCourse = (menuRights.containsKey("MyCourse"));
	boolean showFeedback = (menuRights.containsKey("Feedback"));
	boolean showAddFeedback = showFeedback ? menuRights.get("Feedback")
			.contains("AddFeedback") : false;
	boolean showAllocateFeedback = showFeedback ? menuRights.get(
			"Feedback").contains("AllocateFeedback") : false;
	boolean showSearchFeedback = showFeedback ? menuRights.get(
			"Feedback").contains("SearchFeedback") : false;
	pageContext.setAttribute("showHome", showHome);
	pageContext.setAttribute("showCourse", showCourse);
	pageContext.setAttribute("showAddFeedback", showAddFeedback);
	pageContext.setAttribute("showAllocateFeedback",
			showAllocateFeedback);
	pageContext.setAttribute("showSearchFeedback", showSearchFeedback);
	
%>

<!-- The #page-top ID is part of the scrolling feature - the data-spy and data-target are part of the built-in Bootstrap scrollspy function -->

<body id="page-top" data-spy="scroll" data-target=".navbar-fixed-top">
	<c:if test="${webPage.header}">
		<!-- Navigation -->
		<nav class="navbar navbar-inverse navbar-fixed-top top-nav-collapse"
			role="navigation" style="background-color: #DCDCDC">
			<div class="container-fluid"
				style="height: 60px; color: white; padding-left: 0px;">
				<img src="resources/images/logo.jpg" height="120%" width="16%"
					alt="Logo">
				<div class="navbar-header page-scroll">
					<button type="button" class="navbar-toggle" data-toggle="collapse"
						data-target=".navbar-ex1-collapse">
						<span class="sr-only">Toggle navigation</span> <span
							class="icon-bar"></span> <span class="icon-bar"></span> <span
							class="icon-bar"></span>
					</button>
					<!-- <a class="navbar-brand page-scroll" href="#page-top">Start
                              Bootstrap</a> -->
				</div>

				<!-- Collect the nav links, forms, and other content for toggling -->
				<div class="collapse navbar-collapse navbar-ex1-collapse"
					style="margin-top: 13px; float: right;">
					<ul class="nav navbar-nav" style="font-color: black;">
						<!-- Hidden li included to remove active class from about link when scrolled up past about section -->
						<li class="hidden" style="font-size: 14px;"><a
							class="page-scroll" href="#page-top"></a></li>
						<c:if test="${showHome}">
							<li><a class="page-scroll" href="homepage"> <img
									src="resources/images/home.jpg" height="30%" alt="Home"
									style="border-radius: 10px; height: 26px; width: 23px; padding: 1px;">
									Home
							</a></li>
						</c:if>

						<c:if test="${showCourse}">
							<li class="dropdown"><a href="#" class="dropdown-toggle"
								data-toggle="dropdown" role="button" aria-expanded="false">
									<img src="resources/images/course.jpg" height="30%"
									alt="Course"
									style="border-radius: 10px; height: 26px; width: 23px; padding: 1px;">My
									Courses <span class="caret"></span>
							</a>
								<ul class="dropdown-menu" role="menu">
									<li><a onclick="return theFunction(99);"
										href="<c:url value="viewCourse?id=99" />">Engineering
											Physics</a></li>
									<li><a href="<c:url value="viewCourse?id=100" />">Basic
											Electrical Engineering </a></li>
									<li><a href="<c:url value="viewCourse?id=101" />">Engineering
											Drawing - I</a></li>
									<li><a href="<c:url value="viewCourse?id=102" />">Engineering
											Machanics - I </a></li>
									<li><a href="<c:url value="viewCourse?id=103" />">Computer
											Programming - I</a></li>
								</ul></li>

						</c:if>





						<c:if test="${showCourse}">
							<li class="dropdown"><a href="#" class="dropdown-toggle"
								data-toggle="dropdown" role="button" aria-expanded="false">
									<img src="resources/images/feedback.png" height="30%"
									alt="Feedback"
									style="border-radius: 10px; height: 26px; width: 23px; padding: 1px;">Feedback
									<span class="caret"></span>
							</a>
								<ul class="dropdown-menu" role="menu">
									<c:if test="${showAddFeedback}">
										<li><a href="<c:url value="addFeedbackForm" />">Add
												Feedback</a></li>
									</c:if>
									<c:if test="${showSearchFeedback}">
										<li><a href="<c:url value="searchFeedback" />">View
												Feedback</a></li>
									</c:if>
									<c:if test="${showAllocateFeedback}">
										<li><a href="<c:url value="addStudentFeedbackForm" />">Allocate
												Feedback</a></li>
									</c:if>
								</ul></li>
						</c:if>

						

						<sec:authorize access="hasRole('ROLE_FACULTY')">
							<li class="dropdown"><a href="#" class="dropdown-toggle"
								data-toggle="dropdown" role="button" aria-expanded="false">
									Assignment <span class="caret"></span>
							</a>
								<ul class="dropdown-menu" role="menu">
									<li><a href="<c:url value="createAssignmentFromMenu" />">Create
											Assignments</a></li>
									<li><a href="<c:url value="createAssignmentFromGroup" />">Create
											Assignments For Groups</a></li>
									<li><a href="<c:url value="searchFacultyAssignment" />">View
											Assignments</a></li>
									<li><a href="<c:url value="searchAssignmentToEvaluate" />">Evaluate
											Assignments With Advanced Search</a></li>
									<li><a href="<c:url value="evaluateByStudentForm" />">Evaluate
											Assignments for Students</a></li>

									<li><a href="<c:url value="lateSubmissionApprovalForm" />">
											Late Submitted Assignments</a></li>
									<li><a href="<c:url value="nonSubmittedStudentsForm" />">
											Not Submitted Students</a></li>
									<!--                                                        <li><a -->
									<%--                                                              href="<c:url value="/evaluateByStudentFormGroup" />">Evaluate --%>
									<!--                                                                    
 For Groups</a></li> -->

								</ul></li>
						</sec:authorize>

						<%-- <sec:authorize access="hasRole('ROLE_ADMIN')">
                              <li class="dropdown"><a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false">Program <span class="caret"></span></a>
                              <ul class="dropdown-menu" role="menu">
                              <li><a href="<c:url value="/addProgramForm" />">Add Single Program</a></li>
                              <li><a href="<c:url value="/uploadProgramForm" />">Upload Program Excel</a></li>
                              <li><a href="<c:url value="/searchProgram" />">Search Programs</a></li>
                              <li><a href="<c:url value="/searchProgramSession" />">Map Program Sessions</a></li>
                              <li><a href="<c:url value="/addProgramSessionCourseForm" />">Map Program Courses</a></li>
                        </ul>
                              </li>
                              </sec:authorize> --%>

						<sec:authorize access="hasRole('ROLE_ADMIN')">
							<li class="dropdown"><a href="#" class="dropdown-toggle"
								data-toggle="dropdown" role="button" aria-expanded="false">
									<img src="resources/images/feedback.png" height="30%"
									alt="Feedback"
									style="border-radius: 10px; height: 26px; width: 23px; padding: 1px;">Course
									<span class="caret"></span>
							</a>
								<ul class="dropdown-menu" role="menu">
									<li><a href="<c:url value="addCourseForm" />">Add
											Single Course</a></li>
									<li><a href="<c:url value="uploadCourseForm" />">Upload
											Course Excel</a></li>
									<li><a href="<c:url value="searchCourse" />">Search
											Courses</a></li>
									<li><a href="<c:url value="uploadUserCourseForm" />">Course
											User Enrollment</a></li>
									<li><a href="<c:url value="searchUserCourse" />">Search
											Course User Enrollment</a></li>
									<%-- <li><a href="<c:url value="/addFacultyCourseForm" />">Course Faculty Enrollment-Not needed</a></li> --%>
								</ul></li>
						</sec:authorize>

						<sec:authorize access="hasRole('ROLE_ADMIN')">
							<li class="dropdown"><a href="#" class="dropdown-toggle"
								data-toggle="dropdown" role="button" aria-expanded="false">
									<img src="resources/images/feedback.png" height="30%"
									alt="Feedback"
									style="border-radius: 10px; height: 26px; width: 23px; padding: 1px;">Manage
									Users <span class="caret"></span>
							</a>
								<ul class="dropdown-menu" role="menu">
									<li><a href="<c:url value="addUserForm" />">Create
											Single User</a></li>
									<li><a href="<c:url value="uploadStudentForm" />">Upload
											Students Excel</a></li>
									<li><a href="<c:url value="uploadFacultyForm" />">Upload
											Faculty Excel</a></li>
								</ul></li>
						</sec:authorize>

						<sec:authorize access="hasRole('ROLE_FACULTY')">
							<li class="dropdown"><a href="#" class="dropdown-toggle"
								data-toggle="dropdown" role="button" aria-expanded="false">
									Announcements <span class="caret"></span>
							</a>
								<ul class="dropdown-menu" role="menu">
									<li><a href="<c:url value="addAnnouncementForm" />">Create
											Announcement</a></li>
									<li><a href="<c:url value="searchAnnouncement" />">Search
											Announcements</a></li>
								</ul></li>
						</sec:authorize>

						<sec:authorize access="hasRole('ROLE_FACULTY')">
							<li class="dropdown"><a href="#" class="dropdown-toggle"
								data-toggle="dropdown" role="button" aria-expanded="false">
									Message <span class="caret"></span>
							</a>
								<ul class="dropdown-menu" role="menu">
									<li><a href="<c:url value="createMessageForm" />">Create
											Message</a></li>
									<li><a href="<c:url value="viewMyMessage" />">View
											Message</a></li>
								</ul></li>
						</sec:authorize>
						<sec:authorize access="hasRole('ROLE_STUDENT')">
							<li class="dropdown"><a href="#" class="dropdown-toggle"
								data-toggle="dropdown" role="button" aria-expanded="false">
									Message <span class="caret"></span>
							</a>
								<ul class="dropdown-menu" role="menu">
									<li><a href="<c:url value="createMessageForm" />">Create
											Message</a></li>
									<li><a href="<c:url value="viewMyMessage" />">View
											Message</a></li>
								</ul></li>
						</sec:authorize>
						<sec:authorize access="hasAnyRole('ROLE_ADMIN', 'ROLE_DEAN')">
							<li class="dropdown"><a href="#" class="dropdown-toggle"
								data-toggle="dropdown" role="button" aria-expanded="false">
									Message <span class="caret"></span>
							</a>
								<ul class="dropdown-menu" role="menu">
									<li><a href="<c:url value="createMessageForm" />">Create
											Message</a></li>
									<li><a href="<c:url value="viewMyMessage" />">View
											Message</a></li>
								</ul></li>
						</sec:authorize>



						<sec:authorize access="hasRole('ROLE_FACULTY')">
							<li class="dropdown"><a href="#" class="dropdown-toggle"
								data-toggle="dropdown" role="button" aria-expanded="false">

									Group <span class="caret"></span>
							</a>
								<ul class="dropdown-menu" role="menu">
									<li><a
										href="<c:url value="createGroupForm?courseId=${course.id}" />">Create
											Group</a></li>
									<li><a href="<c:url value="searchFacultyGroups" />">View
											Group</a></li>


								</ul></li>
						</sec:authorize>
						<%-- 
							<sec:authorize access="hasRole('ROLE_FACULTY')">
								<li class="dropdown"><a href="#" class="dropdown-toggle"
									data-toggle="dropdown" role="button" aria-expanded="false">
										Content <span class="caret"></span>
								</a>
									<ul class="dropdown-menu" role="menu">
										<li><a
											href="<c:url value="addContentForm?contentType=Folder&courseId=1" />">Create
												Folder</a></li>
										<li><a
											href="<c:url value="addContentForm?contentType=File&courseId=1" />">Create
												File</a></li>
										<li class="divider"></li>
										<li><a
											href="<c:url value="getContentUnderAPath?courseId=1" />">View
												Content</a></li>
										<li><a href="<c:url value="/searchTest" />">View Test</a></li>
									</ul></li>
							</sec:authorize> --%>
						<%-- 
							<sec:authorize access="hasAnyRole('ROLE_ADMIN','ROLE_FACULTY')">
								<li class="dropdown"><a href="#" class="dropdown-toggle"
									data-toggle="dropdown" role="button" aria-expanded="false">
										Search <span class="caret"></span>
								</a>
									<ul class="dropdown-menu" role="menu">
										<li><a href="<c:url value="searchAssignmentForm" />">Search
												Assignments</a></li>
										<li><a href="<c:url value="searchTest" />">Search
												Tests</a></li>
									</ul></li>
							</sec:authorize> --%>

						<%-- <sec:authorize access="hasAnyRole('ROLE_STUDENT','ROLE_FACULTY')">
								<li class="dropdown"><a href="#" class="dropdown-toggle"
									data-toggle="dropdown" role="button" aria-expanded="false">
										<img src="resources/images/task.png" height="30%" alt="Task"
										style="border-radius: 10px; height: 26px; width: 23px; padding: 1px;">
										Task <span class="caret"></span>
								</a>
									<ul class="dropdown-menu" role="menu">
										<li><a href="<c:url value="pendingTask" />">Pending
												Task</a></li>

									</ul></li>
							</sec:authorize> --%>

						<%-- <sec:authorize access="hasAnyRole('ROLE_STUDENT','ROLE_FACULTY')">
                                                <li class="dropdown"><a href="#" class="dropdown-toggle"
                                                      data-toggle="dropdown" role="button" aria-expanded="false">
                                                      <img src="resources/images/inbox.png" height="30%" alt="Inbox"
                        style="border-radius: 10px;  height:26px ; width:23px;padding: 1px;">Inbox
                                                            <span class="caret"></span>
                                                </a>
                                                      <ul class="dropdown-menu" role="menu">
                                                            <li><a href="<c:url value="/searchInboxForm" />">Send
                                                                        Email </a></li>
 
                                                      </ul></li>
                                          </sec:authorize>
  --%>



						<sec:authorize access="hasRole('ROLE_STUDENT')">
							<li class="dropdown"><a href="#" class="dropdown-toggle"
								data-toggle="dropdown" role="button" aria-expanded="false">
									<img src="resources/images/support.png" height="30%"
									alt="Support"
									style="border-radius: 10px; height: 26px; width: 23px; padding: 1px;">Student
									Support <span class="caret"></span>
							</a>
								<ul class="dropdown-menu" role="menu">
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
								data-toggle="dropdown" role="button" aria-expanded="false">
									<img src="resources/images/quicklink.png" height="30%"
									alt="Quicklink"
									style="border-radius: 10px; height: 26px; width: 23px; padding: 1px;">Quicklinks
									<span class="caret"></span>
							</a>
								<ul class="dropdown-menu" role="menu">
									<li><a href="<c:url value="searchStudentFeedback" />">View
											Feedback</a></li>
									<li><a href="<c:url value="testList" />">View Test</a></li>
									<li><a href="<c:url value="viewLibrary" />">View
											Library</a></li>
									<li><a href="<c:url value="viewGrievanceResponse" />">Preview
											Grievances Response List </a></li>
									<li><a href="<c:url value="overview" />"> Overview</a></li>

								</ul></li>

						</sec:authorize>



						<sec:authorize access="hasRole('ROLE_ADMIN')">
							<li class="dropdown"><a href="#" class="dropdown-toggle"
								data-toggle="dropdown" role="button" aria-expanded="false">
									<img src="resources/images/feedback.png" height="30%"
									alt="Feedback"
									style="border-radius: 10px; height: 26px; width: 23px; padding: 1px;">Grievances
									<span class="caret"></span>
							</a>
								<ul class="dropdown-menu" role="menu">
									<li><a href="<c:url value="viewAllGrievances" />">View
											Grievances </a></li>
								</ul></li>
						</sec:authorize>


						<sec:authorize access="hasRole('ROLE_PARENT')">
							<li class="dropdown"><a href="#" class="dropdown-toggle"
								data-toggle="dropdown" role="button" aria-expanded="false">
									<img src="resources/images/feedback.png" height="30%"
									alt="Feedback"
									style="border-radius: 10px; height: 26px; width: 23px; padding: 1px;">Grades
									<span class="caret"></span>
							</a>
								<ul class="dropdown-menu" role="menu">
									<li><a href="<c:url value="gradeCenterFormForParents" />">View
											Grades </a></li>
								</ul></li>
						</sec:authorize>

						<sec:authorize access="hasRole('ROLE_PARENT')">
							<li class="dropdown"><a href="#" class="dropdown-toggle"
								data-toggle="dropdown" role="button" aria-expanded="false">
									<img src="resources/images/feedback.png" height="30%"
									alt="Feedback"
									style="border-radius: 10px; height: 26px; width: 23px; padding: 1px;">Pending
									Task <span class="caret"></span>
							</a>
								<ul class="dropdown-menu" role="menu">
									<li><a href="<c:url value="pendingTaskFormForParents" />">View
											Pending Task </a></li>
								</ul></li>
						</sec:authorize>

						<sec:authorize access="hasRole('ROLE_PARENT')">
							<li class="dropdown"><a href="#" class="dropdown-toggle"
								data-toggle="dropdown" role="button" aria-expanded="false">
									<img src="resources/images/feedback.png" height="30%"
									alt="Feedback"
									style="border-radius: 10px; height: 26px; width: 23px; padding: 1px;">Announcement
									<span class="caret"></span>
							</a>
								<ul class="dropdown-menu" role="menu">
									<li><a href="<c:url value="announcementFormForParents" />">View
											Announcements </a></li>
								</ul></li>
						</sec:authorize>
						<sec:authorize access="hasRole('ROLE_USER')">


							<sec:authorize access="hasRole('ROLE_FACULTY')">
								<li class="dropdown"><a href="#" class="dropdown-toggle"
									data-toggle="dropdown" role="button" aria-expanded="false">Test
										<span class="caret"></span>
								</a>
									<ul class="dropdown-menu" role="menu">
										<li><a href="<c:url value="/addTestFromMenu" />">
												Create Test</a></li>
										<li><a href="<c:url value="/testList" />">View Test</a></li>
										<li><a
											href="${pageContext.request.contextPath}/searchTest">Update
												Test</a></li>
										<li><a
											href="${pageContext.request.contextPath}/configureQuestions">Configure
												Questions</a></li>
									</ul></li>
							</sec:authorize>

							<sec:authorize access="hasAnyRole('ROLE_STUDENT','ROLE_FACULTY')">


								<li class="dropdown"><a href="#" class="dropdown-toggle"
									data-toggle="dropdown" role="button" aria-expanded="false">
										<img src="resources/images/forum.jpg" height="30%" alt="Task"
										style="border-radius: 10px; height: 26px; width: 23px; padding: 1px;">
										Discussion Forum <span class="caret"></span>
								</a>
									<ul class="dropdown-menu" role="menu">
										<li><a href="<c:url value="/createForumForm" />">Create
												Forum </a></li>
										<li><a href="<c:url value="/viewForum" />">View Forum
										</a></li>

									</ul></li>
							</sec:authorize>


							<%--   <li class="dropdown"><a href="#" class="dropdown-toggle"
                                                data-toggle="dropdown" role="button" aria-expanded="false">
                                              Library
                                                      <span class="caret"></span>
                                          </a>
                                                <ul class="dropdown-menu" role="menu">
                                                      <li><a href="<c:url value="/viewLibrary" />">Digital
                                                                  Library</a></li>
                                                </ul></li> --%>
							<%-- 
                                          <li class="dropdown"><a href="#" class="dropdown-toggle"
                                                data-toggle="dropdown" role="button" aria-expanded="false">
                                               
                                                Grades
                                                      <span class="caret"></span>
                                          </a>
                                                <ul class="dropdown-menu" role="menu">
                                                      <li><a href="<c:url value="/gradeCenter?courseId=99" />">Grade
                                                                  Center</a></li>
                                                </ul></li> --%>
							<%--   <li class="dropdown"><a href="#" class="dropdown-toggle"
									data-toggle="dropdown" role="button" aria-expanded="false">
									  <img src="resources/images/library.png" height="30%" alt="Library"
                        style="border-radius: 10px; height:26px ; width:23px; padding: 1px;">Event
										<span class="caret"></span>
								</a>
									<ul class="dropdown-menu" role="menu">
										<li><a href="<c:url value="/addCalenderEventForm" />">Add
												Event</a></li>
										<li><a href="<c:url value="/viewEvents" />">View
												Event</a></li>
										
										<li><a href="<c:url value="/addFacultyCourseForm" />">Course Faculty Enrollment-Not needed</a></li>
									</ul></li> --%>

							<li class="dropdown"><a href="#" class="dropdown-toggle"
								data-toggle="dropdown" role="button" aria-expanded="false">
									<img src="resources/images/calendar.png" height="30%"
									alt="Calendar"
									style="border-radius: 10px; height: 26px; width: 23px; padding: 1px;">Calendar<span
									class="caret"></span>
							</a>
								<ul class="dropdown-menu" role="menu">
									<li><a href="<c:url value="addCalenderEventForm" />">Add
											Event</a></li>
									<li><a href="<c:url value="viewEvents" />">View Event</a></li>

									<%-- <li><a href="<c:url value="/addFacultyCourseForm" />">Course Faculty Enrollment-Not needed</a></li> --%>
								</ul></li>

							<sec:authorize access="hasRole('ROLE_ADMIN')">
								<li class="dropdown"><a href="#" class="dropdown-toggle"
									data-toggle="dropdown" role="button" aria-expanded="false">
										<img src="resources/images/feedback.png" height="30%"
										alt="Feedback"
										style="border-radius: 10px; height: 26px; width: 23px; padding: 1px;">SetUp
										<span class="caret"></span>
								</a>
									<ul class="dropdown-menu" role="menu">
										<li><a href="<c:url value="addInstituteCycleForm" />">Make
												Academic Cycle LIVE!</a></li>
									</ul></li>
							</sec:authorize>


							<li class="dropdown"><a href="#" class="dropdown-toggle"
								data-toggle="dropdown" role="button" aria-expanded="false">
									Profile <span class="caret"></span>
							</a>
								<ul class="dropdown-menu" role="menu">
									<li>
									<li><a href="<c:url value="changePasswordForm" />">Change
											Password</a></li>
									<li><a href="<c:url value="updateProfileForm" />">Update
											Profile</a></li>
									<li><a class="page-scroll"
										href="${pageContext.request.contextPath}/loggedout">Logout</a></li>
								</ul></li>


						</sec:authorize>



					</ul>
				</div>
				<!-- /.navbar-collapse -->
			</div>
			<!-- /.container -->
		</nav>
		<!-- Header Strip for logo and user details -->
		<header class="customHeader row">
			<div class="logoWrapper col-xs-12 col-md-4" style="height: 100%">
				<!-- <img src="resources/images/logo.jpg" height="100%" alt="Logo"
                        style="border-radius: 10px; padding: 1px;">
-->
			</div>
			<c:if test="${not empty instituteName }">
				<div class="col-xs-12 col-md-4 flex-vcenter" style="height: 100%">
					<h4 class="white"></h4>
				</div>
			</c:if>
			<c:if test="${not empty currentUser }">
				<div class="col-xs-12 col-md-4" style="height: 100%">
					<h1>Welcome to White Board</h1>
					<div class="userContainer">
						<div class="userImg">
							<img src="resources/images/userImg.jpg" alt="Student Photo"
								class="img-responsive">
						</div>
						<div class="detailWrapper">
							<h2>${currentUser.firstname}${currentUser.lastname}</h2>
							<p>User ID: ${currentUser.username}, Program:
								${currentUser.program.programName}</p>
							<p>
								<a href="updateProfileForm" style="color: white">${currentUser.email}/${currentUser.mobile}</a>
							</p>
						</div>
					</div>
				</div>
			</c:if>
		</header>

	</c:if>