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

<div class="d-flex dataTableBottom paddingFix1"
	id="facultyAssignmentPage">
	<sec:authorize access="hasRole('ROLE_FACULTY')">
		<jsp:include page="../common/newLeftNavBarFaculty.jsp" />
		<jsp:include page="../common/newLeftSidebarFaculty.jsp" />
		<jsp:include page="../common/rightSidebarFaculty.jsp" />
	</sec:authorize>
	<sec:authorize access="hasRole('ROLE_ADMIN')">

		<jsp:include page="../common/newAdminLeftNavBar.jsp" />
		<jsp:include page="../common/rightSidebarAdmin.jsp" />
	</sec:authorize>

	<!-- DASHBOARD BODY STARTS HERE -->

	<div class="container-fluid m-0 p-0 dashboardWraper">

		<sec:authorize access="hasRole('ROLE_FACULTY')">
			<jsp:include page="../common/newTopHeaderFaculty.jsp" />
		</sec:authorize>
		<sec:authorize access="hasRole('ROLE_ADMIN')">
			<jsp:include page="../common/newAdminTopHeader.jsp" />
		</sec:authorize>

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
							<li class="breadcrumb-item active" aria-current="page">Test
								List</li>
						</ol>
					</nav>

					<jsp:include page="../common/alert.jsp" />




					<!-- Results Panel -->

					<c:choose>
						<c:when test="${page.pageItems.size() > 0}">
							<div class="card bg-white border">
								<div class="card-body">
									<div class="col-xs-12 col-sm-12">
										<div class="x_panel">
											<div class="x_title">
												<h5>Tests List | ${page.pageItems.size()} Records Found</h5>

											</div>
											<div class="x_content">
												<div class="table-responsive testAssignTable">
													<table
														class="table-striped table-hover w-100 text-center table-bordered"
														id="tableTest">
														<thead>
															<tr>
																<th class="pt-2 pb-2">Sr. No.</th>
																<th>Name</th>
																<th>Course</th>
																<th>Test Type</th>
																<th>Academic Month</th>
																<th>Academic Year</th>
																<th>Start Date</th>
																<!-- <th>Due Date</th> -->
																<th>End Date</th>
																<th>Show Results</th>
																<th>Show Test Reports</th>
																<th>Actions</th>
															</tr>
														</thead>
														<tbody>

															<c:forEach var="test" items="${page.pageItems}"
																varStatus="status">
																<tr>
																	<td class="pt-3 pb-3"><c:out
																			value="${status.count}" /></td>
																	<td><c:out value="${test.testName}" /></td>
																	<td><c:out value="${test.courseName}" /></td>
																	<td><c:out value="${test.testType}" /></td>
																	<td><c:out value="${test.acadMonth}" /></td>
																	<td><c:out value="${test.acadYear}" /></td>
																	<td><c:out
																			value="${fn:replace(test.startDate, 
                                'T', ' ')}"></c:out></td>
																	<td><c:out
																			value="${fn:replace(test.endDate, 
                                'T', ' ')}"></c:out></td>
																	<%-- <td><c:out
																			value="${fn:replace(test.dueDate, 
                                'T', ' ')}"></c:out></td> --%>
																	<%-- <td><c:if
																			test="${test.showResultsToStudents eq 'Y' }"> Already Shown
</c:if> <c:if test="${test.showResultsToStudents ne 'Y' }">


																			<div class="col-sm-12 column" style="float: right;"
																				id="show">


																				<a href="#" id="show${test.id}" class="showClass"
																					onclick='document.getElementById(this.id).removeAttribute("href");'>
																					Show Now </a>


																			</div>
																		</c:if></td> --%>
																	<td>
																	<c:choose>
																	<c:when test="${'Y' eq test.isCreatedByAdmin}">
																			<sec:authorize access="hasAnyRole('ROLE_ADMIN')">
																				<c:if
																			test="${test.showResultsToStudents eq 'Y' }">
																			<div class="col-sm-12 column" style="float: right;"
																				id="hide${test.id}">


																				<a href="#" id="hide${test.id}" class="hideClass">
																					Hide</a>


																			</div>
																			<div class="col-sm-12 column"
																				style="float: right; display: none;"
																				id="show${test.id}">


																				<a href="#" id="show${test.id}" class="showClass"
																					onclick='document.getElementById(this.id).removeAttribute("href");'>
																					Show Now </a>


																			</div>
																		</c:if> <c:if test="${test.showResultsToStudents ne 'Y' }">


																			<div class="col-sm-12 column" style="float: right;"
																				id="show${test.id}">


																				<a href="#" id="show${test.id}" class="showClass"
																					onclick='document.getElementById(this.id).removeAttribute("href");'>
																					Show Now </a>


																			</div>
																			<div class="col-sm-12 column"
																				style="float: right; display: none;"
																				id="hide${test.id}">


																				<a href="#" id="hide${test.id}" class="hideClass">
																					Hide</a>


																			</div>
																		</c:if>
																			</sec:authorize>
																			<sec:authorize access="hasAnyRole('ROLE_FACULTY')">
																			NA
																			</sec:authorize>
																	</c:when>
																	<c:otherwise>
																	<c:if
																			test="${test.showResultsToStudents eq 'Y' }">
																			<div class="col-sm-12 column" style="float: right;"
																				id="hide${test.id}">


																				<a href="#" id="hide${test.id}" class="hideClass">
																					Hide</a>


																			</div>
																			<div class="col-sm-12 column"
																				style="float: right; display: none;"
																				id="show${test.id}">


																				<a href="#" id="show${test.id}" class="showClass"
																					onclick='document.getElementById(this.id).removeAttribute("href");'>
																					Show Now </a>


																			</div>
																		</c:if> <c:if test="${test.showResultsToStudents ne 'Y' }">


																			<div class="col-sm-12 column" style="float: right;"
																				id="show${test.id}">


																				<a href="#" id="show${test.id}" class="showClass"
																					onclick='document.getElementById(this.id).removeAttribute("href");'>
																					Show Now </a>


																			</div>
																			<div class="col-sm-12 column"
																				style="float: right; display: none;"
																				id="hide${test.id}">


																				<a href="#" id="hide${test.id}" class="hideClass">
																					Hide</a>


																			</div>
																		</c:if>
																		</c:otherwise>
																		</c:choose>
																		</td>
																		
																		<td>
																		<c:choose>
																	<c:when test="${'Y' eq test.isCreatedByAdmin}">
																	<sec:authorize access="hasAnyRole('ROLE_ADMIN')">
																		<c:if test="${userBean.username eq test.facultyId}">
																			<c:if test="${test.showReportsToStudents eq 'Y' }">
																				<div class="col-sm-12 column" style="float: right;" id="hideReports${test.id}">
																					<a href="#" id="hideReports${test.id}" class="hideRportsClass">Hide</a>
																				</div>
																				<div class="col-sm-12 column" style="float: right; display: none;" id="showReports${test.id}">
																					<a href="#" id="showReports${test.id}" class="showReportsClass" onclick='document.getElementById(this.id).removeAttribute("href");'>
																					Show Now </a>
																				</div>
																			</c:if>
																			<c:if test="${test.showReportsToStudents ne 'Y' }">
																				<div class="col-sm-12 column" style="float: right;" id="showReports${test.id}">
																					<a href="#" id="showReports${test.id}" class="showReportsClass" onclick='document.getElementById(this.id).removeAttribute("href");'>
																					Show Now </a>
																				</div>
																				<div class="col-sm-12 column" style="float: right; display: none;" id="hideReports${test.id}">
																					<a href="#" id="hideReports${test.id}" class="hideRportsClass">Hide</a>
																				</div>
																			</c:if>
																		</c:if>
																	</sec:authorize>
																	<sec:authorize access="hasAnyRole('ROLE_FACULTY')">
																	NA
																	</sec:authorize>
																	</c:when>
																	<c:otherwise>
																		<c:if test="${userBean.username eq test.facultyId}">
																			<c:if test="${test.showReportsToStudents eq 'Y' }">
																				<div class="col-sm-12 column" style="float: right;" id="hideReports${test.id}">
																					<a href="#" id="hideReports${test.id}" class="hideRportsClass">Hide</a>
																				</div>
																				<div class="col-sm-12 column" style="float: right; display: none;" id="showReports${test.id}">
																					<a href="#" id="showReports${test.id}" class="showReportsClass" onclick='document.getElementById(this.id).removeAttribute("href");'>
																					Show Now </a>
																				</div>
																			</c:if>
																			<c:if test="${test.showReportsToStudents ne 'Y' }">
																				<div class="col-sm-12 column" style="float: right;" id="showReports${test.id}">
																					<a href="#" id="showReports${test.id}" class="showReportsClass" onclick='document.getElementById(this.id).removeAttribute("href");'>
																					Show Now </a>
																				</div>
																				<div class="col-sm-12 column" style="float: right; display: none;" id="hideReports${test.id}">
																					<a href="#" id="hideReports${test.id}" class="hideRportsClass">Hide</a>
																				</div>
																			</c:if>
																		</c:if>
																		</c:otherwise>
																		</c:choose>
																		</td>
																		
																	<td>
																	<sec:authorize access="hasAnyRole('ROLE_ADMIN')">
																		<c:url value="addTestFormByAdmin" var="editurl">
																			<c:param name="id" value="${test.id}" />
																		</c:url> 
																			<a href="${editurl}" title="Edit"><i
																				class="fa fa-pen-square"></i></a>&nbsp;
                                                        					<c:url
																			value="addTestQuestionForm" var="addurl">
																			<c:param name="id" value="${test.id}" />
																		</c:url>
																			<c:if test="${ test.testType ne 'Mix' }">
																				<a href="${addurl}" title="Configure Questions"><i
																					class="fa fa-question-circle" aria-hidden="true"
																					style="font-size: medium;"></i></a>&nbsp;
																		</c:if>
																	 <c:url value="deleteTest" var="deleteurl">
																			<c:param name="id" value="${test.id}" />
																		</c:url>
																			<a href="${deleteurl}" title="Delete"
																				onclick="return confirm('Are you sure you want to delete this record?')"><i
																				class="fas fa-trash"></i></a>
																		
																			<c:url value="viewTestDetailsByAdmin" var="detailsUrl">
																				<c:param name="testId" value="${test.id}" />
																			</c:url>
																			
																			<a href="${detailsUrl}" title="Details"><i
																				class="fa fa-info-circle fa-lg"></i></a>&nbsp;
																				
																			<c:if test="${ test.isPasswordForTest eq 'N' }">
																				<c:if test="${ test.testType eq 'Objective' }">
																					<c:url value="startStudentTestNew" var="startTest">
																						<c:param name="id" value="${test.id}" />
																					</c:url>
																					<a href="${startTest}" title="Take Demo Test"><i
																						class="fa fa-file-alt"></i></a>
																				</c:if>
																				<c:if test="${ test.testType eq 'Subjective' }">
																					<c:url value="startStudentTestUpdatedForSubjective"
																						var="startTest">
																						<c:param name="id" value="${test.id}" />
																					</c:url>
																					<a href="${startTest}" title="Take Demo Test"><i
																						class="fa fa-file-alt"></i></a>
																				</c:if>
																				<c:if test="${ test.testType eq 'Mix' }">
																					<c:url value="startStudentTestUpdatedForMix"
																						var="startTest">
																						<c:param name="id" value="${test.id}" />
																					</c:url>
																					<a href="${startTest}" title="Take Demo Test"><i
																						class="fa fa-file-alt"></i></a>
																				</c:if>
																			</c:if>
																			<c:if test="${ test.isPasswordForTest eq 'Y' }">
																				<a data-toggle="modal"
																					data-target="#testPassword${status.count}"
																					title="Take Demo Test"><i
																					class="fa fa-file-alt"></i></a>
																			</c:if>
																			
																			&nbsp;
																	</sec:authorize>
																	
																	
																	
																	
																	<c:choose>
																	<c:when test="${'Y' eq test.isCreatedByAdmin }">
																		<c:url
																			value="addTestQuestionForm" var="addurl">
																			<c:param name="id" value="${test.id}" />
																		</c:url> <sec:authorize access="hasAnyRole('ROLE_FACULTY')">
																			<c:if test="${ test.testType ne 'Mix' }">
																				<%-- <a href="${addurl}" title="Configure Questions"><i
																					class="fa fa-question-circle" aria-hidden="true"
																					style="font-size: medium;"></i></a --%>>&nbsp;
																		</c:if>
																		<c:url value="viewTestDetails" var="detailsUrl">
																				<c:param name="testId" value="${test.id}" />
																			</c:url>
																			
																			<a href="${detailsUrl}" title="Details"><i
																				class="fa fa-info-circle fa-lg"></i></a>
																				</sec:authorize> <sec:authorize
																			access="hasAnyRole('ROLE_FACULTY')">

																			<c:if test="${ test.isPasswordForTest eq 'N' }">
																				<c:if test="${ test.testType eq 'Objective' }">
																					<c:url value="startStudentTestNew" var="startTest">
																						<c:param name="id" value="${test.id}" />
																					</c:url>
																					<a href="${startTest}" title="Take Demo Test"><i
																						class="fa fa-file-alt"></i></a>
																				</c:if>
																				<c:if test="${ test.testType eq 'Subjective' }">
																					<c:url value="startStudentTestUpdatedForSubjective"
																						var="startTest">
																						<c:param name="id" value="${test.id}" />
																					</c:url>
																					<a href="${startTest}" title="Take Demo Test"><i
																						class="fa fa-file-alt"></i></a>
																				</c:if>
																				<c:if test="${ test.testType eq 'Mix' }">
																					<c:url value="startStudentTestUpdatedForMix"
																						var="startTest">
																						<c:param name="id" value="${test.id}" />
																					</c:url>
																					<a href="${startTest}" title="Take Demo Test"><i
																						class="fa fa-file-alt"></i></a>
																				</c:if>
																			</c:if>
																			<c:if test="${ test.isPasswordForTest eq 'Y' }">
																				<a data-toggle="modal"
																					data-target="#testPassword${status.count}"
																					title="Take Demo Test"><i
																					class="fa fa-file-alt"></i></a>
																			</c:if>
																		</sec:authorize>
																	</c:when>
																	<c:otherwise>
																	<c:url value="createTestForm" var="editurl">
																			<c:param name="id" value="${test.id}" />
																		</c:url> <sec:authorize access="hasAnyRole('ROLE_FACULTY')">
																			<a href="${editurl}" title="Edit"><i
																				class="fa fa-pen-square"></i></a>&nbsp;
                                                         </sec:authorize> <c:url
																			value="addTestQuestionForm" var="addurl">
																			<c:param name="id" value="${test.id}" />
																		</c:url> <sec:authorize access="hasAnyRole('ROLE_FACULTY')">
																			<c:if test="${ test.testType ne 'Mix' }">
																				<a href="${addurl}" title="Configure Questions"><i
																					class="fa fa-question-circle" aria-hidden="true"
																					style="font-size: medium;"></i></a>&nbsp;
																		</c:if>
																		</sec:authorize> <c:url value="deleteTest" var="deleteurl">
																			<c:param name="id" value="${test.id}" />
																		</c:url> <sec:authorize access="hasAnyRole('ROLE_FACULTY')">
																			<a href="${deleteurl}" title="Delete"
																				onclick="return confirm('Are you sure you want to delete this record?')"><i
																				class="fas fa-trash"></i></a>
																		</sec:authorize> <sec:authorize
																			access="hasAnyRole('ROLE_FACULTY','ROLE_DEAN''ROLE_HOD')">
																			<c:url value="viewTestDetails" var="detailsUrl">
																				<c:param name="testId" value="${test.id}" />
																			</c:url>
																			<c:url value="exportTestForm" var="exportTestUrl">
																				<c:param name="id" value="${test.id}" />
																			</c:url>
																			<a href="${detailsUrl}" title="Details"><i
																				class="fa fa-info-circle fa-lg"></i></a>&nbsp;
																				<a href="${exportTestUrl}" title="Export Test"><i
																				class="fas fa-file-excel"></i></a>&nbsp;</sec:authorize> <sec:authorize
																			access="hasAnyRole('ROLE_FACULTY')">

																			<c:if test="${ test.isPasswordForTest eq 'N' }">
																				<c:if test="${ test.testType eq 'Objective' }">
																					<c:url value="startStudentTestNew" var="startTest">
																						<c:param name="id" value="${test.id}" />
																					</c:url>
																					<a href="${startTest}" title="Take Demo Test"><i
																						class="fa fa-file-alt"></i></a>
																				</c:if>
																				<c:if test="${ test.testType eq 'Subjective' }">
																					<c:url value="startStudentTestUpdatedForSubjective"
																						var="startTest">
																						<c:param name="id" value="${test.id}" />
																					</c:url>
																					<a href="${startTest}" title="Take Demo Test"><i
																						class="fa fa-file-alt"></i></a>
																				</c:if>
																				<c:if test="${ test.testType eq 'Mix' }">
																					<c:url value="startStudentTestUpdatedForMix"
																						var="startTest">
																						<c:param name="id" value="${test.id}" />
																					</c:url>
																					<a href="${startTest}" title="Take Demo Test"><i
																						class="fa fa-file-alt"></i></a>
																				</c:if>
																			</c:if>
																			<c:if test="${ test.isPasswordForTest eq 'Y' }">
																				<a data-toggle="modal"
																					data-target="#testPassword${status.count}"
																					title="Take Demo Test"><i
																					class="fa fa-file-alt"></i></a>
																			</c:if>
																			
																			&nbsp;</sec:authorize>
																			
																			</c:otherwise>
																			</c:choose>
																			
																			</td>
																</tr>
															</c:forEach>


														</tbody>
													</table>
												</div>
											</div>
										</div>
									</div>
								</div>


								<%-- 	<jsp:include page="../common/paginate.jsp">
							<jsp:param name="baseUrl" value="searchFacultyGroups" />
						</jsp:include> --%>

								<!-- /page content: END -->

							</div>
						</c:when>
					</c:choose>
				</div>

				<c:forEach var="test" items="${page.pageItems}" varStatus="status">
					<div id="modalTestPassword">
						<div class="modal fade fnt-13" id="testPassword${status.count}"
							tabindex="-1" role="dialog" aria-labelledby="giveTestPassword"
							aria-hidden="true">
							<div
								class="modal-dialog modal-dialog-centered modal-dialog-scrollable"
								role="document">
								<div class="modal-content">
									<div class="modal-header text-white">
										<h6 class="modal-title" id="giveTestTitle">Test Is
											Password Protected, Please Enter a Password Provided by
											Faculty</h6>
										<button type="button" class="close" data-dismiss="modal"
											aria-label="Close">
											<span aria-hidden="true">&times;</span>
										</button>
									</div>
									<div class="modal-body pt-0 bg-light text-center">
										<h6 class="mt-2 text-capitalize">${test.testName}</h6>
										<hr />
										<p>
											Please enter test password. <span class="text-danger">*
										</p>
										<input class="form-control text-center" type="password"
											value="" id="passwordOfTest${test.id}" required="required"
											placeholder="Enter a Password" />

									</div>
									<div class="modal-footer text-center">
										<a class="btn btn-modalSub text-white"
											onclick="checkPassword(${test.id},'${test.testType}')">Go</a>
										<button type="button" class="btn btn-modalClose"
											data-dismiss="modal">Cancel</button>
									</div>
								</div>
							</div>
						</div>
					</div>
				</c:forEach>

				<!-- SIDEBAR START -->
				<sec:authorize access="hasRole('ROLE_FACULTY')">
					<jsp:include page="../common/newSidebar.jsp" />
				</sec:authorize>
				<!-- SIDEBAR END -->
				<sec:authorize access="hasRole('ROLE_FACULTY')">
					<jsp:include page="../common/footer.jsp" />
				</sec:authorize>
				<sec:authorize access="hasRole('ROLE_ADMIN')">
					<jsp:include page="../common/newAdminFooter.jsp" />
				</sec:authorize>
				<script type="text/javascript">
						$(".showClass")
								.click(
										function() {
											console
													.log("called ........................................................000000.");
											//$(this).css('color', 'black');
											var testId = $(this).attr("id");

											var id = testId.substr(4, 5);
											console.log(id);
											$
													.ajax({
														type : 'GET',
														url : '${pageContext.request.contextPath}/showResultsToStudents?'
																+ 'id=' + id,
														success : function(data) {

															$(this)
																	.find(
																			'span')
																	.addClass(
																			"icon-success");
															var str1 = "hide";
															var str2 = str1
																	.concat(id);
															$('#' + str2)
																	.css(
																			{
																				'display' : 'block'
																			});
															//$('#' + str2).show();
															var str3 = "show";
															var str4 = str3
																	.concat(id);

															$('#' + str4)
																	.hide();

														}

													});

										});
						
$(".showReportsClass").click(function() {
	
	var testId = $(this).attr("id");
	var id = testId.substr(11);
	console.log(id);
	$.ajax({
			type : 'GET',
			url : '${pageContext.request.contextPath}/showReportsToStudents?'+ 'id=' + id,
			success : function(data) {
				$(this).find('span').addClass("icon-success");
				var str1 = "hideReports";
				var str2 = str1.concat(id);
				$('#' + str2).css({'display' : 'block'});
				var str3 = "showReports";
				var str4 = str3.concat(id);
				$('#' + str4).hide();
			}
		});
});
					</script>

				<script type="text/javascript">
						$(".hideClass")
								.click(
										function() {
											//alert('hide called')
											console
													.log("called ........................................................000000.");
											//$(this).css('color', 'black');
											var testId = $(this).attr("id");

											var id = testId.substr(4, 5);
											console.log(id);
											$
													.ajax({
														type : 'GET',
														url : '${pageContext.request.contextPath}/hideResultsToStudents?'
																+ 'id=' + id,
														success : function(data) {

															$(this)
																	.find(
																			'span')
																	.addClass(
																			"icon-success");
															var str1 = "show";
															var str2 = str1
																	.concat(id);
															$('#' + str2)
																	.show();
															var str3 = "hide";
															var str4 = str3
																	.concat(id);

															$('#' + str4)
																	.hide();

														}

													});

										});
$(".hideRportsClass").click(function() {
	var testId = $(this).attr("id");
	var id = testId.substr(11);
	console.log(id);
	$.ajax({
			type : 'GET',
			url : '${pageContext.request.contextPath}/hideReportsToStudents?'+ 'id=' + id,
			success : function(data) {
				$(this).find('span').addClass("icon-success");
				var str1 = "showReports";
				var str2 = str1.concat(id);
				$('#' + str2).show();
				var str3 = "hideReports";
				var str4 = str3.concat(id);
				$('#' + str4).hide();
			}
		});
});						
						
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
						window.location.href = '${pageContext.request.contextPath}/startStudentTestNew?id='+testId;
				 	}
				 	if(testType=='Subjective'){
				 		window.location.href = '${pageContext.request.contextPath}/startStudentTestUpdatedForSubjective?id='+testId;
				 	}
				 	if(testType=='Mix'){
				 		window.location.href = '${pageContext.request.contextPath}/startStudentTestUpdatedForMix?id='+testId;
				 	}
					//$("#testCompleted").text(addedStatus);
				}	
				else{
					alert("Password is incorrect");
				} 
				
			},
			error : function(result) {
				console.log('error');
			}
		});
		
	}

	
</script>

				<script type="text/javascript">
$('#tableTest').DataTable({
    "ordering": false,
    initComplete: function() {
        $(this.api().table().container()).find('input[type="search"]').parent().wrap('<form>').parent().attr('autocomplete','off').css('overflow','hidden').css('margin','auto');
    }    
}); 
</script>