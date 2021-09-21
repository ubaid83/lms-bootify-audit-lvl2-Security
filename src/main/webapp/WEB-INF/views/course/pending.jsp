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
                                <li class="breadcrumb-item active" aria-current="page">  To Do Everyday </li>
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
											<img
												src="https://d30y9cdsu7xlg0.cloudfront.net/png/51139-200.png"
												width=20 height=20> To Do Everyday
										</h5>
										
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
									<div class="x_title">
										<h5>Pending Tasks | ${fn:length(task)} Records Found</h5>
										
									</div>
									<div class="x_itemCount" style="display: none;">
										<div class="image_not_found">
											<i class="fa fa-newspaper-o"></i>
											<p>
												<label class=""></label>${fn:length(task)} Tasks
											</p>
										</div>
									</div>
									<div class="x_content">
										<table class="table table-hover personal-task">

											<tbody>
												<tr>
													<td><span class="badge bg-success">Task name</span></td>
													<td><span class="badge bg-important"> Task type</span></td>
													<td><span class="badge bg-success">Task Id</span></td>
													<td><span class="badge bg-primary">Task Start
															Date Date</span></td>
													<td><span class="badge bg-primary">Task End
															Date Date</span></td>
													<td><span class="badge bg-success">Action Url</span></td>

												</tr>


												<c:forEach items="${task}" var="entry" varStatus="loop">
													<tr>
														<td>${entry.taskName}</td>
														<td><img
															src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRDu6IaowMFcVtMkq18mnrsnQ4MvEY5QZRso8ZAp0ageN5EGUC2"
															height=20 width=20>${entry.type}</td>
														<td>${entry.id}</td>
														<td><span class="badge bg-info">${entry.startDate}</span></td>
														<td><span class="badge bg-info">${entry.endDate}</span></td>

														<!-- <td>${entry.url}</td>-->
														<td><sec:authorize access="hasRole('ROLE_STUDENT')">

																<c:url value="submitAssignmentForm"
																	var="assignmentSubmitUrl">
																	<c:param name="id" value="${entry.id}" />
																</c:url>
																<c:url value="startStudentTest"
																	var="startStudentTestUrl">
																	<c:param name="id" value="${entry.id}" />
																</c:url>
																<c:url value="startStudentTestForSubjective"
																	var="startStudentTestForSubjectiveUrl">
																	<c:param name="id" value="${entry.id}" />
																</c:url>
																<c:url value="startStudentTestForMix"
																	var="startStudentTestForMixUrl">
																	<c:param name="id" value="${entry.id}" />
																</c:url>
																<c:url value="testList" var="testList">
																	<c:param name="courseId" value="${entry.courseId}" />
																</c:url>
																<c:choose>
																	<c:when test="${entry.type eq 'Assigment'}">
																		<a href="${assignmentSubmitUrl}"
																			title="Assignment Details">Submit Assignment</a>&nbsp;
											</c:when>
																	<c:otherwise>
																		<c:choose>
																			<c:when test="${'Subjective' eq entry.testType}">
																				<c:if test="${'N' eq  entry.isPasswordForTest}">
																					<a href="${startStudentTestForSubjectiveUrl}"
																						title="Test Details"
																						onclick="return confirm('Are you ready to take Test?')">Start
																						Test</a>&nbsp;</c:if>
																				<c:if test="${'Y' eq  entry.isPasswordForTest}">
																					<a href="${testList}" title="Start Test">StartTest</a>&nbsp;
																				</c:if>
																			</c:when>
																			
																			<c:when test="${'Mix' eq entry.testType}">
																				<c:if test="${'N' eq  entry.isPasswordForTest}">
																					<a href="${startStudentTestForMixUrl}"
																						title="Test Details"
																						onclick="return confirm('Are you ready to take Test?')">Start
																						Test</a>&nbsp;</c:if>
																				<c:if test="${'Y' eq  entry.isPasswordForTest}">
																					<a href="${testList}" title="Start Test">StartTest</a>&nbsp;
																				</c:if>
																			</c:when>

																			<c:otherwise>
																				<c:if test="${'N' eq  entry.isPasswordForTest}">
																					<a href="${startStudentTestUrl}"
																						title="Test Details"
																						onclick="return confirm('Are you ready to take Test?')">Start
																						Test</a>&nbsp;</c:if>
																				<c:if test="${'Y' eq  entry.isPasswordForTest}">
																					<a href="${testList}" title="Start Test">StartTest</a>&nbsp;
																				</c:if>
																			</c:otherwise>
																		</c:choose>

																	</c:otherwise>
																</c:choose>



															</sec:authorize> <sec:authorize access="hasRole('ROLE_FACULTY')">
																<c:url value="viewAssignment" var="viewAssignmentUrl">
																	<c:param name="id" value="${entry.id}" />
																</c:url>
																<a href="${viewAssignmentUrl}"
																	title="Assignment Details">View Assignment</a>&nbsp;
                                                                        </sec:authorize>
														</td>
													</tr>
												</c:forEach>
											</tbody>

										</table>
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
