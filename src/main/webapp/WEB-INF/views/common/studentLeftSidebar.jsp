<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%-- <div class="col-md-3 left_col">
				<div class="left_col scroll-view">
					<div class="navbar nav_title" style="border: 0;">
						<a href="<c:url value="/homepage" />"><img
							src="<c:url value="/resources/images/logo.gif" />" alt=""></a>
					</div>

					<div class="clearfix"></div> --%>

<!-- menu profile quick info -->
<div class="profile clearfix">
	<div class="profile_pic">
		<img src="<c:url value="/resources/images/law.png" />" alt="">
	</div>
	<div class="profile_info">
		<h2>
			<c:out value="${courseRecord.courseName}" />
		</h2>
	</div>
</div>
<!-- /menu profile quick info -->

<!-- sidebar menu -->
<div id="sidebar-menu" class="main_menu_side hidden-print main_menu">
	<div class="menu_section">
		<ul class="nav side-menu">
			<li><a
				href="${pageContext.request.contextPath}/overview?courseId=${courseId}"><i
					class="fa fa-comments"></i> Overview </a>
				<ul class="nav child_menu">
					<!--   <li><a href="#">Dashboard</a></li>
				                      <li><a href="#">Dashboard2</a></li>
				                      <li><a href="#">Dashboard3</a></li> -->
				</ul></li>




			<li><a
				href="${pageContext.request.contextPath}/assignmentList?courseId=${courseId}"
				class=""><i class="fa fa-newspaper-o"></i> Assignments </a></li>

			<li><a
				href="${pageContext.request.contextPath}/testList?courseId=${courseRecord.id}"><i
					class="fa fa-file-text"></i> Test/Quiz</a></li>
			<li><a
				href="${pageContext.request.contextPath}/studentContentList?courseId=${courseId}"><i
					class="fa fa-folder-open"></i> Learning Resources</a></li>
			<li><a
				href="${pageContext.request.contextPath}/viewForum?courseId=${courseId}"><i
					class="fa fa-twitch "></i> Discussion Forums</a></li>
			<li><a
				href="${pageContext.request.contextPath}/groupList?courseId=${courseId}"><i
					class="fa fa-users"></i> My Groups</a></li>
			<li><a
				href="${pageContext.request.contextPath}/gradeCenterForStudent?courseId=${courseId}"><i
					class="fa fa-pie-chart"></i> Grade Dashboard</a></li>
			<li><a
				href="${pageContext.request.contextPath}/viewFeedbackDetails?courseId=${courseId}"><i
					class="fa fa-reply-all"></i> Feedback</a></li>
			<li><a
				href="${pageContext.request.contextPath}/createReportsForm?courseId=${courseId}"
				class=""><i class="fa fa-bar-chart" aria-hidden="true"></i>Report</a></li>
			<li><a
				href="${pageContext.request.contextPath}/knowMyFaculty?courseId=${courseId}"><i
					class="fa fa-user"></i> Teacher Profile</a></li>

			<%-- <li><a
				href="${pageContext.request.contextPath}/viewGradeWeightForStudent?courseId=${courseId}"><i
					class="fas fa-graduation-cap"></i> View Graded Weight</a></li> --%>

		</ul>
	</div>

</div>
<!-- /sidebar menu -->
<!-- 	</div>
			</div> -->