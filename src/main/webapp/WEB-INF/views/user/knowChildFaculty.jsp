<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<script
	src="//ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
<script
	src="//netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js"></script>

<html lang="en">
<jsp:include page="../common/css.jsp" />

<body class="nav-md footer_fixed">

	<div class="loader"></div>


	<div class="container body">
		<div class="main_container">

			<form:form action="knowChildFaculty" method="post"
				modelAttribute="facultyDetails">
				<%--  <jsp:include page="../common/leftSidebar.jsp">
			<jsp:param name="courseId" value="${courseId}" />
		</jsp:include>     --%>
				<jsp:include page="../common/topHeader.jsp" />



				<!-- page content: START -->
				<div class="right_col" role="main">

					<div class="dashboard_container">

						<div class="dashboard_container_spacing">

							<div class="breadcrumb">
								<c:out value="${Program_Name}" />

								<sec:authorize access="hasRole('ROLE_STUDENT')">

									<i class="fa fa-angle-right"></i>

									<c:out value="${AcadSession}" />

								</sec:authorize>

								<br>
								<br> <a href="${pageContext.request.contextPath}/homepage">Dashboard</a>
								<i class="fa fa-angle-right"></i> Know Your Child's faculty
							</div>
							<jsp:include page="../common/alert.jsp" />
							<!-- Input Form Panel -->
							<div class="row">
								<div class="col-xs-12 col-sm-12">
									<div class="x_panel">

										<div class="x_title">
											<h2>Know My Child's faculty for
												${facultyDetails.course.courseName }</h2>
											<ul class="nav navbar-right panel_toolbox">
												<li><a class="collapse-link"><i
														class="fa fa-chevron-up"></i></a></li>
											</ul>
											<div class="clearfix"></div>
										</div>

										<div class="x_content">

											<form:form action="knowChildFaculty" method="post"
												modelAttribute="facultyDetails">



												<div class="col-sm-4 col-md-4 col-xs-12 column">
													<div class="form-group">
														<form:label path="courseId" for="courseId">Course</form:label>
														<form:select id="courseId" path="courseId"
															class="form-control">
															<form:option value="">Select Course</form:option>
															<c:forEach var="course" items="${allCourses}"
																varStatus="status">
																<form:option value="${course.id}">${course.courseName}</form:option>
															</c:forEach>
														</form:select>
													</div>
												</div>


												<%-- <div class="col-sm-4 col-md-4 col-xs-12 column">
                              <div class="form-group">
                                    <form:label path="username" for="username">Student User Name</form:label>
                                    <form:input path="username" type="text" class="form-control" />
                              </div>
                        </div --%>


												<div class="col-sm-12 column">
													<div class="form-group">
														<form:button id="submit" name="submit"
															class="btn btn-large btn-primary">Search
									
									
									</form:button>
														<input id="reset" name="reset" type="reset"
															class="btn btn-danger">
														<button id="cancel" name="cancel" class="btn btn-danger"
															formnovalidate="formnovalidate">Cancel</button>
													</div>
												</div>

											</form:form>
										</div>
									</div>
								</div>
							</div>
							<c:choose>
								<c:when test="${facultyList.size() > 0}">
									<!-- Results Panel -->
									<div class="row">
										<div class="col-xs-12 col-sm-12">
											<div class="x_panel">
												<div class="x_title">
													&nbsp;Faculty details<font size="2px"> </font>
													<ul class="nav navbar-right panel_toolbox">
														<li><a class="collapse-link"><i
																class="fa fa-chevron-up"></i></a></li>
														<li><a class="close-link"><i class="fa fa-close"></i></a></li>
													</ul>
													<div class="clearfix"></div>
												</div>
												<div class="x_content">

													<%-- <form:form action="knowMyFaculty" id="" method="post"
											modelAttribute="facultyDetails" enctype="multipart/form-data"> --%>

													<%-- <form:input path="courseId" type="hidden" /> --%>
													<%-- <form:input path="id" type="hidden" /> --%>

													<div class="row">
														<div class="col-sm-12">
															<div class="form-group" id="imagePath">
																<form:label path="imagePath" for="imagePath"></form:label>
																<img src="${imagePath}" alt="image"
																	style="height: 220px; width: 220px;">
															</div>
														</div>
														<br>

														<div class="col-md-4 col-sm-6 col-xs-12 column">
															<div class="form-group">
																<form:label path="firstName" for="firstName">First Name :</form:label>
																${facultyDetails.firstName }
															</div>
														</div>
														<div class="col-md-4 col-sm-6 col-xs-12 column">
															<div class="form-group">
																<form:label path="lastName" for="lastName">Last Name:</form:label>
																${facultyDetails.lastName }
															</div>
														</div>
														<div class="col-md-4 col-sm-6 col-xs-12 column">
															<div class="form-group">
																<form:label path="email" for="email">Email ID :</form:label>
																${facultyDetails.email }
															</div>
														</div>
													</div>

													<div class="row">
														<div class="col-md-4 col-sm-6 col-xs-12 column">
															<div class="form-group">
																<form:label path="designation" for="designation">Designation :</form:label>
																${facultyDetails.designation}
															</div>
														</div>
														<div class="col-md-4 col-sm-6 col-xs-12 column">
															<div class="form-group">
																<form:label path="dob" for="dob">DOB :</form:label>
																${facultyDetails.dob}
															</div>
														</div>
														<div class="col-md-4 col-sm-6 col-xs-12 column">
															<div class="form-group">
																<form:label path="experience" for="experience">Experience :</form:label>
																${facultyDetails.experience } Years
															</div>
														</div>
														<%-- <div class="col-md-4 col-sm-6 col-xs-12 column">
							<div class="form-group">
								<form:label path="overview" for="overview">Experience / Overview :</form:label>
								${facultyDetails.overview}
							</div>
					  </div> --%>
													</div>

													<div class="row">
														<div class="col-md-4 col-sm-6 col-xs-12 column">
															<div class="form-group">
																<form:label path="age" for="age">Age :</form:label>
																${facultyDetails.age}
															</div>
														</div>
														<div class="col-md-4 col-sm-6 col-xs-12 column">
															<div class="form-group">
																<form:label path="mobile" for="mobile">Contact No. :</form:label>
																${facultyDetails.mobile}
															</div>
														</div>



													</div>




													<div class="row">
														<div class="col-sm-12 column">
															<form:label path="overview" for="overview">
																<a data-toggle="collapse" href="#overview"
																	aria-expanded="false" aria-controls="overview">
																	Experience / Overview : (Expand/Collapse) </a>
															</form:label>
															<div id="overview" class="collapse"
																style="border-style: solid; border-width: 1px; padding: 10px; margin-top: 10px">${facultyDetails.overview}</div>
														</div>
													</div>
													<br>


													<button id="cancel" class="btn btn-danger"
														formaction="homepage" formnovalidate="formnovalidate">BACK</button>

												</div>
											</div>
										</div>
									</div>
								</c:when>
							</c:choose>

							<jsp:include page="../common/paginate.jsp">
								<jsp:param name="baseUrl" value="evaluateByStudent" />
							</jsp:include>

						</div>



					</div>

				</div>
				<!-- /page content: END -->


				<jsp:include page="../common/footer.jsp" />
			</form:form>

		</div>
	</div>


	<script>
		$(document)
				.ready(
						function() {

							$('#reset').on(
									'click',
									function() {
										$('#assid').empty();
										var optionsAsString = "";

										optionsAsString = "<option value=''>"
												+ "Select Assignment"
												+ "</option>";
										$('#assid').append(optionsAsString);

										$("#courseId").each(function() {
											this.selectedIndex = 0
										});

									});

							$("#courseId")
									.on(
											'change',
											function() {
												var courseid = $(this).val();
												if (courseid) {
													$
															.ajax({
																type : 'GET',
																url : '${pageContext.request.contextPath}/getAssigmentByCourse?'
																		+ 'courseId='
																		+ courseid,
																success : function(
																		data) {
																	var json = JSON
																			.parse(data);
																	var optionsAsString = "";

																	$('#assid')
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

																	$('#assid')
																			.append(
																					optionsAsString);

																}
															});
												} else {
													alert('Error no course');
												}
											});

						});
	</script>


</body>
</html>
