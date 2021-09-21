<%@page import="java.io.Console"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<html lang="en">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<jsp:include page="../common/css.jsp" />



<body class="nav-md footer_fixed">


	<!-- <div class="loader"></div> -->

	<div class="container body">
		<div class="main_container">

			<%-- <jsp:include page="../common/leftSidebar.jsp">
				<jsp:param name="courseId" value="${courseId}" />
			</jsp:include> --%>
			<jsp:include page="../common/leftSidebar.jsp" />
			<jsp:include page="../common/topHeader.jsp" />



			<!-- page content: START -->
			<div class="right_col" role="main">

				<div class="dashboard_container">

					<div class="dashboard_container_spacing">

						<div class="breadcrumb">
							<c:out value="${Program_Name}" />
							<c:if test="${UserRole=='ROLE_STUDENT'}">
								<i class="fa fa-angle-right"></i>

								<c:out value="${AcadSession}" />
							</c:if>
							<br> <br> <a
								href="${pageContext.request.contextPath}/homepage">Dashboard</a>
							<i class="fa fa-angle-right"></i> Download Report

						</div>
						<jsp:include page="../common/alert.jsp" />

						<!-- Input Form Panel -->
						<div class="row">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">

									<div class="x_title">
										<h2>Reports</h2>
										<ul class="nav navbar-right panel_toolbox">
											<li><a class="collapse-link"><i
													class="fa fa-chevron-up"></i></a></li>
										</ul>
										<div class="clearfix"></div>
									</div>

									<div class="x_content">

										<form:form action="createReportsForm" method="post"
											modelAttribute="charts" enctype="multipart/form-data">


											<form:input path="courseId" type="hidden" />
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
											</div>
										</form:form>
									</div>
								</div>
							</div>
						</div>


						<!-- Results Panel -->
						<c:if test="${showTable}">
							<div class="row">
								<div class="col-xs-12 col-sm-12">

									<div class="x_panel">
										<div class="x_title">
											<h2>Reports</h2>
											<ul class="nav navbar-right panel_toolbox">
												<li><a class="collapse-link"><i
														class="fa fa-chevron-up"></i></a></li>
											</ul>
											<div class="clearfix"></div>
										</div>

										<div class="x_content">


											<form:form action="createReportsForm" method="post"
												modelAttribute="charts" enctype="multipart/form-data">

												<form:input path="courseId" type="hidden" />

												<div class="table-responsive">
													<table class="table  table-hover">
														<thead>
															<tr>

																<th class="text-center border-grey">Charts</th>
																<th class="text-center border-grey">View <br>
																	(Right Click on the link & save link to download
																	chart.)
																</th>

															</tr>
														</thead>

														<tbody>


															<tr>
																<td class="text-center border-grey">Bar Chart For
																	Assignments</td>

																<td class="text-center border-grey"><a
																	target="_blank"
																	href="create3DStackedBarChartForStudentAssignment?courseId=${courseId}">View</a></td>
															</tr>
															<tr>
																<td class="text-center border-grey">Bar Chart For
																	Tests</td>
																<td class="text-center border-grey"><a
																	target="_blank"
																	href="create3DStackedBarChartForStudentTest?courseId=${courseId}">View</a>

																</td>

															</tr>

														</tbody>
													</table>
												</div>


											</form:form>

										</div>
									</div>
								</div>
							</div>
						</c:if>
					</div>



				</div>

			</div>

			<!-- /page content: END -->


			<jsp:include page="../common/footer.jsp" />

		</div>
	</div>
</body>

<script>
	var score = $('#courseIdForForum').val();
	if (score == "") {
		alert('Please select course!');
	}

	$("#courseIdForForum")
			.on(
					'change',
					function() {

						//alert("Onchange Function called!");
						var selectedValue = $(this).val();
						window.location = '${pageContext.request.contextPath}/createReportsForm?courseId='
								+ encodeURIComponent(selectedValue);
						return false;
					});
</script>

<!-- <script>
	$("#down123")
	.on(
			'click',
			function() {
				console.log(selectedValue);
				 var selectedValue = $(this).val();
				window.location = '${pageContext.request.contextPath}/create3DStackedBarChartForStudent?studentUsername=${username}&courseId='
						+ encodeURIComponent(selectedValue);
				
				
				
				return false;
			});
</script> -->


</html>
