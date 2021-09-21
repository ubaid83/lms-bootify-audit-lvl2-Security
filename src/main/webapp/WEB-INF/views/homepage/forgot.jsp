<jsp:include page="header.jsp" />
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
	<section class="container">
		<jsp:include page="../common/alert.jsp" />
		<div class="row">
			<div class="row">
				<div class="col-md-4 col-md-offset-4">
					<div class="panel panel-default">
						<div class="panel-body">
							<div class="text-center">
								<img
									src="<c:url value="/resources/images/avatar_2x.png" />"
									class="profile-img-card" height="70">
								<h3 class="text-center">Forgot Password?</h3>
								<p>If you have forgotten your password - reset it here.</p>
								<div class="panel-body">

									<form class="form" action="<c:url value="/resetPassword" />" method="get">
										<!--start form-->
										<!--add form action as needed-->
										<fieldset>
											<div class="form-group">
												<div class="input-group">
													<span class="input-group-addon"><i
														class="glyphicon glyphicon-envelope color-blue"></i></span>
													<!--EMAIL ADDRESS-->
													<label>Enter your username<span style="color: red">*</span> :</label>
													<input id="userName" name="userName" placeholder="User name"
														class="form-control" type="text" value="${userName}"
														required>
												</div>
											</div>
											<div class="form-group">
												<input class="btn btn-lg btn-primary btn-block"
													value="Send My Password" type="submit">
											</div>
										</fieldset>
									</form>
									<!--/end form-->

								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</section>
	<!-- /container -->
<jsp:include page="footer.jsp" />