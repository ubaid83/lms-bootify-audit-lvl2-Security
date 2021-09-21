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
						<sec:authorize access="hasRole('ROLE_STUDENT')">
							<div class="row">
								<div class="col-xs-12 col-sm-12">
									<div class="x_panel">

										<div class="x_title">
											<h2>User Announcement List</h2>
											<ul class="nav navbar-right panel_toolbox">
												<li><a class="collapse-link"><i
														class="fa fa-chevron-up"></i></a></li>
											</ul>
											<div class="clearfix"></div>
										</div>

										<div class="x_content">
											<form:form action="viewUserAnnouncementsSearch" method="post"
												modelAttribute="announcement">
												<div class="row">

													<c:if test="${announcement.announcementType eq null}">
														<input type="hidden" id="disableAnnouncementSubtype"
															value="disable" />
													</c:if>
													<c:if test="${announcement.announcementType ne null}">
														<input type="hidden" id="disableAnnouncementSubtype"
															value="enable" />
													</c:if>
													<div class="col-md-4 col-sm-6 col-xs-12 column" id="">
														<div class="form-group">

															<form:label path="announcementType"
																for="announcementType">Announcement Type</form:label>

															<form:select id="announcementType"
																path="announcementType" class="form-control"
																itemValue="${announcement.announcementType}">
																<c:if test="${announcement.announcementType eq null}">
																	<form:option value="">Select Announcement Type</form:option>
																</c:if>
																<form:option value="COURSE">Course Related</form:option>
																<%-- <form:option value="PROGRAM">Program Related</form:option> Commented by Sanket. Program level Announcements not provided in backend logic--%>
																<form:option value="INSTITUTE">Institute Related</form:option>
																<form:option value="LIBRARY">Library Related</form:option>
																<form:option value="COUNSELOR">Counselor Related</form:option>
															</form:select>
														</div>
													</div>
													<div class="col-md-4 col-sm-6 col-xs-12 column"
														id="annnouncementSubtypeDiv">
														<div class="form-group">

															<form:label path="announcementSubType"
																for="announcementSubType">Announcement SubType</form:label>

															<form:select id="announcementSubType"
																path="announcementSubType" class="form-control">
																<c:if test="${empty announcement.announcementSubType }">
																	<form:option value="">Select Announcement Subtype</form:option>
																</c:if>
																<form:option value="EXAM">EXAM</form:option>
																<form:option value="EVENT">EVENT</form:option>
																<form:option value="ASSIGNMENT">ASSIGNMENT</form:option>
															</form:select>
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
						</sec:authorize>
						<c:choose>
							<c:when test="${pageRowCount > 0}">

								<!-- Results Panel -->
								<div class="row">
									<div class="col-xs-12 col-sm-12">
										<div class="x_panel">
											<div class="x_title">
												<h2>Announcements | ${page.rowCount } Records Found</h2>
												<c:url value="addAnnouncementForm" var="addAnnouncementUrl">
													<c:param name="courseId">${courseId}</c:param>
												</c:url>

												<sec:authorize
													access="hasAnyRole('ROLE_FACULTY', 'ROLE_ADMIN', 'ROLE_DEAN', 'ROLE_HOD')">
													<%-- <a class="btn btn-primary btn-sm pull-right" role="button"
														href="${addAnnouncementUrl}"><i class="fa fa-plus"
														aria-hidden="true"></i> Add Announcement</a> --%>
												</sec:authorize>
												<ul class="nav navbar-right panel_toolbox">
													<li><a class="collapse-link"><i
															class="fa fa-chevron-up"></i></a></li>
													<li><a class="close-link"><i class="fa fa-close"></i></a></li>
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
																	<td><c:out
																			value="${announcement.announcementSubType}" /></td>
																	<%-- <td><c:out value="${announcement.courseName}" /></td>
																	<td><c:out value="${announcement.programName}" /></td> --%>
																	<td><c:if
																			test="${announcement.announcementType eq 'INSTITUTE' }">
									ALL COURSES
								</c:if> <c:if test="${announcement.announcementType eq 'COURSE' }">
																			${announcement.courseName}
																		</c:if></td>
																	<td><c:if
																			test="${announcement.announcementType eq 'INSTITUTE' }">
									ALL PROGRAMS
								</c:if> <c:if test="${announcement.announcementType eq 'COURSE' }">
																			${announcement.programName}
																		</c:if> <c:if
																			test="${announcement.announcementType eq 'PROGRAM' }">
																			${announcement.programName}
																		</c:if></td>
																	<td><c:out value="${announcement.startDate}" /></td>
																	<td><c:out value="${announcement.endDate}" /></td>

																	<td><sec:authorize
																			access="hasAnyRole('ROLE_ADMIN', 'ROLE_DEAN','ROLE_HOD')">

																			<c:url value="viewAnnouncement" var="detailsUrl">
																				<c:param name="id" value="${announcement.id}" />
																			</c:url>
																			<c:choose>
																				<c:when
																					test="${announcement.announcementType eq 'PROGRAM' }">
																					<c:url value="addAnnouncementFormProgram"
																						var="editurl">
																						<c:param name="id" value="${announcement.id}" />
																					</c:url>
																				</c:when>
																				<c:otherwise>
																					<c:url value="addAnnouncementForm" var="editurl">
																						<c:param name="id" value="${announcement.id}" />
																					</c:url>
																				</c:otherwise>
																			</c:choose>

																			<c:url value="deleteAnnouncement" var="deleteurl">
																				<c:param name="id" value="${announcement.id}" />
																			</c:url>

																			<a href="${detailsUrl}" title="Details"><i
																				class="fa fa-info-circle fa-lg"></i></a>&nbsp;
                                                      <a
																				href="${editurl}" title="Edit"><i
																				class="fa fa-pencil-square-o fa-lg"></i></a>&nbsp;
                                                      <a
																				href="${deleteurl}" title="Delete"
																				onclick="return confirm('Are you sure you want to delete this record?')"><i
																				class="fa fa-trash-o fa-lg"></i></a>


																		</sec:authorize> <sec:authorize
																			access="hasAnyRole('ROLE_FACULTY', 'ROLE_HOD')">

																			<c:url value="viewAnnouncement" var="detailsUrl">
																				<c:param name="id" value="${announcement.id}" />
																			</c:url>

																			<c:url value="addAnnouncementForm" var="editurl">
																				<c:param name="id" value="${announcement.id}" />
																			</c:url>
																			<c:url value="deleteAnnouncement" var="deleteurl">
																				<c:param name="id" value="${announcement.id}" />
																			</c:url>

																			<a href="${detailsUrl}" title="Details"><i
																				class="fa fa-info-circle fa-lg"></i></a>&nbsp;
                                                    
                                                     
                                                      <c:if
																				test="${announcement.createdBy == username}">
																				<a href="${editurl}" title="Edit"><i
																					class="fa fa-pencil-square-o fa-lg"></i></a>&nbsp;
                                                      <a
																					href="${deleteurl}" title="Delete"
																					onclick="return confirm('Are you sure you want to delete this record?')"><i
																					class="fa fa-trash-o fa-lg"></i></a>


																			</c:if>


																		</sec:authorize> <sec:authorize access="hasAnyRole('ROLE_STUDENT')">
																			<c:url value="viewAnnouncement" var="detailsUrl">
																				<c:param name="id" value="${announcement.id}" />
																			</c:url>
																			<a href="${detailsUrl}" title="Details">View</a>
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
