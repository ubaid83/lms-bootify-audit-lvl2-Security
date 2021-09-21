<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>


<jsp:include page="../common/newDashboardHeader.jsp" />

<div class="d-flex" id="facultyAssignmentPage">
	<%-- <jsp:include page="../common/newAdminLeftNavBar.jsp" /> --%>
	<jsp:include page="../common/rightSidebarAdmin.jsp" />


	<!-- DASHBOARD BODY STARTS HERE -->

	<div class="container-fluid m-0 p-0 dashboardWraper">

		<jsp:include page="../common/newAdminTopHeader.jsp" />

		<!-- SEMESTER CONTENT -->
		<div class="col-lg-12 col-md-12 col-sm-12 dashboardBody p-5">

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
						List</li>
				</ol>
			</nav>

			<jsp:include page="../common/alert.jsp" />

			<!-- Input Form Panel -->
			<div class="card bg-white border">
				<div class="card-body">
					<h5 class="text-center pb-2 border-bottom">ICA List</h5>

					

					<div class="table-responsive mt-3 mb-3 testAssignTable">
						<table class="table table-striped table-hover">
							<thead>
								<tr>
									<th scope="col">Sl. No.</th>
									<th scope="col">ICA Name</th>
									<th scope="col">ICA Description</th>
									<th scope="col">Program</th>
									<th scope="col">Module Name</th>
									<th scope="col">Assign Faculty</th>
									<th scope="col">Acad Session</th>
									<th scope="col">Acad Year</th>
									<th scope="col">Start Date</th>
									<th scope="col">End Date</th>
									<th scope="col">Campus</th>
									<th scope="col">Total Internal Marks</th>
									<th scope="col">Status</th>
									<th scope="col">Query Raised??</th>
									<th scope="col">Re-evaluate Approved</th>
									<th scope="col">Actions</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="ica" items="${icaList}" varStatus="status">
									<tr>
										<td><c:out value="${status.count}" /></td>
										<td><c:out value="${ica.icaName}" /></td>
										<%-- <td><c:out value="${course.abbr}" /></td> --%>
										<td><c:out value="${ica.icaDesc}" /></td>
										<td><c:out value="${ica.programName}" /></td>

										<c:if test="${ica.courseName ne null}">
											<td><c:out value="${ica.moduleName}(${ica.courseName})" /></td>
										</c:if>
										<c:if test="${ica.courseName eq null}">
											<td><c:out value="${ica.moduleName}" /></td>
										</c:if>
										<td><c:out value="${ica.facultyName}" /></td>



										<td><c:out value="${ica.acadSession}" /></td>

										<td><c:out value="${ica.acadYear}" /></td>
										<td><c:out value="${ica.startDate}" /></td>
										<td><c:out value="${ica.endDate}" /></td>
										<td><c:out value="${ica.campusName}" /></td>
										<td><c:out value="${ica.internalMarks}" /></td>
										<c:choose>
											<c:when test="${ica.isIcaDivisionWise eq 'Y'}">
												<c:choose>
													<c:when test="${empty ica.parentIcaId}">
														<td><c:out value="-" /></td>
													</c:when>
													<c:otherwise>
														<c:choose>
															<c:when test="${ica.isSubmitted eq 'Y'}">
																<td><c:out value="Evaluated" /></td>
															</c:when>
															<c:otherwise>
																<td><c:out value="Pending" /></td>
															</c:otherwise>
														</c:choose>
													</c:otherwise>
												</c:choose>
											</c:when>
											<c:otherwise>
												<c:choose>
													<c:when test="${ica.isSubmitted eq 'Y'}">
														<td><c:out value="Evaluated" /></td>
													</c:when>
													<c:otherwise>
														<td><c:out value="Pending" /></td>
													</c:otherwise>
												</c:choose>
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
												<c:param name="id" value="${ica.id}" />
											</c:url> <%-- <td><c:out value="${course.property1}" /></td>
								<td><c:out value="${course.property2}" /></td>
								<td><c:out value="${course.property3}" /></td> --%> 
											<c:if test="${ica.isNonEventModule ne 'Y'}">
											<c:url
												value="/addIcaForm" var="editurl">
												<c:param name="id" value="${ica.id}" />
											</c:url> </c:if>
											
											<c:if test="${ica.isCourseraIca eq 'Y'}">
											<c:url
												value="/addIcaFormForCoursera" var="editurl">
												<c:param name="id" value="${ica.id}" />
											</c:url> 
											</c:if>
											
											<c:if test="${ica.isNonEventModule eq 'Y'}">
											<c:url
												value="/addIcaFormForNonEventModules" var="editurl">
												<c:param name="id" value="${ica.id}" />
											</c:url>
											</c:if> <c:url value="/addIcaComponentsForm" var="addComponent">
												<c:param name="id" value="${ica.id}" />
											</c:url> <c:url value="/evaluateIca" var="evaluateIcaStudents">
												<c:param name="icaId" value="${ica.id}" />
												</c:url>
												<c:url value="/evaluateIcaForNonEventModule" var="evaluateIcaStudentsByAdmin">
												<c:param name="icaId" value="${ica.id}" />
												</c:url>
												<c:url value="/showEvaluatedInternalMarks"
													var="showInternalMarks">
													<c:param name="icaId" value="${ica.id}" />
												</c:url>
												<c:url value="/searchIcaList"
													var="icaListDivision">
													<c:param name="icaId" value="${ica.id}" />
												</c:url>
												<c:url value="/createStudentGroupForm"
													var="addStudentsFacultyWise">
													<c:param name="id" value="${ica.id}" />
												</c:url>
											 <sec:authorize access="hasRole('ROLE_ADMIN')">
												<a href="${deleteurl}" title="Delete"
													onclick="return confirm('Are you sure you want to delete this record?')"><i
													class="fas fa-trash-alt fa-lg text-danger"></i></a>
												
												<a href="${editurl}" title="Edit"><i
													class="fas fa-edit fa-lg text-primary"></i></a>
												<c:if test="${icaId eq null}">
												<a href="${addComponent}" title="Add Components"><i
													class="fa fa-plus fa-lg"></i></a>
												<c:if test="${ica.isIcaDivisionWise eq 'Y'}">
												<a href="${icaListDivision}" title="Show All Divisions ICA"><i
													class="fa fa-users fa-lg"></i></a>
												</c:if>
												<c:if test="${fn:contains(ica.assignedFaculty, ',')}">
												<a href="${addStudentsFacultyWise}" title="Add Students FacultyWise"><i
													class="fa fa-cog fa-lg"></i></a>
												</c:if>
												</c:if>
											</sec:authorize> <sec:authorize access="hasRole('ROLE_FACULTY')">
												<a href="${evaluateIcaStudents}" title="Evaluate ICA"><i
													class="fas fa-check-square"></i></a>

											</sec:authorize> <c:if test="${ica.isApproved eq 'Y'}">
												<c:if test="${ica.isNonEventModule ne 'Y'}">
												<sec:authorize access="hasRole('ROLE_ADMIN')">
													<a href="${evaluateIcaStudents}" title="Evaluate ICA"><i
														class="fas fa-check-square"></i></a>

												</sec:authorize>
												</c:if>
											</c:if> <c:if test="${ica.isSubmitted eq 'Y'}">
												<sec:authorize access="hasRole('ROLE_ADMIN')">
													<a href="${showInternalMarks}" title="Show Ica Marks"><i
														class="fa fa-info-circle fa-lg"></i></a>

												</sec:authorize>
											</c:if><c:if test="${ica.isNonEventModule eq 'Y'}">
												<sec:authorize access="hasRole('ROLE_ADMIN')">
													<a href="${evaluateIcaStudentsByAdmin}" title="Evaluate ICA"><i
														class="fas fa-check-square"></i></a>

												</sec:authorize>
											</c:if></td>
									</tr>
								</c:forEach>
							</tbody>
							<tfoot>
								<tr>
									<th>--</th>
									<th>--</th>
									<th>--</th>
									<th>--</th>
									<th>--</th>
									<th>--</th>
									<th>--</th>
									<th>--</th>
									<th>--</th>
									<th>--</th>
									<th>--</th>
									<th>--</th>
									<th>--</th>
									<th>--</th>
									<th>--</th>
									<th>--</th>
								</tr>
							</tfoot>
						</table>
					</div>
				</div>
			</div>



			<!-- /page content: END -->

		</div>

		<!-- SIDEBAR START -->

		<!-- SIDEBAR END -->

		<jsp:include page="../common/newAdminFooter.jsp" />

		<script>
			$('input[type="checkbox"]').click(function() {
				if ($(this).prop("checked") == true) {
					console.log('checked!!!');
					var id = $(this).attr('id');
					$('#mark' + id).prop('disabled', false);
					$('#mark' + id).prop('required', true);
				} else if ($(this).prop("checked") == false) {
					var id = $(this).attr('id');
					$('#mark' + id).prop('disabled', true);
					$('#mark' + id).prop('required', false);
					$('#mark' + id).val('');
				}
			});
		</script>