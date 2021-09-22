<%@page import="java.util.*"%>
<%@page import="com.spts.lms.beans.dashboard.DashBoard"%>
<%-- <jsp:include page="../common/DashboardHeader.jsp" /> --%>
<%-- <jsp:include page="../common/NewHeader1.jsp" /> --%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>


<%
	List<DashBoard> courseDetailList = (List<DashBoard>) session
      .getAttribute("courseDetailList");
      System.out.println("SIZE :"+courseDetailList.size());
      Map<String,List<DashBoard>> dashboardListSemesterWise = (Map<String,List<DashBoard>>)
      session.getAttribute("sessionWiseCourseListMap");
%>
<%
	boolean isEdit = "true".equals((String) request
      .getAttribute("edit"));
%>
<jsp:include page="../common/newDashboardHeader.jsp" />

<script type="text/javascript">
	var headerCourseId = null;
	function theFunction(obj) {

		return true;
	}
</script>
<div class="d-flex" id="facultyDashboardPage">
	<jsp:include page="../common/newLeftNavBarFaculty.jsp" />
	<jsp:include page="../common/newLeftSidebarFaculty.jsp" />
	<jsp:include page="../common/rightSidebarFaculty.jsp" />

	<!-- DASHBOARD BODY STARTS HERE -->

	<div class="container-fluid m-0 p-0 dashboardWraper">

		<jsp:include page="../common/newTopHeaderFaculty.jsp" />

		<div class="container mt-5">
			<div class="row">
				<div class="container mt-5">
					<div class="row">
						<div
							class="col-lg-9 col-md-9 col-sm-12 dashboardBody ml-auto mr-auto">
							<nav aria-label="breadcrumb">
								<ol class="breadcrumb">
									<li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/homepage">Dashboard</a></li>
									<li class="breadcrumb-item active" aria-current="page">
										<%
											if (isEdit) {
										%> Update Assignment<%
											}else{
										%> Create Assignment <%
											}
										%>
									</li>
								</ol>
							</nav>
							<!-- 14-06-2021 -->
							<jsp:include page="../common/alert.jsp" />
							<div class="card bg-white">
								<div class="card-body">

									<form:form action="createAssignment" id="createAssignment"
										method="post" modelAttribute="assignment"
										enctype="multipart/form-data">
										<form:input type="hidden" path="isCheckSumReq" value="N"/>
										<%-- <form:input path="courseId" type="hidden" /> --%>
										<%
											if (isEdit) {
										%>
										<form:input type="hidden" path="id" />
										<%
											if (isEdit) {
										%>
										<form:input type="hidden" path="id" />
										<form:input type="hidden" path="acadYear" />
										<form:input type="hidden" path="acadMonth" />
										<input type="hidden" id="plagscanRequired"
											value="${assignment.plagscanRequired}" />
										<%
											}
										%>
										<%
											}
										%>
										<div class="form-row">
											<%
												if (isEdit) {
											%>
											<div class="col-12 text-center">
												<h3>Update Assignment</h3>
											</div>
											<%
												}else{
											%>
											<div class="col-12 text-center">
												<h3>Add Assignment</h3>
											</div>
											<%
												}
											%>
											<!--FORM ITEMS STARTS HERE-->


											<div
												class="col-lg-4 col-md-4 col-sm-12 mt-3 position-relative">
												<label class="textStrong">Name <span class="text-danger">*</span></label>											
												<form:input id="assignmentName" path="assignmentName"
													type="text" placeholder="Assignment Name"
													class="form-control" required="required" />
											</div>
											<%-- <div
                                                                        class="col-lg-4 col-md-4 col-sm-12 mt-3 position-relative">
                                                                        <label class="sr-only">Select Semester</label> <label
                                                                              class="f-13 text-danger req">*</label> <select
                                                                              class="form-control" id="assignSem1">
                                                                              <c:forEach items="${sessionWiseCourseList}" var="sList"
                                                                                    varStatus="count">
                                                                                    <c:if test="${sList.key eq course.acadSession}">
                                                                                          <option value="${sList.key}" selected>${sList.key}</option>
                                                                                    </c:if>
                                                                                    <c:if test="${sList.key ne course.acadSession }">
                                                                                          <option value="${sList.key}">${sList.key}</option>
                                                                                    </c:if>
                                                                              </c:forEach>
                                                                        </select>
                                                                  </div> --%>
											<div
												class="col-lg-4 col-md-4 col-sm-12 mt-3 position-relative">
												<label class="textStrong">Select Course <span class="text-danger">*</span></label>
												<form:select id="idForCourse" path="courseId"
													class="form-control facultyparameter" required="required">
													<c:forEach var='cList' items='${allCourses}'>
														<c:if test="${courseId eq cList.id}">
															<option value="${cList.id}" selected>${cList.courseName}</option>
														</c:if>
														<c:if test="${courseId ne cList.id }">
															<option value="${cList.id}">${cList.courseName}</option>
														</c:if>
													</c:forEach>
												</form:select>
											</div>





											<%--       <form:select id="idForCourse" path="courseId"
                                                                                                required="required" class="form-control">
                                                                                                <form:option value="">Select Course</form:option>
                                                                                                <c:forEach var="course" items="${allCourses}"
                                                                                                      varStatus="status">
                                                                                                      <form:option value="${course.id}">${course.courseName}</form:option>
                                                                                                </c:forEach>
                                                                                          </form:select> --%>


											<div
												class="col-lg-4 col-md-4 col-sm-12 mt-3 position-relative">
												<label class="textStrong">Assignment Type <span class="text-danger">*</span></label>
												<form:select id="showStatusDropDown" path="assignmentType"
													required="required" class="form-control">
													<form:option value="" disabled="true" selected="true">Select </form:option>
													<form:option value="Presentation">Presentation</form:option>
													<form:option value="WrittenAssignment">Written Assignment</form:option>
													<form:option value="Viva">Viva</form:option>
													<form:option value="ReportWriting">Report Writing</form:option>
												</form:select>
											</div>

											<%-- <div
                                                                        class="col-lg-4 col-md-4 col-sm-12 mt-3 position-relative">
                                                                        <label class="f-13 text-danger req">*</label> <label
                                                                              class="sr-only">Faculty</label>
                                                                        <form:select id="facultyid" path="facultyId"
                                                                              class="form-control">
                                                                              <form:option value="">Select Faculty</form:option>
                                                                              <  <c:forEach var="course" items="${allCourses}"
                                                            varStatus="status">
                                                            <form:option value="${course.id}">${course.courseName}</form:option>
                                                      </c:forEach>
                                                                        </form:select>
                                                                  </div> --%>

											<%
												if(isEdit) {
											%>

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

											<%
												}else{
											%>
											<div
												class="col-lg-8 col-md-8 col-sm-12 mt-3 position-relative">
												<label class="textStrong">Select Date Range <span class="text-danger">*</span></label>
												<div class="input-group">	
													<form:input type="hidden" id="testStartDate"
														path="startDate" />
													<form:input type="hidden" id="testEndDate" path="endDate" />
													<input id="startDate" type="text"
														placeholder="Start Date - End Date" class="form-control"
														required="required" />
													<div class="input-group-append">
														<button class="btn btn-outline-secondary" type="button"
															id="testDateRangeBtn">
															<i class="fas fa-calendar"></i>
														</button>
													</div>
												</div>
											</div>
											<%
												}
											%>

											<div
												class="col-lg-4 col-md-4 col-sm-12 mt-3 position-relative">
												<label class="textStrong">Total Score <span class="text-danger">*</span></label>
												<form:input id="maxScore" path="maxScore" min="1"
													type="number" placeholder="Total Score"
													value="${assignment.maxScore}" class="form-control"
													required="required" />
											</div>



											<div
												class="col-lg-4 col-md-4 col-sm-12 mt-3 position-relative">
												<div class="form-group">
													<label class="textStrong" for="file"> <%
 	if (isEdit) {
 %> Select if you wish to override earlier file <%
 	} else {
 %> Assignment Question File <%
 	}
 %>
													</label> <input id="file" name="file" type="file"
														class="form-control" multiple="multiple" />
												</div>
												<div id=fileSize></div>
											</div>
										</div>


										<hr />

										<%-- 
										<div>									
											class="col-lg-12 col-md-12 col-sm-12 mt-3 position-relative">
											<div class="form-group">
												<label for="plagscanRequired">Is Plagcheck
													Requiredcase?</label>
												<div class="pretty p-switch p-fill p-toggle">

													<form:checkbox value="No" id="checkPlagValue"
														path="plagscanRequired" />
													<div class="state p-success p-on">
														<label>Yes</label>
													</div>
													<div class="state p-danger p-off">
														<label>No</label>
													</div>

												</div>
											</div>



											<div id="plagContent" class="d-none row">
												<div
													class="col-lg-6 col-md-6 col-sm-12 mt-3 position-relative"
													id="runPlagiarismDiv">
													<div class="form-group">
														<label for="runPlagiarism">Run Plagiarism while?</label>
														<div class="pretty p-switch p-fill p-toggle">

															<form:checkbox value="Manual" path="runPlagiarism"
																id="runPlagiarism" />

															<div class="state p-success p-on">
																<label>Submission</label>
															</div>
															<div class="state p-danger p-off">
																<label>Manual</label>
															</div>

														</div>
													</div>

												</div>

												<div
													class="col-lg-6 col-md-6 col-sm-12 mt-3 position-relative"
													id="thresholdDiv">
													<label class="f-13 text-danger req">*</label>
													<form:input id="threshold" path="threshold" type="text"
														placeholder="Threshold Value"
														value="${assignment.threshold}" class="form-control" />
												</div>
											</div>
										</div> --%>
										<!-- START -->
										<div class="row">
											<div
												class="col-lg-4 col-md-4 col-sm-12 mt-3 position-relative">
												<div class="slider round">
													<form:label class="textStrong" path="plagscanRequired" for="plagscanRequired">Is Plagiarism Check Required? <span
															style="color: red">*</span>
													</form:label>
													<br>
													<form:radiobutton name="plagscanRequired" id="Yes"
														value="Yes" path="plagscanRequired" disabled="true"/>
													Yes<br>
													<form:radiobutton name="plagscanRequired" id="No"
														value="No" path="plagscanRequired" checked="true"/>
													No<br>


													<%-- 	<form:checkbox path="plagscanRequired" onclick="clicked();"
																class="form-control" value="Y" data-toggle="toggle"
																data-on="Yes" data-off="No" data-onstyle="success"
																data-offstyle="danger" data-style="ios" data-size="mini" /> --%>
												</div>
											</div>



											<%-- <div
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
											</div> --%>
										</div>

										<!--END  -->

										<div class="form-row">
											<table
												class="table table-bordered table-striped mt-5 font-weight-bold">
												<tbody>
													<tr>
														<td>Allow Submission after End date?</td>
														<td>
														
														<%if(isEdit) { %>
															<div class="pretty p-switch p-fill p-toggle">
																<form:checkbox path="allowAfterEndDate"
																	id="subAfterEndDate" value="${assignment.allowAfterEndDate}" />
																<div class="state p-success p-on">
																	<label>Yes</label>
																</div>
																<div class="state p-danger p-off">
																	<label>No</label>
																</div>

															</div>
															<% } else { %>
															<div class="pretty p-switch p-fill p-toggle">
																<form:checkbox path="allowAfterEndDate"
																	id="subAfterEndDate" value="N" />
																<div class="state p-success p-on">
																	<label>Yes</label>
																</div>
																<div class="state p-danger p-off">
																	<label>No</label>
																</div>

															</div>
															<% } %>
														</td>
													</tr>

													<%-- <tr>
														<td>Submit Assignment By One in the Group?</td>
														<td>
															<%if(isEdit) { %>
															<div class="pretty p-switch p-fill p-toggle">
																<form:checkbox path="submitByOneInGroup"
																	id="submitByOneInGroup" value="${assignment.submitByOneInGroup}" />
																<div class="state p-success p-on">
																	<label>Yes</label>
																</div>
																<div class="state p-danger p-off">
																	<label>No</label>
																</div>

															</div>
															<% } else { %>
															<div class="pretty p-switch p-fill p-toggle">
																<form:checkbox path="submitByOneInGroup"
																	id="submitByOneInGroup" value="N" />
																<div class="state p-success p-on">
																	<label>Yes</label>
																</div>
																<div class="state p-danger p-off">
																	<label>No</label>
																</div>

															</div>
															<% } %>
														</td>
													</tr> --%>

													<tr>
														<td>Show Results to Students immediately?</td>
														<td>
														<%if(isEdit) { %>
															<div class="pretty p-switch p-fill p-toggle">
																<form:checkbox value="${assignment.showResultsToStudents}" id="showResult"
																	path="showResultsToStudents" />
																<div class="state p-success p-on">
																	<label>Yes</label>
																</div>
																<div class="state p-danger p-off">
																	<label>No</label>
																</div>

															</div>
															<% } else { %>
															<div class="pretty p-switch p-fill p-toggle">
																<form:checkbox value="N" id="showResult"
																	path="showResultsToStudents" />
																<div class="state p-success p-on">
																	<label>Yes</label>
																</div>
																<div class="state p-danger p-off">
																	<label>No</label>
																</div>

															</div>
															<% } %>
														</td>
													</tr>


													<%-- <tr>
                                                                                    <td>Is Plagiarism Check Required?</td>
                                                                                    <td>
                                                                                          <div class="pretty p-switch p-fill p-toggle">
                                                                                                <form:checkbox value="No" id="plagscanRequired"
                                                                                                      path="plagscanRequired" />
                                                                                                <div class="state p-success p-on">
                                                                                                      <label>Yes</label>
                                                                                                </div>
                                                                                                <div class="state p-danger p-off">
                                                                                                      <label>No</label>
                                                                                                </div>

                                                                                          </div>
                                                                                    </td>
                                                                              </tr> --%>



													<%-- <tr>
                                                                        <td>Run Plagiarism while</td>
                                                                        <td>
                                                                              <div class="pretty p-switch p-fill p-toggle">
                                                                                    <form:checkbox value="No" id="runPlagiarism"
                                                                                          path="runPlagiarism" required="required" />
                                                                                    <div class="state p-success p-on">
                                                                                          <label>Submission</label>
                                                                                    </div>
                                                                                    <div class="state p-danger p-off">
                                                                                          <label>Manual</label>
                                                                                    </div>

                                                                              </div>
                                                                        </td>
                                                                  </tr> --%>



													<tr>
														<td>Evaluation Right Granted to Peer Faculties?</td>
														<td>
														<% if(isEdit) { %>
															<div class="pretty p-switch p-fill p-toggle">
																<form:checkbox value="${assignment.rightGrant}" id="rightGrant"
																	path="rightGrant" />
																<div class="state p-success p-on">
																	<label>Yes</label>
																</div>
																<div class="state p-danger p-off">
																	<label>No</label>
																</div>

															</div>
															<% } else { %>
															<div class="pretty p-switch p-fill p-toggle">
																<form:checkbox value="N" id="rightGrant"
																	path="rightGrant" />
																<div class="state p-success p-on">
																	<label>Yes</label>
																</div>
																<div class="state p-danger p-off">
																	<label>No</label>
																</div>

															</div>
															<% } %>
														</td>
													</tr>






													<tr>
														<td>Send Email Alert for New Assignment?</td>
														<td>
														<%if(isEdit){ %>
															<div class="pretty p-switch p-fill p-toggle">
																<form:checkbox value="${assignment.sendEmailAlert}" id="sendEmailAlert"
																	path="sendEmailAlert" />
																<div class="state p-success p-on">
																	<label>Yes</label>
																</div>
																<div class="state p-danger p-off">
																	<label>No</label>
																</div>

															</div>
															<% } else { %>
															<div class="pretty p-switch p-fill p-toggle">
																<form:checkbox value="N" id="sendEmailAlert"
																	path="sendEmailAlert" />
																<div class="state p-success p-on">
																	<label>Yes</label>
																</div>
																<div class="state p-danger p-off">
																	<label>No</label>
																</div>

															</div>
															<% } %>
														</td>
													</tr>
													<tr>
														<td>Send SMS Alert for New Assignment?</td>
														<td>
														
														<%if(isEdit) { %>
															<div class="pretty p-switch p-fill p-toggle">
																<form:checkbox value="${assignment.sendSmsAlert}" id="sendSmsAlert"
																	path="sendSmsAlert" />
																<div class="state p-success p-on">
																	<label>Yes</label>
																</div>
																<div class="state p-danger p-off">
																	<label>No</label>
																</div>

															</div>
															<% } else { %>
															<div class="pretty p-switch p-fill p-toggle">
																<form:checkbox value="N" id="sendSmsAlert"
																	path="sendSmsAlert" />
																<div class="state p-success p-on">
																	<label>Yes</label>
																</div>
																<div class="state p-danger p-off">
																	<label>No</label>
																</div>

															</div>
															<% } %>
														</td>
													</tr>
													<c:if test="${sendAlertsToParents eq true}">
														<tr>
															<td>Send Email Alert To Parents?</td>
															<td>
															<%if(isEdit){ %>
																<div class="pretty p-switch p-fill p-toggle">
																	<form:checkbox value="${assignment.sendEmailAlertToParents}" id="sendEmailAlertParents"
																		path="sendEmailAlertToParents" />
																	<div class="state p-success p-on">
																		<label>Yes</label>
																	</div>
																	<div class="state p-danger p-off">
																		<label>No</label>
																	</div>

																</div>
																<% } else { %>
																<div class="pretty p-switch p-fill p-toggle">
																	<form:checkbox value="N" id="sendEmailAlertParents"
																		path="sendEmailAlertToParents" />
																	<div class="state p-success p-on">
																		<label>Yes</label>
																	</div>
																	<div class="state p-danger p-off">
																		<label>No</label>
																	</div>

																</div>
																<% } %>
															</td>
														</tr>
														<tr>
															<td>Send SMS Alert To Parents?</td>
															<td>
															
															<%if(isEdit){ %>
																<div class="pretty p-switch p-fill p-toggle">
																	<form:checkbox value="${assignment.sendSmsAlertToParents}" id="sendEmailAlertParents"
																		path="sendSmsAlertToParents" />
																	<div class="state p-success p-on">
																		<label>Yes</label>
																	</div>
																	<div class="state p-danger p-off">
																		<label>No</label>
																	</div>

																</div>
																<% } else { %>
																<div class="pretty p-switch p-fill p-toggle">
																	<form:checkbox value="N" id="sendEmailAlertParents"
																		path="sendSmsAlertToParents" />
																	<div class="state p-success p-on">
																		<label>Yes</label>
																	</div>
																	<div class="state p-danger p-off">
																		<label>No</label>
																	</div>

																</div>
																<%} %>
															</td>
														</tr>
													</c:if>
												</tbody>
											</table>
										</div>


										<div class="col-12 mt-5">
											<h5 class="text-center">Assignment Text / Instructions</h5>
											<form:textarea id="editor1" class="editor1" name="editor1"
												path="assignmentText"></form:textarea>
										</div>

										<div class="col-6 m-auto">
											<div class="form-row mt-5">


												<%
													if (isEdit) {
												%>
												<button id="submit"
													class="btn btn-info col-md-5 col-sm-12  ml-auto mr-auto mb-3"
													formaction="updateAssignment">Update Assignment</button>
												<%
													} else {
												%>
												<button id="submit" onclick="myFunction()"
													class="btn btn-info col-md-5 col-sm-12  ml-auto mr-auto mb-3"
													formaction="createAssignment">Create Assignment</button>
												<%
													}
												%>




											</div>
										</div>






									</form:form>
								</div>
							</div>










						</div>
						<jsp:include page="../common/newSidebar.jsp" />

					</div>
				</div>
				<!-- SIDEBAR START -->

				<!-- SIDEBAR END -->
				<jsp:include page="../common/footer.jsp" />

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
	<!-- 			<script>
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
				</script> -->

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

						/* var plagReq = $("#plagscanRequired").val();
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
						}); */

					});
				</script>