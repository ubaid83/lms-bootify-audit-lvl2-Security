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

<%
						boolean isEdit = "true".equals((String) request
								.getAttribute("edit"));
					%>

<div class="d-flex dataTableBottom" id="facultyAssignmentPage">
	<jsp:include page="../common/newLeftNavBarFaculty.jsp" />
	<jsp:include page="../common/newLeftSidebarFaculty.jsp" />
	<jsp:include page="../common/rightSidebarFaculty.jsp" />

	<!-- DASHBOARD BODY STARTS HERE -->

	<div class="container-fluid m-0 p-0 dashboardWraper">

		<jsp:include page="../common/newTopHeaderFaculty.jsp" />

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
							<li class="breadcrumb-item active" aria-current="page">
                        <% if (isEdit) {  %>
                        Edit Group
                        <% } else {%> 
								Create Group
							<% } %>	
								</li>
						</ol>
					</nav>

					<jsp:include page="../common/alert.jsp" />

					

					<!-- Input Form Panel -->
					<div class="card bg-white border">
						<div class="card-body">
									<div class="x_title">
										<c:if test="${showDropDown}">
											<h5 class="text-center border-bottom pb-2">Create Group</h5>
										</c:if>
										<c:if test="${showDropDown ne 'true'}">

											<h5 class="text-center border-bottom pb-2">
											<% if (isEdit) {  %> Edit Group <%} else {%> Create Group <%} %>
											for ${groups.course.courseName }
											
											</h5>
											
											
										</c:if>
										<c:if test="${showRandom}">
											<%-- <div class="col-12 text-center">
												<div class="form-group">
													<a href="createRandomGroupsForm?courseId=${courseId}"><i
														class="btn btn-large btn-dark">Create Groups
															Randomly</i></a>
												</div>
											</div> --%>
										</c:if>

									</div>
										<form:form action="createGroup" id="createGroup" method="post"
											modelAttribute="groups" enctype="multipart/form-data">

											<form:input path="courseId" type="hidden" />
											<%
												if (isEdit) {
											%>
											<form:input type="hidden" path="id" />
											<%
												}
											%>

											<div class="row">
												<%-- <div class="col-sm-6 col-md-4 col-xs-12 column">
							<div class="form-group">
								<form:label path="acadMonth" for="acadMonth">Academic Month</form:label>
								<form:select id="acadMonth" path="acadMonth"
									class="form-control" required="required">
									<form:option value="">Select Academic Month</form:option>
									<form:options items="${acadMonths}" />
								</form:select>
							</div>
						</div>
						<div class="col-sm-6 col-md-4 col-xs-12 column">
							<div class="form-group">
								<form:label path="acadYear" for="acadYear">Academic Year</form:label>
								<form:select id="acadYear" path="acadYear" class="form-control"
									required="required">
									<form:option value="">Select Academic Year</form:option>
									<form:options items="${acadYears}" />
								</form:select>
							</div>
						</div> --%>
												<div class="col-md-6 col-sm-12">
													<div class="form-group">
														<form:label class="textStrong" path="groupName" for="groupName">Group Name <span
																style="color: red">*</span>
														</form:label>
														<form:input path="groupName" type="text"
															class="form-control" required="required" />
													</div>
												</div>
												
												<c:if test="${showDropDown}">
												<div class="col-md-6 col-sm-12">
													<div class="form-group">
															<form:label class="textStrong" path="courseId" for="courseId">Course <span
																	style="color: red">*</span>
															</form:label>
															<form:select id="idForCourse" path="idForCourse"
																class="form-control" required="required">
																<form:option value="">Select Course</form:option>
																<c:forEach var="course" items="${courses}"
																	varStatus="status">
																	<form:option value="${course.id}">${course.courseName}</form:option>
																</c:forEach>
															</form:select>
													</div>
												</div>
												</c:if>
												
												<c:if test="${edit eq true}">
												<div class="col-md-6 col-sm-12">
													<div class="form-group">
															<form:label class="textStrong" path="courseIDS" for="courseIDS">Course <span
																	style="color: red">*</span>
															</form:label>
															<form:select id="courseIDS" path="courseIDS"
																multiple="multiple" class="form-control"
																required="required">
																<form:option value="">Select Course</form:option>
																<c:forEach var="course" items="${courses}"
																	varStatus="status">
																	<form:option value="${course.id}">${course.courseName}</form:option>
																</c:forEach>
															</form:select>
													</div>
												</div>
												</c:if>


												<div class="col-md-6 col-sm-12">
													<div class="form-group">
														<form:label class="textStrong" path="noOfStudents" for="noOfStudents">Number of students <span
																style="color: red">*</span>
														</form:label>
														<form:input path="noOfStudents" type="number"
															class="form-control" required="required" min="0" />
													</div>
												</div>
											</div>


											<hr>


											<div class="col-12 p-0">
												<form:label class="textStrong" path="group_details" for="editor">Group Description:</form:label>
												<%-- <form:textarea class="form-group" path="group_details"
													id="editor" style="margin-top: 30px;margin-bottom:10px" /> --%>
												<form:textarea class="testDesc form-group" path="group_details"
													name="editor1" id="editor1" rows="10" cols="80" />

											</div>


											<hr>
											<div class="row">

												<div class="col-sm-8">
													<div class="form-group">

														<%
															if (isEdit) {
														%>
														<button id="submit" class="btn btn-large btn-success"
															formaction="updateGroup">Update Group</button>
														<%
															} else {
														%>
														<button id="submit" class="btn btn-large btn-success"
															formaction="createGroup">Create Group</button>
														<%
															}
														%>
														<button id="cancel" class="btn btn-danger"
															formaction="homepage" formnovalidate="formnovalidate">Cancel</button>
														<!-- <button id="cancel" class="btn btn-danger" type="button"
															formaction="homepage" formnovalidate="formnovalidate"
															onclick="history.go(-1);">Back</button> -->
													</div>
												</div>
											</div>




										</form:form>
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
				<script type="text/javascript"
					src="https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.0/MathJax.js?config=TeX-AMS_HTML"></script>