<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<html lang="en">
<jsp:include page="../common/css.jsp" />
<link
	href="https://gitcdn.github.io/bootstrap-toggle/2.2.2/css/bootstrap-toggle.min.css"
	rel="stylesheet">
<script
	src="https://gitcdn.github.io/bootstrap-toggle/2.2.2/js/bootstrap-toggle.min.js"></script>
<body class="nav-md footer_fixed">

	<div class="loader"></div>


	<div class="container body">
		<div class="main_container">

			<jsp:include page="../common/leftSidebar.jsp">
				<jsp:param name="courseId" value="${courseId}" />
			</jsp:include>




			<jsp:include page="../common/topHeader.jsp" />


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
							<i class="fa fa-angle-right"></i> View Component
						</div>
						<jsp:include page="../common/alert.jsp" />


						<!-- Input Form Panel -->
						<div class="row">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">

									<div class="x_title">
										<h2>Components</h2>
										<ul class="nav navbar-right panel_toolbox">
											<li><a class="collapse-link"><i
													class="fa fa-chevron-up"></i></a></li>
										</ul>
										<div class="clearfix"></div>
									</div>

									<div class="x_content">
										<form:form action="viewComponents" method="post"
											modelAttribute="comp" enctype="multipart/form-data">


											<form:input path="courseId" type="hidden" />
											<div class="row">
												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<form:label path="courseId" for="courseId">Course</form:label>
														<form:select id="courseIdForForum" path="courseId"
															class="form-control">
															<form:option value="">Select Course</form:option>
															<c:forEach var="course" items="${allCourses}"
																varStatus="status">
																<form:option value="${course.id}">${course.courseName}</form:option>
															</c:forEach>
														</form:select>
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
										<h2>Components | ${fn:length(allForums)} Records found</h2>
										<ul class="nav navbar-right panel_toolbox">
											<li><a class="collapse-link"><i
													class="fa fa-chevron-up"></i></a></li>
											<li><a class="close-link"><i class="fa fa-close"></i></a></li>
										</ul>
										<div class="clearfix"></div>
									</div>
									<div class="x_itemCount" style="display: none;">
										<div class="image_not_found">
											<i class="fa fa-newspaper-o"></i>
											<p>
												<label class=""></label>${fn:length(compList)} Components
											</p>
										</div>
									</div>
									<div class="x_content">
										<form:form action="viewComponents" method="post"
											modelAttribute="comp" enctype="multipart/form-data">
											<form:input path="courseId" type="hidden" />
											<div class="table-responsive">
												<table class="table  table-hover"
													style="font: small-caption;">
													<thead>
														<tr>
															<th>Sr. No.</th>
															<th>Component name</th>
															<th>Action</th>

														</tr>
													</thead>
													<tbody>

														<c:forEach var="comps" items="${compList}"
															varStatus="status">

															<tr>
																<td><c:out value="${status.count}" /></td>
																<td><c:out value="${comps}"></c:out></td>
																<td><c:url value="/addComponent" var="editurl">
																		<c:param name="compName" value="${comps}" />
																		<c:param name="courseId" value="${comp.courseId}" />

																	</c:url> <c:url value="deleteComponent" var="deleteurl">
																		<c:param name="compName" value="${comps}" />
																		<c:param name="courseId" value="${comp.courseId}" />
																	</c:url><a href="${editurl}" title="Edit/View"><i
																		class="fa fa-pencil-square-o fa-lg"></i></a>&nbsp; <a
																	href="${deleteurl}" title="Delete"><i
																		class="fa fa-trash-o fa-lg"></i></a></td>


															</tr>
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



				</div>

			</div>
			<!-- /page content: END -->


			<jsp:include page="../common/footer.jsp" />
			<script>
				$("#courseIdForForum")
						.on(
								'change',
								function() {

									//alert("Onchange Function called!");
									var selectedValue = $(this).val();
									window.location = '${pageContext.request.contextPath}/viewComponents?courseId='
											+ encodeURIComponent(selectedValue);
									return false;
								});
			</script>

		</div>
	</div>





</body>
</html>

