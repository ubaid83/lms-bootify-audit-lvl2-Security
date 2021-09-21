<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

<%
	String courseId = request.getParameter("courseId");
%>

<%--   
  <div class="col-md-3 left_col">
                <div class="left_col scroll-view">
                    <div class="navbar nav_title" style="border: 0;">
                        <a href="index.html"><img src="<c:url value="/resources/images/logo.gif" />" alt="" ></a>
                    </div>

                    <div class="clearfix"></div>
 --%>

<!-- sidebar menu -->
<div id="sidebar-menu" class="main_menu_side hidden-print main_menu">
	<div class="menu_section">
		<ul class="nav side-menu">
			<sec:authorize access="hasRole('ROLE_FACULTY')">
				<li><a><i class="fa fa-file-text"></i> Tests <span
						class="fa fa-chevron-down"></span></a>
					<ul class="nav child_menu">
						<li><a
							href="${pageContext.request.contextPath}/addTestForm?courseId=${courseId}">Create
								Test</a></li>
						<c:if test="${courseId ne null ||  not empty courseId}">
						
							<%-- <li><a
								href="${pageContext.request.contextPath}/addOfflineTestForm?courseId=${courseId}">Create
									Offline Test</a></li>
							<li><a
								href="${pageContext.request.contextPath}/viewOfflineTests?courseId=${courseId}">View
									Offline Test</a></li> --%>
									
							<li><a
								href="${pageContext.request.contextPath}/addTestPoolForm?courseId=${courseId}">Create
									Test Pool</a></li>
							<li><a
								href="${pageContext.request.contextPath}/viewTestPools?courseId=${courseId}">View
									Test Pools For Course</a></li>
						</c:if>
						<li><a
							href="${pageContext.request.contextPath}/testList?courseId=${courseId}">View
								Test</a></li>
						<li><a
							href="${pageContext.request.contextPath}/searchTest?courseId=${courseId}">Search
								Test</a></li>
						<%-- <li><a
							href="${pageContext.request.contextPath}/configureQuestions?courseId=${courseId}">Configure
								Questions</a></li> --%>
						<c:if test="${ not empty courseId || courseId ne null}">
							<li><a
								href="${pageContext.request.contextPath}/uploadTestQuestionForm?courseId=${courseId}&type=Subjective">Configure
									Subjective From Question Bank</a></li>
							<li><a
								href="${pageContext.request.contextPath}/uploadTestQuestionForm?courseId=${courseId}&type=Objective">Configure
									Objective From Question Bank</a></li>
						</c:if>
					</ul></li>
			</sec:authorize>
			<sec:authorize access="hasRole('ROLE_HOD')">
				<li><a><i class="fa fa-file-text"></i> Tests <span
						class="fa fa-chevron-down"></span></a>
					<ul class="nav child_menu">

						<li><a
							href="${pageContext.request.contextPath}/testList?courseId=${courseId}">View
								Test</a></li>
						<li><a
							href="${pageContext.request.contextPath}/searchTest?courseId=${courseId}">Search
								Test</a></li>
						<%-- <li><a
							href="${pageContext.request.contextPath}/configureQuestions?courseId=${courseId}">Configure
								Questions</a></li> --%>

					</ul></li>
			</sec:authorize>
			<sec:authorize access="hasRole('ROLE_FACULTY')">
				<li><a><i class="fa fa-users"></i> Groups <span
						class="fa fa-chevron-down"></span></a>
					<ul class="nav child_menu">
						<li><a
							href="${pageContext.request.contextPath}/createGroupForm?courseId=${courseId}">Create
								Group</a></li>
						<li><a
							href="${pageContext.request.contextPath}/searchFacultyGroups?courseId=${courseId}">View
								Group</a></li>
						<li><a
							href="${pageContext.request.contextPath}/removeStudentsFromGroupForm?courseId=${courseId}">Remove
								Students from Group</a></li>
					</ul></li>
			</sec:authorize>
			<sec:authorize access="hasRole('ROLE_HOD')">
				<li><a><i class="fa fa-users"></i> Groups <span
						class="fa fa-chevron-down"></span></a>
					<ul class="nav child_menu">

						<li><a
							href="${pageContext.request.contextPath}/searchFacultyGroups?courseId=${courseId}">View
								Group</a></li>

					</ul></li>
			</sec:authorize>
			<sec:authorize access="hasRole('ROLE_FACULTY')">
				<li><c:if
						test="${courseRecord.id ne '' or not empty courseRecord.id}">

						<a><i class="fa fa-newspaper-o"></i> Assignments <span
							class="fa fa-chevron-down"></span></a>

						<ul class="nav child_menu">
							<c:if test="${ not empty courseId || courseId ne null}">
								<li><a
									href="${pageContext.request.contextPath}/createAssignmentFromGroup?courseId=${courseId}">Create
										For Group</a></li>
							</c:if>
							<li><a
								href="${pageContext.request.contextPath}/createAssignmentFromMenu?courseId=${courseId}">Create
									For Student</a></li>
							<li><a
								href="${pageContext.request.contextPath}/searchFacultyAssignment?courseId=${courseId}">View</a></li>
							<li><a
								href="${pageContext.request.contextPath}/searchAssignmentToEvaluate?courseId=${courseId}">Evaluate
									with Advance Search</a></li>
							<c:if test="${ not empty courseId || courseId ne null}">
								<li><a
									href="${pageContext.request.contextPath}/createGroupAssignmentsForm?courseId=${courseId}">
										Create Multiple Assignments for Groups</a></li>
							</c:if>
							<li><a
								href="${pageContext.request.contextPath}/evaluateByStudentForm?courseId=${courseId}">Evaluate
									Student</a></li>
							<li><a
								href="${pageContext.request.contextPath}/evaluateByStudentGroupForm?courseId=${courseId}">Evaluate
									Group</a></li>
							<li><a class=""
								href="${pageContext.request.contextPath}/lateSubmissionApprovalForm?courseId=${courseId}">Late
									Submitted Assignments</a></li>

						</ul>
					</c:if> <c:if test="${courseRecord.id eq '' and  empty courseRecord.id}">


						<ul class="nav child_menu">
							<%-- <li><a class=""
								href="${pageContext.request.contextPath}/createAssignmentFromGroup">For
									Group</a></li> --%>
							<%-- <li><a class=""
								href="${pageContext.request.contextPath}/createAssignmentFromMenu">For
									Student</a></li> --%>
							<!-- 							<li><a class="" -->
							<%-- 								href="${pageContext.request.contextPath}/createGroupAssignmentsForm?courseId=${courseId}"> --%>
							<!-- 									Multiple Assignments for Groups</a></li> -->
							<li><a class=""
								href="${pageContext.request.contextPath}/searchFacultyAssignment">View</a></li>
							<li><a class=""
								href="${pageContext.request.contextPath}/searchAssignmentToEvaluate">Evaluate</a></li>
							<li><a class=""
								href="${pageContext.request.contextPath}/evaluateByStudentForm">Evaluate
									For Student</a></li>
						</ul>
					</c:if></li>

			</sec:authorize>
			<sec:authorize access="hasRole('ROLE_HOD')">
				<li><c:if
						test="${courseRecord.id ne '' or not empty courseRecord.id}">

						<a><i class="fa fa-newspaper-o"></i> Assignments <span
							class="fa fa-chevron-down"></span></a>

						<ul class="nav child_menu">

							<li><a
								href="${pageContext.request.contextPath}/searchFacultyAssignment?courseId=${courseId}">View</a></li>




						</ul>
					</c:if> <c:if test="${courseRecord.id eq '' and  empty courseRecord.id}">


						<ul class="nav child_menu">
							<%-- <li><a class=""
								href="${pageContext.request.contextPath}/createAssignmentFromGroup">For
									Group</a></li> --%>
							<%-- <li><a class=""
								href="${pageContext.request.contextPath}/createAssignmentFromMenu">For
									Student</a></li> --%>
							<!-- 							<li><a class="" -->
							<%-- 								href="${pageContext.request.contextPath}/createGroupAssignmentsForm?courseId=${courseId}"> --%>
							<!-- 									Multiple Assignments for Groups</a></li> -->
							<li><a class=""
								href="${pageContext.request.contextPath}/searchFacultyAssignment">View</a></li>

						</ul>
					</c:if></li>

			</sec:authorize>
			<c:if test="${ not empty courseId || courseId ne null}">
				<li><a
					href="${pageContext.request.contextPath}/classParticipation?courseId=${courseId}"
					class=""><i class="fa fa-user"></i>Class Participation</a></li>
			</c:if>

			<sec:authorize access="hasAnyRole('ROLE_FACULTY','ROLE_HOD')">

				<li><a><i class="fa fa-bullhorn"></i> Announcements <span
						class="fa fa-chevron-down"></span></a> <c:if test="${empty courseId}">
						<ul class="nav child_menu">
							<li><a
								href="${pageContext.request.contextPath}/viewUserAnnouncements">View
									Announcement</a></li>
						</ul>
					</c:if> <c:if test="${not empty courseId}">
						<ul class="nav child_menu">
							<li><a
								href="${pageContext.request.contextPath}/addAnnouncementForm?courseId=${courseId}">Add
									Announcement</a></li>
							<li><a
								href="${pageContext.request.contextPath}/viewUserAnnouncements">View
									Announcement</a></li>
						</ul>
					</c:if></li>
			</sec:authorize>
			<li><a><i class="fa fa-commenting"></i> Discussion Forums <span
					class="fa fa-chevron-down"></span></a>
				<ul class="nav child_menu">
					<li><a
						href="${pageContext.request.contextPath}/createForumForm?courseId=${courseId}">Create
							Forum</a></li>
					<li><a
						href="${pageContext.request.contextPath}/viewForum?courseId=${courseId}">View
							Forum</a></li>

				</ul></li>
			<sec:authorize access="hasRole('ROLE_FACULTY')">
				<c:if test="${courseRecord.id ne '' or not empty courseRecord.id}">
					<li><a
						href="${pageContext.request.contextPath}/showMyCourseStudents?courseId=${courseId}"
						class=""><i class="fa fa-file-text"></i>My Course Students</a></li>

					<li><a
						href="${pageContext.request.contextPath}/gradeCenter?courseId=${courseId}"
						class=""><i class="fa fa-pie-chart"></i>Grade Center</a></li>
					<li><a
						href="${pageContext.request.contextPath}/createReportsForm?courseId=${courseId}"
						class=""><i class="fa fa-bar-chart" aria-hidden="true"></i>Report</a></li>
				</c:if>
			</sec:authorize>
<%-- 			<sec:authorize access="hasAnyRole('ROLE_FACULTY','ROLE_HOD')">
				<li><a><i class="fa fa-folder-open"></i> Learning Resource
						<span class="fa fa-chevron-down"></span></a>
					<ul class="nav child_menu">
					 <li><a
							href="${pageContext.request.contextPath}/getContentUnderAPathForFaculty?courseId=${courseId}">
								View Content</a></li>
								
								
						<li><a href="http://ezproxy.svkm.ac.in:2048/login">Library
								Link</a></li>

					</ul></li>
			</sec:authorize> --%>
			<sec:authorize access="hasAnyRole('ROLE_FACULTY','ROLE_HOD')">
				<li><a><i class="fa fa-folder-open"></i> Learning Resource
						<span class="fa fa-chevron-down"></span></a>
					<ul class="nav child_menu">
						
								<li><a
							href="${pageContext.request.contextPath}/getContentUnderAPathForFacultyForModule">
								View Content</a></li>

						<li><a href="http://ezproxy.svkm.ac.in:2048/login">Library
								Link</a></li>

					</ul></li>
			</sec:authorize>
			
			<%-- <sec:authorize access="hasAnyRole('ROLE_FACULTY')">
				<li><a><i class="fa fa-folder-open"></i> Graded Weight <span
						class="fa fa-chevron-down"></span></a>
					<ul class="nav child_menu">
						<li><a
							href="${pageContext.request.contextPath}/generateGrades?courseId=${courseId}">
								View Graded Weight</a></li>



					</ul></li>
			</sec:authorize> --%>


		</ul>



	</div>

</div>
<!-- /sidebar menu -->
<!--    </div>
            </div> -->