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

								<i class="fa fa-angle-right"></i>

								<c:out value="${AcadSession}" />

							</sec:authorize>

							<br> <br> <a
								href="${pageContext.request.contextPath}/homepage">Dashboard</a>
							<i class="fa fa-angle-right"></i> Upload Marks For Components
						</div>
						<jsp:include page="../common/alert.jsp" />

						<!-- Input Form Panel -->
						<div class="row">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">

									<div class="x_title">
										<h2>Upload Marks For Component</h2>



										<%-- <a href="viewTestDetails?testId=${testId}"><i
											class="btn btn-large btn-primary" style="float: right;">Proceed
												to allocate students</i></a> --%>
										<ul class="nav navbar-right panel_toolbox">
											<li><a class="collapse-link"><i
													class="fa fa-chevron-up"></i></a></li>
										</ul>
										<div class="clearfix"></div>
									</div>

									<div class="x_content">
										<form:form action="uploadMarksForComponentsUnderModule"
											method="post" modelAttribute="componentWeightage"
											enctype="multipart/form-data">
											

											<div class="row">
												<div class="col-sm-4 column">
													<div class="form-group">
														<label for="file">Upload Marks For Component
															File(Excel):</label> <input id="file" name="file" type="file"
															class="form-control" />
													</div>
												</div>

												<div class="col-sm-4 column ">
													<div class="form-group">


														<label class="control-label" for="courses">Excel
															Format: </label>
														<p>Component Id | Component | Marks</p>
														<p>
															<b>Note:</b>
														<ul>
															<li>Do Not Edit First Column i.e <b>Component Id</b>
															</li>

														</ul>
														<br>
														<p>
															<a
																href="downloadModuleComponentTemplate?filePath=${filePath}"><font
																color="red">Download Template</font></a>
														</p>



													</div>
												</div>
											</div>
											<div class="row">

												<div class="col-sm-12 column">
													<div class="form-group">

														<button id="submit" class="btn btn-large btn-primary"
															formaction="uploadMarksForComponentsUnderModule">Upload</button>

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

			<jsp:include page="../common/footer.jsp" />
		</div>
	</div>





</body>

</html>