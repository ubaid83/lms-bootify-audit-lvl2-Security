<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8">
		<title>Login</title>
		
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		
		<!-- Styles -->
		<!-- Bootstrap CSS -->
		<link href="<c:url value="/resources/css/bootstrap.min.css" />" rel="stylesheet">
		<!-- Font awesome CSS -->
		<link href="<c:url value="/resources/css/font-awesome.min.css" />" rel="stylesheet">
		
		
		<!-- Main CSS -->
		<link href="<c:url value="/resources/css/ui.css" />" rel="stylesheet">
				
		<!-- Favicon -->
		<!-- <link rel="shortcut icon" href="#"> -->
	</head>
	
	<body>
	
		<!-- UI # -->
		
		<div class="ui-105">
			<!-- Combined Form Content -->
			<div class="ui-105-content">
				<ul class="nav nav-tabs nav-justified">
					  <li class="active link-one"><a href="#login-block" data-toggle="tab"><i class="fa fa-sign-in"></i>Sign In</a></li>
					  <li class="link-two"><a href="#register-block" data-toggle="tab"><i class="fa fa-pencil"></i>Sign Up</a></li>
					  <li class="link-three"><a href="#contact-block" data-toggle="tab"><i class="fa fa-envelope"></i>Contact</a></li>
				</ul>
				<div class="tab-content">
					<div class="tab-pane active fade in" id="login-block">
						<!-- Login Block Form -->
						<div class="login-block-form">
							<!-- Heading -->
							<h4>Sign In to your Account</h4>
							<!-- Border -->
							<div class="bor bg-green"></div>
							<!-- Form -->
							<form class="form" role="form">
								<!-- Form Group -->
								<div class="form-group">
									<!-- Label -->
									<label class="control-label">Username</label>
									<!-- Input -->
									<input type="text" class="form-control" placeholder="Enter Username">
								</div>
								<div class="form-group">
									<label class="control-label">Password</label>
									 <input type="password" class="form-control" placeholder="Enter Password">
								</div>
								<div class="form-group">
									<div class="checkbox">
										<label>
											<input type="checkbox"> Remember
										</label>
									</div>
								</div>
								<div class="form-group">
									<!-- Button -->
									<button type="submit" class="btn btn-red">Sign In</button>&nbsp;
									<input  id="reset" type="reset" class="btn btn-danger">
								</div>
								<div class="form-group">
									<a href="#" class="black">Forget Password ?</a>
								</div>
							</form>
						</div>
					</div>
					<div class="tab-pane fade" id="register-block">
						<div class="register-block-form">
							<!-- Heading -->
							<h4>Create the New Account</h4>
							<!-- Border -->
							<div class="bor bg-lblue"></div>
							<!-- Form -->
							<form class="form" role="form">
								<!-- Form Group -->
								<div class="form-group">
									<!-- Label -->
									<label class="control-label">Name</label>
									<!-- Input -->
									<input type="text" class="form-control"  placeholder="Enter Name">
								</div>
								<div class="form-group">
									<label class="control-label">Email</label>
									<input type="text" class="form-control" placeholder="Enter Email">
								</div>
								<div class="form-group">
									<label class="control-label">Password</label>
									<input type="password" class="form-control" placeholder="Enter Password">
								</div>
								<div class="form-group">
									<label class="control-label">Your Country</label>
									<select class="form-control" id="country">
										<option>Select Your Country</option>
										<option>India</option>
										<option>USA</option>
										<option>London</option>
										<option>Canada</option>
									</select>
								</div>
								<div class="form-group">
									<!-- Checkbox -->
									<div class="checkbox">
										<label>
											<input type="checkbox"> Agree with terms and conditions
										</label>
									</div>
								</div>
								<div class="form-group">
									<!-- Buton -->
									<button type="submit" class="btn btn-red">Submit</button>&nbsp;
									<input  id="reset" type="reset" class="btn btn-danger">
								</div>
							</form>
						</div>
					</div>
					<div class="tab-pane fade" id="contact-block">
						<!-- Contact Block Form -->
						<div class="contact-block-form">
							<h4>Contact Form</h4>
							<!-- Border -->
							<div class="bor bg-purple"></div>
							<!-- Form -->
							<form class="form" role="form">
								<!-- Form Group -->
								<div class="form-group">
									<label class="control-label">Name</label>
									<input type="text" class="form-control" placeholder="Enter Name">
								</div>
								<div class="form-group">
									<label class="control-label">Email</label>
									<input type="text" class="form-control" placeholder="Enter Email">
								</div>
								<div class="form-group">
									<label for="comments" class="control-label">Comments</label>
									<textarea class="form-control" id="comments" rows="5" placeholder="Enter Comments"></textarea>
								</div>
								<div class="form-group">
									<!-- Buton -->
									<button type="submit" class="btn btn-red">Submit</button>&nbsp;
									<input  id="reset" type="reset" class="btn btn-danger">
								</div>
							</form>
						</div>
					</div>
				</div>
			</div>
		</div>
		
		<!-- Javascript files -->
		<!-- jQuery -->
		<script src="<c:url value="/resources/js/vendor/jquery.min.js" />"></script>
		<!-- Bootstrap JS -->
		<script src="<c:url value="/resources/js/vendor/bootstrap.min.js" />"></script>
	</body>	
</html>