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

<div class="top_nav">
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
							<li><a> <span>${announcement.subject}</span> <%--  <span class="time">${announcement.createdDate}</span> --%>
									<span class="message"> ${announcement.description} </span>
							</a></li>



						</c:forEach>
						
						<li><a class="dropdown-item" href="<c:url value="addTimeTableForm" />">Create
									Announcement For TimeTable </a></li>
									
						<c:url value="searchAnnouncementForLibrarian" var="addTimeTableFormUrl">
							<c:param name="announcementType">TIMETABLE</c:param>
							<c:param name="announcementSubType">EXAM</c:param>
						</c:url>
						<li><a class="dropdown-item" href="${ addTimeTableFormUrl }">View/Search
									Announcement For TimeTable </a></li>

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
</div>
<!-- top navigation -->