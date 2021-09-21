<%@page import="java.util.*"%>
<%@page import="com.spts.lms.beans.dashboard.DashBoard"%>
<%-- <jsp:include page="../common/DashboardHeader.jsp" /> --%>
<%-- <jsp:include page="../common/NewHeader1.jsp" /> --%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

<%
	boolean isEdit = "true".equals((String) request.getAttribute("edit"));
%>
<jsp:include page="../common/newDashboardHeader.jsp" />

<script type="text/javascript">
	var headerCourseId = null;
	function theFunction(obj) {

		return true;
	}
</script>
<div class="d-flex" id="facultyAssignmentPage">
	<jsp:include page="../common/newAdminLeftNavBar.jsp"/>
<jsp:include page="../common/rightSidebarAdmin.jsp" />

	<!-- DASHBOARD BODY STARTS HERE -->

	<div class="container-fluid m-0 p-0 dashboardWraper">

		 <jsp:include page="../common/newAdminTopHeader.jsp" />

		<div class="container mt-5">
			<div class="row">
				<div class="container mt-5">
					<div class="row">
						<div
							class="col-lg-9 col-md-9 col-sm-12 dashboardBody ml-auto mr-auto">
							<nav aria-label="breadcrumb">
								<ol class="breadcrumb">
									<li class="breadcrumb-item"><a
										href="${pageContext.request.contextPath}/homepage">Dashboard</a></li>
									<%
										if (isEdit) {
									%>
									<li class="breadcrumb-item active" aria-current="page">Edit
										Test</li>
									<%
										} else {
									%>
									<li class="breadcrumb-item active" aria-current="page">Create
										Test</li>
									<%
										}
									%>

								</ol>
							</nav>
							<jsp:include page="../common/alert.jsp" />
							<div class="card bg-white">
								<div class="card-body">

									<form:form action="addTestByAdmin" method="post" modelAttribute="test">
										<input type="hidden" id="username" value="${username}" />
										<form:input type="hidden" id="testId" path="id"
											value="${ test.id }" />
										<form:input type="hidden" id="courseId" path="courseId"
											value="${ test.courseId }" />
										<c:if test='${canUpdateTestType eq "N"}'>
											<form:input type="hidden" id="testType" path="testType"
												value="${ test.testType }" />
										</c:if>
										<div class="form-row">
											<%
												if (isEdit) {
											%>
											<div class="col-12 text-center">
												<h3>Edit Test</h3>
											</div>
											<%
												} else {
											%>
											<div class="col-12 text-center">
												<h3>Create Test</h3>
											</div>
											<%
												}
											%>
											<!--FORM ITEMS STARTS HERE-->
											<div
												class="col-lg-4 col-md-4 col-sm-12 mt-3 position-relative">
												<label class="textStrong">Name <span
													class="text-danger">*</span></label>
												<form:input id="testName" path="testName" type="text"
													placeholder="Test Name" class="form-control"
													required="required" />
											</div>
											<%if (isEdit){ %>
											<div class="col-lg-4 col-md-4 col-sm-12 mt-3 position-relative">
												
													<form:label path="acadYear" for="acadYear">Academic Year</form:label>
													<form:select id="acadYear" path="acadYear"
														class="form-control subjectParam">
														<option value="${testFromDb.acadYear}">${testFromDb.acadYear}</option>
													
													</form:select>
											
											</div>
											<%}else{ %>
											<div class="col-lg-4 col-md-4 col-sm-12 mt-3 position-relative">
												
													<form:label path="acadYear" for="acadYear">Academic Year</form:label>
													<form:select id="acadYear" path="acadYear"
														class="form-control subjectParam">
														<option value="" selected disabled hidden>Select
															Academic Year</option>
														<%-- <form:options items="${acadYears}" /> --%>
														<form:options items="${acadYearCodeList}" />
													</form:select>
											
											</div>
											<%} %>
											<c:if test="${campusList.size()>0 }">
											<%if (isEdit){ %>
												<div class="col-lg-4 col-md-4 col-sm-12 mt-3 position-relative">
													
														<form:label path="campusId" for="campusId">Select Campus </form:label>


														<form:select id="campus" path="campusId"
															class="form-control subjectParam">


															<option value="${pcBean.campusId}">${pcBean.campusName}</option>


														</form:select>
													
												</div>
											<%}else{ %>
												<div class="col-lg-4 col-md-4 col-sm-12 mt-3 position-relative">
													
														<form:label path="campusId" for="campusId">Select Campus </form:label>


														<form:select id="campus" path="campusId"
															class="form-control subjectParam">


															<option value="" selected disabled hidden>Select
																Campus</option>

															<c:forEach var="listValue" items="${campusList}">
																<option value="${listValue.campusId}">${listValue.campusName}</option>
															</c:forEach>


														</form:select>
													
												</div>
											<%} %>

											</c:if>
											<%-- <div
                                                                        class="col-lg-4 col-md-4 col-sm-12 mt-3 position-relative">
                                                                        <label class="sr-only">Select Semester</label> <label
                                                                              class="f-13 text-danger req">*</label> <select
                                                                              class="form-control" id="assignSem1">
                                                                              <c:forEach items="${sessionWiseCourseList}" var="sList"
                                                                                    varStatus="count">
                                                                                    <c:if test="${sList.key eq course.acadSession}">
                                                                                          <option value="${sList.key}" selected>${sList.key}</option>
                                                                                    </c:if>
                                                                                    <c:if test="${sList.key ne course.acadSession }">
                                                                                          <option value="${sList.key}">${sList.key}</option>
                                                                                    </c:if>
                                                                              </c:forEach>
                                                                        </select>
                                                                  </div> --%>
                                           <%if (isEdit){ %>
                                           <div class="col-md-6 col-sm-8 col-xs-12 column">
												<div class="form-group">
													<form:label path="moduleId" for="moduleId">Subject</form:label>
													<form:select id="moduleId" path="moduleId"
														class="form-control subParam">
														<form:option value="${course.moduleId}">${course.moduleName}(${course.moduleAbbr })</form:option>
														<%--  <c:forEach var="course" items="${allCourses}"
                                                            varStatus="status">
                                                            <form:option value="${course.id}">${course.courseName}</form:option>
                                                      </c:forEach> --%>
													</form:select>
												</div>
											</div>
                                           
                                           <%}else{ %>
											<div class="col-md-6 col-sm-8 col-xs-12 column">
												<div class="form-group">
													<form:label path="moduleId" for="moduleId">Subject</form:label>
													<form:select id="moduleId" path="moduleId"
														class="form-control subParam">
														<form:option value="">Select Subject</form:option>
														<%--  <c:forEach var="course" items="${allCourses}"
                                                            varStatus="status">
                                                            <form:option value="${course.id}">${course.courseName}</form:option>
                                                      </c:forEach> --%>
													</form:select>
												</div>
											</div>
											<%} %>
											<%if (isEdit){ %>
                                           <div class="col-md-6 col-sm-8 col-xs-12 column">
												<div class="form-group">
													<form:label path="moduleId" for="moduleId">Faculty</form:label>
													<form:select id="moduleId" path="moduleId" multiple="multiple"
														class="form-control subParam" disabled="true">
														<c:forEach var="fac" items="${facultyList }">
														<form:option value="${fac.username}" selected="selected">${fac.facultyName}(${fac.username})</form:option>
														
														</c:forEach>
														<%--  <c:forEach var="course" items="${allCourses}"
                                                            varStatus="status">
                                                            <form:option value="${course.id}">${course.courseName}</form:option>
                                                      </c:forEach> --%>
													</form:select>
												</div>
											</div>
                                           
                                           <%}else{ %>
											
														<div class="col-md-6 col-sm-8 col-xs-12 column">
												<div class="form-group">
													<form:label path="assignedFaculty" for="assignedFaculty">Faculty</form:label>
													<form:select multiple="multiple" id="assignedFaculty"
													path="assignedFaculty" required="required" disabled="true" 
													class="form-control " style="overflow: auto;">
												</form:select>
											
										</div>
										</div>
										<%} %>
											<%
												if (isEdit) {
											%>

											<div
												class="col-lg-8 col-md-8 col-sm-12 mt-3 position-relative">
												<label class="textStrong">Select Date Range <span
													class="text-danger">*</span></label>
												<div class="input-group">
													<form:input type="hidden" id="testStartDate"
														path="startDate" />
													<form:input type="hidden" id="testEndDate" path="endDate" />
													<input id="startDate" type="text"
														placeholder="Start Date - End Date" class="form-control"
														required="required"
														value="${test.startDate} - ${test.endDate}" readonly />
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
											<div
												class="col-lg-8 col-md-8 col-sm-12 mt-3 position-relative">
												<label class="textStrong">Select Date Range <span
													class="text-danger">*</span></label>
												<div class="input-group">
													<form:input type="hidden" id="testStartDate"
														path="startDate" />
													<form:input type="hidden" id="testEndDate" path="endDate" />
													<input id="startDate" type="text"
														placeholder="Start Date - End Date" class="form-control"
														required="required" readonly />
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

											<%--  <div
                                                                        class="col-lg-4 col-md-4 col-sm-12 mt-3 position-relative">
                                                                        <div class="input-group">
                                                                              <label class="f-13 text-danger req">*</label> 
                                                                              <label
                                                                                    class="sr-only">Select Date Range</label>
                                                                                    <form:input type="hidden" id="testStartDate" path="startDate"/>
                                                                                    <form:input type="hidden" id="testEndDate" path="endDate"/>   
                                                                                    <input id="startDate"  type="text"
                                                                                    placeholder="Start Date" class="form-control"
                                                                                    required="required"/>
                                                                              <div class="input-group-append">
                                                                                    <button class="btn btn-outline-secondary" type="button"
                                                                                          id="testDateRangeBtn">
                                                                                          <i class="fas fa-calendar"></i>
                                                                                    </button>
                                                                              </div>
                                                                        </div>
                                                                  </div> --%>
											<div
												class="col-lg-4 col-md-4 col-sm-12 mt-3 position-relative">
												<label class="textStrong"> Test Duration in Minutes
													<span class="text-danger">*</span>
												</label>
												<form:input id="duration" path="duration" min="1"
													type="number" placeholder="Duration" class="form-control"
													required="required" />
											</div>
											<div
												class="col-lg-4 col-md-4 col-sm-12 mt-3 position-relative">
												<label class="textStrong">Total Score <span
													class="text-danger">*</span></label>
												<form:input id="maxScore" path="maxScore" min="1"
													type="number" placeholder="Total Score"
													class="form-control" required="required" />
											</div>
											<div
												class="col-lg-4 col-md-4 col-sm-12 mt-3 position-relative">
												<label class="textStrong">Passing Score <span
													class="text-danger">*</span></label>
												<form:input id="passScore" path="passScore" min="1"
													type="number" placeholder="Passing Score"
													class="form-control" required="required" />
											</div>
											<div
												class="col-lg-4 col-md-4 col-sm-12 mt-3 position-relative">
												<label class="textStrong">Maximum Attempts <span
													class="text-danger">*</span></label>
												<form:input id="maxAttempt" path="maxAttempt" min="1"
													type="number" placeholder="Maximum Attempts"
													class="form-control" required="required" />

												<%-- <form:input id="maxAttempt" min="1" path="maxAttempt"
                                                                        type="number" placeholder="Max Attempt"
                                                                        class="form-control" required="required" /> --%>
											</div>
											<%-- <div
												class="col-lg-4 col-md-4 col-sm-12 mt-3 position-relative">
												<label class="textStrong">Test Type <span class="text-danger">*</span></label>
												<form:select id="testType" path="testType"
													class="form-control facultyparameter" required="required">

													<form:option value="Objective">Objective</form:option>
													<form:option value="Subjective">Subjective</form:option>
													<form:option value="Mix">Mix</form:option>
												</form:select>
											</div> --%>

											<c:if test='${canUpdateTestType eq "N"}'>
												<div
													class="col-lg-4 col-md-4 col-sm-12 mt-3 position-relative">
													<label class="textStrong">Test Type <span
														class="text-danger">*</span></label>
													<form:select id="testType" path="testType"
														class="form-control facultyparameter" required="required"
														disabled='true'>

														<form:option value="Objective">Objective</form:option>
														<form:option value="Subjective">Subjective</form:option>
														<form:option value="Mix">Mix</form:option>
													</form:select>
												</div>
											</c:if>
											<c:if
												test='${canUpdateTestType eq "Y" || canUpdateTestType eq null}'>
												<div
													class="col-lg-4 col-md-4 col-sm-12 mt-3 position-relative">
													<label class="textStrong">Test Type <span
														class="text-danger">*</span></label>
													<form:select id="testType" path="testType"
														class="form-control facultyparameter" required="required">

														<form:option value="Objective">Objective</form:option>
														<form:option value="Subjective">Subjective</form:option>
														<form:option value="Mix">Mix</form:option>
													</form:select>
												</div>
											</c:if>
											<!--FORM ITEMS ENDS HERE-->
										</div>

										<hr />






										<div class="form-row">




											<%
												if (isEdit) {
											%>
											<div class="col-lg-6 col-md-6 col-sm-12">
												<label class="textStrong">Random Question Required?</label>
												<div>
													<div class="pretty p-switch p-fill p-toggle">
														<form:checkbox id="randQReq"
															value="${ test.randomQuestion }" path="randomQuestion" />
														<div class="state p-success p-on">
															<label>Yes</label>
														</div>
														<div class="state p-danger p-off">
															<label>No</label>
														</div>
													</div>
												</div>


												<div class="form-check pl-0 mt-2 d-none">
													<label class="textStrong">Maximum Question To Show:</label>
													<form:input id="inputMaxQ" value="${test.maxQuestnToShow}"
														type="number" path="maxQuestnToShow"
														class="form-control w-75"
														placeholder="Maximum Questions To Show" />
												</div>
											</div>
											<%
												} else {
											%>
											<div class="col-lg-6 col-md-6 col-sm-12">
												<label class="textStrong">Random Question Required?</label>
												<div>
													<div class="pretty p-switch p-fill p-toggle">
														<form:checkbox id="randQReq" value="N"
															path="randomQuestion" />
														<div class="state p-success p-on">
															<label>Yes</label>
														</div>
														<div class="state p-danger p-off">
															<label>No</label>
														</div>
													</div>
												</div>
												<div class="form-check pl-0 mt-2 d-none">
													<label class="textStrong">Maximum Question To Show:</label>
													<form:input id="inputMaxQ" value="0" type="number"
														path="maxQuestnToShow" class="form-control w-75"
														placeholder="Maximum Questions To Show" />
												</div>
											</div>
											<%
												}
											%>


											<%
												if (isEdit) {
											%>
											<div class="col-lg-6 col-md-6 col-sm-12">
												<label class="textStrong">Set a Password for Test?</label>
												<div>
													<div class="pretty p-switch p-fill p-toggle">
														<form:checkbox id="setTestPwd"
															value="${test.isPasswordForTest}"
															path="isPasswordForTest" />
														<div class="state p-success p-on">
															<label>Yes</label>
														</div>
														<div class="state p-danger p-off">
															<label>No</label>
														</div>
													</div>
												</div>
												<div class="form-check pl-0 mt-2 d-none">
													<form:input id="testPwdVal" type="password"
														path="passwordForTest" class="form-control w-75"
														placeholder="Set Test Password" />
												</div>
											</div>
											<%
												} else {
											%>
											<div class="col-lg-6 col-md-6 col-sm-12">
												<label class="textStrong">Set a Password for Test?</label>
												<div>
													<div class="pretty p-switch p-fill p-toggle">
														<form:checkbox id="setTestPwd" value="N"
															path="isPasswordForTest" />
														<div class="state p-success p-on">
															<label>Yes</label>
														</div>
														<div class="state p-danger p-off">
															<label>No</label>
														</div>
													</div>
												</div>
												<div class="form-check pl-0 mt-2 d-none">
													<form:input id="testPwdVal" type="password"
														path="passwordForTest" class="form-control w-75"
														placeholder="Set Test Password" />
												</div>
											</div>

											<%
												}
											%>

											<!-- SET QUESTION NOS -->


											<%
												if (isEdit) {
											%>

											<div class="col-lg-6 col-md-6 col-sm-12 mt-3 mb-3">
												<label class="textStrong">Same Mark Question?</label>
												<div>
													<div class="pretty p-switch p-fill p-toggle">
														<form:checkbox id="smqChk" value="${test.sameMarksQue}"
															path="sameMarksQue" />
														<div class="state p-success p-on">
															<label>Yes</label>
														</div>
														<div class="state p-danger p-off">
															<label>No</label>
														</div>
													</div>
												</div>
												<div class="form-check pl-0 mt-2 d-none">
													<form:input id="marksPerQueIn" path="marksPerQue"
														type="number" placeholder="Marks per question"
														class="form-control" step=".0001" />
												</div>
											</div>
											<%
												} else {
											%>
											<div class="col-lg-6 col-md-6 col-sm-12 mt-3 mb-3">
												<label class="textStrong">Same Mark Question?</label>
												<div>
													<div class="pretty p-switch p-fill p-toggle">
														<form:checkbox id="smqChk" value="N" path="sameMarksQue" />
														<div class="state p-success p-on">
															<label>Yes</label>
														</div>
														<div class="state p-danger p-off">
															<label>No</label>
														</div>
													</div>
												</div>
												<div class="form-check pl-0 mt-2 d-none">
													<form:input id="marksPerQueIn" path="marksPerQue"
														type="number" placeholder="Marks per question"
														class="form-control" step=".0001" />
												</div>
											</div>

											<%
												}
											%>


											<!--For Mix Test created by Amey-->

											<%
												if (isEdit) {
											%>
											<div class="row">
												<div id="mixQuestions" style="display: none">
													<label class="textStrong ml-3">Maximum Question
														Type To Show:</label>
													<div class="col-lg-12 col-md-12 col-sm-12">
														<div class="form-group">
															<form:label path="maxDesQueToShow" for="maxDesQueToShow">Maximum Descriptive Questions to be Shown to students <span
																	style="color: red">*</span>
															</form:label>
															<form:input id="maxQuestnToShow1" path="maxDesQueToShow"
																type="number" placeholder="Maximum Questions To Show"
																class="form-control" value="${test.maxDesQueToShow}" />
														</div>
													</div>

													<div class="col-lg-12 col-md-12 col-sm-12">
														<div class="form-group">
															<form:label path="maxMcqQueToShow" for="maxMcqQueToShow">Maximum MCQ Questions to be Shown to students <span
																	style="color: red">*</span>
															</form:label>
															<form:input id="maxQuestnToShow2" path="maxMcqQueToShow"
																type="number" placeholder="Maximum Questions To Show"
																class="form-control" value="${test.maxMcqQueToShow}" />
														</div>
													</div>

													<div class="col-lg-12 col-md-12 col-sm-12">
														<div class="form-group">
															<form:label path="maxRngQueToShow" for="maxRngQueToShow">Maximum Range type Questions to be Shown to students <span
																	style="color: red">*</span>
															</form:label>
															<form:input id="maxQuestnToShow3" path="maxRngQueToShow"
																type="number" placeholder="Maximum Questions To Show"
																class="form-control" value="${test.maxRngQueToShow}" />
														</div>
													</div>

													<div class="col-lg-12 col-md-12 col-sm-12">
														<div class="form-group">
															<form:label path="maxImgQueToShow" for="maxImgQueToShow">Maximum Image type Questions to be Shown to students <span
																	style="color: red">*</span>
															</form:label>
															<form:input id="maxQuestnToShow4" path="maxImgQueToShow"
																type="number" placeholder="Maximum Questions To Show"
																class="form-control" value="${test.maxImgQueToShow}" />
														</div>
													</div>
												</div>
											</div>
											<%
												} else {
											%>
											<div class="row">
												<div id="mixQuestions" style="display: none">
													<label class="textStrong ml-3">Maximum Question
														Type To Show:</label>
													<div class="col-lg-12 col-md-12 col-sm-12">
														<div class="form-group">
															<form:label path="maxDesQueToShow" for="maxDesQueToShow">Maximum Descriptive Questions to be Shown to students <span
																	style="color: red">*</span>
															</form:label>
															<form:input id="maxQuestnToShow1" path="maxDesQueToShow"
																type="number" placeholder="Maximum Questions To Show"
																class="form-control" value="0" />
														</div>
													</div>

													<div class="col-lg-12 col-md-12 col-sm-12">
														<div class="form-group">
															<form:label path="maxMcqQueToShow" for="maxMcqQueToShow">Maximum MCQ Questions to be Shown to students <span
																	style="color: red">*</span>
															</form:label>
															<form:input id="maxQuestnToShow2" path="maxMcqQueToShow"
																type="number" placeholder="Maximum Questions To Show"
																class="form-control" value="0" />
														</div>
													</div>

													<div class="col-lg-12 col-md-12 col-sm-12">
														<div class="form-group">
															<form:label path="maxRngQueToShow" for="maxRngQueToShow">Maximum Range type Questions to be Shown to students <span
																	style="color: red">*</span>
															</form:label>
															<form:input id="maxQuestnToShow3" path="maxRngQueToShow"
																type="number" placeholder="Maximum Questions To Show"
																class="form-control" value="0" />
														</div>
													</div>

													<div class="col-lg-12 col-md-12 col-sm-12">
														<div class="form-group">
															<form:label path="maxImgQueToShow" for="maxImgQueToShow">Maximum Image type Questions to be Shown to students <span
																	style="color: red">*</span>
															</form:label>
															<form:input id="maxQuestnToShow4" path="maxImgQueToShow"
																type="number" placeholder="Maximum Questions To Show"
																class="form-control" value="0" />
														</div>
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
														<td>Allow to Start test any time?</td>
														<td>
															<%
																if (isEdit) {
															%>
															<div class="pretty p-switch p-fill p-toggle">
																<form:checkbox path="allowAfterEndDate"
																	id="subAfterEndDate" value="${test.allowAfterEndDate}" />
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
																<form:checkbox path="allowAfterEndDate"
																	id="subAfterEndDate" value="N" />
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
														<td>Show Results to Students immediately?</td>
														<td>
															<%
																if (isEdit) {
															%>
															<div class="pretty p-switch p-fill p-toggle">
																<form:checkbox value="${test.showResultsToStudents}"
																	id="showResult" path="showResultsToStudents" />
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
																<form:checkbox value="N" id="showResult"
																	path="showResultsToStudents" />
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



														<td>Automatic Allocation To Students?</td>
														<td>
															<%
																if (isEdit) {
															%>

															<div class="pretty p-switch p-fill p-toggle">
																<form:checkbox value="${test.autoAllocateToStudents}"
																	id="autoAllocateTest" path="autoAllocateToStudents" />
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
																<form:checkbox value="N" id="autoAllocateTest"
																	path="autoAllocateToStudents" />
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
														<td>Send Email Alert for New Test?</td>
														<td>
															<%
																if (isEdit) {
															%>
															<div class="pretty p-switch p-fill p-toggle">
																<form:checkbox value="${test.sendEmailAlert }"
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
														<td>Send SMS Alert for New Test?</td>
														<td>
															<%
																if (isEdit) {
															%>
															<div class="pretty p-switch p-fill p-toggle">
																<form:checkbox value="${test.sendSmsAlert}"
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
																	<form:checkbox value="${test.sendEmailAlertToParents}"
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
																	<form:checkbox value="${test.sendSmsAlertToParents}"
																		id="sendSmsAlertToParents"
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
																	<form:checkbox value="N" id="sendSmsAlertToParents"
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
												</tbody>
											</table>
										</div>



										<div class="col-12 mt-5 p-0">
											<h5>Test Description:</h5>
											<form:textarea id="editor1" class="editor1" name="editor1"
												path="testDescription"></form:textarea>
										</div>

										<div class="col-6 m-auto">
											<div class="form-row mt-5">
												<%
													if (isEdit) {
												%>
												<button id="updateTestBtn"
													class="btn btn-info col-md-5 col-sm-12  ml-auto mr-auto mb-3"
													formaction="updateTestByAdmin">Update Test</button>

												<%
													} else {
												%>
												<button id="createTestBtn" onclick="myFunction()"
													class="btn btn-info col-md-5 col-sm-12  ml-auto mr-auto mb-3">Create
													Test</button>
												<%
													}
												%>

												<button id="cancel"
													class="btn btn-danger col-md-5 col-sm-12  ml-auto mr-auto mb-3"
													formaction="homepage" formnovalidate="formnovalidate">Cancel</button>
											</div>
										</div>
									</form:form>




								</div>
							</div>







						</div>
						

					</div>
				</div>
				<!-- SIDEBAR START -->

				<!-- SIDEBAR END -->
				 <jsp:include page="../common/newAdminFooter.jsp"/>
				<script>
					$(function() {

						$('#setTestPwd').on('click', function() {
							//alert('slidYes');
							var testpwd = $(this).val();
							console.log("testpwd-->" + testpwd)
							if (testpwd == 'Y') {
								$('#testPwdVal').attr('required', 'required');
							} else {
								$('#testPwdVal').removeAttr('required');

							}

						});
						/* 
						$('#setPasswordN').on('click', function() {
							//alert('slidNo');
							$('#passwordForTest').hide();

							$("#passwordForTestText").prop('required', false);

						}); */

					});

					//For Mix Test
					$("#randQReq").change(
							function() {
								let isChecked = $(this).is(":checked");
								let testType = $("#testType").val()
										.toLowerCase();

								if (isChecked === true && testType === "mix") {
									$("#smqChk").val("Y").prop("checked", true)
									$("#marksPerQueIn").parent().removeClass(
											"d-none")

								} else {
									$("#smqChk").val("N")
											.prop("checked", false)
									$("#marksPerQueIn").val(0).parent()
											.addClass("d-none")
								}
							})
				</script>
				<script>
					function myFunction() {
						console.log("fdghdhyrfdhuytuytrutryutrruht");
						var dateinput = $('#startDate').val();
						console.log(dateinput);
					}

					$(function() {

						$('#createTestBtn')
								.on(
										'click',
										function(e) {
											//console.log("fdghdhyrfdhuytuytrutryutrruht");
											var dateinput = $('#startDate')
													.val();
											var smq = $('#marksPerQueIn').val();
											var smqChk = $('#smqChk').val();
											//console.log("smq-->"+smq);
											//console.log(dateinput);
											//rmd mq
											var randQReq = $('#randQReq').val();
											var imq = $('#inputMaxQ').val();
											//console.log("imq-->"+imq);
											var selected = $('#testType').val();
											//console.log("smq-->"+smq);
											var maxQ1 = $("#maxQuestnToShow1")
													.val();
											var maxQ2 = $("#maxQuestnToShow2")
													.val();
											var maxQ3 = $("#maxQuestnToShow3")
													.val();
											var maxQ4 = $("#maxQuestnToShow4")
													.val();
											var total = parseInt(maxQ1)
													+ parseInt(maxQ2)
													+ parseInt(maxQ3)
													+ parseInt(maxQ4);

											if (dateinput == '') {
												alert('Error: Enter proper Date inputs.');
												e.preventDefault();
											} else if (imq <= '0'
													&& randQReq == 'Y') {
												alert('Maximum Question To Show should be more than 0');
												e.preventDefault();
											} else if (smq <= '0.00'
													&& smqChk == 'Y') {
												alert('Same Mark Question should be more than 0.0');
												e.preventDefault();
											} else if (randQReq == 'Y'
													&& selected == 'Mix') {
												if ((maxQ1 == 0 && maxQ2 == 0
														&& maxQ3 == 0 && maxQ4 == 0)) {
													alert('Atleast One Maximum Question Type To Show should be more than 0');
													e.preventDefault();
												} else if (total != imq) {
													alert("Total of Maximum Question Type should be sequal to Maximum Question To Show.");
													e.preventDefault();
												}
											}

										});
						$('#updateTestBtn')
								.on(
										'click',
										function(e) {
											var smq = $('#marksPerQueIn').val();
											var smqChk = $('#smqChk').val();
											//rmd mq
											var randQReq = $('#randQReq').val();
											var imq = $('#inputMaxQ').val();
											//console.log("smq-->"+smq);
											var selected = $('#testType').val();
											//console.log("smq-->"+smq);
											var maxQ1 = $("#maxQuestnToShow1")
													.val();
											var maxQ2 = $("#maxQuestnToShow2")
													.val();
											var maxQ3 = $("#maxQuestnToShow3")
													.val();
											var maxQ4 = $("#maxQuestnToShow4")
													.val();
											var total = parseInt(maxQ1)
													+ parseInt(maxQ2)
													+ parseInt(maxQ3)
													+ parseInt(maxQ4);

											if (imq <= '0' && randQReq == 'Y') {
												alert('Maximum Question To Show should be more than 0');
												e.preventDefault();
											} else if (smq <= '0.00'
													&& smqChk == 'Y') {
												alert('Same Mark Question should be more than 0.0');
												e.preventDefault();
											} else if (randQReq == 'Y'
													&& selected == 'Mix') {
												if ((maxQ1 == 0 && maxQ2 == 0
														&& maxQ3 == 0 && maxQ4 == 0)) {
													alert('Atleast One Maximum Question Type To Show should be more than 0');
													e.preventDefault();
												} else if (total != imq) {
													alert("Total of Maximum Question Type should be equal to Maximum Question To Show.");
													e.preventDefault();
												}
											}
										});

					});
				</script>
				<script>
					function myFunction() {
						console.log("fdghdhyrfdhuytuytrutryutrruht");
						var dateinput = $('#startDate').val();
						console.log(dateinput);
					}

					/* $(function() {
						
						$('#createTestBtn').on('click', function(e){ 
							console.log("fdghdhyrfdhuytuytrutryutrruht");
							var dateinput = $('#startDate').val();
							console.log(dateinput);
							if(dateinput == ''){
								alert('Error: Enter proper Date inputs.');
								
								    e.preventDefault();
							
							} else {
								
							}
							
						});
						
					}); */
				</script>



				<script>
					$(function() {

						$('#Yes').on('click', function() {
							//alert('slidYes');

							var selected = $('#testType').val();

							$('#maxQuestions').show();

							$("#maxQuestnToShow").prop('required', true);

							$('#sameMarks').show();

							$("#SMQYes").prop('required', true);

							if (selected == 'Mix') {

								$('#specifyQuestion').show();

								$("#MaxYes").prop('required', true);

								/* $('#mixQuestions').show();
								
								$("#maxQuestnToShow1").prop('required', true);
								
								$("#maxQuestnToShow2").prop('required', true);
								
								$("#maxQuestnToShow3").prop('required', true);
								
								$("#maxQuestnToShow4").prop('required', true); */
							}

						});

						$('#No')
								.on(
										'click',
										function() {
											//alert('slidNo');
											$('#maxQuestions').hide();

											$("#maxQuestnToShow").prop(
													'required', false);

											$('#sameMarks').hide();

											$("#SMQYes")
													.prop('required', false);

											$('input:radio[name=sameMarks]')
													.each(
															function() {
																$(this)
																		.prop(
																				'checked',
																				false);
															});

											$('#specifyQuestion').hide();

											$("#MaxYes")
													.prop('required', false);

											$('input:radio[name=specifyMaxQue]')
													.each(
															function() {
																$(this)
																		.prop(
																				'checked',
																				false);
															});

											$('#marksPerQue').hide();

											$("#marksPerQueIn").prop(
													'required', false);

											/* $('#mixQuestions').hide();

											$("#maxQuestnToShow1").prop('required', false);

											$("#maxQuestnToShow2").prop('required', false);

											$("#maxQuestnToShow3").prop('required', false);
											
											$("#maxQuestnToShow4").prop('required', false); */

										});

					});
				</script>

				<script>
					$(function() {

						$('#SMQYes').on('click', function() {

							$('#marksPerQue').show();

							$("#marksPerQueIn").prop('required', true);

						});

						$('#SMQNo').on('click', function() {

							$('#marksPerQue').hide();

							$("#marksPerQueIn").prop('required', false);

						});

					});
				</script>

				<script>
					$(function() {

						$('#MaxYes').on('click', function() {

							var selected = $('#testType').val();

							if (selected == 'Mix') {

								$('#mixQuestions').show();

								$("#maxQuestnToShow1").prop('required', true);

								$("#maxQuestnToShow2").prop('required', true);

								$("#maxQuestnToShow3").prop('required', true);

								$("#maxQuestnToShow4").prop('required', true);
							}

						});

						$('#MaxNo').on('click', function() {

							$('#mixQuestions').hide();

							$("#maxQuestnToShow1").prop('required', false);

							$("#maxQuestnToShow2").prop('required', false);

							$("#maxQuestnToShow3").prop('required', false);

							$("#maxQuestnToShow4").prop('required', false);

						});

					});
				</script>

				<script>
					$(function() {
						var isRandom = $('input[name=randomQuestion]:checked')
								.val();
						var isSame = $('#smqChk').val();

						var selected = $('#testType').val();
						$('#randQReq')
								.on(
										'click',
										function() {
											isRandom = $(
													'input[name=randomQuestion]:checked')
													.val();
											//alert(isSame)
											isSame = $(
													'input[name=randomQuestion]:checked')
													.val();
											console.log("same: " + isSame)
											selected = $('#testType').val();
											console.log("Random Chnaged");
											if (isRandom == 'Y'
													&& selected == 'Mix') {
												//alert("hello");
												console.log("Type Mix");
												//$('#smqChk').prop('checked', true);
												//$('#smqChk').attr('disabled', true);
												//$('#marksPerQueIn').parent().toggleClass('d-none');
												//$('#smqChkForOthers').toggleClass('d-none');
												//$('#smqChkForMix').toggleClass('d-none');
												$('#mixQuestions').show();

												$("#maxQuestnToShow1").prop(
														'required', true);

												$("#maxQuestnToShow2").prop(
														'required', true);

												$("#maxQuestnToShow3").prop(
														'required', true);

												$("#maxQuestnToShow4").prop(
														'required', true);
											} else {
												console.log("Type Mix Hide");
												//$('#smqChk').prop('checked', false);
												//$('#smqChkForOthers').toggleClass('d-none');
												//$('#smqChkForMix').toggleClass('d-none');
												//$('#smqChkForMix').toggleClass('d-none');
												//$('#smqChk').attr('disabled', false);
												//$('#marksPerQueIn').parent().toggleClass('d-none');
												$('#mixQuestions').hide();

												$("#maxQuestnToShow1").prop(
														'required', false);

												$("#maxQuestnToShow2").prop(
														'required', false);

												$("#maxQuestnToShow3").prop(
														'required', false);

												$("#maxQuestnToShow4").prop(
														'required', false);

											}

										});
						if (isRandom == 'Y' && selected == 'Mix') {
							console.log("Type Mix");
							//$('#smqChk').prop('checked', true);
							//$('#smqChk').attr('disabled', true);
							//$('#marksPerQueIn').parent().toggleClass('d-none');

							$('#mixQuestions').show();

							$("#maxQuestnToShow1").prop('required', true);

							$("#maxQuestnToShow2").prop('required', true);

							$("#maxQuestnToShow3").prop('required', true);

							$("#maxQuestnToShow4").prop('required', true);
						} else {
							//$('#smqChk').prop('checked', false);
							//$('#smqChk').attr('disabled', false);
							$('#mixQuestions').hide();
							//$('#marksPerQueIn').parent().toggleClass('d-none');

							$("#maxQuestnToShow1").prop('required', false);

							$("#maxQuestnToShow2").prop('required', false);

							$("#maxQuestnToShow3").prop('required', false);

							$("#maxQuestnToShow4").prop('required', false);

						}

						//$('#smqChkForOthers').toggleClass('d-none');

					});
				</script>

				<script>
					$(function() {

						$('#testType')
								.on(
										'change',
										function() {
											//For Mix Test
											$(
													".testTypeCheckBox input[type='checkbox']")
													.val("N").prop("checked",
															false);
											$(".testTypeCheckBox .form-check")
													.addClass("d-none");
											$(
													".testTypeCheckBox .form-check input")
													.val(0);

											//alert('slidYes');

											var isRandom = $(
													'input[name=randomQuestion]:checked')
													.val();
											var selected = $('#testType').val();

											if (isRandom == 'Y'
													&& selected == 'Mix') {

												$('#specifyQuestion').show();

												$("#MaxYes").prop('required',
														true);

												$('#mixQuestions').show();

												$("#maxQuestnToShow1").prop(
														'required', true);

												$("#maxQuestnToShow2").prop(
														'required', true);

												$("#maxQuestnToShow3").prop(
														'required', true);

												$("#maxQuestnToShow4").prop(
														'required', true);
											}
											if (selected != 'Mix') {

												$('#specifyQuestion').hide();

												$("#MaxYes").prop('required',
														false);

												$(
														'input:radio[name=specifyMaxQue]')
														.each(
																function() {
																	$(this)
																			.prop(
																					'checked',
																					false);
																});

												$('#mixQuestions').hide();

												$("#maxQuestnToShow1").prop(
														'required', false);

												$("#maxQuestnToShow2").prop(
														'required', false);

												$("#maxQuestnToShow3").prop(
														'required', false);

												$("#maxQuestnToShow4").prop(
														'required', false);
											}

										});

					});
				</script>


				<script>
					$(function() {

						$('#setPasswordY').on('click', function() {
							//alert('slidYes');
							$('#passwordForTest').show();

							$("#passwordForTestText").prop('required', true);

						});

						$('#setPasswordN').on('click', function() {
							//alert('slidNo');
							$('#passwordForTest').hide();

							$("#passwordForTestText").prop('required', false);

						});

					});
				</script>


				<script>
					
				$(".subjectParam")
				.on(
						'change',
						function() {

							console.log("subject param entered");
							var acadYear = $('#acadYear').val();
							var campusId = $('#campus').val();
							
							var courseid = $('#moduleId').val();
							

							if (campusId == null) {
								console
										.log("undefined campusId"
												+ campusId);
								//campusId='null';
							}
							console.log('acad year'+acadYear+' campusId'+campusId);
							if (acadYear) {
							
								$
								.ajax({
									type : 'GET',
									url : '${pageContext.request.contextPath}/getModuleByParamForTest?'
											+ 'acadYear='
											+ acadYear
											+ '&campusId='
											+ campusId,
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
				<script>
				$(".subParam")
				.on(
						'change',
						function() {

							console.log("subject param entered");
							
							
							var courseid = $('#moduleId').val();
							var acadYear = $('#acadYear').val();
							var campusId = $("#campusId").val();
						
							if (courseid && acadYear) {
							
								$
								.ajax({
									type : 'GET',
									url : '${pageContext.request.contextPath}/getFacultyByModuleTest?'
											+ 'moduleId='
											+ courseid
											+ '&acadYear='
											+ acadYear,
									success : function(data) {
										console.log("sucess");
										console.log(data);

										var json = JSON.parse(data);
										var optionsAsString = "";

										$('#assignedFaculty').find(
												'option').remove();
										console.log(json);
										for (var i = 0; i < json.length; i++) {
											var idjson = json[i];
											console.log(idjson);

											for ( var key in idjson) {

												optionsAsString += "<option value='" +key + "'>"
														+ idjson[key]
														+ "</option>";

											}
										}
										console
												.log("optionsAsString"
														+ optionsAsString);

										$('#assignedFaculty')
												.append(
														optionsAsString);

									}
								});
					} else {
						console.log("no course");
					}

				});
						
				
				</script>
				<script>
					$(document)
							.ready(
									function() {

										var course = '${courseIdVal}';
										var username = $('#username').val();

										$(".facultyparameter")
												.on(
														'change',
														function() {
															var courseid = $(
																	'#idOfCourse')
																	.val();
															console
																	.log("called");
															var acadMonth = $(
																	'#acadMonth')
																	.val();
															var acadYear = $(
																	'#acadYear')
																	.val();
															console
																	.log(courseid);
															if (courseid) {
																$
																		.ajax({
																			type : 'GET',
																			url : '${pageContext.request.contextPath}/getFacultyByCourseForFeedback?'
																					+ 'courseId='
																					+ courseid,
																			success : function(
																					data) {
																				console
																						.log("sucess");
																				console
																						.log(data);

																				var json = JSON
																						.parse(data);
																				var optionsAsString = "";

																				$(
																						'#facultyid')
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
																						if (username == idjson[key]) {
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
																				console
																						.log("optionsAsString"
																								+ optionsAsString);

																				$(
																						'#facultyid')
																						.append(
																								optionsAsString);

																			}
																		});
															} else {
																console
																		.log("no course");
															}
														});

										//------------------------------------ side bar course dependent------------------------------------
										console.log(course);
										$(".facultyparameter")
												.on(
														'change',
														function() {

															var acadMonth = $(
																	'#acadMonth')
																	.val();
															var acadYear = $(
																	'#acadYear')
																	.val();

															if (course
																	&& acadMonth
																	&& acadYear) {
																$
																		.ajax({
																			type : 'GET',
																			url : '${pageContext.request.contextPath}/getFacultyByCourseForFeedback?'
																					+ 'courseId='
																					+ course,
																			success : function(
																					data) {
																				var json = JSON
																						.parse(data);
																				var optionsAsString = "";

																				$(
																						'#facultyid')
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
																						if (username == idjson[key]) {
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
																				console
																						.log("optionsAsString"
																								+ optionsAsString);

																				$(
																						'#facultyid')
																						.append(
																								optionsAsString);

																			}
																		});
															} else {
																console
																		.log("no course");
															}

														});
										$('#idOfCourse').trigger('change');

									});
					//------------------------------------ side bar course dependent------------------------------------

					/* 	var TommorowDate = new Date();
						TommorowDate.setDate(TommorowDate.getDate() + 1);

						$("#datetimepicker1").on("dp.change", function(e) {

							validDateTimepicks();
						}).datetimepicker({
							minDate : TommorowDate,
							useCurrent : false,
							format : 'YYYY-MM-DD HH:mm:ss'
						});

						$("#datetimepicker2").on("dp.change", function(e) {

							validDateTimepicks();
						}).datetimepicker({
							minDate : TommorowDate,
							useCurrent : false,
							format : 'YYYY-MM-DD HH:mm:ss'
						});

						function validDateTimepicks() {
							if (($('#startDate').val() != '' && $('#startDate').val() != null)
									&& ($('#endDate').val() != '' && $('#endDate').val() != null)) {
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
				</script>
				<script>
					/* setTimeout(function(){
						$('#startDate').val('');
					}, 600);
					 */
				</script>