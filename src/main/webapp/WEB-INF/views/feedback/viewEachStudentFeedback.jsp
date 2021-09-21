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

			<jsp:include page="../common/topHeader.jsp" />
			


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
							Allocate Feedback
						</div>
						<jsp:include page="../common/alert.jsp" />
						<form:form id="studentFeedbackForm" action="saveStudentFeedback" method="post" modelAttribute="feedback">

						<!-- Input Form Panel -->
						<div class="row">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">

									<div class="x_title">
										<h2>My Assignments</h2>
										<ul class="nav navbar-right panel_toolbox">
											<li><a class="collapse-link"><i
													class="fa fa-chevron-up"></i></a></li>
										</ul>
										<div class="clearfix"></div>
									</div>
									

									<div class="x_content">
										<div class="row">
											<div class="col-md-4 col-sm-6 col-xs-12 column">
												<div class="form-group">
													<label class="control-label" for="feedback">Feedback <span style="color: red">*</span></label>
													<form:select id="feedback" path="id"
														placeholder="Feedback Name" class="form-control"
														required="required">
														<form:option value="">Select Feedback</form:option>
														<form:options items="${feedbackList}"
															itemLabel="feedbackName" itemValue="id" />
													</form:select>
												</div>
											</div>
											<div class="col-md-4 col-sm-6 col-xs-12 column">
												<div class="form-group">
													<label class="control-label" for="course">Course <span style="color: red">*</span></label>
													<form:select id="course" path="courseId"
														placeholder="Course" class="form-control facultyparameter"
														required="required">
														<form:option value="">Select Course</form:option>
														<form:options items="${courseList}" itemLabel="courseName"
															itemValue="id" />
													</form:select>
												</div>
											</div>

											<div class="col-md-4 col-sm-6 col-xs-12 column">
												<div class="form-group">
													<form:label path="acadMonth" for="acadMonth">Academic Month <span style="color: red">*</span></form:label>
													<form:select id="acadMonth" path="acadMonth"
														class="form-control facultyparameter" required="required">
														<form:option value="">Select Academic Month</form:option>
														<form:options items="${acadMonths}" />
													</form:select>
												</div>
											</div>
											<div class="col-md-4 col-sm-6 col-xs-12 column">
												<div class="form-group">
													<form:label path="acadYear" for="acadYear">Academic Year <span style="color: red">*</span></form:label>
													<form:select id="acadYear" path="acadYear"
														class="form-control facultyparameter" required="required">
														<form:option value="">Select Academic Year</form:option>
														<form:options items="${acadYears}" />
													</form:select>
												</div>
											</div>
											<div class="col-md-4 col-sm-6 col-xs-12 column">
												<div class="form-group">
													<form:label path="facultyId" for="facultyId">Faculty</form:label>
													<form:select id="facultyid" path="facultyId"
														class="form-control">
														<c:if test="${empty feedback.facultyId }">
															<form:option value="">Select Faculty</form:option>
														</c:if>
														<c:if test="${not empty feedback.facultyId }">
															<form:option value="${feedback.facultyId }">${feedback.facultyId }</form:option>
														</c:if>
													</form:select>
												</div>
											</div>
											<!-- <div class="col-md-4 col-sm-6 col-xs-12 column">
								<div class="form-group">
									<form:label path="facultyId" for="faculty">Faculty</form:label>
									<select id="faculty" name="facultyId" class="form-control"
										required="required" data-value="${feedback.facultyId}">
										<option value="">Select Faculty</option>
									</select>
								</div>
							</div>-->
											<%-- <div class="col-md-4 col-sm-6 col-xs-12 column">
								<div class="form-group">
									<form:label path="startDate" for="startDate">Start Date</form:label>
									<form:input id="startDate" path="startDate"
										type="datetime-local" placeholder="Start Date"
										class="form-control"  />
								</div>
							</div>
							<div class="col-md-4 col-sm-6 col-xs-12 column">
								<div class="form-group">
									<form:label path="endDate" for="endDate">End Date</form:label>
									<form:input id="endDate" path="endDate" type="datetime-local"
										placeholder="End Date" class="form-control"  />
								</div>
							</div>
							
							<div class="clearfix" ></div>
							<div class="col-md-4 col-sm-6 col-xs-12 column">
								<div class="form-group">
									<form:label path="allowAfterDueDate" for="allowAfterDueDate">Allow to attend after due date?</form:label><br>
									<form:checkbox path="allowAfterDueDate" class="form-control" value="Y" 
									data-toggle="toggle" data-on="Yes" data-off="No" data-onstyle="success"  data-offstyle="danger" data-style="ios" 
									data-size="mini"/>

								</div>
							</div>
							<div class="col-md-4 col-sm-6 col-xs-12 column">
								<div class="form-group">
									<form:label path="mandatory" for="mandatory">Is Feedback mandatory?</form:label><br>
									<form:checkbox path="mandatory" class="form-control" value="Y" 
									data-toggle="toggle" data-on="Yes" data-off="No" data-onstyle="success"  data-offstyle="danger" data-style="ios" 
									data-size="mini"/>

								</div>
							</div> --%>
										</div>
										<div class="row">
											<div class="col-md-4 col-sm-6 col-xs-12 column">
												<div class="form-group">

													<button id="search" class="btn btn-large btn-primary"
														formaction="viewEachStudentFeedbackForm">Search</button>
													<button id="cancel" class="btn btn-danger"
														formaction="homepage" formnovalidate="formnovalidate">Cancel</button>
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>

						<!-- Results Panel -->
						<div class="row">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">
									<div class="x_title">
										<h2>Feedback Allocation Details</h2>
										<ul class="nav navbar-right panel_toolbox">
											<li><a class="collapse-link"><i
													class="fa fa-chevron-up"></i></a></li>
											<li><a class="close-link"><i class="fa fa-close"></i></a></li>
										</ul>
										<div class="clearfix"></div>
									</div>
									<div class="x_content">
										<div class="table-responsive">
											<table class="table table-hover" id="studentFeedbackTable">
												<thead>
													<tr>
														<th>Sr. No.</th>
														<th>Select To Allocate</th>
														<th>SAPID</th>
														<th>Student Name</th>
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

													<c:forEach var="student" items="${page.pageItems}"
														varStatus="status">
														<tr>
															<td><c:out value="${status.count}" /></td>
															<td><c:if test="${empty student.id }">
																	<form:checkbox path="students"
																		value="${student.username}" />
																</c:if> <c:if test="${not empty student.id }">
						            	Feedback Allocated
						            </c:if></td>
															<td><c:out value="${student.username}" /></td>
															<td><c:out
																	value="${student.firstname} ${student.lastname}" /></td>
														</tr>
													</c:forEach>
												</tbody>
											</table>
										</div>
										<div class="form-group">

											<button id="submit" class="btn btn-large btn-primary"
												formaction="saveStudentFeedback">Allocate Feedback</button>
											<button id="cancel" class="btn btn-danger"
												formaction="homepage" formnovalidate="formnovalidate">Cancel</button>
										</div>
									</div>
								</div>
							</div>
						</div>
						</form:form>

					</div>



				</div>

			</div>
			<!-- /page content: END -->


			<jsp:include page="../common/footer.jsp" />
			<script>
				$(document)
						.ready(
								function() {

									/*  $("#search")
									 .on(
									             'click',
									             function() {
									            	 alert("hi");
									            	 $
									                 .ajax({
									                       type : 'POST',
									                       url : 'http://localhost:8084/addStudentFeedbackForm',
									                       success : function(
									                                   data) {
									                       	
									                            console.log("called"+data);

									                       }
									                 });
									            	 
									             }) */

									$(".facultyparameter")
											.on(
													'change',
													function() {
														var courseid = $(
																'#course')
																.val();
														
														console.log(courseid);
														
														if (courseid
															) {
															console
																	.log("called")
															$
																	.ajax({
																		type : 'GET',
																		url : '/getFacultyByCourseForFeedback?'
																				+ 'courseId='
																				+ courseid,
																		success : function(
																				data) {

																			var json = JSON
																					.parse(data);
																			var optionsAsString = "";

																			$(
																					'#facultyid')
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
																					'#facultyid')
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
