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
                                                        <%
																	if(isEdit) {
																%>
                                                                  <li class="breadcrumb-item active" aria-current="page">Edit
                                                            Test</li>
                                                                  <%
																	} else {
                                                                  %>
                                                                  <li class="breadcrumb-item active" aria-current="page">Create
                                                            Test</li>
																	<%} %>
                                                </ol>
                                          </nav>
                                          <jsp:include page="../common/alert.jsp" />
                                          <div class="card bg-white">
                                                <div class="card-body">

                                                      <form:form action="addTest" method="post" modelAttribute="test">
                                                            <div class="form-row">

                                                                   <%
																	if(isEdit) {
																%>
                                                                  <div class="col-12 text-center">
                                                                        <h3>Edit Test</h3>
                                                                  </div>
                                                                  <%
																	} else {
                                                                  %>
                                                                  <div class="col-12 text-center">
                                                                        <h3>Create Test</h3>
                                                                  </div>
																	<%} %>

                                                                  <!--FORM ITEMS STARTS HERE-->
                                                                  <div
                                                                        class="col-lg-4 col-md-4 col-sm-12 mt-3 position-relative">
                                                                        <label class="f-13 text-danger req">*</label> <form:input id="testName" path="testName" type="text"
                                                                        placeholder="Test Name" class="form-control"
                                                                        required="required" />
                                                                  </div>
                                                           
                                                                  <div
                                                                        class="col-lg-4 col-md-4 col-sm-12 mt-3 position-relative">
                                                                        <label class="sr-only">Select Course</label> <label
                                                                              class="f-13 text-danger req">*</label> <form:select id="idOfCourse" path="idOfCourse"
                                                                        class="form-control facultyparameter">
                                                                              <c:forEach var='cList'
                                                                                    items='${sessionWiseCourseList[course.acadSession]}'>
                                                                                    <c:if test="${course.id eq cList.id}">
                                                                                          <option value="${cList.id}" selected>${cList.courseName}</option>
                                                                                    </c:if>
                                                                                    <c:if test="${course.id ne cList.id }">
                                                                                          <option value="${cList.id}">${cList.courseName}</option>
                                                                                    </c:if>
                                                                              </c:forEach>
                                                                        </form:select>
                                                                  </div>
                                                                  <div
                                                                        class="col-lg-4 col-md-4 col-sm-12 mt-3 position-relative">
                                                                        <label class="f-13 text-danger req">*</label> <label
                                                                              class="sr-only">Faculty</label>
                                                                        <form:select id="facultyid" path="facultyId"
                                                                              class="form-control">
                                                                              <form:option value="">Select Faculty</form:option>
                                                                              <%--  <c:forEach var="course" items="${allCourses}"
                                                            varStatus="status">
                                                            <form:option value="${course.id}">${course.courseName}</form:option>
                                                      </c:forEach> --%>
                                                                        </form:select>
                                                                  </div>


											<%
												if(isEdit) {
											%>

											<div
												class="col-lg-8 col-md-8 col-sm-12 mt-3 position-relative">
												<div class="input-group">
													<label class="f-13 text-danger req">*</label> <label
														class="sr-only">Select Date Range</label>
													<form:input type="hidden" id="testStartDate"
														path="startDate" />
													<form:input type="hidden" id="testEndDate" path="endDate" />
													<input id="startDate" type="text"
														placeholder="Start Date - End Date" class="form-control"
														required="required"
														value="${test.startDate} - ${test.endDate}"
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
												<div class="input-group">
													<label class="f-13 text-danger req">*</label> <label
														class="sr-only">Select Date Range</label>
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
                                                                        <label class="f-13 text-danger req">*</label> <form:input id="duration" path="duration" min="1"
                                                                              type="number" placeholder="Duration" class="form-control"
                                                                              required="required" />
                                                                  </div>
                                                                  <div
                                                                        class="col-lg-4 col-md-4 col-sm-12 mt-3 position-relative">
                                                                        <label class="f-13 text-danger req">*</label> <form:input id="maxScore" path="maxScore" min="1"
                                                                        type="number" placeholder="Score Out of"
                                                                        class="form-control" required="required" />
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
                                                                        
                                                                        <%-- <form:input id="maxAttempt" min="1" path="maxAttempt"
                                                                        type="number" placeholder="Max Attempt"
                                                                        class="form-control" required="required" /> --%>
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
                                                                  <!-- SET QUESTION NOS -->
                                                                  
                                                                  
                                        <div class="col-lg-6 col-md-6 col-sm-12 mt-3 mb-3">
                                                                        <label>Same Mark Question?</label>
                                                                        <div>
                                                                              <div class="pretty p-switch p-fill p-toggle">
                                                                                    <form:checkbox  id="smqChk" value="N" path="sameMarksQue"/>
                                                                                    <div class="state p-success p-on">
                                                                                          <label>Yes</label>
                                                                                    </div>
                                                                                    <div class="state p-danger p-off">
                                                                                          <label>No</label>
                                                                                    </div>
                                                                              </div>
                                                                        </div>
                                                                        <div class="form-check pl-0 mt-2 d-none">
                                                                             <form:input id="marksPerQueIn" path="marksPerQue"
													type="number" placeholder="Marks per question"
													class="form-control" step=".0001" />
                                                                        </div>
                                                                  </div>
      
                                                                  <!-- SET QUE END  -->
                                                                  
                                                                  
                                                            </div>


                                                            <div class="form-row">
                                                                  <table
                                                                        class="table table-bordered table-striped mt-5 font-weight-bold">
                                                                        <tbody>
                                                                              <tr>
                                                                                    <td>Allow to Start test any time?</td>
                                                                                    <td>
                                                                                          <div class="pretty p-switch p-fill p-toggle">
                                                                                                <form:checkbox path="allowAfterEndDate" id="subAfterEndDate" value="Yes" />
                                                                                                <div class="state p-success p-on">
                                                                                                      <label>Yes</label>
                                                                                                </div>
                                                                                                <div class="state p-danger p-off">
                                                                                                      <label>No</label>
                                                                                                </div>

                                                                                          </div>
                                                                                    </td>
                                                                              </tr>
                                                                              <tr>
                                                                                    <td>Show Results to Students immediately?</td>
                                                                                    <td>
                                                                                          <div class="pretty p-switch p-fill p-toggle">
                                                                                                <form:checkbox value="Yes" id="showResult" path="showResultsToStudents"/>
                                                                                                <div class="state p-success p-on">
                                                                                                      <label>Yes</label>
                                                                                                </div>
                                                                                                <div class="state p-danger p-off">
                                                                                                      <label>No</label>
                                                                                                </div>

                                                                                          </div>
                                                                                    </td>
                                                                              </tr>
                                                                              
                                                                            <%
																				if(!isEdit) {
																			%>
	                                                                              
                                                                              <tr>
                                                                                    <td>Automatic Allocation To Students?</td>
                                                                                    <td>

                                                                                          <div class="pretty p-switch p-fill p-toggle">
                                                                                                <form:checkbox value="Yes" id="autoAllocateTest" path="autoAllocateToStudents"/>
                                                                                                <div class="state p-success p-on">
                                                                                                      <label>Yes</label>
                                                                                                </div>
                                                                                                <div class="state p-danger p-off">
                                                                                                      <label>No</label>
                                                                                                </div>

                                                                                          </div>
                                                                                    </td>
                                                                              </tr>
                                                                              <tr>
                                                                                    <td>Send Email Alert for New Test?</td>
                                                                                    <td>
                                                                                          <div class="pretty p-switch p-fill p-toggle">
                                                                                                <form:checkbox value="Yes" id="sendEmailAlert" path="sendEmailAlert" />
                                                                                                <div class="state p-success p-on">
                                                                                                      <label>Yes</label>
                                                                                                </div>
                                                                                                <div class="state p-danger p-off">
                                                                                                      <label>No</label>
                                                                                                </div>

                                                                                          </div>
                                                                                    </td>
                                                                              </tr>
                                                                              <tr>
                                                                                    <td>Send SMS Alert for New Test?</td>
                                                                                    <td>
                                                                                          <div class="pretty p-switch p-fill p-toggle">
                                                                                                <form:checkbox value="Yes" id="sendSmsAlert" path="sendSmsAlert"/>
                                                                                                <div class="state p-success p-on">
                                                                                                      <label>Yes</label>
                                                                                                </div>
                                                                                                <div class="state p-danger p-off">
                                                                                                      <label>No</label>
                                                                                                </div>

                                                                                          </div>
                                                                                    </td>
                                                                              </tr>
                                                                              <c:if test="${sendAlertsToParents eq true}">
                                                                              <tr>
                                                                                    <td>Send Email Alert To Parents?</td>
                                                                                    <td>
                                                                                          <div class="pretty p-switch p-fill p-toggle">
                                                                                                <form:checkbox value="Yes"
                                                                                                      id="sendEmailAlertParents" path="sendEmailAlertToParents"/>
                                                                                                <div class="state p-success p-on">
                                                                                                      <label>Yes</label>
                                                                                                </div>
                                                                                                <div class="state p-danger p-off">
                                                                                                      <label>No</label>
                                                                                                </div>

                                                                                          </div>
                                                                                    </td>
                                                                              </tr>
                                                                              <tr>
                                                                                    <td>Send SMS Alert To Parents?</td>
                                                                                    <td>
                                                                                          <div class="pretty p-switch p-fill p-toggle">
                                                                                                <form:checkbox value="Yes"
                                                                                                      id="sendEmailAlertParents" path="sendSmsAlertToParents"/>
                                                                                                <div class="state p-success p-on">
                                                                                                      <label>Yes</label>
                                                                                                </div>
                                                                                                <div class="state p-danger p-off">
                                                                                                      <label>No</label>
                                                                                                </div>

                                                                                          </div>
                                                                                    </td>
                                                                              </tr>
                                                                              </c:if>
                                                                            <%
																				}
																			%>
                                                                        </tbody>
                                                                  </table>
                                                            </div>

                                                            <div class="col-12 mt-5 p-0">
                                                            <h5 class="text-center">Test Description:</h5>
                                                                  <form:textarea id="editor1" class="editor1" name="editor1" path="testDescription"></form:textarea>
                                                            </div>

                                                            <div class="col-6 m-auto">
                                                                  <div class="form-row mt-5">
                                                                        <button id="createTestBtn" onclick="myFunction()"
                                                                              class="btn btn-info col-md-5 col-sm-12  ml-auto mr-auto mb-3">Create
                                                                              Test</button>
                                                                        <button
                                                                              class="btn btn-danger col-md-5 col-sm-12 ml-auto mr-auto mb-3">Cancel</button>
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
                        
                       <!--  <script type="text/javascript"
			src="//cdn.ckeditor.com/4.8.0/standard-all/ckeditor.js"></script>
		<script type="text/javascript"
			src="//cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.0/MathJax.js?config=TeX-AMS_HTML"></script>

		<script type="text/javascript">
			CKEDITOR
					.replace(
							'testDescription',
							{
								extraPlugins : 'mathjax',
								mathJaxLib : 'https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.0/MathJax.js?config=TeX-AMS_HTML'
							});
		</script> -->

		<script>
		
		function myFunction(){
			console.log("fdghdhyrfdhuytuytrutryutrruht");
			var dateinput = $('#startDate').val();
			console.log(dateinput);
		}
		
		$(function() {
			
			$('#createTestBtn').on('click', function(e){ 
				var dateinput = $('#startDate').val();
				console.log(dateinput);
				if(dateinput == ''){
					alert('Error: Enter proper Date inputs.');
					
					    e.preventDefault();
				
				} else {
					
				}
				
			});
			
		});
		
		
		
		
		
			$(function() {

				$('#Yes').on('click', function() {
					//alert('slidYes');

					var selected = $('#testType').val();

					$('#maxQuestions').show();

					$("#maxQuestnToShow").prop('required', true);

					$('#sameMarks').show();

					$("#SMQYes").prop('required', true);

					if (selected == 'Mix') {

						$('#specifyQuestion').show();

						$("#MaxYes").prop('required', true);

						/* $('#mixQuestions').show();
						
						$("#maxQuestnToShow1").prop('required', true);
						
						$("#maxQuestnToShow2").prop('required', true);
						
						$("#maxQuestnToShow3").prop('required', true);
						
						$("#maxQuestnToShow4").prop('required', true); */
					}

				});

				$('#No').on('click', function() {
					//alert('slidNo');
					$('#maxQuestions').hide();

					$("#maxQuestnToShow").prop('required', false);

					$('#sameMarks').hide();

					$("#SMQYes").prop('required', false);

					$('input:radio[name=sameMarks]').each(function() {
						$(this).prop('checked', false);
					});

					$('#specifyQuestion').hide();

					$("#MaxYes").prop('required', false);

					$('input:radio[name=specifyMaxQue]').each(function() {
						$(this).prop('checked', false);
					});

					$('#marksPerQue').hide();

					$("#marksPerQueIn").prop('required', false);

					/* $('#mixQuestions').hide();

					$("#maxQuestnToShow1").prop('required', false);

					$("#maxQuestnToShow2").prop('required', false);

					$("#maxQuestnToShow3").prop('required', false);
					
					$("#maxQuestnToShow4").prop('required', false); */

				});

			});
		</script>

		<script>
			$(function() {

				$('#SMQYes').on('click', function() {

					$('#marksPerQue').show();

					$("#marksPerQueIn").prop('required', true);

				});

				$('#SMQNo').on('click', function() {

					$('#marksPerQue').hide();

					$("#marksPerQueIn").prop('required', false);

				});

			});
		</script>

		<script>
			$(function() {

				$('#MaxYes').on('click', function() {

					var selected = $('#testType').val();

					if (selected == 'Mix') {

						$('#mixQuestions').show();

						$("#maxQuestnToShow1").prop('required', true);

						$("#maxQuestnToShow2").prop('required', true);

						$("#maxQuestnToShow3").prop('required', true);

						$("#maxQuestnToShow4").prop('required', true);
					}

				});

				$('#MaxNo').on('click', function() {

					$('#mixQuestions').hide();

					$("#maxQuestnToShow1").prop('required', false);

					$("#maxQuestnToShow2").prop('required', false);

					$("#maxQuestnToShow3").prop('required', false);

					$("#maxQuestnToShow4").prop('required', false);

				});

			});
		</script>

		<script>
			$(function() {

				$('#testType')
						.on(
								'change',
								function() {
									//alert('slidYes');

									var isRandom = $(
											'input[name=randomQuestion]:checked')
											.val();
									var selected = $('#testType').val();

									if (isRandom == 'Y' && selected == 'Mix') {

										$('#specifyQuestion').show();

										$("#MaxYes").prop('required', true);

										/* $('#mixQuestions').show();
										
										$("#maxQuestnToShow1").prop('required', true);
										
										$("#maxQuestnToShow2").prop('required', true);
										
										$("#maxQuestnToShow3").prop('required', true);
										
										$("#maxQuestnToShow4").prop('required', true); */
									}
									if (selected != 'Mix') {

										$('#specifyQuestion').hide();

										$("#MaxYes").prop('required', false);

										$('input:radio[name=specifyMaxQue]')
												.each(
														function() {
															$(this).prop(
																	'checked',
																	false);
														});

										$('#mixQuestions').hide();

										$("#maxQuestnToShow1").prop('required',
												false);

										$("#maxQuestnToShow2").prop('required',
												false);

										$("#maxQuestnToShow3").prop('required',
												false);

										$("#maxQuestnToShow4").prop('required',
												false);
									}

								});

			});
		</script>


		<script>
			$(function() {

				$('#setPasswordY').on('click', function() {
					//alert('slidYes');
					$('#passwordForTest').show();

					$("#passwordForTestText").prop('required', true);

				});

				$('#setPasswordN').on('click', function() {
					//alert('slidNo');
					$('#passwordForTest').hide();

					$("#passwordForTestText").prop('required', false);

				});

			});
		</script>


		<script>
			$(document)
					.ready(
							function() {

								var course = '${courseIdVal}';
								var username = $('#username').val();

								$(".facultyparameter")
										.on(
												'change',
												function() {
													var courseid = $(
															'#idOfCourse')
															.val();
													console.log("called");
													var acadMonth = $(
															'#acadMonth').val();
													var acadYear = $(
															'#acadYear').val();
													console.log(courseid);
													if (courseid) {
														$
																.ajax({
																	type : 'GET',
																	url : '${pageContext.request.contextPath}/getFacultyByCourseForFeedback?'
																			+ 'courseId='
																			+ courseid,
																	success : function(
																			data) {
																		console
																				.log("sucess");
																		console
																				.log(data);

																		var json = JSON
																				.parse(data);
																		var optionsAsString = "";

																		$(
																				'#facultyid')
																				.find(
																						'option')
																				.remove();
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
																				if (username == idjson[key]) {
																					optionsAsString += "<option value='" +key + "' selected>"
																							+ idjson[key]
																							+ "</option>";

																				} else {
																					console
																							.log('else entered00');
																					optionsAsString += "<option value='" +key + "'>"
																							+ idjson[key]
																							+ "</option>";
																				}
																			}
																		}
																		console
																				.log("optionsAsString"
																						+ optionsAsString);

																		$(
																				'#facultyid')
																				.append(
																						optionsAsString);

																	}
																});
													} else {
														console
																.log("no course");
													}
												});

								//------------------------------------ side bar course dependent------------------------------------
								console.log(course);
								$(".facultyparameter")
										.on(
												'change',
												function() {

													var acadMonth = $(
															'#acadMonth').val();
													var acadYear = $(
															'#acadYear').val();

													if (course && acadMonth
															&& acadYear) {
														$
																.ajax({
																	type : 'GET',
																	url : '${pageContext.request.contextPath}/getFacultyByCourseForFeedback?'
																			+ 'courseId='
																			+ course,
																	success : function(
																			data) {
																		var json = JSON
																				.parse(data);
																		var optionsAsString = "";

																		$(
																				'#facultyid')
																				.find(
																						'option')
																				.remove();
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
																				if (username == idjson[key]) {
																					optionsAsString += "<option value='" +key + "' selected>"
																							+ idjson[key]
																							+ "</option>";

																				} else {
																					console
																							.log('else entered00');
																					optionsAsString += "<option value='" +key + "'>"
																							+ idjson[key]
																							+ "</option>";
																				}
																			}
																		}
																		console
																				.log("optionsAsString"
																						+ optionsAsString);

																		$(
																				'#facultyid')
																				.append(
																						optionsAsString);

																	}
																});
													} else {
														console
																.log("no course");
													}

												});
								$('#idOfCourse').trigger('change');

							});
			//------------------------------------ side bar course dependent------------------------------------

			var TommorowDate = new Date();
			TommorowDate.setDate(TommorowDate.getDate() + 1);

			$("#datetimepicker1").on("dp.change", function(e) {

				validDateTimepicks();
			}).datetimepicker({
				minDate : TommorowDate,
				useCurrent : false,
				format : 'YYYY-MM-DD HH:mm:ss'
			});

			$("#datetimepicker2").on("dp.change", function(e) {

				validDateTimepicks();
			}).datetimepicker({
				minDate : TommorowDate,
				useCurrent : false,
				format : 'YYYY-MM-DD HH:mm:ss'
			});

			function validDateTimepicks() {
				if (($('#startDate').val() != '' && $('#startDate').val() != null)
						&& ($('#endDate').val() != '' && $('#endDate').val() != null)) {
					var fromDate = $('#startDate').val();
					var toDate = $('#endDate').val();
					var eDate = new Date(fromDate);
					var sDate = new Date(toDate);
					if (sDate < eDate) {
						alert("endate cannot be smaller than startDate");
						$('#startDate').val("");
						$('#endDate').val("");
					}
				}
			}
		</script>
