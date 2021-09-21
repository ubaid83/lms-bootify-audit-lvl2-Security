
<%@page import="java.util.*"%>
<%@page import="com.spts.lms.beans.dashboard.DashBoard"%>
<%-- <jsp:include page="../common/DashboardHeader.jsp" /> --%>
<%-- <jsp:include page="../common/NewHeader1.jsp" /> --%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<meta name="viewport" content="width=device-width, initial-scale=.5, maximum-scale=12.0, minimum-scale=.25, user-scalable=yes"/>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>


<%
	List<DashBoard> courseDetailList = (List<DashBoard>) session
			.getAttribute("courseDetailList");
	System.out.println("SIZE :"+courseDetailList.size());
	int count = 0;
%>

<!doctype html>
<html lang="en">


<jsp:include page="../common/css.jsp" />


<script type="text/javascript">
	var headerCourseId = null;
	function theFunction(obj) {

		return true;
	}
</script>

<body class="nav-md footer_fixed dashboard_left">
<div class="loader"></div>


	<div class="container body">
		<div class="main_container">

     
     <jsp:include page="../common/leftSidebar.jsp" />
     <jsp:include page="../common/topHeader.jsp" />

<!--  -->
<div class="right_col" role="main">
                
                <div class="dashboard_container">

<!-- <div class="dash-main"> -->
	<div class="dashboard_height" id="main">
	
     <!-- <div class="right-arrow"><img class="toggle_to_do" src="" alt="" onclick="openNav2()"></div> -->
     <div class="right-arrow"><img class="toggle_to_do" src="<c:url value="/resources/images/dash-right.gif" />" alt="" onclick="openNav2()"></div>

  		<div class="dashboard_contain_specing dash-main">
  		
  		<div class="alert alert-success alert-dismissible" role="alert">
								<button type="button" class="close" data-dismiss="alert"
									aria-label="Close">
									<span aria-hidden="true">&times;</span>
								</button>
								<h4>
									<strong>Welcome to HOD Login </strong>
								</h4>
							</div>
  
		<div class="container-fluid dashboardcon">
			<%
				for (DashBoard d : courseDetailList) {
					
					count++;
			%>
			<div class="col-md-4 course_widget_height">
				<div class="dashboardcon_title" id="courseDetail<%=count%>">
					<div>
					<c:url value="/viewCourse" var="viewCourseUrl">
							<c:param name="id" value="<%=String.valueOf(d.getCourse().getId())%>" />
					</c:url>
					<c:url value="assignmentList" var="viewAssignmentUrl">
							<c:param name="courseId" value="<%=String.valueOf(d.getCourse().getId())%>" />
					</c:url>
					<c:url value="viewUserAnnouncements" var="viewAnnouncementUrl">
							<c:param name="courseId" value="<%=String.valueOf(d.getCourse().getId())%>" />
					</c:url>
					<c:url value="viewForum" var="viewForumUrl">
							<c:param name="courseId" value="<%=String.valueOf(d.getCourse().getId())%>" />
					</c:url>
					
					
					<c:url value="testList" var="viewTestUrl">
							<c:param name="courseId" value="<%=String.valueOf(d.getCourse().getId())%>" />
					</c:url>
					<c:url value="getContentUnderAPathForFaculty" var="viewContentUrl">
							 <c:param name="courseId" value="<%=String.valueOf(d.getCourse().getId())%>" /> 
					</c:url>	
						
						
						<span><a href="${viewCourseUrl}" /><%=d.getCourse().getCourseName()%></a></span>
						<!-- <i><a href="#" class="hide_course_submenu"
							data-toggle="dropdown" aria-expanded="false"><span></span><span></span><span></span></a></i> -->
					</div>
					<div class="show_course_submenu" style="display: none;">
						<div class="arrow-left"></div>
						<ul>
							<li><a href="<c:url value="${viewAnnouncementUrl}" />"><i class="fa fa-bullhorn"></i>
									Announcements</a></li>
							<li><a href="<c:url value="${viewAssignmentUrl}" />"><i class="fa fa-newspaper-o"></i>
									Assignments</a></li>
									<li><a href="<c:url value="${viewTestUrl}" />"><i class="fa fa-file-text"></i> Test</a></li>
									<li><a href="<c:url value="${viewForumUrl}" />"><i class="fa fa-twitch "></i> Discussions</a></li>
									<li><a href="<c:url value="${viewContentUrl}" />"><i class="fa fa fa-folder-open"></i> Content</a></li>
									
						</ul>
					</div>
				</div>
				<%-- <ul>
				    <li>Announcements <span><%=d.getPendingAssigmentCount()%></span></li>
					<li>Assignments <span><%=d.getPendingAssigmentCount()%></span></li>
					<li>Tests <span><%=d.getPendingTestCount()%></span></li>
					
				</ul> --%>
			</div>
			<%
				}
			%>
</div>
		</div>

	</div>
		
		<%-- <%@include file="toDoDashboard.jsp"%> --%>
	      <jsp:include page="../common/studentToDo.jsp" /> 
		
		
<!-- </div> -->
</div>
</div>

<jsp:include page="../common/footer.jsp" />

   </div>
    </div>

</body>
</html>

<script>
$(document).ready(function(){
	var cars = ["bgcolor1", "bgcolor2", "bgcolor3","bgcolor4","bgcolor5","bgcolor6","bgcolor7","bgcolor8","bgcolor9"];
	var count = 0;
	$('[id^=courseDetail]').each(function(){
		if(count == cars.length-1){
			count = 0;
		}
		$(this).addClass(cars[count]);
		count++;
	})
	
	$('body').addClass("dashboard_left");
});

</script>

