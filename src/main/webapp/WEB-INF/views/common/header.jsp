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
<meta name="viewport" content="width=device-width, initial-scale=.5, maximum-scale=12.0, minimum-scale=.25, user-scalable=yes"/>
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
	System.out.print("menuRights" + menuRights);
	if (menuRights != null) {
		boolean showHome = (menuRights != null) ? menuRights
				.containsKey("Home") : false;
		boolean showCourse = (menuRights.containsKey("MyCourse"));
		boolean showFeedback = (menuRights.containsKey("Feedback"));
		boolean showMessage = (menuRights.containsKey("Message"));
		boolean showStudentSupport = (menuRights
				.containsKey("StudentSupport"));
		boolean showQuicklinks = (menuRights.containsKey("Quicklinks"));
		boolean showForum = (menuRights.containsKey("Forum"));
		boolean showCalendar = (menuRights.containsKey("Calendar"));
		boolean showProfile = (menuRights.containsKey("Profile"));
		boolean showAssignment = (menuRights.containsKey("Assignment"));
		boolean showAnnouncement = (menuRights
				.containsKey("Announcement"));
		boolean showGroup = (menuRights.containsKey("Group"));
		boolean showTest = (menuRights.containsKey("Test"));
		boolean showManageUser = (menuRights.containsKey("ManageUser"));
		boolean showGrievances = (menuRights.containsKey("Grievances"));
		boolean showSetup = (menuRights.containsKey("Setup"));
		boolean showContent = (menuRights.containsKey("Content"));
		boolean showCourseMenu = (menuRights.containsKey("Course"));
		boolean showAttendence = (menuRights.containsKey("Attendence"));
		
		boolean showMarkAttendence = showAttendence ? menuRights.get(
				"Attendence").contains("MarkAttendence") : false;
		boolean showViewAttendence =  showAttendence ? menuRights.get(
				"Attendence").contains("ViewAttendence") : false;
		

		boolean showAddFeedback = showFeedback ? menuRights.get(
				"Feedback").contains("AddFeedback") : false;
		boolean showAllocateFeedback = showFeedback ? menuRights.get(
				"Feedback").contains("AllocateFeedback") : false;
		boolean showSearchFeedback = showFeedback ? menuRights.get(
				"Feedback").contains("SearchFeedback") : false;
		boolean showViewFeedback = showFeedback ? menuRights.get(
				"Feedback").contains("ViewFeedback") : false;
		boolean showFeedbackList = showFeedback ? menuRights.get(
				"Feedback").contains("FeedbackList") : false;

		boolean showAddSingleCourse = showCourseMenu ? menuRights.get(
				"Course").contains("AddSingleCourse") : false;

		boolean showAddCourseOverview = showCourseMenu ? menuRights
				.get("Course").contains("AddCourseOverview") : false;

		boolean showUploadCourse = showCourseMenu ? menuRights.get(
				"Course").contains("UploadCourse") : false;
		boolean showSearchCourses = showCourseMenu ? menuRights.get(
				"Course").contains("SearchCourses") : false;
		boolean showCourseUserEnrollment = showCourseMenu ? menuRights
				.get("Course").contains("CourseUserEnrollment") : false;
		boolean showSearchCourseUserEnrollment = showCourseMenu ? menuRights
				.get("Course").contains("SearchCourseUserEnrollment")
				: false;

		boolean showCreateMessage = showMessage ? menuRights.get(
				"Message").contains("createMessage") : false;
		boolean showViewMessage = showMessage ? menuRights.get(
				"Message").contains("viewMessage ") : false;
				
			

		boolean showRaiseGrieviance = showStudentSupport ? menuRights
				.get("StudentSupport").contains("RaiseGrieviance")
				: false;
		boolean showOverview = showStudentSupport ? menuRights.get(
				"StudentSupport").contains("Overview") : false;
		boolean showPrieviewGrievianceResponseList = showStudentSupport ? menuRights
				.get("StudentSupport").contains(
						"PrieviewGrievianceResponseList") : false;

		boolean showViewFeedbackQlinks = showQuicklinks ? menuRights
				.get("Quicklinks").contains("ViewFeedback") : false;
		boolean showViewTestQlinks = showQuicklinks ? menuRights.get(
				"Quicklinks").contains("ViewTest") : false;

		boolean showViewLibrary = showQuicklinks ? menuRights.get(
				"Quicklinks").contains("viewLibrary") : false;

		boolean showPrieviewGrievianceResponseListQlinks = showQuicklinks ? menuRights
				.get("Quicklinks").contains(
						"PrieviewGrievianceResponseList ") : false;
		boolean showOverviewQlinks = showQuicklinks ? menuRights.get(
				"Quicklinks").contains("Overview") : false;

		boolean showCreateForum = showForum ? menuRights.get("Forum")
				.contains("CreateForum") : false;
		boolean showViewsForum = showForum ? menuRights.get("Forum")
				.contains("ViewForum") : false;

		boolean showAddEvents = showCalendar ? menuRights.get(
				"Calendar").contains("AddEvents") : false;
		boolean showViewEvents = showCalendar ? menuRights.get(
				"Calendar").contains("ViewEvents") : false;

		boolean showCreateAssignment = showAssignment ? menuRights.get(
				"Assignment").contains("CreateAssignments") : false;
		boolean showCreateAssignmentsForGroups = showAssignment ? menuRights
				.get("Assignment").contains(
						"CreateAssignmentsForGroups") : false;
		boolean showViewAssignment = showAssignment ? menuRights.get(
				"Assignment").contains("ViewAssigments") : false;
		boolean showEvaluateAssignmentsAdvancedSearch = showAssignment ? menuRights
				.get("Assignment").contains(
						"EvaluateAssignmentsAdvancedSearch") : false;
		boolean showEvaluateAssignments = showAssignment ? menuRights
				.get("Assignment").contains("EvaluateAssignments")
				: false;
		boolean showLateSubmittedAssignments = showAssignment ? menuRights
				.get("Assignment").contains("LateSubmittedAssignments")
				: false;
		boolean showAssigmentList = showAssignment ? menuRights.get(
				"Assignment").contains("AssigmentList") : false;

		boolean showCreateAnnouncement = showAnnouncement ? menuRights
				.get("Announcement").contains("CreateAnnouncements")
				: false;
		boolean showSearchAnnouncements = showAnnouncement ? menuRights
				.get("Announcement").contains("SearchAnnouncements")
				: false;
		boolean showViewAnnouncements = showAnnouncement ? menuRights
				.get("Announcement").contains("ViewAnnouncements")
				: false;

		boolean showCreateGroup = showGroup ? menuRights.get("Group")
				.contains("CreateGroup") : false;
		boolean showViewGroup = showGroup ? menuRights.get("Group")
				.contains("ViewGroup") : false;
		boolean showGroupList = showGroup ? menuRights.get("Group")
				.contains("GroupList") : false;
				
		
	

		boolean showCreateTest = showTest ? menuRights.get("Test")
				.contains("CreateTest") : false;
		boolean showViewTest = showTest ? menuRights.get("Test")
				.contains("ViewTest") : false;
		boolean showUpdateTest = showTest ? menuRights.get("Test")
				.contains("UpdateTest") : false;
		boolean showConfigureQuestions = showTest ? menuRights.get(
				"Test").contains("ConfigureQuestions") : false;
		boolean showTestList = showTest ? menuRights.get("Test")
				.contains("TestList") : false;

		boolean showCreateSingleUser = showManageUser ? menuRights.get(
				"ManageUser").contains("CreateSingleUser") : false;
		boolean showUploadStudentExcel = showManageUser ? menuRights
				.get("ManageUser").contains("UploadStudentExcel")
				: false;
		boolean showUploadFacultyExcel = showManageUser ? menuRights
				.get("ManageUser").contains("UploadFacultyExcel")
				: false;

		boolean showViewGrievances = showGrievances ? menuRights.get(
				"Grievances").contains("ViewGrievances") : false;

		boolean showMakeAcademicCycleLive = showSetup ? menuRights.get(
				"Setup").contains("MakeAcademicCycleLive") : false;

		boolean showStudentContentList = showContent ? menuRights.get(
				"Content").contains("StudentContentList") : false;
		boolean showGetContentList = showContent ? menuRights.get(
				"Content").contains("GetContentList") : false;

		
		pageContext.setAttribute("showAttendence", showAttendence);
		pageContext.setAttribute("showHome", showHome);
		pageContext.setAttribute("showCourse", showCourse);
		pageContext.setAttribute("showProfile", showProfile);
		pageContext.setAttribute("showFeedback", showFeedback);
		pageContext.setAttribute("showMessage", showMessage);
		pageContext.setAttribute("showStudentSupport",
				showStudentSupport);
		pageContext.setAttribute("showQuicklinks", showQuicklinks);
		pageContext.setAttribute("showForum", showForum);
		pageContext.setAttribute("showCalendar", showCalendar);
		pageContext.setAttribute("showAssignment", showAssignment);
		pageContext.setAttribute("showAnnouncement", showAnnouncement);
		pageContext.setAttribute("showGroup", showGroup);

		pageContext.setAttribute("showTest", showTest);
		pageContext.setAttribute("showCourseMenu", showCourseMenu);
		pageContext.setAttribute("showManageUser", showManageUser);
		pageContext.setAttribute("showGrievances", showGrievances);
		pageContext.setAttribute("showSetup", showSetup);
		pageContext.setAttribute("showContent", showContent);
		
		
		pageContext.setAttribute("showMarkAttendence", showMarkAttendence);
		pageContext.setAttribute("showViewAttendence", showViewAttendence);
		pageContext.setAttribute("showAddFeedback", showAddFeedback);
		pageContext.setAttribute("showAllocateFeedback",
				showAllocateFeedback);
		pageContext.setAttribute("showSearchFeedback",
				showSearchFeedback);
		pageContext.setAttribute("showViewFeedback", showViewFeedback);
		pageContext.setAttribute("showFeedbackList", showFeedbackList);
		pageContext
				.setAttribute("showCreateMessage", showCreateMessage);
		pageContext.setAttribute("showViewMessage", showViewMessage);
		pageContext.setAttribute("showRaiseGrieviance",
				showRaiseGrieviance);
		pageContext.setAttribute("showOverview", showOverview);
		pageContext.setAttribute("showPrieviewGrievianceResponseList",
				showPrieviewGrievianceResponseList);
		pageContext.setAttribute("showViewFeedbackQlinks",
				showViewFeedbackQlinks);
		pageContext.setAttribute("showViewTestQlinks",
				showViewTestQlinks);
		pageContext.setAttribute(
				"showPrieviewGrievianceResponseListQlinks",
				showPrieviewGrievianceResponseListQlinks);
		pageContext.setAttribute("showOverviewQlinks",
				showOverviewQlinks);
		pageContext.setAttribute("showCreateForum", showCreateForum);

		pageContext.setAttribute("showViewsForum", showViewsForum);
		pageContext.setAttribute("showAddEvents", showAddEvents);
		pageContext.setAttribute("showViewEvents", showViewEvents);
		pageContext.setAttribute("showCreateAnnouncement",
				showCreateAnnouncement);
		pageContext.setAttribute("showSearchAnnouncements",
				showSearchAnnouncements);
		pageContext.setAttribute("showViewAnnouncements",
				showViewAnnouncements);

		pageContext.setAttribute("showCreateAssignment",
				showCreateAssignment);
		pageContext.setAttribute("showCreateAssignmentsForGroups",
				showCreateAssignmentsForGroups);
		pageContext.setAttribute("showViewAssignment",
				showViewAssignment);
		pageContext.setAttribute(
				"showEvaluateAssignmentsAdvancedSearch",
				showEvaluateAssignmentsAdvancedSearch);
		pageContext.setAttribute("showEvaluateAssignments",
				showEvaluateAssignments);
		pageContext.setAttribute("showLateSubmittedAssignments",
				showLateSubmittedAssignments);
		pageContext
				.setAttribute("showAssigmentList", showAssigmentList);

		pageContext.setAttribute("showCreateGroup", showCreateGroup);
		pageContext.setAttribute("showViewGroup", showViewGroup);
		pageContext.setAttribute("showGroupList", showGroupList);
		pageContext.setAttribute("showCreateTest", showCreateTest);
		pageContext.setAttribute("showViewTest", showViewTest);
		pageContext.setAttribute("showUpdateTest", showUpdateTest);
		pageContext.setAttribute("showTestList", showTestList);

		pageContext.setAttribute("showCreateSingleUser",
				showCreateSingleUser);
		pageContext.setAttribute("showUploadStudentExcel",
				showUploadStudentExcel);
		pageContext.setAttribute("showUploadFacultyExcel",
				showUploadFacultyExcel);
		pageContext.setAttribute("showViewGrievances",
				showViewGrievances);
		pageContext.setAttribute("showMakeAcademicCycleLive",
				showMakeAcademicCycleLive);
		pageContext.setAttribute("showStudentContentList",
				showStudentContentList);
		pageContext.setAttribute("showGetContentList",
				showGetContentList);

		pageContext.setAttribute("showAddSingleCourse",
				showAddSingleCourse);
		pageContext.setAttribute("showAddCourseOverview",
				showAddCourseOverview);
		pageContext.setAttribute("showUploadCourse", showUploadCourse);
		pageContext
				.setAttribute("showSearchCourses", showSearchCourses);
		pageContext.setAttribute("showCourseUserEnrollment",
				showCourseUserEnrollment);
		pageContext.setAttribute("showSearchCourseUserEnrollment",
				showSearchCourseUserEnrollment);
	} else {
		pageContext.setAttribute("showHome", false);
		pageContext.setAttribute("showCourse", false);
		pageContext.setAttribute("showProfile", false);
		pageContext.setAttribute("showFeedback", false);
		pageContext.setAttribute("showMessage", false);
		pageContext.setAttribute("showStudentSupport", false);
		pageContext.setAttribute("showQuicklinks", false);
		pageContext.setAttribute("showForum", false);
		pageContext.setAttribute("showCalendar", false);
		pageContext.setAttribute("showAssignment", false);
		pageContext.setAttribute("showAnnouncement", false);
		pageContext.setAttribute("showGroup", false);

		pageContext.setAttribute("showTest", false);
		pageContext.setAttribute("showCourseMenu", false);
		pageContext.setAttribute("showManageUser", false);
		pageContext.setAttribute("showGrievances", false);
		pageContext.setAttribute("showSetup", false);
		pageContext.setAttribute("showContent", false);

		pageContext.setAttribute("showAddFeedback", false);
		pageContext.setAttribute("showAllocateFeedback", false);
		pageContext.setAttribute("showSearchFeedback", false);
		pageContext.setAttribute("showViewFeedback", false);
		pageContext.setAttribute("showFeedbackList", false);
		pageContext.setAttribute("showCreateMessage", false);
		pageContext.setAttribute("showViewMessage", false);
		pageContext.setAttribute("showRaiseGrieviance", false);
		pageContext.setAttribute("showOverview", false);
		pageContext.setAttribute("showPrieviewGrievianceResponseList",
				false);
		pageContext.setAttribute("showViewFeedbackQlinks", false);
		pageContext.setAttribute("showViewTestQlinks", false);
		pageContext.setAttribute(
				"showPrieviewGrievianceResponseListQlinks", false);
		pageContext.setAttribute("showOverviewQlinks", false);
		pageContext.setAttribute("showCreateForum", false);

		pageContext.setAttribute("showViewsForum", false);
		pageContext.setAttribute("showAddEvents", false);
		pageContext.setAttribute("showViewEvents", false);
		pageContext.setAttribute("showCreateAnnouncement", false);
		pageContext.setAttribute("showSearchAnnouncements", false);
		pageContext.setAttribute("showViewAnnouncements", false);

		pageContext.setAttribute("showCreateAssignment", false);
		pageContext.setAttribute("showCreateAssignmentsForGroups",
				false);
		pageContext.setAttribute("showViewAssignment", false);
		pageContext.setAttribute(
				"showEvaluateAssignmentsAdvancedSearch", false);
		pageContext.setAttribute("showEvaluateAssignments", false);
		pageContext.setAttribute("showLateSubmittedAssignments", false);
		pageContext.setAttribute("showAssigmentList", false);

		pageContext.setAttribute("showCreateGroup", false);
		pageContext.setAttribute("showViewGroup", false);
		pageContext.setAttribute("showGroupList", false);
		pageContext.setAttribute("showCreateTest", false);
		pageContext.setAttribute("showViewTest", false);
		pageContext.setAttribute("showUpdateTest", false);
		pageContext.setAttribute("showTestList", false);

		pageContext.setAttribute("showCreateSingleUser", false);
		pageContext.setAttribute("showUploadStudentExcel", false);
		pageContext.setAttribute("showUploadFacultyExcel", false);
		pageContext.setAttribute("showViewGrievances", false);
		pageContext.setAttribute("showMakeAcademicCycleLive", false);
		pageContext.setAttribute("showStudentContentList", false);
		pageContext.setAttribute("showGetContentList", false);

		pageContext.setAttribute("showAddSingleCourse", false);
		pageContext.setAttribute("showAddCourseOverview", false);
		pageContext.setAttribute("showUploadCourse", false);
		pageContext.setAttribute("showSearchCourses", false);
		pageContext.setAttribute("showCourseUserEnrollment", false);
		pageContext.setAttribute("showSearchCourseUserEnrollment",
				false);
	}
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





						<c:if test="${showFeedback}">
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




						<c:if test="${showAssignment}">
							<li class="dropdown"><a href="#" class="dropdown-toggle"
								data-toggle="dropdown" role="button" aria-expanded="false">
									Assignment <span class="caret"></span>
							</a>
								<ul class="dropdown-menu" role="menu">
									<c:if test="${showCreateAssignment}">
										<li><a href="<c:url value="createAssignmentFromMenu" />">Create
												Assignments</a></li>
									</c:if>
									<c:if test="${showCreateAssignmentsForGroups}">
										<li><a href="<c:url value="createAssignmentFromGroup" />">Create
												Assignments For Groups</a></li>
									</c:if>
									<c:if test="${showViewAssignment}">
										<li><a href="<c:url value="searchFacultyAssignment" />">View
												Assignments</a></li>
									</c:if>
									<c:if test="${showEvaluateAssignmentsAdvancedSearch}">
										<li><a
											href="<c:url value="searchAssignmentToEvaluate" />">Evaluate
												Assignments With Advanced Search</a></li>
									</c:if>
									<c:if test="${showEvaluateAssignments}">
										<li><a href="<c:url value="evaluateByStudentForm" />">Evaluate
												Assignments for Students</a></li>
									</c:if>
									<c:if test="${showLateSubmittedAssignments}">
										<li><a
											href="<c:url value="lateSubmissionApprovalForm" />"> Late
												Submitted Assignments</a></li>
									</c:if>
									<c:if test="${showAssigmentList}">
										<li><a href="<c:url value="nonSubmittedStudentsForm" />">
												Not Submitted Students</a></li>
									</c:if>
									<!--                                                        <li><a -->
									<%--                                                              href="<c:url value="/evaluateByStudentFormGroup" />">Evaluate --%>
									<!--                                                                    
 For Groups</a></li> -->

								</ul></li>
						</c:if>

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


						<c:if test="${showCourseMenu}">
							<li class="dropdown"><a href="#" class="dropdown-toggle"
								data-toggle="dropdown" role="button" aria-expanded="false">
									<img src="resources/images/feedback.png" height="30%"
									alt="Feedback"
									style="border-radius: 10px; height: 26px; width: 23px; padding: 1px;">Course
									<span class="caret"></span>
							</a>
								<ul class="dropdown-menu" role="menu">
									<c:if test="${showAddSingleCourse}">
										<li><a href="<c:url value="addCourseForm" />">Add
												Single Course</a></li>
									</c:if>
									<c:if test="${showAddCourseOverview}">
										<li><a href="<c:url value="addCourseOverviewForm" />">Add
												Course Overview</a></li>
									</c:if>
									<c:if test="${showUploadCourse}">
										<li><a href="<c:url value="uploadCourseForm" />">Upload
												Course Excel</a></li>
									</c:if>
									<c:if test="${showSearchCourses}">
										<li><a href="<c:url value="searchCourse" />">Search
												Courses</a></li>
									</c:if>
									<c:if test="${showCourseUserEnrollment}">
										<li><a href="<c:url value="uploadUserCourseForm" />">Course
												User Enrollment</a></li>
									</c:if>
									<c:if test="${showSearchCourseUserEnrollment}">
										<li><a href="<c:url value="searchUserCourse" />">Search
												Course User Enrollment</a></li>
									</c:if>
									<%-- <li><a href="<c:url value="/addFacultyCourseForm" />">Course Faculty Enrollment-Not needed</a></li> --%>
								</ul></li>
						</c:if>

						<c:if test="${showManageUser}">
							<li class="dropdown"><a href="#" class="dropdown-toggle"
								data-toggle="dropdown" role="button" aria-expanded="false">
									<img src="resources/images/feedback.png" height="30%"
									alt="Feedback"
									style="border-radius: 10px; height: 26px; width: 23px; padding: 1px;">Manage
									Users <span class="caret"></span>
							</a>
								<ul class="dropdown-menu" role="menu">
									<c:if test="${showCreateSingleUser}">
										<li><a href="<c:url value="addUserForm" />">Create
												Single User</a></li>
									</c:if>
									<c:if test="${showUploadStudentExcel}">
										<li><a href="<c:url value="uploadStudentForm" />">Upload
												Students Excel</a></li>
									</c:if>
									<c:if test="${showUploadStudentExcel}">
										<li><a href="<c:url value="uploadFacultyForm" />">Upload
												Faculty Excel</a></li>
									</c:if>
								</ul></li>
						</c:if>


						<c:if test="${showAnnouncement}">
							<li class="dropdown"><a href="#" class="dropdown-toggle"
								data-toggle="dropdown" role="button" aria-expanded="false">
									Announcements <span class="caret"></span>
							</a>
								<ul class="dropdown-menu" role="menu">
									<c:if test="${showCreateAnnouncement}">
										<li><a href="<c:url value="addAnnouncementForm" />">Create
												Announcement</a></li>
									</c:if>
									<c:if test="${showSearchAnnouncements}">
										<li><a href="<c:url value="searchAnnouncement" />">Search
												Announcements</a></li>
									</c:if>
								</ul></li>
						</c:if>


						<c:if test="${showMessage}">
							<li class="dropdown"><a href="#" class="dropdown-toggle"
								data-toggle="dropdown" role="button" aria-expanded="false">
									Message <span class="caret"></span>
							</a>
								<ul class="dropdown-menu" role="menu">
									<c:if test="${showCreateMessage}">
										<li><a href="<c:url value="createMessage" />">Create
												Message</a></li>
									</c:if>
									<c:if test="${showViewMessage}">
										<li><a href="<c:url value="viewMessage" />">View
												Message</a></li>
									</c:if>
								</ul></li>
						</c:if>

						<c:if test="${showGroup}">
							<li class="dropdown"><a href="#" class="dropdown-toggle"
								data-toggle="dropdown" role="button" aria-expanded="false">

									Group <span class="caret"></span>
							</a>
								<ul class="dropdown-menu" role="menu">
									<c:if test="${showCreateGroup}">
										<li><a
											href="<c:url value="createGroupForm?courseId=${course.id}" />">Create
												Group</a></li>
									</c:if>
									<c:if test="${showViewGroup}">
										<li><a href="<c:url value="searchFacultyGroups" />">View
												Group</a></li>
									</c:if>


								</ul></li>
						</c:if>
						
						
						<c:if test="${showAttendence}">
							<li class="dropdown"><a href="#" class="dropdown-toggle"
								data-toggle="dropdown" role="button" aria-expanded="false">

									Attendence<span class="caret"></span>
							</a>
								<ul class="dropdown-menu" role="menu">
									<c:if test="${showMarkAttendence}">
										<li><a
											href="<c:url value="addAttendanceForm" />">
												Mark Attendence</a></li>
									</c:if>
									<c:if test="${showViewAttendence}">
										<li><a href="<c:url value="viewDailyAttendance" />">View
												Attendence</a></li>
									</c:if>


								</ul></li>
						</c:if>
						
						
						
						<%-- <c:if test="${showAttendence}">
								<li class="dropdown"><a href="#" class="dropdown-toggle"
									data-toggle="dropdown" role="button" aria-expanded="false">
										Content <span class="caret"></span>
								</a>
									<ul class="dropdown-menu" role="menu">
										<li><a
											href="<c:url value="addAttendanceForm" />">Mark
												Attendance</a></li>
										
									</ul></li>
							</c:if> --%>

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



						<c:if test="${showStudentSupport}">
							<li class="dropdown"><a href="#" class="dropdown-toggle"
								data-toggle="dropdown" role="button" aria-expanded="false">
									<img src="resources/images/support.png" height="30%"
									alt="Support"
									style="border-radius: 10px; height: 26px; width: 23px; padding: 1px;">Student
									Support <span class="caret"></span>
							</a>
								<ul class="dropdown-menu" role="menu">
									<c:if test="${showRaiseGrieviance}">
										<li><a href="<c:url value="grievanceForm" />">Raise
												Grievance </a></li>
									</c:if>
									<c:if test="${showOverview}">
										<li><a
											href="<c:url value="overview?courseId=${course.id}" />">Overview
										</a></li>
									</c:if>
									<c:if test="${showPrieviewGrievianceResponseList}">
										<li><a href="<c:url value="viewGrievanceResponse" />">Preview
												Grievances Response List </a></li>
									</c:if>
								</ul></li>
						</c:if>




						<c:if test="${showQuicklinks}">
							<li class="dropdown"><a href="#" class="dropdown-toggle"
								data-toggle="dropdown" role="button" aria-expanded="false">
									<img src="resources/images/quicklink.png" height="30%"
									alt="Quicklink"
									style="border-radius: 10px; height: 26px; width: 23px; padding: 1px;">Quicklinks
									<span class="caret"></span>
							</a>
								<ul class="dropdown-menu" role="menu">
									<c:if test="${showViewFeedbackQlinks}">
										<li><a href="<c:url value="searchStudentFeedback" />">View
												Feedback</a></li>
									</c:if>
									<c:if test="${showViewTest}">
										<li><a href="<c:url value="testList" />">View Test</a></li>
									</c:if>
									<c:if test="${showViewLibrary}">
										<li><a href="<c:url value="viewLibrary" />">View
												Library</a></li>
									</c:if>
									<c:if test="${showPrieviewGrievianceResponseListQlinks}">
										<li><a href="<c:url value="viewGrievanceResponse" />">Preview
												Grievances Response List </a></li>
									</c:if>
									<c:if test="${showOverviewQlinks}">
										<li><a href="<c:url value="overview" />"> Overview</a></li>
									</c:if>

								</ul></li>
						</c:if>





						<c:if test="${showGrievances}">
							<li class="dropdown"><a href="#" class="dropdown-toggle"
								data-toggle="dropdown" role="button" aria-expanded="false">
									<img src="resources/images/feedback.png" height="30%"
									alt="Feedback"
									style="border-radius: 10px; height: 26px; width: 23px; padding: 1px;">Grievances
									<span class="caret"></span>
							</a>
								<ul class="dropdown-menu" role="menu">
									<c:if test="${showViewGrievances}">
										<li><a href="<c:url value="viewAllGrievances" />">View
												Grievances </a></li>
									</c:if>
								</ul></li>
						</c:if>



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
						
						
						 <!-- ROLE_CORD HEADER MENU -->
						   
                                          <%-- <sec:authorize access="hasRole('ROLE_CORD')">
												 <li><a class="page-scroll" href="homepage"> <img
                                                      src="resources/images/home.jpg" height="30%" alt="Home"
                                                      style="border-radius: 10px; height: 26px; width: 23px; padding: 1px;">
                                                      Home
                                          </a></li>
                                          </sec:authorize>
                                          
                                          
                                          
                                          
                                          	<sec:authorize access="hasRole('ROLE_CORD')">
								<li class="dropdown"><a href="#" class="dropdown-toggle"
									data-toggle="dropdown" role="button" aria-expanded="false">
										Content <span class="caret"></span>
								</a>
									<ul class="dropdown-menu" role="menu">
				
										<li class="divider"></li>
										<li><a
											href="<c:url value="getContentUnderAPath?courseId=1" />">View
												Content</a></li>
										<li><a href="<c:url value="/searchTest" />">View Test</a></li>
									</ul></li>
							</sec:authorize> 
							
							  <sec:authorize access="hasRole('ROLE_CORD')">
                                                <li class="dropdown"><a href="#" class="dropdown-toggle"
                                                      data-toggle="dropdown" role="button" aria-expanded="false">Test
                                                            <span class="caret"></span>
                                                </a>
                                                      <ul class="dropdown-menu" role="menu">
                                                    
                                                            <li><a href="<c:url value="/testList" />">View Test</a></li>
                
                                                      </ul></li>
                                          </sec:authorize>
                                          
                                            <sec:authorize access="hasRole('ROLE_CORD')">
                                                <li class="dropdown"><a href="#" class="dropdown-toggle"
                                                      data-toggle="dropdown" role="button" aria-expanded="false">Attendance
                                                            <span class="caret"></span>
                                                </a>
                                                      <ul class="dropdown-menu" role="menu">
                                                    
                                                            <li><a href="<c:url value="addAttendanceForm" />">Mark Attendance</a></li>
                
                                                      </ul></li>
                                          </sec:authorize>
                                          
                                          <sec:authorize access="hasRole('ROLE_CORD')">
							<li class="dropdown"><a href="#" class="dropdown-toggle"
								data-toggle="dropdown" role="button" aria-expanded="false">
									<img src="resources/images/course.jpg" height="30%"
									alt="Course"
									style="border-radius: 10px; height: 26px; width: 23px; padding: 1px;">My
									Courses <span class="caret"></span>
							</a>
								<ul class="dropdown-menu" role="menu">
									<li><a href="<c:url value="viewCourse?id=99" />">Engineering
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

						</sec:authorize>
						
						<sec:authorize access="hasRole('ROLE_CORD')">
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
									<li><a class="page-scroll" href="${pageContext.request.contextPath}/loggedout">Logout</a></li>
								</ul></li>
								</sec:authorize>
								
									<sec:authorize access="hasRole('ROLE_CORD')">
								<li class="dropdown"><a href="#" class="dropdown-toggle"
									data-toggle="dropdown" role="button" aria-expanded="false">
										Assignment <span class="caret"></span>
								</a>
									<ul class="dropdown-menu" role="menu">
										<li><a href="<c:url value="searchFacultyAssignment" />">View
												Assignments</a></li>
							
									
									</ul></li>
							</sec:authorize>
							
							<sec:authorize access="hasRole('ROLE_CORD')">
								<li class="dropdown"><a href="#" class="dropdown-toggle"
									data-toggle="dropdown" role="button" aria-expanded="false">
										Announcements <span class="caret"></span>
								</a>
									<ul class="dropdown-menu" role="menu">
										<li><a href="<c:url value="searchAnnouncement" />">Search
												Announcements</a></li>
									</ul></li>
							</sec:authorize>
							
							
							<sec:authorize access="hasRole('ROLE_CORD')">
								<li class="dropdown"><a href="#" class="dropdown-toggle"
									data-toggle="dropdown" role="button" aria-expanded="false">

										Group <span class="caret"></span>
								</a>
									<ul class="dropdown-menu" role="menu">
										<li><a href="<c:url value="searchFacultyGroups" />">View
												Group</a></li>


									</ul></li>
							</sec:authorize>

				<!-- ROLE_CORD HEADER ENDS -->
				
				<!-- ROLE_AR HEADER STARTS -->
				<sec:authorize access="hasRole('ROLE_AR')">
								<li class="dropdown"><a href="#" class="dropdown-toggle"
									data-toggle="dropdown" role="button" aria-expanded="false">
									<img src="resources/images/course.jpg" height="30%"
									alt="Course"
									style="border-radius: 10px; height: 26px; width: 23px; padding: 1px;">
										Announcements <span class="caret"></span>
								</a>
									<ul class="dropdown-menu" role="menu">
									
										<li><a href="<c:url value="searchAnnouncement" />">Search
												Announcements</a></li>
									</ul></li>
							</sec:authorize>
							
							
							
	
							<sec:authorize access="hasRole('ROLE_AR')">
								<li class="dropdown"><a href="#" class="dropdown-toggle"
									data-toggle="dropdown" role="button" aria-expanded="false">
<img src="resources/images/course.jpg" height="30%"
									alt="Course"
									style="border-radius: 10px; height: 26px; width: 23px; padding: 1px;">
										Group <span class="caret"></span>
								</a>
									<ul class="dropdown-menu" role="menu">
										
										<li><a href="<c:url value="searchFacultyGroups" />">View
												Group</a></li>


									</ul></li>
							</sec:authorize>
							
							
							
		<sec:authorize access="hasRole('ROLE_AR')">
						<li class="dropdown"><a href="#" class="dropdown-toggle"
								data-toggle="dropdown" role="button" aria-expanded="false">
								<img src="resources/images/course.jpg" height="30%"
									alt="Course"
									style="border-radius: 10px; height: 26px; width: 23px; padding: 1px;">
									Profile <span class="caret"></span>
							</a>
								<ul class="dropdown-menu" role="menu">
									<li>
									<li><a href="<c:url value="changePasswordForm" />">Change
											Password</a></li>
									<li><a href="<c:url value="updateProfileForm" />">Update
											Profile</a></li>
									<li><a class="page-scroll" href="${pageContext.request.contextPath}/loggedout">Logout</a></li>
								</ul></li>
						
						</sec:authorize>
						
						
						
<sec:authorize access="hasRole('ROLE_AR')">
							<li class="dropdown"><a href="#" class="dropdown-toggle"
								data-toggle="dropdown" role="button" aria-expanded="false">
									<img src="resources/images/course.jpg" height="30%"
									alt="Course"
									style="border-radius: 10px; height: 26px; width: 23px; padding: 1px;">My
									Courses <span class="caret"></span>
							</a>
								<ul class="dropdown-menu" role="menu">
									<li><a href="<c:url value="viewCourse?id=99" />">Engineering
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

						</sec:authorize>
						
						
						
	<sec:authorize access="hasRole('ROLE_AR')">
								<li class="dropdown"><a href="#" class="dropdown-toggle"
									data-toggle="dropdown" role="button" aria-expanded="false">
									<img src="resources/images/course.jpg" height="30%"
									alt="Course"
									style="border-radius: 10px; height: 26px; width: 23px; padding: 1px;">
										Assignment <span class="caret"></span>
								</a>
									<ul class="dropdown-menu" role="menu">
										<li><a href="<c:url value="searchFacultyAssignment" />">View
												Assignments</a></li>
										
												
												<li><a href="<c:url value="lateSubmissionApprovalForm" />">
											Late Submitted Assignments</a></li>
									
									</ul></li>
							</sec:authorize>
							
							
			<sec:authorize access="hasRole('ROLE_AR')">
								<li class="dropdown"><a href="#" class="dropdown-toggle"
									data-toggle="dropdown" role="button" aria-expanded="false">
										Message <span class="caret"></span>
										<img src="resources/images/course.jpg" height="30%"
									alt="Course"
									style="border-radius: 10px; height: 26px; width: 23px; padding: 1px;">
								</a>
									<ul class="dropdown-menu" role="menu">
									
										<li><a href="<c:url value="viewMyMessage" />">View
												Message</a></li>
									</ul></li>
							</sec:authorize>
							 <!-- ROLE_AR HEADER ENDS --> --%>

						<c:if test="${showTest}">
							<li class="dropdown"><a href="#" class="dropdown-toggle"
								data-toggle="dropdown" role="button" aria-expanded="false">Test
									<span class="caret"></span>
							</a>
								<ul class="dropdown-menu" role="menu">
									<c:if test="${showCreateTest}">
										<li><a href="<c:url value="/addTestFromMenu" />">
												Create Test</a></li>
									</c:if>
									<c:if test="${showViewTest}">
										<li><a href="<c:url value="/testList" />">View Test</a></li>
									</c:if>
									<c:if test="${showUpdateTest}">
										<li><a
											href="${pageContext.request.contextPath}/searchTest">Update
												Test</a></li>
									</c:if>
									<c:if test="${showConfigureQuestions}">
										<li><a
											href="${pageContext.request.contextPath}/configureQuestions">Configure
												Questions</a></li>
									</c:if>
								</ul></li>
						</c:if>


						<c:if test="${showForum}">


							<li class="dropdown"><a href="#" class="dropdown-toggle"
								data-toggle="dropdown" role="button" aria-expanded="false">
									<img src="resources/images/forum.jpg" height="30%" alt="Task"
									style="border-radius: 10px; height: 26px; width: 23px; padding: 1px;">
									Discussion Forum <span class="caret"></span>
							</a>
								<ul class="dropdown-menu" role="menu">
									<c:if test="${showCreateForum}">
										<li><a href="<c:url value="createForumForm" />">Create
												Forum </a></li>
									</c:if>
									<c:if test="${showViewsForum}">
										<li><a href="<c:url value="viewForum" />">View Forum
										</a></li>
									</c:if>

								</ul></li>
						</c:if>
						
						
					






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


						<c:if test="${showCalendar}">
							<li class="dropdown"><a href="#" class="dropdown-toggle"
								data-toggle="dropdown" role="button" aria-expanded="false">
									<img src="resources/images/calendar.png" height="30%"
									alt="Calendar"
									style="border-radius: 10px; height: 26px; width: 23px; padding: 1px;">Calendar<span
									class="caret"></span>
							</a>
								<ul class="dropdown-menu" role="menu">
									<c:if test="${showAddEvents}">
										<li><a href="<c:url value="addCalenderEventForm" />">Add
												Event</a></li>
									</c:if>
									<c:if test="${showViewEvents}">
										<li><a href="<c:url value="viewEvents" />">View Event</a></li>
									</c:if>

									<%-- <li><a href="<c:url value="/addFacultyCourseForm" />">Course Faculty Enrollment-Not needed</a></li> --%>
								</ul></li>
						</c:if>



						<c:if test="${showSetup}">
							<li class="dropdown"><a href="#" class="dropdown-toggle"
								data-toggle="dropdown" role="button" aria-expanded="false">
									<img src="resources/images/feedback.png" height="30%"
									alt="Feedback"
									style="border-radius: 10px; height: 26px; width: 23px; padding: 1px;">SetUp
									<span class="caret"></span>
							</a>
								<ul class="dropdown-menu" role="menu">
									<c:if test="${showMakeAcademicCycleLive}">
										<li><a href="<c:url value="addInstituteCycleForm" />">Make
												Academic Cycle LIVE!</a></li>
									</c:if>
								</ul></li>
						</c:if>
						<c:if test="${showProfile}">

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
						</c:if>





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