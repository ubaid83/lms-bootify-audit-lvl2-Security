<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%@page import="java.util.*"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

   <div id="rightNav">
        <div class="modal right" id="rightnav" tabindex="-1" role="dialog" aria-labelledby="rightSidebarNavigation" aria-hidden="true">
            <div class="modal-dialog" role="document">
                <div class="modal-content scrollcust">
                    <div class="modal-header d-block border-bottom">
                        <h5 class="modal-title float-left" id="rightSidebarNavigation"><img src="<c:url value="/resources/images/logo-sidebar.png" />" style="width: 100px;" /></h5>

                        <button type="button" id="sidebarClose" class="close text-dark float-right" aria-label="Close">
                            <span aria-hidden="true"><i class="fas fa-times-circle"></i></span>
                        </button>
                    </div>
                    <div class="modal-body text-left">
                        <ul class="list-unstyled p-0 m-0" id="accordion3">
                            <span>
                                <li class="collapsed cursor-pointer d-flex" data-toggle="collapse" data-target="#collapseCalendar" aria-expanded="false" aria-controls="collapseCalendar">
                                    <i class="fas fa-caret-right ml-3 pt-1"></i>
                                    <span class="badge badge-pill bg-danger pt-1 text-white"></span>
                                    <span class="mr-auto">Calendar <i class="fas fa-calendar-alt"></i></span>
                                </li>
                                <ul id="collapseCalendar" class="collapse list-unstyled pr-0" data-parent="#accordion3" aria-labelledby="Calendar">
                                    <a href="#">
                                        <li class="pl-5">Current Event</li>
                                    </a>
                                    <a href="#">
                                        <li class="pl-5">Upcoming Event</li>
                                    </a>
                                    <a href="#">
                                        <li class="pl-5">Recent Event</li>
                                    </a>
                                </ul>
                            </span>
                                                            <a href="#">
                                    <li>Library <i class="fas fa-book-open"></i></li>
                                </a>
                            <span>
                                <li class="collapsed cursor-pointer d-flex" data-toggle="collapse" data-target="#collapseMessage" aria-expanded="false" aria-controls="collapseMessage">
                                    <i class="fas fa-caret-right ml-3 pt-1"></i>
                                    <span class="badge badge-pill bg-danger pt-1 text-white">3 Unread</span>
                                    <span class="mr-auto">Message <i class="fas fa-envelope"></i></span>
                                </li>

                                <ul id="collapseMessage" class="collapse list-unstyled pr-0" data-parent="#accordion3" aria-labelledby="Message">
                                    <a href="#">
                                        <li class="pl-5">Inbox</li>
                                    </a>
                                    <a href="#">
                                        <li class="pl-5">Create Message</li>
                                    </a>
                                </ul>
                            </span>

                            <span>
                                <li class="collapsed cursor-pointer d-flex" data-toggle="collapse" data-target="#collapseSupport" aria-expanded="false" aria-controls="collapseSupport">
                                    <i class="fas fa-caret-right ml-3 pt-1"></i>
                                    <span class="badge badge-pill bg-danger pt-1 text-white"></span>
                                    <span class="mr-auto">Support <i class="fas fa-life-ring"></i></span>
                                </li>

                                <ul id="collapseSupport" class="collapse list-unstyled pr-0" data-parent="#accordion3" aria-labelledby="Support">
                                    <a href="#">
                                        <li class="pl-5">Contact Support</li>
                                    </a>
                                    <a href="#">
                                        <li class="pl-5">Raise a Ticket</li>
                                    </a>
                                    <a href="#">
                                        <li class="pl-5">Track Your Ticket Status</li>
                                    </a>
                                    <a href="#">
                                        <li class="pl-5">FAQ</li>
                                    </a>
                                </ul>
                            </span>
                        </ul>
                    </div>
                    <div class="modal-footer rightNavAcc text-center p-2">
                        <div class="col-12 d-flex">
                            <i class="fas fa-sign-out-alt fa-2x"></i> <span class="mr-auto">Logout</span>
                        </div>
                    </div>

                </div>
            </div>
        </div>
    </div>