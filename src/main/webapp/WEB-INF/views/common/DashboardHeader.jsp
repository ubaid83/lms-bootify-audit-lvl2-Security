
<%@page import="java.util.*"%>
<%@page import="com.spts.lms.beans.dashboard.DashBoard"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>



<%-- <!doctype html>
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=0, minimal-ui" />

<title>Svkm's NMIMS dcemed to be University</title>

<link rel="shortcut icon" href="favicon.ico">
<link rel="icon"
	type="${pageContext.request.contextPath}/resources/images/png"
	href="favicon.png">

<!-- Bootstrap -->

<link href="<c:url value="/resources/css/bootstrap.min.css" />"
	rel="stylesheet">
<!-- Font Awesome -->

<link href="<c:url value="/resources/css/font-awesome.min.css" />"
	rel="stylesheet">
<!-- bootstrap-progressbar -->

<link
	href="<c:url value="/resources/css/bootstrap-progressbar-3.3.4.min.css" />"
	rel="stylesheet">


<!-- Custom Toggle Style -->
<link href="<c:url value="/resources/css/toggle.css" />"
	rel="stylesheet">


<!-- Custom Theme Style -->
<link href="<c:url value="/resources/css/custom.css" />"
	rel="stylesheet">
<link href="<c:url value="/resources/css/responsive.css" />"
	rel="stylesheet">


</head>


<body>

	<%
		List<DashBoard> courseDetailList = (List<DashBoard>) request
				.getAttribute("courseDetailList");
		ArrayList<String> cssColourList = new ArrayList<String>(
				Arrays.asList("dashboardconhedbg1", "dashboardconhedbg2",
						"dashboardconhedbg3", "dashboardconhedbg4",
						"dashboardconhedbg5", "dashboardconhedbg6"));
		int count = 0;
	%>

	<div class="container-fluid maintop">
		<div class="row">
			<div class="col-md-2">
				<a href="dashboard.html"><img
					src="${pageContext.request.contextPath}/resources/images/logo.gif"
					alt=""></a>
			</div>
			<div class="col-md-10">
				<div class="top-rightmenu">
					<a href="dashboard.html" class="on"><i class="fa fa-line-chart"></i>Dashboard</a>
					<a href="my-courses.html"><i class="fa fa-comment-o"></i> My
						Courses</a> <a href="calendar.html"><i class="fa fa-calendar"></i>
						Calendar</a> <a href="support.html"><i class="fa fa-support"></i>
						Support</a> <a href="quick-links.html"><i
						class="fa fa-question-circle"></i> Quick Links</a> <a href="#"><i
						class="fa fa-bell-o"></i> <span class="topnot">${announcmentList.size()}</span></a>
					<a href="#" class="righttop"><img
						src="${pageContext.request.contextPath}/resources/images/login-img.jpg"
						alt=""> ${userBean.firstname} ${userBean.lastname}<i
						class="fa fa-chevron-down"></i></a>
					<div class="righttopshow" style="display: none;">
						<span><a href=""><i class="fa fa-phone"></i> PGDFM ID:
								${userBean.username}</a></span> <span><a
							href="mailto:${userBean.email}"><i class="fa fa-envelope"></i>
								${userBean.email}</a></span> <span><a href=""><i
								class="fa fa-phone"></i> ${userBean.mobile}</a></span> <span><a
							href=""><i class="fa fa-user"></i> Profile</a></span> <span><a
							href=""><i class="fa fa-sign-out"></i> Logout</a></span>
					</div>
				</div>
			</div>
		</div>
	</div>
 --%>

	//--------------------

	<!doctype html>
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=0, minimal-ui" />

<title>Svkm's NMIMS dcemed to be University</title>

<link rel="shortcut icon" href="favicon.ico">
<link rel="icon"
	type="${pageContext.request.contextPath}/resources/images/png"
	href="favicon.png">


<!-- Bootstrap -->
<link href="<c:url value="/resources/css/bootstrap.min.css" />" rel="stylesheet">
<!-- Font Awesome -->
<link href="<c:url value="/resources/css/font-awesome.min.css" />" rel="stylesheet">
<!-- bootstrap-progressbar -->
<link href= "<c:url value="/resources/css/bootstrap-progressbar-3.3.4.min.css" />" rel="stylesheet">

<!-- Custom Toggle Style -->
<link href="<c:url value="/resources/css/toggle.css" />" rel="stylesheet">

<!-- Custom menu Style -->
<link href="<c:url value="/resources/css/menu.css" />" rel="stylesheet">

<!-- Custom Theme Style -->
<link href="<c:url value="/resources/css/custom.css" />" rel="stylesheet">
<link href="<c:url value="/resources/css/responsive.css" />" rel="stylesheet">


</head>

<body>

	

	<div class="container-fluid maintop">
		<div class="row">
			<div class="col-md-2">
				
				<a href="${pageContext.request.contextPath}/homepage"><img
					src="${pageContext.request.contextPath}/resources/images/logo.gif"
					alt=""></a>
			</div>
			<div class="col-md-10">

				<!-- top navigation -->
				<div class="top_nav">
					<div class="nav_menu">
						<nav>
							<ul class="nav navbar-nav navbar-right">
								<li class=""><a href="javascript:;"
									class="user-profile dropdown-toggle" data-toggle="dropdown"
									aria-expanded="false"> <img src="${pageContext.request.contextPath}/resources/images/login-img.jpg"
										alt="">${userBean.firstname} ${userBean.lastname}<span class=" fa fa-angle-down"></span>
								</a>
								
									<ul class="dropdown-menu">
										<li><a href="#"><i class="fa fa-sign-out"></i> PGDFM
												ID: 
								${userBean.username}</a></li>
										<li><a href="mailto:${userBean.email}"><i
												class="fa fa-envelope"></i> ${userBean.email}</a></li>
										<li><a href=""><i class="fa fa-phone"></i> ${userBean.mobile}</a></li>
										<li><a href=""><i class="fa fa-user"></i> Profile</a></li>
										<li><a href="${pageContext.request.contextPath}/loggedout"><i class="fa fa-sign-out"></i> Logout</a></li>
									</ul></li>

								<li role="presentation" class="dropdown"><a
									href="javascript:;" class="dropdown-toggle info-number"
									data-toggle="dropdown" aria-expanded="false"> <i
										class="fa fa-bell-o"></i> <span class="badge bg-red">${announcmentList.size()}</span>
								</a>
									<ul id="menu1" class="dropdown-menu list-unstyled msg_list"
										role="menu">
										<li><a> <span class="image"><img
													src="${pageContext.request.contextPath}/resources/images/img.jpg" alt="Profile Image" /></span> <span>
													<span>John Smith</span> <span class="time">3 mins
														ago</span>
											</span> <span class="message"> Film festivals used to be
													do-or-die moments for movie makers. They were where... </span>
										</a></li>
										<li><a> <span class="image"><img
													src="images/img.jpg" alt="Profile Image" /></span> <span>
													<span>John Smith</span> <span class="time">3 mins
														ago</span>
											</span> <span class="message"> Film festivals used to be
													do-or-die moments for movie makers. They were where... </span>
										</a></li>
										<li><a> <span class="image"><img
													src="images/img.jpg" alt="Profile Image" /></span> <span>
													<span>John Smith</span> <span class="time">3 mins
														ago</span>
											</span> <span class="message"> Film festivals used to be
													do-or-die moments for movie makers. They were where... </span>
										</a></li>
										<li><a> <span class="image"><img
													src="images/img.jpg" alt="Profile Image" /></span> <span>
													<span>John Smith</span> <span class="time">3 mins
														ago</span>
											</span> <span class="message"> Film festivals used to be
													do-or-die moments for movie makers. They were where... </span>
										</a></li>
										<li>
											<div class="text-center">
												<a> <strong>See All Alerts</strong> <i
													class="fa fa-angle-right"></i>
												</a>
											</div>
										</li>
									</ul></li>

								<li class="top_right_menu"><a href="javascript:;"
									class="user-profile dropdown-toggle" data-toggle="dropdown"
									aria-expanded="false"><i class="fa fa-question-circle"></i>
										Quick Links</a>
									<ul class="dropdown-menu">
										<li><a href="quick-links.html">Text Name 1</a></li>
										<li><a href="quick-links.html">Text Name 2</a></li>
										<li><a href="quick-links.html">Text Name 3</a></li>
										<li><a href="quick-links.html">Text Name 4</a></li>
										<li><a href="quick-links.html">Text Name 5</a></li>
										<li><a href="quick-links.html">Text Name 6</a></li>
									</ul></li>
								<li class="top_right_menu"><a href="javascript:;"
									class="user-profile dropdown-toggle" data-toggle="dropdown"
									aria-expanded="false"><i class="fa fa-support"></i> Support</a>
									<ul class="dropdown-menu">
										<li><a href="support.html">Text Name 1</a></li>
										<li><a href="support.html">Text Name 2</a></li>
										<li><a href="support.html">Text Name 3</a></li>
										<li><a href="support.html">Text Name 4</a></li>
										<li><a href="support.html">Text Name 5</a></li>
										<li><a href="support.html">Text Name 6</a></li>
									</ul></li>
								<li class="top_right_menu"><a href="javascript:;"
									class="user-profile dropdown-toggle" data-toggle="dropdown"
									aria-expanded="false"><i class="fa fa-calendar"></i>
										Calendar</a>
									<ul class="dropdown-menu">
										<li><a href="calendar.html">Text Name 1</a></li>
										<li><a href="calendar.html">Text Name 2</a></li>
										<li><a href="calendar.html">Text Name 3</a></li>
										<li><a href="calendar.html">Text Name 4</a></li>
										<li><a href="calendar.html">Text Name 5</a></li>
										<li><a href="calendar.html">Text Name 6</a></li>
									</ul></li>
								<sec:authorize access="hasRole('ROLE_STUDENT')">	
								<li class="top_right_menu"><a href="javascript:;"
									class="user-profile dropdown-toggle" data-toggle="dropdown"
									aria-expanded="false"><i class="fa fa-comment-o"></i> My
										Courses</a>
									<ul class="dropdown-menu">
										<li><a href="<c:url value="viewCourse?id=99" />">Engineering
											Physics</a></li>
										<li><a href="viewCourse?id=100">Basic
											Electrical Engineering</a></li>
										<li><a href="viewCourse?id=101">Engineering
											Drawing - I</a></li>
										<li><a href="viewCourse?id=102">Engineering
											Machanics - I </a></li>
										<li><a href="viewCourse?id=103">Computer
											Programming - I</a></li>
										
									</ul></li>
								</sec:authorize>
								<li class="top_right_menu"><a href="<c:url value="homepage" />"
									class="on"><i class="fa fa-line-chart"></i>Dashboard</a></li>
							</ul>
						</nav>
					</div>
				</div>
				<!-- /top navigation -->

			</div>
		</div>
	</div>