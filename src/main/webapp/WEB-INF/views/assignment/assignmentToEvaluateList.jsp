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
<jsp:include page="../common/newLeftNavBarFaculty.jsp"/>
<jsp:include page="../common/newLeftSidebarFaculty.jsp"/>
<jsp:include page="../common/rightSidebarFaculty.jsp"/>

<!-- DASHBOARD BODY STARTS HERE -->

<div class="container-fluid m-0 p-0 dashboardWraper">

	<jsp:include page="../common/newTopHeaderFaculty.jsp" />
     
     <div class="container mt-5">
                <div class="row">
            <!-- SEMESTER CONTENT -->    
                    <div class="col-lg-9 col-md-9 col-sm-12 dashboardBody ml-auto mr-auto">
                        
          <!-- page content: START -->
          
                        <nav aria-label="breadcrumb">
                            <ol class="breadcrumb">
                                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/homepage">Dashboard</a></li>
                                <li class="breadcrumb-item" aria-current="page"><c:out value="${Program_Name}" /></li>
                                <sec:authorize access="hasRole('ROLE_STUDENT')">
								<c:out value="${AcadSession}" />
							</sec:authorize>
                                <li class="breadcrumb-item active" aria-current="page"> Evaluate Assignment with
							Advance Search</li>
                            </ol>
                        </nav>
                        
						<jsp:include page="../common/alert.jsp" />
						
						
						<!-- Input Form Panel -->
						<div class="card bg-white border">
						<div class="card-body">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">

										<h5 class="text-center border-bottom pb-2">Search Assignment To Evaluate</h5>
								

									<div class="x_content">
										<form:form action="searchAssignmentToEvaluate" method="post"
											modelAttribute="assignment">
											<fieldset>


												<div class="row">
													<div class="col-sm-4 col-md-4 col-xs-12 column">
														<div class="form-group">
															<form:label  class="textCourse" path="courseId" for="courseId"><strong>Course</strong></form:label>
															<form:select id="courseId" path="courseId"
																class="form-control">
																<form:option value="">Select Course</form:option>
																<c:forEach var="course" items="${allCourses}"
																	varStatus="status">
																	<form:option value="${course.id}">${course.courseName}</form:option>
																</c:forEach>
															</form:select>
														</div>
													</div>


													<div class="col-sm-4 col-md-4 col-xs-12 column">
														<div class="form-group">
															<form:label class="textCourse" path="evaluationStatus"
																for="evaluationStatus"><strong>Evaluation Status</strong></form:label>
															<form:select id="evaluationStatus"
																path="evaluationStatus" class="form-control">
																<form:option value="">Select Evaluation Status</form:option>
																<form:option value="Y">Evaluated</form:option>
																<form:option value="N">Not Evaluated</form:option>
															</form:select>
														</div>
													</div>

													<div class="col-sm-4 col-md-4 col-xs-12 column">
														<div class="form-group">
															<form:label class="textCourse" path="submissionStatus"
																for="submissionStatus"><strong>Submission Status</strong></form:label>
															<form:select id="submissionStatus"
																path="submissionStatus" class="form-control">
																<form:option value="">Select Submission Status</form:option>
																<form:option value="Y">Submitted</form:option>
																<form:option value="N">Not Submitted</form:option>
																<form:option value="">All</form:option>
															</form:select>
														</div>
													</div>

													<div class="col-sm-4 col-md-4 col-xs-12 column">
														<div class="form-group">
															<form:label class="textCourse" path="lowScoreReason" for="lowScoreReason">Low Score Reason</form:label>
															<form:select id="lowScoreReason" path="lowScoreReason"
																class="form-control">
																<form:option value=""><strong>Select Low Score Reason</strong></form:option>
																<form:option value="Copy Case-Internet">Copy Case-Internet</form:option>
																<form:option value="Copy Case-Other Student">Copy Case-Other Student</form:option>
																<form:option value="Wrong Answers">Wrong Answers</form:option>
																<form:option value="Other subject Assignment">Other subject Assignment</form:option>
																<form:option value="Scanned/Handwritten Assignment">Scanned/Handwritten Assignment</form:option>
																<form:option value="Only Questions written">Only Questions written</form:option>
																<form:option value="Question Paper Uploaded">Question Paper Uploaded</form:option>
																<form:option value="Blank Assignment">Blank Assignment</form:option>
																<form:option value="Corrupt file uploaded">Corrupt file uploaded</form:option>
															</form:select>
														</div>
													</div>

													<div class="col-sm-4 col-md-4 col-xs-12 column">
														<div class="form-group">
															<form:label class="textCourse" path="username" for="username"><strong>Student User Name</strong></form:label>
															<form:input path="username" type="text"
																class="form-control" />
														</div>
													</div>

													<div class="col-sm-12 column">
														<div class="form-group">
															<button id="submit" name="submit"
																class="btn btn-large btn-primary">Search</button>
															<button id="reset" type="reset" class="btn btn-danger">Reset</button>

															<!-- <button id="cancel" name="cancel" class="btn btn-danger"
									formnovalidate="formnovalidate">Cancel</button>-->
														</div>
													</div>
												</div>
											</fieldset>
										</form:form>
									</div>
								</div>
							</div>
						</div>
						</div>

						<!-- Results Panel -->

						<c:choose>
							<c:when test="${assignmentsList.size() > 0}">
								<div class="card bg-white border">
								<div class="card-body">								
									<div class="col-xs-12 col-sm-12">
										<div class="x_panel">
											<div class="text-center border-bottom pb-2">
												<h5>
													Assignment Submissions<font size="2px"> |
														${assignmentsList.size()} Records Found &nbsp; </font>
												</h5>
											</div>
											
											<div class="row mt-3">
											<div class="col-6">
													<div class="form-group">
														<a href="gradeCenterForm"><i
															class="btn btn-large btn-primary w-100">Go to Grade Center</i></a>
													</div>
												</div>
											<div class="col-6">
											   <a href="sendReminderToAllStudents?courseId=${courseId}">
																	<button id="selectall" name="submit"
																		class="btn btn-large btn-primary w-100">Reminder To
																		All</button>
																</a>
											</div>
											
											</div>
											
											
											
											
											<div class="x_content">
												<div class="table-responsive testAssignTable">
													<table class="table table-striped table-hover"
														style="font-size: 12px">
														<thead>
															<tr>
																<th>Sr. No.</th>
																<th>Student User Name</th>
																<th>Roll No.</th>
																<th>Student Name</th>
																<th>Evaluated By</th>
																<th>Assignment Name</th>
																<th>Assignment File</th>
																<!-- <th>Assignment Details</th> -->
																<th>Preview</th>
																<th>Student Submitted File</th>
																<th>Evaluation Status</th>
																<th>Submission Status</th>


																<th>Save Score</th>
																<th>Save Remarks</th>
																<th>Save Low Score Reason</th>
																<c:if test="${assignment.submissionStatus eq 'N' }">
																	<th>Actions</th>
																</c:if>
																
																<!-- 	<button id="selectall" name="submit"
																class="btn btn-large btn-primary" href="">Search</button> -->
																</th>
															</tr>
														</thead>
														<tbody>
															<%
																Date now = Utils.getInIST();

																		String year = "" + (now.getYear() + 1900);
																		String month = "" + (now.getMonth() + 1);
																		if (month.length() == 1) {
																			month = "0" + month;
																		}
																		String day = "" + now.getDate();

																		if (day.length() == 1) {
																			day = "0" + day;
																		}
																		String hour = "" + now.getHours();
																		if (hour.length() == 1) {
																			hour = "0" + hour;
																		}
																		String minute = "" + now.getMinutes();
																		if (minute.length() == 1) {
																			minute = "0" + minute;
																		}
																		String second = "" + now.getSeconds();
																		if (second.length() == 1) {
																			second = "0" + second;
																		}
																		String newDate = year + "-" + month + "-" + day + " "
																				+ hour + ":" + minute + ":" + second;
																		System.out.println("Year " + year);
																		System.out.println("New Date is " + newDate);
															%>

															<c:set var="today">
																<%=newDate%>
															</c:set>

															<c:forEach var="assignment" items="${assignmentsList}"
																varStatus="status">
																<tr>
																	<td><c:out value="${status.count}" /></td>
																	<td><c:out value="${assignment.username}" /></td>
																	<td><c:out value="${assignment.rollNo}" /></td>
																	<td><c:out
																			value="${assignment.firstname} ${assignment.lastname}" /></td>
																	<td><c:out value="${assignment.evaluatedBy}" /></td>
																	<td><c:out value="${assignment.assignmentName}" /></td>
																	<td><c:if
																			test="${assignment.showFileDownload eq 'true'}">
																			<a href="downloadFile?id=${assignment.assignmentId}">Download</a>
																		</c:if> <c:if
																			test="${assignment.showFileDownload eq 'false'}">No File</c:if>
																	</td>
																	<%-- 	<td><a href="#"
																		onClick="showModal('${assignment.assignmentId}', '${assignment.assignmentName}');">Assignment
																			Details</a></td> --%>
																	<td><a href="sendFile?id=${assignment.id}"
																		target="_blank" onclick="return preview(this.href); ">Preview</a></td>
																	<td><c:if
																			test="${assignment.showStudentFileDownload eq 'true'}">
																			<a href="downloadFile?saId=${assignment.id }">Download
																				Answer File</a>
																		</c:if> <c:if
																			test="${assignment.showStudentFileDownload eq 'false'}">No File</c:if>

																	</td>
																	<td><c:out value="${assignment.evaluationStatus}" /></td>
																	<td><c:out value="${assignment.submissionStatus}" /></td>
																	<%-- <td><a href="#" class="editable" id="score"
												data-type="text" data-pk="${assignment.id}"
												data-url="saveAssignmentScore" data-title="Enter Score">${assignment.score}</a></td> --%>
																	<td><a href="#" class="editable" id="score"
																		data-type="text" data-pk="${assignment.id}"
																		data-url="saveAssignmentScore"
																		data-title="Enter Score">${assignment.score}</a></td>

																	<%-- 	<td><a href="#" class="editable" id="remarks"
																		data-type="textarea" data-pk="${assignment.id}"
																		data-url="saveAssignmentRemarks"
																		data-title="Enter Remarks">${assignment.remarks}</a></td> --%>
																	<%-- 	<td>
																		<div class="dropdown">
																			<a href="#" class="dropdown-toggle"
																				data-ytoggle="dropdown" role="button"
																				aria-expanded="false">${assignment.remarks}</a>
																			<div class="saved_score dropdown-menu">
																				<input type="text"> <a href="#" id="remarks"
																					data-pk="${assignment.id}"
																					data-url="saveAssignmentRemarks"
																					data-title="Enter Remarks"><i
																					class="check_ellipse fa fa-check bg-green"></i></a> <a
																					href="#"><i
																					class="check_ellipse fa fa-close bg-red"></i></a>
																			</div>
																		</div>
																	</td>

																	<td><a href="#" id="remarks" data-type="select"
																		data-pk="${assignment.id}"
																		data-source="[{value: 'Copy Case-Internet', text: 'Copy Case-Internet'},{value: 'Copy Case-Other Student', text: 'Copy Case-Other Student'},{value: 'Wrong Answers', text: 'Wrong Answers'},{value: 'Other subject Assignment', text: 'Other subject Assignment'},{value: 'Scanned/Handwritten Assignment', text: 'Scanned/Handwritten Assignment'},{value: 'Only Questions written', text: 'Only Questions written'},{value: 'Question Paper Uploaded', text: 'Question Paper Uploaded'},{value: 'Blank Assignment', text: 'Blank Assignment'},{value: 'Corrupt file uploaded', text: 'Corrupt file uploaded'}]"
																		data-url="saveLowScoreReason"
																		data-title="Select Low Marks Reason">${assignment.lowScoreReason}</a>
																	</td> --%>
																	<td><a href="#" class="editable" id="remarks"
																		data-type="textarea" data-pk="${assignment.id}"
																		data-url="saveAssignmentRemarks"
																		data-title="Enter Remarks">${assignment.remarks}</a></td>

																	<td><a href="#" class="editable" id="remarks"
																		data-type="select" data-pk="${assignment.id}"
																		data-source="[{value: 'Copy Case-Internet', text: 'Copy Case-Internet'},{value: 'Copy Case-Other Student', text: 'Copy Case-Other Student'},{value: 'Wrong Answers', text: 'Wrong Answers'},{value: 'Other subject Assignment', text: 'Other subject Assignment'},{value: 'Scanned/Handwritten Assignment', text: 'Scanned/Handwritten Assignment'},{value: 'Only Questions written', text: 'Only Questions written'},{value: 'Question Paper Uploaded', text: 'Question Paper Uploaded'},{value: 'Blank Assignment', text: 'Blank Assignment'},{value: 'Corrupt file uploaded', text: 'Corrupt file uploaded'}]"
																		data-url="saveLowScoreReason"
																		data-title="Select Low Marks Reason">${assignment.lowScoreReason}</a>
																	</td>

																	<td id="first_td"><c:if
																			test="${assignment.submissionStatus eq 'N' && assignment.endDate  ge  today}">


																			<c:url value="sendReminder" var="reminderUrl">
																				<c:param name="st_username"
																					value="${assignment.username}" />
																				<c:param name="assgn_name"
																					value="${assignment.assignmentName} " />
																			</c:url>
																			<a href="${reminderUrl}"
																				id="reminderId${status.count}" title="Reminder"><i
																				class="btn btn-large btn-primary">Reminder</i></a>&nbsp;
																				</c:if></td>

																	<%-- <td><c:url value="viewAssignmentSubmission"
																			var="detailsUrl">
																			<c:param name="id" value="${assignment.id}" />
																			<c:param name="username"
																				value="${assignment.username}" />
																			<c:param name="assignmentId"
																				value="${assignment.assignmentId}" />
																		</c:url> <a href="${detailsUrl}" title="Details"><i
																			class="fa fa-info-circle fa-lg"></i></a>&nbsp;</td> --%>


																	<%-- <td><c:if
																			test="${assignment.submissionStatus eq 'N' }">

																			<c:url value="sendReminder" var="reminderUrl">
																				<c:param name="st_username"
																					value="${assignment.username}" />
																				<c:param name="assgn_name"
																					value="${assignment.assignmentName} " />
																			</c:url>
																			<a href="${reminderUrl}" id="reminderId"
																				title="Reminder"><i
																				class="btn btn-large btn-primary">Reminder</i></a>&nbsp;</c:if></td>
 --%>





																</tr>
															</c:forEach>

														</tbody>
													</table>
												</div>
											</div>
										</div>
									</div>
								</div>
								</div>
							</c:when>
						</c:choose>

					<%-- 	<jsp:include page="../common/paginate.jsp">
							<jsp:param name="baseUrl" value="searchFacultyGroups" />
						</jsp:include> --%>

			<!-- /page content: END -->
                   
                    </div>
			
			<!-- SIDEBAR START -->
	         <jsp:include page="../common/newSidebar.jsp" />
	        <!-- SIDEBAR END -->
	<jsp:include page="../common/footer.jsp"/>

	<script>
		function preview(path) {
			var iframe = document.getElementById("iframe");
			if (iframe) {
			} else {
				iframe = document.createElement('iframe');
			}

			iframe.src = path;
			document.body.appendChild(iframe);

		}
	</script>
	<script>
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
		});
	
	</script>













