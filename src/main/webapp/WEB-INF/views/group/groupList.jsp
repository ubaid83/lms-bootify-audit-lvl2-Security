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
                                <li class="breadcrumb-item active" aria-current="page"> My Groups </li>
                            </ol>
                        </nav>
                        
						<jsp:include page="../common/alert.jsp" />
						
						
						<!-- Input Form Panel -->
						<div class="card bg-white border">
						<div class="card-body">
							<div class="col-xs-12 col-sm-12">
										<div class="x_panel">
											<div class="x_title">
												<h5>Groups | (${rowCount} Records Found)</h5>
												
											</div>
											<div class="x_content">
												<c:if test="${rowCount == 0}">
				No Groups
				</c:if>
												<c:choose>
													<c:when test="${rowCount > 0}">
														<div class="table-responsive">
															<table class="table table-hover"
																>
																<thead>
																	<tr>
																		<th>Sr. No.</th>
																		<th>Session Month</th>
																		<th>Session Year</th>
																		<th>Course</th>
																		<th>Group Name</th>
																		<th>No. of students</th>

																	

																		<th>Created By</th>
																		<th>Action</th>
																	</tr>
																</thead>
																<tbody>

																	<c:forEach var="groups" items="${groups}"
																		varStatus="status">
																		<tr>
																			<td><c:out value="${status.count}" /></td>
																			<td><c:out value="${groups.acadMonth}" /></td>
																			<td><c:out value="${groups.acadYear}" /></td>
																			<td><c:out value="${groups.courseName}" /></td>
																			<td><c:out value="${groups.groupName}" /></td>
																			<td><c:out value="${groups.noOfStudents}" /></td>
																			<td><c:out value="${groups.createdBy}"></c:out></td>
																			<td>
																			<sec:authorize access="hasAnyRole('ROLE_STUDENT')">
																			<c:url value="viewGroupStudents" var="detailsUrl">
																				<c:param name="id" value="${groups.id}" />
																			</c:url>
																			<a href="${detailsUrl}" title="Details">View Group Members</a>
																		</sec:authorize>
																			
																			</td>
																		</tr>
																	</c:forEach>

																</tbody>
															</table>
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
