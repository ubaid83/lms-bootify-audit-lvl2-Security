<%@page import="com.spts.lms.web.utils.Utils"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@page import="java.util.*"%>



<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<jsp:include page="../common/newDashboardHeader.jsp" />

<div class="d-flex dataTableBottom" id="facultyAssignmentPage">
	<sec:authorize access="hasRole('ROLE_FACULTY')">
		<jsp:include page="../common/newLeftNavBarFaculty.jsp" />
		<jsp:include page="../common/newLeftSidebarFaculty.jsp" />
		<jsp:include page="../common/rightSidebarFaculty.jsp" />
	</sec:authorize>
	<sec:authorize access="hasRole('ROLE_ADMIN')">

		<jsp:include page="../common/newAdminLeftNavBar.jsp" />
		<jsp:include page="../common/rightSidebarAdmin.jsp" />
	</sec:authorize>
	<!-- DASHBOARD BODY STARTS HERE -->

	<div class="container-fluid m-0 p-0 dashboardWraper">

		
		<sec:authorize access="hasRole('ROLE_FACULTY')">
			<jsp:include page="../common/newTopHeaderFaculty.jsp" />
		</sec:authorize>
		<sec:authorize access="hasRole('ROLE_ADMIN')">
			<jsp:include page="../common/newAdminTopHeader.jsp" />
		</sec:authorize>

		<div class="container mt-5">
			<div class="row">
				<!-- SEMESTER CONTENT -->
				<div
					class="col-lg-9 col-md-9 col-sm-12 dashboardBody ml-auto mr-auto">

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
							<li class="breadcrumb-item active" aria-current="page">Upload Test-Question Pools</li>
						</ol>
					</nav>

					<jsp:include page="../common/alert.jsp" />


					<!-- Input Form Panel -->
				

					<!-- Results Panel -->


					<div class="card bg-white border">
						<div class="card-body">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">
										<h5 class="text-center pb-2 border-bottom">Upload Test Question Pools</h5>


									<div class="x_content">
										<form:form action="uploadTestQuestionPool" method="post"
											modelAttribute="testPool" enctype="multipart/form-data">
											<form:input type="hidden" value="${testPool.id}" path="id" />
											<div class="row">

												<div class="col-sm-6 column">
													<div class="form-group">
														<label for="file">Upload Test Question Pools
															File(Excel):</label> <input id="file" name="file" type="file"
															class="form-control" />
													</div>
												</div>
												
												<div class="col-sm-6 column">
													<div class="form-group">
														<a
															href="configureImageTestQuestionPoolForm?testPoolId=${testPool.id}"><i
															class="btn btn-large btn-primary"
															style="float: right; font-size: 15px;">Configure
																Questions Manually</i></a>
																
																
													</div>
												</div>
												
												
												<div class="col-12">
													<div class="form-group">

														<button id="submit" class="btn btn-large btn-primary"
															formaction="uploadTestQuestionPool">Upload</button>

														<button id="cancel" class="btn btn-danger"
															formaction="homepage" formnovalidate="formnovalidate">Cancel</button>
													</div>
													
														<p>
															<a
																href="resources/templates/testQuestionPoolTemplate.xlsx"><font
																color="red">Download a sample template</font></a>
														</p>
												</div>

												<div class="col-12 mt-3">
													<div class="form-group">


														<label class="control-label" for="courses"><strong>Excel
															Format:</strong> </label>
														<p>Description | Marks | Type |Test Type|
															Question-Type | Options Shuffle Required | Range From |
															Range To | option 1 | option 2 | option 3 | option 4 |
															option 5 | option 6 | option 7 | option 8 | correctOption</p>
														<p>
															<br> <br> <b><font color="red">*</font>Note:</b>
														<ul>
															<label>1) Please Fill the Following Field If the
																test is Objective + MCQ</label>
															<br>

															<p>i) Description | Marks | Type |Test Type|
																Question-Type | Options Shuffle Required | option 1 |
																option 2 | option 3 | option 4 | option 5 | option 6 |
																option 7 | option 8 |</p>
															<br>
															<label>2) Please Fill the Following Field If the
																test is Subjective</label>

															<p>Description | Marks | TestType | Question-Type</p>
															<br>

															<label>3) Please Fill the Following Field If the
																test is Objective + Numeric </label>
															<p>i) Description | Marks | Test Type| Question-Type
																| Range From | Range To | correctOption</p>


														</ul>




													</div>
												</div>



											</div>
										</form:form>
									</div>
								</div>
							</div>
						</div>
					</div>


					<%-- 	<jsp:include page="../common/paginate.jsp">
							<jsp:param name="baseUrl" value="searchFacultyGroups" />
						</jsp:include> --%>

					<!-- /page content: END -->

				</div>

				<!-- SIDEBAR START -->
				<sec:authorize access="hasRole('ROLE_FACULTY')">
					<jsp:include page="../common/newSidebar.jsp" />
				</sec:authorize>
				<!-- SIDEBAR END -->
				<sec:authorize access="hasRole('ROLE_FACULTY')">
					<jsp:include page="../common/footer.jsp" />
				</sec:authorize>
				<sec:authorize access="hasRole('ROLE_ADMIN')">
					<jsp:include page="../common/newAdminFooter.jsp" />
				</sec:authorize>