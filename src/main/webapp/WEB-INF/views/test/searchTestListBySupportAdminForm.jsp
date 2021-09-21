
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>



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

					<li class="breadcrumb-item active" aria-current="page">Test
						List</li>
				</ol>
			</nav>

			<jsp:include page="../common/alert.jsp" />

			<!-- Input Form Panel -->
			<div class="card bg-white border">
				<div class="card-body">
					<h5 class="text-center pb-2 border-bottom">TEST List</h5>

					<div class="x_panel">



						<div class="x_content">
							<form:form action="searchTestListBySupportAdmin" method="post"
								modelAttribute="test">
								<fieldset>


									<div class="row">

								                    <!--<div class="col-sm-6 col-md-4">
														<div class="form-group">
															<label class="small"><strong>Start Date</strong>
																<span class="text-danger f-13">*</span></label>
															<div class="input-group">
																<div class='input-group date' id='datetimepicker1'>
																	<form:input id="startDateTest" path="startDate" type="text"
																		placeholder="Start Date" class="form-control"
																		readonly="true" value=""/>
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
															<div class='input-group date' id='datetimepicker2'>
																<form:input id="endDateTest" path="endDate" type="text"
																	placeholder="End Date" class="form-control"
																	readonly="true"  value=""/>
																<span class="input-group-addon"><span
																	class="glyphicon glyphicon-calendar"></span> </span>
															</div>
														</div>
													</div>-->
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

									<div class="col-sm-12 column">
										<div class="form-group">
											<button id="searchOne" name="submit"  value="SearchOne" class="btn btn-large btn-primary">Search One</button>
											<button id="searchAll" name="submit"  value="SearchAll" class="btn btn-large btn-primary">Search All School</button>
											
											<!-- <input id="reset" type="reset" class="btn btn-danger"> -->
											<button id="cancel" name="cancel" class="btn btn-danger"
												formaction="homepage" formnovalidate="formnovalidate">Cancel</button>
										</div>
									</div>
								</fieldset>
							</form:form>
						</div>
					</div>


					<div class="table-responsive mt-3 mb-3 testAssignTable">
						<table class="table table-striped table-hover" id="searchTableId">
							<thead>

								<tr>

									<th>School Name</th>
									<th>Test Id</th>
									<th>Test Name</th>
									<th>Test Type</th>
									<th>Start Date</th>
									<th>End Date</th>
									<th>Student Count</th>
									<th>Duration</th>
									<th>Last Modified Date</th>
                                    <th>No. of Question</th>
									<th>Action</th>

								</tr>

							</thead>
							<tbody>
								<c:forEach var="finalTestList" items="${finalTestList}"
									varStatus="status">
									<tr>

										<td>${finalTestList.schoolName}</td>
										<td>${finalTestList.id}</td>
										<td>${finalTestList.testName}</td>

										<td>${finalTestList.testType}</td>
										<td>${fn:replace(finalTestList.startDate,'T', ' ')} </td>
										<td>${fn:replace(finalTestList.endDate,'T', ' ')} </td>
										<td>${finalTestList.studentCount}</td>
										<td>${finalTestList.duration}</td>
										<td>${finalTestList.lastModifiedDate}</td>

                                        <td>${finalTestList.noOfQuestion}</td>
                                        
										<td><c:url value="studetTestListBySupportAdmin"
												var="quickaction">
												<c:param name="id" value="${finalTestList.id}" />
												<c:param name="dbName" value="${finalTestList.dbName}" />
												<c:param name="dbPort" value="${finalTestList.dbPort}" />
											</c:url> <a target="_blank" href="${quickaction}" title="studentDetails"> 
											<i class='fas fa-address-card'> </i></a>&nbsp;
											
												
											<c:url value="searchTestListBySupportAdmin" var="detailsUrl">
												<c:param name="id" value="${finalTestList.id}" />
												<c:param name="dbName" value="${finalTestList.dbName}" />
												<c:param name="dbPort" value="${finalTestList.dbPort}" />											
												<c:param name="schoolName" value="${finalTestList.schoolName}" />									
												<c:param name="courseId" value="${finalTestList.courseId}"/>
												<c:param name="acadMonth" value="${finalTestList.acadMonth}"/>
												<c:param name="acadYear" value="${finalTestList.acadYear}"/>
												<c:param name="testName" value="${finalTestList.testName}"/>
												<c:param name="testType" value="${finalTestList.testType}"/>
												<c:param name="duration" value="${finalTestList.duration}"/>
												<c:param name="startDate" value="${finalTestList.startDate}"/>
												<c:param name="endDate" value="${finalTestList.endDate}"/>
												<c:param name="allowAfterEndDate" value="${finalTestList.allowAfterEndDate}"/>
												<c:param name="showResultsToStudents" value="${finalTestList.showResultsToStudents}"/>
												<c:param name="passwordForTest" value="${finalTestList.passwordForTest}" />
												<c:param name="maxScore" value="${finalTestList.maxScore}"/>
												<c:param name="facultyId" value="${finalTestList.facultyId}"/>
												<c:param name="maxQuestnToShow" value="${finalTestList.maxQuestnToShow}"/>
												<c:param name="maxAttempt" value="${finalTestList.maxAttempt}"/>
												<c:param name="randomQuestion" value="${finalTestList.randomQuestion}"/>
												<c:param name="sameMarksQue" value="${finalTestList.sameMarksQue}"/>
												<c:param name="marksPerQue" value="${finalTestList.marksPerQue}"/>
												
													</c:url> <a target="_blank" href="${detailsUrl}" title="Details">
													<i class="fas fa-info-circle"></i> </a>
													
													<c:choose>
													<c:when test="${finalTestList.testType eq 'Objective'}">
													<button class="btn btn-success rounded-0 text-light objectiveTestUrl" data-obTest-id="${finalTestList.id}" >Complete Test</button>
													</c:when>
									
													<c:when test="${finalTestList.testType eq 'Subjective'}">  
													<button class="btn btn-success rounded-0 text-light subjectiveTestUrl" data-subTest-id="${finalTestList.id}" >Complete Test</button>
													</c:when>
													</c:choose>
										</td>

									</tr>

								</c:forEach>
							</tbody>
							<tfoot>
								<tr>
									<th>--</th>
									<th>--</th>
									<th>--</th>
									<th>--</th>
									<th>--</th>
									<th>--</th>
									<th>--</th>
									<th>--</th>
									<th>--</th>
									<th>--</th>
									<th>--</th>
								</tr>
							</tfoot>
						</table>
					</div>
				</div>
			</div>



			<!-- /page content: END -->

		</div>

		<!-- SIDEBAR START -->

		<!-- SIDEBAR END -->

		<jsp:include page="../common/newAdminFooter.jsp" />


<script>
$(document).ready(function(){
	//ObjectiveTest 
	   $("#searchTableId").on("click", ".objectiveTestUrl", function(){
		   
		   console.log("Id ---->>>>", $(this).attr("data-obTest-id"))
		   
		  	let objectiveTestId = $(this).attr("data-obTest-id");
		  	
			   console.log('objectiveTestId---------------------------->',objectiveTestId)
		    $.ajax({
		           url: "${pageContext.request.contextPath}/api/completeStudentTestAjaxForObjectiveExt",
		       	type: "POST",
		           data: {
		           	"testId": objectiveTestId
		           },
		           success: function (data) {
		        	   
		        	   console.log('data-------------->',data)
		        	  
		        	   if(data=='Success'){
		        		   location.reload();
		        		  
		        		   alert('SuccessFully Submitted')
		        	   }else
	        		   {
		        		   alert('Fail')
		        		   }		        	
			           }
		       });
	   })
	   
	 //Subjective Test
	 	   $("#searchTableId").on("click", ".subjectiveTestUrl", function(){
		  	let subjectivetestId = $(this).attr("data-subTest-id");
			   console.log('subjectivetestId---------------------------->',subjectivetestId)
		    $.ajax({
		           url: "${pageContext.request.contextPath}/api/completeStudentTestAjaxForSubjectiveExt",
		       	type: "POST",
		           data: {
		           	"testId": subjectivetestId
		           },
		           success: function (data) {
		        	   
		        	   console.log('data-------------->',data)
		        	  
		        	   if(data=='Success'){
		        		   location.reload();  
		        		  
		        		   alert('SuccessFully Submitted')
		        	   }else
	        		   {
		        		   alert('Fail')
		        		   }		        	
			           }
		       });
	   })
	   
})
</script>


<script>
	$(document).ready(
			function() {
				var cars = [ "bgcolor1", "bgcolor2", "bgcolor3", "bgcolor4",
						"bgcolor5", "bgcolor6", "bgcolor7", "bgcolor8",
						"bgcolor9" ];
				var count = 0;
				$('[id^=courseDetail]').each(function() {
					if (count == cars.length - 1) {
						count = 0;
					}
					$(this).addClass(cars[count]);
					count++;
				})

				$('body').addClass("dashboard_left");
				
				
			/* 	setTimeout(function(){
					$("#startDate, #endDate").val("")
				},700) */
				
				
				 
			});
</script>

<!--<script>
if ($('#startDateTest').length > 0) {
	$(function() {

		  $('#startDateTest').daterangepicker({
		      autoUpdateInput: false,
		      locale: {
		          cancelLabel: 'Clear',
		          format: "YYYY-MM-DD"
		      },
		      "singleDatePicker": true,
      	  "showDropdowns" : true,
            "timePicker" : true,
            "showCustomRangeLabel" : false,
            "alwaysShowCalendars" : true,
            "opens" : "center"
		  });

		  $('#startDateTest').on('apply.daterangepicker', function(ev, picker) {
		      $(this).val(picker.startDate.format('YYYY-MM-DD HH:mm:ss'));
		  });

		  $('#startDateTest').on('cancel.daterangepicker', function(ev, picker) {
		      $(this).val('');
		  });

		});
} 

if ($('#endDateTest').length > 0) {
  	$(function() {

		  $('#endDateTest').daterangepicker({
		      autoUpdateInput: false,
		      locale: {
		          cancelLabel: 'Clear',
		         format: "YYYY-MM-DD"
		      },
		      "singleDatePicker": true,
        	  "showDropdowns" : true,
              "timePicker" : true,
              "showCustomRangeLabel" : false,
              "alwaysShowCalendars" : true,
              "opens" : "center"
		  });

		  $('#endDateTest').on('apply.daterangepicker', function(ev, picker) {
		      $(this).val(picker.startDate.format('YYYY-MM-DD HH:mm:ss'));
		  });

		  $('#endDateTest').on('cancel.daterangepicker', function(ev, picker) {
		      $(this).val('');
		  });

		});
}
</script>-->

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


							});
		</script>


