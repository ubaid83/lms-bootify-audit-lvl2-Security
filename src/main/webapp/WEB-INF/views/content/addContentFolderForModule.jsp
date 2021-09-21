
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
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/sweetalert/2.1.0/sweetalert.min.js"></script>

<div class="d-flex dataTableBottom" id="facultyAssignmentPage">
	<jsp:include page="../common/newLeftNavBarFaculty.jsp" />
	<jsp:include page="../common/newLeftSidebarFaculty.jsp" />
	<jsp:include page="../common/rightSidebarFaculty.jsp" />

	<!-- DASHBOARD BODY STARTS HERE -->
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
								Add/Edit Folder Details</li>
						</ol>
					</nav>

					<jsp:include page="../common/alert.jsp" />


					<!-- Input Form Panel -->
					<div class="card bg-white border">
						<div class="card-body">
							<%
								if (isEdit) {
							%>
							<h5 class="text-center pb-2 border-bottom">Edit Folder for
								${moduleName}</h5>
							<%
								} else {
							%>

							<h5 class="text-center pb-2 border-bottom">
								Add Folder
								<c:if test="${moduleName ne null}">For ${moduleName}</c:if>
							</h5>


							<%
								}
							%>
							
							

							<form:form action="createAssignment" id="createAssignment"
								method="post" modelAttribute="content"
								enctype="multipart/form-data">
								<fieldset>
									<form:input path="folderPath" type="hidden"/>
									<form:input path="contentType" type="hidden"/>
									<form:input path="courseId" type="hidden" />
									<form:input path="parentContentId" type="hidden"/>
										
										
								 <c:if test="${content.parentContentId ne null }">
										<%--<form:input path="moduleId" type="hidden"/>--%>
										<form:input path="acadYear" type="hidden"/>
								 </c:if> 
									
									<%
										if (isEdit) {
									%>
									<form:input type="hidden" path="id" />
									<form:input type="hidden" path="acadYear" />
									<form:input type="hidden" path="moduleId" />
									<form:hidden path="acadMonth" />
									<form:hidden path="parentContentId" />
									<c:if test="${content.parentContentId ne null }">
										<form:input path="accessType" type="hidden"/>
								 	</c:if> 
									<%
										}else{
									%>
									<c:if test="${content.parentContentId ne null }">
										<form:input path="accessType" type="hidden"/>
								 	</c:if> 
								 	<%} %>

									<div class="row">

										<div class="col-md-3 col-sm-12">
											<div class="form-group">
												<form:label path="contentName" for="contentName">
													<strong>Folder Name <span style="color: red">*</span>
														<i class="fa fa-folder-o"></i></strong>
												</form:label>
												<form:input path="contentName" type="text"
													class="form-control" required="required" />
											</div>
										</div>
										<%
											if (!isEdit) {
										%>
										 <c:if test="${content.parentContentId eq null}">  
											<sec:authorize
												access="hasAnyRole('ROLE_ADMIN','ROLE_FACULTY')">

												<div class="col-md-3 col-sm-9">
													<div class="form-group">

														<form:label path="acadYear" for="acadYear">
															<strong>Academic year <span style="color: red">*</span></strong>
														</form:label>
														<form:select id="acadYear" path="acadYear"
															class="form-control" required="required">
															<c:if test="${content.acadYear eq null}">
																<option value="">Select Year</option>
															</c:if>
															<c:forEach var="acadYear" items="${acadYear}"
																varStatus="status">
																<c:if test="${content.acadYear ne null}">
																	<c:if test="${content.acadYear eq acadYear}">
																		<option value="${acadYear}" selected>${acadYear}</option>
																	</c:if>
																	<c:if test="${content.acadYear ne acadYear}">
																		<option value="${acadYear}">${acadYear}</option>
																	</c:if>
																</c:if>
																<c:if test="${content.acadYear eq null}">
																	<form:option value="${acadYear}">${acadYear}</form:option>
																</c:if>
															</c:forEach>
															<%-- 
														<c:forEach var="acadYear" items="${acadYear}"
															varStatus="status">
															<option value="${acadYear}">${acadYear}</option>
														</c:forEach> --%>
														</form:select>
													</div>
												</div>

												<div class="col-md-3 col-sm-9">
													<div class="form-group">
														<form:label path="idForModule" for="idForModule">
															<strong>Module <span style="color: red">*</span></strong>
														</form:label>
														<form:select id="idOfModule" path="idForModule"
															class="form-control" required="required">
															<c:if test="${content.moduleId eq null || content.moduleId eq ''}">
																<form:option value="">Select Module</form:option>
															</c:if>
															<c:forEach var="module" items="${modules}"
																varStatus="status">
																<c:if test="${module.moduleId ne null}">
																	<c:if test="${module.moduleId eq content.moduleId}">
																		<option value="${module.moduleId}" selected>${module.moduleName}</option>
																	</c:if>
																	<c:if test="${module.moduleId ne content.moduleId}">
																		<option value="${module.moduleId}">${module.moduleName}</option>
																	</c:if>
																</c:if>
																<c:if test="${module.moduleId eq null}">
																	<form:option value="${module.moduleId}">${module.moduleName}</form:option>
																</c:if>
															</c:forEach>
														</form:select>

													</div>
												</div>

											</sec:authorize>
									 </c:if> 
										<%
											}
										%>

										<div class="col-12">
											<div class="form-group">
												<form:label path="contentDescription"
													for="contentDescription">
													<strong>Content Description</strong>
												</form:label>
												<form:textarea path="contentDescription"
													class="form-control" />

											</div>
										</div>

										<div class="col-md-6 col-sm-12">
											<div class="form-group">

												<form:label path="accessType" for="accessType">
													<strong>Access Type (Read Notes below) <span
														style="color: red">*</span></strong>
												</form:label>
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

											</div>
										</div>




										<%
											if (isEdit) {
										%>

										<div class="col-md-6 col-sm-12 position-relative">
											<label><strong>Select Date Range <span
													style="color: red">*</span></strong></label>
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
										<div class="col-md-6 col-sm-12 position-relative">
											<label><strong>Select Date Range <span
													style="color: red">*</span></strong></label>
											<div class="input-group">
												<label class="f-13 text-danger req">*</label> <label
													class="sr-only">Select Date Range</label>
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


									</div>

									<div class="form-row">
										<table
											class="table table-bordered table-striped mt-5 font-weight-bold">
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
																	id="sendEmailAlertParents" path="sendSmsAlertToParents" />
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


									<div class="row">
										<div class="col-sm-12 column">
											<div class="form-group font-weight-bold">

												<b>Note:</b>
												<ul>
													<li>If Access Type is set as Private, folder cannot be
														viewed by any student. It can only be viewed by Faculty
														and Administrator</li>
													<li>If Access Type is set as Public, Folder can be
														shared with students, selectively.</li>
													<li>Once Public Folder is created, it must be shared
														by selecting Students to share with, it will not be
														visible by default to all students.</li>
													<li>If Folder is marked as Public, and shared with
														students, then everything within that folder including
														Subfolders will be visible to students</li>

													<li>If Folder is marked as Everyone, it will be
														visible to every user of selected course. No allocation is
														needed here.</li>
												</ul>

											</div>
										</div>
									</div>


									<div class="row">

										<div class="col-12 text-center mt-4">
											<div class="form-group">

												<%
													if (isEdit) {
												%>
												<button id="submit" class="btn btn-large btn-primary"
													formaction="updateFolderForModule">Update Folder</button>
												<%
													} else {
												%>
												<c:if test="">
												</c:if>
												<button id="submit" class="btn btn-large btn-primary"
													formaction="addFolderForModule">Create Folder</button>
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
											swal("Once Public Folder is created, it must be 	shared by selecting Students to share with, it will not be visible by default to all students. If Folder is marked as Public, and shared with students, then everything within that Folder including Subfolders will be visible to students");

										}
										if (selectedValue == 'Private') {
											swal("If Access Type is set as Private, Folder	cannot be viewed by any student. It can only be viewed by Faculty and Administrator");

										}
										if (selectedValue == 'Everyone') {
											swal("If Folder is marked as Everyone, it will be visible to every user of selected course. No allocation is needed here.");

										}
									});
				</script>

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

					$("#idOfModule")
							.on(
									'change',
									function() {

										console.log('Course Selected');
										var acadYear = $('#acadYear').val();
										var moduleId = $('#idOfModule').val();
										console.log(acadYear);
										if (acadYear) {
											$
													.ajax({
														type : 'POST',
														url : '${pageContext.request.contextPath}/findCoursesByModuleIdAndUsernameAndAcadYear?acadYear='
																+ acadYear
																+ "&moduleId="
																+ moduleId,
														success : function(data) {
															var json = JSON
																	.parse(data);

															var optionsAsString = "";
															optionsAsString = "<option selected='selected' value='' disabled>Select Course</option>";
															$('#idOfCourse')
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

															$('#idOfCourse')
																	.append(
																			optionsAsString);

														}
													});
										} else {
											var optionsAsString = "";
											optionsAsString = "<option selected='selected'>Select Course</option>";
											$('#idOfCourse').find('option')
													.remove();
											$('#idOfCourse').append(
													optionsAsString);
											console.log("no course");
										}

									});
					$('#idOfCourse').trigger('change');
				</script>