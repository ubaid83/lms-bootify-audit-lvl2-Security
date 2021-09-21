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
                        <c:if test = "${not empty Program_Name}">
                        <li class="breadcrumb-item" aria-current="page">
                            <c:out value="${Program_Name}" />
                        </li>
                        </c:if>
                        
                        <sec:authorize access="hasRole('ROLE_STUDENT')">
                            <c:out value="${AcadSession}" />
                        </sec:authorize>
                        <li class="breadcrumb-item active" aria-current="page"> Faculty List For Feedback | ${feedback.feedbackName} | ${faculties.size()}</li>
                    </ol>
                </nav>
						<jsp:include page="../common/alert.jsp" />

						<!-- Input Form Panel -->
						<div class="card bg-white border">
							<div class="card-body">
																<h5 class="text-center border-bottom pb-2">
																	Faculties For Feedback<font size="2px"> |
																		${faculties.size()} Records Found &nbsp; </font>
																</h5>

																<div class="table-responsive testAssignTable">
																	<table class="table table-striped table-hover">
																		<thead>
																			<tr>

																				

																				<th>Course</th>

																				<th>Faculty Name</th>
																				
																				<th>Acad Session</th>
																				
																				<!-- <th>Action</th> -->

																			</tr>
																		</thead>

																		<tfoot>
																			<tr>
																				<th>Course</th>

																				<th>Faculty Name</th>
																				
																				<th>Acad Session</th>
																				
																				
																				
																				<!-- <th>Start Time</th>
															<th>End Time</th>

															<th>Absence Reason</th> -->

																			</tr>
																		</tfoot>
																		<tbody>

																			<c:forEach var="facultyList" items="${faculties}"
																				varStatus="status">
																				
																				<tr>
																				

																					<td><c:out value="${facultyList.courseName}" /></td>
																					<td><c:out value="${facultyList.facultyName}" /></td>
																					<td><c:out value="${facultyList.acadSession}" /></td>
																					
																					<%-- <td> <c:url value="deleteUserCourse" var="deleteUrl">
																							
																							<c:param name="courseId" value="${facultyList.courseId}" />
																							<c:param name="username" value="${facultyList.facultyId}" />
																							<c:param name="role" value="ROLE_FACULTY" />
																							<c:param name="redirectToFeedback" value="Y"/>
																							<c:param name="feedbackId" value="${feedback.id}"/>
																						</c:url> 
																						
																						
																						<sec:authorize
																							access="hasAnyRole('ROLE_ADMIN')">
																							
																							<a href="${deleteUrl}" title="Delete"
																								onclick="return confirm('Are you sure you want to delete this record?')"><i
																								class="fa fa-trash-o fa-lg"></i></a>
																							
																						</sec:authorize></td> --%>
																				</tr>
																			</c:forEach>


																		</tbody>
																	</table>
																</div>
						</div>



					</div>

			<!-- /page content: END -->
                     
                   
                    </div>
			
			<!-- SIDEBAR START -->

	        <!-- SIDEBAR END -->
	<jsp:include page="../common/newAdminFooter.jsp"/>