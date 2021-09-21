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
                                <li class="breadcrumb-item active" aria-current="page"> Grievance List</li>
                            </ol>
                        </nav>
                        
						<jsp:include page="../common/alert.jsp" />
						
						
					
						<!-- Results Panel -->

						
								<div class="card bg-white border">
								<div class="card-body">								
									<div class="col-xs-12 col-sm-12">
								<div class="x_panel">
									<div class="x_title">
										<h5>Grievance List</h5>
										
									</div>
									<div class="x_content">
										<form:form action="viewAllGrievances" id="grievanceList"
											method="post" modelAttribute="grievances">

											<div class="table-responsive">
												<table id="grievanceTable" class="table table-hover">
													<thead>
														<tr>
															<th>Sr. No.</th>
															<th>Username</th>
															<th>Student Name</th>
															<th>Grievance Type</th>
															<th>Respond</th>
															<th>Status</th>
															<th>Reason</th>

														</tr>
													</thead>

													<tbody>

														<c:forEach var="grievance" items="${listofAllGrievances}"
															varStatus="status">
															<tr>
																<td><c:out value="${status.count}" /></td>
																<td><c:out value="${grievance.username} " /></td>
																<td><c:out value="${grievance.studentName} " /></td>
																<td><c:out value="${grievance.grievanceCase} " /></td>
																<td><c:url value="giveResponseToGrievance"
																		var="respondToGrievance">
																		<c:param name="id" value="${grievance.id}" />
																	</c:url> <a href="${respondToGrievance}" title="Response"><i
																		class="fa fa-check-square-o fa-lg"></i></a></td>

																<td><c:out value="${grievance.grievanceStatus} " /></td>
																<td><c:out value="${grievance.grievanceReason} " /></td>
														</c:forEach>
													</tbody>

												</table>
											</div>

											<div class="col-sm-6 col-md-4 col-xs-12 column">
												<div class="form-group">

													<button id="cancel" class="btn btn-large btn-danger"
														formaction="homepage" formnovalidate="formnovalidate">Cancel</button>
												</div>
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
                   
                    </div>
			
			<!-- SIDEBAR START -->
	         <jsp:include page="../common/newSidebar.jsp" />
	        <!-- SIDEBAR END -->
	<jsp:include page="../common/footer.jsp"/>
