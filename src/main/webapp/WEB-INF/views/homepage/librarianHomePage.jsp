<jsp:include page="../common/topHeaderLibrian.jsp" />
<jsp:include page="../common/css.jsp" />
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<body class="nav-md footer_fixed dashboard_left">

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
									<strong>Welcome to Librarian Login of<p>${libraryName }</p> </strong>
									
								</h4>
							</div>

							<div class="container-fluid dashboardcon">


								<%-- <div class="col-md-4 course_widget_height">
									<div class="dashboardcon_title bgcolor2">
										<div>
											<c:url value="searchAnnouncementForLibrarian" var="viewAnnouncementUrl">
												<c:param name="" value="" />
											</c:url>
										</div>
										<span><a href="${viewAnnouncementUrl}" />Announcement</a></span>
									</div>
								</div> --%>

								<div class="col-md-4 course_widget_height">
									<div class="dashboardcon_title bgcolor4">
										<div>
											<c:url value="viewLibraryAnnouncements" var="viewLibraryUrl">
												<c:param name="announcementType" value="LIBRARY" />
											</c:url>
										</div>
										<span><a href="${viewLibraryUrl}" />Library</a></span>
									</div>
								</div>
								
										<div class="col-md-4 course_widget_height">
									<div class="dashboardcon_downloadManual bgcolor4">
										<div>
											<c:url value="downloadPortalManuals" var="downloadPortalManualsStudent">
												<c:param name="student" value="Student" />
											</c:url>
											
											<c:url value="downloadPortalManuals" var="downloadPortalManualsFaculty">
												<c:param name="student" value="Faculty" />
											</c:url>
											
										</div>
									<%-- 	<span><a href="${viewLibraryUrl}" />Stude</a></span> --%>
										<h4 class="text-white">Download Manual</h4>
										<a class="dashboardcon_downloadManual_text" href="${downloadPortalManualsStudent}">Student Manual</a><br>
										<a class="dashboardcon_downloadManual_text" href="${downloadPortalManualsFaculty}">Faculty Manual</a>
									</div>
								</div>
								
								<div class="col-md-4 course_widget_height">
									<div class="dashboardcon_title bgcolor4">
										<div>
											
										</div>
										<span><a href="https://ezproxy.svkm.ac.in/login" target="_blank"/>E-database Link</a></span>
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

<jsp:include page="../common/footerLibrarian.jsp" />

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
