<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<html lang="en">
<jsp:include page="../common/css.jsp" />
<jsp:include page="../common/alert.jsp" />
<body class="nav-md footer_fixed">

	<div class="loader"></div>


	<div class="container body">
		<div class="main_container">

			<jsp:include page="../common/leftSidebar.jsp">
				<jsp:param name="courseId" value="${courseId}" />
			</jsp:include>

			<jsp:include page="../common/topHeaderLibrian.jsp">
				<jsp:param name="Assignment" value="activeMenu" />
			</jsp:include>


			<!-- page content: START -->
			<div class="right_col" role="main">

				<div class="dashboard_container">

					<div class="dashboard_container_spacing">

						<div class="breadcrumb">
						

						

							<br> <br> <a
								href="${pageContext.request.contextPath}/homepage">Dashboard</a>
							<i class="fa fa-angle-right"></i>View User Details
						</div>
						<jsp:include page="../common/alert.jsp" />
						<!-- Input Form Panel -->
						<div class="row">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">

									<div class="x_title">
										<h2>View User Details</h2>
										<ul class="nav navbar-right panel_toolbox">
											<li><a class="collapse-link"><i
													class="fa fa-chevron-up"></i></a></li>
										</ul>
										<div class="clearfix"></div>
									</div>

									<div class="x_content">
										<form:form action="viewUserDetails" method="post"
											modelAttribute="user">
											<fieldset>




										

												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<form:label path="username" for="username">Enter User</form:label>
														<form:input id="username" path="username" class="form-control"
														placeholder="User SapId"/>
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
										</form:form>
									</div>
								</div>
								
								<div class="x_panel">

									<div class="x_title">
										<h2>User List</h2>
										<ul class="nav navbar-right panel_toolbox">
											<li><a class="collapse-link"><i
													class="fa fa-chevron-up"></i></a></li>
										</ul>
										<div class="clearfix"></div>
									</div>

									<div class="x_content">
										<div class="table-responsive">
													<table id="showUserDetails" class="table table-hover ">
													<thead>
															<tr>
																<th>Sr. No.</th>
																<th>User Name</th>
																<th>First Name</th>
																<th>Last Name</th>
																<th>Email</th>
																<th>Mobile</th>
																<th>Program Id</th>
																<th>Acad Session</th>
																<th>Enabled</th>
																<th>Campus Id</th>
																<th>Campus Name</th>
															</tr>
													</thead>
													<tbody>
													<c:forEach var="userList" items="${userList}"
																varStatus="status">
																
															
														<tr>
														
															<td>${status.count}</td>
															<td>${userList.username}</td>
															<td>${userList.firstname}</td>
															<td>${userList.lastname}</td>
															<td>${userList.email}</td>
															<td>${userList.mobile}</td>
															<td>${userList.programId}</td>
															<td>${userList.acadSession}</td>
															<td>${userList.enabled}</td>
															<td>${userList.campusId}</td>
															<td>${userList.campusName}</td>
														</tr>
													
													</c:forEach>
													</tbody>
													</table>
									    </div>
									</div>
									<div class="alert alert-dismissible" role="alert">${note}</div>
								</div>
							</div>
						</div>
						
						<jsp:include page="../common/paginate.jsp">
							<jsp:param name="baseUrl" value="evaluateByStudent" />
						</jsp:include>
					</div>



				</div>

			</div>
			<!-- /page content: END -->

			<jsp:include page="../common/paginate.jsp">
				<jsp:param name="baseUrl" value="evaluateByStudent" />
			</jsp:include>

			<jsp:include page="../common/footerLibrarian.jsp" />
			

		</div>
		</div>





</body>
</html>
