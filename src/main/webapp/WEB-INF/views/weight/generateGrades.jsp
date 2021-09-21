<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
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
							<a href="${pageContext.request.contextPath}/homepage">Dashboard</a>
							<i class="fa fa-angle-right"></i> View Grades
						</div>
						<jsp:include page="../common/alert.jsp" />

						<!-- Input Form Panel -->
						<div class="row">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">

									<div class="x_title">
										<h2>Forums</h2>
										<ul class="nav navbar-right panel_toolbox">
											<li><a class="collapse-link"><i
													class="fa fa-chevron-up"></i></a></li>
										</ul>
										<div class="clearfix"></div>
									</div>

									<div class="x_content">
										<form:form action="viewGradeWeight" method="post"
											modelAttribute="studentWeightData"
											enctype="multipart/form-data">


											<form:input path="courseId" type="hidden" />
											<div class="row">
												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<form:label path="courseId" for="courseId">Course</form:label>
														<form:select id="courseIdForForum" path="courseId"
															class="form-control">
															<form:option value="">Select Course</form:option>
															<c:forEach var="course" items="${allCourses}"
																varStatus="status">
																<form:option value="${course.id}">${course.courseName}</form:option>
															</c:forEach>
														</form:select>
													</div>
												</div>
											</div>

											<div class="col-md-4 col-sm-6 col-xs-12 column">
												<div class="slider round">
													<form:label path="gradingType" for="gradingType">Select Grading formula :</form:label>
													<br>
													<%-- <form:radiobutton name="gradingType" id="Best" value="Best"
														path="gradingType" required="required" />
													Best of <br> --%>
													<form:radiobutton name="gradingType" id="Average" value="Average"
														path="gradingType" />
													Average<br>


													<%-- 	<form:checkbox path="plagscanRequired" onclick="clicked();"
																class="form-control" value="Y" data-toggle="toggle"
																data-on="Yes" data-off="No" data-onstyle="success"
																data-offstyle="danger" data-style="ios" data-size="mini" /> --%>
												</div>
											</div>

											<div class="col-sm-12 column">
												<div class="form-group">
													<button id="submit" name="submit"
														class="btn btn-large btn-primary">View Grades</button>
													<input id="reset" type="reset" class="btn btn-danger">
													<button id="cancel" name="cancel" class="btn btn-danger"
														formnovalidate="formnovalidate">Cancel</button>
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
			<script>
				$("#courseIdForForum")
						.on(
								'change',
								function() {

									//alert("Onchange Function called!");
									var selectedValue = $(this).val();
									window.location = '${pageContext.request.contextPath}/generateGrades?courseId='
											+ encodeURIComponent(selectedValue);
									return false;
								});
			</script>

		</div>
	</div>





</body>
</html>

