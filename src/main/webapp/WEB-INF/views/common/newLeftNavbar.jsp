<%@page import="java.util.*"%>
<%@page import="com.spts.lms.beans.dashboard.DashBoard"%>
<%-- <jsp:include page="../common/DashboardHeader.jsp" /> --%>
<%-- <jsp:include page="../common/NewHeader1.jsp" /> --%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>



<div class="leftSideBar">
            <div class="leftSideBarBody position-fixed vh-100 scrollcust">
                <ul class="list-unstyled text-white">
                <sec:authorize access="hasRole('ROLE_STUDENT')">
                    <a href="${pageContext.request.contextPath}/homepage" title="Dashboard">
                        <li class="sideActive color-3"><img src="<c:url value="/resources/images/icons/dashboard-icon.png" />" title="Dashboard" class="w-50" /></li>
                    </a>
                    <a href="${pageContext.request.contextPath}/viewTestFinal" title="Test/Quiz">
                        <li><img src="<c:url value="/resources/images/icons/test-icon.png" />" title="Test" class="w-50" /></li>
                    </a>
                    <a href="${pageContext.request.contextPath}/viewAssignmentFinal" title="Assignments">
                        <li><img src="<c:url value="/resources/images/icons/assignment-icon.png" />" title="Assignments" class="w-50" /></li>
                    </a>
                    <a href="${pageContext.request.contextPath}/myCourseForm" title="My Courses">
                        <li><img src="<c:url value="/resources/images/icons/courses-icon.png" />" title="Courses" class="w-50" /></li>
                    </a>
                    <a href="${pageContext.request.contextPath}/viewUserAnnouncementsSearchNew" title="Announcements">
                        <li><img src="<c:url value="/resources/images/icons/announcement-icon.png" />" title="Announcements" class="w-50" /></li>
                    </a>
                    <a href="${pageContext.request.contextPath}/viewDailyAttendanceByStudent" title="Attendance">
                        <li><img src="<c:url value="/resources/images/icons/attendance-icon.png" />" title="Attendance" class="w-50" /></li>
                    </a>
                    <a href="${pageContext.request.contextPath}/knowMyFaculty?courseId=" title="Teachers&#39; Profile">
                        <li><img src="<c:url value="/resources/images/icons/teacher-icon.png" />" title="Teachers' Profile" class="w-50" /></li>
                    </a>
                    <a href="${pageContext.request.contextPath}/profileDetails" title="Profile">
                        <li><img src="<c:url value="/resources/images/icons/profile-icon.png" />" title="Profile" class="w-50" /></li>
                    </a>
                    </sec:authorize>
                    <sec:authorize access="hasRole('ROLE_PARENT')">
                    <a href="${pageContext.request.contextPath}/homepage" title="Dashboard">
                        <li class="sideActive color-3"><img src="<c:url value="/resources/images/icons/dashboard-icon.png" />" title="Dashboard" class="w-50" /></li>
                    </a>
                    <a href="${pageContext.request.contextPath}/reportFormForParents" title="Report">
                        <li><img src="<c:url value="/resources/images/icons/report-icon.png" />" title="Report" class="w-50" /></li>
                    </a>
                    <a href="${pageContext.request.contextPath}/gradeCenterFormForParents" title="Grade Center">
                        <li><img src="<c:url value="/resources/images/icons/gradecenter-icon.png" />" title="Grade Center" class="w-50" /></li>
                    </a>
                    <a href="${pageContext.request.contextPath}/viewDailyAttendanceByStudent" title="Attendance">
                        <li><img src="<c:url value="/resources/images/icons/attendance-icon.png" />" title="Attendance" class="w-50" /></li>
                    </a>
                    <a href="${pageContext.request.contextPath}/profileDetails" title="Profile">
                        <li><img src="<c:url value="/resources/images/icons/profile-icon.png" />" title="Profile" class="w-50" /></li>
                    </a>
                    </sec:authorize>
                </ul>
            </div>
        </div>