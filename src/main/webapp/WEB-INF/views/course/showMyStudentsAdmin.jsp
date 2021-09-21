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

    <div class="d-flex adminPage tabPage dataTableBottom" id="adminPage">
    <jsp:include page="../common/newAdminLeftNavBar.jsp"/>
    <jsp:include page="../common/rightSidebarAdmin.jsp" />

    

    <!-- DASHBOARD BODY STARTS HERE -->

    <div class="container-fluid m-0 p-0 dashboardWraper">

        <jsp:include page="../common/newAdminTopHeader.jsp" />

                <!-- SEMESTER CONTENT -->    
                        <div class="col-lg-12 col-md-12 col-sm-12 dashboardBody p-5">
                        
       
          <!-- page content: START -->
          
                        <nav aria-label="breadcrumb">
                            <ol class="breadcrumb">
                                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/homepage">Dashboard</a></li>
                                <li class="breadcrumb-item" aria-current="page"><c:out value="${Program_Name}" /></li>
                                <sec:authorize access="hasRole('ROLE_STUDENT')">
								<c:out value="${AcadSession}" />
							</sec:authorize>
                                <li class="breadcrumb-item active" aria-current="page"> My Course Students </li>
                            </ol>
                        </nav>
                        
						<jsp:include page="../common/alert.jsp" />
						
						
						<!-- Input Form Panel -->
						<div class="card bg-white border">
						<div class="card-body">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">

									<div class="x_title">
										<h5>My Course Students for ${userCourse.courseName }</h5>
										
									</div>

									<div class="x_content">
										<form:form action="" method="post" modelAttribute="userCourse"
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
																<form:option value="${course.id}" title="${course.courseName}">${course.courseName}(${course.acadYear}-${course.acadSession})</form:option>
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
										<h5>Students | ${fn:length(students)} Records found</h5>
										
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
										<form:form action="" method="post" modelAttribute="userCourse"
											enctype="multipart/form-data">
											<form:input path="courseId" type="hidden" />
											<c:if test="${allCampuses.size() > 0}">
												<div class="row">
													<div class="col-md-4 col-sm-6 col-xs-12 column">
														<div class="form-group">
															<form:label path="campusId" for="campusId">Select Campus</form:label>

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
												<a id="dlink" style="display: none;"></a> <input
													type="button" class="btn btn-success float-right" role="button"
													onclick="tableToExcel('example', '${course.courseName }', 'My Course Students for ${course.courseName }.xls')"
													value="Export to Excel">
											</c:if>

											<div class="table-responsive pb-3">
												<table id="example" class="table table-hover">
													<thead>
														<tr>
															<th>Sr. No.</th>
															<th>Picture</th>
															<th>Roll No. <i class="fa fa-sort"
																aria-hidden="true" style="cursor: pointer"></i></th>

															<th>SAP IDs <i class="fa fa-sort" aria-hidden="true"
																style="cursor: pointer"></i></th>
															<th>Student Name <i class="fa fa-sort"
																aria-hidden="true" style="cursor: pointer"></i></th>
															<th>Email Id <i class="fa fa-sort"
																aria-hidden="true" style="cursor: pointer"></i></th>

															<th>Contact No. <i class="fa fa-sort"
																aria-hidden="true" style="cursor: pointer"></i></th>

															<th>Session <i class="fa fa-sort" aria-hidden="true"
																style="cursor: pointer"></i></th>

															<th>Campus<i class="fa fa-sort" aria-hidden="true"
																style="cursor: pointer"></i></th>
														</tr>
													</thead>
													<tfoot>
														<tr>
															<th></th>
															<th>Picture</th>
															<th>Roll No.</th>
															<th>SAP IDs</th>
															<th>Student Name</th>
															<th>Email Id</th>
															<th>Contact No.</th>
															<th>Session</th>
															<th>Campus</th>
														</tr>
													</tfoot>
													<tbody>

														<c:forEach var="student" items="${students}"
															varStatus="status">
															<tr>
																<td><c:out value="${status.count}" /></td>
																<td><img
																	src="${pageContext.request.contextPath}/savedImages/${student.username}.JPG"
																	alt="No image"
																	onerror="this.src='<c:url value="/resources/images/download.png" />'"
																	></td>


																<td><c:out value="${student.rollNo}" /></td>
																<td><c:out value="${student.username}" /></td>
																<td><c:out
																		value="${student.firstname} ${student.lastname}" /></td>
																<td><c:out value="${student.email}" /></td>
																<td><c:out value="${student.mobile}" /></td>
																<td><c:out value="${student.acadSession}" /></td>
																<td><c:out value="${student.campusName}" /></td>
															</tr>
														</c:forEach>
													</tbody>
												</table>
												
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

                <!-- SIDEBAR END -->
        <jsp:include page="../common/newAdminFooter.jsp"/>
        
        <script type="text/javascript"
		src="${pageContext.request.contextPath}/resources/js/select2.min.js"></script>


	<script type="text/javascript">
		$(document).ready(function() {

			// Initialize select2
			$("#courseIdForForum").select2();
		});
	</script>
<script>
				$("#courseIdForForum")
						.on(
								'change',
								function() {

									//alert("Onchange Function called!");
									var selectedValue = $(this).val();
									window.location = '${pageContext.request.contextPath}/showMyCourseStudents?courseId='
											+ encodeURIComponent(selectedValue);
									return false;
								});

				$("#campusId")
						.on(
								'change',
								function() {

									//alert("Onchange Function called!");
									var selectedValue = $(this).val();
									var id = $(courseIdForForum).val();
									window.location = '${pageContext.request.contextPath}/showMyCourseStudents?courseId='
											+ id + '&campusId=' + selectedValue;
									return false;
								});
			</script>


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
