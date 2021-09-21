<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
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
                        <li class="breadcrumb-item active" aria-current="page"> Feedback List</li>
                    </ol>
                </nav>
                
						<jsp:include page="../common/alert.jsp" />
						<c:choose>
							<c:when test="${fn:length(pageList) gt 0}">

								<!-- Input Form Panel -->
								<div class="card border bg-white">
									<div class="card-body">
												<h5 class="text-center border-bottom pb-2">Feedbacks | ${fn:length(pageList)} Records Found</h5>
											<div class="x_itemCount" style="display: none;">
												<div class="image_not_found">
													<i class="fa fa-newspaper-o"></i>
													<p>
														<label class=""></label>${fn:length(pageList)} Feedbacks
													</p>
												</div>
											</div>

												<div class="table-responsive testAssignTable">
													<table class="table table-striped table-hover">
														<thead>
															<tr>
																<th>Sr. No.</th>
																<th>Name</th>
																<th>Created Date</th>
																<th>Start Date</th>
																<th>End Date</th>
																<th>Publish Feedback</th>
																<th>Actions</th>
															</tr>
														</thead>
														<tbody>
															<sec:authorize access="hasRole('ROLE_ADMIN')">
																<c:forEach var="feedback" items="${pageList}"
																	varStatus="status">
																	<tr>
																		<td><c:out value="${status.count}" /></td>
																		<td><c:out value="${feedback.feedbackName}" /></td>
																		<td><c:out value="${feedback.createdOn}" /></td>
																		<td><c:out value="${feedback.startDate}" /></td>

																		<td><c:out value="${feedback.endDate}" /></td>
																		<td><c:if test="${feedback.isPublished eq 'Y' }">
Published
</c:if> <c:if test="${feedback.isPublished ne 'Y' }">
																				<c:url value="publishFeedbackReport"
																					var="publishUrl">
																					<c:param name="id" value="${feedback.id}" />
																				</c:url>
																				<a href="${publishUrl}" title="Details"
																					onclick="return confirm('Are you sure you want to publish this feedback?');">Publish</a>
																			</c:if></td>


																		<td><c:url value="addFeedbackForm" var="editurl">
																				<c:param name="id" value="${feedback.id}" />
																			</c:url> <c:url value="deleteFeedback" var="deleteurl">
																				<c:param name="programId" value="${feedback.id}" />
																			</c:url> <c:url value="viewFeedbackDetails" var="detailsUrl">
																				<c:param name="feedbackId" value="${feedback.id}" />
																			</c:url> <c:url value="searchAllFacultiesForFeedback"
																				var="searchFaculty">
																				<c:param name="feedbackId" value="${feedback.id}" />
																			</c:url><a href="${detailsUrl}" title="Details"><i
																				class="fa fa-info-circle fa-lg"></i></a>&nbsp; <a
																			href="${editurl}" title="Edit"><i
																				class="fas fa-edit fa-lg"></i></a>&nbsp; <a
																			href="${deleteurl}" title="Delete"
																			onclick="return confirm('Are you sure you want to delete this record?')"><i
																				class="fas fa-trash-alt fa-lg"></i></a> <a
																			href="${searchFaculty}"
																			title="View Assigned Faculties"><i
																				class="fa fa-users fa-lg"></i></a></td>
																	</tr>
																</c:forEach>
															</sec:authorize>
															<sec:authorize access="hasRole('ROLE_STUDENT')">
																<c:forEach var="feedback" items="${pageList}"
																	varStatus="status">
																	<tr>
																		<td><c:out value="${status.count}" /></td>
																		<td><c:out value="${feedback.feedbackName}" /></td>

																		<td><c:url value="viewFeedbackDetails"
																				var="detailsUrl">
																				<c:param name="feedbackId" value="${feedback.id}" />
																			</c:url> <a href="${detailsUrl}" title="Details"><i
																				class="fa fa-info-circle fa-lg"></i></a>&nbsp;</td>
																	</tr>
																</c:forEach>
															</sec:authorize>

														</tbody>
													</table>
												</div>
									</div>
								</div>
							</c:when>
						</c:choose>
<%-- 						<jsp:include page="../common/paginate.jsp">
							<jsp:param name="baseUrl" value="searchFeedback" />
						</jsp:include> --%>
			<!-- /page content: END -->
                     
                   
                    </div>
			
			<!-- SIDEBAR START -->

	        <!-- SIDEBAR END -->
	<jsp:include page="../common/newAdminFooter.jsp"/>
	
	
	
	
	
	



		
