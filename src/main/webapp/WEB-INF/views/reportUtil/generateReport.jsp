<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
							<i class="fa fa-angle-right"></i> Reports
						</div>
						<jsp:include page="../common/alert.jsp" />

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
										<form:form name="myForm" novalidate="required" method="post"
											modelAttribute="charts" enctype="multipart/form-data">


											<form:input path="courseId" type="hidden" />
											<div class="row">


												<div class="well" id="College">

													<a href="#" id="collegeId">Generate Report For College</a>
													<a href="#" id="collegeIdClose"
														style="display: none; float: right"><i
														class="fa fa-times" aria-hidden="true"></i></a>
													<div class="row" style="display: none"
														id="showDivForCollege">
														<div class="col-sm-4 col-md-4 col-xs-12 column"
															id="searchType">
															<div class="form-group">
																<form:label path="searchType" for="searchType">Select Assignments/Test/Feedback/Content <span style="color: red">*</span></form:label>
																<form:select id="searchType" path="searchType"
																	name="searchType" required="required"
																	class="form-control">
																	<form:option value="">Select Assignments/Test/Feedback/Content</form:option>

																	<c:forEach var="list" items="${chartList}"
																		varStatus="status">
																		<form:option value="${list}">${list}</form:option>
																	</c:forEach>

																</form:select>
															</div>
														</div>

														<div class="col-sm-12 column" id="submitSeach">
															<div class="form-group">
																<button id="submitId" name="submit"
																	formaction="create3DPieChartForSemesterYearForCollegeAll"
																	class="btn btn-large btn-primary">Search</button>
																<input id="reset" type="reset" class="btn btn-danger">


															</div>
														</div>
													</div>


												</div>
												<div class="well" id="Program">

													<a href="#" id="programId">Generate Report For Program</a>
													<a href="#" id="programIdClose"
														style="display: none; float: right"><i
														class="fa fa-times" aria-hidden="true"></i></a>
													<div class="row" style="display: none"
														id="showDivForProgram">
														<div class="col-sm-4 col-md-4 col-xs-12 column"
															id="searchType">
															<div class="form-group">
																<form:label path="searchType" for="searchType">Select Assignments/Test/Feedback/Content <span style="color: red">*</span></form:label>
																<form:select id="searchType" path="searchType"
																	name="searchType" required="required"
																	class="form-control">
																	<form:option value="">Select Assignments/Test/Feedback/Content</form:option>

																	<c:forEach var="list" items="${chartList}"
																		varStatus="status">
																		<form:option value="${list}">${list}</form:option>
																	</c:forEach>

																</form:select>
															</div>
														</div>

														<div class="col-sm-12 column" id="submitSeach">
															<div class="form-group">
																<button id="submitId" name="submit"
																	formaction="create3DPieChartForProgram"
																	class="btn btn-large btn-primary">Search</button>
																<input id="reset" type="reset" class="btn btn-danger">


															</div>
														</div>
													</div>
												</div>

												<div class="well" id="Course">
													<a href="#" id="courseId1">Generate Report For Course</a> <a
														href="#" id="courseIdClose"
														style="display: none; float: right"><i
														class="fa fa-times" aria-hidden="true"></i></a>
													<div class="row" style="display: none"
														id="showDivForCourse">

														<div class="col-md-4 col-sm-6 col-xs-12 column">
															<div class="form-group">
																<form:label path="" for="">Course</form:label>
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
														<%-- <div class="col-sm-12 column" id="">
															<div class="form-group">

																<a href="create3DPieChartForCourse?courseId=${courseId}"><i
																	class="btn btn-large btn-primary"
																	id="searchCourseReport">Search</i></a>

																<button id="submitCourse"
																	onclick="genereateCourseReport()"
																	class="btn btn-large btn-primary">Search</button>
																<input id="reset" type="reset" class="btn btn-danger">


															</div>
														</div> --%>
													</div>
												</div>

												<div class="well" id="Session">
													<a href="#" id="sessionId1">Generate Report For Session
														and Year</a> <a href="#" id="sessionId1Close"
														style="display: none; float: right"><i
														class="fa fa-times" aria-hidden="true"></i></a>

													<div class="row" style="display: none"
														id="showDivForSession">

														<div class="col-md-4 col-sm-6 col-xs-12 column"
															id="acadYearId">
															<div class="form-group">
																<label class="control-label" for="course">acadYear <span style="color: red">*</span></label>
																<form:select id="acadYearForAcadSession" path="acadYear"
																	placeholder="acad Year"
																	class="form-control facultyparameter"
																	required="required">
																	<form:option value="">Select Acad Year</form:option>
																	<form:options items="${yearList}" />
																</form:select>
															</div>
														</div>


														<div class="col-md-4 col-sm-6 col-xs-12 column"
															id="sessionId">
															<div class="form-group">
																<form:label path="acadSession" for="acadSession">Semester</form:label>
																<form:select id="acadSessionId" path="acadSession"
																	class="form-control">

																	<form:option value="">Select Semester</form:option>


																	<form:option value="${charts.acadSession }">${charts.acadSession }</form:option>

																</form:select>
															</div>

														</div>

														<div class="col-sm-4 col-md-4 col-xs-12 column"
															id="searchType">
															<div class="form-group">
																<form:label path="" for="">Select Assignments/Test/Feedback/Content <span style="color: red">*</span></form:label>
																<form:select id="searchType" path="searchType"
																	name="searchType" required="required"
																	class="form-control">
																	<form:option value="">Select Assignments/Test/Feedback/Content</form:option>

																	<c:forEach var="list" items="${chartList}"
																		varStatus="status">
																		<form:option value="${list}">${list}</form:option>
																	</c:forEach>

																</form:select>
															</div>
														</div>

														<div class="col-sm-12 column" id="submitSeach">
															<div class="form-group">
																<button id="submitId" name="submit"
																	formaction="create3DPieChartForSemesterYearForProgram"
																	class="btn btn-large btn-primary">Search</button>
																<input id="reset" type="reset" class="btn btn-danger">


															</div>
														</div>

													</div>

												</div>

												<div class="well" id="Users">
													<a href="#" id="usersId">Generate Report For Users</a> <a
														href="#" id="userIdClose"
														style="display: none; float: right"><i
														class="fa fa-times" aria-hidden="true"></i></a>
													<div class="container" id="chooseUser"
														style="display: none">

														<label class="radio-inline"> <input type="radio"
															name="role" value="ROLE_FACULTY" id="facultyId"
															name="optradio" />Faculty
														</label> <label class="radio-inline"> <input type="radio"
															name="role" value="ROLE_STUDENT" id="studentId"
															name="optradio" />Student
														</label>

														<div class="row" id="showUsersDiv" style="display: none">
															<div class="col-md-4 col-sm-6 col-xs-12 column">
																<div class="form-group">
																	<form:label path="username" for="username">Username</form:label>
																	<form:select id="username" path="username"
																		class="form-control">
																		<form:option value="">Select Username</form:option>

																		<c:forEach var="preUsers" items="${preUsersList}"
																			varStatus="status">
																			<form:option value="${preUsers.username}">${preUsers.username} - ${preUsers.firstname} ${preUsers.lastname}</form:option>
																		</c:forEach>
																	</form:select>
																</div>
															</div>
															<div class="col-sm-12 column">
																<div class="form-group">
																	<button id="submitId" name="submit"
																		formaction="create3DPieChartForUser"
																		class="btn btn-large btn-primary">Search</button>
																	<input id="reset" type="reset" class="btn btn-danger">


																</div>
															</div>


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
				</div>
			</div>



			<!-- /page content -->




			<jsp:include page="../common/footer.jsp" />
			<script>
				$(document)
						.ready(
								function() {

									$("#programId").on('click', function() {

										$('#showDivForProgram').show();
										$('#programIdClose').show();
										//alert("Onchange Function called!");

									});
									$("#programIdClose").on('click',
											function() {

												$('#showDivForProgram').hide();
												$('#programIdClose').hide();
												//alert("Onchange Function called!");

											});
									$("#collegeId").on('click', function() {

										$('#showDivForCollege').show();
										$('#collegeIdClose').show();

										//alert("Onchange Function called!");

									});
									$("#collegeIdClose").on('click',
											function() {

												$('#showDivForCollege').hide();
												$('#collegeIdClose').hide();

												//alert("Onchange Function called!");

											});

									$("#courseId1").on('click', function() {

										$('#showDivForCourse').show();
										$('#courseIdClose').show();

										//alert("Onchange Function called!");

									});
									$("#courseIdClose").on('click', function() {

										$('#showDivForCourse').hide();
										$('#courseIdClose').hide();

										//alert("Onchange Function called!");

									});

									$("#sessionId1").on('click', function() {

										$('#showDivForSession').show();
										$('#sessionId1Close').show();

										//alert("Onchange Function called!");

									});
									$("#sessionId1Close").on('click',
											function() {

												$('#showDivForSession').hide();
												$('#sessionId1Close').hide();

												//alert("Onchange Function called!");

											});
									$("#usersId").on('click', function() {

										$('#chooseUser').show();
										$('#userIdClose').show();

										//alert("Onchange Function called!");

									});
									$("#userIdClose").on('click', function() {

										$('#chooseUser').hide();
										$('#userIdClose').hide();

										//alert("Onchange Function called!");

									});

									$("#chooseUser").on('click', function() {

										$('#chooseUser').show();

										//alert("Onchange Function called!");

									});
									$("#facultyId")
											.on(
													'click',
													function() {

														$('#showUsersDiv')
																.show();

														var role = 'ROLE_FACULTY';
														$
																.ajax({
																	type : 'GET',
																	url : '${pageContext.request.contextPath}/getUsersListByProgram?'
																			+ 'role='
																			+ role,
																	success : function(
																			data) {

																		var json = JSON
																				.parse(data);
																		var optionsAsString = "";

																		$(
																				'#username')
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

																		$(
																				'#username')
																				.append(
																						optionsAsString);

																	}
																});

													});
									$("#studentId")
											.on(
													'click',
													function() {

														$('#showUsersDiv')
																.show();

														var role = 'ROLE_STUDENT';
														$
																.ajax({
																	type : 'GET',
																	url : '${pageContext.request.contextPath}/getUsersListByProgram?'
																			+ 'role='
																			+ role,
																	success : function(
																			data) {

																		var json = JSON
																				.parse(data);
																		var optionsAsString = "";

																		$(
																				'#username')
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

																		$(
																				'#username')
																				.append(
																						optionsAsString);

																	}
																});

													});

									$(".facultyparameter")
											.on(
													'change',
													function() {
														var acadYear = $(
																'#acadYearForAcadSession')
																.val();

														console.log(acadYear);

														if (acadYear) {
															console
																	.log("called acad session")
															$
																	.ajax({
																		type : 'GET',
																		url : '${pageContext.request.contextPath}/getSemesterByAcadYearForFeedback?'
																				+ 'acadYear='
																				+ acadYear,
																		success : function(
																				data) {

																			var json = JSON
																					.parse(data);
																			var optionsAsString = "";

																			$(
																					'#acadSessionId')
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

																			$(
																					'#acadSessionId')
																					.append(
																							optionsAsString);

																		}
																	});
														} else {
															//  alert('Error no faculty');
														}
													});

									$('#facultyparameter').trigger('change');

								});
			</script>
			<script>
				$("#courseIdForForum")
						.on(
								'change',
								function() {

									//alert("Onchange Function called!");
									var selectedValue = $(this).val();
									window.location = '${pageContext.request.contextPath}/create3DPieChartForCourse?courseId='
											+ encodeURIComponent(selectedValue);
									return false;
								});
			</script>

		</div>
	</div>
</body>
</html>
