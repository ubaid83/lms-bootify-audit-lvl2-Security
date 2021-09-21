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
			<jsp:include page="../common/topHeader.jsp" />



			<!-- page content: START -->
			<div class="right_col" role="main">

				<div class="dashboard_container">

					<div class="dashboard_container_spacing">

						<div class="breadcrumb">
						
							<a href="${pageContext.request.contextPath}/homepage">Dashboard</a> <i class="fa fa-angle-right"></i>
							Assignment
						</div>
						<jsp:include page="../common/alert.jsp" />

						<!-- Input Form Panel -->
						<div class="row">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">

									<div class="x_title">
										<h2>Assignment for ${assignment.course.courseName }</h2>
										<ul class="nav navbar-right panel_toolbox">
											<li><a class="collapse-link"><i
													class="fa fa-chevron-up"></i></a></li>
										</ul>
										<div class="clearfix"></div>
									</div>

									<div class="x_content">
										<form:form action="assignmentGroup" id="editAssignment"
											method="post" modelAttribute="assignment"
											enctype="multipart/form-data">

											<form:input path="courseId" type="hidden" />
											<form:input path="id" type="hidden" />
											<form:input path="acadMonth" type="hidden" />
											<form:input path="acadYear" type="hidden" />
											<div class="row">
												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<form:label path="assignmentName" for="assignmentName">Assignment Name:</form:label>
														${assignment.assignmentName }
													</div>
												</div>
											</div>

											<div class="row">
												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<form:label path="startDate" for="startDate">Start Date:</form:label>
														${assignment.startDate}
													</div>
												</div>
											<%-- 	<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<form:label path="dueDate" for="dueDate">Due Date:</form:label>
														${assignment.dueDate}
													</div>
												</div> --%>
												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<form:label path="endDate" for="endDate">End Date:</form:label>
														${assignment.endDate}
													</div>
												</div>
											</div>

											<div class="row">
												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<form:label path="allowAfterEndDate"
															for="allowAfterEndDate">Allow Submission after End date?:</form:label>
														${assignment.allowAfterEndDate}
													</div>
												</div>
												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<form:label path="sendEmailAlert" for="sendEmailAlert">Send Email Alert for New Assignment?:</form:label>
														${assignment.sendEmailAlert}
													</div>
												</div>
												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<form:label path="sendSmsAlert" for="sendSmsAlert">Send SMS Alert for New Assignment?:</form:label>
														${assignment.sendSmsAlert}
													</div>
												</div>
											</div>

											<div class="row">
												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<form:label path="showResultsToStudents"
															for="showResultsToStudents">Show Results to Students immediately?:</form:label>
														${assignment.showResultsToStudents}
													</div>
												</div>
												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<label for="file">Assignment Question File</label> <a
															href="downloadFile?id=${assignment.id}">Download</a>
													</div>
												</div>

												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<form:label path="maxScore" for="maxScore">Score Out of:</form:label>
														${assignment.maxScore}
													</div>
												</div>
											</div>

											<div class="row">

												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<form:label path="facultyId" for="facultyId">Faculty:</form:label>
														${assignment.facultyId}
													</div>
												</div>
												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<form:label path="facultyId" for="facultyId">Submit Assignment By One In a Group:</form:label>
														${assignment.submitByOneInGroup}
													</div>
												</div>
											</div>

											<div class="row">
												<div class="col-md-4 col-sm-6 col-xs-12 column">
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
												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">

														<button id="submit" class="btn btn-large btn-primary"
															formaction="createAssignmentFromGroup">Edit
															Assignment</button>
														<button id="cancel" class="btn btn-large btn-danger"
															formaction="homepage" formnovalidate="formnovalidate">Cancel</button>
													</div>
												</div>
											</div>


										</form:form>
									</div>
								</div>
							</div>
						</div>

						<!-- Results Panel -->
						<div class="row">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">
									<div class="x_title">
										<h2>Select Groups to Allocate Assignment</h2>
										<ul class="nav navbar-right panel_toolbox">
											<li><a class="collapse-link"><i
													class="fa fa-chevron-up"></i></a></li>
											<li><a class="close-link"><i class="fa fa-close"></i></a></li>
										</ul>
										<div class="clearfix"></div>
									</div>
									<div class="x_content">
										<form:form action="saveGroupAssignmentAllocation"
											id="saveGroupAssignmentAllocation" method="post"
											modelAttribute="groups">


											<form:input path="id" type="hidden" />
											<form:input path="courseId" type="hidden" />
											<form:input path="acadMonth" type="hidden" />
											<form:input path="acadYear" type="hidden" />
											<form:input path="facultyId" type="hidden" />

											<div class="table-responsive">
												<table class="table table-hover">
													<thead>
														<tr>
															<th>Sr. No.</th>
															<th>Select (<a onclick="checkAll()">All</a> | <a
																onclick="uncheckAll()">None</a>)
															</th>
															<th>Program</th>
															<th>Group Name</th>
														</tr>
													</thead>
													<tfoot>
														<tr>
															<th></th>
															<th></th>
															<th>Program</th>
															<th>Student Name</th>
														</tr>
													</tfoot>
													<tbody>

														<c:forEach var="groups" items="${groups}"
															varStatus="status">
															<tr>
																<td><c:out value="${status.count}" /></td>
																<td><c:if test="${empty groups.id }">
																		<form:checkbox path="students"
																			value="${groups.groupName}" />
																	</c:if> <c:if test="${not empty groups.id }">
						            	Assignment Allocated
						            </c:if></td>
																<td><c:out value="${student.programName}" /></td>

															</tr>
														</c:forEach>
													</tbody>
												</table>
											</div>

											<div class="col-md-4 col-sm-6 col-xs-12 column">
												<div class="form-group">

													<button id="submit" class="btn btn-large btn-primary"
														formaction="saveStudentAssignmentAllocation">Allocate
														Assignment</button>
													<button id="cancel" class="btn btn-large btn-danger"
														formaction="homepage" formnovalidate="formnovalidate">Cancel</button>
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

		</div>
	</div>





</body>
</html>
