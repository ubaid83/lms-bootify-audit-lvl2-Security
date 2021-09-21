<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<html lang="en">
<jsp:include page="../common/css.jsp" />

<body class="nav-md footer_fixed">

	<div class="loader"></div>


	<div class="container body">
		<div class="main_container">


			<jsp:include page="../common/leftSidebar.jsp" />
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
							<a href="${pageContext.request.contextPath}/homepage">Dashboard</a> <i class="fa fa-angle-right"></i>
							Search Assignments
						</div>
						<jsp:include page="../common/alert.jsp" />

						<!-- Input Form Panel -->
						<div class="row">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">

									<div class="x_title">
										<h2>My Assignments</h2>
										<ul class="nav navbar-right panel_toolbox">
											<li><a class="collapse-link"><i
													class="fa fa-chevron-up"></i></a></li>
										</ul>
										<div class="clearfix"></div>
									</div>

									<div class="x_content">
										<form:form action="searchAssignment" method="post"
											modelAttribute="assignment">


											<div class="row">
												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<form:label path="courseId" for="courseId">Course</form:label>
														<form:select id="courseIdForForum" path="courseId"
															class="form-control">
															<form:option value="">Select Course</form:option>
															<c:forEach var="course" items="${allCourses}"
																varStatus="status">
																<form:option value="${course.id}">${course.courseName}</form:option>
															</c:forEach>
														</form:select>
													</div>
												</div>
												

												<div class="col-sm-12 column">
													<div class="form-group">
														<button id="submit" name="submit"
															class="btn btn-large btn-primary">Search</button>
														<input id="reset" type="reset" class="btn btn-danger">
														<button id="cancel" name="cancel" class="btn btn-danger"
															formnovalidate="formnovalidate">Cancel</button>
													</div>
												</div>
											</div>

										</form:form>
									</div>
								</div>
							</div>
						</div>
						<c:choose>
							<c:when test="${page.rowCount > 0}">
								<!-- Results Panel -->
								<div class="row">
									<div class="col-xs-12 col-sm-12">
										<div class="x_panel">
											<div class="x_title">
												<h2>Assignments | ${rowCount} Records Found</h2>
												<ul class="nav navbar-right panel_toolbox">
													<li><a class="collapse-link"><i
															class="fa fa-chevron-up"></i></a></li>
													<li><a class="close-link"><i class="fa fa-close"></i></a></li>
												</ul>
												<div class="clearfix"></div>
											</div>
											<div class="x_content">
												<div class="table-responsive">
													<table class="table table-hover">
														<thead>
															<tr>
																<th>Sr. No.</th>
																<th>Session Month</th>
																<th>Session Year</th>
																<th>Course</th>
																<th>Assignment Name</th>
																<th>End Date</th>
																<th>Marks out of</th>
																<th>Assignment Details</th>
																<th>Assignment File</th>
																<!-- <th>Actions</th> -->
															</tr>
														</thead>
														<tbody>

															<c:forEach var="assignment" items="${page.pageItems}"
																varStatus="status">
																<tr>
																	<td><c:out value="${status.count}" /></td>
																	<td><c:out value="${assignment.acadMonth}" /></td>
																	<td><c:out value="${assignment.acadYear}" /></td>
																	<td><c:out value="${assignment.course.courseName}" /></td>
																	<td><c:out value="${assignment.assignmentName}" /></td>
																	<td><c:out value="${assignment.endDate}" /></td>
																	<td><c:out value="${assignment.maxScore}" /></td>
																	<td><a href="#"
																		onClick="showModal('${assignment.id}', '${assignment.assignmentName}');">Assignment
																			Details</a></td>
																	<td><c:url value="downloadFile" var="downloadurl">
																			<c:param name="filePath"
																				value="${assignment.filePath}" />
																		</c:url> <a href="${downloadurl}" title="Details">Download</a>&nbsp;
																		<%--  <td> 
												<c:url value="viewAssignment" var="viewUrl">
												  <c:param name="id" value="${assignment.id}" />
												</c:url>
												<a href="${viewUrl}" title="Assignment Details">Details</a>&nbsp;
								            </td> --%>
																</tr>
															</c:forEach>

														</tbody>
													</table>
												</div>
											</div>
										</div>
									</div>
								</div>
								<jsp:include page="../common/paginate.jsp">
									<jsp:param name="baseUrl" value="searchAssignment" />
								</jsp:include>
							</c:when>
						</c:choose>

					</div>



				</div>

			</div>
			<!-- /page content: END -->


			<jsp:include page="../common/footer.jsp" />

		</div>
	</div>





</body>
</html>
