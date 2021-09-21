<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<html lang="en">
<jsp:include page="../common/css.jsp" />

<body class="nav-md footer_fixed">

	<div class="loader"></div>


	<div class="container body">
		<div class="main_container">

			<jsp:include page="../common/topHeaderLibrian.jsp" />

			<!-- page content: START -->
			<div class="right_col" role="main">

				<div class="dashboard_container">

					<div class="dashboard_container_spacing">

						<div class="breadcrumb">
							<a href="${pageContext.request.contextPath}/homepage">Dashboard</a>
							<i class="fa fa-angle-right"></i> Profile
						</div>
						<jsp:include page="../common/alert.jsp" />

						<!-- Input Form Panel -->
						<div class="row">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">

									<div class="x_title">
										<h2>Change Password</h2>
										<ul class="nav navbar-right panel_toolbox">
											<li><a class="collapse-link"><i
													class="fa fa-chevron-up"></i></a></li>
										</ul>
										<div class="clearfix"></div>
									</div>

									<div class="x_content">
										<form:form action="changePassword" id="" method="post"
											modelAttribute="user">

											<form:input path="username" type="hidden" />
											<form:input path="id" type="hidden" />

											<div class="row">
												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">

														<form:password path="oldPassword" cssClass="form-control"
															placeholder="Current Password" id="oldPassword"
															required="required" />
														<label><i class="fa fa-key"></i> <span style="color: red">*</span></label>

													</div>
												</div>

												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<form:password path="newPassword" cssClass="form-control"
															placeholder="Enter New Password" id="password"
															required="required" />
														<label><i class="fa fa-lock"></i> <span style="color: red">*</span></label>
													</div>
												</div>
												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<form:password path="reenterPassword"
															name="reenterPassword"
															placeholder="Re Enter New Password" id="reenterPassword"
															class="form-control" required="required" />
														<label><i class="fa fa-lock"></i> <span style="color: red">*</span></label>
													</div>
												</div>

											</div>



											<br>
											<button id="submit" class="btn btn-danger"
												formaction="changePassword" formnovalidate="formnovalidate">Change
												Password Details</button>

											<button id="cancel" class="btn btn-danger"
												formaction="homepage" formnovalidate="formnovalidate">BACK</button>
										</form:form>
									</div>
								</div>
							</div>
						</div>


					</div>



				</div>

			</div>
			<!-- /page content: END -->


			<jsp:include page="../common/footerLibrarian.jsp" />

		</div>
	</div>





</body>
</html>
