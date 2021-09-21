<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/fmt" prefix = "fmt" %>

<!doctype html>
<html lang="en">

<jsp:include page="../common/headerCss.jsp" />
<style>
.fontSize{
font-size: 15px;
padding-left: 40px;
}
</style>
<body class="nav-md ">


	<div class="container body">
		<div class="main_container">


			<jsp:include page="../common/leftSidebar.jsp" />
			<jsp:include page="../common/topHeader.jsp" />





			<!-- page content -->
			<div class="right_col" role="main">

				<div class="dashboard_container">



					<div class="dashboard_container_spacing">
						<div class="breadcrumb">

							<%-- <c:out value="${Program_Name}" /> --%>

							<sec:authorize access="hasRole('ROLE_STUDENT')">

								<!-- <i class="fa fa-angle-right"></i> -->

								<%-- <c:out value="${AcadSession}" /> --%>

							</sec:authorize>
							<br>
							<br> <a href="${pageContext.request.contextPath}/homepage">Dashboard</a>
							<i class="fa fa-angle-right"></i> Student Bonafide
						</div>

						<jsp:include page="../common/alert.jsp" />

						<div class="row">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">
								<form:form action="" modelAttribute="bf">
									<h4 align="center"><u>Bonafide Student Certificate</u></h4>
									<br><br>
							<div class="fontSize">
							<p>This is to certify that Mr. / Ms.<c:out value="${bf.firstname}"/>&nbsp;<c:out value="${bf.lastname}"/>
							 son / daughter of Mr. / Ms.<c:out value="${bf.fatherName}"/>&nbsp;<c:out value="${bf.lastname}"/>
							is a bonafide student of SVKM's NMIMS University, School of Business Management, Mumbai.</p>
							<br>
							<p style="padding-left: 40px">At present, he / she is studying in <c:out value="${bf.studyClass}"/> of 
							<c:out value="${bf.programName}"/> program for Academic Year 20___ - ___.</p>
							
							<br>
							<p style="padding-left: 40px">This certificate is issued against the request of student vide 
							his/her application dated <fmt:formatDate value="${bf.createdDate}" pattern="dd MMM yyyy" /> for <c:out value="${bf.reason}"/> purpose.</p>
							
							
							</div>
							
							</form:form>
								</div>
							</div>
						</div>

					</div>
					<%-- <jsp:include page="../common/paginate.jsp">
						<jsp:param name="baseUrl" value="viewFeedbackDetails" />
					</jsp:include> --%>
				</div>


				<%-- <jsp:include page="../common/studentToDo.jsp" /> --%>
			</div>


			<!-- /page content -->

			<jsp:include page="../common/DashboardFooter.jsp" />

		</div>
	</div>

</body>
</html>
