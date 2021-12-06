<%@page import="java.util.*"%>
<%@page import="com.spts.lms.beans.dashboard.DashBoard"%>
<%@page import="com.spts.lms.beans.course.Course"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>


<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%
	String redirectURL = "https://portal.svkm.ac.in/usermgmt/login";
	response.setStatus(response.SC_MOVED_TEMPORARILY);
	response.setHeader("Location", redirectURL);
%>

<meta http-equiv="refresh"
	content="<%=session.getMaxInactiveInterval()%>;url=https://portal.svkm.ac.in/usermgmt/login" />

<!-- top navigation -->

<!-- Global site tag (gtag.js) - Google Analytics -->
<script async
	src="https://www.googletagmanager.com/gtag/js?id=UA-125010959-1"></script>
<script>
	window.dataLayer = window.dataLayer || [];
	function gtag() {
		dataLayer.push(arguments);
	}
	gtag('js', new Date());

	gtag('config', 'UA-125010959-1');
</script>
<%
	String activeMenu = (String) request.getParameter("activeMenu");
	String dashboard = "";
	String messageMenuClass = "";
	String testMenuClass = "";
	String assignmentMenuClass = "";
	String announcementMenuClass = "";
	String courseMenuClass = "";
	String libraryMenuClass = "";
	String gradeMenuClass = "";
	String forumMenuClass = "";
	String calendarMenuClass = "";
%>


<%
	if (activeMenu != null) {
		if ("Message".equals(activeMenu)) {
	messageMenuClass = "on";
		}
		if ("Test".equals(activeMenu)) {
	testMenuClass = "on";
		}
		if ("Assignment".equals(activeMenu)) {
	assignmentMenuClass = "on";
		}
		if ("Announcement".equals(activeMenu)) {
	announcementMenuClass = "on";
		}
		if ("Course".equals(activeMenu)) {
	courseMenuClass = "on";
		}
		if ("Library".equals(activeMenu)) {
	libraryMenuClass = "on";
		}
		if ("Grades".equals(activeMenu)) {
	gradeMenuClass = "on";
		}
		if ("Forum".equals(activeMenu)) {
	forumMenuClass = "on";
		}
		if ("Calendar".equals(activeMenu)) {
	calendarMenuClass = "on";
		}
	} else {
		dashboard = "on";
	}
%>

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
		boolean showSearch = (menuRights.containsKey("Search"));
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
		boolean showGrievance = (menuRights.containsKey("Grievance"));
		boolean showSetup = (menuRights.containsKey("Setup"));
		boolean showContent = (menuRights.containsKey("Content"));
		boolean showCourseMenu = (menuRights.containsKey("Course"));
		boolean showAttendence = (menuRights.containsKey("Attendence"));
		boolean showLibrary = (menuRights.containsKey("Library"));
		boolean showGrade = (menuRights.containsKey("Grades"));

		boolean showMarkAttendence = showAttendence ? menuRights.get(
		"Attendence").contains("MarkAttendence") : false;
		boolean showViewAttendence = showAttendence ? menuRights.get(
		"Attendence").contains("ViewAttendence") : false;
		boolean showAssTestSearch = showSearch ? menuRights.get(
		"Search").contains("AssignmentTestSearch") : false;
		boolean showGenericSearch = showSearch ? menuRights.get(
		"Search").contains("GenericSearch") : false;
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
		"Message").contains("CreateMessage") : false;
		boolean showViewMessage = showMessage ? menuRights.get(
		"Message").contains("ViewMessage") : false;

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

		boolean showViewGrievance = showGrievance ? menuRights.get(
		"Grievance").contains("ViewAllGrievances") : false;

		boolean showMakeAcademicCycleLive = showSetup ? menuRights.get(
		"Setup").contains("MakeAcademicCycleLive") : false;

		boolean showStudentContentList = showContent ? menuRights.get(
		"Content").contains("StudentContentList") : false;
		boolean showGetContentList = showContent ? menuRights.get(
		"Content").contains("GetContentList") : false;

		boolean showLibraryList = showLibrary ? menuRights.get(
		"Library").contains("ViewLibraryList") : false;

		boolean addLibraryList = showLibrary ? menuRights
		.get("Library").contains("AddLibraryList") : false;

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
		pageContext.setAttribute("showAttendence", showAttendence);
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
		pageContext.setAttribute("showSearch", showSearch);
		pageContext
		.setAttribute("showAssTestSearch", showAssTestSearch);
		pageContext
		.setAttribute("showGenericSearch", showGenericSearch);
		pageContext.setAttribute("showCreateGroup", showCreateGroup);
		pageContext.setAttribute("showViewGroup", showViewGroup);
		pageContext.setAttribute("showGroupList", showGroupList);
		pageContext.setAttribute("showCreateTest", showCreateTest);
		pageContext.setAttribute("showViewTest", showViewTest);
		pageContext.setAttribute("showUpdateTest", showUpdateTest);
		pageContext.setAttribute("showTestList", showTestList);
		pageContext.setAttribute("showGrade", showGrade);

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

		pageContext.setAttribute("showMarkAttendence",
		showMarkAttendence);
		pageContext.setAttribute("showViewAttendence",
		showViewAttendence);
		pageContext.setAttribute("showLibrary", showLibrary);
		pageContext.setAttribute("showGrievance", showGrievance);
		pageContext.setAttribute("showLibraryList", showLibraryList);
		pageContext.setAttribute("addLibraryList", addLibraryList);
	} else {
		pageContext.setAttribute("showSearch", false);

		pageContext.setAttribute("showAssTestSearch", false);
		pageContext.setAttribute("showGenericSearch", false);
		pageContext.setAttribute("showMarkAttendence", false);
		pageContext.setAttribute("showViewAttendence", false);
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
		pageContext.setAttribute("showLibrary", false);
		pageContext.setAttribute("showLibraryList", false);
		pageContext.setAttribute("addLibraryList", false);
	}
%>

<div class="container-fluid m-0 p-0 dashboardWraper">
	<div class="topFixx">
		<div class="headTop container-fluid">
			<div class="container p-1">
				<div class="d-flex">
					<div class="mr-auto">
						<c:choose>
							<c:when test="${instiFlag eq 'nm'}">
								<small class="text-white pb-0 mb-0">Welcome to NMIMS</small>
							</c:when>
							<c:otherwise>
								<small class="text-white pb-0 mb-0">Welcome to SVKM</small>
							</c:otherwise>
						</c:choose>
					</div>
					<div>
						<ul class="list-unstyled d-flex mb-0">
							<li class="pl-2"><a href="#"><span class="fa-round"><i
										class="fab fa-facebook-f"></i></span></a></li>
							<li class="pl-2"><a href="#"><span class="fa-round"><i
										class="fab fa-google-plus-g"></i></span></a></li>
							<li class="pl-2"><a href="#"><span class="fa-round"><i
										class="fab fa-twitter"></i></span></a></li>
							<li class="pl-2"><a href="#"><span class="fa-round"><i
										class="fab fa-pinterest-p"></i></span></a></li>
							<li class="pl-2"><a href="#"><span class="fa-round"><i
										class="fab fa-instagram"></i></span></a></li>
							<li class="nav-item dropdown pl-2 notif"><a href="#"
								role="button" data-toggle="dropdown" aria-haspopup="true"
								aria-expanded="false"> <span class="notifNew">${announcementForTopMenu.size()}</span>
									<span class="fa-round"> <i class="fas fa-bell"></i>
								</span>
							</a>
								<div id="navDropdownNotifications"
									class="dropdown-menu dropdown-menu-right"
									aria-labelledby="navbarDropdownNotifications">
									<a class="dropdown-item" href="#">${announcement.description}</a>
									<a class="dropdown-item" href="#">Some important
										notification</a> <a class="dropdown-item" href="#">Some
										important notification</a>
									<div class="dropdown-divider"></div>
									<a class="dropdown-item" href="#">View All</a>
								</div></li>
						</ul>
					</div>
				</div>
			</div>
		</div>
		<header class="container-fluid sticky-top">
			<nav class="navbar navbar-expand-lg navbar-light p-0">
				<i class="fas fa-bars fa-2x mr-3 leftnav-btn" data-toggle="modal"
					data-target="#leftnav"></i> <a class="navbar-brand" href="homepage">
					<c:choose>
						<c:when test="${instiFlag eq 'nm'}">
							<img src="<c:url value="/resources/images/logo.png" />"
								class="logo" title="NMIMS logo" alt="NMIMS logo" />
						</c:when>
						<c:otherwise>
							<img src="<c:url value="/resources/images/svkmlogo.png" />"
								class="logo" title="SVKM logo" alt="SVKM logo" />
						</c:otherwise>
					</c:choose>
				</a>
				<button class="navbar-toggler" type="button" data-toggle="modal"
					data-target="#rightnav">
					<i class="fas fa-bars"></i>
				</button>

				<div class="collapse navbar-collapse" id="navbarSupportedContent">
					<ul class="navbar-nav ml-auto">


						<c:if test="${showHome}">
							<li id="home" class="nav-item active" data-toggle="tooltip"
								data-placement="bottom" title="Home"><a class="nav-link"
								href="homepage"> <i class="fas fa-home"></i>

							</a></li>
						</c:if>






						<c:if test="${showCourse}">
							<li class="nav-item dropdown"><a
								class="nav-link dropdown-toggle" href="#" role="button"
								data-toggle="dropdown" aria-haspopup="true"
								aria-expanded="false"> <i class="fab fa-wpforms"
									data-toggle="tooltip" data-placement="bottom" title="Course"></i>
							</a>
								<div id="navDropdownSupport"
									class="dropdown-menu dropdown-menu-right"
									aria-labelledby="navbarDropdown">

									<c:forEach items="${courseDetailList}" var="courseDetail">
										<a class="dropdown-item"
											href="viewCourse?id=${courseDetail.course.id}">${courseDetail.course.courseName}</a>
									</c:forEach>
								</div></li>
						</c:if>





						<c:if test="${showFeedback}">

							<sec:authorize access="hasAnyRole('ROLE_ADMIN')">
								<li class="nav-item dropdown"><a
									class="nav-link dropdown-toggle" href="#" role="button"
									data-toggle="dropdown" aria-haspopup="true"
									aria-expanded="false"><i class="fa fa-reply-all"
										data-toggle="tooltip" data-placement="bottom" title="Feedback"></i>
								</a>
									<div id="navDropdownSupport"
										class="dropdown-menu dropdown-menu-right"
										aria-labelledby="navbarDropdown">


										<a class="dropdown-item"
											href="<c:url value="addFeedbackForm" />">Add Feedback</a> <a
											class="dropdown-item" href="<c:url value="searchFeedback" />">View
											Feedback</a> <a class="dropdown-item"
											href="<c:url value="addStudentFeedbackForm" />">Allocate
											Feedback</a>
									</div></li>







							</sec:authorize>
						</c:if>





						<c:if test="${showAssignment}">
							<li class="nav-item dropdown"><a
								class="nav-link dropdown-toggle" href="#" role="button"
								data-toggle="dropdown" aria-haspopup="true"
								aria-expanded="false"><i class="fas fa-newspaper"
									data-toggle="tooltip" data-placement="bottom"
									title="Assignments"></i> </a>
								<div id="navDropdownSupport"
									class="dropdown-menu dropdown-menu-right"
									aria-labelledby="navbarDropdown">

									<c:if test="${showViewAssignment}">
										<a class="dropdown-item"
											href="<c:url value="searchFacultyAssignment" />">View
											Assignments</a>
										<a class="dropdown-item" href="<c:url value="searchFacultyAssignmentForModule" />">View Assignments For Module</a>
									</c:if>
									<c:if test="${showEvaluateAssignmentsAdvancedSearch}">
										<a class="dropdown-item"
											href="<c:url value="searchAssignmentToEvaluate" />">Evaluate
											Assignments With Advanced Search</a>
									</c:if>
									<c:if test="${showEvaluateAssignments}">
										<a class="dropdown-item"
											href="<c:url value="evaluateByStudentForm" />">Evaluate
											Assignments for Students</a>
									</c:if>
									<c:if test="${showLateSubmittedAssignments}">
										<a class="dropdown-item"
											href="<c:url value="lateSubmissionApprovalForm" />"> Late
											Submitted Assignments</a>
									</c:if>
									<c:if test="${showAssigmentList}">
										<a class="dropdown-item"
											href="<c:url value="nonSubmittedStudentsForm" />"> Not
											Submitted Students</a>
									</c:if>
								</div></li>
						</c:if>



						<c:if test="${showCourseMenu}">
							<li class="nav-item dropdown"><a
								class="nav-link dropdown-toggle" href="#" role="button"
								data-toggle="dropdown" aria-haspopup="true"
								aria-expanded="false"><i class="fas fa-newspaper"
									data-toggle="tooltip" data-placement="bottom" title="Course"></i>
							</a>

								<div id="navDropdownSupport"
									class="dropdown-menu dropdown-menu-right"
									aria-labelledby="navbarDropdown">


									<c:if test="${showAddSingleCourse}">
										<a class="dropdown-item"
											href="<c:url value="addCourseForm" />">Add Single Course</a>
									</c:if>
									<c:if test="${showAddCourseOverview}">
										<a class="dropdown-item"
											href="<c:url value="addCourseOverviewForm" />">Add Course
											Overview</a>
									</c:if>
									<c:if test="${showUploadCourse}">
										<a class="dropdown-item"
											href="<c:url value="uploadCourseForm" />">Upload Course
											Excel</a>
									</c:if>
									<c:if test="${showSearchCourses}">
										<a class="dropdown-item" href="<c:url value="searchCourse" />">Search
											Courses</a>
									</c:if>
									<c:if test="${showCourseUserEnrollment}">
										<a class="dropdown-item"
											href="<c:url value="uploadUserCourseForm" />">Course User
											Enrollment</a>
									</c:if>
									<c:if test="${showSearchCourseUserEnrollment}">
										<a class="dropdown-item"
											href="<c:url value="searchUserCourse" />">Search Course
											User Enrollment</a>
									</c:if>
								</div></li>
						</c:if>


						<c:if test="${showManageUser}">
							<li class="nav-item dropdown"><a
								class="nav-link dropdown-toggle" href="#" role="button"
								data-toggle="dropdown" aria-haspopup="true"
								aria-expanded="false"><i class="fas fa-newspaper"
									data-toggle="tooltip" data-placement="bottom" title="User"></i>
							</a>

								<div id="navDropdownSupport"
									class="dropdown-menu dropdown-menu-right"
									aria-labelledby="navbarDropdown">
									<c:if test="${showCreateSingleUser}">
										<a class="dropdown-item" href="<c:url value="addUserForm" />">Create
											Single User</a>
									</c:if>
									<c:if test="${showUploadStudentExcel}">
										<a class="dropdown-item"
											href="<c:url value="uploadStudentForm" />">Upload
											Students Excel</a>
									</c:if>
									<c:if test="${showUploadStudentExcel}">
										<a class="dropdown-item"
											href="<c:url value="uploadFacultyForm" />">Upload Faculty
											Excel</a>
									</c:if>
									<a class="dropdown-item"
										href="<c:url value="addFacultyDetailsForm" />">Add Faculty
										Details</a> <a class="dropdown-item"
										href="<c:url value="viewAllGrievances" />">View All
										Grievances</a>

								</div></li>
						</c:if>



						<c:if test="${showGrievance}">
							<li class="nav-item dropdown"><a
								class="nav-link dropdown-toggle" href="#" role="button"
								data-toggle="dropdown" aria-haspopup="true"
								aria-expanded="false"><i class="fas fa-newspaper"
									data-toggle="tooltip" data-placement="bottom"
									title="Grievances"></i> </a>

								<div id="navDropdownSupport"
									class="dropdown-menu dropdown-menu-right"
									aria-labelledby="navbarDropdown">
									<c:if test="${showGiveResponse}">
										<a class="dropdown-item"
											href="<c:url value="viewAllGrievances" />">Response To
											Grievances </a>
									</c:if>
								</div></li>
						</c:if>



						<c:if test="${showAnnouncement}">
							<li class="nav-item dropdown"><a
								class="nav-link dropdown-toggle" href="#" role="button"
								data-toggle="dropdown" aria-haspopup="true"
								aria-expanded="false"><i class="fas fa-bullhorn"
									data-toggle="tooltip" data-placement="bottom"
									title="Announcement"></i> </a>

								<div id="navDropdownSupport"
									class="dropdown-menu dropdown-menu-right"
									aria-labelledby="navbarDropdown">
									<c:if test="${showCreateAnnouncement}">
										<a class="dropdown-item"
											href="<c:url value="addAnnouncementForm" />">Create
											Announcement</a>
									</c:if>
									<c:if test="${showSearchAnnouncements}">
										<a class="dropdown-item"
											href="<c:url value="viewUserAnnouncementsSearchNew" />">Search
											Announcements</a>
									</c:if>
								</div></li>
						</c:if>




						<c:if test="${showAttendence}">
							<li class="nav-item dropdown"><a
								class="nav-link dropdown-toggle" href="#" role="button"
								data-toggle="dropdown" aria-haspopup="true"
								aria-expanded="false"><i class="fas fa-clipboard-user"
									data-toggle="tooltip" data-placement="bottom"
									title="Attendance"></i> </a>

								<div id="navDropdownSupport"
									class="dropdown-menu dropdown-menu-right"
									aria-labelledby="navbarDropdown">
									<c:if test="${showMarkAttendence}">
										<a class="dropdown-item"
											href="<c:url value="addAttendanceForm" />">Mark
											Attendence </a>
									</c:if>
									<c:if test="${showViewAttendence}">
										<a class="dropdown-item"
											href="<c:url value="viewDailyAttendance" />">View Daily
											Attendence </a>
									</c:if>
								</div></li>
						</c:if>



						<c:if test="${showMessage}">
							<li class="nav-item dropdown"><a
								class="nav-link dropdown-toggle" href="#" role="button"
								data-toggle="dropdown" aria-haspopup="true"
								aria-expanded="false"><i class="fas fa-envelope"
									data-toggle="tooltip" data-placement="bottom" title="Message"></i>
							</a>
								<div id="navDropdownSupport"
									class="dropdown-menu dropdown-menu-right"
									aria-labelledby="navbarDropdown">
									<c:if test="${showCreateMessage}">
										<a class="dropdown-item"
											href="<c:url value="createMessageForm" />">Create Message</a>
									</c:if>
									<c:if test="${showViewMessage}">
										<a class="dropdown-item"
											href="<c:url value="viewMyMessage" />">View Message</a>
									</c:if>
								</div></li>
						</c:if>

						<c:if test="${showStudentSupport}">
							<li class="nav-item dropdown"><a
								class="nav-link dropdown-toggle" href="#" role="button"
								data-toggle="dropdown" aria-haspopup="true"
								aria-expanded="false"><i class="fa fa-file-text"
									data-toggle="tooltip" data-placement="bottom"
									title="Grievances"></i> </a>

								<div id="navDropdownSupport"
									class="dropdown-menu dropdown-menu-right"
									aria-labelledby="navbarDropdown">

									<c:if test="${showOverview}">
										<a class="dropdown-item"
											href="<c:url value="overview?courseId=${course.id}" />">Overview
										</a>
									</c:if>
									<c:if test="${showPrieviewGrievianceResponseList}">
										<a class="dropdown-item"
											href="<c:url value="viewGrievanceResponse" />">Preview
											Grievances Response List </a>
									</c:if>
								</div></li>
						</c:if>

						<c:if test="${showContent}">
							<li class="nav-item dropdown"><a
								class="nav-link dropdown-toggle" href="#" role="button"
								data-toggle="dropdown" aria-haspopup="true"
								aria-expanded="false"><i class="fa fa fa-folder-open"
									data-toggle="tooltip" data-placement="bottom"
									title="Learning Resources"></i> </a>

								<div id="navDropdownSupport"
									class="dropdown-menu dropdown-menu-right"
									aria-labelledby="navbarDropdown">

									<c:if test="${showStudentContentList}">
										<%-- <li><a href="<c:url value="studentContentList" />">Student
									Content </a></li> --%>
									</c:if>
									<c:if test="${showGetContentList}">
										<a class="dropdown-item"
											href="<c:url value="getContentUnderAPathForFaculty" />">View
											Learning Resource </a>
									</c:if>
								</div></li>
						</c:if>

						<c:if test="${showLibrary}">
							<li class="nav-item dropdown"><a
								class="nav-link dropdown-toggle" href="#" role="button"
								data-toggle="dropdown" aria-haspopup="true"
								aria-expanded="false"><i class="fa fa-book"
									aria-hidden="true" data-toggle="tooltip"
									data-placement="bottom" title="Library"></i> </a>

								<div id="navDropdownSupport"
									class="dropdown-menu dropdown-menu-right"
									aria-labelledby="navbarDropdown">

									<c:if test="${showLibraryList}">
										<a class="dropdown-item"
											href="<c:url value="viewLibraryAnnouncements" />">View
											Library</a>
									</c:if>
									<%-- <c:if test="${addLibraryList}">
							<li><c:url value="/addLibraryItemForm" var="addFolder">
									<c:param name="folderPath" value="" />
									<c:param name="contentType" value="Folder" />
									<c:param name="parentId" value="" />
								</c:url> <a href="${addFolder}" title="Add Library">Add Non-Course
									Content</a></li>
						</c:if> --%>
								</div></li>
						</c:if>

						<c:if test="${showGrade}">
							<li class="nav-item dropdown"><a
								class="nav-link dropdown-toggle" href="#" role="button"
								data-toggle="dropdown" aria-haspopup="true"
								aria-expanded="false"><i class="fa fa-graduation-cap"
									aria-hidden="true" data-toggle="tooltip"
									data-placement="bottom" title="Grade"></i> </a>

								<div id="navDropdownSupport"
									class="dropdown-menu dropdown-menu-right"
									aria-labelledby="navbarDropdown">


									<a class="dropdown-item"
										href="<c:url value="gradeCenterForm" />">View Grade </a>
								</div></li>
						</c:if>


						<c:if test="${showQuicklinks}">
							<li class="nav-item dropdown"><a
								class="nav-link dropdown-toggle" href="#" role="button"
								data-toggle="dropdown" aria-haspopup="true"
								aria-expanded="false"><i class="fa fa-file-text"
									aria-hidden="true" data-toggle="tooltip"
									data-placement="bottom" title="Feedback"></i> </a>

								<div id="navDropdownSupport"
									class="dropdown-menu dropdown-menu-right"
									aria-labelledby="navbarDropdown">
									<c:if test="${showViewFeedbackQlinks}">
										<a class="dropdown-item"
											href="<c:url value="searchStudentFeedback" />">View
											Feedback</a>
									</c:if>
									<c:if test="${showViewTest}">
										<a class="dropdown-item" href="<c:url value="testList" />">View
											Test</a>
									</c:if>
									<c:if test="${showViewLibrary}">
										<a class="dropdown-item"
											href="<c:url value="viewLibraryAnnouncements" />">View
											Library</a>
									</c:if>
									<c:if test="${showPrieviewGrievianceResponseListQlinks}">
										<a class="dropdown-item"
											href="<c:url value="viewGrievanceResponse" />">Preview
											Grievances Response List </a>
									</c:if>
									<c:if test="${showOverviewQlinks}">
										<a class="dropdown-item" href="<c:url value="overview" />">
											Overview</a>
									</c:if>

								</div></li>
						</c:if>


						<c:if test="${showGrievances}">
							<li class="nav-item dropdown"><a
								class="nav-link dropdown-toggle" href="#" role="button"
								data-toggle="dropdown" aria-haspopup="true"
								aria-expanded="false"><i class="fa fa-file-text"
									aria-hidden="true" data-toggle="tooltip"
									data-placement="bottom" title="Grievances"></i></a>

								<div id="navDropdownSupport"
									class="dropdown-menu dropdown-menu-right"
									aria-labelledby="navbarDropdown">
									<c:if test="${showViewGrievances}">
										<a href="<c:url value="viewAllGrievances" />">View
											Grievances </a>
									</c:if>
								</div></li>
						</c:if>



						<c:if test="${showTest}">
							<li class="nav-item dropdown"><a
								class="nav-link dropdown-toggle" href="#" role="button"
								data-toggle="dropdown" aria-haspopup="true"
								aria-expanded="false"> <i class="fas fa-file-alt"
									aria-hidden="true" data-toggle="tooltip"
									data-placement="bottom" title="Test"></i>
							</a>

								<div id="navDropdownSupport"
									class="dropdown-menu dropdown-menu-right"
									aria-labelledby="navbarDropdown">

									<c:if test="${showViewTest}">
										<a class="dropdown-item" href="<c:url value="/testList" />">View
											Test</a>
									</c:if>
									<c:if test="${showUpdateTest}">
										<a class="dropdown-item"
											href="${pageContext.request.contextPath}/searchTest">Search
											Test</a>
									</c:if>
									<c:if test="${showConfigureQuestions}">
										<a class="dropdown-item"
											href="${pageContext.request.contextPath}/configureQuestions">Configure
											Questions</a>
									</c:if>

									<a class="dropdown-item"
										href="<c:url value="/viewTestPools" />">View Test Pools</a>
								</div></li>
						</c:if>


						<!-- ICA Faculty Menus -->
						  <li id="navIca" class="nav-item dropdown" data-toggle="tooltip"
							data-placement="bottom" title="ICA"><a
							class="nav-link dropdown-toggle" href="#" role="button"
							data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
								<i class="fas fa-marker"></i> <span class="mobNavbar">ICA</span>
						</a>
							<div id="navDropdownSupport"
								class="dropdown-menu dropdown-menu-right"
								aria-labelledby="navbarDropdown">



								<a class="dropdown-item" href="<c:url value="searchIcaList" />">Search
									ICA</a>

						<a class="dropdown-item" href="downloadIcaReportFacultyForm"> ICA Report</a>

							</div></li>  

						<!--  TEE Starts  -->	
						<%-- <c:if test="${fn:contains( appNameForTee, 'SBM-NM') || (appNameForTee eq 'PDSEFBM') || (appNameForTee eq 'SBM-Indore') || (appNameForTee eq 'COE')}"> --%>
						<li id="navIca" class="nav-item dropdown">
							<a class="nav-link dropdown-toggle" href="#" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
								<i class="fab fa-readme"  data-toggle="tooltip" data-placement="bottom" title="TEE"></i> 
								<span class="mobNavbar">TEE</span>
							</a>
							<div id="navDropdownSupport" class="dropdown-menu dropdown-menu-right" aria-labelledby="navbarDropdown">
								<a class="dropdown-item" href="<c:url value="searchTeeList" />">Search TEE</a>
								<a class="dropdown-item" href="<c:url value="downloadTeeReportFacultyForm" />">TEE Report</a>
							 </div>
						</li>
						<%-- </c:if> --%>
						<!--   TEE Ends  -->

						<c:if test="${showForum}">

							<li class="nav-item dropdown"><a
								class="nav-link dropdown-toggle" href="#" role="button"
								data-toggle="dropdown" aria-haspopup="true"
								aria-expanded="false"> <i class="fas fa-comments"
									aria-hidden="true" data-toggle="tooltip"
									data-placement="bottom" title="Grievances"></i>
							</a>

								<div id="navDropdownSupport"
									class="dropdown-menu dropdown-menu-right"
									aria-labelledby="navbarDropdown">


									<c:if test="${showCreateForum}">
										<a class="dropdown-item"
											href="<c:url value="createForumForm" />">Create Forum </a>
									</c:if>
									<c:if test="${showViewsForum}">
										<a class="dropdown-item" href="<c:url value="viewForum" />">View
											Forum </a>
									</c:if>

								</div></li>
						</c:if>


						<%-- <c:if test="${showCalendar}">
						<li id="calendar" class="nav-item dropdown"><a
							class="nav-link dropdown-toggle" href="#" role="button"
							data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
								<i class="far fa-calendar-alt" data-toggle="tooltip" data-placement="bottom" title="Event"></i>
						</a>


							<div id="navDropdownSupport"
								class="dropdown-menu dropdown-menu-right"
								aria-labelledby="navbarDropdown">
								<c:if test="${showViewEvents}">
									<a class="dropdown-item" href="<c:url value="viewEvents" />">View
										Event</a>
								</c:if>

								<li><a href="<c:url value="/addFacultyCourseForm" />">Course Faculty Enrollment-Not needed</a></li>
							</div></li>
					</c:if> --%>

						<li class="nav-item dropdown"><a
							class="nav-link dropdown-toggle"
							href="downloadReportMyCourseStudentForm" role="button"
							data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
								<i class="fas fa-file-download" aria-hidden="true"
								data-toggle="tooltip" data-placement="bottom" title="Report"></i>
						</a>

							<div id="navDropdownSupport"
								class="dropdown-menu dropdown-menu-right"
								aria-labelledby="navbarDropdown">
								<a class="dropdown-item"
									href="downloadReportMyCourseStudentForm">Download Reports</a>
							</div></li>






						<li id="navCalendar" class="nav-item dropdown"><a
							class="nav-link dropdown-toggle"
							href="https://dev-portal.svkm.ac.in/usermgmt"> <i
								class="fas fa-exchange-alt" data-toggle="tooltip"
								data-placement="bottom" title="Change Program"></i>
						</a></li>

						<sec:authorize access="hasAnyRole('ROLE_FACULTY')">

							<li id="search" class="nav-item dropdown"><a
								class="nav-link dropdown-toggle" href="#" role="button"
								data-toggle="dropdown" aria-haspopup="true"
								aria-expanded="false"><i class="fas fa-search"
									data-toggle="tooltip" data-placement="bottom" title="Search"></i>
							</a>

								<div id="navDropdownSupport"
									class="dropdown-menu dropdown-menu-right"
									aria-labelledby="navbarDropdown">

									<a class="dropdown-item"
										href="<c:url value="searchAssignmentTestForm" />">Search
										All Assignment/Test</a> <a class="dropdown-item"
										href="<c:url value="addSearchForm" />">Generic Search</a> <a
										class="dropdown-item"
										href="<c:url value="markAttendanceForm" />">Mark
										Attendance</a>
								</div></li>




						</sec:authorize>















						<!-- 
				<li id="navCalendar" class="nav-item"><a class="nav-link"
					href="teacher-library.html"><i class="fas fa-calendar-alt"></i>
						Calendar</a></li>
				<li id="navLibrary" class="nav-item"><a class="nav-link"
					href="teacher-library.html"><i class="fas fa-book"></i> Library</a>
				</li>
				<li id="navLibrary" class="nav-item"><a class="nav-link"
					href="teacher-library.html"><i class="fas fa-envelope"></i>
						Message</a></li>

				<li id="navSupport" class="nav-item dropdown"><a
					class="nav-link dropdown-toggle" href="#" role="button"
					data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
						<i class="fas fa-life-ring"></i> Support
				</a>
					<div id="navDropdownSupport"
						class="dropdown-menu dropdown-menu-right"
						aria-labelledby="navbarDropdown">
						<a class="dropdown-item" href="support.html">Support</a> <a
							class="dropdown-item" href="support.html">Raise a ticket</a> <a
							class="dropdown-item" href="support.html">Track your ticket
							status</a> <a class="dropdown-item" href="faqs.html">FAQ</a>
					</div></li>
				<li id="navQuicklinks" class="nav-item dropdown"><a
					class="nav-link dropdown-toggle" href="#" role="button"
					data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
						<i class="fas fa-link"></i> Quicklinks
				</a>
					<div id="navDropdownQuicklinks"
						class="dropdown-menu dropdown-menu-right"
						aria-labelledby="navbarDropdown">
						<a class="dropdown-item" href="assignment.html">Assignment</a> <a
							class="dropdown-item" href="test-quiz.html">Test/Quiz</a> <a
							class="dropdown-item" href="#">Announcements</a> <a
							class="dropdown-item" href="#">Library</a> <a
							class="dropdown-item" href="#">Student's Courses</a> <a
							class="dropdown-item" href="#">Learning Resources</a> <a
							class="dropdown-item" href="#">Groups</a> <a
							class="dropdown-item" href="#">Forum</a>
					</div></li> -->

						<li class="nav-item dropdown"><a
							class="nav-link dropdown-toggle" href="#" role="button"
							data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
								<img src="<c:url value="/resources/images/img-user.png" />"
								class="user-ico" title="Name of the user" alt="Name of the user" />
								<span>${userBean.firstname} ${userBean.lastname} </span> <!--Truncate to 12 Char -->
						</a>
							<div class="dropdown-menu dropdown-menu-right"
								aria-labelledby="navbarDropdown">
								<a class="dropdown-item" href="#"><i
									class="fas fa-id-card-alt"></i> ${userBean.username}</a> <a
									class="dropdown-item" href="#"><i class="fas fa-at"></i>
									${userBean.email}</a> <a class="dropdown-item" href="#"><i
									class="fas fa-phone"></i>${userBean.mobile}</a> <a
									class="dropdown-item"
									href="${pageContext.request.contextPath}/profileDetails"><i
									class="fas fa-user"></i> My Profile</a> <a class="dropdown-item"
									href="${pageContext.request.contextPath}/changePasswordForm"><i
									class="fas fa-key"></i> Change Password</a> <a
									class="dropdown-item" href="#"><i
									class="fas fa-chalkboard-teacher"></i> Faculty Type:
									${facultyType}</a> <a class="dropdown-item"
									href="${pageContext.request.contextPath}/loggedout"><i
									class="fas fa-sign-out-alt"></i> Logout</a>

							</div></li>


					</ul>
				</div>
			</nav>
		</header>
	</div>