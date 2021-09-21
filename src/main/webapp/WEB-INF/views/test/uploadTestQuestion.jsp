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
<sec:authorize access="hasRole('ROLE_ADMIN')">

<jsp:include page="../common/newAdminLeftNavBar.jsp"/>
<jsp:include page="../common/rightSidebarAdmin.jsp" />
</sec:authorize>

<!-- DASHBOARD BODY STARTS HERE -->

<div class="container-fluid m-0 p-0 dashboardWraper">

		<sec:authorize access="hasRole('ROLE_FACULTY')">
	<jsp:include page="../common/newTopHeaderFaculty.jsp" />
</sec:authorize>
<sec:authorize access="hasRole('ROLE_ADMIN')">
<jsp:include page="../common/newAdminTopHeader.jsp" />
</sec:authorize>
     
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
                                <li class="breadcrumb-item active" aria-current="page">Upload Test Questions</li>
                            </ol>
                        </nav>
                        
						<jsp:include page="../common/alert.jsp" />
						
						
						<!-- Input Form Panel -->
						<div class="card bg-white border">
						<div class="card-body">
									<div class="text-center">
										<h5 class="border-bottom pb-2">Upload Test Questions</h5>
										

										<%-- <a href="viewTestDetails?testId=${testId}"><i
											class="btn btn-large btn-primary" style="float: right;">Proceed
												to allocate students</i></a> --%>
										
									</div>

									<div class="x_content">
										<form:form action="uploadTestQuestion" method="post"
											modelAttribute="test" enctype="multipart/form-data">
											<form:input type="hidden" value="${test.courseId}"
												path="courseId"></form:input>

											<div class="row">
												<c:if test="${test.id eq null}">
													<div class="col-md-4 col-sm-6">

														<div class="form-group">
															<form:label path="id" for="testName" class="textStrong">Test Name <span style="color: red">*</span></form:label>
															<form:select id="testName" path="id"
																class="form-control facultyparameter"
																required="required">

																<form:option value="">Select Test</form:option>

																<c:forEach var="test" items="${testListOfFaculty}"
																	varStatus="status">

																	<form:option value="${test.id}">${test.testName}</form:option>




																</c:forEach>


															</form:select>
														</div>

													</div>
												</c:if>
												
												<c:if test="${test.id ne null}">
													<form:input type="hidden" value="${test.id}" path="id"></form:input>
													<form:input type="hidden" value="${test.testType}"
														path="testType"></form:input>
												</c:if>
												
												
										
												<div class="col-md-4 col-sm-6">
													<div class="form-group">
														<label class="textStrong" for="file">Upload Test Question
															File(Excel):</label>
															<input id="file" name="file" type="file"
															class="form-control" required="required"/>
													</div>
												</div>
												
												<c:if test="${showProceed}">
											<div class="col-sm-4 col-md-4">
												<div class="form-group">
													<a href="viewTestDetails?testId=${testId}"><i
														class="btn btn-large btn-primary"
														style="float: right; font-size: 15px;">Proceed to
															allocate students</i></a>
												</div>
											</div>
										</c:if>
										<c:if test="${showStudents}">
											<div class="col-sm-4 col-md-4">
												<div class="form-group">
													<a href="viewTestDetails?testId=${testId}"><i
														class="btn btn-large btn-primary"
														style="float: right; font-size: 15px;">Proceed to view
															allocated students</i></a>
												</div>
											</div>
										</c:if>
												
												
													<div class="col-sm-12 column">
													<div class="form-group">

														<button type="submit" id="submit" class="btn btn-large btn-primary"
															formaction="uploadTestQuestion">Upload</button>

														<button id="cancel" class="btn btn-danger"
															formaction="homepage" formnovalidate="formnovalidate">Cancel</button>
													</div>
													<c:if test="${test.testType eq 'Objective'}">
													 <p>
																<a href="resources/templates/testQuestionObjective.xlsx"><font
																	color="red">Download a sample template</font></a>
															</p> 
													</c:if>
													<c:if test="${test.testType eq 'Subjective'}">
													<p>
																<a href="resources/templates/testQuestSubjective.xlsx"><font
																	color="red">Download a sample template</font></a>
															</p>
													</c:if>
												</div>
												
												<c:if test="${test.testType eq 'Objective'}">
													<div class="col-12">
														<div class="form-group">


															<label class="control-label textStrong" for="courses">Excel
																Format: </label>
															<p>Description | Marks | Type | Question-Type |
																Options Shuffle Required | option 1 | option 2 | option
																3 | option 4 | option 5 | option 6 | option 7 | option 8
																| Answer Range From | Answer Range To | correctOption</p>
															<p>
																<b>Note:</b>
															<ul>
																<li><b><font color="red">--></font>Description
																</b>is a test question</li>
																<li><b><font color="red">--></font>Type </b>can be
																	SINGLESELECT or MULTISELECT</li>
																<li><b><font color="red">--></font>Question-Type
																</b>can be MCQ or Numeric</li>
																<li><b><font color="red">--></font>Marks </b>Should
																	be a valid Integer/Decimal value</li>
																<li><b><font color="red">--></font>Correct
																		Option Column </b>should be be a valid Integer value in
																	case question type is MCQ AND it has to be correct
																	option number not correct answer.</li>
																<li><b><font color="red">--></font>Correct
																		Option Column </b>should be be a valid Integer/Decimal
																	value in case question type is Numeric AND it has to be
																	correct answer value, Options,type will remain blank in
																	this case.</li>
																<li><b><font color="red">--></font>Correct
																		Option Column </b>should be be a valid Integer value</li>
																<li><b><font color="red">--></font>Answer Range
																		From</b>is specifically for Numeric Questions and it is
																	minimum range answer value and can be Integer/Decimal.</li>
																<li><b><font color="red">--></font>Answer Range
																		To</b>is specifically for Numeric Questions and it is
																	maximum range answer value and can be Integer/Decimal.</li>

															</ul>
															<br>
															



														</div>
													</div>
												</c:if>
												<c:if test="${test.testType eq 'Subjective'}">
													<div class="col-12">
														<div class="form-group">


															<label class="control-label" for="courses">Excel
																Format: </label>
															<p>question | totalScore</p>
															<p>
																<b>Note:</b>
															<ul>

																<li>Total Score Should be a valid Integer value</li>
															</ul>
															<br>
															



														</div>
													</div>
												</c:if>
											</div>


											

										</form:form>
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
	        <sec:authorize access="hasRole('ROLE_FACULTY')">
	         <jsp:include page="../common/newSidebar.jsp" />
	         </sec:authorize>
	        <!-- SIDEBAR END -->
			<sec:authorize access="hasRole('ROLE_FACULTY')">
			<jsp:include page="../common/footer.jsp"/>
			</sec:authorize>
			<sec:authorize access="hasRole('ROLE_ADMIN')">
			<jsp:include page="../common/newAdminFooter.jsp"/>
			</sec:authorize>

	













