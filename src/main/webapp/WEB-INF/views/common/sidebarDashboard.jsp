  <%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
  
  
   <div class="col-md-3 left_col">
                <div class="left_col scroll-view">
                    <div class="navbar nav_title" style="border: 0;">
                        <a href="${pageContext.request.contextPath}/homepage"><img src="<c:url value="images/logo.gif" />" alt="" ></a>
                    </div>

                    <div class="clearfix"></div>

                    <!-- menu profile quick info -->
                    <div class="profile clearfix">
                        <div class="profile_pic"><img src="<c:url value="images/law.png" />" alt="" ></div>
                        <div class="profile_info">
                            <h2>Business Law</h2>
                        </div>
                    </div>
                    <!-- /menu profile quick info -->

                  <!--   <!-- sidebar menu -->
                    <div id="sidebar-menu" class="main_menu_side hidden-print main_menu">
                        <div class="menu_section">
                            <h3>General</h3>
                            <ul class="nav side-menu">
                                <li>
                                    <a><i class="fa fa-comments"></i> Overview</a>
                                    <ul class="nav child_menu">
                                        <li><a href="${pageContext.request.contextPath}/homepage">Dashboard</a></li>
                                        <li><a href="index2.html">Dashboard2</a></li>
                                        <li><a href="index3.html">Dashboard3</a></li>
                                    </ul>
                                </li>
                                <li>
                                    <a><i class="fa fa-newspaper-o"></i> Assignments</a>
                                    <ul class="nav child_menu">
                                        <li><a href="form.html">General Form</a></li>
                                        <li><a href="form_advanced.html">Advanced Components</a></li>
                                        <li><a href="form_validation.html">Form Validation</a></li>
                                        <li><a href="form_wizards.html">Form Wizard</a></li>
                                        <li><a href="form_upload.html">Form Upload</a></li>
                                        <li><a href="form_buttons.html">Form Buttons</a></li>
                                    </ul>
                                </li>
                                <li><a><i class="fa fa-file-text"></i> Test/Quiz</a></li>
                                <li><a><i class="fa fa-folder-open"></i> Learning Resources</a></li>
                                <li><a><i class="fa fa-folder"></i> Discussion Forums</a></li>
                                <li><a><i class="fa fa-users"></i> My Groups</a></li>
                                <li><a><i class="fa fa-pie-chart"></i> Grade Dashboard</a></li>
                                <li><a><i class="fa fa-reply-all"></i> Feedback</a></li>
                                <li><a><i class="fa fa-user"></i> Teacher Profile</a></li>
                            </ul>
                        </div>

                    </div>
                    /sidebar menu -->
                </div>
            </div>