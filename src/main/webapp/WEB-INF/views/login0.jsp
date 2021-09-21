<!DOCTYPE html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!-->
<html class="no-js">
<!--<![endif]-->

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<head>
<title>Sign In</title>
</head>


<body>



	<div>
		<!-- Example row of columns -->
		<div class="row">
			<div class="col-sm-15 ">
				<h3>Login</h3>
				<div class="row">
					<div class="col-sm-12 ">
						<div class="">

							<form role="form" id="login-form"
								action="<c:url value="/j_spring_security_check" />"
								method="post">
								<div class="form-group">
									<label for="userId">User ID (Student Number)</label> <input
										type="text" class="form-control" id="userId" name="j_username"
										placeholder="Enter User Id" required />
								</div>
								<div class="form-group">
									<label for="password">Password</label>&nbsp; <a
										href="resetPasswordForm"> (Forgot Password?) </a> <input
										type="password" class="form-control" id="password"
										name="j_password" placeholder="Enter Password" required />
								</div>
								<button type="submit" class="btn btn-default">Sign In</button>
								<c:if test="${not empty param.error}">
									<font color="red"> Login error. <br /> Reason :
										${sessionScope["SPRING_SECURITY_LAST_EXCEPTION"].message}
									</font>
								</c:if>
							</form>



						</div>
					</div>


				</div>
			</div>
		</div>


	</div>



</body>
</html>
