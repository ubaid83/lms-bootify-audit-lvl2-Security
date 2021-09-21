<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>





<jsp:include page="../common/newDashboardHeader.jsp" />

<div class="d-flex dataTableBottom" id="gradeListPage">
	<jsp:include page="../common/newLeftNavBarFaculty.jsp" />
	<jsp:include page="../common/newLeftSidebarFaculty.jsp" />
	<jsp:include page="../common/rightSidebarFaculty.jsp" />

	<!-- DASHBOARD BODY STARTS HERE -->

	<div class="container-fluid m-0 p-0 dashboardWraper">

		<jsp:include page="../common/newTopHeaderFaculty.jsp" />

		<div class="container mt-5">
			<div class="row">
				<!-- SEMESTER CONTENT -->
				<div
					class="col-lg-9 col-md-9 col-sm-12 dashboardBody ml-auto mr-auto">
					<!-- page content: START -->


					<nav aria-label="breadcrumb">
						<ol class="breadcrumb">
							<li class="breadcrumb-item"><a
								href="${pageContext.request.contextPath}/homepage">Dashboard</a></li>
							<li class="breadcrumb-item" aria-current="page"><c:out
									value="${Program_Name}" /></li>
							<sec:authorize access="hasRole('ROLE_STUDENT')">
								<c:out value="${AcadSession}" />
							</sec:authorize>
							<li class="breadcrumb-item active" aria-current="page">
								Search Assignments For Approval</li>
						</ol>
					</nav>
					<jsp:include page="../common/alert.jsp" />


					<!-- Input Form Panel -->
					<div class="card bg-white border">
						<div class="card-body">
							<div class="text-center border-bottom pb-2">
								<h5>Search Assignments For Approval</h5>
							</div>
							<form:form action="lateSubmissionApproval" method="post"
								modelAttribute="assignment">

								<div class="row">

									<div class="col-sm-6 col-md-6 col-sm-6 mt-3">
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

									<div class="col-sm-6 col-md-6 col-sm-6 mt-3">
										<div class="form-group">
											<form:label path="id" for="id">Assignment</form:label>
											<form:select id="assid" path="id" class="form-control">
												<form:option value="">Select Assignment</form:option>

												<c:forEach var="preAssigment" items="${preAssigments}"
													varStatus="status">
													<form:option value="${preAssigment.id}">${preAssigment.assignmentName}</form:option>
												</c:forEach>
											</form:select>
										</div>
									</div>

									<div class="col-12 text-center mt-3">
										<div class="form-group">
											<button id="submit" name="submit"
												class="btn btn-large btn-success">Search</button>
											<input id="reset" type="reset" class="btn btn-dark">

										</div>
									</div>
								</div>

							</form:form>
						</div>
					</div>

					<c:choose>
						<c:when test="${assignmentsList.size() > 0}">
							<!-- Results Panel -->
							<div class="card  bg-white border">
								<div class="card-body">

									<div class="x_title">
										<h5 class="text-center border-bottom pb-2">
											Late Submitted Students<font size="2px"> |
												${assignmentsList.size()} Records Found</font>
										</h5>
										<a href="gradeCenterForm"><i
											class="btn btn-large btn-info float-left">Go to Grade
												Center</i></a>
										<a
											href="downloadAllFileForLateSubmitted?assignmentId=${assignment.id}"><i
											class="btn btn-large btn-danger ml-2">Download all Files</i></a>


									</div>

									<div class="table-responsive testAssignTable">
										<table class="table table-hover">
											<thead>
												<tr>
													<th>Sr. No.</th>
													<th>Student User Name</th>
													<th>Roll No</th>
													<th>Faculty ID</th>
													<th>Group</th>
													<th>Assignment Name</th>
													<th>Assignment File</th>

													<th>Student Submitted File</th>
													<th>Submission Status</th>
													<th>Submission Date</th>
													<th>Evaluation Status</th>
													<th>Start Date</th>
													<th>End Date</th>
													<th>Submission Date</th>

													<th>Approval Status</th>
													<th>Save Score</th>
													<th>Save Remarks</th>
													<th>Save Low Score Reason</th>



												</tr>
											</thead>
											<tbody>

												<c:forEach var="assignment" items="${assignmentsList}"
													varStatus="status">
													<tr>
														<td><c:out value="${status.count}" /></td>
														<td><c:out value="${assignment.username}" /></td>
														<td><c:out value="${assignment.rollNo}" /></td>
														<td><c:out value="${assignment.evaluatedBy}" /></td>
														<%-- 											<td><c:out value="${assignment.course.courseName}" /></td> --%>
														<td><c:out value="${assignment.groupName}" /></td>
														<td><c:out value="${assignment.assignmentName}" /></td>
														<td><a
															href="downloadFile?id=${assignment.assignmentId}">Download</a>
														</td>

														<td><a href="downloadFile?saId=${assignment.id}">Download
																Answer File</a></td>
														<td><c:out value="${assignment.submissionStatus}" /></td>
														<td><c:out value="${assignment.submissionDate}" /></td>
														<td><c:out value="${assignment.evaluationStatus}" /></td>
														<td><a href="#" id="date" data-type="datetime-local"
															data-pk="${assignment.id}" data-url="saveStartDate"
															data-title="Start Date">${assignment.startDate}</a></td>




														<td><a href="#" id="date" data-type="datetime-local"
															data-pk="${assignment.id}" data-url="saveEndDate"
															data-title="End Date">${assignment.endDate}</a></td>


														<td><a href="#" id="date" data-type="datetime-local"
															data-pk="${assignment.id}" data-url="saveSubmissionDate"
															data-title="Submission Date">${assignment.submissionDate}</a></td>

														<td><a href="#" class="editable" id="remarks"
															data-type="select" data-pk="${assignment.id}"
															data-source="[{value: 'Approve', text: 'Approve'},{value: 'Reject', text: 'Reject'}]"
															data-url="saveApprovalStatus"
															data-title="Select Approval Status">${assignment.approvalStatus}</a>
														</td>

														<td><a href="#" class="editable" id="score"
															data-type="text" data-pk="${assignment.id}"
															data-url="saveAssignmentScore" data-title="Enter Score">${assignment.score}</a></td>

														<td><a href="#" class="editable" id="remarks"
															data-type="textarea" data-pk="${assignment.id}"
															data-url="saveAssignmentRemarks"
															data-title="Enter Remarks">${assignment.remarks}</a></td>

														<td><a href="#" class="editable" id="remarks"
															data-type="select" data-pk="${assignment.id}"
															data-source="[{value: 'Late Submission', text: 'Late Submission'},{value: 'Copy Case-Internet', text: 'Copy Case-Internet'},{value: 'Copy Case-Other Student', text: 'Copy Case-Other Student'},{value: 'Wrong Answers', text: 'Wrong Answers'},{value: 'Other subject Assignment', text: 'Other subject Assignment'},{value: 'Scanned/Handwritten Assignment', text: 'Scanned/Handwritten Assignment'},{value: 'Only Questions written', text: 'Only Questions written'},{value: 'Question Paper Uploaded', text: 'Question Paper Uploaded'},{value: 'Blank Assignment', text: 'Blank Assignment'},{value: 'Corrupt file uploaded', text: 'Corrupt file uploaded'}]"
															data-url="saveLowScoreReason"
															data-title="Select Low Marks Reason">${assignment.lowScoreReason}</a>
														</td>




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
						</c:when>
					</c:choose>
					<jsp:include page="../common/paginate.jsp">
						<jsp:param name="baseUrl" value="evaluateByStudent" />
					</jsp:include>

					<!-- /page content: END -->



				</div>

				<!-- SIDEBAR START -->
				<jsp:include page="../common/newSidebar.jsp" />
				<!-- SIDEBAR END -->
				<jsp:include page="../common/footer.jsp" />







				
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
																	this).val();
															if (courseid) {
																$
																		.ajax({
																			type : 'GET',
																			url : '${pageContext.request.contextPath}/getAssigmentByCourseForLateSubmitted?'
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
																//alert('Error no course');
															}
														});

									});
				</script>

				<script>
					$.fn.editable.defaults.mode = 'inline';
					$('.editable').each(function() {
						$(this).editable({
							success : function(response, newValue) {
								obj = JSON.parse(response);
								if (obj.status == 'error') {
									return obj.msg; // msg will be shown in editable
									// form
								}
							}
						});
					});
				</script>
				