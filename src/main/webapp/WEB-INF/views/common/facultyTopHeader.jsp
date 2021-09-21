<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@page import="java.util.*"%>
<%@page import="com.spts.lms.beans.dashboard.DashBoard"%>
<%@page import="com.spts.lms.beans.course.Course"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

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



<li class="top_right_menu">
	<button type="button" class="navbar-toggle collapsed"
		data-toggle="collapse" data-target="#navbar" aria-expanded="false"
		aria-controls="navbar">
		<span class="icon-bar"></span> <span class="icon-bar"></span> <span
			class="icon-bar"></span>
	</button>

	<div id="navbar" class="navbar-collapse collapse">
		<ul class="nav navbar-nav">

			<!-- <li class="active"><a href="index.html"><i
									class="fa fa-home"></i> <span class="topnav_rstext">Home</span></a>
							</li> -->

			<c:if test="${showHome}">
				<li class="active"><a
					class="page-scroll toptooltip <%=dashboard%>" href="homepage">
						<i class="fa fa-home"></i>
						<div class="toptooltiptext">Home</div> <span class="topnav_rstext">Home</span>
				</a></li>
			</c:if>






			<c:if test="${showCourse}">
				<li class="dropdown menu_scrollbar"><a href="#"
					class="dropdown-toggle toptooltip <%=courseMenuClass%>"
					data-toggle="dropdown" role="button" aria-haspopup="true"
					aria-expanded="false"><i class="fa fa-wpforms"></i>
						<div class="toptooltiptext">My courses</div> <span
						class="topnav_rstext">My Courses</span></a>
					<ul class="dropdown-menu">

						<c:forEach items="${courseDetailList}" var="courseDetail">
							<li><a href="viewCourse?id=${courseDetail.course.id}">${courseDetail.course.courseName}</a></li>
						</c:forEach>

					</ul></li>

			</c:if>





			<c:if test="${showFeedback}">

				<sec:authorize access="hasAnyRole('ROLE_ADMIN')">
					<li class="dropdown"><a href="#"
						class="dropdown-toggle toptooltip " data-toggle="dropdown"
						role="button" aria-haspopup="true" aria-expanded="false"><i
							class="fa fa-reply-all"></i>
							<div class="toptooltiptext">Feedback</div> <span
							class="topnav_rstext">Feedback</span></a>
						<ul class="dropdown-menu">


							<li><a href="<c:url value="addFeedbackForm" />">Add
									Feedback</a></li>

							<li><a href="<c:url value="searchFeedback" />">View
									Feedback</a></li>

							<li><a href="<c:url value="addStudentFeedbackForm" />">Allocate
									Feedback</a></li>

						</ul></li>







				</sec:authorize>
			</c:if>





			<c:if test="${showAssignment}">
				<li class="dropdown"><a href="#"
					class="dropdown-toggle toptooltip <%=assignmentMenuClass%>"
					data-toggle="dropdown" role="button" aria-haspopup="true"
					aria-expanded="false"><i class="fa fa-newspaper-o"></i>
						<div class="toptooltiptext">Assignment</div> <span
						class="topnav_rstext">Assignment</span></a>
					<ul class="dropdown-menu">

						<c:if test="${showViewAssignment}">
							<li><a href="<c:url value="searchFacultyAssignment" />">View
									Assignments</a></li>
						</c:if>
						<c:if test="${showEvaluateAssignmentsAdvancedSearch}">
							<li><a href="<c:url value="searchAssignmentToEvaluate" />">Evaluate
									Assignments With Advanced Search</a></li>
						</c:if>
						<c:if test="${showEvaluateAssignments}">
							<li><a href="<c:url value="evaluateByStudentForm" />">Evaluate
									Assignments for Students</a></li>
						</c:if>
						<c:if test="${showLateSubmittedAssignments}">
							<li><a href="<c:url value="lateSubmissionApprovalForm" />">
									Late Submitted Assignments</a></li>
						</c:if>
						<c:if test="${showAssigmentList}">
							<li><a href="<c:url value="nonSubmittedStudentsForm" />">
									Not Submitted Students</a></li>
						</c:if>

					</ul></li>
			</c:if>



			<c:if test="${showCourseMenu}">
				<li class="dropdown"><a href="#"
					class="dropdown-toggle toptooltip <%=courseMenuClass%>"
					data-toggle="dropdown" role="button" aria-haspopup="true"
					aria-expanded="false"><i class="fa fa-newspaper-o"></i>
						<div class="toptooltiptext">course</div> <span
						class="topnav_rstext">Course</span></a>

					<ul class="dropdown-menu">


						<c:if test="${showAddSingleCourse}">
							<li><a href="<c:url value="addCourseForm" />">Add Single
									Course</a></li>
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

					</ul></li>
			</c:if>


			<c:if test="${showManageUser}">
				<li class="dropdown"><a href="#"
					class="dropdown-toggle toptooltip " data-toggle="dropdown"
					role="button" aria-haspopup="true" aria-expanded="false"><i
						class="fa fa-newspaper-o"></i>
						<div class="toptooltiptext">Manage User</div> <span
						class="topnav_rstext">Manage Users</span></a>

					<ul class="dropdown-menu">
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
						<li><a href="<c:url value="addFacultyDetailsForm" />">Add
								Faculty Details</a></li>
						<li><a href="<c:url value="viewAllGrievances" />">View
								All Grievances</a></li>

					</ul></li>
			</c:if>



			<c:if test="${showGrievance}">
				<li class="dropdown"><a href="#"
					class="dropdown-toggle toptooltip " data-toggle="dropdown"
					role="button" aria-haspopup="true" aria-expanded="false"><i
						class="fa fa-newspaper-o"></i>
						<div class="toptooltiptext">Grievance</div> <span
						class="topnav_rstext">View Grievance</span></a>

					<ul class="dropdown-menu">
						<c:if test="${showGiveResponse}">
							<li><a href="<c:url value="viewAllGrievances" />">Response
									To Grievances </a></li>
						</c:if>

					</ul></li>
			</c:if>



			<c:if test="${showAnnouncement}">
				<li class="dropdown"><a href="#"
					class="dropdown-toggle toptooltip <%=announcementMenuClass%>"
					data-toggle="dropdown" role="button" aria-haspopup="true"
					aria-expanded="false"><i class="fa fa-bullhorn"></i>
						<div class="toptooltiptext">Announcements</div> <span
						class="topnav_rstext">Announcements</span></a>

					<ul class="dropdown-menu">
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




			<c:if test="${showAttendence}">
				<li class="dropdown"><a href="#"
					class="dropdown-toggle toptooltip " data-toggle="dropdown"
					role="button" aria-haspopup="true" aria-expanded="false"><i
						class="fa fa-bullhorn"></i>
						<div class="toptooltiptext">Attendance</div> <span
						class="topnav_rstext">Attendence</span></a>

					<ul class="dropdown-menu">
						<c:if test="${showMarkAttendence}">
							<li><a href="<c:url value="addAttendanceForm" />">Mark
									Attendence </a></li>
						</c:if>
						<c:if test="${showViewAttendence}">
							<li><a href="<c:url value="viewDailyAttendance" />">View
									Daily Attendence </a></li>
						</c:if>

					</ul></li>
			</c:if>



			<c:if test="${showMessage}">
				<li class="dropdown"><a href="#"
					class="dropdown-toggle toptooltip <%=messageMenuClass%>"
					data-toggle="dropdown" role="button" aria-haspopup="true"
					aria-expanded="false"><i class="fa fa-envelope-o"></i>
						<div class="toptooltiptext">Message</div> <span
						class="topnav_rstext">Message</span></a>
					<ul class="dropdown-menu">
						<c:if test="${showCreateMessage}">
							<li><a href="<c:url value="createMessageForm" />">Create
									Message</a></li>
						</c:if>
						<c:if test="${showViewMessage}">
							<li><a href="<c:url value="viewMyMessage" />">View
									Message</a></li>
						</c:if>
					</ul></li>
			</c:if>






			<%-- <li class="dropdown"><a href="#" class="dropdown-toggle"
				data-toggle="dropdown" role="button" aria-haspopup="true"
				aria-expanded="false"><i class="fa fa-users"></i> <span
					class="topnav_rstext">Group</span></a>
				<ul class="dropdown-menu">
					<li><a
						href="<c:url value="/createGroupForm?courseId=${course.id}" />">Create
							Group</a></li>
					<li><a href="<c:url value="/searchFacultyGroups" />">View
							Group </a></li>
				</ul></li> --%>



			<%-- <c:if test="${showGroup}">
				<li class="dropdown"><a href="#"
					class="dropdown-toggle toptooltip" data-toggle="dropdown"
					role="button" aria-haspopup="true" aria-expanded="false"><i
						class="fa fa-users"></i>
						<div class="toptooltiptext">Group</div>
						<span class="topnav_rstext">Group</span></a>
					<ul class="dropdown-menu">
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
			</c:if> --%>




			<c:if test="${showStudentSupport}">
				<li class="dropdown"><a href="#"
					class="dropdown-toggle toptooltip " data-toggle="dropdown"
					role="button" aria-haspopup="true" aria-expanded="false"><i
						class="fa fa-file-text"></i>
						<div class="toptooltiptext">Overview</div> <span
						class="topnav_rstext">Student Support</span></a>

					<ul class="dropdown-menu">

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

			<c:if test="${showContent}">
				<li class="dropdown"><a href="#"
					class="dropdown-toggle toptooltip " data-toggle="dropdown"
					role="button" aria-haspopup="true" aria-expanded="false"><i
						class="fa fa fa-folder-open"></i>
						<div class="toptooltiptext">Learning Resourses</div> <span
						class="topnav_rstext">Learning Resource</span></a>

					<ul class="dropdown-menu">

						<c:if test="${showStudentContentList}">
							<%-- <li><a href="<c:url value="studentContentList" />">Student
									Content </a></li> --%>
						</c:if>
						<c:if test="${showGetContentList}">
							<li><a
								href="<c:url value="getContentUnderAPathForFaculty" />">View
									Learning Resource </a></li>
						</c:if>
					</ul></li>
			</c:if>

			<c:if test="${showLibrary}">
				<li class="dropdown"><a href="#"
					class="dropdown-toggle toptooltip <%=libraryMenuClass%>"
					data-toggle="dropdown" role="button" aria-haspopup="true"
					aria-expanded="false"><i class="fa fa-book" aria-hidden="true"></i>
						<div class="toptooltiptext">Library</div> <span
						class="topnav_rstext">Library</span></a>

					<ul class="dropdown-menu">

						<c:if test="${showLibraryList}">
							<li><a href="<c:url value="viewLibraryAnnouncements" />">View
									Library</a></li>
						</c:if>
						<%-- <c:if test="${addLibraryList}">
							<li><c:url value="/addLibraryItemForm" var="addFolder">
									<c:param name="folderPath" value="" />
									<c:param name="contentType" value="Folder" />
									<c:param name="parentId" value="" />
								</c:url> <a href="${addFolder}" title="Add Library">Add Non-Course
									Content</a></li>
						</c:if> --%>
					</ul></li>
			</c:if>

			<c:if test="${showGrade}">
				<li class="dropdown"><a href="#"
					class="dropdown-toggle toptooltip <%=gradeMenuClass%>"
					data-toggle="dropdown" role="button" aria-haspopup="true"
					aria-expanded="false"><i class="fa fa-graduation-cap"
						aria-hidden="true"></i>
						<div class="toptooltiptext">Grade Center</div> <span
						class="topnav_rstext">Grade Center</span></a>

					<ul class="dropdown-menu">


						<li><a href="<c:url value="gradeCenterForm" />">View
								Grade </a></li>


					</ul></li>
			</c:if>


			<c:if test="${showQuicklinks}">
				<li class="dropdown"><a href="#"
					class="dropdown-toggle toptooltip " data-toggle="dropdown"
					role="button" aria-haspopup="true" aria-expanded="false"><i
						class="fa fa-file-text"></i>
						<div class="toptooltiptext">Quicklinks</div> <span
						class="topnav_rstext">Quicklinks</span></a>

					<ul class="dropdown-menu">

						<c:if test="${showViewFeedbackQlinks}">
							<li><a href="<c:url value="searchStudentFeedback" />">View
									Feedback</a></li>
						</c:if>
						<c:if test="${showViewTest}">
							<li><a href="<c:url value="testList" />">View Test</a></li>
						</c:if>
						<c:if test="${showViewLibrary}">
							<li><a href="<c:url value="viewLibraryAnnouncements" />">View
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
				<li class="dropdown"><a href="#"
					class="dropdown-toggle toptooltip " data-toggle="dropdown"
					role="button" aria-haspopup="true" aria-expanded="false"><i
						class="fa fa-file-text"></i>
						<div class="toptooltiptext">Grieviences</div> <span
						class="topnav_rstext">Grievances</span></a> <c:if
						test="${showViewGrievances}">
						<li><a href="<c:url value="viewAllGrievances" />">View
								Grievances </a></li>
					</c:if>
		</ul>
</li>
</c:if>



<c:if test="${showTest}">
	<li class="dropdown"><a href="#"
		class="dropdown-toggle toptooltip <%=testMenuClass%>"
		data-toggle="dropdown" role="button" aria-haspopup="true"
		aria-expanded="false"><i class="fa fa-file-text"></i>
			<div class="toptooltiptext">Test</div> <span class="topnav_rstext">Test</span></a>

		<ul class="dropdown-menu">

			<c:if test="${showViewTest}">
				<li><a href="<c:url value="/testList" />">View Test</a></li>
			</c:if>
			<c:if test="${showUpdateTest}">
				<li><a href="${pageContext.request.contextPath}/searchTest">Search
						Test</a></li>
			</c:if>
			<c:if test="${showConfigureQuestions}">
				<li><a
					href="${pageContext.request.contextPath}/configureQuestions">Configure
						Questions</a></li>
			</c:if>
			
			<li><a href="<c:url value="/viewTestPools" />">View Test Pools</a></li>
		</ul></li>
</c:if>



<c:if test="${showForum}">

	<li class="dropdown"><a href="#"
		class="dropdown-toggle toptooltip <%=forumMenuClass%>"
		data-toggle="dropdown" role="button" aria-haspopup="true"
		aria-expanded="false"><i class="fa fa-commenting"></i>
			<div class="toptooltiptext">Forum</div> <span class="topnav_rstext">Discussion
				Forum</span></a>
		<ul class="dropdown-menu">
			<c:if test="${showCreateForum}">
				<li><a href="<c:url value="createForumForm" />">Create
						Forum </a></li>
			</c:if>
			<c:if test="${showViewsForum}">
				<li><a href="<c:url value="viewForum" />">View Forum </a></li>
			</c:if>

		</ul></li>
</c:if>


<c:if test="${showCalendar}">
	<li class="dropdown"><a href="#"
		class="dropdown-toggle toptooltip <%=calendarMenuClass%>"
		" data-toggle="dropdown" role="button" aria-haspopup="true"
		aria-expanded="false"><i class="fa fa-calendar"></i>
			<div class="toptooltiptext">Calendar</div> <span
			class="topnav_rstext">Calendar</span></a>
		<ul class="dropdown-menu">


			<c:if test="${showViewEvents}">
				<li><a href="<c:url value="viewEvents" />">View Event</a></li>
			</c:if>

			<%-- <li><a href="<c:url value="/addFacultyCourseForm" />">Course Faculty Enrollment-Not needed</a></li> --%>
		</ul></li>
</c:if>
<li class="dropdown"><a class="page-scroll toptooltip "
	href="downloadReportMyCourseStudentForm"> <i
		class="fa fa-file-excel-o" aria-hidden="true"></i>
		<div class="toptooltiptext">Reports</div> <span class="topnav_rstext">Reports</span>
</a></li>
<%-- <c:if test="${showCalendar}">
	<li><a href="#"><i class="fa fa-calendar"></i> <span
			class="topnav_rstext">Calendar</span></a>
		<ul class="dropdown-menu">
			
			<c:if test="${showViewEvents}">
				<li><a href="<c:url value="viewEvents" />">View Event</a></li>
			</c:if>

			<li><a href="<c:url value="/addFacultyCourseForm" />">Course Faculty Enrollment-Not needed</a></li>
		</ul></li>
</c:if> --%>

<li class="dropdown"><a class="page-scroll toptooltip "
	href="https://portal.svkm.ac.in/usermgmt"> <i
		class="fa fa-exchange" aria-hidden="true"></i>
		<div class="toptooltiptext">Change Program</div> <span
		class="topnav_rstext">Change Program</span>
</a></li>

<c:if test="${showSetup}">
	<li><a href="#"><i class="fa fa-calendar toptooltip"></i>
			<div class="toptooltiptext">Setup</div> <span class="topnav_rstext">SetUp</span></a>
		<ul class="dropdown-menu">

			<c:if test="${showMakeAcademicCycleLive}">
				<li><a href="<c:url value="addInstituteCycleForm" />">Make
						Academic Cycle LIVE!</a></li>
			</c:if>
		</ul></li>
</c:if>
<sec:authorize access="hasAnyRole('ROLE_FACULTY')">
	<li class="dropdown"><a href="#"
		class="dropdown-toggle toptooltip" data-toggle="dropdown"
		role="button" aria-haspopup="true" aria-expanded="false"><i
			class="fa fa-reply-all"></i>
			<div class="toptooltiptext">Search</div> <span class="topnav_rstext">Search</span></a>
		<ul class="dropdown-menu">


			<li><a href="<c:url value="searchAssignmentTestForm" />">Search
					All Assignment/Test</a></li>

			<li><a href="<c:url value="addSearchForm" />">Generic Search</a></li>
			<%-- <li><a href="<c:url value="markAttendanceForm" />">Mark
					Attendance</a></li> --%>
			<!-- <li><a href="viewExtLibraries">Librarian Link</a></li> -->



		</ul></li>
</sec:authorize>













</ul>
</div>
</li>
