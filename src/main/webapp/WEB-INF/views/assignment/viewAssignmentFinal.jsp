
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@page import="java.util.*"%>
<%@page import="com.spts.lms.web.utils.Utils"%>
<%
	Date now = Utils.getInIST();
%>

<fmt:formatDate var="dateTimeValue" type="both" value="<%=now%>"
	pattern="yyyy-MM-dd HH:mm:ss" />

<jsp:include page="../common/newDashboardHeader.jsp" />

<div class="d-flex dataTableBottom paddingFixAssign "
	id="assignmentPage">
	<jsp:include page="../common/newLeftNavbar.jsp" />
	<jsp:include page="../common/newLeftSidebar.jsp" />
	<jsp:include page="../common/rightSidebar.jsp" />

	<div class="container-fluid m-0 p-0 dashboardWraper assignmentW">
		<jsp:include page="../common/newTopHeader.jsp" />
		<jsp:include page="../common/newLoaderWrap.jsp" />
		<div class="container mt-5">
			<div class="row">
				<div
					class="col-lg-9 col-md-9 col-sm-12 dashboardBody ml-auto mr-auto">

					<jsp:include page="../common/alert.jsp" />

					<div class="bg-white pb-5 mb-5">
						<nav aria-label="breadcrumb">
							<ol class="breadcrumb">
								<li class="breadcrumb-item"><a
									href="${pageContext.request.contextPath}/homepage">Dashboard</a></li>
								<li class="breadcrumb-item active" aria-current="page">Assignment</li>
							</ol>
						</nav>
						<!-- FILTER STARTS -->
						<div class="col-12">
							<form>
								<div class="form-row">
									<div class="col-lg-4 col-md-4 col-sm-12 mt-3">
										<label class="sr-only">Select Semester</label> <select
											class="form-control" id="assignSem">
											<c:if test="${empty course.acadSession}">
												<option value="" disabled selected>--SELECT
													SEMESTER--</option>
											</c:if>
											<c:forEach items="${ sessionWiseCourseList }" var="sList"
												varStatus="count">
												<c:if test="${sList.key eq course.acadSession}">
													<option value="${sList.key}" selected>${sList.key}</option>
												</c:if>
												<c:if test="${sList.key ne course.acadSession }">
													<option value="${sList.key}">${sList.key}</option>
												</c:if>
											</c:forEach>
										</select>
									</div>
									<div class="col-lg-4 col-md-4 col-sm-12 mt-3">
										<label class="sr-only">Select Course</label> <select
											class="form-control" id="assignCourse">
											<c:if test="${empty course.id}">
												<option value="" disabled selected>--SELECT
													COURSE--</option>
											</c:if>
											<c:forEach var='cList'
												items='${ sessionWiseCourseList[course.acadSession] }'>
												<c:if test="${cList.id eq course.id}">
													<option value="${cList.id}" selected>${cList.courseName}</option>
												</c:if>
												<c:if test="${cList.id ne course.id }">
													<option value="${cList.id}">${cList.courseName}</option>
												</c:if>
											</c:forEach>
										</select>
									</div>

								</div>
							</form>
						</div>
						<!-- FILTER ENDS -->
						<!-- GRAPH STARTS -->
						<section
							class="col-lg-12 col-md-12 col-sm-12 mb-5 fnt-13 chartWrap">
							<canvas id="assignmentBarChart"></canvas>
						</section>
						<!-- GRAPH ENDS -->
					</div>
					<div class="bg-white pb-5 mb-5">
						<section class="searchAssign">
							<div class="col-12 bg-dark text-white subHead1">
								<h6 class="p-2 mb-3">SEARCH ASSIGNMENTS</h6>
							</div>

							<div class="col-12">

								<!-- <div class="col-3 p-0 mr-auto mt-3 mb-3">
                                        <div class="input-group flex-nowrap input-group-sm">
                                            <div class="input-group-prepend">
                                                <span class="input-group-text cust-select-span" id="addon-wrapping">Filter Assignments</span>
                                            </div>

                                            <select class="cust-select">
                                                <option>All</option>
                                                <option>Completed</option>
                                                <option>Pending</option>
                                                <option>Late submitted</option>
                                                <option>Rejected</option>
                                            </select>
                                        </div>

                                    </div> -->
								<div class="table-responsive mb-3 testAssignTable">
									<table class="table table-striped table-hover"
										id="viewAssignmentTable">
										<thead>
											<tr>
												<th scope="col">No.</th>
												<th scope="col">Assignment Name</th>
												<th scope="col">Start Date</th>
												<th scope="col">End Date</th>
												<th scope="col">Marks</th>
												<th scope="col">Type</th>
												<th scope="col">Status</th>
												<th scope="col">File</th>
												<c:if test="${appNameForTee eq 'SBM-NM-M' || appNameForTee eq 'PDSEFBM' || appNameForTee eq 'CIPS'}">
												<th scope="col">Generate HashKey</th>
												</c:if>
												<th scope="col">Action</th>
												<th scope="col">Edit</th>
												<th scope="col">Remark File</th>
												<th scope="col">Remarks</th>
												<th scope="col">Low Score Reason</th>
											</tr>
										</thead>
										<tbody>
											<c:forEach items="${ assignments }" var="assignment"
												varStatus="status">
												<fmt:parseDate
													value="${fn:replace( assignment.submissionDate , '.0', '')}"
													pattern="yyyy-MM-dd HH:mm:ss" var="sDate" />
												<fmt:parseDate
													value="${fn:replace( assignment.endDate , '.0', '')}"
													pattern="yyyy-MM-dd'T'HH:mm:ss" var="eDate" />
												<fmt:formatDate value="${sDate}"
													pattern="yyyy-MM-dd HH:mm:ss" var="submDate" />
												<fmt:formatDate value="${eDate}"
													pattern="yyyy-MM-dd HH:mm:ss" var="enDate" />

												<tr>
													<th scope="row"><c:out value="${ status.count }"></c:out></th>
													<td><c:out value="${assignment.assignmentName}" /></td>
													<td><c:out value="${assignment.startDate}" /></td>
													<td><c:out value="${assignment.endDate}" /></td>
													<td><c:out value="${assignment.maxScore}" /></td>
													<td><c:out value="${assignment.assignmentType}" /></td>
													<td><c:if
															test="${assignment.submissionStatus eq 'Y' }">

															<i class="fas fa-check-circle text-success"></i>
															<c:out value="Completed" />
														</c:if> <c:if test="${assignment.submissionStatus ne 'Y' }">
															<c:if
																test="${ (empty assignment.approvalStatus || assignment.approvalStatus ne 'Reject') && assignment.attempts eq 0 }">
																<i class="fas fa-hourglass-start text-orange"></i>
																<c:out value="Pending" />
															</c:if>
															<c:if
																test="${ assignment.approvalStatus eq 'Reject' && submDate gt enDate }">
																<i class="fas fa-ban text-danger"></i>
																<c:out value="Rejected" />
															</c:if>
															<c:if
																test="${ assignment.approvalStatus ne 'Reject' && submDate gt enDate }">
																<i class="fas fa-exclamation-circle text-danger"></i>
																<c:out value="Waiting Approval" />
															</c:if>
														</c:if></td>
														
														
														<td><c:if
															test="${ fn:replace(assignment.startDate, 'T', ' ') le dateTimeValue }">
															<c:if
																test="${showDisclaimerInAssignment eq 'Yes' && empty assignment.isAcceptDisclaimer}">
																<c:choose>
														
																<c:when test="${assignment.isQuesConfigFromPool eq 'Y'}">
																<input type="button" data-toggle="modal" data-target="#disclaimerModal${status.count}" data-assign-id="${assignment.id}" class="open-disclaimer" value="View"/>
																</c:when>
																<c:otherwise>
																<input type="button" data-toggle="modal" data-target="#disclaimerModal${status.count}" data-assign-id="${assignment.id}" class="open-disclaimer" value="Download"/>
																</c:otherwise>
																</c:choose>	
																
															</c:if>
															<c:if
																test="${showDisclaimerInAssignment eq 'Yes' && assignment.isAcceptDisclaimer eq 'Y'}">
																<c:choose>
																<c:when test="${assignment.isQuesConfigFromPool eq 'Y'}">
																<c:url value="viewQuestionsForStudent" var="viewurl">
																	<c:param name="assignmentId" value="${assignment.id}" />
																</c:url>
																<a href="${viewurl}" title="Details">View</a>
																</c:when>
																<c:otherwise>
																<c:url value="downloadFile" var="downloadurl">
																	<c:param name="id" value="${assignment.id}" />
																</c:url>
																<c:if test="${empty assignment.filePath}">No File</c:if>
																<c:if test="${not empty assignment.filePath}"><a href="${downloadurl}" title="Details">Download</a></c:if>
																</c:otherwise>
																</c:choose>	
																
																
															</c:if>
															<c:if test="${showDisclaimerInAssignment eq 'No'}">
																<c:url value="downloadFile" var="downloadurl">
																	<c:param name="id" value="${assignment.id}" />
																</c:url>
																<c:if test="${empty assignment.filePath}">No File</c:if>
																<c:if test="${not empty assignment.filePath}"><a href="${downloadurl}" title="Details">Download</a></c:if>
															</c:if>
														</c:if> <c:if
															test="${ fn:replace(assignment.startDate, 'T', ' ') gt dateTimeValue}">
															<input type="button" value="Download" disabled />
														</c:if></td>
														
														<c:if test="${appNameForTee eq 'SBM-NM-M' || appNameForTee eq 'PDSEFBM' || appNameForTee eq 'CIPS'}">
														<td>
														<c:if test="${assignment.isCheckSumReq eq 'Y'}">
															<c:choose>
														
														<c:when test="${assignment.evaluationStatus eq 'Y'}">
															<input type="button" value="Submitted & Evaluated" disabled />
														</c:when>
														<c:otherwise>
														
															<c:choose>
																<c:when test="${assignment.evaluationStatus eq 'Y'}">
																	<input type="button" value="Evaluated" disabled />
																</c:when>
																<c:otherwise>
																	<input data-toggle="modal"
																	data-target="#submitAssignmentForCheckSum${status.count}"
																	type="button" value="Submit" />
																</c:otherwise>
															</c:choose>
															
														</c:otherwise>
														</c:choose>
														</c:if> 
														<c:if test="${assignment.isCheckSumReq ne 'Y'}">
														 Not Available
														 </c:if> </td>
														 </c:if>
														
													<td><c:if
															test="${assignment.submissionStatus eq 'Y' }">
															<input type="button" value="Submitted" disabled />
														</c:if> <c:if test="${assignment.submissionStatus ne 'Y' }">
														<%-- <c:if test="${assignment.isCheckSumReq eq 'N'}"> --%>
															<c:if
																test="${ (empty assignment.approvalStatus || assignment.approvalStatus ne 'Reject') && assignment.attempts eq 0 }">
																<input data-toggle="modal"
																	data-target="#submitAssignment${status.count}"
																	type="button" value="Submit" />
															</c:if>
															<c:if
																test="${ assignment.approvalStatus eq 'Reject' && submDate gt enDate}">
																<input type="button" value="Late Submitted" disabled />
															</c:if>
															<c:if
																test="${ assignment.approvalStatus ne 'Reject' && submDate gt enDate }">
																<input type="button" value="Late Submitted" disabled />
															</c:if>
														<%-- </c:if> --%>
														<%-- <c:if test="${assignment.isCheckSumReq eq 'Y'}">
															<c:choose>
																<c:when test="${assignment.showGenHashKey eq 'N'}">
																	
																	<input data-toggle="modal"
																	data-target="#lateSubmitRemarks${status.count}"
																	type="button" value="Add HashKey(For Late Submission)" />
																	
																</c:when>
																<c:otherwise>
																	
																	<input data-toggle="modal"
																	data-target="#submitAssignment${status.count}"
																	type="button" value="Submit" />
																	
																</c:otherwise>
															</c:choose>
														</c:if> --%>
														</c:if></td>

													



													<td><c:if
															test="${assignment.submissionStatus eq 'Y' }">
															<input data-toggle="modal"
																data-target="#editAssignment${status.count}"
																type="button" value="Edit" />
														</c:if> <c:if test="${assignment.submissionStatus ne 'Y' }">
															<c:if
																test="${ (empty assignment.approvalStatus || assignment.approvalStatus ne 'Reject') && assignment.attempts eq 0 }">
																<input data-toggle="modal"
																	data-target="#editAssignment${status.count}"
																	type="button" value="Edit" disabled />
															</c:if>
															<c:if
																test="${ assignment.approvalStatus eq 'Reject' && submDate gt enDate }">
																<input data-toggle="modal"
																	data-target="#editAssignment${status.count}"
																	type="button" value="Edit" />
															</c:if>
															<c:if
																test="${ assignment.approvalStatus ne 'Reject' && submDate gt enDate }">
																<input data-toggle="modal"
																	data-target="#editAssignment${status.count}"
																	type="button" value="Edit" />
															</c:if>
														</c:if></td>
													
													<td><c:if test="${assignment.assignmentRemarkFile}">
															<c:url value="downloadStudentAssignmentRemarkFile"
																var="downloadRemarkFile">
																<c:param name="assignmentId" value="${assignment.id}" />
															</c:url>
															<a href="${downloadRemarkFile}" title="Details">Download</a>
														</c:if> <c:if test="${!assignment.assignmentRemarkFile}">
															<input type="button" value="Download" disabled />
														</c:if></td>
														
														<td><c:if test="${not empty assignment.showResultsToStudents && assignment.showResultsToStudents eq 'Y'}">
															<c:if test="${empty assignment.remarks}">No Remarks</c:if>
															<c:if test="${not empty assignment.remarks}">${assignment.remarks}</c:if>
														</c:if>
														<c:if test="${empty assignment.showResultsToStudents || assignment.showResultsToStudents eq 'N'}">
															Result not publish
														</c:if></td>
														<td><c:if test="${not empty assignment.showResultsToStudents && assignment.showResultsToStudents eq 'Y'}">
															<c:if test="${empty assignment.lowScoreReason}">No Reason</c:if>
															<c:if test="${not empty assignment.lowScoreReason}">${assignment.lowScoreReason}</c:if>
														</c:if>
														<c:if test="${empty assignment.showResultsToStudents || assignment.showResultsToStudents eq 'N'}">
															Result not publish
														</c:if></td>
												</tr>

											</c:forEach>
										</tbody>
									</table>
								</div>

								<!-- <div class="row">
                                        <div class="col-3">
                                            <div class="input-group flex-nowrap input-group-sm">
                                                <div class="input-group-prepend">
                                                    <span class="input-group-text cust-select-span" id="addon-wrapping">Show</span>
                                                </div>
                                                
                                            <select class="cust-select">
                                            <option>5</option>
                                            <option>10</option>
                                            <option>15</option>
                                            <option>20</option>
                                            <option>25</option>
                                            <option>30</option>
                                            <option>All</option>
                                            </select>
                                            </div>                    
 
                                    </div>
                                        
                                    <nav class="col-9" aria-label="Assignment page navigation">
                                        <ul class="pagination float-right">
                                            <li class="page-item">
                                                <a class="page-link" href="#" aria-label="Previous">
                                                    <span aria-hidden="true">&laquo;</span>
                                                </a>
                                            </li>
                                            <li class="page-item"><a class="page-link" href="#">1</a></li>
                                            <li class="page-item"><a class="page-link" href="#">2</a></li>
                                            <li class="page-item"><a class="page-link" href="#">3</a></li>
                                            <li class="page-item">
                                                <a class="page-link" href="#" aria-label="Next">
                                                    <span aria-hidden="true">&raquo;</span>
                                                </a>
                                            </li>
                                        </ul>
                                    </nav>
                                </div> -->
							</div>



						</section>
					</div>
				</div>
				<c:forEach items="${ assignments }" var="assignment"
					varStatus="status">

					<div id="modalAssignSub">
					
					<div class="modal fade fnt-13"
							id="lateSubmitRemarks${status.count}" tabindex="-1" role="dialog"
							aria-labelledby="submitAssignmentTitle" aria-hidden="true">
							<div
								class="modal-dialog modal-dialog-centered modal-dialog-scrollable"
								role="document">
								<div class="modal-content">
									<div class="modal-header">
										<h5 class="modal-title" id="submitAssignmentTitle">Late Assignment Submission Remarks</h5>
										<button type="button" class="close" data-dismiss="modal"
											aria-label="Close">
											<span aria-hidden="true">&times;</span>
										</button>
									</div>
									<form:form action="lateAssgnSubmissionRemarks" id="lateAssgnSubmissionRemarks"
											method="post" modelAttribute="assignment"
											enctype="multipart/form-data" onsubmit="return checkSubmitForm(this);">
									<div class="modal-body">
										
											<form:input path="id" type="hidden" value="${assignment.id}" />
											<form:input path="courseId" type="hidden"
												value="${assignment.courseId}" />
												
											<div class="form-group">
										    <form:label path="hashKey">Enter Hash Key</form:label>
										    <form:input type="text" class="form-control" path="hashKey"
										    placeholder="Hash Key" required="required"/>
										   
										  </div>
										  <div class="form-group">
										    <form:label path="lateSubmRemark">Remarks</form:label>
										    <form:textarea path="lateSubmRemark"  class="form-control"  placeholder="Add Remark"
										    required="required"/>
										  </div>
										  
										  <button id="subRemarks" class="btn btn-modalSub" name="subRemarks"
														formaction="lateAssgnSubmissionRemarks"
														>Submit</button>
										<button type="button" class="btn btn-modalClose"
											data-dismiss="modal">Close</button>
										
									</div>
									
									</form:form>
								</div>
							</div>
						</div>

						<c:if test="${assignment.isCheckSumReq eq 'Y'}">
							<div class="modal fade fnt-13"
								id="submitAssignmentForCheckSum${status.count}" tabindex="-1"
								role="dialog" aria-labelledby="submitAssignmentTitle"
								aria-hidden="true">
								<div
									class="modal-dialog modal-dialog-centered modal-dialog-scrollable"
									role="document">
									<div class="modal-content">
										<div class="modal-header">
											<h5 class="modal-title" id="submitAssignmentTitle">Generate
												Hash Key By Uploading Your Assignment</h5>
											<button type="button" class="close" data-dismiss="modal"
												aria-label="Close">
												<span aria-hidden="true">&times;</span>
											</button>
										</div>
										<form:form action="submitAssignmentForCheckSum"
											id="submitAssignmentForCheckSum" method="post"
											modelAttribute="assignment" enctype="multipart/form-data"
											onsubmit="return checkSumForm(this);">
											<div class="modal-body">

												<form:input path="id" type="hidden" value="${assignment.id}" />
												<form:input path="courseId" type="hidden"
													value="${assignment.courseId}" />
												<div class="row">
													<div class="col-md-6 col-sm-12">
														<p>
															Course: <span>${assignment.courseName}</span>
														</p>
													</div>
													<div class="col-md-6 col-sm-12">
														<p>
															Assignment: <span>${assignment.assignmentName}</span>
														</p>
													</div>
												</div>
												<div class="row">
													<div class="col-md-6 col-sm-12">
														<p>
															Start Date: <span>${assignment.startDate}</span>
														</p>
													</div>
													<div class="col-md-6 col-sm-12">
														<p>
															End Date: <span>${assignment.endDate}</span>
														</p>
													</div>
												</div>
												<div class="row">
													<div class="col-md-6 col-sm-12">
														<p>
															Assignment Type: <span>${assignment.assignmentType}</span>
														</p>
													</div>
													<div class="col-md-6 col-sm-12">
														<p>
															Question Sheet:
															<c:if test="${not empty assignment.filePath}">
																<c:if test="${ fn:replace(assignment.startDate, 'T', ' ') le dateTimeValue }">
																	<%-- <a href="downloadFile?id=${assignment.id}"><i
																		class="fas fa-download mr-0"></i> Download</a> --%>
																<c:if test="${showDisclaimerInAssignment eq 'Yes' && empty assignment.isAcceptDisclaimer}">
																	<c:choose>
																		<c:when
																			test="${assignment.isQuesConfigFromPool eq 'Y'}">
																			<input type="button" data-toggle="modal"
																				data-target="#disclaimerModal${status.count}"
																				data-assign-id="${assignment.id}"
																				class="open-disclaimer btn-info" value="View" />
																		</c:when>
																		<c:otherwise>
																			<input type="button" data-toggle="modal"
																				data-target="#disclaimerModal${status.count}"
																				data-assign-id="${assignment.id}"
																				class="open-disclaimer btn-info" value="Download" />
																		</c:otherwise>
																	</c:choose>
																</c:if>
																<c:if test="${showDisclaimerInAssignment eq 'Yes' && assignment.isAcceptDisclaimer eq 'Y'}">
																
																	<c:choose>
																		<c:when
																			test="${assignment.isQuesConfigFromPool eq 'Y'}">
																			<c:url value="viewQuestionsForStudent" var="viewurl">
																				<c:param name="assignmentId"
																					value="${assignment.id}" />
																			</c:url>
																			<a href="${viewurl}" title="Details">View</a>
																		</c:when>
																		<c:otherwise>
																			<c:url value="downloadFile" var="downloadurl">
																				<c:param name="id" value="${assignment.id}" />
																			</c:url>
																			<c:if test="${empty assignment.filePath}">No File</c:if>
																			<c:if test="${not empty assignment.filePath}">
																				<a href="${downloadurl}" title="Details">Download</a>
																			</c:if>
																		</c:otherwise>
																	</c:choose>
																</c:if>
																<c:if test="${showDisclaimerInAssignment eq 'No'}">
																	<c:url value="downloadFile" var="downloadurl">
																		<c:param name="id" value="${assignment.id}" />
																	</c:url>
																	<c:if test="${empty assignment.filePath}">No File</c:if>
																	<c:if test="${not empty assignment.filePath}">
																		<a href="${downloadurl}" title="Details">Download</a>
																	</c:if>
																</c:if>
																</c:if>
																<c:if test="${ fn:replace(assignment.startDate, 'T', ' ') gt dateTimeValue}">
																	Allowed to download after start time
																</c:if>

															</c:if>
															<c:if test="${empty assignment.filePath}">
															<c:if test="${ fn:replace(assignment.startDate, 'T', ' ') le dateTimeValue }">
															<c:if test="${showDisclaimerInAssignment eq 'Yes' && empty assignment.isAcceptDisclaimer}">
																<c:if
																	test="${assignment.isQuesConfigFromPool eq 'Y'}">
																	<input type="button" data-toggle="modal"
																		data-target="#disclaimerModal${status.count}"
																		data-assign-id="${assignment.id}"
																		class="open-disclaimer btn-info" value="View" />
																</c:if>
															</c:if>
															<c:if test="${showDisclaimerInAssignment eq 'Yes' && assignment.isAcceptDisclaimer eq 'Y'}">
															<c:choose>
																<c:when
																	test="${assignment.isQuesConfigFromPool eq 'Y'}">
																	<c:url value="viewQuestionsForStudent" var="viewurl">
																		<c:param name="assignmentId"	
																			value="${assignment.id}" />
																	</c:url>
																	<a href="${viewurl}" title="Details">View</a>
																</c:when>
																<c:otherwise>
																	No File
																</c:otherwise>
															</c:choose>
															</c:if>
															<c:if test="${showDisclaimerInAssignment eq 'No'}">
																No File
															</c:if>
														</c:if>
														<c:if test="${ fn:replace(assignment.startDate, 'T', ' ') gt dateTimeValue}">
																	Allowed to download after start time
														</c:if>
														</c:if>
														</p>
													</div>
												</div>
												<div class="row">
													<div class="col-md-6 col-sm-12">
														<p>
															Total Marks: <span>${assignment.maxScore}</span>
														</p>
													</div>
													<div class="col-md-6 col-sm-12">
														<p>
															HashKey: <span>${assignment.studentHashKey}</span>
														</p>
													</div>
												</div>
												<div class="row">
													<div class="col-12">
														<a href="#moreAssign${status.count}"
															data-toggle="collapse">Read about the assignment</a>
														<div class="collapse" id="moreAssign${status.count}">
															<c:out value="${ assignment.assignmentText}"
																escapeXml="false" />
														</div>
													</div>
												</div>
												<hr />
												<div class="row">
													<div class="col-12">
														<h6 for="uploadAssignment">
															Generate Hash Key
															<c:if test="${assignment.evaluationStatus eq 'Y'}">
													(Assignment Evaluated!!)
													</c:if>
														</h6>
														<input type="file" name="file" class="form-control-file"
															id="uploadAssignment">
													</div>
												</div>
												<div class="row">
													<div class="col-12 mt-3" id="subDisplay"></div>
												</div>

											</div>
											<div class="modal-footer">
												<button type="button" class="btn btn-modalClose"
													data-dismiss="modal">Close</button>
												<!-- <button type="button" id="subAssign" class="btn btn-modalSub">Submit</button> -->
												<c:if test="${assignment.evaluationStatus ne 'Y'}">
													<button id="performChecksum" class="btn btn-modalSub"
														name="performChecksum"
														formaction="submitAssignmentForCheckSum">Submit</button>
												</c:if>
											</div>
										</form:form>
									</div>
								</div>
							</div>
						</c:if>


						<div class="modal fade fnt-13"
							id="submitAssignment${status.count}" tabindex="-1" role="dialog"
							aria-labelledby="submitAssignmentTitle" aria-hidden="true">
							<div
								class="modal-dialog modal-dialog-centered modal-dialog-scrollable"
								role="document">
								<div class="modal-content">


									<div class="modal-header">
										<h5 class="modal-title" id="submitAssignmentTitle">Submit
											Your Assignment</h5>
										<button type="button" class="close" data-dismiss="modal"
											aria-label="Close">
											<span aria-hidden="true">&times;</span>
										</button>
									</div>
									<form:form action="createAssignment" id="submitAssignment"
										method="post" modelAttribute="assignment"
										enctype="multipart/form-data"
										onsubmit="return checkSubmitForm(this);">
										<div class="modal-body">

											<form:input path="id" type="hidden" value="${assignment.id}" />
											<form:input path="courseId" type="hidden"
												value="${assignment.courseId}" />
											<form:input path="plagscanRequired" type="hidden"
												value="${assignment.plagscanRequired}" id="plagscanRequired" />
											<form:input path="runPlagiarism" type="hidden"
												value="${assignment.runPlagiarism}" id="runPlagiarism" />
											<div class="row">
												<div class="col-md-6 col-sm-12">
													<p>
														Course: <span>${assignment.courseName}</span>
													</p>
												</div>
												<div class="col-md-6 col-sm-12">
													<p>
														Assignment: <span>${assignment.assignmentName}</span>
													</p>
												</div>
											</div>
											<div class="row">
												<div class="col-md-6 col-sm-12">
													<p>
														Start Date: <span>${assignment.startDate}</span>
													</p>
												</div>
												<div class="col-md-6 col-sm-12">
													<p>
														End Date: <span>${assignment.endDate}</span>
													</p>
												</div>
											</div>
											<div class="row">
												<div class="col-md-6 col-sm-12">
													<p>
														Assignment Type: <span>${assignment.assignmentType}</span>
													</p>
												</div>
												<div class="col-md-6 col-sm-12">
													<p>
														Question Sheet:
														<c:if test="${not empty assignment.filePath}">
															<c:if test="${ fn:replace(assignment.startDate, 'T', ' ') le dateTimeValue }">
																<%-- <a href="downloadFile?id=${assignment.id}"><i
																	class="fas fa-download mr-0"></i> Download</a> --%>
																<c:if test="${showDisclaimerInAssignment eq 'Yes' && empty assignment.isAcceptDisclaimer}">
																	<c:choose>
																		<c:when
																			test="${assignment.isQuesConfigFromPool eq 'Y'}">
																			<input type="button" data-toggle="modal"
																				data-target="#disclaimerModal${status.count}"
																				data-assign-id="${assignment.id}"
																				class="open-disclaimer btn-info" value="View" />
																		</c:when>
																		<c:otherwise>
																			<input type="button" data-toggle="modal"
																				data-target="#disclaimerModal${status.count}"
																				data-assign-id="${assignment.id}"
																				class="open-disclaimer btn-info" value="Download" />
																		</c:otherwise>
																	</c:choose>
																</c:if>
																<c:if test="${showDisclaimerInAssignment eq 'Yes' && assignment.isAcceptDisclaimer eq 'Y'}">
																	<c:choose>
																		<c:when
																			test="${assignment.isQuesConfigFromPool eq 'Y'}">
																			<c:url value="viewQuestionsForStudent" var="viewurl">
																				<c:param name="assignmentId"
																					value="${assignment.id}" />
																			</c:url>
																			<a href="${viewurl}" title="Details">View</a>
																		</c:when>
																		<c:otherwise>
																			<c:url value="downloadFile" var="downloadurl">
																				<c:param name="id" value="${assignment.id}" />
																			</c:url>
																			<c:if test="${empty assignment.filePath}">No File</c:if>
																			<c:if test="${not empty assignment.filePath}">
																				<a href="${downloadurl}" title="Details">Download</a>
																			</c:if>
																		</c:otherwise>
																	</c:choose>
																</c:if>
																<c:if test="${showDisclaimerInAssignment eq 'No'}">
																	<c:url value="downloadFile" var="downloadurl">
																		<c:param name="id" value="${assignment.id}" />
																	</c:url>
																	<c:if test="${empty assignment.filePath}">No File</c:if>
																	<c:if test="${not empty assignment.filePath}">
																		<a href="${downloadurl}" title="Details">Download</a>
																	</c:if>
																</c:if>
															</c:if>
															<c:if
																test="${ fn:replace(assignment.startDate, 'T', ' ') gt dateTimeValue}">
																	Allowed to download after start time
															</c:if>

														</c:if>
														<c:if test="${empty assignment.filePath}">
															<c:if test="${ fn:replace(assignment.startDate, 'T', ' ') le dateTimeValue }">
															<c:if test="${showDisclaimerInAssignment eq 'Yes' && empty assignment.isAcceptDisclaimer}">
																<c:if
																	test="${assignment.isQuesConfigFromPool eq 'Y'}">
																	<input type="button" data-toggle="modal"
																		data-target="#disclaimerModal${status.count}"
																		data-assign-id="${assignment.id}"
																		class="open-disclaimer btn-info" value="View" />
																</c:if>
															</c:if>
															<c:if test="${showDisclaimerInAssignment eq 'Yes' && assignment.isAcceptDisclaimer eq 'Y'}">
															<c:choose>
																<c:when
																	test="${assignment.isQuesConfigFromPool eq 'Y'}">
																	<c:url value="viewQuestionsForStudent" var="viewurl">
																		<c:param name="assignmentId"	
																			value="${assignment.id}" />
																	</c:url>
																	<a href="${viewurl}" title="Details">View</a>
																</c:when>
																<c:otherwise>
																	No File
																</c:otherwise>
															</c:choose>
															</c:if>
															<c:if test="${showDisclaimerInAssignment eq 'No'}">
																No File
															</c:if>
														</c:if>
														<c:if test="${ fn:replace(assignment.startDate, 'T', ' ') gt dateTimeValue}">
																	Allowed to download after start time
														</c:if>
														</c:if>
													</p>
												</div>
											</div>
											<div class="row">
												<div class="col-md-6 col-sm-12">
													<p>
														Total Marks: <span>${assignment.maxScore}</span>
													</p>
												</div>
												<div class="col-md-6 col-sm-12">
													<p>
														Status: <span>Not Submitted</span>
													</p>
												</div>
											</div>
											<div class="row">
												<div class="col-12">
													<a href="#moreAssign${status.count}" data-toggle="collapse">Read
														about the assignment</a>
													<div class="collapse" id="moreAssign${status.count}">
														<c:out value="${ assignment.assignmentText}"
															escapeXml="false" />
													</div>
												</div>
											</div>
											<hr />
												<c:if test="${assignment.showGenHashKey eq 'N' }">
											<c:if test="${not empty assignment.isCheckSumReq && assignment.isCheckSumReq eq 'Y'}">
											<input type="hidden" value="Y" name="isHashKeyLateSubmitted">
											<div class="row">
											<div class="col-6">
													<h6 for="uploadAssignment">Upload assignment file</h6>
													<input type="file" name="file" class="form-control-file"
														id="uploadAssignment">
												</div>
											<div class="col-6">
													<h6 for="uploadAssignment">Enter Hash Key</h6>
													<input type="text" name="hashKey" class="form-control"
														id="hashKey">
												</div>
											</div>
											
										
											<div class="row">
												<div class="col-12">
													<h6 for="lateSubmRemark">Enter Reason Of Late Sumission</h6>
													<input type="textarea" name="lateSubmRemark" class="form-control"
														id="lateSubmRemark"/>
												</div>
											</div>
											
											</c:if>
											<c:if test="${empty assignment.isCheckSumReq || assignment.isCheckSumReq eq 'N'}">
											<input type="hidden" value="N" name="isHashKeyLateSubmitted">
											<div class="row">
												<div class="col-12">
													<h6 for="uploadAssignment">Upload assignment file</h6>
													<input type="file" name="file" class="form-control-file"
														id="uploadAssignment">
												</div>
											</div>
											</c:if>
											</c:if>
											
											<c:if test="${assignment.showGenHashKey eq 'Y'}">
											<input type="hidden" value="N" name="isHashKeyLateSubmitted">
											<div class="row">
												<div class="col-12">
													<h6 for="uploadAssignment">Upload assignment file</h6>
													<input type="file" name="file" class="form-control-file"
														id="uploadAssignment">
												</div>
											</div>
											</c:if>
											<!-- <div class="row">
												<div class="col-12">
													<h6 for="uploadAssignment">Upload assignment file</h6>
													<input type="file" name="file" class="form-control-file"
														id="uploadAssignment">
												</div>
											</div> -->
											<div class="row">
												<div class="col-12 mt-3" id="subDisplay"></div>
											</div>

										</div>
										<div class="modal-footer">
											<button type="button" class="btn btn-modalClose"
												data-dismiss="modal">Close</button>
											<!-- <button type="button" id="subAssign" class="btn btn-modalSub">Submit</button> -->
											<c:choose>
												<c:when test="${assignment.submitByOneInGroup eq 'Y'}">
													<c:choose>
														<c:when test="${assignment.isSubmitterInGroup eq 'Y'}">
															<c:if
																test="${assignment.plagscanRequired eq 'Yes' and assignment.runPlagiarism eq 'Submission' }">
																<c:if test="${assignment.evaluationStatus eq 'N' }">
																	<button id="subAssign" class="btn btn-modalSub"
																		name="subAssign"
																		formaction="submitAssignmentByOneInGroup?id=${assignment.id}"
																		onclick="submitAssignPlag()">Submit</button>
																</c:if>
															</c:if>
															<c:if test="${assignment.plagscanRequired eq 'No' }">
																<c:if test="${assignment.evaluationStatus eq 'N' }">
																	<button id="subAssign" class="btn btn-modalSub"
																		name="subAssign"
																		formaction="submitAssignmentByOneInGroup?id=${assignment.id}"
																		onclick="submitAssign()">Submit</button>
																</c:if>
															</c:if>
															<c:if test="${assignment.evaluationStatus eq 'Y' }">
																<button id="subAssign" class="btn btn-modalSub"
																	formaction="#" disabled="disabled">Cannot
																	Submit As already Evaluated</button>
															</c:if>
														</c:when>
														<c:otherwise>
															<button id="subAssign" class="btn btn-modalSub"
																formaction="#" disabled="disabled">Cannot
																Submit</button>
														</c:otherwise>
													</c:choose>
												</c:when>
												<c:otherwise>
													<c:if
														test="${assignment.plagscanRequired eq 'Yes' and assignment.runPlagiarism eq 'Submission' }">
														<c:if test="${assignment.evaluationStatus eq 'N' }">
															<button id="subAssign" class="btn btn-modalSub"
																name="subAssign" formaction="submitAssignment"
																onclick="submitAssignPlag()">Submit</button>
														</c:if>
													</c:if>
													<c:if test="${assignment.plagscanRequired eq 'No' }">
														<c:if test="${assignment.evaluationStatus eq 'N' }">
															<button id="subAssign" class="btn btn-modalSub"
																name="subAssign" formaction="submitAssignment"
																onclick="submitAssign()">Submit</button>
														</c:if>
													</c:if>
													<c:if test="${assignment.evaluationStatus eq 'Y' }">
														<button id="subAssign" class="btn btn-modalSub"
															formaction="#" disabled="disabled">Cannot Submit
															As already Evaluated</button>
													</c:if>
												</c:otherwise>
											</c:choose>
										</div>
									</form:form>
								</div>
							</div>
						</div>
					</div>

					<form:form action="createAssignment" id="submitAssignment"
						method="post" modelAttribute="assignment"
						enctype="multipart/form-data"
						onsubmit="return checkReSubmitForm(this);">
						<div id="modalAssignEdit">
							<div class="modal fade fnt-13" id="editAssignment${status.count}"
								tabindex="-1" role="dialog"
								aria-labelledby="submitAssignmentTitle" aria-hidden="true">
								<div
									class="modal-dialog modal-dialog-centered modal-dialog-scrollable"
									role="document">
									<div class="modal-content">
										<div class="modal-header">
											<h5 class="modal-title" id="submitAssignmentTitle">Edit
												Your Assignment</h5>
											<button type="button" class="close" data-dismiss="modal"
												aria-label="Close">
												<span aria-hidden="true">&times;</span>
											</button>
										</div>


										<div class="modal-body">

											<form:input path="id" type="hidden" value="${assignment.id}" />
											<form:input path="courseId" type="hidden"
												value="${assignment.courseId}" />
											<form:input path="plagscanRequired" type="hidden"
												value="${assignment.plagscanRequired}" id="plagscanRequired" />
											<form:input path="runPlagiarism" type="hidden"
												value="${assignment.runPlagiarism}" id="runPlagiarism" />
											<div class="row">
												<div class="col-md-6 col-sm-12">
													<p>
														Course: <span>${assignment.courseName}</span>
													</p>
												</div>
												<div class="col-md-6 col-sm-12">
													<p>
														Assignment: <span>${assignment.assignmentName}</span>
													</p>
												</div>
											</div>
											<div class="row">
												<div class="col-md-6 col-sm-12">
													<p>
														Start Date: <span>${assignment.startDate}</span>
													</p>
												</div>
												<div class="col-md-6 col-sm-12">
													<p>
														End Date: <span>${assignment.endDate}</span>
													</p>
												</div>
											</div>
											<div class="row">
												<div class="col-md-6 col-sm-12">
													<p>
														Assignment Type: <span>${assignment.assignmentType}</span>
													</p>
												</div>
												<div class="col-md-6 col-sm-12">
													<p>
														Question Sheet:
														<c:if test="${not empty assignment.filePath}">
															<c:if
																test="${ fn:replace(assignment.startDate, 'T', ' ') le dateTimeValue }">
																<%-- <a href="downloadFile?id=${assignment.id}"><i
																	class="fas fa-download mr-0"></i> Download</a> --%>
																<c:if test="${showDisclaimerInAssignment eq 'Yes' && empty assignment.isAcceptDisclaimer}">
																	<c:choose>
																		<c:when
																			test="${assignment.isQuesConfigFromPool eq 'Y'}">
																			<input type="button" data-toggle="modal"
																				data-target="#disclaimerModal${status.count}"
																				data-assign-id="${assignment.id}"
																				class="open-disclaimer btn-info" value="View" />
																		</c:when>
																		<c:otherwise>
																			<input type="button" data-toggle="modal"
																				data-target="#disclaimerModal${status.count}"
																				data-assign-id="${assignment.id}"
																				class="open-disclaimer btn-info" value="Download" />
																		</c:otherwise>
																	</c:choose>
																</c:if>
																<c:if test="${showDisclaimerInAssignment eq 'Yes' && assignment.isAcceptDisclaimer eq 'Y'}">
																	<c:choose>
																		<c:when
																			test="${assignment.isQuesConfigFromPool eq 'Y'}">
																			<c:url value="viewQuestionsForStudent" var="viewurl">
																				<c:param name="assignmentId"
																					value="${assignment.id}" />
																			</c:url>
																			<a href="${viewurl}" title="Details">View</a>
																		</c:when>
																		<c:otherwise>
																			<c:url value="downloadFile" var="downloadurl">
																				<c:param name="id" value="${assignment.id}" />
																			</c:url>
																			<c:if test="${empty assignment.filePath}">No File</c:if>
																			<c:if test="${not empty assignment.filePath}">
																				<a href="${downloadurl}" title="Details">Download</a>
																			</c:if>
																		</c:otherwise>
																	</c:choose>
																</c:if>
																<c:if test="${showDisclaimerInAssignment eq 'No'}">
																	<c:url value="downloadFile" var="downloadurl">
																		<c:param name="id" value="${assignment.id}" />
																	</c:url>
																	<c:if test="${empty assignment.filePath}">No File</c:if>
																	<c:if test="${not empty assignment.filePath}">
																		<a href="${downloadurl}" title="Details">Download</a>
																	</c:if>
																</c:if>
															</c:if>
															<c:if
																test="${ fn:replace(assignment.startDate, 'T', ' ') gt dateTimeValue}">
																	Allowed to download after start time
															</c:if>
														</c:if>
														<c:if test="${empty assignment.filePath}">
															<c:if test="${ fn:replace(assignment.startDate, 'T', ' ') le dateTimeValue }">
															<c:if test="${showDisclaimerInAssignment eq 'Yes' && empty assignment.isAcceptDisclaimer}">
																<c:if
																	test="${assignment.isQuesConfigFromPool eq 'Y'}">
																	<input type="button" data-toggle="modal"
																		data-target="#disclaimerModal${status.count}"
																		data-assign-id="${assignment.id}"
																		class="open-disclaimer btn-info" value="View" />
																</c:if>
															</c:if>
															<c:if test="${showDisclaimerInAssignment eq 'Yes' && assignment.isAcceptDisclaimer eq 'Y'}">
															<c:choose>
																<c:when
																	test="${assignment.isQuesConfigFromPool eq 'Y'}">
																	<c:url value="viewQuestionsForStudent" var="viewurl">
																		<c:param name="assignmentId"	
																			value="${assignment.id}" />
																	</c:url>
																	<a href="${viewurl}" title="Details">View</a>
																</c:when>
																<c:otherwise>
																	No File
																</c:otherwise>
															</c:choose>
															</c:if>
															<c:if test="${showDisclaimerInAssignment eq 'No'}">
																No File
															</c:if>
														</c:if>
														<c:if test="${ fn:replace(assignment.startDate, 'T', ' ') gt dateTimeValue}">
																	Allowed to download after start time
														</c:if>
														</c:if>
													</p>
												</div>
											</div>
											<div class="row">
												<div class="col-md-6 col-sm-12">
													<p>
														Total Marks: <span>${assignment.maxScore} </span>
													</p>
												</div>
												<div class="col-md-6 col-sm-12">
													<p>
														Status: <span>Submitted</span>
													</p>
												</div>
											</div>
											<div class="row">
												<div class="col-md-6 col-sm-12">
													<p>
														Download Uploaded Answer File: <a
															href="downloadFile?saId=${assignment.studentAssignmentId }"><i
															class="fas fa-download mr-0"></i> Download Submitted File</a>
													</p>
												</div>
											</div>
											<div class="row">
												<div class="col-md-6 col-sm-12">
													<a href="#moreAssignEdit${status.count}"
														data-toggle="collapse">Read about the assignment</a>
													<div class="collapse" id="moreAssignEdit${status.count}">
														<c:out value="${ assignment.assignmentText}"
															escapeXml="false" />
													</div>
												</div>


											</div>
											<hr />
											<!-- <div class="row">
												<div class="col-12">
													<h6 for="uploadAssignment">Upload assignment file</h6>
													<input type="file" class="form-control-file" name="file"
														id="uploadAssignment">
												</div>
											</div> -->
											
											<c:if test="${assignment.showGenHashKey eq 'N' }">
											<c:if test="${not empty assignment.isCheckSumReq && assignment.isCheckSumReq eq 'Y'}">
											<input type="hidden" value="Y" name="isHashKeyLateSubmitted">
											<div class="row">
											<div class="col-6">
													<h6 for="uploadAssignment">Upload assignment file</h6>
													<input type="file" name="file" class="form-control-file"
														id="uploadAssignment">
												</div>
											<div class="col-6">
													<h6 for="uploadAssignment">Enter Hash Key</h6>
													<input type="text" name="hashKey" class="form-control"
														id="hashKey">
												</div>
											</div>
											
										
											<div class="row">
												<div class="col-12">
													<h6 for="lateSubmRemark">Enter Reason Of Late Sumission</h6>
													<input type="textarea" name="lateSubmRemark" class="form-control"
														id="lateSubmRemark"/>
												</div>
											</div>
											
											</c:if>
											<c:if test="${empty assignment.isCheckSumReq || assignment.isCheckSumReq eq 'N'}">
											<input type="hidden" value="N" name="isHashKeyLateSubmitted">
											<div class="row">
												<div class="col-12">
													<h6 for="uploadAssignment">Upload assignment file</h6>
													<input type="file" name="file" class="form-control-file"
														id="uploadAssignment">
												</div>
											</div>
											</c:if>
											</c:if>
											
											<c:if test="${assignment.showGenHashKey eq 'Y'}">
											<input type="hidden" value="N" name="isHashKeyLateSubmitted">
											<div class="row">
												<div class="col-12">
													<h6 for="uploadAssignment">Upload assignment file</h6>
													<input type="file" name="file" class="form-control-file"
														id="uploadAssignment">
												</div>
											</div>
											</c:if>
											<div class="row">
												<div class="col-12 mt-3" id="editDisplay"></div>
											</div>

										</div>
										<div class="modal-footer">
											<button type="button" class="btn btn-modalClose"
												data-dismiss="modal">Close</button>

											<c:choose>
												<c:when test="${assignment.submitByOneInGroup eq 'Y'}">
													<c:choose>
														<c:when test="${assignment.isSubmitterInGroup eq 'Y'}">
															<c:if
																test="${assignment.plagscanRequired eq 'Yes' and assignment.runPlagiarism eq 'Submission' }">
																<c:if test="${assignment.evaluationStatus eq 'N' }">
																	<button id="resubAssign" class="btn btn-modalSub"
																		name="resubAssign"
																		formaction="submitAssignmentByOneInGroup?id=${assignment.id}"
																		onclick="submitAssignPlag()">Resubmit</button>
																</c:if>
															</c:if>
															<c:if test="${assignment.plagscanRequired eq 'No'}">
																<c:if test="${assignment.evaluationStatus eq 'N' }">
																	<button id="resubAssign" class="btn btn-modalSub"
																		name="resubAssign"
																		formaction="submitAssignmentByOneInGroup?id=${assignment.id}"
																		onclick="submitAssign()">Resubmit</button>
																</c:if>
															</c:if>
															<c:if test="${assignment.evaluationStatus eq 'Y' }">
																<button id="resubAssign" class="btn btn-modalSub"
																	formaction="#" disabled="disabled">Cannot
																	Submit As already Evaluated</button>
															</c:if>
														</c:when>
														<c:otherwise>
															<button id="resubAssign" class="btn btn-modalSub"
																formaction="#" disabled="disabled">Cannot
																Submit</button>
														</c:otherwise>
													</c:choose>
												</c:when>
												<c:otherwise>
													<c:if
														test="${assignment.plagscanRequired eq 'Yes' and assignment.runPlagiarism eq 'Submission' }">
														<c:if test="${assignment.evaluationStatus eq 'N' }">
															<button id="resubAssign" class="btn btn-modalSub"
																name="resubAssign" formaction="submitAssignment"
																onclick="submitAssignPlag()">Resubmit</button>
														</c:if>
													</c:if>
													<c:if test="${assignment.plagscanRequired eq 'No'}">
														<c:if test="${assignment.evaluationStatus eq 'N' }">
															<button id="resubAssign" class="btn btn-modalSub"
																name="resubAssign" formaction="submitAssignment"
																onclick="submitAssign()">Resubmit</button>
														</c:if>
													</c:if>
													<c:if test="${assignment.evaluationStatus eq 'Y' }">
														<button id="resubAssign" class="btn btn-modalSub"
															formaction="#" disabled="disabled">Cannot Submit
															As already Evaluated</button>
													</c:if>
												</c:otherwise>
											</c:choose>
										</div>

									</div>
								</div>
							</div>
						</div>
					</form:form>

<!-- Disclaimer Modal Start -->

				<div class="modal disclaimerModal" id="disclaimerModal${status.count}" tabindex="-1" role="dialog">
					<div
						class="modal-dialog modal-dialog-centered modal-dialog-scrollable modal-lg"
						role="document">
						<div class="modal-content">
							<!-- <div class="modal-header">
								<h5 class="modal-title">Modal title</h5>
								<button type="button" class="close" data-dismiss="modal"
									aria-label="Close">
									<span aria-hidden="true">&times;</span>
								</button>
							</div> -->
							<div class="modal-body">
								<h3 class="text-center">Disclaimer</h3>
								<h6 class="text-center">Online End Term Examination</h6>
								<h6 class="text-center mb-3">General Academic Integrity
									Information</h6>
								<hr style="width: 250px" />
								<p>It is expected that a student will complete the online
									examination with academic integrity. NMIMS SBM considers the
									following as academic offences and a student found committing
									any of them will be penalized as per the policy of the school.</p>
								<ul>
									<li>The student copies from another student or from online
										resources during the online examination</li>
									<li>The student allows another student to copy from
										his/her work</li>
									<li>The student possesses or uses materials, other
										resources and technology that are not permitted to be used
										during the examination.</li>
									<li>The student undertakes activities, either personally
										or electronically, that are not permitted in the process of
										writing and completing the examination.</li>
									<li>The student collaborates and communicates with other
										students in the process of writing and completing an
										examination which is an individual test.</li>
									<li>The student assists or facilitates an academic
										offence.</li>
									<li>The student allows another person to take the
										examination in the student's place</li>
								</ul>
								<p class="font-weight-bold">
									<input type="checkbox" class="agree-disclaimer"> I
									acknowledge that I have read and understood the above-mentioned
									academic integrity information and I agree to complete the
									online examination in accordance with the same.
								</p>
							</div>
							<div class="modal-footer">
								<!-- <button class="btn btn-md btn-success btn-agree" disabled>Continue</button> -->
									<c:choose>
										<c:when test="${assignment.isQuesConfigFromPool eq 'Y'}">
											
												<button id="acceptDisclaimer${assignment.id}" data-assignId="${assignment.id}" class="btn btn-md btn-success btn-agree-view d-none">I Agree</button>
											
										</c:when>
										<c:otherwise>
											<c:url value="downloadFile" var="downloadurl">
												<c:param name="id" value="${assignment.id}" />
											</c:url>
											<c:if test="${not empty assignment.filePath}">
												<a href="${downloadurl}" data-assignId="${assignment.id}" class="btn btn-md btn-success btn-agree d-none" title="Details">Download</a>
											</c:if>
											<c:if test="${empty assignment.filePath}">
												<a href="${downloadurl}" data-assignId="${assignment.id}" class="btn btn-md btn-success btn-agree d-none" title="Details">No file</a>
											</c:if>
										</c:otherwise>
									</c:choose>
									
								<button type="button" class="btn btn-modalClose"
									data-dismiss="modal">Close</button>
							</div>
						</div>
					</div>
				</div>
				<!-- Disclaimer Modal End -->

				</c:forEach>

				







				<!-- SIDEBAR START -->
				<jsp:include page="../common/newSidebar.jsp" />
				<!-- SIDEBAR END -->
				<jsp:include page="../common/footer.jsp" />

				<!-- <script type="text/javascript"
					src="//cdn.ckeditor.com/4.8.0/standard-all/ckeditor.js"></script>
				<script type="text/javascript"
					src="https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.0/MathJax.js?config=TeX-AMS_HTML"></script>

				<script type="text/javascript">
					CKEDITOR
							.replace(
									'editor1',
									{
										extraPlugins : 'mathjax',
										mathJaxLib : 'https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.0/MathJax.js?config=TeX-AMS_HTML'
									});
				</script> -->
				<script>
					function submitAssign() {
						var confirmation = confirm('Are you sure you want to Submit Your Assignment');
						console.log(confirmation);
						if (confirmation == true) {
							$(".newLoaderWrap .loader-text")
									.html(
											"Processing...<br>Do not close the browser or refresh the page.")
							$(".newLoaderWrap").removeClass("d-none");
							console.log('Submitted');
						} else {
							event.preventDefault();
						}
					}
					function submitAssignPlag() {
						var confirmation = confirm('Are you sure you want to Submit Your Assignment');
						console.log(confirmation);
						if (confirmation == true) {
							$(".newLoaderWrap .loader-text")
									.html(
											"Please wait, plagiarism is checking...<br> Do not close the browser or refresh the page.")
							$(".newLoaderWrap").removeClass("d-none");
							console.log('Submitted');
						} else {
							event.preventDefault();
						}
					}
					function checkSubmitForm(form) // Submit button clicked
					{
						console.log("submit")
						form.subAssign.disabled = true;
						$("#submitAssignment #subAssign").html("Processing...")
						return true;
					}
					function checkSumForm(form) // Submit button clicked
					{
						var confirmation = confirm('Are you sure you want to Generate Hash Key?');
						console.log(confirmation);
						if (confirmation == true) {
							form.performChecksum.disabled = true;
							$("#submitAssignmentForCheckSum #performChecksum")
									.html("Processing...")
							$(".newLoaderWrap .loader-text")
									.html(
											"Processing...<br>Do not close the browser or refresh the page.")
							$(".newLoaderWrap").removeClass("d-none");
							console.log('Submitted');
						} else {
							event.preventDefault();
						}
						return true;
					}
					function checkReSubmitForm(form) // Submit button clicked
					{
						console.log("ReSubmit")
						form.resubAssign.disabled = true;
						$("#submitAssignment #resubAssign").html(
								"Processing...")
						return true;
					}
					
					
					/* $(".open-disclaimer").click(function(){
						let $this = $(this)
						let assignId = $this.attr("data-assign-id")
						$("#disclaimerModal .btn-agree").attr("data-assign-id", assignId)
						$("#disclaimerModal").modal("show")
					}) */

					$(".agree-disclaimer").click(
							function() {
								if ($(this).is(":checked")) {
									$(this).parent().parent().parent().find(
											".btn-agree").removeClass("d-none")
									$(this).parent().parent().parent().find(
											".btn-agree-view").removeClass("d-none")
								} else {
									$(this).parent().parent().parent().find(
											".btn-agree").addClass("d-none")
									$(this).parent().parent().parent().find(
											".btn-agree-view").addClass("d-none")
								}

							})
							
					let isDownloaded = false;
					
					$(".btn-agree").click(function() {
						isDownloaded = true
					}) 

					$('.disclaimerModal').on('hidden.bs.modal', function(e) {
						$(".agree-disclaimer").prop("checked", false)
						$(this).find(".btn-agree").addClass("d-none")
						
						console.log("Is Downloaded", isDownloaded)
						
						if(isDownloaded) {
							location.reload()
						}
						isDownloaded = false;
						
					})
					
					$(".btn-agree-view").click(function() {
						let assignId = $(this).attr('data-assignId');
						let assignTP = $(this).parent().parent().parent().find(
						".btn-agree-view").attr("data-assignId");
						console.log("assignId--->", assignId);
						console.log("assignTP--->", assignTP);
						$
						.ajax({
							type : "POST",
							
							/* dataType : 'json', */
							url : "${pageContext.request.contextPath}/acceptDiscalimer",
							data : {
								assignmentId : assignTP
							},
							success : function(result) {
								console.log("resultString-->",result);
								if(result == "success"){
									window.location.href = '${pageContext.request.contextPath}/viewQuestionsForStudent?assignmentId='
										+ assignTP
								}
								

							},
							error : function(result) {
								
								console.log('error');
								console.log(result);
							}
						})
					}) 
				</script>