<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>


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
											<form:input type="text" path="date"
												value="${currentDate}" readonly="true" />
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
												studying in  <form:input type="text" path="year" required="required" /><span style="color: red">*</span> class for the
												academic year
												<form:input type="text" path="acadYear"
													value="${student.acadYear}" readonly="true" />
												.
											</p>
											<br>
											<p>My personal details are as follows -</p>
											<br>
											<ol>
												<li>SAP ID. <form:input type="text" path="username"
														value="${student.username}" readonly="true" /> Roll No. <form:input
														type="text" path="rollNo" value="${student.rollNo}"
														readonly="true" /></li>
												<br>

												<li>Name in full : <form:input type="text"
														path="lastname" value="${student.lastname}"
														readonly="true" /> <form:input type="text"
														path="firstname" value="${student.firstname}"
														readonly="true" /><br>
														
												<li>DOB <span style="color: red">*</span>: <form:input type="date" path="dob" value=""
														required="required" /><br>
														
														
														<li>Monthly /Quarterly with Class <span style="color: red">*</span>: <form:select class="form-control"  style="width: fit-content;"
																path="type" 
																required="required">

																<form:option value="" disabled="true" selected="true">Select </form:option>
																<form:option value="Quarterly I">Quarterly I</form:option>
																<form:option value="Quarterly II">Quarterly II</form:option>
																<form:option value="Monthly - I">Monthly - I</form:option>
																<form:option value="Monthly - II">Monthly - II</form:option>
															</form:select><br>
														
														<li>Sex <span style="color: red">*</span>:  <form:radiobutton name="sex" id="Male"
																value="M" path="sex" required="required" />
															Male
															<form:radiobutton name="sex" id="Female"
																value="F" path="sex" /> Female
																
																<br>
																<li>From Station <span style="color: red">*</span>: <form:input type="text" path="fromStation" value=""
														required="required" /> To Station <span style="color: red">*</span>: <form:input type="text" path="toStation" value=""
														required="required" />
														
														<br>
														<li>Address <span style="color: red">*</span>: <form:textarea class="form-group" path="address" required="required"
														name="editor1" id="editor1"  /> 
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
										<div class="col-sm-4">
											<div class="form-group">
												<button id="cancel" name="cancel" formaction="viewServices"
													class="btn btn-success" formnovalidate="formnovalidate">Cancel</button>
												<!-- send service id from here -->
												<form:input path="serviceId" value="${serviceId}"
													type="hidden" />
												<button id="submit" name="submit"
													formaction="sendStudentRCData"
													class="btn btn-success">Submit</button>
											</div>
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
