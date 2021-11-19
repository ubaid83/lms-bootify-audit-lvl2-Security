<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link
	href="${pageContext.request.contextPath}/resources/css/bootstrap.min.css"
	rel="stylesheet" />
<!-- <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"> -->
<link
	href="${pageContext.request.contextPath}/resources/css/font-awesome.min.css"
	rel="stylesheet" />
<link
	href="${pageContext.request.contextPath}/resources/css/style-2019.css"
	rel="stylesheet" />
<!--  <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.7.2/css/all.css" > -->
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/all.css">
<title>${status}</title>

<style>
body {
	/*  background-image:url("${pageContext.request.contextPath}/images/Galaxy.jpg"); */
	background-repeat: no-repeat;
	background-size: cover;
	/*background: #222D32;*/
	font-family: 'Roboto', sans-serif;
}

.login-box {
	margin-top: 75px;
	height: auto;
	background: rgba(8, 36, 49, 0.51);
	text-align: center;
	box-shadow: 0 3px 6px rgba(0, 0, 0, 0.16), 0 3px 6px rgba(0, 0, 0, 0.23);
	font-family: 'Reem Kufi', sans-serif;
}

.login-key {
	height: 150px;
	font-size: 80px;
	line-height: 100px;
	background: -webkit-linear-gradient(#27EF9F, #0DB8DE);
	-webkit-background-clip: text;
	-webkit-text-fill-color: transparent;
}

.login-title h1 {
	margin-top: 20px;
	text-align: center;
	font-size: 10em;
	letter-spacing: 3px;
	margin-top: 15px;
	font-weight: bold;
	color: #000;
	font-family: 'Reem Kufi', sans-serif;
	/* Fallback: assume this color ON TOP of image */
	/*   background: url(${pageContext.request.contextPath}/images/Galaxy.jpg) no-repeat;
   -webkit-background-clip: text;
   -webkit-text-fill-color: transparent;
   width: fit-content; */
}

.login-form {
	margin-top: 25px;
	text-align: left;
}

input[type=text] {
	background-color: #1a22266b;
	border: none;
	border-bottom: 2px solid #0DB8DE;
	border-top: 0px;
	border-radius: 0px;
	font-weight: bold;
	outline: 0;
	margin-bottom: 20px;
	padding-left: 0px;
	color: #ECF0F5;
	font-family: 'Reem Kufi', sans-serif;
}

input[type=password] {
	background-color: #1a22266b;
	border: none;
	border-bottom: 2px solid #0DB8DE;
	border-top: 0px;
	border-radius: 0px;
	font-weight: bold;
	outline: 0;
	padding-left: 0px;
	margin-bottom: 20px;
	color: #ECF0F5;
	font-family: 'Reem Kufi', sans-serif;
}

.form-group {
	margin-bottom: 40px;
	outline: 0px;
}

.form-control:focus {
	border-color: inherit;
	-webkit-box-shadow: none;
	box-shadow: none;
	border-bottom: 2px solid #0DB8DE;
	outline: 0;
	background-color: #1A2226;
	color: #ECF0F5;
	transition: 3s;
}

input:focus {
	outline: none;
	box-shadow: 0 0 0;
}

label {
	margin-bottom: 0px;
}

.form-control-label {
	font-size: 10px;
	color: #ffffff;
	font-weight: bold;
	letter-spacing: 1px;
}

.btn-outline-primary {
	border-color: #0DB8DE;
	color: #0DB8DE;
	border-radius: 0px;
	font-weight: bold;
	letter-spacing: 1px;
	box-shadow: 0 1px 3px rgba(0, 0, 0, 0.12), 0 1px 2px rgba(0, 0, 0, 0.24);
}

.btn-outline-primary:hover {
	background-color: #0DB8DE;
	right: 0px;
}

.login-button {
	padding-right: 0px;
	margin-bottom: 25px;
}

.login-text {
	text-align: left;
	padding-left: 0px;
	color: #A2A4A4;
}

.loginbttm {
	padding: 0px;
}

.Conceptualize-text {
	font-weight: 700;
	color: white;
	font-family: 'Reem Kufi', sans-serif;
}

.error-text h3 {
	font-family: 'Staatliches', cursive;
	text-align: center;
	margin-left: 25px;
}
</style>
</head>
<body>


	<div class="container">
		<div class="row">
			<div class="col-lg-3 col-md-2"></div>
			<div class="col-lg-5 col-md-4">
				<!-- <div class="col-lg-5 col-md-4 login-box"> -->
				<!-- <div class="col-lg-12 login-key">
					<i class="fa fa-key" aria-hidden="true"></i>
				</div> -->
				<div class="col-lg-12 login-title text-center">
					<h1>Oops!</h1>

				</div>

				<div class="col-lg-12 text-center error-text">
				<h3>Error-${status}</h3>
					<%-- <h3>Congrats You Have Found Error ${status}</h3> --%>
					<%-- <c:choose>


						<c:when test="${status==0}">
							<h3>Error!!!</h3>
						</c:when>
						<c:otherwise>
							
						</c:otherwise>
					</c:choose> --%>


				</div>
				<div class="row">
					<div class="col-lg-12 my-2 text-center">
						<!-- <form action="/iceList">
<button type="submit"  class="btn btn-primary btn-lg rounded-0">Back to home</button>
</form> -->
						<input type="button" class="btn btn-primary btn-lg rounded-0"
							onclick="javascript:history.go(-1)" value="Back to home" />
					</div>
				</div>

				<div class="col-lg-3 col-md-2"></div>
			</div>

		</div>
	</div>
	<!--  <table>
        <tr>
            <td>Date</td>
            <td>${timestamp}</td>
        </tr>
        <tr>
            <td>Error</td>
            <td>${error}</td>
        </tr>
        <tr>
            <td>Status</td>
            <td>${status}</td>
        </tr>
        <tr>
            <td>Message</td>
            <td>${message}</td>
        </tr>
        <tr>
            <td>Exception</td>
            <td>${exception}</td>
        </tr>
        <tr>
            <td>Trace</td>
            <td>
                <pre>${trace}</pre>
            </td>
        </tr>
    </table>-->
	<script
		src="${pageContext.request.contextPath}/resources/js/jquery-3.3.1.min.js"></script>
	<%-- <script src="${pageContext.request.contextPath}/resources/js/popper.min"></script> --%>
	<script
		src="${pageContext.request.contextPath}/resources/js/bootstrap.min.js"></script>
	<script
		src="${pageContext.request.contextPath}/resources/js/wow.min.js"></script>
	<script
		src="${pageContext.request.contextPath}/resources/js/style-2019.js"></script>
	<script
		src="${pageContext.request.contextPath}/resources/js/filevalidationupload.js"></script>
	<script
		src="${pageContext.request.contextPath}/resources/js/input-form-validation.js"></script>
</body>
</html>