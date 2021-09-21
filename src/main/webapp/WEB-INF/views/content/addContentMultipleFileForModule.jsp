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
	<jsp:include page="../common/newLeftNavBarFaculty.jsp" />
	<jsp:include page="../common/newLeftSidebarFaculty.jsp" />
	<jsp:include page="../common/rightSidebarFaculty.jsp" />

	<!-- DASHBOARD BODY STARTS HERE -->
	<script
		src="https://cdnjs.cloudflare.com/ajax/libs/sweetalert/2.1.0/sweetalert.min.js"></script>




	<%
		boolean isEdit = "true".equals((String) request
				.getAttribute("edit"));
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
								Add/Edit File</li>
						</ol>
					</nav>

					<jsp:include page="../common/alert.jsp" />


					<!-- Input Form Panel -->
					<div class="card bg-white border">
						<div class="card-body">
							<%
								if (isEdit) {
							%>
							<h5 class="text-center pb-2 border-bottom">Edit File for
								${moduleName}</h5>
							<%
								} else {
							%>

							<h5 class="text-center pb-2 border-bottom">
								Add File
								<c:if test="${moduleName ne null}">For ${moduleName}</c:if>
							</h5>


							<%
								}
							%>
							<!-- <ul class="nav navbar-right panel_toolbox">
											<li><a class="collapse-link"><i
													class="fa fa-chevron-up"></i></a></li>
										</ul>
										<div class="clearfix"></div> -->

							<form:form action="createAssignment" id="createAssignment"
								method="post" modelAttribute="content"
								enctype="multipart/form-data">
								<fieldset>
								<form:input type="hidden" path="id" />
									<form:input path="courseId" type="hidden" />
									<form:hidden path="contentType" />
									<form:input path="courseId" type="hidden" />
									<form:hidden path="folderPath" />
									 <form:hidden path="parentContentId" />
									<%--<form:hidden path="moduleId" />
									<form:hidden path="acadYear" />
									<form:hidden path="acadMonth" /> --%>
								<%-- 	<c:if
										test="${content.moduleId ne null && not empty content.moduleId && content.parentModuleId ne null}">
										<form:input path="moduleId" type="hidden" />
										<form:input path="acadYear" type="hidden" />
									</c:if> --%>
									
							
									
									<%
										if (isEdit) {
									%>
									<form:input type="hidden" path="id" />
									<form:input type="hidden" path="filePath" />
									<form:hidden path="parentContentId" />
									<form:hidden path="moduleId" />
									<form:hidden path="acadYear" />
									<form:hidden path="acadMonth" />
									<form:input type="hidden" path="filePath" />
									<c:if test="${content.parentContentId ne null }">
										<%-- <form:input path="moduleId" type="hidden"/> --%>
										<form:input path="accessType" type="hidden"/>
										<%-- <form:input path="acadYear" type="hidden"/> --%>
								 	</c:if> 
									<%
										}else{
									%>
									<c:if test="${content.parentContentId ne null }">
										<%-- <form:input path="moduleId" type="hidden"/> --%>
										<form:input path="accessType" type="hidden"/>
										<%-- <form:input path="acadYear" type="hidden"/> --%>
								 	</c:if> 
								 	<%} %>
									
									
									
									<%
										if(!isEdit) {
									%>
									<c:choose>
									 <c:when test="${content.courseId ne null or content.parentModuleId ne null or content.parentContentId ne null}">
									 	<form:input type="hidden" path="acadYear" value="${content.acadYear}"/>
									 	<form:input type="hidden" path="idForModule" value="${content.moduleId}"/>
									 </c:when>
									 <c:otherwise>  
									<div class="row">
										<div class="col-md-6 col-sm-12">
											<div class="form-group">
												<form:label path="courseId" for="courseId">
													<strong>Academic year <span style="color: red">*</span></strong>
												</form:label>
												<form:select id="acadYear" path="acadYear"
													class="form-control" required="required">
													<option value="">Select Year</option>
													<c:forEach var="acadYear" items="${acadYear}"
														varStatus="status">
														<option value="${acadYear}">${acadYear}</option>
													</c:forEach>
												</form:select>
											</div>
										</div>
										<div class="col-md-6 col-sm-12">
											<div class="form-group">
												<form:label path="idForModule" for="idForModule">
													<strong>Module <span style="color: red">*</span></strong>
												</form:label>
												<form:select id="idOfModule" path="idForModule"
													class="form-control" required="required">
													<form:option value="">Select Module</form:option>
												</form:select>
											</div>
										</div>
									</div>
									</c:otherwise>
									</c:choose>
								<%--  </c:if>   --%>
									<%
										} 
									%>


									<%-- <div class="row">
													<div class="col-sm-4 column">
														<div class="form-group">
															<form:label path="acadMonth" for="acadMonth">Academic Month</form:label>
															<form:select id="acadMonth" path="acadMonth"
																class="form-control" required="required">
																<form:option value="">Select Academic Month</form:option>
																<form:options items="${acadMonths}" />
															</form:select>
														</div>
													</div>
													<div class="col-sm-4 column">
														<div class="form-group">
															<form:label path="acadYear" for="acadYear">Academic Year</form:label>
															<form:select id="acadYear" path="acadYear"
																class="form-control" required="required">
																<form:option value="">Select Academic Year</form:option>
																<form:options items="${acadYears}" />
															</form:select>
														</div>
													</div>
													<div class="col-sm-4 column">
														<div class="form-group">
															<form:label path="contentName" for="contentName">File Name</form:label>
															<form:input path="contentName" type="text"
																class="form-control" required="required" />

														</div>
													</div>

												</div> --%>
									<%-- <div class="row">
										<div class="col-md-6 col-sm-12">
											<div class="form-group">
												<form:label path="courseId" for="courseId">
													<strong>Academic year <span style="color: red">*</span></strong>
												</form:label>
												<form:select id="acadYear" path="acadYear"
													class="form-control" required="required">
													<option value="">Select Year</option>
													<c:forEach var="acadYear" items="${acadYear}"
														varStatus="status">
														<option value="${acadYear}">${acadYear}</option>
													</c:forEach>
												</form:select>
											</div>
										</div>
										<div class="col-md-6 col-sm-12">
											<div class="form-group">
												<form:label path="idForModule" for="idForModule">
													<strong>Module <span style="color: red">*</span></strong>
												</form:label>
												<form:select id="idOfModule" path="idForModule"
													class="form-control" required="required">
													<form:option value="">Select Module</form:option>
												</form:select>

											</div>
										</div> --%>
										
										
										<div class="row">
											<div class="col-md-6 col-sm-12">
												<div class="form-group">
													<form:label path="" for="file">
														<%
															if (isEdit) {
														%>
														<strong>Select File only if you wish to override
															earlier file <i class="fa fa-file-o"></i>
														</strong>
														<%
															} else {
														%>
														<strong>Select File from Computer <i
															class="fa fa-file-o"></i></strong>
														<%
															}
														%>

													</form:label>
													<input id="file" name="file" type="file"
														multiple="multiple" class="form-control" /><!--  <small
														class="text-danger">* Only .zip file is allowed</small> -->

												</div>
												<div id=fileSize></div>
												<ul id="output">
												</ul>
											</div>

											<%-- <div class="col-sm-4 column">
													<div class="form-group">
														<form:label path="accessType" for="accessType">Access Type</form:label>
														<c:if test="${ not empty content.accessType }">
															<form:hidden path="accessType" />
															: ${content.accessType}
														</c:if>
														<c:if test="${ empty content.accessType }">
														<form:select id="accessType" path="accessType"
															class="form-control" required="required">
															<form:option value="">Select Access Type</form:option>
															<form:option value="Public">Public</form:option>
															<form:option value="Private">Private</form:option>
														</form:select>
														</c:if>
													</div>
													</div> --%>
											<div class="col-md-6 col-sm-12">
												<%-- <div class="form-group">
															<form:label path="accessType" for="accessType">Access Type (Read Notes below)</form:label>
															<c:if test="${ not empty content.accessType }">
															<form:select id="accessType" path="accessType"
																class="form-control" required="required" itemValue="${content.accessType}">
																<form:option value="">Select Access Type</form:option>
																<form:option value="Public">Public</form:option>
																<form:option value="Private">Private</form:option>
															</form:select>
															
															
															
																<form:hidden path="accessType" />
															: ${content.accessType}
														</c:if>
															<c:if test="${ empty content.accessType }">
																<form:select id="accessType" path="accessType"
																	class="form-control" required="required"
																	itemValue="${content.accessType}">
																	<form:option value="">Select Access Type</form:option>
																	<form:option value="Public">Public</form:option>
																	<form:option value="Private">Private</form:option>
																</form:select>
															</c:if>
														</div> --%>

												<div class="form-group">
													<form:label path="accessType" for="accessType">
														<strong>Access Type (Read Notes below) <span
															style="color: red">*</span></strong>
													</form:label>
													<%-- <c:if test="${ not empty content.accessType }">
															<form:hidden path="accessType" />
															: ${content.accessType}
														</c:if>
														<c:if test="${ empty content.accessType }"> --%>
													<c:if test="${content.parentContentId eq null }">
												<form:select id="accessType" path="accessType"
													class="form-control" required="required"
													itemValue="${content.accessType}">
													<form:option value="">Select Access Type</form:option>
													<form:option value="Public">Public (Selective Users)</form:option>
													<form:option value="Private">Private</form:option>
													<form:option value="Everyone">Everyone (All Users)</form:option>
												</form:select>
												</c:if>
												<c:if test="${content.parentContentId ne null }">
												<form:select id="accessType" path="accessType"
													class="form-control" required="required"
													itemValue="${content.accessType}" disabled='true'>
													<form:option value="">Select Access Type</form:option>
													<form:option value="Public">Public (Selective Users)</form:option>
													<form:option value="Private">Private</form:option>
													<form:option value="Everyone">Everyone (All Users)</form:option>
												</form:select>
												</c:if>
													<%-- </c:if> --%>
												</div>
											</div>

											<div class="col-12">
												<div class="form-group">
													<form:label path="contentDescription"
														for="contentDescription">
														<strong>File Description</strong>
													</form:label>
													<form:textarea path="contentDescription"
														class="form-control" />

												</div>
											</div>


											<%-- 		<div
														class="col-lg-4 col-md-4 col-sm-12 mt-3 position-relative">
														<div class="input-group">
															<label class="f-13 text-danger req">*</label> <label
																class="sr-only">Select Date Range</label>
															<form:input type="hidden" id="testStartDate"
																path="startDate" />
															<form:input type="hidden" id="testEndDate" path="endDate" />
															<input id="startDate" type="text"
																placeholder="Start Date-End Date" class="form-control"
																required="required" />
															<div class="input-group-append">
																<button class="btn btn-outline-secondary" type="button"
																	id="testDateRangeBtn">
																	<i class="fas fa-calendar"></i>
																</button>
															</div>
														</div>
													</div> --%>


											<%
												if (isEdit) {
											%>

											<div class="col-md-6 col-sm12 position-relative">
												<label><strong>Select Date Range <span
														class="text-danger">*</span></strong></label>
												<div class="input-group">
													<form:input type="hidden" id="testStartDate"
														path="startDate" />
													<form:input type="hidden" id="testEndDate" path="endDate" />
													<input id="startDate" type="text"
														placeholder="Start Date - End Date" class="form-control"
														required="required"
														value="${content.startDate} - ${content.endDate}" readonly />
													<div class="input-group-append">
														<button class="btn btn-outline-secondary" type="button"
															id="testDateRangeBtn">
															<i class="fas fa-calendar"></i>
														</button>
													</div>
												</div>
											</div>

											<%
												} else {
											%>
											<div class="col-md-6 col-sm12 position-relative">
												<label><strong>Select Date Range <span
														class="text-danger">*</span></strong></label>
												<div class="input-group">
													<form:input type="hidden" id="testStartDate"
														path="startDate" />
													<form:input type="hidden" id="testEndDate" path="endDate" />
													<input id="startDate" type="text"
														placeholder="Start Date - End Date" class="form-control"
														required="required" />
													<div class="input-group-append">
														<button class="btn btn-outline-secondary" type="button"
															id="testDateRangeBtn">
															<i class="fas fa-calendar"></i>
														</button>
													</div>
												</div>
											</div>
											<%
												}
											%>


											<%-- <div class="col-sm-4 column">
														<div class="form-group">
															<form:label path="startDate" for="startDate">Display From <span style="color: red">*</span></form:label>


															<div class='input-group date' id='datetimepicker1'>
																<form:input id="startDate" path="startDate" type="text"
																	placeholder="Start Date" class="form-control"
																	required="required" readonly="true" />
																<span class="input-group-addon"><span
																	class="glyphicon glyphicon-calendar"></span> </span>
															</div>

														</div>
													</div>
													<div class="col-sm-4 column">
														<div class="form-group">
															<form:label path="endDate" for="endDate">Display Until <span style="color: red">*</span></form:label>

															<div class='input-group date' id='datetimepicker2'>
																<form:input id="endDate" path="endDate" type="text"
																	placeholder="End Date" class="form-control"
																	required="required" readonly="true" />
																<span class="input-group-addon"><span
																	class="glyphicon glyphicon-calendar"></span> </span>
															</div>

														</div>
													</div> --%>
											<sec:authorize access="hasAnyRole('ROLE_ADMIN')">
												<div class="col-md-6 col-sm12">
													<div class="form-group">

														<form:label path="courseId" for="courseId">Course</form:label>
														<form:select id="idForCourse" path="idForCourse"
															class="form-control">
															<form:option value="">Select Course</form:option>
															<c:forEach var="course" items="${courses}"
																varStatus="status">
																<form:option value="${course.id}">${course.courseName}</form:option>
															</c:forEach>
														</form:select>

													</div>
												</div>
											</sec:authorize>
										</div>

										<div class="form-row mt-5">
											<table
												class="table-bordered table-striped mt-5 font-weight-bold">
												<tbody>

													<tr>
														<td>Send Email Alert for New Content?</td>
														<td>
															<%
																if (isEdit) {
															%>
															<div class="pretty p-switch p-fill p-toggle">
																<form:checkbox value="${content.sendEmailAlert}"
																	id="sendEmailAlert" path="sendEmailAlert" />
																<div class="state p-success p-on">
																	<label>Yes</label>
																</div>
																<div class="state p-danger p-off">
																	<label>No</label>
																</div>

															</div> <%
 	} else {
 %>
															<div class="pretty p-switch p-fill p-toggle">
																<form:checkbox value="N" id="sendEmailAlert"
																	path="sendEmailAlert" />
																<div class="state p-success p-on">
																	<label>Yes</label>
																</div>
																<div class="state p-danger p-off">
																	<label>No</label>
																</div>

															</div> <%
 	}
 %>
														</td>
													</tr>
													<tr>
														<td>Send SMS Alert for New Content?</td>
														<td>
															<%
																if (isEdit) {
															%>
															<div class="pretty p-switch p-fill p-toggle">
																<form:checkbox value="${content.sendSmsAlert}"
																	id="sendSmsAlert" path="sendSmsAlert" />
																<div class="state p-success p-on">
																	<label>Yes</label>
																</div>
																<div class="state p-danger p-off">
																	<label>No</label>
																</div>

															</div> <%
 	} else {
 %>
															<div class="pretty p-switch p-fill p-toggle">
																<form:checkbox value="N" id="sendSmsAlert"
																	path="sendSmsAlert" />
																<div class="state p-success p-on">
																	<label>Yes</label>
																</div>
																<div class="state p-danger p-off">
																	<label>No</label>
																</div>

															</div> <%
 	}
 %>
														</td>
													</tr>
													<c:if test="${sendAlertsToParents eq true}">
														<tr>
															<td>Send Email Alert To Parents?</td>
															<td>
																<%
																	if (isEdit) {
																%>
																<div class="pretty p-switch p-fill p-toggle">
																	<form:checkbox
																		value="${content.sendEmailAlertToParents}"
																		id="sendEmailAlertParents"
																		path="sendEmailAlertToParents" />
																	<div class="state p-success p-on">
																		<label>Yes</label>
																	</div>
																	<div class="state p-danger p-off">
																		<label>No</label>
																	</div>

																</div> <%
 	} else {
 %>
																<div class="pretty p-switch p-fill p-toggle">
																	<form:checkbox value="N" id="sendEmailAlertParents"
																		path="sendEmailAlertToParents" />
																	<div class="state p-success p-on">
																		<label>Yes</label>
																	</div>
																	<div class="state p-danger p-off">
																		<label>No</label>
																	</div>

																</div> <%
 	}
 %>
															</td>
														</tr>
														<tr>
															<td>Send SMS Alert To Parents?</td>
															<td>
																<%
																	if (isEdit) {
																%>
																<div class="pretty p-switch p-fill p-toggle">
																	<form:checkbox value="${content.sendSmsAlertToParents}"
																		id="sendEmailAlertParents"
																		path="sendSmsAlertToParents" />
																	<div class="state p-success p-on">
																		<label>Yes</label>
																	</div>
																	<div class="state p-danger p-off">
																		<label>No</label>
																	</div>

																</div> <%
 	} else {
 %>
																<div class="pretty p-switch p-fill p-toggle">
																	<form:checkbox value="N" id="sendEmailAlertParents"
																		path="sendSmsAlertToParents" />
																	<div class="state p-success p-on">
																		<label>Yes</label>
																	</div>
																	<div class="state p-danger p-off">
																		<label>No</label>
																	</div>

																</div> <%
 	}
 %>
															</td>
														</tr>
													</c:if>

													<tr>
														<td>Make this content visible at the time of exam ?</td>
														<td>
															<%
																if (isEdit) {
															%>
															<div class="pretty p-switch p-fill p-toggle">
																<form:checkbox value="${content.examViewType}"
																	id="examViewType" path="examViewType" />
																<div class="state p-success p-on">
																	<label>Yes</label>
																</div>
																<div class="state p-danger p-off">
																	<label>No</label>
																</div>

															</div> <%
 	} else {
 %>
															<div class="pretty p-switch p-fill p-toggle">
																<form:checkbox value="N" id="examViewType"
																	path="examViewType" />
																<div class="state p-success p-on">
																	<label>Yes</label>
																</div>
																<div class="state p-danger p-off">
																	<label>No</label>
																</div>

															</div> <%
 	}
 %>
														</td>
													</tr>
												</tbody>
											</table>
										</div>
										<%-- <div class="row">
													<div class="col-sm-4 column">
														<div class="slider round">
															<form:label path="sendEmailAlert" for="sendEmailAlert">Send Email Alert for New Content?</form:label>
															<br>
															<form:checkbox path="sendEmailAlert" class="form-control"
																value="Y" data-toggle="toggle" data-on="Yes"
																data-off="No" data-onstyle="success"
																data-offstyle="danger" data-style="ios" data-size="mini" />
														</div>
													</div>
													<div class="col-sm-4 column">
														<div class="slider round">
															<form:label path="sendSmsAlert" for="sendSmsAlert">Send SMS Alert for New Content?</form:label>
															<br>
															<form:checkbox path="sendSmsAlert" class="form-control"
																value="Y" data-toggle="toggle" data-on="Yes"
																data-off="No" data-onstyle="success"
																data-offstyle="danger" data-style="ios" data-size="mini" />
														</div>
													</div>
													<c:if test="${sendAlertsToParents eq false}">
														<div class="col-sm-6 col-md-4 col-xs-12 column">
															<div class="slider round">
																<form:label path="sendEmailAlertToParents"
																	for="sendEmailAlertToParents">Send Email Alert To Parents?</form:label>
																<br>
																<form:checkbox path="sendEmailAlertToParents"
																	class="form-control" value="Y" data-toggle="toggle"
																	data-on="Yes" data-off="No" data-onstyle="success"
																	data-offstyle="danger" data-style="ios"
																	data-size="mini" />
															</div>
														</div>
													</c:if>


													<c:if test="${sendAlertsToParents eq false}">
														<div class="col-sm-6 col-md-4 col-xs-12 column">
															<div class="slider round">
																<form:label path="sendSmsAlertToParents"
																	for="sendSmsAlertToParents">Send SMS Alert To Parents?</form:label>
																<br>
																<form:checkbox path="sendSmsAlertToParents"
																	class="form-control" value="Y" data-toggle="toggle"
																	data-on="Yes" data-off="No" data-onstyle="success"
																	data-offstyle="danger" data-style="ios"
																	data-size="mini" />
															</div>
														</div>
													</c:if>

												</div> --%>
										<div class="row">
											<div class="col-sm-12 column">
												<div class="form-group font-weight-bold">
													<b>Note:</b>
													<ul>
														<li>If Access Type is set as Private, File cannot be
															viewed by any student. It can only be viewed by Faculty
															and Administrator</li>
														<li>If Access Type is set as Public, File can be
															shared with students, selectively.</li>
														<li>Once Public File is created, it must be shared by
															selecting Students to share with, it will not be visible
															by default to all students.</li>
														<li>If File is marked as Public, and shared with
															students, then everything within that File including
															Subfolders will be visible to students</li>

														<li>If File is marked as Everyone, it will be visible
															to every user of selected course. No allocation is needed
															here.</li>
													</ul>
												</div>
											</div>
										</div>



										<div class="col-12 text-center mt-5">
											<div class="form-group">

												<%
													if (isEdit) {
												%>
												<!-- <button id="submit" class="btn btn-large btn-primary"
													formaction="updateFile">Update File</button> -->
													<button id="submit" class="btn btn-large btn-primary"
													formaction="updateFileForModule">Update File</button>
												<%
													} else {
												%>
												<!-- <button id="submit" class="btn btn-large btn-primary"
													formaction="addMultipleFile">Add Files</button> -->
												 <button id="submit" class="btn btn-large btn-primary"
													formaction="addMultipleFileForModule">Add Files</button> 
												<%
													}
												%>
												<button id="cancel" class="btn btn-danger"
													formaction="homepage" formnovalidate="formnovalidate">Cancel</button>
											</div>
										</div>
								</fieldset>
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
				<script>
					$("#accessType")
							.on(
									'change',
									function() {

										//alert("Onchange Function called!");
										var selectedValue = $(this).val();
										if (selectedValue == 'Public') {
											swal("Once Public File is created, it must be 	shared by selecting Students to share with, it will not be visible by default to all students. If File is marked as Public, and shared with students, then everything within that File including Subfolders will be visible to students");

										}
										if (selectedValue == 'Private') {
											swal("If Access Type is set as Private, File	cannot be viewed by any student. It can only be viewed by Faculty and Administrator");

										}
										if (selectedValue == 'Everyone') {
											swal("If File is marked as Everyone, it will be visible to every user of selected course. No allocation is needed here.");

										}
									});
				</script>
				<script type="text/javascript">
					$('#file').bind(
							'change',
							function() {
								// alert('This file size is: ' + this.files[0].size/1024/1024 + "MB");
								var fileSize = this.files[0].size / 1024 / 1024
										+ " MB";
								$('#fileSize').html("File Size:" + (fileSize));
							});
				</script>
				<script type="text/javascript"
					src="<c:url value="/resources/js/customDateTimePicker.js" />"></script>


				<script>
					//$(document).ready(function() {
					/* $("#datetimepicker1").on("dp.change", function(e) {

						validDateTimepicks();
					}).datetimepicker({
						// minDate:new Date(),
						useCurrent : false,
						format : 'YYYY-MM-DD HH:mm:ss'
					});

					$("#datetimepicker2").on("dp.change", function(e) {

						validDateTimepicks();
					}).datetimepicker({
						// minDate:new Date(),
						useCurrent : false,
						format : 'YYYY-MM-DD HH:mm:ss'
					});

					function validDateTimepicks() {
						if (($('#startDate').val() != '' && $('#startDate')
								.val() != null)
								&& ($('#endDate').val() != '' && $('#endDate')
										.val() != null)) {
							var fromDate = $('#startDate').val();
							var toDate = $('#endDate').val();
							var eDate = new Date(fromDate);
							var sDate = new Date(toDate);
							if (sDate < eDate) {
								alert("endate cannot be smaller than startDate");
								$('#startDate').val("");
								$('#endDate').val("");
							}
						}
					} */
					//});
				</script>
				<script>
					$('input:file[multiple]')
							.change(
									function(e) {
										console.log(e.currentTarget.files);
										$('#output').empty();
										var numFiles = e.currentTarget.files.length;
										for (i = 0; i < numFiles; i++) {
											fileSize = parseInt(
													e.currentTarget.files[i].size,
													10) / 1024;
											filesize = Math.round(fileSize);
											$('<li />')
													.text(
															e.currentTarget.files[i].name)
													.appendTo($('#output'));
											$('<span />')
													.addClass('filesize')
													.text(
															'(' + filesize
																	+ 'kb)')
													.appendTo(
															$('#output li:last'));
										}
									});

					/* AJAX FOR ACAD YEAR */

					$("#acadYear")
							.on(
									'change',
									function() {

										console.log('Year Selected');

										var acadYear = $('#acadYear').val();
										console.log(acadYear);
										if (acadYear) {
											$
													.ajax({
														type : 'POST',
														url : '${pageContext.request.contextPath}/findModulesByUsernameAndAcadYear?acadYear='
																+ acadYear,
														success : function(data) {
															var json = JSON
																	.parse(data);

															var optionsAsString = "";
															optionsAsString = "<option selected='selected'>Select Module</option>";
															$('#idOfModule')
																	.find(
																			'option')
																	.remove();
															console.log(json);
															for (var i = 0; i < json.length; i++) {
																var idjson = json[i];
																console
																		.log(idjson);

																for ( var key in idjson) {
																	console
																			.log(key
																					+ ""
																					+ idjson[key]);
																	optionsAsString += "<option value='" +key + "'>"
																			+ idjson[key]
																			+ "</option>";
				<%--if (moduleId == idjson[key]) {
																			optionsAsString += "<option value='" +key + "' selected>"
																					+ idjson[key]
																					+ "</option>";

																		} else {
																			console
																					.log('else entered00');
																			optionsAsString += "<option value='" +key + "'>"
																					+ idjson[key]
																					+ "</option>";
																		} --%>
					}
															}
															console
																	.log("optionsAsString"
																			+ optionsAsString);

															$('#idOfModule')
																	.append(
																			optionsAsString);

														}
													});
										} else {
											var optionsAsString = "";
											optionsAsString = "<option selected='selected'>Select Module</option>";
											$('#idOfModule').find('option')
													.remove();
											$('#idOfModule').append(
													optionsAsString);
											console.log("no course");
										}

									});
					$('#idOfModule').trigger('change');
				</script>