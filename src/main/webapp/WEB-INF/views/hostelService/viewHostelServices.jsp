<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<html lang="en">
<jsp:include page="../common/css.jsp" />
<style>
input {
	display: none;
}

.button {
	display: inline-block;
	position: relative;
	width: 20px;
	height: 20px;
	margin: 0px;
	cursor: pointer;
}

.button span {
	display: block;
	position: absolute;
	width: 15px;
	height: 15px;
	padding: 0;
	top: 50%;
	left: 50%;
	-webkit-transform: translate(-50%, -50%);
	-ms-transform: translate(-50%, -50%);
	-o-transform: translate(-50%, -50%);
	transform: translate(-50%, -50%);
	border-radius: 100%;
	background: #eeeeee;
	box-shadow: 0 2px 5px 0 rgba(0, 0, 0, 0.26);
	transition: ease .3s;
}

.button span:hover {
	padding: 10px;
	/* -ms-transform: scale(1.5); 
	-webkit-transform: scale(1.5);
	transform: scale(1.5); */
}

.orange .button span {
	background: red;
}

.amber .button span {
	background: yellow;
}

.lime .button span {
	background: green;
}

.nocolor .button span {
	background: #b9b6b0;
}

.layer {
	display: block;
	position: absolute;
	width: 100%;
	height: 100%;
	top: 0;
	left: 0;
	background: transparent;
	/*transition: ease .3s;*/
	z-index: -1;
}

.orange input:checked ~ .layer {
	background: #F4511E;
}

.amber input:checked ~ .layer {
	background: #FFB300;
}

.lime input:checked ~ .layer {
	background: #7CB342;
}
</style>
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
							<i class="fa fa-angle-right"></i> View Hostel Services
						</div>
						<jsp:include page="../common/alert.jsp" />
						<!-- Input Form Panel -->

						<c:choose>
							<c:when test="${hfList.size() > 0}">
								<!-- Results Panel -->
								<div class="row">
									<div class="col-xs-12 col-sm-12">
										<div class="x_panel">
											<div class="x_title">
												<h2>Hostel Service List | ${hfList.size()} Records
													Found</h2>

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

																<th>Location</th>
																<th>Total count</th>
																<th>Total Fees</th>
																<th>Available Count</th>
																<th>Action</th>




															</tr>
														</thead>

														<tbody>

															<c:forEach var="service" items="${hfList}"
																varStatus="status">
																<tr>
																	<td><c:out value="${status.count}" /></td>
																	<td><c:out value="${service.location}" /></td>
																	<td><c:out value="${service.totalSeats}" /></td>
																	<td><c:out value="${service.totalFees}" /></td>
																	<td><c:out value="${service.availCount}" /></td>
																	<td><c:url value="createHostelServiceForm"
																			var="detailsUrl">
																			<c:param name="id" value="${service.id}" />
																		</c:url> <a href="${detailsUrl}" title="Edit/Update/View"><i
																			class="fa fa-pencil-square-o fa-lg"></i></a>
																			
																			<c:url value="viewEachHostelService"
																			var="viewUrl">
																			<c:param name="id" value="${service.id}" />
																		</c:url> <a href="${viewUrl}" title="View Applications"><i
																			class="fa fa-info-circle fa-lg"></i></a>
																			
																			</td>



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


			<jsp:include page="../common/footer.jsp" />

		</div>
	</div>





</body>
</html>
