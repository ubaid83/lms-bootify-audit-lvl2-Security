
<%-- <jsp:include page="../common/header.jsp" /> --%>

<%@page import="com.spts.lms.beans.forum.Forum"%>
<%@page import="com.spts.lms.beans.calender.Calender"%>
<%@page import="java.util.List"%>
<%@ page import="com.spts.lms.web.utils.Utils"%>
<%-- <jsp:include page="../common/NewHeader1.jsp" /> --%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>




<!doctype html>
<html lang="en">

<jsp:include page="../../common/css.jsp" />

<script type="text/javascript">
	var headerCourseId = null;
	function theFunction(obj) {
		return true;
	}
</script>

<body class="nav-md footer_fixed">


	<div class="container body">
	<div class="main_container">

     
     <jsp:include page="../../common/leftSidebar.jsp" />
     <jsp:include page="../../common/topHeader.jsp" />
      
      
      
<!-- page content -->
<div class="right_col" role="main">

	<div class="dashboard_container">

		<div class="dashboard_height" id="main">
		
		 <div class="right-arrow"><img class="toggle_to_do" src="<c:url value="/resources/images/dash-right.gif" />" alt="" onclick="openNav2()"></div>
		

			<div class="dashboard_container_spacing">
				<div class="breadcrumb">
			<c:out value="${Program_Name}" />
						
						<sec:authorize access="hasRole('ROLE_STUDENT')">
						
						<i
						class="fa fa-angle-right"></i>
											
											<c:out value="${AcadSession}" />
											
											</sec:authorize>
						<br><br>
				
					<a href="${pageContext.request.contextPath}/viewCourse?id=${courseId}">My Courses</a> <i
						class="fa fa-angle-right"></i><c:out value="${courseRecord.courseName}" />
				</div>
			    <%@include file="mycourseAssignments.jsp" %>
				<%@include file="mycourseForumEvent.jsp" %>
				<%@include file="facultyLearningResource.jsp" %>
			</div>
		</div>
		
	</div>
</div>
<!-- /page content -->


<jsp:include page="../../common/footer.jsp" />

   </div>
  </div>
</body>
</html>

