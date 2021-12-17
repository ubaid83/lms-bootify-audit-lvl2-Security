<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<jsp:include page="../common/newDashboardHeader.jsp" />

<div class="d-flex dataTableBottom" id="facultyAssignmentPage">
	<jsp:include page="../common/newLeftNavBarFaculty.jsp" />
	<jsp:include page="../common/newLeftSidebarFaculty.jsp" />
	<jsp:include page="../common/rightSidebarFaculty.jsp" />

	<!-- DASHBOARD BODY STARTS HERE -->

	<div class="container-fluid m-0 p-0 dashboardWraper">

		<jsp:include page="../common/newTopHeaderFaculty.jsp" />

		<div class="container mt-5">
			<div class="row">
				<!-- SEMESTER CONTENT -->
				<div
					class="col-lg-9 col-md-9 col-sm-12 dashboardBody ml-auto mr-auto">


					<!-- page content: START -->

					<nav aria-label="breadcrumb">
						<ol class="breadcrumb">
							<li class="breadcrumb-item"><a
								href="${pageContext.request.contextPath}/homepage">Dashboard</a></li>
							<li class="breadcrumb-item" aria-current="page"><c:out
									value="${Program_Name}" /></li>

							<li class="breadcrumb-item active" aria-current="page">My
								Assignments</li>
							<li class="breadcrumb-item" aria-current="page"><sec:authorize
									access="hasRole('ROLE_STUDENT')">
									<c:out value="${AcadSession}" />
								</sec:authorize></li>
						</ol>
					</nav>


					<%-- <jsp:include page="../common/alert.jsp" />
 --%>
					<!-- Input Form Panel -->
					<div class="card bg-white">
						<div class="card-body">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">

									<h5 class="text-center text-uppercase border-bottom pb-3">My
										Assignments</h5>

									<form:form action="searchFacultyAssignment" method="post"
										modelAttribute="assignment">
										<fieldset>


											<div class="form-row mt-3">

												<div class="col-md-3 col-md-3 col-sm-12 column">
													<form:select id="courseId" path="courseId"
														class="form-control">
														<form:option value="">Select Course</form:option>
														<c:forEach var="course" items="${allCourses}"
															varStatus="status">
															<form:option value="${course.id}">${course.courseName}</form:option>
														</c:forEach>
													</form:select>
												</div>
												<div class="col-md-3 col-md-3 col-sm-12 column">
													<button id="submit" name="submit"
														class="btn btn-large btn-primary w-100">Search</button>
												</div>
												<div class="col-md-3 col-md-3 col-sm-12 column">
													<button id="submit" type="reset" value="reset"
														class="btn btn-large btn-primary w-100">Reset</button>
												</div>
												<div class="col-md-3 col-md-3 col-sm-12 column">
													<button id="cancel" class="btn btn-danger w-100"
														formaction="homepage" formnovalidate="formnovalidate">Cancel</button>
												</div>
											</div>
										</fieldset>
									</form:form>
								</div>
							</div>


							<!-- Results Panel -->
							<c:choose>
								<c:when test="${assignmentList.size() > 0}">
									<div class="row">
										<div class="col-xs-12 col-sm-12">
											<div class="x_panel">

												<h6 class="text-center mt-3">
													Assignments<font size="2px"> |
														${assignmentList.size()} Records Found </font>
												</h6>


												<div class="x_content">
													<div class="table-responsive testAssignTable">
														<table class="table table-hover">
															<thead>
																<tr>
																	<th>Sr. No.</th>
																	<th>Session Month <i class="fa fa-sort"
																		aria-hidden="true" style="cursor: pointer"></i></th>
																	<th>Session Year <i class="fa fa-sort"
																		aria-hidden="true" style="cursor: pointer"></i></th>
																	<th>Course</th>
																	<th>Assignment Name <i class="fa fa-sort"
																		aria-hidden="true" style="cursor: pointer"></i></th>

																	<th>End Date <i class="fa fa-sort"
																		aria-hidden="true" style="cursor: pointer"></i></th>
																	<th>Marks out of <i class="fa fa-sort"
																		aria-hidden="true" style="cursor: pointer"></i></th>
																	<th>Assignment Type <i class="fa fa-sort"
																		aria-hidden="true" style="cursor: pointer"></i></th>
																	<th>Assignment File</th>
																	<th>Show Results</th>
																	<th>Actions</th>
																</tr>
															</thead>
															<tbody>

																<c:forEach var="assignment" items="${assignmentList}"
																	varStatus="status">
																	<tr>
																		<td><c:out value="${status.count}" /></td>
																		<td><c:out value="${assignment.acadMonth}" /></td>
																		<td><c:out value="${assignment.acadYear}" /></td>
																		<td><c:out value="${assignment.courseName}" /></td>
																		<td><c:out value="${assignment.assignmentName}" /></td>

																		<td><c:out value="${assignment.endDate}" /></td>
																		<td><c:out value="${assignment.maxScore}" /></td>
																		<td><c:out value="${assignment.assignmentType}" /></td>
																		<td><c:if
																				test="${assignment.showFileDownload eq 'true'}">
																				<c:url value="downloadFile" var="downloadurl">
																					<c:param name="id" value="${assignment.id}" />
																				</c:url>
																				<a href="${downloadurl}" title="Details">Download</a>&nbsp;</c:if>

																			<c:if
																				test="${assignment.showFileDownload eq 'false'}">No File</c:if></td>
																		<td>
																		<c:if test="${assignment.createdByAdmin eq 'Y'}">
																			<c:if test="${assignment.showResultsToStudents eq 'Y' }"> Already Shown
																			</c:if> 
																			<c:if test="${assignment.showResultsToStudents ne 'Y' }"> Not Shown</c:if>
																		</c:if>
																		<c:if test="${assignment.createdByAdmin ne 'Y'}">
																			<c:if test="${assignment.showResultsToStudents eq 'Y' }"> Already Shown
																			</c:if> 
																			<c:if test="${assignment.showResultsToStudents ne 'Y' }">
																				<div class="col-sm-12 column" style="float: right;" id="show">
																				<a href="#" id="show${assignment.id}"
																						class="showClass"
																						onclick='document.getElementById(this.id).removeAttribute("href");'>
																						Show Now </a>
																				</div>
																			</c:if>
																		</c:if>
																		</td>

																		<td class="d-flex"><c:url value="viewAssignment" var="viewUrl">
																				<c:param name="id" value="${assignment.id}" />
																			</c:url> <c:url value="deleteAssignment" var="deleteurl">
																				<c:param name="id" value="${assignment.id}" />
																			</c:url> <c:url value="evaluateByStudent"
																				var="evaluateAssignmentUrl">
																				<%-- <c:param name="acadMonth"
																					value="${assignment.acadMonth}" />
																				<c:param name="acadYear"
																					value="${assignment.acadYear}" /> --%>
																				<c:param name="courseId"
																					value="${assignment.courseId}" />
																				<c:param name="id"
																					value="${assignment.id}" />
																			</c:url> <c:url value="viewByGroupAssignment"
																				var="viewByGroupUrl">
																				<c:param name="id" value="${assignment.id }"></c:param>

																			</c:url> <a href="${viewUrl}" title="View Assignment"><i
																				class="fa fa-info-circle fa-lg"></i></a>&nbsp;<sec:authorize
																				access="hasAnyRole('ROLE_FACULTY')"><a href="${evaluateAssignmentUrl}"
																					title="Evaluate Assignment"><i
																					class="fas fa-check"></i></a></sec:authorize>&nbsp;
																					<%--  <a href="${viewByGroupUrl}"
																			title="View By Group"><i class="fa fa-users"></i></a> --%>
																			
																			&nbsp;<sec:authorize access="hasAnyRole('ROLE_FACULTY')">&nbsp;<a href="${deleteurl}"
																					onclick="return confirm('Are you sure you want to delete this record?')"
																					title="Delete assignment"><i
																					class="fas fa-trash"></i></a>&nbsp;
																			</sec:authorize>&nbsp; <sec:authorize
																				access="hasAnyRole('ROLE_ADMIN')">
																				<c:url value="makeAssignmentInactive"
																					var="makeAssignmentInactiveurl">
																					<c:param name="id" value="${assignment.id }"></c:param>


																				</c:url>
																				<a href="${makeAssignmentInactiveurl}"
																					title="Make Assignment Inactive"><i
																					class="fa fa-check-square-o fa-lg"></i></a>&nbsp;</sec:authorize></td>
																	</tr>
																</c:forEach>

															</tbody>
														</table>
													</div>
												</div>
											</div>
										</div>

									</div>
									<jsp:include page="../common/paginate.jsp">
										<jsp:param name="baseUrl" value="searchFacultyAssignment" />
									</jsp:include>
								</c:when>
							</c:choose>
						</div>
					</div>
					<!-- /page content: END -->

			
					



				</div>

				<!-- SIDEBAR START -->
				<jsp:include page="../common/newSidebar.jsp" />
				<!-- SIDEBAR END -->
				<jsp:include page="../common/footer.jsp" />
				
					<script type="text/javascript">
						$(".showClass")
								.click(
										function() {
											console
													.log("called ........................................................000000.");
											$(this).css('color', 'black');
											var testId = $(this).attr("id");

											var id = testId.substr(4, 5);
											console.log(id);
											$
													.ajax({
														type : 'GET',
														url : '${pageContext.request.contextPath}/showResultsToStudentsAssignment?'
																+ 'id=' + id,
														success : function(data) {

															$(this)
																	.find(
																			'span')
																	.addClass(
																			"icon-success");
															var str1 = "show";
															var str2 = str1
																	.concat(id);

															$('#' + str2).html(
																	"Shown");

														}

													});

										});
					</script>

					<script>
						$("#deleteurl").on("click", deleteAssignment); //when #deleteurl link is clicked, deleteAssignment() will be called

						function deleteAssignment() {

							swal(
									{
										title : "Are you sure?",
										text : "You will not be able to recover the Assignment later !",
										type : "warning"

									},
									function(isConfirm) {
										if (isConfirm) {
											var data = {};
											data["id"] = $("#id").html();
											$
													.ajax({
														type : "GET",
														contentType : "application/json",
														url : "${home}/deleteAssignment",
														data : JSON
																.stringify(data),
														dataType : "json",
														success : function() {
															swal(
																	"Deleted!",
																	"The Assignment has been deleted Successfully !!",
																	"success");
														},
														error : function() {
															swal(
																	"Error",
																	"Assignment Could not be deleted ! :)",
																	"error");
														}

													});
										}
									});
						}
					</script>
				

				