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
			<jsp:include page="../common/topHeader.jsp">
				<jsp:param name="courseMenu" value="activeMenu" />
			</jsp:include>



			<!-- page content: START -->
			<!--  <div class="right_col" role="main"> -->
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
							<i class="fa fa-angle-right"></i> View Student Escalated Services
						</div>
						<jsp:include page="../common/alert.jsp" />
						<!-- Input Form Panel -->

						<c:choose>
							<c:when test="${serviceList.size() > 0}">
								<!-- Results Panel -->
								<div class="row">
									<div class="col-xs-12 col-sm-12">
										<div class="x_panel">
											<div class="x_title">
												<h2>Service List | ${serviceList.size()} Records Found</h2>

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
																<th>Service Name</th>
																<th>Action</th>

															</tr>
														</thead>

														<tbody>

															<c:forEach var="service" items="${serviceList}"
																varStatus="status">
																<c:if test="${service.level3 eq username}">
																<tr>
																	<td><c:out value="${status.count}" /></td>
																	<td><c:out value="${service.name}" /></td>
																	<td><sec:authorize
																			access="hasAnyRole('ROLE_ADMIN','ROLE_STAFF')">
																			<c:if test="${service.name eq 'Bonafide Service'}">
																			<c:url value="viewBonafideServiceForLevel3" var="detailsUrl">
																				<c:param name="serviceId" value="${service.id}" />
																			</c:url>
																			<a href="${detailsUrl}" title="Details"><i
																				class="fa fa-info-circle fa-lg"></i></a>
																			</c:if>
																			<c:if test="${service.name eq 'Railway Service'}">
																			<c:url value="viewRCServiceForLevel3" var="detailsUrl">
																				<c:param name="serviceId" value="${service.id}" />
																			</c:url>
																			<a href="${detailsUrl}" title="Details"><i
																				class="fa fa-info-circle fa-lg"></i></a>
																			</c:if>


																		</sec:authorize> </td>

																</tr>
																</c:if>
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
