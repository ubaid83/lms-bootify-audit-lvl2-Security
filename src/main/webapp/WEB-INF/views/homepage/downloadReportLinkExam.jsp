<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<html lang="en">
<jsp:include page="../common/css.jsp" />

<body class="nav-md footer_fixed">

	<div class="loader"></div>


	<div class="container body">
		<div class="main_container">


			<jsp:include page="../common/leftSidebar.jsp">
				<jsp:param name="courseId" value="${courseId}" />
			</jsp:include>


			<jsp:include page="../common/topHeaderLibrian.jsp">
				<jsp:param value="Announcement" name="activeMenu" />
			</jsp:include>

			<!-- page content: START -->
			<div class="right_col" role="main">

				<div class="dashboard_container">

					<div class="dashboard_container_spacing">

						<div class="breadcrumb">
							<c:out value="${Program_Name}" />

							<sec:authorize access="hasRole('ROLE_STUDENT')">

								<i class="fa fa-angle-right"></i>

								<c:out value="${AcadSession}" />

							</sec:authorize>
							<br> <br> <a
								href="${pageContext.request.contextPath}/homepage">Dashboard</a>
							<i class="fa fa-angle-right"></i> Exam Report
						</div>
						<jsp:include page="../common/alert.jsp" />
						<!-- Input Form Panel -->
						<div class="row">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">

									<div class="x_title">
										<h2>Exam Report</h2>
										<sec:authorize access="hasAnyRole('ROLE_LIBRARIAN')">
											<c:url value="addAnnouncementFormLibrary"
												var="addAnnouncementUrl">

											</c:url>
										</sec:authorize>

										<sec:authorize access="hasAnyRole('ROLE_EXAM')">
											<c:url value="addAnnouncementForm" var="addAnnouncementUrl">

											</c:url>
										</sec:authorize>

										
										<ul class="nav navbar-right panel_toolbox">
											<li><a class="collapse-link"><i
													class="fa fa-chevron-up"></i></a></li>
										</ul>
										<div class="clearfix"></div>
									</div>

									<div class="x_content">
				
					<div class="panel panel-default">
								<div class="panel-heading" role="tab" id="heading-71">
									<h4 class="panel-title" data-toggle="collapse"
										data-parent="#feedbackReportAccordion"
										data-target="#collapse-71" aria-expanded="false"
										aria-controls="collapse-71">Student-Portal Utilization
										Report(Consolidated)</h4>
								</div>
								<div id="collapse-71" class="panel-collapse collapse"
									role="tabpanel" aria-labelledby="heading-71">
									<div class="panel-body">

										<form action="featureWiseUtilisationReport" method="get">
											<div class="row">

												<div class="col-md-6 col-sm-12">
													<div class="form-group">
														<label class="control-label" for="campus1">Campus</label>
														<select id="campus1" class="form-control facultyparameter">
															<option value="" selected disabled hidden>Select
																Campus</option>
															<c:forEach var="listValue" items="${campusList}">
																<option value="${listValue.campusId}">${listValue.campusName}</option>
															</c:forEach>
														</select>
													</div>
												</div>


												<div class="col-md-6 col-sm-12">
													<div class="form-group">

														<label path="" for="startDate">From Date <span
															style="color: red">*</span></label>

														<div class='input-group date' id='datetimepicker1'>
															<input id="startDate" type="text" placeholder="From Date"
																class="form-control" required="required" readonly="true" />
															<span class="input-group-addon"><span
																class="glyphicon glyphicon-calendar"></span> </span>
														</div>

													</div>
												</div>

												<div class="col-md-6 col-sm-12">
													<div class="form-group">

														<label path="" for="endDate">To Date <span
															style="color: red">*</span></label>

														<div class='input-group date' id='datetimepicker2'>
															<input id="endDate" type="text" placeholder="To Date"
																class="form-control" required="required" readonly="true" />
															<span class="input-group-addon"><span
																class="glyphicon glyphicon-calendar"></span> </span>
														</div>

													</div>
												</div>


												<div class="col-12">
													<a class="btn btn-large btn-primary"
														onclick="submitFeatureWiseConsoliUtilisationReport()">
														Generate Report</a>
												</div>

											</div>
										</form>
										<hr />

									</div>
								</div>


							</div>
				
									</div>
								</div>
							</div>
						</div>

				
						<jsp:include page="../common/paginate.jsp">
							<jsp:param name="baseUrl" value="searchAnnouncement" />
						</jsp:include>
					</div>



				</div>

			</div>
			<!-- /page content: END -->


			<jsp:include page="../common/footerLibrarian.jsp" />

		</div>
	</div>




	<script type="text/javascript"
		src="<c:url value="/resources/js/customDateTimePicker.js" />"></script>


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
	
	<script>
	
	function submitFeatureWiseConsoliUtilisationReport() {
		var fromDate = $('#startDate').val();
		var endDate = $('#endDate').val();
		var campus = $('#campus1').val();

		if (fromDate != null && endDate != null) {

			window.location.href = '${pageContext.request.contextPath}/lmsUsageReportCombinedWithAllCampus?'
					+ 'fromDate='
					+ fromDate
					+ '&toDate='
					+ endDate
					+ '&campus=' + campus;

		} else {
			alert("please fill all the fields");
		}
	}
	</script>

</body>
</html>

