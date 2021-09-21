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
                    <a href="${pageContext.request.contextPath}/homepage" title="Dashboard">
                        <li class="sideActive color-3"><img src="<c:url value="/resources/images/icons/dashboard-icon.png" />" title="Dashboard" class="w-50" /></li>
                    </a>
                    <%-- <a href="<c:url value="showMyCourseStudents" />" title="My Program">
                        <li><img src="<c:url value="/resources/images/icons/test-icon.png" />" title="My Program" class="w-50" /></li>
                    </a> --%>
                    <a href="${pageContext.request.contextPath}/facultyTestDashboard" title="Tests">
                        <li><img src="<c:url value="/resources/images/icons/test-icon.png" />" title="Tests" class="w-50" /></li>
                    </a>
                    
                    <!-- https://portal.svkm.ac.in/usermgmt -->
                    <a href="${pageContext.request.contextPath}/searchFacultyAssignment" title="Assignment">
                        <li><img src="<c:url value="/resources/images/icons/assignment-icon.png" />" title="Assignment" class="w-50" /></li>
                    </a>
                    <%-- <a href="my-courses.html" title="My Courses">
                        <li><img src="<c:url value="/resources/images/icons/courses-icon.png" />" title="Courses" class="w-50" /></li>
                    </a> --%>
                    
                     <%-- <a href="teacher-groups.html" title="Groups">
                        <li><img src="<c:url value="/resources/images/icons/group-icon.png" />" title="Groups" class="w-50" /></li>
                    </a> --%>
                    <%-- <a href="${pageContext.request.contextPath}/viewForum" title="Discussion Forum">
                        <li><img src="<c:url value="/resources/images/icons/forum-icon.png" />" title="Discussion Forum" class="w-50" /></li>
                    </a> --%>
                    <%-- <a href="${pageContext.request.contextPath}/gradeCenter?courseId=${courseId}" title="Grade Center">
                        <li><img src="<c:url value="/resources/images/icons/gradecenter-icon.png" />" title="Grade Center" class="w-50" /></li>
                    </a> --%>
                    <a href="downloadReportMyCourseStudentForm" title="Report">
                        <li><img src="<c:url value="/resources/images/icons/report-icon.png" />" title="Report" class="w-50" /></li>
                    </a>
<%--                     <a href="teacher-classparticipation.html" title="Class Participation">
                        <li><img src="<c:url value="/resources/images/icons/class-icon.png" />" tite="Class Participation" class="w-50" /></li>
                    </a> --%>
<%--                     <a href="${pageContext.request.contextPath}/getContentUnderAPathForFaculty?courseId=${courseId}" title="Learning Resources">
                        <li><img src="<c:url value="/resources/images/icons/learning-resources-icon.png" />" title="Learning Resources" class="w-50" /></li>
                    </a> --%>
                    
                    <a href="${pageContext.request.contextPath}/getContentUnderAPathForFacultyForModule" title="Learning Resources">
                        <li><img src="<c:url value="/resources/images/icons/learning-resources-icon.png" />" title="Learning Resources" class="w-50" /></li>
                    </a>
                    <a href="${pageContext.request.contextPath}/profileDetails" title="Profile">
                        <li><img src="<c:url value="/resources/images/icons/profile-icon.png" />" title="Profile" class="w-50" /></li>
                    </a>
                </ul>
            </div>
        </div>