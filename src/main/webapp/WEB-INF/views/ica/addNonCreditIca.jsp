<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%
	boolean isEdit = "true".equals((String) request
			.getAttribute("edit"));
%>


<jsp:include page="../common/newDashboardHeader.jsp" />

<div class="d-flex" id="facultyAssignmentPage">
	<jsp:include page="../common/newAdminLeftNavBar.jsp" />
	<jsp:include page="../common/rightSidebarAdmin.jsp" />


	<!-- DASHBOARD BODY STARTS HERE -->

	<div class="container-fluid m-0 p-0 dashboardWraper">

		<jsp:include page="../common/newAdminTopHeader.jsp" />

		<!-- SEMESTER CONTENT -->
		<div class="col-lg-12 col-md-12 col-sm-12 dashboardBody p-5">

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
					<%
						if (isEdit) {
					%>
					<li class="breadcrumb-item active" aria-current="page">Update
						Internal Continuous Assessment / Evaluation COMPONENT</li>
					<%
						} else {
					%>
					<li class="breadcrumb-item active" aria-current="page">
						NonCredit Internal Continuous Assessment</li>
					<%
						}
					%>
				</ol>
			</nav>

			<jsp:include page="../common/alert.jsp" />

			<!-- Input Form Panel -->
			<div class="card bg-white border">
				<div class="card-body">
					<h5 class="text-center pb-2 border-bottom">
						<%
							if ("true".equals((String) request.getAttribute("edit"))) {
						%>
						Edit Internal Continuous Assessment / Evaluation COMPONENT
						<%
							} else {
						%>
						NonCredit Internal Continuous Assessment
						<%
							}
						%>
					</h5>

					<form:form action="addNonCreditIca" method="post"
						modelAttribute="nsBean">

						<%
							if (isEdit) {
						%>
						<form:input type="hidden" path="id" value="${nsBean.id}" />
						<%-- <form:input type="hidden" path="moduleId"
							value="${icaBean.moduleId}" />
						<form:input type="hidden" path="acadYear"
							value="${icaBean.acadYear}" />
						<form:input type="hidden" path="acadSession"
							value="${icaBean.acadSession}" />
						<form:input type="hidden" path="programId"
							value="${icaBean.programId}" /> --%>
						<%
							}
						%>
						<div class="row">
							<div class="col-md-4 col-sm-6 col-xs-12 column">
								<div class="form-group">
									<form:label path="icaName" for="icaName">ICA Name <span
											style="color: red">*</span>
									</form:label>
									<form:input path="icaName" type="text" class="form-control"
										required="required" />
								</div>
							</div>
							<%
								if (isEdit) {
							%>
							<div class="col-md-4 col-sm-6 col-xs-12 column">
								<div class="form-group">
									<div class="col-md-4 col-sm-6 col-xs-12 column">
										<div class="form-group">
											<form:label path="acadYear" for="acadYear">Academic Year</form:label>
											<form:select id="acadYear" path="acadYear"
												class="form-control subjectParam" disabled="disabled">
												<option value="${nsBean.acadYear}" selected="selected">${nsBean.acadYear}</option>

											</form:select>
										</div>
									</div>
								</div>
							</div>

							<%
								} else {
							%>
							<div class="col-md-4 col-sm-6 col-xs-12 column">
								<div class="form-group">
									<form:label path="acadYear" for="acadYear">Academic Year</form:label>
									<form:select id="acadYear" path="acadYear"
										class="form-control subjectParam">
										<option value="" selected disabled hidden>Select
											Academic Year</option>
									<%-- 	<form:options items="${acadYears}" /> --%>
									<form:options items="${acadYearCodeListNS}" />
									</form:select>
								</div>
							</div>
							<%
								}
							%>
							
							<%
								if (isEdit) {
							%>
							<div class="col-md-4 col-sm-6 col-xs-12 column">
								<div class="form-group">
									<form:label path="acadSession" for="acadSession">Acad Session</form:label>
									<form:select id="acadSession" path="acadSession"
										class="form-control subjectParam" disabled="disabled">
										<form:option value="${nsBean.acadSession}">${nsBean.acadSession}</form:option>
										<%--  <c:forEach var="course" items="${allCourses}"
                                                            varStatus="status">
                                                            <form:option value="${course.id}">${course.courseName}</form:option>
                                                      </c:forEach> --%>
									</form:select>
								</div>
							</div>
							<%
								} else {
							%>
							 <div class="col-md-4 col-sm-6 col-xs-12 column">
								<div class="form-group">
									<form:label path="acadSession" for="acadSession">Acad Session</form:label>
									<form:select id="acadSession" path="acadSession"
										class="form-control subjectParam">
										<form:option value="">Select Session</form:option>
										 <c:forEach var="course" items="${allCourses}"
                                                            varStatus="status">
                                                            <form:option value="${course.id}">${course.courseName}</form:option>
                                                      </c:forEach>
									</form:select>
								</div>
							</div> 

							<%-- <div class="col-md-4 col-sm-6" id="sessionId">
								<div class="form-group">
									<form:label path="acadSession" for="acadSession">Semester</form:label>
									<form:select id="acadSession" path="acadSession"
										class="form-control">

										<form:option value="">Select Semester</form:option>

										<c:forEach items="${acadSessionList}" var="acadSession">
											<form:option value="${acadSession}">${acadSession}</form:option>
										</c:forEach>
									</form:select>
								</div>

							</div> --%>
							<%
								}
							%>
							
							<%
								if (isEdit) {
							%>
							<div class="col-md-6 col-sm-8 col-xs-12 column">
								<div class="form-group">
									<form:label path="moduleId" for="moduleId">Subject</form:label>
									<form:select id="moduleId" path="moduleId"
										class="form-control subjectParam">
										<form:option value="${nsBean.moduleId}">${moduleName}</form:option>
										<%--  <c:forEach var="course" items="${allCourses}"
                                                            varStatus="status">
                                                            <form:option value="${course.id}">${course.courseName}</form:option>
                                                      </c:forEach> --%>
									</form:select>
								</div>
							</div>
							<%
								} else {
							%>
							<div class="col-md-6 col-sm-8 col-xs-12 column">
								<div class="form-group">
									<form:label path="moduleId" for="moduleId">Subject</form:label>
									<form:select id="moduleId" path="moduleId"
										class="form-control subjectParam">
										<form:option value="">Select Subject</form:option>
										<%--  <c:forEach var="course" items="${allCourses}"
                                                            varStatus="status">
                                                            <form:option value="${course.id}">${course.courseName}</form:option>
                                                      </c:forEach> --%>
									</form:select>
								</div>
							</div>
							<%
								}
							%>

							<%
								if (isEdit) {
							%>
							<div class="col-sm-6">
								<div class="form-group">

									<label path="" for="groupId">Select Program <span
										style="color: red">*</span></label>
									<form:select multiple="multiple" id="programIdForProgramWise"
										path="programId" required="required" class="form-control"
										style="overflow: auto;" disabled="disabled">

										<c:forEach var="p" items="${programList}">
											<option value="${p.id}" selected="selected">${p.programName}</option>
										</c:forEach>

									</form:select>
								</div>
							</div>
							<%
								} else {
							%>
							<div class="col-sm-6">
								<div class="form-group">

									<label path="" for="groupId">Select Program <span
										style="color: red">*</span></label>
									<form:select multiple="multiple" id="programIdForProgramWise"
										path="programId" required="required" class="form-control"
										style="overflow: auto;">



									</form:select>
								</div>
							</div>
							<%
								}
							%>

		
							<div class="col-md-4 col-sm-6">
								<div class="form-group">
									<form:label path="campusId" for="campusId">Select Campus</form:label>

									<form:select id="campusId" path="campusId" type="text"
										placeholder="campus" class="form-control">
										<form:option value="">Select Campus</form:option>
										<c:forEach var="campus" items="${allCampuses}"
											varStatus="status">
											<form:option value="${campus.campusId}">${campus.campusName}</form:option>
										</c:forEach>
									</form:select>
								</div>
							</div>

							<div class="col-lg-6 col-md-6 col-sm-12 mt-3 mb-3">
								<label class="textStrong">Show to Students?</label>
								<div>
									<div class="pretty p-switch p-fill p-toggle">

										<form:checkbox value="N" id="showToStudents"
											class="custToggle" path="showToStudents" />

										<div class="state p-success p-on">
											<label>Yes</label>
										</div>
										<div class="state p-danger p-off">
											<label>No</label>
										</div>
									</div>
								</div>

							</div>
						</div>




						<div class="col-sm-12 column">
							<div class="form-group">
								<%
									if (isEdit) {
								%>
								<button id="submit" name="submit"
									class="btn btn-large btn-primary" formaction="updateIca">Update
									ICA</button>
								<%
									} else {
								%>

								<button id="submit" name="submit"
									class="btn btn-large btn-primary">Create</button>
								<%
									}
								%>

								<button id="cancel" name="cancel" class="btn btn-danger"
									formnovalidate="formnovalidate" formaction="homepage">Cancel</button>
							</div>
						</div>



					</form:form>
				</div>
			</div>
			<!-- /page content: END -->

		</div>

		<!-- SIDEBAR START -->

		<!-- SIDEBAR END -->

		<jsp:include page="../common/newAdminFooter.jsp" />
		<!-- <script>
			$(".subjectParam")
					.on(
							'change',
							function() {
								var programId = $("#programId").val();
								var acadYear = $("#acadYear").val();

								if (acadYear && programId) {
									$
											.ajax({
												type : 'GET',
												url : '${pageContext.request.contextPath}/getModuleIdByProgramId?'
														+ 'programId='
														+ programId
														+ '&acadYear='
														+ acadYear,
												success : function(data) {
													var json = JSON.parse(data);
													var optionsAsString = "";

													$('#moduleId').find(
															'option').remove();
													console.log(json);
													for (var i = 0; i < json.length; i++) {
														var idjson = json[i];
														console.log(idjson);

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

													$('#moduleId').append(
															optionsAsString);

													$('#moduleId').trigger(
															'change');

												}
											});
								} else {

								}
							});
		</script> -->
		
		<script>
		
			$("#moduleId").select2( {
				placeholder: "Select Subject",
				allowClear: true
			} );

			$("#acadYear").select2( {
				placeholder: "Select Academic Year",
				allowClear: true
			} );
			
			var acadSessionGen;
			$(".subjectParam")
					.on(
							'change',
							function() {

								console.log("subject param entered");
								var acadYear = $('#acadYear').val();
								var campusId = $('#campus').val();
								var programId = $('#programId').val();
								var courseid = $('#moduleId').val();
								var acadSession = $('#acadSession').val();

								if (campusId == null) {
									console
											.log("undefined campusId"
													+ campusId);
									//campusId='null';
								}

								if (acadYear && !acadSession) {

									$
											.ajax({
												type : 'GET',
												url : '${pageContext.request.contextPath}/getSessionByParamForModule?'
														+ 'acadYear='
														+ acadYear
														+ '&campusId='
														+ campusId,

												success : function(data) {
													var json = JSON.parse(data);
													console.log('json--->'
															+ json);
													var optionsAsString = "";

													$('#acadSession').find(
															'option').remove();
													optionsAsString += "<option value=\"\" selected disabled> Select Session</option>";
													console.log(json);
													for (var i = 0; i < json.length; i++) {
														var idjson = json[i];
														console.log(idjson);

														for ( var key in idjson) {
															console
																	.log(key
																			+ ""
																			+ idjson[key]);

															console
																	.log('else entered00');
															optionsAsString += "<option value='" +key + "'>"
																	+ idjson[key]
																	+ "</option>";

														}
													}

													$('#acadSession').append(
															optionsAsString);

												}
											});
								}
								var isSessionChange=false;
                                if(acadSession){
                                                if(acadSession!=acadSessionGen){
                                                                isSessionChange=true;
                                                }
                                }

								if (acadYear && acadSession && isSessionChange==true) {
									acadSessionGen = acadSession;
									$
											.ajax({
												type : 'GET',
												url : '${pageContext.request.contextPath}/getModuleByParamForModule?'
														+ 'acadYear='
														+ acadYear
														+ '&campusId='
														+ campusId
														+ '&acadSession='
														+ acadSession,
												success : function(data) {
													var json = JSON.parse(data);
													console.log('json--->'
															+ json);
													var optionsAsString = "";

													$('#moduleId').find(
															'option').remove();
													optionsAsString += "<option value=\"\" selected disabled> Select Subject</option>";
													console.log(json);
													for (var i = 0; i < json.length; i++) {
														var idjson = json[i];
														console.log(idjson);

														for ( var key in idjson) {
															console
																	.log(key
																			+ ""
																			+ idjson[key]);
															if (moduleId == idjson[key]) {
																optionsAsString += "<option value='" +key + "' selected>"
																		+ idjson[key]
																		+ "</option>";

															} else {
																console
																		.log('else entered00');
																optionsAsString += "<option value='" +key + "'>"
																		+ idjson[key]
																		+ "</option>";
															}
														}
													}

													$('#moduleId').append(
															optionsAsString);

												}
											});
								} else {
									console.log("no acadYear");
								}
								console.log('course id got' + courseid);

								if (courseid && acadYear) {

									if (acadSession) {
										$
												.ajax(
														{
															url : "${pageContext.request.contextPath}/GetProgramsFromAcadSessionYearModuleForModule?acadSession="
																	+ acadSession
																	+ '&moduleId='
																	+ courseid
																	+ '&year='
																	+ acadYear
														})
												.done(
														function(data) {
															console.log("data:"
																	+ data);
															programObj = JSON
																	.parse(data);
															console
																	.log("Size:"
																			+ programObj.length);
															$(
																	"#programIdForProgramWise option")
																	.remove();
															for (var i = 0; i < programObj.length; i++) {
																var a = JSON
																		.stringify(programObj[i]);
																console
																		.log("a is "
																				+ a);
																$(
																		"#programIdForProgramWise")
																		.append(
																				$(
																						'<option>',
																						{
																							value : programObj[i].value,
																							text : programObj[i].text
																						}));

															}

														});

									}

								
								} else {
									console.log("no course");
								}

							});
		</script>