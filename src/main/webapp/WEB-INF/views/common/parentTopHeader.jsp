<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@page import="java.util.*"%>
<%@page import="com.spts.lms.beans.dashboard.DashBoard"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>


<li class="top_right_menu">
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
			
				<li class="active"><a href="<c:url value="homepage" />"
				><i class="fa fa-line-chart"></i>Dashboard</a></li>
				
				
				<li class="dropdown"><a href="#" class="dropdown-toggle"
					data-toggle="dropdown" role="button" aria-haspopup="true"
					aria-expanded="false"> <i class="fa fa-graduation-cap"></i> Grade
						Center
				</a>
					<ul class="dropdown-menu">

						<li><a href="<c:url value="gradeCenterFormForParents" />">View
								Grade Center </a></li>
					</ul></li>
					
					
					<li class="dropdown"><a href="#" class="dropdown-toggle"
					data-toggle="dropdown" role="button" aria-haspopup="true"
					aria-expanded="false"> <i class="fa fa-comment-o"></i> Know
						Your Child Attendance
				</a>
					<ul class="dropdown-menu">
<%-- 
						<li><a href="<c:url value="knowChildFacultyForm" />"> Get
								Faculty Details </a></li>
 --%>
						<li><a href="<c:url value="viewDailyAttendanceByStudent" />">View
								Attendance </a>
								<%-- <form action='${pageContext.request.contextPath}/attendance/showStudentAttendance.jsp'><input type='submit' value='attendance' /></form> --%></li>
					</ul></li>
					
					
					

			</sec:authorize>



		</ul>
	</div>
</li>


