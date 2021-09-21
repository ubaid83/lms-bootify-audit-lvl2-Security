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




			<jsp:include page="../common/topHeaderLibrian.jsp" />


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
						
						<div class="row">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">

									<div class="x_title">
										<h2>Assignments | ${rowCount} Records Found | Course: ${course.courseName}</h2>
										
										<ul class="nav navbar-right panel_toolbox">
											<li><a class="collapse-link"><i
													class="fa fa-chevron-up"></i></a></li>
										</ul>
										<div class="clearfix"></div>
									</div>
									<div class="x_content">
										
												<div class="table-responsive">
													<table class="table table-hover">
														<thead>
															<tr>
																<th>Sr. No.</th>
																<!-- <th>Session Month</th>
																<th>Session Year</th> -->
																
																<th>Assignment Name</th>
																<th>Start Date</th>
																<th>End Date</th>
																<th>Status</th>
																
																<!-- <th>Assignment Type</th> -->
																
															</tr>
														</thead>
														<c:choose>
											<c:when test="${rowCount > 0}">
														<tbody>

															<c:forEach var="assignment" items="${assignments}"
																varStatus="status">
																<tr>
																	<td><c:out value="${status.count}" /></td>
																	<%-- <td><c:out value="${assignment.acadMonth}" /></td>
																	<td><c:out value="${assignment.acadYear}" /></td> --%>
																	
																	<td><c:out value="${assignment.assignmentName}" /></td>
																	<td><c:out value="${assignment.startDate}"/></td>
																	<td><c:out value="${assignment.endDate}" /></td>
																	<td><c:if
																			test="${assignment.submissionStatus eq 'Y' }">
																			<c:out value="Submitted" />
																		</c:if> <c:if test="${assignment.submissionStatus ne 'Y' }">
																			<c:out value="Not Submitted" />
																		</c:if></td>
																	
																	<%-- <td><c:out value="${assignment.assignmentType}" /></td> --%>

																	
																</tr>
															</c:forEach>

														</tbody>
															</c:when>
										</c:choose>
													</table>
												</div>
												<br>

										<c:url value="/checkUserCourse" var="goBack">
													<c:param name="username" value="${username}"></c:param>
													</c:url>
													<a href="${goBack}" class="btn btn-success" >Back</a>
										
										
									</div>

										
								</div>
							</div>
						</div>
						
						
						<!-- Results Panel -->

						<jsp:include page="../common/paginate.jsp">
							<jsp:param name="baseUrl" value="assignmentList" />
						</jsp:include>
					</div>



				</div>

			</div>
			<!-- /page content: END -->


			<jsp:include page="../common/footerLibrarian.jsp" />

		</div>
	</div>





</body>
</html>

