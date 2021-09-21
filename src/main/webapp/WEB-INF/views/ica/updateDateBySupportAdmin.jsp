<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>


<jsp:include page="../common/newDashboardHeader.jsp" />

<div class="d-flex" id="facultyAssignmentPage">



	<!-- DASHBOARD BODY STARTS HERE -->
	<sec:authorize access="hasRole('ROLE_ADMIN')">
		<jsp:include page="../common/newAdminLeftNavBar.jsp" />
	</sec:authorize>
		
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
					
					
					<li class="breadcrumb-item active" aria-current="page">
						 Update ICA End Date and Publish Date</li>
				
				</ol>
			</nav>

			<jsp:include page="../common/alert.jsp" />

			<!-- Input Form Panel -->
			<div class="card bg-white border">
				<div class="card-body">
					<h5 class="text-center pb-2 border-bottom">
						
						 Update ICA End Date and Publish Date with Submit Null and isPublished Null
						
					</h5>

				<span class="text-danger">Note:</span>
				<ul>
				<li><span class="font-weight-bold">Update End Date with isSubmit Null :</span><br> 
				After updating enddate of the ica Assign faculty need to submit the ica again.
				</li>
				<li> <span class="font-weight-bold">Update Publish Date with isPublish Null :</span><br>
				Affter updating published date admin need to publish the ica again.
				</li>
				</ul>


					<form:form action="updateIcaDateBySupportAdmin" method="post" modelAttribute="icaBean">
						<form:input type="hidden" path="id" value="${icaId}" />
						
				
						<div class="row">
							
							<div class="col-md-4 col-sm-6 col-xs-12 column">
								<label class="small"><strong> Update End Date with isSubmit Null</strong> <span
									class="text-danger f-13">*</span></label>

								<div class="input-group">
									<form:input id="endDateIca" path="endDate" type="text"
										placeholder="End Date" class="form-control"
										 readonly="true" />
									<div class="input-group-append">
										<button class="btn btn-outline-secondary initiateEDatePicker"
											type="button">
											<i class="fas fa-calendar"></i>
										</button>
									</div>
								</div>
							</div>
							
							<div class="col-md-4 col-sm-6 col-xs-12 column">
								<label class="small"><strong> Update Publish Date with isPublish Null</strong> <span
									class="text-danger f-13">*</span></label>

								<div class="input-group">
									<form:input path="publishedDate" type="text"
										 placeholder="Publish Date" class="form-control"
										  />
									<div class="input-group-append">
										<button class="btn btn-outline-secondary initiatePublishedDate"
											type="button">
											<i class="fas fa-calendar"></i>
										</button>
									</div>
								</div>
							</div>
						</div>
					
						<div class="col-sm-12 mt-3 column">
							<div class="form-group">
							
								<button id="submit" name="submit"
									class="btn btn-large btn-primary" formaction="updateIcaDateBySupportAdmin">Update</button>
								
								<button id="cancel" name="cancel" class="btn btn-danger"
									formnovalidate="formnovalidate" formaction="homepage">Cancel</button>
							</div>
						</div>



					</form:form>
				</div>
			</div>
			<div class="card bg-white border">
				<div class="card-body">
					<h5 class="text-center pb-2 border-bottom">
						
						 Update ICA Only End Date and Publish Date
						
					</h5>
				<span class="text-danger">Note:</span>
				<p>
				<ul>
				<li><span class="font-weight-bold">Update End Date :</span><br> 
				Submission status of ICA remains same only end date will update.</li>
				<li>
				<span class="font-weight-bold">Update publish Date :</span><br> 
				If Ica is published then only published date will be update.</li>
				</ul>
					<form:form action="updateIcaDateBySupportAdminWithoutSubmit" method="post" modelAttribute="icaBeanS">
						<form:input type="hidden" path="id" value="${icaId}" />
						
				
						<div class="row">
							
							<div class="col-md-4 col-sm-6 col-xs-12 column">
								<label class="small"><strong> Update End Date</strong> <span
									class="text-danger f-13">*</span></label>

								<div class="input-group">
									<form:input id="endDateIca" path="endDate" type="text"
										placeholder="End Date" class="form-control"
										 readonly="true" />
									<div class="input-group-append">
										<button class="btn btn-outline-secondary initiateEDatePicker"
											type="button">
											<i class="fas fa-calendar"></i>
										</button>
									</div>
								</div>
							</div>
							
							<div class="col-md-4 col-sm-6 col-xs-12 column">
								<label class="small"><strong> Update Publish Date</strong> <span
									class="text-danger f-13">*</span></label>

								<div class="input-group">
									<form:input id="publishedDateS" path="publishedDate" type="text"
										placeholder="Publish Date"  class="form-control" 
										/>
									<div class="input-group-append">
										<button class="btn btn-outline-secondary initiatePublishedDateS"
											type="button">
											<i class="fas fa-calendar"></i>
										</button>
									</div>
								</div>
							</div>
						</div>
					
						<div class="col-sm-12 mt-3 column">
							<div class="form-group">
							
								<button id="submit" name="submit"
									class="btn btn-large btn-primary" formaction="updateIcaDateBySupportAdminWithoutSubmit">Update</button>
								
								<button id="cancel" name="cancel" class="btn btn-danger"
									formnovalidate="formnovalidate" formaction="homepage">Cancel</button>
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
		<script>
			$(document).ready(function() {
				$('#moduleId').select2();
				if ($('#scaledReq').val() == "N") {
					$('#scaledReq').attr('checked', false);
				}

			});
		</script>
		
		<script>
			$(window)
					.bind(
							"pageshow",
							function() {
								var TommorowDate = new Date();
								//end Date picker

								$('.initiateEDatePicker').daterangepicker({
									autoUpdateInput : false,
									minDate : TommorowDate,
									locale : {
										cancelLabel : 'Clear'
									},
									"singleDatePicker" : true,
									"showDropdowns" : true,
									"timePicker" : true,
									"showCustomRangeLabel" : false,
									"alwaysShowCalendars" : true,
									"opens" : "center"
								});

								$('.initiateEDatePicker')
										.on(
												'apply.daterangepicker',
												function(ev, picker) {
													//validDateTimepicks();
													var fromDate = $(
															'#startDateIca')
															.val();
													var toDate = $(
															'#endDateIca')
															.val();
													var eDate = new Date(
															toDate);
													var sDate = new Date(fromDate);
													console
															.log('validate called'
																	+ sDate
																	+ ','
																	+ eDate);
													if (sDate > eDate) {
														alert("endate cannot be smaller than startDate");
														$('#startDateIca')
																.val(
																		$(
																				'#startDateDB')
																				.val());
														$('#endDateIca').val(
																$('#endDateDB')
																		.val());
													} else {
														$(this)
																.parent()
																.parent()
																.find('input')
																.val(
																		picker.startDate
																				.format('YYYY-MM-DD HH:mm:ss'));
													}
												});

								$('.initiateEDatePicker').on(
										'cancel.daterangepicker',
										function(ev, picker) {
											$(this).parent().parent().find(
													'input').val(
													$('#endDateDB').val());

										});
								
								
							//INITIATE PUBLISH DATE
								var publishedDate = "";
								$('.initiatePublishedDate').daterangepicker({
								    "singleDatePicker": true,
								    autoUpdateInput : false,
								    minDate : TommorowDate,
								    "showDropdowns" : true,
								    "alwaysShowCalendars" : true,
									"opens" : "center"
								    
								}, function(start, end, label) {
									publishedDate =  end.format('YYYY-MM-DD');
								  	$("#publishedDate").val(publishedDate);
								  	console.log(end.format(publishedDate));
								});
								
							//Without submit
							
								var publishedDateS = "";
								$('.initiatePublishedDateS').daterangepicker({
								    "singleDatePicker": true,
								    autoUpdateInput : false,
								    minDate : TommorowDate,
								    "showDropdowns" : true,
								    "alwaysShowCalendars" : true,
									"opens" : "center"
								    
								}, function(start, end, label) {
									publishedDateS =  end.format('YYYY-MM-DD');
								  	$("#publishedDateS").val(publishedDateS);
								  	console.log(end.format(publishedDateS));
								});
							
								
								


							});
		</script>
		
		
		
		
		