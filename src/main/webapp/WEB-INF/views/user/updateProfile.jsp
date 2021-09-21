<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html lang="en">
<jsp:include page="../common/css.jsp" />

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
							<a href="${pageContext.request.contextPath}/homepage">Dashboard</a> <i class="fa fa-angle-right"></i>
							Update Profile
						</div>
						<jsp:include page="../common/alert.jsp" />

						<!-- Input Form Panel -->
						<div class="row">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">

									<div class="x_title">
										<h2>Update Profile</h2>
										<ul class="nav navbar-right panel_toolbox">
											<li><a class="collapse-link"><i
													class="fa fa-chevron-up"></i></a></li>
										</ul>
										<div class="clearfix"></div>
									</div>

									<div class="x_content">
										<form:form action="updateProfile" id="user" method="post"
											modelAttribute="user">


											<div class="row">
												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<label for="abbr">First Name:</label> ${user.firstname }
													</div>
												</div>
												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<label for="abbr">Last Name:</label> ${user.lastname }
													</div>
												</div>
												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<label for="email">Email <span style="color: red">*</span></label>
														<form:input path="email" type="email"
															placeholder="Enter Email ID" class="form-control"
															required="required" />
													</div>
												</div>
												<div class="col-md-4 col-sm-6 col-xs-12 column">
													
														<div class="form-group">
															<label for="mobile">Mobile <span style="color: red">*</span></label>
															<form:input path="mobile" type="text"
																placeholder="Enter Mobile" class="form-control"
																required="required" />
														
													</div>
												</div>
											<div class="clearfix"></div>
										
												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<button id="submit" class="btn btn-large btn-primary"
															formaction="updateProfile">Update</button>
														<button id="cancel" class="btn btn-danger"
															formaction="homepage" formnovalidate="formnovalidate">Cancel</button>
													</div>
												</div>

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

		</div>
	</div>





</body>
</html>
