<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<html lang="en">
<jsp:include page="../common/css.jsp" />
<%
	boolean isEdit = "true".equals((String) request
			.getAttribute("edit"));
%>

<body class="nav-md footer_fixed">

	<div class="loader"></div>


	<div class="container body">
		<div class="main_container">

			<jsp:include page="../common/leftSidebar.jsp">
				<jsp:param name="courseId" value="${courseId}" />
			</jsp:include>

			<jsp:include page="../common/topHeader.jsp" />
			<jsp:include page="../common/alert.jsp" />


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
							<i class="fa fa-angle-right"></i> Course Overview
						</div>

						<!-- Input Form Panel -->
						<div class="row">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">

									<div class="x_title">
										<h2>Course Overview Form</h2>
										<ul class="nav navbar-right panel_toolbox">
											<li><a class="collapse-link"><i
													class="fa fa-chevron-up"></i></a></li>
										</ul>
										<div class="clearfix"></div>
									</div>

									<div class="x_content">
										<form:form action="addCourseOverview" id="addCourseOverview"
											method="post" modelAttribute="courseOverview"
											enctype="multipart/form-data">
											<fieldset>

												<form:input path="courseId" type="hidden" />
												<%
													if (isEdit) {
												%>
												<form:input type="hidden" path="id" />
												<%
													}
												%>
												<div class="row">
													<div class="col-md-4 col-sm-6 col-xs-12 column">
														<div class="form-group">
															<form:label path="programId" for="programId">Program</form:label>
															<form:select id="programId" path="programId"
																class="form-control">
																<form:option value="">Select Program</form:option>
																<c:forEach var="program" items="${allPrograms}"
																	varStatus="status">
																	<form:option value="${program.id}">${program.programName}</form:option>
																</c:forEach>
															</form:select>
														</div>
													</div>
													<div class="col-md-4 col-sm-6 col-xs-12 column">
														<div class="form-group">
															<form:label path="courseId" for="courseId">Course</form:label>
															<form:select id="courseIdOverview" path="courseId"
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
												<div class="row">
													<div class="col-md-4 col-sm-6 col-xs-12 column">
														<div class="form-group">
															<form:label path="sessionType" for="sessionType">Trimester/Semester</form:label>
															<form:select id="sessionType" path="sessionType"
																class="form-control">
																<form:option value="">Select Session Type</form:option>
																<c:forEach var="session" items="${instituteSessionType}"
																	varStatus="status">
																	<form:option value="${session}"></form:option>
																</c:forEach>
															</form:select>
														</div>
													</div>
													<div class="col-md-4 col-sm-6 col-xs-12 column">
														<div class="form-group">
															<form:label path="code" for="code">Code</form:label>
															<form:input path="code" type="text" class="form-control" />
														</div>
													</div>


												</div>
												<div class="table-responsive">
													<table class="table table-striped table-hover"
														style="font-size: 12px">
														<thead>
															<tr>
																<th>Teaching Scheme</th>
															</tr>
														</thead>

														<tbody>

															<tr>
																<th>Lecture</th>
																<th>Practical</th>
																<th>Tutorial</th>
																<th>Credits</th>
															</tr>


															<tr>
																<td><form:input type="number" path="lecture"
																		for="lecture" /></td>
																<td><form:input type="number" path="practical"
																		for="practical" /></td>
																<td><form:input type="number" path="tutorial"
																		for="tutorial" /></td>
																<td><form:input type="number" path="credit"
																		for="credit" /></td>

															</tr>

														</tbody>
														<thead>
															<tr>
																<th>Evaluation Scheme</th>
															</tr>
														</thead>
														<tbody>

															<tr>
																<th>ICA weightage</th>
																<th>TEE weightage</th>

															</tr>


															<tr>
																<td><form:input type="number" path="ica" for="ica" /></td>
																<td><form:input type="number" path="tee" for="ica" /></td>


															</tr>

														</tbody>

													</table>
												</div>
												<div class="row">

													<div class="form-group">
														<form:label path="preRequisite" for="preRequisite">Pre Requisite</form:label>
														<form:textarea class="form-control" path="preRequisite"
															id="preRequisite" rows="5" placeholder="Pre Requisite" />
													</div>
												</div>
												<div class="row">
													<div class="form-group">
														<form:label path="objectives" for="objectives">Objectives</form:label>
														<form:textarea class="form-control" path="objectives"
															id="objectives" rows="5" placeholder="Objectives" />
													</div>
												</div>
												<div class="row">
													<div class="form-group">
														<form:label path="outcomes" for="outcomes">Outcomes</form:label>
														<form:textarea class="form-control" path="outcomes"
															id="outcomes" rows="5" placeholder="Outcomes" />
													</div>
												</div>

												<button id="addCourseOverview"
													class="btn btn-large btn-primary">Add Overview And
													then Add Syllabus</button>






											</fieldset>
										</form:form>
										<form:form action="addSyllabus" id="addSyllabus" method="post"
											modelAttribute="courseSyllabus">
											<button id="addSyllabus" class="btn btn-large btn-primary">Add
												Syllabus</button>


											<button id="cancel" class="btn btn-danger"
												formaction="homepage" formnovalidate="formnovalidate">Cancel</button>
										</form:form>
									</div>
								</div>
							</div>
						</div>

						<!-- Results Panel -->


					</div>



				</div>

			</div>
			<!-- /page content: END -->


			<jsp:include page="../common/footer.jsp" />

		</div>
	</div>





</body>
<script>
	$(document)
			.ready(
					function() {
						$("#courseIdOverview")
								.change(
										function() {
											console.log(" course id"
													+ $(this).val());
											var courseId = $(this).val();
											var url = '${pageContext.request.contextPath}/addSyllabusForm?courseId='
													+ courseId;
											$("#addSyllabus").attr(
													'formaction', url);
										});
					});
</script>
</html>
