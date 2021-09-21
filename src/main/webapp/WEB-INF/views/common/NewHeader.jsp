<%@page import="java.util.*"%>
<%@page import="com.spts.lms.beans.dashboard.DashBoard"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>


<!doctype html>
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=0, minimal-ui" />

<title>Svkm's NMIMS dcemed to be University</title>

<link rel="shortcut icon" href="favicon.ico">
<link rel="icon" type="image/png" href="favicon.png">


<!-- Bootstrap -->
<link href="<c:url value="/resources/css/bootstrap.min.css" />" rel="stylesheet">
<!-- Font Awesome -->
<link href="<c:url value="/resources/css/font-awesome.min.css" />" rel="stylesheet">
<!-- bootstrap-progressbar -->
<link href="<c:url value="/resources/css/bootstrap-progressbar-3.3.4.min.css" /> " rel="stylesheet">


<!-- Custom Toggle Style -->
<link href=" <c:url value="/resources/css/toggle.css" />" rel="stylesheet">

<!-- Custom menu Style -->
<link href=" <c:url value="/resources/css/menu.css" />" rel="stylesheet">


<!-- Custom Theme Style -->
<link href="<c:url value="/resources/css/custom.css" /> " rel="stylesheet">
<link href="<c:url value="/resources/css/responsive.css" />" rel="stylesheet">


</head>

<body class="nav-md">
    
    
    <div class="container body">
        <div class="main_container">
            
            
            <div class="col-md-3 left_col">
                <div class="left_col scroll-view">
                    <div class="navbar nav_title" style="border: 0;">
                        <a href="${pageContext.request.contextPath}/homepage"><img src="<c:url value="/resources/images/logo.gif" />" alt="" ></a>
                    </div>

                    <div class="clearfix"></div>

                    <!-- menu profile quick info -->
                    <div class="profile clearfix">
                        <div class="profile_pic"><img src="<c:url value="/resources/images/law.png" />" alt="" ></div>
                        <div class="profile_info">
                            <h2>Business Law</h2>
                        </div>
                    </div>
                    <!-- /menu profile quick info -->

                    <!-- sidebar menu -->
                    <div id="sidebar-menu" class="main_menu_side hidden-print main_menu">
                        <div class="menu_section">
                            <ul class="nav side-menu">
                                <li><a href="overview.html"><i class="fa fa-comments"></i> Overview</a></li>
                                <li><a href="assignments.html"><i class="fa fa-newspaper-o"></i> Assignments</a></li>
                                <li><a href="test-quiz.html"><i class="fa fa-file-text"></i> Test/Quiz</a></li>
                                <li><a href="learning-resources.html"><i class="fa fa-folder-open"></i> Learning Resources</a></li>
                                <li><a href="discussion-forums.html"><i class="fa fa-folder"></i> Discussion Forums</a></li>
                                <li><a href="my-groups.html"><i class="fa fa-users"></i> My Groups</a></li>
                                <li><a href="grade-dashboard.html"><i class="fa fa-pie-chart"></i> Grade Dashboard</a></li>
                                <li><a href="feedback.html"><i class="fa fa-reply-all"></i> Feedback</a></li>
                                <li><a href="teacher-profile.html"><i class="fa fa-user"></i> Teacher Profile</a></li>
                            </ul>
                        </div>

                    </div>
                    <!-- /sidebar menu -->
                </div>
            </div>
            

            <!-- top navigation -->
            <div class="top_nav">
                <div class="nav_menu">
                    <nav>
                        
                        <div class="nav toggle">
                            <a id="menu_toggle"><i class="fa fa-bars"></i></a>
                        </div>

                        <ul class="nav navbar-nav navbar-right">
                            <li class="">
                                <a href="javascript:;" class="user-profile dropdown-toggle" data-toggle="dropdown" aria-expanded="false">
                                    <img src="<c:url value="/resources/images/login-img.jpg"/>" alt=""> John Doe
                                    <span class=" fa fa-angle-down"></span>
                                </a>
                                <ul class="dropdown-menu">
                                    <li><a href="#"><i class="fa fa-sign-out"></i> PGDFM ID: 77116000012</a></li>
                                    <li><a href="mailto:jagruti.mehta@live.com"><i class="fa fa-envelope"></i> jagruti.mehta@live.com</a></li>
                                    <li><a href=""><i class="fa fa-phone"></i> 9810417398</a></li>
                                    <li><a href=""><i class="fa fa-user"></i> Profile</a></li>
                                    <li><a href=""><i class="fa fa-sign-out"></i> Logout</a></li>
                                </ul>
                            </li>

                            <li role="presentation" class="dropdown">
                                <a href="javascript:;" class="dropdown-toggle info-number" data-toggle="dropdown" aria-expanded="false">
                                    <i class="fa fa-bell-o"></i>
                                    <span class="badge bg-red">6</span>
                                </a>
                                <ul id="menu1" class="dropdown-menu list-unstyled msg_list" role="menu">
                                    <li>
                                        <a>
                                            <span class="image"><img src="<c:url value="/resources/images/img.jpg" /> " alt="Profile Image" /></span>
                                            <span>
                                                <span>John Smith</span>
                                                <span class="time">3 mins ago</span>
                                            </span>
                                            <span class="message">
                                                Film festivals used to be do-or-die moments for movie makers. They were where...
                                            </span>
                                        </a>
                                    </li>
                                    <li>
                                        <a>
                                            <span class="image"><img src="<c:url value="/resources/images/img.jpg" /> " alt="Profile Image" /></span>
                                            <span>
                                                <span>John Smith</span>
                                                <span class="time">3 mins ago</span>
                                            </span>
                                            <span class="message">
                                                Film festivals used to be do-or-die moments for movie makers. They were where...
                                            </span>
                                        </a>
                                    </li>
                                    <li>
                                        <a>
                                            <span class="image"><img src="<c:url value="/resources/images/img.jpg" /> " alt="Profile Image" /></span>
                                            <span>
                                                <span>John Smith</span>
                                                <span class="time">3 mins ago</span>
                                            </span>
                                            <span class="message">
                                                Film festivals used to be do-or-die moments for movie makers. They were where...
                                            </span>
                                        </a>
                                    </li>
                                    <li>
                                        <a>
                                            <span class="image"><img src="<c:url value="/resources/images/img.jpg" /> " alt="Profile Image" /></span>
                                            <span>
                                                <span>John Smith</span>
                                                <span class="time">3 mins ago</span>
                                            </span>
                                            <span class="message">
                                                Film festivals used to be do-or-die moments for movie makers. They were where...
                                            </span>
                                        </a>
                                    </li>
                                    <li>
                                        <div class="text-center">
                                            <a>
                                                <strong>See All Alerts</strong>
                                                <i class="fa fa-angle-right"></i>
                                            </a>
                                        </div>
                                    </li>
                                </ul>
                            </li>
                            
                            <li class="top_right_menu">
                                <a href="javascript:;" class="user-profile dropdown-toggle" data-toggle="dropdown" aria-expanded="false"><i class="fa fa-question-circle"></i> Quick Links</a>
                                <ul class="dropdown-menu">
                                    <li><a href="quick-links.html">Text Name 1</a></li>
                                    <li><a href="quick-links.html">Text Name 2</a></li>
                                    <li><a href="quick-links.html">Text Name 3</a></li>
                                    <li><a href="quick-links.html">Text Name 4</a></li>
                                    <li><a href="quick-links.html">Text Name 5</a></li>
                                    <li><a href="quick-links.html">Text Name 6</a></li>
                                </ul>
                            </li>
                            <li class="top_right_menu">
                                <a href="javascript:;" class="user-profile dropdown-toggle" data-toggle="dropdown" aria-expanded="false"><i class="fa fa-support"></i> Support</a>
                                <ul class="dropdown-menu">
                                    <li><a href="support.html">Text Name 1</a></li>
                                    <li><a href="support.html">Text Name 2</a></li>
                                    <li><a href="support.html">Text Name 3</a></li>
                                    <li><a href="support.html">Text Name 4</a></li>
                                    <li><a href="support.html">Text Name 5</a></li>
                                    <li><a href="support.html">Text Name 6</a></li>
                                </ul>
                            </li>
                            <li class="top_right_menu">
                                <a href="javascript:;" class="user-profile dropdown-toggle" data-toggle="dropdown" aria-expanded="false"><i class="fa fa-calendar"></i> Calendar</a>
                                <ul class="dropdown-menu">
                                    <li><a href="calendar.html">Text Name 1</a></li>
                                    <li><a href="calendar.html">Text Name 2</a></li>
                                    <li><a href="calendar.html">Text Name 3</a></li>
                                    <li><a href="calendar.html">Text Name 4</a></li>
                                    <li><a href="calendar.html">Text Name 5</a></li>
                                    <li><a href="calendar.html">Text Name 6</a></li>
                                </ul>
                            </li>
                            <li class="top_right_menu">
                                <a href="javascript:;" class="user-profile dropdown-toggle on" data-toggle="dropdown" aria-expanded="false"><i class="fa fa-comment-o"></i> My Courses</a>
                                <ul class="dropdown-menu">
                               
                                    <li><a href="viewCourse?id=99">Engineering
											Physics</a></li>
                                    <li><a href="<c:url value="viewCourse?id=100" />">Basic
											Electrical Engineering</a></li>
                                    <li><a href="<c:url value="viewCourse?id=101" />">Engineering
											Drawing - I</a></li>
                                    <li><a href="<c:url value="viewCourse?id=102" />" >Engineering
											Machanics - I</a></li>
                                    <li><a href="<c:url value="viewCourse?id=103" /> ">Computer
											Programming - I</a></li>
                                    
                                    
                                </ul>
                            </li>
                            <li class="top_right_menu"><a href="<c:url value="homepage" />" ><i class="fa fa-line-chart"></i>Dashboard</a></li>
                        </ul>
                    </nav>
                </div>
            </div>
            <!-- top navigation -->
            