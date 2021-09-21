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
                                <li class="breadcrumb-item active" aria-current="page"> Test List</li>
                            </ol>
                        </nav>
                        
						<jsp:include page="../common/alert.jsp" />
						
						
						<!-- Input Form Panel -->
					

						<!-- Results Panel -->

						
								<div class="card bg-white border">
								<div class="card-body">								
									<div class="col-xs-12 col-sm-12">
										<div class="x_panel">
											<div class="x_title">
												<h5>Test Record| ${programList.size()} Records Found</h5>
										
										
											</div>
											<div class="x_content">
												<div class="table-responsive testAssignTable">
													<table class="table table-hover ">
														<thead>

															<tr>
																<th>Sr. No.</th>
																<th>Name</th>
																<th>Course</th>
																<th>Academic Month</th>
																<th>Academic Year</th>
																<th>Start Date</th>
																<!-- <th>Due Date</th> -->
																<th>End Date</th>
																<th>Actions</th>
															</tr>
														</thead>
														<tbody>

															<c:forEach var="test" items="${programList}"
																varStatus="status">
																<tr>
																	<td><c:out value="${status.count}" /></td>
																	<td><c:out value="${test.testName}" /></td>
																	<td><c:out value="${test.courseName}" /></td>
																	<td><c:out value="${test.acadMonth}" /></td>
																	<td><c:out value="${test.acadYear}" /></td>
																	<td><c:out
																			value="${fn:replace(test.startDate, 
                                'T', ' ')}"></c:out></td>
																	<td><c:out
																			value="${fn:replace(test.endDate, 
                                'T', ' ')}"></c:out></td>
																	<%-- <td><c:out
																			value="${fn:replace(test.dueDate, 
                                'T', ' ')}"></c:out></td> --%>
																	<td>
																	<c:choose>
																	<c:when test="${'Y' eq test.isCreatedByAdmin }">
																	</c:when>
																	<c:otherwise>
																	<c:url value="createTestForm" var="editurl">
																			<c:param name="id" value="${test.id}" />
																		</c:url> <c:url value="viewTestDetailsToEvaluate"
																			var="evaluateurl">
																			<c:param name="testId" value="${test.id}" />
																		</c:url> <sec:authorize access="hasAnyRole('ROLE_FACULTY')">
																			<c:url value="deleteTest" var="deleteurl">
																				<c:param name="programId" value="${test.id}" />
																			</c:url>
																		</sec:authorize> <c:url value="viewTestDetails" var="detailsUrl">
																			<c:param name="testId" value="${test.id}" />
																		</c:url> <a href="${detailsUrl}" title="Details"><i
																			class="fas fa-info-circle fa-lg"></i></a>&nbsp; <sec:authorize
																			access="hasAnyRole('ROLE_FACULTY')">
																			<a href="${editurl}" title="Edit"><i
																				class="fas fa-pen-square"></i></a>&nbsp;
											</sec:authorize> <sec:authorize access="hasAnyRole('ROLE_FACULTY')">
																			<a href="${deleteurl}" title="Delete"
																				onclick="return confirm('Are you sure you want to delete this record?')"><i
																				class="fas fa-trash"></i></a>
																		</sec:authorize> <sec:authorize access="hasAnyRole('ROLE_FACULTY')">
																			<c:if test="${test.testType == 'Subjective' || test.testType == 'Mix' }">
																				<a href="${evaluateurl}" title="Evaluate"><i
																					class="fa fa-check-square fa-lg"></i></a>
																			</c:if>

																		</sec:authorize> <c:url value="addTestQuestionForm" var="addurl">
																			<c:param name="id" value="${test.id}" />
																		</c:url> <sec:authorize access="hasAnyRole('ROLE_FACULTY')">
																			<a href="${addurl}" title="Configure Questions"><i
																				class="fa fa-question-circle" aria-hidden="true"
																				style="font-size: medium;"></i></a>&nbsp;
                                                         </sec:authorize>
                                                         </c:otherwise>
																	</c:choose></td>
																</tr>
															</c:forEach>


														</tbody>
													</table>
												</div>
											</div>
										</div>
									</div>
								</div>
								</div>
							

					<%-- 	<jsp:include page="../common/paginate.jsp">
							<jsp:param name="baseUrl" value="searchFacultyGroups" />
						</jsp:include> --%>

			<!-- /page content: END -->
                   
                    </div>
			
			<!-- SIDEBAR START -->
	         <jsp:include page="../common/newSidebar.jsp" />
	        <!-- SIDEBAR END -->
	<jsp:include page="../common/footer.jsp"/>

	













