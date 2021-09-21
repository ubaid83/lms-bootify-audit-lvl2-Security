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
                                <li class="breadcrumb-item active" aria-current="page"> Search Tests</li>
                            </ol>
                        </nav>
                        
						<jsp:include page="../common/alert.jsp" />
						
						
						<!-- Input Form Panel -->
						<div class="card bg-white border">
						<div class="card-body">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">



									<div class="x_content">
										<form:form action="viewThisTest" method="post"
											modelAttribute="test">



											<c:choose>
												<c:when test="${testList.size() > 0}">
													<div class="row">
														<div class="col-xs-12 col-sm-12">

															<div class="x_title">
																<h5>
																	Tests<font size="2px"> | ${testList.size()}
																		Records Found &nbsp; </font>
																</h5>
																</div>
															</div>
															<div class="x_content">
																<div class="table-responsive">
																	<table class="table table-hover"
																		style="font-size: 12px">
																		<thead>
																			<tr>
																				<th>Sr. No.</th>
																				<th>Username</th>
																				<th>Name</th>
																				<th>Course</th>

																				<th>Attempts</th>
																				<th>Start Time</th>
																				<th>End Time</th>
																				<th>Test Completed</th>
																				<th>Score</th>
																			</tr>
																		</thead>
																		<tbody>

																			<c:forEach var="test" items="${testList}"
																				varStatus="status">
																				<tr>
																					<td><c:out value="${status.count}" /></td>
																					<td><c:out value="${test.username}" /></td>
																					<td><c:out value="${test.testName}" /></td>
																					<td><c:out value="${test.courseName}" /></td>
																					<td><c:out value="${test.attempt}" /></td>

																					<td><c:out
																							value="${fn:replace(test.testStartTime,'T', ' ')}"></c:out></td>
																					<td><c:out
																							value="${fn:replace(test.testEndTime, 
                                'T', ' ')}"></c:out></td>

																					<td><c:if test="${test.testCompleted eq 'Y' }">
									Yes
								                                        </c:if> <c:if
																							test="${test.testCompleted ne 'Y' }">
								No
								
																		
																		
																		</c:if></td>
																					<td><c:out value="${test.score}" /></td>
																				</tr>
																			</c:forEach>


																		</tbody>
																	</table>
																</div>
															</div>

														</div>
													
												</c:when>
											</c:choose>
											<div class="row">

												<div class="col-sm-8 column">
													<div class="form-group">
											<button id="cancel" class="btn btn-danger" type="button"
															formaction="homepage" formnovalidate="formnovalidate"
															onclick="history.go(-1);">Back</button>
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

						
								<div class="card bg-white border">
								<div class="card-body">								
									
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

	













