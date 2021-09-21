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
			<jsp:include page="../common/topHeader.jsp">
				<jsp:param name="courseMenu" value="activeMenu" />
			</jsp:include>



			<!-- page content: START -->
			<!--  <div class="right_col" role="main"> -->
			<div class="right_col" role="main">
				<div class="dashboard_container">

					<div class="dashboard_container_spacing">

						<div class="breadcrumb">

							<c:out value="${Program_Name}" />

							<sec:authorize access="hasRole('ROLE_STUDENT')">

								<i class="fa fa-angle-right"></i>

								<c:out value="${AcadSession}" />

							</sec:authorize>

							<br> <br> <a
								href="${pageContext.request.contextPath}/homepage">Dashboard</a>
							<i class="fa fa-angle-right"></i> Search Course
						</div>
						<jsp:include page="../common/alert.jsp" />


						<!-- Results Panel -->
						<div class="row">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">

									<div class="x_title">
										<h2>Assign Weight</h2>
										<ul class="nav navbar-right panel_toolbox">
											<li><a class="collapse-link"><i
													class="fa fa-chevron-up"></i></a></li>
										</ul>
										<div class="clearfix"></div>
									</div>

									<div class="x_content">
										<div class="row page-body">
											<div class="col-sm-12 column">
												<form:form action="viewWieghtDetails" method="post"
													id="viewWieghtDetails" modelAttribute="weight"
													enctype="multipart/form-data">

													<div class="row">



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
																<form:label path="acadSession" for="acadSession">Semester <span style="color: red">*</span></form:label>
																<form:select id="acadSessionId" path="acadSession"
																	class="form-control" required="required">

																	<form:option value="">Select Semester</form:option>


																	<form:option value="${weight.acadSession }">${weight.acadSession }</form:option>

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



													<hr>








												</form:form>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">
									<div class="x_title">
										<h2>Assigned Weight List | ${allCourses.size()}
											Records Found</h2>

										<ul class="nav navbar-right panel_toolbox">
											<li><a class="collapse-link"><i
													class="fa fa-chevron-up"></i></a></li>
											<li><a class="close-link"><i class="fa fa-close"></i></a></li>
										</ul>
										<div class="clearfix"></div>
									</div>
									<div class="x_content">


										<c:forEach var="course" items="${allCourses}"
											varStatus="status">
											<div class="well">
												<h4>${course.courseName}</h4>

												<c:forEach var="map" items="${weightMap}">
													<c:if test="${map.key eq course.id}">
														<%-- <h5>${map.value}</h5> --%>
														<c:forEach var="list" items="${map.value}">

															<h5>${list.weightType}=${list.weightAssigned}</h5>

														</c:forEach>



													</c:if>




												</c:forEach>

											</div>

										</c:forEach>

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
				$(document)
						.ready(
								function() {

									//$('#searchType').trigger('change');

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

								});
			</script>


		</div>
	</div>





</body>
</html>
