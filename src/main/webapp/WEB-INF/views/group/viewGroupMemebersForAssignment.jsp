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
							<li class="breadcrumb-item active" aria-current="page">Groups</li>
						</ol>
					</nav>

					<jsp:include page="../common/alert.jsp" />


					<!-- Input Form Panel -->
					<div class="card bg-white border">
						<div class="card-body">
						
						<div class="col-xs-12 col-sm-12">
								<div class="x_panel">

									<div class="x_title">
										<h5>Students of ${groups.groupName}</h5>
										
									</div>

									<div class="x_content">
										<form:form action="viewGroupStudents" method="post"
											modelAttribute="groups">

											<div class="table-responsive">
												<table class="table  table-hover">
													<thead>
														<tr>
															<th>Sr. No.</th>
															<th>Username <i class="fa fa-sort"
																aria-hidden="true" style="cursor: pointer"></i></th>
															<th>Student Name <i class="fa fa-sort"
																aria-hidden="true" style="cursor: pointer"></i></th>
															<th>Roll No. <i class="fa fa-sort"
																aria-hidden="true" style="cursor: pointer"></i></th>
															<th>Allocation Status <i class="fa fa-sort"
																aria-hidden="true" style="cursor: pointer"></i></th>

														</tr>
													</thead>
													<tfoot>
														<tr>
															<th></th>

															<th>Username</th>
															<th>Student Name</th>
															<th>Roll No.</th>
															<th>Allocation Status</th>
														</tr>
													</tfoot>
													<tbody>

														<c:forEach var="student" items="${students}"
															varStatus="status">
															<tr>
																<td><c:out value="${status.count}" /></td>


																<td><c:out value="${student.username}" /></td>
																<td><c:out
																		value="${student.firstname} ${student.lastname}" /></td>
																<td><c:out value="${student.rollNo}" /></td>
																<td><c:out value="${student.allocated}" /></td>
															</tr>
														</c:forEach>

													</tbody>

												</table>
												<div class="row">

													<div class="col-sm-8 column">
														<div class="form-group">
															<a href="viewByGroupAssignment?id=${assignmentId}"><i
																class="btn btn-large btn-primary">Back</i></a>

														</div>
													</div>
												</div>
											</div>
										</form:form>
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
				<jsp:include page="../common/footer.jsp" />