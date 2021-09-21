<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<html lang="en">
<jsp:include page="../common/css.jsp" />

<link
	href="https://gitcdn.github.io/bootstrap-toggle/2.2.2/css/bootstrap-toggle.min.css"
	rel="stylesheet">
<script
	src="https://gitcdn.github.io/bootstrap-toggle/2.2.2/js/bootstrap-toggle.min.js"></script>

<body class="nav-md footer_fixed">

	<div class="loader"></div>

	<%
		String courseRecord = request.getParameter("courseRecord");
	%>


	<%
		boolean isEdit = "true".equals((String) request
				.getAttribute("edit"));
	%>

	<div class="container body">
		<div class="main_container">


			<jsp:include page="../common/leftSidebar.jsp">
				<jsp:param name="courseId" value="${courseId}" />
			</jsp:include>


			<jsp:include page="../common/topHeaderLibrian.jsp">
				<jsp:param value="Announcement" name="activeMenu" />
			</jsp:include>

			<!-- page content: START -->
			<div class="right_col" role="main">

				<div class="dashboard_container">

					<div class="dashboard_container_spacing">

						<div class="breadcrumb">
							<c:out value="${Program_Name}" />

							<sec:authorize access="hasRole('ROLE_STUDENT')">

								<i class="fa fa-angle-right"></i>

								<c:out value="${AcadSession}" />

							</sec:authorize>
							<br> <br> <a
								href="${pageContext.request.contextPath}/homepage">Dashboard</a>
							<i class="fa fa-angle-right"></i> Add TimeTable
						</div>
						<jsp:include page="../common/alert.jsp" />
						<!-- Input Form Panel -->
						<div class="row">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">

									<div class="x_title">
										<h5 class="text-center border-bottom pb-2 text-uppercase">
											<%
												if (isEdit) {
											%>
											<%-- <form:input type="hidden" path="id" /> --%>
											Edit ${announcement.announcementType} Announcement
											<%
												} else {
											%>
											Add ${announcement.announcementType} Announcement
											<%
												}
											%>
											<%-- <sec:authorize access="hasRole('ROLE_ADMIN')"> --%>
											<c:if test="${announcement.announcementType == 'TIMETABLE'}">
												<!-- <p>(Will be visible to All Students of Institute)</p> -->
											</c:if>
											<%-- </sec:authorize> --%>
											<c:if test="${announcement.announcementType == 'COURSE'}">
						        for ${courseIdNameMap[announcement.courseId] } 
						        <p>(Will be visible to All Students Enrolled in this
													Course)</p>
											</c:if>
										</h5>
									</div>


									<form:form action="addTimeTable" method="post"
										id="addAnnouncement" modelAttribute="announcement"
										enctype="multipart/form-data">
										<fieldset>
											<%
												if (isEdit) {
											%>
											<form:input path="id" type="hidden" />
											<form:input path="courseId" type="hidden" />
											<form:input path="programId" type="hidden" />


											<%
												}
											%>
											<form:input path="announcementType" type="hidden" />

											<div class="row">

												<%-- <sec:authorize access="hasRole('ROLE_ADMIN')"> --%>

												<div class="col-md-4 col-sm-6" id="sessionId">
													<div class="form-group">
														<form:label path="acadSession" for="acadSession">Semester</form:label>
														<form:select id="acadSessionId" path="acadSession"
															class="form-control" multiple="mutliple">

															<form:option value="">Select Semester</form:option>

															<c:forEach items="${announcement.semesters}"
																var="acadSession">
																<form:option value="${acadSession }">${acadSession }</form:option>
															</c:forEach>
														</form:select>
													</div>

												</div>
												<div class="col-sm-6 col-md-8 column">
													<div class="form-group">
														<form:label path="programIds" for="programIds">Select Program</form:label>
														<form:select id="programIds" path="programIds"
															placeholder="Select Program"
															class="form-control table-responsive">
															<form:option value="" disabled="true">Select Program</form:option>
															<c:forEach var="prog" items="${programList}"
																varStatus="status">

																<form:option value="${prog.id}">${prog.programName}</form:option>

															</c:forEach>
														</form:select>
													</div>
												</div>

												<div class="col-md-4 col-sm-6" id="acadYearId">
													<div class="form-group">
														<label class="control-label" for="course">acadYear
															<span style="color: red">*</span>
														</label>
														<form:select id="acadYearForAcadSession" path="acadYear"
															placeholder="acad Year"
															class="form-control facultyparameter" required="required">
															<form:option value="">Select Acad Year</form:option>
															<form:options items="${yearList}" />
														</form:select>
													</div>
												</div>



												<div class="col-sm-6 col-md-4">
													<div class="form-group">
														<label class="control-label" for="announcementSubType">Select
															Announcement Subtype</label>
														<form:select id="announcementSubType"
															path="announcementSubType"
															placeholder="Announcement Subtype" class="form-control">
															<form:option value="">Select Announcement Subtype</form:option>
															<form:option value="EXAM">EXAM</form:option>
															<form:option value="EVENT">EVENT</form:option>
															<form:option value="ASSIGNMENT">ASSIGNMENT</form:option>
															<form:option value="Internal">Internal Marks</form:option>
															<form:option value="Academics">Academics</form:option>
															<form:option value="WeCare">We Care</form:option>
															<form:option value="FROMTHECOUNSELLORSDESK">From the Counsellor's Desk</form:option>

														</form:select>
													</div>
												</div>
												<%-- </sec:authorize> --%>
												<div class="col-sm-6 col-md-4">
													<div class="form-group">
														<form:label path="subject" for="subject">Announcement Title <span
																style="color: red">*</span>
														</form:label>
														<form:input path="subject" type="text"
															class="form-control" required="required" />
													</div>
												</div>

												<div class="col-sm-6 col-md-4">
													<div class="form-group">
														<label class="small"><strong>Start Date</strong> <span
															class="text-danger f-13">*</span></label>
														<div class="input-group">
															<%-- <form:input id="startDateTimeTable" path="startDate" type="text"
												placeholder="Start Date" class="form-control"
												required="required" readonly="true" />
											<div class="input-group-append">
												<button
													class="btn btn-outline-secondary initiateSDatePicker"
													type="button">
													<i class="fas fa-calendar"></i>
												</button>
											</div> --%>
															<div class='input-group date' id='datetimepicker1'>
																<form:input id="startDate" path="startDate" type="text"
																	placeholder="Start Date" class="form-control"
																	readonly="true" />
																<span class="input-group-addon"><span
																	class="glyphicon glyphicon-calendar"></span> </span>
															</div>
														</div>
													</div>
												</div>
												<div class="col-sm-6 col-md-4">
													<label class="small"><strong>End Date</strong> <span
														class="text-danger f-13">*</span></label>

													<div class="input-group">
														<%-- <form:input id="endDateTimeTable" path="endDate" type="text"
										placeholder="End Date" class="form-control"
										required="required" readonly="true" />
									<div class="input-group-append">
										<button class="btn btn-outline-secondary initiateEDatePicker"
											type="button">
											<i class="fas fa-calendar"></i>
										</button>
									</div> --%>
														<div class='input-group date' id='datetimepicker2'>
															<form:input id="endDate" path="endDate" type="text"
																placeholder="End Date" class="form-control"
																readonly="true" />
															<span class="input-group-addon"><span
																class="glyphicon glyphicon-calendar"></span> </span>
														</div>
													</div>
												</div>
												<%-- <c:if test="${allCampuses.size() > 0}">
									<div class="col-md-4 col-sm-6">
										<div class="form-group">
											<form:label path="campusId" for="campusId">Select Campus</form:label>

											<form:select id="campusId" path="campusId" type="text"
												placeholder="campus" class="form-control">
												<form:option value="">Select Campus</form:option>
												<c:forEach var="campus" items="${allCampuses}"
													varStatus="status">
													<form:option value="${campus.campusId}">${campus.campusName}</form:option>
												</c:forEach>
											</form:select>
										</div>
									</div>
								</c:if> --%>
												<div class="col-md-4 col-sm-6">
													<div class="form-group">
														<%-- <label for="file" > <%
 	if (isEdit) {
 %> Select if you wish to override earlier file <%
 	} else {
 %> Announcement File <%
 	}
 %>
										</label> <input id="file" name="file" type="file" class="form-control" 
											multiple="multiple"/ required="required"> --%>
														<%
															if (isEdit) {
														%>

														<label for="file">Select if you wish to override
															earlier file</label> <input id="file" name="file" type="file"
															class="form-control" multiple="multiple" />

														<%
															} else {
														%>

														<label for="file">Announcement File</label> <input
															id="file" name="file" type="file" class="form-control"
															multiple="multiple" required="required" />

														<%
															}
														%>

													</div>
													<div id=fileSize></div>
												</div>

											</div>
											<hr />
											<div class="row">
												<div class="col-md-3 col-sm-6">
													<div class="slider round">
														<form:label path="sendEmailAlert" for="sendEmailAlert">Send Email Alert for New Announcement?</form:label>
														<br>
														<form:checkbox path="sendEmailAlert" class="form-control"
															value="Y" data-toggle="toggle" data-on="Yes"
															data-off="No" data-onstyle="success"
															data-offstyle="danger" data-style="ios" data-size="mini" />
													</div>
												</div>

												<div class="col-md-3 col-sm-6">
													<div class="slider round">
														<form:label path="sendSmsAlert" for="sendSmsAlert">Send SMS Alert for New Announcement?</form:label>
														<br>
														<form:checkbox path="sendSmsAlert" class="form-control"
															value="Y" data-toggle="toggle" data-on="Yes"
															data-off="No" data-onstyle="success"
															data-offstyle="danger" data-style="ios" data-size="mini" />
													</div>
												</div>

												<div class="col-md-3 col-sm-6">
													<div class="form-group">
														<label class="control-label" for="examMonth">Exam
															Month <span style="color: red">*</span>
														</label>
														<form:select id="examMonth" path="examMonth"
															placeholder="Exam Month"
															class="form-control facultyparameter" required="required">
															<form:option value="" selected="selected" disabled="true">Select Exam Month</form:option>
															<form:option value="January">January</form:option>
															<form:option value="Febuary">Febuary</form:option>
															<form:option value="March">March</form:option>
															<form:option value="April">April</form:option>
															<form:option value="May">May</form:option>
															<form:option value="June">June</form:option>
															<form:option value="July">July</form:option>
															<form:option value="August">August</form:option>
															<form:option value="September">September</form:option>
															<form:option value="October">October</form:option>
															<form:option value="November">November</form:option>
															<form:option value="December">December</form:option>
														</form:select>
													</div>
												</div>

												<div class="col-md-3 col-sm-6">
													<div class="form-group">
														<label class="control-label" for="examYear">Exam
															Year <span style="color: red">*</span>
														</label>
														<form:select id="examYear" path="examYear"
															placeholder="Exam Year"
															class="form-control facultyparameter" required="required">
															<form:option value="" selected="selected" disabled="true">Select Exam Year</form:option>
															<form:options items="${yearList}" />
														</form:select>
													</div>
												</div>

												<div class="col-md-3 col-sm-6">
													<div class="form-group">
														<label class="control-label" for="examMonth">Exam
															Type <span style="color: red">*</span>
														</label>
														<form:select id="examType" path="examType"
															placeholder="Exam Month"
															class="form-control facultyparameter" required="required">
															<form:option value="finalExam" selected="selected">Final Exam</form:option>
															<form:option value="reExam">Re-Exam</form:option>
														</form:select>
													</div>
												</div>
											</div>
											<hr>
											<div class="row">
												<div class="col-12">
													<form:label path="description" for="editor">Announcement Description:</form:label>

													<form:textarea class="form-group" path="description"
														name="editor1" id="editor1" rows="10" cols="80" />

												</div>



												<div class="col-12">
													<div class="form-group">

														<%
															if (isEdit) {
														%>
														<button id="submit" class="btn btn-large btn-success mt-2"
															formaction="updateTimeTable?typeOfAnn=PROGRAM">Update
															Announcement</button>
														<%
															} else {
														%>
														<button id="submit" class="btn btn-large btn-success mt-2"
															formaction="addTimeTable?typeOfAnn=PROGRAM">Create
															Announcement</button>
														<%
															}
														%>
														<button id="cancel" class="btn btn-danger mt-2"
															formaction="homepage" formnovalidate="formnovalidate">Cancel</button>
													</div>
												</div>
											</div>



										</fieldset>
									</form:form>
								</div>
							</div>
						</div>

						<!-- Results Panel -->

					</div>



				</div>

			</div>
			<!-- /page content: END -->


			<jsp:include page="../common/footerLibrarian.jsp" />

		</div>
	</div>




	<script type="text/javascript"
		src="//cdn.ckeditor.com/4.8.0/standard-all/ckeditor.js"></script>
	<script type="text/javascript"
		src="https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.0/MathJax.js?config=TeX-AMS_HTML"></script>

	<script type="text/javascript">
		CKEDITOR
				.replace(
						'editor1',
						{
							extraPlugins : 'mathjax',
							mathJaxLib : 'https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.0/MathJax.js?config=TeX-AMS_HTML'
						});
	</script>

	<script>
		//$(document).ready(function() {
		$("#datetimepicker1").on("dp.change", function(e) {

			validDateTimepicks();
		}).datetimepicker({
			// minDate:new Date(),
			useCurrent : false,
			format : 'YYYY-MM-DD HH:mm:ss'
		});

		$("#datetimepicker2").on("dp.change", function(e) {

			validDateTimepicks();
		}).datetimepicker({
			// minDate:new Date(),
			useCurrent : false,
			format : 'YYYY-MM-DD HH:mm:ss'
		});

		function validDateTimepicks() {
			if (($('#startDate').val() != '' && $('#startDate').val() != null)
					&& ($('#endDate').val() != '' && $('#endDate').val() != null)) {
				var fromDate = $('#startDate').val();
				var toDate = $('#endDate').val();
				var eDate = new Date(fromDate);
				var sDate = new Date(toDate);
				if (sDate < eDate) {
					alert("endate cannot be smaller than startDate");
					$('#startDate').val("");
					$('#endDate').val("");
				}
			}
		}
		//});
	</script>


	<!-- <script>
			$(window)
					.bind(
							"pageshow",
							function() {
								var TommorowDate = new Date();
								//start Date picker
								$('.initiateSDatePicker').daterangepicker({
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

								$('.initiateSDatePicker')
										.on(
												'apply.daterangepicker',
												function(ev, picker) {
													//validDateTimepicks();
													var fromDate = $(
															'#startDateTimeTable')
															.val();
													var toDate = $(
															'#endDateTimeTable')
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
															'#startDateTimeTable')
															.val();
													var toDate = $(
															'#endDateTimeTable')
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

								$('.initiateEDatePicker').on(
										'cancel.daterangepicker',
										function(ev, picker) {
											$(this).parent().parent().find(
													'input').val(
													$('#endDateDB').val());

										});

							});
		</script> -->

</body>
</html>

