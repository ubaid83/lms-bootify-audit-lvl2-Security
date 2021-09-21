<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>


<jsp:include page="../common/newDashboardHeader.jsp" />

<div class="d-flex dataTableBottom paddingFixAssign" id="assignmentPage">
	<jsp:include page="../common/newLeftNavbar.jsp" />
	<jsp:include page="../common/newLeftSidebar.jsp" />
	<jsp:include page="../common/rightSidebar.jsp" />

	<div class="container-fluid m-0 p-0 dashboardWraper assignmentW">
		<jsp:include page="../common/newTopHeader.jsp" />

		<div class="container mt-5">
			<div class="row">
				<div
					class="col-lg-9 col-md-9 col-sm-12 dashboardBody ml-auto mr-auto">
					
					<jsp:include page="../common/alert.jsp" />
					
					

<!-- PAGE CONTENT -->

                        <nav aria-label="breadcrumb">
                            <ol class="breadcrumb">
                                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/homepage">Dashboard</a></li>
                                <li class="breadcrumb-item" aria-current="page"><c:out value="${Program_Name}" /> 
                                <sec:authorize access="hasRole('ROLE_STUDENT')">
								<c:out value="${AcadSession}" />
							</sec:authorize>
							</li>           
                               <li class="breadcrumb-item active" aria-current="page"> View Each Assignment</li>
                            </ol>
                        </nav>


						<div class="card bg-white border">
							<div class="card-body">

										<h5 class="text-center pb-2 border-bottom">Submit Assignment for
											${courseIdNameMap[assignment.courseId] }</h5>


										<form:form action="createAssignment" id="submitAssignment"
											method="post" modelAttribute="assignment"
											enctype="multipart/form-data">



											<form:input path="id" type="hidden" value="${assignment.id}" />
											<form:input path="courseId" type="hidden"
												value="${assignment.courseId}" />
											<div class="row">

												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<form:label path="assignmentName" for="assignmentName"><strong>Assignment Name:</strong></form:label>
														${assignment.assignmentName}
													</div>
												</div>
											</div>

											<div class="row">
												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<form:label path="startDate" for="startDate"><strong>Start Date:</strong></form:label>
														${assignment.startDate}
													</div>
												</div>

												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<form:label path="endDate" for="endDate"><strong>End Date:</strong></form:label>
														${assignment.endDate}
													</div>
												</div>

												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<form:label path="assignmentType" for="assignmentType"><strong>Assignment Type:</strong></form:label>
														${assignment.assignmentType}
													</div>
												</div>

											</div>


											<div class="row">

												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<label for="file">Assignment Question File:</label> <a
															href="downloadFile?id=${assignment.id }">Download</a>
													</div>
												</div>

												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<form:label path="maxScore" for="maxScore">Score Out of:</form:label>
														${assignment.maxScore}
													</div>
												</div>

												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<label for="file">Submission Status:</label>
														<c:if
															test="${assignmentSubmission.submissionStatus eq 'Y' }">
									Submitted
								</c:if>
														<c:if
															test="${assignmentSubmission.submissionStatus ne 'Y' }">
									Not Submitted
								</c:if>
													</div>
												</div>

											</div>

											<div class="row">
												<div class="col-sm-12 column">
													<form:label path="assignmentText" for="assignmentText">
														<a data-toggle="collapse" href="#assignmentText"
															aria-expanded="true" aria-controls="assignmentText">
															Assignment Details: (Expand/Collapse) </a>
													</form:label>
													<div id="assignmentText" class="collapse"
														style="border-style: solid; border-width: 1px; padding: 10px; margin-top: 10px">${assignment.assignmentText}</div>
												</div>
											</div>
											<br>

											<div class="row">


												<c:if
													test="${assignmentSubmission.submissionStatus eq 'Y' }">
													<div class="col-md-4 col-sm-6 col-xs-12 column">
														<div class="form-group">
															<form:label path="submissionDate" for="submissionDate">Submission Date:</form:label>
															${assignmentSubmission.submissionDate }
														</div>
													</div>
													<div class="col-md-4 col-sm-6 col-xs-12 column">
														<div class="form-group">
															<form:label path="maxScore" for="maxScore">Download Uploaded Answer File:</form:label>
															<a href="downloadFile?saId=${assignmentSubmission.id }">Download
																Submitted File</a>
														</div>
													</div>

													<sec:authorize access="hasRole('ROLE_STUDENT')">
														<div class="col-md-4 col-sm-6 col-xs-12 column">
															<div class="form-group">
																<label for="file">Upload New Answer File:</label> <input
																	id="file" name="file" type="file" class="form-control" />
															</div>
															<div id=fileSize></div>
														</div>
													</sec:authorize>
												</c:if>

												<sec:authorize access="hasRole('ROLE_STUDENT')">
													<c:if
														test="${assignmentSubmission.submissionStatus ne 'Y' }">

														<div class="col-md-4 col-sm-6 col-xs-12 column">
															<div class="form-group">
																<label for="file">Select Assignment Answer File:</label>
																<input id="file" name="file" type="file"
																	class="form-control" />
															</div>
															<div id=fileSize></div>
														</div>
													</c:if>
												</sec:authorize>

											</div>

											<div class="row">

												<div class="col-sm-8 column">
													<div class="form-group">

														<sec:authorize access="hasRole('ROLE_STUDENT')">
															<%-- <c:if
														test="${assignmentSubmission.evaluationStatus eq 'N' }">
															<button id="submit" class="btn btn-large btn-primary"
																formaction="submitAssignment" onclick="return confirm('Are you sure you want to Submit Your Assignment')">Submit Assignment</button>
																</c:if> --%>

															<c:choose>
																<c:when test="${isSubmittedByOneInGroup eq true}">
																	<c:choose>
																		<c:when test="${isSubmitter eq true}">
																			<c:if
																				test="${assignmentSubmission.evaluationStatus eq 'N' }">
																				<c:if
																					test="${assignmentSubmission.submissionStatus eq 'N' }">
																					<button id="submit"
																						class="btn btn-large btn-primary"
																						formaction="submitAssignmentByOneInGroup?id=${assignmentSubmission.assignmentId}"
																						onclick="return confirm('Are you sure you want to Submit Your Assignment')">Submit
																						Assignment</button>
																				</c:if>

																				<c:if
																					test="${assignmentSubmission.submissionStatus eq 'Y' }">
																					<button id="submit"
																						class="btn btn-large btn-primary"
																						onclick="return confirm('Are you sure you want to Submit Your Assignment')"
																						formaction="submitAssignmentByOneInGroup?id=${assignmentSubmission.assignmentId}">Resubmit
																						Assignment</button>
																				</c:if>

																				<c:if
																					test="${assignmentSubmission.evaluationStatus eq 'Y' }">
																					<button id="submit"
																						class="btn btn-large btn-primary"
																						formaction="submitAssignment" disabled="disabled">Cannot
																						Submit As already Evaluated</button>
																				</c:if>
																				<%-- 	<c:if
																	test="${assignmentSubmission.submissionStatus eq 'Y' && showCheckForPlagiarism eq true}">
																	<a
																		href="checkForPlagiarism?saId=${assignmentSubmission.id}"><i
																		class="btn btn-large btn-primary">Check for
																			Plagiarism</i></a>
																</c:if> --%>
																				<!-- <button id="submit" class="btn btn-large btn-primary"
																	formaction="submitAssignment"
																	onclick="return confirm('Are you sure you want to Submit Your Assignment')">Submit
																	Assignment</button> -->
																			</c:if>
																		</c:when>
																		<c:otherwise>

																			<button id="submit" class="btn btn-large btn-primary"
																				formaction="submitAssignment" disabled="disabled">Cannot
																				Submit</button>

																		</c:otherwise>
																	</c:choose>
																</c:when>
																<c:otherwise>
																	<c:if
																		test="${assignmentSubmission.evaluationStatus eq 'N' }">
																		<c:if
																			test="${assignmentSubmission.submissionStatus eq 'N' }">
																			<button id="submit" class="btn btn-large btn-primary"
																				formaction="submitAssignment"
																				onclick="return confirm('Are you sure you want to Submit Your Assignment')">Submit
																				Assignment</button>
																		</c:if>

																		<c:if
																			test="${assignmentSubmission.submissionStatus eq 'Y' }">
																			<button id="submit" class="btn btn-large btn-primary"
																				onclick="return confirm('Are you sure you want to Submit Your Assignment')"
																				formaction="submitAssignment">Resubmit
																				Assignment</button>
																		</c:if>
																	</c:if>

																	<c:if
																		test="${assignmentSubmission.evaluationStatus eq 'Y' }">
																		<button id="submit" class="btn btn-large btn-primary"
																			formaction="submitAssignment" disabled="disabled">Cannot
																			Submit As already Evaluated</button>
																	</c:if>
																</c:otherwise>
															</c:choose>


														</sec:authorize>
														<button id="cancel" class="btn btn-danger" type="button"
															formaction="homepage" formnovalidate="formnovalidate"
															onclick="history.go(-1);">Back</button>
													</div>
												</div>
											</div>



										</form:form>

							</div>
						</div>


						<c:if test="${studentAssignmentListForGroup.size() > 0 }">
							<div class="card bg-white border">
								<div class="card-body">


										<div class="x_content">
											<form:form action="viewThisAssignment" method="post"
												modelAttribute="assignment">


												<div class="row">
													<div class="col-12">

														<div class="x_title">
															<h5 class="text-center border-bottom pb-2">
																Assignments<font size="2px"> |
																	${studentAssignmentListForGroup.size()} Records Found
																	&nbsp; </font>
															</h5>
														</div>
															<div class="table-responsive testAssignTable">
																<table class="table table-striped table-hover">
																	<thead>
																		<tr>
																			<th>Sr. No.</th>
																			<th>Name</th>
																			<th>UserName</th>
																			<th>Name</th>


																			<th>Start Date</th>
																			<th>End Date</th>
																			<th>Submission Date</th>
																			<th>Assignment File</th>
																			<th>Submitted File</th>

																			<th>Submission Status</th>
																			<th>Evaluation Status</th>




																		</tr>
																	</thead>
																	<tbody>

																		<c:forEach var="assignment"
																			items="${studentAssignmentListForGroup}"
																			varStatus="status">
																			<tr>
																				<td><c:out value="${status.count}" /></td>
																				<td><c:out value="${assignment.assignmentName}" /></td>
																				<td><c:out value="${assignment.username}" /></td>
																				<td><c:out value="${assignment.studentName}" /></td>

																				<%-- <td><c:out value="${assignment.createdBy}" /></td> --%>

																				<td><c:out
																						value="${fn:replace(assignment.startDate,'T', ' ')}"></c:out></td>
																				<td><c:out
																						value="${fn:replace(assignment.endDate, 
                                'T', ' ')}"></c:out></td>
																				<td><c:out
																						value="${fn:replace(assignment.submissionDate, 
                                'T', ' ')}"></c:out></td>

																				<td><c:if test="${assignment.filePath ne null}">
																						<a
																							href="downloadFile?id=${assignment.assignmentId}">Download</a>
																					</c:if> <c:if test="${assignment.filePath eq null}">No File</c:if>
																				</td>
																				<td><c:if
																						test="${assignment.studentFilePath ne null}">
																						<a href="downloadFile?saId=${assignment.id }">Download
																							Answer File</a>
																					</c:if> <c:if test="${assignment.studentFilePath eq null}">No File</c:if></td>

																				<td><c:out
																						value="${assignment.submissionStatus}" /></td>
																				<td><c:out
																						value="${assignment.evaluationStatus}" /></td>


																			</tr>
																		</c:forEach>


																	</tbody>
																</table>
															</div>
													</div>
												</div>




											</form:form>
										</div>
								</div>
							</div>

						</c:if>

			<!-- /page content -->
					
					
					
					
					
					
					
					
					</div>


				<!-- SIDEBAR START -->
				<jsp:include page="../common/newSidebar.jsp" />
				<!-- SIDEBAR END -->
				<jsp:include page="../common/footer.jsp" />
				
				
				
				



			<script type="text/javascript">
				$('#file').bind('change', function() {
					// alert('This file size is: ' + this.files[0].size/1024/1024 + "MB");
					var fileSize = this.files[0].size / 1024 / 1024 + " MB";
					$('#fileSize').html("File Size:" + (fileSize));
				});
			</script>

			<script type="text/javascript">
				$(document).ready(function() {
					var clicked = false;
					$('#submit').click(function(e) {
						console.log('inside click event');
						if (clicked === false) {
							console.log('inside click false');
							clicked = true;
						} else {
							console.log('inside click true');
							e.preventDefault();
						}
					});
				});
			</script>

