<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@page import="java.util.*"%>
<%@page import="com.spts.lms.beans.dashboard.DashBoard"%>
<%@page import="com.spts.lms.beans.course.Course"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

<%
	Map<String, Set<String>> menuRights = (Map<String, Set<String>>) session
			.getAttribute("menuRights");
	System.out.print("menuRights" + menuRights);
	if (menuRights != null) {
		boolean showHome = (menuRights != null) ? menuRights
				.containsKey("Home") : false;
		boolean showMyProgram = (menuRights != null) ? menuRights
				.containsKey("MyProgram") : false;
		boolean showFeedback = (menuRights.containsKey("Feedback"));
		boolean showMakeInactive = (menuRights
				.containsKey("MakeInactive"));
		boolean showSearch = (menuRights.containsKey("Search"));
		boolean showAssignWieghtage = (menuRights
				.containsKey("Wiegtage"));
		boolean showAssignWieghtageSub = showAssignWieghtage ? menuRights
				.get("Wiegtage").contains("WiegtageSub") : false;
		boolean showForum = (menuRights.containsKey("Forum"));
		boolean showCalendar = (menuRights.containsKey("Calendar"));
		boolean showCourseMenu = (menuRights.containsKey("Course"));
		boolean showProfile = (menuRights.containsKey("Profile"));
		boolean showAnnouncement = (menuRights
				.containsKey("Announcement"));
		boolean showManageUser = (menuRights.containsKey("ManageUser"));
		boolean showGrievances = (menuRights.containsKey("Grievances"));
		boolean showAttendence = (menuRights.containsKey("Attendence"));
		boolean showFacultyAllocation = (menuRights
				.containsKey("facultyAllocation"));
		boolean showMarkAttendence = showAttendence ? menuRights.get(
				"Attendence").contains("MarkAttendence") : false;
		boolean showAssTestSearch = showSearch ? menuRights.get(
				"Search").contains("AssignmentTestSearch") : false;
		boolean showGenericSearch = showSearch ? menuRights.get(
				"Search").contains("GenericSearch") : false;
		boolean showAddQBank = showSearch ? menuRights.get("Search")
				.contains("AddQBankPapers") : false;
		boolean showAddFacultyDetails = showSearch ? menuRights.get(
				"Search").contains("AddFacultyDetails") : false;
		boolean showAddFeedback = showFeedback ? menuRights.get(
				"Feedback").contains("AddFeedback") : false;
		boolean showAllocateFeedback = showFeedback ? menuRights.get(
				"Feedback").contains("AllocateFeedback") : false;
		boolean showSearchFeedback = showFeedback ? menuRights.get(
				"Feedback").contains("SearchFeedback") : false;
		boolean showViewFeedback = showFeedback ? menuRights.get(
				"Feedback").contains("ViewFeedback") : false;
		boolean showFeedbackReport = showFeedback ? menuRights.get(
				"Feedback").contains("ViewFeedbackReport") : false;
		//boolean showFeedbackList = showFeedback ? menuRights.get("Feedback").contains("FeedbackList") : false;
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
		boolean showMultiCourseUserEnrollment = showCourseMenu ? menuRights
				.get("Course").contains("MultiCourseUserEnrollment")
				: false;

		boolean showViewGrievances = showGrievances ? menuRights.get(
				"Grievances").contains("ViewGrievances") : false;
		boolean showUploadGrievances = showGrievances ? menuRights.get(
				"Grievances").contains("UploadGrievances") : false;

		boolean showMakeInactiveUser = showMakeInactive ? menuRights
				.get("MakeInactive").contains("MakeInactiveUser")
				: false;
		boolean showFacultyAllocationSub = showFacultyAllocation ? menuRights
				.get("FacultyAllocation").contains(
						"FacultyAllocationSub") : false;
		boolean showCreateForum = showForum ? menuRights.get("Forum")
				.contains("CreateForum") : false;
		boolean showViewsForum = showForum ? menuRights.get("Forum")
				.contains("ViewForum") : false;
		boolean showAddEvents = showCalendar ? menuRights.get(
				"Calendar").contains("AddEvents") : false;
		boolean showViewEvents = showCalendar ? menuRights.get(
				"Calendar").contains("ViewEvents") : false;
		boolean showCreateAnnouncement = showAnnouncement ? menuRights
				.get("Announcement").contains("CreateAnnouncements")
				: false;
		boolean showSearchAnnouncements = showAnnouncement ? menuRights
				.get("Announcement").contains("SearchAnnouncements")
				: false;
		boolean showCreateAnnouncementsForInstitute = showAnnouncement ? menuRights
				.get("Announcement").contains(
						"CreateAnnouncementsForInstitute") : false;
		boolean showViewAnnouncements = showAnnouncement ? menuRights
				.get("Announcement").contains("ViewAnnouncements")
				: false;
		boolean showCreateAnnouncementForProgram = showAnnouncement ? menuRights
				.get("Announcement").contains(
						"addAnnouncementFormProgram") : false;
		boolean showCreateSingleUser = showManageUser ? menuRights.get(
				"ManageUser").contains("CreateSingleUser") : false;
		boolean showUploadStudentExcel = showManageUser ? menuRights
				.get("ManageUser").contains("UploadStudentExcel")
				: false;
		boolean showUploadFacultyExcel = showManageUser ? menuRights
				.get("ManageUser").contains("UploadFacultyExcel")
				: false;
		boolean showSearchUser = showManageUser ? menuRights.get(
				"ManageUser").contains("SearchUser") : false;

		boolean showMakeInactiveSub = showMakeInactive ? menuRights
				.get("MakeInactive").contains("MakeInactiveSub")
				: false;

		boolean showMyProgramStudents = showMyProgram ? menuRights.get(
				"MyProgram").contains("ShowMyProgramStudentsForAdmin")
				: false;

		pageContext.setAttribute("showAssignWieghtage",
				showAssignWieghtage);
		pageContext.setAttribute("showAssignWieghtageSub",
				showAssignWieghtageSub);

		pageContext.setAttribute("showHome", showHome);
		pageContext.setAttribute("showSearch", showSearch);
		pageContext
				.setAttribute("showAssTestSearch", showAssTestSearch);
		pageContext
				.setAttribute("showGenericSearch", showGenericSearch);
		pageContext.setAttribute("showAddQBank", showAddQBank);
		pageContext.setAttribute("showAddFacultyDetails",
				showAddFacultyDetails);
		pageContext.setAttribute("showProfile", showProfile);
		pageContext.setAttribute("showFeedback", showFeedback);
		pageContext.setAttribute("showForum", showForum);
		pageContext.setAttribute("showCalendar", showCalendar);
		pageContext.setAttribute("showAnnouncement", showAnnouncement);
		pageContext.setAttribute("showCourseMenu", showCourseMenu);
		pageContext.setAttribute("showManageUser", showManageUser);
		pageContext.setAttribute("showGrievances", showGrievances);
		pageContext.setAttribute("showAttendence", showAttendence);
		pageContext.setAttribute("showAddFeedback", showAddFeedback);
		pageContext.setAttribute("showAllocateFeedback",
				showAllocateFeedback);
		pageContext.setAttribute("showViewFeedback", showViewFeedback);
		pageContext.setAttribute("showFeedbackReport",
				showFeedbackReport);
		pageContext.setAttribute("showMakeInactiveSub",
				showMakeInactiveSub);
		pageContext.setAttribute("showMakeInactive", showMakeInactive);

		pageContext.setAttribute("showCreateForum", showCreateForum);
		pageContext.setAttribute("showViewsForum", showViewsForum);
		pageContext.setAttribute("showAddEvents", showAddEvents);
		pageContext.setAttribute("showViewEvents", showViewEvents);
		pageContext.setAttribute("showCreateAnnouncement",
				showCreateAnnouncement);
		pageContext.setAttribute("showSearchAnnouncements",
				showSearchAnnouncements);
		pageContext.setAttribute("showCreateAnnouncementsForInstitute",
				showCreateAnnouncementsForInstitute);
		pageContext.setAttribute("showViewAnnouncements",
				showViewAnnouncements);
		pageContext.setAttribute("showCreateAnnouncementForProgram",
				showCreateAnnouncementForProgram);

		pageContext.setAttribute("showCreateSingleUser",
				showCreateSingleUser);
		pageContext.setAttribute("showUploadStudentExcel",
				showUploadStudentExcel);
		pageContext.setAttribute("showUploadFacultyExcel",
				showUploadFacultyExcel);
		pageContext.setAttribute("showSearchUser", showSearchUser);
		pageContext.setAttribute("showViewGrievances",
				showViewGrievances);
		pageContext.setAttribute("showUploadGrievances",
				showUploadGrievances);

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
		pageContext.setAttribute("showMyProgram", showMyProgram);
		pageContext.setAttribute("showMyProgramStudents",
				showMyProgramStudents);
		pageContext.setAttribute("showMultiCourseUserEnrollment",
				showMultiCourseUserEnrollment);

	} else {
		pageContext.setAttribute("showMakeInactive", false);
		pageContext.setAttribute("showMarkAttendence", false);
		pageContext.setAttribute("showSearch", false);
		pageContext.setAttribute("showAssTestSearch", false);
		pageContext.setAttribute("showGenericSearch", false);
		pageContext.setAttribute("showAddQBank", false);
		pageContext.setAttribute("showAddFacultyDetails", false);
		pageContext.setAttribute("showHome", false);
		pageContext.setAttribute("showCourse", false);
		pageContext.setAttribute("showProfile", false);
		pageContext.setAttribute("showFeedback", false);
		pageContext.setAttribute("showMessage", false);
		pageContext.setAttribute("showMarkInactive", false);
		pageContext.setAttribute("showStudentSupport", false);
		pageContext.setAttribute("showQuicklinks", false);
		pageContext.setAttribute("showForum", false);
		pageContext.setAttribute("showCalendar", false);
		pageContext.setAttribute("showAssignment", false);
		pageContext.setAttribute("showAnnouncement", false);
		pageContext.setAttribute("showGroup", false);
		pageContext.setAttribute("showAssignWieghtage", false);
		pageContext.setAttribute("showAssignWieghtageSub", false);

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
		pageContext.setAttribute("showFeedbackReport", false);
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
		pageContext.setAttribute("showCreateAnnouncementsForInstitute",
				false);
		pageContext.setAttribute("showViewAnnouncements", false);
		pageContext.setAttribute("showCreateAnnouncementForProgram",
				false);

		pageContext.setAttribute("showCreateAssignment", false);
		pageContext.setAttribute("showCreateAssignmentsForGroups",
				false);
		pageContext.setAttribute("showViewAssignment", false);
		pageContext.setAttribute(
				"showEvaluateAssignmentsAdvancedSearch", false);
		pageContext.setAttribute("showEvaluateAssignments", false);
		pageContext.setAttribute("showLateSubmittedAssignments", false);
		pageContext.setAttribute("showAssigmentList", false);
		pageContext.setAttribute("showMakeInactive", false);
		pageContext.setAttribute("showFacultyAllocation", false);
		pageContext.setAttribute("showFacultyAllocationSub", false);
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
		pageContext.setAttribute("showSearchUser", false);

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

		pageContext.setAttribute("showViewGrievances", false);
		pageContext.setAttribute("showUploadGrievances", false);
		pageContext.setAttribute("showMyProgram", false);
		pageContext.setAttribute("showMyProgramStudents", false);
		pageContext
				.setAttribute("showMultiCourseUserEnrollment", false);
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
				<li class="active"><a class="page-scroll toptooltip"
					href="homepage"> <i class="fa fa-home"></i>
						<div class="toptooltiptext">Home</div> <span class="topnav_rstext">Home</span>
				</a></li>
			</c:if>


			<c:if test="${showCourseMenu}">
				<li class="dropdown"><a href="#"
					class="dropdown-toggle toptooltip" data-toggle="dropdown"
					role="button" aria-haspopup="true" aria-expanded="false"><i
						class="fa fa-newspaper-o"></i>
						<div class="toptooltiptext">Course</div> <span
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
							<li><a href="<c:url value="uploadUserCourseEnrolForm" />">Course
									User Enrollment</a></li>
						</c:if>
						<c:if test="${showSearchCourseUserEnrollment}">
							<li><a href="<c:url value="searchUserCourse" />">Search
									Course User Enrollment</a></li>
						</c:if>
						<c:if test="${showMultiCourseUserEnrollment}">
							<li><a href="<c:url value="uploadUserCourseEnrolForm" />">Multiple
									Courses User Enrollment</a></li>
						</c:if>


					</ul></li>
			</c:if>


			<c:if test="${showAssignWieghtage}">
				<li class="dropdown"><a href="#"
					class="dropdown-toggle toptooltip" data-toggle="dropdown"
					role="button" aria-haspopup="true" aria-expanded="false"><i
						class="fa fa-pie-chart"></i>
						<div class="toptooltiptext">Weight</div> <span
						class="topnav_rstext">Weight</span></a>
					<ul class="dropdown-menu">
						<c:if test="${showAssignWieghtageSub}">
							<li><a href="<c:url value="addWeight" />"> Assign Weight</a></li>
							<li><a href="<c:url value="viewWieghtDetails" />"> View
									Assigned Weight</a></li>
						</c:if>

					</ul></li>
			</c:if>


			<c:if test="${showManageUser}">
				<li class="dropdown"><a href="#"
					class="dropdown-toggle toptooltip" data-toggle="dropdown"
					role="button" aria-haspopup="true" aria-expanded="false"><i
						class="fa fa fa-user-plus"></i>
						<div class="toptooltiptext">Manage Users</div> <span
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
						<c:if test="${showSearchUser}">
							<li><a href="<c:url value="searchUser" />"> Search Users
							</a></li>
						</c:if>
					</ul></li>
			</c:if>


			<c:if test="${showAttendence}">
				<li class="dropdown"><a href="#"
					class="dropdown-toggle toptooltip" data-toggle="dropdown"
					role="button" aria-haspopup="true" aria-expanded="false"><i
						class="fa fa-address-book"></i>
						<div class="toptooltiptext">Attendence</div> <span
						class="topnav_rstext">Attendence</span></a>

					<ul class="dropdown-menu">
						<c:if test="${showMarkAttendence}">
							<li><a href="<c:url value="addAttendanceForm" />">Mark
									Attendence </a></li>
						</c:if>


					</ul></li>
			</c:if>

			<c:if test="${showCourseOverview}">
				<li><a href="#" class="toptooltip"><i class="fa fa-wpforms"></i>
						<div class="toptooltiptext">Course Overview</div> <span
						class="topnav_rstext">Course Overview</span></a>
					<ul class="dropdown-menu">
						<c:if test="${showCourseOverview}">
							<li><a href="<c:url value="addCourseOverviewForm" />">Add
									Course Overview</a></li>
						</c:if>

					</ul></li>
			</c:if>

			<%-- 
			<c:if test="${showMyProgram}">
				<li class="dropdown"><a href="#" class="dropdown-toggle toptooltip"><i class="fa fa-wpforms"></i>
						<div class="toptooltiptext">My Program</div> <span
						class="topnav_rstext">My Program</span></a>
					<ul class="dropdown-menu">
						<c:if test="${showMyProgramStudents}">
							<li><a
								href="<c:url value="showMyProgramStudentsForAdmin" />"> My
									Program Students</a></li>
						</c:if>

					</ul></li>
			</c:if> --%>

			<%-- 			<c:if test="${showMyProgram}"> --%>

			<!-- 				<li class="dropdown"><a href="#" -->
			<!-- 					class="dropdown-toggle toptooltip" data-toggle="dropdown" -->
			<!-- 					role="button" aria-haspopup="true" aria-expanded="false"> -->
			<!-- 					<i class="fa fa-address-book"></i> -->
			<!-- 						<div class="toptooltiptext">My Program</div>  -->
			<!-- 						<span -->
			<!-- 						class="topnav_rstext">My Program</span></a> -->

			<!-- 					<ul class="dropdown-menu"> -->
			<%-- 						<c:if test="${showMyProgramStudents}"> --%>
			<%-- 							<li><a href="<c:url value="showMyProgramStudentsForAdmin" />">My --%>
			<!-- 									Program Students </a></li> -->
			<%-- 						</c:if> --%>


			<!-- 					</ul></li> -->

			<%-- 			</c:if> --%>

			<%-- <c:if test="${showMyProgram}"> --%>
			<li><a class="page-scroll toptooltip"
				href="<c:url value="showMyCourseStudents" />"> <i
					class="fa fa-wpforms"></i>
					<div class="toptooltiptext">My Program</div> <span
					class="topnav_rstext">My Program</span>
			</a></li>




			<li><a class="page-scroll toptooltip"
				href="<c:url value="viewLibraryAnnouncements" />"> <i
					class="fa fa-wpforms"></i>
					<div class="toptooltiptext">Libraries</div> <span
					class="topnav_rstext">Libraries</span>
			</a></li>
			<%-- </c:if> --%>



			<c:if test="${showSearch}">

				<sec:authorize access="hasAnyRole('ROLE_ADMIN','ROLE_CORD')">
					<li class="dropdown"><a href="#"
						class="dropdown-toggle toptooltip" data-toggle="dropdown"
						role="button" aria-haspopup="true" aria-expanded="false"><i
							class="fa fa-search"></i>
							<div class="toptooltiptext">Search</div> <span
							class="topnav_rstext">Search</span></a>
						<ul class="dropdown-menu">
							<c:if test="${showAssTestSearch}">

								<li><a href="<c:url value="searchAssignmentTestForm" />">Search
										All Assignment/Test</a></li>
							</c:if>
							<c:if test="${showGenericSearch}">

								<li><a href="<c:url value="addSearchForm" />">Generic
										Search</a></li>
							</c:if>
							<c:if test="${showAddQBank}">

								<li><a href="<c:url value="addQBank" />">Add Question
										Papers</a></li>
							</c:if>
							<c:if test="${showAddFacultyDetails}">

								<li><a href="<c:url value="addFacultyDetailsForm" />">Add
										Faculty Details</a></li>
							</c:if>
							<li><a href="<c:url value="viewQuery" />">Preview
									Queries List </a></li>
							<li><a href="<c:url value="addFAQForm" />">Add FAQs</a></li>
							
							<li><a href="<c:url value="searchUser" />"> Search Users
								</a></li>

						</ul></li>
				</sec:authorize>
			</c:if>


			<c:if test="${showAnnouncement}">
				<li class="dropdown"><a href="#"
					class="dropdown-toggle toptooltip" data-toggle="dropdown"
					role="button" aria-haspopup="true" aria-expanded="false"><i
						class="fa fa-bullhorn"></i>
						<div class="toptooltiptext">Announcements</div> <span
						class="topnav_rstext">Announcements</span></a>

					<ul class="dropdown-menu">
						<c:if test="${showCreateAnnouncement}">
							<li><a href="<c:url value="addAnnouncementForm" />">Create
									Announcement for Institute</a></li>
						</c:if>
						<c:if test="${showSearchAnnouncements}">
							<li><a href="<c:url value="searchAnnouncement" />">Search
									Announcements</a></li>
						</c:if>
						<c:if test="${showCreateAnnouncementForProgram}">
							<li><a href="<c:url value="addAnnouncementFormMultiProgram" />">Create
									Announcement For Program</a></li>
						</c:if>
					</ul></li>
			</c:if>





			<c:if test="${showCalendar}">
				<li class="dropdown"><a href="#"
					class="dropdown-toggle toptooltip" data-toggle="dropdown"
					role="button" aria-haspopup="true" aria-expanded="false"><i
						class="fa fa-calendar"></i>
						<div class="toptooltiptext">Calendar</div> <span
						class="topnav_rstext">Calendar</span></a>

					<ul class="dropdown-menu">
						<c:if test="${showAddEvents}">
							<li><a href="<c:url value="addCalenderEventForm" />">Add
									Event </a></li>
						</c:if>
						<c:if test="${showViewEvents}">
							<li><a href="<c:url value="viewEvents" />">View Event </a></li>
						</c:if>

					</ul></li>
			</c:if>




			<c:if test="${showForum}">

				<li class="dropdown"><a href="#"
					class="dropdown-toggle toptooltip" data-toggle="dropdown"
					role="button" aria-haspopup="true" aria-expanded="false"><i
						class="fa fa-commenting"></i>
						<div class="toptooltiptext">Discussion Forum</div> <span
						class="topnav_rstext">Discussion Forum</span></a>
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



			<c:if test="${showGrievances}">
				<li class="dropdown"><a href="#"
					class="dropdown-toggle toptooltip" data-toggle="dropdown"
					role="button" aria-haspopup="true" aria-expanded="false"><i
						class="fa fa-file-text"></i>
						<div class="toptooltiptext">Grievances</div> <span
						class="topnav_rstext">Grievances</span></a>
					<ul class="dropdown-menu">
						<c:if test="${showViewGrievances}">
							<li><a href="<c:url value="viewAllGrievances" />">View
									Grievances </a></li>
						</c:if>
						<c:if test="${showUploadGrievances}">
							<li><a
								href="<c:url value="uploadGrievanceConfigItemsForm" />">Upload
									Grievances Items</a></li>
						</c:if>
					</ul></li>
			</c:if>


			<li class="dropdown"><a class="page-scroll toptooltip "
				href="https://portal.svkm.ac.in/usermgmt"> <i
					class="fa fa-exchange" aria-hidden="true"></i>
					<div class="toptooltiptext">Change Program</div> <span
					class="topnav_rstext">Change Program</span>
			</a></li>


			<c:if test="${showFeedback}">
				<li class="dropdown"><a href="#"
					class="dropdown-toggle toptooltip" data-toggle="dropdown"
					role="button" aria-haspopup="true" aria-expanded="false"><i
						class="fa fa-reply-all"></i>
						<div class="toptooltiptext">Feedback</div> <span
						class="topnav_rstext">Feedback</span></a>
					<ul class="dropdown-menu">

						<c:if test="${showAddFeedback}">
							<li><a href="<c:url value="addFeedbackForm" />">Add
									Feedback</a></li>
						</c:if>
						<c:if test="${showViewFeedback}">
							<li><a href="<c:url value="searchFeedback" />">View
									Feedback</a></li>
						</c:if>
						<c:if test="${showAllocateFeedback}">
							<li><a href="<c:url value="addStudentFeedbackForm" />">Allocate
									Feedback</a></li>
						</c:if>
						<li><a
							href="<c:url value="addStudentFeedbackFormForCourses" />">Allocate
								Feedback CourseWise</a></li>
						<%-- <li><a
							href="<c:url value="searchStudentFeedbackResponseForm" />">Show
								Feedback Report Download</a></li> --%>
					</ul></li>
			</c:if>
			<li class="dropdown"><a class="page-scroll toptooltip "
				href="downloadReportMyCourseStudentForm"> <i
					class="fa fa-file-excel-o" aria-hidden="true"></i>
					<div class="toptooltiptext">Reports</div> <span
					class="topnav_rstext">Reports</span>
			</a></li>

			<c:if test="${showMakeInactive}">
				<li class="dropdown"><a href="#"
					class="dropdown-toggle toptooltip" data-toggle="dropdown"
					role="button" aria-haspopup="true" aria-expanded="false"><i
						class="fa fa-envelope-o"></i>
						<div class="toptooltiptext">Inactive</div> <span
						class="topnav_rstext">Inactive</span></a>
					<ul class="dropdown-menu">
						<c:if test="${showMakeInactiveSub}">
							<li><a href="<c:url value="makeInactive" />"> Make
									Inactive</a></li>
						</c:if>

					</ul></li>
			</c:if>



			<c:if test="${showFacultyAllocation}">
				<li class="dropdown"><a href="#"
					class="dropdown-toggle toptooltip" data-toggle="dropdown"
					role="button" aria-haspopup="true" aria-expanded="false"><i
						class="fa fa-envelope-o"></i>
						<div class="toptooltiptext">Inactive</div> <span
						class="topnav_rstext">Inactive</span></a>
					<ul class="dropdown-menu">
						<c:if test="${showFacultyAllocationSub}">
							<li><a href="<c:url value="facultyAllocation" />">
									Change Faculty</a></li>
						</c:if>

					</ul></li>
			</c:if>
				<%-- <li class="dropdown"><a href="#"
				class="dropdown-toggle toptooltip" data-toggle="dropdown"
				role="button" aria-haspopup="true" aria-expanded="false"><i
					class="fa fa-universal-access"></i>
					<div class="toptooltiptext">Student Service</div>
					<span class="topnav_rstext">Student Service</span></a>
				<ul class="dropdown-menu">
					<li><a href="<c:url value="viewCreateServiceForm" />">
							Update Student Service</a></li>
					<li><a href="<c:url value="viewServices" />"> View
							Services</a></li>

					<li><a href="<c:url value="viewEscalatedServiceForLevel3" />">
							View Escalated Students</a></li>

					<li><a href="<c:url value="createHostelServiceForm" />">
							Create Hostel Service</a></li>


				</ul></li> --%>
				














			<%-- <li class="dropdown"><a href="#"
				class="dropdown-toggle toptooltip" data-toggle="dropdown"
				role="button" aria-haspopup="true" aria-expanded="false"><i
					class="fa fa-reply-all"></i>
					<div class="toptooltiptext">Search</div> <span
					class="topnav_rstext">Search</span></a>
				<ul class="dropdown-menu">


					<li><a href="<c:url value="searchAssignmentTestForm" />">Search
							All Assignment/Test</a></li>

					<li><a href="<c:url value="addSearchForm" />">Generic
							Search</a></li>

					<li><a href="<c:url value="addQBank" />">Add Question
							Papers</a></li>

					<li><a href="<c:url value="addFacultyDetailsForm" />">Add
							Faculty Details</a></li>



				</ul></li> --%>




		</ul>
	</div>
</li>



