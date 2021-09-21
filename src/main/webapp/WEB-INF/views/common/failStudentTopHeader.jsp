<%@page import="java.util.*"%>
<%@page import="com.spts.lms.beans.dashboard.DashBoard"%>


<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%
	String redirectURL = "https://portal.svkm.ac.in/usermgmt/login";
	response.setStatus(response.SC_MOVED_TEMPORARILY);
	response.setHeader("Location", redirectURL);
%>

<meta http-equiv="refresh"
	content="<%=session.getMaxInactiveInterval()%>;url=https://portal.svkm.ac.in/usermgmt/login" />

<%
	String activeMenu = (String) request.getParameter("activeMenu");
%>
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

<style>
@media (min-width: 992px)
.topFixxMenuFailStudent {
    position: fixed;
    width: calc(100% - 69px);
    top: 0;
    left: 69px;
    z-index: 100;
</style>
<div class="topFixxMenuFailStudent">
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
						<%--  <li class="nav-item dropdown pl-2 notif">
                            <a href="#" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                <span class="notifNew">${announcementForTopMenu.size()}</span>
                                <span class="fa-round"> <i class="fas fa-bell"></i> </span>
                            </a>
                            <div id="navDropdownNotifications" class="dropdown-menu dropdown-menu-right" aria-labelledby="navbarDropdownNotifications">
								<c:forEach items="${announcementForTopMenu}" var="announcement">
								<a class="dropdown-item" href="#">${announcement.description}</a>            
								</c:forEach>
					            <a class="dropdown-item" href="#">Some important notification</a>
                                <a class="dropdown-item" href="#">Some important notification</a>
                                <div class="dropdown-divider"></div>
                                <a class="dropdown-item" href="#">View All</a>
                            </div>
                            
                            						
							<li><a> <span></span>  <span class="time">${announcement.createdDate}</span>
									
							</a></li>



						 
                        </li>--%>
					</ul>
				</div>
			</div>
		</div>
	</div>

	<header class="container-fluid">
		<nav class="navbar navbar-expand-lg navbar-light p-0">
			<!-- <i class="fas fa-bars fa-2x mr-3 leftnav-btn" data-toggle="modal"
				data-target="#leftnav"></i>  --><a class="navbar-brand"
				href="${pageContext.request.contextPath}/homepage"> <c:choose>
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
					<sec:authorize access="hasRole('ROLE_STUDENT')">
						<!-- <li id="navCalendar" class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                            <i class="fas fa-calendar-alt"></i> Calendar
                        </a>
                        <div id="navDropdownCalendar" class="dropdown-menu dropdown-menu-right" aria-labelledby="navbarDropdown">
                            <a class="dropdown-item" href="#">Current Events</a>
                            <a class="dropdown-item" href="#">Upcoming Events</a>
                            <a class="dropdown-item" href="#">Recent Events</a>
                        </div>
                    </li> -->
                  
                    
						<li id="navMessage" class="nav-item"><a
							class="nav-link " target="_blank" href="https://g21.tcsion.com/LX/INDEXES/AppLaunchSAML?app_id=9517&org_id=${orgId}&serviceid=20&lsamltype=${appName}" role="button"
							>

								<i class="fas fa-file-alt"></i> Exam
						</a>
						</li>
							<%-- <div id="navDropdownMessage"
								class="dropdown-menu dropdown-menu-right"
								aria-labelledby="navbarDropdown">
								<a class="dropdown-item"
									href="${pageContext.request.contextPath}/viewMyMessage">View
									Message</a> <a class="dropdown-item"
									href="${pageContext.request.contextPath}/createMessageForm">Create
									Message</a>
							</div></li> --%>
	<%-- 					<li id="navMessage" class="nav-item dropdown"><a
							class="nav-link dropdown-toggle" href="#" role="button"
							data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
								<i class="fas fa-book-reader"></i> Library
						</a>
							<div id="navDropdownMessage"
								class="dropdown-menu dropdown-menu-right"
								aria-labelledby="navbarDropdown">
								<a class="dropdown-item"
									href="${pageContext.request.contextPath}/viewLibraryAnnouncements">View
									Library</a>
							</div></li> --%>
	<%-- 					<li id="navSupport" class="nav-item dropdown"><a
							class="nav-link dropdown-toggle" href="#" role="button"
							data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
								<i class="fas fa-life-ring"></i> Support
						</a>
							<div id="navDropdownSupport"
								class="dropdown-menu dropdown-menu-right"
								aria-labelledby="navbarDropdown">
								<a class="dropdown-item"
									href="${pageContext.request.contextPath}/overview?courseId=">Support</a>
								<a class="dropdown-item"
									href="${pageContext.request.contextPath}/createQueryForm">Raise
									a ticket</a> <a class="dropdown-item"
									href="${pageContext.request.contextPath}/viewQueryResponse">Track
									your ticket status</a> <a class="dropdown-item"
									href="${pageContext.request.contextPath}/viewFAQ">FAQ</a>
							</div></li> --%>
	<%-- 					<li id="navQuicklinks" class="nav-item dropdown"><a
							class="nav-link dropdown-toggle" href="#" role="button"
							data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
								<i class="fas fa-link"></i> Quicklinks
						</a>
							<div id="navDropdownQuicklinks"
								class="dropdown-menu dropdown-menu-right"
								aria-labelledby="navbarDropdown">
								<a class="dropdown-item"
									href="${pageContext.request.contextPath}/viewUserAnnouncementsSearchNew"><i
									class="fas fa-bullhorn mr-2"></i> Announcements</a> <a
									class="dropdown-item"
									href="http://ezproxy.svkm.ac.in:2048/login"><i
									class="fas fa-book-open mr-2"></i> Library</a>
								<div class="dropdown-divider"></div>
								<a class="dropdown-item"
									href="${pageContext.request.contextPath}/viewAssignmentFinal">Assignment</a>
								<a class="dropdown-item"
									href="${pageContext.request.contextPath}/viewTestFinal">Test/Quiz</a>
								<a class="dropdown-item"
									href="${pageContext.request.contextPath}/viewDailyAttendanceByStudent">Daily
									Attendance</a>
							</div></li> --%>
							
						<!-- Student ICA Menus -->
		<%-- 				  <li id="navIca" class="nav-item dropdown" data-toggle="tooltip"
							data-placement="bottom" title="ICA"><a
							class="nav-link dropdown-toggle" href="#" role="button"
							data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
								<i class="fas fa-marker"></i>ICA
						</a>
							<div id="navDropdownSupport"
								class="dropdown-menu dropdown-menu-right"
								aria-labelledby="navbarDropdown">



								<a class="dropdown-item"
									href="<c:url value="showInternalMarks" />">Ica Marks</a>



							</div></li>  --%> 

					</sec:authorize>

<%-- 					<sec:authorize access="hasRole('ROLE_PARENT')">
						<li id="navDashboard" class="nav-item active"><a
							class="nav-link"
							href="${pageContext.request.contextPath}/homepage"><i
								class="fas fa-tachometer-alt"></i> Dashboard<span
								class="sr-only">(current)</span></a></li>
						<li id="navDashboard" class="nav-item"><a class="nav-link"
							href="${pageContext.request.contextPath}/reportFormForParents"><i
								class="fas fa-chart-line"></i> Report</a></li>
						<li id="navDashboard" class="nav-item"><a class="nav-link"
							href="${pageContext.request.contextPath}/gradeCenterFormForParents"><i
								class="fas fa-chart-bar"></i> Grade Center</a></li>
						<li id="navDashboard" class="nav-item"><a class="nav-link"
							href="${pageContext.request.contextPath}/viewDailyAttendanceByStudent"><i
								class="fas fa-file-signature"></i> Attendance</a></li>
					</sec:authorize>
 --%>
					<li class="nav-item dropdown"><a
						class="nav-link dropdown-toggle" href="#" role="button"
						data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
							<img
							src="${pageContext.request.contextPath}/savedImages/${userBean.username}.JPG"
							onerror="this.src='<c:url value="/resources/images/img-user.png" />'"
							class="user-ico" title="Name of the user" alt="Name of the user" />
							<span>${userBean.firstname} ${userBean.lastname} </span> <!--Truncate to 12 Char -->
					</a>
						<div class="dropdown-menu dropdown-menu-right"
							aria-labelledby="navbarDropdown">
							<a class="dropdown-item" href="#"><i
								class="fas fa-id-card-alt"></i> PGDFM ID: ${userBean.username}</a> <a
								class="dropdown-item" href="mailto:" ${userBean.email}><i
								class="fas fa-at"></i> ${userBean.email}</a> <a
								class="dropdown-item" href="#"><i class="fa fa-phone"></i><i
								class=""></i> ${userBean.mobile}</a>
							<sec:authorize access="hasRole('ROLE_FACULTY')">
								<a class="dropdown-item" href="#"><i class=""></i> Faculty
									Type: ${facultyType}</a>
							</sec:authorize>
						<%-- 	<a class="dropdown-item"
								href="${pageContext.request.contextPath}/profileDetails"><i
								class="fas fa-user"></i> Profile</a> <a class="dropdown-item"
								href="${pageContext.request.contextPath}/changePasswordForm"><i
								class="fas fa-key"></i> Change Password</a> --%>
							<sec:authorize access="hasRole('ROLE_ADMIN')">
								<a class="dropdown-item"
									href="${pageContext.request.contextPath}/changePasswordFormStudentByAdmin"><i
									class="fas fa-key"></i> Change Password For Student</a>
							</sec:authorize>

							<a class="dropdown-item"
								href="${pageContext.request.contextPath}/loggedout"><i
								class="fas fa-sign-out-alt"></i> Logout</a>


						</div></li>
				</ul>
				<!--     </ul>
            </div> -->
		</nav>
	</header>
</div>

<!-- top navigation -->