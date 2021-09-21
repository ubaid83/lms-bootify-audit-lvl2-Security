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
			<jsp:include page="../common/topHeaderLibrian.jsp">
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
							<i class="fa fa-angle-right"></i> View Libraries
						</div>
						<jsp:include page="../common/alert.jsp" />

						<c:choose>
							<c:when test="${librayList.size() > 0}">
								<!-- Results Panel -->
								<div class="row">
									<div class="col-xs-12 col-sm-12">
										<div class="x_panel">
											<div class="x_title">
												<h2>Library List | ${librayList.size()} Records Found</h2>

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
																<th>Library Name<i class="fa fa-sort"
																	aria-hidden="true" style="cursor: pointer"></i></th>

															</tr>
														</thead>

														<tbody>

															<c:forEach var="library" items="${librayList}"
																varStatus="status">
																<tr>
																	<td><c:out value="${status.count}" /></td>
																	<td><c:url value="viewThisLibrary"
																			var="detailsUrl">
																			<c:param name="libraryName" value="${library.abbr}" />
																		</c:url> <a href="${detailsUrl}" target="_blank"><c:out
																				value="${library.libraryName}" /></a></td>


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


			<jsp:include page="../common/footerLibrarian.jsp" />

		</div>
	</div>





</body>
</html>
