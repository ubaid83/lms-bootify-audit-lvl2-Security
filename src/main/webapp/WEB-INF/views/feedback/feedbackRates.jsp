<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
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
							<i class="fa fa-angle-right"></i> Feedback Record for Faculty
						</div>
						<jsp:include page="../common/alert.jsp" />

						<!-- Input Form Panel -->
						<div class="row">

							<div class="col-md-12 col-xs-12 col-sm-12">
								<div class="x_panel">

									<div class="x_title">
										<h2>Feedback Record for faculty ${user.username}</h2>
										<ul class="nav navbar-right panel_toolbox">
											<li><a class="collapse-link"><i
													class="fa fa-chevron-up"></i></a></li>
										</ul>
										<div class="clearfix"></div>
									</div>


									<div class="x_content">
										<form>
											<div class="row">
												<div class="col-md-4 col-sm-6 col-xs-12">
													<div class="form-group">

														<label for="facultyId">Faculty</label> <select id="facultyId"
															name="facultyId" class="form-control">
															<c:if test="${facultyId eq  null }">
																<option value="">Select Faculty</option>
															</c:if>
															<c:forEach var="user" items="${userList}"
																varStatus="status">
																<c:if test="${facultyId eq user.username }">
																	<option value="${user.username}" selected>${user.username}</option>
																</c:if>
																<c:if test="${facultyId ne user.username }">
																	<option value="${user.username}">${user.username}</option>
																</c:if>
															</c:forEach>
														</select>
													</div>
												</div>
												
											</div>

										</form>
									</div>
								</div>
							</div>
						</div>



						<!-- Results Panel -->
						<c:if test="${showFeedbackRecords eq true}">
						<div class="row">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">
									<div class="x_title">
										<h2>Feedback Report | ${sfrecordSize} Records Found</h2>
										<ul class="nav navbar-right panel_toolbox">
											<li><a class="collapse-link"><i
													class="fa fa-chevron-up"></i></a></li>
											<li><a class="close-link"><i class="fa fa-close"></i></a></li>
										</ul>
										<div class="clearfix"></div>
									</div>
									<div class="x_content">
										<c:choose>
											<c:when test="${sfrecordSize>0}">
												<div class="table-responsive">
													<table class="table table-hover" id="feedbackReport1"
														style="font-size: 12px">
														<thead>
															<tr>
																<th rowspan="2" class="text-center border-grey">Program</th>
																<th rowspan="2" class="text-center border-grey">Trimester</th>
																<th rowspan="2" class="text-center border-grey">FacultyName</th>
																<th rowspan="2" class="text-center border-grey">CoreVisitingFaculty</th>
																<th rowspan="2" class="text-center border-grey">Area</th>
																<th rowspan="2" class="text-center border-grey">CourseName</th>
																<th rowspan="2" class="text-center border-grey">TotalNoOfStudents</th>
																<th rowspan="2" class="text-center border-grey">TotalNoOfStudentsGaveFeedback</th>
																<th rowspan="2" class="text-center border-grey">Course</th>
																<th rowspan="2" class="text-center border-grey">Average</th>
																<th rowspan="2" class="text-center border-grey">Term</th>
																<th rowspan="2" class="text-center border-grey">Grand
																	Average</th>
															</tr>
														</thead>
														<tbody>

															<c:forEach var="sFacultyRecord" items="${sfrecord}"
																varStatus="status">
																<tr>
																	<td><div class="text-nowrap">${sFacultyRecord.programId}</div></td>
																	<td><div class="text-nowrap">${sFacultyRecord.trimester}</div></td>
																	<td><div class="text-nowrap">${sFacultyRecord.facultyName}</div></td>

																	<td><div class="text-nowrap">${sFacultyRecord.coreVisitingFaculty}</div></td>
																	<td><div class="text-nowrap">${sFacultyRecord.area}</div></td>

																	<td><div class="text-nowrap">${sFacultyRecord.courseName}</div></td>
																	<td><div class="text-nowrap">${sFacultyRecord.totalNoOfStudents}</div></td>
																	<td><div class="text-nowrap">${sFacultyRecord.totalNoOfStudentGaveFeedback}</div></td>
																	<td><div class="text-nowrap">${sFacultyRecord.courseName}</div></td>

																	<td><div class="text-nowrap">${sFacultyRecord.average}</div></td>
																	<td><div class="text-nowrap">${sFacultyRecord.term}</div></td>
																	<td><div class="text-nowrap">${sFacultyRecord.grandAverage}</div></td>
																</tr>
															</c:forEach>


														</tbody>
													</table>
												</div>
											</c:when>
										</c:choose>
										<br>
										<button id="btn1" class="btn btn-primary">Export to
											CSV</button>
									</div>
								</div>
							</div>
						</div>
					</c:if>
					</div>



				</div>

			</div>
			<!-- /page content: END -->


			<jsp:include page="../common/footerForFeedback.jsp" />

		</div>
	</div>





</body>
</html>
<script>
	$("#facultyId")
			.on(
					'change',
					function() {
						var selectedValue = $(this).val();
						window.location = '${pageContext.request.contextPath}/searchStudentFeedbackResponse?facultyId='
								+ encodeURIComponent(selectedValue);
						return false;
					});
</script>

<script src="<c:url value="/resources/js/table2csv.js" />"
	type="text/javascript"></script>

<script>
	$("#btn1").click(function() {
		console.log("clicked-----------");
		$("#feedbackReport1").table2csv({

			filename : 'feedbackReport.csv'

		});
		$("#feedbackReport1").table2csv();

	})
</script>
