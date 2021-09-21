<jsp:include page="../common/topHeader.jsp" />
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
									<strong>Welcome to Support Admin Login </strong>

								</h4>
							</div>

							<div class="container-fluid dashboardcon">


								<div class="col-md-4 course_widget_height">
									<div class="dashboardcon_title bgcolor2">
										<div>
											<c:url value="viewSchoolsList" var="searchArticlesUrl">
												<c:param name="" value="" />
											</c:url>
										</div>
										<span><a href="${searchArticlesUrl}" />View Portal
											Feedbacks</a></span>
									</div>
								</div>

								<div class="col-md-4 course_widget_height">
									<div class="dashboardcon_title bgcolor3">
										<div>
											<c:url value="addNewAdminSchoolWiseForm" var="addAdminUrl">
												<c:param name="" value="" />
											</c:url>
										</div>
										<span><a href="${addAdminUrl}" />Add new Admin </a></span>
									</div>
								</div>

								<div class="col-md-4 course_widget_height">
									<div class="dashboardcon_title bgcolor4">
										<div>
											<c:url value="addAdminProgramForm" var="addAdminProgramUrl">
												<c:param name="" value="" />
											</c:url>
										</div>
										<span><a href="${addAdminProgramUrl}" />Admin Program
											Mapping</a></span>
									</div>
								</div>
								<div class="col-md-4 course_widget_height">
									<div class="dashboardcon_title bgcolor5">
										<div>
											<c:url value="addAdminMenuRightsForm"
												var="addAdminMenuRightsUrl">
												<c:param name="" value="" />
											</c:url>
										</div>
										<span><a href="${addAdminMenuRightsUrl}" />Admin Menu
											Rights</a></span>
									</div>
								</div>



								<div class="col-md-4 course_widget_height">
									<div class="dashboardcon_title bgcolor6">
										<div>
											<c:url value="enterCourseList" var="enterCourseList">
												<c:param name="" value="" />
											</c:url>
										</div>
										<span><a href="${enterCourseList}">Course List </a></span>
									</div>
								</div>

								<div class="col-md-4 course_widget_height">
									<div class="dashboardcon_title bgcolor7">
										<div>
											<c:url value="viewUserLoginDetails"
												var="viewUserLoginDetails">
												<c:param name="" value="" />
											</c:url>
										</div>
										<span><a href="${viewUserLoginDetails}">View User
												Login Details</a></span>
									</div>
								</div>
								<%-- <div class="col-md-4 course_widget_height">
									<div class="dashboardcon_title bgcolor8">
										<div>
											<c:url value="addOtherUserForm" var="addLibrarianUserUrl">
												<c:param name="" value="" />
											</c:url>
										</div>
										<span><a href="${addLibrarianUserUrl}">View User
												Login Details</a></span>
									</div>
								</div>
 --%>
								<div class="col-md-4 course_widget_height">
									<div class="dashboardcon_title bgcolor8">
										<div>
											<c:url value="addOtherUserForm" var="addLibrarianUserUrl">
												<c:param name="" value="" />
											</c:url>
										</div>
										<span><a href="${addLibrarianUserUrl}">Other User
												Login Creation </a></span>
									</div>
								</div>

								<div class="col-md-4 course_widget_height">
									<div class="dashboardcon_title bgcolor9">
										<div>
											<c:url value="createBFServiceSupportForm"
												var="createBFServiceSupportUrl">
												<c:param name="" value="" />
											</c:url>
										</div>
										<span><a href="${createBFServiceSupportUrl}">Create
												Student Service</a></span>
									</div>
								</div>


								<div class="col-md-4 course_widget_height">
									<div class="dashboardcon_title bgcolor10">
										<div>
											<c:url value="customizeMenuUser" var="customizeMenuUrl">
												<c:param name="" value="" />
											</c:url>
										</div>
										<span><a href="${customizeMenuUrl}">Menu Right
												Customization</a></span>
									</div>
								</div>

								<div class="col-md-4 course_widget_height">
									<div class="dashboardcon_title bgcolor11">
										<div>
											<c:url value="addNewFeatures" var="addNewFeaturesUrl">
												<c:param name="" value="" />
											</c:url>
										</div>
										<span><a href="${addNewFeaturesUrl}">PUBLISH FEATURE NOTIFICATION
												</a></span>
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
						"bgcolor9", "bgcolor10", "bgcolor11" ];
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


