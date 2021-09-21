<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<jsp:include page="../common/newDashboardHeader.jsp" />

<div class="d-flex" id="lmsVariableListSupportAdmin">
	<!-- DASHBOARD BODY STARTS HERE -->
	<div class="container-fluid m-0 p-0 dashboardWraper">
		<sec:authorize access="hasRole('ROLE_SUPPORT_ADMIN')">
		<header class="container-fluid sticky-top">
			<nav class="navbar navbar-expand-lg navbar-light p-0">
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
							data-placement="bottom" title="Homepage"><a
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
					<li class="breadcrumb-item active" aria-current="page">LMS Variables
						List</li>
				</ol>
			</nav>

			<jsp:include page="../common/alert.jsp" />

			<!-- Input Form Panel -->
			<div class="card bg-white border">
				<div class="card-body">
					<h5 class="text-center pb-2 border-bottom">LMS Variables List</h5>

					<div class="table-responsive mt-3 mb-3 testAssignTable">
						<table class="table table-striped table-hover">
							<thead>
								<tr>
									<th scope="col">Id</th>
									<th scope="col">Keyword</th>
									<th scope="col">Value</th>
									<th scope="col">Active</th>
									<th scope="col">Actions</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="lmsVariable" items="${lmsVariablesList}" varStatus="status">
									<tr>
										<td><c:out value="${lmsVariable.id}" /></td>
										<td><c:out value="${lmsVariable.keyword}" /></td>
										<td><c:out value="${lmsVariable.value}" /></td>
										<td><c:out value="${lmsVariable.active}" /></td>
										<td>
											<sec:authorize access="hasRole('ROLE_SUPPORT_ADMIN') ">
												<a href="updateLmsVariableBySupportAdminForm?lmsVariableId=${lmsVariable.id}"
													title="Update Variable"><i
													class="fas fa-edit fa-lg text-primary"></i></a>
											</sec:authorize>
										</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
			</div>
			<!-- /page content: END -->
		</div>
	</div>
</div>
		<jsp:include page="../common/newAdminFooter.jsp" />
