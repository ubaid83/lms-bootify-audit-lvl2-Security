<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
      uri="http://www.springframework.org/security/tags"%>




<jsp:include page="../common/newDashboardHeader.jsp" />

<div class="d-flex" id="facultyAssignmentPage">
      <jsp:include page="../common/newLeftNavBarFaculty.jsp" />
      <jsp:include page="../common/newLeftSidebarFaculty.jsp" />
      <jsp:include page="../common/rightSidebarFaculty.jsp" />

      <!-- DASHBOARD BODY STARTS HERE -->
      <%
            boolean isEdit = "true".equals((String) request
                        .getAttribute("edit"));
      %>
      <div class="container-fluid m-0 p-0 dashboardWraper">

            <jsp:include page="../common/newTopHeaderFaculty.jsp" />

            <div class="container mt-5">
                  <div class="row">
                        <!-- SEMESTER CONTENT -->
                        <div
                              class="col-lg-9 col-md-9 col-sm-12 dashboardBody ml-auto mr-auto">
                              <div class="dashboard_container_spacing">


                                    <nav aria-label="breadcrumb">
                                          <ol class="breadcrumb">
                                                <li class="breadcrumb-item"><a
                                                      href="${pageContext.request.contextPath}/homepage">Dashboard</a></li>
                                                <li class="breadcrumb-item" aria-current="page"><c:out
                                                            value="${Program_Name}" /></li>
                                                <sec:authorize access="hasRole('ROLE_STUDENT')">
                                                      <c:out value="${AcadSession}" />
                                                </sec:authorize>
                                                <li class="breadcrumb-item active" aria-current="page">
                                                      Create Assignment</li>
                                          </ol>
                                    </nav>

                                    <jsp:include page="../common/alert.jsp" />


                                    <!-- Input Form Panel -->
                                    <div class="card bg-white border">
                                          <div class="card-body">
                                                <div>
                                                      <h5 class="text-center border-bottom pb-2">Create Multiple
                                                            Assignments for ${assignment.course.courseName }</h5>
                                                      <div class="form-group">
                                                            <button id="target" class="btn btn-large btn-dark"
                                                                  type="button" data-toggle="modal" data-target="#myModal">Create
                                                                  Random Groups</button>
                                                      </div>
                                                </div>

<%-- 
                                                <div class="modal fade" id="myModal" role="dialog">
                                                      <div class="modal-dialog">

                                                            <!-- Modal content-->
                                                            <div class="modal-content">
                                                                  <div class="modal-header">
                                                                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                                                                        <h4 class="modal-title">Please Enter number of students
                                                                              for each group.</h4>
                                                                        <br>

                                                                        <p>
                                                                              <b>Note:</b>
                                                                        <ul>
                                                                              <li>Extra students will be added in last group.</li>
                                                                              <li>Random characters will be appended in Group name</li>
                                                                              <li>Example : ${assignment.course.courseName } Group 1
                                                                                    nB</li>


                                                                        </ul>
                                                                  </div>
                                                                  <div class="modal-body">
                                                                        <div class="col-sm-6 col-md-4 col-xs-12 column">
                                                                              <div class="form-group" id="totalStudents">
                                                                                    <label for="">Total No. of Students : </label>
                                                                                    <c:out value="${totalStudentsList}"></c:out>
                                                                              </div>

                                                                        </div>
                                                                        <input type="number" name="noOfStudents" id="id"
                                                                              value="${noOfStudents}">

                                                                  </div>

                                                                  <div class="modal-footer">


                                                                        <button type="button" class="btn btn-default" id="create">Create
                                                                              Groups</button>
                                                                        <button id="close" type="button" class="btn btn-default"
                                                                              data-dismiss="modal">Close</button>

                                                                  </div>
                                                            </div>

                                                      </div>
                                                </div> --%>

                                                <!-- =================== -->

                                                <div class="modal fade fnt-13" id="myModal" tabindex="-1"
                                                      role="dialog" aria-labelledby="createRandomGroup"
                                                      aria-hidden="true">
                                                      <div
                                                            class="modal-dialog modal-dialog-centered modal-dialog-scrollable"
                                                            role="document">
                                                            <div class="modal-content">
                                                                  <div class="modal-header bg-blue">
                                                                        <h5 class="modal-title text-white" id="submitAssignmentTitle">Create
                                                                              Random Group</h5>
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
                                                                              <li>Example : ${assignment.course.courseName } Group 1
                                                                                    nB</li>


                                                                        </ul>
                                                                        <hr/>
                                                                        
                                                                        <div class="form-group text-center font-weight-bold" id="totalStudents">
                                                                                    <label for="">Total No. of Students : </label>
                                                                                    <c:out value="${totalStudentsList}"></c:out>
                                                                              </div>
                                                                              
                                                                              <input class="form-control w-50 m-auto" type="number" name="noOfStudents" id="id"
                                                                              value="${noOfStudents}">
                                                                         <div id="grpAlert" class="groupAlertC alert alert-success fade show alert-dismissible alert-success d-none mt-3" role="alert">
                                                                         <!-- <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a> -->
                                                                         Group has been created successfully. Please close the tab to continue.
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

                                                <!-- Modal content-->


                                                <div class="x_content">
                                                      <form:form action="createGroupAssignments" method="post"
                                                            modelAttribute="assignment" enctype="multipart/form-data">

                                                            <form:input path="courseId" type="hidden" />
                                                            <%
                                                                  if (isEdit) {
                                                            %>
                                                            <form:input type="hidden" path="id" />
                                                            <%
                                                                  }
                                                            %>
                                                            <c:forEach begin='1' end='5' varStatus="loop">
                                                                  <div class="row">
                                                                        <div class="col-md-4 col-sm-6 col-xs-12 column">
                                                                              <div class="form-group">
                                                                                    <form:label class="textStrong" path="assignmentName" for="assignmentName">Assignment Name ${loop.index}</form:label>
                                                                                    <form:input path="assignmentName" type="text"
                                                                                          name="assignmentName[]" class="form-control" />
                                                                              </div>
                                                                        </div>
                                                                        <div class="col-md-4 col-sm-6 col-xs-12 column">
                                                                              <div class="form-group" id="check">
                                                                                    <!-- <label for="file">  -->

                                                                                    <label class="textStrong" for="file"> <%
     if (isEdit) {
%> Select if you wish to override earlier file <%
     } else {
%> Assignment Question File ${loop.index} <%
     }
%>
                                                                                    </label>
                                                                                    <input id="filePaths${loop.index}"
                                                                                          name="filePaths${loop.index}" type="file"
                                                                                          class="form-control" multiple="multiple" />



                                                                              </div>
                                                                              <div id="fileSize${loop.index}"></div>
                                                                        </div>
                                                                        <div class="col-md-4 col-sm-6 col-xs-12 column">
                                                                              <div class="form-group">
                                                                                    <form:label class="textStrong" path="groupName" for="groupId">Groups</form:label>
                                                                                    <form:select multiple="multiple" id="grps${loop.index}"
                                                                                          path="grps${loop.index}" name="grps${loop.index}"
                                                                                          class="form-control" style="overflow: auto;">
                                                                                          <form:option value="">Select Group</form:option>
                                                                                          <c:forEach var="groups" items="${allGroups}"
                                                                                                varStatus="status">
                                                                                                <form:option value="${groups.id}">${groups.groupName}</form:option>
                                                                                          </c:forEach>

                                                                                    </form:select>
                                                                              </div>
                                                                        </div>
                                                                  </div>
                                                            </c:forEach>
<hr/>
                                                            <div class="row">
                                                                 <%--  <div class="col-md-4 col-sm-6 col-xs-12 column">
                                                                        <div class="form-group">
                                                                              <form:label path="startDate" for="startDate">Start Date <span
                                                                                          style="color: red">*</span>
                                                                              </form:label>

                                                                              <div class='input-group date' id='singleDatePicker1'>
                                                                                    <form:input id="singleStartDate" path="startDate" type="text"
                                                                                          placeholder="Start Date" class="form-control"
                                                                                          required="required" readonly="true" />
                                                                                    <span class="input-group-addon"><span
                                                                                          class="glyphicon glyphicon-calendar"></span> </span>
                                                                              </div>

                                                                        </div>
                                                                  </div>
                                                                  
                                                                  <div class="col-md-4 col-sm-6 col-xs-12 column">
                                                                        <div class="form-group">
                                                                              <form:label path="endDate" for="endDate">End Date <span
                                                                                          style="color: red">*</span>
                                                                              </form:label>
                                                                              <div class='input-group date' id='singleDatePicker2'>
                                                                                    <form:input id="singleEndDate" path="endDate" type="text"
                                                                                          placeholder="End Date" class="form-control"
                                                                                          required="required" readonly="true" />
                                                                                    <span class="input-group-addon"><span
                                                                                          class="glyphicon glyphicon-calendar"></span> </span>
                                                                              </div>

                                                                        </div>
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
														value="${test.startDate} - ${test.endDate}" readonly />
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
														required="required" readonly />
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
                                                                  
                                                                  
                                                                  
                                                                  
                                                                  <div class="col-md-4 col-sm-12 mt-3">
                                                                                          <div class="form-group">
                                                                                                <form:label class="textStrong" path="maxScore" for="maxScore">Score Out of <span
                                                                                                            style="color: red">*</span>
                                                                                                </form:label>
                                                                                                <form:input path="maxScore" class="form-control"
                                                                                                      value="${assignment.maxScore}" type="number"
                                                                                                      required="required" />
                                                                                          </div>
                                                                                    </div>
                                                                                    <div class="col-md-4 col-sm-12 mt-3">
                                                                                    <div class="form-group">
                                                                                          <label class="textStrong control-label">Assignment
                                                                                                Type <span style="color: red">*</span>
                                                                                          </label>
                                                                                          <form:select class="form-control" id="showStatusDropDown"
                                                                                                path="assignmentType" placeholder="Assignment Type"
                                                                                                required="required">
                                                                                                <form:option value="" disabled="true" selected="true">Select </form:option>
                                                                                                <form:option value="Presentation">Presentation</form:option>
                                                                                                <form:option value="WrittenAssignment">Written Assignment</form:option>
                                                                                                <form:option value="Viva">Viva</form:option>
                                                                                                <form:option value="ReportWriting">Report Writing</form:option>
                                                                                          </form:select>
                                                                                    </div>
                                                                              </div>
                                                            </div>
<hr/>
                                                            <div class="row">
                                                                                    <div class="col-lg-4 col-md-4 col-sm-12 mb-3">
                                                                                          <form:label class="textStrong" path="plagscanRequired"
                                                                                                for="plagscanRequired">Is Plagiarism Check Required? <span
                                                                                                      style="color: red">*</span>
                                                                                          </form:label>
                                                                                          <br>
                                                                                          <form:radiobutton name="plagscanRequired" id="Yes"
                                                                                                value="Yes" path="plagscanRequired"  disabled="true"/>
                                                                                          Yes<br>
                                                                                          <form:radiobutton name="plagscanRequired" id="No"
                                                                                                value="No" path="plagscanRequired" checked="true"/>
                                                                                          No<br>
                                                                                    </div>
                                                                                    
                                                                                   <%--  <div class="col-lg-4 col-md-4 col-sm-12  mb-3"
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
                                                                              <div class="col-lg-4 col-md-4 col-sm-12 mb-3"
                                                                                          style="display: none;" id="threshold">
                                                                                          <div class="form-group">
                                                                                                <form:label class="textStrong" path="threshold" for="threshold">Threshold Value for Plagarism Check <span
                                                                                                            style="color: red">*</span>
                                                                                                </form:label>
                                                                                                <form:input id="thresholdId" path="threshold"
                                                                                                      class="form-control" value="${assignment.threshold}"
                                                                                                      type="number" required="required" max="100" />
                                                                                          </div>
                                                                                    </div>
                                                                                    
                                                                                    
                                                                                    
                                                                                    <div class="col-lg-4 col-md-4 col-sm-12">
                                                                                    <div class="form-group">
                                                                                          <label class="textStrong" for="rightGrant">Evaluation Right Granted
                                                                                                to Peer Faculties?</label> <br> <input id="Yes"
                                                                                                name="rightGrant" name="rightGrant" type="radio"
                                                                                                value="Y" /> Yes<br> <input id="No"
                                                                                                name="rightGrant" name="rightGrant" type="radio"
                                                                                                value="N" /> No<br>




                                                                                    </div>
                                                                              </div> --%>
                                                                              </div>
                                                                        <hr/>
                                                                  <!-- 
                                                                  ================================
                                                                  TABLE 
                                                                  ==================================
                                                                  -->
                                                            <table class="table table-bordered table-striped mt-5 font-weight-bold">
                                            <tbody>
                                                <tr>
                                                    <td>Allow Submission after End date? </td>
                                                    <td>
                                                    <%if(isEdit){ %>
                                                        <div class="pretty p-switch p-fill p-toggle">
                                                            <form:checkbox path="allowAfterEndDate"
                                                                                                class="form-control" value="${assignment.allowAfterEndDate}" data-toggle="toggle"
                                                                                                data-on="Yes" data-off="No" data-onstyle="success"
                                                                                                data-offstyle="danger" data-style="ios" data-size="mini" />
                                                            <div class="state p-success p-on">
                                                                <label>Yes</label>
                                                            </div>
                                                            <div class="state p-danger p-off">
                                                                <label>No</label>
                                                            </div>

                                                        </div>
                                                        <%} else { %>
                                                        <div class="pretty p-switch p-fill p-toggle">
                                                            <form:checkbox path="allowAfterEndDate"
                                                                                                class="form-control" value="N" data-toggle="toggle"
                                                                                                data-on="Yes" data-off="No" data-onstyle="success"
                                                                                                data-offstyle="danger" data-style="ios" data-size="mini" />
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
                                                    <td>Show Results to Students immediately? </td>
                                                    <td>
                                                    <%if(isEdit) {%>
                                                        <div class="pretty p-switch p-fill p-toggle">
                                                            <form:checkbox path="showResultsToStudents"
                                                                                                class="form-control" value="${assignment.showResultsToStudents}" data-toggle="toggle"
                                                                                                data-on="Yes" data-off="No" data-onstyle="success"
                                                                                                data-offstyle="danger" data-style="ios" data-size="mini" />
                                                            <div class="state p-success p-on">
                                                                <label>Yes</label>
                                                            </div>
                                                            <div class="state p-danger p-off">
                                                                <label>No</label>
                                                            </div>

                                                        </div>
                                                        <% }  else { %>
                                                        <div class="pretty p-switch p-fill p-toggle">
                                                            <form:checkbox path="showResultsToStudents"
                                                                                                class="form-control" value="N" data-toggle="toggle"
                                                                                                data-on="Yes" data-off="No" data-onstyle="success"
                                                                                                data-offstyle="danger" data-style="ios" data-size="mini" />
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
                                                                                                class="form-control" value="#{assignment.submitByOneInGroup}" data-toggle="toggle"
                                                                                                data-on="Yes" data-off="No" data-onstyle="success"
                                                                                                data-offstyle="danger" data-style="ios" data-size="mini"
                                                                                                id="submitByOneInGroup" onclick="mouseoverEvent()" />
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
                                                                                                class="form-control" value="N" data-toggle="toggle"
                                                                                                data-on="Yes" data-off="No" data-onstyle="success"
                                                                                                data-offstyle="danger" data-style="ios" data-size="mini"
                                                                                                id="submitByOneInGroup" onclick="mouseoverEvent()" />
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
                                                    <td>Send Email Alert for New Assignment? </td>
                                                    <td>
                                                    
                                                    <%if(isEdit){ %>
                                                        <div class="pretty p-switch p-fill p-toggle">
                                                            <form:checkbox path="sendEmailAlert" class="form-control"
                                                                                                value="${assignment.sendEmailAlert}" data-toggle="toggle" data-on="Yes"
                                                                                                data-off="No" data-onstyle="success"
                                                                                                data-offstyle="danger" data-style="ios" data-size="mini" />
                                                            <div class="state p-success p-on">
                                                                <label>Yes</label>
                                                            </div>
                                                            <div class="state p-danger p-off">
                                                                <label>No</label>
                                                            </div>

                                                        </div>
                                                        <% } else { %>
                                                        <div class="pretty p-switch p-fill p-toggle">
                                                            <form:checkbox path="sendEmailAlert" class="form-control"
                                                                                                value="N" data-toggle="toggle" data-on="Yes"
                                                                                                data-off="No" data-onstyle="success"
                                                                                                data-offstyle="danger" data-style="ios" data-size="mini" />
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
                                                    <%if(isEdit){ %>
                                                        <div class="pretty p-switch p-fill p-toggle">
                                                            <form:checkbox path="sendSmsAlert" class="form-control"
                                                                                                value="#{assignment.sendSmsAlert}" data-toggle="toggle" data-on="Yes"
                                                                                                data-off="No" data-onstyle="success"
                                                                                                data-offstyle="danger" data-style="ios" data-size="mini" />
                                                            <div class="state p-success p-on">
                                                                <label>Yes</label>
                                                            </div>
                                                            <div class="state p-danger p-off">
                                                                <label>No</label>
                                                            </div>

                                                        </div>
                                                        <% } else { %>
                                                        <div class="pretty p-switch p-fill p-toggle">
                                                            <form:checkbox path="sendSmsAlert" class="form-control"
                                                                                                value="N" data-toggle="toggle" data-on="Yes"
                                                                                                data-off="No" data-onstyle="success"
                                                                                                data-offstyle="danger" data-style="ios" data-size="mini" />
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
                                                    <td>Send Email Alert To Parents? </td>
                                                    <td>
                                                    <c:if test="${sendAlertsToParents eq false}">
                                                    
                                                    	<%if(isEdit){ %>
                                                        <div class="pretty p-switch p-fill p-toggle">
                                                            <form:checkbox path="sendEmailAlertToParents"
                                                                                                      class="form-control" value="${assignment.sendEmailAlertToParents}" data-toggle="toggle"
                                                                                                      data-on="Yes" data-off="No" data-onstyle="success"
                                                                                                      data-offstyle="danger" data-style="ios"
                                                                                                      data-size="mini" />
                                                            <div class="state p-success p-on">
                                                                <label>Yes</label>
                                                            </div>
                                                            <div class="state p-danger p-off">
                                                                <label>No</label>
                                                            </div>

                                                        </div>
                                                        <% } else { %>
                                                        <div class="pretty p-switch p-fill p-toggle">
                                                            <form:checkbox path="sendEmailAlertToParents"
                                                                                                      class="form-control" value="N" data-toggle="toggle"
                                                                                                      data-on="Yes" data-off="No" data-onstyle="success"
                                                                                                      data-offstyle="danger" data-style="ios"
                                                                                                      data-size="mini" />
                                                            <div class="state p-success p-on">
                                                                <label>Yes</label>
                                                            </div>
                                                            <div class="state p-danger p-off">
                                                                <label>No</label>
                                                            </div>

                                                        </div>
                                                        <% } %>
                                                        </c:if>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>Send SMS Alert To Parents? </td>
                                                    <td>
                                                    <c:if test="${sendAlertsToParents eq false}">
                                                    <%if(isEdit) { %>
                                                        <div class="pretty p-switch p-fill p-toggle">
                                                            <form:checkbox path="sendSmsAlertToParents"
                                                                                                      class="form-control" value="${assignment.sendSmsAlertToParents}" data-toggle="toggle"
                                                                                                      data-on="Yes" data-off="No" data-onstyle="success"
                                                                                                      data-offstyle="danger" data-style="ios"
                                                                                                      data-size="mini" />
                                                            <div class="state p-success p-on">
                                                                <label>Yes</label>
                                                            </div>
                                                            <div class="state p-danger p-off">
                                                                <label>No</label>
                                                            </div>

                                                        </div>
                                                        <% } else { %>
                                                        <div class="pretty p-switch p-fill p-toggle">
                                                            <form:checkbox path="sendSmsAlertToParents"
                                                                                                      class="form-control" value="N" data-toggle="toggle"
                                                                                                      data-on="Yes" data-off="No" data-onstyle="success"
                                                                                                      data-offstyle="danger" data-style="ios"
                                                                                                      data-size="mini" />
                                                            <div class="state p-success p-on">
                                                                <label>Yes</label>
                                                            </div>
                                                            <div class="state p-danger p-off">
                                                                <label>No</label>
                                                            </div>

                                                        </div>
                                                        <% } %>
                                                        </c:if>
                                                    </td>
                                                </tr>
                                            </tbody>
                                        </table>
                                        <!-- TABLE END  -->
                                                            
                                                            <h5 class="text-center mt-5 mb-3">Assignment Text / Instructions</h5>

                                                                              <form:textarea class="form-group w-100 testDesc"  path="assignmentText"
                                                                                    name="editor1" id="editor1" rows="10" cols="80" />

                                                            <!-- <div class="row">
                                                                        
                                                                  </div> -->

                                                            <hr>
                                                            <div class="row">

                                                                  <div class="col-12 text-center">
                                                                        <div class="form-group">

                                                                              <%
                                                                                    if (isEdit) {
                                                                              %>
                                                                              <button id="submit123" class="btn btn-large btn-success"
                                                                                    formaction="updateAssignment">Update Assignment</button>
                                                                              <%
                                                                                    } else {
                                                                              %>

                                                                              <%--       <c:url value="createGroupAssignments" var="detailsUrl">
                                                                                          <c:param name="filePaths" value="${filePaths}" />
                                                                                    </c:url> --%>

                                                                              <button id="submit" class="btn btn-large btn-success" onclick="myFunction()"
                                                                                    formaction="createGroupAssignments">Create
                                                                                    Assignment</button>
                                                                              <%
                                                                                    }
                                                                              %>
                                                                              <button id="cancel" class="btn btn-danger"
                                                                                    formaction="homepage" formnovalidate="formnovalidate">Cancel</button>
                                                                        </div>
                                                                  </div>
                                                            </div>



                                                      </form:form>
                                                </div>
                                          </div>
                                    </div>



                              </div>



                        </div>

                        <!-- SIDEBAR START -->
                        <jsp:include page="../common/newSidebar.jsp" />
                        <!-- SIDEBAR END -->
                        <jsp:include page="../common/footer.jsp" />
                       <!--  <script>
                              $(function() {

                                    $('#Yes').on('click', function() {
                                          $('#runPlagiarism').show();
                                          $('#threshold').show();

                                          $("#Submission").prop('required', true);

                                          $("#Manual").prop('required', true);

                                          $("#thresholdId").prop('required', true);

                                    });

                                    $('#No').on('click', function() {
                                          $('#runPlagiarism').hide();
                                          $('#threshold').hide();

                                          $("#Submission").prop('required', false);

                                          $("#Manual").prop('required', false);

                                          $("#thresholdId").prop('required', false);
                                    });

                              });
                        </script> -->

						<script>
						<script>
						
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
						</script>
						</script>
                        <script>
                              var times = 5;
                              for (var i = 1; i <= times; i++) {

                                    //var str = "filePaths";
                                    var id = "filePaths".concat(i);

                                    //alert("ID 1 "+id);

                                    $('#' + id).bind(
                                                'change',
                                                function() {
                                                      //alert('This file size is: ' + this.files[0].size/1024/1024 + "MB");
                                                      var fileSize = this.files[0].size / 1024
                                                                  / 1024 + "MB";
                                                      var id1 = $(this).attr("id");
                                                      //alert(id1);
                                                      var j = id1.charAt(9);
                                                      //alert(j);
                                                      var id2 = "fileSize".concat(j);
                                                      //alert("Id 2 "+id2);
                                                      $('#' + id2)
                                                                  .html("File Size:" + (fileSize));
                                                });
                              }
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
                                                                                                      '#courseId')
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
                                                                                                                        for (var i = 1; i <= times; i++) {

                                                                                                                              //var str = "filePaths";
                                                                                                                              var id = "grps"
                                                                                                                                          .concat(i);

                                                                                                                              $(
                                                                                                                                          '#'
                                                                                                                                                      + id)
                                                                                                                                          .append(
                                                                                                                                                      optionsAsString);
                                                                                                                        }

                                                                                                                  }
                                                                                                            });
                                                                                                //$("#close").trigger("click")
                                                                                                //$("#close").trigger("click")
                                                                                                //$("#close").click()

                                                                                                //$('#myModal').modal('toggle');
                                                                                                //$("#myModal .close").click()
                                                                                                //$('#myModal').modal('toggle');
                                                                                                
                                                                                                //$('#grpAlert').removeClass('d-none');
                                                                                                $('.groupAlertC').removeClass('d-none	');
                                                                                                //alert('Group created successfully. Please close the group tab to continue.');
                                                                                          } else {
                                                                                                alert('Error!');
                                                                                          }

                                                                                    });

                                                      });
                        </script>

