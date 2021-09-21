<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html lang="en">

<head>

<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="author" content="">

<title>Scrolling Nav - Start Bootstrap Template</title>

<!-- Bootstrap Core CSS -->
<link href="<c:url value="/resources/css/bootstrap.min.css" />"
	rel="stylesheet">

<!-- Font awesome CSS -->
<link href="<c:url value="/resources/css/font-awesome.min.css" />" rel="stylesheet">	

<!-- Custom CSS -->
<link href="<c:url value="/resources/css/scrolling-nav.css" />"
	rel="stylesheet">

<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
<style type="text/css">
.main-footer {
  height: 50px;
  border-top: 2px solid;
  font-family: "Droid Sans", sans-serif;
  color: #b5b5b5;
  line-height: 48px;
  background-color: #f8f8f8;
  border-color: #e7e7e7;
}
.archive {
  float: right;
  margin: 0;
}

.archive > li {
  margin-left: 30px;
}

.archive .fa-li {
  position: static;
}

.archive a:hover {
  text-decoration: none;
}

.copyright {
  float: left;
}

@media screen and (max-width: 767px) {
	.main-footer {
    position: static;
	height: auto;
    text-align: center;
  }
	
  .archive {
    float: none;
    display: inline-block;	
  }
	
  .archive.fa-ul {
    margin: 0;
    padding: 0;
    text-align: center;
  }
	
  .archive > li {
    margin: 0;
  }
	
  .copyright {
    float: none;	
    display: block;
  }
}
</style>
</head>

<!-- The #page-top ID is part of the scrolling feature - the data-spy and data-target are part of the built-in Bootstrap scrollspy function -->

<body id="page-top" data-spy="scroll" data-target=".navbar-fixed-top">

	<!-- Navigation -->
	<nav class="navbar navbar-default navbar-fixed-top" role="navigation">
		<div class="container">
			<div class="navbar-header page-scroll">
				<button type="button" class="navbar-toggle" data-toggle="collapse"
					data-target=".navbar-ex1-collapse">
					<span class="sr-only">Toggle navigation</span> <span
						class="icon-bar"></span> <span class="icon-bar"></span> <span
						class="icon-bar"></span>
				</button>
				<a class="navbar-brand page-scroll" href="#page-top">Start
					Bootstrap</a>
			</div>

			<!-- Collect the nav links, forms, and other content for toggling -->
			<div class="collapse navbar-collapse navbar-ex1-collapse">
				<ul class="nav navbar-nav">
					<!-- Hidden li included to remove active class from about link when scrolled up past about section -->
					<li class="hidden"><a class="page-scroll" href="#page-top"></a>
					</li>
					<li><a class="page-scroll" href="#about">About</a></li>
					<li><a class="page-scroll" href="#services">Services</a></li>
					<li><a class="page-scroll" href="#contact">Contact</a></li>
				</ul>
			</div>
			<!-- /.navbar-collapse -->
		</div>
		<!-- /.container -->
	</nav>

	<!-- Intro Section -->
	<section id="intro" class="intro-section">
		<div class="container">
			<div class="row">
				<div class="col-lg-12">
					<h1>Scrolling Nav</h1>
					<p>
						<strong>Usage Instructions:</strong> Make sure to include the
						<code>scrolling-nav.js</code>
						,
						<code>jquery.easing.min.js</code>
						, and
						<code>scrolling-nav.css</code>
						files. To make a link smooth scroll to another section on the
						page, give the link the
						<code>.page-scroll</code>
						class and set the link target to a corresponding ID on the page.
					</p>
					<a class="btn btn-default page-scroll" href="#about">Click Me
						to Scroll Down!</a>
				</div>
			</div>
		</div>
	</section>

	<!-- About Section -->
	<section id="about" class="about-section">
		<div class="container">
			<div class="row">
				<div class="col-lg-12">
					<h1>About Section</h1>
				</div>
			</div>
		</div>
	</section>

	<!-- Services Section -->
	<section id="services" class="services-section">
		<div class="container">
			<div class="row">
				<div class="col-lg-12">
					<h1>Services Section</h1>
				</div>
			</div>
		</div>
	</section>

	<!-- Contact Section -->
	<section id="contact" class="contact-section">
		<div class="container">
			<div class="row">
				<div class="col-lg-12">
					<h1>Contact Section</h1>
				</div>
			</div>
		</div>
	</section>
	
	<!-- Footer -->
	<footer class="main-footer navbar-fixed-bottom">
    <div class="container">			
      <ul class="list-inline fa-ul archive">
        <li><i class="fa-li fa fa-save"></i><a href="#">Download CV as PDF</a></li>
        <li><i class="fa-li fa fa-print"></i><a href="#">Print CV</a></li>
      </ul>
			
      <span class="copyright">All Rights Reserved © 2013-2015.</span>
    </div>
  </footer>

	<!-- jQuery -->
	<script src="<c:url value="/resources/js/vendor/jquery.min.js" />"></script>

	<!-- Bootstrap Core JavaScript -->
	<script src="<c:url value="/resources/js/vendor/bootstrap.min.js" />"></script>

	<!-- Scrolling Nav JavaScript -->
	<script
		src="<c:url value="/resources/js/vendor/jquery.easing.min.js" />"></script>
	<script src="<c:url value="/resources/js/vendor/scrolling-nav.js" />"></script>

</body>

</html>