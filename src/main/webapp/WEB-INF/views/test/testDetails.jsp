<%@page import="com.spts.lms.web.utils.Utils"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@page import="java.util.*"%>



<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<jsp:include page="../common/newDashboardHeader.jsp" />

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
								Subjective Test Question</li>
						</ol>
					</nav>

					<jsp:include page="../common/alert.jsp" />


					<!-- Input Form Panel -->
					<div class="card bg-white border">
						<div class="card-body">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">
									<div class="x_title">
										<h5>Test Details</h5>



									</div>
									<div class="x_content">

										<form:form action="addTest" method="post"
											modelAttribute="test">

											<div class="row">
												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<form:label class="textStrong" path="courseName" for="courseName">Course :</form:label>
														${test.courseName}
													</div>
												</div>
												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<form:label class="textStrong" path="acadMonth" for="acadMonth">Academic Month :</form:label>
														${test.acadMonth}
													</div>
												</div>
												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<form:label class="textStrong" path="acadYear" for="acadYear">Academic Year :</form:label>
														${test.acadYear}
													</div>
												</div>
												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<form:input type="hidden" path="courseId" />
													<div class="form-group">
														<form:label class="textStrong" path="testName" for="testName">Test Name :</form:label>
														${test.testName}
													</div>
												</div>
												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<form:label class="textStrong" path="duration" for="duration">Test Duration :</form:label>
														<c:out
															value="${fn:replace(test.duration,'T', ' ')}${' Minutes'}"></c:out>
													</div>
												</div>
												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<form:label class="textStrong" path="startDate" for="startDate">Start Date :</form:label>
														<c:out
															value="${fn:replace(test.startDate, 
                                'T', ' ')}"></c:out>
													</div>
												</div>
												<%-- <div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<form:label path="dueDate" for="dueDate">Due Date :</form:label>
														<c:out value="${fn:replace(test.dueDate,'T', ' ')}"></c:out>
													</div>
												</div> --%>
												<sec:authorize access="hasRole('ROLE_FACULTY')">
													<div class="col-md-4 col-sm-6 col-xs-12 column">
														<div class="form-group">
															<form:label class="textStrong" path="endDate" for="endDate">End Date :</form:label>
															<c:out value="${fn:replace(test.endDate,'T', ' ')}"></c:out>
														</div>
													</div>
													<div class="col-md-4 col-sm-6 col-xs-12 column">
														<div class="form-group">
															<form:label class="textStrong" path="sendEmailAlert" for="sendEmailAlert">Allow Submission after End date :</form:label>
															${test.allowAfterEndDate}

														</div>
													</div>
													<div class="col-md-4 col-sm-6 col-xs-12 column">
														<div class="form-group">
															<form:label class="textStrong" path="sendEmailAlert" for="sendEmailAlert">Send Email Alert for New Test :</form:label>
															${test.sendEmailAlert}
														</div>
													</div>
													<div class="col-md-4 col-sm-6 col-xs-12 column">
														<div class="form-group">
															<form:label class="textStrong" path="sendSmsAlert" for="sendSmsAlert">Send SMS Alert for New Test :</form:label>
															${test.sendSmsAlert}
														</div>
													</div>
													<div class="col-sm-6 col-md-4 col-xs-12 column">
														<div class="form-group">
															<form:label class="textStrong" path="sendEmailAlertToParents"
																for="sendEmailAlertToParents">Send Email Alert to Parents:</form:label>
															${test.sendEmailAlertToParents}
														</div>
													</div>

													<div class="col-sm-6 col-md-4 col-xs-12 column">
														<div class="form-group">
															<form:label class="textStrong" path="sendSmsAlertToParents"
																for="sendSmsAlertToParents">Send SMS Alert to Parents:</form:label>
															${test.sendSmsAlertToParents}
														</div>
													</div>

													<div class="col-md-4 col-sm-6 col-xs-12 column">
														<div class="form-group">
															<form:label class="textStrong" path="showResultsToStudents"
																for="showResultsToStudents">Show Results to Students immediately :</form:label>
															${test.showResultsToStudents}
														</div>
													</div>
													<div class="col-md-4 col-sm-6 col-xs-12 column">
														<div class="form-group">
															<form:label class="textStrong" path="facultyId" for="facultyId">Password For Test :</form:label>
															${test.passwordForTest}
														</div>
													</div>
												</sec:authorize>
												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<form:label class="textStrong" path="maxScore" for="maxScore">Score Out of :</form:label>
														${test.maxScore}
													</div>
												</div>
												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<form:label class="textStrong" path="facultyId" for="facultyId">Faculty :</form:label>
														${test.facultyId}
													</div>
												</div>
												<c:if test="${isTestQConfigured eq true}">

													<div class="col-md-4 col-sm-6 col-xs-12 column">
														<div class="form-group">
															<a
																href="${pageContext.request.contextPath}/downloadTestQuestionByTestId?testId=${test.id}"><font
																color="red">Download a uploaded test-question
																	bank</font></a>
														</div>
													</div>
												</c:if>
												<sec:authorize access="hasRole('ROLE_FACULTY')">
													<div class="col-md-4 col-sm-6 col-xs-12 column">
														<div class="form-group">
															<a
																href="${pageContext.request.contextPath}/downloadTestReportByTestId?testId=${test.id}"><font
																color="red">Download a Test Report</font></a>
														</div>
													</div>
													<div class="col-md-4 col-sm-6 col-xs-12 column">
														<div class="form-group">
															<a
																href="${pageContext.request.contextPath}/downloadTestReportByTestIdAttemptWise?testId=${test.id}"><font
																color="red">Download a Test Report Attempt-Wise</font></a>
														</div>
													</div>
												</sec:authorize>

											</div>
											<div class="row">

												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<sec:authorize access="hasRole('ROLE_FACULTY')">
															<c:url value="createTestForm" var="editTestUrl">
																<c:param name="id">${test.id}</c:param>
																<c:param name="courseId">${test.courseId}</c:param>
															</c:url>

															<c:url value="addTestQuestionForm" var="addTestQuestion">
																<c:param name="id">${test.id}</c:param>
															</c:url>
															<c:url value="uploadTestQuestionForm"
																var="uploadTestQuestion">
																<c:param name="id">${test.id}</c:param>
															</c:url>
															<c:url value="viewTestQuestionsByTestPoolForm"
																var="viewTestQuestionPools">
																<c:param name="testId">${test.id}</c:param>
															</c:url>

															<button id="submit" class="btn btn-large btn-primary mt-2"
																formaction="${editTestUrl}">Edit</button>
															<button id="cancel" class="btn btn-danger mt-2"
																formaction="homepage" formnovalidate="formnovalidate">Cancel</button>
															<c:if test="${!testPoolConfig}">
															<c:if test="${test.testType ne 'Mix'}">
																<button class="btn btn-large btn-primary mt-2"
																	formaction="${addTestQuestion}">Configure
																	Questions</button>
																<button class="btn btn-large btn-primary mt-2"
																	formaction="${uploadTestQuestion}">Upload Test
																	Questions</button>
															</c:if>
															</c:if>
															<%-- <c:if test="${testPoolConfig}"> --%>
															<button class="btn btn-large btn-primary mt-2"
																formaction="${viewTestQuestionPools}">Configure
																Questions From Question Pool</button>
															<%-- </c:if> --%>
															<!-- New Pool Changes -->
															<br>
															<c:url value="addTestConfigurationForm"
																var="addTestConfiguration">
																<c:param name="testId">${test.id}</c:param>
															</c:url>
															<c:if test="${test.randomQuestion eq 'Y' && test.sameMarksQue eq 'N'}">
															<button id="submit" class="btn btn-large btn-primary mt-2"
																formaction="${addTestConfiguration}">Cofigure Question Weightage</button>
															</c:if>
															<!-- New Pool Changes -->

														</sec:authorize>
														<sec:authorize access="hasRole('ROLE_STUDENT')">
															<%-- <c:url value="startStudentTest" var="takeTestUrl">
																<c:param name="id" value="${test.id}" />
															</c:url> --%>

															<c:if test="${test.testType eq 'Objective' }">
																<c:url value="startStudentTest" var="takeTestUrl">
																	<c:param name="id" value="${test.id}" />
																</c:url>
															</c:if>
															<c:if test="${test.testType eq 'Subjective' }">
																<c:url value="startStudentTestForSubjective"
																	var="takeTestUrl">
																	<c:param name="id" value="${test.id}" />
																</c:url>
															</c:if>
															<c:if test="${test.testType eq 'Mix' }">
																<c:url value="startStudentTestForMix" var="takeTestUrl">
																	<c:param name="id" value="${test.id}" />
																</c:url>
															</c:if>


															<%-- <a href="${takeTestUrl}" title="Start Test"
																onclick="return confirm('Are you ready to take the test?')"><i
																class="fa fa-pencil-square-o fa-lg"
																style="font-size: 4em;"></i></a> --%>

															<c:if test="${test.isPasswordForTest eq 'N'}">
																<a href="${takeTestUrl}" title="Start Test"
																	class="btn btn-primary"
																	onclick="return confirm('Are you ready to take the test?')">
																	<i class="fa fa-check-circle-o" aria-hidden="true"
																	style="font-size: 4em;"></i>
																</a>
															</c:if>
															<c:if test="${test.isPasswordForTest eq 'Y'}">

																<a class="btn btn-primary" onclick="callFancyBox()"><i
																	class="fa fa-check-circle-o" aria-hidden="true"
																	style="font-size: 4em;"></i></a>
															</c:if>
															<div id="test_quiz_pop1" class="fancy_pop_box1">
																<h5>
																	Test Is Password Protected, Please Enter a Password <span
																		style="color: red">*</span>
																</h5>
																<hr>
																<div class="text-center">
																	<input type="password" value="" id="passwordOfTest"
																		required="required" /><br> <br> <a
																		data-fancybox data-src="#test_quiz_pop1"
																		href="javascript;;"
																		class="btn btn-primary btntestquiz"
																		onclick="checkPassword(${test.id},'${test.testType}');closeFancybox()">Yes</a>

																	<!-- <a href="testList" class="btn btn-primary"
																		onclick="closeFancybox()">Cancel</a> -->
																	<a href="#"
																		class="btn btn-primary btntestquiz closeLink">Close</a>

																	<hr>



																</div>
															</div>

														</sec:authorize>

													</div>
												</div>

											</div>



										</form:form>
									</div>
								</div>
							</div>
						</div>
					</div>

					<!-- Results Panel -->

					<sec:authorize access="hasRole('ROLE_FACULTY')">
						<div class="card bg-white border">
							<div class="card-body">
								<div class="col-xs-12 col-sm-12">

									<div class="x_panel">
										<div class="x_title">
											<h5>Select Students to Allocate Test | Test allocated to
												: ${noOfStudentAllocated} Students</h5>

										</div>
										<div class="x_content">

											<form:form action="saveStudentTest" id="saveStudentTest"
												method="post" modelAttribute="test">

												<form:input path="id" type="hidden" />
												<form:input path="acadMonth" type="hidden" />
												<form:input path="acadYear" type="hidden" />
												<form:input path="facultyId" type="hidden" />
												<form:input path="courseId" type="hidden"
													value="${test.courseId}" />
												<c:if test="${allCampuses.size() > 0}">
													<div class="row">
														<div class="col-md-4 col-sm-6 col-xs-12 column">
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
													</div>
												</c:if>
												<div class="table-responsive">
													<table class="table table-hover" id="example">
														<thead>
															<tr>
																<th>Sr. No.</th>
																<th><input name="select_all" value="1"
																	id="example-select-all" type="checkbox" /></th>
																<th>Select to De-Allocate</th>
																<th>SAPID</th>
																<th>Roll No.</th>
																<th>Student Name</th>
																<th>Campus</th>
															</tr>
														</thead>
														<!-- <tfoot>
															<tr>
																<th></th>
																<th></th>
																<th></th>
																<th>SAPID</th>
																<th>Roll No.</th>
																<th>Student Name</th>
																<th>Campus</th>
															</tr>
														</tfoot> -->
														<tbody>

															<c:forEach var="student" items="${students}"
																varStatus="status">
																<tr>
																	<td><c:out value="${status.count}" /></td>
																	<td><c:if test="${empty student.id }">
																			<form:checkbox path="students"
																				value="${student.username}" />
																		</c:if> <c:if test="${not empty student.id }">
						            	Test Allocated
						            </c:if></td>
																	<td><c:if test="${not empty student.id }">
																			<form:checkbox class="deallocation"
																				path="deallocatedStudents"
																				value="${student.username}" />
																		</c:if> <c:if test="${empty student.id }">
																				-
																			</c:if></td>
																	<td><c:out value="${student.username}" /></td>
																	<td><c:out value="${student.rollNo}" /></td>
																	<td><c:out
																			value="${student.firstname} ${student.lastname}" /></td>

																	<td><c:out value="${student.campusName}" /></td>
																</tr>
															</c:forEach>
														</tbody>
													</table>
												</div>

												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<sec:authorize access="hasRole('ROLE_FACULTY')">
															<button id="allocateTest" class="btn btn-large btn-primary mt-2"
																onclick="return clicked();" formaction="saveStudentTest">Allocate
																Test</button>
															<button id="allocateToAllStudentButton" class="btn btn-large btn-primary mt-2"
																formaction="saveStudentTestAllStudents">Allocate
																Test to all Students</button>
															<button id="removeStudent"
																class="btn btn-large btn-primary mt-2"
																onclick="return deleteStudentFromTest();"
																formaction="removeStudentTest"
																formnovalidate="formnovalidate">De-Allocate
																Test</button>

														</sec:authorize>
														<button id="cancel" class="btn btn-danger mt-2"
															formaction="homepage" formnovalidate="formnovalidate">Cancel</button>


													</div>
												</div>


											</form:form>

										</div>
									</div>
								</div>
							</div>
						</div>
					</sec:authorize>

					<%-- 	<jsp:include page="../common/paginate.jsp">
							<jsp:param name="baseUrl" value="searchFacultyGroups" />
						</jsp:include> --%>

					<!-- /page content: END -->

				</div>

				<!-- SIDEBAR START -->
				<jsp:include page="../common/newSidebar.jsp" />
				<!-- SIDEBAR END -->
				<jsp:include page="../common/footer.jsp" />

				<script>

				function clicked() {
					var name = document
							.querySelectorAll('input[type="checkbox"]:checked').length
							+ " Students selected";

					return confirm(name);
				}
				$("#campusId")
						.on(
								'change',
								function() {

									//alert("Onchange Function called!");
									var selectedValue = $(this).val();
									var id = ${id};
									window.location = '${pageContext.request.contextPath}/viewTestDetails?testId='
											+ id + '&campusId=' + selectedValue;
									+encodeURIComponent(selectedValue);
									return false;
								});
				//var email = document.getElementById('name').value;
			</script>

			</div>
		</div>





		<!-- </body> -->

		<script type="text/javascript">

	function deleteStudentFromTest() {
	var name = document
			.querySelectorAll('.deallocation:checked').length
			+ " " + "Students selected";
	console.log("name" + name);
	
	//alert(name);
	
	var id = ${test.id};
	var str = new Array();

	var checked = document
			.querySelectorAll('input[type="checkbox"]:checked');

	for (var i = 0; i < checked.length; i++) {
		str += checked[i].value + ",";
	}
	console.log("str------>" + str);

	$
			.ajax({
				url : "${pageContext.request.contextPath}/removeStudentTest?name="

						+ str + '&id=' + id,
				

				type : "POST",
				dataType : 'application/json; charset=utf-8',
				data : str,
				beforeSend : function() {
				},
				success : function(data, status, jqXHR) {
					console.log("success");
				},
				error : function(jqXHR, status, err) {
					console.log("error");
				},
				complete : function(jqXHR, status) {

					window.location.reload();
				}
			}); 

	console.log("checked asadws");
	return confirm(name);
	return str;

};

</script>

		<!-- </html> -->

		<script type="text/javascript">
	$(document).ready(function() {
		$("#modal_link").fancybox().trigger('click');
		$("#modal_link1").fancybox().trigger('click');
		$('#modal-added').hide();
	
			$('#example').DataTable();

		 $('.closeLink').on('click', function(event){
			 console.log("called345");
		        event.stopPropagation();
		        $.fancybox.close();
		 });
		      
		
				 
		 
	});
	
	$('[data-fancybox]').fancybox({
		// Clicked on the background (backdrop) element
		clickSlide : false,
		"smallBtn" : false,
		"buttons" : false,
		"touch" : false,
		keyboard : false

	});
	
	function callFancyBox() {
		console.log('fancybox called');
		$("#test_quiz_pop1").fancybox().trigger('click');
	};

	
	function closeFancybox() {
		console.log('fancybox closed')
		$.fancybox.close();
	};

	



	
	window.checkPassword = function checkPassword(testId,testType){
		var passwordEnterdByUser = $("#passwordOfTest").val();
		$.ajax({
			type : 'POST',
			url : '${pageContext.request.contextPath}/getPasswordForTest?'
					+ 'testId=' + testId+'&password='+passwordEnterdByUser,
			success : function(data) {
				console.log('success'+ data);
				//var parsedObj = JSON.parse(data);
				//console.log("score is---" + parsedObj);
				
			 	if(data == "SUCCESS"){
					console.log('corerect password');
					
					if(testType=='Objective'){
					window.location.href = '${pageContext.request.contextPath}/startStudentTest?id='+testId;
			 	}
			 	if(testType=='Subjective'){
			 		window.location.href = '${pageContext.request.contextPath}/startStudentTestForSubjective?id='+testId;
			 	}
			 	if(testType=='Mix'){
			 		window.location.href = '${pageContext.request.contextPath}/startStudentTestForMix?id='+testId;
			 	}
					//$("#testCompleted").text(addedStatus);
			}	
				else{
					alert("Password is incorrect");
					window.location.href = '${pageContext.request.contextPath}/testList';
				} 
				
			},
			error : function(result) {
				console.log('error');
			}
		});
		
	}

	
	
	




	
</script>