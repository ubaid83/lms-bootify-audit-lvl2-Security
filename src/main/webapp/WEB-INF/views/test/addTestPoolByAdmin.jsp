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

<jsp:include page="../common/newAdminLeftNavBar.jsp"/>
<jsp:include page="../common/rightSidebarAdmin.jsp" />

<!-- DASHBOARD BODY STARTS HERE -->

<div class="container-fluid m-0 p-0 dashboardWraper">

	<jsp:include page="../common/newAdminTopHeader.jsp" />
     
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
                                <li class="breadcrumb-item active" aria-current="page"> Add Test Pool</li>
                            </ol>
                        </nav>
                        
						<jsp:include page="../common/alert.jsp" />
						
						
						<!-- Input Form Panel -->
						<div class="card bg-white border">
						<div class="card-body">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">

									<div class="x_title">
										<h5>
											<%
												if ("true".equals((String) request.getAttribute("edit"))) {
											%>
											Edit Test Pool
											<%
												} else {
											%>
											Add Test Pool
											<%
												}
											%>
										</h5>
									
									</div>

									<div class="x_content">
										<form:form action="addTestPoolByAdmin" method="post"
											modelAttribute="testPool">
											

											<div class="row">
											<%
												if (!"true".equals((String) request.getAttribute("edit"))) {
											%>
											
											
											<div class="col-sm-4 column">
												<div class="form-group">
												<form:label path="acadYear" for="acadYear">Academic Year</form:label>
													<form:select id="acadYear" path="acadYear"
														class="form-control subjectParam">
														<option value="" selected disabled hidden>Select
															Academic Year</option>
														<%-- <form:options items="${acadYears}" /> --%>
														<form:options items="${acadYearCodeList}" />
													</form:select>
												</div>
											</div>
											
											
											<div class="col-sm-4 column">
												<div class="form-group">
												
													<form:label path="moduleId" for="moduleId">Subject</form:label>
													<form:select id="moduleId" path="moduleId"
														class="form-control">
														<form:option value="">Select Subject</form:option>
														<%--  <c:forEach var="course" items="${allCourses}"
                                                            varStatus="status">
                                                            <form:option value="${course.id}">${course.courseName}</form:option>
                                                      </c:forEach> --%>
													</form:select>
												
												</div>
											</div>
											<%} %>
											
												<div class="col-sm-4 column">
													<%
														if ("true".equals((String) request.getAttribute("edit"))) {
													%>
													<form:input type="hidden" path="id" />
													<form:input type="hidden" path="moduleId" />
													
													<%
														}
													%>
													<div class="form-group">
														<form:label class="textStrong" path="testPoolName" for="testPoolName">Test Pool Name <span style="color: red">*</span></form:label>
														<form:input id="testPoolName" path="testPoolName" type="text"
															placeholder="Add  Test Pool Name" class="form-control"
															required="required" />
													</div>
												</div>
											</div>
											<div class="row">

												<div class="col-sm-12 column">
													<div class="form-group">
														<%
															if ("true".equals((String) request.getAttribute("edit"))) {
														%>
														<button id="submit" class="btn btn-large btn-primary"
															formaction="updateTestPool">Update</button>
														<%
															} else {
														%>
														<button id="submit" class="btn btn-large btn-primary"
															formaction="addTestPoolByAdmin">Add</button>
														<%
															}
														%>
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

						<!-- Results Panel -->

						
								
							

					<%-- 	<jsp:include page="../common/paginate.jsp">
							<jsp:param name="baseUrl" value="searchFacultyGroups" />
						</jsp:include> --%>

			<!-- /page content: END -->
                   
                    </div>
			
			<!-- SIDEBAR START -->
	        
	        <!-- SIDEBAR END -->
	<jsp:include page="../common/newAdminFooter.jsp"/>

	<script>
					
				$(".subjectParam")
				.on(
						'change',
						function() {

							console.log("subject param entered");
							var acadYear = $('#acadYear').val();
							
							
							var courseid = $('#moduleId').val();
							

							var campus
							
							if (acadYear) {
							
								$
								.ajax({
									type : 'GET',
									url : '${pageContext.request.contextPath}/getModuleByParamForTest?'
											+ 'acadYear='
											+ acadYear,
									success : function(data) {
										var json = JSON.parse(data);
										console.log('json--->'
												+ json);
										var optionsAsString = "";

										$('#moduleId').find(
												'option').remove();
										optionsAsString += "<option value=\"\" selected disabled> Select Subject</option>";
										console.log(json);
										for (var i = 0; i < json.length; i++) {
											var idjson = json[i];
											console.log(idjson);

											for ( var key in idjson) {
												console
														.log(key
																+ ""
																+ idjson[key]);
												if (moduleId == idjson[key]) {
													optionsAsString += "<option value='" +key + "' selected>"
															+ idjson[key]
															+ "</option>";

												} else {
													console
															.log('else entered00');
													optionsAsString += "<option value='" +key + "'>"
															+ idjson[key]
															+ "</option>";
												}
											}
										}

										$('#moduleId').append(
												optionsAsString);

									}
								});
							}
						});
				
				</script>













