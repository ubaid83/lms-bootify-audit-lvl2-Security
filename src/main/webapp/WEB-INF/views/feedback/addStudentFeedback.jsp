<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>


<jsp:include page="../common/newDashboardHeader.jsp" />

<div class="d-flex adminPage" id="adminPage">
	<jsp:include page="../common/newAdminLeftNavBar.jsp" />
	<jsp:include page="../common/rightSidebarAdmin.jsp" />


	<!-- DASHBOARD BODY STARTS HERE -->

	<div class="container-fluid m-0 p-0 dashboardWraper">

		<jsp:include page="../common/newAdminTopHeader.jsp" />

		<!-- SEMESTER CONTENT -->
		<div class="col-lg-12 col-md-12 col-sm-12 dashboardBody p-5">

			<!-- page content: START -->

			<nav aria-label="breadcrumb">
				<ol class="breadcrumb">
					<li class="breadcrumb-item"><a
						href="${pageContext.request.contextPath}/homepage">Dashboard</a></li>
					<li class="breadcrumb-item" aria-current="page"><c:out
							value="${Program_Name}" /></li>
					<sec:authorize access="hasRole('ROLE_STUDENT')">
						<c:out value="${AcadSession}" />
					</sec:authorize>
					<li class="breadcrumb-item active" aria-current="page">
						Allocate Feedback</li>
				</ol>
			</nav>

			<jsp:include page="../common/alert.jsp" />

			<!-- Input Form Panel -->
			<div class="card bg-white border">

				<div class="card-body">
					<div class="x_panel">
						<div class="text-center pb-2 border-bottom mb-3">
							<h5>Allocate Feedback For Program</h5>
							<p class="text-danger">(Please choose the year and month when
								the module booking was done.)</p>
						</div>

						<form:form id="studentFeedbackForm" action="saveStudentFeedback"
							method="post" modelAttribute="feedback">
							<div class="row">
								<div class="col-md-4 col-sm-6">
									<div class="form-group">
										<label class="control-label" for="feedback">Feedback <span
											style="color: red">*</span></label>
										<form:select id="feedback" path="id"
											placeholder="Feedback Name"
											class="form-control feedbackdropdown" required="required">
											<form:option value="">Select Feedback</form:option>
											<form:options items="${feedbackList}"
												itemLabel="feedbackName" itemValue="id" />
										</form:select>
									</div>
								</div>
								<div class="col-md-4 col-sm-6">
									<div class="form-group">
										<form:label path="startDate" for="startDate">Start Date <span
												style="color: red">*</span>
										</form:label>

										<div class='input-group date' id='datetimpicker1'>
											<form:input id="starDProgram" path="startDate"
												type="text" placeholder="Start Date" class="form-control"
												required="required" readonly="true"/>
											<!-- <span class="input-group-addon"><span
												class="glyphicon glyphicon-calendar"></span></span> -->
												<button class="btn btn-outline-secondary iniSDatePicker"
											type="button">
											<i class="fas fa-calendar"></i>
										</button>
										</div>


									</div>
								</div>
								<div class="col-md-4 col-sm-6">
									<div class="form-group">
										<form:label path="endDate" for="endDate">End Date <span
												style="color: red">*</span>
										</form:label>

										<div class='input-group date' id='datetimepicker2'>
											<form:input id="enDProgram" path="endDate" type="text"
												placeholder="End Date" class="form-control"
												required="required" readonly="true" />
											<!-- <span class="input-group-addon"><span
												class="glyphicon glyphicon-calendar"></span> </span> -->
												<button class="btn btn-outline-secondary iniSDatePicker"
											type="button">
											<i class="fas fa-calendar"></i>
										</button>
										</div>

									</div>
								</div>

								<div class="col-md-4 col-sm-6">
									<div class="form-group">
										<form:label path="campusId" for="campusId">Select Campus</form:label>

										<form:select id="campusId" path="campusId" type="text"
											placeholder="campus" class="form-control">
											<form:option value="">Select Campus</form:option>
											<c:forEach var="campus" items="${campusListByProgram}"
												varStatus="status">
												<form:option value="${campus.campusId}">${campus.campusName}</form:option>
											</c:forEach>
										</form:select>
									</div>
								</div>
								<div class="col-md-4 col-sm-6">
									<div class="form-group">
										<label class="control-label" for="course">Select Acad
											Year <span style="color: red">*</span>
										</label>
										<form:select id="acadYear" path="acadYear"
											placeholder="acad Year" class="form-control facultyparameter"
											required="required">
											<form:option value="">Select Acad Year</form:option>
											<form:options items="${yearList}" />
											<%-- <form:option value="2017">2017</form:option>
															<form:option value="2018">2018</form:option> --%>
										</form:select>
									</div>
								</div>

								<div class="col-md-4 col-sm-6">
									<div class="form-group">
										<label class="control-label" for="acadMonth">Select
											Acad Month <span style="color: red">*</span>
										</label>
										<form:select id="acadMonthForAcadSession" path="acadMonth"
											placeholder="Acad Month"
											class="form-control facultyparameter" required="required">
											<form:option value="">Select Acad Month</form:option>
											<form:options items="${acadMonths}" />
										</form:select>
									</div>
								</div>
								<div class="col-md-4 col-sm-6">
									<div class="form-group">

										<!-- <button id="search" class="btn btn-large btn-primary"
															formaction="addStudentFeedbackForm">Search</button> -->

										<button id="allocateFeedbackByProgram"
											class="btn btn-large btn-success"
											formaction="saveStudentFeedbackForProgram">Allocate
											To Program</button>

										<button id="cancel" class="btn btn-danger"
											formaction="homepage" formnovalidate="formnovalidate">Cancel</button>
									</div>
								</div>
							</div>
						</form:form>
					</div>
				</div>
			</div>



			<!-- allocation by semester  -->
			<div class="card bg-white border">
				<div class="col-xs-12 col-sm-12">
					<div class="x_panel">
						<div class="text-center pb-2 border-bottom mb-3 pt-3">
							<h5>Allocate Feedback To Semester</h5>
							<p class="text-danger">(Please choose the year and month when
								the module booking was done.)</p>
						</div>
						<form:form id="studentFeedbackForm" action="saveStudentFeedback"
							method="post" modelAttribute="feedback">
							<div class="x_content">

								<div class="row">
									<div class="col-md-4 col-sm-6 col-xs-12 column">
										<div class="form-group">
											<label class="control-label" for="feedback">Feedback
												<span style="color: red">*</span>
											</label>
											<form:select id="feedbackAcadSession" path="id"
												placeholder="Feedback Name"
												class="form-control feedbackdropdownAcadSession"
												required="required">
												<form:option value="">Select Feedback</form:option>
												<form:options items="${feedbackList}"
													itemLabel="feedbackName" itemValue="id" />
											</form:select>
										</div>
									</div>
									<div class="col-md-4 col-sm-6 col-xs-12 column">
										<div class="form-group">
											<label class="control-label" for="course">acadYear <span
												style="color: red">*</span></label>
											<form:select id="acadYearForAcadSession" path="acadYear"
												placeholder="acad Year"
												class="form-control facultyparameter" required="required">
												<form:option value="">Select Acad Year</form:option>
												<form:options items="${yearList}" />
												<%-- <form:option value="2017">2017</form:option>
															<form:option value="2018">2018</form:option> --%>
											</form:select>
										</div>
									</div>

									<div class="col-md-4 col-sm-6 col-xs-12 column">
										<div class="form-group">
											<label class="control-label" for="acadMonth">acadMonth
												<span style="color: red">*</span>
											</label>
											<form:select id="acadMonthForAcadSession" path="acadMonth"
												placeholder="Acad Month"
												class="form-control facultyparameter" required="required">
												<form:option value="">Select Acad Month</form:option>
												<form:options items="${acadMonths}" />
											</form:select>
										</div>
									</div>


									<div class="col-md-4 col-sm-6 col-xs-12 column">
										<div class="form-group">
											<form:label path="acadSession" for="acadSession">Semester</form:label>
											<form:select id="acadSessionId" path="acadSession"
												class="form-control">

												<form:option value="">Select Semester</form:option>

												<c:if test="${not empty feedback.acadSession }">
													<form:option value="${feedback.acadSession }">${feedback.acadSession }</form:option>
												</c:if>
											</form:select>
										</div>
									</div>

									<div class="col-md-4 col-sm-6 col-xs-12 column">
										<div class="form-group">
											<form:label path="startDate" for="startDate">Start Date <span
													style="color: red">*</span>
											</form:label>

											<div class='input-group date' id='datetimepicker3'>
												<form:input id="startDatAcadSession" path="startDate"
													type="text" placeholder="Start Date" class="form-control"
													required="required" readonly="true" />
												<!-- <span class="input-group-addon"><span
													class="glyphicon glyphicon-calendar"></span> </span> -->
													<button class="btn btn-outline-secondary iniSDatePickerForSem"
														type="button">
														<i class="fas fa-calendar"></i>
													</button>
											</div>


										</div>
									</div>
									<div class="col-md-4 col-sm-6 col-xs-12 column">
										<div class="form-group">
											<form:label path="endDate" for="endDate">End Date <span
													style="color: red">*</span>
											</form:label>

											<div class='input-group date' id='datetimepicker4'>
												<form:input id="endDatAcadSession" path="endDate"
													type="text" placeholder="End Date" class="form-control"
													required="required" readonly="true" />
												<!-- <span class="input-group-addon"><span
													class="glyphicon glyphicon-calendar"></span> </span> -->
													<button class="btn btn-outline-secondary iniEDatePickerForSem"
														type="button">
														<i class="fas fa-calendar"></i>
													</button>
											</div>

										</div>
									</div>


									<div class="col-md-4 col-sm-6 col-xs-12 column">
										<div class="form-group">
											<form:label path="campusId" for="campusId">Select Campus</form:label>

											<form:select id="campusId" path="campusId" type="text"
												placeholder="campus" class="form-control">
												<form:option value="">Select Campus</form:option>
												<c:forEach var="campus" items="${campusListByProgram}"
													varStatus="status">
													<form:option value="${campus.campusId}">${campus.campusName}</form:option>
												</c:forEach>
											</form:select>
										</div>
									</div>
								</div>
								<div class="row">
									<div class="col-md-4 col-sm-6 col-xs-12 column">
										<div class="form-group">

											<!-- <button id="search" class="btn btn-large btn-primary"
															formaction="addStudentFeedbackForm">Search</button> -->

											<button id="allocateFeedbackByAcadSession"
												class="btn btn-large btn-success"
												formaction="saveStudentFeedbackForAcadSession">Allocate
												to Acad Session</button>

											<button id="cancel" class="btn btn-danger"
												formaction="homepage" formnovalidate="formnovalidate">Cancel</button>
										</div>
									</div>
								</div>

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
		<%-- 	<script type="text/javascript"
		src="<c:url value="/resources/js/customDateTimePicker.js" />"></script> --%>
		<!-- Peter Start 07/12/2021-->
		<script>
			$(window)
					.bind(
							"pageshow",
							function() {
								var TommorowDate = new Date();
								//start Date picker
								$('.iniSDatePicker, .iniSDatePickerForSem').daterangepicker({
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

								$(".iniSDatePicker, .iniSDatePickerForSem")
										.on(
												'apply.daterangepicker',
												function(ev, picker) {
													//validDateTimepicks();
													var fromDate = $(
															'#starDProgram')
															.val();
													var toDate = $(
															'#enDProgram')
															.val();
													var sDate = new Date(
															fromDate);
													var eDate = new Date(toDate);
													console.log('validate called'
																	+ sDate
																	+ ','
																	+ eDate);
													if (sDate > eDate) {
														alert("endate cannot be smaller than startDate");
														$('#startDateTimeTable')
																.val(
																		$(
																				'#startDateDB')
																				.val());
														$('#endDateTimeTable').val(
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

								$('.iniSDatePicker .iniSDatePickerForSem').on(
										'cancel.daterangepicker',
										function(ev, picker) {
											$(this).parent().parent().find(
													'input').val(
													$('#startDateDB').val());

										});

								//end Date picker

								$('.iniEDatePicker, .iniEDatePickerForSem').daterangepicker({
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

								$('.iniEDatePicker, .iniEDatePickerForSem')
										.on(
												'apply.daterangepicker',
												function(ev, picker) {
													//validDateTimepicks();
													var fromDate = $(
															'#starDProgram')
															.val();
													var toDate = $(
															'#enDProgram')
															.val();
													var eDate = new Date(
															fromDate);
													var sDate = new Date(toDate);
													console
															.log('validate called'
																	+ sDate
																	+ ','
																	+ eDate);
													/* if (sDate > eDate) {
														alert("endate cannot be smaller than startDate");
														$('#startDateTimeTable')
																.val(
																		$(
																				'#startDateDB')
																				.val());
														$('#endDateTimeTable').val(
																$('#endDateDB')
																		.val());
													} else { */
														$(this)
																.parent()
																.parent()
																.find('input')
																.val(
																		picker.startDate
																				.format('YYYY-MM-DD HH:mm:ss'));
													//}
												});

								$('.iniEDatePicker, .iniEDatePickerForSem').on(
										'cancel.daterangepicker',
										function(ev, picker) {
											$(this).parent().parent().find(
													'input').val(
													$('#endDateDB').val());

										});

							});
		</script>
		<!-- Peter End 07/12/2021-->

		<script>
			$(document)
					.ready(
							function() {

								$(".feedbackdropdown")
										.on(
												'change',
												function() {
													var feedbackId = $(
															'#feedback').val();
													if (feedbackId) {

														$
																.ajax({
																	type : 'GET',
																	url : '${pageContext.request.contextPath}/StudentFeedbackAllocationCount?'
																			+ 'feedbackId='
																			+ feedbackId,
																	success : function(
																			data) {

																		if (data != 0) {
																			$(
																					'#allocateFeedbackByProgram')
																					.attr(
																							'disabled',
																							true);

																		} else {
																			$(
																					'#allocateFeedbackByProgram')
																					.attr(
																							'disabled',
																							false);

																		}

																	}
																});

													}

												})

								$("#feedbackAcadSession")
										.on(
												'change',
												function() {

													var feedbackId = $(
															'#feedbackAcadSession')
															.val();
													if (feedbackId) {

														$
																.ajax({
																	type : 'GET',
																	url : '${pageContext.request.contextPath}/StudentFeedbackAllocationCount?'
																			+ 'feedbackId='
																			+ feedbackId,
																	success : function(
																			data) {

																		if (data != 0) {

																			$(
																					'#allocateFeedbackByAcadSession')
																					.attr(
																							'disabled',
																							true);
																		} else {

																			$(
																					'#allocateFeedbackByAcadSession')
																					.attr(
																							'disabled',
																							false);
																		}

																	}
																});

													}

												})

								$(".facultyparameter")
										.on(
												'change',
												function() {
													var acadYear = $(
															'#acadYearForAcadSession')
															.val();

													console.log(acadYear);

													if (acadYear) {
														console
																.log("called acad session")
														$
																.ajax({
																	type : 'GET',
																	url : '${pageContext.request.contextPath}/getSemesterByAcadYearForFeedback?'
																			+ 'acadYear='
																			+ acadYear,
																	success : function(
																			data) {

																		var json = JSON
																				.parse(data);
																		var optionsAsString = "";

																		$(
																				'#acadSessionId')
																				.find(
																						'option')
																				.remove();
																		console
																				.log(json);
																		for (var i = 0; i < json.length; i++) {
																			var idjson = json[i];
																			console
																					.log(idjson);

																			for ( var key in idjson) {
																				console
																						.log(key
																								+ ""
																								+ idjson[key]);
																				optionsAsString += "<option value='" +key + "'>"
																						+ idjson[key]
																						+ "</option>";
																			}
																		}
																		console
																				.log("optionsAsString"
																						+ optionsAsString);

																		$(
																				'#acadSessionId')
																				.append(
																						optionsAsString);

																	}
																});
													} else {
														//  alert('Error no faculty');
													}
												});

							});
			
			//$("#datetimepicker1 input").destroy();
			
			
			
			
			
			//$("#startDatePram, #datetimepicker2 input").daterangepicker({
			/* $('#startDatePram, #datetimepicker2 input').daterangepicker({
				locale: {
		            format: 'YYYY-MM-DD HH:mm:ss'
		                },
		        "minYear": "2000",
		        "showDropdowns": true,
				"timePicker": true,
				"singleDatePicker": true,
			    "startDate": "12/01/2021",
			    "endDate": "12/07/2021"
			}, function(start, end, label) {
			  console.log('New date range selected: ' + start.format('YYYY-MM-DD') + ' to ' + end.format('YYYY-MM-DD') + ' (predefined range: ' + label + ')');
			}); */
			/* $("#datetimepicker2").on("dp.change", function(e) {

			validDateTimepicks();

			}).datetimepicker({

			// minDate:new Date(),
			useCurrent : false,
			format : 'YYYY-MM-DD HH:mm:ss'

			});

			$("#datetimepicker3").on("dp.change", function(e) {

			validDateTimepicks1();

			}).datetimepicker({

			minDate:new Date(),
			useCurrent : false,
			format : 'YYYY-MM-DD HH:mm:ss'

			});

			$("#datetimepicker4").on("dp.change", function(e) {

			validDateTimepicks1();

			}).datetimepicker({

			//	 minDate:new Date(),
			useCurrent : false,
			format : 'YYYY-MM-DD HH:mm:ss'

			});
			 
			function validDateTimepicks() {

				//alert("called ");

				if (($('#startDateProgram').val() != '' && $(

				'#startDateProgram').val() != null)

				&& ($('#endDateProgram').val() != '' && $(

				'#endDateProgram').val() != null)) {

					var fromDate = $('#startDateProgram').val();

					var toDate = $('#endDateProgram').val();

					var eDate = new Date(fromDate);

					var sDate = new Date(toDate);

					if (sDate < eDate) {

						alert("endate cannot be smaller than startDate");

						$('#startDateProgram').val("");

						$('#endDateProgram').val("");

					}

				}

			}

			function validDateTimepicks1() {

				//alert("called ");

				if (($('#startDateAcadSession').val() != '' && $(

				'#startDateAcadSession').val() != null)

				&& ($('#endDateAcadSession').val() != '' && $(

				'#endDateAcadSession').val() != null)) {

					var fromDate = $('#startDateAcadSession').val();

					var toDate = $('#endDateAcadSession').val();

					var eDate = new Date(fromDate);

					var sDate = new Date(toDate);

					if (sDate < eDate) {

						alert("endate cannot be smaller than startDate");

						$('#startDateAcadSession').val("");

						$('#endDateAcadSession').val("");

					}

				}

			} */
		</script>

		<script type="text/javascript">
		</script>
		
		<script>
			
		</script>