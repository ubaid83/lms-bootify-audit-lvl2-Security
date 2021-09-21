<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>


<!doctype html>
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no" />

<!-- <title>Svkm's NMIMS dcemed to be University</title> -->
<title>${webPage.title}</title>

<!-- <link rel="shortcut icon" href="favicon.ico">
<link rel="icon" type="image/png" href="favicon.png"> -->

<link rel="shortcut icon"
	href="${pageContext.request.contextPath}/resources/favicon.ico">
<link rel="icon" type="image/png"
	href="${pageContext.request.contextPath}/resources/favicon.png">
	
	
<!-- date timepicker -->
<%-- <link rel='stylesheet prefetch'
	href="${pageContext.request.contextPath}/resources/css/bootstrap_datetimepicker.min.css"> --%>

<!-- date timepicker-->
<%-- 
<!-- Bootstrap -->
<link href="<c:url value="/resources/css/bootstrap.min.css" />"
	rel="stylesheet">
<!-- Font Awesome -->
<link href="<c:url value="/resources/css/font-awesome.min.css" />"
	rel="stylesheet">
	 --%>
<!-- bootstrap-progressbar -->
    <!-- Bootstrap CSS -->
    <link href="<c:url value="/resources/css/bootstrap.min.css" />" rel="stylesheet">
     
    <link href="<c:url value="/resources/css/dataTables.bootstrap4.min.css" />" rel="stylesheet">
    
<!--     <link rel="stylesheet" href="resources/css/bootstrap.min.css" crossorigin="anonymous">
    <link rel="stylesheet" href="resources/css/style.css" crossorigin="anonymous"> -->
        
        
<%-- <link
	href="<c:url value="resources/css/style.css" /> "
	rel="stylesheet"> --%>
	
	    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/font-awesome.min.css">
<!-- <link rel="stylesheet" type="text/css" href="https://cdn.jsdelivr.net/npm/daterangepicker/daterangepicker.css" /> -->
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/daterangepicker.css"/>

<%-- <link
	href="<c:url value="/resources/css/bootstrap-progressbar-3.3.4.min.css" /> "
	rel="stylesheet"> --%>


<!-- Custom Toggle Style -->
<%-- <link href=" <c:url value="/resources/css/toggle.css" />"
	rel="stylesheet"> --%>


<%-- <link href=" <c:url value="/resources/css/jquery.fancybox.min.css" />"
	rel="stylesheet"> --%>

<link href=" <c:url value="/resources/css/TimeCircles.css" />"
	rel="stylesheet">

<%-- <link href=" <c:url value="/resources/css/wizard.css" />"
	rel="stylesheet"> --%>

<!-- Custom menu Style -->
<%-- <link href=" <c:url value="/resources/css/menu.css" />" rel="stylesheet">
 --%>

<!-- Custom Theme Style -->
<%-- <link href="<c:url value="/resources/css/custom.css" /> "
	rel="stylesheet"> --%>
<%-- <link href="<c:url value="/resources/css/responsive.css" />"
	rel="stylesheet"> --%>


<!-- FullCalendar -->
<%-- <link
	href="<c:url value="/resources/vendors/nprogress/nprogress.css" />"
	rel="stylesheet"> --%>
<link
	href=" <c:url value="/resources/vendors/fullcalendar/dist/fullcalendar.min.css" />"
	rel="stylesheet">
<link
	href="<c:url value="/resources/vendors/fullcalendar/dist/fullcalendar.print.css" />"
	rel="stylesheet" media="print">



<!-- Editable element CSS -->
<link href="<c:url value="resources/css/bootstrap-editable.css" />"
	rel="stylesheet">

<!-- Table Tree CSS -->
<link href="<c:url value="resources/css/jquery.treetable.css" />"
	rel="stylesheet">
<link
	href="<c:url value="resources/css/jquery.treetable.theme.default.css" />"
	rel="stylesheet">
	<link href="<c:url value="/resources/css/preety-checkbox.css" />" rel="stylesheet">
	<link href="<c:url value="/resources/css/richtext.min.css" />" rel="stylesheet">

	
	   <link href="<c:url value="/resources/css/style.css" />" rel="stylesheet">
	   <!-- <link href="https://cdn.datatables.net/1.10.19/css/dataTables.bootstrap4.min.css" rel="stylesheet"> -->
	   
	   <link href="${pageContext.request.contextPath}/resources/css/select2.min.css" rel="stylesheet">
</head>
<body>