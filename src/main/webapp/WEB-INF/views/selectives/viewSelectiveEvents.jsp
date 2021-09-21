<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>


<jsp:include page="../common/newDashboardHeader.jsp" />

<div class="d-flex dataTableBottom paddingFixAssign" id="assignmentPage">
	<jsp:include page="../common/newLeftNavbar.jsp" />
	<jsp:include page="../common/newLeftSidebar.jsp" />
	<jsp:include page="../common/rightSidebar.jsp" />

	<div class="container-fluid m-0 p-0 dashboardWraper assignmentW">
		<jsp:include page="../common/newTopHeader.jsp" />

		<div class="container mt-5">
			<div class="row">
				<div
					class="col-lg-9 col-md-9 col-sm-12 dashboardBody ml-auto mr-auto">

					<jsp:include page="../common/alert.jsp" />



					<!-- PAGE CONTENT -->

					<nav aria-label="breadcrumb">
						<ol class="breadcrumb">
							<li class="breadcrumb-item"><a
								href="${pageContext.request.contextPath}/homepage">Dashboard</a></li>
							<li class="breadcrumb-item" aria-current="page"><c:out
									value="${Program_Name}" /> <sec:authorize
									access="hasRole('ROLE_STUDENT')">
									<c:out value="${AcadSession}" />
								</sec:authorize></li>
							<li class="breadcrumb-item active" aria-current="page">Electives/Specialized
								Subjects Registration</li>
						</ol>
					</nav>


					<div class="card bg-white border">
						<div class="card-body">

							<h5 class="text-center pb-2 border-bottom">Electives/Specialized
								Subjects Registration</h5>

							<div class="row">
								<div class="col-xs-12 col-sm-12">
									<div class="x_panel">
										<div class="x_title">
											<h6>Registrations | ${pageRowCount} Records Found</h6>
										</div>
										<div class="x_content">
											<div class="table-responsive">
												<table class="table table-hover ">
													<thead>

														<tr>
															<th>Sr.No</th>
															<th>Registration Name</th>
															<th>StartDate</th>
															<th>EndDate</th>

															<th>Actions</th>
														</tr>
													</thead>
													<tbody>

														<c:forEach var="event" items="${eventsList}"
															varStatus="status">
															<tr>
																<td><c:out value="${status.count}" /></td>
																<td><c:out value="${event.title}" /></td>
																<td><c:out value="${event.startDate}" /></td>
																<td><c:out value="${event.endDate}" /></td>

																<c:url value="uploadSelectiveCourseForm"
																		var="uploadCourseUrl">
																		<c:param name="eventId" value="${event.id}" />
																	</c:url> <c:url value="uploadSelectiveStudentForm"
																		var="uploadStudentUrl">
																		<c:param name="id" value="${event.id}" />
																	</c:url> <c:url value="deleteSelectiveByEventId"
																		var="deleteurl">
																		<c:param name="id" value="${event.id}" />
																	</c:url> <c:url value="studentRegisterForm"
																		var="viewStudentRegisterForm">
																		<c:param name="eventId" value="${event.id}" />
																	</c:url> 
																<td>
																	
																	<sec:authorize access="hasRole('ROLE_ADMIN')">

																		<a href="${uploadCourseUrl}"
																			title="Upload Course For An Event"><i
																			class="fa fa-plus-square-o fa-lg"></i></a>&nbsp;
																	<a href="${uploadStudentUrl}"
																			title="Upload Course For An Event"><i
																			class="fa fa-plus-square-o fa-lg"></i></a>&nbsp;
																	<a href="${deleteurl}" title="Delete"
																			onclick="return confirm('Are you sure you want to delete this record?')"><i
																			class="fa fa-trash-o fa-lg"></i></a>

																	</sec:authorize> 
																	
																	<sec:authorize access="hasRole('ROLE_STUDENT')">
																		<a href="${viewStudentRegisterForm}" title="Register">
																		Register</a>&nbsp;
																	 </sec:authorize>
																	 </td>
															</tr>
														</c:forEach>
													</tbody>
												</table>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>



					<!-- /page content -->







				</div>




				<!-- SIDEBAR START -->
				<jsp:include page="../common/newSidebar.jsp" />
				<!-- SIDEBAR END -->
				<jsp:include page="../common/footer.jsp" />