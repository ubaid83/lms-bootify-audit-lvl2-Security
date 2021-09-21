<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
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
							<a href="${pageContext.request.contextPath}/homepage">Dashboard</a>
							<i class="fa fa-angle-right"></i> View Grades
						</div>
						<jsp:include page="../common/alert.jsp" />

						<!-- Input Form Panel -->
						<div class="row">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">

									<div class="x_title">
										<h2>Forums</h2>
										<ul class="nav navbar-right panel_toolbox">
											<li><a class="collapse-link"><i
													class="fa fa-chevron-up"></i></a></li>
										</ul>
										<div class="clearfix"></div>
									</div>

									<div class="x_content">
										<form:form action="viewGradeWeight" method="post"
											modelAttribute="studentWeightData"
											enctype="multipart/form-data">


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
						<div class="row">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">
									<div class="x_title">
										<h2>Grades | ${fn:length(studentList)} Records found</h2>

										<ul class="nav navbar-right panel_toolbox">
											<li><a class="collapse-link"><i
													class="fa fa-chevron-up"></i></a></li>
											<li><a class="close-link"><i class="fa fa-close"></i></a></li>
										</ul>
										<div class="clearfix"></div>
									</div>
									<div class="x_itemCount" style="display: none;">
										<div class="image_not_found">
											<i class="fa fa-newspaper-o"></i>
											<p>
												<label class=""></label>${fn:length(studentList)} Students
											</p>
										</div>
									</div>
									<div class="x_content">
										<form:form action="viewGradeWeight" method="post"
											modelAttribute="studentWeightData"
											enctype="multipart/form-data">
											<form:input path="courseId" type="hidden" />
											<div class="table-responsive">
												<table class="table  table-hover">
													<thead>
														<tr>
															<th>Sr. No.</th>


															<th>Student Name <i class="fa fa-sort"
																aria-hidden="true" style="cursor: pointer"></i></th>
															<th>Roll No. <i class="fa fa-sort"
																aria-hidden="true" style="cursor: pointer"></i></th>
															<th>SAPID <i class="fa fa-sort" aria-hidden="true"
																style="cursor: pointer"></i></th>
															<th>ICA Out of<i class="fa fa-sort"
																aria-hidden="true" style="cursor: pointer"></i></th>
															<th>ICA Scored<i class="fa fa-sort"
																aria-hidden="true" style="cursor: pointer"></i></th>
															<th>P/F<i class="fa fa-sort" aria-hidden="true"
																style="cursor: pointer"></i></th>
														</tr>
													</thead>
													<tfoot>
														<tr>
															<th></th>

															<th>Student Name</th>
															<th>Roll No.</th>
															<th>SAPID</th>
															<th>ICA Out of</th>
															<th>ICA Scored</th>


														</tr>
													</tfoot>
													<tbody>
														<c:forEach var="student" items="${studentList}"
															varStatus="status">
															<tr>
																<td><c:out value="${status.count}" /></td>

																<td><c:out
																		value="${student.firstname} ${student.lastname}" /></td>
																<td><c:out value="${student.rollNo}" /></td>
																<td><c:out value="${student.username}" /></td>


																<td><c:out value="${student.icaTotal}" /></td>
																<td><c:out value="${student.icaScored}" /></td>

																<td><c:if test="${student.icaPassed eq true }">
																		<h6 style="color: navy;">P</h6>
																	</c:if> <c:if test="${student.icaPassed eq false }">
																		<h6 style="color: red;">F</h6>
																	</c:if></td>
															</tr>
														</c:forEach>

													</tbody>
												</table>
											</div>

											<div class="table-responsive">
												<table class="table  table-hover">
													<thead>
														<tr>
															<th>Sr. No.</th>


															<th>Component<i class="fa fa-sort"
																aria-hidden="true" style="cursor: pointer"></i></th>
															<th>Number of Components<i class="fa fa-sort"
																aria-hidden="true" style="cursor: pointer"></i></th>
															<th>Out of<i class="fa fa-sort" aria-hidden="true"
																style="cursor: pointer"></i></th>
															<th>Scored<i class="fa fa-sort" aria-hidden="true"
																style="cursor: pointer"></i></th>

														</tr>
													</thead>
													<tfoot>
														<tr>
															<th></th>

															<th>Component</th>
															<th>Number of Components</th>
															<th>Out of</th>
															<th>Scored</th>


														</tr>
													</tfoot>
													<tbody>
														<c:forEach var="weight" items="${weightDataList}"
															varStatus="st">
															<tr>
																<td><c:out value="${st.count}" /></td>

																<td><c:out value="${weight.weightType}" /></td>
																<td><c:out value="${weight.count}" /></td>
																<td><c:out value="${weight.weightAssigned}" /></td>


																<td><c:out value="${weight.semiFinalScore}" /></td>


															</tr>
														</c:forEach>

													</tbody>
												</table>
											</div>
										</form:form>
									</div>
								</div>
							</div>
						</div>

					</div>



				</div>

			</div>
			<!-- /page content: END -->


			<jsp:include page="../common/footer.jsp" />
			<script>
				$("#courseIdForForum")
						.on(
								'change',
								function() {

									//alert("Onchange Function called!");
									var selectedValue = $(this).val();
									window.location = '${pageContext.request.contextPath}/viewGradeWeight?courseId='
											+ encodeURIComponent(selectedValue);
									return false;
								});
			</script>

		</div>
	</div>





</body>
</html>

