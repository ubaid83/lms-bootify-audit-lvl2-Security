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
<head>
<style>
.osTable {
	overflow-x: scroll;
}

table.scroll {
	width: 100%;
	/* Optional */
	/* border-collapse: collapse; */
	border-spacing: 0;
	/*border: 2px solid black;*/
}

table.scroll tbody, table.scroll thead {
	display: block;
}

thead tr th {
	height: 30px;
	line-height: 30px;
	/*text-align: left;*/
}

table.scroll tbody {
	height: 500px;
	overflow-y: auto;
	overflow-x: hidden;
}

tbody {
	border-top: 2px solid black;
}

tbody td, thead th {
	width: 20%;
	/* Optional */
	border-right: 1px solid black;
}

tbody td:last-child, thead th:last-child {
	border-right: none;
}
</style>
</head>
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
							<sec:authorize access="hasRole('ROLE_STUDENT')">
								<c:out value="${AcadSession}" />
							</sec:authorize>
							<li class="breadcrumb-item active" aria-current="page">Term End Exam Evaluation
								LIST</li>
						</ol>
					</nav>

					<jsp:include page="../common/alert.jsp" />


					<!-- Input Form Panel -->
					<div class="card bg-white border">
						<div class="card-body">
							<h5 class="text-center border-bottom pb-2 text-uppercase">Term End Exam Evaluation
								LIST</h5>
							<c:if test="${isQueryRaised eq true}">
								<a
									href="${pageContext.request.contextPath}/downloadTeeRaiseQueryReport"><font
									color="red">Download Student TEE Queries</font></a>
							</c:if>

							<div class="table-responsive mt-3 mb-3 testAssignTable">
								<table class="table table-striped table-hover">
									<thead>
										<tr>
											<th scope="col">Sl. No.</th>
											<th scope="col">TEE Name</th>
											<th scope="col">TEE Description</th>
											<th scope="col">Program</th>
											<th scope="col">Module Name</th>
											<th scope="col">Acad Session</th>
											<th scope="col">Acad Year</th>
											<%--<th scope="col">Start Date</th>
											<th scope="col">End Date</th>--%>
											<th scope="col">Campus</th>
											<th scope="col">External Marks</th>
											<th scope="col">Status</th>
											<th scope="col">Query Raised??</th>
											<th scope="col">Re-evaluate Approved</th>
											<th scope="col">Actions</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach var="tee" items="${teeList}" varStatus="status">
											<tr>
												<td><c:out value="${status.count}" /></td>
												<td><c:out value="${tee.teeName}" /></td>
												<%-- <td><c:out value="${course.abbr}" /></td> --%>
												<td><c:out value="${tee.teeDesc}" /></td>
												<td><c:out value="${tee.programName}" /></td>
												<c:if test="${tee.courseName ne null}">
													<td><c:out
															value="${tee.moduleName}(${tee.courseName})" /></td>
												</c:if>
												<c:if test="${tee.courseName eq null}">
													<td><c:out value="${tee.moduleName}" /></td>
												</c:if>

												<td><c:out value="${tee.acadSession}" /></td>

												<td><c:out value="${tee.acadYear}" /></td>
												<%-- <td><c:out value="${tee.startDate}" /></td>
												<td><c:out value="${tee.endDate}" /></td> --%>
												<td><c:out value="${tee.campusName}" /></td>
												<td><c:out value="${tee.externalMarks}" /></td>
												<c:choose>
													<c:when test="${tee.isSubmitted eq 'Y'}">
														<td><c:out value="Evaluated" /></td>
													</c:when>
													<c:otherwise>
														<td><c:out value="Pending" /></td>
													</c:otherwise>
												</c:choose>
												<c:choose>
													<c:when test="${tee.teeQueryId ne null}">
														<td><c:out value="Y" /></td>
													</c:when>
													<c:otherwise>
														<td><c:out value="N" /></td>
													</c:otherwise>
												</c:choose>

												<c:choose>
													<c:when test="${tee.isApproved eq 'Y'}">
														<td><c:out value="Approved" /></td>
													</c:when>
													<c:otherwise>
														<td><c:out value="Not Approved" /></td>
													</c:otherwise>
												</c:choose>
												<td>
                                                    <c:url value="/evaluateTee" var="evaluateurl">
														<c:param name="teeId" value="${tee.id}" />
													</c:url> 
													<c:url value="/showEvaluatedTeeMarks" var="showTeeMarks">
														<c:param name="teeId" value="${tee.id}" />
													</c:url>  
													<c:url value="/assignAssignmentMarksToTeeForm" var="teeMarksAutoAssign">
														<c:param name="teeId" value="${tee.id}" />
														<c:if test="${'Y' eq tee.isTeeDivisionWise}">
														<c:param name="courseId" value="${tee.eventId}" />
														</c:if>
														
													</c:url>
													<sec:authorize access="hasRole('ROLE_FACULTY')">
														<a href="${evaluateurl}" title="Evaluate TEE"><i
															class="fas fa-check-square"></i></a>
														<a href="${showTeeMarks}" title="Show External Marks"><i
															class="fa fa-info-circle fa-lg"></i></a>
														<c:if test="${'Y' eq tee.autoAssignMarks}">
														<a href="${teeMarksAutoAssign}" title="Add Test Marks To TEE"><i
															class="fas fa-file-alt"></i></a>
														</c:if>	
													</sec:authorize></td>
											</tr>
										</c:forEach>
									</tbody>
								</table>
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
				<jsp:include page="../common/footer.jsp" />



				<!-- <script type="text/javascript"
	src="https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.0/MathJax.js?config=TeX-AMS_HTML"></script> -->

				<script src="https://unpkg.com/sweetalert/dist/sweetalert.min.js"></script>