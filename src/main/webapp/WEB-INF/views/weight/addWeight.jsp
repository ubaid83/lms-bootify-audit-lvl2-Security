<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<html lang="en">
<jsp:include page="../common/css.jsp" />


<script
	src="https://ajax.googleapis.com/ajax/libs/angularjs/1.6.4/angular.min.js"></script>
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
												<form:form action="addWeight" method="post" id="addWeight"
													modelAttribute="weight" enctype="multipart/form-data">

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
						<c:choose>
							<c:when test="${allCourses.size() > 0}">
								<!-- Results Panel -->
								<div class="row">
									<div class="col-xs-12 col-sm-12">
										<div class="x_panel">

											<div class="x_title">
												<h2>Assign Weight for following courses</h2>
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
																	<!-- <th>Assign Internal Weight</th>
																	<th>Assign External Weight</th> -->
																	<!-- <th>Assign Passing Internal</th> -->
																	<!-- <th>Total</th> -->
																	<!-- <th>Action</th> -->

																</tr>
															</thead>

															<tbody>
																<c:forEach var="course" items="${allCourses}"
																	varStatus="status">
																	<tr>
																		<td><c:out value="${status.count}" /></td>


																		<td><c:out value="${course.courseName}" /></td>


																		<%-- 	<td><form:input path="internal" name="internal[]"
																				class="form-control"
																				id="saveInternal${status.count}"
																				value="${course.internal}" type="number" min="0"
																				required="required" /></td>
																		<td><form:input path="external" name="external[]"
																				id="saveExternal${status.count}"
																				class="form-control" value="${course.external}"
																				type="number" min="0" required="required" /></td>
																		<td><form:input path="passInternal" name="passInternal[]"
																				id="savepassInternal${status.count}"
																				class="form-control" value="${course.passInternal}"
																				type="number" min="0" required="required" /></td>
																		<td><span id="sum${status.count}">0</span></td>

 --%>





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
								<div class="row">
									<div class="col-xs-12 col-sm-12">
										<div class="x_panel">

											<div class="x_title">
												<h2>Upload Weight</h2>
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


<label for="">Note :</label>
															<ul>
																<li>Mandatory fields : icaTotal, icaPassing, TEE,
																	Total</li>
																<li>Weigh can be assigned for your preferred weight
																	type.</li>

															</ul>


															<a target="_blank" href="downloadFile?filePath=${filePath}"><font
																color="red">Download a sample template</font></a>



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
							</c:when>
						</c:choose>

						<%-- <c:if test="${proceed}">
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


						</c:if>
 --%>


					</div>



				</div>

			</div>
			<!-- /page content: END -->


			<jsp:include page="../common/footer.jsp" />
			<script>
				/* 	$(document)
							.ready(
									function() {
										/* var times = $
										{
											allCourses.size()
										}
										;
										//alert(times);
										var internal = 0;
										var external = 0;
										var total = 0;
										for (var i = 1; i <= times; i++) {

											//var str = "filePaths";
											var id1 = "saveInternal".concat(i);
											//alert('id1 ' + id1);
											var id2 = "saveExternal".concat(i);

											//alert("ID 1 "+id);

											$('#' + id1)
													.keyup(
															function() {
																var id = $(this)
																		.attr('id');
																//alert('id '+id);
																internal = $(this)
																		.val();
																//alert('internal ' + internal);
																//alert('id2 ' + id2);
																//external = $("#"+id2).val();
																//alert('external ' + external);
																var sumId = id
																		.substr(12);
																//alert('sumId ' + sumId);
																var newsunmId = "sum"
																		.concat(sumId);
																if (!isNaN(this.value)
																		&& this.value.length != 0) {
																	total = parseInt(internal);

																}

																$('#' + newsunmId)
																		.html(total);
															});
											$('#' + id2)
													.keyup(
															function() {
																var id = $(this)
																		.attr('id');
																//alert('id '+id);
																external = $(this)
																		.val();
																//alert('external ' + external);
																var sumId = id
																		.substr(12);

																//alert('sumId ' + sumId);
																var intId = "saveInternal"
																		.concat(sumId);
																internal = $(
																		"#" + intId)
																		.val();
																//alert('internal ' + internal);
																var newsunmId = "sum"
																		.concat(sumId);
																if (!isNaN(this.value)
																		&& this.value.length != 0) {
																	total = parseInt(internal)
																			+ parseInt(external);

																}

																$('#' + newsunmId)
																		.html(total);
															});
										}
									}); 
					/* for (var i = 1; i <= times; i++) {
						//alert('inside for loop')

						//$(document).ready(function() {

							//iterate through each textboxes and add keyup
							//handler to trigger sum event
							var str1 = "saveInternal".concat(i);
							var str2 = "saveExternal".concat(i);
							alert('str1 '+str1);
							alert('str2 '+str2);
							$("#" + str1).each(function() {

								$(this).keyup(function() {
									calculateSum();
								});
							});
							$("#" + str2).each(function() {

								$(this).keyup(function() {
									calculateSum();
								});
							});

						//});
						function calculateSum() {

							var sum = 0;
							//iterate through each textboxes and add the values
							$("#" + str1).each(function() {

								//add only if the value is number
								if (!isNaN(this.value) && this.value.length != 0) {
									//sum += parseFloat(this.value);
									var internal = $("#" + str1).val();
									alert('internal ' + internal);
									var external = $("#" + str2).val();
									alert('external ' + external);
									sum = internal + external;
									alert('sum ' + sum);
								}

							});
							var str3 = "sum".concat(i);
							$("#"+str3).html(sum);
						}

					} */
			</script>
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
