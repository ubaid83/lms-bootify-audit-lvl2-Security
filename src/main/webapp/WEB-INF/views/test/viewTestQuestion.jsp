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
                                <li class="breadcrumb-item active" aria-current="page"> Added Test Question</li>
                            </ol>
                        </nav>
                        
						<jsp:include page="../common/alert.jsp" />
						
						
						<!-- Input Form Panel -->
						<div class="card bg-white border">
						<div class="card-body">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">

									<div class="x_title">
										<h5>Added Test Question</h5>
										<c:if test="${allocation eq 'Y'}"> <a href="viewTestDetails?testId=${testId}"><i
											class="btn btn-large btn-primary"
											style="float: right; font-size: 15px;"> Proceed to
												allocate students</i></a>
										</c:if>
										
										
									</div>

									<div class="x_content">
										<c:choose>
											<c:when test="${testQuestionList.size() > 0}">


												<div class="table-responsive">
													<table class="table  table-hover">
														<thead>
															<tr>
																<th>Sr. No.</th>
																<th>Marks</th>
																<th>Question</th>
																<th>Type</th>
																<th>Options</th>

															</tr>
														</thead>
														<tbody>

															<c:forEach var="test" items="${testQuestionList}"
																varStatus="status">
																<%-- <form:input path="testId" value="${test.testId}" type="hidden" /> --%>
																<tr>
																	<td><c:out value="${status.count}" /></td>
																	<td><c:out value="${test.marks}" /></td>
																	<td>${test.description}</td>
																	<td><c:out value="${test.type}" /></td>
																	<td>
																		<div class="table-responsive">
																			<table class="table  table-hover">
																				<thead>
																					<tr>

																						<c:if test="${not empty test.option1}">
																							<th>Option1</th>
																						</c:if>
																						<c:if test="${not empty test.option2}">
																							<th>Option2</th>
																						</c:if>
																						<c:if test="${not empty test.option3}">
																							<th>Option3</th>
																						</c:if>
																						<c:if test="${not empty test.option4}">
																							<th>Option4</th>
																						</c:if>

																						<c:if test="${not empty test.option5}">
																							<th>Option5</th>
																						</c:if>
																						<c:if test="${not empty test.option6}">
																							<th>Option6</th>
																						</c:if>
																						<c:if test="${not empty test.option7}">
																							<th>Option7</th>
																						</c:if>
																						<c:if test="${not empty test.option8}">
																							<th>Option8</th>
																						</c:if>
																						<th>Correct Option</th>





																					</tr>
																				</thead>
																				<tbody>




																					<tr>

																						<c:if test="${not empty test.option1}">
																							<td><c:out value="${test.option1}" /></td>
																						</c:if>
																						<c:if test="${not empty test.option2}">
																							<td><c:out value="${ test.option2}" /></td>
																						</c:if>
																						<c:if test="${not empty test.option3}">
																							<td><c:out value="${test.option3}" /></td>
																						</c:if>
																						<c:if test="${not empty test.option4}">
																							<td><c:out value="${test.option4}" /></td>
																						</c:if>

																						<c:if test="${not empty test.option5}">
																							<td><c:out value="${test.option5}" /></td>
																						</c:if>
																						<c:if test="${not empty test.option6}">
																							<td><c:out value="${test.option6}" /></td>
																						</c:if>
																						<c:if test="${not empty test.option7}">
																							<td><c:out value="${test.option8}" /></td>
																						</c:if>
																						<c:if test="${not empty test.option8}">
																							<td><c:out value="${test.option7}" /></td>
																						</c:if>
																						<td><c:out
																								value="Option ${test.correctOption}" /></td>





																					</tr>


																				</tbody>
																			</table>
																		</div>
																	</td>



																</tr>
															</c:forEach>

														</tbody>
													</table>
													<div class="row">

														<div class="col-sm-8 column">
															<div class="form-group">
																<button id="cancel" class="btn btn-danger ml-3" type="button"
																	formaction="homepage" formnovalidate="formnovalidate"
																	onclick="history.go(-1);">Back</button>
															</div>
														</div>
													</div>
												</div>

											</c:when>
										</c:choose>

										<c:choose>
											<c:when test="${testQuestionListSub.size() > 0}">


												<div class="table-responsive">
													<table class="table  table-hover">
														<thead>
															<tr>
																<th>Sr. No.</th>
																<th>Marks</th>
																<th>Question</th>


															</tr>
														</thead>
														<tbody>

															<c:forEach var="test" items="${testQuestionListSub}"
																varStatus="status">
																<%-- <form:input path="testId" value="${test.testId}" type="hidden" /> --%>
																<tr>
																	<td><c:out value="${status.count}" /></td>
																	<td><c:out value="${test.marks}" /></td>
																	<td>${test.description}</td>




																</tr>
															</c:forEach>

														</tbody>
													</table>
													<div class="row">

														<div class="col-sm-8 column">
															<div class="form-group">
																<button id="cancel" class="btn btn-danger ml-3" type="button"
																	formaction="homepage" formnovalidate="formnovalidate"
																	onclick="history.go(-1);">Back</button>
															</div>
														</div>
													</div>
												</div>

											</c:when>
										</c:choose>
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
	         <jsp:include page="../common/newSidebar.jsp" />
	        <!-- SIDEBAR END -->
	<jsp:include page="../common/footer.jsp"/>

	<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.4/MathJax.js?config=TeX-AMS_HTML"></script>
	













