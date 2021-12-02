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

tr[data-saveAsDraft='Y'] {
    background: #ffef82;
}

tr[data-isSubmitted='Y'] {
    background: #dbffe3;
}

tr[data-isSubmitted='N'][data-saveAsDraft=''] {
    background: #ffd0d0;
}

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
							<li class="breadcrumb-item active" aria-current="page">ICA
								LIST</li>
						</ol>
					</nav>

					<jsp:include page="../common/alert.jsp" />


					<!-- Input Form Panel -->
					<div class="card bg-white border">
						<div class="card-body">
							<h5 class="text-center border-bottom pb-2 text-uppercase">ICA
								LIST</h5>
							<c:if test="${isQueryRaised eq true}">
								<a
									href="${pageContext.request.contextPath}/downloadIcaRaiseQueryReport"><font
									color="red">Download Student ICA Queries</font></a>
							</c:if>

							<div class="table-responsive mt-3 mb-3 testAssignTable">
								<table class="table table-striped table-hover">
									<thead>
										<tr>
											<th scope="col">Actions</th>
											<th scope="col">Sl. No.</th>
											<th scope="col">ICA Name</th>
											<th scope="col">ICA Description</th>
											<th scope="col">Program</th>
											<th scope="col">Module Name</th>
											<th scope="col">Acad Session</th>
											<th scope="col">Acad Year</th>
											<%--<th scope="col">Start Date</th>
											<th scope="col">End Date</th>--%>
											<th scope="col">Campus</th>
											<th scope="col">Internal Marks</th>
											<th scope="col">Status</th>
											<th scope="col">Query Raised??</th>
											<th scope="col">Re-evaluate Approved</th>
											<th scope="col">Actions</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach var="ica" items="${icaList}" varStatus="status">
											<tr data-saveAsDraft="${ica.saveAsDraft}" 
											data-isSubmitted="<c:choose><c:when test="${ica.isSubmitted eq 'Y'}"><c:out value="Y"/></c:when><c:otherwise><c:out value="N"/></c:otherwise></c:choose>">
												<td><c:url value="deleteIca" var="deleteurl">
														<c:param name="icaId" value="${ica.id}" />
													</c:url> <%-- <td><c:out value="${course.property1}" /></td>
								<td><c:out value="${course.property2}" /></td>
								<td><c:out value="${course.property3}" /></td> --%> <c:url
														value="/addIcaForm" var="editurl">
														<c:param name="id" value="${ica.id}" />
													</c:url> <c:url value="/addIcaComponentsForm" var="addComponent">
														<c:param name="id" value="${ica.id}" />
													</c:url> <c:url value="/evaluateIca" var="evaluateIcaStudents">
														<c:param name="icaId" value="${ica.id}" />
													</c:url> <c:url value="/showEvaluatedInternalMarks"
														var="showInternalMarks">
														<c:param name="icaId" value="${ica.id}" />
													</c:url>
													<c:if test="${ica.isIcaDivisionWise eq 'Y'}">
													 <c:url value="/assignTestMarksToIcaForm"
														var="assgnTestMarksToIca">
														<c:param name="icaId" value="${ica.id}" />
														<c:param name="courseId" value="${ica.eventId}" />
													</c:url>
													</c:if>
													<c:if test="${ica.isIcaDivisionWise ne 'Y'}">
													<c:url value="/assignTestMarksToIcaForm"
														var="assgnTestMarksToIca">
														<c:param name="icaId" value="${ica.id}" />
														
													</c:url>
													</c:if>
													
													
													 <sec:authorize access="hasRole('ROLE_ADMIN')">
														<a href="${deleteurl}" title="Delete"
															onclick="return confirm('Are you sure you want to delete this record?')"><i
															class="fas fa-trash-alt fa-lg text-danger"></i></a>
														<a href="${editurl}" title="Edit"><i
															class="fas fa-edit fa-lg text-primary"></i></a>
														<a href="${addComponent}" title="Add Components"><i
															class="fa fa-plus fa-lg"></i></a>
													</sec:authorize> <sec:authorize access="hasRole('ROLE_FACULTY')">
														<a href="${evaluateIcaStudents}" title="Evaluate ICA"><i
															class="fas fa-check-square"></i></a>
														<a href="${showInternalMarks}" title="Show Internal Marks"><i
															class="fa fa-info-circle fa-lg"></i></a>
														<c:if test="${ica.showTestMarksIcon eq 'true'}">
														<a href="${assgnTestMarksToIca}" title="Add Test Marks To Component"><i
															class="fas fa-file-alt"></i></a>
														</c:if>	
													</sec:authorize></td>
												<td><c:out value="${status.count}" /></td>
												<td><c:out value="${ica.icaName}" /></td>
												<%-- <td><c:out value="${course.abbr}" /></td> --%>
												<td><c:out value="${ica.icaDesc}" /></td>
												<td><c:out value="${ica.programName}" /></td>
												<c:if test="${ica.courseName ne null}">
													<td><c:out
															value="${ica.moduleName}(${ica.courseName})" /></td>
												</c:if>
												<c:if test="${ica.courseName eq null}">
													<td><c:out value="${ica.moduleName}" /></td>
												</c:if>




												<td><c:out value="${ica.acadSession}" /></td>

												<td><c:out value="${ica.acadYear}" /></td>
												<%-- <td><c:out value="${ica.startDate}" /></td>
												<td><c:out value="${ica.endDate}" /></td> --%>
												<td><c:out value="${ica.campusName}" /></td>
												<td><c:out value="${ica.internalMarks}" /></td>
												<c:choose>
													<c:when test="${ica.isSubmitted eq 'Y'}">
														<td><c:out value="Evaluated" /></td>
													</c:when>
													<c:when test="${ica.saveAsDraft eq 'Y'}">
															<td><c:out value="Draft" /></td>
													</c:when>
													<c:otherwise>
														<td><c:out value="Pending" /></td>
													</c:otherwise>
												</c:choose>
												<c:choose>
													<c:when test="${ica.icaQueryId ne null}">
														<td><c:out value="Y" /></td>
													</c:when>
													<c:otherwise>
														<td><c:out value="N" /></td>
													</c:otherwise>
												</c:choose>

												<c:choose>
													<c:when test="${ica.isApproved eq 'Y'}">
														<td><c:out value="Approved" /></td>
													</c:when>
													<c:otherwise>
														<td><c:out value="Not Approved" /></td>
													</c:otherwise>
												</c:choose>
												<td><c:url value="deleteIca" var="deleteurl">
														<c:param name="icaId" value="${ica.id}" />
													</c:url> <%-- <td><c:out value="${course.property1}" /></td>
								<td><c:out value="${course.property2}" /></td>
								<td><c:out value="${course.property3}" /></td> --%> <c:url
														value="/addIcaForm" var="editurl">
														<c:param name="id" value="${ica.id}" />
													</c:url> <c:url value="/addIcaComponentsForm" var="addComponent">
														<c:param name="id" value="${ica.id}" />
													</c:url> <c:url value="/evaluateIca" var="evaluateIcaStudents">
														<c:param name="icaId" value="${ica.id}" />
													</c:url> <c:url value="/showEvaluatedInternalMarks"
														var="showInternalMarks">
														<c:param name="icaId" value="${ica.id}" />
													</c:url>
													<c:if test="${ica.isIcaDivisionWise eq 'Y'}">
													 <c:url value="/assignTestMarksToIcaForm"
														var="assgnTestMarksToIca">
														<c:param name="icaId" value="${ica.id}" />
														<c:param name="courseId" value="${ica.eventId}" />
													</c:url>
													</c:if>
													<c:if test="${ica.isIcaDivisionWise ne 'Y'}">
													<c:url value="/assignTestMarksToIcaForm"
														var="assgnTestMarksToIca">
														<c:param name="icaId" value="${ica.id}" />
														
													</c:url>
													</c:if>
													
													
													 <sec:authorize access="hasRole('ROLE_ADMIN')">
														<a href="${deleteurl}" title="Delete"
															onclick="return confirm('Are you sure you want to delete this record?')"><i
															class="fas fa-trash-alt fa-lg text-danger"></i></a>
														<a href="${editurl}" title="Edit"><i
															class="fas fa-edit fa-lg text-primary"></i></a>
														<a href="${addComponent}" title="Add Components"><i
															class="fa fa-plus fa-lg"></i></a>
													</sec:authorize> <sec:authorize access="hasRole('ROLE_FACULTY')">
														<a href="${evaluateIcaStudents}" title="Evaluate ICA"><i
															class="fas fa-check-square"></i></a>
														<a href="${showInternalMarks}" title="Show Internal Marks"><i
															class="fa fa-info-circle fa-lg"></i></a>
														<c:if test="${ica.showTestMarksIcon eq 'true'}">
														<a href="${assgnTestMarksToIca}" title="Add Test Marks To Component"><i
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