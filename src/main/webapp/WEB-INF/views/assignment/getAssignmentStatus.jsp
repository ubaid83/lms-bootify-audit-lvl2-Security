<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%
	boolean isEdit = "true".equals((String) request
			.getAttribute("edit"));
%>


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
					<li class="breadcrumb-item" aria-current="page"><c:out
							value="${Program_Name}" /></li>
				</ol>
			</nav>

			<jsp:include page="../common/alert.jsp" />

			<!-- Input Form Panel -->
			
			<div class="card bg-white border">
							<div class="card-body">


<form:form  modelAttribute="assignment" method="post" >
  
  
  <div class="form-row">
    
    
    						<input type="hidden" value="" id="startDateDB" />
						<input type="hidden" value="" id="endDateDB" />
    <%-- <div class="row">
    
							<div class="col-md-6 col-sm-12">
							<input type="hidden" value="" id="startDateDB" />
						<input type="hidden" value="" id="endDateDB" />
													<div class="form-group">

														<label path="" for="startDate">From Date <span
															style="color: red">*</span></label>

														<div class='input-group date' id='datetimepicker1'>
															<form:input path="startDate" id="startDate" type="text" placeholder="From Date"
																class="form-control" required="required" readonly="true" />
															<div class="input-group-append">
																<button class="btn btn-outline-secondary startDate"
																	type="button">
																	<i class="fas fa-calendar"></i>
																</button>
															</div>
														</div>

													</div>
												</div>

												<div class="col-md-6 col-sm-12">
													<div class="form-group">

														<label path="endDate" for="endDate">To Date <span
															style="color: red">*</span></label>

														<div class='input-group date' id='datetimepicker2'>
															<form:input path="endDate" id="endDate" type="text" placeholder="To Date"
																class="form-control" required="required" readonly="true" />
															<div class="input-group-append">
																<button class="btn btn-outline-secondary endDate"
																	type="button">
																	<i class="fas fa-calendar"></i>
																</button>
															</div>
														</div>

													</div>
												</div>
						</div> --%>
						<div class="col-md-4 col-sm-12">
								<label class="small"><strong>Start Date</strong> <span
									class="text-danger f-13">*</span></label>
								<div class="input-group">
									<form:input id="startDateIca" path="startDate" type="text"
										placeholder="Start Date" class="form-control"
										required="required" readonly="true" />
									<div class="input-group-append">
										<button class="btn btn-outline-secondary initiateSDatePicker"
											type="button">
											<i class="fas fa-calendar"></i>
										</button>
									</div>
								</div>
							</div>
							<div class="col-md-4 col-sm-12">
								<label class="small"><strong>End Date</strong> <span
									class="text-danger f-13">*</span></label>

								<div class="input-group">
									<form:input id="endDateIca" path="endDate" type="text"
										placeholder="End Date" class="form-control"
										required="required" readonly="true" />
									<div class="input-group-append">
										<button class="btn btn-outline-secondary initiateEDatePicker"
											type="button">
											<i class="fas fa-calendar"></i>
										</button>
									</div>
								</div>
							</div>
    

 </div>
  <br><br>
  <button type="submit"  formaction="getAssignmentCount" class="btn btn-primary">Get Assignment Count </button>
  <button id="cancel" name="cancel" class="btn btn-danger" formaction="homepage"
															formnovalidate="formnovalidate">Cancel</button>
															
															
	
</form:form>
<br><br>



						

				<!-- Input Form Panel -->
				<div class="card bg-white border">
						
<div class="table-responsive mt-3 mb-3 testAssignTable">

<table class="table table-hover" id="POITable">
															<thead>
																<tr>
																	
																	<th>Assignment Id</th>
																	<th>Faculty Name</th>
																	<th>Course Name</th>
																	
																	<th>Start Time</th>
																	<th>End Time</th>
																	<th>Over all Count </th>
																	<th>Completed Count</th>						
																	<th>lateSubmittedCount</th>
																	<th>Assignment Name</th>
																	<th>Action</th>
																</tr>
															</thead>
														
															<tbody>

																<c:forEach var="allAssignment" items="${allAssignment}"
																	varStatus="status">
																	<tr>
																	<td>${allAssignment.id}</td>
																	<td>${allAssignment.facultyName}</td>
																	<td>${allAssignment.courseName}</td>
																	<td>${allAssignment.startDate}</td>
																	<td>${allAssignment.endDate}</td>
																	<td>${allAssignment.countOverall}</td>
																	<td>${allAssignment.completedCount}</td>
																	<td><a href = "getLateSubmittedDetails?id=${allAssignment.id}">${allAssignment.lateSubmittedCount}</a></td>
																	<td>${allAssignment.assignmentName}</td>	
																<td><a target="_blank" href="getStudentDetails?id=${allAssignment.id}" title="studentDetails"> 
											<i class='fas fa-address-card'> </i></a>
											<a href="${pageContext.request.contextPath}/editAssignment?id=${allAssignment.id}"  class="fas fa-edit fa-lg text-primary"></a>
											</td>
																			
																	</tr>
																</c:forEach>
															</tbody>
														</table>
</div>

		
							</div>
							</div>
							</div>
			
			<!-- /page content: END -->

		</div>

		<!-- SIDEBAR START -->

		<!-- SIDEBAR END -->

		<jsp:include page="../common/newAdminFooter.jsp" />
		
		
		<!-- <script>
			$(window)
					.bind(
							"pageshow",
							function() {
								
								$("#startDate").val('');
								$('#endDate').val('');

								$('#startDate').daterangepicker({
									autoUpdateInput : false,
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

								$('#startDate')
										.on(
												'apply.daterangepicker',
												function(ev, picker) {
													$(this)
															.val(
																	picker.startDate
																			.format('YYYY-MM-DD HH:mm:ss'));
																										
												});

								$('#startDate').on('cancel.daterangepicker',
										function(ev, picker) {
											$(this).val('');
										});

								$('#endDate').daterangepicker({
									autoUpdateInput : false,
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

								$('#endDate')
										.on(
												'apply.daterangepicker',
												function(ev, picker) {
													$(this)
													.val(
															picker.startDate
																	.format('YYYY-MM-DD HH:mm:ss'));
													var fromDate = $('#startDate').val();
													var toDate = $('#endDate').val();
													
													//var sDate = new Date($('#startDate').val());
													//var eDate = new Date($('#endDate').val());
													//var eDate = new Date($(this).val());
													console
															.log('validate called'
																	+ fromDate
																	+ ', '
																	+ toDate);
													//alert(fromDate);
													//alert(toDate);
													
													if(fromDate >= toDate){
														$(this).val('');
														alert('End Date should be greater than start Date');
													}
													
												});

								$('#endDate').on('cancel.daterangepicker',
										function(ev, picker) {
											$(this).val('');
										});

							});
		</script> -->
		
		<script>
			$(window)
					.bind(
							"pageshow",
							function() {
								var TommorowDate = new Date();
								//start Date picker
								$('.initiateSDatePicker').daterangepicker({
									autoUpdateInput : false,
									//minDate : TommorowDate,
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

								$('.initiateSDatePicker')
										.on(
												'apply.daterangepicker',
												function(ev, picker) {
													$(this)
													.parent()
													.parent()
													.find('input')
													.val(
															picker.startDate
																	.format('YYYY-MM-DD HH:mm:ss'));
													//validDateTimepicks();
													var fromDate = $(
															'#startDateIca')
															.val();
													var toDate = $(
															'#endDateIca')
															.val();
													var sDate = new Date(
															fromDate);
													var eDate = new Date(toDate);
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

								$('.initiateSDatePicker').on(
										'cancel.daterangepicker',
										function(ev, picker) {
											$(this).parent().parent().find(
													'input').val(
													$('#startDateDB').val());

										});

								//end Date picker

								$('.initiateEDatePicker').daterangepicker({
									autoUpdateInput : false,
									//minDate : TommorowDate,
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
													$(this)
													.parent()
													.parent()
													.find('input')
													.val(
															picker.startDate
																	.format('YYYY-MM-DD HH:mm:ss'));
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
															.log('validate called end'
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

								/* 	function validDateTimepicks() {
										if (($('#startDateIca').val() != '' && $('#startDateIca').val() != null)
												&& ($('#endDateIca').val() != '' && $('#endDateIca').val() != null)) {
											var fromDate = $('#startDateIca').val();
											var toDate = $('#endDateIca').val();
											var eDate = new Date(fromDate);
											var sDate = new Date(toDate);
											console.log('validate called'+ sDate+','+eDate);
											if (sDate < eDate) {
												alert("endate cannot be smaller than startDate");
												$('#startDateIca').val($('#startDateDB').val());
												$('#endDateIca').val($('#endDateDB').val());
											}
										}
									} */

							});
		</script>
		
		
		