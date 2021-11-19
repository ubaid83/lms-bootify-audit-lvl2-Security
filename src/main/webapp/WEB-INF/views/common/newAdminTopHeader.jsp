<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@page import="java.util.*"%>
<%@page import="com.spts.lms.beans.dashboard.DashBoard"%>
<%@page import="com.spts.lms.beans.course.Course"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
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
		
		
		boolean showTest = (menuRights.containsKey("Test"));

		boolean showCreateTest = showTest ? menuRights.get("Test")
						.contains("CreateTest") : false;
				boolean showViewTest = showTest ? menuRights.get("Test")
				.contains("ViewTest") : false;
				boolean showCreateTestPool = showTest ? menuRights.get("Test")
				.contains("CreateTestPool") : false;
				boolean showViewTestPool = showTest ? menuRights.get(
				"Test").contains("ViewTestPool") : false;
				
				
				
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
		
		
		pageContext.setAttribute("showTest", showTest);
		pageContext.setAttribute("showCreateTest",
				showCreateTest);
		pageContext.setAttribute("showViewTest",
				showViewTest);
		pageContext.setAttribute("showCreateTestPool",
				showCreateTestPool);
		pageContext.setAttribute("showViewTestPool",
				showViewTestPool);

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
		
		
		pageContext.setAttribute("showCreateTest",
				false);
		pageContext.setAttribute("showViewTest",
				false);
		pageContext.setAttribute("showCreateTestPool",
				false);
		pageContext.setAttribute("showViewTestPool",
				false);
	}
%>
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
					<!-- <li class="nav-item dropdown pl-2 notif"><a href="#"
							role="button" data-toggle="dropdown" aria-haspopup="true"
							aria-expanded="false"> <span class="notifNew">&#33;</span> <span
								class="fa-round"> <i class="fas fa-bell"></i>
							</span>
						</a>
							<div id="navDropdownNotifications"
								class="dropdown-menu dropdown-menu-right"
								aria-labelledby="navbarDropdownNotifications">
								<a class="dropdown-item" href="#">Some important
									notification</a> <a class="dropdown-item" href="#">Some
									important notification</a> <a class="dropdown-item" href="#">Some
									important notification</a>
								<div class="dropdown-divider"></div>
								<a class="dropdown-item" href="#">View All</a>
							</div></li> -->
				</ul>
			</div>
		</div>
	</div>
</div>
<header class="container-fluid sticky-top">
	<nav class="navbar navbar-expand-lg navbar-light p-0">
		<!--                     <i class="fas fa-bars fa-2x mr-3 leftnav-btn" data-toggle="modal" data-target="#leftnav"></i> -->
		<a class="navbar-brand" href="homepage"> <c:choose>
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
		<button class="adminNavbarToggler" type="button"
			data-toggle="collapse" data-target="#adminNavbarCollapse">
			<i class="fas fa-bars"></i>
		</button>

		<div class="collapse navbar-collapse" id="adminNavbarCollapse">
			<ul class="navbar-nav ml-auto">

				<c:if test="${showHome}">
					<li id="home" class="nav-item active" data-toggle="tooltip"
						data-placement="bottom" title="Home"><a class="nav-link"
						href="homepage"> <i class="fas fa-home"></i> <span
							class="mobNavbar">Home</span>

					</a></li>
				</c:if>

				<c:if test="${showCourseMenu}">


					<li id="navCourseM" class="nav-item dropdown"><a
						class="nav-link dropdown-toggle" href="#" role="button"
						data-toggle="dropdown" aria-haspopup="true" aria-expanded="false" >
							<i class="fas fa-copyright"  data-toggle="tooltip"
						data-placement="bottom" title="Course"></i> <span class="mobNavbar">Course</span>
					</a>
						<div id="navDropdownSupport"
							class="dropdown-menu dropdown-menu-right"
							aria-labelledby="navbarDropdown">

							<c:if test="${showAddSingleCourse}">
								<a class="dropdown-item" href="<c:url value="addCourseForm" />">Add
									Single Course</a>
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
									href="<c:url value="uploadUserCourseEnrolForm" />">Course
									User Enrollment</a>
							</c:if>
							<c:if test="${showSearchCourseUserEnrollment}">
								<a class="dropdown-item"
									href="<c:url value="searchUserCourse" />">Search Course
									User Enrollment</a>
							</c:if>
							<c:if test="${showMultiCourseUserEnrollment}">
								<a class="dropdown-item"
									href="<c:url value="uploadUserCourseEnrolForm" />">Multiple
									Courses User Enrollment</a>
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

									<c:if test="${showCreateTest}">
										<a class="dropdown-item" href="<c:url value="/addTestFormByAdmin" />">
											Create Test</a>
									</c:if>
									<c:if test="${showViewTest}">
										<a class="dropdown-item"
											href="${pageContext.request.contextPath}/testList">View Test</a>
									</c:if>
									<c:if test="${showCreateTestPool}">
										<a class="dropdown-item"
											href="${pageContext.request.contextPath}/addTestPoolFormByAdmin">Create Test Pool</a>
									</c:if>
									<c:if test="${showViewTestPool}">
										<a class="dropdown-item"
											href="${pageContext.request.contextPath}/viewTestPools">View Test Pool</a>
									</c:if>
									<a class="dropdown-item" href="<c:url value="/gradeCenterFormForAdmin" />">
											Grade Center</a>
		
								</div></li>
						</c:if>

				<c:if test="${showAssignWieghtage}">

					<%-- <li id="navWeight" class="nav-item dropdown" data-toggle="tooltip"
								data-placement="bottom" title="Weight"><a
							class="nav-link dropdown-toggle" href="#" role="button"
							data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
								<i class="fas fa-balance-scale"></i>
						</a>
							<div id="navDropdownSupport"
								class="dropdown-menu dropdown-menu-right"
								aria-labelledby="navbarDropdown">

								<c:if test="${showAssignWieghtageSub}">
									<a class="dropdown-item" href="<c:url value="addWeight" />"> Assign Weight</a>
									<a class="dropdown-item" href="<c:url value="viewWieghtDetails" />"> View
									Assigned Weight</a>
								</c:if>

							</div></li> --%>

				</c:if>

				<c:if test="${showManageUser}">
					<li id="navUser" class="nav-item dropdown"><a
						class="nav-link dropdown-toggle" href="#" role="button"
						data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
							<i class="fa fa fa-user-plus" data-toggle="tooltip"
						data-placement="bottom" title="Manage Users"></i> <span class="mobNavbar">Manage
								Users</span>
					</a>
						<div id="navDropdownSupport"
							class="dropdown-menu dropdown-menu-right"
							aria-labelledby="navbarDropdown">

							<%-- <c:if test="${showCreateSingleUser}">
									<a class="dropdown-item" href="<c:url value="addUserForm" />">Create
											Single User</a>
								</c:if>
								<c:if test="${showUploadStudentExcel}">
									<a class="dropdown-item" href="<c:url value="uploadStudentForm" />">Upload
											Students Excel</a>
								</c:if>--%>
								<c:if test="${showUploadFacultyExcel}">
									<a class="dropdown-item" href="<c:url value="uploadFacultyForm" />">Upload
											Faculty Excel</a>
								</c:if> 
							<c:if test="${showSearchUser}">
								<a class="dropdown-item" href="<c:url value="searchUser" />">
									Search Users </a>
							</c:if>

						</div></li>
				</c:if>

				<c:if test="${showAttendence}">

					<li id="navAttendance" class="nav-item dropdown"><a
						class="nav-link dropdown-toggle" href="#" role="button"
						data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
							<i class="fa fa-address-book" data-toggle="tooltip" data-placement="bottom" title="Attendance"></i> <span class="mobNavbar">Attendance</span>
					</a>
						<div id="navDropdownSupport"
							class="dropdown-menu dropdown-menu-right"
							aria-labelledby="navbarDropdown">

							<c:if test="${showMarkAttendence}">
								<a class="dropdown-item"
									href="<c:url value="addAttendanceForm" />">Mark Attendance
								</a>
							</c:if>

						</div></li>

				</c:if>

				<c:if test="${showCourseOverview}">

					<li id="navCourseO" class="nav-item dropdown"><a
						class="nav-link dropdown-toggle" href="#" role="button"
						data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
							<i class="fa fa-wpforms" data-toggle="tooltip"
						data-placement="bottom" title="Course Overview"></i> <span class="mobNavbar">Course
								Overview</span>
					</a>
						<div id="navDropdownSupport"
							class="dropdown-menu dropdown-menu-right"
							aria-labelledby="navbarDropdown">

							<c:if test="${showCourseOverview}">
								<a class="dropdown-item"
									href="<c:url value="addCourseOverviewForm" />">Add Course
									Overview</a>
							</c:if>

						</div></li>

				</c:if>
				
				
				
				
		<!-- Master Validation || Student Detail Confirmation Period Admin Side-->
				
				<%
				List<String> schoolabbrList = (List)session.getAttribute("schoolListMaster");
				String appName = (String)session.getAttribute("appName");
			
				if(null != schoolabbrList && schoolabbrList.contains(appName)){
					%>
					<li id="navIca" class="nav-item dropdown"><a
					class="nav-link dropdown-toggle" href="#" role="button"
					data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
						<i class="fas fa-check"  data-toggle="tooltip"
					data-placement="bottom" title="Master Validation"></i> <span class="mobNavbar">Master Validation</span>
				</a>
					<div id="navDropdownSupport"
						class="dropdown-menu dropdown-menu-right"
						aria-labelledby="navbarDropdown">
						<a class="dropdown-item" href="<c:url value="createStudentDetails" />">Create / View Student Master</a>					
					</div></li> 
				<%
				}
				%>
						<%-- <a class="dropdown-item" href="<c:url value="viewstudents" />">View Students</a> 
						<a class="dropdown-item"
							href="<c:url value="downloadStudentDetailConfirmation" />">Download Report</a>	 --%>
				<!-- End  -->
				
				
				
				
				

				<!-- program -->
				<li id="program" class="nav-item active" data-toggle="tooltip"
					data-placement="bottom" title="My Program"><a class="nav-link"
					href="<c:url value="showMyCourseStudents" />"> <i
						class="fas fa-file-powerpoint"></i> <span class="mobNavbar">My
							Program</span>

				</a></li>

				<!-- libraries -->
				<li id="library" class="nav-item active" data-toggle="tooltip"
					data-placement="bottom" title="Libraries"><a class="nav-link"
					href="<c:url value="viewLibraryAnnouncements" />"> <i
						class="fas fa-book"></i> <span class="mobNavbar">Libraries</span>

				</a></li>


				<c:if test="${showSearch}">

					<li id="navSearch" class="nav-item dropdown"><a
						class="nav-link dropdown-toggle" href="#" role="button"
						data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
							<i class="fa fa-search" data-toggle="tooltip"
						data-placement="bottom" title="Search"></i> <span class="mobNavbar">Search</span>
					</a>
						<div id="navDropdownSupport"
							class="dropdown-menu dropdown-menu-right"
							aria-labelledby="navbarDropdown">

							<c:if test="${showAssTestSearch}">
								<a class="dropdown-item"
									href="<c:url value="searchAssignmentTestForm" />">Search
									All Assignment/Test</a>
							</c:if>
							<c:if test="${showGenericSearch}">
								<a class="dropdown-item" href="<c:url value="addSearchForm" />">Generic
									Search</a>
							</c:if>
							<%-- <c:if test="${showAddQBank}">
									<a class="dropdown-item" href="<c:url value="addQBank" />">Add Question
										Papers</a>
								</c:if> --%>
							<%-- <c:if test="${showAddFacultyDetails}">
									<a class="dropdown-item" href="<c:url value="addFacultyDetailsForm" />">Add
										Faculty Details</a>
								</c:if> --%>
							<a class="dropdown-item" href="<c:url value="viewQuery" />">Preview
								Queries List</a> <a class="dropdown-item"
								href="<c:url value="addFAQForm" />">Add FAQs</a> <a
								class="dropdown-item" href="<c:url value="searchUser" />">Search
								Users</a>

						</div></li>

				</c:if>
				
				

				<c:if test="${showAnnouncement}">

					<li id="navAnnouncement" class="nav-item dropdown"><a class="nav-link dropdown-toggle"
						href="#" role="button" data-toggle="dropdown" aria-haspopup="true"
						aria-expanded="false"> <i class="fa fa-bullhorn"
						data-toggle="tooltip" data-placement="bottom"
						title="Announcements"></i> <span
							class="mobNavbar">Announcements</span>
					</a>
						<div id="navDropdownSupport"
							class="dropdown-menu dropdown-menu-right"
							aria-labelledby="navbarDropdown">

							<c:if test="${showCreateAnnouncement}">
								<a class="dropdown-item"
									href="<c:url value="addAnnouncementForm" />">Create
									Announcement for Institute</a>
							</c:if>
							<c:if test="${showSearchAnnouncements}">
								<a class="dropdown-item"
									href="<c:url value="searchAnnouncement" />">Search
									Announcements</a>
							</c:if>
							<c:if test="${showCreateAnnouncementForProgram}">
									<a class="dropdown-item" href="<c:url value="addAnnouncementFormMultiProgram" />">Create
									Announcement For Program</a>
								</c:if>
								<%-- <c:if test="${showCreateAnnouncementForProgram}">
									<a class="dropdown-item" href="<c:url value="addTimeTableForm" />">Create
									Announcement For TimeTable </a>
								</c:if> --%>

						</div></li>

				</c:if>

				<c:if test="${showCalendar}">

					<%-- <li id="navCalendar" class="nav-item dropdown" data-toggle="tooltip"
								data-placement="bottom" title="Calendar"><a
							class="nav-link dropdown-toggle" href="#" role="button"
							data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
								<i class="fas fa-calendar-alt"></i>
						</a>
							<div id="navDropdownSupport"
								class="dropdown-menu dropdown-menu-right"
								aria-labelledby="navbarDropdown">

								<c:if test="${showAddEvents}">
									<a class="dropdown-item" href="<c:url value="addCalenderEventForm" />">Add
									Event</a>
								</c:if>
								<c:if test="${showViewEvents}">
									<a class="dropdown-item" href="<c:url value="viewEvents" />">View Event</a>
								</c:if>

							</div></li> --%>

				</c:if>

				<%-- <c:if test="${showForum}">
					
						<li id="navForum" class="nav-item dropdown" data-toggle="tooltip"
								data-placement="bottom" title="Discussion Forum"><a
							class="nav-link dropdown-toggle" href="#" role="button"
							data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
								<i class="fa fa-commenting"></i>
						</a>
							<div id="navDropdownSupport"
								class="dropdown-menu dropdown-menu-right"
								aria-labelledby="navbarDropdown">

								<c:if test="${showCreateForum}">
									<a class="dropdown-item" href="<c:url value="createForumForm" />">Create
									Forum</a>
								</c:if>
								<c:if test="${showViewsForum}">
									<a class="dropdown-item" href="<c:url value="viewForum" />">View Forum</a>
								</c:if>

							</div></li>
					
					</c:if> --%>

				<%-- <c:if test="${showGrievances}">
					
						<li id="navGrievances" class="nav-item dropdown" data-toggle="tooltip"
								data-placement="bottom" title="Grievances"><a
							class="nav-link dropdown-toggle" href="#" role="button"
							data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
								<i class="fa fa-file-text"></i>
						</a>
							<div id="navDropdownSupport"
								class="dropdown-menu dropdown-menu-right"
								aria-labelledby="navbarDropdown">

								<c:if test="${showViewGrievances}">
									<a class="dropdown-item" href="<c:url value="viewAllGrievances" />">View
									Grievances</a>
								</c:if>
								<c:if test="${showUploadGrievances}">
									<a class="dropdown-item" href="<c:url value="uploadGrievanceConfigItemsForm" />">Upload
									Grievances Items</a>
								</c:if>

							</div></li>
					
					</c:if> --%>

				<!-- change program -->
				<li id="changeProgram" class="nav-item active" data-toggle="tooltip"
					data-placement="bottom" title="Change Program"><a
					class="nav-link" href="https://dev-portal.svkm.ac.in/usermgmt"> <i
						class="fas fa-exchange-alt"></i> <span class="mobNavbar">Change
							Program</span>

				</a></li>

				<c:if test="${showFeedback}">

					<li id="navFeedback" class="nav-item dropdown"><a
						class="nav-link dropdown-toggle" href="#" role="button"
						data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
							<i class="fa fa-reply-all" data-toggle="tooltip" data-placement="bottom" title="Feedback"></i> <span class="mobNavbar">Feedback</span>
					</a>
						<div id="navDropdownSupport"
							class="dropdown-menu dropdown-menu-right"
							aria-labelledby="navbarDropdown">

							<c:if test="${showAddFeedback}">
								<a class="dropdown-item"
									href="<c:url value="addFeedbackForm" />">Add Feedback</a>
							</c:if>
							<c:if test="${showViewFeedback}">
								<a class="dropdown-item" href="<c:url value="searchFeedback" />">View
									Feedback</a>
							</c:if>
							<c:if test="${showAllocateFeedback}">
								<a class="dropdown-item"
									href="<c:url value="addStudentFeedbackForm" />">Allocate
									Feedback</a>
							</c:if>
							<a class="dropdown-item"
								href="<c:url value="addStudentFeedbackFormForCourses" />">Allocate
								Feedback CourseWise</a>

						</div></li>

				</c:if>


				<!-- ICA Admin Menus -->

				  <li id="navIca" class="nav-item dropdown"><a
					class="nav-link dropdown-toggle" href="#" role="button"
					data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
						<i class="fas fa-marker"  data-toggle="tooltip"
					data-placement="bottom" title="ICA"></i> <span class="mobNavbar">ICA</span>
				</a>
					<div id="navDropdownSupport"
						class="dropdown-menu dropdown-menu-right"
						aria-labelledby="navbarDropdown">


						<a class="dropdown-item" href="<c:url value="addIcaForm" />">Create
							ICA</a>
							<a class="dropdown-item" href="<c:url value="addIcaFormForCoursera" />">Create
							ICA Coursera</a> 
							 <a class="dropdown-item"
							href="<c:url value="searchIcaList" />">Search ICA</a> <a
							class="dropdown-item" href="<c:url value="publishIca" />">Publish
							ICA</a> 
							 <a
							class="dropdown-item" href="<c:url value="publishIcaComponents" />">Publish
							ICA Components</a>
							<a class="dropdown-item"
							href="<c:url value="showIcaQueries" />">ICA Raised Queries</a> <a
							class="dropdown-item"
							href="<c:url value="downloadIcaReportForm" />">ICA Report</a>
							 <a
							class="dropdown-item"
							href="<c:url value="addIcaFormForDivision" />">Create ICA For Divisions</a>
							 <a
							class="dropdown-item"
							href="<c:url value="addIcaFormForNonEventModules" />">Create ICA For Non-Event Modules</a>
							
							<a
							class="dropdown-item"
							href="<c:url value="addNonCreditIcaForm" />">Create Non Credit ICA</a>
							
							<a
							class="dropdown-item"
							href="<c:url value="searchNsList" />">Search Non Credit ICA</a> 
							
							<c:if test="${userBean.username eq '32200056_ADMIN' && ( (appNameForTee eq 'SBM-NM-M') || (appNameForTee eq 'PDSEFBM') ) }">
                        		<a class="dropdown-item" href="<c:url value="icaListBySupportAdmin" />">Extend ICA Date</a>
                 		 	</c:if>
					</div></li> 
					
				
				<!-- Assignment -->
				
				<c:if test="${userBean.username eq '32200513_ADMIN' || userBean.username eq '32200500_ADMIN' || userBean.username eq '32200354_ADMIN' || userBean.username eq 'CIPS_ADMIN'}">
				
				<li id="navAssignment" class="nav-item dropdown">
					<a class="nav-link dropdown-toggle" href="#" role="button"
						data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
							<i class="fas fa-newspaper"  data-toggle="tooltip"
						data-placement="bottom" title="Assignment"></i> <span class="mobNavbar">Assignment</span>
					</a>
					<div id="navDropdownSupport"
						class="dropdown-menu dropdown-menu-right"
						aria-labelledby="navbarDropdown">
						<a class="dropdown-item" href="<c:url value="createAssignmentByAdmin" />">Create Assignment For Student</a>
						<a class="dropdown-item" href="<c:url value="searchAdminAssignment" />">View Assignment</a>					
					</div>
				</li> 
				
				</c:if>
				<!--  TEE Starts -->	
				
				<%-- <c:if test="${fn:contains( appNameForTee, 'SBM-NM') || (appNameForTee eq 'PDSEFBM') || (appNameForTee eq 'SBM-Indore') || (appNameForTee eq 'COE')}"> --%>
				<li id="navIca" class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" href="#" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                        <i class="fab fa-readme"  data-toggle="tooltip" data-placement="bottom" title="TEE"></i> 
                        <span class="mobNavbar">TEE</span>
                    </a>
                    <div id="navDropdownSupport" class="dropdown-menu dropdown-menu-right" aria-labelledby="navbarDropdown">
						<a class="dropdown-item" href="<c:url value="addTeeForm" />">Create TEE</a> 
                        <a class="dropdown-item" href="<c:url value="addTeeFormForDivision" />">Create TEE For Divisions</a>
				        <a class="dropdown-item" href="<c:url value="searchTeeList" />">Search TEE</a>
                        <a class="dropdown-item" href="<c:url value="publishTeeForm" />">Publish TEE</a>
                        <a class="dropdown-item" href="<c:url value="showTeeQueries" />">TEE Raised Queries</a>
                        <a class="dropdown-item" href="<c:url value="downloadTeeReportForm" />">TEE Report</a>
                       <c:if test="${userBean.username eq '32200056_ADMIN' && ( (appNameForTee eq 'SBM-NM-M') || (appNameForTee eq 'PDSEFBM') ) }">
                        	<a class="dropdown-item" href="<c:url value="teeListBySupportAdmin" />">Extend Tee Date</a>
                        </c:if>
                     </div>
                </li>
                <%-- </c:if> --%>
                <!--   TEE Ends  -->

				<!-- reports -->
				<li id="report" class="nav-item active"><a class="nav-link"
					href="downloadReportMyCourseStudentForm"> <i
						class="fas fa-file-excel"  data-toggle="tooltip"
					data-placement="bottom" title="Reports"></i> <span class="mobNavbar">Reports</span>

				</a></li>
				
				<!-- content report -->
				<li id="report" class="nav-item active"><a class="nav-link"
					href="downloadContentReportForm"> <i class="fa fa-book"></i>
						<span class="mobNavbar">Content Report</span>
				
				</a></li>
				<!-- content report ends-->

				<c:if test="${showMakeInactive}">

					<li id="navMkInactive" class="nav-item dropdown"
						data-toggle="tooltip" data-placement="bottom" title="Inactive"><a
						class="nav-link dropdown-toggle" href="#" role="button"
						data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
							<i class="fa fa-envelope-o"></i> <span class="mobNavbar">Inactive</span>
					</a>
						<div id="navDropdownSupport"
							class="dropdown-menu dropdown-menu-right"
							aria-labelledby="navbarDropdown">

							<c:if test="${showMakeInactiveSub}">
								<a class="dropdown-item" href="<c:url value="makeInactive" />">
									Make Inactive</a>
							</c:if>

						</div></li>

				</c:if>

				<c:if test="${showFacultyAllocation}">
					<li id="navFacultyAlloc" class="nav-item dropdown"><a
						class="nav-link dropdown-toggle" href="#" role="button"
						data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
							<i class="fa fa-envelope-o" data-toggle="tooltip" data-placement="bottom" title="Faculty Allocation"></i> <span class="mobNavbar">Faculty
								Allocation</span>
					</a>
						<div id="navDropdownSupport"
							class="dropdown-menu dropdown-menu-right"
							aria-labelledby="navbarDropdown">

							<c:if test="${showFacultyAllocationSub}">
								<a class="dropdown-item"
									href="<c:url value="facultyAllocation" />"> Change Faculty</a>
							</c:if>

						</div></li>
				</c:if>


				<li class="nav-item dropdown"><a
					class="nav-link dropdown-toggle" href="#" role="button"
					data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
						<img src="<c:url value="/resources/images/img-user.png" />"
						class="user-ico" title="Name of the user" alt="Name of the user" />
						<span>${userBean.firstname} ${userBean.lastname}</span> <!--Truncate to 12 Char -->
				</a>
					<div class="dropdown-menu dropdown-menu-right"
						aria-labelledby="navbarDropdown">
						<a class="dropdown-item" href="#"><i
							class="fas fa-id-card-alt"></i> Admin ID: ${userBean.username}</a> <a
							class="dropdown-item" href="#"><i class="fas fa-at"></i>
							${userBean.email}</a> <a class="dropdown-item" href="#"><i
							class="fas fa-phone"></i> ${userBean.mobile}</a> <a
							class="dropdown-item"
							href="${pageContext.request.contextPath}/profileDetails"><i
							class="fas fa-user"></i> My Profile</a> <a class="dropdown-item"
							href="${pageContext.request.contextPath}/changePasswordForm"><i
							class="fas fa-key"></i> Change Password</a> <a class="dropdown-item"
							href="${pageContext.request.contextPath}/changePasswordFormStudentByAdmin"><i
							class="fas fa-key"></i> Change Password For Users</a> <a
							class="dropdown-item"
							href="${pageContext.request.contextPath}/loggedout"><i
							class="fas fa-sign-out-alt"></i> Logout</a>


					</div></li>

			</ul>
		</div>
	</nav>
</header>