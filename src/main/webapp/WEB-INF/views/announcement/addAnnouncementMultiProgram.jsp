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

			<%
				String courseRecord = request.getParameter("courseRecord");
			%>


			<%
				boolean isEdit = "true".equals((String) request
						.getAttribute("edit"));
			%>

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
					<li class="breadcrumb-item active" aria-current="page">Add
						Announcement</li>
				</ol>
			</nav>


			<jsp:include page="../common/alert.jsp" />
			<!-- Input Form Panel -->
			<div class="card bg-white border">
				<div class="card-body">
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
							<sec:authorize access="hasRole('ROLE_ADMIN')">
								<c:if test="${announcement.announcementType == 'INSTITUTE'}">
									<p>(Will be visible to All Students of Institute)</p>
								</c:if>
							</sec:authorize>
							<c:if test="${announcement.announcementType == 'COURSE'}">
        for ${courseIdNameMap[announcement.courseId] } 
        <p>(Will be visible to All Students Enrolled in this Course)</p>
							</c:if>
						</h5>
					</div>


					<form:form action="addAnnouncementMultiProgram" method="post"
						id="addAnnouncement" modelAttribute="announcement"
						enctype="multipart/form-data">
						<fieldset>
						<%if(isEdit){ %>
							<form:input path="id" type="hidden" />
							<form:input path="courseId" type="hidden" />
							<form:input path="programId" type="hidden" />
							
							
						<%} %>
						<form:input path="announcementType" type="hidden" />

							<div class="row">

								<sec:authorize access="hasRole('ROLE_ADMIN')">
									
									
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
									
									<%
 									if (!isEdit) {
 									%>
 								
								         <div class="col-sm-6 col-md-8 column">
													<div class="form-group">
														<form:label path="programIds" for="programIds">Select Program</form:label><span
												style="color: red">*</span>
														<form:select id="programIds" path="programIds" multiple="multiple"
														required="required" class="form-control table-responsive">
															<form:option value="" disabled="true">Select Program</form:option>
															<c:forEach var="prog" items="${programList}"
																varStatus="status">

																<form:option value="${prog.id}">${prog.programName}</form:option>

															</c:forEach>
														</form:select>
													</div>
												</div>
												<%	}
										 %>
										 
										 
									
									<%
 									if (!isEdit) {
 									%>
									
									<!-- Select Course  -->
							<div class="col-md-4 col-sm-6" >
										<div class="form-group">
											<label class="control-label text-success" for="course">Select Course (Optional)
											</label>

											<select name="admincourseId" id="courseIds" class="form-control" multiple="multiple">
											<c:forEach items="${courseList}" var="course">
												<option value="${course.id}">${course.courseName}</option>
												</c:forEach>
											</select>
										</div>
									</div>
										<%	
											}
										 %>
										 
										 
										 
									
									<div class="col-md-4 col-sm-6" id="acadYearId">
										<div class="form-group">
											<label class="control-label" for="course">acadYear <span
												style="color: red">*</span></label>
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
												Announcement Subtype</label><span
												style="color: red">*</span>
												
											<form:select id="announcementSubType"
												path="announcementSubType"  required="required"
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
								</sec:authorize>
								<div class="col-sm-6 col-md-4">
									<div class="form-group">
										<form:label path="subject" for="subject">Announcement Titttle <span
												style="color: red">*</span>
										</form:label>
										<form:input path="subject" type="text" class="form-control"
											required="required" />
									</div>
								</div>
								<div class="col-sm-6 col-md-4">
									<div class="form-group">
										<label class="small"><strong>Start Date</strong> <span
											class="text-danger f-13">*</span></label>
										<div class="input-group">
											<form:input id="startDateTimeTable" path="startDate" type="text"
												placeholder="Start Date" class="form-control"
												required="required" readonly="true" />
											<div class="input-group-append">
												<button
													class="btn btn-outline-secondary initiateSDatePicker"
													type="button">
													<i class="fas fa-calendar"></i>
												</button>
											</div>
										</div>
									</div>
								</div>
								<div class="col-sm-6 col-md-4">
									<label class="small"><strong>End Date</strong> <span
									class="text-danger f-13">*</span></label>

								<div class="input-group">
									<form:input id="endDateTimeTable" path="endDate" type="text"
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
								<c:if test="${allCampuses.size() > 0}">
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
								</c:if>
								<div class="col-md-4 col-sm-6">
									<div class="form-group">
										<label for="file"> <%
 	if (isEdit) {
 %> Select if you wish to override earlier file <%
 	} else {
 %> Announcement File <%
 	}
 %>
										</label> <input id="file" name="file" type="file" class="form-control"
											multiple="multiple" />
									</div>
									<div id=fileSize></div>
								</div>
								
							</div>
							<hr/>

							<div class="row mt-3">
							<div class="col-12">
							<table
												class="table table-bordered table-striped mt-5 font-weight-bold">
												<tbody>
													<tr>
														<td>Send Email Alert for New Announcement?</td>
														<td>
															<div class="pretty p-switch p-fill p-toggle">
																<form:checkbox value="Yes" id="sendEmailAlert"
																	path="sendEmailAlert" name="sendEmailAlert" />
																<div class="state p-success p-on">
																	<label>Yes</label>
																</div>
																<div class="state p-danger p-off">
																	<label>No</label>
																</div>

															</div>
														</td>
													</tr>
													<tr>
														<td>Send SMS Alert for New Announcement?</td>
														<td>
															<div class="pretty p-switch p-fill p-toggle">
																<form:checkbox value="Yes" id="sendSmsAlert"
																	path="sendSmsAlert" />
																<div class="state p-success p-on">
																	<label>Yes</label>
																</div>
																<div class="state p-danger p-off">
																	<label>No</label>
																</div>

															</div>
														</td>
													</tr>
													<c:if test="${sendAlertsToParents eq true}">
														<tr>
															<td>Send Email Alert To Parents?</td>
															<td>
																<div class="pretty p-switch p-fill p-toggle">
																	<form:checkbox value="Yes" id="sendEmailAlertParents"
																		path="sendEmailAlertToParents" />
																	<div class="state p-success p-on">
																		<label>Yes</label>
																	</div>
																	<div class="state p-danger p-off">
																		<label>No</label>
																	</div>

																</div>
															</td>
														</tr>
														<tr>
															<td>Send SMS Alert To Parents?</td>
															<td>
																<div class="pretty p-switch p-fill p-toggle">
																	<form:checkbox value="Yes" id="sendSmsAlertToParents"
																		path="sendSmsAlertToParents" />
																	<div class="state p-success p-on">
																		<label>Yes</label>
																	</div>
																	<div class="state p-danger p-off">
																		<label>No</label>
																	</div>

																</div>
															</td>
														</tr>
													</c:if>
												</tbody>
											</table>
											
											
								<%-- 			
								<div class="col-sm-6 col-md-4">
									<div class="slider round">
										<form:label path="sendEmailAlert" for="sendEmailAlert">Send Email Alert for New Announcement?</form:label>
										<br>
										<form:checkbox path="sendEmailAlert" class="form-control"
											value="Y" data-toggle="toggle" data-on="Yes" data-off="No"
											data-onstyle="success" data-offstyle="danger"
											data-style="ios" data-size="mini" />
									</div>
								</div>
								<c:if test="${sendAlertsToParents eq false}">
									<div class="col-sm-6 col-md-4">
										<div class="slider round">
											<form:label path="sendEmailAlertToParents"
												for="sendEmailAlertToParents">Send Email Alert To Parents?</form:label>
											<br>
											<form:checkbox path="sendEmailAlertToParents"
												class="form-control" value="Y" data-toggle="toggle"
												data-on="Yes" data-off="No" data-onstyle="success"
												data-offstyle="danger" data-style="ios" data-size="mini" />
										</div>
									</div>
								</c:if> --%>
<%-- 
								<div class="col-sm-6 col-md-4">
									<div class="slider round">
										<form:label path="sendSmsAlert" for="sendSmsAlert">Send SMS Alert for New Announcement?</form:label>
										<br>
										<form:checkbox path="sendSmsAlert" class="form-control"
											value="Y" data-toggle="toggle" data-on="Yes" data-off="No"
											data-onstyle="success" data-offstyle="danger"
											data-style="ios" data-size="mini" />
									</div>
								</div>
								<c:if test="${sendAlertsToParents eq false}">
									<div class="col-sm-6 col-md-4">
										<div class="slider round">
											<form:label path="sendSmsAlertToParents"
												for="sendSmsAlertToParents">Send SMS Alert To Parents?</form:label>
											<br>
											<form:checkbox path="sendSmsAlertToParents"
												class="form-control" value="Y" data-toggle="toggle"
												data-on="Yes" data-off="No" data-onstyle="success"
												data-offstyle="danger" data-style="ios" data-size="mini" />
										</div>
									</div>
								</c:if> --%>


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
											formaction="newUpdateAnnouncement?typeOfAnn=PROGRAM">Update
											Announcement</button>
										<%
											} else {
										%>
										<button id="submit" class="btn btn-large btn-success mt-2"
											formaction="addAnnouncementMultiProgram?typeOfAnn=PROGRAM">Create
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

			<!-- /page content: END -->




		</div>

		<!-- SIDEBAR START -->

		<!-- SIDEBAR END -->
		<jsp:include page="../common/newAdminFooter.jsp" />





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
		
			<!-- <script>
		$(window).bind("pageshow", function(){
			$("#startDate").val('');
			$('#endDate').val('');
			  		  $('#startDate').daterangepicker({
			  		      autoUpdateInput: false,
			  		      locale: {
			  		          cancelLabel: 'Clear'
			  		      },
			  		      "singleDatePicker": true,
			          	  "showDropdowns" : true,
			                "timePicker" : true,
			                "showCustomRangeLabel" : false,
			                "alwaysShowCalendars" : true,
			                "opens" : "center"
			  		  });

			  		  $('#startDate').on('apply.daterangepicker', function(ev, picker) {
			  		      $(this).val(picker.startDate.format('YYYY-MM-DD HH:mm:ss'));
			  		  });

			  		  $('#startDate').on('cancel.daterangepicker', function(ev, picker) {
			  		      $(this).val('');
			  		  });
			  		  
			  		  
			  		$('#endDate').daterangepicker({
			  		      autoUpdateInput: false,
			  		      locale: {
			  		          cancelLabel: 'Clear'
			  		      },
			  		      "singleDatePicker": true,
			          	  "showDropdowns" : true,
			                "timePicker" : true,
			                "showCustomRangeLabel" : false,
			                "alwaysShowCalendars" : true,
			                "opens" : "center"
			  		  });

			  		  $('#endDate').on('apply.daterangepicker', function(ev, picker) {
			  		      $(this).val(picker.startDate.format('YYYY-MM-DD HH:mm:ss'));
			  		  });

			  		  $('#endDate').on('cancel.daterangepicker', function(ev, picker) {
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
		</script>
		
		<script>
$(document).ready( function() {$("#programIds").on('change',
										function() {
											let programIds = $(this).val();								
											if (programIds) {
												$.ajax({
															type : 'POST',
															url : '${pageContext.request.contextPath}/getCourseByProgramIdAdmin?'
																	+ 'programIds='
																	+ programIds,
															success : function(
																	data) {
																var json = JSON.parse(data);
																
																console.log('json-------->',json)
																
																var optionsAsString = "";

																$('#courseIds').find('option').remove();
																console.log(json);
																for (var i = 0; i < json.length; i++) {
																	var idjson = json[i];
																	console.log('idjson'+idjson['NA']);
																	if(idjson['NA']!=undefined){
																		for ( var key in idjson) {
																			console.log(key+ ""+ idjson[key]);
																			optionsAsString += "<option value='" +key + "'>"
																					+ idjson[key]
																					+ "</option>";
																		}
																		//document.getElementById("myBtn").disabled = true;
																	}else{
																		//document.getElementById("myBtn").disabled = false;
																	for ( var key in idjson) {
																		console.log(key+ ""+ idjson[key]);
																		optionsAsString += "<option value='" +key + "'>"
																				+ idjson[key]
																				+ "</option>";
																	}
																	}
																}
																console.log("optionsAsString------------>"+ optionsAsString);

																$('#courseIds').append(optionsAsString);

																$('#courseIds').trigger('change');

															}
														});
											} else {
												//alert('Error no tds');
											}
										});
						$('#ProgramIds').trigger('change');
					});
</script>
		
		
