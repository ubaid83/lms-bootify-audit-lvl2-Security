<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<html lang="en">
<head>
<style id="fancybox-style-noscroll" type="text/css">
.compensate-for-scrollbar, .fancybox-enabled body {
	margin-right: 17px;
}
</style>

</head>
<jsp:include page="../common/css.jsp" />

<body class="nav-md footer_fixed">

	<div class="loader"></div>


	<div class="container body">
		<div class="main_container">

			<jsp:include page="../common/leftSidebar.jsp">
				<jsp:param name="courseId" value="${courseId}" />
			</jsp:include>
			<jsp:include page="../common/topHeader.jsp">
				<jsp:param name="courseMenu" value="activeMenu" />
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
							<i class="fa fa-angle-right"></i> Search Subjects To Add TEE
							Components
						</div>
						<jsp:include page="../common/alert.jsp" />
						<!-- Input Form Panel -->
						<div class="row">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">

									<div class="x_title">
										<h2>Search Subjects To Add TEE Components</h2>
										<ul class="nav navbar-right panel_toolbox">
											<li><a class="collapse-link"><i
													class="fa fa-chevron-up"></i></a></li>
										</ul>
										<div class="clearfix"></div>
									</div>

									<div class="x_content">
										<form:form action="searchModulesToAddComponents" method="post"
											modelAttribute="moduleComponent">



											<div class="row">

												<div class="col-sm-6 col-md-4 col-xs-12 column">
													<div class="form-group">
														<form:label path="acadYear" for="acadYear">Academic Year</form:label>
														<form:select id="acadYear" path="acadYear"
															class="form-control">
															<option value="" selected disabled hidden>Select
																Academic Year</option>
															<form:options items="${acadYears}" />
														</form:select>
													</div>
												</div>

												<c:if test="${campusListByProgram.size()>0 }">
													<div class="col-sm-6 col-md-4 col-xs-12 column">
														<div class="form-group">
															<form:label path="campusId" for="campusId">Program Campus </form:label>

															<form:select id="campus" path="campusId"
																class="form-control facultyparameter">
																<c:if test="${moduleComponent.campusId eq null}">
																	<option value="" selected disabled hidden>Select
																		Campus</option>
																</c:if>

																<c:forEach var="listValue"
																	items="${campusListByProgram}">
																	<c:if
																		test="${moduleComponent.campusId eq listValue.campusId}">
																		<option value="${listValue.campusId}" selected>${listValue.campusName}</option>
																	</c:if>
																	<c:if
																		test="${moduleComponent.campusId ne listValue.campusId}">
																		<option value="${listValue.campusId}">${listValue.campusName}</option>
																	</c:if>
																</c:forEach>
															</form:select>
														</div>
													</div>

												</c:if>






												<div class="col-sm-12 column">
													<div class="form-group">
														<button id="submit" name="submit"
															class="btn btn-large btn-primary">Search</button>
														<input id="reset" type="reset" class="btn btn-danger">
														<button id="cancel" name="cancel" class="btn btn-danger"
															formnovalidate="formnovalidate" formaction="homepage">Cancel</button>
													</div>
												</div>
											</div>

										</form:form>
									</div>
								</div>
							</div>
						</div>
						<c:choose>
							<c:when test="${moduleList.size() > 0}">
								<!-- Results Panel -->
								<div class="row">
									<div class="col-xs-12 col-sm-12">
										<div class="x_panel">
											<div class="x_title">
												<h2>Subject List | ${moduleList.size()} Records Found</h2>

												<ul class="nav navbar-right panel_toolbox">
													<li><a class="collapse-link"><i
															class="fa fa-chevron-up"></i></a></li>
													<li><a class="close-link"><i class="fa fa-close"></i></a></li>
												</ul>
												<div class="clearfix"></div>
											</div>
											<div class="x_content">
												<div class="table-responsive">
													<table class="table table-hover " id="example">
														<thead>
															<tr>
																<th>Sr. No.</th>
																<th>Subject ID <i class="fa fa-sort"
																	aria-hidden="true" style="cursor: pointer"></i></th>
																<!-- 	<th>Course Abbreviation</th> -->
																<th>Subject Name <i class="fa fa-sort"
																	aria-hidden="true" style="cursor: pointer"></i></th>

																<th>Subject Abbreviation<i class="fa fa-sort"
																	aria-hidden="true" style="cursor: pointer"></i></th>

																<th>Academic Session<i class="fa fa-sort"
																	aria-hidden="true" style="cursor: pointer"></i></th>


																<th>Actions</th>

																<!-- <th>Course Custom Property 1</th>
							<th>Course Custom Property 2</th>
							<th>Course Custom Property 3</th> -->
																<!-- <th>Actions</th> -->
															</tr>
														</thead>
														<tfoot>
															<tr>
																<th></th>
																<th>Subject ID</th>
																<!-- <th>Course abbr</th> -->
																<th>Subject Name</th>

																<th>Subject Abbreviation</th>
																<th>Academic Session</th>


															</tr>
														</tfoot>
														<tbody>

															<c:forEach var="module" items="${moduleList}"
																varStatus="status">

																<%-- <c:forEach items="${facultyCountMap}" var="faculty">
																	<c:forEach items="${StudentCountMap}" var="student"> --%>






																<tr>



																	<td><c:out value="${status.count}" /></td>
																	<td><c:out value="${module.moduleId}" /></td>
																	<%-- <td><c:out value="${course.abbr}" /></td> --%>
																	<td><c:out value="${module.moduleName}" /></td>
																	<td><c:out value="${module.moduleAbbr}" /></td>
																	<td><c:out value="${module.acadSession}" /></td>



																	<td><c:url value="addComponentsUnderModuleForm"
																			var="addComponents">
																			<c:param name="moduleId" value="${module.moduleId}" />
																			<c:if test="${moduleComponent.campusId ne null}">
																				<c:param name="campusId"
																					value="${moduleComponent.campusId}" />
																			</c:if>
																			<c:param name="acadYear" value="${moduleComponent.acadYear}" />
																			<c:param name="programId"
																				value="${moduleComponent.programId}" />
																		</c:url> <a href="${addComponents}" title="Add Components"><i
																			class="fa fa-plus fa-lg"
																			onclick="callFancyBox1('${module.moduleId}')"></i></a></td>



																</tr>


															</c:forEach>


														</tbody>
													</table>
												</div>
											</div>
										</div>
									</div>
								</div>
							</c:when>
						</c:choose>



					</div>



				</div>

			</div>
			<!-- /page content: END -->


			<jsp:include page="../common/footer.jsp" />

		</div>
	</div>




</body>

</html>

