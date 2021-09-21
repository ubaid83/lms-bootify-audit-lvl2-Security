<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html lang="en">
<jsp:include page="../common/css.jsp" />

<body class="nav-md footer_fixed">

	<div class="loader"></div>


	<div class="container body">
		<div class="main_container">


			<jsp:include page="../common/leftSidebar.jsp">
				<jsp:param name="courseId" value="${courseId}" />
			</jsp:include>



			
			<jsp:include page="../common/topHeader.jsp" >
				<jsp:param value="courseMenu" name="activeMenu"/>
			</jsp:include>

			<!-- page content: START -->
			<div class="right_col" role="main">

				<div class="dashboard_container">

					<div class="dashboard_container_spacing">

						<div class="breadcrumb">
							<a href="${pageContext.request.contextPath}/homepage">Dashboard</a> <i class="fa fa-angle-right"></i>
							Upload User Course
						</div>
						<jsp:include page="../common/alert.jsp" />

						<!-- Input Form Panel -->
						<div class="row">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">

									<div class="x_title">
										<h2>Upload Student/Faculty Course Mapping</h2>
										<ul class="nav navbar-right panel_toolbox">
											<li><a class="collapse-link"><i
													class="fa fa-chevron-up"></i></a></li>
										</ul>
										<div class="clearfix"></div>
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
														<form:label path="acadMonth" for="acadMonth">Academic Month <span style="color: red">*</span></form:label>
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
														<form:label path="acadYear" for="acadYear">Academic Year <span style="color: red">*</span></form:label>
														<form:select id="acadYear" path="acadYear"
															class="form-control" required="required"
															itemValue="${userCourse.acadYear}">
															<form:option value="">Select Academic Year</form:option>
															<form:options items="${acadYears}" />
														</form:select>
													</div>
												</div>
												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<label for="abbr">Upload Users</label> <input id="file"
															name="file" type="file" class="form-control" />
													</div>
												</div>
												<div id=fileSize></div>
											</div>

											<div class="row">
												<div class="col-md-4 col-sm-6 col-xs-12 column">
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

												<div class="col-sm-8 column">
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
						</div>



					</div>



				</div>

			</div>
			<!-- /page content: END -->


			<jsp:include page="../common/footer.jsp" />
			<script type="text/javascript">
        $('#file').bind('change', function() {
           // alert('This file size is: ' + this.files[0].size/1024/1024 + "MB");
            var fileSize = this.files[0].size/1024/1024 + "MB";
            $('#fileSize')
			.html(
					"File Size:" + (fileSize) );
        });
    </script>

		</div>
	</div>





</body>
</html>
