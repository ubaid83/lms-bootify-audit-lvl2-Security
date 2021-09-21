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
					
					<div class="alert alert-success alert-dismissible" role="alert">
								<button type="button" class="close" data-dismiss="alert"
									aria-label="Close">
									<span aria-hidden="true">&times;</span>
								</button>
								<h4>
									<strong>Welcome to Staff Login </strong>

								</h4>
							</div>
					<!-- <div class="dash-main"> -->
					<div class="dashboard_height" id="main">


						<div class="dashboard_contain_specing dash-main">

							

							<div class="container-fluid dashboardcon">


								<div class="col-md-4 course_widget_height">
									<div class="dashboardcon_title bgcolor2">
										<div>
											<c:url value="viewServices" var="viewServicesUrl">
												<c:param name="" value="" />
											</c:url>
										</div>
										<span><a href="${viewServicesUrl}" >Student Service</a></span>
									</div>
								</div>

								<div class="col-md-4 course_widget_height">
									<div class="dashboardcon_title bgcolor3">
										<div>
											<c:url value="viewEscalatedServiceForLevel3"
												var="viewEscalatedServiceForLevel3Url">
												<c:param name="" value="" />
											</c:url>
										</div>
										<span><a href="${viewEscalatedServiceForLevel3Url}">Escalated
												Student Service</a></span>
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
