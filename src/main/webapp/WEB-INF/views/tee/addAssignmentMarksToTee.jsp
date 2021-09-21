<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>



<jsp:include page="../common/newDashboardHeader.jsp" />

<div class="d-flex adminPage dataTableBottom" id="facultyAssignmentPage">
	<jsp:include page="../common/newLeftNavBarFaculty.jsp" />
	<jsp:include page="../common/newLeftSidebarFaculty.jsp" />
	<jsp:include page="../common/rightSidebarFaculty.jsp" />


	<!-- DASHBOARD BODY STARTS HERE -->

	<div class="container-fluid m-0 p-0 dashboardWraper">

		<jsp:include page="../common/newTopHeaderFaculty.jsp" />

		<div class="container mt-5">
			<div class="row">
				<!-- LOADER HTML -->


				<!-- SEMESTER CONTENT -->
				<div
					class="col-lg-9 col-md-9 col-sm-12 dashboardBody ml-auto mr-auto">



					<!-- page content: START -->


					<nav aria-label="breadcrumb">
						<ol class="breadcrumb">
							<li class="breadcrumb-item"><a
								href="${pageContext.request.contextPath}/homepage">Dashboard</a></li>

							<c:if test="${not empty Program_Name}">
								<li class="breadcrumb-item" aria-current="page"><c:out
										value="${Program_Name}" /></li>
							</c:if>

							<sec:authorize access="hasRole('ROLE_STUDENT')">
								<c:out value="${AcadSession}" />
							</sec:authorize>
							<li class="breadcrumb-item active" aria-current="page">Tee
								Marks Evaluation</li>
						</ol>
					</nav>

					<jsp:include page="../common/alert.jsp" />

					<!-- Input Form Panel -->
					<div class="card border bg-white">
						<div class="card-body">

							<div class="text-center border-bottom pb-2">

								<h5>Add Portal Assignment Marks To TEE</h5>

							</div>


							<div class="row mt-4">
								<div class="col-md-4 col-sm-12">
									<div class="form-group">
										<label class="control-label" for="courseId">Courses <span
											style="color: red">*</span></label> <input type="hidden" name="id"
											value="${tee.id}" id="teeIdDb" /> <input type="hidden"
											name="courseId" value="${tee.courseId}" /> <select
											id="teeCourseId" placeholder="Course Name"
											class="form-control courses" required="required">

											<option value="">Select Course</option>
											<c:forEach items="${coursesForTee}" var="cName">
												<c:choose>
													<c:when test="${courseId ne null }">
														<c:if test="${courseId eq cName.courseId}">
															<option value="${cName.courseId}" selected>${cName.courseName}</option>
														</c:if>
														<c:if test="${courseId ne cName.courseId}">
															<option value="${cName.courseId}">${cName.courseName}</option>
														</c:if>
													</c:when>
													<c:otherwise>
														<option value="${cName.courseId}">${cName.courseName}</option>
													</c:otherwise>
												</c:choose>

											</c:forEach>
										</select>
									</div>
								</div>

							</div>

							<c:if test="${showEvalBtn eq 'true'}">
								<div class="row mt-4">
									<div class="col-md-4 col-sm-12">
										<div class="form-group">
											<a href="evaluateTee?teeId=${tee.id}"><i
												class="btn btn-large btn-primary">Proceed to Evaluation</i></a>
										</div>

									</div>


								</div>
							</c:if>
						</div>

						<!-- Results Panel -->

						<c:if test="${showTests eq 'true'|| courseId ne null}">





							<div class="card bg-white border mt-5">
								<div class="card-body">

									<h5 class="text-center border-bottom pb-2">Select
										Assignments to Add Marks into TEE</h5>


									<form:form action="saveAssignmentMarksToTee"
										id="saveAssignmentMarksToTee" method="post"
										modelAttribute="tee">
										<input type="hidden" name="id" value="${tee.id}" />
										<input type="hidden" name="courseId" value="${tee.courseId}" />


										<div class="row">
											<%-- <div
											class="col-lg-4 col-md-4 col-sm-12 mt-3 position-relative">
											<label for="inputEmail4">How Do You Want To Add Test
												Marks?</label>
											<form:radiobutton name="modeOfAddingAssignmentMarks" id="bestOf"
												value="bestOf" path="modeOfAddingAssignmentMarks" />
											Best Of<br>
											<form:radiobutton name="modeOfAddingAssignmentMarks" id="avgOfSel"
												value="avg" path="modeOfAddingAssignmentMarks" />
											Avg. Of Selection
										</div> --%>
											<form:input type="hidden" path="modeOfAddingAssignmentMarks"
												value="avg"></form:input>
										</div>
										<%-- <c:if test="${tee.bestOf ne '0' && tee.bestOf ne null}">
										<div class="row">
											<div class="ol-lg-4 col-md-4 col-sm-12 mt-3 position-relativ">
												<label for="inputEmail4">Add Best Of No</label>
												<form:input type="number" min="1" path="bestOf"
													id="bestOfNum" />
											</div>
											
										</div>
									</c:if>
									<c:if test="${tee.bestOf eq '0' || tee.bestOf eq null}">
										<div class="row" id="hiddenDiv">
											<div class="ol-lg-4 col-md-4 col-sm-12 mt-3 position-relativ">
												<label for="inputEmail4">Add Best Of No</label>
												<form:input type="number" min="1" path="bestOf"
													id="bestOfNum" />
											</div>
										</div>
									</c:if> --%>
										<div class="row">
											<div class="ol-lg-4 col-md-4 col-sm-12 mt-3 position-relativ">

												<a
													href="clearTeeAsignmentMarks?teeId=${tee.id}&courseId=${tee.courseId}"><i
													class="btn btn-large btn-primary">Clear Selections</i></a>
											</div>
										</div>


										<div class="table-responsive testAssignTable">
											<table class="table table-striped table-hover">
												<thead>
													<tr>
														<th>Sr. No.</th>
														<th>Select (<a onclick="checkAll()">All</a> | <a
															onclick="uncheckAll()">None</a>)
														</th>


														<th>Assignment Name</th>
														<th>Course Name</th>
														<th>Start Date</th>
														<th>End Date</th>
														<th>Total Score</th>
													</tr>
												</thead>
												<tfoot>
													<tr>
														<th></th>
														<th></th>

														<th>Test Name</th>
														<th>Course Name</th>
														<th>Start Date</th>
														<th>End Date</th>
														<th>Total Score</th>
													</tr>
												</tfoot>
												<tbody>

													<c:forEach var="test" items="${assgnList}"
														varStatus="status">
														<tr>
															<td><c:out value="${status.count}" /></td>
															<td><c:choose>
																	<c:when test="${assignmentIdsForTee eq null}">

																		<c:if test="${assgnList.size() eq 1}">
																			<form:radiobutton path="assignmentIds"
																				value="${test.id}" name="assignmentIds"
																				checked="checked" />
																		</c:if>
																		<c:if test="${assgnList.size() ne 1}">
																			<form:radiobutton path="assignmentIds"
																				value="${test.id}" name="assignmentIds" required="required"/>
																		</c:if>
																		<%-- <form:checkbox path="assignmentIds" value="${test.id}" name="assignmentIds"/> --%>

																	</c:when>
																	<c:otherwise>
																		<c:choose>
																			<c:when
																				test="${fn:contains(assignmentIdsForTee[test.courseId], test.id)}">
															 	
                                                      					Test Added to TEE
                                                      				</c:when>
																			<c:otherwise>
																				<c:if test="${assgnList.size() eq 1}">
																					<form:radiobutton path="assignmentIds"
																						value="${test.id}" name="assignmentIds"
																						checked="checked" />
																				</c:if>
																				<c:if test="${assgnList.size() ne 1}">
																					<form:radiobutton path="assignmentIds"
																						value="${test.id}" name="assignmentIds" required="required"/>
																				</c:if>

																			</c:otherwise>
																		</c:choose>
																	</c:otherwise>
																</c:choose></td>
															<td><c:out value="${test.assignmentName}" /></td>
															<td><c:out value="${test.courseName}" /></td>
															<td><c:out value="${test.startDate}" /></td>
															<td><c:out value="${test.endDate}" /></td>
															<td><c:out value="${test.maxScore}" /></td>
														</tr>
													</c:forEach>
												</tbody>
											</table>
										</div>

										<div class="col-12 text-left">
											<div class="form-group">

												<button id="submit" class="btn btn-large btn-success"
													formaction="saveAssignmentMarksToTee"
													onclick="validateForm()">Assign Marks</button>


												<button id="cancel" class="btn btn-danger"
													formaction="homepage" formnovalidate="formnovalidate">Cancel</button>


											</div>
										</div>
									</form:form>
								</div>
							</div>
						</c:if>

						<!-- /page content: END -->

					</div>
				</div>

				<!-- SIDEBAR START -->
				<jsp:include page="../common/newSidebar.jsp" />
				<!-- SIDEBAR END -->
				<jsp:include page="../common/footer.jsp" />



				<script>
					var teeId = $('#teeIdDb').val();
					$("#teeCourseId")
							.on(
									'change',
									function() {

										var selectedValue = $(this).val();

										window.location.href = '${pageContext.request.contextPath}/assignAssignmentMarksToTeeForm?teeId='
												+ teeId
												+ '&courseId='
												+ encodeURIComponent(selectedValue);
										return false;
									});
				</script>


				<script>
					var count = 0;
					$(document).ready(function() {

						$('#hiddenDiv').hide();
						if ($("#bestOf").prop("checked")) {
							// do something
							// alert('best of checked');
							count++;
						}
						if ($("#avgOfSel").prop("checked")) {
							// do something
							// alert('avg checked');
							$('#hiddenDiv').hide();
							$('#bestOfNum').val('1');
							count++;
						}

					});

					if ($('#bestOf').click(function() {
						count++;
						$('#hiddenDiv').show();
					}))
						;

					/* window.checkAll = function checkAll(){
					 console.log('checked all checked!!');
					   	$('input:checkbox[name=assignmentIds]').prop('checked', true);
					   	return false;
					   }


					   window.uncheckAll = function uncheckAll(){
					   	 console.log('unchecked all checked!!');
					   	$('input:checkbox[name=assignmentIds]').prop('checked', false);
					   	return false;
					   } */

					$('#avgOfSel').click(function() {
						count++;
						$('#hiddenDiv').hide();
						$('#bestOfNum').val('1');
					});

					/* 	function validateForm() {
							if (count == 0) {
								alert('Mode of Adding Test Marks Selection Is Mandatory')
							}
						} */

					$(".facultyparameter")
							.on(
									'change',
									function() {
										var acadYear = $(
												'#acadYearForAcadSession')
												.val();

										console.log(acadYear);

										if (acadYear) {
											console.log("called acad session")
											$
													.ajax({
														type : 'GET',
														url : '${pageContext.request.contextPath}/getSemesterByAcadYearForFeedback?'
																+ 'acadYear='
																+ acadYear,
														success : function(data) {

															var json = JSON
																	.parse(data);
															var optionsAsString = "";

															$('#acadSessionId')
																	.find(
																			'option')
																	.remove();
															console.log(json);
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

															$('#acadSessionId')
																	.append(
																			optionsAsString);

														}
													});
										} else {
											//  alert('Error no faculty');
										}
									});

					/*    $("#datetimepicker3").on("dp.change", function(e) {

					 validDateTimepicks1();

					 }).datetimepicker({

					 // minDate:new Date(),
					 useCurrent : false,
					 format : 'YYYY-MM-DD HH:mm:ss'

					 });

					 $("#datetimepicker4").on("dp.change", function(e) {

					 validDateTimepicks1();

					 }).datetimepicker({

					 //    minDate:new Date(),
					 useCurrent : false,
					 format : 'YYYY-MM-DD HH:mm:ss'

					 });

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