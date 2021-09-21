<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>


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
                                <li class="breadcrumb-item active" aria-current="page"> Evaluate Assignment with
							Advance Search</li>
                            </ol>
                        </nav>
						<jsp:include page="../common/alert.jsp" />

						<!-- Input Form Panel -->
						<div class="card bg-white border">
							<div class="card-body">
									<div class="text-center border-bottom pb-2 mb-5">
										<h5>Assignment for ${assignment.course.courseName }</h5>
									</div>
										<form:form action="createAssignmentForm" id="editAssignment"
											method="post" modelAttribute="assignment"
											enctype="multipart/form-data">

											<form:input path="courseId" type="hidden" />
											<form:input path="id" type="hidden" />
											<form:input path="acadMonth" type="hidden" />
											<form:input path="acadYear" type="hidden" />
											<div class="row">
												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<form:label path="acadMonth" for="acadMonth"><strong>Academic Month:</strong></form:label>
														${assignment.acadMonth }
													</div>
												</div>
												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<form:label path="acadYear" for="acadYear"><strong>Academic Year:</strong></form:label>
														${assignment.acadYear }
													</div>
												</div>
												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<form:label path="assignmentName" for="assignmentName"><strong>Assignment Name:</strong></form:label>
														${assignment.assignmentName }
													</div>
												</div>
											</div>

											<div class="row">
												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<form:label path="startDate" for="startDate"><strong>Start Date:</strong></form:label>
														${assignment.startDate}
													</div>
												</div>
											
												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<form:label path="endDate" for="endDate"><strong>End Date:</strong></form:label>
														${assignment.endDate}
													</div>
												</div>
											</div>

											<div class="row">
												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<form:label path="allowAfterEndDate"
															for="allowAfterEndDate"><strong>Allow Submission after End date?:</strong></form:label>
														${assignment.allowAfterEndDate}
													</div>
												</div>
												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<form:label path="sendEmailAlert" for="sendEmailAlert"><strong>Send Email Alert for New Assignment?:</strong></form:label>
														${assignment.sendEmailAlert}
													</div>
												</div>
												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<form:label path="sendSmsAlert" for="sendSmsAlert"><strong>Send SMS Alert for New Assignment?:</strong></form:label>
														${assignment.sendSmsAlert}
													</div>
												</div>
											</div>

											<div class="row">
												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<form:label path="showResultsToStudents"
															for="showResultsToStudents"><strong>Show Results to Students immediately?:</strong></form:label>
														${assignment.showResultsToStudents}
													</div>
												</div>
												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<label for="file"><strong>Assignment Question File:</strong></label> <a class="font-italic text-primary"
															href="downloadFile?id=${assignment.id}">Download</a>
													</div>
												</div>

												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<form:label path="maxScore" for="maxScore"><strong>Score Out of:</strong></form:label>
														${assignment.maxScore}
													</div>
												</div>
											</div>

											<div class="row">

												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<form:label path="facultyId" for="facultyId"><strong>Faculty:</strong></form:label>
														${assignment.facultyId}
													</div>
												</div>
												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<form:label path="facultyId" for="facultyId"><strong>Submit Assignment By One In a Group:</strong></form:label>
														${assignment.submitByOneInGroup}
													</div>
												</div>
											</div>

											<div class="row">
												<div class="col-sm-12 column">
													<form:label path="assignmentText" for="assignmentText">
														<p class="cursor-pointer" data-toggle="collapse" data-target="#assignmentText"
															aria-expanded="true" aria-controls="assignmentText"><strong>
															Assignment Details: (Expand/Collapse) </strong></p>
													</form:label>
													<div id="assignmentText" class="collapse"
														style="border-style: solid; border-width: 1px; padding: 10px; margin-top: 10px">${assignment.assignmentText}</div>
												</div>
											</div>
											<br>
											<div class="row">
												<div class="text-center col-12">
													<div class="form-group">
														<sec:authorize access="hasRole('ROLE_FACULTY')">

															<button id="submit" class="btn btn-large btn-success"
																formaction="createAssignmentFromGroup">Edit
																Assignment</button>
														</sec:authorize>
														<!-- <button id="selectall" class="btn btn-large btn-primary"
															formaction="saveGroupAssignmentAllocationSelectAll">Allocate
															To All Groups</button> -->
														<button id="cancel" class="btn btn-large btn-danger"
															formaction="homepage" formnovalidate="formnovalidate">Cancel</button>
													</div>
												</div>
											</div>
											<c:if test="${showNote}">
												<div class="col-sm-8 column">
													<div class="form-group">
														<ul>
															<li><h4>1. For the groups with checkbox,
																	assignment has already been allocated to group members.
																</h4></li>
															<li><h4>2. Allocation status can be seen in
																	'View Group Members'</h4></li>

														</ul>

													</div>
												</div>
											</c:if>


										</form:form>
							</div>
						</div>

						<!-- Results Panel -->
						<div class="card bg-white border">
							<div class="card-body">
									<div class="text-center">
										<h5>Select groups to Allocate Assignment | Group
											allocated : ${noOfStudentAllocated}</h5>
									</div>
									<div class="x_itemCount" style="display: none;">
										<div class="image_not_found">
											<i class="fa fa-newspaper-o"></i>
											<p>
												<label class=""></label>${fn:length(groups)} Groups
											</p>
										</div>
									</div>
										<form:form action="saveGroupAssignmentAllocation"
											id="saveGroupAssignmentAllocation" method="post"
											modelAttribute="assignment">



											<form:input path="id" type="hidden" />
											<form:input path="courseId" type="hidden" />

											<form:input path="facultyId" type="hidden" />
											
											<div class="testAssignTable">
											<div class="table-responsive">
												<c:choose>
													<c:when test="${'Y' eq assignment.submitByOneInGroup}">
														<table class="table table-hover">
															<thead>
																<tr>
																	<th>Sr. No.</th>
																	<th>Select (<a onclick="checkAll()">All</a> | <a
																		onclick="uncheckAll()">None</a>)
																	</th>

																	<th>Group Name<i class="fa fa-sort"
																		aria-hidden="true" style="cursor: pointer"></i></th>
																	<th>Select Submitter for Group<i
																		class="fa fa-sort" aria-hidden="true"
																		style="cursor: pointer"></i></th>
																	<th>Action</th>
																</tr>
															</thead>
															<tfoot>
																<tr>
																	<th></th>

																	<th></th>

																	<th></th>
																</tr>
															</tfoot>
															<tbody>

																<c:forEach var="group" items="${groups}"
																	varStatus="status">

																	<tr>
																		<td><c:out value="${status.count}" /></td>
																		<td><c:if test="${group.allocated eq 'N' }">
																				<form:checkbox path="grps" value="${group.groupId}" />
																			</c:if> <c:if test="${group.allocated eq 'Y' }">
                                                      Assignment Allocated
                                                </c:if></td>
																		<td><c:out value="${group.groupName}" /></td>

																		<c:if test="${group.allocated eq 'Y' }">
																			<td><a href="#" class="editable" id="remarks"
																				data-type="select" data-pk="${group.groupId}"
																				data-source="${mapper[group.groupId]}"
																				data-url="updateSubmitter" data-title="">${mapGroupToSubmitter[group.groupId]}</a>
																			</td>
																		</c:if>
																		<c:if test="${group.allocated eq 'N'}">
																			<td>NA</td>
																		</c:if>
																		<td><c:url value="viewGroupStudentsForAssignment"
																				var="detailsUrl">
																				<c:param name="id" value="${group.groupId}" />
																				<c:param name="assignmentId" value="${id}" />
																			</c:url> <a href="${detailsUrl}" title="Details">View
																				Group Members</a>&nbsp;</td>
																	</tr>
																</c:forEach>
															</tbody>
														</table>
													</c:when>
													<c:otherwise>
														<table class="table table-hover">
															<thead>
																<tr>
																	<th>Sr. No.</th>
																	<th>Select (<a onclick="checkAll()">All</a> | <a
																		onclick="uncheckAll()">None</a>)
																	</th>

																	<th>Group Name<i class="fa fa-sort"
																		aria-hidden="true" style="cursor: pointer"></i></th>
																	<th>Action</th>
																</tr>
															</thead>
															<tfoot>
																<tr>
																	<th></th>
																	<th></th>
																	<th></th>
																</tr>
															</tfoot>
															<tbody>

																<c:forEach var="group" items="${groups}"
																	varStatus="status">

																	<tr>
																		<td><c:out value="${status.count}" /></td>
																		<td><c:if test="${group.allocated eq 'N' }">
																				<form:checkbox path="grps" value="${group.groupId}" />
																			</c:if> <c:if test="${group.allocated eq 'Y' }">
                                                      Assignment Allocated
                                                </c:if></td>
																		<td><c:out value="${group.groupName}" /></td>
																		<td><c:url value="viewGroupStudentsForAssignment"
																				var="detailsUrl">
																				<c:param name="id" value="${group.groupId}" />
																				<c:param name="assignmentId" value="${id}" />
																			</c:url> <a href="${detailsUrl}" title="Details">View
																				Group Members</a>&nbsp;</td>
																	</tr>
																</c:forEach>
															</tbody>
														</table>
													</c:otherwise>
												</c:choose>
											</div>
											</div>
											<hr/>

											<div class="text-center">
												<div class="form-group">
													<sec:authorize access="hasRole('ROLE_FACULTY')">

														<button id="submit" class="btn btn-large btn-success"
															formaction="saveGroupAssignmentAllocation">Allocate
															Assignment</button>
													</sec:authorize>

													<button id="cancel" class="btn btn-large btn-danger"
														formaction="homepage" formnovalidate="formnovalidate">Cancel</button>
												</div>
											</div>


										</form:form>
							</div>
						</div>

			<!-- /page content: END -->
                   
                    </div>
			
			<!-- SIDEBAR START -->
	         <jsp:include page="../common/newSidebar.jsp" />
	        <!-- SIDEBAR END -->
	<jsp:include page="../common/footer.jsp"/>
<script>
$(function() {
	// toggle `popup` / `inline` mode
	$.fn.editable.defaults.mode = 'inline';
	console.log('editable js ');
	$('.editable').each(function() {
		$(this).editable({
			success : function(response, newValue) {
				obj = JSON.parse(response);
				if (obj.status == 'error') {
					return obj.msg; // msg will be shown in editable
					// form
				}
			}
		});
	});

});

</script>