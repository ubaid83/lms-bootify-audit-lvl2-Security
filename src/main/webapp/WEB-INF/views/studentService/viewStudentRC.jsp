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
.fontSize {
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
							<br> <br> <a
								href="${pageContext.request.contextPath}/homepage">Dashboard</a>
							<i class="fa fa-angle-right"></i> Student Bonafide
						</div>

						<jsp:include page="../common/alert.jsp" />

						<div class="row">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">
									<form:form action="sendStudentRCData"
										modelAttribute="railwayForm">
										<h4 align="center">APPLICATION FOR RAILWAY CONCESSION
											CERTIFICATE</h4>
										<p class="fontSize" align="right">
											Date:
											<fmt:formatDate value="${student.date}" pattern="dd/MM/yyyy" />
										</p>
										<p class="fontSize">
											To,<br> The Dean,<br>
											<!-- School of Business Management<br> -->
											SVKM's NMIMS University<br> Mumbai
										</p>
										<br>
										<br>
										<p class="fontSize" style="padding-left: 170px">Sub:
											Regarding Railway Concession Certificate.</p>
										<br>
										<br>
										<div class="fontSize">
											<p>
												Sir / Madam,<br> I am student of your Institute and
												studying in  <c:out value="${student.year}"/> class <%-- for the
												academic year
												<form:input type="text" path="acadYear"
													value="${student.acadYear}" readonly="true" /> --%>
												.
											</p>
											<br>
											<p>My personal details are as follows -</p>
											<br>
											<ol>
												<li>SAP ID. <c:out value="${student.username}"/> <%-- Roll No. <form:input
														type="text" path="rollNo" value="${student.rollNo}"
														readonly="true" /> --%></li>
												<br>

												<li>Name in full : <c:out value="${student.lastname}"/> <c:out value="${student.firstname}"/><br>
														
												<li>DOB : <c:out value="${student.dob}"/><br>
														
														
														<li>Monthly /Quarterly with Class : <c:out value="${student.type}"/><br>
														
														<li>Sex :  <c:out value="${student.sex}"/>
																
																<br>
																<li>From Station : <c:out value="${student.fromStation}"/>
																 To Station : <c:out value="${student.toStation}"/>
														
														<br>
														<li>Address : <c:out value="${student.address}"/> 
<br>
													<p>Please issue me a railway concession certificate as
														early as possible. Thanking You,</p>
											</ol>
										</div>
										<br>
										<br>
										<div class="fontSize">
											<p>Yours sincerely,</p>
											<input type="text"
												value="${student.firstname} ${student.lastname}"
												readonly="readonly" />
										</div>
										<br>
										<div class="col-sm-5">
											<div class="form-group"></div>
										</div>
										<button id="cancel" class="btn btn-danger" type="button"
															formaction="homepage" formnovalidate="formnovalidate"
															onclick="history.go(-1);">Back</button>
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
