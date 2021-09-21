<%@page import="com.spts.lms.web.utils.Utils"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@page import="java.util.*"%>



<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<jsp:include page="../common/newDashboardHeader.jsp" />

<div class="d-flex dataTableBottom" id="facultyAssignmentPage">
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
								Export Test</li>
						</ol>
					</nav>

					<jsp:include page="../common/alert.jsp" />


					<!-- Input Form Panel -->
					<div class="card bg-white border">
						<div class="card-body">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">
									<div class="x_title">
										<h5>Export Test</h5>

									</div>
									<div class="x_content">

										<form:form action="exportTest" method="post"
											modelAttribute="test">
											<div class="row">
												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<label><strong>Course :</strong>strong></label> ${test.courseName}
													</div>
												</div>
												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<form:label path="acadMonth" for="acadMonth"><strong>Academic Month :</strong></form:label>
														${test.acadMonth}
													</div>
												</div>
												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<form:label path="acadYear" for="acadYear"><strong>Academic Year :</strong></form:label>
														${test.acadYear}
													</div>
												</div>
												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<form:input type="hidden" path="courseId" />
													<div class="form-group">
														<form:label path="testName" for="testName"><strong>Test Name :</strong></form:label>
														${test.testName}
													</div>
												</div>
												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<form:label path="duration" for="duration"><strong>Test Duration :</strong></form:label>
														<c:out
															value="${fn:replace(test.duration,'T', ' ')}${' Minutes'}"></c:out>
													</div>
												</div>
												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<form:label path="startDate" for="startDate"><strong>Start Date :</strong></form:label>
														<c:out
															value="${fn:replace(test.startDate, 
                                'T', ' ')}"></c:out>
													</div>
												</div>
												<%-- <div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<form:label path="dueDate" for="dueDate">Due Date :</form:label>
														<c:out value="${fn:replace(test.dueDate,'T', ' ')}"></c:out>
													</div>
												</div> --%>
												<sec:authorize access="hasRole('ROLE_FACULTY')">
													<div class="col-md-4 col-sm-6 col-xs-12 column">
														<div class="form-group">
															<form:label path="endDate" for="endDate"><strong>End Date :</strong></form:label>
															<c:out value="${fn:replace(test.endDate,'T', ' ')}"></c:out>
														</div>
													</div>
													
													<div class="col-md-4 col-sm-6 col-xs-12 column">
														<div class="form-group">
															<form:label path="sendEmailAlert" for="sendEmailAlert"><strong>Allow Submission after End date :</strong></form:label>
															${test.allowAfterEndDate}

														</div>
													</div>
													<div class="col-md-4 col-sm-6 col-xs-12 column">
														<div class="form-group">
															<form:label path="sendEmailAlert" for="sendEmailAlert"><strong>Send Email Alert for New Test :</strong></form:label>
															${test.sendEmailAlert}
														</div>
													</div>
													<div class="col-md-4 col-sm-6 col-xs-12 column">
														<div class="form-group">
															<form:label path="sendSmsAlert" for="sendSmsAlert"><strong>Send SMS Alert for New Test :</strong></form:label>
															${test.sendSmsAlert}
														</div>
													</div>
													<div class="col-sm-6 col-md-4 col-xs-12 column">
														<div class="form-group">
															<form:label path="sendEmailAlertToParents"
																for="sendEmailAlertToParents"><strong>Send Email Alert to Parents:</strong></form:label>
															${test.sendEmailAlertToParents}
														</div>
													</div>

													<div class="col-sm-6 col-md-4 col-xs-12 column">
														<div class="form-group">
															<form:label path="sendSmsAlertToParents"
																for="sendSmsAlertToParents"><strong>Send SMS Alert to Parents:</strong></form:label>
															${test.sendSmsAlertToParents}
														</div>
													</div>

													<div class="col-md-4 col-sm-6 col-xs-12 column">
														<div class="form-group">
															<form:label path="showResultsToStudents"
																for="showResultsToStudents"><strong>Show Results to Students immediately :</strong></form:label>
															${test.showResultsToStudents}
														</div>
													</div>
													<div class="col-md-4 col-sm-6 col-xs-12 column">
														<div class="form-group">
															<form:label path="facultyId" for="facultyId"><strong>Password For Test :</strong></form:label>
															${test.passwordForTest}
														</div>
													</div>
												</sec:authorize>
												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<form:label path="maxScore" for="maxScore"><strong>Score Out of :</strong></form:label>
														${test.maxScore}
													</div>
												</div>
												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<form:label path="facultyId" for="facultyId"><strong>Faculty :</strong></form:label>
														${test.facultyId}
													</div>
												</div>
											</div>
										</form:form>

									</div>
								</div>
							</div>
						</div>
					</div>

					<!-- Results Panel -->


					<div class="card bg-white border">
						<div class="card-body">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">
									<div class="x_title">
										<h5>Select Courses to export test ${test.testName}</h5>

									</div>
									<div class="x_content">
										<form:form action="exportContent" id="exportContent"
											method="post" modelAttribute="test">
											<div class="row">

												<form:input path="id" type="hidden" id="testId"
													value="${test.id}" />
												<form:input path="courseId" type="hidden" />


												<div class="col-sm-4 column">
													<div class="form-group">
														<form:label path="programId" for="programId"><strong>Select Program</strong></form:label>
														<form:select id="programId" path="programId"
															class="form-control">
															<form:option value="">Select Program</form:option>
															<c:forEach var="prog" items="${programList}"
																varStatus="status">


																<form:option value="${prog.id}">${prog.programName}</form:option>



															</c:forEach>
														</form:select>
													</div>
												</div>
												<div class="col-md-6 col-sm-8 col-xs-12 column">
													<div class="form-group">
														<form:label path="courseIdToExport" for="courseIdToExport">Courses</form:label>
														<form:select id="assid" path="courseIdToExport"
															class="form-control" multiple="multiple"
															required="required">
															<form:option value="">Select Course</form:option>

															<c:forEach var="preAssigment" items="${preAssigments}"
																varStatus="status">
																<form:option value="${preAssigment.id}">${preAssigment.courseName}</form:option>
															</c:forEach>

														</form:select>
													</div>
												</div>
												<%-- <c:if test="${content.accessType eq 'Public' }">



													<div class="col-md-4 col-sm-6 col-xs-12 column">
														<div class="slider round">
															<form:label path="allocateToStudents"
																for="allocateToStudents">Share content with all the students of selected course?</form:label>
															<br>
															<form:checkbox path="allocateToStudents"
																class="form-control" value="Y" data-toggle="toggle"
																data-on="Yes" data-off="No" data-onstyle="success"
																data-offstyle="danger" data-style="ios" data-size="mini" />
														</div>
													</div>







												</c:if> --%>

												<div class="col-sm-4 column">
													<div class="form-group">

														<a class="btn btn-large btn-success text-white"
															onclick="exportTest()"> Export Test</a>
														<button id="cancel" class="btn btn-xs btn-danger"
															formaction="homepage" formnovalidate="formnovalidate">Cancel</button>
													</div>
												</div>



												<div class="col-sm-4 column">
													<div class="form-group">

														<c:forEach var="prog" items="${programList}"
															varStatus="status">

															<form:input path="" id="${prog.id}-schoolObjId"
																type="hidden" value="${prog.schoolObjId}" />
															<form:input path="" id="${prog.id}-abbr" type="hidden"
																value="${prog.abbr}" />
															<form:input path="" id="${prog.id}-username"
																type="hidden" value="${prog.username}" />
															<form:input path="" id="${prog.id}-url" type="hidden"
																value="${prog.url}" />

															<form:input path="" id="${prog.id}-dbName" type="hidden"
																value="${prog.dbName}" />




														</c:forEach>

													</div>
												</div>
											</div>
										</form:form>
									</div>
								</div>
							</div>
						</div>
					</div>


					<%-- 	<jsp:include page="../common/paginate.jsp">
							<jsp:param name="baseUrl" value="searchFacultyGroups" />
						</jsp:include> --%>

					<!-- /page content: END -->

				</div>

				<!-- SIDEBAR START -->
				<jsp:include page="../common/newSidebar.jsp" />
				<!-- SIDEBAR END -->
				<jsp:include page="../common/footer.jsp" />




				<script>
					$("#programId")
							.on(
									'change',
									function() {

										var programid = $(this).val();

										var schoolObjId = $(
												'#' + programid
														+ '-schoolObjId').val();
										var abbr = $('#' + programid + '-abbr')
												.val();
										var username = $(
												'#' + programid + '-username')
												.val();
										var url = $('#' + programid + '-url')
												.val();

										var dbname = $(
												'#' + programid + '-dbName')
												.val();
										//alert(programid)
										if (programid) {
											$
													.ajax({
														type : 'GET',
														url : '${pageContext.request.contextPath}/getCourseByProgramIdByParam?'
																+ 'programId='
																+ programid
																+ '&abbr='
																+ abbr
																+ '&username='
																+ username
																+ '&url='
																+ url

																+ '&dbName='
																+ dbname,
														success : function(data) {
															var json = JSON
																	.parse(data);
															var optionsAsString = "";

															$('#assid').find(
																	'option')
																	.remove();
															console.log(json);
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
											//alert('Error no course');
										}
									});
					$('#programId').trigger('change');
				</script>

				<script>
					function exportTest() {
						var testId = $("#testId").val();
						var programid = $("#programId").val();
						var courseIds = $("#assid").val();
						var schoolObjId = $('#' + programid + '-schoolObjId')
								.val();
						var abbr = $('#' + programid + '-abbr').val();
						var username = $('#' + programid + '-username').val();
						var url = $('#' + programid + '-url').val();

						var dbname = $('#' + programid + '-dbName').val();

						console.log("courseIds--->" + courseIds);
						console.log("testId--->" + testId);
						if (programid != null && courseIds != null) {

							window.location.href = '${pageContext.request.contextPath}/exportTest?'
									+ 'programId='
									+ programid
									+ '&testId='
									+ testId
									+ '&courseIds='
									+ courseIds
									+ '&schoolObjId='
									+ schoolObjId
									+ '&abbr='
									+ abbr
									+ '&username='
									+ username
									+ '&url='
									+ url

									+ '&dbName=' + dbname;

						} else {
							alert("please fill all the fields");
						}

					}
				</script>