<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>


<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport"
	content="width=device-width, initial-scale=.5, maximum-scale=12.0, minimum-scale=.25, user-scalable=yes" />

<!-- <title>Svkm's NMIMS dcemed to be University</title> -->
<title>${webPage.title}</title>

<!-- <link rel="shortcut icon" href="favicon.ico">
<link rel="icon" type="image/png" href="favicon.png"> -->

<link rel="shortcut icon"
	href="${pageContext.request.contextPath}/resources/favicon.ico">
<link rel="icon" type="image/png"
	href="${pageContext.request.contextPath}/resources/favicon.png">
	
	
<!-- date timepicker -->
<link rel='stylesheet prefetch'
	href='https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datetimepicker/3.1.3/css/bootstrap-datetimepicker.min.css'>

<!-- date timepicker-->

<!-- Bootstrap -->
<link href="<c:url value="/resources/librarianCss/bootstrap.min.css" />"
	rel="stylesheet">
<!-- Font Awesome -->
<link href="<c:url value="/resources/librarianCss/font-awesome.min.css" />"
	rel="stylesheet">
<!-- bootstrap-progressbar -->
<link
	href="<c:url value="/resources/librarianCss/bootstrap-progressbar-3.3.4.min.css" /> "
	rel="stylesheet">


<!-- Custom Toggle Style -->
<link href=" <c:url value="/resources/librarianCss/toggle.css" />"
	rel="stylesheet">


<link href=" <c:url value="/resources/librarianCss/jquery.fancybox.min.css" />"
	rel="stylesheet">

<link href=" <c:url value="/resources/librarianCss/TimeCircles.css" />"
	rel="stylesheet">

<link href=" <c:url value="/resources/librarianCss/wizard.css" />"
	rel="stylesheet">

<!-- Custom menu Style -->
<link href=" <c:url value="/resources/librarianCss/menu.css" />" rel="stylesheet">


<!-- Custom Theme Style -->
<link href="<c:url value="/resources/librarianCss/custom.css" /> "
	rel="stylesheet">
<link href="<c:url value="/resources/librarianCss/responsive.css" />"
	rel="stylesheet">


<!-- FullCalendar -->
<link
	href="<c:url value="/resources/vendors/nprogress/nprogress.css" />"
	rel="stylesheet">
<link
	href=" <c:url value="/resources/vendors/fullcalendar/dist/fullcalendar.min.css" />"
	rel="stylesheet">
<link
	href="<c:url value="/resources/vendors/fullcalendar/dist/fullcalendar.print.css" />"
	rel="stylesheet" media="print">



<!-- Editable element CSS -->
<link href="<c:url value="resources/librarianCss/bootstrap-editable.css" />"
	rel="stylesheet">

<!-- Table Tree CSS -->
<link href="<c:url value="resources/librarianCss/jquery.treetable.css" />"
	rel="stylesheet">
<link
	href="<c:url value="resources/librarianCss/jquery.treetable.theme.default.css" />"
	rel="stylesheet">

<style>
.loader {display:none!important;}

</style>

</head>