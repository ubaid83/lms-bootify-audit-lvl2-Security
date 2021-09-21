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

<div class="d-flex dataTableBottom" id="facultyAssignmentPage">
	<jsp:include page="../common/newLeftNavbar.jsp" />
	<jsp:include page="../common/newLeftSidebar.jsp" />
	<jsp:include page="../common/rightSidebar.jsp" />

	<!-- DASHBOARD BODY STARTS HERE -->

	<div class="container-fluid m-0 p-0 dashboardWraper">

		<jsp:include page="../common/newTopHeader.jsp" />

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

							<li class="breadcrumb-item active" aria-current="page">NC
								Marks</li>
						</ol>
					</nav>

					<jsp:include page="../common/alert.jsp" />


					<!-- Input Form Panel -->
					<div class="card bg-white border">
						<div class="card-body">
							<div class="row">
								<div class="col-xs-12 col-sm-12">
									<div class="x_panel">
										<div class="x_title">
											<h2>Non-Credit Grades</h2>
											<div class="clearfix"></div>
										</div>
										<div class="x_content">
											<div class="table-responsive testAssignTable">
												<table class="table table-hover" id="viewFeedbackTable">
													<thead>
														<tr>
															<th>Subject Name</th>
															<th>Year</th>
															<th>Semester</th>
															<th>Satisfactory/Non-Satisfactory</th>

														</tr>

														<div>
															<span class="legendS">S-</span> Satisfactory <span class="ham ml-3">N-</span> Non-satisfactory
														</div>

													</thead>
													<tbody>


														<c:forEach var="ns" items="${ncGrade}" varStatus="status">
															<tr>
																<td>${ns.moduleDescription}</td>
																<td>${ns.acadYear}</td>

																<td>${ns.acadSession}</td>
																<td>${ns.grade}</td>
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


				</div>

				<!-- SIDEBAR START -->
				<jsp:include page="../common/newSidebar.jsp" />
				<!-- SIDEBAR END -->
				<jsp:include page="../common/footer.jsp" />
				<script src="https://unpkg.com/sweetalert/dist/sweetalert.min.js"></script>