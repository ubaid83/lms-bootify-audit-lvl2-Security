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

<sec:authorize access="hasAnyRole('ROLE_STUDENT', 'ROLE_FACULTY')">
							
							
<jsp:include page="../common/newDashboardHeader.jsp" />
<style>
td.details-control {
	background:
		url('https://cdn.rawgit.com/DataTables/DataTables/6c7ada53ebc228ea9bc28b1b216e793b1825d188/examples/resources/details_open.png')
		no-repeat center center;
	cursor: pointer;
}

tr.shown td.details-control {
	background:
		url('https://cdn.rawgit.com/DataTables/DataTables/6c7ada53ebc228ea9bc28b1b216e793b1825d188/examples/resources/details_close.png')
		no-repeat center center;
}
</style>
<style>
.dot {
	background: cornflowerblue;
	border-radius: 0.8em;
	-moz-border-radius: 0.8em;
	-webkit-border-radius: 0.8em;
	color: #ffffff;
	display: inline-block;
	font-weight: bold;
	line-height: 1.6em;
	margin-right: 5px;
	text-align: center;
	width: 1.6em;
}
</style>
<div class="d-flex dataTableBottom" id="facultyAssignmentPage">
	<jsp:include page="../common/newLeftNavBarFaculty.jsp" />
	<jsp:include page="../common/newLeftSidebarFaculty.jsp" />
	<jsp:include page="../common/rightSidebarFaculty.jsp" />

	<!-- DASHBOARD BODY STARTS HERE -->

	<div class="container-fluid m-0 p-0 dashboardWraper">

		<jsp:include page="../common/newTopHeaderFaculty.jsp" />

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
							<li class="breadcrumb-item active" aria-current="page">
								Portal Feedback - Schools List</li>
						</ol>
					</nav>

					<jsp:include page="../common/alert.jsp" />


					<!-- Input Form Panel -->
					<div class="card bg-white border">
						<div class="card-body">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">

									<div class="x_title">
										<h2>Portal Feedback - Schools List</h2>
										<ul class="nav navbar-right panel_toolbox">
											<li><a class="collapse-link"><i
													class="fa fa-chevron-up"></i></a></li>
										</ul>
										<div class="clearfix"></div>
									</div>

									<div class="x_content">
										<form:form action="viewPortalFeedbacks" method="post"
											modelAttribute="portalFeedback">

											<div class="table-responsive">
												<!-- <button id="btn-show-all-children" type="button">Expand
													All</button>
												<button id="btn-hide-all-children" type="button">Collapse
													All</button> -->
												<table class="table  table-hover" id="example">
													<thead>
														<tr>
															<!-- <th></th> -->
															<th>School Name</th>
															<th>Download Report</th>
															<!-- <th>Feedback Count</th> -->


														</tr>
													</thead>
													<tbody>

														<c:forEach var="school" items="${listOfSchools}"
															varStatus="status">
															<c:if test="${school.feedbackCount > 0}">
																<tr>
																	<%-- <td class="details-control" id="${school.feedbackList }"></td> --%>


																	<td><a target="_blank"
																		href="viewPortalFeedbacks?schoolObjId=${school.schoolObjId }">
																			<c:out
																				value="${school.collegeName} | ${school.dbName } | " /><span
																			class="dot">${school.feedbackCount}</span>
																	</a></td>
																	<%-- <td><c:out value="${school.feedbackCount}" /></td> --%>
																	<td><a
																		href="downloadPortalFeedbackReport?schoolObjId=${school.schoolObjId }">Download</</a>

																	</td>

																</tr>
															</c:if>
														</c:forEach>

													</tbody>

												</table>
												<div class="row">

													<div class="col-sm-8 column">
														<div class="form-group">
															<button id="cancel" class="btn btn-danger" type="button"
																formaction="homepage" formnovalidate="formnovalidate"
																onclick="history.go(-1);">Back</button>
														</div>
													</div>
												</div>
											</div>
										</form:form>
									</div>
								</div>
							</div>
						</div>
					</div>

					<!-- Results Panel -->




					<%-- 	<jsp:include page="../common/paginate.jsp">
							<jsp:param name="baseUrl" value="searchFacultyGroups" />
						</jsp:include> --%>

					<!-- /page content: END -->

				</div>

				<!-- SIDEBAR START -->
				<jsp:include page="../common/newSidebar.jsp" />
				<!-- SIDEBAR END -->
				<jsp:include page="../common/footer.jsp" />
				</sec:authorize>			
				
				<sec:authorize access="hasRole('ROLE_SUPPORT_ADMIN')">
								
<html lang="en">
<jsp:include page="../common/css.jsp" />
<style>
td.details-control {
	background:
		url('https://cdn.rawgit.com/DataTables/DataTables/6c7ada53ebc228ea9bc28b1b216e793b1825d188/examples/resources/details_open.png')
		no-repeat center center;
	cursor: pointer;
}

tr.shown td.details-control {
	background:
		url('https://cdn.rawgit.com/DataTables/DataTables/6c7ada53ebc228ea9bc28b1b216e793b1825d188/examples/resources/details_close.png')
		no-repeat center center;
}
</style>
<style>
.dot {
	background: cornflowerblue;
	border-radius: 0.8em;
	-moz-border-radius: 0.8em;
	-webkit-border-radius: 0.8em;
	color: #ffffff;
	display: inline-block;
	font-weight: bold;
	line-height: 1.6em;
	margin-right: 5px;
	text-align: center;
	width: 1.6em;
}
</style>
<body class="nav-md footer_fixed">

	<div class="loader"></div>


	<div class="container body">
		<div class="main_container">

			<jsp:include page="../common/leftSidebar.jsp">
				<jsp:param name="courseId" value="${courseId}" />
			</jsp:include>
			<jsp:include page="../common/topHeaderLibrian.jsp">
				<jsp:param value="Forum" name="activeMenu" />
			</jsp:include>


			<!-- page content: START -->
			<div class="right_col" role="main">

				<div class="dashboard_container">

					<div class="dashboard_container_spacing">

						<div class="breadcrumb">

							<c:out value="${Program_Name}" />

							<sec:authorize access="hasRole('ROLE_STUDENT')">

								<i class="fa fa-angle-right"></i>

								<c:out value="${AcadSession}" />

							</sec:authorize>

							<br> <br> <a
								href="${pageContext.request.contextPath}/homepage">Dashboard</a>
							<i class="fa fa-angle-right"></i> Portal Feedback - Schools List
						</div>
						<jsp:include page="../common/alert.jsp" />

						<!-- Input Form Panel -->
						<div class="row">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">

									<div class="x_title">
										<h2>Portal Feedback - Schools List</h2>
										<ul class="nav navbar-right panel_toolbox">
											<li><a class="collapse-link"><i
													class="fa fa-chevron-up"></i></a></li>
										</ul>
										<div class="clearfix"></div>
									</div>

									<div class="x_content">
										<form:form action="viewPortalFeedbacks" method="post"
											modelAttribute="portalFeedback">

											<div class="table-responsive">
												<!-- <button id="btn-show-all-children" type="button">Expand
													All</button>
												<button id="btn-hide-all-children" type="button">Collapse
													All</button> -->
												<table class="table  table-hover" id="example">
													<thead>
														<tr>
															<!-- <th></th> -->
															<th>School Name</th>
															<th>Download Report</th>
															<!-- <th>Feedback Count</th> -->


														</tr>
													</thead>
													<tbody>

														<c:forEach var="school" items="${listOfSchools}"
															varStatus="status">
															<c:if test="${school.feedbackCount > 0}">
																<tr>
																	<%-- <td class="details-control" id="${school.feedbackList }"></td> --%>


																	<td><a target="_blank"
																		href="viewPortalFeedbacks?schoolObjId=${school.schoolObjId }">
																			<c:out
																				value="${school.collegeName} | ${school.dbName } | " /><span
																			class="dot">${school.feedbackCount}</span>
																	</a></td>
																	<%-- <td><c:out value="${school.feedbackCount}" /></td> --%>
																	<td><a
																		href="downloadPortalFeedbackReport?schoolObjId=${school.schoolObjId }">Download</</a>

																	</td>

																</tr>
															</c:if>
														</c:forEach>

													</tbody>

												</table>
												<div class="row">

													<div class="col-sm-8 column">
														<div class="form-group">
															<button id="cancel" class="btn btn-danger" type="button"
																formaction="homepage" formnovalidate="formnovalidate"
																onclick="history.go(-1);">Back</button>
														</div>
													</div>
												</div>
											</div>
										</form:form>
									</div>
								</div>
							</div>
						</div>
					</div>

					<!-- Results Panel -->


				</div>



			</div>

		</div>
		<!-- /page content: END -->


		<jsp:include page="../common/footerLibrarian.jsp" />


	</div>
	</div>





</body>
</html>

								
				</sec:authorize>
	