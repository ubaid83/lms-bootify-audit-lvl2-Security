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
	boolean isEdit = "true".equals((String) request
			.getAttribute("edit"));
%>
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

								<i class="fa fa-angle-right"></i>

								<c:out value="${AcadSession}" />

							</sec:authorize>
							<br> <br> <a
								href="${pageContext.request.contextPath}/homepage">Dashboard</a>
							<i class="fa fa-angle-right"></i> Create Assignments
						</div>
						<jsp:include page="../common/alert.jsp" />

						<!-- Input Form Panel -->
						<div class="row">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">
									<div class="x_title">
										<c:if test="${showCourseHeader}">
											<h2>Create Assignment for ${assignment.course.courseName }</h2>
											<c:if test="${showWieghtageFor}">
												<div style="padding-top: 5%; color: black; font-size: 20px">
													<c:forEach var="showWieghtageForAssignment"
														items="${showWieghtageForAssignment}" varStatus="status">
														<span> || </span>
														<c:out value="${showWieghtageForAssignment.wieghtagetype}" />
														<span> - </span>
														<c:out
															value="${showWieghtageForAssignment.wieghtageassigned}%" />
													</c:forEach>
												</div>
											</c:if>


										</c:if>
										<%-- 	 <c:if test="${showWieghtageFor ne 'false'}">
                                                            <div style="color:black;font-size:20px">
                                                                        Weight Not Assigned
                                                                                                </div>
                                                            </c:if>   --%>
										<c:if test="${showCourseHeader ne 'true'}">
											<h2>Create Assignment</h2>
										</c:if>
										<ul class="nav navbar-right panel_toolbox">
											<li><a class="collapse-link"><i
													class="fa fa-chevron-up"></i></a></li>
										</ul>
										<div class="clearfix"></div>
									</div>

									<div class="x_content">
										<form:form action="createAssignment" id="createAssignment"
											method="post" modelAttribute="assignment"
											enctype="multipart/form-data">
											<fieldset>

												<form:input path="courseId" type="hidden" />
												<%
													if (isEdit) {
												%>
												<form:input type="hidden" path="id" />
												<%
													if (isEdit) {
												%>
												<form:input type="hidden" path="id" />
												<form:input type="hidden" path="acadYear" />
												<form:input type="hidden" path="acadMonth" />
												<input type="hidden"  id="plagscanRequired"
												value="${assignment.plagscanRequired}"/>
												<%
													}
												%>
												<%
													}
												%>

												<div class="row">

													<div class="col-md-4 col-sm-6 col-xs-12 column">
														<div class="form-group">
															<form:label path="assignmentName" for="assignmentName">Assignment Name <span
																	style="color: red">*</span>
															</form:label>
															<form:input path="assignmentName" type="text"
																class="form-control" required="required" />
														</div>
													</div>
												</div>

												<div class="row">
													<div class="col-md-4 col-sm-6 col-xs-12 column">
														<div class="form-group">
															<form:label path="startDate" for="startDate">Start Date <span
																	style="color: red">*</span>
															</form:label>

															<div class='input-group date' id='datetimepicker1'>
																<form:input id="startDate" path="startDate" type="text"
																	placeholder="Start Date" class="form-control"
																	required="required" readonly="true" />
																<span class="input-group-addon"><span
																	class="glyphicon glyphicon-calendar"></span> </span>
															</div>

														</div>
													</div>
													<%-- 	<div class="col-md-4 col-sm-6 col-xs-12 column">
														<div class="form-group">
															<form:label path="dueDate" for="dueDate">Due Date</form:label>
																
																<div class='input-group date' >
															<form:input id="dueDate" path="dueDate" type="text"
																placeholder="Due Date" class="form-control"
																required="required" />
															<span class="input-group-addon"><span
																class="glyphicon glyphicon-calendar"></span> </span>
														</div>
																
														</div>
													</div> --%>
													<div class="col-md-4 col-sm-6 col-xs-12 column">
														<div class="form-group">
															<form:label path="endDate" for="endDate">End Date <span
																	style="color: red">*</span>
															</form:label>
															<%-- <form:input path="endDate" type="datetime-local"
																class="form-control" value="${assignment.endDate}"
																required="required" /> --%>

															<div class='input-group date' id='datetimepicker2'>
																<form:input id="endDate" path="endDate" type="text"
																	placeholder="End Date" class="form-control"
																	required="required" readonly="true" />
																<span class="input-group-addon"><span
																	class="glyphicon glyphicon-calendar"></span> </span>
															</div>

														</div>
													</div>
												</div>

												<div class="row">
													<div class="col-md-4 col-sm-6 col-xs-12 column">
														<div class="slider round">
															<form:label path="allowAfterEndDate"
																for="allowAfterEndDate">Allow Submission after End date?</form:label>
															<br>
															<form:checkbox path="allowAfterEndDate"
																class="form-control" value="Y" data-toggle="toggle"
																data-on="Yes" data-off="No" data-onstyle="success"
																data-offstyle="danger" data-style="ios" data-size="mini" />
														</div>
													</div>
													<div class="col-md-4 col-sm-6 col-xs-12 column">
														<div class="slider round">
															<form:label path="sendEmailAlert" for="sendEmailAlert">Send Email Alert for New Assignment?</form:label>
															<br>
															<form:checkbox path="sendEmailAlert" class="form-control"
																value="Y" data-toggle="toggle" data-on="Yes"
																data-off="No" data-onstyle="success"
																data-offstyle="danger" data-style="ios" data-size="mini" />
														</div>
													</div>
													<c:if test="${sendAlertsToParents eq false}">
														<div class="col-sm-6 col-md-4 col-xs-12 column">
															<div class="slider round">
																<form:label path="sendEmailAlertToParents"
																	for="sendEmailAlertToParents">Send Email Alert To Parents?</form:label>
																<br>
																<form:checkbox path="sendEmailAlertToParents"
																	class="form-control" value="Y" data-toggle="toggle"
																	data-on="Yes" data-off="No" data-onstyle="success"
																	data-offstyle="danger" data-style="ios"
																	data-size="mini" />
															</div>
														</div>
													</c:if>
													<div class="col-md-4 col-sm-6 col-xs-12 column">
														<div class="slider round">
															<form:label path="sendSmsAlert" for="sendSmsAlert">Send SMS Alert for New Assignment?</form:label>
															<br>
															<form:checkbox path="sendSmsAlert" class="form-control"
																value="Y" data-toggle="toggle" data-on="Yes"
																data-off="No" data-onstyle="success"
																data-offstyle="danger" data-style="ios" data-size="mini" />
														</div>
													</div>
													<c:if test="${sendAlertsToParents eq false}">
														<div class="col-sm-6 col-md-4 col-xs-12 column">
															<div class="slider round">
																<form:label path="sendSmsAlertToParents"
																	for="sendSmsAlertToParents">Send SMS Alert To Parents?</form:label>
																<br>
																<form:checkbox path="sendSmsAlertToParents"
																	class="form-control" value="Y" data-toggle="toggle"
																	data-on="Yes" data-off="No" data-onstyle="success"
																	data-offstyle="danger" data-style="ios"
																	data-size="mini" />
															</div>
														</div>
													</c:if>
												</div>

												<div class="row">
													<div class="col-md-4 col-sm-6 col-xs-12 column">
														<div class="slider round">
															<form:label path="showResultsToStudents"
																for="showResultsToStudents">Show Results to Students immediately?</form:label>
															<br>
															<form:checkbox path="showResultsToStudents"
																class="form-control" value="Y" data-toggle="toggle"
																data-on="Yes" data-off="No" data-onstyle="success"
																data-offstyle="danger" data-style="ios" data-size="mini" />
														</div>
													</div>
													<div class="col-md-4 col-sm-6 col-xs-12 column">
														<div class="slider round">
															<form:label path="plagscanRequired"
																for="plagscanRequired">Is Plagiarism Check Required? <span
																	style="color: red">*</span>
															</form:label>
															<br>
															<form:radiobutton name="plagscanRequired" id="Yes"
																value="Yes" path="plagscanRequired" required="required" />
															Yes<br>
															<form:radiobutton name="plagscanRequired" id="No"
																value="No" path="plagscanRequired" />
															No<br>


															<%-- 	<form:checkbox path="plagscanRequired" onclick="clicked();"
																class="form-control" value="Y" data-toggle="toggle"
																data-on="Yes" data-off="No" data-onstyle="success"
																data-offstyle="danger" data-style="ios" data-size="mini" /> --%>
														</div>
													</div>



													<div class="col-md-4 col-sm-6 col-xs-12 column"
														style="display: none" id="runPlagiarism">
														<div class="slider round">
															<form:label path="runPlagiarism" for="runPlagiarism">Run Plagiarism while <span
																	style="color: red">*</span> :</form:label>
															<br>
															<form:radiobutton name="runPlagiarism" id="Submission"
																value="Submission" path="runPlagiarism"
																required="required" />
															Submission<br>
															<form:radiobutton name="runPlagiarism" id="Manual"
																value="Manual" path="runPlagiarism" />
															Manual<br>



														</div>
													</div>

													<div class="col-md-4 col-sm-6 col-xs-12 column">
														<div class="form-group">
															<label for="rightGrant">Evaluation Right Granted
																to Peer Faculties?</label> <br> <input id="Yes"
																name="rightGrant" name="rightGrant" type="radio"
																value="Y" /> Yes<br> <input id="No"
																name="rightGrant" name="rightGrant" type="radio"
																value="N" /> No<br>




														</div>
													</div>
													<div class="col-md-4 col-sm-6 col-xs-12 column">
														<div class="form-group">
															<label for="file"> <%
 	if (isEdit) {
 %> Select if you wish to override earlier file <%
 	} else {
 %> Assignment Question File <%
 	}
 %>
															</label> <input id="file" name="file" type="file"
																class="form-control" multiple="multiple" />
														</div>
														<div id=fileSize></div>
													</div>
													<div class="row">
														<div class="col-md-4 col-sm-6 col-xs-12 column">
															<div class="form-group">
																<form:label path="maxScore" for="maxScore">Score Out of <span
																		style="color: red">*</span>
																</form:label>
																<form:input path="maxScore" class="form-control"
																	value="${assignment.maxScore}" type="number"
																	required="required" />
															</div>
														</div>
														<div class="col-md-4 col-sm-6 col-xs-12 column"
															style="display: none;" id="threshold">
															<div class="form-group">
																<form:label path="threshold" for="threshold">Threshold Value for Plagarism Check <span
																		style="color: red">*</span>
																</form:label>
																<form:input id="thresholdId" path="threshold"
																	class="form-control" value="${assignment.threshold}"
																	type="number" required="required" max="100" />
															</div>
														</div>
													</div>
												</div>
												<div class="row">
													<div class="col-md-4 col-sm-6 col-xs-12 column">
														<div class="form-group">
															<form:label path="courseId" for="courseId">Course</form:label>
															<form:select id="idForCourse" path="idForCourse"
																class="form-control">
																<form:option value="">Select Course</form:option>
																<c:forEach var="course" items="${allCourses}"
																	varStatus="status">
																	<form:option value="${course.id}">${course.courseName}</form:option>
																</c:forEach>
															</form:select>
														</div>
													</div>

													<%-- <div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<form:label path="assignmentType" for="assignmentType">Assignment Type</form:label>
														<form:input path="assignmentType" type="text"
															class="form-control" required="required" />
													</div>
												</div> --%>
													<div class="col-md-4 col-sm-6 col-xs-12 column">
														<div class="form-group">
															<label class="col-sm-3 control-label">Assignment
																Type <span style="color: red">*</span>
															</label>
															<form:select class="form-control" id="showStatusDropDown"
																path="assignmentType" placeholder="Assignment Type"
																required="required">
																<form:option value="" disabled="true" selected="true">Select </form:option>
																<form:option value="Presentation">Presentation</form:option>
																<form:option value="WrittenAssignment">Written Assignment</form:option>
																<form:option value="Viva">Viva</form:option>
																<form:option value="ReportWriting">Report Writing</form:option>
															</form:select>
														</div>
													</div>


												</div>

												<div class="row">
													<div class="col-sm-6 col-md-4 col-xs-12 column">
														<div class="slider round">
															<form:label path="submitByOneInGroup"
																for="sendSmsAlertToParents">Submit Assignment By One in the Group?  <span
																	style="color: red">(Select only if you want to
																	create this assignment for group)</span>
															</form:label>
															<br>
															<form:checkbox path="submitByOneInGroup"
																class="form-control" value="Y" data-toggle="toggle"
																data-on="Yes" data-off="No" data-onstyle="success"
																data-offstyle="danger" data-style="ios" data-size="mini"
																id="submitByOneInGroup" onclick="mouseoverEvent()" />
														</div>
													</div>

												</div>

												<div class="row">
													<form:label path="assignmentText" for="editor">Assignment Text / Instructions</form:label>

													<form:textarea class="form-group" path="assignmentText"
														name="editor1" id="editor1" rows="10" cols="80" />


												</div>

												<hr>
												<div class="row">

													<div class="col-sm-8 column">
														<div class="form-group">

															<%
																if (isEdit) {
															%>
															<button id="submit" class="btn btn-large btn-primary"
																formaction="updateAssignment">Update Assignment</button>
															<%
																} else {
															%>
															<button id="submit" class="btn btn-large btn-primary"
																formaction="createAssignment">Create Assignment</button>
															<%
																}
															%>
															<button id="cancel" class="btn btn-danger"
																formaction="homepage" formnovalidate="formnovalidate">Cancel</button>
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


					</div>



				</div>

			</div>
			<!-- /page content: END -->


			<jsp:include page="../common/footer.jsp" />
			<script>
				$(function() {
					
					var plagReq =  $("#plagscanRequired").val();
					console.log('Plag Req--->'+plagReq);
					if(plagReq == 'No'){
						
						console.log('plag Req N')
						
						$('#runPlagiarism').hide();
						$('#threshold').hide();

						$("#Submission").prop('required', false);

						$("#Manual").prop('required', false);

						$("#thresholdId").prop('required', false);
					}

					$('#Yes').on('click', function() {
						//alert('slidYes');
						$('#runPlagiarism').show();
						$('#threshold').show();

						$("#Submission").prop('required', true);

						$("#Manual").prop('required', true);

						$("#thresholdId").prop('required', true);

					});

					$('#No').on('click', function() {
						//alert('slidNo');
						$('#runPlagiarism').hide();
						$('#threshold').hide();

						$("#Submission").prop('required', false);

						$("#Manual").prop('required', false);

						$("#thresholdId").prop('required', false);
					});

				});
			</script>
			<script type="text/javascript">
				$('#file').bind('change', function() {
					// alert('This file size is: ' + this.files[0].size/1024/1024 + "MB");
					var fileSize = this.files[0].size / 1024 / 1024 + "MB";
					$('#fileSize').html("File Size:" + (fileSize));
				});
			</script>

		</div>
	</div>



	<script type="text/javascript"
		src="<c:url value="/resources/js/customDateTimePicker.js" />"></script>


</body>
<!-- <script src="//cdn.ckeditor.com/4.8.0/standard/ckeditor.js"></script>

<script>
	// Replace the <textarea id="editor1"> with a CKEditor
	// instance, using default configuration.
	CKEDITOR.replace('editor1');
</script> -->

<script type="text/javascript"
	src="//cdn.ckeditor.com/4.8.0/standard-all/ckeditor.js"></script>
<script type="text/javascript"
	src="https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.0/MathJax.js?config=TeX-AMS_HTML"></script>

<script type="text/javascript">
	CKEDITOR
			.replace(
					'editor1',
					{
						extraPlugins : 'mathjax',
						mathJaxLib : 'https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.0/MathJax.js?config=TeX-AMS_HTML'
					});
</script>

<script>
	//$(document).ready(function() {
	$("#datetimepicker1").on("dp.change", function(e) {

		validDateTimepicks();
	}).datetimepicker({
		// minDate:new Date(),
		useCurrent : false,
		format : 'YYYY-MM-DD HH:mm:ss'
	});

	$("#datetimepicker2").on("dp.change", function(e) {

		validDateTimepicks();
	}).datetimepicker({
		// minDate:new Date(),
		useCurrent : false,
		format : 'YYYY-MM-DD HH:mm:ss'
	});

	function validDateTimepicks() {
		if (($('#startDate').val() != '' && $('#startDate').val() != null)
				&& ($('#endDate').val() != '' && $('#endDate').val() != null)) {
			var fromDate = $('#startDate').val();
			var toDate = $('#endDate').val();
			var eDate = new Date(fromDate);
			var sDate = new Date(toDate);
			if (sDate < eDate) {
				alert("endate cannot be smaller than startDate");
				$('#startDate').val("");
				$('#endDate').val("");
			}
		}
	}
	//});
</script>

</html>
