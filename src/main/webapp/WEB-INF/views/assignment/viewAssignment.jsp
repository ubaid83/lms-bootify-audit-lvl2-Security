<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

<jsp:include page="../common/newDashboardHeader.jsp" />

<sec:authorize access="hasRole('ROLE_ADMIN')">
	<div class="d-flex adminPage tabPage dataTableBottom" id="adminPage">
		<jsp:include page="../common/newAdminLeftNavBar.jsp" />
		<jsp:include page="../common/rightSidebarAdmin.jsp" />

		<!-- DASHBOARD BODY STARTS HERE -->

		<div class="container-fluid m-0 p-0 dashboardWraper">

			<jsp:include page="../common/newAdminTopHeader.jsp" />

			<!-- SEMESTER CONTENT -->
			<div class="col-lg-12 col-md-12 col-sm-12 dashboardBody p-5">


				<!-- page content: START -->

				<nav aria-label="breadcrumb">
					<ol class="breadcrumb">
						<li class="breadcrumb-item"><a
							href="${pageContext.request.contextPath}/homepage">Dashboard</a></li>
						<li class="breadcrumb-item" aria-current="page"><c:out
								value="${Program_Name}" /></li>
						<li class="breadcrumb-item active" aria-current="page">
							Search Assignments/Tests</li>
					</ol>
				</nav>
				<jsp:include page="../common/alert.jsp" />
				<!-- Input Form Panel -->
				<c:choose>
					<c:when test="${assignmentlist.size() > 0}">
						<div class="card bg-white border">
							<div class="card-body">



								<form:form action="viewThisAssignment" method="post"
									modelAttribute="assignment">

									<h5 class="text-center border-bottom pb-2">
										Assignments<font size="2px"> | ${assignmentlist.size()}
											Records Found &nbsp; </font>
									</h5>

									<div class="x_content">
										<div class="table-responsive testAssignTable">
											<table class="table table-striped table-hover">
												<thead>
													<tr>
														<th>Sr. No.</th>
														<th>Name</th>
														<th>UserName</th>
														<th>Course</th>

														<th>Start Date</th>
														<th>End Date</th>
														<th>Submission Date</th>
														<th>Assignment File</th>
														<th>Submitted File</th>
														<th>Attempts</th>
														<th>Submission Status</th>
														<th>Evaluation Status</th>
														<th>Score</th>
														<th>Remarks</th>



													</tr>
												</thead>
												<tbody>

													<c:forEach var="assignment" items="${assignmentlist}"
														varStatus="status">
														<tr>
															<td><c:out value="${status.count}" /></td>
															<td><c:out value="${assignment.assignmentName}" /></td>
															<td><c:out value="${assignment.username}" /></td>
															<td><c:out value="${assignment.courseName}" /></td>
															<%-- <td><c:out value="${assignment.createdBy}" /></td> --%>

															<td><c:out
																	value="${fn:replace(assignment.startDate,'T', ' ')}"></c:out></td>
															<td><c:out
																	value="${fn:replace(assignment.endDate, 
                                'T', ' ')}"></c:out></td>
															<td><c:out
																	value="${fn:replace(assignment.submissionDate, 
                                'T', ' ')}"></c:out></td>

															<td><c:if
																	test="${assignment.showFileDownload eq 'true'}">
																	<a href="downloadFile?id=${assignment.assignmentId}">Download</a>
																</c:if> <c:if test="${assignment.showFileDownload eq 'false'}">No File</c:if>
															</td>
															<td><c:if
																	test="${assignment.showStudentFileDownload eq 'true'}">
																	<a href="downloadFile?saId=${assignment.id }">Download
																		Answer File</a>
																</c:if> <c:if
																	test="${assignment.showStudentFileDownload eq 'false'}">No File</c:if></td>
															<td><c:out value="${assignment.attempts}" /></td>
															<td><c:out value="${assignment.submissionStatus}" /></td>
															<td><c:out value="${assignment.evaluationStatus}" /></td>
															<td><c:out value="${assignment.score}" /></td>
															<td><c:out value="${assignment.remarks}" /></td>

														</tr>
													</c:forEach>


												</tbody>
											</table>
											<div class="col-12">
											<button class="btn btn-danger"><a href="${pageContext.request.contextPath}/searchAssignmentTestForm">Back</a></button>
											
											</div>
										</div>
									</div>

								</form:form>
							</div>
						</div>
					</c:when>
					<c:otherwise>
						<div class="alert alert-danger" role="alert"><h5>No Data Found!</h5></div>
					</c:otherwise>

				</c:choose>

				<!-- Results Panel -->
				<!-- /page content: END -->
			</div>

			<!-- SIDEBAR END -->
			<jsp:include page="../common/newAdminFooter.jsp" />
</sec:authorize>

<sec:authorize access="hasAnyRole('ROLE_FACULTY', 'ROLE_FACULTY')">
<sec:authorize access="hasRole('ROLE_FACULTY')">
	<div class="d-flex dataTableBottom paddingFixFac"
		id="facultyAssignmentPage">
		<jsp:include page="../common/newLeftNavBarFaculty.jsp" />
		<jsp:include page="../common/newLeftSidebarFaculty.jsp" />
		<jsp:include page="../common/rightSidebarFaculty.jsp" />
</sec:authorize>
<sec:authorize access="hasRole('ROLE_STUDENT')">
	<div class="d-flex dataTableBottom paddingFixFac"
		id="facultyAssignmentPage">
		<jsp:include page="../common/newLeftNavbar.jsp" />
		<jsp:include page="../common/newLeftSidebar.jsp" />
		<jsp:include page="../common/rightSidebar.jsp" />
</sec:authorize>


<!-- DASHBOARD BODY STARTS HERE -->

<div class="container-fluid m-0 p-0 dashboardWraper">
	<sec:authorize access="hasRole('ROLE_STUDENT')">
		<jsp:include page="../common/newTopHeader.jsp" />
	</sec:authorize>

	<sec:authorize access="hasRole('ROLE_FACULTY')">
		<jsp:include page="../common/newTopHeaderFaculty.jsp" />
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
						<li class="breadcrumb-item active" aria-current="page">
							Search Assignments/Tests</li>
					</ol>
				</nav>

				<jsp:include page="../common/alert.jsp" />
				<!-- Input Form Panel -->
				<div class="card bg-white border">
					<div class="card-body">
						<div class="x_content">
							<form:form action="viewThisAssignment" method="post"
								modelAttribute="assignment">



								<c:choose>
									<c:when test="${assignmentlist.size() > 0}">
										<div class="row">
											<div class="col-xs-12 col-sm-12">

												<div class="x_title">
													<h5 class="text-center pb-2 border-bottom">
														Assignments <small> | ${assignmentlist.size()}
															Records Found &nbsp; </small>
													</h5>
												</div>
												<div class="x_content">
													<div class="table-responsive testAssignTable">
														<table class="table table-striped table-hover">
															<thead>
																<tr>
																	<th>Sr. No.</th>
																	<th>Name</th>
																	<th>UserName</th>
																	<th>Course</th>

																	<th>Start Date</th>
																	<th>End Date</th>
																	<th>Submission Date</th>
																	<th>Assignment File</th>
																	<th>Submitted File</th>
																	<th>Attempts</th>
																	<th>Submission Status</th>
																	<th>Evaluation Status</th>
																	<th>Score</th>
																	<th>Remarks</th>



																</tr>
															</thead>
															<tbody>

																<c:forEach var="assignment" items="${assignmentlist}"
																	varStatus="status">
																	<tr>
																		<td><c:out value="${status.count}" /></td>
																		<td><c:out value="${assignment.assignmentName}" /></td>
																		<td><c:out value="${assignment.username}" /></td>
																		<td><c:out value="${assignment.courseName}" /></td>
																		<%-- <td><c:out value="${assignment.createdBy}" /></td> --%>

																		<td><c:out
																				value="${fn:replace(assignment.startDate,'T', ' ')}"></c:out></td>
																		<td><c:out
																				value="${fn:replace(assignment.endDate, 
                                'T', ' ')}"></c:out></td>
																		<td><c:out
																				value="${fn:replace(assignment.submissionDate, 
                                'T', ' ')}"></c:out></td>

																		<td><c:if
																				test="${assignment.showFileDownload eq 'true'}">
																				<a href="downloadFile?id=${assignment.assignmentId}">Download</a>
																			</c:if> <c:if
																				test="${assignment.showFileDownload eq 'false'}">No File</c:if>
																		</td>
																		<td><c:if
																				test="${assignment.showStudentFileDownload eq 'true'}">
																				<a href="downloadFile?saId=${assignment.id }">Download
																					Answer File</a>
																			</c:if> <c:if
																				test="${assignment.showStudentFileDownload eq 'false'}">No File</c:if></td>
																		<td><c:out value="${assignment.attempts}" /></td>
																		<td><c:out value="${assignment.submissionStatus}" /></td>
																		<td><c:out value="${assignment.evaluationStatus}" /></td>
																		<td><c:out value="${assignment.score}" /></td>
																		<td><c:out value="${assignment.remarks}" /></td>

																	</tr>
																</c:forEach>


															</tbody>
														</table>
													</div>
												</div>

											</div>
										</div>
									</c:when>
								</c:choose>
								<!-- <div class="row">

												<div class="col-sm-8 column">
													<div class="form-group">
														<button id="cancel" class="btn btn-danger" type="button"
															formaction="homepage" formnovalidate="formnovalidate"
															onclick="history.go(-2);">Back</button>
													</div>
												</div>
											</div> -->


							</form:form>
						</div>
					</div>
				</div>

				<!-- Results Panel -->

			</div>

			<!-- SIDEBAR START -->
			<jsp:include page="../common/newSidebar.jsp" />
			<!-- SIDEBAR END -->
			<jsp:include page="../common/footer.jsp" />
			</sec:authorize>