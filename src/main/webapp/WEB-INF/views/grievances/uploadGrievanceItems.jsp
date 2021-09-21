
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
							<a href="${pageContext.request.contextPath}/homepage">Dashboard</a>
							<i class="fa fa-angle-right"></i> Upload Grievances Items
						</div>
						<jsp:include page="../common/alert.jsp" />

						<!-- Input Form Panel -->
						<div class="row">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">

									<div class="x_title">
										<h2>Upload Grievances Items</h2>
										<ul class="nav navbar-right panel_toolbox">
											<li><a class="collapse-link"><i
													class="fa fa-chevron-up"></i></a></li>
										</ul>
										<div class="clearfix"></div>
									</div>

									<div class="x_content">
										<form:form action="uploadGrievanceItems" method="post"
											modelAttribute="grievancesConfig" enctype="multipart/form-data">
											

											<div class="row">
										
												<div class="col-sm-4 column">
													<div class="form-group">
														<label for="file">Upload Grievance Items File
															File(Excel):</label> <input id="file" name="file" type="file"
															class="form-control" />
													</div>
												</div>
												<%-- <c:if test="${test.testType eq 'Objective'}"> --%>
													<div class="col-sm-4 column ">
														<div class="form-group">


															<label class="control-label" for="courses">Excel
																Format: </label>
															<p>typeOfGrievance | grievanceCase </p>
															<p>
																<a href="resources/templates/Grievance_Config_Upload_Template.xlsx"><font
																	color="red">Download a sample template</font></a>
															</p>



														</div>
													</div>
											<%-- 	</c:if> --%>
										
												</div>
												<div class="row">

													<div class="col-sm-12 column">
														<div class="form-group">

															<button id="submit" class="btn btn-large btn-primary"
																formaction="uploadGrievanceItems">Upload</button>

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


	<%-- 		<jsp:include page="../common/footer.jsp" /> --%>

<jsp:include page="../common/DashboardFooter.jsp" />
		</div>
	</div>





</body>

</html>