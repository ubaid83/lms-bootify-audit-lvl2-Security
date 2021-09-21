<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

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

					<nav aria-label="breadcrumb">
						<ol class="breadcrumb">
							<li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/homepage">Dashboard</a></li>
							<li class="breadcrumb-item" aria-current="page"><c:out
									value="${Program_Name}" /></li>
							<li class="breadcrumb-item active" aria-current="page"><sec:authorize
									access="hasRole('ROLE_STUDENT')">
									<c:out value="${AcadSession}" />
								</sec:authorize> Assignment</li>
						</ol>
					</nav>
					<jsp:include page="../common/alert.jsp" />
					
					<div class="card bg-white border">
					<div class="card-body">
						<div class="x_title">
							<h5 class='text-center border-bottom pb-2'>Assignment for ${assignment.course.courseName}</h5>
						</div>
							<form:form action="createAssignmentForm" id="editAssignment" class="font-weigth-bold"
								method="post" modelAttribute="assignment"
								enctype="multipart/form-data">
								<fieldset>
									<form:input path="courseId" type="hidden" />
									<form:input path="id" type="hidden" />
									<form:input path="acadMonth" type="hidden" />
									<form:input path="acadYear" type="hidden" />
									<div class="form-row">
										<div class="col-md-6 col-sm-6">
											<div class="form-group">
												<form:label path="assignmentName" for="assignmentName"><strong>Assignment Name:</strong></form:label>
												${assignment.assignmentName }
											</div>
										</div>

										<div class="col-md-6 col-sm-6">
											<div class="form-group">
												<form:label path="assignmentType" for="assignmentType"><strong>Assignment Type:</strong></form:label>
												${assignment.assignmentType }
											</div>
										</div>

										<div class="col-md-6 col-sm-6">
											<div class="form-group">
												<form:label path="startDate" for="startDate"><strong>Start Date:</strong></form:label>
												${assignment.startDate}
											</div>
										</div>
										<%-- 	<div class="col-md-4 col-sm-6 col-xs-12 column">
														<div class="form-group">
															<form:label path="dueDate" for="dueDate">Due Date:</form:label>
															${assignment.dueDate}
														</div>
													</div> --%>
										<div class="col-md-6 col-sm-6">
											<div class="form-group">
												<form:label path="endDate" for="endDate"><strong>End Date:</strong></form:label>
												${assignment.endDate}
											</div>
										</div>
										<div class="col-md-6 col-sm-6">
											<div class="form-group">
												<form:label path="maxScore" for="maxScore"><strong>Total Score:</strong></form:label>
												${assignment.maxScore}
											</div>
										</div>
									</div>
									<hr/>

									<div class="row">
										<div class="col-md-6 col-sm-6">
											<div class="form-group">
												<form:label path="allowAfterEndDate" for="allowAfterEndDate"><strong>Allow Submission after End date?:</strong></form:label>
												${assignment.allowAfterEndDate}
											</div>
										</div>
										<div class="col-md-6 col-sm-6">
											<div class="form-group">
												<form:label path="sendEmailAlert" for="sendEmailAlert"><strong>Send Email Alert for New Assignment?:</strong></form:label>
												${assignment.sendEmailAlert}
											</div>
										</div>
										<div class="col-md-6 col-sm-6">
											<div class="form-group">
												<form:label path="sendSmsAlert" for="sendSmsAlert"><strong>Send SMS Alert for New Assignment?:</strong></form:label>
												${assignment.sendSmsAlert}
											</div>
										</div>
										<div class="col-sm-6 col-md-4">
											<div class="form-group">
												<form:label path="sendEmailAlertToParents"
													for="sendEmailAlertToParents"><strong>Send Email Alert to Parents:</strong></form:label>
												${assignment.sendEmailAlertToParents}
											</div>
										</div>

										<div class="col-sm-6 col-md-4">
											<div class="form-group">
												<form:label path="sendSmsAlertToParents"
													for="sendSmsAlertToParents"><strong>Send SMS Alert to Parents:</strong></form:label>
												${assignment.sendSmsAlertToParents}
											</div>
										</div>
									</div>
									<hr/>
									<div class="row">
										<div class="col-md-6 col-sm-6">
											<div class="form-group">
												<form:label path="plagscanRequired" for="plagscanRequired"><strong>Is Plagiarism Required:</strong></form:label>
												${assignment.plagscanRequired}
											</div>
										</div>
										<div class="col-md-6 col-sm-6">
											<div class="form-group">
												<form:label path="runPlagiarism" for="runPlagiarism"><strong>Run plagiarism while :</strong></form:label>
												${assignment.runPlagiarism}
											</div>
										</div>

									</div>

									<div class="row">
										<div class="col-md-6 col-sm-6 col-xs-12 column">
											<div class="form-group">
												<form:label path="showResultsToStudents"
													for="showResultsToStudents"><strong>Show Results to Students immediately?:</strong></form:label>
												${assignment.showResultsToStudents}
											</div>
										</div>
										<div class="col-md-6 col-sm-6 col-xs-12 column">
											<div class="form-group">
												<label for="file"><strong>Assignment Question File:</strong></label>
												<c:if test="${assignment.showFileDownload eq 'true'}">

													<a class="text-primary font-italic" href="downloadFile?id=${assignment.id}">Download</a>
												</c:if>
												<c:if test="${assignment.showFileDownload eq 'false'}">No File</c:if>
											</div>
										</div>
									</div>

									<div class="row">

										<div class="col-md-6 col-sm-6 col-xs-12 column">
											<div class="form-group">
												<form:label path="facultyId" for="facultyId"><strong>Faculty:</strong></form:label>
												${assignment.facultyId}
											</div>
										</div>

										<div class="col-md-6 col-sm-6 col-xs-12 column">
											<div class="form-group">
												<form:label path="facultyId" for="facultyId"><strong>Is Submitted By One In a Group?:</strong></form:label>
												${assignment.submitByOneInGroup}
											</div>
										</div>
									
										<div class="col-12 cursor-pointer" data-toggle="collapse" data-target="#assignmentText"
													aria-expanded="true" aria-controls="assignmentText">
													<p class="text-center pt-1 bg-secondary text-white">
											<form:label class="cursor-pointer" path="assignmentText" for="assignmentText">
													Assignment Details: (Expand/Collapse)</strong>
											</form:label></p>
											<div id="assignmentText" class="collapse p-2">${assignment.assignmentText}</div>
										</div>
									</div>
									<hr/>
									<div class="row">
										<div class="m-auto">
    <div class="form-group">
        <sec:authorize access="hasRole('ROLE_FACULTY')">
            <button id="submit" class="btn btn-large btn-secondary" formaction="createAssignmentFromMenu?id=${assignment.id}">Edit
                Assignment</button>
        </sec:authorize>
        <sec:authorize access="hasRole('ROLE_FACULTY')">
            <button id="selectall" class="btn btn-large btn-secondary" formaction="saveStudentAssignmentAllocationForAllStudents">Allocate
                To All Students</button>
        </sec:authorize>
        <button id="cancel" class="btn btn-large btn-danger" formaction="homepage" formnovalidate="formnovalidate">Cancel</button>
    </div>
</div>


									</div>

								</fieldset>
							</form:form>
					</div>
					</div>

                    <div class="card bg-white">
                    <div class="card-body">
					<div class="col-12">
							<h5 class="text-center border-bottom pb-2">Select Students to Allocate Assignment | Assignment
								Allocated to : ${noOfStudentAllocated} Students</h5>
						<div class="x_itemCount" style="display: none;">
							<div class="image_not_found">
								<i class="fa fa-newspaper-o"></i>
								<p>
									<label class=""></label> ${fn:length(students)} Students
								</p>
							</div>
						</div>
						<div class="x_content">

							<form:form action="saveStudentAssignmentAllocation"
								id="saveStudentAssignmentAllocation" method="post"
								modelAttribute="assignment">
								<form:input path="id" type="hidden" />
								<form:input path="courseId" type="hidden" />
								<form:input path="facultyId" type="hidden" />

								<c:if test="${allCampuses.size() > 0}">
									<div class="row">
										<div class="col-md-4 col-sm-12 mt-3">
											<div class="form-group">
												<form:label path="campusId" for="campusId" class="d-none">Select Campus</form:label>

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

								<div class="table-responsive testAssignTable">
									<table class="table table-striped table-hover" id="example">
										<thead>
											<tr>
												<th>Sr. No.</th>
												<th><input name="select_all" value="1"
													id="example-select-all" type="checkbox" /></th>

												<th>Student Name <i class="fa fa-sort"
													aria-hidden="true" style="cursor: pointer"></i></th>
												<th>Roll No. <i class="fa fa-sort" aria-hidden="true"
													style="cursor: pointer"></i></th>
												<th>SAPID <i class="fa fa-sort" aria-hidden="true"
													style="cursor: pointer"></i></th>
												<th>Campus<i class="fa fa-sort" aria-hidden="true"
													style="cursor: pointer"></i></th>
												<th>Program</th>
											</tr>
										</thead>
										<tfoot>
											<tr>
												<th></th>
												<th></th>

												<th>Student Name</th>
												<th>Roll No.</th>
												<th>SAPID</th>
												<th>Campus</th>
												<th>Program</th>
											</tr>
										</tfoot>
										<tbody>
											<c:forEach var="student" items="${students}"
												varStatus="status">
												<tr>
													<td><c:out value="${status.count}" /></td>
													<td><sec:authorize access="hasRole('ROLE_FACULTY')">
															<c:if test="${empty student.id }">
																<form:checkbox path="students"
																	value="${student.username}" />
															</c:if>

															<c:if test="${not empty student.id }">
													            	Assignment Allocated
													            </c:if>
														</sec:authorize> <sec:authorize access="hasRole('ROLE_DEAN')">
															<c:if test="${ empty student.id }">
																<form:checkbox path="students"
																	value="${student.username}" />
															</c:if>

															<c:if test="${not empty student.id }">
													            	Assignment Allocated
													            </c:if>
														</sec:authorize></td>

													<td><c:out
															value="${student.firstname} ${student.lastname}" /></td>
													<td><c:out value="${student.rollNo}" /></td>
													<td><c:out value="${student.username}" /></td>
													<td><c:out value="${student.campusName}" /></td>
													<td><c:out value="${student.programName}" /></td>
												</tr>
											</c:forEach>
										</tbody>
									</table>
								</div>

								<div class="mt-5 text-center">
									<div class="form-group">
										<sec:authorize access="hasRole('ROLE_FACULTY')">
											<button id="submit" class="btn btn-large btn-primary"
												onclick="return clicked();"
												formaction="saveStudentAssignmentAllocation">Allocate
												Assignment</button>
										</sec:authorize>
										<button id="cancel" class="btn btn-large btn-danger"
											formaction="homepage" formnovalidate="formnovalidate">Cancel</button>
									</div>
								</div>
							</form:form>

						</div>
					</div>
					</div>
					</div>
					<!-- /page content -->



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
									window.location = '${pageContext.request.contextPath}/viewAssignment?id='
											+ id + '&campusId=' + selectedValue;
									+encodeURIComponent(selectedValue);
									return false;
								});
				//var email = document.getElementById('name').value;
			</script>
			
			<script type="text/javascript">
				$(document).ready(function() {
					var clicked = false;
					$('#submit').click(function(e) {
						console.log('inside click event');
						if (clicked === false) {
							console.log('inside click false');
							clicked = true;
						} else {
							console.log('inside click true');
							e.preventDefault();
						}
					});
					var clickedAll = false;
					$('#selectall').click(function(e) {
						console.log('inside click event');
						if (clickedAll === false) {
							console.log('inside click false');
							clickedAll = true;
						} else {
							console.log('inside click true');
							e.preventDefault();
						}
					});
				});
			</script>
