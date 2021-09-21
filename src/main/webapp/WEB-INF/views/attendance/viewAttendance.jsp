



<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>





<jsp:include page="../common/newDashboardHeader.jsp" />
<style>

input[type="checkbox"][readonly] {
	pointer-events: none;
}


#nav-tab .nav-item {
    background: #e2e2e2;
    border: 1px solid;
    
}
#nav-tabs .nav-item.show .nav-link, .nav-tabs .nav-link.active {
    background: #c10000!important;
    color: #fff!important;
    
    
}
}
</style>
<div class="d-flex dataTableBottom" id="facultyAssignmentPage">
	<jsp:include page="../common/newLeftNavBarFaculty.jsp" />
	<jsp:include page="../common/newLeftSidebarFaculty.jsp" />
	<jsp:include page="../common/rightSidebarFaculty.jsp" />

	<!-- DASHBOARD BODY STARTS HERE -->

	<div class="container-fluid m-0 p-0 dashboardWraper">

		<jsp:include page="../common/newTopHeaderFaculty.jsp" />

		<div class="container mt-5">
			<div class="row">
				<!-- SEMESTER CONTENT -->
				<div
					class="col-lg-9 col-md-9 col-sm-12 dashboardBody ml-auto mr-auto">

					<nav aria-label="breadcrumb">
						<ol class="breadcrumb">
							<li class="breadcrumb-item"><a
								href="${pageContext.request.contextPath}/homepage">Dashboard</a></li>
							<li class="breadcrumb-item" aria-current="page"><c:out
									value="${Program_Name}" /></li>
							<sec:authorize access="hasRole('ROLE_STUDENT')">
								<c:out value="${AcadSession}" />
							</sec:authorize>
							<li class="breadcrumb-item active" aria-current="page">View
								Attendance</li>
						</ol>
					</nav>


					<jsp:include page="../common/alert.jsp" />

					<div class="card bg-white border">
						<div class="card-body">

							<%-- <h5 class="text-center border-bottom pb-2">Mark Attendance for ${attendance.course.courseName }</h5> --%>
							<h5 class="text-center border-bottom pb-2">View Attendance
								for ${courseName}</h5>


							<form:form action="viewAttendance" id="viewAttendance"
								method="post" modelAttribute="attendance"
								enctype="multipart/form-data">




								<div class="row">
									<div class="col-md-4 col-sm-6 col-xs-12 column">
										<div class="form-group">
											<form:label path="courseId" for="courseId">Course</form:label>
											<span style="color: red">*</span>
											<form:select id="courseIdForForum" path="courseId"
												required="required" onchange="resetDate();"
												class="form-control">
												<form:option value="">Select Course</form:option>
												<c:forEach var="course" items="${allCourses}"
													varStatus="status">
													<form:option value="${course.id}">${course.courseName}</form:option>
												</c:forEach>
											</form:select>
										</div>
									</div>


									<div class="col-md-4 col-sm-6 col-xs-12 column">
										<label class="small"><strong>Select Date </strong> <span
											class="text-danger f-13">*</span></label>
										<div class="input-group">
											<form:input id="attdDate" path="attdDate" type="text"
												placeholder="Attendance Date" class="form-control"
												required="required" readonly="readonly" />
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


								<div class="col-md-4 col-sm-6 col-xs-12 column">
									<div class="form-group">

										<button type="submit" class="btn btn-success">Search</button>
									</div>
								</div>

							</form:form>
						</div>
					</div>


<c:if test="${ not empty studentData }">
					<div class="card bg-white border" id="studentAttendance">
						<div class="card-body">


	
							<h5 class="text-center border-bottom pb-2">
								Lecture Records 
							</h5>
							






								<nav>
									<div class="nav nav-tabs" id="nav-tab" role="tablist">
										<c:forEach var="lectureList" items="${studentData}"
											varStatus="lecture">

											<c:if test="${lecture.count eq 1 }">
												<%-- 	<button class="nav-item nav-link active" id="nav-${lecture.count}" data-toggle="tab" data-bs-target="#nav-${lecture.count}" type="button" role="tab" aria-controls="nav-${lecture.count}" aria-selected="true">${lectureList.key.start_time} - ${lectureList.key.end_time}</button> --%>

												<a class="nav-item nav-link active"
													id="nav-${lecture.count}-tab" data-toggle="tab"
													href="#nav-${lecture.count}" role="tab"
													aria-controls="nav-${lecture.count}"
													aria-selected="true">${lectureList.key.start_time}<br>
													- ${lectureList.key.end_time}</a>
											</c:if>
											<c:if test="${lecture.count ne 1}">
												<a class="nav-item nav-link"
													id="nav-${lecture.count}-tab" data-toggle="tab"
													href="#nav-${lecture.count}" role="tab"
													aria-controls="nav-${lecture.count}"
													aria-selected="false">${lectureList.key.start_time}<br>
													- ${lectureList.key.end_time}</a>
											</c:if>
														
										</c:forEach>
									</div>
								</nav>

								<!-- ----------------- -->

								<h5 class="text-center border-bottom pb-2 pt-2">
								Students Records 
							</h5>
								<div class="tab-content" id="nav-tabContent">
								<c:forEach var="lectureLists" items="${studentData}" varStatus="lecture">
									<c:if test="${lecture.count eq 1 }">
										<div class="tab-pane fade show active"
											id="nav-${lecture.count}" role="tabpanel"
											aria-labelledby="nav-${lecture.count}-tab">
											<div class="table-responsive ">
												<table class="table table-striped  table-hover" id="studentList">
													<thead>
														<tr>
															<th>Sr. No.</th>
															<th>SAP IDs</th>
															<th>Student Name</th>
															<th>Roll No.</th>
															<th>Attendance</th>
														</tr>
													</thead>
													<tbody>
														<c:forEach var="studentlist" items="${lectureLists.value}" varStatus="student">
															<tr>
																<td>${student.count}</td>
																<td>${studentlist.username}</td>
																<td>${studentlist.firstname}  ${studentlist.lastname}</td>
																<td>${studentlist.rollNo} </td>
																<td>
																
																
																<c:if test="${studentlist.status eq 'Present'}">
																<span style="color: green;">Present</span>
															</c:if>
															<c:if test="${studentlist.status eq 'Absent'}">
																<span style="color: red;">Absent</span>
															</c:if>
																</td>
															</tr>
														</c:forEach>
													</tbody>
												</table>
											</div>
										</div>
									</c:if>
									<c:if test="${lecture.count ne 1 }">
										<div class="tab-pane fade"
											id="nav-${lecture.count}" role="tabpanel"
											aria-labelledby="nav-${lecture.count}-tab">
											<div class="table-responsive ">
												<table class=" table table-striped  table-hover" id="">
													<thead>
														<tr>
															<th>Sr. No.</th>
															<th>SAP IDs</th>
															<th>Student Name</th>
															<th>Roll No.</th>
															<th>Attendance</th>
														</tr>
													</thead>
													<tbody>
														<c:forEach var="studentlist" items="${lectureLists.value}" varStatus="student">
															<tr>
																<td>${student.count}</td>
																<td>${studentlist.username}</td>
																<td>${studentlist.firstname}  ${studentlist.lastname}</td>
																<td>${studentlist.rollNo}</td>
																<td>
																<c:if test="${studentlist.status eq 'Present'}">
																<span style="color: green;">Present</span>
															</c:if>
															<c:if test="${studentlist.status eq 'Absent'}">
																<span style="color: red;">Absent</span>
															</c:if></td>
															</tr>
														</c:forEach>
													</tbody>
												</table>
											</div>
										</div>
									</c:if>
									</c:forEach>
								</div>

















								<!-- .................... -->


								<%-- <div class="table-responsive  ">
									<table class="table-striped  table-hover w-100"
										id="showMyStudents">
										<thead>
											<tr >
												<th>Sr. No.</th>
												<th>SAP IDs </th>
												<th>Student Name </th>
												<th>Roll No. </th>
												<th>Attendance</th>
												


											</tr>
										</thead>
										
										<tbody class="tab-content" id="nav-tabContent">
						
						
						<c:forEach var="lectureLists" items="${studentData}" varStatus="lecture">
						 <c:forEach var="studentlist" items="${lectureLists.value}" varStatus="student">
						 <tr>
						 
						 <td>${student.count}</td>
						  <c:if test="${lecture.count eq 1 }">
						   <td class="tab-pane fade show active" id="nav-${lecture.count}" role="tabpanel" aria-labelledby="nav-${lecture.count}-tab">
						   ${studentlist.username} 
						  </td>
						   <td class="tab-pane fade show active" id="nav-${lecture.count}" role="tabpanel" aria-labelledby="nav-${lecture.count}-tab">
						   ${studentlist.firstname}  ${studentlist.lastname}
						    
						  </td>
						   <td class="tab-pane fade show active" id="nav-${lecture.count}" role="tabpanel" aria-labelledby="nav-${lecture.count}-tab">
						   ${studentlist.rollNo} 
						  </td>
						   <td class="tab-pane fade show active" id="nav-${lecture.count}" role="tabpanel" aria-labelledby="nav-${lecture.count}-tab">
						   ${studentlist.status} 
						  </td>
						  </c:if>
						   <c:if test="${lecture.count ne 1 }">
						       <td class="tab-pane fade show active" id="nav-${lecture.count}" role="tabpanel" aria-labelledby="nav-${lecture.count}-tab" >${studentlist.username}</td>
						  </c:if>
						  </tr>
						  </c:forEach>
						  </c:forEach>
						
						
						
						
						</tbody>
									</table>

								</div> --%>


							
							
							<%-- <form:form action="markAttendance" method="post"
								modelAttribute="attendance">
								
								
								
								<div class="table-responsive  ">
									<table class="table-striped  table-hover w-100"
										id="showMyStudents">
										<thead>
											<tr>
												<th>Sr. No.</th>
												<th>SAP IDs </th>
												<th>Student Name </th>
												<th>Roll No. </th>
												<th>Attendance</th>
												


											</tr>
										</thead>
										
										<tbody>

											
										</tbody>
									</table>

								</div>
							
							</form:form> --%>
						</div>
					</div>
</c:if>
					<!-- /page content -->



				</div>

				<!-- SIDEBAR START -->
				<jsp:include page="../common/newSidebar.jsp" />
				<!-- SIDEBAR END -->
				<jsp:include page="../common/footer.jsp" />













				<script>
					var table = $('#showMyStudent')
							.DataTable(
									{
										"dom" : '<"top"i>rt<"bottom"flp><"clear">',
										"lengthMenu" : [ [ -1, 10, 25, 50 ],
												[ "All", 10, 25, 50 ] ],
										/*
										 * buttons: [ 'selectAll', 'selectNone' ],
										 * language: { buttons: { selectAll: "Select
										 * all items", selectNone: "Select none" } },
										 */
										initComplete : function() {
											this
													.api()
													.columns()
													.every(
															function() {
																var column = this;
																var headerText = $(
																		column
																				.header())
																		.text();
																if (headerText == "Sr. No."
																		|| headerText == "Select To Allocate")
																	return;
																var select = $(
																		'<select class="form-control"><option value="">All</option></select>')
																		.appendTo(
																				$(
																						column
																								.footer())
																						.empty())
																		.on(
																				'change',
																				function() {
																					var val = $.fn.dataTable.util
																							.escapeRegex($(
																									this)
																									.val());

																					column
																							.search(
																									val ? '^'
																											+ val
																											+ '$'
																											: '',
																									true,
																									false)
																							.draw();
																				});

																column
																		.data()
																		.unique()
																		.sort()
																		.each(
																				function(
																						d,
																						j) {
																					select
																							.append('<option value="'
																		+ d
																		+ '">'
																									+ d
																									+ '</option>')
																				});
															});
										}
									});

					$("#isAbsentAll")
							.click(
									function() {
										var selectedValue = $(
												"#lectureForAttendance").val();
										var isContinueLecture = $(
												"#lectureForAttendance").find(
												':selected').attr(
												"data-isContinueLecture");
										var isAbsentAll = $(this).val();
										console.log("checkBox Changed "
												+ isAbsentAll);
										window.location = '${pageContext.request.contextPath}/markAttendanceForm?lecture='
												+ encodeURIComponent(selectedValue)
												+ "&isAbsentAll="
												+ isAbsentAll
												+ '&isContinueLecture='
												+ isContinueLecture;
									});
				</script>
				<script>
					function resetDate() {

						$("#showMyStudents td").remove();
						$("#attdDate").val('');
						$("#records").text('0');

					}
					$(window)
							.bind(
									"pageshow",
									function() {

										var TommorowDate = new Date();
										//start Date picker
										$('.initiateSDatePicker')
												.daterangepicker(
														{
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

										$('#lectureForViewAttendances')
												.on(
														'change',
														function() {
															var courseId = $(
																	"#courseIdForForum")
																	.val();

															var starttime = $(
																	"#lectureForViewAttendance")
																	.val();
															console
																	.log(courseId);
															console
																	.log(attdDate);
															var selected = $(
																	this)
																	.find(
																			'option:selected');
															var endTime = selected
																	.data('foo');
															console
																	.log(
																			"endtime---",
																			endTime);
															$(
																	"#showMyStudents tr")
																	.remove();

															$
																	.ajax({
																		url : '${pageContext.request.contextPath}/getstudentAttendancedata?courseId='
																				+ courseId
																				+ '&starttime='
																				+ starttime
																				+ '&endTime='
																				+ endTime,
																		type : 'POST',

																		success : function(
																				data) {

																			var json = JSON
																					.parse(data);
																			let sr;

																			var jsonData = "";

																			for (var i = 0; i < json.length; i++) {

																				var idjson = json[i];
																				console
																						.log(idjson.eventId);
																				sr = i + 1;
																				jsonData += '<tr scope="row" ><td>'
																						+ sr
																						+ '</td><td>'
																						+ idjson.username
																						+ '</td><td>'
																						+ idjson.firstname
																						+ idjson.lastname
																						+ '</td>><td>'
																						+ idjson.rollNo
																						+ '</td><td>'
																						+ idjson.status
																						+ '</td></tr>';

																			}
																			if (jsonData == '') {
																				swal({
																					title : "Note",
																					text : "No Attendance marked  For Selected TimeSlot",
																					type : "error"
																				})
																			}
																			$(
																					'#showMyStudents tbody')
																					.html(
																							jsonData)
																			console
																					.log(
																							'sr---',
																							sr)
																			$(
																					'#records')
																					.text(
																							sr);

																			//$('#studentAttendance table tbody').html(jsonData)

																		},
																		error : function(
																				result) {
																			alert(result);
																		}

																	})

														})
										$('.initiateSDatePicker')
												.on(
														'apply.daterangepicker',
														function(ev, picker) {
															$(this)
																	.parent()
																	.parent()
																	.find(
																			'input')
																	.val(
																			picker.startDate
																					.format('DD-MM-YYYY'));
															var courseId = $(
																	"#courseIdForForum")
																	.val();

															var attdDate = $(
																	"#attdDate")
																	.val();
															console
																	.log(
																			"attdDate--",
																			attdDate);

														

															
														});

										$('.initiateSDatePicker')
												.on(
														'cancel.daterangepicker',
														function(ev, picker) {
															$(this)
																	.parent()
																	.parent()
																	.find(
																			'input')
																	.val(
																			$(
																					'#startDateDB')
																					.val());
														});

									});
				</script>

				</body>
				</html>