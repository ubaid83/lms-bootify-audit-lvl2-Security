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
                                <li class="breadcrumb-item active" aria-current="page">Mark Attendance</li>
                            </ol>
                        </nav>
                        
						<jsp:include page="../common/alert.jsp" />
						
						
						<!-- Input Form Panel -->
						<div class="card bg-white border">
						<div class="card-body">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">
									<div class="x_title">
										<h2>Mark Attendance for ${attendance.course.courseName }</h2>
										<ul class="nav navbar-right panel_toolbox">


											<li><a class="collapse-link"><i
													class="fa fa-chevron-up"></i></a></li>



										</ul>
										<div class="clearfix"></div>

									</div>


									<div class="x_content">

										<form:form action="" id="markAttendance" method="post"
											modelAttribute="attendance" enctype="multipart/form-data">

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

										</form:form>
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
										<%-- <h2>Students | ${fn:length(students)} Records found</h2> --%>
										<h2>
											Students | ${fn:length(students)} Records found

											<%-- <c:if test="${showWieghtageForCP}">
															Weight Assigned : <c:out value="${cpWieghtage}"></c:out>


											</c:if>
											<c:if test="${showWieghtageForCP eq 'false'}">
															
															Weight not assigned!
															</c:if> --%>

										</h2>
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
												<label class=""></label>${fn:length(students)} Students
											</p>
										</div>
									</div>
									<div class="x_content">
										<form:form action="markAttendance" method="post"
											modelAttribute="attendance">
											<form:input path="courseId" type="hidden" />
											<div class="row">

												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<form:label path="startTime" for="startTime">Start Time <span style="color: red">*</span></form:label>

														<div class='input-group date' id='datetimepicker1'>
															<form:input id="startTime" path="startTime" type="text"
																placeholder="Start Time" class="form-control"
																required="required" />
															<span class="input-group-addon"><span
																class="glyphicon glyphicon-calendar"></span> </span>
														</div>

													</div>
												</div>

												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<form:label path="endTime" for="endTime">End Time <span style="color: red">*</span></form:label>
														<%-- <form:input path="endDate" type="datetime-local"
																class="form-control" value="${assignment.endDate}"
																required="required" /> --%>

														<div class='input-group date' id='datetimepicker2'>
															<form:input id="endTime" path="endTime" type="text"
																placeholder="End Time" class="form-control"
																required="required" />
															<span class="input-group-addon"><span
																class="glyphicon glyphicon-calendar"></span> </span>
														</div>

													</div>
												</div>
											</div>
											<div class="table-responsive">
												<table class="table  table-hover" id="showMyStudents">
													<thead>
														<tr>
															<th>Sr. No.</th>


															<th>SAP IDs <i class="fa fa-sort" aria-hidden="true"
																style="cursor: pointer"></i></th>
															<th>Student Name <i class="fa fa-sort"
																aria-hidden="true" style="cursor: pointer"></i></th>
															<th>Roll No. <i class="fa fa-sort"
																aria-hidden="true" style="cursor: pointer"></i></th>
															<th>Select Status <span style="color: red">*</span></th>
															<th>Reason (Optional)</th>

														</tr>
													</thead>
													<tfoot>
														<tr>
															<th></th>

															<th>SAP IDs</th>
															<th>Roll No.</th>
															<th>Student Name</th>
														</tr>
													</tfoot>
													<tbody>

														<c:forEach var="student" items="${students}"
															varStatus="status">
															<tr>
																<td><c:out value="${status.count}" /></td>


																<td><c:out value="${student.username}" /></td>
																<td><c:out
																		value="${student.firstname} ${student.lastname}" /></td>
																<td><c:out value="${student.rollNo}" /></td>
																<%-- <td><sec:authorize access="hasRole('ROLE_FACULTY')">
																		<c:if test="${student.status eq 'P'}">
																		Present

																		</c:if>

																		<c:if test="${student.status ne 'P'}">
																			<form:checkbox path="students"
																				value="${student.username}" />
																				Absent / Not Marked
																		</c:if>
																	</sec:authorize> <sec:authorize access="hasRole('ROLE_DEAN')">
																		<c:if test="${ empty student.id }">
																			<form:checkbox path="students"
																				value="${student.username}" />
																		</c:if>

																		<c:if test="${not empty student.id }">
													            	Assignment Allocated
													            </c:if>
																	</sec:authorize></td> --%>

																<%-- 	<td><form:checkbox path="status" name="status[]"
																		class="form-control" value="P" 
																		data-value="${student.status}" data-toggle="toggle"
																		data-on="P" data-off="A" data-onstyle="success"
																		data-offstyle="danger" data-style="ios"
																		data-size="mini" /></td> --%>
																<!--  -->

																<td><c:if test="${student.status eq null }">
																		<form:select class="form-control" id="status"
																			path="status" placeholder="Attendance Status"
																			required="required">
																			<form:option value="Present" selected="true">Present </form:option>
																			<form:option value="Absent">Absent</form:option>
																			<form:option value="Exemption">Exemption</form:option>
																			<form:option value="tardy">tardy</form:option>
																		</form:select>

																	</c:if> <c:if test="${student.status ne null }">


																		<c:if test="${student.status eq 'Present' }">
																			<form:select class="form-control" id="status"
																				style="color : green;" path="status"
																				placeholder="Attendance Status" required="required">
																				<form:option value="${student.status}"
																					selected="true">${student.status} </form:option>
																				<form:option value="Absent">Absent</form:option>
																				<form:option value="Exemption">Exemption</form:option>
																				<form:option value="tardy">tardy</form:option>
																			</form:select>
																		</c:if>
																		<c:if test="${student.status eq 'Absent' }">
																			<form:select class="form-control" id="status"
																				style="color : red;" path="status"
																				placeholder="Attendance Status" required="required">
																				<form:option value="${student.status}"
																					selected="true">${student.status} </form:option>
																				<form:option value="Present">Present</form:option>
																				<form:option value="Exemption">Exemption</form:option>
																				<form:option value="tardy">tardy</form:option>
																			</form:select>
																		</c:if>
																		<c:if test="${student.status eq 'Exemption' }">
																			<form:select class="form-control" id="status"
																				style="color : blue;" path="status"
																				placeholder="Attendance Status" required="required">
																				<form:option value="${student.status}"
																					selected="true">${student.status} </form:option>
																				<form:option value="Present">Present</form:option>
																				<form:option value="Absent">Absent</form:option>
																				<form:option value="tardy">tardy</form:option>
																			</form:select>
																		</c:if>
																		<c:if test="${student.status eq 'tardy' }">
																			<form:select class="form-control" id="status"
																				style="color : black;" path="status"
																				placeholder="Attendance Status" required="required">
																				<form:option value="${student.status}"
																					selected="true">${student.status} </form:option>
																				<form:option value="Present">Present</form:option>
																				<form:option value="Absent">Absent</form:option>
																				<form:option value="Exemption">Exemption</form:option>
																			</form:select>
																		</c:if>


																	</c:if></td>
																<td><form:input type="text" path="reason"
																		value="${student.reason}"></form:input></td>

															</tr>
														</c:forEach>
													</tbody>
												</table>
												<br> <a id="dlink" style="display: none;"></a> <input
													type="button" class="btn btn-primary" role="button"
													onclick="tableToExcel('showMyStudents', '${attendance.courseId}', 'Attendance for ${attendance.course.courseName }.xls')"
													value="Export to Excel">
											</div>
											<div class="clearfix"></div>
											<div class="col-md-4 col-sm-6 col-xs-12 column">
												<div class="form-group">

													<button id="submit" class="btn btn-large btn-primary"
														onclick="return clicked();"
														formaction="saveStudentCourseAttendance">Mark
														Attendance</button>


													<button id="cancel" class="btn btn-large btn-danger"
														formaction="homepage" formnovalidate="formnovalidate">Cancel</button>
												</div>
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
<script type="text/javascript">
				var tableToExcel = (function() {
					var uri = 'data:application/vnd.ms-excel;base64,', template = '<html xmlns:o="urn:schemas-microsoft-com:office:office" xmlns:x="urn:schemas-microsoft-com:office:excel" xmlns="http://www.w3.org/TR/REC-html40"><head><!--[if gte mso 9]><xml><x:ExcelWorkbook><x:ExcelWorksheets><x:ExcelWorksheet><x:Name>{worksheet}</x:Name><x:WorksheetOptions><x:DisplayGridlines/></x:WorksheetOptions></x:ExcelWorksheet></x:ExcelWorksheets></x:ExcelWorkbook></xml><![endif]--></head><body><table>{table}</table></body></html>', base64 = function(
							s) {
						return window.btoa(unescape(encodeURIComponent(s)))
					}, format = function(s, c) {
						return s.replace(/{(\w+)}/g, function(m, p) {
							return c[p];
						})
					}
					return function(table, name, filename) {
						if (!table.nodeType)
							table = document.getElementById(table)
						var ctx = {
							worksheet : name || 'Worksheet',
							table : table.innerHTML
						};
						// window.location.href = uri + base64(format(template, ctx));

						document.getElementById("dlink").href = uri
								+ base64(format(template, ctx));
						document.getElementById("dlink").download = filename;
						document.getElementById("dlink").click();
					}
				})()
			</script>
			<script>
				$(document)
						.ready(
								function() {
									$("#datetimepicker1").on("dp.change",
											function(e) {

												validDateTimepicks();
											}).datetimepicker({
										minDate : new Date(),
										useCurrent : false,
										format : 'YYYY-MM-DD HH:mm'
									});

									$("#datetimepicker2").on("dp.change",
											function(e) {

												validDateTimepicks();
											}).datetimepicker({
										minDate : new Date(),
										useCurrent : false,
										format : 'YYYY-MM-DD HH:mm'
									});

									function validDateTimepicks() {
										if (($('#startDate').val() != '' && $(
												'#startDate').val() != null)
												&& ($('#endDate').val() != '' && $(
														'#endDate').val() != null)) {
											var fromDate = $('#startDate')
													.val();
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
								});
			</script>



			<script>
				$("#courseIdForForum")
						.on(
								'change',
								function() {

									//alert("Onchange Function called!");
									var selectedValue = $(this).val();
									window.location = '${pageContext.request.contextPath}/markAttendanceForm?courseId='
											+ encodeURIComponent(selectedValue);
									return false;
								});
			</script>
	













