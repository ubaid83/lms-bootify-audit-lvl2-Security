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


			<!-- page content: START -->
			<div class="right_col" role="main">

				<div class="dashboard_container">

					<div class="dashboard_container_spacing">

						<div class="breadcrumb">
			


							<br> <br> <a
								href="${pageContext.request.contextPath}/homepage">Dashboard</a>
							<i class="fa fa-angle-right"></i> Add Announcement
						</div>
						<jsp:include page="../common/alert.jsp" />

						<!-- Input Form Panel -->
						<div class="row">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">

									<div class="x_title">
										<h2>Add Announcement For Server</h2>
										<ul class="nav navbar-right panel_toolbox">
										</ul>
										<div class="clearfix"></div>
									</div>


									<div class="x_content">

										<form:form action="createAnnouncementBySupportAdminForm"
											method="post" modelAttribute="announcement">

											<div class="col-sm-6 col-md-4 col-xs-12 column">
												<div class="form-group">
													<form:label path="announcementTitle" for="announcementTitle">Announcement Title <span
															style="color: red">*</span>
													</form:label>
													<form:input path="announcementTitle" type="text" class="form-control"
														required="required" />
												</div>
											</div>


											<div class="col-sm-6 col-md-4">
												<div class="form-group">
													<label class="small"><strong>Start Date</strong> <span
														class="text-danger f-13">*</span></label>
													<div class="input-group">
														<div class='input-group date' id='datetimepicker1'>
															<form:input id="startDate" path="startDate" type="text"
																placeholder="Start Date" class="form-control"
																readonly="true" />
															<span class="input-group-addon"><span
																class="glyphicon glyphicon-calendar"></span> </span>
														</div>
													</div>
												</div>
											</div>
											<div class="col-sm-6 col-md-4">
												<label class="small"><strong>End Date</strong> <span
													class="text-danger f-13">*</span></label>

												<div class="input-group">
													<div class='input-group date' id='datetimepicker2'>
														<form:input id="endDate" path="endDate" type="text"
															placeholder="End Date" class="form-control"
															readonly="true" />
														<span class="input-group-addon"><span
															class="glyphicon glyphicon-calendar"></span> </span>
													</div>
												</div>
											</div>





											<div class="row">
												<form:label path="announcementDesc" for="announcementDesc">Announcement Description (This will be visible to All)</form:label>

												<form:textarea class="form-group" path="announcementDesc"
													name="announcementDesc" id="editor" rows="10" cols="80" />

											</div>



											<div class="row">

												<div class="col-sm-8 column">
													<div class="form-group">

														
														<button id="submit" class="btn btn-large btn-primary"
															formaction="createAnnouncementBySupportAdmin">Create
															Announcement</button>
														
														<button id="cancel" class="btn btn-danger"
															formaction="homepage" formnovalidate="formnovalidate">Cancel</button>
													</div>
												</div>
											</div>
										</form:form>

									</div>

									<!-- Ends Here -->


								</div>
							</div>
						</div>



					</div>



				</div>

			</div>
			<!-- /page content: END -->


			<jsp:include page="../common/footerLibrarian.jsp" />
			<script type="text/javascript"
				src="<c:url value="/resources/js/customDateTimePicker.js" />"></script>

		</div>
	</div>

</body>
</html>
<script>
	//$(document).ready(function() {
	$("#datetimepicker1").on("dp.change", function(e) {

		validDateTimepicks();
	}).datetimepicker({
		// minDate:new Date(),
		useCurrent : false,
		format : 'YYYY-MM-DD HH:mm:ss'
	});

	$("#datetimepicker2").on("dp.change", function(e) {

		validDateTimepicks();
	}).datetimepicker({
		// minDate:new Date(),
		useCurrent : false,
		format : 'YYYY-MM-DD HH:mm:ss'
	});

	function validDateTimepicks() {
		if (($('#startDate').val() != '' && $('#startDate').val() != null)
				&& ($('#endDate').val() != '' && $('#endDate').val() != null)) {
			var fromDate = $('#startDate').val();
			var toDate = $('#endDate').val();
			var eDate = new Date(fromDate);
			var sDate = new Date(toDate);
			if (sDate < eDate) {
				alert("endate cannot be smaller than startDate");
				$('#startDate').val("");
				$('#endDate').val("");
			}
		}
	}
	//});
</script>