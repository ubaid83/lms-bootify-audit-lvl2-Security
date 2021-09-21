<%@page import="java.util.*"%>
<%@page import="com.spts.lms.beans.dashboard.DashBoard"%>
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
                    <a href="<c:url value="showMyCourseStudents" />" title="My Program">
                        <li><i class="fas fa-file-powerpoint f-22 text-danger"></i></li>
                    </a>
                    <a href="https://portal.svkm.ac.in/usermgmt" title="Change Program">
                        <li><i class="fas fa-exchange-alt f-22 text-success"></i></li>
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
                        <li><img src="<c:url value="/resources/images/icons/class-icon.png" />" title="Class Participation" class="w-50" /></li>
                    </a> --%>
                    <a href="<c:url value="viewLibraryAnnouncements" />" title="Libraries">
                        <li><img src="<c:url value="/resources/images/icons/library-icon.png" />" title="Libraries" class="w-50" /></li>
                    </a>
                    <a href="${pageContext.request.contextPath}/profileDetails" title="Profile">
                        <li><img src="<c:url value="/resources/images/icons/profile-icon.png" />" title="Profile" class="w-50" /></li>
                    </a>
                    
                     <a href="downloadPortalManuals" title="Download Admin Manuals">
                        <li><img src="<c:url value="/resources/images/icons/manual.png" />" title="Download Admin Manuals" class="w-50" /></li>
                    </a>
                </ul>
            </div>
        </div>	