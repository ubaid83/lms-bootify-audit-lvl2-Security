<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html lang="en">
<jsp:include page="../common/css.jsp" />

<body class="nav-md footer_fixed">

	<div class="loader"></div>


	<div class="container body">
		<div class="main_container">

			<jsp:include page="../common/leftSidebar.jsp">
				<jsp:param name="courseId" value="${courseId}" />
			</jsp:include>
			<jsp:include page="../common/topHeader.jsp" />
			<%-- <jsp:include page="../common/alert.jsp" />
 --%>

			<!-- page content: START -->
			<div class="right_col" role="main">

				<div class="dashboard_container">

					<div class="dashboard_container_spacing">

						<div class="breadcrumb">
							<a href="${pageContext.request.contextPath}/homepage">Dashboard</a> <i class="fa fa-angle-right"></i>
							Search Program
						</div>
						<jsp:include page="../common/alert.jsp" />
						

						<!-- Input Form Panel -->
						<div class="row">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">

									<div class="x_title">
										<h2>Program List</h2>
										<ul class="nav navbar-right panel_toolbox">
											<li><a class="collapse-link"><i
													class="fa fa-chevron-up"></i></a></li>
										</ul>
										<div class="clearfix"></div>
									</div>

									<div class="x_content">
										<form:form action="searchProgram" method="post"
											modelAttribute="program">



											<div class="row">
												<div class="col-sm-6 col-md-4 col-xs-12 column">
													<div class="form-group">
														<form:label path="abbr" for="abbr">Program Abbreviation</form:label>
														<form:input id="abbr" path="abbr" type="text"
															placeholder="Program Abbreviation" class="form-control"
															value="${program.abbr}" />
													</div>
												</div>
												<div class="col-sm-6 col-md-4 col-xs-12 column">
													<div class="form-group">
														<form:label path="programName" for="programName">Program Name</form:label>
														<form:input id="programName" path="programName"
															type="text" placeholder="Program Full Name"
															class="form-control" />
													</div>
												</div>
												<div class="col-sm-6 col-md-4 col-xs-12 column">
													<div class="form-group">
														<form:label path="sessionType" for="sessionType">Program Type</form:label>
														<form:select id="sessionType" path="sessionType"
															class="form-control" itemValue="${program.sessionType}">
															<form:option value="">Select Program Type</form:option>
															<form:option value="ANNUAL">ANNUAL</form:option>
															<form:option value="SEMESTER">SEMESTER</form:option>
															<form:option value="TRIMESTER">TRIMESTER</form:option>
														</form:select>
													</div>
												</div>
												<div class="col-sm-12 column">
													<div class="form-group">
														<button id="submit" name="submit"
															class="btn btn-large btn-primary">Search</button>
														<input id="reset" type="reset" class="btn btn-danger">
														<button id="cancel" name="cancel" class="btn btn-danger"
															formnovalidate="formnovalidate">Cancel</button>
													</div>
												</div>
											</div>

										</form:form>
									</div>
								</div>
							</div>
						</div>

						<!-- Results Panel -->
						<c:choose>
							<c:when test="${page.rowCount > 0}">
								<div class="row">
									<div class="col-xs-12 col-sm-12">
										<div class="x_panel">
											<div class="x_title">
												<h2>Programs | ${page.rowCount} Records Found</h2>
												<ul class="nav navbar-right panel_toolbox">
													<li><a class="collapse-link"><i
															class="fa fa-chevron-up"></i></a></li>
													<li><a class="close-link"><i class="fa fa-close"></i></a></li>
												</ul>
												<div class="clearfix"></div>
											</div>
											<div class="x_content">
												<div class="table-responsive">
													<table class="table table-hover">
														<thead>
															<tr>
																<th>Sr. No.</th>
																<th>Abbreviation</th>
																<th>Full Name</th>
																<th>Program Type</th>
																<th>Duration in Months</th>
																<th>Max Duration in Months</th>
																<th>Syllabus Revision Month</th>
																<th>Syllabus Revision Year</th>
																<th>Actions</th>
															</tr>
														</thead>
														<tbody>

															<c:forEach var="program" items="${page.pageItems}"
																varStatus="status">
																<tr>
																	<td><c:out value="${status.count}" /></td>
																	<td><c:out value="${program.abbr}" /></td>
																	<td><c:out value="${program.programName}" /></td>
																	<td><c:out value="${program.sessionType}" /></td>
																	<td><c:out value="${program.durationInMonths}" /></td>
																	<td><c:out value="${program.maxDurationInMonths}" /></td>
																	<td><c:out value="${program.revisedFromMonth}" /></td>
																	<td><c:out value="${program.revisedFromYear}" /></td>
																	<td><c:url value="addProgramForm" var="editurl">
																			<c:param name="programId" value="${program.id}" />
																		</c:url> <c:url value="deleteProgram" var="deleteurl">
																			<c:param name="programId" value="${program.id}" />
																		</c:url> <a href="${editurl}" title="Edit"><i
																			class="fa fa-pencil-square-o fa-lg"></i></a>&nbsp; <a
																		href="${deleteurl}" title="Delete"
																		onclick="return confirm('Are you sure you want to delete this record?')"><i
																			class="fa fa-trash-o fa-lg"></i></a></td>
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

						<jsp:include page="../common/paginate.jsp">
							<jsp:param name="baseUrl" value="searchProgram" />
						</jsp:include>

					</div>



				</div>

			</div>
			<!-- /page content: END -->


			<jsp:include page="../common/footer.jsp" />

		</div>
	</div>





</body>
</html>
