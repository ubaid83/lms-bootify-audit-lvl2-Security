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
                                <li class="breadcrumb-item active" aria-current="page"> View Each Test</li>
                            </ol>
                        </nav>
                        
						<jsp:include page="../common/alert.jsp" />
						
						
						<!-- Input Form Panel -->
						<div class="card bg-white border">
						<div class="card-body">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">

										<h5 class="text-center border-bottom pb-2">Test Details</h5>
			
									<div class="x_content">

										<form:form action="addTest" method="post"
											modelAttribute="test">

											<div class="row">
												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<form:label path="courseName" for="courseName">Course :</form:label>
														${test.courseName}
													</div>
												</div>
												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<form:label path="acadMonth" for="acadMonth">Academic Month :</form:label>
														${test.acadMonth}
													</div>
												</div>
												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<form:label path="acadYear" for="acadYear">Academic Year :</form:label>
														${test.acadYear}
													</div>
												</div>
												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<form:input type="hidden" path="courseId" />
													<div class="form-group">
														<form:label path="testName" for="testName">Test Name :</form:label>
														${test.testName}
													</div>
												</div>
												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<form:label path="duration" for="duration">Test Duration :</form:label>
														<c:out
															value="${fn:replace(test.duration,'T', ' ')}${' Minutes'}"></c:out>
													</div>
												</div>
												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<form:label path="startDate" for="startDate">Start Date :</form:label>
														<c:out
															value="${fn:replace(test.startDate, 
                                'T', ' ')}"></c:out>
													</div>
												</div>
												<%-- <div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<form:label path="dueDate" for="dueDate">Due Date :</form:label>
														<c:out value="${fn:replace(test.dueDate,'T', ' ')}"></c:out>
													</div>
												</div> --%>
												<sec:authorize access="hasRole('ROLE_FACULTY')">
													<div class="col-md-4 col-sm-6 col-xs-12 column">
														<div class="form-group">
															<form:label path="endDate" for="endDate">End Date :</form:label>
															<c:out value="${fn:replace(test.endDate,'T', ' ')}"></c:out>
														</div>
													</div>
													<div class="col-md-4 col-sm-6 col-xs-12 column">
														<div class="form-group">
															<form:label path="sendEmailAlert" for="sendEmailAlert">Allow Submission after End date :</form:label>
															${test.allowAfterEndDate}

														</div>
													</div>
													<div class="col-md-4 col-sm-6 col-xs-12 column">
														<div class="form-group">
															<form:label path="sendEmailAlert" for="sendEmailAlert">Send Email Alert for New Test :</form:label>
															${test.sendEmailAlert}
														</div>
													</div>
													<div class="col-md-4 col-sm-6 col-xs-12 column">
														<div class="form-group">
															<form:label path="sendSmsAlert" for="sendSmsAlert">Send SMS Alert for New Test :</form:label>
															${test.sendSmsAlert}
														</div>
													</div>
													<div class="col-md-4 col-sm-6 col-xs-12 column">
														<div class="form-group">
															<form:label path="showResultsToStudents"
																for="showResultsToStudents">Show Results to Students immediately :</form:label>
															${test.showResultsToStudents}
														</div>
													</div>
												</sec:authorize>
												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<form:label path="maxScore" for="maxScore">Score Out of :</form:label>
														${test.maxScore}
													</div>
												</div>
												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<form:label path="facultyId" for="facultyId">Faculty :</form:label>
														${test.facultyId}
													</div>
												</div>

											</div>
											<div class="row">

												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<sec:authorize access="hasRole('ROLE_FACULTY')">
															<c:url value="addTestForm" var="editTestUrl">
																<c:param name="id">${test.id}</c:param>
																<c:param name="courseId">${test.courseId}</c:param>
															</c:url>

															<c:url value="addTestQuestionForm" var="addTestQuestion">
																<c:param name="id">${test.id}</c:param>
															</c:url>

															<%-- <button id="submit" class="btn btn-large btn-primary"
																formaction="${editTestUrl}">Edit</button> --%>
															<button id="cancel" class="btn btn-danger"
																formaction="searchTest" formnovalidate="formnovalidate">Back</button>

														</sec:authorize>
														<sec:authorize access="hasRole('ROLE_STUDENT')">
															<c:url value="startStudentTest" var="takeTestUrl">
																<c:param name="id" value="${test.id}" />
															</c:url>
															<c:url value="testList" var="testListUrl">
																<c:param name="courseId">${test.courseId}</c:param>
															</c:url>

															<c:if test="${test.isPasswordForTest eq 'N'}">

																<a href="${takeTestUrl}" title="Start Test"
																	onclick="return confirm('Are you ready to take the test?')"><i
																	class="fa fa-pencil-square-o fa-lg"></i></a>
															</c:if>
															<c:if test="${test.isPasswordForTest eq 'Y'}">
																<a href="${testList}" title="Start Test"><i
																	class="fa fa-pencil-square-o fa-lg"></i></a>&nbsp;
															</c:if>


														</sec:authorize>
													</div>
												</div>

											</div>



										</form:form>
									</div>
								</div>
							</div>
						</div>
						</div>
						<sec:authorize access="hasRole('ROLE_FACULTY')">

						<!-- Results Panel -->

						
								<div class="card bg-white border">
								<div class="card-body">								
									<div class="col-xs-12 col-sm-12">

									<div class="x_panel">

											<h5 class="text-center pb-2 border-bottom">Evaluate Students Test</h5>
				
										<div class="x_content">

											<form:form action="saveStudentTest" id="saveStudentTest"
												method="post" modelAttribute="test">

												<form:input path="id" type="hidden" />
												<form:input path="acadMonth" type="hidden" />
												<form:input path="acadYear" type="hidden" />
												<form:input path="facultyId" type="hidden" />
												<form:input path="courseId" type="hidden"
													value="${courseId}" />
												<div class="table-responsive">
													<table class="table table-hover">
														<thead>
															<tr>
															
																<th>Sr. No.</th>
																<th>SAPID</th>
																<th>Roll No.</th>
																<th>Student Name</th>
																<th>Evaluate</th>
															</tr>
														</thead>

														<tbody>

															<c:forEach var="student" items="${students}"
																varStatus="status">
																<tr>
																	<td><c:out value="${status.count}" /></td>
																	<td><c:out value="${student.username}" /></td>
																	<td><c:out value="${student.rollNo}" /></td>
																	<td><c:out
																			value="${student.firstname} ${student.lastname}" /></td>
																	<c:url value="evaluateTestForm" var="evaluateTest">
																		<c:param name="id">${test.id}</c:param>
																		<c:param name="studusername">${student.username}</c:param>
																	</c:url>

																	<td><c:choose>
																			<c:when test="${student.testCompleted == 'Y' }">
																				<button class="btn btn-large btn-primary"
																					formaction="${evaluateTest}">Evaluate Test</button>
																			</c:when>
																			<c:otherwise>
																			Pending
																		</c:otherwise>

																		</c:choose></td>

																</tr>
															</c:forEach>
														</tbody>
													</table>
												</div>

												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group"></div>
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
                   </sec:authorize>
                    </div>
			
			<!-- SIDEBAR START -->
	         <jsp:include page="../common/newSidebar.jsp" />
	        <!-- SIDEBAR END -->
	<jsp:include page="../common/footer.jsp"/>

	













