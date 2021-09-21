<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<html lang="en">
<jsp:include page="../common/css.jsp" />

<body class="nav-md footer_fixed">

	<div class="loader"></div>


	<div class="container body">
		<div class="main_container">


			<jsp:include page="../common/leftSidebar.jsp">
				<jsp:param name="courseId" value="${courseId}" />
			</jsp:include>




			<jsp:include page="../common/topHeader.jsp" />


			<!-- page content: START -->
			<div class="right_col" role="main">

				<div class="dashboard_container">

					<div class="dashboard_container_spacing">

						<div class="breadcrumb">
						
						<c:out value="${Program_Name}" />
						
						<sec:authorize access="hasRole('ROLE_STUDENT')">
						
						<i
						class="fa fa-angle-right"></i>
											
											<c:out value="${AcadSession}" />
											
											</sec:authorize>
						<br><br>
							<a href="${pageContext.request.contextPath}/homepage">Dashboard</a>
							<i class="fa fa-angle-right"></i> Assignment List
						</div>
						<jsp:include page="../common/alert.jsp" />
						<!-- Input Form Panel -->
						<c:choose>
							<c:when test="${rowCount > 0}">
						<div class="row">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">

									<div class="x_title">
										<h2>Assignments | ${rowCount} Records Found</h2>
										
										<ul class="nav navbar-right panel_toolbox">
											<li><a class="collapse-link"><i
													class="fa fa-chevron-up"></i></a></li>
										</ul>
										<div class="clearfix"></div>
									</div>
									<div class="x_content">
										<c:choose>
											<c:when test="${rowCount > 0}">
												<div class="table-responsive">
													<table class="table table-hover">
														<thead>
															<tr>
																<th>Sr. No.</th>
																<!-- <th>Session Month</th>
																<th>Session Year</th> -->
																<th>Course</th>
																<th>Assignment Name</th>
																
																<th>End Date</th>
																<th>Status</th>
																<th>Marks out of</th>
																<th>Assignment Type</th>
																<th>Assignment File</th>
																<th>Actions</th>
															</tr>
														</thead>
														<tbody>

															<c:forEach var="assignment" items="${assignments}"
																varStatus="status">
																<tr>
																	<td><c:out value="${status.count}" /></td>
																	<%-- <td><c:out value="${assignment.acadMonth}" /></td>
																	<td><c:out value="${assignment.acadYear}" /></td> --%>
																	<td><c:out value="${assignment.courseName}" /></td>
																	<td><c:out value="${assignment.assignmentName}" /></td>
																	
																	<td><c:out value="${assignment.endDate}" /></td>
																	<td><c:if
																			test="${assignment.submissionStatus eq 'Y' }">
																			<c:out value="Submitted" />
																		</c:if> <c:if test="${assignment.submissionStatus ne 'Y' }">
																			<c:out value="Not Submitted" />
																		</c:if></td>
																	<td><c:out value="${assignment.maxScore}" /></td>
																	<td><c:out value="${assignment.assignmentType}" /></td>

																	<td><c:url value="downloadFile" var="downloadurl">
																			<c:param name="id" value="${assignment.id}" />
																		</c:url> <a href="${downloadurl}" title="Details">Download</a>&nbsp;






																	
																	<td><sec:authorize
																			access="hasRole('ROLE_STUDENT')">

																			<c:url value="submitAssignmentForm" var="submiturl">
																				<c:param name="id" value="${assignment.id}" />
																			</c:url>
																			<a href="${submiturl}" title="Submit Assignment">Submit</a>&nbsp;
																					
																			

																		</sec:authorize> <sec:authorize access="hasRole('ROLE_FACULTY')">
																			<c:url value="viewAssignment" var="editAssignmentUrl">
																				<c:param name="id" value="${assignment.id}" />
																			</c:url>
																			<a href="${editAssignmentUrl}"
																				title="Submit Assignment">Edit Assignment</a>&nbsp;
												</sec:authorize></td>
																</tr>
															</c:forEach>

														</tbody>
													</table>
												</div>
												<br>

											</c:when>
										</c:choose>
									</div>


								</div>
							</div>
						</div>
						</c:when>
						</c:choose>

						<!-- Results Panel -->

						<jsp:include page="../common/paginate.jsp">
							<jsp:param name="baseUrl" value="assignmentList" />
						</jsp:include>
					</div>



				</div>

			</div>
			<!-- /page content: END -->


			<jsp:include page="../common/footer.jsp" />

		</div>
	</div>





</body>
</html>

