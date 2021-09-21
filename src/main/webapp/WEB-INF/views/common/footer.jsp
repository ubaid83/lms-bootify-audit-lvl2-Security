<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

<%--  <c:if test="${webPage.footer}"> --%>








<footer class="text-white m-0">

	<div class="container-fluid">
		<div class="container pt-3 footer-nav">
			<div class="row">
				<div class="col-lg-4 col-md-4 col-sm-12">
				
<%
if(session.getAttribute("studentFeedbackActive")!= "Y"){
%>

					<h6>${userBean.firstname}${userBean.lastname}</h6>
					<hr />
					<ul class="list-unstyled">
						<li><a
							href="${pageContext.request.contextPath}/profileDetails">My
								Profile</a></li>
						<li><a href="${pageContext.request.contextPath}/homepage">Dashboard</a></li>
						<li><a href="${pageContext.request.contextPath}/loggedout">Log
								Out</a></li>

					</ul>
<%
}
%>
				</div>

				<!-- <div class="col-lg-4 col-md-4 col-sm-12">
					<h6>Quick Links</h6>
					<hr />
					<ul class="list-unstyled">
						<li>My Tasks</li>
						<li>Forum</li>
						<li>Announcements</li>
						<li>Teachers</li>
					</ul>
				</div> -->
				<div class="col-lg-4 col-md-4 col-sm-12">
					<h6>Quick Links</h6>
					<hr />
					<ul class="list-unstyled">
						<sec:authorize access="hasRole('ROLE_FACULTY')">
							<li><a
								href="${pageContext.request.contextPath}/viewUserAnnouncementsSearchNew">Search
									Announcements</a></li>
							<li><a
								href="${pageContext.request.contextPath}/facultyTestDashboard">Test</a></li>
							<li><a
								href="${pageContext.request.contextPath}/searchFacultyAssignment">Assignment</a></li>
							<li><a
								href="${pageContext.request.contextPath}/downloadReportMyCourseStudentForm">Report</a></li>
						</sec:authorize>

						<sec:authorize access="hasRole('ROLE_STUDENT')">
<%
if(session.getAttribute("studentFeedbackActive")!= "Y"){
%>
							<li><a
								href="${pageContext.request.contextPath}/viewTestFinal">Test</a></li>
							<li><a
								href="${pageContext.request.contextPath}/viewAssignmentFinal">Assignment</a></li>
							<li><a
								href="${pageContext.request.contextPath}/myCourseForm">Course</a></li>
<%
}
%>
						</sec:authorize>
					</ul>
				</div>

				<div class="col-lg-4 col-md-4 col-sm-12">
					<h6>Download App From Play Store</h6>
					<hr />
					<a
						href="https://play.google.com/store/apps/details?id=com.nmims.app"
						target="_blank"> <img
						src="${pageContext.request.contextPath}/resources/images/portalapp.jpg" />
					</a>
				</div>
			</div>
		</div>
	</div>
	<div class="container-fluid footer-nav2 pb-3">
		<hr />
		<div class="container text-center">
			© Copyright
			<script>document.write(new Date().getFullYear())</script>
			| NMIMS
		</div>
	</div>
	<!--Server Notification -->

	<c:if test="${showModalS eq 'Y'}">

		<div class="modal show showModal" id="NotificationModal" tabindex="-1"
			role="dialog" aria-labelledby="NotificationModal" aria-hidden="true">
			<div class="modal-dialog modal-dialog-centered" role="document">
				<div class="modal-content">
					<div class="modal-header">
						<h5 class="modal-title text-dark" id="NotificationModal">Warm
							Greetings!</h5>
						<!-- <button type="button" class="close text-danger" data-dismiss="modal"
							aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button> -->
					</div>
					<div class="modal-body" value="announcement">

						<p class="text-dark">
							<c:out value="${announcement.announcementDesc}" />
						</p>

						<button type="button"
							class="btn btn-danger hideModal w-100 mt-4 h-45">OK</button>

					</div>
				</div>
			</div>
		</div>
	</c:if>
	
</footer>
</div>
</div>

<%--   </c:if> --%>

<script src="<c:url value="/resources/js/jquery.min.js" />"></script>
<!-- popper -->
<script src="<c:url value="/resources/js/popper.min.js" />"
	type="text/javascript"></script>

<script src="<c:url value="/resources/js/bootstrap.min.js" />"></script>
<script src="<c:url value="/resources/js/jquery.richtext.min.js" />"></script>
<script
	src="<c:url value="/resources/js/vendor/bootstrap-editable.js" />"></script>
<script src="<c:url value="/resources/js/moment.min.js" />"></script>
<script src="<c:url value="/resources/js/daterangepicker.min.js" />"></script>

<script src="<c:url value="/resources/js/jquery.dataTables.min.js" />"></script>
<script
	src="<c:url value="/resources/js/dataTables.bootstrap4.min.js" />"></script>

<script type="text/javascript"
	src="<c:url value="/resources/js/Chart.min.js"/>"></script>
<script type="text/javascript"
	src="<c:url value="/resources/js/timecircles.js" />"></script>
<script>
	var myContextPath = "${pageContext.request.contextPath}"
</script>
<script src="<c:url value="/resources/js/sweetalert.min.js" />"></script>
<script type="text/javascript"
	src="<c:url value="/resources/js/style.js" />"></script>

<script src="<c:url value="/resources/ckeditor/ckeditor.js" />"></script>
<script src="<c:url value="/resources/js/select2.min.js" />"></script>
<script>
if($('.editable').length > 0){
    $('.table')
          .on( 'order.dt',  function () { 
           callEditable();
          })
          .on( 'search.dt', function () {
           callEditable();
          })
          .on( 'page.dt',   function () {
           setTimeout(function(){ callEditable(); }, 100);
          })
          .dataTable();

 function callEditable(){
	 
 $.fn.editable.defaults.mode = 'inline';
 $('.editable').each(function() {
  $(this).editable({
   success : function(response, newValue) {
    obj = JSON.parse(response);
    if (obj.status == 'error') {
     return obj.msg; // msg will be shown in editable
     // form
    }
   }
  });
 })};
 callEditable();
};
</script>


<script>
	$(".ckeditorClass")
			.each(
					function() {
						console.log("id--->" + ($(this).attr('id')));

						CKEDITOR
								.replace(
										$(this).attr('id')
									);

					});
</script>


<script type="text/javascript">
	CKEDITOR
			.replace(
					'editor1',
					{
						
						
					});
</script>







<!--  DashBoard Js -->

<!-- <script>
	$(function() {

		$('#selectSem')
				.on(
						'change',
						function() {

							var selected = $('#selectSem').val();
							console.log(selected);

							if (dashboardPie) {
								dashboardPie.destroy();
							}
							if (dashboardBar) {
								dashboardBar.destroy();
							}

							var dataArr = [];
							$
									.ajax({
										type : 'POST',
										url : myContextPath
												+ '/getTestStatsBySem?acadSess='
												+ selected,
										success : function(data) {

											var parsedObj = JSON.parse(data);
											if (parsedObj
													.hasOwnProperty("passed")) {
												dataArr
														.push(Number(parsedObj.passed));
												dataArr
														.push(Number(parsedObj.pending));
												dataArr
														.push(Number(parsedObj.failed));
												console.log(dataArr);

												dashboardPie = new Chart(
														document
																.getElementById("testPieChart"),
														{
															type : 'pie',
															data : {
																labels : [
																		"Completed",
																		"Pending",
																		"Failed" ],
																datasets : [ {
																	label : "Test",
																	backgroundColor : [
																			"#39b54a",
																			"#feb42f",
																			"#d53439" ],
																	data : dataArr
																} ]
															},
															options : {
																legend : {
																	display : false
																},
																title : {
																	display : true,
																	text : 'Test Pie Chart'
																}
															}
														});
											} else {
												dashboardPie = new Chart(
														document
																.getElementById("testPieChart"),
														{
															type : 'pie',
															data : {
																labels : [
																		"Completed",
																		"Pending",
																		"Failed",
																		"No Data" ],
																datasets : [ {
																	label : "Test",
																	backgroundColor : [
																			"#39b54a",
																			"#feb42f",
																			"#d53439",
																			"#ddd" ],
																	data : [ 0,
																			0,
																			0,
																			1 ]
																} ]
															},
															options : {
																legend : {
																	display : false
																},
																title : {
																	display : true,
																	text : 'Test Pie Chart'
																}
															}
														});
											}
										},
										error : function(result) {
											var parsedObj = JSON.parse(result);
											console.log('error' + parsedObj);
										}
									});

							var dataArr1 = [];
							$
									.ajax({
										type : 'POST',
										url : myContextPath
												+ '/getAssignmentStatsBySem?acadSess='
												+ selected,
										success : function(data) {

											var parsedObj = JSON.parse(data);

											if (parsedObj
													.hasOwnProperty("completed")) {
												dataArr1
														.push(Number(parsedObj.completed));
												dataArr1
														.push(Number(parsedObj.pending));
												dataArr1
														.push(Number(parsedObj.lateSubmitted));
												dataArr1
														.push(Number(parsedObj.rejected));
												console.log(dataArr1);

												dashboardBar = new Chart(
														document
																.getElementById("assignBarChart"),
														{
															type : 'bar',
															data : {
																labels : [
																		"Completed",
																		"Pending",
																		"Late Submitted",
																		"Rejected" ],
																datasets : [ {
																	label : "Assignments",
																	backgroundColor : [
																			"#39b54a",
																			"#feb42f",
																			"#8E5EA2",
																			"#d53439" ],
																	data : dataArr1
																} ]
															},
															options : {
																legend : {
																	display : false
																},
																title : {
																	display : true,
																	text : 'Assignments Bar Chart'
																},
																scales : {
																	yAxes : [ {
																		ticks : {
																			min : 0,
																			stepSize : 20
																		}
																	} ]
																}
															}
														});
											} else {
												dashboardBar = new Chart(
														document
																.getElementById("assignBarChart"),
														{
															type : 'bar',
															data : {
																labels : [
																		"Completed",
																		"Pending",
																		"Late Submitted",
																		"Rejected" ],
																datasets : [ {
																	label : "Assignments",
																	backgroundColor : [
																			"#39b54a",
																			"#feb42f",
																			"#8E5EA2",
																			"#d53439" ],
																	data : [ 0,
																			0,
																			0,
																			0 ]
																} ]
															},
															options : {
																legend : {
																	display : false
																},
																title : {
																	display : true,
																	text : 'Assignments Bar Chart  (No Data)'
																},
																scales : {
																	yAxes : [ {
																		ticks : {
																			min : 0,
																			stepSize : 1
																		}
																	} ]
																}
															}
														});
											}
										},
										error : function(result) {
											var parsedObj = JSON.parse(result);
											console.log('error' + parsedObj);
										}
									});

						});

	});
</script> -->
<!--  END DashBoard Js -->

<!--  myCourse Js -->
<script>
	$(function() {

		$('#semSelect')
				.on(
						'change',
						function() {

							var selected = $('#semSelect').val();
							console.log(selected);

							<c:forEach var='sem' items='${ sessionWiseCourseList }'>
							if (selected == '<c:out value="${sem.key}"/>') {

								document.getElementById("courseListSemWise").innerHTML = ""
								<c:forEach var='group' items='${ sessionWiseCourseList[sem.key] }'>
										+ '<div class="col-lg-3 col-md-3 col-sm-6 col-6 mt-5"> '
										+ '<div class="courseAsset d-flex align-items-start flex-column"> '
										+ '<h6 class="text-uppercase mb-auto">${ group.courseName }</h6>'
										+ '<span class="courseNav"> <a href="${pageContext.request.contextPath}/viewAssignmentFinal?courseId=${group.id}">'
										+ '		<p class="caBg">View Assignment</p>'
										+ '</a> <a href="#">'
										+ '		<p class="ctBg">View Test</p>'
										+ '</a>' + '</span>' + '</div>'
										+ '</div>'
								</c:forEach>;
							}
							</c:forEach>

						});

	});
</script>
<!-- END myCourse Js -->

<!--  viewAssignmentFinal Js -->
<!-- <script>
	$(function() {

		$('#assignSem')
				.on(
						'change',
						function() {

							var selected = $('#assignSem').val();
							console.log(selected);

							<c:forEach var='sem' items='${ sessionWiseCourseList }'>
							if (selected == '<c:out value="${sem.key}"/>') {

								var optionsAsString = "";

								$('#assignCourse').find('option').remove();

								optionsAsString += "<option value='' disabled selected>--SELECT COURSE--</option>";
								<c:forEach var='group' items='${ sessionWiseCourseList[sem.key] }'>
								optionsAsString += "<option value='${ group.id }'>${ group.courseName }</option>";
								</c:forEach>

								console
										.log("optionsAsString"
												+ optionsAsString);

								$('#assignCourse').append(optionsAsString);
							}
							</c:forEach>

							if (AssignmentBar) {
								AssignmentBar.destroy();
							}

							AssignmentBar = new Chart(
									document
											.getElementById("assignmentBarChart"),
									{
										type : 'bar',
										data : {
											labels : [ "Completed", "Pending",
													"Late Submitted",
													"Rejected" ],
											datasets : [ {
												label : "Total",
												backgroundColor : [ "#2ea745",
														"#d69400", "#8e5ea2",
														"#d53439" ],
												data : [ 0, 0, 0, 0 ]
											} ]
										},
										options : {
											responsive : true,
											maintainAspectRatio : false,
											legend : {
												display : false
											},
											scales : {
												yAxes : [ {
													ticks : {
														min : 0,
														stepSize : 1
													}
												} ],
												xAxes : [ {
													ticks : {
														fontSize : 14
													}
												} ]
											},

											title : {
												display : true,
												text : 'Overall Assignment Data (No Data)'
											}
										}
									});

						});

		$('#assignCourse')
				.on(
						'change',
						function() {

							var acadSession = $('#assignSem').val();
							var courseId = $('#assignCourse').val();

							if (AssignmentBar) {
								AssignmentBar.destroy();
							}

							$.ajax({
								type : 'POST',
								url : myContextPath
										+ '/viewAssignmentFinalAjax?courseId='
										+ courseId,
								success : function(data) {

									var parsedObj = JSON.parse(data);
									console.log(parsedObj);

								},
								error : function(result) {
									var parsedObj = JSON.parse(result);
									console.log('error' + parsedObj);
								}
							});

							var dataArr1 = [];
							$
									.ajax({
										type : 'POST',
										url : myContextPath
												+ '/getAssignmentStatsBySem?acadSess='
												+ acadSession + '&courseId='
												+ courseId,
										success : function(data) {

											var parsedObj = JSON.parse(data);

											if (parsedObj
													.hasOwnProperty("completed")) {
												dataArr1
														.push(Number(parsedObj.completed));
												dataArr1
														.push(Number(parsedObj.pending));
												dataArr1
														.push(Number(parsedObj.lateSubmitted));
												dataArr1
														.push(Number(parsedObj.rejected));
												console.log(dataArr1);

												if (AssignmentBar) {
													AssignmentBar.destroy();
												}
												AssignmentBar = new Chart(
														document
																.getElementById("assignmentBarChart"),
														{
															type : 'bar',
															data : {
																labels : [
																		"Completed",
																		"Pending",
																		"Late Submitted",
																		"Rejected" ],
																datasets : [ {
																	label : "Total",
																	backgroundColor : [
																			"#2ea745",
																			"#d69400",
																			"#8e5ea2",
																			"#d53439" ],
																	data : dataArr1
																} ]
															},
															options : {
																responsive : true,
																maintainAspectRatio : false,
																legend : {
																	display : false
																},
																scales : {
																	yAxes : [ {
																		ticks : {
																			min : 0,
																			stepSize : 1
																		}
																	} ],
																	xAxes : [ {
																		ticks : {
																			fontSize : 14
																		}
																	} ]
																},

																title : {
																	display : true,
																	text : 'Overall Assignment Data'
																}
															}
														});
											} else {
												AssignmentBar = new Chart(
														document
																.getElementById("assignmentBarChart"),
														{
															type : 'bar',
															data : {
																labels : [
																		"Completed",
																		"Pending",
																		"Late Submitted",
																		"Rejected" ],
																datasets : [ {
																	label : "Total",
																	backgroundColor : [
																			"#2ea745",
																			"#d69400",
																			"#8e5ea2",
																			"#d53439" ],
																	data : [ 0,
																			0,
																			0,
																			0 ]
																} ]
															},
															options : {
																responsive : true,
																maintainAspectRatio : false,
																legend : {
																	display : false
																},
																scales : {
																	yAxes : [ {
																		ticks : {
																			min : 0,
																			stepSize : 1
																		}
																	} ],
																	xAxes : [ {
																		ticks : {
																			fontSize : 14
																		}
																	} ]
																},

																title : {
																	display : true,
																	text : 'Overall Assignment Data (No Data)'
																}
															}
														});
											}
										},
										error : function(result) {
											var parsedObj = JSON.parse(result);
											console.log('error' + parsedObj);
										}
									});

						});

		$('#viewAssignmentTable').DataTable();

	});
</script> -->
<!-- DataTable JS -->
<script>
	$(".table").DataTable().destroy();
	var table = $('.table').DataTable(
					{
						"dom" : '<"top"i>rt<"bottom"flp><"clear">',
						"lengthMenu" : [ [ 10, 25, 50, -1 ],
								[ 10, 25, 50, "All" ] ],
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
														column.header()).text();
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
																function(d, j) {
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

/* 	  window.checkAll = function checkAll(){
	    	$('input:checkbox[name=students]').prop('checked', true);
	    	return false;
	    }


	    window.uncheckAll = function uncheckAll(){
	    	$('input:checkbox[name=students]').prop('checked', false);
	    	return false;
	    } */
	    
	$('#example-select-all').on('click', function() {
		// Check/uncheck all checkboxes in the table
		var rows = table.rows({
			'search' : 'applied'
		}).nodes();
		$('input[type="checkbox"]', rows).prop('checked', this.checked);
	});
</script>
<!-- END viewAssignmentFinal Js -->

<script>
$(document).ready(function(){
	
	  var custToggleVal = $('.custToggle').val();
	    if (custToggleVal == 'Y') {
	                    $('.custToggle').prop('checked', true);
	    } else {
	                    $('.custToggle').prop('checked', false);
	    }	
	
	var randQReq = $('#randQReq').val();
	if (randQReq == 'Y') {
		$('#randQReq').prop('checked', true);
		$('#inputMaxQ').parent().toggleClass('d-none');
		$('#sameMarks').toggleClass('d-none');
	} else {
		$('#randQReq').prop('checked', false);
	}
	/* ---- */
	var setTestPwd = $('#setTestPwd').val();
	if (setTestPwd == 'Y') {
		$('#setTestPwd').prop('checked', true);
		$('#testPwdVal').parent().toggleClass('d-none');
	} else {
		$('#setTestPwd').prop('checked', false);
	}
	/* ---- */
	var smqChk = $('#smqChk').val();
	if (smqChk == 'Y') {
		$('#smqChk').prop('checked', true);
		$('#marksPerQueIn').parent().toggleClass('d-none');
		
	} else {
		$('#smqChk').prop('checked', false);
	}
	
	var isPeerFacultyForDemo = $('#isPeerFacultyForDemo').val();
	if (isPeerFacultyForDemo == 'Y') {
		$('#isPeerFacultyForDemo').prop('checked', true);
		$('#peerFacultiesForDemo').parent().toggleClass('d-none');
		
	} else {
		$('#isPeerFacultyForDemo').prop('checked', false);
	}
	/* ---- */

	var subAfterEndDate = $('#subAfterEndDate').val();
	if (subAfterEndDate == 'Y') {
		$('#subAfterEndDate').prop('checked', true);
	} else {
		$('#subAfterEndDate').prop('checked', false);
	}
	/* ---- */
	var showResult = $('#showResult').val();
	if (showResult == 'Y') {
		$('#showResult').prop('checked', true);
	} else {
		$('#showResult').prop('checked', false);
	}
	/* ---- */
	var autoAllocateTest = $('#autoAllocateTest').val();
	if (autoAllocateTest == 'Y') {
		$('#autoAllocateTest').prop('checked', true);
	} else {
		$('#autoAllocateTest').prop('checked', false);
	}
	/* ---- */
	var sendEmailAlert = $('#sendEmailAlert').val();
	if (sendEmailAlert == 'Y') {
		$('#sendEmailAlert').prop('checked', true);
	} else {
		$('#sendEmailAlert').prop('checked', false);
	}
	/* ---- */
	var sendSmsAlert = $('#sendSmsAlert').val();
	if (sendSmsAlert == 'Y') {
		$('#sendSmsAlert').prop('checked', true);
	} else {
		$('#sendSmsAlert').prop('checked', false);
	}
	/* ---- */
	var submitByOneInGroup = $('#submitByOneInGroup').val();
	if (submitByOneInGroup == 'Y') {
		$('#submitByOneInGroup').prop('checked', true);
	} else {
		$('#submitByOneInGroup').prop('checked', false);
	}
	/* ---- */
	var rightGrant = $('#rightGrant').val();
	if (rightGrant == 'Y') {
		$('#rightGrant').prop('checked', true);
	} else {
		$('#rightGrant').prop('checked', false);
	}
	/* Multiple assignment */
    var allowAfterEndDate1 = $('#allowAfterEndDate1').val();
	if (rightGrant == 'Y') {
		$('#allowAfterEndDate1').prop('checked', true);
	} else {
		$('#allowAfterEndDate1').prop('checked', false);
	}
	/* ----- */
	var showResultsToStudents1 = $('#showResultsToStudents1').val();
	if (showResultsToStudents1 == 'Y') {
		$('#showResultsToStudents1').prop('checked', true);
	} else {
		$('#showResultsToStudents1').prop('checked', false);
	}
	/* ----- */
	var sendEmailAlert1 = $('#sendEmailAlert1').val();
	if (sendEmailAlert1 == 'Y') {
		$('#sendEmailAlert1').prop('checked', true);
	} else {
		$('#sendEmailAlert1').prop('checked', false);
	}
	/* ----- */
	var sendSmsAlert1 = $('#sendSmsAlert1').val();
	if (sendSmsAlert1 == 'Y') {
		$('#sendSmsAlert1').prop('checked', true);
	} else {
		$('#sendSmsAlert1').prop('checked', false);
	}
	/* ----- */
	var sendEmailAlertToParents1 = $('#sendEmailAlertToParents1').val();
	if (sendEmailAlertToParents1 == 'Y') {
		$('#sendEmailAlertToParents1').prop('checked', true);
	} else {
		$('#sendEmailAlertToParents1').prop('checked', false);
	}
	/* ----- */
	var sendSmsAlertToParents1 = $('#sendSmsAlertToParents1').val();
	if (sendSmsAlertToParents1 == 'Y') {
		$('#sendSmsAlertToParents1').prop('checked', true);
	} else {
		$('#sendSmsAlertToParents1').prop('checked', false);
	}
	
	/* SEND ALERT TO PARENTS */
	var sendEmailAlertToParents = $('#sendEmailAlertParents').val();
	if (sendEmailAlertToParents == 'Y') {
		$('#sendEmailAlertParents').prop('checked', true);
	} else {
		$('#sendEmailAlertParents').prop('checked', false);
	}
	/* ----- */
	var sendSmsAlertToParents = $('#sendSmsAlertToParents').val();
	if (sendSmsAlertToParents == 'Y') {
		$('#sendSmsAlertToParents').prop('checked', true);
	} else {
		$('#sendSmsAlertToParents').prop('checked', false);
	}
	
	
	
	
	
	
	/* ----- */
	var examViewType = $('#examViewType').val();
	if (examViewType == 'Y') {
		$('#examViewType').prop('checked', true);
	} else {
		$('#examViewType').prop('checked', false);
	}
	
	
	/* ----- */
	var setTestPwd = $('#setTestPwd').val();
	if (setTestPwd == 'Y') {
		$('#setTestPwd').prop('checked', true);
	} else {
		$('#setTestPwd').prop('checked', false);
	}
});

</script>


<script>
	/*SET RAND QUESTION AND PASSWORD FOR TEST*/

	$('#randQReq').click(function() {
		var randQReq = $('#randQReq').val();
		$('#inputMaxQ').parent().toggleClass('d-none');
		$('#sameMarks').toggleClass('d-none');
		//IF Y AND N

		if (randQReq == 'Y') {
			$('#randQReq').val('N');
		} else {
			$('#randQReq').val('Y');
		}

	});
	
	$('#setTestPwd').click(function() {
		var setTestPwd = $('#setTestPwd').val();
		$('#testPwdVal').parent().toggleClass('d-none');
		console.log('value test pwsd'+setTestPwd);
		if (setTestPwd == 'N') {
			$('#setTestPwd').val('Y');
		} else {
			$('#setTestPwd').val('N');
		}

	});
	
	$('#smqChk').click(function(e) {
		if($("#testType").val().toLowerCase() !== "mix" || ($("#testType").val().toLowerCase() === "mix") && $("#randQReq").is(":checked") !== true){
			var smqChk = $('#smqChk').val();
			$('#marksPerQueIn').parent().toggleClass('d-none');
	
			if (smqChk == 'Y') {
				$('#smqChk').val('N');
			} else {
				$('#smqChk').val('Y');
			}
		}else {
			e.preventDefault()
		}
	});
	
	$('#isPeerFacultyForDemo').click(function() {
		var isPeerFacultyForDemo = $('#isPeerFacultyForDemo').val();
		$('#peerFacultiesForDemo').parent().toggleClass('d-none');
		//console.log('value test pwsd'+setTestPwd);
		if (isPeerFacultyForDemo == 'N') {
			$('#isPeerFacultyForDemo').val('Y');
		} else {
			$('#isPeerFacultyForDemo').val('N');
		}

	});
	
	$('#subAfterEndDate').click(function() {
		var subAfterEndDate = $('#subAfterEndDate').val();
		if (subAfterEndDate == 'Y') {
			$('#subAfterEndDate').val('N');
		} else {
			$('#subAfterEndDate').val('Y');
		}

	});
	
	$('#showResult').click(function() {
		var showResult = $('#showResult').val();
		if (showResult == 'Y') {
			$('#showResult').val('N');
		} else {
			$('#showResult').val('Y');
		}

	});
	
	$('#autoAllocateTest').click(function() {
		var autoAllocateTest = $('#autoAllocateTest').val();
		if (autoAllocateTest == 'Y') {
			$('#autoAllocateTest').val('N');
		} else {
			$('#autoAllocateTest').val('Y');
		}

	});
	
	$('#sendEmailAlert').click(function() {
		var sendEmailAlert = $('#sendEmailAlert').val();
		if (sendEmailAlert == 'Y') {
			$('#sendEmailAlert').val('N');
		} else {
			$('#sendEmailAlert').val('Y');
		}

	});
	
	$('#sendSmsAlert').click(function() {
		var sendSmsAlert = $('#sendSmsAlert').val();
		if (sendSmsAlert == 'Y') {
			$('#sendSmsAlert').val('N');
		} else {
			$('#sendSmsAlert').val('Y');
		}

	});
	
	$('#submitByOneInGroup').click(function() {
		var submitByOneInGroup = $('#submitByOneInGroup').val();
		if (submitByOneInGroup == 'Y') {
			$('#submitByOneInGroup').val('N');
		} else {
			$('#submitByOneInGroup').val('Y');
		}

	});
	
	$('#rightGrant').click(function() {
		var rightGrant = $('#rightGrant').val();
		if (rightGrant == 'Y') {
			$('#rightGrant').val('N');
		} else {
			$('#rightGrant').val('Y');
		}

	});
	/* Multiple Assignment */
	$('#allowAfterEndDate1').click(function() {
		var allowAfterEndDate1 = $('#allowAfterEndDate1').val();
		if (allowAfterEndDate1 == 'Y') {
			$('#allowAfterEndDate1').val('N');
		} else {
			$('#allowAfterEndDate1').val('Y');
		}

	});
	
	$('#showResultsToStudents1').click(function() {
		var showResultsToStudents1 = $('#showResultsToStudents1').val();
		if (showResultsToStudents1 == 'Y') {
			$('#showResultsToStudents1').val('N');
		} else {
			$('#showResultsToStudents1').val('Y');
		}

	});
	
	$('#sendEmailAlert1').click(function() {
		var sendEmailAlert1 = $('#sendEmailAlert1').val();
		if (sendEmailAlert1 == 'Y') {
			$('#sendEmailAlert1').val('N');
		} else {
			$('#sendEmailAlert1').val('Y');
		}

	});
	
	$('#sendSmsAlert1').click(function() {
		var sendSmsAlert1 = $('#sendSmsAlert1').val();
		if (sendSmsAlert1 == 'Y') {
			$('#sendSmsAlert1').val('N');
		} else {
			$('#sendSmsAlert1').val('Y');
		}

	});
	
	$('#sendEmailAlertToParents1').click(function() {
		var sendEmailAlertToParents1 = $('#sendEmailAlertToParents1').val();
		if (sendEmailAlertToParents1 == 'Y') {
			$('#sendEmailAlertToParents1').val('N');
		} else {
			$('#sendEmailAlertToParents1').val('Y');
		}

	});
	
	$('#sendSmsAlertToParents1').click(function() {
		var sendSmsAlertToParents1 = $('#sendSmsAlertToParents1').val();
		if (sendSmsAlertToParents1 == 'Y') {
			$('#sendSmsAlertToParents1').val('N');
		} else {
			$('#sendSmsAlertToParents1').val('Y');
		}

	});
	
	$('#examViewType').click(function() {
		var examViewType = $('#examViewType').val();
		if (examViewType == 'Y') {
			$('#examViewType').val('N');
		} else {
			$('#examViewType').val('Y');
		}

	});
	
	
	
	$('#sendEmailAlertParents').click(function() {
		var sendEmailAlrtPrnt = $('#sendEmailAlertParents').val();
		if (sendEmailAlrtPrnt == 'Y') {
			$('#sendEmailAlertParents').val('N');
		} else {
			$('#sendEmailAlertParents').val('Y');
		}

	});
	
	$('#sendSmsAlertToParents').click(function() {
		var sendSmsAlrtPrnt = $('#sendSmsAlertToParents').val();
		if (sendSmsAlrtPrnt == 'Y') {
			$('#sendSmsAlertToParents').val('N');
		} else {
			$('#sendSmsAlertToParents').val('Y');
		}

	});
	
	
	/* $('#setTestPwd').click(function() {
		var setTestPwd = $('#setTestPwd').val();
		if (setTestPwd == 'Y') {
			$('#setTestPwd').val('N');
		} else {
			$('#setTestPwd').val('Y');
		}

	}); */

	/*CALLING RICH TEXT EDITOR*/
	/* $('.testDesc').richText(); */

	/*CALLING DATE PICKER*/
	$(function() {

		$('#testDateRangeBtn').daterangepicker({

			"showDropdowns" : true,
			"timePicker" : true,
			"showCustomRangeLabel" : false,
			"alwaysShowCalendars" : true,
			"opens" : "center",
		    "minDate": new Date()

		/* autoUpdateInput: false,
		locale: {
		    cancelLabel: 'Clear'
		} */
		},

		function(start, end, label) {
			var sDate = start.format('YYYY-MM-DD HH:mm:ss');
			var eDate = end.format('YYYY-MM-DD HH:mm:ss');
			$('#startDate').val(sDate + '-' + eDate);
			$('#testStartDate').val(sDate);
			$('#testEndDate').val(eDate);

		});

		/*  $('#startDate').on('apply.daterangepicker', function(ev, picker) {
		     $(this).val(picker.startDate.format('YYYY/MM/DD HH:mm:ss') + ' - ' + picker.endDate.format('YYYY/MM/DD HH:mm:ss'));
		 });
		 
		 $('#testDateRangeBtn').on('apply.daterangepicker', function(ev, picker) {
		     
		$('#startDate').val(picker.startDate.format('YYYY/MM/DD HH:mm:ss') + ' - ' + picker.endDate.format('YYYY/MM/DD HH:mm:ss'));
		
		var x = $('#startDate').val(picker.startDate.format('YYYY/MM/DD HH:mm:ss') + ' - ' + picker.endDate.format('YYYY/MM/DD HH:mm:ss'));
		
		console.log($('#startDate').val(picker.startDate.format('YYYY/MM/DD HH:mm:ss') + ' - ' + picker.endDate.format('YYYY/MM/DD HH:mm:ss')));
		     
		$('#startDate').on('apply.daterangepicker', function(start, end) {
		console.log("A new date selection was made: " + start.format('YYYY-MM-DD') + ' to ' + end.format('YYYY-MM-DD'));
		}); 
		 
		 }); */

		$('#startDate').on('cancel.daterangepicker', function(ev, picker) {
			$(this).val('');
		});
		$('#testDateRangeBtn').on('cancel.daterangepicker',
				function(ev, picker) {
					$('#startDate').val('');
				});

	});
</script>

<script>
	$(function() {

		$('#semDashboardFaculty')
				.on(
						'change',
						function() {

							var selected = $('#semDashboardFaculty').val();
							var vals = selected.split('-')[0];

							console.log(vals);

							<c:forEach var='sem' items='${ sessionWiseCourseListMap }'>
							if (selected == '<c:out value="${sem.key}"/>') {

								document
										.getElementById("courseListSemWisefaculty").innerHTML = ""
								<c:forEach var='group' items='${ sessionWiseCourseListMap[sem.key] }'>
										+ '<div class="col-lg-3 col-md-3 col-sm-6 col-6 mt-5"> '
										+ '<div class="courseAsset d-flex align-items-start flex-column"> '
										+ '<h6 class="text-uppercase mb-auto">${ group.course.courseName }</h6>'
										+ '<span class="courseNav"> <a href="${pageContext.request.contextPath}/viewCourse?id=${group.course.id}">'
										+ '         <p class="caBg">View Course</p>'
										+ '</a>' + '</span>' + '</div>'
										+ '</div>'
								</c:forEach>;
							}
							</c:forEach>

						});

	});
	
	
	
	$(function() {

		$('#semSelectFaculty')
				.on(
						'change',
						function() {

							var selected = $('#semSelectFaculty').val();
							var vals = selected.split('-')[0];

							console.log(vals);

							<c:forEach var='sem' items='${ sessionWiseCourseListMap }'>
							if (selected == '<c:out value="${sem.key}"/>') {

								document.getElementById("courseListSemWisefaculty").innerHTML= ""
                                    <c:forEach var='group' items='${ sessionWiseCourseListMap[sem.key] }'>
                                    + '<div class="col-lg-3 col-md-3 col-sm-6 col-6 mt-5"> '
                                    + '<div class="courseAsset d-flex align-items-start flex-column"> '
                                    + '<h6 class="text-uppercase mb-auto">${ group.course.courseName }</h6>'
                                    + '<span class="courseNav"> <a href="${pageContext.request.contextPath}/createTestForm?courseId=${group.course.id}&acadSession=${group.course.acadSession}&acadYear=${group.course.acadYear}">'
                                    + '         <p class="caBg">Create Test</p>'
                                    + '</a> <a href="${pageContext.request.contextPath}/testList?courseId=${group.course.id}">'
                                    + '         <p class="ctBg">View Test</p>'
                                    + '</a>'
                                    + '</span>'
                                    + '</div>'
                                    + '</div>'

								</c:forEach>;
							}
							</c:forEach>

						});

	});
	
	
	
	
	
$(function() {
		
		$('#semSelectFacultyAssignment').on('change', function() {
			
			var selected = $('#semSelectFacultyAssignment').val();
			var vals = selected.split('-')[0];
			

			
			<c:forEach var='sem' items='${ sessionWiseCourseListMap }'>
				if(selected == '<c:out value="${sem.key}"/>'){
					
					document.getElementById("courseListSemWisefacultyAssignment").innerHTML= ""
						<c:forEach var='group' items='${ sessionWiseCourseListMap[sem.key] }'>
						+ '<div class="col-lg-3 col-md-3 col-sm-6 col-6 mt-5"> '
						+ '<div class="courseAsset d-flex align-items-start flex-column"> '
						+ '<h6 class="text-uppercase mb-auto">${ group.course.courseName }</h6>'
						+ '<span class="courseNav"> <a href="${pageContext.request.contextPath}/createAssignmentFromGroup?courseId=${group.course.id}">'
						+ '		<p class="caBg">Create Group Assignment</p>'
						+ '</a> <a href="${pageContext.request.contextPath}/createAssignmentFromMenu?courseId=${group.course.id}&acadSession=${group.course.acadSession}&acadYear=${group.course.acadYear}">'
						+ '		<p class="ctBg">Create Student Assignement</p>'
						+ '</a>'
						+ '</span>'
						+ '</div>'
						+ '</div>'
						</c:forEach>;
				}
			</c:forEach>

		});

	});
	
	
	
</script>


<script>
	//Timer Logic

	$("#DateCountdown").TimeCircles();
	//For Mobile
	$("#CountDownTimer").TimeCircles({
		count_past_zero : false,
		time : {
			Hours : {
				show : true,
				color : "#d53439"
			},
			Minutes : {
				show : true,
				color : "#2e8a00"
			},
			Seconds : {
				show : true,
				color : "#071e38"
			}

		}
	}).addListener(countdownComplete);

	

	$("#CountDownTimerMobile, #CountDownTimer").TimeCircles({
		circle_bg_color : "#d2d2d2"
	});

	//For larger devices

	$("#PageOpenTimer").TimeCircles();

	var updateTime = function() {
		var date = $("#date").val();
		var time = $("#time").val();
		var datetime = date + ' ' + time + ':00';
		$("#DateCountdown").data('date', datetime).TimeCircles().start();
	}
	$("#date").change(updateTime).keyup(updateTime);
	$("#time").change(updateTime).keyup(updateTime);
	//Stopwatch Timer end
</script>

<!-- End ViewFinal Test -->

</script>




<!-- <script>
	$(function() {

		$('#semSelectFaculty')
				.on(
						'change',
						function() {
							var selected = $('#semSelectFaculty').val();
							var vals = selected.split('-')[0];

							console.log(vals);

							<c:forEach var='sem' items='${sessionWiseCourseListMap}'>
							if (selected == '<c:out value="${sem.key}"/>') {

								document
										.getElementById("courseListSemWisefaculty").innerHTML = ""
								<c:forEach var='group' items='${ sessionWiseCourseListMap[sem.key] }'>
										+ '<div class="col-lg-3 col-md-3 col-sm-6 col-6 mt-5"> '
										+ '<div class="courseAsset d-flex align-items-start flex-column"> '
										+ '<h6 class="text-uppercase mb-auto">${ group.course.courseName }</h6>'
										+ '<span class="courseNav"> <a href="${pageContext.request.contextPath}/createTestForm?courseId=${group.course.id}&acadSession=${group.course.acadSession}&acadYear=${group.course.acadYear}">'
										+ '		<p class="caBg">Create Test</p>'
										+ '</a> <a href="#">'
										+ '		<p class="ctBg">View Test</p>'
										+ '</a>' + '</span>' + '</div>'
										+ '</div>'
								</c:forEach>;
							}
							</c:forEach>
						});
	});
</script> -->

<!--  <script type="text/javascript">


	$(window).bind("pageshow", function() {
		$("#semDashboardFaculty").val("Select Semester");
	});
</script>  -->

<!-- Start newUserAnnouncementList -->
<script>
$(function() {

            $('#stProgram')
                        .on('change',
                                    function() {
                                    
                                    var selected = $('#stProgram').val();
                                    console.log(selected);
                                    var str = "";
                                    <c:forEach var="announcement"
                                          items="${announcementTypeMap['PROGRAM']}" varStatus="status">
                                          if (selected == 'ALL') {
                                                str += ' <div class="announcementItem" data-toggle="modal" '
                                                      
                                                      +' data-target="#modalAnnounceProgram${status.count}"> '
                                                      +' <h6 class="card-title">${announcement.subject}<sup '
                                                      +' class="announcementDate text-danger font-italic"><small><span '
                                                      +' class="fromDate">${announcement.startDate}</span>&nbsp;-&nbsp;<span '
                                                      +' class="toDate">${announcement.endDate}</span></small></sup> '
                                                      +' </h6> '
                                                      
                                                      +' <p class="border-bottom"></p> '
                                                      +' </div> '
                                                      +' <div id="modalAnnouncement position-fixed"> '
                                                      +' <div class="modal fade fnt-13" id="modalAnnounceProgram${status.count}" '
                                                      +' tabindex="-1" role="dialog" '
                                                      +' aria-labelledby="submitAssignmentTitle" aria-hidden="true"> '
                                                      +' <div '
                                                      +' class="modal-dialog modal-dialog-centered modal-dialog-scrollable" '
                                                      +' role="document"> '
                                                      +' <div class="modal-content"> '
                                                      +' <div class="modal-header"> '
                                                      +' <h6 class="modal-title">${announcement.subject}</h6> '
                                                      +' <button type="button" class="close" data-dismiss="modal" '
                                                      +' aria-label="Close"> '
                                                      +' <span aria-hidden="true">&times;</span> '
                                                      +' </button> '
                                                      +' </div> '
                                                      +' <div class="modal-body"> '
                                                      +' <div class="d-flex font-weight-bold"> '
                                                      +' <p class="mr-auto"> '
                                                      +' Start Date: <span>${announcement.startDate}</span> '
                                                      +' </p> '
                                                      +' <p> '
                                                      +' End Date: <span>${announcement.endDate}</span> '
                                                      +' </p> '
                                                      +' </div><c:if test="${announcement.filePath ne null}"> '
                                                      +' <p class="font-weight-bold">Attachment: <a href="sendAnnouncementFile?id=${announcement.id}">Download</a></p> '
                                                      +' </c:if> '

                                                      +' <p class="announcementDetail"> ' ;
                                                      
                                                str   += `${announcement.description}`;
                                                
                                                str += ' </p> ' 
                                                      +' </div> '
                                                      +' <div class="modal-footer"> '
                                                      +' <button type="button" class="btn btn-modalClose" '
                                                      +' data-dismiss="modal">Close</button> '
                                                      +' </div> '
                                                      +' </div> '
                                                      +' </div> '
                                                      +' </div> '
                                                      +' </div> ';
                                          }else if (selected == '<c:out value="${announcement.announcementSubType}"/>') {
                                          str += ' <div class="announcementItem" data-toggle="modal" '
                                          
                                                +' data-target="#modalAnnounceProgram${status.count}"> '
                                                +' <h6 class="card-title">${announcement.subject}<sup '
                                                +' class="announcementDate text-danger font-italic"><small><span '
                                                +' class="fromDate">${announcement.startDate}</span>&nbsp;-&nbsp;<span '
                                                +' class="toDate">${announcement.endDate}</span></small></sup> '
                                                +' </h6> '
                                                
                                                +' <p class="border-bottom"></p> '
                                                +' </div> '
                                                +' <div id="modalAnnouncement position-fixed"> '
                                                +' <div class="modal fade fnt-13" id="modalAnnounceProgram${status.count}" '
                                                +' tabindex="-1" role="dialog" '
                                                +' aria-labelledby="submitAssignmentTitle" aria-hidden="true"> '
                                                +' <div '
                                                +' class="modal-dialog modal-dialog-centered modal-dialog-scrollable" '
                                                +' role="document"> '
                                                +' <div class="modal-content"> '
                                                +' <div class="modal-header"> '
                                                +' <h6 class="modal-title">${announcement.subject}</h6> '
                                                +' <button type="button" class="close" data-dismiss="modal" '
                                                +' aria-label="Close"> '
                                                +' <span aria-hidden="true">&times;</span> '
                                                +' </button> '
                                                +' </div> '
                                                +' <div class="modal-body"> '
                                                +' <div class="d-flex font-weight-bold"> '
                                                +' <p class="mr-auto"> '
                                                +' Start Date: <span>${announcement.startDate}</span> '
                                                +' </p> '
                                                +' <p> '
                                                +' End Date: <span>${announcement.endDate}</span> '
                                                +' </p> '
                                                +' </div><c:if test="${announcement.filePath ne null}"> '
                                                +' <p class="font-weight-bold">Attachment: <a href="sendAnnouncementFile?id=${announcement.id}">Download</a></p> '
                                                +' </c:if> '

                                                +' <p class="announcementDetail"> ' ;
                                                
                                          str   += `${announcement.description}`;
                                          
                                          str += ' </p> ' 
                                                +' </div> '
                                                +' <div class="modal-footer"> '
                                                +' <button type="button" class="btn btn-modalClose" '
                                                +' data-dismiss="modal">Close</button> '
                                                +' </div> '
                                                +' </div> '
                                                +' </div> '
                                                +' </div> '
                                                +' </div> ';
                                          }
                                    </c:forEach>
                                    document.getElementById("programAnn").innerHTML = "" + str;
                  });
            
            $('#stCourse')
            .on('change',
                        function() {
                        
                        var selected = $('#stCourse').val();
                        console.log(selected);
                        var str = "";
                        <c:forEach var="announcement"
                              items="${announcementTypeMap['COURSE']}" varStatus="status">
                              if (selected == 'ALL') {
                                    str += ' <div class="announcementItem" data-toggle="modal" '
                                          
                                          +' data-target="#modalAnnounceCourse${status.count}"> '
                                          +' <h6 class="card-title">${announcement.subject}<sup '
                                          +' class="announcementDate text-danger font-italic"><small><span '
                                          +' class="fromDate">${announcement.startDate}</span>&nbsp;-&nbsp;<span '
                                          +' class="toDate">${announcement.endDate}</span></small></sup> '
                                          +' </h6> '
                                          
                                          +' <p class="border-bottom"></p> '
                                          +' </div> '
                                          +' <div id="modalAnnouncement position-fixed"> '
                                          +' <div class="modal fade fnt-13" id="modalAnnounceCourse${status.count}" '
                                          +' tabindex="-1" role="dialog" '
                                          +' aria-labelledby="submitAssignmentTitle" aria-hidden="true"> '
                                          +' <div '
                                          +' class="modal-dialog modal-dialog-centered modal-dialog-scrollable" '
                                          +' role="document"> '
                                          +' <div class="modal-content"> '
                                          +' <div class="modal-header"> '
                                          +' <h6 class="modal-title">${announcement.subject}</h6> '
                                          +' <button type="button" class="close" data-dismiss="modal" '
                                          +' aria-label="Close"> '
                                          +' <span aria-hidden="true">&times;</span> '
                                          +' </button> '
                                          +' </div> '
                                          +' <div class="modal-body"> '
                                          +' <div class="d-flex font-weight-bold"> '
                                          +' <p class="mr-auto"> '
                                          +' Start Date: <span>${announcement.startDate}</span> '
                                          +' </p> '
                                          +' <p> '
                                          +' End Date: <span>${announcement.endDate}</span> '
                                          +' </p> '
                                          +' </div><c:if test="${announcement.filePath ne null}"> '
                                          +' <p class="font-weight-bold">Attachment: <a href="sendAnnouncementFile?id=${announcement.id}">Download</a></p> '
                                          +' </c:if> '

                                          +' <p class="announcementDetail"> ' ;
                                          
                                    str   += `${announcement.description}`;
                                    
                                    str += ' </p> ' 
                                          +' </div> '
                                          +' <div class="modal-footer"> '
                                          +' <button type="button" class="btn btn-modalClose" '
                                          +' data-dismiss="modal">Close</button> '
                                          +' </div> '
                                          +' </div> '
                                          +' </div> '
                                          +' </div> '
                                          +' </div> ';
                              }else if (selected == '<c:out value="${announcement.announcementSubType}"/>') {
                              str += ' <div class="announcementItem" data-toggle="modal" '
                              
                                    +' data-target="#modalAnnounceCourse${status.count}"> '
                                    +' <h6 class="card-title">${announcement.subject}<sup '
                                    +' class="announcementDate text-danger font-italic"><small><span '
                                    +' class="fromDate">${announcement.startDate}</span>&nbsp;-&nbsp;<span '
                                    +' class="toDate">${announcement.endDate}</span></small></sup> '
                                    +' </h6> '
                                    
                                    +' <p class="border-bottom"></p> '
                                    +' </div> '
                                    +' <div id="modalAnnouncement position-fixed"> '
                                    +' <div class="modal fade fnt-13" id="modalAnnounceCourse${status.count}" '
                                    +' tabindex="-1" role="dialog" '
                                    +' aria-labelledby="submitAssignmentTitle" aria-hidden="true"> '
                                    +' <div '
                                    +' class="modal-dialog modal-dialog-centered modal-dialog-scrollable" '
                                    +' role="document"> '
                                    +' <div class="modal-content"> '
                                    +' <div class="modal-header"> '
                                    +' <h6 class="modal-title">${announcement.subject}</h6> '
                                    +' <button type="button" class="close" data-dismiss="modal" '
                                    +' aria-label="Close"> '
                                    +' <span aria-hidden="true">&times;</span> '
                                    +' </button> '
                                    +' </div> '
                                    +' <div class="modal-body"> '
                                    +' <div class="d-flex font-weight-bold"> '
                                    +' <p class="mr-auto"> '
                                    +' Start Date: <span>${announcement.startDate}</span> '
                                    +' </p> '
                                    +' <p> '
                                    +' End Date: <span>${announcement.endDate}</span> '
                                    +' </p> '
                                    +' </div><c:if test="${announcement.filePath ne null}"> '
                                    +' <p class="font-weight-bold">Attachment: <a href="sendAnnouncementFile?id=${announcement.id}">Download</a></p> '
                                    +' </c:if> '

                                    +' <p class="announcementDetail"> ' ;
                                    
                              str   += `${announcement.description}`;
                              
                              str += ' </p> ' 
                                    +' </div> '
                                    +' <div class="modal-footer"> '
                                    +' <button type="button" class="btn btn-modalClose" '
                                    +' data-dismiss="modal">Close</button> '
                                    +' </div> '
                                    +' </div> '
                                    +' </div> '
                                    +' </div> '
                                    +' </div> ';
                              }
                        </c:forEach>
                        document.getElementById("courseAnn").innerHTML = "" + str;
            });
            
            $('#stInstitute')
            .on('change',
                        function() {
                        
                        var selected = $('#stInstitute').val();
                        console.log(selected);
                        var str = "";
                        <c:forEach var="announcement"
                              items="${announcementTypeMap['INSTITUTE']}" varStatus="status">
                              if (selected == 'ALL') {
                                    str += ' <div class="announcementItem" data-toggle="modal" '
                                          
                                          +' data-target="#modalAnnounceInstitute${status.count}"> '
                                          +' <h6 class="card-title">${announcement.subject}<sup '
                                          +' class="announcementDate text-danger font-italic"><small><span '
                                          +' class="fromDate">${announcement.startDate}</span>&nbsp;-&nbsp;<span '
                                          +' class="toDate">${announcement.endDate}</span></small></sup> '
                                          +' </h6> '
                                          
                                          +' <p class="border-bottom"></p> '
                                          +' </div> '
                                          +' <div id="modalAnnouncement position-fixed"> '
                                          +' <div class="modal fade fnt-13" id="modalAnnounceInstitute${status.count}" '
                                          +' tabindex="-1" role="dialog" '
                                          +' aria-labelledby="submitAssignmentTitle" aria-hidden="true"> '
                                          +' <div '
                                          +' class="modal-dialog modal-dialog-centered modal-dialog-scrollable" '
                                          +' role="document"> '
                                          +' <div class="modal-content"> '
                                          +' <div class="modal-header"> '
                                          +' <h6 class="modal-title">${announcement.subject}</h6> '
                                          +' <button type="button" class="close" data-dismiss="modal" '
                                          +' aria-label="Close"> '
                                          +' <span aria-hidden="true">&times;</span> '
                                          +' </button> '
                                          +' </div> '
                                          +' <div class="modal-body"> '
                                          +' <div class="d-flex font-weight-bold"> '
                                          +' <p class="mr-auto"> '
                                          +' Start Date: <span>${announcement.startDate}</span> '
                                          +' </p> '
                                          +' <p> '
                                          +' End Date: <span>${announcement.endDate}</span> '
                                          +' </p> '
                                          +' </div><c:if test="${announcement.filePath ne null}"> '
                                          +' <p class="font-weight-bold">Attachment: <a href="sendAnnouncementFile?id=${announcement.id}">Download</a></p> '
                                          +' </c:if> '

                                          +' <p class="announcementDetail"> ' ;
                                          
                                    str   += `${announcement.description}`;
                                    
                                    str += ' </p> ' 
                                          +' </div> '
                                          +' <div class="modal-footer"> '
                                          +' <button type="button" class="btn btn-modalClose" '
                                          +' data-dismiss="modal">Close</button> '
                                          +' </div> '
                                          +' </div> '
                                          +' </div> '
                                          +' </div> '
                                          +' </div> ';
                              }else if (selected == '<c:out value="${announcement.announcementSubType}"/>') {
                              str += ' <div class="announcementItem" data-toggle="modal" '
                              
                                    +' data-target="#modalAnnounceInstitute${status.count}"> '
                                    +' <h6 class="card-title">${announcement.subject}<sup '
                                    +' class="announcementDate text-danger font-italic"><small><span '
                                    +' class="fromDate">${announcement.startDate}</span>&nbsp;-&nbsp;<span '
                                    +' class="toDate">${announcement.endDate}</span></small></sup> '
                                    +' </h6> '
                                    
                                    +' <p class="border-bottom"></p> '
                                    +' </div> '
                                    +' <div id="modalAnnouncement position-fixed"> '
                                    +' <div class="modal fade fnt-13" id="modalAnnounceInstitute${status.count}" '
                                    +' tabindex="-1" role="dialog" '
                                    +' aria-labelledby="submitAssignmentTitle" aria-hidden="true"> '
                                    +' <div '
                                    +' class="modal-dialog modal-dialog-centered modal-dialog-scrollable" '
                                    +' role="document"> '
                                    +' <div class="modal-content"> '
                                    +' <div class="modal-header"> '
                                    +' <h6 class="modal-title">${announcement.subject}</h6> '
                                    +' <button type="button" class="close" data-dismiss="modal" '
                                    +' aria-label="Close"> '
                                    +' <span aria-hidden="true">&times;</span> '
                                    +' </button> '
                                    +' </div> '
                                    +' <div class="modal-body"> '
                                    +' <div class="d-flex font-weight-bold"> '
                                    +' <p class="mr-auto"> '
                                    +' Start Date: <span>${announcement.startDate}</span> '
                                    +' </p> '
                                    +' <p> '
                                    +' End Date: <span>${announcement.endDate}</span> '
                                    +' </p> '
                                    +' </div><c:if test="${announcement.filePath ne null}"> '
                                    +' <p class="font-weight-bold">Attachment: <a href="sendAnnouncementFile?id=${announcement.id}">Download</a></p> '
                                    +' </c:if> '

                                    +' <p class="announcementDetail"> ' ;
                                    
                              str   += `${announcement.description}`;
                              
                              str += ' </p> ' 
                                    +' </div> '
                                    +' <div class="modal-footer"> '
                                    +' <button type="button" class="btn btn-modalClose" '
                                    +' data-dismiss="modal">Close</button> '
                                    +' </div> '
                                    +' </div> '
                                    +' </div> '
                                    +' </div> '
                                    +' </div> ';
                              }
                        </c:forEach>
                        document.getElementById("instituteAnn").innerHTML = "" + str;
      });
            
            $('#stLibrary')
            .on('change',
                        function() {
                        
                        var selected = $('#stLibrary').val();
                        console.log(selected);
                        var str = "";
                        <c:forEach var="announcement"
                              items="${announcementTypeMap['LIBRARY']}" varStatus="status">
                              if (selected == 'ALL') {
                                    str += ' <div class="announcementItem" data-toggle="modal" '
                                          
                                          +' data-target="#modalAnnounceLibrary${status.count}"> '
                                          +' <h6 class="card-title">${announcement.subject}<sup '
                                          +' class="announcementDate text-danger font-italic"><small><span '
                                          +' class="fromDate">${announcement.startDate}</span>&nbsp;-&nbsp;<span '
                                          +' class="toDate">${announcement.endDate}</span></small></sup> '
                                          +' </h6> '
                                          
                                          +' <p class="border-bottom"></p> '
                                          +' </div> '
                                          +' <div id="modalAnnouncement position-fixed"> '
                                          +' <div class="modal fade fnt-13" id="modalAnnounceLibrary${status.count}" '
                                          +' tabindex="-1" role="dialog" '
                                          +' aria-labelledby="submitAssignmentTitle" aria-hidden="true"> '
                                          +' <div '
                                          +' class="modal-dialog modal-dialog-centered modal-dialog-scrollable" '
                                          +' role="document"> '
                                          +' <div class="modal-content"> '
                                          +' <div class="modal-header"> '
                                          +' <h6 class="modal-title">${announcement.subject}</h6> '
                                          +' <button type="button" class="close" data-dismiss="modal" '
                                          +' aria-label="Close"> '
                                          +' <span aria-hidden="true">&times;</span> '
                                          +' </button> '
                                          +' </div> '
                                          +' <div class="modal-body"> '
                                          +' <div class="d-flex font-weight-bold"> '
                                          +' <p class="mr-auto"> '
                                          +' Start Date: <span>${announcement.startDate}</span> '
                                          +' </p> '
                                          +' <p> '
                                          +' End Date: <span>${announcement.endDate}</span> '
                                          +' </p> '
                                          +' </div><c:if test="${announcement.filePath ne null}"> '
                                          +' <p class="font-weight-bold">Attachment: <a href="sendAnnouncementFile?id=${announcement.id}">Download</a></p> '
                                          +' </c:if> '

                                          +' <p class="announcementDetail"> ' ;
                                          
                                    str   += `${announcement.description}`;
                                    
                                    str += ' </p> ' 
                                          +' </div> '
                                          +' <div class="modal-footer"> '
                                          +' <button type="button" class="btn btn-modalClose" '
                                          +' data-dismiss="modal">Close</button> '
                                          +' </div> '
                                          +' </div> '
                                          +' </div> '
                                          +' </div> '
                                          +' </div> ';
                              }else if (selected == '<c:out value="${announcement.announcementSubType}"/>') {
                              str += ' <div class="announcementItem" data-toggle="modal" '
                              
                                    +' data-target="#modalAnnounceLibrary${status.count}"> '
                                    +' <h6 class="card-title">${announcement.subject}<sup '
                                    +' class="announcementDate text-danger font-italic"><small><span '
                                    +' class="fromDate">${announcement.startDate}</span>&nbsp;-&nbsp;<span '
                                    +' class="toDate">${announcement.endDate}</span></small></sup> '
                                    +' </h6> '
                                    
                                    +' <p class="border-bottom"></p> '
                                    +' </div> '
                                    +' <div id="modalAnnouncement position-fixed"> '
                                    +' <div class="modal fade fnt-13" id="modalAnnounceLibrary${status.count}" '
                                    +' tabindex="-1" role="dialog" '
                                    +' aria-labelledby="submitAssignmentTitle" aria-hidden="true"> '
                                    +' <div '
                                    +' class="modal-dialog modal-dialog-centered modal-dialog-scrollable" '
                                    +' role="document"> '
                                    +' <div class="modal-content"> '
                                    +' <div class="modal-header"> '
                                    +' <h6 class="modal-title">${announcement.subject}</h6> '
                                    +' <button type="button" class="close" data-dismiss="modal" '
                                    +' aria-label="Close"> '
                                    +' <span aria-hidden="true">&times;</span> '
                                    +' </button> '
                                    +' </div> '
                                    +' <div class="modal-body"> '
                                    +' <div class="d-flex font-weight-bold"> '
                                    +' <p class="mr-auto"> '
                                    +' Start Date: <span>${announcement.startDate}</span> '
                                    +' </p> '
                                    +' <p> '
                                    +' End Date: <span>${announcement.endDate}</span> '
                                    +' </p> '
                                    +' </div><c:if test="${announcement.filePath ne null}"> '
                                    +' <p class="font-weight-bold">Attachment: <a href="sendAnnouncementFile?id=${announcement.id}">Download</a></p> '
                                    +' </c:if> '

                                    +' <p class="announcementDetail"> ' ;
                                    
                              str   += `${announcement.description}`;
                              
                              str += ' </p> ' 
                                    +' </div> '
                                    +' <div class="modal-footer"> '
                                    +' <button type="button" class="btn btn-modalClose" '
                                    +' data-dismiss="modal">Close</button> '
                                    +' </div> '
                                    +' </div> '
                                    +' </div> '
                                    +' </div> '
                                    +' </div> ';
                              }
                        </c:forEach>
                        document.getElementById("libraryAnn").innerHTML = "" + str;
      });
            
            $('#stCounselor')
            .on('change',
                        function() {
                        
                        var selected = $('#stCounselor').val();
                        console.log(selected);
                        var str = "";
                        <c:forEach var="announcement"
                              items="${announcementTypeMap['COUNSELOR']}" varStatus="status">
                              if (selected == 'ALL') {
                                    str += ' <div class="announcementItem" data-toggle="modal" '
                                          
                                          +' data-target="#modalAnnounceCounselor${status.count}"> '
                                          +' <h6 class="card-title">${announcement.subject}<sup '
                                          +' class="announcementDate text-danger font-italic"><small><span '
                                          +' class="fromDate">${announcement.startDate}</span>&nbsp;-&nbsp;<span '
                                          +' class="toDate">${announcement.endDate}</span></small></sup> '
                                          +' </h6> '
                                          
                                          +' <p class="border-bottom"></p> '
                                          +' </div> '
                                          +' <div id="modalAnnouncement position-fixed"> '
                                          +' <div class="modal fade fnt-13" id="modalAnnounceCounselor${status.count}" '
                                          +' tabindex="-1" role="dialog" '
                                          +' aria-labelledby="submitAssignmentTitle" aria-hidden="true"> '
                                          +' <div '
                                          +' class="modal-dialog modal-dialog-centered modal-dialog-scrollable" '
                                          +' role="document"> '
                                          +' <div class="modal-content"> '
                                          +' <div class="modal-header"> '
                                          +' <h6 class="modal-title">${announcement.subject}</h6> '
                                          +' <button type="button" class="close" data-dismiss="modal" '
                                          +' aria-label="Close"> '
                                          +' <span aria-hidden="true">&times;</span> '
                                          +' </button> '
                                          +' </div> '
                                          +' <div class="modal-body"> '
                                          +' <div class="d-flex font-weight-bold"> '
                                          +' <p class="mr-auto"> '
                                          +' Start Date: <span>${announcement.startDate}</span> '
                                          +' </p> '
                                          +' <p> '
                                          +' End Date: <span>${announcement.endDate}</span> '
                                          +' </p> '
                                          +' </div><c:if test="${announcement.filePath ne null}"> '
                                          +' <p class="font-weight-bold">Attachment: <a href="sendAnnouncementFile?id=${announcement.id}">Download</a></p> '
                                          +' </c:if> '

                                          +' <p class="announcementDetail"> ' ;
                                          
                                    str   += `${announcement.description}`;
                                    
                                    str += ' </p> ' 
                                          +' </div> '
                                          +' <div class="modal-footer"> '
                                          +' <button type="button" class="btn btn-modalClose" '
                                          +' data-dismiss="modal">Close</button> '
                                          +' </div> '
                                          +' </div> '
                                          +' </div> '
                                          +' </div> '
                                          +' </div> ';
                              }else if (selected == '<c:out value="${announcement.announcementSubType}"/>') {
                              str += ' <div class="announcementItem" data-toggle="modal" '
                              
                                    +' data-target="#modalAnnounceCounselor${status.count}"> '
                                    +' <h6 class="card-title">${announcement.subject}<sup '
                                    +' class="announcementDate text-danger font-italic"><small><span '
                                    +' class="fromDate">${announcement.startDate}</span>&nbsp;-&nbsp;<span '
                                    +' class="toDate">${announcement.endDate}</span></small></sup> '
                                    +' </h6> '
                                    
                                    +' <p class="border-bottom"></p> '
                                    +' </div> '
                                    +' <div id="modalAnnouncement position-fixed"> '
                                    +' <div class="modal fade fnt-13" id="modalAnnounceCounselor${status.count}" '
                                    +' tabindex="-1" role="dialog" '
                                    +' aria-labelledby="submitAssignmentTitle" aria-hidden="true"> '
                                    +' <div '
                                    +' class="modal-dialog modal-dialog-centered modal-dialog-scrollable" '
                                    +' role="document"> '
                                    +' <div class="modal-content"> '
                                    +' <div class="modal-header"> '
                                    +' <h6 class="modal-title">${announcement.subject}</h6> '
                                    +' <button type="button" class="close" data-dismiss="modal" '
                                    +' aria-label="Close"> '
                                    +' <span aria-hidden="true">&times;</span> '
                                    +' </button> '
                                    +' </div> '
                                    +' <div class="modal-body"> '
                                    +' <div class="d-flex font-weight-bold"> '
                                    +' <p class="mr-auto"> '
                                    +' Start Date: <span>${announcement.startDate}</span> '
                                    +' </p> '
                                    +' <p> '
                                    +' End Date: <span>${announcement.endDate}</span> '
                                    +' </p> '
                                    +' </div><c:if test="${announcement.filePath ne null}"> '
                                    +' <p class="font-weight-bold">Attachment: <a href="sendAnnouncementFile?id=${announcement.id}">Download</a></p> '
                                    +' </c:if> '

                                    +' <p class="announcementDetail"> ' ;
                                    
                              str   += `${announcement.description}`;
                              
                              str += ' </p> ' 
                                    +' </div> '
                                    +' <div class="modal-footer"> '
                                    +' <button type="button" class="btn btn-modalClose" '
                                    +' data-dismiss="modal">Close</button> '
                                    +' </div> '
                                    +' </div> '
                                    +' </div> '
                                    +' </div> '
                                    +' </div> ';
                              }
                        </c:forEach>
                        document.getElementById("counselorAnn").innerHTML = "" + str;
      });
                                    
      });
</script>
<!-- End newUserAnnouncementList -->
<!-- NEW STYLE JS FOR STUDENTS -->
<!--  DashBoard Js -->
<script>
	$(function() {

		$('#selectSem')
				.on(
						'change',
						function() {

							var selected = $('#selectSem').val();
							console.log(selected);

							if (dashboardPie) {
								dashboardPie.destroy();
							}
							if (dashboardBar) {
								dashboardBar.destroy();
							}

							var dataArr = [];
							$
									.ajax({
										type : 'POST',
										url : myContextPath
												+ '/getTestStatsBySem?acadSess='
												+ selected,
										success : function(data) {

											var parsedObj = JSON.parse(data);
											if (parsedObj
													.hasOwnProperty("passed")) {
												
												if(Number(parsedObj.passed) != 0 || Number(parsedObj.pending) != 0 || Number(parsedObj.failed) != 0){
													dataArr
															.push(Number(parsedObj.passed));
													dataArr
															.push(Number(parsedObj.pending));
													dataArr
															.push(Number(parsedObj.failed));
													console.log(dataArr);
	
													dashboardPie = new Chart(
															document
																	.getElementById("testPieChart"),
															{
																type : 'pie',
																data : {
																	labels : [
																			"Completed",
																			"Pending",
																			"Failed" ],
																	datasets : [ {
																		label : "Test",
																		backgroundColor : [
																				"#39b54a",
																				"#feb42f",
																				"#d53439" ],
																		data : dataArr
																	} ]
																},
																options : {
																	legend : {
																		display : false
																	},
																	title : {
																		display : true,
																		text : 'Test Pie Chart'
																	}
																}
															});
													}
												else {
													dashboardPie = new Chart(
															document
																	.getElementById("testPieChart"),
															{
																type : 'pie',
																data : {
																	labels : [
																			"Completed",
																			"Pending",
																			"Failed",
																			"No Data" ],
																	datasets : [ {
																		label : "Test",
																		backgroundColor : [
																				"#39b54a",
																				"#feb42f",
																				"#d53439",
																				"#ddd" ],
																		data : [ 0,
																				0,
																				0,
																				1 ]
																	} ]
																},
																options : {
																	legend : {
																		display : false
																	},
																	title : {
																		display : true,
																		text : 'Test Pie Chart'
																	}
																}
															});
												}
											} else {
												dashboardPie = new Chart(
														document
																.getElementById("testPieChart"),
														{
															type : 'pie',
															data : {
																labels : [
																		"Completed",
																		"Pending",
																		"Failed",
																		"No Data" ],
																datasets : [ {
																	label : "Test",
																	backgroundColor : [
																			"#39b54a",
																			"#feb42f",
																			"#d53439",
																			"#ddd" ],
																	data : [ 0,
																			0,
																			0,
																			1 ]
																} ]
															},
															options : {
																legend : {
																	display : false
																},
																title : {
																	display : true,
																	text : 'Test Pie Chart'
																}
															}
														});
											}
										},
										error : function(result) {
											var parsedObj = JSON.parse(result);
											console.log('error' + parsedObj);
										}
									});

							var dataArr1 = [];
							$
									.ajax({
										type : 'POST',
										url : myContextPath
												+ '/getAssignmentStatsBySem?acadSess='
												+ selected,
										success : function(data) {

											var parsedObj = JSON.parse(data);

											if (parsedObj
													.hasOwnProperty("completed")) {
												dataArr1
														.push(Number(parsedObj.completed));
												dataArr1
														.push(Number(parsedObj.pending));
												dataArr1
														.push(Number(parsedObj.lateSubmitted));
												dataArr1
														.push(Number(parsedObj.rejected));
												console.log(dataArr1);

												dashboardBar = new Chart(
														document
																.getElementById("assignBarChart"),
														{
															type : 'bar',
															data : {
																labels : [
																		"Completed",
																		"Pending",
																		"Late Submitted",
																		"Rejected" ],
																datasets : [ {
																	label : "Assignments",
																	backgroundColor : [
																			"#39b54a",
																			"#feb42f",
																			"#8E5EA2",
																			"#d53439" ],
																	data : dataArr1
																} ]
															},
															options : {
																legend : {
																	display : false
																},
																title : {
																	display : true,
																	text : 'Assignments Bar Chart'
																},
																scales : {
																	yAxes : [ {
																		ticks : {
																			min : 0,
																			stepSize : 20
																		}
																	} ]
																}
															}
														});
											} else {
												dashboardBar = new Chart(
														document
																.getElementById("assignBarChart"),
														{
															type : 'bar',
															data : {
																labels : [
																		"Completed",
																		"Pending",
																		"Late Submitted",
																		"Rejected" ],
																datasets : [ {
																	label : "Assignments",
																	backgroundColor : [
																			"#39b54a",
																			"#feb42f",
																			"#8E5EA2",
																			"#d53439" ],
																	data : [ 0,
																			0,
																			0,
																			0 ]
																} ]
															},
															options : {
																legend : {
																	display : false
																},
																title : {
																	display : true,
																	text : 'Assignments Bar Chart  (No Data)'
																},
																scales : {
																	yAxes : [ {
																		ticks : {
																			min : 0,
																			stepSize : 1
																		}
																	} ]
																}
															}
														});
											}
										},
										error : function(result) {
											var parsedObj = JSON.parse(result);
											console.log('error' + parsedObj);
										}
									});

						});

	});
</script>
<!--  END DashBoard Js -->

<!--  myCourse Js -->
<script>
	$(function() {

		$('#semSelect')
				.on(
						'change',
						function() {

							var selected = $('#semSelect').val();
							console.log(selected);

							<c:forEach var='sem' items='${ sessionWiseCourseList }'>
							if (selected == '<c:out value="${sem.key}"/>') {

								document.getElementById("courseListSemWise").innerHTML = ""
								<c:forEach var='group' items='${ sessionWiseCourseList[sem.key] }'>
										+ '<div class="col-lg-3 col-md-3 col-sm-6 col-6 mt-5"> '
										+ '<div class="courseAsset d-flex align-items-start flex-column"> '
										+ '<h6 class="text-uppercase mb-auto">${ group.courseName }</h6>'
										+ '<span class="courseNav"> <a href="${pageContext.request.contextPath}/viewAssignmentFinal?courseId=${group.id}">'
										+ '		<p class="caBg">View Assignment</p>'
										+ '</a> <a href="${pageContext.request.contextPath}/viewTestFinal?courseId=${group.id}">'
										+ '		<p class="ctBg">View Test</p>'
										+ '</a>' + '</span>' + '</div>'
										+ '</div>'
								</c:forEach>;
							}
							</c:forEach>

						});

	});
</script>
<!-- END myCourse Js -->

<!--  viewAssignmentFinal Js -->
<script>
	$(function() {

		$('#assignSem')
				.on(
						'change',
						function() {

							var selected = $('#assignSem').val();
							console.log(selected);

							<c:forEach var='sem' items='${ sessionWiseCourseList }'>
							if (selected == '<c:out value="${sem.key}"/>') {

								var optionsAsString = "";

								$('#assignCourse').find('option').remove();

								optionsAsString += "<option value='' disabled selected>--SELECT COURSE--</option>";
								<c:forEach var='group' items='${ sessionWiseCourseList[sem.key] }'>
								optionsAsString += "<option value='${ group.id }'>${ group.courseName }</option>";
								</c:forEach>

								console
										.log("optionsAsString"
												+ optionsAsString);

								$('#assignCourse').append(optionsAsString);
							}
							</c:forEach>

							if (AssignmentBar) {
								AssignmentBar.destroy();
							}

							AssignmentBar = new Chart(
									document
											.getElementById("assignmentBarChart"),
									{
										type : 'bar',
										data : {
											labels : [ "Completed", "Pending",
													"Late Submitted",
													"Rejected" ],
											datasets : [ {
												label : "Total",
												backgroundColor : [ "#2ea745",
														"#d69400", "#8e5ea2",
														"#d53439" ],
												data : [ 0, 0, 0, 0 ]
											} ]
										},
										options : {
											responsive : true,
											maintainAspectRatio : false,
											legend : {
												display : false
											},
											scales : {
												yAxes : [ {
													ticks : {
														min : 0,
														stepSize : 1
													}
												} ],
												xAxes : [ {
													ticks : {
														fontSize : 14
													}
												} ]
											},

											title : {
												display : true,
												text : 'Overall Assignment Data (No Data)'
											}
										}
									});

							var table = $('#viewAssignmentTable').DataTable();

							table.clear().draw();

						});

		$('#assignCourse')
				.on(
						'change',
						function() {

							var acadSession = $('#assignSem').val();
							var courseId = $('#assignCourse').val();

							window.location = '${pageContext.request.contextPath}/viewAssignmentFinal?courseId='
								+ encodeURIComponent(courseId);
								

							return false;
							
							
							/* if (AssignmentBar) {
								AssignmentBar.destroy();
							}

							$
									.ajax({
										type : 'POST',
										url : myContextPath
												+ '/viewAssignmentFinalAjax?courseId='
												+ courseId,
										success : function(data) {

											var parsedObj = JSON.parse(data);
											console.log(parsedObj);

											//$("#viewAssignmentTable > tbody").empty();

											var table = $(
													'#viewAssignmentTable')
													.DataTable();

											table.clear().draw();

											var optionsAsString = "";

											for (var i = 0; i < parsedObj.length; i++) {

												var sd = Date
														.parse(parsedObj[i].submissionDate);
												var ed = Date
														.parse(parsedObj[i].endDate);

												var subDate, eDate;
												eDate = new Date(ed);

												if (parsedObj[i].submissionDate) {
													subDate = new Date(sd);
												}

												var tdAsArray = [];

												tdAsArray.push("" + (i + 1));
												tdAsArray
														.push(parsedObj[i].assignmentName);
												tdAsArray
														.push(parsedObj[i].startDate);
												tdAsArray
														.push(parsedObj[i].endDate);
												tdAsArray
														.push(parsedObj[i].maxScore);
												tdAsArray
														.push(parsedObj[i].assignmentType);

												if (parsedObj[i].submissionStatus === 'Y') {
													tdAsArray
															.push("<i class='fas fa-check-circle text-success'></i>Completed");
													tdAsArray
															.push("<input type='button' value='Submitted' disabled/>");
													tdAsArray
															.push("<input data-toggle='modal' data-target='#editAssignment' type='button' value='Edit'/>");
												} else if (parsedObj[i].submissionStatus !== 'Y') {
													if (parsedObj[i].approvalStatus) {
														if (parsedObj[i].approvalStatus !== 'Reject') {
															tdAsArray
																	.push("<i class='fas fa-hourglass-start text-orange'></i>Pending");
															tdAsArray
																	.push("<input data-toggle='modal' data-target='#submitAssignment' type='button' value='Submit'/>");
															tdAsArray
																	.push("<input data-toggle='modal' data-target='#editAssignment' type='button' value='Edit' disabled/>");
														}
													} else if (parsedObj[i].attempts == 0) {
														tdAsArray
																.push("<i class='fas fa-hourglass-start text-orange'></i>Pending");
														tdAsArray
																.push("<input data-toggle='modal' data-target='#submitAssignment' type='button' value='Submit'/>");
														tdAsArray
																.push("<input data-toggle='modal' data-target='#editAssignment' type='button' value='Edit' disabled/>");
													} else if (parsedObj[i].approvalStatus === 'Reject') {
														tdAsArray
																.push("<i class='fas fa-ban text-danger'></i>Rejected");
														tdAsArray
																.push("<input type='button' value='Late Submitted' disabled/>");
														tdAsArray
																.push("<input data-toggle='modal' data-target='#editAssignment' type='button' value='Edit'/>");
													} else if (subDate) {
														if (subDate > eDate) {
															tdAsArray
																	.push("<i class='fas fa-exclamation-circle text-danger'></i>Waiting Approval");
															tdAsArray
																	.push("<input type='button' value='Late Submitted' disabled/>");
															tdAsArray
																	.push("<input data-toggle='modal' data-target='#editAssignment' type='button' value='Edit'/>");
														} else {
															tdAsArray
																	.push("<i class='fas fa-hourglass-start text-orange'></i>Pending");
															tdAsArray
																	.push("<input data-toggle='modal' data-target='#submitAssignment' type='button' value='Submit'/>");
															tdAsArray
																	.push("<input data-toggle='modal' data-target='#editAssignment' type='button' value='Edit' disabled/>");
														}
													} else {
														tdAsArray.push("");
														tdAsArray.push("");
														tdAsArray.push("");
													}
												}

												tdAsArray
														.push("<a href='downloadFile?id="
																+ parsedObj[i].id
																+ "' title='Details'>Download</a>");

												table.row.add(tdAsArray).draw(
														false);
											}

											// $('#viewAssignmentTable > tbody').append(optionsAsString); 

										},
										error : function(result) {
											var parsedObj = JSON.parse(result);
											console.log('error' + parsedObj);
										}
									});

							var dataArr1 = [];
							$
									.ajax({
										type : 'POST',
										url : myContextPath
												+ '/getAssignmentStatsBySem?acadSess='
												+ acadSession + '&courseId='
												+ courseId,
										success : function(data) {

											var parsedObj = JSON.parse(data);

											if (AssignmentBar) {
												AssignmentBar.destroy();
											}

											if (parsedObj
													.hasOwnProperty("completed")) {
												dataArr1
														.push(Number(parsedObj.completed));
												dataArr1
														.push(Number(parsedObj.pending));
												dataArr1
														.push(Number(parsedObj.lateSubmitted));
												dataArr1
														.push(Number(parsedObj.rejected));
												console.log(dataArr1);

												AssignmentBar = new Chart(
														document
																.getElementById("assignmentBarChart"),
														{
															type : 'bar',
															data : {
																labels : [
																		"Completed",
																		"Pending",
																		"Late Submitted",
																		"Rejected" ],
																datasets : [ {
																	label : "Total",
																	backgroundColor : [
																			"#2ea745",
																			"#d69400",
																			"#8e5ea2",
																			"#d53439" ],
																	data : dataArr1
																} ]
															},
															options : {
																responsive : true,
																maintainAspectRatio : false,
																legend : {
																	display : false
																},
																scales : {
																	yAxes : [ {
																		ticks : {
																			min : 0,
																			stepSize : 1
																		}
																	} ],
																	xAxes : [ {
																		ticks : {
																			fontSize : 14
																		}
																	} ]
																},

																title : {
																	display : true,
																	text : 'Overall Assignment Data'
																}
															}
														});
											} else {
												AssignmentBar = new Chart(
														document
																.getElementById("assignmentBarChart"),
														{
															type : 'bar',
															data : {
																labels : [
																		"Completed",
																		"Pending",
																		"Late Submitted",
																		"Rejected" ],
																datasets : [ {
																	label : "Total",
																	backgroundColor : [
																			"#2ea745",
																			"#d69400",
																			"#8e5ea2",
																			"#d53439" ],
																	data : [ 0,
																			0,
																			0,
																			0 ]
																} ]
															},
															options : {
																responsive : true,
																maintainAspectRatio : false,
																legend : {
																	display : false
																},
																scales : {
																	yAxes : [ {
																		ticks : {
																			min : 0,
																			stepSize : 1
																		}
																	} ],
																	xAxes : [ {
																		ticks : {
																			fontSize : 14
																		}
																	} ]
																},

																title : {
																	display : true,
																	text : 'Overall Assignment Data (No Data)'
																}
															}
														});
											}
										},
										error : function(result) {
											var parsedObj = JSON.parse(result);
											console.log('error' + parsedObj);
										}
									}); */

						});

		$('#viewAssignmentTable').DataTable();

	});
</script>
<!-- END viewAssignmentFinal Js -->


<!--  viewTestFinal Js -->
<script>
	$(function() {

		$('#testSem')
				.on(
						'change',
						function() {

							var selected = $('#testSem').val();
							console.log(selected);

							<c:forEach var='sem' items='${ sessionWiseCourseList }'>
							if (selected == '<c:out value="${sem.key}"/>') {

								var optionsAsString = "";

								$('#testCourse').find('option').remove();

								optionsAsString += "<option value='' disabled selected>--SELECT COURSE--</option>";
								<c:forEach var='group' items='${ sessionWiseCourseList[sem.key] }'>
								optionsAsString += "<option value='${ group.id }'>${ group.courseName }</option>";
								</c:forEach>

								console
										.log("optionsAsString"
												+ optionsAsString);

								$('#testCourse').append(optionsAsString);
							}
							</c:forEach>

							if (testBar) {
								testBar.destroy();
							}

							testBar = new Chart(
									document.getElementById("testBarChart"),
									{
										type : 'bar',
										data : {
											labels : [ "Passed", "Pending",
													"Failed" ],
											datasets : [ {
												label : "Total",
												backgroundColor : [ "#2ea745",
														"#d69400", "#d53439" ],
												data : [ 0, 0, 0 ],

											} ]
										},
										options : {
											responsive : true,
											maintainAspectRatio : false,
											legend : {
												display : false
											},
											scales : {
												yAxes : [ {
													ticks : {
														min : 0,
														stepSize : 1
													}
												} ],
												xAxes : [ {
													ticks : {
														fontSize : 14
													}
												} ]
											},

											title : {
												display : true,
												text : 'Overall Assignment Data (no data)'
											}
										}
									});

							var table = $('#viewTestTable').DataTable();

							table.clear().draw();

						});

		$('#testCourse')
				.on(
						'change',
						function() {

							var acadSession = $('#testSem').val();
							var courseId = $('#testCourse').val();
							
							
							window.location = '${pageContext.request.contextPath}/viewTestFinal?courseId='
								+ encodeURIComponent(courseId);
								

							return false;
							
							 /* if (testBar) {
								testBar.destroy();
							}

							$
									.ajax({
										type : 'POST',
										url : myContextPath
												+ '/viewTestFinalAjax?courseId='
												+ courseId,
										success : function(data) {

											var parsedObj = JSON.parse(data);
											console.log(parsedObj);

											

											var table = $('#viewTestTable')
													.DataTable();

											table.clear().draw();

											

											for (var i = 0; i < parsedObj.length; i++) {

												var tdAsArray = [];

												tdAsArray.push("" + (i + 1));
												tdAsArray
														.push(parsedObj[i].testName);
												tdAsArray
														.push(parsedObj[i].acadMonth);
												tdAsArray
														.push(parsedObj[i].acadYear);
												tdAsArray
														.push(parsedObj[i].startDate);
												tdAsArray
														.push(parsedObj[i].endDate);
												tdAsArray
														.push(parsedObj[i].testType);

												if (parsedObj[i].testCompleted === 'Y') {
													tdAsArray
															.push('<i class="fas fa-check-circle text-success"></i> Completed');
													tdAsArray
															.push('<input type="button" value="Give Test" disabled /> </td>');
												} else {
													tdAsArray
															.push('<i class="fas fa-hourglass-start text-orange"></i> Pending');
													tdAsArray
															.push('<input type="button" value="Give Test" class="text-info"/>');
												}

												tdAsArray
														.push('<input type="button" value="View" /> </td>');

												table.row.add(tdAsArray).draw(
														false);
												
											}

											

										},
										error : function(result) {
											var parsedObj = JSON.parse(result);
											console.log('error' + parsedObj);
										}
									});

							var dataArr1 = [];
							$
									.ajax({
										type : 'POST',
										url : myContextPath
												+ '/getTestStatsBySem?acadSess='
												+ acadSession + '&courseId='
												+ courseId,
										success : function(data) {

											var parsedObj = JSON.parse(data);

											if (testBar) {
												testBar.destroy();
											}

											if (parsedObj
													.hasOwnProperty("passed")) {
												dataArr1
														.push(Number(parsedObj.passed));
												dataArr1
														.push(Number(parsedObj.pending));
												dataArr1
														.push(Number(parsedObj.failed));
												console.log(dataArr1);

												testBar = new Chart(
														document
																.getElementById("testBarChart"),
														{
															type : 'bar',
															data : {
																labels : [
																		"Passed",
																		"Pending",
																		"Failed" ],
																datasets : [ {
																	label : "Total",
																	backgroundColor : [
																			"#2ea745",
																			"#d69400",
																			"#d53439" ],
																	data : dataArr1,

																} ]
															},
															options : {
																responsive : true,
																maintainAspectRatio : false,
																legend : {
																	display : false
																},
																scales : {
																	yAxes : [ {
																		ticks : {
																			min : 0,
																			stepSize : 1
																		}
																	} ],
																	xAxes : [ {
																		ticks : {
																			fontSize : 14
																		}
																	} ]
																},

																title : {
																	display : true,
																	text : 'Overall Assignment Data'
																}
															}
														});

											} else {

												testBar = new Chart(
														document
																.getElementById("testBarChart"),
														{
															type : 'bar',
															data : {
																labels : [
																		"Passed",
																		"Pending",
																		"Failed" ],
																datasets : [ {
																	label : "Total",
																	backgroundColor : [
																			"#2ea745",
																			"#d69400",
																			"#d53439" ],
																	data : [ 0,
																			0,
																			0 ],

																} ]
															},
															options : {
																responsive : true,
																maintainAspectRatio : false,
																legend : {
																	display : false
																},
																scales : {
																	yAxes : [ {
																		ticks : {
																			min : 0,
																			stepSize : 1
																		}
																	} ],
																	xAxes : [ {
																		ticks : {
																			fontSize : 14
																		}
																	} ]
																},

																title : {
																	display : true,
																	text : 'Overall Assignment Data (no data)'
																}
															}
														});

											}
										},
										error : function(result) {
											var parsedObj = JSON.parse(result);
											console.log('error' + parsedObj);
										}
									});  */

						});

		$('#viewTestTable').DataTable();

	});
</script>
<!-- END viewTestFinal Js -->

<!-- start gradeListForStudent -->

<script>
	$(function() {

		$('#sGradeSem')
				.on(
						'change',
						function() {
							var selected = $('#sGradeSem').val();
							console.log(selected);
							<c:forEach var='sem' items='${ sessionWiseCourseList }'>
							if (selected == '<c:out value="${sem.key}"/>') {
								var optionsAsString = "";
								$('#sGradeCourse').find('option').remove();
								optionsAsString += "<option value='' disabled selected>--SELECT COURSE--</option>";
								<c:forEach var='group' items='${ sessionWiseCourseList[sem.key] }'>
								optionsAsString += "<option value='${ group.id }'>${ group.courseName }</option>";
								</c:forEach>
								console
										.log("optionsAsString"
												+ optionsAsString);

								$('#sGradeCourse').append(optionsAsString);
							}
							</c:forEach>

						});

		$('#sGradeCourse')
				.on(
						'change',
						function() {

							var selected = $('#sGradeCourse').val();

							console.log($("#sGradeSem").val());

							window.location = '${pageContext.request.contextPath}/gradeCenterForStudents?courseId='
									+ encodeURIComponent(selected)
									+ '&acadSessionStudent='
									+ $("#sGradeSem").val();

							return false;
						});

		$('#viewAssignmentTableStudent').DataTable();
		$('#viewTestTableStudent').DataTable();

	});
</script>

<!-- End gradeListForStudent -->


<!-- start reportParent -->

<script>
	$(function() {

		$('#reportCourseStudent')
				.on(
						'change',
						function() {

							$('.assignWrap, .testWrap').addClass('d-block');

							var selected = $('#reportCourseStudent').val();

							console.log($("#reportCourseStudent").val());

							/*  window.location = '${pageContext.request.contextPath}/reportForParents?courseId='
								+ encodeURIComponent(selected); 
							
							return false; */

							var assignmentStudentBar;
							var testStudentLine;

							$
									.ajax({
										type : 'POST',
										url : myContextPath
												+ '/getAssignmentStatsByCourseParent?courseId='
												+ selected,
										success : function(data) {
											if (assignmentStudentBar) {
												assignmentStudentBar.destroy();
											}
											var dataArr1 = [];
											var dataArr2 = [];
											var dataArr3 = [];
											var parsedObj = JSON.parse(data);
											if (parsedObj.length > 0) {
												for (var i = 0; i < parsedObj.length; i++) {
													dataArr1
															.push(parsedObj[i].assignment.assignmentName);
													/* var str = '{"x":"parsedObj[i].assignment.assignmentName","y":"parsedObj[i].score"}'; */
													dataArr2
															.push(Number(parsedObj[i].score));
													dataArr3
															.push(Number(parsedObj[i].unscored));
												}
												console.log(dataArr1);
												console.log(dataArr2);
												console.log(dataArr3);
												var max = dataArr2
														.reduce(function(a, b) {
															return Math.max(a,
																	b);
														});
												var min = dataArr3
														.reduce(function(a, b) {
															return Math.min(a,
																	b);
														});
												console.log('mathsMax--->'
														+ max);
												console.log('maths--->' + min);
												/* dataArr1.push(parsedObj.unscored); */

												if ($('#assignReportChartStudent').length) {

													assignmentStudentBar = new Chart(
															document
																	.getElementById("assignReportChartStudent"),
															{

																'type' : 'bar',
																'data' : {
																	'labels' : dataArr1,
																	'datasets' : [
																			{
																				label : "Marks Scored",
																				backgroundColor : "#177726",
																				data : dataArr2
																			},
																			{
																				label : "Marks Unscored",
																				backgroundColor : "#ff0000",
																				data : dataArr3
																			} ]
																},
																'options' : {
																	'animation' : {
																		'duration' : 0
																	},
																	'maintainAspectRatio' : false,
																	'responsive' : true,
																	'scales' : {
																		'xAxes' : [ {
																			'barPercentage' : 0.5,
																			'categoryPercentage' : 1,
																			'stacked' : true,
																			'barThickness' : 40
																		} ],
																		'yAxes' : [ {
																			'gridLines' : {
																				'zeroLineColor' : '#000',
																				'zeroLineWidth' : 1,
																				'offsetGridLines' : false
																			},
																			ticks : {
																				stepSize : 10,
																				min : min,
																				max : max

																			}
																		} ]
																	}
																}
															});

													assignmentStudentBar
															.update();
												}

											} else {
												assignmentStudentBar = new Chart(
														document
																.getElementById("assignReportChartStudent"),
														{

															'type' : 'bar',
															'data' : {

																'datasets' : [
																		{
																			label : "Marks Scored",
																			backgroundColor : "#177726",
																			data : [ 0 ]
																		},
																		{
																			label : "Marks Unscored",
																			backgroundColor : "#ff0000",
																			data : [ 0 ]
																		} ]
															},
															'options' : {
																'animation' : {
																	'duration' : 0
																},
																'maintainAspectRatio' : false,
																'responsive' : true,
																'scales' : {
																	'xAxes' : [ {
																		'barPercentage' : 0.5,
																		'categoryPercentage' : 1,
																		'stacked' : true,
																		'barThickness' : 40
																	} ],
																	'yAxes' : [ {
																		'gridLines' : {
																			'zeroLineColor' : '#000',
																			'zeroLineWidth' : 1,
																			'offsetGridLines' : false
																		},
																		ticks : {
																			stepSize : 10,
																			min : 0,
																			max : 100

																		}
																	} ]
																}
															}
														});

												assignmentStudentBar.update();

											}
										},
										error : function(result) {
											var parsedObj = JSON.parse(result);
											console.log('error' + parsedObj);
										}

									});

							$
									.ajax({
										type : 'POST',
										url : myContextPath
												+ '/getTestStatsByCourseParent?courseId='
												+ selected,
										success : function(data) {
											if (testStudentLine) {
												testStudentLine.destroy();
											}
											var dataArr1 = [];
											var dataArr2 = [];
											var dataArr3 = [];

											var parsedObj = JSON.parse(data);
											console.log(parsedObj);
											if (parsedObj.length > 0) {
												for (var i = 0; i < parsedObj.length; i++) {
													console
															.log(parsedObj[i].testName);
													console
															.log(parsedObj[i].stringscore);
													console
															.log(parsedObj[i].unscored);
													dataArr1
															.push(parsedObj[i].testName);
													dataArr2
															.push(Number(parsedObj[i].stringscore));
													dataArr3
															.push(Number(parsedObj[i].unscored));
												}
												console.log(dataArr1);
												console.log(dataArr2);
												console.log(dataArr3);
												var max = dataArr2
														.reduce(function(a, b) {
															return Math.max(a,
																	b);
														});
												var min = dataArr3
														.reduce(function(a, b) {
															return Math.min(a,
																	b);
														});
												console.log('mathsMaxt--->'
														+ max);
												console.log('mathst--->' + min);
												/* dataArr1.push(parsedObj.unscored); */

												if ($('#testReportChartStudent').length) {

													testStudentLine = new Chart(
															document
																	.getElementById("testReportChartStudent"),
															{
																'type' : 'line',
																'data' : {
																	'labels' : dataArr1,
																	'datasets' : [
																			{
																				label : "Marks Scored",

																				data : dataArr2,
																				categoryPercentage : 1,
																				lineTension : 0,
																				fill : true,
																				backgroundColor : "rgb(23, 119, 38, 0.5)",
																				pointHitRadius : 10,
																				pointRadius : 5,
																				pointHoverRadius : 7,
																				borderColor : "#177726"
																			},
																			{
																				label : "Marks Unscored",
																				data : dataArr3,
																				categoryPercentage : 1,
																				lineTension : 0,

																				fill : true,
																				pointHitRadius : 10,
																				pointRadius : 5,
																				pointHoverRadius : 7,
																				backgroundColor : "rgb(214, 0, 0, 0.5)",
																				borderColor : "#ff0000"
																			} ]
																},
																'options' : {
																	'animation' : {
																		'duration' : 0
																	},
																	'maintainAspectRatio' : false,
																	'responsive' : true,
																	'scales' : {
																		'xAxes' : [ {
																			'barPercentage' : 0.5,
																			'categoryPercentage' : 1,
																			'stacked' : true,
																			'barThickness' : 40
																		} ],
																		'yAxes' : [ {
																			'gridLines' : {
																				'zeroLineColor' : '#000',
																				'zeroLineWidth' : 1,
																				'offsetGridLines' : false
																			},
																			ticks : {
																				stepSize : 10,
																				min : min,
																				max : max

																			}
																		} ]
																	}
																}

															});

													testStudentLine.update();
												}

											} else {
												testStudentLine = new Chart(
														document
																.getElementById("testReportChartStudent"),
														{

															'type' : 'line',
															'data' : {
																'datasets' : [
																		{
																			label : "Marks Scored",
																			backgroundColor : "#177726",
																			data : [ 0 ]
																		},
																		{
																			label : "Marks Unscored",
																			backgroundColor : "#ff0000",
																			data : [ 0 ]
																		} ]
															},
															'options' : {
																'animation' : {
																	'duration' : 0
																},
																'maintainAspectRatio' : false,
																'responsive' : true,
																'scales' : {
																	'xAxes' : [ {
																		'barPercentage' : 0.5,
																		'categoryPercentage' : 1,
																		'stacked' : true
																	} ],
																	'yAxes' : [ {
																		'gridLines' : {
																			'zeroLineColor' : '#000',
																			'zeroLineWidth' : 1,
																			'offsetGridLines' : false
																		},
																		ticks : {
																			stepSize : 10,
																			min : 0,
																			max : 100
																		}
																	} ]
																}
															}
														});

												testStudentLine.update();

											}

										},
										error : function(result) {
											var parsedObj = JSON.parse(result);
											console.log('error' + parsedObj);
										}
									});

						});

	});
</script>


<!-- End reportParent -->

<!-- Start newUserAnnouncementList -->
<!-- 
<script>
	$(function() {

		$('#stProgram')
				.on(
						'change',
						function() {
							var selected = $('#stProgram').val();
							console.log(selected);
							var str = "";
							<c:forEach var="announcement"
			items="${announcementTypeMap['PROGRAM']}" varStatus="status">
							if (selected == 'ALL') {
								str += ' <div class="announcementItem" data-toggle="modal" '
										+' data-target="#modalAnnounceProgram${status.count}"> '
										+ ' <h6 class="card-title">${announcement.subject}<sup '
										+' class="announcementDate text-danger font-italic"><small><span '
										+' class="fromDate">${announcement.startDate}</span>&nbsp;-&nbsp;<span '
										+' class="toDate">${announcement.endDate}</span></small></sup> '
										+ ' </h6> '
										+ ' <p class="border-bottom"></p> '
										+ ' </div> '
										+ ' <div id="modalAnnouncement position-fixed"> '
										+ ' <div class="modal fade fnt-13" id="modalAnnounceProgram${status.count}" '
										+' tabindex="-1" role="dialog" '
										+' aria-labelledby="submitAssignmentTitle" aria-hidden="true"> '
										+ ' <div '
										+' class="modal-dialog modal-dialog-centered modal-dialog-scrollable" '
										+' role="document"> '
										+ ' <div class="modal-content"> '
										+ ' <div class="modal-header"> '
										+ ' <h6 class="modal-title">${announcement.subject}</h6> '
										+ ' <button type="button" class="close" data-dismiss="modal" '
										+' aria-label="Close"> '
										+ ' <span aria-hidden="true">&times;</span> '
										+ ' </button> '
										+ ' </div> '
										+ ' <div class="modal-body"> '
										+ ' <div class="d-flex font-weight-bold"> '
										+ ' <p class="mr-auto"> '
										+ ' Start Date: <span>${announcement.startDate}</span> '
										+ ' </p> '
										+ ' <p> '
										+ ' End Date: <span>${announcement.endDate}</span> '
										+ ' </p> '
										+ ' </div><c:if test="${announcement.filePath ne null}"> '
										+ ' <p class="font-weight-bold">Attachment: <a href="sendAnnouncementFile?id=${announcement.id}">Download</a></p> '
										+ ' </c:if> '

										+ ' <p class="announcementDetail"> ';
								 	str += `${announcement.description}`;
									str += ' </p> '
										+ ' </div> '
										+ ' <div class="modal-footer"> '
										+ ' <button type="button" class="btn btn-modalClose" '
										+' data-dismiss="modal">Close</button> '
										+ ' </div> ' + ' </div> ' + ' </div> '
										+ ' </div> ' + ' </div> ';
							} else if (selected == '<c:out value="${announcement.announcementSubType}"/>') {
								str += ' <div class="announcementItem" data-toggle="modal" '
										+' data-target="#modalAnnounceProgram${status.count}"> '
										+ ' <h6 class="card-title">${announcement.subject}<sup '
										+' class="announcementDate text-danger font-italic"><small><span '
										+' class="fromDate">${announcement.startDate}</span>&nbsp;-&nbsp;<span '
										+' class="toDate">${announcement.endDate}</span></small></sup> '
										+ ' </h6> '
										+ ' <p class="border-bottom"></p> '
										+ ' </div> '
										+ ' <div id="modalAnnouncement position-fixed"> '
										+ ' <div class="modal fade fnt-13" id="modalAnnounceProgram${status.count}" '
										+' tabindex="-1" role="dialog" '
										+' aria-labelledby="submitAssignmentTitle" aria-hidden="true"> '
										+ ' <div '
										+' class="modal-dialog modal-dialog-centered modal-dialog-scrollable" '
										+' role="document"> '
										+ ' <div class="modal-content"> '
										+ ' <div class="modal-header"> '
										+ ' <h6 class="modal-title">${announcement.subject}</h6> '
										+ ' <button type="button" class="close" data-dismiss="modal" '
										+' aria-label="Close"> '
										+ ' <span aria-hidden="true">&times;</span> '
										+ ' </button> '
										+ ' </div> '
										+ ' <div class="modal-body"> '
										+ ' <div class="d-flex font-weight-bold"> '
										+ ' <p class="mr-auto"> '
										+ ' Start Date: <span>${announcement.startDate}</span> '
										+ ' </p> '
										+ ' <p> '
										+ ' End Date: <span>${announcement.endDate}</span> '
										+ ' </p> '
										+ ' </div><c:if test="${announcement.filePath ne null}"> '
										+ ' <p class="font-weight-bold">Attachment: <a href="sendAnnouncementFile?id=${announcement.id}">Download</a></p> '
										+ ' </c:if> '

										+ ' <p class="announcementDetail"> ';
								 	str += `${announcement.description}`; 
									str += ' </p> '
										+ ' </div> '
										+ ' <div class="modal-footer"> '
										+ ' <button type="button" class="btn btn-modalClose" '
										+' data-dismiss="modal">Close</button> '
										+ ' </div> ' + ' </div> ' + ' </div> '
										+ ' </div> ' + ' </div> ';
							}
							</c:forEach>
							document.getElementById("programAnn").innerHTML = ""
									+ str;
						});
		
		$('#stCourse')
		.on('change',
		function() {
		var selected = $('#stCourse').val();
		console.log(selected);
		var str = "";
		<c:forEach var="announcement"
		items="${announcementTypeMap['COURSE']}" varStatus="status">
		if (selected == 'ALL') {
		str += ' <div class="announcementItem" data-toggle="modal" '
		+' data-target="#modalAnnounceCourse${status.count}"> '
		+' <h6 class="card-title">${announcement.subject}<sup '
		+' class="announcementDate text-danger font-italic"><small><span '
		+' class="fromDate">${announcement.startDate}</span>&nbsp;-&nbsp;<span '
		+' class="toDate">${announcement.endDate}</span></small></sup> '
		+' </h6> '
		+' <p class="border-bottom"></p> '
		+' </div> '
		+' <div id="modalAnnouncement position-fixed"> '
		+' <div class="modal fade fnt-13" id="modalAnnounceCourse${status.count}" '
		+' tabindex="-1" role="dialog" '
		+' aria-labelledby="submitAssignmentTitle" aria-hidden="true"> '
		+' <div '
		+' class="modal-dialog modal-dialog-centered modal-dialog-scrollable" '
		+' role="document"> '
		+' <div class="modal-content"> '
		+' <div class="modal-header"> '
		+' <h6 class="modal-title">${announcement.subject}</h6> '
		+' <button type="button" class="close" data-dismiss="modal" '
		+' aria-label="Close"> '
		+' <span aria-hidden="true">&times;</span> '
		+' </button> '
		+' </div> '
		+' <div class="modal-body"> '
		+' <div class="d-flex font-weight-bold"> '
		+' <p class="mr-auto"> '
		+' Start Date: <span>${announcement.startDate}</span> '
		+' </p> '
		+' <p> '
		+' End Date: <span>${announcement.endDate}</span> '
		+' </p> '
		+' </div><c:if test="${announcement.filePath ne null}"> '
		+' <p class="font-weight-bold">Attachment: <a href="sendAnnouncementFile?id=${announcement.id}">Download</a></p> '
		+' </c:if> '

		+' <p class="announcementDetail"> ' ;
		str  += `${announcement.description}`;
		str += ' </p> ' 
		+' </div> '
		+' <div class="modal-footer"> '
		+' <button type="button" class="btn btn-modalClose" '
		+' data-dismiss="modal">Close</button> '
		+' </div> '
		+' </div> '
		+' </div> '
		+' </div> '
		+' </div> ';
		}else if (selected == '<c:out value="${announcement.announcementSubType}"/>') {
		str += ' <div class="announcementItem" data-toggle="modal" '
		+' data-target="#modalAnnounceCourse${status.count}"> '
		+' <h6 class="card-title">${announcement.subject}<sup '
		+' class="announcementDate text-danger font-italic"><small><span '
		+' class="fromDate">${announcement.startDate}</span>&nbsp;-&nbsp;<span '
		+' class="toDate">${announcement.endDate}</span></small></sup> '
		+' </h6> '
		+' <p class="border-bottom"></p> '
		+' </div> '
		+' <div id="modalAnnouncement position-fixed"> '
		+' <div class="modal fade fnt-13" id="modalAnnounceCourse${status.count}" '
		+' tabindex="-1" role="dialog" '
		+' aria-labelledby="submitAssignmentTitle" aria-hidden="true"> '
		+' <div '
		+' class="modal-dialog modal-dialog-centered modal-dialog-scrollable" '
		+' role="document"> '
		+' <div class="modal-content"> '
		+' <div class="modal-header"> '
		+' <h6 class="modal-title">${announcement.subject}</h6> '
		+' <button type="button" class="close" data-dismiss="modal" '
		+' aria-label="Close"> '
		+' <span aria-hidden="true">&times;</span> '
		+' </button> '
		+' </div> '
		+' <div class="modal-body"> '
		+' <div class="d-flex font-weight-bold"> '
		+' <p class="mr-auto"> '
		+' Start Date: <span>${announcement.startDate}</span> '
		+' </p> '
		+' <p> '
		+' End Date: <span>${announcement.endDate}</span> '
		+' </p> '
		+' </div><c:if test="${announcement.filePath ne null}"> '
		+' <p class="font-weight-bold">Attachment: <a href="sendAnnouncementFile?id=${announcement.id}">Download</a></p> '
		+' </c:if> '

		+' <p class="announcementDetail"> ' ;
		str  += `${announcement.description}`;
		str += ' </p> ' 
		+' </div> '
		+' <div class="modal-footer"> '
		+' <button type="button" class="btn btn-modalClose" '
		+' data-dismiss="modal">Close</button> '
		+' </div> '
		+' </div> '
		+' </div> '
		+' </div> '
		+' </div> ';
		}
		</c:forEach>
		document.getElementById("courseAnn").innerHTML = "" + str;
		});
		$('#stInstitute')
		.on('change',
		function() {
		var selected = $('#stInstitute').val();
		console.log(selected);
		var str = "";
		<c:forEach var="announcement"
		items="${announcementTypeMap['INSTITUTE']}" varStatus="status">
		if (selected == 'ALL') {
		str += ' <div class="announcementItem" data-toggle="modal" '
		+' data-target="#modalAnnounceInstitute${status.count}"> '
		+' <h6 class="card-title">${announcement.subject}<sup '
		+' class="announcementDate text-danger font-italic"><small><span '
		+' class="fromDate">${announcement.startDate}</span>&nbsp;-&nbsp;<span '
		+' class="toDate">${announcement.endDate}</span></small></sup> '
		+' </h6> '
		+' <p class="border-bottom"></p> '
		+' </div> '
		+' <div id="modalAnnouncement position-fixed"> '
		+' <div class="modal fade fnt-13" id="modalAnnounceInstitute${status.count}" '
		+' tabindex="-1" role="dialog" '
		+' aria-labelledby="submitAssignmentTitle" aria-hidden="true"> '
		+' <div '
		+' class="modal-dialog modal-dialog-centered modal-dialog-scrollable" '
		+' role="document"> '
		+' <div class="modal-content"> '
		+' <div class="modal-header"> '
		+' <h6 class="modal-title">${announcement.subject}</h6> '
		+' <button type="button" class="close" data-dismiss="modal" '
		+' aria-label="Close"> '
		+' <span aria-hidden="true">&times;</span> '
		+' </button> '
		+' </div> '
		+' <div class="modal-body"> '
		+' <div class="d-flex font-weight-bold"> '
		+' <p class="mr-auto"> '
		+' Start Date: <span>${announcement.startDate}</span> '
		+' </p> '
		+' <p> '
		+' End Date: <span>${announcement.endDate}</span> '
		+' </p> '
		+' </div><c:if test="${announcement.filePath ne null}"> '
		+' <p class="font-weight-bold">Attachment: <a href="sendAnnouncementFile?id=${announcement.id}">Download</a></p> '
		+' </c:if> '

		+' <p class="announcementDetail"> ' ;
		str  += `${announcement.description}`;
		str += ' </p> ' 
		+' </div> '
		+' <div class="modal-footer"> '
		+' <button type="button" class="btn btn-modalClose" '
		+' data-dismiss="modal">Close</button> '
		+' </div> '
		+' </div> '
		+' </div> '
		+' </div> '
		+' </div> ';
		}else if (selected == '<c:out value="${announcement.announcementSubType}"/>') {
		str += ' <div class="announcementItem" data-toggle="modal" '
		+' data-target="#modalAnnounceInstitute${status.count}"> '
		+' <h6 class="card-title">${announcement.subject}<sup '
		+' class="announcementDate text-danger font-italic"><small><span '
		+' class="fromDate">${announcement.startDate}</span>&nbsp;-&nbsp;<span '
		+' class="toDate">${announcement.endDate}</span></small></sup> '
		+' </h6> '
		+' <p class="border-bottom"></p> '
		+' </div> '
		+' <div id="modalAnnouncement position-fixed"> '
		+' <div class="modal fade fnt-13" id="modalAnnounceInstitute${status.count}" '
		+' tabindex="-1" role="dialog" '
		+' aria-labelledby="submitAssignmentTitle" aria-hidden="true"> '
		+' <div '
		+' class="modal-dialog modal-dialog-centered modal-dialog-scrollable" '
		+' role="document"> '
		+' <div class="modal-content"> '
		+' <div class="modal-header"> '
		+' <h6 class="modal-title">${announcement.subject}</h6> '
		+' <button type="button" class="close" data-dismiss="modal" '
		+' aria-label="Close"> '
		+' <span aria-hidden="true">&times;</span> '
		+' </button> '
		+' </div> '
		+' <div class="modal-body"> '
		+' <div class="d-flex font-weight-bold"> '
		+' <p class="mr-auto"> '
		+' Start Date: <span>${announcement.startDate}</span> '
		+' </p> '
		+' <p> '
		+' End Date: <span>${announcement.endDate}</span> '
		+' </p> '
		+' </div><c:if test="${announcement.filePath ne null}"> '
		+' <p class="font-weight-bold">Attachment: <a href="sendAnnouncementFile?id=${announcement.id}">Download</a></p> '
		+' </c:if> '

		+' <p class="announcementDetail"> ' ;
		str  += `${announcement.description}`;
		str += ' </p> ' 
		+' </div> '
		+' <div class="modal-footer"> '
		+' <button type="button" class="btn btn-modalClose" '
		+' data-dismiss="modal">Close</button> '
		+' </div> '
		+' </div> '
		+' </div> '
		+' </div> '
		+' </div> ';
		}
		</c:forEach>
		document.getElementById("instituteAnn").innerHTML = "" + str;
		});
		$('#stLibrary')
		.on('change',
		function() {
		var selected = $('#stLibrary').val();
		console.log(selected);
		var str = "";
		<c:forEach var="announcement"
		items="${announcementTypeMap['LIBRARY']}" varStatus="status">
		if (selected == 'ALL') {
		str += ' <div class="announcementItem" data-toggle="modal" '
		+' data-target="#modalAnnounceLibrary${status.count}"> '
		+' <h6 class="card-title">${announcement.subject}<sup '
		+' class="announcementDate text-danger font-italic"><small><span '
		+' class="fromDate">${announcement.startDate}</span>&nbsp;-&nbsp;<span '
		+' class="toDate">${announcement.endDate}</span></small></sup> '
		+' </h6> '
		+' <p class="border-bottom"></p> '
		+' </div> '
		+' <div id="modalAnnouncement position-fixed"> '
		+' <div class="modal fade fnt-13" id="modalAnnounceLibrary${status.count}" '
		+' tabindex="-1" role="dialog" '
		+' aria-labelledby="submitAssignmentTitle" aria-hidden="true"> '
		+' <div '
		+' class="modal-dialog modal-dialog-centered modal-dialog-scrollable" '
		+' role="document"> '
		+' <div class="modal-content"> '
		+' <div class="modal-header"> '
		+' <h6 class="modal-title">${announcement.subject}</h6> '
		+' <button type="button" class="close" data-dismiss="modal" '
		+' aria-label="Close"> '
		+' <span aria-hidden="true">&times;</span> '
		+' </button> '
		+' </div> '
		+' <div class="modal-body"> '
		+' <div class="d-flex font-weight-bold"> '
		+' <p class="mr-auto"> '
		+' Start Date: <span>${announcement.startDate}</span> '
		+' </p> '
		+' <p> '
		+' End Date: <span>${announcement.endDate}</span> '
		+' </p> '
		+' </div><c:if test="${announcement.filePath ne null}"> '
		+' <p class="font-weight-bold">Attachment: <a href="sendAnnouncementFile?id=${announcement.id}">Download</a></p> '
		+' </c:if> '

		+' <p class="announcementDetail"> ' ;
		str  += `${announcement.description}`;
		str += ' </p> ' 
		+' </div> '
		+' <div class="modal-footer"> '
		+' <button type="button" class="btn btn-modalClose" '
		+' data-dismiss="modal">Close</button> '
		+' </div> '
		+' </div> '
		+' </div> '
		+' </div> '
		+' </div> ';
		}else if (selected == '<c:out value="${announcement.announcementSubType}"/>') {
		str += ' <div class="announcementItem" data-toggle="modal" '
		+' data-target="#modalAnnounceLibrary${status.count}"> '
		+' <h6 class="card-title">${announcement.subject}<sup '
		+' class="announcementDate text-danger font-italic"><small><span '
		+' class="fromDate">${announcement.startDate}</span>&nbsp;-&nbsp;<span '
		+' class="toDate">${announcement.endDate}</span></small></sup> '
		+' </h6> '
		+' <p class="border-bottom"></p> '
		+' </div> '
		+' <div id="modalAnnouncement position-fixed"> '
		+' <div class="modal fade fnt-13" id="modalAnnounceLibrary${status.count}" '
		+' tabindex="-1" role="dialog" '
		+' aria-labelledby="submitAssignmentTitle" aria-hidden="true"> '
		+' <div '
		+' class="modal-dialog modal-dialog-centered modal-dialog-scrollable" '
		+' role="document"> '
		+' <div class="modal-content"> '
		+' <div class="modal-header"> '
		+' <h6 class="modal-title">${announcement.subject}</h6> '
		+' <button type="button" class="close" data-dismiss="modal" '
		+' aria-label="Close"> '
		+' <span aria-hidden="true">&times;</span> '
		+' </button> '
		+' </div> '
		+' <div class="modal-body"> '
		+' <div class="d-flex font-weight-bold"> '
		+' <p class="mr-auto"> '
		+' Start Date: <span>${announcement.startDate}</span> '
		+' </p> '
		+' <p> '
		+' End Date: <span>${announcement.endDate}</span> '
		+' </p> '
		+' </div><c:if test="${announcement.filePath ne null}"> '
		+' <p class="font-weight-bold">Attachment: <a href="sendAnnouncementFile?id=${announcement.id}">Download</a></p> '
		+' </c:if> '

		+' <p class="announcementDetail"> ' ;
		str  += `${announcement.description}`;
		str += ' </p> ' 
		+' </div> '
		+' <div class="modal-footer"> '
		+' <button type="button" class="btn btn-modalClose" '
		+' data-dismiss="modal">Close</button> '
		+' </div> '
		+' </div> '
		+' </div> '
		+' </div> '
		+' </div> ';
		}
		</c:forEach>
		document.getElementById("libraryAnn").innerHTML = "" + str;
		});
		$('#stCounselor')
		.on('change',
		function() {
		var selected = $('#stCounselor').val();
		console.log(selected);
		var str = "";
		<c:forEach var="announcement"
		items="${announcementTypeMap['COUNSELOR']}" varStatus="status">
		if (selected == 'ALL') {
		str += ' <div class="announcementItem" data-toggle="modal" '
		+' data-target="#modalAnnounceCounselor${status.count}"> '
		+' <h6 class="card-title">${announcement.subject}<sup '
		+' class="announcementDate text-danger font-italic"><small><span '
		+' class="fromDate">${announcement.startDate}</span>&nbsp;-&nbsp;<span '
		+' class="toDate">${announcement.endDate}</span></small></sup> '
		+' </h6> '
		+' <p class="border-bottom"></p> '
		+' </div> '
		+' <div id="modalAnnouncement position-fixed"> '
		+' <div class="modal fade fnt-13" id="modalAnnounceCounselor${status.count}" '
		+' tabindex="-1" role="dialog" '
		+' aria-labelledby="submitAssignmentTitle" aria-hidden="true"> '
		+' <div '
		+' class="modal-dialog modal-dialog-centered modal-dialog-scrollable" '
		+' role="document"> '
		+' <div class="modal-content"> '
		+' <div class="modal-header"> '
		+' <h6 class="modal-title">${announcement.subject}</h6> '
		+' <button type="button" class="close" data-dismiss="modal" '
		+' aria-label="Close"> '
		+' <span aria-hidden="true">&times;</span> '
		+' </button> '
		+' </div> '
		+' <div class="modal-body"> '
		+' <div class="d-flex font-weight-bold"> '
		+' <p class="mr-auto"> '
		+' Start Date: <span>${announcement.startDate}</span> '
		+' </p> '
		+' <p> '
		+' End Date: <span>${announcement.endDate}</span> '
		+' </p> '
		+' </div><c:if test="${announcement.filePath ne null}"> '
		+' <p class="font-weight-bold">Attachment: <a href="sendAnnouncementFile?id=${announcement.id}">Download</a></p> '
		+' </c:if> '

		+' <p class="announcementDetail"> ' ;
		str  += `${announcement.description}`;
		str += ' </p> ' 
		+' </div> '
		+' <div class="modal-footer"> '
		+' <button type="button" class="btn btn-modalClose" '
		+' data-dismiss="modal">Close</button> '
		+' </div> '
		+' </div> '
		+' </div> '
		+' </div> '
		+' </div> ';
		}else if (selected == '<c:out value="${announcement.announcementSubType}"/>') {
		str += ' <div class="announcementItem" data-toggle="modal" '
		+' data-target="#modalAnnounceCounselor${status.count}"> '
		+' <h6 class="card-title">${announcement.subject}<sup '
		+' class="announcementDate text-danger font-italic"><small><span '
		+' class="fromDate">${announcement.startDate}</span>&nbsp;-&nbsp;<span '
		+' class="toDate">${announcement.endDate}</span></small></sup> '
		+' </h6> '
		+' <p class="border-bottom"></p> '
		+' </div> '
		+' <div id="modalAnnouncement position-fixed"> '
		+' <div class="modal fade fnt-13" id="modalAnnounceCounselor${status.count}" '
		+' tabindex="-1" role="dialog" '
		+' aria-labelledby="submitAssignmentTitle" aria-hidden="true"> '
		+' <div '
		+' class="modal-dialog modal-dialog-centered modal-dialog-scrollable" '
		+' role="document"> '
		+' <div class="modal-content"> '
		+' <div class="modal-header"> '
		+' <h6 class="modal-title">${announcement.subject}</h6> '
		+' <button type="button" class="close" data-dismiss="modal" '
		+' aria-label="Close"> '
		+' <span aria-hidden="true">&times;</span> '
		+' </button> '
		+' </div> '
		+' <div class="modal-body"> '
		+' <div class="d-flex font-weight-bold"> '
		+' <p class="mr-auto"> '
		+' Start Date: <span>${announcement.startDate}</span> '
		+' </p> '
		+' <p> '
		+' End Date: <span>${announcement.endDate}</span> '
		+' </p> '
		+' </div><c:if test="${announcement.filePath ne null}"> '
		+' <p class="font-weight-bold">Attachment: <a href="sendAnnouncementFile?id=${announcement.id}">Download</a></p> '
		+' </c:if> '

		+' <p class="announcementDetail"> ' ;
		str  += `${announcement.description}`;
		str += ' </p> ' 
		+' </div> '
		+' <div class="modal-footer"> '
		+' <button type="button" class="btn btn-modalClose" '
		+' data-dismiss="modal">Close</button> '
		+' </div> '
		+' </div> '
		+' </div> '
		+' </div> '
		+' </div> ';
		}
		</c:forEach>
		document.getElementById("counselorAnn").innerHTML = "" + str;
		});

	});
</script> -->

<!-- End newUserAnnouncementList -->

<!-- Test Timer Logic -->
<script>
	$("#DateCountdown").TimeCircles();
	//For Mobile
	$("#CountDownTimer").TimeCircles({
		count_past_zero : false,
		time : {
			Hours : {
				show : true,
				color : "#d53439"
			},
			Minutes : {
				show : true,
				color : "#2e8a00"
			},
			Seconds : {
				show : true,
				color : "#071e38"
			}

		}
	}).addListener(countdownComplete);
	
	
	var i = 0;
	var j = $('#durationCompletedByStudent').val();
	
	console.log('j value'+j);
	
	
	function countdownComplete(unit, value, total) {
        
        
        console.log("function called---");

        i++;

        if (i % 60 == 0 && total!=0) {

              j++;
              console.log(j + ' minute completed');
              saveCompletedDuration(j);
        }
  
        if (total == 0) {
              console.log("total " + total);
              /*
              * $("#DateCountdown").TimeCircles().stop();
              * $(this).fadeOut('slow').replaceWith( "<h2 style='margin-top:5%;'>Time
              * Over!</h2>");
              */
              completeTest();
              /* swal(
                                {
                                      title : 'Times Up!!!',
                                      // text: "It will permanently deleted !",
                                      //type: 'warning',
                                      icon : 'success',
                                      showCancelButton : true,
                                      confirmButtonColor : '#3085d6',
                                      cancelButtonColor : '#d33',
                                // confirmButtonText: 'Yes, delete it!'
                                }).then(function() {
                                      window.location.href = 'viewTestFinal';

                    }); */
              alert('Times Up!!');
              window.location.href = 'viewTestFinal';
              //$('#test_quiz_pop2').fadeOut('slow');
        }
  }

	
	
	
	
	
	$("#CountDownTimerMobile, #CountDownTimer").TimeCircles({
		circle_bg_color : "#d2d2d2"
	});

	//For larger devices

	$("#PageOpenTimer").TimeCircles();

	var updateTime = function() {
		var date = $("#date").val();
		var time = $("#time").val();
		var datetime = date + ' ' + time + ':00';
		$("#DateCountdown").data('date', datetime).TimeCircles().start();
	}
	$("#date").change(updateTime).keyup(updateTime);
	$("#time").change(updateTime).keyup(updateTime);
	//Stopwatch Timer end
</script>

<!-- End ViewFinal Test -->


<!-- start Content list -->
<script>

	$(function() {

		$('#contentCourseStudent')
				.on(
						'change',
						function() {

							var selected = $('#contentCourseStudent').val();

							console.log($("#contentCourseStudent").val());
							
							window.location = '${pageContext.request.contextPath}/studentContentList?courseId='
								+ encodeURIComponent(selected);
								

							return false;
							
						});
	});

</script>

<!-- end Content list -->

<!-- start teachersProfile -->
<script>

	$(function() {

		$('#teacherCourseStudent')
				.on(
						'change',
						function() {

							var selected = $('#teacherCourseStudent').val();

							console.log($("#teacherCourseStudent").val());
							
							window.location = '${pageContext.request.contextPath}/knowMyFaculty?courseId='
								+ encodeURIComponent(selected);
								

							return false;
							
						});
	});

</script>

<!-- end teachersProfile -->

<!-- start gradeListForParents -->

<script>
	$(function() {

		$('#selectSem')
				.on(
						'change',
						function() {
							var selected = $('#selectSem').val();
							console.log(selected);
							<c:forEach var='sem' items='${ sessionWiseCourseList }'>
							if (selected == '<c:out value="${sem.key}"/>') {
								var optionsAsString = "";
								$('#gradeCourse').find('option').remove();
								optionsAsString += "<option value='' disabled selected>--SELECT COURSE--</option>";
								<c:forEach var='group' items='${ sessionWiseCourseList[sem.key] }'>
								optionsAsString += "<option value='${ group.id }'>${ group.courseName }</option>";
								</c:forEach>
								console
										.log("optionsAsString"
												+ optionsAsString);

								$('#gradeCourse').append(optionsAsString);
							}
							</c:forEach>

						});

		$('#gradeCourse')
				.on(
						'change',
						function() {

							var selected = $('#gradeCourse').val();

							console.log($("#selectSem").val());

							window.location = '${pageContext.request.contextPath}/gradeCenterForParents?courseId='
									+ encodeURIComponent(selected)
									+ '&acadSessionParent='
									+ $("#selectSem").val();

							return false;
						});

		$('#viewAssignmentTableParent').DataTable();
		$('#viewTestTableParent').DataTable();

	});
</script>

<!-- End gradeListForParents -->

<!-- start reportParent -->

<script>
	$(function() {

		$('#reportCourseParent')
				.on(
						'change',
						function() {
							
							$('.assignWrap, .testWrap').addClass('d-block');

							var selected = $('#reportCourseParent').val();

							console.log($("#reportCourseParent").val());

							/*  window.location = '${pageContext.request.contextPath}/reportForParents?courseId='
								+ encodeURIComponent(selected); 
							
							return false; */

							var assignmentParentBar;
							var testParentLine;

							$
									.ajax({
										type : 'POST',
										url : myContextPath
												+ '/getAssignmentStatsByCourseParent?courseId='
												+ selected,
										success : function(data) {
											if (assignmentParentBar) {
												assignmentParentBar.destroy();
											}
											var dataArr1 = [];
											var dataArr2 = [];
											var dataArr3 = [];
											var parsedObj = JSON.parse(data);
											if (parsedObj.length > 0) {
												for (var i = 0; i < parsedObj.length; i++) {
													dataArr1
															.push(parsedObj[i].assignment.assignmentName);
													/* var str = '{"x":"parsedObj[i].assignment.assignmentName","y":"parsedObj[i].score"}'; */
													dataArr2
															.push(Number(parsedObj[i].score));
													dataArr3
															.push(Number(parsedObj[i].unscored));
												}
												console.log(dataArr1);
												console.log(dataArr2);
												console.log(dataArr3);
												var max = dataArr2
														.reduce(function(a, b) {
															return Math.max(a,
																	b);
														});
												var min = dataArr3
														.reduce(function(a, b) {
															return Math.min(a,
																	b);
														});
												console.log('mathsMax--->'
														+ max);
												console.log('maths--->' + min);
												/* dataArr1.push(parsedObj.unscored); */

												if ($('#assignReportChartParent').length) {

													assignmentParentBar = new Chart(
															document
																	.getElementById("assignReportChartParent"),
															{

																'type' : 'bar',
																'data' : {
																	'labels' : dataArr1,
																	'datasets' : [
																			{
																				label : "Marks Scored",
																				backgroundColor : "#177726",
																				data : dataArr2
																			},
																			{
																				label : "Marks Unscored",
																				backgroundColor : "#ff0000",
																				data : dataArr3
																			} ]
																},
																'options' : {
																	'animation' : {
																		'duration' : 0
																	},
																	'maintainAspectRatio' : false,
																	'responsive' : true,
																	'scales' : {
																		'xAxes' : [ {
																			'barPercentage' : 0.5,
																			'categoryPercentage' : 1,
																			'stacked' : true,
																			'barThickness' : 40
																		} ],
																		'yAxes' : [ {
																			'gridLines' : {
																				'zeroLineColor' : '#000',
																				'zeroLineWidth' : 1,
																				'offsetGridLines' : false
																			},
																			ticks : {
																				stepSize : 10,
																				min : min,
																				max : max

																			}
																		} ]
																	}
																}
															});

													assignmentParentBar.update();
												}

											} else {
												assignmentParentBar = new Chart(
														document
																.getElementById("assignReportChartParent"),
														{

															'type' : 'bar',
															'data' : {

																'datasets' : [{
																	label : "Marks Scored",
																	backgroundColor : "#177726",
																	data : [0]
																},
																{
																	label : "Marks Unscored",
																	backgroundColor : "#ff0000",
																	data : [0]
																} ]
															},
															'options' : {
																'animation' : {
																	'duration' : 0
																},
																'maintainAspectRatio' : false,
																'responsive' : true,
																'scales' : {
																	'xAxes' : [ {
																		'barPercentage' : 0.5,
																		'categoryPercentage' : 1,
																		'stacked' : true,
																		'barThickness' : 40
																	} ],
																	'yAxes' : [ {
																		'gridLines' : {
																			'zeroLineColor' : '#000',
																			'zeroLineWidth' : 1,
																			'offsetGridLines' : false
																		},
																		ticks : {
																			stepSize : 10,
																			min : 0,
																			max : 100

																		}
																	} ]
																}
															}
														});

												assignmentParentBar.update();

											}
										},
										error : function(result) {
											var parsedObj = JSON.parse(result);
											console.log('error' + parsedObj);
										}

									});

						

		$.ajax({
			type : 'POST',
			url : myContextPath + '/getTestStatsByCourseParent?courseId='+ selected,
			success : function(data) {
				if (testParentLine) {
					testParentLine.destroy();
				}
				var dataArr1 = [];
				var dataArr2 = [];
				var dataArr3 = [];

				var parsedObj = JSON.parse(data);
				console.log(parsedObj);
				if (parsedObj.length > 0) {
					for (var i = 0; i < parsedObj.length; i++) {
						console.log(parsedObj[i].testName);
						console.log(parsedObj[i].stringscore);
						console.log(parsedObj[i].unscored);
						dataArr1.push(parsedObj[i].testName);
						dataArr2.push(Number(parsedObj[i].stringscore));
						dataArr3.push(Number(parsedObj[i].unscored));
					}
					console.log(dataArr1);
					console.log(dataArr2);
					console.log(dataArr3);
					var max = dataArr2.reduce(function(a, b) {
						return Math.max(a, b);
					});
					var min = dataArr3.reduce(function(a, b) {
						return Math.min(a, b);
					});
					console.log('mathsMaxt--->' + max);
					console.log('mathst--->' + min);
					/* dataArr1.push(parsedObj.unscored); */

					if ($('#testReportChartParent').length) {

						testParentLine = new Chart(document
								.getElementById("testReportChartParent"), {
							'type' : 'line',
							'data' : {
								'labels' : dataArr1,
								'datasets' : [
										{
											label : "Marks Scored",
											
											data : dataArr2,
											categoryPercentage: 1,
											lineTension: 0,
											  fill: true,
											  backgroundColor: "rgb(23, 119, 38, 0.5)",
											  pointHitRadius: 10,
											  pointRadius: 5,
											  pointHoverRadius: 7,
											  borderColor: "#177726"
										},
										{
											label : "Marks Unscored",
											data : dataArr3,
											categoryPercentage: 1,
											lineTension: 0,
											
											  fill: true,
											  pointHitRadius: 10,
											  pointRadius: 5,
											  pointHoverRadius: 7,
											  backgroundColor: "rgb(214, 0, 0, 0.5)",
											  borderColor: "#ff0000"
										} ]
							},
							'options' : {
								'animation' : {
									'duration' : 0
								},
								'maintainAspectRatio' : false,
								'responsive' : true,
								'scales' : {
									'xAxes' : [ {
										'barPercentage' : 0.5,
										'categoryPercentage' : 1,
										'stacked' : true,
										'barThickness' : 40
									} ],
									'yAxes' : [ {
										'gridLines' : {
											'zeroLineColor' : '#000',
											'zeroLineWidth' : 1,
											'offsetGridLines' : false
										},
										ticks : {
											stepSize : 10,
											min : min,
											max : max

										}
									} ]
								}
							}
							
						});

						testParentLine.update();
					}

				}else {
					testParentLine = new Chart(
							document
									.getElementById("testReportChartParent"),
							{

								'type' : 'line',
								'data' : {
									'datasets' : [{
										label : "Marks Scored",
										backgroundColor : "#177726",
										data : [0]
									},
									{
										label : "Marks Unscored",
										backgroundColor : "#ff0000",
										data : [0]
									} ]
								},
								'options' : {
									'animation' : {
										'duration' : 0
									},
									'maintainAspectRatio' : false,
									'responsive' : true,
									'scales' : {
										'xAxes' : [ {
											'barPercentage' : 0.5,
											'categoryPercentage' : 1,
											'stacked' : true
										} ],
										'yAxes' : [ {
											'gridLines' : {
												'zeroLineColor' : '#000',
												'zeroLineWidth' : 1,
												'offsetGridLines' : false
											},
											ticks : {
												stepSize : 10,
												min: 0,
												max : 100
											}
										} ]
									}
								}
							});

					testParentLine.update();

				}

			},
			error : function(result) {
				var parsedObj = JSON.parse(result);
				console.log('error' + parsedObj);
			}
		});
						
						});

	});
</script>
<!-- <script>
//Notificatiopn Modal
$('#NotificationModal').css('display','block');
$('.hideModal').click(function(){
	$('#NotificationModal').css('display','none')
})
</script> -->

<!-- End reportParent -->
<noscript>
	
	<div class="vh-100 w-100" style="z-index:9999999; background:#000000">
		<h1>Please enable Java Script.</h1>
	</div>

</noscript>
</body>
</html>