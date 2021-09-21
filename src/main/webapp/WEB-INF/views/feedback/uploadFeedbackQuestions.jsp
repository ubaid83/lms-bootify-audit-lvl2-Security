<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

<jsp:include page="../common/newDashboardHeader.jsp" />

<div class="d-flex adminPage" id="adminPage">
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
						Feedback Question</li>
				</ol>
			</nav>
			<jsp:include page="../common/alert.jsp" />

			<!-- Input Form Panel -->
			<div class="card bg-white border">
				<div class="card-body">

					<h5 class="border-bottom text-center pb-2">Upload Feedback
						Questions</h5>

					<c:if test="${showProceed}">
						<div class="col-12 text-right">
							<a href="addStudentFeedbackForm"><i
								class="btn btn-large btn-primary">Proceed to allocate
									students</i></a>
						</div>
					</c:if>

					<form:form action="uploadFeedbackQuestion" method="post"
						modelAttribute="feedback" enctype="multipart/form-data">
						<form:input type="hidden" value="${feedback.id}" path="id" />
						<div class="row">

							<div class="col-12">
								<div class="form-group">
									<label for="file">Upload Feedback Questions
										File(Excel):</label> <input id="file" name="file" type="file"
										class="form-control" />
								</div>
							</div>

							<div class="col-12">
								<div class="form-group">
									<label class="control-label" for="courses">Excel
										Format: </label>
									<p>description</p>
									<p>
										<b>Note:</b>
									<ul>
										<c:choose>
											<c:when test="${feedback.feedbackType eq 'it-feedback'}">
												<li>description is feedback question to be
													added(Mandatory)</li>
												<li>type : is a type of feedback question (SINGLESELECT
													OR MULTISELECT-MANDATORY)</li>
												<li>option 1-8 (option 1 & option 2 are mandatory)
											</c:when>
											<c:otherwise>
											<li>description is feedback question to be added</li>
											</c:otherwise>
										</c:choose>

										

									</ul>
									<br>
									<p>
									<c:choose>
											<c:when test="${feedback.feedbackType eq 'it-feedback'}">
												<a class="text-danger"
													href="resources/templates/feedbackItTemplate.xlsx">Download
													a sample template</a>
											</c:when>
											<c:otherwise>
												<a class="text-danger"
													href="resources/templates/feedbackQuestionTemplate.xlsx">Download
													a sample template</a>
											</c:otherwise>
										</c:choose>
									</p>
								</div>
							</div>


						</div>
						<div class="row">

							<div class="col-sm-12 column">
								<div class="form-group">

									<c:choose>
										<c:when test="${feedback.feedbackType eq 'it-feedback'}">
											<button id="submit" class="btn btn-large btn-success"
												formaction="uploadItFeedbackQuestions">Upload</button>
										</c:when>
										<c:otherwise>
											<button id="submit" class="btn btn-large btn-success"
												formaction="uploadFeedbackQuestion">Upload</button>
										</c:otherwise>
									</c:choose>

									<button id="cancel" class="btn btn-danger"
										formaction="homepage" formnovalidate="formnovalidate">Cancel</button>
								</div>
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