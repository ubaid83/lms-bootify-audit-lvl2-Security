<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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
				<jsp:param value="Announcement" name="activeMenu" />
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
							<i class="fa fa-angle-right"></i> Announcement List
						</div>
						<jsp:include page="../common/alert.jsp" />
						<!-- Input Form Panel -->
						<div class="row">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">

									<div class="x_title">
										<h2>Announcement List</h2>
										<sec:authorize access="hasAnyRole('ROLE_LIBRARIAN')">
											<c:url value="addAnnouncementFormLibrary"
												var="addAnnouncementUrl">

											</c:url>
										</sec:authorize>

										<sec:authorize access="hasAnyRole('ROLE_EXAM')">
											<c:url value="addAnnouncementForm" var="addAnnouncementUrl">

											</c:url>
										</sec:authorize>

										<sec:authorize
											access="hasAnyRole('ROLE_LIBRARIAN', 'ROLE_EXAM')">
											<a class="btn btn-primary btn-sm pull-right" role="button"
												href="${addAnnouncementUrl}"><i class="fa fa-plus"
												aria-hidden="true"></i> Add Announcement</a>
										</sec:authorize>
										<ul class="nav navbar-right panel_toolbox">
											<li><a class="collapse-link"><i
													class="fa fa-chevron-up"></i></a></li>
										</ul>
										<div class="clearfix"></div>
									</div>

									<div class="x_content">
										<form:form action="searchAnnouncementForLibrarian"
											method="post" modelAttribute="announcement">
											<div class="row">
												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<form:label path="subject" for="subject">Announcement Title</form:label>
														<form:input id="subject" path="subject" type="text"
															placeholder="Announcement Title" class="form-control" />
													</div>
												</div>

												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<form:label path="startDate" for="startDate">Display From</form:label>

														<div class='input-group date' id='datetimepicker1'>
															<form:input id="startDate" path="startDate" type="text"
																placeholder="Start Date" class="form-control"
																readonly="true" />
															<span class="input-group-addon"><span
																class="glyphicon glyphicon-calendar"></span> </span>
														</div>
													</div>
												</div>
												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<form:label path="endDate" for="endDate">Display Until</form:label>

														<div class='input-group date' id='datetimepicker2'>
															<form:input id="endDate" path="endDate" type="text"
																placeholder="End Date" class="form-control"
																readonly="true" />
															<span class="input-group-addon"><span
																class="glyphicon glyphicon-calendar"></span> </span>
														</div>

													</div>
												</div>


												<div class="clearfix"></div>
												<div class="col-sm-12 column">
													<div class="form-group">
														<button id="submit" name="submit"
															class="btn btn-large btn-primary">Search</button>
														<button type="reset" value="reset"
															class="btn btn-large btn-primary">Reset</button>

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

																<th>Start Date</th>
																<th>End Date</th>

																<th>Actions</th>
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

																	<td><c:out value="${announcement.startDate}" /></td>
																	<td><c:out value="${announcement.endDate}" /></td>

																	<td><c:url value="viewAnnouncement"
																			var="detailsUrl">
																			<c:param name="id" value="${announcement.id}" />
																		</c:url> <c:url value="addAnnouncementFormLibrary"
																			var="editurl">
																			<c:param name="id" value="${announcement.id}" />
																		</c:url> <c:url value="deleteAnnouncement" var="deleteurl">
																			<c:param name="id" value="${announcement.id}" />
																		</c:url> <a href="${detailsUrl}" title="Details"><i
																			class="fa fa-info-circle fa-lg"></i></a>&nbsp; <sec:authorize
																			access="hasAnyRole('ROLE_LIBRARIAN', 'ROLE_EXAM')">

																				<sec:authorize access="hasRole('ROLE_LIBRARIAN')">
																					<a href="${editurl}" title="Edit"><i
																					class="fa fa-pencil-square-o fa-lg"></i></a>&nbsp; 
																				</sec:authorize>
																				<c:if test="${announcement.announcementType eq 'TIMETABLE'}">
																					<a href="downloadStudentsReport?id=${announcement.id}"><i class="fa fa-download"></i></a>
																				</c:if>
																				<c:if test="${fn:containsIgnoreCase(announcement.createdBy,userBean.username)}">
																					<a href="${deleteurl}" title="Delete" onclick="return confirm('Are you sure you want to delete this Announcement?')">
																						<i class="fa fa-trash-o fa-lg"></i></a>
																				</c:if>
																					
																				
																		</sec:authorize></td>
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
							<jsp:param name="baseUrl" value="searchAnnouncement" />
						</jsp:include>
					</div>



				</div>

			</div>
			<!-- /page content: END -->


			<jsp:include page="../common/footerLibrarian.jsp" />

		</div>
	</div>




	<script type="text/javascript"
		src="<c:url value="/resources/js/customDateTimePicker.js" />"></script>


	<script>
		//$(document).ready(function() {
		$("#datetimepicker1").on("dp.change", function(e) {

			validDateTimepicks();
		}).datetimepicker({
			// minDate:new Date(),
			useCurrent : false,
			format : 'YYYY-MM-DD HH:mm:ss'
		});

		$("#datetimepicker2").on("dp.change", function(e) {

			validDateTimepicks();
		}).datetimepicker({
			// minDate:new Date(),
			useCurrent : false,
			format : 'YYYY-MM-DD HH:mm:ss'
		});

		function validDateTimepicks() {
			if (($('#startDate').val() != '' && $('#startDate').val() != null)
					&& ($('#endDate').val() != '' && $('#endDate').val() != null)) {
				var fromDate = $('#startDate').val();
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
		//});
	</script>

</body>
</html>

