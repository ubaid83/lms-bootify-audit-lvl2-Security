<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<jsp:include page="../common/newDashboardHeader.jsp" />

<div class="d-flex" id="updateLmsVariableBySupportAdmin">
	<div class="container-fluid m-0 p-0 dashboardWraper">
		<sec:authorize access="hasRole('ROLE_SUPPORT_ADMIN')">
		<header class="container-fluid sticky-top">
			<nav class="navbar navbar-expand-lg navbar-light p-0">
				<a class="navbar-brand" href="homepage"> 
					<c:choose>
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
						<li id="homepage" class="nav-item active" data-toggle="tooltip"
							data-placement="bottom" title="Homepage">
							<a class="nav-link" href="homepage"><i class="fas fa-home"></i>
								<span>Home</span>
							</a>
						</li>
					</ul>
				</div>
			</nav>
		</header>
		</sec:authorize>
		<!-- LMS Variables List -->
		<div class="col-lg-12 col-md-12 col-sm-12 dashboardBody p-5">

			<!-- page content: START -->
			<nav aria-label="breadcrumb">
				<ol class="breadcrumb">
					<li class="breadcrumb-item"><a
						href="${pageContext.request.contextPath}/homepage">Dashboard</a></li>
					<li class="breadcrumb-item active" aria-current="page">LMS Variables List</li>
				</ol>
			</nav>

			<jsp:include page="../common/alert.jsp" />

			<!-- Input Form Panel -->
			<div class="card bg-white border">
				<div class="card-body">
					<h5 class="text-center pb-2 border-bottom">Change LMS Variable</h5>
						<div class="x_panel">
							<form:form action="updateLmsVariableBySupportAdmin" method="post" modelAttribute="lmsVariable">
							<%-- <jsp:include page="../common/alert.jsp" /> --%>
								<div class="col-sm-6 col-md-4 col-xs-12 column">
									<div class="form-group">
										<span style="color: red">Keyword</span>
										<form:input type="text" path="keyword"
											class="form-control" placeholder="${lmsVariable.keyword}"
											id="keyword" disabled="true"/>
									</div>
									<div class="form-group">
										<form:input type="hidden" path="id" value="${lmsVariable.id}" />
									</div>
									<div class="form-group">
										<span style="color: red">* Enter New Value</span>
										<form:input type="text" path="value"
											cssClass="form-control" placeholder="${lmsVariable.value}"
											id="value" value="${lmsVariable.value}" />
									</div>
									<div class="form-group">
									<span style="color: red">* Active</span>
										<c:set value="${lmsVariable.active}" var="active"/>
										<c:choose>
											<c:when test="${active eq 'Y'}">
												<form:select id="active" path="active" placeholder="${lmsVariable.active}" class="form-control" required="required">
													<form:option selected="${lmsVariable.active}" value="${lmsVariable.active}">${lmsVariable.active}</form:option>
													<form:option value="N">N</form:option>
												</form:select>
											</c:when>
											<c:otherwise>
												<form:select id="active" path="active" placeholder="${lmsVariable.active}" class="form-control" required="required">
													<form:option selected="${lmsVariable.active}" value="${lmsVariable.active}">${lmsVariable.active}</form:option>
													<form:option value="Y">Y</form:option>
												</form:select>
											</c:otherwise>
										</c:choose>
									</div>
									<div class="form-group">
										<button id="submit" name="submit" class="btn btn-danger">Change</button>
										<button id="cancel" class="btn btn-danger" formaction="homepage" formnovalidate="formnovalidate">Back</button>
									</div>
								</div>
							</form:form>
						</div>
				</div>
			</div>
			<!-- /page content: END -->
		</div>
	</div>
</div>
<jsp:include page="../common/newAdminFooter.jsp" />


