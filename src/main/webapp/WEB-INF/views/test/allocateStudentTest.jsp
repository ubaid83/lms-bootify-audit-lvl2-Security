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
                                <li class="breadcrumb-item active" aria-current="page">Allocate Test</li>
                            </ol>
                        </nav>
                        
						<jsp:include page="../common/alert.jsp" />
						
						
						<!-- Input Form Panel -->
						<div class="card bg-white border">
						<div class="card-body">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">

									<div class="x_title">
										<h5>Allocate Test</h5>
										
									</div>

									<div class="x_content">
										<form:form id="studentTestForm"
											action="allocateStudentTestForm" method="post"
											modelAttribute="test">
											<div class="row page-body">
												<div class="col-sm-12 column">

													<div class="row">
														<div class="col-sm-6 col-md-4 col-xs-12 column">
															<div class="form-group">
																<label class="control-label" for="test">Test <span style="color: red">*</span></label>
																<form:select id="test" path="id" placeholder="Test Name"
																	class="form-control" required="required">
																	<form:option value="">Select Test</form:option>
																	<form:options items="${TestList}" itemLabel="testName"
																		itemValue="id" />
																</form:select>
															</div>
														</div>
														<div class="col-sm-6 col-md-4 col-xs-12 column">
															<div class="form-group">
																<label class="control-label" for="course">Course <span style="color: red">*</span></label>
																<form:select id="course" path="courseId"
																	placeholder="Course"
																	class="form-control facultyparameter"
																	required="required">
																	<form:option value="">Select Course</form:option>
																	<form:options items="${courseList}"
																		itemLabel="courseName" itemValue="id" />
																</form:select>
															</div>
														</div>

														<%-- <div class="col-sm-6 col-md-4 col-xs-12 column">
								<div class="form-group">
									<form:label path="acadMonth" for="acadMonth">Academic Month</form:label>
									<form:select id="acadMonth" path="acadMonth"
										class="form-control facultyparameter" required="required">
										<form:option value="">Select Academic Month</form:option>
										<form:options items="${acadMonths}" />
									</form:select>
								</div>
							</div>
							<div class="col-sm-6 col-md-4 col-xs-12 column">
								<div class="form-group">
									<form:label path="acadYear" for="acadYear">Academic Year</form:label>
									<form:select id="acadYear" path="acadYear" class="form-control facultyparameter"
										required="required">
										<form:option value="">Select Academic Year</form:option>
										<form:options items="${acadYears}" />
									</form:select>
								</div>
							</div> --%>
														<%-- 	<div class="col-sm-6 col-md-4 col-xs-12 column">
								<div class="form-group">
									<form:label path="facultyId" for="facultyId">Faculty</form:label>
									 <form:select id="facultyid" path="facultyId" class="form-control">
									 					<c:if test="${empty test.facultyId }">
                                                      <form:option value="">Select Faculty</form:option>
                                                      </c:if>
                                                      <c:if test="${not empty test.facultyId }">
                                                     	<form:option value="${test.facultyId }">${test.facultyId }</form:option>
                                                      </c:if>
                                                </form:select>
								</div>
							</div> --%>
														<div class="col-sm-6 col-md-4 col-xs-12 column">
															<div class="form-group">
																<form:label path="facultyId" for="facultyId">Faculty</form:label>
																<form:select id="facultyid" path="facultyId"
																	class="form-control">
																	<c:if test="${empty test.facultyId }">
																		<form:option value="">Select Faculty</form:option>
																	</c:if>
																	<c:if test="${not empty test.facultyId }">
																		<form:option value="${test.facultyId }">${test.facultyId }</form:option>
																	</c:if>
																</form:select>
															</div>
														</div>
														<!-- <div class="col-sm-6 col-md-4 col-xs-12 column">
								<div class="form-group">
									<form:label path="facultyId" for="faculty">Faculty</form:label>
									<select id="faculty" name="facultyId" class="form-control"
										required="required" data-value="${feedback.facultyId}">
										<option value="">Select Faculty</option>
									</select>
								</div>
							</div>-->
														<div class="col-sm-6 col-md-4 col-xs-12 column">
															<div class="form-group">
																<form:label path="startDate" for="startDate">Start Date <span style="color: red">*</span></form:label>
																	
																	<div class='input-group date' id='datetimepicker1' >
															<form:input id="startDate" path="startDate" type="text"
																placeholder="Start Date" class="form-control"
																required="required" readonly="true"/>
															<span class="input-group-addon"><span
																class="glyphicon glyphicon-calendar"></span> </span>
														</div>
																	
															</div>
														</div>
														<div class="col-sm-6 col-md-4 col-xs-12 column">
															<div class="form-group">
																<form:label path="endDate" for="endDate">End Date <span style="color: red">*</span></form:label>
															
																	
																	<div class='input-group date' id='datetimepicker2'>
															<form:input id="endDate" path="endDate" type="text"
																placeholder="End Date" class="form-control"
																required="required" readonly="true"/>
															<span class="input-group-addon"><span
																class="glyphicon glyphicon-calendar"></span> </span>
														</div>
																	
															</div>
														</div>

														<div class="clearfix"></div>



													</div>
													<div class="row">
														<div class="col-sm-6 col-md-4 col-xs-12 column">
															<div class="form-group">

																<button id="search" class="btn btn-large btn-primary"
																	formaction="allocateStudentTestForm">Search</button>
																<button id="cancel" class="btn btn-danger"
																	formaction="homepage" formnovalidate="formnovalidate">Cancel</button>
															</div>
														</div>
													</div>

												</div>
											</div>
										</form:form>
									</div>
								</div>
							</div>
						</div>
						</div>

						<!-- Results Panel -->

								<sec:authorize access="hasRole('ROLE_FACULTY')">
								<div class="card bg-white border">
								<div class="card-body">								
									<div class="col-xs-12 col-sm-12">
									<div class="x_panel">
										<div class="x_title">
											<h5>Students | ${studentList.size()} Records Found | Test allocated to : ${noOfStudentAllocated} Students</h5>
										
										</div>
										<div class="x_content">
											<form:form action="saveStudentTest" id="saveStudentTest"
												method="post" modelAttribute="test">
												
													<form:input path="id" type="hidden" />
													<form:input path="acadMonth" type="hidden" />
													<form:input path="acadYear" type="hidden" />
													<form:input path="facultyId" type="hidden" />
													<form:input path="courseId" type="hidden"
														value="${courseId}" />
													<div class="table-responsive">
														<table class="table table-hover"
															 id="studentTestTable">
															<thead>
																<tr>
																	<th>Sr. No.</th>
																	<th>Select (<a onclick="checkAll()">All</a> | <a
																onclick="uncheckAll()">None</a>)
															</th>
																	<th>SAPID</th>
																	<th>Student Name</th>
																</tr>
															</thead>
															<tfoot>
																<tr>
																	<th></th>
																	<th></th>
																	<th>SAPID</th>
																	<th>Student Name</th>
																</tr>
															</tfoot>
															<tbody>

																<%-- <c:forEach var="student" items="${page.pageItems}" varStatus="status">
						        <tr>
						            <td><c:out value="${status.count}" /></td>
						            <td>
						            <c:if test="${empty student.id }">
						            	<form:checkbox path="students" value="${student.username}"/> 
						            </c:if>
						            <c:if test="${not empty student.id }">
						            	Test Allocated
						            </c:if>
						            </td>
									<td><c:out value="${student.programName}" /></td>
									<td><c:out value="${student.firstname} ${student.lastname}" /></td>
						        </tr>   
						    </c:forEach> --%>

																<c:forEach var="student" items="${studentList}"
																	varStatus="status">
																	<tr>
																		<td><c:out value="${status.count}" /></td>
																		<td><c:if test="${empty student.id }">
																				<form:checkbox path="students"
																					value="${student.username}" />
																			</c:if> <c:if test="${not empty student.id }">
						            	Test Allocated
						            </c:if></td>
																		<td><c:out value="${student.username}" /></td>
																		<td><c:out
																				value="${student.firstname} ${student.lastname}" /></td>
																	</tr>
																</c:forEach>

															</tbody>
														</table>
													</div>

													<div class="col-sm-6 col-md-4 col-xs-12 column">
														<div class="form-group">

															<button id="submit" class="btn btn-large btn-primary"
																formaction="saveStudentTest">Allocate Test</button>
																<button class="btn btn-large btn-primary"
																formaction="saveStudentTestAllStudents">Allocate Test to all Students</button>
															<button id="cancel" class="btn btn-danger"
																formaction="homepage" formnovalidate="formnovalidate">Cancel</button>
														</div>
													</div>

												
											</form:form>
										</div>
									</div>
								</div>
								</div>
								</div>
								</sec:authorize>
							

					<%-- 	<jsp:include page="../common/paginate.jsp">
							<jsp:param name="baseUrl" value="searchFacultyGroups" />
						</jsp:include> --%>

			<!-- /page content: END -->
                   
                    </div>
			
			<!-- SIDEBAR START -->
	         <jsp:include page="../common/newSidebar.jsp" />
	        <!-- SIDEBAR END -->
	<jsp:include page="../common/footer.jsp"/>

<script type="text/javascript" src="<c:url value="/resources/js/customDateTimePicker.js" />" ></script>
	<script>
		$(document)
				.ready(
						function() {

							/*  $("#search")
							 .on(
							             'click',
							             function() {
							            	 alert("hi");
							            	 $
							                 .ajax({
							                       type : 'POST',
							                       url : 'http://localhost:8084/addStudentFeedbackForm',
							                       success : function(
							                                   data) {
							                       	
							                            console.log("called"+data);

							                       }
							                 });
							            	 
							             }) */

							$(".facultyparameter")
									.on(
											'change',
											function() {
												var courseid = $('#course')
														.val();
												var acadMonth = $('#acadMonth')
														.val();
												var acadYear = $('#acadYear')
														.val();
												console.log(courseid);
												console.log(acadMonth);
												console.log(acadYear);
												if (courseid) {
													console.log("called")
													$
															.ajax({
																type : 'GET',
																url : '${pageContext.request.contextPath}/getFacultyByCourseForFeedback?'
																		+ 'courseId='
																		+ courseid,

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
																			optionsAsString += "<option value='" +key + "'>"
																					+ idjson[key]
																					+ "</option>";
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
													//  alert('Error no faculty');
												}
											});
							
							

						});
		
		$("#datetimepicker1").on("dp.change", function(e) {
			
			validDateTimepicks();
		}).datetimepicker({
			// minDate:new Date(),
			  useCurrent: false,
			  format: 'YYYY-MM-DD HH:mm:ss'
		});

		$("#datetimepicker2").on("dp.change", function(e) { 	
			
			validDateTimepicks();
		}).datetimepicker({
			// minDate:new Date(),
			  useCurrent: false,
			  format: 'YYYY-MM-DD HH:mm:ss'
			});
			

		function validDateTimepicks(){
			 if(($('#startDate').val() !='' && $('#startDate').val() !=null)&&($('#endDate').val() !='' && $('#endDate').val() !=null)){
			        var fromDate = $('#startDate').val();
			        var toDate = $('#endDate').val();
			        var eDate = new Date(fromDate);
			        var sDate = new Date(toDate);
			        if(sDate < eDate){
			        	alert("endate cannot be smaller than startDate");
			        	$('#startDate').val("");
			        	$('#endDate').val("");       	
			        }
			    } 
		}
		
		
	</script>	













