<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>


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
							data-placement="bottom" title="Update Marks"><a
							class="nav-link" href="${pageContext.request.contextPath}/uploadTeeMarksChangeForm"><i class="fab fa-readme"></i>
								<span>Update Marks</span></a></li>
						<li id="program" class="nav-item active" data-toggle="tooltip"
							data-placement="bottom" title="Home"><a
							class="nav-link" href="homepage"><i class="fas fa-home"></i>
								<span>Home</span></a></li>
					</ul>
				</div>
			</nav>
		</header>
		<div class="col-lg-12 col-md-12 col-sm-12 dashboardBody p-5">
                        
                        	<!-- page content: START -->

						<jsp:include page="../common/alert.jsp" />

						<!-- Input Form Panel -->
						<div class="card bg-white border">
							<div class="card-body">

										<h5 class="border-bottom text-center pb-2">Update TEE Marks</h5>
										
										<%-- <c:if test="${showProceed}">
										<div class="col-12 text-right">
											<a href="addStudentFeedbackForm"><i
												class="btn btn-large btn-primary">Proceed
													to allocate students</i></a>
													</div>
										</c:if> --%>

										<form:form action="uploadTeeMarksChangeTemplate" method="post"
											 enctype="multipart/form-data">
											
											<div class="row">
												
												<div class="col-12">
													<div class="form-group">
														<label for="file">Upload
															File(Excel):</label> <input id="file" name="file" type="file"
															class="form-control" />
													</div>
												</div>
												
													<div class="col-12">
														<div class="form-group">
															<label class="control-label" for="courses">Excel
																Format: </label>
															<p>TEE ID | SAPID | Total Marks | REMARKS</p>
															<br>
															<p>
																<a class="text-danger" href="downloadTeeMarksChangeTemplate">Download a template</a>
															</p>
														</div>
													</div>
										
										
												</div>
												<div class="row">

													<div class="col-sm-12 column">
														<div class="form-group">

															<button id="submit" class="btn btn-large btn-success"
																formaction="uploadTeeMarksChangeTemplate">Upload</button>

															<button id="cancel" class="btn btn-danger"
																formaction="homepage" formnovalidate="formnovalidate">Cancel</button>
														</div>
													</div>

												</div>
										</form:form>
							</div>
						</div>

			<!-- /page content: END -->
                     
                   
        </div>

		<!-- SIDEBAR START -->

		<!-- SIDEBAR END -->

		<jsp:include page="../common/newAdminFooter.jsp" />
		
		
		
		
		
		