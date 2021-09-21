<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>



    <jsp:include page="../common/newDashboardHeader.jsp" /> 

    <div class="d-flex adminPage tabPage dataTableBottom" id="adminPage">
    <jsp:include page="../common/newAdminLeftNavBar.jsp"/>
    <jsp:include page="../common/rightSidebarAdmin.jsp" />

    

    <!-- DASHBOARD BODY STARTS HERE -->

    <div class="container-fluid m-0 p-0 dashboardWraper">

        <jsp:include page="../common/newAdminTopHeader.jsp" />

                <!-- SEMESTER CONTENT -->    
                        <div class="col-lg-12 col-md-12 col-sm-12 dashboardBody p-5">
                        
			<!-- page content: START -->
			<div class="right_col" role="main">

				<div class="dashboard_container">

					<div class="dashboard_container_spacing">
					
					<nav aria-label="breadcrumb">
                            <ol class="breadcrumb">
                                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/homepage">Dashboard</a></li>
                                
                                <li class="breadcrumb-item active" aria-current="page">Upload User Course</li>
                            </ol>
                        </nav>

						<jsp:include page="../common/alert.jsp" />

						<!-- Input Form Panel -->
						<div class="card bg-white">
							<div class="card-body">
								<div class="x_panel">

									<div class="x_title">
										<h5 class="text-center border-bottom pb-2">Upload Student/Faculty Course Mapping</h5>
									</div>

									<div class="x_content">
										<form:form action="uploadUserCourse" id="uploadUserCourseForm"
											method="post" modelAttribute="userCourse"
											enctype="multipart/form-data">


											<!-- Commented by Sanket: Since Enrollment will be by Course and irrespective of Program and session -->
											<%-- <div class="row">
						<div class="col-md-4 col-sm-6 col-xs-12 column">
								<div class="form-group">
									<label class="control-label" for="program">Program</label>
									<form:select id="program" path="" type="text"
										placeholder="Program" class="form-control" required="required" >
										<form:option value="">Select Program</form:option>
										<form:options items="${programs}" itemLabel="programName" itemValue="id"/>
									</form:select>
								</div>
						</div>
						<div class="col-md-4 col-sm-6 col-xs-12 column">
								<div class="form-group">
									<label class="control-label" for="session">Session</label>
									<div id="sessionSelect" >
										<form:select id="session" path="" type="text"
											placeholder="Session" class="form-control" required="required" >
										</form:select>
									</div>
								</div>
						</div>
						<div class="col-md-4 col-sm-6 col-xs-12 column">
								<div class="form-group">
									<label class="control-label" for="course">Course</label>
									<div id="courseSelect" >
										<form:select id="course" path="courseIds" type="text"
											placeholder="course" class="form-control" required="required" multiple="multiple">
										</form:select>
									</div>
								</div>
						</div>
					</div> --%>
					<div class="row">
					<div class="col-md-4 col-sm-6 col-xs-12 column">
						<div class="form-group">
							<form:label path="acadYear" for="acadYear">Academic Year <span style="color: red">*</span></form:label>
							<form:select id="acadYear" path="acadYear"
								class="form-control" required="required"
								itemValue="${userCourse.acadYear}">
								<form:option value="">Select Academic Year</form:option>
								<form:options items="${acadYearCodeList}" />
							</form:select>
						</div>
					</div>
					</div>
											<div class="row">
												<%-- <div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<form:label path="acadMonth" for="acadMonth">Academic Month</form:label>
														<form:select id="acadMonth" path="acadMonth"
															class="form-control" required="required"
															itemValue="${userCourse.acadMonth}">
															<form:option value="">Select Academic Month</form:option>
															<form:options items="${acadMonths}" />
														</form:select>
													</div>
												</div>
												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<form:label path="acadYear" for="acadYear">Academic Year</form:label>
														<form:select id="acadYear" path="acadYear"
															class="form-control" required="required"
															itemValue="${userCourse.acadYear}">
															<form:option value="">Select Academic Year</form:option>
															<form:options items="${acadYears}" />
														</form:select>
													</div>
												</div> --%>
												<%-- <div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<label class="control-label" for="courses">Courses</label>
														<div id="coursesSelect">
															<form:select id="courses" path="courseIds" type="text"
																placeholder="course" class="form-control"
																required="required" multiple="multiple">
																<form:options items="${courseNameMap}" />
															</form:select>
														</div>
													</div>
												</div> --%>
												<div class="col-md-8 col-sm-12">
													<div class="form-group">
														<label class="control-label" for="courses">Courses <span style="color: red">*</span></label>
														<form:select id="courses" path="courseIds" type="text"	placeholder="course" class="form-control"
															required="required" multiple="multiple" style="height: 200px!important; overflow-x:scroll!important">
															<form:option value="">Select Course</form:option>
															<c:forEach var="course" items="${allCourses}"
																varStatus="status">
																<form:option value="${course.id}">${course.courseName}</form:option>
															</c:forEach>
														</form:select>
													</div>
												</div>


												<c:if test="${filePath ne null}">
													<div class="col-md-4 col-sm-12">
														<div class="form-group">
															<label for="abbr">Upload Users</label> <input id="abbr"
																name="file" type="file" class="form-control" />
														</div>
														<a target="_blank" href="downloadFileForCourseUpload?filePath=${filePath}"><font
														color="red">Download a sample template</font></a>
													</div>
												</c:if>
											</div>




											<div class="row">

												<div class="col-sm-12 column">
													<div class="form-group">
														<c:if test="${filePath eq null}">
															<button id="submit" class="btn btn-large btn-primary"
																formaction="generateUserCourseEnrolTemplate">Generate
																Template</button>
														</c:if>
														<c:if test="${filePath ne null}">


															<button id="submit" class="btn btn-large btn-primary"
																formaction="uploadUserCourseEnrol">Upload</button>
														</c:if>


														<button id="cancel" class="btn btn-danger"
															formaction="homepage" formnovalidate="formnovalidate">Cancel</button>
													</div>
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



                        </div>

                <!-- SIDEBAR START -->

                <!-- SIDEBAR END -->
        <jsp:include page="../common/newAdminFooter.jsp"/>

        
<script>
       
$(document)
		.ready(
				function() {
					
					$("#acadYear")
							.on(
									'change',
									function() {
										//alert(program);
										var acadYear = $(this).val();
										
										if (acadYear) {
											$
													.ajax({
														type : 'GET',
														url : '${pageContext.request.contextPath}/getCourseByAcadYearAndProgram?'
																+ 'acadYear='
																+ acadYear +'&progName='+program,
														success : function(
																data) {
															var json = JSON
																	.parse(data);
															var optionsAsString = "";

															$(
																	'#courses')
																	.find(
																			'option')
																	.remove();
															console
																	.log(json);
															for (var i = 0; i < json.length; i++) {
																var idjson = json[i];
																console.log(idjson);
																console.log(idjson.courseId+ ""+ idjson.courseName);
																optionsAsString += "<option value='" +idjson.courseId + "'>"+ idjson.courseName+ "</option>";
																/* for ( var key in idjson) {
																	console.log(key+ ""+ idjson[key]);
																	optionsAsString += "<option value='" +key + "'>"+ idjson[key]+ "</option>";
																} */
															}
															console
																	.log("optionsAsString"
																			+ optionsAsString);

															$(
																	'#courses')
																	.append(
																			optionsAsString);

															$(
																	'#courses')
																	.trigger(
																			'change');

														}
													});
										} else {

										}
									});
					$('#courses').trigger('change');
				});
</script>

