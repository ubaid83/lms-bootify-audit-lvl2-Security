<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

<jsp:include page="../common/newDashboardHeader.jsp" />
<div class="d-flex dataTableBottom" id="prilfeDetailsPage">
	<jsp:include page="../common/newLeftNavbar.jsp" />
	<sec:authorize access="hasRole('ROLE_STUDENT')">
		<jsp:include page="../common/newLeftSidebar.jsp" />
	</sec:authorize>
	<%-- <sec:authorize access="hasRole('ROLE_PARENT')">
		<jsp:include page="../common/newLeftSidebarParent.jsp" />
	</sec:authorize> --%>
	<jsp:include page="../common/rightSidebar.jsp" />


	<!-- DASHBOARD BODY STARTS HERE -->

	<div class="container-fluid m-0 p-0 dashboardWraper assignmentW">

		<jsp:include page="../common/newTopHeader.jsp" />

		<div class="container mt-5">
			<div class="row">
				<!-- SEMESTER CONTENT -->
				<div
					class="col-lg-9 col-md-9 col-sm-12 dashboardBody ml-auto mr-auto">
					<jsp:include page="../common/alert.jsp" />
					<div class="bg-white pb-5 mb-5">
						<nav class="" aria-label="breadcrumb">
							<ol class="breadcrumb d-flex">
								<li class="breadcrumb-item"><a
									href="${pageContext.request.contextPath}/homepage">Dashboard</a></li>
								<li class="breadcrumb-item active" aria-current="page">To Do Everyday
								</li>
							</ol>

						</nav>

						<div class="table-responsive mb-3 testAssignTable">
								<table class="table table-striped table-hover" 
								id="viewPendingList">
									<thead>
										<tr>
											<th scope="col">Task name</th>
											<th scope="col">Task type</th>
											<th scope="col">Task Id</th>
											<th scope="col">Task Start Date</th>
											<th scope="col">Task End Date</th>
			<sec:authorize access="hasAnyRole('ROLE_STUDENT','ROLE_FACULTY')"><th scope="col">Action Url</th></sec:authorize>
											
										</tr>
									</thead>
									<tbody>

										<c:forEach items="${task}" var="entry" varStatus="loop">
											<tr>
												<td>${entry.taskName}</td>



												<td><img
															src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRDu6IaowMFcVtMkq18mnrsnQ4MvEY5QZRso8ZAp0ageN5EGUC2"
															height=20 width=20>${entry.type}</td>
												<td>${entry.id}</td>
												<td>${entry.startDate}</td>
														<td>${entry.endDate}</td>
												<td><sec:authorize access="hasRole('ROLE_STUDENT')">

																<c:url value="viewAssignmentFinal"
																	var="assignmentSubmitUrl">
																	<c:param name="courseId" value="${entry.courseId}" />
																</c:url>
																<c:url value="startStudentTestNew"
																	var="startStudentTestUrl">
																	<c:param name="id" value="${entry.id}" />
																</c:url>
																<c:url value="startStudentTestUpdatedForSubjective"
																	var="startStudentTestForSubjectiveUrl">
																	<c:param name="id" value="${entry.id}" />
																</c:url>
																<c:url value="startStudentTestUpdatedForMix"
																	var="startStudentTestForMixUrl">
																	<c:param name="id" value="${entry.id}" />
																</c:url>
																<c:url value="viewTestFinal" var="testList">
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



				<!-- SIDEBAR START -->
				<jsp:include page="../common/newSidebar.jsp" />
				<!-- SIDEBAR END -->
				
				<jsp:include page="../common/footer.jsp" />
				<script>
				$(function() {
				$('#viewPendingList').DataTable();
				});
				</script>