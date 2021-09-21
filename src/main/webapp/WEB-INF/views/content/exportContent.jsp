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
<jsp:include page="../common/newLeftNavBarFaculty.jsp"/>
<jsp:include page="../common/newLeftSidebarFaculty.jsp"/>
<jsp:include page="../common/rightSidebarFaculty.jsp"/>

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
                    <div class="col-lg-9 col-md-9 col-sm-12 dashboardBody ml-auto mr-auto">
                        
          <!-- page content: START -->
          
                        <nav aria-label="breadcrumb">
                            <ol class="breadcrumb">
                                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/homepage">Dashboard</a></li>
                                <li class="breadcrumb-item" aria-current="page"><c:out value="${Program_Name}" /></li>
                                <sec:authorize access="hasRole('ROLE_STUDENT')">
								<c:out value="${AcadSession}" />
							</sec:authorize>
                                <li class="breadcrumb-item active" aria-current="page"> Export Content </li>
                            </ol>
                        </nav>
                        
						<jsp:include page="../common/alert.jsp" />
						
						
						<!-- Input Form Panel -->
						<div class="card bg-white border">
						<div class="card-body">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">

									<div class="x_title">
										<h5>Export Content</h5>
										
									</div>

									<div class="x_content">
										<form:form action="exportContent" id="exportContent"
											method="post" modelAttribute="content"
											enctype="multipart/form-data">
											<fieldset>
												<form:input type="hidden" path="id" />
												<%-- <form:input path="courseId" type="hidden" /> --%>
												<form:hidden path="contentType" />
												<form:hidden path="folderPath" />
												<form:hidden path="parentContentId" />
												<form:input path="acadMonth" type="hidden" />
												<%--  <form:input path="acadYear" type="hidden" />  --%>
												<form:hidden path="accessType" />

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
												<c:url value="/exportContentToOtherSchoolForm"
													var="exportToSchool">
													<form:input type="hidden" path="id" /> 
												</c:url>
												<c:url value="/getContentUnderAPath"
													var="rootFolderForAdmin">
													<c:param name="courseId" value="${content.courseId}" />
													<c:param name="acadMonth" value="${content.acadMonth}" />
													<c:param name="acadYear" value="${content.acadYear}" />
												</c:url>

												<div class="row"></div>

												<div class="row">
													<div class="col-sm-4 column">
														<div class="form-group">
															<form:label path="acadMonth" for="acadMonth"><strong>Academic Month:</strong></form:label>
															${content.acadMonth }
														</div>
													</div>
													<div class="col-sm-4 column">
														<div class="form-group">
															<form:label path="acadYear" for="acadYear"><strong>Academic Year:</strong></form:label>
															${content.acadYear}
														</div>
													</div>
													<div class="col-sm-4 column">
														<div class="form-group">
															<form:label path="contentName" for="contentName"><strong>Folder Name: </strong><i
																	class="fa fa-folder-o"></i>
															</form:label>
															${content.contentName}
														</div>
													</div>
											

													
													<div class="col-md-4">
														<div class="form-group">
															<form:label path="accessType" for="accessType"><strong>Access Type:</strong></form:label>
															${content.accessType }
														</div>
													</div>
													
													<div class="col-12">
														<div class="form-group">
															<form:label path="contentDescription"
																for="contentDescription"><strong>Content Description:</strong></form:label>
															${content.contentDescription}

														</div>
													</div>
													

												</div>
<hr/>
												<div class="row">
													<div class="col-md-4">
														<div class="form-group">
															<form:label path="startDate" for="startDate"><strong>Display From:</strong></form:label>
															${content.startDate }
														</div>
													</div>
													<div class="col-md-4">
														<div class="form-group">
															<form:label path="endDate" for="endDate"><strong>Display Until:</strong></form:label>
															${content.endDate }
														</div>
													</div>
												</div>
<hr/>

												<div class="row">
													<div class="col-sm-4 column">
														<div class="form-group">
															<form:label path="sendEmailAlert" for="sendEmailAlert"><strong>Send Email Alert for New Content:</strong></form:label>
															${content.sendEmailAlert }
														</div>
													</div>
													<div class="col-sm-4 column">
														<div class="form-group">
															<form:label path="sendSmsAlert" for="sendSmsAlert"><strong>Send SMS Alert for New Content:</strong></form:label>
															${content.sendSmsAlert }
														</div>
													</div>
													<div class="col-sm-6 col-md-4 col-xs-12 column">
														<div class="form-group">
															<form:label path="sendEmailAlertToParents"
																for="sendEmailAlertToParents"><strong>Send Email Alert to Parents:</strong></form:label>
															${content.sendEmailAlertToParents}
														</div>
													</div>

													<div class="col-sm-6 col-md-4 col-xs-12 column">
														<div class="form-group">
															<form:label path="sendSmsAlertToParents"
																for="sendSmsAlertToParents"><strong>Send SMS Alert to Parents:</strong></form:label>
															${content.sendSmsAlertToParents}
														</div>
													</div>

												</div>


												<div class="row">

													<div class="col-sm-8 column">


														<div class="form-group">


															<sec:authorize access="hasAnyRole('ROLE_ADMIN')">
																<button id="submit" class="btn btn-xs btn-primary "
																	formaction="${rootFolderForAdmin}">Back to
																	Root Folder</button>
															</sec:authorize>
															<sec:authorize access="hasAnyRole('ROLE_FACULTY')">
																<button id="submit" class="btn btn-xs btn-primary "
																	formaction="${exportToSchool}">Export to other school</button>
																<button id="submit" class="btn btn-xs btn-primary "
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

						
								<div class="card bg-white border">
								<div class="card-body">								
									<div class="col-xs-12 col-sm-12">
								<div class="x_panel">
									
									<c:if test="${content.moduleId ne null }">
									<div class="x_title">
										<h5>Select Module to export content With</h5>
										
									</div>
									<div class="x_content">
									<form:form action="exportContentForModule" id="exportContent"
											method="post" modelAttribute="content">
											<fieldset>

												<form:input path="id" type="hidden" />
												
												<form:input path="contentType" type="hidden" />
									<div class="col-md-6 col-sm-12">
									<div class="form-group">

														<%--<form:label path="acadYear" for="acadYear">
															<strong>Academic year <span style="color: red">*</span></strong>
														</form:label>
														 <form:select id="acadYear1" path="acadYear"
															class="form-control" required="required">
															
															<option value="">Select Year</option>
															
															<c:forEach var="acadYear" items="${acadYear}"
																varStatus="status">
																	<form:option value="${acadYear}">${acadYear}</form:option>
														</c:forEach>
														</form:select> --%>
													<div class="form-group">
													<form:label path="acadYearToExport" for="acadYearToExport">
														<strong>Academic year <span style="color: red">*</span></strong>
													</form:label>
													<form:select id="acadYearToExport"  path="acadYearToExport"
														class="form-control " required="required">
														<option value="">Select Year</option>
														<c:forEach var="acadYear" items="${acadYear}"
															varStatus="status">
															<form:option value="${acadYear}">${acadYear}</form:option>
															<%-- <option value="${acadYear}">${acadYear}</option> --%>
														</c:forEach>
													</form:select>
												</div>
													</div>
													</div>
													
													<div class="col-md-6 col-sm-12">
													<div class="form-group">
														<form:label path="moduleId" for="moduleId">
															<strong>Module <span style="color: red">*</span></strong>
														</form:label>
														<form:select id="idOfModule" path="moduleId"
															class="form-control" required="required">
															
																<form:option value="">Select Module</form:option>
														
															<c:forEach var="module" items="${modules}"
																varStatus="status">
																	<form:option value="${module.moduleId}">${module.moduleName}</form:option>
															</c:forEach>
														</form:select>

													</div>
												</div>
												<div class="col-12">
													<div class="form-group">

														<button id="submit" class="btn btn-success mt-3"
															 onclick="return clicked();">Export
															Content with Selected Module</button>
														<button id="cancel" class="btn btn-danger mt-3"
															formaction="homepage" formnovalidate="formnovalidate">Cancel</button>
													</div>
												</div>
												</fieldset>
										</form:form>
										</div>
									</c:if>
									<c:if test="${content.moduleId eq null }">
									<div class="x_title">
										<h5>Select Courses to export content With</h5>
										
									</div>
									<div class="x_content">
									<form:form action="exportContent" id="exportContent"
											method="post" modelAttribute="content">
											<fieldset>

												<form:input path="id" type="hidden" />
												<form:input path="courseId" type="hidden" />
												<form:input path="contentType" type="hidden" />

												<div class="col-sm-4 column">
													<div class="form-group">
														<form:label path="programId" for="programId">Select Program</form:label>
														<form:select id="programId" path="programId"
															class="form-control">
															<form:option value="">Select Program</form:option>
															<c:forEach var="prog" items="${programList}"
																varStatus="status">
																<form:option value="${prog.id}">${prog.programName}</form:option>
															</c:forEach>
														</form:select>
													</div>
												</div>
												<div class="col-md-8 col-sm-12">
													<div class="form-group">
														<form:label path="courseIdToExport" for="courseIdToExport">Courses <span style="color: red">*</span></form:label>
														<form:select id="assid" path="courseIdToExport"
															class="form-control" multiple="multiple"
															required="required">
															<form:option value="">Select Course</form:option>

															<c:forEach var="preAssigment" items="${preAssigments}"
																varStatus="status">
																<form:option value="${preAssigment.id}">${preAssigment.courseName}</form:option>
															</c:forEach>

														</form:select>
													</div>
												</div>
											

												<div class="col-12">
													<div class="form-group">

														<button id="submit" class="btn btn-success mt-3"
															formaction="exportContent" onclick="return clicked();">Export
															Content with Selected course</button>
														<button id="cancel" class="btn btn-danger mt-3"
															formaction="homepage" formnovalidate="formnovalidate">Cancel</button>
													</div>
												</div>

											</fieldset>
										</form:form>
										</div>
									</c:if>
										
									</div>
								</div>
							</div>
								</div>
								</div>
							

					<%-- 	<jsp:include page="../common/paginate.jsp">
							<jsp:param name="baseUrl" value="searchFacultyGroups" />
						</jsp:include> --%>

			<!-- /page content: END -->

			
			<!-- SIDEBAR START -->
	         <jsp:include page="../common/newSidebar.jsp" />
	        <!-- SIDEBAR END -->
	<jsp:include page="../common/footer.jsp"/>
<script>
				$(document)
						.ready(
								function() {

									$('#reset')
											.on(
													'click',
													function() {
														$('#assid').empty();
														var optionsAsString = "";

														optionsAsString = "<option value=''>"
																+ "Select Course"
																+ "</option>";
														$('#assid')
																.append(
																		optionsAsString);

														$("#programId")
																.each(
																		function() {
																			this.selectedIndex = 0
																		});

													});

									$("#programId")
											.on(
													'change',
													function() {

														var programid = $(this)
																.val();
														//alert(programid)
														if (programid) {
															$
																	.ajax({
																		type : 'GET',
																		url : '${pageContext.request.contextPath}/getCourseByProgramId?'
																				+ 'programId='
																				+ programid,
																		success : function(
																				data) {
																			var json = JSON
																					.parse(data);
																			var optionsAsString = "";

																			$(
																					'#assid')
																					.find(
																							'option')
																					.remove();
																			console
																					.log(json);
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
																				}
																			}
																			console
																					.log("optionsAsString"
																							+ optionsAsString);

																			$(
																					'#assid')
																					.append(
																							optionsAsString);

																		}
																	});
														} else {
															//alert('Error no course');
														}
													});
									$('#programId').trigger('change');

								});
				
				
				$("#acadYearToExport")
				.on(
						'change',
						function() {

							console.log('Year Selected');

							var acadYear = $('#acadYearToExport').val();
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