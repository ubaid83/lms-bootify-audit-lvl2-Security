<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<jsp:include page="../common/sidebar.jsp">
	<jsp:param name="courseId" value="${courseId}" />
</jsp:include>

<html lang="en">
<jsp:include page="../common/css.jsp" />

<body class="nav-md footer_fixed">

	<div class="loader"></div>


	<div class="container body">
		<div class="main_container">


			  <jsp:include page="../common/leftSidebar.jsp" />
             <jsp:include page="../common/topHeader.jsp" />
			


			<!-- page content: START -->
			

						<!-- Input Form Panel -->
						<div class="right_col" role="main">

							<div class="dashboard_container">

								<div class="dashboard_container_spacing">

									<div class="breadcrumb">
										<a href="${pageContext.request.contextPath}/homepage">Dashboard</a> <i
											class="fa fa-angle-right"></i> Late Submission assignment
									</div>
									<jsp:include page="../common/alert.jsp" />

									<div class="row">
										<div class="col-xs-12 col-sm-12">
											<form:form action="nonSubmittedStudents" method="post"
												modelAttribute="assignment">
												<div class="x_panel">
													<div class="x_title">
														<h2>Search Assignments</h2>
														<ul class="nav navbar-right panel_toolbox">
															<li><a href="#"><span>View All</span></a></li>
															<li><a class="collapse-link"><i
																	class="fa fa-chevron-up"></i></a></li>
															<li><a class="close-link"><i class="fa fa-close"></i></a></li>
														</ul>
														<div class="clearfix"></div>
													</div>
													<div class="x_content">
														<form>
															<div class="row">
																<div class="col-md-4 col-sm-6 col-xs-12">
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
																<div class="col-md-4 col-sm-6 col-xs-12">
																	<div class="form-group">

																		<form:label path="id" for="id">Assignment</form:label>
																		<form:select id="assid" path="id" class="form-control">
																			<form:option value="">Select Assignment</form:option>

																			<c:forEach var="preAssigment"
																				items="${preAssigments}" varStatus="status">
																				<form:option value="${preAssigment.id}">${preAssigment.assignmentName}</form:option>
																			</c:forEach>

																		</form:select>
																	</div>
																</div>
															</div>
															<div class="clear"></div>
															<div class="form-group">
																<button id="submit" name="submit" type="button"
																	class="btn btn-primary">Search</button>
																<button id="reset" type="reset" class="btn btn-primary">Reset</button>
																<button id="cancel" name="cancel" type="button"
																	formnovalidate="formnovalidate" class="btn btn-primary">Cancel</button>


															</div>
														</form>
													</div>
												</div>
											</form:form>
										</div>
									</div>

									<c:choose>
										<c:when test="${assignmentsList.size() > 0}">
											<div class="row">
												<div class="col-xs-12 col-sm-12">
													<div class="x_panel">
														<div class="x_title">
															<h2>
																Students who Failed to Submit the Assignment before the
																End Date <font size="2px">
																	(${assignmentsList.size()} Records Found) &nbsp; </font>
															</h2>
															<ul class="nav navbar-right panel_toolbox">
																<li><a href="#"><span>View All</span></a></li>
																<li><a class="collapse-link"><i
																		class="fa fa-chevron-up"></i></a></li>
																<li><a class="close-link"><i
																		class="fa fa-close"></i></a></li>
															</ul>
															<div class="clearfix"></div>
														</div>
														<div class="x_content">
															<div class="table-responsive">
																<table class="table table-hover">
																	<thead>
																		<tr>
																			<th>Sr. No.</th>
																			<th>Student User Name</th>
																			<th>Faculty Id</th>
																			<th>Assignment Name</th>
																			<th>Assignment Details</th>
																			<th>Submission Status</th>
																			<th>Evalution Status</th>
																			<th>Start Date</th>
																			<th>End Date</th>
																			<th></th>

																		</tr>
																	</thead>
																	<tbody>


																		<c:forEach var="assignment" items="${assignmentsList}"
																			varStatus="status">
																			<tr>
																				<td><c:out value="${status.count}" /></td>
																				<td><c:out value="${assignment.username}" /></td>
																				<td><c:out value="${assignment.evaluatedBy}" /></td>

																				<td><c:out value="${assignment.groupName}" /></td>
																				<td><c:out value="${assignment.assignmentName}" /></td>

																				<td><a href="#"
																					onClick="showModal('${assignment.id}', '${assignment.assignmentName}');">Assignment
																						Details</a></td>

																				<td><c:out
																						value="${assignment.submissionStatus}" /></td>
																				<td><c:out
																						value="${assignment.evaluationStatus}" /></td>
																				<td><a href="#" id="date"
																					data-type="datetime-local"
																					data-pk="${assignment.id}" data-url="saveStartDate"
																					data-title="Start Date">${assignment.startDate}</a></td>




																				<td><a href="#" id="date"
																					data-type="datetime-local"
																					data-pk="${assignment.id}" data-url="saveEndDate"
																					data-title="End Date">${assignment.endDate}</a></td>

																				<td><c:url value="sendReminder"
																						var="reminderUrl">
																						<c:param name="st_username"
																							value="${assignment.username}" />
																						<c:param name="assgn_name"
																							value="${assignment.assignmentName} " />
																					</c:url> <a href="${reminderUrl}" id="reminderId"
																					title="Reminder"><i
																						class="btn btn-large btn-primary">Reminder</i></a>&nbsp;</td>


																			</tr>
																		</c:forEach>


																	</tbody>
																</table>
															</div>
														</div>
													</div>
												</div>
											</div>
										</c:when>
									</c:choose>

								</div>


							</div>

						</div>
						<!-- /page content: END -->


						<jsp:include page="../common/footer.jsp" />

					</div>
				</div>
				
				<script>
	$(document)
			.ready(
					function() {
						
						

						$('#reset')
								.on(
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


						/* 	$('#remind')
							.on(
									'click',
									function reverseGeocodeAddress() 
									{ $.ajax({ type: "POST", url: '${pageContext.request.contextPath}/sendReminder', 
									data: "", success: function() 
										{ console.log("Reminder Sent successfully !!"); } }) };); */

					
</script>
				
</body>
</html>
