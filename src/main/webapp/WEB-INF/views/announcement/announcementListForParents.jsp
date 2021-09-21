<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
			<jsp:include page="../common/topHeader.jsp" />
			<%--  <jsp:include page="../common/alert.jsp" /> --%>


			<!-- page content: START -->
			<div class="right_col" role="main">

				<div class="dashboard_container">

					<div class="dashboard_container_spacing">

						<div class="breadcrumb">
							<c:out value="${Program_Name}" />
						
						<sec:authorize access="hasRole('ROLE_STUDENT')">
						
						<i
						class="fa fa-angle-right"></i>
											
											<c:out value="${AcadSession}" />
											
											</sec:authorize>
						<br><br>

						
							<a href="${pageContext.request.contextPath}/homepage">Dashboard</a> <i class="fa fa-angle-right"></i>
							Announcements For Parents
						</div>
						<jsp:include page="../common/alert.jsp" />

						<!-- Input Form Panel -->
						<div class="row">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">

									<div class="x_title">
										<h2>Announcements For Parents</h2>
										<ul class="nav navbar-right panel_toolbox">
											<li><a class="collapse-link"><i
													class="fa fa-chevron-up"></i></a></li>
										</ul>
										<div class="clearfix"></div>
									</div>

									<div class="x_content">
										<form>
											<div class="row">
												<div class="col-md-4 col-sm-6 col-xs-12">
													<div class="form-group">
														<label>Username</label> <select id="username"
															name="username" class="form-control">
															<c:if test="${uname eq  null }">
																<option value="">Select Username</option>
															</c:if>
															<c:forEach var="students" items="${listOfStudents}"
																varStatus="status">
																<c:if test="${uname eq students.stud_username }">
																	<option value="${students.stud_username}" selected>${students.stud_username}</option>
																</c:if>
																<c:if test="${uname ne students.stud_username }">
																	<option value="${students.stud_username}">${students.stud_username}</option>
																</c:if>
															</c:forEach>
														</select>
													</div>
												</div>

											</div>
											<div class="clear"></div>

										</form>
									</div>
								</div>
							</div>
						</div>
						<c:if test="${showAnnouncementList eq true}">
							<c:choose>
								<c:when test="${page.rowCount > 0}">
									<!-- Results Panel -->
									<div class="row">
										<div class="col-xs-12 col-sm-12">
											<div class="x_panel">
												<div class="x_title">
													<h2>Announcements | ${page.rowCount} Records Found</h2>
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
																	<th>Announcement Title</th>
																	<th>Type</th>
																	<th>Course</th>
																	<th>Program</th>
																	<th>Start Date</th>
																	<th>End Date</th>


																</tr>
															</thead>
															<tbody>

																<c:forEach var="announcement" items="${page.pageItems}"
																	varStatus="status">
																	<tr>
																		<td><c:out value="${status.count}" /></td>
																		<td><c:out value="${announcement.subject}" /></td>
																		<td><c:out
																				value="${announcement.announcementType}" /></td>
																		<td><c:out value="${announcement.courseId}" /></td>
																		<td><c:out value="${announcement.programId}" /></td>
																		<td><c:out value="${announcement.startDate}" /></td>
																		<td><c:out value="${announcement.endDate}" /></td>

																		<td></td>
																	</tr>
																</c:forEach>
															</tbody>
														</table>


														<!--Project Activity end-->
													</div>
												</div>
											</div>
										</div>
									</div>
								</c:when>
							</c:choose>
						</c:if>
						<jsp:include page="../common/paginate1.jsp">
							<jsp:param name="baseUrl" value="announcementListForParents" />



						</jsp:include>
					</div>



				</div>

			</div>
			<!-- /page content: END -->


			<jsp:include page="../common/footer.jsp" />

		</div>
	</div>



	<script>
		$("#username")
				.on(
						'change',
						function() {
							var selectedValue = $(this).val();
							window.location = '${pageContext.request.contextPath}/announcementListForParents?uname='
									+ encodeURIComponent(selectedValue);
							return false;
						});
	</script>

</body>
</html>
