<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<html lang="en">
<jsp:include page="../common/css.jsp" />
<%
	boolean isEdit = "true".equals((String) request
			.getAttribute("edit"));
%>

<body class="nav-md footer_fixed">

	<div class="loader"></div>


	<div class="container body">
		<div class="main_container">

			<jsp:include page="../common/leftSidebar.jsp">
				<jsp:param name="courseId" value="${courseId}" />
			</jsp:include>

			<jsp:include page="../common/topHeader.jsp" />



			<!-- page content: START -->
			<div class="right_col" role="main">

				<div class="dashboard_container">

					<div class="dashboard_container_spacing">

						<div class="breadcrumb">
						
						<c:out value="${Program_Name}" />
						
						<sec:authorize access="hasRole('ROLE_STUDENT')">
						
						<i
						class="fa fa-angle-right"></i>
											
											<c:out value="${AcadSession}" />
											
											</sec:authorize>
						<br><br>
						
							<a href="${pageContext.request.contextPath}/homepage">Dashboard</a>
							<i class="fa fa-angle-right"></i> Assign Weight
						</div>
						<jsp:include page="../common/alert.jsp" />

						<!-- Input Form Panel -->
						<div class="row">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">

									<div class="x_title">
										<h2>Assign Internal Weight</h2>
										<ul class="nav navbar-right panel_toolbox">
											<li><a class="collapse-link"><i
													class="fa fa-chevron-up"></i></a></li>
										</ul>
										<div class="clearfix"></div>
									</div>

									<div class="x_content">
										<form:form action="internalWieghtage" id="internalWieghtage"
											method="post" modelAttribute="wieghtage"
											enctype="multipart/form-data">
											<fieldset>

												<form:input path="courseId" type="hidden" />
												<%
													if (isEdit) {
												%>
												<form:input type="hidden" path="id" />
												<%
													}
												%>
												<div class="row">

													<div class="col-md-4 col-sm-6 col-xs-12 column">
														<div class="form-group">
															<form:label path="courseId" for="courseId">Course <span style="color: red">*</span></form:label>
															<form:select id="courseIdWieghtage" path="courseId" required="required"
																class="form-control">
																<form:option value="">Select Course</form:option>
																<c:forEach var="course" items="${courses}"
																	varStatus="status">
																	<form:option value="${course.id}">${course.courseName}</form:option>
																</c:forEach>
															</form:select>
														</div>
													</div>

												</div>

												<button id="wieghtageData" class="btn btn-large btn-primary">Assign
													Weight</button>

												<button id="cancel" class="btn btn-danger"
													formaction="homepage" formnovalidate="formnovalidate">Cancel</button>





											</fieldset>
										</form:form>
									</div>
								</div>
							</div>
						</div>

						<!-- Results Panel -->


					</div>



				</div>

			</div>
			<!-- /page content: END -->


			<jsp:include page="../common/footer.jsp" />

		</div>
	</div>





</body>
<script>
	$(document)
			.ready(
					function() {
						$("#courseIdWieghtage")
								.change(
										function() {
											console.log(" course id"
													+ $(this).val());
											var courseId = $(this).val();
											var url = '${pageContext.request.contextPath}/addInternalWieghtageForm?courseId='
													+ courseId;
											$("#wieghtageData").attr(
													'formaction', url);
										});
					});
</script>
</html>
