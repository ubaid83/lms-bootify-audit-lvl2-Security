<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<html lang="en">
<jsp:include page="../common/css.jsp" />

<body class="nav-md footer_fixed">

	<div class="loader"></div>


	<div class="container body">
		<div class="main_container">


			<jsp:include page="../common/leftSidebar.jsp" />
			<jsp:include page="../common/topHeader.jsp" >
				<jsp:param name="Assignment" value="activeMenu"/>
			</jsp:include>	


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
						
							<a href="${pageContext.request.contextPath}/homepage">Dashboard</a> <i class="fa fa-angle-right"></i>
							Search Assignments
						</div>
						<jsp:include page="../common/alert.jsp" />

						<!-- Input Form Panel -->
						<div class="row">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">

									<div class="x_title">
										<h2>Not submitted assignements</h2>
										<ul class="nav navbar-right panel_toolbox">
											<li><a class="collapse-link"><i
													class="fa fa-chevron-up"></i></a></li>
										</ul>
										<div class="clearfix"></div>
									</div>

									<div class="x_content">
										<form:form action="nonSubmittedStudents" method="post"
											modelAttribute="assignment">




											<div class="col-md-4 col-sm-6 col-xs-12 column">
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

											<div class="col-md-4 col-sm-6 col-xs-12 column">
												<div class="form-group">
													<form:label path="id" for="id">Assignment</form:label>
													<form:select id="assid" path="id" class="form-control">
														<form:option value="">Select Assignment</form:option>

														<c:forEach var="preAssigment" items="${preAssigments}"
															varStatus="status">
															<form:option value="${preAssigment.id}">${preAssigment.assignmentName}</form:option>
														</c:forEach>
														<%--                                            <c:forEach var="assignment" items="${allAssignments}" --%>
														<%--                                                  varStatus="status"> --%>
														<%--                                                  <form:option value="${assignment.id}">${assignment.assignmentName}</form:option> --%>
														<%--                                            </c:forEach> --%>
													</form:select>
												</div>
											</div>












											<%-- <div class="col-md-4 col-sm-6 col-xs-12 column">
                              <div class="form-group">
                                    <form:label path="username" for="username">Student User Name</form:label>
                                    <form:input path="username" type="text" class="form-control" />
                              </div>
                        </div --%>


											<div class="col-sm-12 column">
												<div class="form-group">
													<button id="submit" name="submit"
														class="btn btn-large btn-primary">Search</button>
													<input id="reset" type="reset" class="btn btn-danger">
													<button id="cancel" name="cancel" class="btn btn-danger"
														formnovalidate="formnovalidate">Cancel</button>
												</div>
											</div>
										</form:form>
									</div>
								</div>
								</div>
								<c:choose>
									<c:when test="${assignmentsList.size() > 0}">
										<!-- Results Panel -->
										<div class="row">
											<div class="col-xs-12 col-sm-12">
												<div class="x_panel">
													<div class="x_title">
														<h2>Students who Failed to Submit the Assignment
															before the End Date | ${assignmentsList.size()} Records
															Found</h2>
														<ul class="nav navbar-right panel_toolbox">
															<li><a class="collapse-link"><i
																	class="fa fa-chevron-up"></i></a></li>
															<li><a class="close-link"><i class="fa fa-close"></i></a></li>
														</ul>
														<div class="clearfix"></div>
													</div>
													<div class="x_content">
														<div class="table-responsive">
															<table class="table table-striped table-hover"
																style="font-size: 12px">
																<thead>
																	<tr>
																		<th>Sr. No.</th>
																		<th>Student User Name</th>
																		<th>Faculty ID</th>
																		<th>Group</th>
																		<th>Assignment Name</th>

																		<th>Assignment Details</th>

																		<th>Submission Status</th>
																		<th>Evaluation Status</th>

																		<th>Start Date</th>
																		<th>End Date</th>







																	</tr>
																</thead>
																<tbody>

																	<c:forEach var="assignment" items="${assignmentsList}"
																		varStatus="status">
																		<tr>
																			<td><c:out value="${status.count}" /></td>
																			<td><c:out value="${assignment.username}" /></td>
																			<td><c:out value="${assignment.evaluatedBy}" /></td>
																			<%-- 											<td><c:out value="${assignment.course.courseName}" /></td> --%>
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

																			<%-- <c:param name="st_username" value="${assignment.username}"/>
												<button id="remind" name="remind"
												 
												formaction="sendReminder"
														
														class="btn btn-large btn-primary">Send Reminder</button></td> --%>








																			<%-- <c:if test="${empty assignment.id }">
						            	<form:checkbox path="assignmentsList" value="${assignment.id}"/> 
						            </c:if>
						            <c:if test="${not empty assignment.id }">
						            	<a href="#" class="editable" id="remarks"
												data-type="select" data-pk="${assignment.id}"
												data-source="[{value: 'Approve', text: 'Approve'},{value: 'Reject', text: 'Reject'}]"
												data-url="saveApprovalStatus"
												data-title="Select Approval Status">${assignment.approvalStatus}</a>
						            	
						            	
						            	<!--  <input type="checkbox" id="check1"> -->
						            	
						            	
						            	
						            	
						            	
						            	
						            	
						            	
						            	
						            	
						            	
						           
						            </c:if> --%>


																			<%-- <td><a href="#" class="editable" id="remarks"
												data-type="select" data-pk="${assignment.id}"
												data-source="[{value: 'Approve', text: 'Approve'},{value: 'Reject', text: 'Reject'}]"
												data-url="saveApprovalStatus"
												data-title="Select Approval Status">${assignment.approvalStatus}</a>
											</td> --%>


																			<%-- 											<td><c:url --%>
																			<%-- 													value="updateStudentAssignmentForm?courseId=${courseId}" --%>
																			<%-- 													var="editurl"> --%>
																			<%-- 													<c:param name="id" value="${assignment.id}" /> --%>
																			<%-- 													<c:param name="username" value="${assignment.username}" /> --%>
																			<%-- 													<c:param name="assignmentId" value="${assignment.id}" /> --%>
																			<%-- 												</c:url> <a href="${editurl}" title="Edit"><i --%>
																			<!-- 													class="fa fa-pencil-square-o fa-lg"></i></a>&nbsp;</td> -->
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

							<jsp:include page="../common/paginate.jsp">
								<jsp:param name="baseUrl" value="evaluateByStudent" />
							</jsp:include>

						</div>

					</div>

					<!-- /page content: END -->


					<jsp:include page="../common/footer.jsp" />
					<script>
						$(document)
								.ready(
										function() {

											$('#reset')
													.on(
															'click',
															function() {
																$('#assid')
																		.empty();
																var optionsAsString = "";

																optionsAsString = "<option value=''>"
																		+ "Select Assignment"
																		+ "</option>";
																$('#assid')
																		.append(
																				optionsAsString);

																$("#courseId")
																		.each(
																				function() {
																					this.selectedIndex = 0
																				});

															});

											$("#courseId")
													.on(
															'change',
															function() {
																var courseid = $(
																		this)
																		.val();
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

																					$(
																							'#assid')
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
																							'#assid')
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

				</div>
			</div>
		</div>
</body>
</html>
