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


			<jsp:include page="../common/leftSidebar.jsp" />
			<jsp:include page="../common/topHeader.jsp" />



			<!-- page content: START -->
			<div class="right_col" role="main">

				<div class="dashboard_container">

					<div class="dashboard_container_spacing">

						<div class="breadcrumb">
							<a href="${pageContext.request.contextPath}/homepage">Dashboard</a> <i class="fa fa-angle-right"></i>
							Groups
						</div>
						<jsp:include page="../common/alert.jsp" />

						<!-- Input Form Panel -->
						<div class="row">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">

									<div class="x_title">
										<h2>Group Details</h2>
										<ul class="nav navbar-right panel_toolbox">
											<li><a class="collapse-link"><i
													class="fa fa-chevron-up"></i></a></li>
										</ul>
										<div class="clearfix"></div>
									</div>

									<div class="x_content">
										<form:form action="creategroupsForm" id="editgroups"
											method="post" modelAttribute="group"
											enctype="multipart/form-data">

											<form:input path="id" type="hidden" />
											<form:input path="courseId" type="hidden" />
											<form:input path="acadMonth" type="hidden" />
											<form:input path="acadYear" type="hidden" />


											<div class="row">
												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-groups">
														<form:label path="groupsName" for="groupsName">groups Title:</form:label>
														${group.groupsName}
													</div>
												</div>
											</div>
											<div class="row">
												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-groups">
														<form:label path="noOfStudents" for="noOfStudents">No. of Students:</form:label>
														${group.noOfStudents}
													</div>
												</div>
											</div>

											<div class="row">
												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-groups">
														<form:label path="sendEmailAlert" for="sendEmailAlert">Send Email Alert for New groups:</form:label>
														${group.sendEmailAlert}
													</div>
												</div>
												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-groups">
														<form:label path="sendSmsAlert" for="sendSmsAlert">Send SMS Alert for New groups:</form:label>
														${group.sendSmsAlert}
													</div>
												</div>
											</div>

											<div class="row">
												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<form:label path="groups_details" for="groups_details">
														<a data-toggle="collapse" href="#groups_details"
															aria-expanded="true" aria-controls="groups_details">
															groups Details: (Expand/Collapse) </a>
													</form:label>
													<div id="groups_details" class="collapse"
														style="border-style: solid; border-width: 1px; padding: 10px; margin-top: 10px">${groups.groups_details}</div>
												</div>
											</div>
											<br>
											<div class="row">
												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-groups">

														<button id="submit" class="btn btn-large btn-primary"
															formaction="creategroupsForm">Edit groups</button>
														<button id="cancel" class="btn btn-large btn-danger"
															formaction="homepage" formnovalidate="formnovalidate">Cancel</button>
														<%-- <button
									class="btn btn-large btn-primary" formaction="${addStudents}">Add Students</button> --%>
													</div>
												</div>
											</div>

										</form:form>
									</div>
								</div>
							</div>
						</div>

						<!-- Results Panel -->
						<div class="row">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">
									<div class="x_title">
										<h2>Select Students to add in groups</h2>
										<ul class="nav navbar-right panel_toolbox">
											<li><a class="collapse-link"><i
													class="fa fa-chevron-up"></i></a></li>
											<li><a class="close-link"><i class="fa fa-close"></i></a></li>
										</ul>
										<div class="clearfix"></div>
									</div>
									<div class="x_content">
										<form:form action="saveStudentGroupAllocation"
											id="saveStudentGroupAllocation" method="post"
											modelAttribute="group">


											<form:input path="id" type="hidden" />
											<form:input path="courseId" type="hidden" />
											<form:input path="acadMonth" type="hidden" />
											<form:input path="acadYear" type="hidden" />
											<form:input path="facultyId" type="hidden" />

											<div class="table-responsive">
												<table class="table table-hover">
													<thead>
														<tr>
															<th>Sr. No.</th>
															<th>Select (<a onclick="checkAll()">All</a> | <a
																onclick="uncheckAll()">None</a>)
															</th>
															<th>Program</th>
															<th>Student Name</th>
														</tr>
													</thead>
													<tfoot>
														<tr>
															<th></th>
															<th></th>

														</tr>
													</tfoot>
													<tbody>

														<c:forEach var="student" items="${students}"
															varStatus="status">
															<tr>
																<td><c:out value="${status.count}" /></td>
																<td><c:if test="${empty student.id }">
																		<form:checkbox path="studentListToBeAssigned"
																			value="${student.username}" />
																	</c:if> <c:if test="${not empty student.id }">
						            	groups Allocated
						            </c:if></td>
																<td><c:out value="${student.programName}" /></td>
																<td><c:out
																		value="${student.firstname} ${student.lastname}" /></td>
															</tr>
														</c:forEach>
													</tbody>
												</table>
											</div>

											<div class="col-md-4 col-sm-6 col-xs-12 column">
												<div class="form-groups">

													<button id="submit" class="btn btn-large btn-primary"
														formaction="saveStudentGroupAllocation">Add
														Students</button>
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



				</div>

			</div>
			<!-- /page content: END -->


			<jsp:include page="../common/footer.jsp" />

		</div>
	</div>





</body>
</html>
