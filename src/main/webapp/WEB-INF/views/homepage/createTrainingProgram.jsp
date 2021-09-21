<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<html lang="en">
<jsp:include page="../common/css.jsp" />
<link
	href="https://gitcdn.github.io/bootstrap-toggle/2.2.2/css/bootstrap-toggle.min.css"
	rel="stylesheet">
<script
	src="https://gitcdn.github.io/bootstrap-toggle/2.2.2/js/bootstrap-toggle.min.js"></script>

<body class="nav-md footer_fixed">

	<div class="loader"></div>


	<div class="container body">
		<div class="main_container">

			<jsp:include page="../common/leftSidebar.jsp">
				<jsp:param name="courseId" value="${courseId}" />
			</jsp:include>
			<jsp:include page="../common/topHeader.jsp">
				<jsp:param value="Forum" name="activeMenu" />
			</jsp:include>


			<!-- page content: START -->
			<div class="right_col" role="main">

				<div class="dashboard_container">

					<div class="dashboard_container_spacing">

						<div class="breadcrumb">





							<br> <br> <a
								href="${pageContext.request.contextPath}/homepage">Dashboard</a>
							<i class="fa fa-angle-right"></i>
						</div>
						<jsp:include page="../common/alert.jsp" />
						<!-- Input Form Panel -->
						<div class="row">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">

									<!-- <div class="x_title">
										<h2>Create Forum</h2>
										<ul class="nav navbar-right panel_toolbox">
											<li><a class="collapse-link"><i
													class="fa fa-chevron-up"></i></a></li>
										</ul>
										<div class="clearfix"></div>
									</div> -->

									<div class="x_title">
										<h2></h2>


										<ul class="nav navbar-right panel_toolbox">
											<li><a class="collapse-link"><i
													class="fa fa-chevron-up"></i></a></li>
										</ul>
										<div class="clearfix"></div>
									</div>

									<div class="x_content">
										<form:form action="createTraingProgramForm" method="post"
											modelAttribute="TrainingProgram"
											enctype="multipart/form-data" path="id">

											<%-- <form:input path="id" type="hidden" /> --%>

											<div class="row">
												<div class="col-sm-6 col-md-4 col-xs-12 column">
													<div class="form-group">
														<form:label path="trainingTitle" for="name">Title</form:label>
														<form:input path="trainingTitle" type="text"
															class="form-control" required="required" />
													</div>
												</div>
											</div>
											<div class="row">
												<div class="col-sm-6 col-md-4 col-xs-12 column">
													<div class="form-group">
														<form:label path="date" for="mapping">Date</form:label>
														<form:input path="date" type="date" class="form-control"
															required="required" />
													</div>
												</div>
											</div>
											<div class="row">
												<div class="col-sm-6 col-md-4 col-xs-12 column">
													<div class="form-group">
														<form:label path="startTime" for="level1">Start Time</form:label>
														<%-- <form:input path="startTime" type="time"
															class="form-control" required="required" /> --%>
														<form:input path="startTime" type="datetime-local"
															class="form-control" required="required" />
													</div>
												</div>
											</div>
											<div class="row">
												<div class="col-sm-6 col-md-4 col-xs-12 column">
													<div class="form-group">
														<form:label path="endTime" for="level1">End Time</form:label>
														<%-- <form:input path="endTime" type="time"
															class="form-control" required="required" /> --%>
														<form:input path="endTime" type="datetime-local"
															class="form-control" required="required" />
													</div>
												</div>
											</div>

											<div class="row">
												<div class="col-sm-6 col-md-4 col-xs-12 column">
													<div class="form-group">
														<form:label path="userType" for="level1">Select Training For</form:label>
														<%-- <form:input path="userType" type="text" class="form-control"
															required="required" /> --%>
														<form:select path="userType"
															class="form-control rounded-0 clearInput">
															<!-- required="required" -->
															<option value="">Select Role</option>
															<c:forEach var="pro" items="${userRoleList}"
																varStatus="status">
																<option value="${pro.role}">${pro.role}</option>
															</c:forEach>
														</form:select>

														<%-- 	<form:select path="userType" class="form-control">
															<form:option value="Student" label="Student" />
															<form:option value="Faculty" label="Faculty" />
															<form:option value="Parents" label="Parents" />
															<form:option value="Admin" label="Admin" />
															<form:option value="SAP Administrators"
																label="SAP Administrators" />
															<form:option value="Visiting Faculties"
																label="Visiting Faculties" />
															<form:option value="Faculty/Student"
																label="Faculty/Student" />
															<form:option value="Faculty /Admin"
																label="Faculty /Admin" />
														</form:select> --%>
													</div>
												</div>
											</div>

											<div class="row">
												<div class="col-sm-6 col-md-4 col-xs-12 column">
													<div class="form-group">
														<form:select id="programId" path="programId"
															class="form-control rounded-0 clearInput">
															<!-- required="required" -->
															<option value="">Select Program Name</option>
															<c:forEach var="pro" items="${programName}"
																varStatus="status">
																<option value="${pro.id}">${pro.programName}</option>
															</c:forEach>
														</form:select>
													</div>
												</div>
											</div>

											<div class="row">
												<div class="col-sm-6 col-md-4 col-xs-12 column">
													<div class="form-group">
														<form:select id="campusId" path="campusId"
															class="form-control rounded-0 clearInput">
															<!-- required="required" -->
															<option value="">Select Campus</option>
															<c:forEach var="pro" items="${campusNameid}"
																varStatus="status">
																<option value="${pro.campusId}">${pro.campusAbbr}</option>
															</c:forEach>
														</form:select>
													</div>
												</div>
											</div>


											<%-- 				<div class="row">
												<div class="col-sm-6 col-md-4 col-xs-12 column">
													<div class="form-group">
														<form:label path="school" for="level1">School/College Name</form:label>
														<form:input path="school" type="text" class="form-control"
															required="required" />
													</div>
												</div>
											</div>

											<div class="row">
												<div class="col-sm-6 col-md-4 col-xs-12 column">
													<div class="form-group">
														<form:label path="location" for="level1">Location</form:label>
														<form:input path="location" type="text"
															class="form-control" required="required" />
													</div>
												</div>
											</div> --%>

											<div class="row">
												<div class="col-sm-6 col-md-4 col-xs-12 column">
													<div class="form-group">
														<form:label path="ConductedBy" for="level1">Conducted By</form:label>
														<form:input path="ConductedBy" type="text"
															class="form-control" required="required" />
													</div>
												</div>
											</div>





											<div>
												<div class="col-sm-12 column">
													<div class="form-group">
														<button id="submit" class="btn btn-large btn-primary"
															formaction="createTrainingProgram">Create
															Training</button>
														<input id="reset" type="reset" class="btn btn-danger">

														<c:url value="viewscheduledprogram"
															var="viewscheduledprogram">
															<c:param name="" value="" />
														</c:url>
														<a href="viewscheduledprogram"
															class="btn btn-large btn-primary">View scheduled
															program</a>


													</div>
												</div>
											</div>


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

<script type="text/javascript">
	CKEDITOR
			.replace(
					'editor1',
					{
						extraPlugins : 'mathjax',
						mathJaxLib : 'https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.0/MathJax.js?config=TeX-AMS_HTML'
					});
</script>



<script>
	$("#campusName")
			.on(
					'change',
					function() {
						var campusName = $("#campusName").val();
						var programId = $("#programId").val();

						if (acadYear && programId) {
							$
									.ajax({
										type : 'GET',
										url : '${pageContext.request.contextPath}/getCampuseNameForTraining?'
												+ 'campusName='
												+ campusName
												+ '&programId=' + programId,
										success : function(data) {
											var json = JSON.parse(data);
											var optionsAsString = "";

											$('#programId').find('option')
													.remove();
											console.log(json);
											for (var i = 0; i < json.length; i++) {
												var idjson = json[i];
												console.log(idjson);

												for ( var key in idjson) {
													console.log(key + ""
															+ idjson[key]);
													optionsAsString += "<option value='" +key + "'>"
															+ idjson[key]
															+ "</option>";
												}
											}
											console.log("optionsAsString"
													+ optionsAsString);

											$('#programId').append(
													optionsAsString);

											$('#programId').trigger('change');

										}
									});
						} else {

						}
					});
</script>


<!-- <script>
	$(document)
			.ready(
					function() {

						$("#campusName")
								.on(
										'change',
										function() {
											var campusid = $(this).val();
											if (campusid) {
												$
														.ajax({
															type : 'GET',
															url : '${pageContext.request.contextPath}/getCampuseNameForTraining?'
																	+ 'campusid='
																	+ campusid,
															success : function(
																	data) {
																var json = JSON
																		.parse(data);
																var optionsAsString = "";

																$('#programId')
																		.find(
																				'option')
																		.remove();
																console
																		.log(json);
																for (var i = 0; i < json.length; i++) {
																	var idjson = json[i];
																	console
																			.log(idjson);

																	for ( var key in idjson) {
																		console
																				.log(key
																						+ ""
																						+ idjson[key]);
																		optionsAsString += "<option value='" +key + "'>"
																				+ idjson[key]
																				+ "</option>";
																	}
																}
																console
																		.log("optionsAsString"
																				+ optionsAsString);

																$('#programId')
																		.append(
																				optionsAsString);

																$('#programId')
																		.trigger(
																				'change');

															}
														});
											} else {
												//alert('Error no tds');
											}
										});
						$('#campusName').trigger('change');
					});
</script> -->

</html>
