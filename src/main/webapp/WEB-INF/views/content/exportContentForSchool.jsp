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
<link href="${pageContext.request.contextPath}/resources/logCss/select2.min.css" rel="stylesheet">
<jsp:include page="../common/newDashboardHeader.jsp" />

<div class="d-flex dataTableBottom" id="facultyAssignmentPage">
	<jsp:include page="../common/newLeftNavBarFaculty.jsp" />
	<jsp:include page="../common/newLeftSidebarFaculty.jsp" />
	<jsp:include page="../common/rightSidebarFaculty.jsp" />

	<!-- DASHBOARD BODY STARTS HERE -->
	<%
	boolean isEdit = "true".equals((String) request.getAttribute("edit"));
	%>
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
								Export Content</li>
						</ol>
					</nav>

					<jsp:include page="../common/alert.jsp" />


					<!-- Input Form Panel -->
					<div class="card bg-white border">
						<div class="card-body">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">

									<!-- <div class="x_title">
										<h5>Export Content</h5>
										
									</div>
 -->
									<div class="x_content">
										<%-- <form:form action="exportContentToOtherSchool" id="exportContentToSchool"
											method="post" modelAttribute="content"
											enctype="multipart/form-data"> --%>
										<fieldset>
											<%-- <form:input type="hidden" path="id" />
												<form:input path="courseId" type="hidden" />
												<form:hidden path="contentType" />
												<form:hidden path="folderPath" />
												<form:hidden path="parentContentId" />
												<form:input path="acadMonth" type="hidden" />
												 <form:input path="acadYear" type="hidden" /> 
												<form:hidden path="accessType" /> --%>

											<%-- 		<c:url value="/getContentUnderAPathForFaculty"
													var="rootFolder">
													<c:param name="courseId" value="${content.courseId}" />
													<c:param name="acadMonth" value="${content.acadMonth}" />
													<c:param name="acadYear" value="${content.acadYear}" />
												</c:url> --%>

											<c:url value="/getContentUnderAPathForFacultyForModule"
												var="rootFolder">
												<%-- <c:param name="courseId" value="${content.courseId}" />
													<c:param name="acadMonth" value="${content.acadMonth}" />
													<c:param name="acadYear" value="${content.acadYear}" /> --%>
											</c:url>

											<div class="x_title">
												<h5>Export content With School</h5>

											</div>
											<div class="x_content">
												<form:form action="exportContentToOtherSchool"
													id="exportContentToOtherSchool" method="post"
													modelAttribute="content">
													<fieldset>

														<form:input path="id" type="hidden" />
														<form:input path="contentType" type="hidden" />

														<div class="col-md-12 col-sm-12">
															<div class="form-group">
																	<form:label path="schoolToExport" for="schoolToExport">
														School Name <span style="color: red">*</span>
																	</form:label>
																	<form:select id="schoolToExport" path="schoolToExport"
																		class="form-control " required="required">
																		<option value="">Select School</option>
																		<c:forEach var="school" items="${schoolListMap}"
																			varStatus="status">
																			<%-- <c:if test="${school.abbr != appName}"> --%>
																			<option value="${school.abbr}">${school.collegeName}</option>
																			<%-- </c:if> --%>
																		</c:forEach>
																	</form:select>
																</div>
														</div>
														<div class="col-md-12 col-sm-12">
															<div class="form-group">
																	<form:label path="acadYearToExport"
																		for="acadYearToExport">
														Academic year <span style="color: red">*</span>
																	</form:label>
																	<form:select id="acadYearToExport"
																		path="acadYearToExport" class="form-control "
																		required="required">
																		<option value="">Select Year</option>
																		<c:forEach var="acadYear" items="${acadYear}"
																			varStatus="status">
																			<form:option value="${acadYear}">${acadYear}</form:option>
																			<%-- <option value="${acadYear}">${acadYear}</option> --%>
																		</c:forEach>
																	</form:select>
																</div>
														</div>
														<div class="col-md-12 col-sm-12">
															<div class="form-group">
																<form:label path="programId" for="programId">Select Program<span
																		style="color: red">*</span>
																</form:label>
																<form:select id="programId" path="programId"
																	class="form-control" required="required">
																	<form:option value="">Select Program</form:option>
																	<c:forEach var="prog" items="${programList}"
																		varStatus="status">
																		<form:option value="${prog.id}">${prog.programName}</form:option>
																	</c:forEach>
																</form:select>
															</div>
														</div>
														<div class="col-md-12 col-sm-12">
															<div class="form-group">
																<div class="form-group">
																	<form:label path="campusId" for="campusId">
														Campus 
													</form:label>
																	<form:select id="campusId" path="campusId"
																		class="form-control">
																		<option value="">Select Campus</option>
																		<c:forEach var="campus" items="${allCampuses}"
																			varStatus="status">
																			<%-- <c:if test="${school.abbr != appName}"> --%>
																			<option value="${campus.campusId}">${campus.campusName}</option>
																			<%-- </c:if> --%>
																		</c:forEach>
																	</form:select>
																</div>
															</div>
														</div>



														<div class="col-md-12 col-sm-12">
															<div class="form-group">
																<form:label path="courseIdToExport"
																	for="courseIdToExport">Courses <span
																		style="color: red">*</span>
																</form:label>
																<form:select id="courses" path="courseIdToExport"
																	class="form-control" required="required" data-live-search="true">
																	<form:option value="">Select Course</form:option>

																	<c:forEach var="courseItem" items="${courseList}"
																		varStatus="status">
																		<form:option value="${courseItem.id}">${courseItem.courseName}</form:option>
																	</c:forEach>

																</form:select>
															</div>
														</div>
														<div class="col-md-12 col-sm-12">
															<div class="form-group">
																<form:label path="facultyId" for="facultyId">Faculty <span
																		style="color: red">*</span>
																</form:label>
																<form:select id="faculty" path="facultyId"
																	class="form-control" multiple="multiple"
																	required="required">
																	<form:option value="">Select Faculty</form:option>
																</form:select>
															</div>
														</div>

														<div clss="col-12">
															<strong>Note: This content will be visible to
																all students which are associated with selected course
																after exporting content.</strong>
														</div>
														<div class="col-12">
															<div class="form-group">

																<button id="submit" class="btn btn-success mt-3"
																	onclick="return clicked();">Export Content
																	with School</button>
																<!-- <button id="cancel" class="btn btn-danger mt-3"
															formaction="homepage" formnovalidate="formnovalidate">Cancel</button> -->
															</div>
														</div>
													</fieldset>
											</div>
											<div class="row">

												<div class="col-sm-8 column">


													<div class="form-group">


														<sec:authorize access="hasAnyRole('ROLE_ADMIN')">
															<button id="submit" class="btn btn-xs btn-primary "
																formaction="${rootFolderForAdmin}">Back to Root
																Folder</button>
														</sec:authorize>
														<sec:authorize access="hasAnyRole('ROLE_FACULTY')">
															<button id="backToRoot" class="btn btn-xs btn-primary "
																formaction="${rootFolder}">Back to Root Folder</button>
														</sec:authorize>

														<button id="cancel" class="btn btn-xs btn-danger"
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


				</div>


				<%-- 	<jsp:include page="../common/paginate.jsp">
							<jsp:param name="baseUrl" value="searchFacultyGroups" />
						</jsp:include> --%>

				<!-- /page content: END -->


				<!-- SIDEBAR START -->
				<jsp:include page="../common/newSidebar.jsp" />
				<!-- SIDEBAR END -->
				<jsp:include page="../common/footer.jsp" />
				<script>
					$(document).ready(function() {
						// Initialize select2
						$('#schoolToExport').select2();
						$("#courses").select2();
						$('#programId').select2();
						
						$('#reset').on('click', function() {
							$('#assid').empty();
							var optionsAsString = "";
							optionsAsString = "<option value=''>Select Course</option>";
							$('#assid').append(optionsAsString);
							$("#programId").each(function() {
								this.selectedIndex = 0
							});

						});

						$("#programId").on('change', function() {
							var acadYear = $('#acadYearToExport').val();
							var campusId = $('#campusId').val();
							var programid = $(this).val();
							var schoolName = $('#schoolToExport').val();
							if (programid) {
								$.ajax({
									type : 'GET',
									url : '${pageContext.request.contextPath}/getCourseByProgramIdOfSchool?' + 'programId=' + programid
											+ '&acadYear=' + acadYear + '&schoolName=' + schoolName
											+ '&campusId=' + campusId,
									success : function(data) {
											var json = JSON.parse(data);
											var optionsAsString = "";
											$('#courses').find('option').remove();
											optionsAsString = "<option selected='selected'>Select course</option>";
											for (var i = 0; i < json.length; i++) {
												var idjson = json[i];
												for ( var key in idjson) {
													optionsAsString += "<option value='" +key + "'>" + idjson[key] + "</option>";
												}
											}
											$('#courses').append(optionsAsString);
									}
								});
							}
						});
						$('#programId').trigger('change');
						
					});

					$("#schoolToExport").on('change', function() {

						var schoolName = $('#schoolToExport').val();
						var campusId = $('#campusId').val();
						$.ajax({
							type : 'POST',
							url : '${pageContext.request.contextPath}/getProgramsOfSchool?schoolName=' + schoolName,
							success : function(data) {
									var json = JSON.parse(data);
									var optionsAsString = "";
									optionsAsString = "<option selected='selected'>Select Program</option>";
									$('#programId').find('option').remove();
									for (var i = 0; i < json.length; i++) {
										var idjson = json[i];
										for ( var key in idjson) {
											optionsAsString += "<option value='" +key + "'>" + idjson[key] + "</option>";
										}
									}
									$('#programId').append(optionsAsString);
							}
						});
						$.ajax({
							type : 'POST',
							url : '${pageContext.request.contextPath}/getProgramCampusOfSchool?schoolName=' + schoolName,
							success : function(data) {
									var json = JSON.parse(data);
									var optionsAsString = "";
									optionsAsString = "<option selected='selected'>Select Campus</option>";
									$('#campusId').find('option').remove();
									for (var i = 0; i < json.length; i++) {
										var idjson = json[i];
										for ( var key in idjson) {
											optionsAsString += "<option value='" +key + "'>" + idjson[key] + "</option>";
										}
									}
									$('#campusId').append(optionsAsString);
							}
						});

					});

					$("#campusId").on('change', function() {
						var acadYear = $('#acadYearToExport').val();
						var campusId = $('#campusId').val();
						var programid = $('#programId').val();
						var schoolName = $('#schoolToExport').val();
						var campusId = $('#campusId').val();
						$.ajax({
							type : 'POST',
							url : '${pageContext.request.contextPath}/getCourseByProgramIdOfSchool?' + 'programId='+ programid
									+ '&acadYear='+ acadYear+ '&schoolName='+ schoolName+ '&campusId='+ campusId,
							success : function(data) {
									var json = JSON.parse(data);
									var optionsAsString = "";
									optionsAsString = "<option selected='selected'>Select course</option>";
									$('#courses').find('option').remove();
									for (var i = 0; i < json.length; i++) {
										var idjson = json[i];
										for ( var key in idjson) {
											optionsAsString += "<option value='" +key + "'>" + idjson[key] + "</option>";
										}
									}
									$('#courses').append(optionsAsString);
									$('#faculty').find('option').remove();
								}
						});
					});
					$("#courses").on('change', function() {
						var acadYear = $('#acadYearToExport').val();
						var campusId = $('#campusId').val();
						var programid = $('#programId').val();
						var schoolName = $('#schoolToExport').val();
						var campusId = $('#campusId').val();
						var courseId = $('#courses').val();
						$.ajax({
							type : 'POST',
							url : '${pageContext.request.contextPath}/getFacultyOfCourseFromOtherSchool?'+ 'programId=' + programid
									+ '&acadYear=' + acadYear + '&schoolName=' + schoolName + '&campusId=' + campusId + '&courseId=' + courseId,
							success : function(data) {
									var json = JSON.parse(data);
									var optionsAsString = "";
									optionsAsString = "<option selected='selected'>Select Faculty</option>";
									$('#faculty').find('option').remove();
									for (var i = 0; i < json.length; i++) {
										var idjson = json[i];
										optionsAsString += "<option value='" +idjson.username + "'>" + idjson.username + " ("
													+ idjson.firstname + " " + idjson.lastname + ") " + "</option>";
									}
									$('#faculty').append(optionsAsString);
							}
						});
					});
				</script>