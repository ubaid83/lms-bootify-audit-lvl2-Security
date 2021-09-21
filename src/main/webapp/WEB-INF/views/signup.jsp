<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8">
		<title>Register</title>
		
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
		<div class="ui-15 ui-243">
				
				   <div class="container-fluid">
						<div class="row">
							<div class="col-md-3 col-sm-3">
								<!--  Hover menu drop down -->
								<div class="hover-menu">
									<a class="menu-btn" href="#"><i class="fa fa-bars"></i></a>
									<ul class="dd-menu list-unstyled menu-two">
										<li>
											<a href="#" class="color-one">
												<i class="fa fa-dashboard"></i>
												<!-- Menu title -->
												<span class="menu-title">Dashboard</span>
											</a>
										</li>	
										<li>
											<a href="#" class="color-two">
												<i class="fa fa-cube"></i>
												<!-- Menu title -->
												<span class="menu-title">Widgets</span>
											</a>
										</li>
										<li>
											<a href="#" class="color-three">
												<i class="fa fa-bar-chart-o"></i>
												<!-- Menu title -->
												<span class="menu-title">Charts</span>
											</a>
										</li>
										<li>
											<a href="#" class="color-four">
												<i class="fa fa-file"></i>
												<!-- Menu title -->
												<span class="menu-title">Files</span>
											</a>
										</li>
										<li>
											<a href="#" class="color-five">
												<i class="fa fa-users"></i>
												<!-- Menu title -->
												<span class="menu-title">Users</span>
											</a>
										</li>
										<li>
											<a href="#" class="color-six">
												<i class="fa fa-picture-o"></i>
												<!-- Menu title -->
												<span class="menu-title">Galery</span>
											</a>
										</li>
										<li>
											<a href="#" class="color-seven">
												<i class="fa fa-phone-square"></i>
												<!-- Menu title -->
												<span class="menu-title">Contact</span>
											</a>
										</li>
									</ul>
								</div>
							</div>
							<div class="col-md-6 col-sm-6 ui-padd">
								<!-- UI Content -->
								<div class="ui-content">
								<!-- Ui Form -->
								<div class="ui-form">
									<!-- Heading -->
									<h2>Register form</h2>
									<!-- Form -->
									<form>
										<!-- UI Input -->
										<div class="ui-input">
											<!-- Input Box -->
											<input type="text" name="uname" placeholder="Enter Your Name" class="form-control" />
											<label class="ui-icon"><i class="fa fa-user"></i></label>
										</div>
										<div class="ui-input">
											<input type="email" name="email" placeholder="Enter Your Email" class="form-control" />
											<label class="ui-icon"><i class="fa fa-envelope-o"></i></label>
										</div>
										<div class="ui-input">
											<input type="text" name="uname" placeholder="Enter Phone Number" class="form-control" />
											<label class="ui-icon"><i class="fa fa-phone"></i></label>
										</div>
										<div class="ui-input">
											<input type="Password" name="uname" placeholder="Enter your Password" class="form-control" />
											<label class="ui-icon"><i class="fa fa-lock"></i></label>
										</div>
										<input type="submit" name="submit" value="Register" class="btn btn-red btn-lg btn-block">
									</form>
								</div>
							</div>
							<div class="clearfix"></div>
						</div>
						</div>
					
				</div>
		</div>
		
		<!-- Javascript files -->
		<!-- jQuery -->
		<script src="<c:url value="/resources/js/vendor/jquery.min.js" />"></script>
		<!-- Bootstrap JS -->
		<script src="<c:url value="/resources/js/vendor/bootstrap.min.js" />"></script>
		<script>
			
			/* Drop Down */
			$(document).ready(function() {
				var hidden = true;
				$('.hover-menu .menu-btn').click(function(e) {
					e.preventDefault();
					if (hidden){
					   $(this).parent().children('.dd-menu').slideToggle(500, function(){hidden = false;});
					}
				});

				$('html').click(function() {
					if (!hidden) {
						$('.dd-menu').slideUp(500);
						hidden=true;
					}
				});

				$('.dd-menu').click(function(event) {
					event.stopPropagation();
				});
			});
			
		</script>
	</body>	
</html>