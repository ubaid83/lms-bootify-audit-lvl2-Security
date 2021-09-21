<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
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

			<jsp:include page="../common/topHeaderLibrian.jsp" />



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
							<i class="fa fa-angle-right"></i> Announcement List
						</div>
						<jsp:include page="../common/alert.jsp" />
						
							<div class="row">
								<div class="col-xs-12 col-sm-12">
									<div class="x_panel">

										<div class="x_title">
											<!-- <h2>User Announcement List</h2> -->
											<h2>Announcements | ${page.rowCount } Records Found</h2>
											<ul class="nav navbar-right panel_toolbox">
												<li><a class="collapse-link"><i
														class="fa fa-chevron-up"></i></a></li>
											</ul>
											<div class="clearfix"></div>
										</div>
										<div class="x_content">
												<div class="table-responsive">
													<table class="table table-hover ">
														<thead>

															<tr>
																<th>Sr. No.</th>
																<th>Announcement Title</th>
																<th>Type</th>
																<th>SubType</th>
																<th>Course</th>
																<th>Program</th>
																<th>Start Date</th>
																<th>End Date</th>

																
															</tr>
														</thead>
														<c:choose>
							<c:when test="${pageRowCount > 0}">
														<tbody>

															<c:forEach var="announcement" items="${announcementList}"
																varStatus="status">
																<tr>
																	<td><c:out value="${status.count}" /></td>
																	<td><c:out value="${announcement.subject}" /></td>
																	<td><c:out
																			value="${announcement.announcementType}" /></td>
																	<td><c:out
																			value="${announcement.announcementSubType}" /></td>
																	<%-- <td><c:out value="${announcement.courseName}" /></td>
																	<td><c:out value="${announcement.programName}" /></td> --%>
																	<td>
																			${announcement.courseName}
																		</td>
																	<td>
																			${announcement.programName}
																		</td>
																	<td><c:out value="${announcement.startDate}" /></td>
																	<td><c:out value="${announcement.endDate}" /></td>

																	
																</tr>
															</c:forEach>
														</tbody>
														</c:when>
										</c:choose>
													</table>
												</div>
												<c:url value="/checkUserCourse" var="goBack">
													<c:param name="username" value="${username}"></c:param>
													</c:url>
													<a href="${goBack}" class="btn btn-success" >Back</a>
											</div>

										
									</div>
								</div>
							</div>
						
						

								<!-- Results Panel -->
								<%-- <div class="row">
									<div class="col-xs-12 col-sm-12">
										<div class="x_panel">
											<div class="x_title">
												
												<c:url value="addAnnouncementForm" var="addAnnouncementUrl">
													<c:param name="courseId">${courseId}</c:param>
												</c:url>

												<sec:authorize
													access="hasAnyRole('ROLE_FACULTY', 'ROLE_ADMIN', 'ROLE_DEAN', 'ROLE_HOD')">
													<a class="btn btn-primary btn-sm pull-right" role="button"
														href="${addAnnouncementUrl}"><i class="fa fa-plus"
														aria-hidden="true"></i> Add Announcement</a>
												</sec:authorize>
												<ul class="nav navbar-right panel_toolbox">
													<li><a class="collapse-link"><i
															class="fa fa-chevron-up"></i></a></li>
													<li><a class="close-link"><i class="fa fa-close"></i></a></li>
												</ul>
												<div class="clearfix"></div>
											</div>
											
										</div>
									</div>
								</div> --%>
							<%-- </c:when>
						</c:choose>
 --%>
						<%-- <jsp:include page="../common/paginate.jsp">
							<jsp:param name="baseUrl" value="viewUserAnnouncements" />
						</jsp:include> --%>


						<sec:authorize
							access="hasAnyRole('ROLE_FACULTY', 'ROLE_ADMIN', 'ROLE_DEAN')">
							<jsp:include page="../common/paginate.jsp">
								<jsp:param name="baseUrl" value="viewUserAnnouncements" />
							</jsp:include></sec:authorize>
						<sec:authorize access="hasRole('ROLE_STUDENT')">
							<jsp:include page="../common/paginate.jsp">
								<jsp:param name="baseUrl" value="viewUserAnnouncementsSearch" /></jsp:include></sec:authorize>

					</div>



				</div>

			</div>
			<!-- /page content: END -->


			<jsp:include page="../common/footerLibrarian.jsp" />

			<script type="text/javascript">
				$(document)
						.ready(
								function() {
									console.log("hello-----"
											+ $("#disableAnnouncementSubtype")
													.val());
									if ($("#disableAnnouncementSubtype").val() == 'disable') {
										$("#annnouncementSubtypeDiv").hide();
									} else {
										$("#annnouncementSubtypeDiv").show();
									}

								});
				function showModal(id, subject) {
					var myModal = new jBox('Modal', {
						ajax : {
							url : 'getAnnouncementDetails',
							data : 'id=' + id,
							reload : true
						},
						animation : 'flip',
						draggable : true,
						title : subject

					});

					myModal.open();
				}
			</script>

		</div>
	</div>





</body>
</html>
