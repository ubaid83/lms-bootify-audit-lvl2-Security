<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>



<jsp:include page="../common/newDashboardHeader.jsp" />

<div class="d-flex adminPage tabPage dataTableBottom" id="adminPage">
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
					<li class="breadcrumb-item active" aria-current="page">Upload
						Course</li>
				</ol>
			</nav>

			<jsp:include page="../common/alert.jsp" />

			<!-- Input Form Panel -->
			<div class="card bg-white border">
				<div class="card-body">
					<div class="x_panel">

						<h5 class="text-center border-bottom pb-2">Upload All Courses</h5>


						<div class="x_content">
							<form:form action="uploadCourse" id="uploadCourse" method="post"
								modelAttribute="course" enctype="multipart/form-data">

								<div class="row">


									<div class="col-8">
										<div class="form-group">
											<label class="font-weight-bold">Program <span
												class="text-danger">*</span></label> <select class="form-control"
												id="programIds" name="programIds"
												style="max-height: 200px; overflow: auto" multiple>
												<option selected disabled>Select Program</option>
												<c:forEach var="program" items="${programList}">
													<option value="${program.id}">${program.programName}</option>
												</c:forEach>
											</select>
										</div>
									</div>
								</div>

								<div class="row">

									<div class="col-md-4 col-sm-12">
										<div class="form-group">
											<label for="abbr">Select Course File as per template</label>
											<input id="abbr" name="file" type="file" class="form-control" />
										</div>
									</div>

									<div class="col-md-4 col-sm-12">
										<b>Template Format:</b><br> Course Full Name | Course
										Course Area/Department | College Abbreviation | Acad Month |
										Acad Year | Acad Session <br> <br> <a
											class="text-danger"
											href="resources/templates/Course_Upload_Template.xlsx">Download
											a sample template</a>
									</div>
								</div>
								<div class="row">

									<div class="col-12">
										<div class="form-group">

											<button id="submit" class="btn btn-large btn-primary"
												formaction="uploadCourse">Upload</button>
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

			<!-- /page content: END -->




		</div>

		<!-- SIDEBAR START -->

		<!-- SIDEBAR END -->
		<jsp:include page="../common/newAdminFooter.jsp" />