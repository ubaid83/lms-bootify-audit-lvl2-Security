<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<html lang="en">
<head>
<style id="fancybox-style-noscroll" type="text/css">
.compensate-for-scrollbar, .fancybox-enabled body {
	margin-right: 17px;
}
</style>

</head>
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
							<i class="fa fa-angle-right"></i> Add Components Under Module
						</div>
						<jsp:include page="../common/alert.jsp" />
						<!-- Input Form Panel -->
						<div class="row">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">

									<div class="x_title">
										<h2>Add a Component For - ${moduleName}</h2>
										<ul class="nav navbar-right panel_toolbox">
											<li><a class="collapse-link"><i
													class="fa fa-chevron-up"></i></a></li>
										</ul>
										<div class="clearfix"></div>
									</div>

									<div class="x_content">
										<form:form action="addComponentsUnderModule" method="post"
											modelAttribute="icaComponent">




											<div class="row">
												<form:input type="hidden" path="icaId"
													value="${icaComponent.icaId}" />
												<form:input type="hidden" path="moduleName"
													value="${icaComponent.moduleName}" />

												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<form:label path="componentName" for="componentName">Component Name <span
																style="color: red">*</span>
														</form:label>
														<form:input path="componentName" type="text"
															class="form-control" required="required" />
													</div>
												</div>

												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<form:label path="componentDesc" for="componentDesc">Component Description <span
																style="color: red">*</span>
														</form:label>
														<form:textarea rows="4" cols="15" path="componentDesc"
															type="text" class="form-control" required="required" />
													</div>
												</div>








												<div class="col-sm-12 column">
													<div class="form-group">
														<button id="submit" name="submit"
															class="btn btn-large btn-primary">Add Component</button>
														<input id="reset" type="reset" class="btn btn-danger">
														<button id="cancel" name="cancel" class="btn btn-danger"
															formnovalidate="formnovalidate" formaction="homepage">Cancel</button>
													</div>
												</div>
											</div>

										</form:form>
									</div>
								</div>
							</div>
						</div>
						<c:choose>
							<c:when test="${icaComponentListByIcaId.size() > 0}">
								<!-- Results Panel -->
								<div class="row">
									<div class="col-xs-12 col-sm-12">
										<div class="x_panel">
											<div class="x_title">
												<h2>Components</h2>



												<ul class="nav navbar-right panel_toolbox">
													<li><a class="collapse-link"><i
															class="fa fa-chevron-up"></i></a></li>
													<li><a class="close-link"><i class="fa fa-close"></i></a></li>
												</ul>
												<div class="clearfix"></div>
											</div>
											<div class="x_content">
												<table>
													<thead>
														<tr>
															<th>AcadYear</th>
															<th>Module</th>
															<th>Program</th>
															<c:if test="${icaBean.campusId ne null}">
																<th>Campus</th>
															</c:if>
														</tr>
													</thead>
													<tbody>
														<tr>
															<td>${icaBean.acadYear}</td>
															<td>${icaBean.moduleName}</td>
															<td>${icaBean.programName}</td>

															<c:if test="${icaBean.campusId ne null}">
																<td>${icaBean.campusName}</td>
															</c:if>
														</tr>
													</tbody>
												</table>
											</div>
											<div class="x_content">
												<div class="table-responsive">
													<table class="table table-hover " id="example">
														<thead>
															<tr>
																<th>Component No</th>

																<th>Component Name <i class="fa fa-sort"
																	aria-hidden="true" style="cursor: pointer"></i></th>

																<th>Component Description<i class="fa fa-sort"
																	aria-hidden="true" style="cursor: pointer"></i></th>




																<th>Actions</th>

																<!-- <th>Course Custom Property 1</th>
							<th>Course Custom Property 2</th>
							<th>Course Custom Property 3</th> -->
																<!-- <th>Actions</th> -->
															</tr>
														</thead>
														<tfoot>
															<tr>
																<th></th>
																<th>Component No</th>
																<!-- <th>Course abbr</th> -->
																<th>Component Name</th>

																<th>Component Description</th>



															</tr>
														</tfoot>
														<tbody>






															<c:forEach items="${icaComponentListByIcaId}" var="comp"
																varStatus="status">

																<tr>



																	<td><c:out value="Component-${status.count}" /></td>
																	<td><c:out value="${comp.componentName}" /></td>
																	<td><c:out value="${comp.componentDesc}" /></td>





																	<td><c:url value="addComponentsUnderModule"
																			var="addComponents">
																			<c:param name="moduleId" value="${module.moduleId}" />
																			<c:if test="${moduleComponent.campusId ne null}">
																				<c:param name="campusId"
																					value="${moduleComponent.campusId}" />
																			</c:if>
																			<c:param name="acadYear" value="${module.acadYear}" />
																			<c:param name="programId"
																				value="${moduleComponent.programId}" />
																		</c:url> <a href="${addComponents}" title="Add Components"><i
																			class="fa fa-plus fa-lg"
																			onclick="callFancyBox1('${module.moduleId}')"></i></a></td>



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




	<script>
		$(document).ready(function() {

			// Initialize select2
			$("#moduleIdSelect").select2();
		});
	</script>

</body>

</html>

