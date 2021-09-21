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
<div class="topFixx">
    <div class="headTop container-fluid">
        <div class="container p-1">
            <div class="d-flex">
                <div class="mr-auto">
                    <small class="text-white pb-0 mb-0">Welcome to NMIMS School of Business Management</small>
                </div>
                <div>
                    <ul class="list-unstyled d-flex mb-0">
                        <li class="pl-2"><a href="#"><span class="fa-round"><i class="fab fa-facebook-f"></i></span></a></li>
                        <li class="pl-2"><a href="#"><span class="fa-round"><i class="fab fa-google-plus-g"></i></span></a></li>
                        <li class="pl-2"><a href="#"><span class="fa-round"><i class="fab fa-twitter"></i></span></a></li>
                        <li class="pl-2"><a href="#"><span class="fa-round"><i class="fab fa-pinterest-p"></i></span></a></li>
                        <li class="pl-2"><a href="#"><span class="fa-round"><i class="fab fa-instagram"></i></span></a></li>
                        <li class="nav-item dropdown pl-2 notif">
                            <a href="#" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                <span class="notifNew">&#33;</span>
                                <span class="fa-round"> <i class="fas fa-bell"></i> </span>
                            </a>
                            <div id="navDropdownNotifications" class="dropdown-menu dropdown-menu-right" aria-labelledby="navbarDropdownNotifications">
                                <a class="dropdown-item" href="#">Some important notification</a>
                                <a class="dropdown-item" href="#">Some important notification</a>
                                <a class="dropdown-item" href="#">Some important notification</a>
                                <div class="dropdown-divider"></div>
                                <a class="dropdown-item" href="#">View All</a>
                            </div>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
    <header class="container-fluid sticky-top">
        <nav class="navbar navbar-expand-lg navbar-light p-0">
            <i class="fas fa-bars fa-2x mr-3 leftnav-btn" data-toggle="modal" data-target="#leftnav"></i>
           <!--  <a class="navbar-brand" href="dashboard.html">
                <img src="../img/logo.png" class="logo" title="NMIMS logo" alt="NMIMS logo" />
            </a> -->
            <button class="navbar-toggler" type="button" data-toggle="modal" data-target="#rightnav">
                <i class="fas fa-bars"></i>
            </button>

            <div class="collapse navbar-collapse" id="navbarSupportedContent">
                <ul class="navbar-nav ml-auto">
                    <li id="navCalendar" class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                            <i class="fas fa-calendar-alt"></i> Calendar
                        </a>
                        <div id="navDropdownCalendar" class="dropdown-menu dropdown-menu-right" aria-labelledby="navbarDropdown">
                            <a class="dropdown-item" href="#">Current Events</a>
                            <a class="dropdown-item" href="#">Upcoming Events</a>
                            <a class="dropdown-item" href="#">Recent Events</a>
                        </div>
                    </li>
                    <li id="navMessage" class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                            <span id="navMsgAlert">3</span>
                            <i class="fas fa-envelope"></i> Message
                        </a>
                        <div id="navDropdownMessage" class="dropdown-menu dropdown-menu-right" aria-labelledby="navbarDropdown">
                            <a class="dropdown-item" href="#"><span>3</span> Unread Message</a>
                            <a class="dropdown-item" href="#">Inbox</a>
                            <a class="dropdown-item" href="#">Create Message</a>
                        </div>
                    </li>
                    <li id="navSupport" class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                            <i class="fas fa-life-ring"></i> Support
                        </a>
                        <div id="navDropdownSupport" class="dropdown-menu dropdown-menu-right" aria-labelledby="navbarDropdown">
                            <a class="dropdown-item" href="support.html">Support</a>
                            <a class="dropdown-item" href="support.html">Raise a ticket</a>
                            <a class="dropdown-item" href="support.html">Track your ticket status</a>
                            <a class="dropdown-item" href="faqs.html">FAQ</a>
                        </div>
                    </li>
                    <li id="navQuicklinks" class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                            <i class="fas fa-link"></i> Quicklinks
                        </a>
                        <div id="navDropdownQuicklinks" class="dropdown-menu dropdown-menu-right" aria-labelledby="navbarDropdown">
                            <a class="dropdown-item" href="#"><i class="fas fa-bullhorn mr-2"></i> Announcements</a>
                            <a class="dropdown-item" href="#"><i class="fas fa-book-open mr-2"></i> Library</a>
                            <a class="dropdown-item" href="#"><i class="fas fa-smile mr-2"></i> View Feedback</a>
                            <div class="dropdown-divider"></div>
                            <a class="dropdown-item" href="assignment.html">Assignment</a>
                            <a class="dropdown-item" href="test-quiz.html">Test/Quiz</a>
                            <a class="dropdown-item" href="#">Daily Attendance</a>
                            <a class="dropdown-item" href="#">Grade Weightage</a>
                            <a class="dropdown-item" href="#">Create Forum</a>
                            <a class="dropdown-item" href="#">Create Group</a>
                        </div>
                    </li>

                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                            <img src="../img/img-user.png" class="user-ico" title="Name of the user" alt="Name of the user" /> <span>${userBean.firstname}
						${userBean.lastname} </span>
                            <!--Truncate to 12 Char -->
                        </a>
                        <div class="dropdown-menu dropdown-menu-right" aria-labelledby="navbarDropdown">
                            <a class="dropdown-item" href="#">Student ID: XXXXXXXXXX</a>
                            <a class="dropdown-item" href="#"><i class="fas fa-at"></i> xxxxsharma@gmail.com</a>
                            <a class="dropdown-item" href="#"><i class="fas fa-phone"></i> 978XXXXXXX</a>
                            <a class="dropdown-item" href="student-profile.html"><i class="fas fa-user"></i> My Profile</a>
                            <a class="dropdown-item" href="#"><i class="fas fa-key"></i> Change Password</a>
                            <a class="dropdown-item" href="#"><i class="fas fa-sign-out-alt"></i> Logout</a>
                        </div>
                    </li>

                </ul>
            </div>
        </nav>
    </header>
    </div>
<%-- <div class="top_nav">
	<div class="nav_menu">
		<nav>

			<div class="navbar nav_title" style="border: 0;">
				<c:if test="${instiFlag eq 'nm'}">
					<a href="<c:url value="homepage" />"><img
						src="<c:url value="/resources/images/logo.gif" />" alt=""></a>
				</c:if>

				<c:if test="${instiFlag eq 'sv'}">
					<a href="<c:url value="homepage" />"><img
						src="<c:url value="/resources/images/svkmlogo.png" />" alt=""></a>

				</c:if>
			</div>

			<div class="nav toggle">
				<a id="menu_toggle"><i class="fa fa-bars"></i></a>
			</div>

			<ul class="nav navbar-nav navbar-right">
				<li class=""><a href="#"
					class="user-profile iffyTip wd150 dropdown-toggle"
					data-toggle="dropdown" data-placement="bottom"
					title="${userBean.firstname} ${userBean.lastname} "
					aria-expanded="false"> <img
						src="${pageContext.request.contextPath}/savedImages/${userBean.username}.JPG"
						alt="No image"
						onerror="this.src='<c:url value="/resources/images/download.png" />'">${userBean.firstname}
						${userBean.lastname} <span class=" fa fa-angle-down"></span>
				</a>
					<ul class="dropdown-menu">
						<li><a href="#"><i class="fa fa-sign-out"></i> PGDFM ID:
								${userBean.username}</a></li>
						<li><a href="mailto:" ${userBean.email}><i
								class="fa fa-envelope"></i>${userBean.email}</a></li>
						<li><a href="#"><i class="fa fa-phone"></i>
								${userBean.mobile}</a></li>
						<sec:authorize access="hasRole('ROLE_FACULTY')">
							<li><a href="#">Faculty Type: ${facultyType}</a></li>
						</sec:authorize>
						<li><a
							href="${pageContext.request.contextPath}/profileDetails"><i
								class="fa fa-user"></i> Profile</a></li>
						<li><a
							href="${pageContext.request.contextPath}/changePasswordForm"><i
								class="fa fa-key"></i> Change Password</a></li>
						<sec:authorize access="hasRole('ROLE_ADMIN')">
							<li><a
								href="${pageContext.request.contextPath}/changePasswordFormStudentByAdmin"><i
									class="fa fa-key"></i> Change Password For Student</a></li>
						</sec:authorize>
						<li><a href="${pageContext.request.contextPath}/loggedout"><i
								class="fa fa-sign-out"></i> Logout</a></li>
					</ul></li>

				<li role="presentation" class="dropdown menu_scrollbar"><a
					class="dropdown-toggle info-number" data-toggle="dropdown"
					aria-expanded="false"> <i class="fa fa-bell-o"></i> <span
						class="badge bg-red">${announcementForTopMenu.size()}</span>
				</a>
					<ul id="menu1" class="dropdown-menu list-unstyled msg_list"
						role="menu">
						<c:forEach items="${announcementForTopMenu}" var="announcement">
							<li><a> <span>${announcement.subject}</span>  <span class="time">${announcement.createdDate}</span>
									<span class="message"> ${announcement.description} </span>
							</a></li>



						</c:forEach>


						<li>
							<div class="text-center">
								<a href="<c:url value="/viewUserAnnouncements"/>"> <strong>See
										All Alerts</strong> <i class="fa fa-angle-right"></i>
								</a>
							</div>
						</li>
					</ul></li>
				<sec:authorize
					access="hasAnyRole('ROLE_LIBRARIAN','ROLE_EXAM','ROLE_COUNSELOR','ROLE_SUPPORT_ADMIN')">
					<li class="dropdown"><a class="page-scroll toptooltip "
						href="https://portal.svkm.ac.in/usermgmt"> <i
							class="fa fa-exchange" aria-hidden="true"></i>
							<div class="toptooltiptext">Change Program</div> <span
							class="topnav_rstext">Change Program</span>
					</a></li>
					<li class="dropdown"><a
						href="${pageContext.request.contextPath}/homepage"
						style="font-size: 20; font-family: serif;">${libraryName } </a></li>








				</sec:authorize>




				<sec:authorize
					access="hasAnyRole('ROLE_FACULTY','ROLE_DEAN','ROLE_HOD')">
					<jsp:include page="facultyTopHeader.jsp">
						<jsp:param value="<%=activeMenu%>" name="activeMenu" />
					</jsp:include>

				</sec:authorize>

				<sec:authorize access="hasAnyRole('ROLE_ADMIN','ROLE_CORD')">
					<jsp:include page="adminTopHeader.jsp" />
				</sec:authorize>

				<sec:authorize access="hasRole('ROLE_STUDENT')">
					<jsp:include page="studentTopHeader.jsp">
						<jsp:param value="<%=activeMenu%>" name="activeMenu" />
					</jsp:include>

				</sec:authorize>

				<sec:authorize access="hasRole('ROLE_PARENT')">
					<jsp:include page="parentTopHeader.jsp">
						<jsp:param value="<%=activeMenu%>" name="activeMenu" />
					</jsp:include>

				</sec:authorize>


			</ul>
		</nav>
	</div>
</div> --%>
<!-- top navigation -->