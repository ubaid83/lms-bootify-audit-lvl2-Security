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
                                <li class="breadcrumb-item active" aria-current="page"> My Attendance</li>
                            </ol>
                        </nav>
                        
						<jsp:include page="../common/alert.jsp" />
						
						
						<!-- Input Form Panel -->
						<div class="card bg-white border">
						<div class="card-body">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">

									<div class="x_title">
										<h2>My Attendance for ${courseName}</h2>
										<ul class="nav navbar-right panel_toolbox">
											<li><a class="collapse-link"><i
													class="fa fa-chevron-up"></i></a></li>
										</ul>
										<div class="clearfix"></div>
									</div>

									<div class="x_content">

										<div class="form-group">
											<div class="col-sm-6 col-md-4 col-xs-12 column">
												<form:label path="startDate" for="startDate">Display From <span style="color: red">*</span></form:label>

												<div class='input-group date' id='datetimepicker1'>
													<form:input id="startDate" path="startDate" type="text"
														placeholder="Start Date" class="form-control"
														required="required" readonly="true" />

													<span class="input-group-addon"><span
														class="glyphicon glyphicon-calendar"></span> </span>
												</div>


											</div>
										</div>

										<%-- <form:form action="" method="post"
											modelAttribute="studentAttendance"
											enctype="multipart/form-data">


											<form:input path="courseId" type="hidden" />
											<div class="row">
												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<form:label path="courseId" for="courseId">Course</form:label>
														<form:select id="courseIdForForum" path="courseId"
															class="form-control">
															<form:option value="">Select Course</form:option>
															<c:forEach var="course" items="${allCourses}"
																varStatus="status">
																<form:option value="${course.id}">${course.courseName}</form:option>
															</c:forEach>
														</form:select>
													</div>
												</div>
											</div>
										</form:form> --%>
									</div>
								</div>
							</div>
						</div>
						</div>

						<!-- Results Panel -->

						
								<div class="card bg-white border">
								<div class="card-body">								
									<div class="col-xs-12 col-sm-12">
								<div class="x_panel">
									<div class="x_title">
										<h2>Student Attendance |
											${fn:length(studentAttendanceList)} Records found</h2>
										<ul class="nav navbar-right panel_toolbox">
											<li><a class="collapse-link"><i
													class="fa fa-chevron-up"></i></a></li>
											<li><a class="close-link"><i class="fa fa-close"></i></a></li>
										</ul>
										<div class="clearfix"></div>
									</div>
									<div class="x_itemCount" style="display: none;">
										<div class="image_not_found">
											<i class="fa fa-newspaper-o"></i>
											<p>
												<label class=""></label>${fn:length(studentAttendanceList)}
												Student Attendances
											</p>
										</div>
									</div>
									<div class="x_content">
										<form:form action="" method="post"
											modelAttribute="studentAttendance"
											enctype="multipart/form-data">
											<form:input path="courseId" type="hidden" />
											<div class="table-responsive">
												<table id="showStudentAttendances"
													class="table  table-hover">
													<thead>
														<tr>
															<th>Sr.No</th>
															<th>Student Name<i class="fa fa-sort"
																aria-hidden="true" style="cursor: pointer"></i></th>

															<th>In Time<i class="fa fa-sort" aria-hidden="true"
																style="cursor: pointer"></i></th>
															<th>Out Time<i class="fa fa-sort" aria-hidden="true"
																style="cursor: pointer"></i></th>
															<!-- <th>Start Time<i class="fa fa-sort"
																aria-hidden="true" style="cursor: pointer"></i></th>

															<th>End Time<i class="fa fa-sort" aria-hidden="true"
																style="cursor: pointer"></i></th>

															<th>Absence Reason<i class="fa fa-sort"
																aria-hidden="true" style="cursor: pointer"></i></th>
 -->
														</tr>
													</thead>
													<tfoot>
														<tr>
															<th></th>
															<th>Student Name</th>
															<th>In Time</th>
															<th>Out Time</th>
															<!-- <th>Start Time</th>
															<th>End Time</th>

															<th>Absence Reason</th> -->

														</tr>
													</tfoot>
													<tbody>

														<%-- <c:forEach var="student" items="${studentAttendanceList}"
															varStatus="status">
															<tr>
																<td><c:out value="${status.count}" /></td>



																<td><c:out value="${student.courseName}" /></td>
																<td><c:out value="${student.isPresent}" /></td>
																<td><c:out value="${student.classDate}" /></td>
																<td><c:out value="${student.startTime}" /></td>
																<td><c:out value="${student.endTime}" /></td>

																<c:if test="${student.isPresent eq 'Absent'}">
																	<td><c:out value="${student.absence_reason_code}" /></td>
																</c:if>

																<c:if test="${student.isPresent eq 'Present'}">
																	<td><c:out value="NA" /></td>
																</c:if>
															</tr>
														</c:forEach> --%>
													</tbody>
												</table>
												<br>
											</div>
										</form:form>
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
			
			<!-- SIDEBAR START -->
	         <jsp:include page="../common/newSidebar.jsp" />
	        <!-- SIDEBAR END -->
	<jsp:include page="../common/footer.jsp"/>

	<script>
				$("#courseIdForForum")
						.on(
								'change',
								function() {

									//alert("Onchange Function called!");
									var selectedValue = $(this).val();
									window.location = '${pageContext.request.contextPath}/searchStudentAttendance?courseId='
											+ encodeURIComponent(selectedValue);
									return false;
								});
				$(document)
						.ready(
								function() {
									function ajaxData(){
									$
											.ajax({
												type : 'POST',
												url : 'http://103.19.199.30:8080/StudentAttendanceByParent/studentAttendanceByDateForParent?'
														+ 'studentUsername='
														+ studentTestId
														+ '&dateParam='
														+  $("#datetimepicker1").val(),
												success : function(data) {

													console
															.log("data is ------------"
																	+ data);
												},
												error : function(result) {

												}
											});
									}
									
									
								     $("#datetimepicker1").on("dp.change", function(e) {
							            	
								    	 ajaxData();
							            }).datetimepicker({
							            	// minDate:new Date(),
							           	  useCurrent: false,
							           	  format: 'YYYY-MM-DD HH:mm:ss'
							            });

							        
							
							
							

								});
			</script>