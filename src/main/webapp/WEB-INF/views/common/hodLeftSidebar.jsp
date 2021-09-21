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
			
				<li><a><i class="fa fa-users"></i> Groups <span
						class="fa fa-chevron-down"></span></a>
					<ul class="nav child_menu">

						<li><a
							href="${pageContext.request.contextPath}/searchFacultyGroups?courseId=${courseId}">View
								Group</a></li>

					</ul></li>
			
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

			

				<li><a><i class="fa fa-bullhorn"></i> Announcements <span
						class="fa fa-chevron-down"></span></a>
					<ul class="nav child_menu">
						<li><a
							href="${pageContext.request.contextPath}/addAnnouncementForm?courseId=${courseId}">Add
								Announcement</a></li>
						<li><a
							href="${pageContext.request.contextPath}/viewUserAnnouncements">View
								Announcement</a></li>
					</ul></li>
			
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
			
			
				<li><a><i class="fa fa-folder-open"></i> Learning Resource
						<span class="fa fa-chevron-down"></span></a>
					<ul class="nav child_menu">
						<%-- <li><a
							href="${pageContext.request.contextPath}/getContentUnderAPathForFaculty?courseId=${courseId}">
								View Content</a></li>
 --%>
 
 <li><a
							href="${pageContext.request.contextPath}/getContentUnderAPathForFacultyForModule">
								View Content</a></li>
					</ul></li>
		

		</ul>



	</div>

</div>
<!-- /sidebar menu -->
<!--    </div>
            </div> -->