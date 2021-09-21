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

<div class="d-flex dataTableBottom tabPage" id="facultyAssignmentPage">
	<sec:authorize access="hasRole('ROLE_FACULTY')">
		<jsp:include page="../common/newLeftNavBarFaculty.jsp"/>
		<jsp:include page="../common/newLeftSidebarFaculty.jsp"/>
		<jsp:include page="../common/rightSidebarFaculty.jsp"/>
	</sec:authorize>
	<sec:authorize access="hasRole('ROLE_STUDENT')">
		<jsp:include page="../common/newLeftNavbar.jsp" />
		<jsp:include page="../common/newLeftSidebar.jsp" />
		<jsp:include page="../common/rightSidebar.jsp" />
	</sec:authorize>

	<!-- DASHBOARD BODY STARTS HERE -->

	<div class="container-fluid m-0 p-0 dashboardWraper">

		<sec:authorize access="hasRole('ROLE_FACULTY')">
			<jsp:include page="../common/newTopHeaderFaculty.jsp" />
		</sec:authorize>
		<sec:authorize access="hasRole('ROLE_STUDENT')">
			<jsp:include page="../common/newTopHeader.jsp" />
		</sec:authorize>

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
							<li class="breadcrumb-item active" aria-current="page">View
								My Message</li>
						</ol>
					</nav>

					<jsp:include page="../common/alert.jsp" />


					<!-- Input Form Panel -->
					<div class="card bg-white border">
						<div class="card-body">

							<ul class="row nav nav-tabs text-center" id="myTab"
								role="tablist">
								<li
									class="nav-item col-lg-4 col-md-4 col-sm-12 mt-3 ml-auto mr-auto">
									<a class="nav-link active" id="libDoc" data-toggle="tab"
									href="#inboxContent" role="tab">
										<div class="border">
											<div class="row asset1">
												<div class="col-12 pb-2">
													<i class="fas fa-inbox fa-2x text-white"></i>
												</div>
												<div class="col-12">
													<p>INBOX</p>
												</div>
											</div>
										</div>
								</a>
								</li>
								<li
									class="nav-item col-lg-4 col-md-4 col-sm-12 mt-3 ml-auto mr-auto">
									<a class="nav-link" id="libRes" data-toggle="tab"
									href="#outboxContent" role="tab">
										<div class="border">
											<div class="row asset1">
												<div class="col-12 pb-2">
													<i class="fas fa-sign-in-alt fa-2x text-white"></i>
												</div>
												<div class="col-12" id="outboxTab">
													<p>OutBox</p>
												</div>
											</div>
										</div>
								</a>
								</li>

							</ul>

							<div class="tab-content" id="myTabContent">

								<div class="tab-pane fade show active" id="inboxContent"
									role="tabpanel" aria-labelledby="home-tab">

									<div class="login-block-form">

										<form:form cssClass="form" role="form" action="viewMyMessage"
											method="post" modelAttribute="message" id="viewMyMessage">

											<div class="table-responsive">

												<table id="inboxTable"
													class="table table-striped table-hover"
													style="font-size: 12px">

													<thead>

														<tr>

															<th>Sr. No.</th>

															<th>Message By</th>

															<th>Subject</th>

															<th>Date</th>


															<th>Actions</th>

														</tr>

													</thead>

													<tbody>

														<c:forEach var="message" items="${listOfAllMessages}"
															varStatus="status">

															<tr>

																<td><c:out value="${status.count}" /></td>

																<td><c:out value="${message.createdBy} " /></td>

																<td><c:out value="${message.subject}" /></td>

																<td><c:out value="${message.createdDate}" /></td>



																<td><c:url value="giveResponseToMessage"
																		var="respondToMessage">
																		<c:param name="id" value="${message.id}" />
																	</c:url> <a href="${respondToMessage}" title="Response">Reply</a></td>
														</c:forEach>

													</tbody>

												</table>

											</div>

										</form:form>

									</div>


								</div>



								<div class="tab-pane fade show active" id="outboxContent"
									role="tabpanel" aria-labelledby="home-tab">
									<div class="register-block-form">

										<form:form cssClass="form" role="form" action="viewMyMessage"
											method="post" modelAttribute="message" id="viewMyMessage">

											<div class="table-responsive">

												<table id="outboxTable"
													class="table table-striped table-hover"
													style="font-size: 12px">

													<thead>

														<tr>

															<th>Sr. No.</th>

															<th>Sent To</th>

															<th>Subject</th>

															<th>Date</th>

															<th>Actions</th>

														</tr>

													</thead>

													<tbody>

														<c:forEach var="message" items="${listOfSentMessages}"
															varStatus="status">

															<tr>

																<td><c:out value="${status.count}" /></td>

																<td><c:out value="${message.username} " /></td>

																<td><c:out value="${message.subject}" /></td>

																<td><c:out value="${message.createdDate}" /></td>

																<td><c:url value="viewOutboxMessage"
																		var="viewMessage">
																		<c:param name="id" value="${message.id}" />
																	</c:url> <a href="${viewMessage}" title="Response">View
																		Message</a></td>
														</c:forEach>

													</tbody>

												</table>

											</div>

										</form:form>
									</div>

								</div>
							</div>


							<%-- <div class="col-xs-12 col-sm-12">

								<div class="x_panel">

									<div class="ui-105-content">

										<ul class="nav nav-tabs nav-justified">

											<li class="link-one"><a href="#login-block"
												data-toggle="tab"><i class="fas fa-inbox"></i>INBOX</a></li>

											<li class="link-two" id="link-two"><a
												href="#register-block" data-toggle="tab"><i
													class="fas fa-sign-in-alt"></i>OUTBOX</a></li>

										</ul>

										<div class="tab-content">

											<div class="tab-pane active fade in" id="login-block">

												<!-- Login Block Form -->

												<div class="login-block-form">

													<form:form cssClass="form" role="form"
														action="viewMyMessage" method="post"
														modelAttribute="message" id="viewMyMessage">

														<div class="table-responsive">

															<table id="inboxTable"
																class="table table-striped table-hover"
																style="font-size: 12px">

																<thead>

																	<tr>

																		<th>Sr. No.</th>

																		<th>Message By</th>

																		<th>Subject</th>

																		<th>Date</th>


																		<th>Actions</th>

																	</tr>

																</thead>

																<tbody>

																	<c:forEach var="message" items="${listOfAllMessages}"
																		varStatus="status">

																		<tr>

																			<td><c:out value="${status.count}" /></td>

																			<td><c:out value="${message.createdBy} " /></td>

																			<td><c:out value="${message.subject}" /></td>

																			<td><c:out value="${message.createdDate}" /></td>



																			<td><c:url value="giveResponseToMessage"
																					var="respondToMessage">
																					<c:param name="id" value="${message.id}" />
																				</c:url> <a href="${respondToMessage}" title="Response">Reply</a></td>
																	</c:forEach>

																</tbody>

															</table>

														</div>

													</form:form>

												</div>

											</div>

											<!-- Results Panel -->

											<div class="tab-pane active fade in" id="register-block">

												<!-- Login Block Form -->

												<div class="register-block-form">

													<form:form cssClass="form" role="form"
														action="viewMyMessage" method="post"
														modelAttribute="message" id="viewMyMessage">

														<div class="table-responsive">

															<table id="outboxTable"
																class="table table-striped table-hover"
																style="font-size: 12px">

																<thead>

																	<tr>

																		<th>Sr. No.</th>

																		<th>Sent To</th>

																		<th>Subject</th>

																		<th>Date</th>

																		<th>Actions</th>

																	</tr>

																</thead>

																<tbody>

																	<c:forEach var="message" items="${listOfSentMessages}"
																		varStatus="status">

																		<tr>

																			<td><c:out value="${status.count}" /></td>

																			<td><c:out value="${message.username} " /></td>

																			<td><c:out value="${message.subject}" /></td>

																			<td><c:out value="${message.createdDate}" /></td>

																			<td><c:url value="viewOutboxMessage"
																					var="viewMessage">
																					<c:param name="id" value="${message.id}" />
																				</c:url> <a href="${viewMessage}" title="Response">View
																					Message</a></td>
																	</c:forEach>

																</tbody>

															</table>

														</div>

													</form:form>
												</div>

											</div>

										</div>

									</div>

								</div>

							</div> --%>
						</div>
					</div>

					<!-- Results Panel -->





					<%-- 	<jsp:include page="../common/paginate.jsp">
							<jsp:param name="baseUrl" value="searchFacultyGroups" />
						</jsp:include> --%>

					<!-- /page content: END -->

				</div>

				<!-- SIDEBAR START -->
				<jsp:include page="../common/newSidebar.jsp" />
				<!-- SIDEBAR END -->
				<jsp:include page="../common/footer.jsp" />

				<script>
					$(document)

					.ready(function() {

						$("#outboxContent").hide();

						$("#outboxTab").on("click", function() {
							console.log('outbox tabl');
							$("#outboxContent").show();

						});

					});
				</script>