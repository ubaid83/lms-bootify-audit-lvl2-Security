<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>


<jsp:include page="../common/newDashboardHeader.jsp" />

<div class="d-flex adminPage" id="facultyAssignmentPage">
	<jsp:include page="../common/newAdminLeftNavBar.jsp" />
	<jsp:include page="../common/rightSidebarAdmin.jsp" />


	<!-- DASHBOARD BODY STARTS HERE -->

	<div class="container-fluid m-0 p-0 dashboardWraper">

		<jsp:include page="../common/newAdminTopHeader.jsp" />

		<!-- SEMESTER CONTENT -->
		<div class="col-lg-12  my-5">
			<!-- <h2 class="text-center pb-2 border-bottom mb-5">WELCOME TO ADMIN
				DASHBOARD!</h2> -->

			<jsp:include page="../common/alert.jsp" />
			<div class="card rounded-0">
				<h5 class="my-3 pl-3 text-dark">Create Master Data Validation</h5>
				<h6 class="my-1 pl-3 text-danger">Note : At a time only one
					program can be allocated for the validation purpose.</h6>
				<div class="card-body">

					<form action="createDetailConfirmation" method="post">
						<div class="row my-3 pl-3">
							<div class="col-md-4 ">
								<div class="input-group">
									<input id="startDateIca" name="endDate" type="text"
										placeholder="End Date" class="form-control border border-secondary master-endate-boxborder rounded-0"
										required="required">
									<div class="input-group-append">
										<button
											class="btn btn-outline-secondary rounded-0 initiateSDatePicker"
											type="button">
											<i class="fas fa-calendar"></i>
										</button>
									</div>
								</div>
							</div>
						
						<div class="col-md-4">
								<select name="campusId" id="mastercampusIdId" class="form-control rounded-0" >
									<option value="">Select Campus Name</option>
									<c:forEach var="campus" items="${campusList}"
										varStatus="status">
										<option value="${campus.campusId}">${campus.campusName}</option>
									</c:forEach>
								</select>
							</div>

							<div class="col-md-4">
								<select name="programId" id="masterProgramId" class="form-control rounded-0" required="required">
									<option value="">Select Program Name</option>
									<c:forEach var="program" items="${AdminProgramList}"
										varStatus="status">
										<option value="${program.programId}">${program.programName}</option>
									</c:forEach>
								</select>
							</div>
							
							<div class="col-md-4 my-3">
								<select name="acadSession" id="acadSession" class="form-control rounded-0" multiple="multiple" required="required">
									<option >Select Semester</option>
									<c:forEach var="session" items="${acadSessionList}"
										varStatus="status">
							
											<option value="${session.acadSession}">${session.acadSession}</option>
							
									</c:forEach>
								</select>
							</div>
							
							
						</div>
						<div class="row my-3 pl-3">
						<div class="col-sm-4 ">
							<table class="table table-bordered table-striped">
												<tbody>
													<tr>
														<td>Send Email Alert ?</td>
														<td>
															<div class="pretty p-switch p-fill p-toggle">
																<input type="checkbox" value="Yes" id="sendEmailAlert"
																	name="sendEmailAlert" />
																<div class="state p-success p-on">
																	<label>Yes</label>
																</div>
																<div class="state p-danger p-off">
																	<label>No</label>
																</div>

															</div>
														</td>
													</tr>
													
													
												</tbody>
											</table>
											
							
					
							</div>
							
								
						</div>
						<div class="row my-3 pl-3">
								<div class="col-md-2">

								<%-- <c:if test="${diableadminbtn ne localdate }"> --%>
								<input type="submit" id="myBtn" class="btn btn-secondary rounded-0"
									value="Submit">
								<!-- <input type="submit" class="btn btn-secondary my-2" value="Submit"> -->
								<%-- </c:if> --%>
							</div>
							</div>
					</form>
	

				</div>
			</div>
			
		

			<div class="card rounded-0 my-2">
				<h5 class="my-3 pl-3 text-dark">Download Report</h5>
				<div class="card-body">
			
					<!-- <h4>Download Report</h4> -->
					<div class="col-md-2">

						<a class="btn btn-primary rounded-0 text-light"
							href="downloadMasterReport">Download Report</a>
					</div>
				</div>
			</div>
			
			<div class="card rounded-0 my-2">
				<h5 class="my-3 pl-3 text-dark">Upload Master Status Report</h5>
				<p class="my-2 pl-3">Note: Once excel is uploaded remark data will be send to students via Email</p>
				<div class="card-body">
			
					<!-- <h4>Download Report</h4> -->
					<div class="row">
				
					<form action="uploadMasterStatusReport" method="POST" enctype="multipart/form-data">
					<div class="col-md-2">
					<input type="file" name="excelFileRead" class="my-2" required="required"> 
					</div>
					<div class="col-md-3">
					<input type="submit" class="btn btn-primary rounded-0 text-light" value="Upload Report">
					</div>
					</form>
					
					</div>
					<a class=" text-danger my-3"
							href="downloadMasterSampleTemplet">Download Sample Template</a>
							
					
				</div>
			</div>






			<div class="card rounded-0 my-5">
				<h5 class="my-3 pl-3 text-dark">View</h5>
				<div class="card-body">
					<div class="table-responsive">

						<table class="table table-striped table-hover"
							style="font-size: 12px" id="contentTree">
							<thead>
								<tr>
									<th>End Date</th>
									<th>Program Name</th>
									<th>Campus</th>
									<th>Semester</th>
									<th>Created By</th>
									<th>Actions</th>
								</tr>
							</thead>
							<tbody>

								<c:forEach var="data" items="${allData}" varStatus="status">
									<tr>
										<td><c:out value="${data.endDate}"></c:out></td>

										<td><c:out value="${data.programName}"></c:out></td>
										<td><c:out value="${data.campusName}"></c:out></td>
										<td><c:out value="${data.acadSession}"></c:out></td>
										<td><c:out value="${data.createdBy}"></c:out></td>

										<td><c:url value="deleteStudentDetailConfirmation"
												var="deleteurl">
												<c:param name="id" value="${data.id}" />
											</c:url> <a href="${deleteurl}" title="Delete"
											onclick="return confirm('Are you sure you want to delete this record?')"><i
												class="fas fa-trash"></i></a></td>
									</tr>
								</c:forEach>


							</tbody>
						</table>
						<c:if test="${size == 0}">
					No Content under this folder
				</c:if>
					</div>
				</div>
			</div>


		</div>
	</div>





</div>



<jsp:include page="../common/newAdminFooter.jsp" />
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
							"timePicker" : false,
							"showCustomRangeLabel" : false,
							"alwaysShowCalendars" : true,
							"opens" : "center"
						});

						$('.initiateSDatePicker')
								.on(
										'apply.daterangepicker',
										function(ev, picker) {
											//validDateTimepicks();
											var fromDate = $('#startDateIca')
													.val();
											var toDate = $('#endDateIca').val();
											var sDate = new Date(fromDate);
											var eDate = new Date(toDate);
											console.log('validate called'
													+ sDate + ',' + eDate);
											if (sDate > eDate) {
												alert("endate cannot be smaller than startDate");
												$('#startDateIca')
														.val(
																$(
																		'#startDateDB')
																		.val());
												$('#endDateIca').val(
														$('#endDateDB').val());
											} else {
												$(this)
														.parent()
														.parent()
														.find('input')
														.val(
																picker.startDate
																		.format('YYYY-MM-DD'));
												/* .format('YYYY-MM-DD HH:mm:ss')); */
											}
										});

						$('.initiateSDatePicker').on(
								'cancel.daterangepicker',
								function(ev, picker) {
									$(this).parent().parent().find('input')
											.val($('#startDateDB').val());

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
							"timePicker" : false,
							"showCustomRangeLabel" : false,
							"alwaysShowCalendars" : true,
							"opens" : "center"
						});

						$('.initiateEDatePicker').on(
								'cancel.daterangepicker',
								function(ev, picker) {
									$(this).parent().parent().find('input')
											.val($('#endDateDB').val());

								});

					});
</script>




<script>
	$(document)
			.ready(
					function() {

						$("#masterProgramId")
								.on(
										'change',
										function() {
											var masterProgramId = $(this).val();
											if (masterProgramId) {
												$
														.ajax({
															type : 'GET',
															url : '${pageContext.request.contextPath}/getacadSessionByProgramId?'
																	+ 'masterProgramId='
																	+ masterProgramId,
															success : function(
																	data) {
																var json = JSON.parse(data);
																var optionsAsString = "";

																$('#acadSession').find('option').remove();
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
																		document.getElementById("myBtn").disabled = true;
																	}else{
																		document.getElementById("myBtn").disabled = false;
																	for ( var key in idjson) {
																		console.log(key+ ""+ idjson[key]);
																		optionsAsString += "<option value='" +key + "'>"
																				+ idjson[key]
																				+ "</option>";
																	}
																	}
																}
																console.log("optionsAsString"+ optionsAsString);

																$('#acadSession').append(optionsAsString);

																$('#acadSession').trigger('change');

															}
														});
											} else {
												//alert('Error no tds');
											}
										});
						$('#masterProgramId').trigger('change');
					});
</script>
















