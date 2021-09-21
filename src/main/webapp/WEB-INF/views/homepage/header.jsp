<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html lang="en">

<head>

<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="author" content="">

<title>${webPage.title}</title>

<!-- Bootstrap Core CSS -->
<link href="<c:url value="/resources/css/bootstrap.min.css" />"
	rel="stylesheet">

<!-- Font awesome CSS -->
<link href="<c:url value="/resources/css/font-awesome.min.css" />" rel="stylesheet">

<!-- UI CSS For components -->
<link href="<c:url value="/resources/css/ui.css" />" rel="stylesheet">

<!-- Tiles CSS -->
<link href="<c:url value="/resources/css/tiles.css" />" rel="stylesheet">

<!-- Multi Column Select CSS -->
<link href="<c:url value="/resources/css/Multi-Column-Select.css" />" rel="stylesheet">	

<!-- Custom CSS -->
<c:if test="${webPage.header}"><link href="<c:url value="/resources/css/scrolling-nav.css" />"
	rel="stylesheet"></c:if>

<!-- Editable element CSS -->
<link href="<c:url value="/resources/css/bootstrap-editable.css" />" rel="stylesheet">	

<!-- Table Tree CSS -->
<link href="<c:url value="/resources/css/jquery.treetable.css" />" rel="stylesheet">
<link href="<c:url value="/resources/css/jquery.treetable.theme.default.css" />" rel="stylesheet">

<!-- Countdown plugin CSS -->
<link href="<c:url value="/resources/css/TimeCircles.css" />" rel="stylesheet">	

<!-- DataTables plugin CSS -->
<link href="<c:url value="/resources/css/dataTables.bootstrap.css" />" rel="stylesheet">	

<!-- jBox Modal and Tooltip plugin -->
<link href="<c:url value="/resources/css/jBox.css" />" rel="stylesheet">	

<!-- Bootstrap based Checkbox to Toggle plugin -->
<link href="<c:url value="/resources/css/bootstrap-toggle.min.css" />" rel="stylesheet">	

<!-- Footer CSS -->
<c:if test="${webPage.footer}"><link href="<c:url value="/resources/css/custom/footer.css" />"
	rel="stylesheet"></c:if>

<c:if test="${webPage.css}"><link href="<c:url value="/resources/css/custom/${webPage.name}.css" />" 
	rel="stylesheet"></link></c:if>
<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>

<!-- The #page-top ID is part of the scrolling feature - the data-spy and data-target are part of the built-in Bootstrap scrollspy function -->

<body id="page-top" data-spy="scroll" data-target=".navbar-fixed-top">
	<c:if test="${webPage.header}">
	<!-- Navigation -->
	<nav class="navbar navbar-inverse navbar-fixed-top top-nav-collapse" role="navigation">
		<div class="container-fluid">
			<div class="navbar-header page-scroll">
				<button type="button" class="navbar-toggle" data-toggle="collapse"
					data-target=".navbar-ex1-collapse">
					<span class="sr-only">Toggle navigation</span> <span
						class="icon-bar"></span> <span class="icon-bar"></span> <span
						class="icon-bar"></span>
				</button>
				<!-- <a class="navbar-brand page-scroll" href="#page-top">Start
					Bootstrap</a> -->
			</div>

			<!-- Collect the nav links, forms, and other content for toggling -->
			<div class="collapse navbar-collapse navbar-ex1-collapse">
				<ul class="nav navbar-nav">
					<!-- Hidden li included to remove active class from about link when scrolled up past about section -->
					<li class="hidden"><a class="page-scroll" href="#page-top"></a>
					</li>
					<sec:authorize access="hasRole('ROLE_USER')">
					<li><a class="page-scroll" href="homepage">Home</a></li>
					<sec:authorize access="hasRole('ROLE_FACULTY')">
					<li class="dropdown"><a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false">Test <span class="caret"></span></a>
					<ul class="dropdown-menu" role="menu">
					 <li><a href="/addTestFromMenu" >Create Test</a></li>
			            <li><a href="<c:url value="/testList" />">View Test</a></li>
		          	</ul>
					</li>
					</sec:authorize>
					
					<sec:authorize access="hasRole('ROLE_ADMIN')">
					<li class="dropdown"><a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false">Feedback <span class="caret"></span></a>
					<ul class="dropdown-menu" role="menu">
			            <li><a href="<c:url value="/addFeedbackForm" />">Add Feedback</a></li>
			            <li><a href="<c:url value="/searchFeedback" />">View Feedback</a></li>
			            <li><a href="<c:url value="/addStudentFeedbackForm" />">Allocate Feedback</a></li>
		          	</ul>
					</li>
					</sec:authorize>
					
					<sec:authorize access="hasRole('ROLE_FACULTY')">
					<li class="dropdown"><a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false">Assignment <span class="caret"></span></a>
					<ul class="dropdown-menu" role="menu">
					 <li><a href="<c:url value="/createAssignmentFromMenu" />">Create Assignments</a></li>
			            <li><a href="<c:url value="/searchFacultyAssignment" />">View Assignments</a></li>
			            <li><a href="<c:url value="/searchAssignmentToEvaluate" />">Evaluate Assignments</a></li>
		          	</ul>
					</li>
					</sec:authorize>
					
					<%-- <sec:authorize access="hasRole('ROLE_ADMIN')">
					<li class="dropdown"><a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false">Program <span class="caret"></span></a>
					<ul class="dropdown-menu" role="menu">
			            <li><a href="<c:url value="/addProgramForm" />">Add Single Program</a></li>
			            <li><a href="<c:url value="/uploadProgramForm" />">Upload Program Excel</a></li>
			            <li><a href="<c:url value="/searchProgram" />">Search Programs</a></li>
			            <li><a href="<c:url value="/searchProgramSession" />">Map Program Sessions</a></li>
			            <li><a href="<c:url value="/addProgramSessionCourseForm" />">Map Program Courses</a></li>
		          	</ul>
					</li>
					</sec:authorize> --%>
					
					<sec:authorize access="hasRole('ROLE_ADMIN')">
					<li class="dropdown"><a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false">Course <span class="caret"></span></a>
					<ul class="dropdown-menu" role="menu">
			            <li><a href="<c:url value="/addCourseForm" />">Add Single Course</a></li>
			            <li><a href="<c:url value="/uploadCourseForm" />">Upload Course Excel</a></li>
			            <li><a href="<c:url value="/searchCourse" />">Search Courses</a></li>
			            <li><a href="<c:url value="/uploadUserCourseForm" />">Course User Enrollment</a></li>
			            <li><a href="<c:url value="/searchUserCourse" />">Search Course User Enrollment</a></li>
			            <%-- <li><a href="<c:url value="/addFacultyCourseForm" />">Course Faculty Enrollment-Not needed</a></li> --%>
		          	</ul>
					</li>
					</sec:authorize>
					
					<sec:authorize access="hasRole('ROLE_ADMIN')">
					<li class="dropdown"><a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false">Manage Users <span class="caret"></span></a>
					<ul class="dropdown-menu" role="menu">
			            <li><a href="<c:url value="/addUserForm" />">Create Single User</a></li>
			            <li><a href="<c:url value="/uploadStudentForm" />">Upload Students Excel</a></li>
			            <li><a href="<c:url value="/uploadFacultyForm" />">Upload Faculty Excel</a></li>
		          	</ul>
					</li>
					</sec:authorize>
					
					<sec:authorize access="hasRole('ROLE_FACULTY')">
					<li class="dropdown"><a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false">Announcements <span class="caret"></span></a>
					<ul class="dropdown-menu" role="menu">
			            <li><a href="<c:url value="/addAnnouncementForm" />">Create Announcement</a></li>
			            <li><a href="<c:url value="/searchAnnouncement" />">Search Announcements</a></li>
		          	</ul>
					</li>
					</sec:authorize>
					
					<sec:authorize access="hasRole('ROLE_FACULTY')">
					<li class="dropdown"><a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false">Content <span class="caret"></span></a>
					<ul class="dropdown-menu" role="menu">
			            <li><a href="<c:url value="/addContentForm?contentType=Folder&courseId=1" />">Create Folder</a></li>
			            <li><a href="<c:url value="/addContentForm?contentType=File&courseId=1" />">Create File</a></li>
			            <li class="divider"></li>
			            <li><a href="<c:url value="/getContentUnderAPath?courseId=1" />">View Content</a></li>
			            <li><a href="<c:url value="/searchTest" />">View Test</a></li>
		          	</ul>
					</li>
					</sec:authorize>
					
					<sec:authorize access="hasAnyRole('ROLE_ADMIN','ROLE_FACULTY', 'ROLE_DEAN')">
					<li class="dropdown"><a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false">Search <span class="caret"></span></a>
					<ul class="dropdown-menu" role="menu">
			            <li><a href="<c:url value="/searchAssignmentForm" />">Search Assignments</a></li>
			            <li><a href="<c:url value="/searchTest" />">Search Tests</a></li>
		          	</ul>
					</li>
					</sec:authorize>
					
					<sec:authorize access="hasAnyRole('ROLE_STUDENT','ROLE_FACULTY')">
					<li class="dropdown"><a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false">Task <span class="caret"></span></a>
					<ul class="dropdown-menu" role="menu">
			            <li><a href="<c:url value="/pendingTask" />">Pending Task</a></li>
			            
		          	</ul>
					</li>
					</sec:authorize>
						
						<li class="dropdown"><a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false">Test <span class="caret"></span></a>
					<ul class="dropdown-menu" role="menu">
			            <li><a href="<c:url value="/testList" />">View Tests</a></li>
		          	</ul>
					</li>
					
					
						<li class="dropdown"><a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false">Assignment <span class="caret"></span></a>
					<ul class="dropdown-menu" role="menu">
			            <li><a href="<c:url value="/assignmentList" />">View assignments</a></li>
		          	</ul>
					</li>
					
						<li class="dropdown"><a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false">Announcement <span class="caret"></span></a>
					<ul class="dropdown-menu" role="menu">
			            <li><a href="<c:url value="/viewUserAnnouncements" />">View Announcements</a></li>
		          	</ul>
					</li>
					
					<li class="dropdown"><a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false">Library <span class="caret"></span></a>
					<ul class="dropdown-menu" role="menu">
			            <li><a href="<c:url value="/viewLibrary" />">Digital Library</a></li>
		          	</ul>
					</li>
					
					<%-- <li class="dropdown"><a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false">Grades <span class="caret"></span></a>
					<ul class="dropdown-menu" role="menu">
			            <li><a href="<c:url value="/gradeCenter" />">Grade Center</a></li>
		          	</ul> --%>
					</li>
					<sec:authorize access="hasRole('ROLE_ADMIN')">
					<li class="dropdown"><a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false">SetUp <span class="caret"></span></a>
					<ul class="dropdown-menu" role="menu">
			            <li><a href="<c:url value="/addInstituteCycleForm" />">Make Academic Cycle LIVE!</a></li>
		          	</ul>
					</li>
					</sec:authorize>
					
					
					<li class="dropdown"><a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false">My Profile <span class="caret"></span></a>
					<ul class="dropdown-menu" role="menu">
			            <li><a href="<c:url value="/changePasswordForm" />">Change Password</a></li>
			            <li><a href="<c:url value="/updateProfileForm" />">Update Profile</a></li>
		          	</ul>
					</li>
					
					<li><a class="page-scroll" href="${pageContext.request.contextPath}/loggedout">Logout</a></li>
					 </sec:authorize>
					
				</ul>
			</div>
			<!-- /.navbar-collapse -->
		</div>
		<!-- /.container -->
	</nav>
	<!-- Header Strip for logo and user details -->
	<header class="customHeader row">
    	<div class="logoWrapper col-xs-12 col-md-4" style="height: 100%">
        	<img src="resources/images/logo.jpg" height="100%" alt="Logo" style="border-radius: 10px;padding: 1px;">
   	    </div>
   	    <c:if test="${not empty instituteName }">
   	    <div class="col-xs-12 col-md-4 flex-vcenter" style="height: 100%">
   	    	<h4 class="white"></h4>
   	    </div>
   	    </c:if>
        <c:if test="${not empty currentUser }">
        <div class="col-xs-12 col-md-4" style="height: 100%">
        	<h1>Welcome to White Board</h1>
            <div class="userContainer">
                <div class="userImg">
                    	<img src="resources/images/userImg.jpg" alt="Student Photo" class="img-responsive">
                </div>
                <div class="detailWrapper">
                    <h2>${currentUser.firstname} ${currentUser.lastname}</h2>
                    <p>User ID: ${currentUser.username}, Program: ${currentUser.program.programName}</p>
                    <p><a href="updateProfileForm" style="color: white">${currentUser.email}/${currentUser.mobile}</a></p>
                </div>        
            </div>
        </div>
        </c:if>
    </header>
    
    <div class="clearfix"></div>
	</c:if>
