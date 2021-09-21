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

<div class="d-flex adminPage dataTableBottom">
	<jsp:include page="../common/newAdminLeftNavBar.jsp" />
	<jsp:include page="../common/rightSidebarAdmin.jsp" />


	<!-- DASHBOARD BODY STARTS HERE -->

	<div class="container-fluid m-0 p-0 dashboardWraper">

		<jsp:include page="../common/newAdminTopHeader.jsp" />

		<!-- SEMESTER CONTENT -->
		<div class="col-lg-12 col-md-12 col-sm-12 dashboardBody p-5">


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
					<li class="breadcrumb-item active" aria-current="page">Query
						List</li>
				</ol>
			</nav>

			<jsp:include page="../common/alert.jsp" />


			<!-- Input Form Panel -->
			<div class="card bg-white border">
				<div class="card-body">
					<div class="col-xs-12 col-sm-12">

						<div class="x_panel">



							<div class="ui-105-content">
								<ul class="nav nav-tabs nav-justified" role="tablist">
									<li class="active nav-item"><a class="nav-link active" href="#login-block"
										data-toggle="tab"><i class="fas fa-inbox"></i>Queries
											Generated</a></li>


								</ul>

								<div class="tab-content">
									<div class="tab-pane fade show active" id="login-block">
										<!-- Login Block Form -->
										<div class="login-block-form">

											<form:form cssClass="form" role="form" action="viewQuery"
												method="post" modelAttribute="query" id="viewQuery">



												<div class="table-responsive">
													<table id="grievanceTable"
														class="table table-striped table-hover"
														style="font-size: 12px">
														<thead>
															<tr>
																<th>Sr. No.</th>
																<th>Username</th>
																<th>Query Description</th>

																<th>Actions</th>
															</tr>
														</thead>

														<tbody>

															<c:forEach var="queries" items="${listOfQueries}"
																varStatus="status">
																<tr>
																	<td><c:out value="${status.count}" /></td>
																	<td><c:out value="${queries.username} " /></td>
																	<td><c:out value="${queries.queryDesc}" /></td>

																	<td><c:url value="giveResponseToQuery"
																			var="respondToQuery">
																			<c:param name="id" value="${queries.id}" />
																		</c:url> <a href="${respondToQuery}" title="Response">Respond</a></td>
															</c:forEach>
														</tbody>

													</table>
												</div>


											</form:form>

										</div>

									</div>





									<!-- Results Panel -->













								</div>

							</div>

						</div>

					</div>
				</div>
			</div>

		</div>

		<!-- SIDEBAR START -->

		<!-- SIDEBAR END -->
		<jsp:include page="../common/newAdminFooter.jsp" />




		<script>
			$("#courseId")
					.on(
							'change',
							function() {
								var selectedValue = $(this).val();
								window.location = '${pageContext.request.contextPath}/viewQuery?courseId='
										+ encodeURIComponent(selectedValue);
								return false;
							});
		</script>