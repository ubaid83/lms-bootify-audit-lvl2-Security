
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>



<jsp:include page="../common/newDashboardHeader.jsp" />

<div class="d-flex" id="facultyAssignmentPage">

	<!-- DASHBOARD BODY STARTS HERE -->
		<sec:authorize access="hasRole('ROLE_ADMIN')">
			<!-- <jsp:include page="../common/newAdminLeftNavBar.jsp" /> -->
		</sec:authorize>
		
	<div class="container-fluid m-0 p-0 dashboardWraper">
		<sec:authorize access="hasRole('ROLE_ADMIN')">
			<jsp:include page="../common/newAdminTopHeader.jsp" />
		</sec:authorize>
		<sec:authorize access="hasRole('ROLE_SUPPORT_ADMIN') || hasRole('ROLE_EXAM')">
		<header class="container-fluid sticky-top">
			<nav class="navbar navbar-expand-lg navbar-light p-0">
				<!-- <i class="fas fa-bars fa-2x mr-3 leftnav-btn" data-toggle="modal" data-target="#leftnav"></i> -->
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
											</c:url> 
											<c:if test="${ica.isNonEventModule ne 'Y'}">
												<c:url value="/addIcaForm" var="editurl">
													<c:param name="id" value="${ica.id}" />
												</c:url>
											</c:if> 
											<c:if test="${ica.isNonEventModule eq 'Y'}">
												<c:url value="/addIcaFormForNonEventModules" var="editurl">
													<c:param name="id" value="${ica.id}" />
												</c:url>
											</c:if> 
											<c:url value="/addIcaComponentsForm" var="addComponent">
												<c:param name="id" value="${ica.id}" />
											</c:url> <c:url value="/evaluateIca" var="evaluateIcaStudents">
												<c:param name="icaId" value="${ica.id}" />
											</c:url> 
											<c:url value="/evaluateIcaForNonEventModule"
												var="evaluateIcaStudentsByAdmin">
												<c:param name="icaId" value="${ica.id}" />
											</c:url> 
											<c:url value="/searchIcaList" var="icaListDivision">
												<c:param name="icaId" value="${ica.id}" />
											</c:url> 
											<sec:authorize access="hasRole('ROLE_SUPPORT_ADMIN') ">
												<c:if test="${icaId eq null}">
													<c:if test="${ica.isIcaDivisionWise eq 'Y'}">
														<a href="icaListBySupportAdmin?icaId=${ica.id}"
															title="Show All Divisions ICA"><i
															class="fa fa-users fa-lg"></i></a>
													</c:if>
												</c:if>
												<c:if test="${ica.flagTcs eq 'F'}">
												<a href="updateIcaDateBySupportAdminForm?icaId=${ica.id}"
													title="Update Date"><i
													class="fas fa-edit fa-lg text-primary"></i></a>

												<a
													href="updateIcaInternalPassMarkBySupportAdminForm?icaId=${ica.id}"
													title="Update Internal Pass Marks"> <i
													class="fas fa-marker fa-lg text-primary"></i></a>
												</c:if>
												<c:if test="${ica.flagTcs eq 'S'}">Data Sent</c:if>
											</sec:authorize> <sec:authorize
												access="hasRole('ROLE_EXAM') || hasRole('ROLE_ADMIN')">
												<c:if test="${icaId eq null}">
													<c:if test="${ica.isIcaDivisionWise eq 'Y'}">
														<a href="icaListBySupportAdmin?icaId=${ica.id}"
															title="Show All Divisions ICA"><i
															class="fa fa-users fa-lg"></i></a>
													</c:if>
												</c:if>

												<c:if test="${ica.flagTcs eq 'F'}">
												<a href="updateIcaDateBySupportAdminForm?icaId=${ica.id}"
													title="Update Date"><i
													class="fas fa-edit fa-lg text-primary"></i></a>
												</c:if>
												<c:if test="${ica.flagTcs eq 'S'}">Data Sent</c:if>	
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