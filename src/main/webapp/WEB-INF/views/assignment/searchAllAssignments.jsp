<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!-- <script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script> -->
<!-- <script src="//netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js"></script>	 -->

<html lang="en">
<jsp:include page="../common/css.jsp" />

<body class="nav-md footer_fixed">

	<div class="loader"></div>


	<div class="container body">
		<div class="main_container">

			<jsp:include page="../common/leftSidebar.jsp">
				<jsp:param name="courseId" value="${courseId}" />
			</jsp:include>
			<jsp:include page="../common/topHeader.jsp" />



			<!-- page content: START -->
			<div class="right_col" role="main">

				<div class="dashboard_container">

					<div class="dashboard_container_spacing">

						<div class="breadcrumb">
							<a href="${pageContext.request.contextPath}/homepage">Dashboard</a> <i class="fa fa-angle-right"></i>
							Search InActive Assignments/Tests
						</div>
						<jsp:include page="../common/alert.jsp" />
						<!-- Input Form Panel -->
						<div class="row">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">

									<div class="x_title">
										<h2>Search InActive Assignments/Tests</h2>
										<ul class="nav navbar-right panel_toolbox">
											<li><a class="collapse-link"><i
													class="fa fa-chevron-up"></i></a></li>
										</ul>
										<div class="clearfix"></div>
									</div>

									<div class="x_content">
										<form:form action="searchAllAssignments" method="post"
											modelAttribute="assignment">
											<fieldset>


												<div class="row">
													 <div class="col-sm-4 col-md-4 col-xs-12 column">
														<div class="form-group">
															<form:label path="acadMonth" for="acadMonth">Academic Month <span style="color: red">*</span></form:label>
															<form:select id="acadMonth" path="acadMonth"
																class="form-control" required="required">
																<form:option value="">Select Academic Month</form:option>
																<c:forEach var="course" items="${acadMonths}"
																	varStatus="status">
																	<form:option value="${course}">${course}</form:option>
																</c:forEach>
															</form:select>
														</div>
													</div> 
													 <div class="col-sm-4 col-md-4 col-xs-12 column">
														<div class="form-group">
															<form:label path="acadYear" for="acadYear">Academic Year <span style="color: red">*</span></form:label>
															<form:select id="acadYear" path="acadYear"
																class="form-control" required="required">
																<form:option value="">Select Academic Year</form:option>
																<c:forEach var="course" items="${acadYears}"
																	varStatus="status">
																	<form:option value="${course}">${course}</form:option>
																</c:forEach>
															</form:select>
														</div>
													</div> 

													
													
													
													<div class="col-sm-4 col-md-4 col-xs-12 column">
														<div class="form-group">
															<form:label path="id" for="id">Assignments</form:label>
															<form:select id="assignmentId" path="id"
																class="form-control">
																<c:forEach var="assignment" items="${assignmentList}"
																	varStatus="status">
																	<form:option value="${assignment.id}">${assignment.assignmentName}</form:option>
																</c:forEach>
															</form:select>
														</div>
													</div>




													<div class="col-sm-12 column">
														<div class="form-group">
															<button id="submit" name="submit" 
																class="btn btn-large btn-primary">Search</button>
															<input id="reset" type="reset" class="btn btn-danger">

														
														</div>
													</div>
												</div>
											</fieldset>
										</form:form>
									</div>
								</div>
							</div>
						</div>

						<!-- Results Panel -->

						<c:choose>
							<c:when test="${allAssignments.size() > 0}">
								<div class="row">
									<div class="col-xs-12 col-sm-12">
										<div class="x_panel">
											<div class="x_title">
												<h5>
													Assignment Submissions<font size="2px"> |
														${allAssignments.size()} Records Found &nbsp; </font>
												</h5>
												
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
																<th>Assignment Name</th>
																<th>Assignment File</th>
																<th>Preview</th>
															
																<th>Student Submitted File</th>
																<th>Evaluation Status</th>
																<th>Submission Status</th>


																<th>Score</th>
																<th>Remarks</th>
																<th>Low Score Reason</th>
																
															</tr>
														</thead>
														<tbody>

														


															<c:forEach var="assignment" items="${allAssignments}"
																varStatus="status">
																<tr>
																	<td><c:out value="${status.count}" /></td>
																	<td><c:out value="${assignment.username}" /></td>
																	<td><c:out value="${assignment.evaluatedBy}" /></td>
																	<td><c:out value="${assignment.assignmentName}" /></td>
																	<td><a
																		href="downloadFile?filePath=${assignment.filePath}">Download</a>
																	</td>
																	<td><a
																		href="downloadFile?filePath=${assignment.filePath}"
																		target="_blank">Preview</a></td>
																
																	<td><a
																		href="downloadFile?filePath=${assignment.studentFilePath}">Download
																			Answer File</a></td>
																	<td><c:out value="${assignment.evaluationStatus}" /></td>
																	<td><c:out value="${assignment.submissionStatus}" /></td>
																	<td><c:out value="${assignment.score}" /></td>
																	
																	<td><c:out value="${assignment.remarks}" /></td>
																	<td><c:out value="${assignment.lowScoreReason}" /></td>
																	
																	





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
				<!-- <script>
				$("#submit")
						.on(
								'click',
								function() {
									var assignmentId;
									$("#assignmentId").on('change',
											function(){
										 assignmentId = this.val();
									});
									
									//alert("Onchange Function called!");
									var selectedValue = $(this).val();
									window.location = '${pageContext.request.contextPath}/searchAllAssignments?assignmentId='
											+ encodeURIComponent(selectedValue);
									return false;
								});
			</script>
 -->
		</div>
	</div>





</body>
</html>
