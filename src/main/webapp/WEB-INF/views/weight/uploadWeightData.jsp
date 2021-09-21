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
<%
	String courseRecord = request.getParameter("courseRecord");
%>




<body class="nav-md footer_fixed">

	<div class="loader"></div>


	<div class="container body">
		<div class="main_container">

			<jsp:include page="../common/leftSidebar.jsp">
				<jsp:param name="courseId" value="${courseId}" />
			</jsp:include>

			<jsp:include page="../common/topHeader.jsp">
				<jsp:param value="Announcement" name="activeMenu" />
			</jsp:include>
			<%
				boolean isEdit = "true".equals((String) request
						.getAttribute("edit"));
			%>

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

							<br> <br> <a
								href="${pageContext.request.contextPath}/homepage">Dashboard</a>
							<i class="fa fa-angle-right"></i> Assign Weight
						</div>
						<jsp:include page="../common/alert.jsp" />
						<!-- Input Form Panel -->

						<c:choose>
							<c:when test="${weightList.size() > 0}">
								<!-- Results Panel -->
								<div class="row">
									<div class="col-xs-12 col-sm-12">
										<div class="x_panel">

											<div class="x_title">
												<h2>Assigned Weight for following courses</h2>
												<ul class="nav navbar-right panel_toolbox">
													<li><a class="collapse-link"><i
															class="fa fa-chevron-up"></i></a></li>
												</ul>
												<div class="clearfix"></div>
											</div>

											<div class="x_content">
												<form:form action="" id="saveInternalExternal" method="post"
													modelAttribute="weight" enctype="multipart/form-data">

													<div class="table-responsive">
														<table class="table  table-hover">
															<thead>
																<tr>
																	<th>Sr. No.</th>


																	<th>Course Name</th>
																	<th>Assign Internal Weight</th>
																	<th>Assign External Weight</th>
																	<!-- <th>Action</th> -->

																</tr>
															</thead>

															<tbody>
																<c:forEach var="weight" items="${weightList}"
																	varStatus="status">
																	<tr>
																		<td><c:out value="${status.count}" /></td>


																		<td><c:out value="${weight.courseName}" /></td>
																		<td><c:out value="${weight.internal}" /></td>
																		<td><c:out value="${weight.external}" /></td>







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
							</c:when>
						</c:choose>

						<div class="row">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">

									<div class="x_title">
										<h2>Upload Internal Weight</h2>
										<ul class="nav navbar-right panel_toolbox">
											<li><a class="collapse-link"><i
													class="fa fa-chevron-up"></i></a></li>
										</ul>
										<div class="clearfix"></div>
									</div>

									<div class="x_content">
										<form:form action="uploadWeight" id="uploadWeight"
											method="post" modelAttribute="weight"
											enctype="multipart/form-data">

											<div class="col-sm-4 column ">
												<div class="form-group">


													<label class="control-label" for="courses">Excel
														Format: </label>
													<p>cousreName | weightType | weightAssigned</p>
													<p>
														<b>Note:</b>
													<ul>
														<li><b>weightAssigned </b>Should be a valid Integer
															value</li>

													</ul>
													<br>
													<p>
														<a target="_blank" href="${filePath} download"><font
															color="red">Download a sample template</font></a>
													</p>



												</div>
											</div>
											<div class="col-sm-4 column">
												<div class="form-group">
													<label for="file">Upload Weight File(Excel):</label> <input
														id="file" name="file" type="file" class="form-control" />
												</div>
											</div>
											<div class="row">

												<div class="col-sm-12 column">
													<div class="form-group">

														<button id="submit" class="btn btn-large btn-primary"
															formaction="uploadWeight?acadYear=${acadYear}&acadSession=${acadSession}">Upload</button>

														<button id="cancel" class="btn btn-danger"
															formaction="homepage" formnovalidate="formnovalidate">Cancel</button>
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
									$(".likeClass")
											.click(
													function() {
														/* $('#likeClass').click(
																function() {
																	change(1);
																}); */
														console
																.log("called ........................................................000000.");

														var likeId = $(this)
																.attr("id");

														var courseId = likeId
																.substr(4);
														console.log(courseId);

														var str1 = "saveInternal"
																.concat(courseId);
														console.log("str1"
																+ str1);
														var internal = $(
																'#' + str1)
																.val();
														console
																.log("internal is "
																		+ internal);

														var str2 = "saveExternal"
																.concat(courseId);
														console.log("str2"
																+ str2);
														var external = $(
																'#' + str2)
																.val();
														console.log("external "
																+ external);

														/* var courseid = $
														{
															courseId
														}
														; */
														//var maxScore = ${cpWieghtage};
														//	alert(courseid);
														//alert('Course Id is '+courseid);
														//alert(score);
														//alert(remarks);
														//if ((score > 0) && (score<=maxScore)) {
														//if ((score > 0)) {
														$
																.ajax({
																	type : 'GET',
																	url : '${pageContext.request.contextPath}/saveInternalExternal?'
																			+ 'courseId='
																			+ courseId
																			+ '&internal='
																			+ internal
																			+ '&external='
																			+ external,

																	success : function(
																			data) {
																		console
																				.log("sucess messsgae e like "
																						+ likeId)

																		alert("Marks Saved!");
																		//$('.likeClass').bind('click', false);
																		//return ($(this).attr('disabled')) ? false : true;
																		$(this)
																				.removeAttr(
																						"href");

																	}

																});

													});

								});
			</script>

		</div>
	</div>






</body>



</html>
