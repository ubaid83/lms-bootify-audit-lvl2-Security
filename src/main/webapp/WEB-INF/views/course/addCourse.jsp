<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
	
<jsp:include page="../common/newDashboardHeader.jsp" /> 

<div class="d-flex adminPage" id="adminPage">
<jsp:include page="../common/newAdminLeftNavBar.jsp"/>
<jsp:include page="../common/rightSidebarAdmin.jsp" />


<!-- DASHBOARD BODY STARTS HERE -->

<div class="container-fluid m-0 p-0 dashboardWraper">

	<jsp:include page="../common/newAdminTopHeader.jsp" />
     
            <!-- SEMESTER CONTENT -->    
                    <div class="col-lg-12 col-md-12 col-sm-12 dashboardBody p-5">
                        
                     			<!-- page content: START -->


					<nav aria-label="breadcrumb">
                    <ol class="breadcrumb">
                        <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/homepage">Dashboard</a></li>
                        <li class="breadcrumb-item" aria-current="page">
                            <c:out value="${Program_Name}" />
                        </li>
                        <sec:authorize access="hasRole('ROLE_STUDENT')">
                            <c:out value="${AcadSession}" />
                        </sec:authorize>
                        <li class="breadcrumb-item active" aria-current="page"> Add Course</li>
                    </ol>
                </nav>
                
						<jsp:include page="../common/alert.jsp" />
						<!-- Input Form Panel -->
						<div class="card bg-white border">
							<div class="card-body">
										<h5 class="text-center border-bottom pb-2 mb-3">
										
										<%
														if ("true".equals((String) request.getAttribute("edit"))) {
													%>
													Edit Course
													<%
														} else {
													%>
													Add Course
													<%
														}
													%>



</h5>
										<form:form action="addCourse" method="post"
											modelAttribute="course">
											<fieldset style="min-width: 50%;">
												<div class="row page-body">
													<%
														if ("true".equals((String) request.getAttribute("edit"))) {
													%>
													<form:input type="hidden" path="id" value="${course.id}" />
													<%
														}
													%>

													
													<%
														if (!"true".equals((String) request.getAttribute("edit"))) {
													%>
													<div class="col-md-4 column">
														<div class="form-group">
															<form:input id="id" path="id" type="number"
																placeholder="Course Id" class="form-control" />
														</div>
													</div>
													<%
														}
													%>
													<div class="col-md-4 column">
														<div class="form-group">
															<form:input id="courseName" path="courseName" type="text"
																placeholder="Course Full Name" class="form-control" />
														</div>
													</div>
													<div class="col-md-4 column">
														<div class="form-group">

															<form:select id="acadSession" path="acadSession"
																type="text" placeholder="Acad Session"
																class="form-control">


																<c:if test="${not empty course.acadSession}">
																	<c:forEach items="${acadSessionList}" var="session">
																		<c:if test="${session eq course.acadSession}">
																			<option value="${course.acadSession}" selected>${course.acadSession}</option>
																		</c:if>
																		<c:if test="${session ne course.acadSession}">
																			<option value="${session}">${session}</option>
																		</c:if>
																	</c:forEach>
																</c:if>
																<c:if test="${empty course.acadSession}">
																	<form:option value="">Select Acad Session</form:option>
																	<form:options items="${acadSessionList}" />
																</c:if>

															</form:select>
														</div>
													</div>

													<div class="col-md-4 column">
														<div class="form-group">
															<%-- <form:input id="property3" path="property3" type="text"
																placeholder="Year Number" class="form-control" /> --%>
															<form:select id="property3" path="property3" type="text"
																placeholder="property3" class="form-control">
																<c:if test="${not empty course.property3}">
																	<c:forEach items="${acadYearList}" var="acadYear">
																		<c:if test="${acadYear eq course.property3}">
																			<option value="${course.property3}" selected>${course.property3}</option>
																		</c:if>
																		<c:if test="${acadYear ne course.property3}">
																			<option value="${acadYear}">${acadYear}</option>
																		</c:if>
																	</c:forEach>

																</c:if>
																<c:if test="${empty course.property3}">
																	<form:option value="">Select Acad Year</form:option>
																	<form:options items="${acadYearList}" />
																</c:if>
															</form:select>
														</div>
													</div>

													<div class="col-12">
														<div class="form-group">

															<%
																if ("true".equals((String) request.getAttribute("edit"))) {
															%>
															<button id="submit" name="submit"
																class="btn btn-large btn-primary"
																formaction="updateCourse">Update</button>
															<%
																} else {
															%>
															<button id="submit" name="submit"
																class="btn btn-large btn-primary" formaction="addCourse">Add</button>
															<%
																}
															%>
															<button id="cancel" name="cancel" class="btn btn-danger"
																formaction="homepage" formnovalidate="formnovalidate">Cancel</button>
														</div>
													</div>
												</div>
											</fieldset>
										</form:form>
							</div>
						</div>

						<!-- Results Panel -->

			<!-- /page content: END -->
                   
                    </div>
			
			<!-- SIDEBAR START -->

	        <!-- SIDEBAR END -->
	<jsp:include page="../common/newAdminFooter.jsp"/> 	
	
	





