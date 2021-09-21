<%@page import="com.spts.lms.web.utils.Utils"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@page import="java.util.*"%>



<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<jsp:include page="../common/newDashboardHeader.jsp" />
<head>
<style>
.osTable {
	overflow-x: scroll;
}

table.scroll {
	width: 100%;
	/* Optional */
	/* border-collapse: collapse; */
	border-spacing: 0;
	/*border: 2px solid black;*/
}

table.scroll tbody, table.scroll thead {
	display: block;
}

thead tr th {
	height: 30px;
	line-height: 30px;
	/*text-align: left;*/
}

table.scroll tbody {
	height: 500px;
	overflow-y: auto;
	overflow-x: hidden;
}

tbody {
	border-top: 2px solid black;
}

tbody td, thead th {
	width: 20%;
	/* Optional */
	border-right: 1px solid black;
}

tbody td:last-child, thead th:last-child {
	border-right: none;
}
</style>
</head>
<div class="d-flex dataTableBottom" id="facultyAssignmentPage">
	<sec:authorize access="hasRole('ROLE_FACULTY')">
		<jsp:include page="../common/newLeftNavBarFaculty.jsp" />
		<jsp:include page="../common/newLeftSidebarFaculty.jsp" />
		<jsp:include page="../common/rightSidebarFaculty.jsp" />
	</sec:authorize>
	<sec:authorize access="hasRole('ROLE_ADMIN')">
		<jsp:include page="../common/newAdminLeftNavBar.jsp" />
		<jsp:include page="../common/rightSidebarAdmin.jsp" />
	</sec:authorize>

	<!-- DASHBOARD BODY STARTS HERE -->

	<div class="container-fluid m-0 p-0 dashboardWraper">

		<sec:authorize access="hasRole('ROLE_FACULTY')">
		<jsp:include page="../common/newTopHeaderFaculty.jsp" />
		</sec:authorize>
		<sec:authorize access="hasRole('ROLE_ADMIN')">
		<jsp:include page="../common/newAdminTopHeader.jsp" />
		</sec:authorize>

		<div class="container mt-5">
			<div class="row">
				<!-- SEMESTER CONTENT -->
				<div
					class="col-lg-9 col-md-9 col-sm-12 dashboardBody ml-auto mr-auto">

					<!-- page content: START -->

					<nav aria-label="breadcrumb">
						<ol class="breadcrumb">
							<li class="breadcrumb-item"><a
								href="${pageContext.request.contextPath}/homepage">Dashboard</a></li>
							<li class="breadcrumb-item" aria-current="page"><c:out
									value="${Program_Name}" /></li>
							<sec:authorize access="hasRole('ROLE_STUDENT')">
								<c:out value="${AcadSession}" />
							</sec:authorize>
							<li class="breadcrumb-item active" aria-current="page">Evaluated Term End Exam Marks</li>
						</ol>
					</nav>

					<jsp:include page="../common/alert.jsp" />


					<!-- Input Form Panel -->
					<div class="card bg-white border">
						<div class="card-body">
							<h5 class="text-center border-bottom pb-2 text-uppercase">TEE marks For <c:out value="${moduleName}"> </c:out> </h5>

							<div class="row">

								<div class="col-md-4 col-sm-6 mt-3">
									<strong>Acad year:</strong> <span>${tee.acadYear}</span>
								</div>
								<div class="col-md-4 col-sm-6 mt-3">
									<strong>Semester:</strong> <span>${tee.acadSession}</span>
								</div>
								<div class="col-md-4 col-sm-6 mt-3">
									<strong>Program:</strong> <span>${programName}</span>
								</div>
								<div class="col-md-4 col-sm-6 mt-3">
									<strong>Module:</strong> <span>${moduleName}</span>
								</div>

								<%-- <div class="col-md-4 col-sm-6 mt-3">
									<strong>Start Date:</strong> <span>${tee.startDate}</span>
								</div>
								<div class="col-md-4 col-sm-6 mt-3">
									<strong>End Date:</strong> <span>${tee.endDate}</span>
								</div> --%>
								<div class="col-md-4 col-sm-6 mt-3">
									<strong>Total External Marks:</strong> <span>${tee.externalMarks}</span>
								</div>
								<div class="col-md-4 col-sm-6 mt-3">
									<strong>External Passing Marks:</strong> <span>${tee.externalPassMarks}</span>
								</div>


							</div>

						</div>
					</div>

					<!-- Result Panel -->



					<div class="card bg-white border">
						<div class="card-body">
							<div class="row">
								<div class="col-xs-12 col-sm-12">
									<div class="x_panel">
										<div class="x_title">
											<h2>TEE Marks</h2>
											<div class="clearfix"></div>
										</div>
										<div class="x_content">
											<div class="table-responsive testAssignTable">
												<table class="table table-hover" id="viewFeedbackTable">
													<thead>
														<tr>
															<th>SAPID</th>
															<th>Student Name</th>
															<th>Roll No</th>
															<th>Marks Obained</th>
															<th>Remarks</th>
															<!--<th>Status</th>-->
														</tr>
													</thead>
													<tbody>


														<c:forEach var="tee1" items="${studentsListForTee}"
															varStatus="status">
															<tr>
																<td>${tee1.username}</td>
																<td>${tee1.studentName}</td>
																<td>${tee1.rollNo}</td>
																<td>${tee1.teeTotalMarks}/${tee1.externalMarks}</td>
																<td>${tee1.remarks}</td>
																<!--<td>${ica.passFailStatus}</td>-->
															</tr>
														</c:forEach>
													</tbody>
												</table>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>




					<!-- Results Panel -->

					<!-- /page content: END -->

				</div>

				<!-- SIDEBAR START -->
				<sec:authorize access="hasRole('ROLE_FACULTY')">
				<!-- SIDEBAR START -->
				<jsp:include page="../common/newSidebar.jsp" />
				<!-- SIDEBAR END -->
				<jsp:include page="../common/footer.jsp" />
				</sec:authorize>
				<sec:authorize access="hasRole('ROLE_ADMIN')">
				<jsp:include page="../common/newAdminFooter.jsp" />
				</sec:authorize>



				<!-- <script type="text/javascript"
	src="https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.0/MathJax.js?config=TeX-AMS_HTML"></script> -->

				<script src="https://unpkg.com/sweetalert/dist/sweetalert.min.js"></script>