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
				<div
					class="col-lg-9 col-md-9 col-sm-12 dashboardBody ml-auto mr-auto">
					<nav aria-label="breadcrumb">
						<ol class="breadcrumb">
							<li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/homepage">Dashboard</a></li>
							<li class="breadcrumb-item active" aria-current="page">
								<%
									if (isEdit) {
								%> Update Assignment For Group
							</li>
							<%
								}else{
							%>
							Create Assignment For Group
							<%
								}
							%>
						</ol>
					</nav>
					<!-- 14-06-2021 -->
					<jsp:include page="../common/alert.jsp" />	
					<div class="card bg-white">
						<div class="card-body">

							<form:form action="saveGroupAssignment" method="post"
								modelAttribute="assignment" enctype="multipart/form-data">
								<%-- <form:input path="courseId" type="hidden" /> --%>
								<%
									if (isEdit) {
								%>
								<form:input type="hidden" path="id" />
								<%
									if (isEdit) {
								%>
								<form:input type="hidden" path="id" />
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

									<div class="col-12 text-center">
										<%
											if (isEdit) {
										%>
										<h3>Update Assignment For Group</h3>
										<%
											}else{
										%>
										<h3>Add Assignment For Group</h3>
										<%
											}
										%>
									</div>

									<!--FORM ITEMS STARTS HERE-->


									<div class="col-lg-12 col-md-12 col-sm-12">
										<div class="form-group">
											<form:label class="textStrong" path="groupName" for="groupId">Groups <span
													style="color: red">*</span>
											</form:label>
											<form:select multiple="multiple" id="grps" path="grps"
												required="required" class="form-control"
												style="overflow: auto;">
												<form:option value="">Select Group</form:option>
												<c:forEach var="groups" items="${allGroups}"
													varStatus="status">
													<form:option value="${groups.id}">${groups.groupName}</form:option>
												</c:forEach>

											</form:select>
										</div>
									</div>
									<div class="col-lg-12 col-md-12 col-sm-12">
										<div class="form-group">
											<div>
												<button id="target" class="btn btn-large btn-primary"
													type="button" data-toggle="modal" data-target="#myModal">Create
													Random Groups</button>
											</div>
										</div>
									</div>


									<%-- <div class="modal fade" id="myModal" role="dialog">
                                                                        <div class="modal-dialog">

                                                                              <!-- Modal content-->
                                                                              <div class="modal-content">
                                                                                    <div class="modal-header">
                                                                                          <button type="button" class="close" data-dismiss="modal">&times;</button>
                                                                                          <h4 class="modal-title">Please Enter number of
                                                                                                students for each group.</h4>
                                                                                          <br>
                                                                                          <p>
                                                                                                <b>Note:</b>
                                                                                          <ul>
                                                                                                <li>Extra students will be added in last group.</li>
                                                                                                <li>Random characters will be appended in Group
                                                                                                      name</li>
                                                                                                <li>Example : ${assignment.course.courseName }
                                                                                                      Group 1 nB</li>


                                                                                          </ul>
                                                                                    </div>
                                                                                    <div class="modal-body">
                                                                                          <div class="col-sm-6 col-md-4 col-xs-12 column">
                                                                                                <div class="form-group" id="totalStudents">
                                                                                                      <form:label path="" for="">Total No. of Students : </form:label>
                                                                                                      <c:out value="${totalStudentsList}"></c:out>
                                                                                                </div>

                                                                                          </div>

                                                                                          <input type="number" name="noOfStudents" id="id"
                                                                                                value="${noOfStudents}">

                                                                                    </div>

                                                                                    <div class="modal-footer">


                                                                                          <button type="button" class="btn btn-default" id="close"
                                                                                                data-dismiss="modal">Close</button>
                                                                                          <button id="create" type="button" class="btn btn-default">Create
                                                                                                Groups</button>

                                                                                    </div>
                                                                              </div>

                                                                        </div>
                                                                  </div> --%>

									<!-- NEW MODAL FOR GROUP -->
									<div class="modal fade fnt-13" id="myModal" tabindex="-1"
										role="dialog" aria-labelledby="createRandomGroup"
										aria-hidden="true">
										<div
											class="modal-dialog modal-dialog-centered modal-dialog-scrollable"
											role="document">
											<div class="modal-content">
												<div class="modal-header bg-blue">
													<h5 class="modal-title text-white"
														id="submitAssignmentTitle">Create Random Group</h5>
													<button type="button" class="close" data-dismiss="modal"
														aria-label="Close">
														<span aria-hidden="true">&times;</span>
													</button>
												</div>
												<div class="modal-body">
													<strong>Note:</strong>
													<ul>
														<li>Extra students will be added in last group.</li>
														<li>Random characters will be appended in Group name</li>
														<li>Example : ${assignment.course.courseName } Group
															1 nB</li>


													</ul>
													<hr />

													<div class="form-group text-center font-weight-bold"
														id="totalStudents">
														<label for="">Total No. of Students : </label>
														<c:out value="${totalStudentsList}"></c:out>
													</div>

													<input class="form-control w-50 m-auto" type="number"
														name="noOfStudents" id="noOfStudentsid" value="${noOfStudents}">
													<!-- ALERT -->
													<div
														class="groupAlertC alert alert-success alert-dismissible fade show mt-3 d-none"
														role="alert">Groups created successfully. Please close the tab to continue.
														<button type="button" class="close" data-dismiss="alert"
															aria-label="Close">
															<span aria-hidden="true">&times;</span>
														</button>
													</div>
												</div>
												<div class="modal-footer">
													<button type="button" class="btn btn-success" id="create">Create
														Groups</button>
													<button id="close" type="button" class="btn btn-danger"
														data-dismiss="modal">Close</button>
												</div>
											</div>
										</div>
									</div>

									<!-- NEW MODAL END -->


									<div class="col-lg-4 col-md-4 col-sm-12 mt-3 position-relative">
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
									<div class="col-lg-4 col-md-4 col-sm-12 mt-3 position-relative">
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


									<div class="col-lg-4 col-md-4 col-sm-12 mt-3 position-relative">
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

									<%--     <div
                                                                        class="col-lg-4 col-md-4 col-sm-12 mt-3 position-relative">
                                                                        <div class="input-group">
                                                                              <label class="f-13 text-danger req">*</label> <label
                                                                                    class="sr-only">Select Date Range</label>
                                                                              <form:input type="hidden" id="testStartDate"
                                                                                    path="startDate" />
                                                                              <form:input type="hidden" id="testEndDate" path="endDate" />
                                                                              <input id="startDate" type="text" placeholder="Start Date"
                                                                                    class="form-control" required="required" />
                                                                              <div class="input-group-append">
                                                                                    <button class="btn btn-outline-secondary" type="button"
                                                                                          id="testDateRangeBtn">
                                                                                          <i class="fas fa-calendar"></i>
                                                                                    </button>
                                                                              </div>
                                                                        </div>
                                                                  </div> --%>



									<%
										if(isEdit) {
									%>

									<div class="col-lg-8 col-md-8 col-sm-12 mt-3 position-relative">
									<label class="textStrong">Select Date Range<span class="text-danger">*</span></label>
										<div class="input-group">
										<form:input type="hidden" id="testStartDate" path="startDate" />
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
									<div class="col-lg-8 col-md-8 col-sm-12 mt-3 position-relative">
									<label class="textStrong">Select Date Range<span class="text-danger">*</span></label>
										<div class="input-group">
											<label class="f-13 text-danger req">*</label> <label
												class="sr-only">Select Date Range</label>
											<form:input type="hidden" id="testStartDate" path="startDate" />
											<form:input type="hidden" id="testEndDate" path="endDate" />
											<input id="startDate" type="text"
												placeholder="Start Date - End Date" class="form-control"
												required="required" autocomplete="off"/>
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
									<div class="col-lg-4 col-md-4 col-sm-12 mt-3 position-relative">
										<label class="textStrong">Total Score<span class="text-danger">*</span></label>
										<form:input id="maxScore" path="maxScore" min="1"
											type="number" placeholder="Total Score"
											value="${assignment.maxScore}" class="form-control"
											required="required" />
									</div>



									<div class="col-lg-4 col-md-4 col-sm-12 mt-3 position-relative">
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



									<%-- <div
                                                                        class="col-lg-4 col-md-4 col-sm-12 mt-3 position-relative">
                                                                        <label class="f-13 text-danger req">*</label> <form:input id="duration" path="duration" min="1"
                                                                              type="number" placeholder="Duration" class="form-control"
                                                                              required="required" />
                                                                  </div>

                                                                  <div
                                                                        class="col-lg-4 col-md-4 col-sm-12 mt-3 position-relative">
                                                                        <label class="f-13 text-danger req">*</label> <form:input id="passScore" path="passScore" min="1"
                                                                        type="number" placeholder="Pass Score" class="form-control"
                                                                        required="required" />
                                                                  </div>
                                                                  <div
                                                                        class="col-lg-4 col-md-4 col-sm-12 mt-3 position-relative">
                                                                        <label class="f-13 text-danger req">*</label> 
                                                                        
                                                                        <form:input id="maxAttempt" path="maxAttempt" min="1"
                                                                        type="number" placeholder="Maximum Attempts" class="form-control"
                                                                        required="required" />
                                                                        
                                                                        <form:input id="maxAttempt" min="1" path="maxAttempt"
                                                                        type="number" placeholder="Max Attempt"
                                                                        class="form-control" required="required" />
                                                                  </div>
                                                                  <div
                                                                        class="col-lg-4 col-md-4 col-sm-12 mt-3 position-relative">
                                                                        <label class="f-13 text-danger req">*</label> <label
                                                                              class="sr-only">Test Type</label> <form:select id="testType" path="testType"
                                                                              class="form-control facultyparameter" required="required">

                                                                              <form:option value="Objective">Objective</form:option>
                                                                              <form:option value="Subjective">Subjective</form:option>
                                                                              <form:option value="Mix">Mix</form:option>
                                                                        </form:select>
                                                                  </div>
                                                                  <!--FORM ITEMS ENDS HERE-->
                                                            </div>

                                                            <hr />

                                                            <div class="form-row">
                                                                  <div class="col-lg-6 col-md-6 col-sm-12">
                                                                        <label>Random Question Required?</label>
                                                                        <div>
                                                                              <div class="pretty p-switch p-fill p-toggle">
                                                                                    <form:checkbox  id="randQReq" value="N" path="randomQuestion"/>
                                                                                    <div class="state p-success p-on">
                                                                                          <label>Yes</label>
                                                                                    </div>
                                                                                    <div class="state p-danger p-off">
                                                                                          <label>No</label>
                                                                                    </div>
                                                                              </div>
                                                                        </div>
                                                                        <div class="form-check pl-0 mt-2 d-none">
                                                                              <form:input id="inputMaxQ" type="number" path="maxQuestnToShow"
                                                                                    class="form-control w-75"
                                                                                    placeholder="Maximum Questions To Show" />
                                                                        </div>
                                                                  </div>

                                                                  <div class="col-lg-6 col-md-6 col-sm-12">
                                                                        <label>Set a Password for Test?</label>
                                                                        <div>
                                                                              <div class="pretty p-switch p-fill p-toggle">
                                                                                    <form:checkbox  id="setTestPwd" value="N" path="isPasswordForTest"/>
                                                                                    <div class="state p-success p-on">
                                                                                          <label>Yes</label>
                                                                                    </div>
                                                                                    <div class="state p-danger p-off">
                                                                                          <label>No</label>
                                                                                    </div>
                                                                              </div>
                                                                        </div>
                                                                        <div class="form-check pl-0 mt-2 d-none">
                                                                              <form:input id="testPwdVal" type="password" path="passwordForTest"
                                                                                    class="form-control w-75" placeholder="Set Test Password"
                                                                                    />
                                                                        </div>
                                                                  </div>
                                                            </div> --%>
								</div>



								<div class="row">
									<div class="col-md-4 col-sm-6 col-xs-12 column">
										<div class="slider round">
											<form:label class="textStrong" path="plagscanRequired" for="plagscanRequired">Is Plagiarism Check Required? <span
													style="color: red">*</span>
											</form:label>
											<br>
											<form:radiobutton name="plagscanRequired" id="Yes"
												value="Yes" path="plagscanRequired" disabled="true" />
											Yes<br>
											<form:radiobutton name="plagscanRequired" id="No" value="No"
												path="plagscanRequired" checked="true"/>
											No<br>


											<%-- 	<form:checkbox path="plagscanRequired" onclick="clicked();"
																class="form-control" value="Y" data-toggle="toggle"
																data-on="Yes" data-off="No" data-onstyle="success"
																data-offstyle="danger" data-style="ios" data-size="mini" /> --%>
										</div>
									</div>


									<%--<div class="col-md-4 col-sm-6 col-xs-12 column"
										style="display: none" id="runPlagiarism">
										<div class="slider round">
											<form:label class="textStrong" path="runPlagiarism" for="runPlagiarism">Run Plagiarism while <span
													style="color: red">*</span> :</form:label>
											<br>
											<form:radiobutton name="runPlagiarism" id="Submission"
												value="Submission" path="runPlagiarism" required="required" />
											Submission<br>
											<form:radiobutton name="runPlagiarism" id="Manual"
												value="Manual" path="runPlagiarism" />
											Manual<br>



										</div>
									</div>

									<div class="col-md-4 col-sm-6 col-xs-12 column"
										style="display: none;" id="threshold">
										<div class="form-group">
											<form:label class="textStrong" path="threshold" for="threshold">Threshold Value for Plagarism Check <span
													style="color: red">*</span>
											</form:label>
											<form:input id="thresholdId" path="threshold"
												class="form-control" value="${assignment.threshold}"
												type="number" required="required" max="100" />
										</div>
									</div>--%>

								</div>
								<%-- <div class="form-row">
									<div class="col-lg-4 col-md-4 col-sm-12 mt-3 position-relative">
										<div class="form-group">
											<label for="plagscanRequired">Is Plagcheck Required?</label>
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
										</div>
									</div>


									<div id="plagContent" class="d-none row">
										<div
											class="col-lg-6 col-md-6 col-sm-12 mt-3 position-relative"
											id="runPlagiarismDiv">
											<div class="form-group">
												<label for="plagscanRequired">Run Plagiarism while?</label>
												<div class="pretty p-switch p-fill p-toggle">
													<form:checkbox value="No" path="runPlagiarism"
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
												<% } else {%>
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

										<tr>
											<td>Submit Assignment By One in the Group?</td>
											<td>
											<%if(isEdit){ %>
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
										</tr>

										<tr>
											<td>Show Results to Students immediately?</td>
											<td>
											
											<% if(isEdit) { %>
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
											
											<%if(isEdit) { %>
												<div class="pretty p-switch p-fill p-toggle">
													<form:checkbox value="${assignment.rightGrant}" id="rightGrant" path="rightGrant" />
													<div class="state p-success p-on">
														<label>Yes</label>
													</div>
													<div class="state p-danger p-off">
														<label>No</label>
													</div>

												</div>
												<% } else { %>
												<div class="pretty p-switch p-fill p-toggle">
													<form:checkbox value="N" id="rightGrant" path="rightGrant" />
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
											
											<% if(isEdit){ %>
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
												<%} else {%>
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
											<td>Send SMS Alert for New Test?</td>
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
												
												<% if(isEdit){ %>
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
													<% } %>
												</td>
											</tr>
										</c:if>
									</tbody>
								</table>



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
											formaction="saveGroupAssignment">Create Assignment</button>
										<%
											}
										%>




									</div>
								</div>

							</form:form>




						</div>
					</div>
				</div>

				<!-- ENDDDDD -->







				<!-- SIDEBAR START -->
				<jsp:include page="../common/newSidebar.jsp" />
				<!-- SIDEBAR END -->
				<jsp:include page="../common/footer.jsp" />


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
				
				
					<!-- <script>
				
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
															var num = $('#noOfStudentsid')
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
																$('.groupAlertC').removeClass('d-none');
																
																
																
															} else {
																alert('Error!');
															}
														});
								
										$('#myModal').on('hidden.bs.modal', function (e) {
											  // do something...
											
											$('#noOfStudentsid').val('');
											$('.groupAlertC').addClass('d-none');
											});
									
									});
					
					
				</script>
				
				
				
				
				
				<script>
		$(document).ready(function() {

							$("#idForCourse")
									.on(
											'change',
											function() {
												var courseId = $(this).val();
												if (courseId) {
													$
															.ajax({
																type : 'GET',
																url : '${pageContext.request.contextPath}/getCourseByCourseId?'
																		+ 'idForCourse='
																		+ courseId,
																success : function(data) {
																	var json = JSON.parse(data);
																	console.log('json--------------------->>>>',json)
																	var optionsAsString = "";

																	$('#grps').find('option').remove();
																	console.log(json);
																	for (var i = 0; i < json.length; i++) {
																		var idjson = json[i];
																		console.log(idjson);

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
																	console.log("optionsAsString"+ optionsAsString);

																	$('#grps').append(optionsAsString);

																	$('#grps').trigger('change');
																}
															});
												} else {
													alert('Error no course');
												}
											});
							$('#idForCourse').trigger('change');
						});
	</script>