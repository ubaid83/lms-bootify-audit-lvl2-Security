
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

						<!--   <div class="right-arrow"><img class="toggle_to_do" src="" alt="" onclick="openNav2()"></div> -->

						<div class="right-arrow">
							<img class="toggle_to_do"
								src="<c:url value="/resources/images/dash-right.gif" />" alt=""
								onclick="openNav2()">
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



