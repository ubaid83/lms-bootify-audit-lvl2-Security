<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<html lang="en">
<jsp:include page="../common/css.jsp" />

<body class="nav-md footer_fixed">

	<div class="loader"></div>


	<div class="container body">
		<div class="main_container">

			<jsp:include page="../common/leftSidebar.jsp">
				<jsp:param name="courseId" value="${courseId}" />
			</jsp:include>

			<jsp:include page="../common/topHeaderLibrian.jsp">
				<jsp:param name="Assignment" value="activeMenu" />
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
							<i class="fa fa-angle-right"></i>Check Course List
						</div>
						<jsp:include page="../common/alert.jsp" />
						<!-- Input Form Panel -->
						<div class="row">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">

									<div class="x_title">
										<h2>Check Course List</h2>
										<ul class="nav navbar-right panel_toolbox">
											<li><a class="collapse-link"><i
													class="fa fa-chevron-up"></i></a></li>
										</ul>
										<div class="clearfix"></div>
									</div>

									<div class="x_content">
										<form:form action="checkUserCourse" method="post"
											modelAttribute="userCourse">
											<fieldset>




												<%-- <div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<form:label path="role" for="role">Role</form:label>
														<form:select id="role" path="role"
															class="form-control">
															<form:option value="">Select Role</form:option>
															<c:forEach var="roles" items="${allRoles}"
																varStatus="status">
																<form:option value="${roles.role}">${roles.role}</form:option>
															</c:forEach>
														</form:select>
													</div>
												</div> --%>

												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<form:label path="username" for="username">Users <span style="color: red">*</span></form:label>
														<form:input id="username" path="username" class="form-control"
														placeholder="User SapId" required="required"/>
													</div>
												</div>



												<div class="col-sm-12 column">
													<div class="form-group">
														<button id="submit" name="submit"
															class="btn btn-large btn-primary">Search</button>
														<!-- <input id="reset" type="reset" class="btn btn-danger"> -->
														<button id="cancel" name="cancel" class="btn btn-danger"
															formaction="homepage"
															formnovalidate="formnovalidate">Cancel</button>
													</div>
												</div>
											</fieldset>
										</form:form>
									</div>
								</div>
								
								<div class="x_panel">

									<div class="x_title">
										<h2>Course List</h2>
										<ul class="nav navbar-right panel_toolbox">
											<li><a class="collapse-link"><i
													class="fa fa-chevron-up"></i></a></li>
										</ul>
										<div class="clearfix"></div>
									</div>

									<div class="x_content">
										<div class="table-responsive">
													<table id="showUsers" class="table table-hover ">
													<thead>
															<tr>
																<th>Sr. No.</th>
																<th>Course Id</th>
																<th>Course Name</th>
																<th>Program Id</th>
																<th>Program Name</th>
																<th>Acad Year</th>
																<th>Acad Month</th>
																<th>Acad Session</th>
																<th>Department</th>
																<th>Module Category</th>
																<th>Actions</th>
															</tr>
													</thead>
													<tbody>
													<c:forEach var="courseList" items="${courseList}"
																varStatus="status">
														<tr>
															<td>${status.count}</td>
															<td>${courseList.id}</td>
															<td>${courseList.courseName}</td>
															<td>${courseList.programId}</td>
															<td>${courseList.programName}</td>
															<td>${courseList.acadYear}</td>
															<td>${courseList.acadMonth}</td>
															<td>${courseList.acadSession}</td>
															<td>${courseList.dept}</td>
															<td>${courseList.moduleCategoryName}</td>
															<td><c:url value="supportAdminTestList" var="testurl">
																			<c:param name="courseId" value="${courseList.id}" />
																			<c:param name="username" value="${userName}" />
																</c:url>
																<a href="${testurl}" title="Test"><i
																				class="fa fa-file-text fa-lg"></i></a>&nbsp;
																
																 <c:url value="supportAdminAssignmentList" var="assgnurl">
																			<c:param name="courseId" value="${courseList.id}" />
																			<c:param name="username" value="${userName}" />
																 </c:url>
																 <a href="${assgnurl}" title="Assignment"><i
																				class="fa fa-newspaper-o fa-lg"></i></a>&nbsp;
																
																<c:url value="supportAdminViewUserAnnouncements" var="annurl">
																			<c:param name="courseId" value="${courseList.id}" />
																			<c:param name="username" value="${userName}" />
																 </c:url>
																 <a href="${annurl}" title="Announcement"><i
																				class="fa fa-bullhorn fa-lg"></i></a>&nbsp;
																
																<c:url value="supportAdminStudentContentList" var="lrurl">
																			<c:param name="courseId" value="${courseList.id}" />
																			<c:param name="username" value="${userName}" />
																 </c:url>
																 <a href="${lrurl}" title="Learning Resource"><i
																				class="fa fa-folder-open fa-lg"></i></a>&nbsp;
																				
																<c:if test="${userRole eq 'ROLE_STUDENT'}">		
																<c:url value="supportAdminGradeCenterForStudent" var="gradeurl">
																			<c:param name="courseId" value="${courseList.id}" />
																			<c:param name="username" value="${userName}" />
																 </c:url>
																 <a href="${gradeurl}" title="Grade Center"><i
																				class="fa fa-pie-chart fa-lg"></i></a>&nbsp;
																</c:if>	
																<c:if test="${userRole eq 'ROLE_FACULTY'}">	
																<c:url value="supportAdminGradeCenterForFaculty" var="gradeurl">
																			<c:param name="courseId" value="${courseList.id}" />
																			<c:param name="username" value="${userName}" />
																 </c:url>
																 <a href="${gradeurl}" title="Grade Center"><i
																				class="fa fa-pie-chart fa-lg"></i></a>&nbsp;
																</c:if>
														</tr>
													
													</c:forEach>
													</tbody>
													</table>
									    </div>
									</div>
									<div class="alert alert-dismissible" role="alert">${note}</div>
								</div>
							</div>
						</div>
						
						<jsp:include page="../common/paginate.jsp">
							<jsp:param name="baseUrl" value="evaluateByStudent" />
						</jsp:include>
					</div>



				</div>

			</div>
			<!-- /page content: END -->

			<jsp:include page="../common/paginate.jsp">
				<jsp:param name="baseUrl" value="evaluateByStudent" />
			</jsp:include>

			<jsp:include page="../common/footerLibrarian.jsp" />
			<!-- <script>
				$(document)
						.ready(
								function() {

							});

									 $("#role")
											.on(
													'change',
													function() {
													
														var role = $(this)
																.val();
														
														if (role) {
														
															$
																	.ajax({
																		type : 'GET',
																		url : '${pageContext.request.contextPath}/getUsersByRole?'
																				+ 'role='
																				+ role,
																		success : function(
																				data) {
																			var json = JSON
																					.parse(data);
																			var optionsAsString = "";

																			$(
																					'#assid')
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
																					'#assid')
																					.append(
																							optionsAsString);

																		}
																	});
														} else {
															alert('Error no User Role');
														}
													});
									$('#role').trigger('change');

								
			<!-- </script> --> 
			<!-- <!-- <script type="text/javascript">
				var tableToExcel = (function() {
					var uri = 'data:application/vnd.ms-excel;base64,', template = '<html xmlns:o="urn:schemas-microsoft-com:office:office" xmlns:x="urn:schemas-microsoft-com:office:excel" xmlns="http://www.w3.org/TR/REC-html40"><head><!--[if gte mso 9]><xml><x:ExcelWorkbook><x:ExcelWorksheets><x:ExcelWorksheet><x:Name>{worksheet}</x:Name><x:WorksheetOptions><x:DisplayGridlines/></x:WorksheetOptions></x:ExcelWorksheet></x:ExcelWorksheets></x:ExcelWorkbook></xml><![endif]--><!-- </head><body><table>{table}</table></body></html>', base64 = function( -->
						<!-- 	s) {
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
				})() -->
			<!-- </script> --> 

		</div>
		</div>





</body>
</html>
