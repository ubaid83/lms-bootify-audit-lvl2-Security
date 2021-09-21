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
			<div class="right_col" role="main">

				<div class="dashboard_container">

					<div class="dashboard_container_spacing">

						
			

						<!-- Input Form Panel -->
						<div class="row">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">

									<div class="x_title">
										<h2>Upload NC</h2>



										<%-- <a href="viewTestDetails?testId=${testId}"><i
											class="btn btn-large btn-primary" style="float: right;">Proceed
												to allocate students</i></a> --%>
										
										<div class="clearfix"></div>
									</div>

									<div class="x_content">
										<form:form action="uploadMarksForComponentsUnderModule"
											method="post" modelAttribute="nsBean"
											enctype="multipart/form-data">
											

											<div class="row">
												<div class="col-sm-4 column">
													<div class="form-group">
														<label for="file">Upload File(Excel):</label> <input id="file" name="file" type="file"
															class="form-control" />
													</div>
												</div>

												<div class="col-sm-4 column ">
													<div class="form-group">


														<label class="control-label" for="courses">Excel
															Format: </label>
														<p>RollNo | StudentName | Student Id | N or S </p>
														<p>
															<b>Note:</b>
														<ul>
															<li>Do Not Edit Columns i.e <b> RollNo | StudentName |StudentId</b></li>
															</ul>

														
														<br>
														<p>
															<a
																href="downloadNSTemplate?Id=${ncIcaId}"><font
																color="red">Download Template</font></a>
														</p>



													</div>
												</div>
											</div>
											<div class="row">

												<div class="col-sm-12 column">
													<div class="form-group">

														
														<button id="submit" class="btn btn-large btn-primary"
															formaction="uploadStudentNS?saveAs=submit&icaId=${ncIcaId}">Submit</button>
														
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

		<jsp:include page="../common/newAdminFooter.jsp" />
		