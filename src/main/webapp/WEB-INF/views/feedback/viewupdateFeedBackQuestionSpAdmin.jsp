<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<jsp:include page="../common/newDashboardHeader.jsp" />
<div class="d-flex" id="facultyAssignmentPage">
	<!-- DASHBOARD BODY STARTS HERE -->
	<div class="container-fluid m-0 p-0 dashboardWraper">
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

						<li id="program" class="nav-item active" data-toggle="tooltip"
							data-placement="bottom" title="My Program"><a
							class="nav-link" href="homepage"><i class="fas fa-home"></i>
								<span>Home</span></a></li>
					</ul>
				</div>
			</nav>
		</header>

		<!-- SEMESTER CONTENT -->
		<div class="col-lg-12 col-md-12 col-sm-12 dashboardBody p-5">

			<!-- page content: START -->
			<nav aria-label="breadcrumb">
				<ol class="breadcrumb">
					<li class="breadcrumb-item"><a
						href="${pageContext.request.contextPath}/homepage">Dashboard</a></li>
					<li class="breadcrumb-item active" aria-current="page">Student Master</li>
				</ol>
			</nav>
			<jsp:include page="../common/alert.jsp" />

			<!-- Input Form Panel -->
				<div class="card bg-white border">
				<div class="card-body">
					<h5 class="text-left pb-2 border-bottom">Search Feedback</h5>
					<form:form action="updateFeedBackQuestion" method="post" modelAttribute="FeedbackQuestion">
					<form:input path="id" type="hidden"/>
						<div class="row">
							<div class="col-md-8">
								<div class="form-group">
									<form:label path="description"
										for="exampleFormControlTextarea1">description<span
											style="color: red">*</span>
									</form:label>
									<form:textarea path="description"
										class="form-control rounded-0"
										id="exampleFormControlTextarea1" rows="3"></form:textarea>
								</div>
							</div>

							<div class="col-sm-12 column">
								<div class="form-group">
									<button id="submit" name="submit"
										class="btn btn-large btn-primary rounded-0">Update</button>
									<!-- <input id="reset" type="reset" class="btn btn-danger"> -->
									<button id="cancel" name="cancel"
										class="btn btn-danger rounded-0" formaction="homepage"
										formnovalidate="formnovalidate">Cancel</button>
								</div>
							</div>
						</div>
					</form:form>
				</div>
				</div>
				
				
		</div>
		<jsp:include page="../common/newAdminFooter.jsp" />
