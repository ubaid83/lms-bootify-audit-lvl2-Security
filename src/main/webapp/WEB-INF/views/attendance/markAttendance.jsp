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
							<li class="breadcrumb-item active" aria-current="page">Mark
								Attendance</li>
						</ol>
					</nav>


					<jsp:include page="../common/alert.jsp" />

					<div class="card bg-white border">
						<div class="card-body">

							<%-- <h5 class="text-center border-bottom pb-2">Mark Attendance for ${attendance.course.courseName }</h5> --%>
							<h5 class="text-center border-bottom pb-2">Mark Attendance
								for ${courseName}</h5>
								
								<!--FOR VIEW FACULTY WORKLOAD -->	
								<%-- <div class="x_panel">
								<div class="panel-group" role="tablist" aria-multiselectable="true">
								<div class="panel panel-default">
								<div class="panel-heading" role="tab" id="heading-1">
														<h4 class="panel-title" data-toggle="collapse"
																data-target="#viewWorkload" aria-expanded="false"
																aria-controls="viewWorkload">
															View Faculty Workload
														</h4>
													</div>
											<div id="viewWorkload" class="panel-collapse collapse"
														role="tabpanel" aria-labelledby="heading-1">
														<div class="panel-body">
															<div class="row">
															<div class="col-md-6">
																<div class="form-group">
																	<label class="control-label" for="courseForWorkload">Course
																				<span style="color: red">*</span>
																	</label> 
																	<select id="courseForWorkload" class="form-control" required="required">
																		<option value="">Select Course</option>
																		<c:forEach var="listValue" items="${courseList}">
																				<option value="${listValue.id}">${listValue.courseName}</option>
																		</c:forEach>
																	</select>
																</div>
															</div>
															<div class="col-md-6">
																<div class="courseWiseWorkload">
																
									
															</div>
															</div>
															</div>
															</div>
															</div>
															
								</div>
								</div>
								</div> --%>
							<%-- <form:form action="" id="markAttendance" method="post"
											modelAttribute="attendance" enctype="multipart/form-data">

											<form:input path="courseId" type="hidden" />


											<div class="row">
												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<form:label path="courseId" for="courseId">Course</form:label>
														<form:select id="courseIdForForum" path="courseId"
															class="form-control">
															<form:option value="">Select Course</form:option>
															<c:forEach var="course" items="${allCourses}"
																varStatus="status">
																<form:option value="${course.id}">${course.courseName}</form:option>
															</c:forEach>
														</form:select>
													</div>
												</div>
											</div>
										</form:form> --%>
						</div>
					</div>

					<div class="card bg-white border">
						<div class="card-body">
							<h5 class="text-center border-bottom pb-2">Students |
								${fn:length(students)} Records found</h5>
							<form:form action="markAttendance" method="post"
								modelAttribute="attendance">
								
								<form:input path="courseId" type="hidden" id="courseForLec" />
								<div class="row">

									<div class="col-md-4 col-sm-6 col-xs-12 column">
										<div class="form-group">
											<form:label path="lecture" for="lecture">Lecture Time <span
													style="color: red">*</span>
											</form:label>

											<%-- <form:select id="lectureForAttendance" path="lecture"
															class="form-control">
															<form:option value="">Select Lecture</form:option>
															<c:forEach var="lec" items="${flags}"
																varStatus="status">
																<form:option value="${lec.class_date} ${lec.start_time} To ${lec.class_date} ${lec.end_time},${lec.flag}">${lec.class_date} ${lec.start_time} To ${lec.class_date} ${lec.end_time}</form:option>
															</c:forEach>
														</form:select> --%>
											<form:select id="lectureForAttendance" path="lecture"
												class="form-control">
												<form:option value="">Select Lecture</form:option>
												<c:forEach var="lec" items="${flags}" varStatus="status">
													<%-- <c:if
														test="${attendance.lecture eq 'lec.class_date lec.start_time To lec.class_date lec.end_time,lec.flag,lec.maxEndTimeForCourse,lec.multipleCourseId'}">
														<form:option value="${lecture}">${lecture}</form:option>
													</c:if> --%>
													<form:option data-allottedLecture = "${lec.allottedLecture}" data-conductedLecture = "${lec.conductedLecture}" data-isContinueLecture="${lec.isContinueLecture}" data-facultyId="${lec.facultyId}"
														value="${lec.class_date} ${lec.start_time} To ${lec.class_date} ${lec.end_time},${lec.flag},${lec.maxEndTimeForCourse},${lec.multipleCourseId}">${lec.class_date} ${lec.start_time} To ${lec.class_date} ${lec.end_time}</form:option>
												</c:forEach>
											</form:select>
										</div>

									</div>
									<!-- GET LECTURE WISE WORKLOAD 	 -->
									<div class="col-md-4 col-sm-6 col-xs-12 column">
									<div class="workload">
									<c:if test="${not empty attendance.lecture}">
									Allotted Lectures: ${attendance.allottedLecture} <br>
									Conducted Lecture: ${attendance.conductedLecture} <br>
									Remaining Lectures: <c:out value="${remainingLecture}"/>
									</c:if>
									</div>
									</div>
									
									<c:if test="${faculty_attendance eq 'Y'}">	
										<c:if test="${not empty attendance.lecture}">
											<c:if test="${facultyIdList.size() > 1 }">
												<div class="col-md-4 col-sm-6 col-xs-12 column">
												<label>Mark Faculty Attendance</label><br>
												<c:if test="${notMarked}">
													<c:forEach var="faculty" items="${facultyIdList}" varStatus="status">
														<c:if test="${faculty eq username}">
															<form:checkbox value="${faculty}" checked="checked" id="presentFacultyId_${faculty}" path="presentFacultyId" readonly="readonly"/><label>${faculty} (${facultyNameAndId[faculty]})</label><br>
														</c:if>
														<c:if test="${faculty ne username}">
															<form:checkbox value="${faculty}" checked="checked" id="presentFacultyId_${faculty}" path="presentFacultyId" label="${faculty}"/> (${facultyNameAndId[faculty]})<br>
														</c:if>	
													</c:forEach>
												</c:if>
												<c:if test="${notMarked eq 'update'}">
													<c:forEach var="faculty" items="${facultyIdList}" varStatus="status">
														<c:if test="${fn:contains( attendance.presentFacultyId, faculty)}">
															<c:if test="${faculty eq username}">
																<form:checkbox value="${faculty}" checked="checked" id="presentFacultyId_${faculty}" path="presentFacultyId" readonly="readonly"/><label>${faculty} (${facultyNameAndId[faculty]})</label><br>
															</c:if> 
															<c:if test="${faculty ne username}">
																<form:checkbox value="${faculty}" checked="checked" id="presentFacultyId_${faculty}" path="presentFacultyId" label="${faculty}" /> (${facultyNameAndId[faculty]})<br>
															</c:if>
														</c:if>
														<c:if test="${!fn:contains( attendance.presentFacultyId, faculty)}">
															<c:if test="${faculty eq username}">
																<form:checkbox value="${faculty}" id="presentFacultyId_${faculty}" path="presentFacultyId" readonly="readonly"/><label>${faculty} (${facultyNameAndId[faculty]})</label><br>
															</c:if>
															<c:if test="${faculty ne username}">
																<form:checkbox value="${faculty}" id="presentFacultyId_${faculty}" path="presentFacultyId" label="${faculty}"/> (${facultyNameAndId[faculty]})<br>
															</c:if>
														</c:if>	
													</c:forEach>
												</c:if>
												<c:if test="${notMarked eq 'showList'}">
													<c:forEach var="faculty" items="${facultyIdList}" varStatus="status">
													<c:if test="${fn:contains( attendance.presentFacultyId, faculty)}">
														<label style="color : green;"> ${faculty} (${facultyNameAndId[faculty]}) </lable><br>
													</c:if>
													<c:if test="${!fn:contains( attendance.presentFacultyId, faculty)}">
														<label style="color : red;"> ${faculty} (${facultyNameAndId[faculty]})</lable><br>
													</c:if>	
													</c:forEach>
												</c:if>
												</div>
											</c:if>
										</c:if>
									</c:if>
									
									<div class="col-md-4 col-sm-6 col-xs-12 column">
									<c:if test="${not empty attendance.lecture}">
									<c:if test="${notMarked ne 'update' and notMarked ne 'showList'}">
									<c:if test="${attendance.isAbsentAll eq 'N'}">
									<div class="pretty p-switch p-fill p-toggle">
									<label>Mark Absent All</label>
															<form:checkbox value="N" class="custToggle"
																id="isAbsentAll" path="isAbsentAll" />
															<div class="state p-success p-on">
																<label>Yes</label>
															</div>
															<div class="state p-danger p-off">
																<label>No</label>
															</div>
														</div>
									</c:if>
									<c:if test="${attendance.isAbsentAll eq 'Y'}">
									<div class="pretty p-switch p-fill p-toggle">
									<label>Mark Absent All</label>
															<form:checkbox value="Y" class="custToggle"
																id="isAbsentAll" path="isAbsentAll" />
															<div class="state p-success p-on">
																<label>Yes</label>
															</div>
															<div class="state p-danger p-off">
																<label>No</label>
															</div>
														</div>
									</c:if>
									</c:if>
									</c:if>
									</div>


								</div>
								<div class="table-responsive testAssignTable">
									<table class="table-striped  table-hover w-100"
										id="showMyStudents">
										<thead>
											<tr>
												<th>Sr. No.</th>


												<th>SAP IDs <i class="fa fa-sort" aria-hidden="true"
													style="cursor: pointer"></i></th>
												<th>Student Name <i class="fa fa-sort"
													aria-hidden="true" style="cursor: pointer"></i></th>
												<th>Roll No. <i class="fa fa-sort" aria-hidden="true"
													style="cursor: pointer"></i></th>
												<th><c:if test="${notMarked ne 'showList'}">Select</c:if>
													Status <c:if test="${notMarked ne 'showList'}">
														<span style="color: red">*</span>
													</c:if></th>


											</tr>
										</thead>
										<tfoot>
											<tr>
												<th></th>

												<th>SAP IDs</th>
												<th>Roll No.</th>
												<th>Student Name</th>
											</tr>
										</tfoot>
										<tbody>

											<c:forEach var="student" items="${students}"
												varStatus="status">
												<tr>
													<td><c:out value="${status.count}" /></td>


													<td><c:out value="${student.username}" /></td>
													<td><c:out
															value="${student.firstname} ${student.lastname}" /></td>
													<td><c:out value="${student.rollNo}" /></td>
													<td><c:if test="${notMarked eq true}">
															<form:select class="form-control" id="status"
																path="status" placeholder="Attendance Status"
																required="required">
																<c:if test="${attendance.isAbsentAll eq 'N'}">
																<form:option value="Present_${student.username}_${student.id}" selected="true">Present</form:option>
																<form:option value="Absent_${student.username}_${student.id}">Absent</form:option>
																
																
																</c:if>
																<c:if test="${attendance.isAbsentAll eq 'Y'}">
																<form:option value="Present_${student.username}_${student.id}">Present</form:option>
																<form:option value="Absent_${student.username}_${student.id}" selected="true">Absent</form:option>
																</c:if>
															</form:select>
														</c:if> 
														<c:if test="${notMarked eq 'continue'}">
														<c:if test="${attendance.isAbsentAll eq 'Y'}">
														<form:select class="form-control" id="status"
																path="status" placeholder="Attendance Status"
																required="required">
																
																<form:option value="Present_${student.username}_${student.courseId}">Present</form:option>
																<form:option value="Absent_${student.username}_${student.courseId}" selected="true">Absent</form:option>
																
															</form:select>
														</c:if>
														<c:if test="${attendance.isAbsentAll eq 'N' || attendance.isAbsentAll eq null}">
														<c:if test="${student.status eq 'Present' }">
																<form:select class="form-control" id="status"
																	style="color : green;" path="status"
																	placeholder="Attendance Status" required="required">
																	<form:option value="${student.status}_${student.username}_${student.courseId}" selected="true">${student.status} </form:option>
																	<form:option value="Absent_${student.username}_${student.courseId}">Absent</form:option>

																</form:select>
															</c:if>
															<c:if test="${student.status eq 'Absent' }">
																<form:select class="form-control" id="status"
																	style="color : red;" path="status"
																	placeholder="Attendance Status" required="required">
																	<form:option value="${student.status}_${student.username}_${student.courseId}" selected="true">${student.status} </form:option>
																	<form:option value="Present_${student.username}_${student.courseId}">Present</form:option>

																</form:select>
															</c:if>
														</c:if>
														</c:if>
														<c:if test="${notMarked eq 'showList'}">
															<c:if test="${student.status eq 'Present'}">
																<span style="color: green;">Present</span>
															</c:if>
															<c:if test="${student.status eq 'Absent'}">
																<span style="color: red;">Absent</span>
															</c:if>
														</c:if> <c:if test="${notMarked eq 'update'}">


															<c:if test="${student.status eq 'Present' }">
																<form:select class="form-control" id="status"
																	style="color : green;" path="status"
																	placeholder="Attendance Status" required="required">
																	<form:option value="${student.status}_${student.username}_${student.courseId}" selected="true">${student.status} </form:option>
																	<form:option value="Absent_${student.username}_${student.courseId}">Absent</form:option>

																</form:select>
															</c:if>
															<c:if test="${student.status eq 'Absent' }">
																<form:select class="form-control" id="status"
																	style="color : red;" path="status"
																	placeholder="Attendance Status" required="required">
																	<form:option value="${student.status}_${student.username}_${student.courseId}" selected="true">${student.status} </form:option>
																	<form:option value="Present_${student.username}_${student.courseId}">Present</form:option>

																</form:select>
															</c:if>



														</c:if></td>


												</tr>
											</c:forEach>
										</tbody>
									</table>

								</div>
								<div class="clearfix"></div>
								<div class="col-md-4 col-sm-6 col-xs-12 column">
									<div class="form-group">

										<c:if test="${notMarked eq true || notMarked eq 'continue'}">
											<button id="submit" class="btn btn-large btn-primary"
												onclick="return clicked();"
												formaction="saveStudentCourseAttendance">Mark
												Attendance</button>
										</c:if>
										<c:if test="${notMarked eq 'update'}">
											<button id="submit" class="btn btn-large btn-primary"
												onclick="return clicked();"
												formaction="updStudentCourseAttendance">Update
												Attendance</button>
										</c:if>


										<button id="cancel" class="btn btn-large btn-danger"
											formaction="homepage" formnovalidate="formnovalidate">Cancel</button>
									</div>
								</div>
							</form:form>
						</div>
					</div>

					<!-- /page content -->



				</div>

				<!-- SIDEBAR START -->
				<jsp:include page="../common/newSidebar.jsp" />
				<!-- SIDEBAR END -->
				<jsp:include page="../common/footer.jsp" />









<script src="<c:url value="/resources/js/moment.min.js" />"></script>

				<script type="text/javascript">
					var tableToExcel = (function() {
						var uri = 'data:application/vnd.ms-excel;base64,', template = '<html xmlns:o="urn:schemas-microsoft-com:office:office" xmlns:x="urn:schemas-microsoft-com:office:excel" xmlns="http://www.w3.org/TR/REC-html40"><head><!--[if gte mso 9]><xml><x:ExcelWorkbook><x:ExcelWorksheets><x:ExcelWorksheet><x:Name>{worksheet}</x:Name><x:WorksheetOptions><x:DisplayGridlines/></x:WorksheetOptions></x:ExcelWorksheet></x:ExcelWorksheets></x:ExcelWorkbook></xml><![endif]--></head><body><table>{table}</table></body></html>', base64 = function(
								s) {
							return window.btoa(unescape(encodeURIComponent(s)))
						}, format = function(s, c) {
							return s.replace(/{(\w+)}/g, function(m, p) {
								return c[p];
							})
						}
						return function(table, name, filename) {
							if (!table.nodeType)
								table = document.getElementById(table)
							var ctx = {
								worksheet : name || 'Worksheet',
								table : table.innerHTML
							};
							// window.location.href = uri + base64(format(template, ctx));

							document.getElementById("dlink").href = uri
									+ base64(format(template, ctx));
							document.getElementById("dlink").download = filename;
							document.getElementById("dlink").click();
						}
					})()
				</script>

				<%-- <script type="text/javascript"
					src="<c:url value="/resources/js/customDateTimePicker.js" />"></script> --%>


				<!-- <script src="//cdn.ckeditor.com/4.8.0/standard/ckeditor.js"></script>
 -->
				<!-- <script>
					// Replace the <textarea id="editor1"> with a CKEditor
					// instance, using default configuration.
					CKEDITOR.replace('editor1');
				</script> -->
				<!-- <script>
					$(document)
							.ready(
									function() {
										$("#datetimepicker1").on("dp.change",
												function(e) {

													validDateTimepicks();
												}).datetimepicker({
											minDate : new Date(),
											useCurrent : false,
											format : 'YYYY-MM-DD HH:mm'
										});

										$("#datetimepicker2").on("dp.change",
												function(e) {

													validDateTimepicks();
												}).datetimepicker({
											minDate : new Date(),
											useCurrent : false,
											format : 'YYYY-MM-DD HH:mm'
										});

										function validDateTimepicks() {
											if (($('#startDate').val() != '' && $(
													'#startDate').val() != null)
													&& ($('#endDate').val() != '' && $(
															'#endDate').val() != null)) {
												var fromDate = $('#startDate')
														.val();
												var toDate = $('#endDate')
														.val();
												var eDate = new Date(fromDate);
												var sDate = new Date(toDate);
												if (sDate < eDate) {
													alert("endate cannot be smaller than startDate");
													$('#startDate').val("");
													$('#endDate').val("");
												}
											}
										}
									});
				</script> -->



				<script>
					$("#courseIdForForum")
							.on(
									'change',
									function() {

										//alert("Onchange Function called!");
										var selectedValue = $(this).val();
										window.location = '${pageContext.request.contextPath}/markAttendanceForm?courseId='
												+ encodeURIComponent(selectedValue);
										return false;
									});
				</script>

				<script>
				
					 $("#lectureForAttendance")
							.on(
									'change',
									function() {
										var allottedLecture,conductedLecture,isContinueLecture,facultyId;
										facultyId = $(this).find(':selected').attr("data-facultyId");
										isContinueLecture=$(this).find(':selected').attr("data-isContinueLecture");
										<c:if test="${empty attendacne.allottedLecture}">
										allottedLecture = $(this).find(':selected').attr("data-allottedLecture");
										</c:if>
										<c:if test="${not empty attendacne.allottedLecture}">
										allottedLecture = ${attendance.allottedLecture};
										</c:if>
										<c:if test="${empty attendacne.conductedLecture}">
										conductedLecture = $(this).find(':selected').attr("data-conductedLecture");
										</c:if>
										<c:if test="${not empty attendacne.conductedLecture}">
										conductedLecture =  ${attendance.conductedLecture};
										</c:if>
										//alert("allottedLecture " +allottedLecture)
										
										//max end 11pm
										var selectedValue = $(this).val();
										var res = selectedValue.split(" To ");
										var end = res[1].split(",")[0].split(" ")[1];
										
										var lectureDate = res[1].split(",")[0].split(" ")[0];
										//console.log("lectureDate--->"+lectureDate)
										var startDateTime =	lectureDate+" "+ res[0].split(" ")[1];
										//console.log("startDateTime--->"+startDateTime)
										var sdate_test = moment(startDateTime,'DD-MM-yyyy HH:mm:ss');
										var maxEndTimeForCourse = res[1].split(",")[2];
										var attendance_timelimit = ${attendance_timelimit};
										var maxEndTimeOfDayStr = '${maxEndTimeOfDayStr}';
										//console.log("maxEndTimeForCourse--->"+lectureDate+" "+maxEndTimeForCourse)
										var edate_test = moment(lectureDate+" "+maxEndTimeForCourse,'DD-MM-yyyy HH:mm:ss');
										//console.log("edate_test--->"+edate_test)
									 	var end1 = new Date(edate_test);
										end1.setMinutes( end1.getMinutes() + attendance_timelimit );
									 	//console.log("ecountDownDate--->"+end1)
										//console.log("end1--->"+end1)
										var text = moment(new Date(),'DD-MM-yyyy HH:mm:ss');
										//console.log("text--->"+text)
										var allowToMark = lectureDate +" "+maxEndTimeOfDayStr;
										var allowToMark_test = moment(allowToMark,'DD-MM-yyyy HH:mm:ss');
										//console.log("allowToMark_test--->"+allowToMark_test)
										//alert(allowToMark_test)
										if(end1 > allowToMark_test){
											end1 = allowToMark_test;
										}
										//console.log("end1--->"+end1)
										//alert(end1)
										//max end 11pm
										
										if(null == allottedLecture){
										//alert("if "+ allottedLecture+" "+conductedLecture+" "+remainingLecture)
										var remainingLecture = allottedLecture-conductedLecture;
										//alert("remainingLecture " +remainingLecture)
										//alert(remainigLecture);
										//$('.workload').prepend('<c:out value="'+allottedLecture+'"/>')
										//alert("Onchange Function called!");
										var courseValue = $("#courseForLec")
												.val();
										var selectedValue = $(this).val();
										
										if (text >= sdate_test
												&& text <= end1) {
											console.log("remainingLecture " +remainingLecture)
											window.location = '${pageContext.request.contextPath}/markAttendanceForm?lecture='
													+ encodeURIComponent(selectedValue)
													+ '&courseId='
													+ encodeURIComponent(courseValue)
													+ '&isContinueLecture='
													+ encodeURIComponent(isContinueLecture)
													+ '&facultyId='
													+ encodeURIComponent(facultyId);
										} else if (text >= end1) {
											$
													.ajax({
														url : '${pageContext.request.contextPath}/checkLectureTime?lecture='
																+ selectedValue
																+ '&courseId='
																+ courseValue
																+'&facultyId='
																+ facultyId,
														type : 'POST',
														success : function(data) {
															var parseObj = JSON
																	.parse(data);
															if (parseObj.msg == "Lecture time ended!") {
																$(
																		"#lectureForAttendance")
																		.val('');
																swal(
																		{
																			title : "Note",
																			text : parseObj.msg,
																			type : "error"
																		})
																		.then(
																				function() {
																					window.location = '${pageContext.request.contextPath}/markAttendanceForm?courseId='
																							+ encodeURIComponent(courseValue);
																				});
															} else if (parseObj.msg == "show marked students") {
																window.location = '${pageContext.request.contextPath}/markAttendanceForm?lecture='
																		+ encodeURIComponent(selectedValue)
																		+ '&courseId='
																		+ encodeURIComponent(courseValue)
																		+ '&isContinueLecture='
																		+ isContinueLecture
																		+ '&facultyId='
																		+ encodeURIComponent(facultyId);
															}
														},
														error : function(result) {
															alert(result);
														}
													});

										} else{

											$("#lectureForAttendance").val('');
											swal(
													{
														text : "Lecture Not Yet Started!",
														type : "success"
													})
													.then(
															function() {
																window.location = '${pageContext.request.contextPath}/markAttendanceForm?courseId='
																		+ encodeURIComponent(courseValue);
															});

										}
										return false;
										}else{
											//console.log("else "+allottedLecture+" "+conductedLecture+" "+remainingLecture)
											var remainingLecture = allottedLecture-conductedLecture;
											//console.log("remainingLecture " +remainingLecture)
											//alert(remainingLecture);
											//$('.workload').prepend('<c:out value="'+allottedLecture+'"/>')
											//alert("Onchange Function called!");
											var courseValue = $("#courseForLec")
													.val();
											var selectedValue = $(this).val();
											
											if (text >= sdate_test
													&& text <= end1 && remainingLecture != 0 && allottedLecture >= 0.0) {
												window.location = '${pageContext.request.contextPath}/markAttendanceForm?lecture='
														+ encodeURIComponent(selectedValue)
														+ '&courseId='
														+ encodeURIComponent(courseValue)
														+ '&isContinueLecture='
														+ encodeURIComponent(isContinueLecture)
														+ '&facultyId='
														+ encodeURIComponent(facultyId);
												
												//$('.workload').prepend('<div class="workloadDetails">Allotted Lecture: '+allottedLec+'<br>Conducted Lecture: '+conductedLec+'<br>Remaining Lecture: '+remaining+'</div>');
												 
											} else if (text >= end1) {
												$
														.ajax({
															url : '${pageContext.request.contextPath}/checkLectureTime?lecture='
																	+ encodeURIComponent(selectedValue)
																	+ '&courseId='
																	+ encodeURIComponent(courseValue)
																	+ '&facultyId='
																	+ encodeURIComponent(facultyId),
															type : 'POST',
															success : function(data) {
																var parseObj = JSON
																		.parse(data);
																if (parseObj.msg == "Lecture time ended!") {
																	$(
																			"#lectureForAttendance")
																			.val('');
																	swal(
																			{
																				title : "Note",
																				text : parseObj.msg,
																				type : "error"
																			})
																			.then(
																					function() {
																						window.location = '${pageContext.request.contextPath}/markAttendanceForm?courseId='
																								+ encodeURIComponent(courseValue);
																					});
																} else if (parseObj.msg == "show marked students") {
																	window.location = '${pageContext.request.contextPath}/markAttendanceForm?lecture='
																			+ encodeURIComponent(selectedValue)
																			+ '&courseId='
																			+ encodeURIComponent(courseValue)
																			+ '&isContinueLecture='
																			+ encodeURIComponent(isContinueLecture) 
																			+ '&facultyId='
																			+ encodeURIComponent(facultyId);
																}
															},
															error : function(result) {
																alert(result);
															}
														});

											} else if(remainingLecture <= 0.0 && allottedLecture >= 0.0){
												/* window.location = '${pageContext.request.contextPath}/markAttendanceForm?lecture='
													+ encodeURIComponent(selectedValue)
													+ '&courseId='
													+ encodeURIComponent(courseValue)
													+ '&isContinueLecture='
													+ encodeURIComponent(isContinueLecture) 
													+ '&facultyId='
													+ encodeURIComponent(facultyId); */

												 $("#lectureForAttendance").val('');
												swal(
														{
															text : "Your number of allotted lectures are already conducted!",
															type : "success"
														})
														.then(
																function() {
																	window.location = '${pageContext.request.contextPath}/markAttendanceForm?courseId='
																			+ encodeURIComponent(courseValue);
																}); 
																
																
											}else{

												$("#lectureForAttendance").val('');
												swal(
														{
															text : "Lecture Not Yet Started!",
															type : "success"
														})
														.then(
																function() {
																	window.location = '${pageContext.request.contextPath}/markAttendanceForm?courseId='
																			+ encodeURIComponent(courseValue);
																});

											}
											return false;
										}
									});  

					function addMinutes(time, minsToAdd) {
						function D(J) {
							return (J < 10 ? '0' : '') + J;
						}
						;
						var piece = time.split(':');
						var mins = piece[0] * 60 + +piece[1] + +minsToAdd;

						return D(mins % (24 * 60) / 60 | 0) + ':'
								+ D(mins % 60) + ':' + piece[2];
					}
					
					
			
				</script>

				<script>
					var table = $('#showMyStudents')
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
					
					$("#isAbsentAll").click(function(){						
						var selectedValue = $("#lectureForAttendance").val();
						var isContinueLecture=$("#lectureForAttendance").find(':selected').attr("data-isContinueLecture");
						var isAbsentAll = $(this).val();
						console.log("checkBox Changed "+isAbsentAll);
						window.location = '${pageContext.request.contextPath}/markAttendanceForm?lecture='
							+ encodeURIComponent(selectedValue)
							+ "&isAbsentAll="
							+ isAbsentAll
							+ '&isContinueLecture='
							+ isContinueLecture;
					});
				</script>


				</body>
				</html>