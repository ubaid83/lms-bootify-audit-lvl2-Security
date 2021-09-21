
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
							data-placement="bottom" title="My Program"><a
							class="nav-link" href="homepage"><i class="fas fa-home"></i>
								<span>Home</span></a></li>

					</ul>
				</div>
			</nav>
		</header>

		<!-- SEMESTER CONTENT -->
		

			<!-- page content: START -->
			

			<jsp:include page="../common/alert.jsp" />

			<!-- Input Form Panel -->
			

<div class="col-lg-04 col-md-9 col-sm-12 dashboardBody ml-auto mr-auto">
                        
                      <!-- page content: START -->
                      
                      <nav aria-label="breadcrumb">
                            <ol class="breadcrumb">
                                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/homepage">Dashboard</a></li>
                                <li class="breadcrumb-item" aria-current="page"><c:out value="${Program_Name}" /></li>
                                <sec:authorize access="hasRole('ROLE_STUDENT')">
								<c:out value="${AcadSession}" />
							</sec:authorize>
                                <li class="breadcrumb-item active" aria-current="page"> Upload  Lor Users </li>
                            </ol>
                        </nav>

						<jsp:include page="../common/alert.jsp" />
						<!-- Input Form Panel -->
						<div class="card bg-white border">
							<div class="card-body">
									
									
								
								
										<div class="modal-content">
											<div class="modal-header">
												<h5 class="modal-title" id="uploadmarkModal">Upload Mark Files</h5>
												<button type="button" class="close" data-dismiss="modal"
													aria-label="Close">
													<span aria-hidden="true">&times;</span>
												</button>
											</div>
											<form:form action="uploadStudentAssignmentMarksExcel" id="uploadStudentAssignmentMarksExcel"
													method="post" enctype="multipart/form-data" >
											<div class="modal-body">
													<div class="row">
														<div class="col-6">
															<h6>Upload file</h6>
															<input type="file" name="file" class="form-control-file mb-1" id="file" required="required" >
															<p><Strong>Note:</Strong> Upload Marks For students</p>
														<a href="downloadUserUploadtemplate"><i class="text-danger">Download Template</i></a>
														
														
														
														</div>
														
													</div>
													<div class="row">
														<div class="col-12 mt-3" id="subDisplay"></div>
													</div>
											</div>
											<div class="modal-footer">
												<button type="button" class="btn btn-modalClose"
													data-dismiss="modal">Close</button>
												<button id="uploadusersForLor" class="btn btn-modalSub" name="submitRemarkFiles" 
														formaction="uploadusersForLor">Submit</button>
											</div>
											</form:form>
										</div>
									
									</div>
							</div>
						
							
						
					
			<!-- /page content: END -->

		</div>

		<!-- SIDEBAR START -->

		<!-- SIDEBAR END -->

		<jsp:include page="../common/newAdminFooter.jsp" />

	