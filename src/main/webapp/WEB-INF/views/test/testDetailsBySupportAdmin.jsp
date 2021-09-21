
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>



<jsp:include page="../common/newDashboardHeader.jsp" />



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
			
					<li class="breadcrumb-item active" aria-current="page">TEST
						List</li>
				</ol>
			</nav>

			<jsp:include page="../common/alert.jsp" />

			<!-- Input Form Panel -->
					<div class="card bg-white border">
						<div class="card-body">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">
									<div class="x_title">
										<h5>Test Details</h5>

									</div>
									<div class="x_content">

										

											<div class="row">
											<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
                                                        <label class="textStrong">TestID :</label>
														${finalTestList.id}
													</div>
												</div>
												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<label class="textStrong">Course :</label>
														${finalTestList.courseId}
													</div>
												</div>
												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<label class="textStrong">Academic Month :</label>
														${finalTestList.acadMonth}
													</div>
												</div>
												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<label class="textStrong">Academic Year : </label>
														${finalTestList.acadYear}
													</div>
												</div>
												<div class="col-md-4 col-sm-6 col-xs-12 column">
													
													<div class="form-group">
														<label class="textStrong" >Test Name :</label>
														${finalTestList.testName}
													</div>
												</div>
												<div class="col-md-4 col-sm-6 col-xs-12 column">
													
													<div class="form-group">
														<label class="textStrong" >Test Type :</label>
														${finalTestList.testType}
													</div>
												</div>
											<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<label class="textStrong" >Test Duration :</label>
														<c:out
															value="${fn:replace(finalTestList.duration,'T', ' ')}${' Minutes'}"></c:out>
													</div>
												</div>
												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<label class="textStrong" >Start Date :</label>
														<c:out
															value="${fn:replace(finalTestList.startDate, 'T', ' ')}"></c:out>
													</div>
												</div>
												
												
													<div class="col-md-4 col-sm-6 col-xs-12 column">
														<div class="form-group">
															<label class="textStrong" >End Date :</label>
															<c:out value="${fn:replace(finalTestList.endDate,'T', ' ')}"></c:out>
														</div> 
													</div>
													<div class="col-md-4 col-sm-6 col-xs-12 column">
														<div class="form-group">
															<label class="textStrong">Allow Submission after End date :</label>
															${finalTestList.allowAfterEndDate}

														</div>
													</div>
													<div class="col-md-4 col-sm-6 col-xs-12 column">
														<div class="form-group">
															<label class="textStrong">Random Question :</label>
															${finalTestList.randomQuestion}
														</div>
													</div>
													<div class="col-md-4 col-sm-6 col-xs-12 column">
														<div class="form-group">
															<label class="textStrong" >Max Question to show :</label>
															${finalTestList.maxQuestnToShow}
														</div>
													</div>
													<div class="col-sm-6 col-md-4 col-xs-12 column">
														<div class="form-group">
															<label class="textStrong">Same Marks Question :</label>
															${finalTestList.sameMarksQue}
														</div>
													</div>

													<div class="col-sm-6 col-md-4 col-xs-12 column">
														<div class="form-group">
															<label class="textStrong">Marks Per Question :</label>
															${finalTestList.marksPerQue}
														</div>
													</div>

													<div class="col-md-4 col-sm-6 col-xs-12 column">
														<div class="form-group">
															<label class="textStrong" >Show Results to Students immediately :</label>
															${finalTestList.showResultsToStudents}
														</div>
													</div>
													<div class="col-md-4 col-sm-6 col-xs-12 column">
														<div class="form-group">
															<label class="textStrong" >Password For Test :</label>
															${finalTestList.passwordForTest}
														</div>
													</div>
												
												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<label class="textStrong" >Score Out of :</label>
														${finalTestList.maxScore}
													</div>
												</div>
												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<label class="textStrong">Faculty : </label>
														${finalTestList.facultyId}
													</div>
												</div>
                                                <div class="col-md-4 col-sm-6 col-xs-12 column">
                                                    <div class="form-group">
                                                        <label class="textStrong">Max Attempt : </label>
                                                        ${finalTestList.maxAttempt}
                                                    </div>
                                                </div>
											</div>




										
									</div>
								</div>
							</div>
						</div>
					</div>



			<!-- /page content: END -->

		</div>

		<!-- SIDEBAR START -->

		<!-- SIDEBAR END -->

		<jsp:include page="../common/newAdminFooter.jsp" />

		