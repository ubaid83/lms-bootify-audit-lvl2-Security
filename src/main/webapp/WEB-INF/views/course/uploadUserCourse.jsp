<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

    <jsp:include page="../common/newDashboardHeader.jsp" /> 

    <div class="d-flex adminPage" id="adminPage">
    <jsp:include page="../common/newAdminLeftNavBar.jsp"/>
    <jsp:include page="../common/rightSidebarAdmin.jsp" />


    <!-- DASHBOARD BODY STARTS HERE -->

    <div class="container-fluid m-0 p-0 dashboardWraper">

        <jsp:include page="../common/newAdminTopHeader.jsp" />

                <!-- SEMESTER CONTENT -->    
                        <div class="col-lg-12 col-md-12 col-sm-12 dashboardBody p-5">
                        
                        
                        
                        			<!-- page content: START -->

						<nav aria-label="breadcrumb">
                    <ol class="breadcrumb">
                        <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/homepage">Dashboard</a></li>
                        <li class="breadcrumb-item" aria-current="page">
                            <c:out value="${Program_Name}" />
                        </li>
                        <sec:authorize access="hasRole('ROLE_STUDENT')">
                            <c:out value="${AcadSession}" />
                        </sec:authorize>
                        <li class="breadcrumb-item active" aria-current="page"> Upload User Course</li>
                    </ol>
                </nav>

						<jsp:include page="../common/alert.jsp" />

						<!-- Input Form Panel -->
						<div class="card bg-white border">
							<div class="card-body">
								<div class="x_panel">

										<h5 class="text-center border-bottom pb-2">Upload Student/Faculty Course Mapping</h5>

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
												<div class="col-md-4 col-sm-6">
													<div class="form-group">
														<form:label path="acadMonth" for="acadMonth">Academic Month <span style="color: red">*</span></form:label>
														<form:select id="acadMonth" path="acadMonth"
															class="form-control" required="required"
															itemValue="${userCourse.acadMonth}">
															<form:option value="">Select Academic Month</form:option>
															<form:options items="${acadMonths}" />
														</form:select>
													</div>
												</div>
												<div class="col-md-4 col-sm-6">
													<div class="form-group">
														<form:label path="acadYear" for="acadYear">Academic Year <span style="color: red">*</span></form:label>
														<form:select id="acadYear" path="acadYear"
															class="form-control" required="required"
															itemValue="${userCourse.acadYear}">
															<form:option value="">Select Academic Year</form:option>
															<form:options items="${acadYears}" />
														</form:select>
													</div>
												</div>
												<div class="col-md-4 col-sm-6">
													<div class="form-group">
														<label for="abbr">Upload Users</label> <input id="abbr"
															name="file" type="file" class="form-control" />
													</div>
												</div>
											</div>

											<div class="row">
												<div class="col-md-4 col-sm-6">
													<div class="form-group">
														<label class="control-label" for="courses">Courses <span style="color: red">*</span></label>
														<div id="coursesSelect">
															<form:select id="courses" path="courseIds" type="text"
																placeholder="course" class="form-control"
																required="required" multiple="multiple">
																<form:options items="${courseNameMap}" />
															</form:select>
														</div>
													</div>
												</div>

												<div class="col-12">
													<div class="form-group">
														<label class="control-label" for="courses">Excel
															Format: </label>
														<p>Username | Role</p>
														<p>
															<b>Note:</b>
														<ul>
															<li>Username can be for Student as well as Faculty</li>
															<li>Role must have valid value as ROLE_STUDENT or
																ROLE_FACULTY</li>
														</ul>
														<p>
															<a
																href="resources/templates/User_Course_Upload_Template.xlsx">Download
																a sample template</a>
														</p>
													</div>
												</div>
											</div>

											<div class="row">

												<div class="col-sm-12 column">
													<div class="form-group">

														<button id="submit" class="btn btn-large btn-primary"
															formaction="uploadUserCourse">Upload</button>
														<button id="cancel" class="btn btn-danger"
															formaction="homepage" formnovalidate="formnovalidate">Cancel</button>
													</div>
												</div>

											</div>


										</form:form>
								</div>
							</div>
						</div>

			<!-- /page content: END -->
                        



                        </div>

                <!-- SIDEBAR START -->

                <!-- SIDEBAR END -->
        <jsp:include page="../common/newAdminFooter.jsp"/>




