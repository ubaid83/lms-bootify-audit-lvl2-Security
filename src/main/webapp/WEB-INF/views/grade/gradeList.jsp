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

<div class="d-flex dataTableBottom" id="">
	<jsp:include page="../common/newLeftNavBarFaculty.jsp" />
	<jsp:include page="../common/newLeftSidebarFaculty.jsp" />
	<jsp:include page="../common/rightSidebarFaculty.jsp" />

	<!-- DASHBOARD BODY STARTS HERE -->

		<jsp:include page="../common/newTopHeaderFaculty.jsp" />

		<div class="container mt-5">
			<div class="row">
				<!-- SEMESTER CONTENT -->
				<div
					class="col-lg-9 col-md-9 col-sm-12 dashboardBody ml-auto mr-auto">

					<!-- page content: START -->

					<nav aria-label="breadcrumb">
						<ol class="breadcrumb">
							<li class="breadcrumb-item"><a
								href="${pageContext.request.contextPath}/homepage">Dashboard</a></li>
							<li class="breadcrumb-item" aria-current="page"><c:out
									value="${Program_Name}" /></li>
							<sec:authorize access="hasRole('ROLE_STUDENT')">
								<c:out value="${AcadSession}" />
							</sec:authorize>
							<li class="breadcrumb-item active" aria-current="page">Grade
								List For Course</li>
						</ol>
					</nav>

					<jsp:include page="../common/alert.jsp" />


					<!-- Input Form Panel -->
					<div class="card bg-white border">
						<div class="card-body">

							<div class="col-md-12 col-xs-12 col-sm-12">
								<div class="x_panel">

									<div class="x_title">
										<h5>Grade Center For ${course.courseName }</h5>

									</div>


									<div class="x_content">
										<form>
											<div class="row">
												<div class="col-md-4 col-sm-6 col-xs-12">
													<div class="form-group">

														<label for="courseId">Course</label> 
														<select id="courseId"
															name="courseId" class="form-control">
															<c:if test="${cId eq  null }">
																<option value="">Select Course</option>
															</c:if>
															<c:forEach var="course" items="${listOfCoursesAssigned}"
																varStatus="status">
																<c:if test="${cId eq course.id }">
																	<option value="${course.id}" selected>${course.courseName}</option>
																</c:if>
																<c:if test="${cId ne course.id }">
																	<option value="${course.id}">${course.courseName}</option>
																</c:if>
															</c:forEach>
														</select>
													</div>
												</div>
												<div class="col-md-8 col-xs-12 text-right">
													NA: Not Allocated,<br />NC: Not Completed,<br />NE: Not
													Evaluated,<br />ND : Not Declared,<br />F: FAIL,<br />P:
													PASS
												</div>
											</div>

										</form>
									</div>
								</div>
							</div>

						</div>
					</div>

					<!-- Results Panel -->

					<c:if test="${showGradeCenterList eq true}">
						<%-- <div class="card bg-white border">
							<div class="card-body">
								<section class="searchAssign card bg-white border">
									<div class="card-body">
										<div class="col-12 bg-dark text-white">
											<h6 class="p-2 mb-3 text-center">TABULAR ASSIGNMENTS
												REPORT</h6>
										</div>
										<div class="row">
											<div class="col-3">
												<div class="input-group flex-nowrap input-group-sm">
													<div class="input-group-prepend">
														<span class="input-group-text cust-select-span"
															id="addon-wrapping">Sort By</span>
													</div>

													<select class="cust-select">
														<option>Latest</option>
														<option>Oldest</option>
														<option>All</option>
													</select>
												</div>

											</div>

											<nav class="col-9">
												<ul class="pagination float-right">
													<button class="form-control text-white exprtExcel">Export
														To Exel</button>
												</ul>
											</nav>
										</div>
										<div class="table-responsive mb-3 testAssignTable">
											<table class="table table-striped table-hover">
												<thead>
													<tr>
														<th scope="col">No.</th>

														<th scope="col">Student's Name</th>
														<th scope="col">SAPID</th>
														<th scope="col">ROLL-NO</th>
														<c:forEach items="${assignments}" var="assignment"
															varStatus="status">
															<th scope="col">${assignment}</th>
														</c:forEach>

													</tr>
												</thead>
												<tbody>
													<c:forEach var="student" items="${students}"
														varStatus="status">
														<tr>
															<td><div class="text-nowrap">${status.index+1}</div></td>
															<td><div class="text-nowrap">${student.key}</div></td>
															<td><c:out value="${usernameList[student.key]}" /></td>
															<td><c:out value="${rollNoList[student.key]}" /></td>
															<c:forEach var="studentAssignment" items="${assignments}">
																<c:set var="myParam" value="${studentAssignment}" />
																<td class="text-center"><div>
																		<c:out
																			value="${student.value.assigmentToScore[myParam]}" />
																	</div></td>
															</c:forEach>
														<tr>
													</c:forEach>




												</tbody>
											</table>
										</div>

									</div>

								</section>

							</div>
						</div>

						<div class="card bg-white border">
							<div class="card-body">
								<section class="searchAssign mt-5 card bg-white border"
									id="testTable">
									<div class="card-body">
										<div class="col-12 bg-dark text-white">
											<h6 class="p-2 mb-3 text-center">TABULAR TEST REPORT</h6>
										</div>



										<div class="row">
											<div class="col-3">
												<div class="input-group flex-nowrap input-group-sm">
													<div class="input-group-prepend">
														<span class="input-group-text cust-select-span"
															id="addon-wrapping">Sort By</span>
													</div>

													<select class="cust-select">
														<option>Latest</option>
														<option>Oldest</option>
														<option>All</option>
													</select>
												</div>

											</div>

											<nav class="col-9">
												<ul class="pagination float-right">
													<button class="form-control text-white exprtExcel">Export
														To Exel</button>
												</ul>
											</nav>
										</div>




										<div class="table-responsive mb-3 testAssignTable">
											<table class="table table-striped table-hover">
												<thead>
													<tr>
														<th scope="col">No.</th>

														<th scope="col">Student's Name</th>
														<th scope="col">SAPID</th>
														<th scope="col">ROLL-NO</th>

														<c:forEach items="${tests}" var="test">
															<th scope="col"><div>${test}</div></th>
															<th scope="col">status</th>
														</c:forEach>

													</tr>
												</thead>
												<tbody>
													<!-- <tr>
													<th scope="row">1</th>
													<td>Test Name One</td>
													<td>Kapil Sharma</td>
													<td>12/02/2019</td>
													<td>16/02/2019</td>
													<td>50</td>
													<td>50</td>
												</tr> -->

													<c:forEach var="student" items="${students}"
														varStatus="status">
														<tr>
															<td><div class="text-nowrap">${status.index+1}</div></td>
															<td><div class="text-nowrap">${student.key}</div></td>
															<td><c:out value="${usernameList[student.key]}" /></td>
															<td><c:out value="${rollNoList[student.key]}" /></td>
															<c:forEach var="studentTest" items="${tests}">
																<c:set var="myParam" value="${studentTest}" />
																<td class="text-center"><div>
																		<c:out value="${student.value.testToScore[myParam]}" />
																	</div></td>
																<td class="text-center"><div>
																		<c:out value="${student.value.statusMap[myParam]}" />
																	</div></td>
															</c:forEach>
														<tr>
													</c:forEach>


												</tbody>
											</table>
										</div>

									</div>
								</section>

							</div>
						</div>

						<div class="card bg-white border">
							<div class="card-body">
								<section class="searchAssign mt-5 card bg-white border"
									id="testTable">
									<div class="card-body">
										<div class="col-12 bg-dark text-white">
											<h6 class="p-2 mb-3 text-center">TABULAR CLASS
												PARTICIPATION REPORT</h6>
										</div>



										<div class="row">
											<div class="col-3">
												<div class="input-group flex-nowrap input-group-sm">
													<div class="input-group-prepend">
														<span class="input-group-text cust-select-span"
															id="addon-wrapping">Sort By</span>
													</div>

													<select class="cust-select">
														<option>Latest</option>
														<option>Oldest</option>
														<option>All</option>
													</select>
												</div>

											</div>

											<nav class="col-9">
												<ul class="pagination float-right">
													<button class="form-control text-white exprtExcel">Export
														To Exel</button>
												</ul>
											</nav>
										</div>




										<div class="table-responsive mb-3 testAssignTable">
											<table class="table table-striped table-hover">
												<thead>
													<tr>
														<th scope="col">No.</th>

														<th scope="col">Student's Name</th>
														<th scope="col">SAPID</th>
														<th scope="col">ROLL-NO</th>

														<c:forEach items="${cpList}" var="cp" varStatus="status">
															<th scope="col"><div>Class
																	Participation-${status.index+1}</div></th>

														</c:forEach>

													</tr>
												</thead>
												<tbody>
													<!-- <tr>
													<th scope="row">1</th>
													<td>Test Name One</td>
													<td>Kapil Sharma</td>
													<td>12/02/2019</td>
													<td>16/02/2019</td>
													<td>50</td>
													<td>50</td>
												</tr> -->

													<c:forEach var="student" items="${students}" varStatus="status">
														<tr>
															<td><div class="text-nowrap">${status.index+1}</div></td>
															<td><div class="text-nowrap">${student.key}</div></td>
															<td><c:out value="${usernameList[student.key]}" /></td>
															<td><c:out value="${rollNoList[student.key]}" /></td>
															<c:forEach var="cp" items="${cpList}">
																<c:set var="myParam" value="${cp}" />
																<td class="text-center"><div>
																		<c:out value="${student.value.cpToScore[myParam]}" />
																	</div></td>

															</c:forEach>
														<tr>
													</c:forEach>


												</tbody>
											</table>
										</div>

									</div>
								</section>

							</div>
						</div> --%>



						<div class="card bg-white border">
							<div class="card-body">
								<h5 class="text-center border-bottom pb-2">Grade Center for
									${course.courseName }</h5>

								<div class="table-responsive table-striped testAssignTable">
									<table id="gradeTable" class="table  table-hover"
										style="font-size: 12px">

										<thead>
											<tr>
												<th colspan=3 class="text-center border-grey" id="studentColumn">Student</th>
												<c:if test="${not empty assignments}">
													<th colspan="${fn:length(assignments)}"
														class="text-center border-grey"
														class="bg-blue text-center" id="assignmentColumn">Assignments</th>
												</c:if>
												<c:if test="${not empty tests}">
													<th colspan="${fn:length(tests)*2}"
														class="text-center border-grey" id="testColumn">Tests</th>
												</c:if>
												<c:if test="${not empty cpList}">
													<th colspan="${fn:length(cpList)}"
														class="text-center border-grey" id="cpColumn">Class Participation</th>
												</c:if>

												<c:if test="${not empty offlineList}">
													<th colspan="${fn:length(offlineList)}"
														class="text-center border-grey">Offline Test</th>
												</c:if>
											</tr>
											<tr>
												<th>Full Name</th>
												<th>Username</th>
												<th>Roll No.</th>

												<c:forEach items="${assignments }" var="assignment">
													<th class="bg-blue-sky text-center"><div>${assignment}</div></th>
												</c:forEach>
												<c:forEach items="${tests }" var="test">
													<th class="bg-red-sky text-center"><div>${test}</div></th>
													<th class="bg-red-sky text-center">status</th>
												</c:forEach>
												<c:forEach items="${cpList}" var="cp">
													<th class="bg-green text-center"><div></div></th>
												</c:forEach>
												<c:forEach items="${offlineList}" var="offlineTest">
													<th class="bg-purple text-center"><div>${offlineTest}</div></th>
												</c:forEach>

											</tr>
										</thead>
										<tbody>

											<c:forEach var="student" items="${students}">
												<tr>
													<td><div class="text-nowrap">${usernameList[student.key]}</div></td>
													<td><c:out value="${student.key}" /></td>
													<td><c:out value="${rollNoList[student.key]}" /></td>
													<c:forEach var="studentAssignment" items="${assignments}">
														<c:set var="myParam" value="${studentAssignment}" />
														<td class="text-center"><div>
																<c:out
																	value="${student.value.assigmentToScore[myParam]}" />
															</div></td>
													</c:forEach>
													<c:forEach var="studentTest" items="${tests}">
														<c:set var="myParam" value="${studentTest}" />
														<td class="text-center">
															<div>
																<c:out value="${student.value.testToScore[myParam]}" />
															</div>
														</td>
														<td class="text-center">
															<div>
																<c:out value="${student.value.statusMap[myParam]}" />
															</div>
														</td>
													</c:forEach>
													<c:forEach var="studentCP" items="${cpList}">
														<c:set var="myParam" value="${studentCP}" />
														<td class="text-center">
															<div>
																<c:out value="${student.value.cpToScore[myParam]}" />
															</div>
														</td>
													</c:forEach>

													<c:forEach var="offlineLst" items="${offlineList}">
														<c:set var="myParam" value="${offlineLst}" />
														<td class="text-center">
															<div>
																<c:out value="${student.value.offlineToScore[myParam]}" />
															</div>
														</td>
													</c:forEach>
												</tr>
											</c:forEach>


										</tbody>
									</table>
								</div>
								<br> <a id="dlink" style="display: none;"></a> <input
									type="button" class="btn btn-primary" role="button"
									onclick="tableToExcel('gradeTable', '${course.courseName }', 'Grades for ${course.courseName }.xls'      )"
									value="Export to Excel">
							</div>
						</div>
					</c:if>

					<!-- <div class="card-body">
							
						
						</div> -->



					<%-- <jsp:include page="../common/paginate.jsp">
						<jsp:param name="baseUrl" value="searchFacultyGroups" />
					</jsp:include> --%>

					<!-- /page content: END -->

				</div>

				<!-- SIDEBAR START -->
				<jsp:include page="../common/newSidebar.jsp" />
				<!-- SIDEBAR END -->
				<jsp:include page="../common/footer.jsp" />
				<script>
					$("#courseId")
							.on(
									'change',
									function() {
										var selectedValue = $(this).val();
										window.location.href = '${pageContext.request.contextPath}/gradeCenter?courseId='
												+ encodeURIComponent(selectedValue);
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
				
				
				<script>
				
				var courIdSelected = $('#courseId option').val();
				
				if (courIdSelected !== "") {
					$('.dataTableBottom').attr('id', 'gradeListPage')
					
				}
				
				if ($(window).width() <= 1146) {

					$('#gradeListPage header, #gradeListPage .headTop').css('padding-left', '15px');
				}
				
				</script>