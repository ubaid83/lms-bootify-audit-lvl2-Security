<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
	
	
	<jsp:include page="../common/newDashboardHeader.jsp" /> 

<div class="d-flex adminPage dataTableBottom" id="adminPage">
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
                        <li class="breadcrumb-item active" aria-current="page"> Search Course</li>
                    </ol>
                </nav>

						<jsp:include page="../common/alert.jsp" />
						<!-- Input Form Panel -->
						<c:choose>
							<c:when test="${courseList.size() > 0}">
								<!-- Results Panel -->
								<div class="card border bg-white">
									<div class="card-body">
												<h5 class="text-center border-bottom pb-2">Course List | ${courseList.size()} Records Found</h5>

												<div class="table-responsive testAssignTable">
													<table class="table table-striped table-hover" id="example">
														<thead>
															<tr>
																<th>Sr. No.</th>
																<th>Course ID <i class="fa fa-sort"
																	aria-hidden="true" style="cursor: pointer"></i></th>
																<!-- 	<th>Course Abbreviation</th> -->
																<th>Course Full Name <i class="fa fa-sort"
																	aria-hidden="true" style="cursor: pointer"></i></th>

																<th>Faculty Count<i class="fa fa-sort"
																	aria-hidden="true" style="cursor: pointer"></i></th>

																<th>Student Count<i class="fa fa-sort"
																	aria-hidden="true" style="cursor: pointer"></i></th>
																<th>Acad Session <i class="fa fa-sort"
																	aria-hidden="true" style="cursor: pointer"></i></th>
																<th>Acad Month <i class="fa fa-sort"
																	aria-hidden="true" style="cursor: pointer"></i></th>
																<th>Acad Year <i class="fa fa-sort"
																	aria-hidden="true" style="cursor: pointer"></i></th>
																<th>Acad Year Code<i class="fa fa-sort"
																	aria-hidden="true" style="cursor: pointer"></i></th>
																<th>Actions</th>
															</tr>
														</thead>
														<tfoot>
															<tr>
																<th></th>
																<th>Course ID</th>
																<!-- <th>Course abbr</th> -->
																<th>Course Full Name</th>

																<th>Faculty Count</th>
																<th>Student Count</th>
																<th>Acad Session</th>
																<th>Acad Month</th>
																<th>Acad Year</th>
																<th>Acad Year Code</th>
																<th>Delete Event</th>

															</tr>
														</tfoot>
														<tbody>

															<c:forEach var="course" items="${courseList}"
																varStatus="status">

																<tr>



																	<td><c:out value="${status.count}" /></td>
																	<td><c:out value="${course.id}" /></td>
																	<%-- <td><c:out value="${course.abbr}" /></td> --%>
																	<td><c:out value="${course.courseName}" /></td>


																	<c:choose>
																		<c:when test="${empty facultyCountMap[course.id]}">
																			<td><c:out value="0" /></td>
																		</c:when>
																		<c:otherwise>
																			<td><c:out value="${facultyCountMap[course.id]}" /></td>
																		</c:otherwise>
																	</c:choose>


																	<c:choose>
																		<c:when test="${empty StudentCountMap[course.id]}">
																			<td><c:out value="0" /></td>
																		</c:when>
																		<c:otherwise>
																			<td><c:out value="${StudentCountMap[course.id]}" /></td>
																		</c:otherwise>
																	</c:choose>


																	<td><c:out value="${course.acadSession}" /></td>
																	<td><c:out value="${course.acadMonth}" /></td>
																	<td><c:out value="${course.acadYear}" /></td>
																	<td><c:out value="${course.acadYearCode}" /></td>
																	<td><c:url value="deleteCourseAdmin"
																			var="deleteurl">
																			<c:param name="courseId" value="${course.id}" />
																		</c:url> <a href="${deleteurl}" title="Delete"
																		onclick="return confirm('Are you sure you want to delete this record?')"><i
																			class="fas fa-trash"></i></a></td>
																
																</tr>
															</c:forEach>
														

														</tbody>
													</table>
												</div>
									</div>
								</div>
							</c:when>
						</c:choose>
			<!-- /page content: END -->

                     
                   
                    </div>
			
			<!-- SIDEBAR START -->

	        <!-- SIDEBAR END -->
	<jsp:include page="../common/newAdminFooter.jsp"/>
	

	


			