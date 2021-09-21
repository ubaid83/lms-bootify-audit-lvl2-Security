<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@page import="com.spts.lms.web.utils.*"%>
<%@page import="com.spts.lms.web.utils.*"%>
<%@page import="java.util.List"%>
<%@page import="java.util.*"%>
<%@page import="java.text.*"%>
<%@page import="com.spts.lms.beans.test.Test"%>
<%@page import="com.spts.lms.beans.test.StudentTest"%>
<%@page import="com.spts.lms.services.test.StudentTestService"%>


<%
	List<Test> testList = (List<Test>) request.getAttribute("page");
	 List<StudentTest> studentTestList = (List<StudentTest>)request.getAttribute("sTestList");
	Map<Long,StudentTest> mapOfTestIdAndStudentTest = new HashMap<>();
	for(StudentTest st : studentTestList) {
		
		mapOfTestIdAndStudentTest.put(st.getTestId(),st);
	} 
	String status="";
	int countOfTest = 0;
%>
<!doctype html>

<html lang="en">
<head>
<style id="fancybox-style-noscroll" type="text/css">
.compensate-for-scrollbar, .fancybox-enabled body {
	margin-right: 17px;
}
</style>

</head>

<jsp:include page="../common/css.jsp" />

<body class="nav-md ">
	<div class="loader"></div>



	<div class="container body">
		<div class="main_container">


			<jsp:include page="../common/leftSidebar.jsp" />
			<jsp:include page="../common/topHeader.jsp" />



			<!-- page content -->
			<div class="right_col" role="main">

				<div class="dashboard_container">



					<%-- <div class="right-arrow">
							<img class="toggle_to_do"
								src="<c:url value="/resources/images/dash-right.gif" />" alt=""
								onclick="openNav2()">
						</div> --%>

					<div class="dashboard_container_spacing">
						<div class="breadcrumb">

							<c:out value="${Program_Name}" />

							<sec:authorize access="hasRole('ROLE_STUDENT')">

								<i class="fa fa-angle-right"></i>

								<c:out value="${AcadSession}" />

							</sec:authorize>
							<br> <br> <a
								href="${pageContext.request.contextPath}/homepage">My
								Courses</a> <i class="fa fa-angle-right"></i> <a
								href="${pageContext.request.contextPath}/viewCourse?id=${courseId}">
								${courseRecord.courseName}</a> <i class="fa fa-angle-right"></i>
							Test/Quiz
						</div>
						<jsp:include page="../common/alert.jsp" />

						<div class="row">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">
									<div class="x_title">
										<h2>Test/Quiz</h2>
										<div class="clearfix"></div>
									</div>
									<div class="x_content">
										<div class="table_responsive">
											<table class="table table-hover">
												<thead>
													<tr>
														<th>Name</th>
														<th>Term</th>
														<th>Start Date</th>
														<th>End Date</th>
														<th>Test Type</th>
														<th>Status</th>
													</tr>
												</thead>
												<tbody>

													<%
														for (Test test : testList) {
																																																																																																																																																																																																																					countOfTest++;	
																																																																																																																																																																																																																																																																																																																																																																																																											/* 
																																																																																																																																																																																																																																																																																																																																																																																																									    String startDate = Utils.formatDate("yyyy-MM-dd hh:mm:ss",
																																																																																																																																																																																																																																																																																																																																																																																																											"dd-MMM-yy", test.getStartDate().replace('T', ' '));
																																																																																																																																																																																																																																																																																																																																																																																																										  	String endDate = Utils.formatDate("yyyy-MM-dd hh:mm:ss",
																																																																																																																																																																																																																																																																																																																																																																																																										  	"dd-MMM-yy", test.getEndDate().replace('T', ' ')); */
													%>

													<tr>
														<th><a data-toggle="collapse"
															href="#test_quiz_acoording<%=countOfTest%>"><i
																class="pluse_ellipse fa fa-plus"></i> <%=test.getTestName()%>
														</a></th>
														<td><c:out value="<%=test.getAcadMonth()%>" /></td>
														<%-- <td><%=startDate%></td>
														<td><%=endDate%></td> --%>
														<td><c:out value="<%=test.getStartDate()%>" /></td>
														<td><c:out value="<%=test.getEndDate()%>" /></td>
														<td><%=test.getTestType()%></td>

														<td><c:url value="addTestForm" var="editurl">
																<c:param name="id"
																	value="<%=String.valueOf(test.getId())%>" />
															</c:url> <sec:authorize access="hasAnyRole('ROLE_FACULTY')">
																<a href="${editurl}" title="Edit"><i
																	class="fa fa-pencil-square-o fa-lg"></i></a>&nbsp;
                                                         </sec:authorize> <c:url
																value="deleteTest" var="deleteurl">
																<c:param name="id"
																	value="<%=String.valueOf(test.getId())%>" />
															</c:url> <sec:authorize access="hasAnyRole('ROLE_FACULTY')">
																<a href="${deleteurl}" title="Delete"
																	onclick="return confirm('Are you sure you want to delete this record?')"><i
																	class="fa fa-trash-o fa-lg"></i></a>
															</sec:authorize> <c:url value="viewTestDetails" var="detailsUrl">
																<c:param name="testId"
																	value="<%=String.valueOf(test.getId())%>" />
															</c:url> <sec:authorize access="hasAnyRole('ROLE_STUDENT')">
																<%
																	if ("Y".equals(test.getTestCompleted()) && test.getAttempt()>0) {
																%>
																<a href="${detailsUrl}" title="Details"><i
																	class="check_ellipse fa fa-check bg-green"></i>
																	Submitted</a>&nbsp; <%
 	} else   {
 %>
																<a href="${detailsUrl}" title="Details"><i
																	class="check_ellipse fa fa-check"></i> Not Submitted</a>&nbsp;
																<%
																	}
																%>
															</sec:authorize> <sec:authorize access="hasAnyRole('ROLE_FACULTY')">
																<a href="${detailsUrl}" title="Details"> <i
																	class="fa fa-info-circle" aria-hidden="true"></i>
																</a>
															</sec:authorize></td>
													</tr>
													<sec:authorize access="hasAnyRole('ROLE_STUDENT')">
														<tr id="test_quiz_acoording<%=countOfTest%>"
															class="panel-collapse collapse">
															<td colspan="6">
																<%
																	if ("Y".equals(test.getTestCompleted()) && test.getAttempt()>0) {
																%>
																<div class="feedback_des_detail">
																	<p class="font_weight_bold">Test/quiz Details</p>
																	<div class="testquiz_detail">
																		<div class="group text-right">
																			<%
																				StudentTest studentTest = mapOfTestIdAndStudentTest.get(test.getId());
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																			if(studentTest.getScore()!=null){
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																									      if(studentTest.getScore() >= test.getPassScore()) {
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																									                    status="PASS";
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																									                            }
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																									      else {
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																									                    status="FAIL";
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																									           }
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																									      
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																	}else{
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		studentTest.setScore(0.0);
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		status="NOT EVALUATED";
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																	}
																			%>
																			<%
																				if("Y".equals(test.getShowResultsToStudents())){
																			%>
																			<h6>
																				<span>Total Score:</span>
																				<%=studentTest.getScore()%></h6>
																			<h6>
																				<span>Status:</span>
																				<%=status%></h6>
																			<h6>
																				<%
																					}else {
																				%>
																				<h6>
																					<span>Total Score:</span> NA
																				</h6>
																				<h6>
																					<span>Status:</span> NA
																				</h6>
																				<h6>
																					<%
																						}
																					%>
																					<span>Reattempt:</span> Allowed
																					<%=test.getMaxAttempt()%>
																					times
																				</h6>
																				<h6>
																					<span>Remaining Attempts:</span><%=test.getMaxAttempt() - studentTest.getAttempt()%>
																				</h6>
																		</div>
																	</div>
																	<!-- <ul class="bullet1">
																			<li>This Test is on Concepts of Electricity.</li>
																			<li>All questions are mandatory.</li>
																			<li>Test carries 20% weightage of ICE marks.</li>
																			<li>Marks will be considered only if test is
																				attended before end date</li>
																		</ul> -->

																	<ul class="bullet1">
																		<li><%=test.getTestDescription()%></li>
																	</ul>
																	<%
																		if("Subjective".equals(test.getTestType())){
																	%>
																	<div class="text-center">
																		<%-- <a
																			href="${pageContext.request.contextPath}/viewStudentTestResponse?id=<%=test.getId() %>"
																			class="btn btn-primary">View Responses</a> --%>
																		<%
																			if (test.getMaxAttempt() > studentTest.getAttempt() ) {
																		%>
																		<%
																			if("N".equals(test.getIsPasswordForTest())){
																		%>
																		<a
																			href="startStudentTestForSubjective?id=<%=test.getId()%>"
																			class="btn btn-primary"
																			onclick="return confirm('Are you ready to take Test?  (Attempt: No. <%=studentTest.getAttempt() +1%>)')">Reattempt</a>
																		<%
																			}else{
																		%>

																		<a class="btn btn-primary"
																			onclick="callFancyBox1(<%=test.getId()%>)">Reattempt</a>

																		<%
																			}
																		%>

																		<%
																			}else{
																		%>
																		<a href="startStudentTest?id=<%=test.getId()%>"
																			class="btn btn-primary">Reattempt</a>
																		<%
																			}
																		%>
																		<%
																			if((new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(test.getEndDate()).before(Utils.getInIST())) && "Y".equals(test.getShowResultsToStudents())) {
																		%>
																		<a
																			href="${pageContext.request.contextPath}/downloadTestReportByTestIdAndUsername?testId=<%=test.getId()%>"
																			class="btn btn-primary">Download a Test Report</font>
																		</a>
																		
																		<a
																			href="${pageContext.request.contextPath}/downloadTestReportByTestIdAndUsernameAttemptWise?testId=<%=test.getId()%>"
																			class="btn btn-primary">Download a Test Report Attempt-Wise
																		</a>
																		<%
																			}
																		%>

																	</div>
																	<%
																		}else if("Mix".equals(test.getTestType())){
																			
																	%>
																	<div class="text-center">
																		<%-- <a
																			href="${pageContext.request.contextPath}/viewStudentTestResponse?id=<%=test.getId() %>"
																			class="btn btn-primary">View Responses</a> --%>
																		<%
																			if (test.getMaxAttempt() > studentTest.getAttempt() ) {
																		%>
																		<%
																			if("N".equals(test.getIsPasswordForTest())){
																		%>
																		<a
																			href="startStudentTestForMix?id=<%=test.getId()%>"
																			class="btn btn-primary"
																			onclick="return confirm('Are you ready to take Test?  (Attempt: No. <%=studentTest.getAttempt() +1%>)')">Reattempt</a>
																		<%
																			}else{
																		%>

																	<a class="btn btn-primary"
																			onclick="callFancyBox1(<%=test.getId()%>)">Reattempt</a>

																		<%
																			}
																		%>

																		<%
																			}else{
																		%>
																		<a href="startStudentTest?id=<%=test.getId()%>"
																			class="btn btn-primary">Reattempt</a>
																		<%
																			}
																		%>
																		<%
																			if((new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(test.getEndDate()).before(Utils.getInIST())) && "Y".equals(test.getShowResultsToStudents())) {
																		%>
																		<a
																			href="${pageContext.request.contextPath}/downloadTestReportByTestIdAndUsername?testId=<%=test.getId()%>"
																			class="btn btn-primary">Download a Test Report</font>
																		</a>
																		<a
																			href="${pageContext.request.contextPath}/downloadTestReportByTestIdAndUsernameAttemptWise?testId=<%=test.getId()%>"
																			class="btn btn-primary">Download a Test Report Attempt-Wise
																		</a>
																		<%
																			}
																		%>

																	</div>
																	<%
																		}else{
																	%>
																	<div class="text-center">

																		<%-- <a
																			href="${pageContext.request.contextPath}/viewStudentTestResponse?id=<%=test.getId() %>"
																			class="btn btn-primary">View Responses</a> --%>
																		<%
																			if (test.getMaxAttempt() > studentTest.getAttempt() ) {
																		%>
																		<%
																			if("N".equals(test.getIsPasswordForTest())){
																		%>
																		<a href="startStudentTest?id=<%=test.getId()%>"
																			class="btn btn-primary"
																			onclick="return confirm('Are you ready to take Test?  (Attempt: No. <%=studentTest.getAttempt() +1%>)')">Reattempt</a>
																		<%
																			}else{
																		%>

																		<a class="btn btn-primary"
																			onclick="callFancyBox1(<%=test.getId()%>)">Reattempt</a>

																		<%
																			}
																		%>
																		<%
																			}else{
																		%>
																		<a href="startStudentTest?id=<%=test.getId()%>"
																			class="btn btn-primary">Reattempt</a>
																		<%
																			}
																		%>
																		<%
																			if((new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(test.getEndDate()).before(Utils.getInIST())) && "Y".equals(test.getShowResultsToStudents())) {
																		%>
																		<a
																			href="${pageContext.request.contextPath}/downloadTestReportByTestIdAndUsername?testId=<%=test.getId()%>"
																			class="btn btn-primary">Download a Test Report</font>
																		</a>
																		<a
																			href="${pageContext.request.contextPath}/downloadTestReportByTestIdAndUsernameAttemptWise?testId=<%=test.getId()%>"
																			class="btn btn-primary">Download a Test Report Attempt-Wise
																		</a>
																		<%
																			}
																		%>

																	</div>

																	<%
																		}
																	%>

																</div> <%
 	} else   {
 %>
																<div class="feedback_des_detail">
																	<p class="font_weight_bold">Description Details</p>
																	<ul class="bullet1">
																		<li><%=test.getTestDescription()%></li>
																	</ul>
																	</p>

																	<!-- <p class="font_weight_bold">Description Details</p>
																		<ul class="bullet1">
																			<li>This Test is on Concepts of Electricity.</li>
																			<li>All questions are mandatory.</li>
																			<li>Test carries 20% weightage of ICE marks.</li>
																			<li>Marks will be considered only if test is
																				attended before end date</li>
																	</ul> -->
																	<%
																		if("Subjective".equals(test.getTestType())){
																	%>

																	<div class="text-center">
																		<!--changes by hiren button -->
																		<c:url value="startStudentTestForSubjective"
																			var="takeTestUrl">
																			<c:param name="id"
																				value="<%=String.valueOf(test.getId())%>" />
																		</c:url>
																		<%
																			if("N".equals(test.getIsPasswordForTest())){
																		%>
																		<a href="${takeTestUrl}" class="btn btn-primary"
																			onclick="return confirm('Are you ready to take Test? (Attempt: No. <%= test.getAttempt() +1 %>)')">Provide
																			Test</a>
																		<%
																			}else{
																		%>

																		<a class="btn btn-primary"
																			onclick="callFancyBox1(<%=test.getId()%>)">Provide
																			Test</a>

																		<%
																			}
																		%>
																	</div>
																	<%
																		}else if("Mix".equals(test.getTestType())){
																	%>

																	<div class="text-center">
																		<!--changes by hiren button -->
																		<c:url value="startStudentTestForMix"
																			var="takeTestUrl">
																			<c:param name="id"
																				value="<%=String.valueOf(test.getId())%>" />
																		</c:url>
																		<%
																			if("N".equals(test.getIsPasswordForTest())){
																		%>
																		<a href="${takeTestUrl}" class="btn btn-primary"
																			onclick="return confirm('Are you ready to take Test? (Attempt: No. <%= test.getAttempt() +1 %>)')">Provide
																			Test</a>
																		<%
																			}else{
																		%>

																		<a class="btn btn-primary"
																			onclick="callFancyBox1(<%=test.getId()%>)">Provide
																			Test</a>

																		<%
																			}
																		%>
																	</div>
																	<%
																		}else{
																	%>
																	<div class="text-center">
																		<!--changes by hiren button -->
																		<c:url value="startStudentTest" var="takeTestUrl">
																			<c:param name="id"
																				value="<%=String.valueOf(test.getId())%>" />
																		</c:url>

																		<%
																			if("N".equals(test.getIsPasswordForTest())){
																		%>
																		<a href="${takeTestUrl}" class="btn btn-primary"
																			onclick="return confirm('Are you ready to take Test? (Attempt: No. <%= test.getAttempt() +1 %>)')">Provide
																			Test</a>
																		<%
																			}else{
																		%>

																		<a class="btn btn-primary"
																			onclick="callFancyBox1(<%=test.getId()%>)">Provide
																			Test</a>

																		<%
																			}
																		%>
																	</div>
																	<%
																		}
																	%>
																	<%-- <div class="text-center">
																				<!--changes by hiren button -->
																				<c:url value="startStudentTest" var="takeTestUrl">
																				<c:param name="id"
																					value="<%=String.valueOf(test.getId())%>" />
																			</c:url>
																			
																			<a href="${takeTestUrl}" class="btn btn-primary">Provide
																				Test</a>
																		</div> --%>
																</div> <%
 	}
 %>
															</td>
														</tr>



														<div id="test_quiz_pop<%=test.getId()%>"
															class="fancy_pop_box1">
															<h5>Test Is Password Protected, Please Enter a
																Password <span style="color: red">*</span></h5>
															<hr>
															<div class="text-center">
																<input type="password" value=""
																	id="passwordOfTest<%=test.getId()%>"
																	required="required" placeholder="Enter a Password" /><br>
																<br> <a data-fancybox
																	data-src="#test_quiz_pop<%=test.getId()%>"
																	href="javascript;;" class="btn btn-primary btntestquiz"
																	onclick="checkPassword(<%=test.getId()%>,'<%=test.getTestType()%>')">Yes</a>

																<!-- <a href="javascript;;" class="btn btn-primary"
																	onclick="closeFancybox()">Cancel</a> -->
																<a href="#"
																	class="btn btn-primary btntestquiz closeLink">Close</a>
																<hr>



															</div>
														</div>
													</sec:authorize>



													<%
														}
													%>

												</tbody>
											</table>



										</div>





									</div>
								</div>
							</div>
						</div>
					</div>

				</div>

			</div>


			<%-- <jsp:include page="../common/studentToDo.jsp" /> --%>

		</div>


		<!-- /page content -->


		<%-- 	<jsp:include page="../common/footer.jsp" /> --%>
		<jsp:include page="../common/testFooter.jsp" />

	</div>
	</div>
	<!-- 	<script type="text/javascript">
		$(document).ready(function() {
			function disableBack() {
				window.history.forward()
			}

			window.onload = disableBack();
			window.onpageshow = function(evt) {
				if (evt.persisted)
					disableBack()
			}
		});

		$('[data-fancybox]').fancybox({
			// Clicked on the background (backdrop) element
			clickSlide : false,
			"smallBtn" : false,
			"buttons" : false,
			"touch" : false,
			keyboard : false

		});
		function closeFancybox() {
			console.log('fancybox closed')
			$.fancybox.close();
		}
	</script> -->

	<script type="text/javascript">
	
	$(document).ready(function() {
		$("#modal_link").fancybox().trigger('click');
		$("#modal_link1").fancybox().trigger('click');
		$('#modal-added').hide();
		
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
	
	function callFancyBox1(id) {
		console.log('fancybox called');
		console.log("fancy box id --->"+id );
		$("#test_quiz_pop"+id).fancybox().trigger('click');
	};

	
	function closeFancybox() {
		console.log('fancybox closed')
		$.fancybox.close();
	};

	
	</script>

	<script>
	
	window.checkPassword = function checkPassword(testId,testType){
		console.log("entered--->"+ testId);
		var passwordEnterdByUser = $("#passwordOfTest"+testId).val();
		$param = {testId:testId,password:passwordEnterdByUser};
		console.log("password entered--->"+ passwordEnterdByUser);
		$.ajax({
			type : 'POST',
			url : '${pageContext.request.contextPath}/getPasswordForTest',
			data:$param,
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







</body>
</html>