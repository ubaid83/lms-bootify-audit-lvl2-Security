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
                                <li class="breadcrumb-item active" aria-current="page"> Offline Test List</li>
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
												<c:when test="${offlineTestList.size() > 0}">
													<div class="row">
														<div class="col-xs-12 col-sm-12">

															<div class="x_title">
																<h2>
																	Offline Tests<font size="2px"> |
																		${offlineTestList.size()} Records Found &nbsp; </font>
																</h2>
																<ul class="nav navbar-right panel_toolbox">
																	<li><a class="collapse-link"><i
																			class="fa fa-chevron-up"></i></a></li>
																	<li><a class="close-link"><i
																			class="fa fa-close"></i></a></li>
																</ul>
																<div class="clearfix"></div>
															</div>
															<div class="x_content">
																<div class="table-responsive">
																	<table class="table table-hover"
																		style="font-size: 12px">
																		<thead>
																			<tr>

																				<th>Offline Test Name</th>

																				<th>Course</th>

																				<th>Acad Month</th>
																				<th>Acad Year</th>
																				<th>Acad Session</th>
																				<th>Action</th>

																			</tr>
																		</thead>

																		<tfoot>
																			<tr>
																				<th>Offline Test Name</th>

																				<th>Course</th>

																				<th>Acad Month</th>
																				<th>Acad Year</th>
																				<th>Acad Session</th>
																				<th></th>
																				<!-- <th>Start Time</th>
															<th>End Time</th>

															<th>Absence Reason</th> -->

																			</tr>
																		</tfoot>
																		<tbody>

																			<c:forEach var="testPool" items="${offlineTestList}"
																				varStatus="status">

																				<tr>
																					<td><c:out value="${testPool.testName}" /></td>

																					<td><c:out value="${testPool.courseName}" /></td>
																					<td><c:out value="${testPool.acadMonth}" /></td>
																					<td><c:out value="${testPool.acadYear}" /></td>
																					<td><c:out value="${testPool.acadSession}" /></td>
																					<td><c:url value="addOfflineTestForm"
																							var="editurl">
																							<c:param name="id" value="${testPool.id}" />
																						</c:url> <c:url value="deleteOfflineTest" var="deleteurl">
																							<c:param name="id" value="${testPool.id}" />

																						</c:url> <c:url value="uploadStudentOfflineTestScoreForm"
																							var="uploadStudentOfflineTestScoreForm">
																							<c:param name="id" value="${testPool.id}" />

																						</c:url> <sec:authorize
																							access="hasAnyRole('ROLE_FACULTY')">
																							<a href="${editurl}" title="Edit"><i
																								class="fa fa-pencil-square-o fa-lg"></i></a>
																							<a href="${deleteurl}" title="Delete"
																								onclick="return confirm('Are you sure you want to delete this record?')"><i
																								class="fa fa-trash-o fa-lg"></i></a>
																							<a href="${uploadStudentOfflineTestScoreForm}"
																								title="Upload Test Questions"><i
																								class="fa  fa-upload"></i></a>





																						</sec:authorize></td>
																				</tr>
																			</c:forEach>


																		</tbody>
																	</table>
																</div>
															</div>

														</div>
													</div>
												</c:when>
											</c:choose>



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
	         <jsp:include page="../common/newSidebar.jsp" />
	        <!-- SIDEBAR END -->
	<jsp:include page="../common/footer.jsp"/>

	













