<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<html lang="en">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<jsp:include page="../common/css.jsp" />


<body class="nav-md footer_fixed">


	<!-- <div class="loader"></div> -->

	<div class="container body">
		<div class="main_container">

			<%-- <jsp:include page="../common/leftSidebar.jsp">
				<jsp:param name="courseId" value="${courseId}" />
			</jsp:include> --%>
			<jsp:include page="../common/leftSidebar.jsp" />
			<jsp:include page="../common/topHeader.jsp">
				<jsp:param value="gradeMenu" name="activeMenu" />
			</jsp:include>


			<!-- page content: START -->
			<div class="right_col" role="main">

				<div class="dashboard_container">

					<div class="dashboard_container_spacing">

						<div class="breadcrumb">
							<c:out value="${Program_Name}" />

							<sec:authorize access="hasRole('ROLE_STUDENT')">

								<i class="fa fa-angle-right"></i>

								<c:out value="${AcadSession}" />

							</sec:authorize>

							<br>
							<br> <a href="${pageContext.request.contextPath}/homepage">Dashboard</a>
							<i class="fa fa-angle-right"></i> Grade Center For
							${course.courseName }
						</div>
						<jsp:include page="../common/alert.jsp" />

						<!-- Input Form Panel -->
						<div class="row">

							<div class="col-md-12 col-xs-12 col-sm-12">
								<div class="x_panel">


									<div class="x_content">
										<form>
											<div class="row">
												<div class="col-md-4 col-sm-6 col-xs-12">
													<div class="form-group">

														<label for="courseId">Course</label> <select id="courseId"
															name="courseId" class="form-control">
															<c:if test="${courseId eq  null }">
																<option value="">Select Course</option>
															</c:if>
															<c:forEach var="course" items="${courseForWieghtage}"
																varStatus="status">
																<c:if test="${courseId eq course.id }">
																	<option value="${course.id}" selected>${course.courseName}</option>
																</c:if>
																<c:if test="${courseId ne course.id }">
																	<option value="${course.id}">${course.courseName}</option>
																</c:if>
															</c:forEach>
														</select>
													</div>
												</div>
											</div>

										</form>
									</div>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">
									<div class="ui-105-content">
										<ul class="nav nav-tabs nav-justified">
											<li class="link-one"><a href="#login-block"
												data-toggle="tab"><i class="fa fa-inbox"></i>ASSIGNMENT</a></li>
											<li class="link-two" id="link-two"><a
												href="#register-block" data-toggle="tab"><i
													class="fa fa-sign-in"></i>TEST</a></li>
										</ul>
										<div class="tab-content">
											<div class="tab-pane active fade in" id="login-block">
												<!-- Login Block Form -->
												<div class="login-block-form">
													<form:form cssClass="form" role="form" action=""
														method="post" modelAttribute="grade" id="">
														<div class="table-responsive">
															<table id="inboxTable"
																class="table table-striped table-hover"
																style="font-size: 12px">
																<thead>
																	<tr>
																		<th>Sr. No.</th>
																		<th>Assignment Name</th>
																		<th>Assignment Type</th>
																		<th>Score</th>
																		<th>Maximum Score</th>
																		<th>Weight Assigned</th>
																		<th>Grand Total</th>
																		<th>Average For All Assignments</th>
																	</tr>
																</thead>
																<tbody>
																	<c:forEach var="assignment"
																		items="${ListOfAssignments}" varStatus="status">
																		<tr>
																			<td><c:out value="${status.count}" /></td>
																			<td><c:out value="${assignment.assignmentName} " /></td>
																			<td><c:out value="${assignment.assignmentType} " /></td>
																			<td id="assignmentScore${status.count}"><c:out
																					value="${assignment.score}" /></td>
																			<td><c:out value="${assignment.maxScore}" /></td>
																			<td id="wieghtage${status.count}"><c:out
																					value="${assignment.wieghtageassigned}" /></td>
																			<td></td>
																			<td colspan="5">
																				<div id="resultForAssignment"
																					style="padding-left: 50%; font-size: 30px; color: black;"></div>
																			</td>
																		</tr>
																	</c:forEach>
																</tbody>
															</table>



														</div>
													</form:form>
												</div>
											</div>
											<!-- Results Panel -->
											<div class="tab-pane active fade in" id="register-block">
												<!-- Login Block Form -->
												<div class="register-block-form">
													<form:form cssClass="form" role="form" action=""
														method="post" modelAttribute="grade" id="">
														<div class="table-responsive">
															<table id="outboxTable"
																class="table table-striped table-hover"
																style="font-size: 12px">
																<thead>
																	<tr>
																		<th>Sr. No.</th>
																		<th>Test Name</th>
																		<th>Test Type</th>
																		<th>Score</th>
																		<th>Maximum Score</th>
																		<th>Weight</th>
																		<th>Grand Total</th>
																		<th>Average Of All Tests</th>
																	</tr>
																</thead>
																<tbody>
																	<c:forEach var="test" items="${listOfTests}"
																		varStatus="status">
																		<tr>
																			<td><c:out value="${status.count}" /></td>
																			<td><c:out value="${test.testName} " /></td>
																			<td><c:out value="${test.testType}" /></td>
																			<td id="testScore${status.count}"><c:out
																					value="${test.score}" /></td>
																			<td><c:out value="${test.maxScore}" /></td>
																			<td id="wieghtageTest${status.count}"><c:out
																					value="${test.wieghtageassigned}" /></td>
																			<td></td>
																			<td colspan="5">
																				<div id="resultForTest"
																					style="padding-left: 50%; font-size: 30px; color: black;"></div>
																			</td>
																		</tr>
																	</c:forEach>
																</tbody>
															</table>

															<div id="resultForTest"
																style="padding-left: 45%; font-size: 30px; color: #000;"></div>

														</div>
													</form:form>
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>

						<!-- Results Panel -->
					</div>
				</div>

			</div>

			<!-- /page content: END -->
			<jsp:include page="../common/footer.jsp" />

		</div>
	</div>
</body>
<script>
	$(document)
			.ready(
					function() {
						var assignmentScore = new Array();
						var weightage = new Array();
						var testScore = new Array();
						var wieghtageTest = new Array();
						var finalValue;
						var finalValueTest;
						$("#courseId")
								.on(
										'change',
										function() {
											var selectedValue = $(this).val();
											window.location = '${pageContext.request.contextPath}/getAllAssignments?courseId='
													+ encodeURIComponent(selectedValue);
											return false;
										});
						finalValue = generateWeightedAverage('#inboxTable td',
								assignmentScore, weightage, 'assignmentScore',
								'wieghtage');
						finalValueTest = generateWeightedAverage(
								'#outboxTable td', testScore, wieghtageTest,
								'testScore', 'wieghtageTest');
						$("#resultForAssignment").text(finalValue);
						$("#resultForTest").text(finalValueTest);
						$("#register-block").hide();
						$("#link-two").on("click", function() {
							$("#register-block").show();
						});
					});
	function generateWeightedAverage(tableId, array1, array2, idText1, idText2) {
		var evaluatedValue;
		$(tableId).each(function() {
			var idAttribute = $(this).attr('id');
			if (idAttribute != undefined) {
				if (idAttribute.includes(idText1)) {
					array1.push($(this).text());
				} else if (idAttribute.includes(idText2)) {
					array2.push($(this).text());
				}
			}
		});
		evaluatedValue = calculateWegihtage(array1, array2);
		return evaluatedValue;
	}
	function calculateWegihtage(x, y) {
		var sum = 0;
		for (var a = 0; a < x.length; a++) {
			var output = 0;
			output = performCalc(x[a], y[a]);
			sum = sum + output;
		}
		return sum;
	}
	function performCalc(a, b) {
		var calc = a * (b / 100);
		return calc;
	}
</script>
</html>
