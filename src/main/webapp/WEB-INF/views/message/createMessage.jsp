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
		<jsp:include page="../common/newLeftNavBarFaculty.jsp"/>
		<jsp:include page="../common/newLeftSidebarFaculty.jsp"/>
		<jsp:include page="../common/rightSidebarFaculty.jsp"/>
	</sec:authorize>
	<sec:authorize access="hasRole('ROLE_STUDENT')">
		<jsp:include page="../common/newLeftNavbar.jsp" />
		<jsp:include page="../common/newLeftSidebar.jsp" />
		<jsp:include page="../common/rightSidebar.jsp" />
	</sec:authorize>

	<%
		boolean isEdit = "true".equals((String) request
				.getAttribute("edit"));
	%>

	<!-- DASHBOARD BODY STARTS HERE -->

	<div class="container-fluid m-0 p-0 dashboardWraper">

		<sec:authorize access="hasRole('ROLE_FACULTY')">
			<jsp:include page="../common/newTopHeaderFaculty.jsp" />
		</sec:authorize>
		<sec:authorize access="hasRole('ROLE_STUDENT')">
			<jsp:include page="../common/newTopHeader.jsp" />
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
							<li class="breadcrumb-item active" aria-current="page">Create Message</li>
						</ol>
					</nav>

					<jsp:include page="../common/alert.jsp" />


					<!-- Input Form Panel -->
					<div class="card bg-white border">
						<div class="card-body">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">

									<div class="x_title">
										<h5>Create Message</h5>
										
									</div>

									<div class="x_content">
										<form:form action="createMessage" id="createMessage"
											method="post" modelAttribute="message"
											enctype="multipart/form-data">
											<fieldset>

												<form:input path="courseId" type="hidden"
													value="${message.courseId}" />
												<%
													if (isEdit) {
												%>
												<form:input type="hidden" path="id" />
												<%
													}
												%>

												<sec:authorize access="hasAnyRole('ROLE_USER')">

													<div class="col-sm-4 column">
														<div class="form-group">
															<c:if test="${showDropDown}">
																<form:label path="courseId" for="courseId">Course<span style="color: red">*</span></form:label>
																<form:select id="idForCourse" path="idForCourse"
																	class="form-control" required="required">
																	<form:option value="">Select Course</form:option>
																	<c:forEach var="course" items="${courses}"
																		varStatus="status">
																		<form:option value="${course.id}">${course.courseName}</form:option>
																	</c:forEach>
																</form:select>
															</c:if>
														</div>
													</div>


												</sec:authorize>

												<div class="col-sm-12 column">
													<div class="form-group">

														<form:label path="subject" for="subject">Subject <span
																style="color: red">*</span>
														</form:label>
														<form:textarea required="required" class="form-control"
															path="subject" id="subject" rows="2" />

													</div>
													<div class="form-group">

														<form:label path="description" for="description" >Description</form:label>
														<%-- <form:textarea required="required" class="form-control" path="description" id="description" rows="10"/> --%>
														<form:textarea class="form-group testDesc" path="description"
															name="editor1" id="editor1" rows="10" cols="80" />
													</div>

												</div>
												<hr>
												<div class="row">

													<div class="col-sm-8 column">
														<div class="form-group">

															<%
																if (isEdit) {
															%>
															<button id="submit" class="btn btn-large btn-primary"
																formaction="updateGroup">Update Group</button>
															<%
																} else {
															%>
															<button id="submit" class="btn btn-large btn-primary"
																formaction="createMessage">Create Message</button>
															<%
																}
															%>
															<button id="cancel" class="btn btn-danger"
																formaction="homepage" formnovalidate="formnovalidate">Cancel</button>
														</div>
													</div>
												</div>



											</fieldset>
										</form:form>

									</div>
								</div>
							</div>
						</div>
					</div>

					<!-- Results Panel -->


					


					<%-- 	<jsp:include page="../common/paginate.jsp">
							<jsp:param name="baseUrl" value="searchFacultyGroups" />
						</jsp:include> --%>

					<!-- /page content: END -->

				</div>

				<!-- SIDEBAR START -->
				<jsp:include page="../common/newSidebar.jsp" />
				<!-- SIDEBAR END -->
				<jsp:include page="../common/footer.jsp" />