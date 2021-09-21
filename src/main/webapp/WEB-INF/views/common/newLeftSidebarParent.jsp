<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
    
    <%@page import="java.util.*"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
    
    <div id="leftNav">
        <div class="modal left" id="leftnav" tabindex="-1" role="dialog" aria-labelledby="leftSidebarNavigation" aria-hidden="true">
            <div class="modal-dialog" role="document">
                <div class="modal-content scrollcust">
                    <div class="modal-header d-block border-bottom">
                        <h5 class="modal-title float-left" id="leftSidebarNavigation"><img src="<c:url value="/resources/images/logo.png"/>" style="width: 130px;" /></h5>

                        <button type="button" id="sidebarClose" class="close text-dark float-right" aria-label="Close">
                            <span aria-hidden="true"><i class="fas fa-times-circle"></i></span>
                        </button>
                    </div>
                    <div class="modal-body text-left">
                        <div class="col-12">
                            <div class="row">
                                <div class="col-3 pl-2 pb-3">
                                    <div class="userLeftNav-ico rounded-circle">
                                       <img src="<c:url value="/resources/images/img-user.png" />" class="logo" alt="User Image" />
                                       
                                    </div>
                                </div>
                                <div class="col-9">
                                    <p class="p-0 m-0">ID: <span>${userBean.username}</span></p>
                                    <p class="p-0 m-0"><small>${userBean.firstname}
						${userBean.lastname}</small></p>
                                </div>
                            </div>
                        </div>
                        <ul class="list-unstyled p-0 m-0" id="accordion2">
                            <a href="${pageContext.request.contextPath}/homepage">
                                <li class="sideActive"><img src="<c:url value="/resources/images/icons/dashboard-icon.png"/>"/> Dashboard</li>
                            </a>

                            

                           
                           <%--  <span>
                                <li class="collapsed cursor-pointer d-flex" data-toggle="collapse" data-target="#collapseCourse" aria-expanded="false" aria-controls="collapseCourse">
                                    <span class="mr-auto"><img src="<c:url value="/resources/images/icons/courses-icon.png" />"/> My Courses</span>
                                    <span class="badge badge-pill bg-secondary pt-1 text-white">03</span>
                                    <i class="fas fa-caret-right ml-3 pt-1"></i>
                                </li>

                                <ul id="collapseCourse" class="collapse list-unstyled" data-parent="#accordion2" aria-labelledby="Courses">
                                    <a href="#">
                                        <li class="pl-5">Some Example</li>
                                    </a>
                                    <a href="#">
                                        <li class="pl-5">Some Example</li>
                                    </a>
                                    <a href="#">
                                        <li class="pl-5">Some Example</li>
                                    </a>
                                    <a href="#">
                                        <li class="pl-5">Some Example</li>
                                    </a>
                                    <a href="my-courses.html">
                                        <li class="pl-5">View All</li>
                                    </a>
                                </ul>
                            </span> --%>
                           <%--  <a href="announcements.html">
                                <li><img src="<c:url value="/resources/images/icons/announcement-icon.png" />"/> Announcements</li>
                            </a> --%>
                           

                           
                            <a href="${pageContext.request.contextPath}/viewDailyAttendanceByStudent">
                                <li><img src="<c:url value="/resources/images/icons/attendance-icon.png" />"/> Attendance</li>
                            </a>
                            <a href="${pageContext.request.contextPath}/reportFormForParents">
                                <li><img src="<c:url value="/resources/images/icons/report-icon.png" />"/> My Report</li>
                            </a>
                            <a href="${pageContext.request.contextPath}/gradeCenterFormForParents">
                                <li><img src="<c:url value="/resources/images/icons/gradecenter-icon.png" />"/> Grade Center</li>
                            </a>
                           
                            <span>
                                <li class="collapsed cursor-pointer d-flex" data-toggle="collapse" data-target="#collapseSetting" aria-expanded="false" aria-controls="collapseSettings">
                                    <span class="mr-auto"><img src="<c:url value="/resources/images/icons/profile-icon.png" />"/> Setting</span>
                                    <span class="badge badge-pill bg-secondary pt-1 text-white">&#33;</span>
                                    <i class="fas fa-caret-right ml-3 pt-1"></i>
                                </li>

                                <ul id="collapseSetting" class="collapse list-unstyled" data-parent="#accordion2" aria-labelledby="settings">
                                    <a href="${pageContext.request.contextPath}/profileDetails">
                                        <li class="pl-5">My Profile</li>
                                    </a>
                                    
									<a href="${pageContext.request.contextPath}/loggedout">
                                        <li class="pl-5">Logout</li>
                                    </a>
										
                                </ul>
                            </span>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </div>