<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>







<jsp:include page="../common/newDashboardHeader.jsp" /> 

<div class="d-flex dataTableBottom" id="facultyAssignmentPage">
<jsp:include page="../common/newLeftNavBarFaculty.jsp"/>
<jsp:include page="../common/newLeftSidebarFaculty.jsp"/>
<jsp:include page="../common/rightSidebarFaculty.jsp"/>

<!-- DASHBOARD BODY STARTS HERE -->

<div class="container-fluid m-0 p-0 dashboardWraper">

	<jsp:include page="../common/newTopHeaderFaculty.jsp" />
     
     <div class="container mt-5">
                <div class="row">
            <!-- SEMESTER CONTENT -->    
                    <div class="col-lg-9 col-md-9 col-sm-12 dashboardBody ml-auto mr-auto">
                        
                      
                   




			<!-- page content: START -->
						<nav aria-label="breadcrumb">
                            <ol class="breadcrumb">
                                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/homepage">Dashboard</a></li>
                                <li class="breadcrumb-item" aria-current="page"><c:out value="${Program_Name}" /></li>
                                <sec:authorize access="hasRole('ROLE_STUDENT')">
								<c:out value="${AcadSession}" />
							</sec:authorize>
                                <li class="breadcrumb-item active" aria-current="page"> Search Assignment To Evaluate</li>
                            </ol>
                        </nav>
                        
						<jsp:include page="../common/alert.jsp" />
						<!-- Input Form Panel -->
						<div class="card bg-white border">
							<div class="card-body">
									<div class="text-center border-bottom pb-2">
										<h5>Search Assignment To Evaluate</h5>
									</div>

										<form:form action="evaluateByStudentGroup" method="post"
											modelAttribute="assignment">
											<fieldset>
											<div class="row mt-3">
												<div class="col-md-4 col-sm-6">
													<div class="form-group">
														<form:label path="courseId" for="courseId"><strong>Course</strong></form:label>
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

												<div class="col-md-4 col-sm-6">
													<div class="form-group">
														<form:label path="id" for="id"><strong>Assignment</strong></form:label>
														<form:select id="assid" path="id" class="form-control">
															<form:option value="">Select Assignment</form:option>

															<c:forEach var="preAssigment" items="${preAssigments}"
																varStatus="status">
																<form:option value="${preAssigment.id}">${preAssigment.assignmentName}</form:option>
															</c:forEach>

														</form:select>
													</div>
												</div>
												<div class="col-md-4 col-sm-6">
													<div class="form-group">
														<form:label path="submissionStatus" for="submissionStatus"><strong>Submission Status</strong></form:label>
														<form:select id="submissionStatus" path="submissionStatus"
															class="form-control">
															<form:option value="">Select Submission Status</form:option>
															<form:option value="Y">Submitted</form:option>
															<form:option value="N">Not Submitted</form:option>
															<form:option value="">All</form:option>
														</form:select>
													</div>
												</div>
                                                


												<div class="text-center col-12">
													<div class="form-group">
														<button id="submit" name="submit"
															class="btn btn-large btn-success">Search</button>
														<input id="reset" type="reset" class="btn btn-dark">
														
													</div>
												</div>
												</div>
											</fieldset>
										</form:form>
							</div>
						</div>

						<c:choose>
							<c:when test="${groupList.size() > 0}">
								<!-- Results Panel -->
								<div class="card bg-white border">
									<div class="card-body">
											<div class="text-center">
												<h5 class="border-bottom pb-2">Assignments | ${fn:length(groupList)} Records Found</h5>
												<div class="col-12">
													<div class="form-group">
														<a href="gradeCenterForm"><i
															class="btn btn-large btn-primary" style="float: left;">Go
																to Grade Center</i></a>
													</div>
												</div>
											</div>
											
											<div class="x_content">
												<div class="table-responsive">
													<table class="table table-striped table-hover"
														style="font-size: 12px">
														<thead>
															<tr>
																<th>Sr. No.</th>

																<th>Faculty ID</th>
																<th>Group Name</th>
																<th>Assignment Name</th>


																<!-- <th>Evaluation Status</th> -->
																<th>Save Score</th>
																<th>Save Remarks</th>
																<th>Save Low Score Reason</th>
																<th>Start Date</th>

																<th>End Date</th>
																<th>Submission Date</th>
																<th>Student Submitted File</th>
																<th>Action</th>

															</tr>
														</thead>
														<tbody>


															<c:forEach var="grp" items="${groupList}"
																varStatus="status">

																<tr>

																	<td><c:out value="${status.count}" /></td>

																	<%-- <td><c:out value="${grp.evaluatedBy}" /></td> --%>
																	<%--                                                              <td><c:out value="${assignment.course.courseName}" /></td> --%>
																	<td><c:out value="${grp.createdBy}" /></td>
																	<td><c:out value="${grp.groupName}" /></td>
																	<td><c:out value="${grp.assignmentName}" /></td>


																	<%-- 	<td><a
																		href="downloadFile?filePath=${grp.studentFilePath}">Download
																			Answer File</a></td> --%>
																	<%-- <td><c:out value="${assignment.evaluationStatus}" /></td> --%>
																	<td>
																		<%-- <c:url value="#" var="editUrl">
																				<c:param name="id" value="${grp.id}" />
																			</c:url>  --%> <a href="#" class="editable"
																		id="score" data-type="text"
																		data-pk="${grp.assignmentId}"
																		data-url="saveGroupAssignmentScore?groupId=${grp.groupId}"
																		data-title="Enter Score">${grp.score}</a>
																	</td>

																	<td><a href="#" class="editable" id="remarks"
																		data-type="textarea" data-pk="${grp.groupId}"
																		data-url="saveGroupAssignmentRemarks"
																		data-title="Enter Remarks">${grp.remarks}</a></td>

																	<td><a href="#" class="editable" id="remarks"
																		data-type="select" data-pk="${grp.groupId}"
																		data-source="[{value: 'Copy Case-Internet', text: 'Copy Case-Internet'},{value: 'Copy Case-Other Student', text: 'Copy Case-Other Student'},{value: 'Wrong Answers', text: 'Wrong Answers'},{value: 'Other subject Assignment', text: 'Other subject Assignment'},{value: 'Scanned/Handwritten Assignment', text: 'Scanned/Handwritten Assignment'},{value: 'Only Questions written', text: 'Only Questions written'},{value: 'Question Paper Uploaded', text: 'Question Paper Uploaded'},{value: 'Blank Assignment', text: 'Blank Assignment'},{value: 'Corrupt file uploaded', text: 'Corrupt file uploaded'}]"
																		data-url="saveGroupLowScoreReason"
																		data-title="Select Low Marks Reason">${grp.lowScoreReason}</a>
																	</td>

																	<td><c:out value="${grp.startDate}" /></td>




																	<td><c:out value="${grp.endDate}" /></td>
																	<td><c:out value="${grp.submissionDate}" /></td>
																	<td><c:if test="${grp.submissionStatus eq 'Y' }">
																			<a href="downloadFile?saGId=${grp.studentId}">Download
																				Answer File</a>
																		</c:if> <c:if test="${grp.submissionStatus eq 'N' }">
																		Not submitted

																		</c:if></td>

																	<td><c:url value="viewGroupStudents"
																			var="viewStudentsurl">
																			<c:param name="id" value="${grp.id}" />
																		</c:url> <a href="${viewStudentsurl}" title="Group Members"><i
																			class="fa fa-users"></i></a>&nbsp;</td>


																	<%--                                                              <td><c:url --%>
																	<%--                                                                          value="updateStudentAssignmentForm?courseId=${courseId}" --%>
																	<%--                                                                          var="editurl"> --%>
																	<%--                                                                          <c:param name="id" value="${assignment.id}" /> --%>
																	<%--                                                                          <c:param name="username" value="${assignment.username}" /> --%>
																	<%--                                                                          <c:param name="assignmentId" value="${assignment.id}" /> --%>
																	<%--                                                                    </c:url> <a href="${editurl}" title="Edit"><i --%>
																	<!--                                                                          class="fa fa-pencil-square-o fa-lg"></i></a>&nbsp;</td> -->
																</tr>
															</c:forEach>

														</tbody>
													</table>
												</div>

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
	<jsp:include page="../common/footer.jsp"/>
	
	
	
			<script>
				$(document)
						.ready(
								function() {

									//$('#courseId').trigger.change()
									//$('#courseId').val('...').change()
									//jQuery("#courseId").trigger("change");
									//var courseid= document.getElementById('courseId');
									//courseid.trigger('change');

									//var element = document.getElementById('courseId');
									//var event = new Event('change');
									//element.dispatchEvent(event);

									//$('#courseId').trigger.change();
									//$get('courseId')._events

									//$('#courseId').val(courseid).change();
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
														var courseid = $(this)
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

									/* $('#courseId').trigger('change');
 */
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
			