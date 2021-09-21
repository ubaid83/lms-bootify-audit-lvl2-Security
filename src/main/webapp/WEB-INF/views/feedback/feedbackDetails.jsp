<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

<jsp:include page="../common/newDashboardHeader.jsp" />

<div class="d-flex adminPage dataTableBottom" id="adminPage">
	<jsp:include page="../common/newAdminLeftNavBar.jsp" />
	<jsp:include page="../common/rightSidebarAdmin.jsp" />


	<!-- DASHBOARD BODY STARTS HERE -->

	<div class="container-fluid m-0 p-0 dashboardWraper">

		<jsp:include page="../common/newAdminTopHeader.jsp" />

		<!-- SEMESTER CONTENT -->
		<div class="col-lg-12 col-md-12 col-sm-12 dashboardBody p-5">

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
						Feedback</li>
				</ol>
			</nav>
			<jsp:include page="../common/alert.jsp" />

			<!-- Input Form Panel -->
			<div class="card bg-white border">
				<div class="card-body">
					<h5 class="text-center border-bottom pb-2">Feedback Details</h5>

					<form:form action="addFeedback" method="post"
						modelAttribute="feedback">


						<div class="row">
							<div class="col-sm-4 column">
								<div class="form-group">
									<form:label path="feedbackName" for="feedbackName">
										<strong>Feedback Name:</strong>
									</form:label>
									${feedback.feedbackName}
								</div>
							</div>
						</div>
						<div class="row">

							<div class="col-sm-12 column">
								<div class="form-group">
									<sec:authorize access="hasRole('ROLE_ADMIN')">
										<c:url value="addFeedbackForm" var="editFeedbackUrl">
											<c:param name="id">${feedback.id}</c:param>
										</c:url>
										<c:choose>
										<c:when test="${feedback.feedbackType eq 'it-feedback'}">
											<c:url value="UploadFeedbackQuestionForm"
												var="uploadFeedbackUrl">
												<c:param name="id">${feedback.id}</c:param>
											</c:url>
										</c:when>
										<c:otherwise>
										<c:if test="${appName eq 'SBM-NM-M' || appName eq 'PDSEFBM' || feedback.feedbackType eq 'mid-term'}">
											<c:url value="UploadFeedbackQuestionForm"
												var="uploadFeedbackUrl">
												<c:param name="id">${feedback.id}</c:param>
											</c:url>
											<c:url value="addFeedbackQuestionForm"
												var="addFeedbackQuestion">
												<c:param name="feedbackId">${feedback.id}</c:param>
											</c:url>
										</c:if>
										</c:otherwise>
										</c:choose>
										<c:url value="UploadStudentsToDeallocateForm"
											var="uploadStudentsToDeallocateUrl">
											<c:param name="id">${feedback.id}</c:param>
										</c:url>
										<c:url value="removeFacultyFeedbackForm" var="removeFacultyFeedbackUrl">
										                <c:param name="id">${feedback.id}</c:param>
										</c:url>
																				
										<%-- <c:url value="addStudentFeedbackForm"
																	var="addStudentFeedbackFormUrl">
															</c:url> --%>

										<button id="submit" class="btn btn-large btn-dark mb-2"
											formaction="${editFeedbackUrl}">Edit</button>
										<button id="cancel" class="btn btn-danger mb-2"
											formaction="homepage" formnovalidate="formnovalidate">Cancel</button>
											
										<c:choose>
										<c:when test="${feedback.feedbackType eq 'it-feedback'}">
										<button class="btn btn-large btn-dark mb-2"
												formaction="${uploadFeedbackUrl}">Upload Feedback
												Questions</button>
										</c:when>
										<c:otherwise>
										<c:if test="${appName eq 'SBM-NM-M' || appName eq 'PDSEFBM' || feedback.feedbackType eq 'mid-term'}">
											<button class="btn btn-large btn-dark mb-2"
												formaction="${addFeedbackQuestion}">Configure
												Questions</button>
											<button class="btn btn-large btn-dark mb-2"
												formaction="${uploadFeedbackUrl}">Upload Feedback
												Questions</button>
										</c:if>
										</c:otherwise>
										</c:choose>
										
										

										<button class="btn btn-large btn-dark mb-2"
											formaction="${uploadStudentsToDeallocateUrl}">Upload
											Students To Deallocate</button>
										<button class="btn btn-large btn-dark mb-2" formaction="${removeFacultyFeedbackUrl}">De-alloacate Faculty</button>
										<%-- <button class="btn btn-large btn-primary"
																formaction="${addStudentFeedbackFormUrl}">Proceed
																	to allocate students</button> --%>

										<c:url value="downloadPendingStudentFeedbackList"
											var="downloadFeedbackUrl">
											<c:param name="feedbackId">${feedback.id }</c:param>
										</c:url>

										<button id="submit" class="btn btn-large btn-dark mb-2"
											formaction="${downloadFeedbackUrl}">  
											Feedback Report</button>
											
										<c:if test="${showProceed}">
											<div class="col-12 text-right">
												<a href="addStudentFeedbackForm"><i
													class="btn btn-large btn-primary">Proceed to allocate
														students</i></a>
											</div>
										</c:if>


									</sec:authorize>


									<sec:authorize access="hasRole('ROLE_STUDENT')">

										<c:url value="startStudentFeedback" var="giveFeedbackUrl">
											<c:param name="feedbackId">${feedback.id }</c:param>
										</c:url>


										<%-- <button id="submit" class="btn btn-large btn-success"
								formaction="${giveFeedbackUrl }">Give Feedback</button> --%>

										<c:if test="${studentFeedbackStatus.feedbackCompleted=='N'}">
											<button id="submit" class="btn btn-large btn-success mb-2"
												formaction="${giveFeedbackUrl}">Give Feedback</button>
										</c:if>

										<c:if test="${studentFeedbackStatus.feedbackCompleted=='Y'}">
											<button id="submit" class="btn btn-large btn-success mb-2"
												formaction="${giveFeedbackUrl}">view Feedback</button>
										</c:if>


									</sec:authorize>
								</div>
							</div>

						</div>





					</form:form>


					<!-- Results Panel -->
					<%-- <form:form action="removeStudentFeedback" method="post"
						modelAttribute="feedback"> --%>

						<h5 class="border-bottom pb-2 mt-5">Feedback Allocation
							Details</h5>
						<div class="table-responsive testAssignTable">
							<table class="table table-striped table-hover"
								id="studentFeedbackTable">
								<thead>
									<tr>
										<th>Sr. No.</th>
										<th><input name="select_all" value="1"
											id="example-select-all" type="checkbox" /></th>
										<th>Select to De-Allocate</th>
										<th>Feedback Status</th>

										<th>SAPID</th>
										<th>Student Name</th>
										<th>Course Name</th>
										<th>Acad Year</th>
										<th>Acad Session</th>
										<!-- <th>View Completed Feedback</th> -->
									</tr>
								</thead>
								<tfoot>
									<tr>
										<th></th>
										<th></th>
										<th></th>
										<th></th>
										<th>Program</th>
										<th>Student Name</th>
										<th>Course Name</th>
										<th>Acad Year</th>
										<th>Acad Session</th>

									</tr>
								</tfoot>
								<tbody>

									<c:forEach var="student" items="${studentFeedbackList}"
										varStatus="status">
										<tr>
											<td><c:out value="${status.count}" /></td>

											<td><sec:authorize access="hasRole('ROLE_ADMIN')">
													<c:if test="${student.feedbackCompleted == 'N'}">
														<!-- <input type="checkbox" id="check1" name="checkboxName"> -->
														<%-- <form:checkbox path="rollNo" value="${student.id}" /> --%>
														<input type="checkbox" class="delete_customer" value ="<c:out value="${student.id}" />"/>

													</c:if>


												</sec:authorize></td>
											<td><sec:authorize access="hasRole('ROLE_ADMIN')">
													<c:if test="${not empty student.id }">
						            									Feedback Allocated
						            									</c:if>
													<c:if test="${empty student.id }">
						            									Feedback Allocated
						            									</c:if>
												</sec:authorize></td>
											<td><c:if test="${student.feedbackCompleted =='Y' }">
												Completed
											</c:if> <c:if test="${student.feedbackCompleted =='N' }">
						            	 Pending
						            </c:if></td>
											<td><c:out value="${student.username}" /></td>
											<td><c:out value="${student.studentName}" /></td>

											<td><c:out value="${student.courseName}" /></td>

											<td><c:out value="${student.acadYear}" /></td>
											<td><c:out value="${student.acadMonth}" /></td>

											<%--<td><c:if test="${student.feedbackCompleted =='Y' }">
																	 <c:url value="viewStudentResponseFeedback"
																		var="detailsUrl">
																		<c:param name="feedbackId"
																			value="${student.feedbackId}" />
																		<c:param name="username" value="${student.username}" />
																	</c:url>
																	<a href="${detailsUrl}" title="Details"><i
																		class="fa fa-info-circle fa-lg"></i></a>&nbsp; 
																		Completed
											</c:if> <c:if test="${student.feedbackCompleted =='N' }">
						            	 Pending
						            </c:if></td>--%>
										</tr>
									</c:forEach>
									<div class="form-group">
										<sec:authorize access="hasRole('ROLE_ADMIN')">


											<%-- <c:url value="removeStudentFeedback"
												var="removeStudentFeedbackURL">


											</c:url> --%>

											<c:if test="${fn:length(studentFeedbackList) gt 0}">
												<!-- <button id="submit1" type="button"
																		class="btn btn-large btn-primary" onclick="clicked();">De-Allocate
																		Feedback</button> -->
												<!-- <button id="submit" class="btn btn-large btn-primary"
													onclick="return clicked();"
													formaction="removeStudentFeedback">De-Allocate
													Feedback</button> -->
												<button id="submit" class="btn btn-large btn-primary"
													onclick="return clicked();">De-Allocate
													Feedback</button>
											</c:if>

											<!-- <button id="submit" class="btn btn-large btn-primary"
															onclick="return clicked();"
															formaction="removeStudentFeedback">De-Allocate Feedback</button> -->
										</sec:authorize>

									</div>
								</tbody>
							</table>
						</div>
					<%-- </form:form> --%>

				</div>
			</div>


			<!-- /page content: END -->


		</div>

		<!-- SIDEBAR START -->

		<!-- SIDEBAR END -->
		<jsp:include page="../common/newAdminFooter.jsp" />



		<script>
			function clicked() {
				var name = document
						.querySelectorAll('input[type="checkbox"][class="delete_customer"]:checked').length
						+ " " + "Students selected";
				console.log("name" + name);

				var str = new Array();
				var feedbackId = $
				{
					feedback.id
				}
				;

				var checked = document
						.querySelectorAll('input[type="checkbox"][class="delete_customer"]:checked');

				for (var i = 0; i < checked.length; i++) {
					if (checked.length == 1) {
						str = checked[i].value;
					} else {
						if (i == checked.length - 1) {
							str += checked[i].value;
						} else {
							str += checked[i].value + ",";
						}

					}
				}
				console.log("str------>" + str);

				$
						.ajax({
							url : "${pageContext.request.contextPath}/removeStudentFeedback?ids="
									+ str + "&feedbackId=" + feedbackId,
							/* 	+ "&feedbackId="
								+ feedbackId, */

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
				//return confirm(name);
				return str;

			}

			window.checkAll = function checkAll() {
				$('input:checkbox[name=courseIds]').prop('checked', true);
				return false;
			}

			window.uncheckAll = function uncheckAll() {
				$('input:checkbox[name=courseIds]').prop('checked', false);
				return false;
			}
			var table = $('.table').DataTable();
			$('#example-select-all').on(
					'click',
					function() {
						// Check/uncheck all checkboxes in the table
						var rows = table.rows({
							'search' : 'applied'
						}).nodes();
						$('input[type="checkbox"]', rows).prop('checked',
								this.checked);
					});
		</script>