
<jsp:include page="../common/topHeaderLibrian.jsp" />
<jsp:include page="../common/css.jsp" />
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<body class="nav-md footer_fixed dashboard_left">

	<!-- Example row of columns -->
	<div class="loader"></div>
	<div class="container body">
		<div class="main_container">


			<!--  -->
			<div class="right_col" role="main">

				<div class="dashboard_container">

					<!-- <div class="dash-main"> -->
					<div class="dashboard_height" id="main">


						<div class="dashboard_contain_specing dash-main">



							<div class="row">
							<div class="col-xs-12 col-sm-12">
							<form:form action="searchStudentCopyLeaksAudit" method="post"
											modelAttribute="leaks">
								<div class="x_panel">

									<div class="x_title">
										<h2>Check Student Copy Leaks Audit List</h2>
										<ul class="nav navbar-right panel_toolbox">
											<li><a class="collapse-link"><i
													class="fa fa-chevron-up"></i></a></li>
										</ul>
										<div class="clearfix"></div>
									</div>
									<div class="col-xs-12 column" align="right">
										<a href="getAssignmentStatus"
													class="btn btn-large btn-primary float-right">Get Assignment Submission Count</a>
										</div>
								
									<div class="x_content">
										
											<fieldset>

												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<form:label path="username" for="username">Search Student </form:label>
														<form:input id="username" path="username" class="form-control"
														placeholder="User SapId" required="required"/>
													</div>
												</div>



												<div class="col-sm-12 column">
													<div class="form-group">
														<button id="submit" name="submit"
															class="btn btn-large btn-primary">Search</button>
														<!-- <input id="reset" type="reset" class="btn btn-danger"> -->
														<button id="cancel" name="cancel" class="btn btn-danger"
															formaction="homepage"
															formnovalidate="formnovalidate">Cancel</button>
													</div>
												</div>
											</fieldset>
									</div>
									
								</div>
								
								<div class="x_panel">

									<div class="x_title">
										<h2>Student List</h2>
										<ul class="nav navbar-right panel_toolbox">
											<li><a class="collapse-link"><i
													class="fa fa-chevron-up"></i></a></li>
										</ul>
										<div class="clearfix"></div>
									</div>

									<div class="x_content">
										<div class="table-responsive">
													<table id="showUsers" class="table table-hover ">
														<thead>
															<tr>
																<th>Sr. No</th>
																<th>Assignment ID</th>
																<th>Username</th>
																<th>Count</th>
																<th>Credit Used</th>
																<th>Action</th>
															</tr>
														</thead>
														<tbody>
															<c:forEach var="userList" items="${userList}"
																varStatus="status">
																<tr>
																	<td>${status.count}</td>
																	<td><strong>${userList.assignmentId}</strong></td>
																	<td><strong>${userList.username}</strong></td>
																	<td>${userList.count}</td>
																	<td>${userList.creditUsed}</td>
																	<td><c:url value="deleteStudentCopyLeaks"
																var="deleteEntry">
																<c:param name="id" value="${userList.id}" />
																<c:param name="username" value="${userList.username}" />
															</c:url> <a href="${deleteEntry}" title="Delete Entry"><i
																class="fa fa-trash fa-lg"></i></a></td>
																</tr>

															</c:forEach>
														</tbody>
													</table>
												</div>
									</div>
									<div class="alert alert-dismissible" role="alert">${note}</div>
								</div>
								</form:form>
							</div>
						</div>
						</div>
					</div>
				</div>


			</div>
		</div>
	</div>
</body>

<jsp:include page="../common/footerLibrarian.jsp" />

<script>
	$(document).ready(
			function() {
				var cars = [ "bgcolor1", "bgcolor2", "bgcolor3", "bgcolor4",
						"bgcolor5", "bgcolor6", "bgcolor7", "bgcolor8",
						"bgcolor9" ];
				var count = 0;
				$('[id^=courseDetail]').each(function() {
					if (count == cars.length - 1) {
						count = 0;
					}
					$(this).addClass(cars[count]);
					count++;
				})

				$('body').addClass("dashboard_left");
			});
</script>



