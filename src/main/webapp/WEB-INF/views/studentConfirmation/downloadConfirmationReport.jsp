<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>


<jsp:include page="../common/newDashboardHeader.jsp" />

<div class="d-flex adminPage" id="facultyAssignmentPage">
	<jsp:include page="../common/newAdminLeftNavBar.jsp" />
	<jsp:include page="../common/rightSidebarAdmin.jsp" />


	<!-- DASHBOARD BODY STARTS HERE -->

	<div class="container-fluid m-0 p-0 dashboardWraper">

		<jsp:include page="../common/newAdminTopHeader.jsp" />

		<!-- SEMESTER CONTENT -->
		<div class="col-lg-12 col-md-12 col-sm-12 dashboardBody p-5">
			<div class="card rounded-0">
			<div class="card-body">
			<!-- <h2 class="text-center pb-2 border-bottom mb-5">WELCOME TO ADMIN DASHBOARD!</h2> -->
			<div class="row">
	
	
		<div class="col-md-3">
			<a class="btn btn-primary text-light" href="downloadMasterReport">Download Report</a>
		</div>
			</div>
			</div>
			</div>
		</div>

		<!-- SIDEBAR START -->
		<%-- 	         <jsp:include page="../common/newSidebar.jsp" /> --%>
		<!-- SIDEBAR END -->
		<jsp:include page="../common/newAdminFooter.jsp" />








		<%-- <body class="nav-md footer_fixed dashboard_left">
	<!-- Example row of columns -->
	<div class="loader"></div>
	<div class="container body">
		<div class="main_container">


			<!--  -->
			<div class="right_col" role="main">

				<div class="dashboard_container">

					<!-- <div class="dash-main"> -->
					<div class="dashboard_height" id="main">


						<div class="dashboard_contain_specing dash-main">

							<div class="alert alert-success alert-dismissible" role="alert">
								<button type="button" class="close" data-dismiss="alert"
									aria-label="Close">
									<span aria-hidden="true">&times;</span>
								</button>
								<h4>
									<strong>Welcome to Admin Login </strong>
								</h4>
							</div>
							
							<div class="alert alert-success alert-dismissible" role="alert">
								<button type="button" class="close" data-dismiss="alert"
									aria-label="Close">
									<span aria-hidden="true">&times;</span>
								</button>
								
								
								
								<h4>
								
								<c:out value="${subjectAdmin}"></c:out></b>
							<marquee behavior="scroll" direction="left" onmouseover="this.stop();" onmouseout="this.start();">${notificationAdmin}</marquee>
								
									<strong></strong>
									
								</h4>
							</div>

							<div class="container-fluid dashboardcon">

								<div class="col-md-4 course_widget_height">

									<div class="dashboardcon_title bgcolor1">
										<div>
											<c:url value="searchCourse" var="viewCourseUrl">
												<c:param name="" />
											</c:url>
										</div>
										<span><a href="${viewCourseUrl}" />Course</a></span>
									</div>
								</div>
								<div class="col-md-4 course_widget_height">
									<div class="dashboardcon_title bgcolor2">
										<div>
											<c:url value="searchAnnouncement" var="viewAnnouncementUrl">
												<c:param name="" value="" />
											</c:url>
										</div>
										<span><a href="${viewAnnouncementUrl}" />Announcement</a></span>
									</div>
								</div>
								<div class="col-md-4 course_widget_height">
									<div class="dashboardcon_title bgcolor3">
										<div>
											<c:url value="getContentUnderAPath" var="viewContentUrl">
												<c:param name="" />
											</c:url>
										</div>
										<span><a href="${viewContentUrl}" />Content</a></span>
									</div>
								</div>
								<div class="col-md-4 course_widget_height">
									<div class="dashboardcon_title bgcolor4">
										<div>
											<c:url value="viewLibrary" var="viewLibraryUrl">
												<c:param name="" value="" />
											</c:url>
										</div>
										<span><a href="${viewLibraryUrl}" />Library</a></span>
									</div>
								</div>
								<div class="col-md-4 course_widget_height">

									<div class="dashboardcon_title bgcolor1">

										<div>

											<c:url value="viewQuery" var="viewQueryUrl">

												<c:param name="" value="" />

											</c:url>

										</div>

										<span><a href="${viewQueryUrl}" />Preview Query List</a></span>

									</div>

								</div>
								<!--end dashboard-->




							</div>
						</div>
					</div>
				</div>


			</div>
		</div>
	</div>
</body>

<jsp:include page="../common/footer.jsp" />

<script>
	$(document).ready(
			function() {
				var cars = [ "bgcolor1", "bgcolor2", "bgcolor3", "bgcolor4",
						"bgcolor5", "bgcolor6", "bgcolor7", "bgcolor8",
						"bgcolor9" ];
				var count = 0;
				$('[id^=courseDetail]').each(function() {
					if (count == cars.length - 1) {
						count = 0;
					}
					$(this).addClass(cars[count]);
					count++;
				})

				$('body').addClass("dashboard_left");
			});
</script>
 --%>