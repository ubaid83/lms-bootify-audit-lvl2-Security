<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<jsp:include page="../common/newDashboardHeader.jsp" />

<div class="d-flex" id="facultyAssignmentPage">
	<!-- DASHBOARD BODY STARTS HERE -->
	<div class="container-fluid m-0 p-0 dashboardWraper">
	<sec:authorize access="hasRole('ROLE_ADMIN')">
		<jsp:include page="../common/newAdminTopHeader.jsp" />
	</sec:authorize>
	<sec:authorize access="hasRole('ROLE_SUPPORT_ADMIN') || hasRole('ROLE_EXAM')">
		<header class="container-fluid sticky-top">
			<nav class="navbar navbar-expand-lg navbar-light p-0">
				<!--                     <i class="fas fa-bars fa-2x mr-3 leftnav-btn" data-toggle="modal" data-target="#leftnav"></i> -->
				<a class="navbar-brand" href="homepage"> <c:choose>
						<c:when test="${instiFlag eq 'nm'}">
							<img src="<c:url value="/resources/images/logo.png" />"
								class="logo" title="NMIMS logo" alt="NMIMS logo" />
						</c:when>
						<c:otherwise>
							<img src="<c:url value="/resources/images/svkmlogo.png" />"
								class="logo" title="SVKM logo" alt="SVKM logo" />
						</c:otherwise>
					</c:choose>
				</a>
				<button class="adminNavbarToggler" type="button"
					data-toggle="collapse" data-target="#adminNavbarCollapse">
					<i class="fas fa-bars"></i>
				</button>

				<div class="collapse navbar-collapse" id="adminNavbarCollapse">
					<ul class="navbar-nav ml-auto">
					<sec:authorize access="hasRole('ROLE_SUPPORT_ADMIN')">
					<li id="program" class="nav-item active" data-toggle="tooltip"
							data-placement="bottom" title="Update Marks"><a
							class="nav-link" href="${pageContext.request.contextPath}/uploadTeeMarksChangeForm"><i class="fab fa-readme"></i>
								<span>Update Marks</span></a></li>
					</sec:authorize>
						<li id="program" class="nav-item active" data-toggle="tooltip"
							data-placement="bottom" title="My Program"><a
							class="nav-link" href="homepage"><i class="fas fa-home"></i>
								<span>Home</span></a></li>
					</ul>
				</div>
			</nav>
		</header>
	</sec:authorize>
		<!-- SEMESTER CONTENT -->
		<div class="col-lg-12 col-md-12 col-sm-12 dashboardBody p-5">
			<!-- page content: START -->
			<nav aria-label="breadcrumb">
				<ol class="breadcrumb">
					<li class="breadcrumb-item"><a
						href="${pageContext.request.contextPath}/homepage">Dashboard</a></li>

					<sec:authorize access="hasRole('ROLE_STUDENT')">
						<c:out value="${AcadSession}" />
					</sec:authorize>
					<li class="breadcrumb-item active" aria-current="page">TEE
						List</li>
				</ol>
			</nav>

			<jsp:include page="../common/alert.jsp" />

			<!-- Input Form Panel -->
			<div class="card bg-white border">
				<div class="card-body">
					<h5 class="text-center pb-2 border-bottom">Tee List</h5>



					<div class="table-responsive mt-3 mb-3 testAssignTable">
						<table class="table table-striped table-hover">
							<thead>
								<tr>
									<th scope="col">Sl. No.</th>
									<th scope="col">Tee Name</th>
									<th scope="col">Tee Description</th>
									<th scope="col">Program</th>
									<th scope="col">Module Name</th>
									<th scope="col">Assign Faculty</th>
									<th scope="col">Acad Session</th>
									<th scope="col">Acad Year</th>
									<th scope="col">Start Date</th>
									<th scope="col">End Date</th>
									<th scope="col">Campus</th>
									<th scope="col">Total External Marks</th>
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
											<td><c:out value="${tee.moduleName}(${tee.courseName})" /></td>
										</c:if>
										<c:if test="${tee.courseName eq null}">
											<td><c:out value="${tee.moduleName}" /></td>
										</c:if>
										<td><c:out value="${tee.facultyName}" /></td>



										<td><c:out value="${tee.acadSession}" /></td>

										<td><c:out value="${tee.acadYear}" /></td>
										<td><c:out value="${tee.startDate}" /></td>
										<td><c:out value="${tee.endDate}" /></td>
										<td><c:out value="${tee.campusName}" /></td>
										<td><c:out value="${tee.externalMarks}" /></td>
									<%-- 	<c:choose>
											<c:when test="${tee.isSubmitted eq 'Y'}">
												<td><c:out value="Evaluated" /></td>
											</c:when>
											<c:otherwise>
												<td><c:out value="Pending" /></td>
											</c:otherwise>
										</c:choose> --%>
										
										
											<c:choose>
											<c:when test="${tee.isTeeDivisionWise eq 'Y'}">
												<c:choose>
													<c:when test="${empty tee.parentTeeId}">
														<td><c:out value="-" /></td>
													</c:when>
													<c:otherwise>
														<c:choose>
															<c:when test="${tee.isSubmitted eq 'Y'}">
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
													<c:when test="${tee.isSubmitted eq 'Y'}">
														<td><c:out value="Evaluated" /></td>
													</c:when>
													<c:otherwise>
														<td><c:out value="Pending" /></td>
													</c:otherwise>
												</c:choose>
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

										<td><c:url value="deleteTee" var="deleteurl">
												<c:param name="id" value="${tee.id}" />
											</c:url> <c:if test="${tee.isNonEventModule ne 'Y'}">
												<c:url value="/addTeeForm" var="editurl">
													<c:param name="id" value="${tee.id}" />
												</c:url>
											</c:if> <c:if test="${tee.isNonEventModule eq 'Y'}">
												<c:url value="/addTeeFormForNonEventModules" var="editurl">
													<c:param name="id" value="${tee.id}" />
												</c:url>
											</c:if> <c:url value="/addTeeComponentsForm" var="addComponent">
												<c:param name="id" value="${tee.id}" />
											</c:url> <c:url value="/evaluateTee" var="evaluateTeeStudents">
												<c:param name="teeId" value="${tee.id}" />
											</c:url> <c:url value="/evaluateTeeForNonEventModule"
												var="evaluateTeeStudentsByAdmin">
												<c:param name="teeId" value="${tee.id}" />
											</c:url> <c:url value="/searchTeeList" var="TeeListDivision">
												<c:param name="teeId" value="${tee.id}" />
											</c:url> <sec:authorize access="hasAnyRole('ROLE_SUPPORT_ADMIN','ROLE_ADMIN')">
												<c:if test="${teeId eq null}">
													<c:if test="${tee.isTeeDivisionWise eq 'Y' && empty tee.parentTeeId}">
														<a href="teeListBySupportAdmin?teeId=${tee.id}"
															title="Show All Divisions TEE"><i
															class="fa fa-users fa-lg"></i></a>
													</c:if>
												</c:if>

											<c:if test="${tee.flagTcs eq 'F'}">
												<a href="updateTeeDateBySupportAdminForm?teeId=${tee.id}"
													title="Update Date"><i
													class="fas fa-edit fa-lg text-primary"></i></a>
											</c:if>
											<c:if test="${tee.flagTcs eq 'S'}">Data Sent</c:if>
											</sec:authorize> <sec:authorize access="hasRole('ROLE_EXAM')">
												<c:if test="${teeId eq null}">
													<c:if test="${tee.isTeeDivisionWise eq 'Y'}">
														<a href="teeListBySupportAdmin?TeeId=${tee.id}"
															title="Show All Divisions Tee"><i
															class="fa fa-users fa-lg"></i></a>
													</c:if>
												</c:if>

											<c:if test="${tee.flagTcs eq 'F'}">
												<a href="updateTeeDateBySupportAdminForm?teeId=${tee.id}"
													title="Update Date"><i
													class="fas fa-edit fa-lg text-primary"></i></a>
											</c:if>
											<c:if test="${tee.flagTcs eq 'S'}">Data Sent</c:if>
											</sec:authorize></td>
									</tr>
								</c:forEach>
							</tbody>
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