<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="java.util.*"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
	<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page import="java.util.*"%>
<%@page import="com.spts.lms.beans.dashboard.DashBoard"%>
<%-- <jsp:include page="../common/DashboardHeader.jsp" /> --%>
<%-- <jsp:include page="../common/NewHeader1.jsp" /> --%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>



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

	<form:form action="updateAssignmentBySupportAdmin" id="updateAssignmentBySupportAdmin"
										method="post" modelAttribute="assignment"
										enctype="multipart/form-data">
										
										<%-- <form:input path="courseId" type="hidden" /> --%>
										<form:input type="hidden" path="id"  />
										
										<%-- <form:input type="hidden" path="id" />
										<form:input type="hidden" path="acadYear" />
										<form:input type="hidden" path="acadMonth" /> --%>
										
										
										<div class="form-row">
											
											<div class="col-12 text-center">
												<h3>Update Assignment </h3>
											</div>
											
											
											
											<!--FORM ITEMS STARTS HERE-->


											<div
												class="col-lg-4 col-md-4 col-sm-12 mt-3 position-relative">
												<label class="textStrong">Name <span class="text-danger">*</span></label>											
												<form:input id="assignmentName" path="assignmentName"
													type="text" placeholder="Assignment Name"
													class="form-control" required="required" />
											</div>
											
											<div
												class="col-lg-4 col-md-4 col-sm-12 mt-3 position-relative">
												<label class="textStrong">Course Name  :  <span class="text-danger">*</span></label>
												<label class="textStrong">${assignment.courseName} <span class="text-danger"></span></label>
												
											</div>





											


											<div
												class="col-lg-4 col-md-4 col-sm-12 mt-3 position-relative">
												<label class="textStrong">Assignment Type <span class="text-danger">*</span></label>
												<label> ${assignment.assignmentType} </label>
											</div>

											<div
												class="col-lg-4 col-md-4 col-sm-12 mt-3 position-relative">
												<label class="textStrong">Acad Year
												<span class="text-danger">*</span></label>
												<br>
												<label class="textStrong">${assignment.acadYear}<span class="text-danger"></span></label>
												
											</div>


											<div
												class="col-lg-8 col-md-8 col-sm-12 mt-3 position-relative">
												
												<%-- <div class="input-group">
													
													
													
													<label class="textStrong">${assignment.startDate} - ${assignment.endDate} <span class="text-danger"></span></label>
												</div> --%>
												<div
												class="col-lg-8 col-md-8 col-sm-12 mt-3 position-relative">
												<label class="textStrong">Select Date Range <span class="text-danger">*</span></label>
												<div class="input-group">
													<form:input type="hidden" id="testStartDate"
														path="startDate" />
													<form:input type="hidden" id="testEndDate" path="endDate" />
													<input id="startDate" type="text"
														placeholder="Start Date - End Date" class="form-control"
														required="required"
														value="${assignment.startDate} - ${assignment.endDate}"
														readonly />
													<div class="input-group-append">
														<button class="btn btn-outline-secondary" type="button"
															id="testDateRangeBtn">
															<i class="fas fa-calendar"></i>
														</button>
													</div>
												</div>
											</div>
												
												
												
											</div>

											

											<div
												class="col-lg-4 col-md-4 col-sm-12 mt-3 position-relative">
												<label class="textStrong">Total Score <span class="text-danger">*</span></label>
												<c:if test="${editmarks eq 'Y'}">
												<form:input id="maxScore" path="maxScore" min="1"
													type="number" placeholder="Total Score"
													value="${assignment.maxScore}" class="form-control"
													required="required" />
													</c:if>
													<c:if test="${editmarks eq 'N'}">
												<form:input id="maxScore" path="maxScore" min="1"
													type="number" placeholder="Total Score"
													value="${assignment.maxScore}" readonly="true" class="form-control"
													required="required" />
													</c:if>
											</div>



											
										</div>


										<hr />

										
										<!-- START -->
										<div class="row">
											<%-- <div
												class="col-lg-4 col-md-4 col-sm-12 mt-3 position-relative">
												<div class="slider round">
													<form:label class="textStrong" path="plagscanRequired" for="plagscanRequired">Is Plagiarism Check Required? <span
															style="color: red">*</span>
													</form:label>
													<br>
													
													<c:if test="${assignment.plagscanRequired eq 'No'}">
													<form:radiobutton name="plagscanRequired" id="Yes"
														value="Yes" path="plagscanRequired"/>
													Yes<br>
													<form:radiobutton name="plagscanRequired" id="No"
														value="No" path="plagscanRequired" checked="true"/>
													No<br>
													</c:if>
													<c:if test="${assignment.plagscanRequired eq 'Yes'}">
													<form:radiobutton name="plagscanRequired" id="Yes"
														value="Yes" path="plagscanRequired" checked="true"/>
													Yes<br>
													<form:radiobutton name="plagscanRequired" id="No"
														value="No" path="plagscanRequired" />
													No<br>
													</c:if>
													
													<form:label class="textStrong" path="isCheckSumReq" for="isCheckSumReq">Is CheckSum Required? <span
															style="color: red">*</span>
													</form:label>
													<br>
													 <c:if test="${assignment.isCheckSumReq eq 'N'}">
													<form:radiobutton name="isCheckSumReq" id="Yes"
														value="Y" path="isCheckSumReq"/>
													Yes<br>
													<form:radiobutton name="isCheckSumReq" id="No"
														value="N" path="isCheckSumReq" checked="true"/>
													No<br>
													</c:if>
													<c:if test="${assignment.isCheckSumReq eq 'Y'}">
													<form:radiobutton name="isCheckSumReq" id="Yes"
														value="Y" path="isCheckSumReq" checked="true"/>
													Yes<br>
													<form:radiobutton name="isCheckSumReq" id="No"
														value="N" path="isCheckSumReq" />
													No<br>
													</c:if>
													
												</div>
											</div> --%>



											
										</div>
<div class="row">
											<div
												class="col-lg-4 col-md-4 col-sm-12 mt-3 position-relative">
												<div class="slider round">
												<input type="hidden" id="plagscanRequired"
											value="${assignment.plagscanRequired}" />
													<form:label class="textStrong" path="plagscanRequired" for="plagscanRequired">Is Plagiarism Check Required? <span
															style="color: red">*</span>
													</form:label>
													<br>
													<form:radiobutton name="plagscanRequired" id="Yes"
														value="Yes" path="plagscanRequired" required="required" />
													Yes<br>
													<form:radiobutton name="plagscanRequired" id="No"
														value="No" path="plagscanRequired" />
													No<br>


													<%-- 	<form:checkbox path="plagscanRequired" onclick="clicked();"
																class="form-control" value="Y" data-toggle="toggle"
																data-on="Yes" data-off="No" data-onstyle="success"
																data-offstyle="danger" data-style="ios" data-size="mini" /> --%>
												</div>
											</div>



											<div
												class="col-lg-4 col-md-4 col-sm-12 mt-3 position-relative"
												style="display: none" id="runPlagiarism">
												<div class="slider round">
													<form:label class="textStrong" path="runPlagiarism" for="runPlagiarism">Run Plagiarism while <span
															style="color: red">*</span> :</form:label>
													<br>
													<form:radiobutton name="runPlagiarism" id="Submission"
														value="Submission" path="runPlagiarism"
														required="required" />
													Submission<br>
													<form:radiobutton name="runPlagiarism" id="Manual"
														value="Manual" path="runPlagiarism" />
													Manual<br>



												</div>
											</div>
											<div
												class="col-lg-4 col-md-4 col-sm-12 mt-3 position-relative"
												style="display: none;" id="threshold">
												<div class="form-group">
													<form:label class="textStrong" path="threshold" for="threshold">Threshold Value for Plagarism Check <span
															style="color: red">*</span>
													</form:label>
													<form:input id="thresholdId" path="threshold"
														class="form-control" value="${assignment.threshold}"
														type="number" required="required" max="100" />
												</div>
											</div>
										</div>
										
										<div class="form-row">
										
											<div
												class="col-lg-4 col-md-4 col-sm-12 mt-3 position-relative">
												<div class="slider round">
													<form:label class="textStrong" path="isCheckSumReq" for="isCheckSumReq">Is Assignment Checksum  Required? <span
															style="color: red">*</span>
													</form:label>
													<br>
													<form:radiobutton name="isCheckSumReq" id="Yes"
														value="Y" path="isCheckSumReq" required="required" />
													Yes<br>
													<form:radiobutton name="isCheckSumReq" id="No"
														value="N" path="isCheckSumReq" />
													No<br>

												</div>
											</div>
										
										</div>
										


										

										<div class="col-6 m-auto">
											<div class="form-row mt-5">


												
												<button id="submit"
													class="btn btn-info col-md-5 col-sm-12  ml-auto mr-auto mb-3"
													formaction="updateAssignmentBySupportAdmin">Update Assignment</button>
												
												




											</div>
										</div>






									</form:form>
								



				<!-- Input Form Panel -->
				
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
		<script type="text/javascript">
					$('#file').bind(
							'change',
							function() {
								// alert('This file size is: ' + this.files[0].size/1024/1024 + "MB");
								var fileSize = this.files[0].size / 1024 / 1024
										+ " MB";
								$('#fileSize').html("File Size:" + (fileSize));
							});
				</script>


				<script>
					$(document)
							.ready(
									function() {
										$("#create")
												.on(
														'click',
														function() {
															var courseid = $(
																	'#idForCourse')
																	.val();

															console
																	.log(courseid);
															var num = $('#id')
																	.val();

															console.log("Num "
																	+ num);

															if (courseid) {
																console
																		.log("called")
																$
																		.ajax({
																			type : 'GET',
																			url : "${pageContext.request.contextPath}/createRandomGroupsJson?courseId="
																					+ courseid
																					+ "&noOfStudents="
																					+ num,
																			success : function(
																					data) {

																				console
																						.log("JSON DATA "
																								+ data);

																				var json = JSON
																						.parse(data);
																				var optionsAsString = "";

																				/*    $(
																				                  '#grps')
																				                  .find(
																				                              'option')
																				                  .remove(); */
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
																						'#grps')
																						.append(
																								optionsAsString);

																			}
																		});
																//$("#close").trigger("click")
																//$("#close").click()
																//$('#myModal').modal('toggle');
																//$("#myModal .close").click()
																//$('#myModal').modal('toggle');
																alert("Groups created, Please close this box and continue!");
															} else {
																alert('Error!');
															}
														});
									});
				</script>
				<script>
				function myFunction(){
					console.log("fdghdhyrfdhuytuytrutryutrruht");
					var dateinput = $('#startDate').val();
					console.log(dateinput);
				}
				
				$(function() {
					
					$('#submit').on('click', function(e){ 
						var dateinput = $('#startDate').val();
						console.log(dateinput);
						if(dateinput == ''){
							alert('Error: Enter proper Date inputs.');
							
							    e.preventDefault();
						
						} else {
							
						}
						
					});
					
				});
				</script>

				<!-- <script>
				
				
					$('#plagscanRequired').click(function() {
						console.log('clicked!!!!');
						$('#plagContent').toggleClass('d-none');
						
						if ($('#plagscanRequired').is(":checked")) {
							console.log('clicked Yes');
							$('#threshold').prop('required', true);
							$('#plagscanRequired').val('Yes');

						} else {
							$('#plagscanRequired').val('No');
							$('#threshold').prop('required', false);
						}
					});

					$('#runPlagiarism').click(function() {

						if ($('#runPlagiarism').is(":checked")) {
							$('#runPlagiarism').val('Submission');
						} else {
							$('#runPlagiarism').val('Manual');
						}

					});
				</script>
				-->

				<script>
					$(function() {

						var plagReq = $("#plagscanRequired").val();
						console.log('Plag Req--->' + plagReq);
						if (plagReq == 'No') {

							console.log('plag Req N')

							$('#runPlagiarism').hide();
							$('#threshold').hide();

							$("#Submission").prop('required', false);

							$("#Manual").prop('required', false);

							$("#thresholdId").prop('required', false);
						}else if(plagReq == 'Yes'){
                            
							$('#runPlagiarism').show();
							$('#threshold').show();

							$("#Submission").prop('required', true);

							$("#Manual").prop('required', true);

							$("#thresholdId").prop('required', true);
                        }

						$('#Yes').on('click', function() {
							//alert('slidYes');
							$('#runPlagiarism').show();
							$('#threshold').show();

							$("#Submission").prop('required', true);

							$("#Manual").prop('required', true);

							$("#thresholdId").prop('required', true);

						});

						$('#No').on('click', function() {
							//alert('slidNo');
							$('#runPlagiarism').hide();
							$('#threshold').hide();

							$("#Submission").prop('required', false);

							$("#Manual").prop('required', false);

							$("#thresholdId").prop('required', false);
						});

					});
				</script>