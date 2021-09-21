<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<html lang="en">
<jsp:include page="../common/css.jsp" />

<body class="nav-md footer_fixed">

	<div class="loader"></div>


	<div class="container body">
		<div class="main_container">


			<jsp:include page="../common/leftSidebar.jsp">
				<jsp:param name="programId" value="${programId}" />
			</jsp:include>



			<jsp:include page="../common/alert.jsp" />
			<jsp:include page="../common/topHeader.jsp" />


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
							<i class="fa fa-angle-right"></i> My Program Students
						</div>

						<!-- Input Form Panel -->
						<div class="row">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">

									<div class="x_title">
										<h2>My Program Students for ${Program_Name}</h2>
										<ul class="nav navbar-right panel_toolbox">
											<li><a class="collapse-link"><i
													class="fa fa-chevron-up"></i></a></li>
										</ul>
										<div class="clearfix"></div>
									</div>

									<div class="x_content">
										<form:form action="" method="post" modelAttribute="user"
											enctype="multipart/form-data">


											<form:input path="programId" type="hidden" />
											<%-- <div class="row">
												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<form:label path="programId" for="programId">Program</form:label>
														<form:select id="courseIdForForum" path="programId"
															class="form-control">
															<form:option value="">Select Program</form:option>
															<c:forEach var="program" items="${allPrograms}"
																varStatus="status">
																<form:option value="${program.id}">${program.programName}</form:option>
															</c:forEach>
														</form:select>
													</div>
												</div>
											</div> --%>
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
										<h2>Students | ${fn:length(students)} Records found</h2>
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
												<label class=""></label>${fn:length(students)} Students
											</p>
										</div>
									</div>
									<div class="x_content">
										<form:form action="" method="post" modelAttribute="user"
											enctype="multipart/form-data">
											<form:input path="programId" type="hidden" />
											<div class="table-responsive">
												<table class="table  table-hover">
													<thead>
														<tr>
															<th>Sr. No.</th>


															<th>SAP IDs <i class="fa fa-sort" aria-hidden="true"
																style="cursor: pointer"></i></th>
															<th>Roll No. <i class="fa fa-sort"
																aria-hidden="true" style="cursor: pointer"></i></th>
															<th>Student Name <i class="fa fa-sort"
																aria-hidden="true" style="cursor: pointer"></i></th>
															<th>Email Id <i class="fa fa-sort"
																aria-hidden="true" style="cursor: pointer"></i></th>

															<th>Contact No. <i class="fa fa-sort"
																aria-hidden="true" style="cursor: pointer"></i></th>

															<th>Session <i class="fa fa-sort" aria-hidden="true"
																style="cursor: pointer"></i></th>
														</tr>
													</thead>
													<tfoot>
														<tr>
															<th></th>
															<th>SAP IDs</th>
															<th>Roll No.</th>
															<th>Student Name</th>
															<th>Email Id</th>
															<th>Contact No.</th>
															<th>Session</th>
														</tr>
													</tfoot>
													<tbody>

														<c:forEach var="student" items="${students}"
															varStatus="status">
															<tr>
																<td><c:out value="${status.count}" /></td>


																<td><c:out value="${student.username}" /></td>
																<td><c:out value="${student.rollNo}" /></td>
																<td><c:out
																		value="${student.firstname} ${student.lastname}" /></td>
																<td><c:out value="${student.email}" /></td>
																<td><c:out value="${student.mobile}" /></td>
																<td><c:out value="${student.acadSession}" /></td>
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
									window.location = '${pageContext.request.contextPath}/showMyProgramStudentsForAdmin?programId='
											+ encodeURIComponent(selectedValue);
									return false;
								});
			</script>

		</div>
	</div>





</body>
</html>

