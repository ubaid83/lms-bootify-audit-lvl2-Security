<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<html lang="en">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<jsp:include page="../common/css.jsp" />

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

<body class="nav-md footer_fixed">


	<!-- <div class="loader"></div> -->

	<div class="container body">
		<div class="main_container">

			<%-- <jsp:include page="../common/leftSidebar.jsp">
				<jsp:param name="courseId" value="${courseId}" />
			</jsp:include> --%>
			<jsp:include page="../common/leftSidebar.jsp" />
			<jsp:include page="../common/topHeaderLibrian.jsp">
				<jsp:param value="Grades" name="activeMenu" />
			</jsp:include>


			<!-- page content: START -->
			<div class="right_col" role="main">

				<div class="dashboard_container">

					<div class="dashboard_container_spacing">

						<div class="breadcrumb">
							<c:out value="${Program_Name}" />

							<sec:authorize access="hasRole('ROLE_STUDENT')">

								<i class="fa fa-angle-right"></i>

								<c:out value="${AcadSession}" />

							</sec:authorize>

							<br> <br> <a
								href="${pageContext.request.contextPath}/homepage">Dashboard</a>
							<i class="fa fa-angle-right"></i> Grade Center For
							${course.courseName }
						</div>
						<jsp:include page="../common/alert.jsp" />

						<!-- Input Form Panel -->
						<div class="row">

							<div class="col-md-12 col-xs-12 col-sm-12">
								<div class="x_panel">

									<div class="x_title">
										<h2>Grade Center For ${course.courseName }</h2>
										<ul class="nav navbar-right panel_toolbox">
											<li><a class="collapse-link"><i
													class="fa fa-chevron-up"></i></a></li>
										</ul>
										<div class="clearfix"></div>
									</div>


									<div class="x_content">
										<form>
											<div class="row">
												
												<div class="col-md-4 col-xs-12 ">
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

						<!-- Results Panel -->
						<c:if test="${showGradeCenterList eq true}">
							<div class="row">
								<div class="col-xs-12 col-sm-12">
									<div class="x_panel">
										<div class="x_title">
											<h2>Grade Center for ${course.courseName }</h2>
											<ul class="nav navbar-right panel_toolbox">
												<!-- <li><a href="#"><span>View All</span></a></li> -->
												<li><a class="collapse-link"><i
														class="fa fa-chevron-up"></i></a></li>
												<li><a class="close-link"><i class="fa fa-close"></i></a></li>
											</ul>
											<div class="clearfix"></div>
										</div>
										<div class="x_content">
											<div class="table-responsive">
												<table id="gradeTable" class="table  table-hover"
													style="font-size: 12px">

													<thead>
														<tr>
															<th colspan=3 class="text-center border-grey">Student</th>
															<c:if test="${not empty assignments}">
																<th colspan="${fn:length(assignments)}"
																	class="bg-blue text-center">Assignments</th>
															</c:if>
															<c:if test="${not empty tests}">
																<th colspan="${fn:length(tests)*2}"
																	class="bg-red text-center">Tests</th>
															</c:if>
															<c:if test="${not empty cpList}">
																<th colspan="${fn:length(cpList)}"
																	class="bg-green text-center">Class Participation</th>
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

														</tr>
													</thead>
													<tbody>

														<c:forEach var="student" items="${students}">
															<tr>
																<td><div class="text-nowrap">${student.key}</div></td>
																<td><c:out value="${usernameList[student.key]}" /></td>
																<td><c:out value="${rollNoList[student.key]}" /></td>
																<c:forEach var="studentAssignment"
																	items="${assignments}">
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
															</tr>
														</c:forEach>


													</tbody>
												</table>
											</div>
										</div>
									</div>
									<br> <a id="dlink" style="display: none;"></a> <input
										type="button" class="btn btn-primary" role="button"
										onclick="tableToExcel('gradeTable', '${course.courseName }', 'Grades for ${course.courseName }.xls'      )"
										value="Export to Excel">
									<c:url value="/checkUserCourse" var="goBack">
													<c:param name="username" value="${username}"></c:param>
													</c:url>
													<a href="${goBack}" class="btn btn-primary" >Back</a>
								</div>
							</div>
						</c:if>
						<jsp:include page="../common/paginate.jsp">
							<jsp:param name="baseUrl" value="searchList" />
						</jsp:include>
					</div>



				</div>

			</div>

			<!-- /page content: END -->


			<jsp:include page="../common/footerLibrarian.jsp" />

		</div>
	</div>







</body>


</html>
