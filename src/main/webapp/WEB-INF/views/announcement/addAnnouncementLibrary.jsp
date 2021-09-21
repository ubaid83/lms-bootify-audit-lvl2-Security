<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<html lang="en">
<jsp:include page="../common/css.jsp" />


<link
	href="https://gitcdn.github.io/bootstrap-toggle/2.2.2/css/bootstrap-toggle.min.css"
	rel="stylesheet">
<script
	src="https://gitcdn.github.io/bootstrap-toggle/2.2.2/js/bootstrap-toggle.min.js"></script>
<%
	String courseRecord = request.getParameter("courseRecord");
%>




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
			<%
				boolean isEdit = "true".equals((String) request
						.getAttribute("edit"));
			%>

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
							<i class="fa fa-angle-right"></i> Add Announcement
						</div>
						<jsp:include page="../common/alert.jsp" />
						<!-- Input Form Panel -->
						<div class="row">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">

									<div class="x_title">
										<h2>
											<%
												if (isEdit) {
											%>
											<%-- <form:input type="hidden" path="id" /> --%>
											Edit ${announcement.announcementType} Announcement
											<%
												} else {
											%>
											Add ${announcement.announcementType} Announcement
											<%
												}
											%>

										</h2>
										<ul class="nav navbar-right panel_toolbox">
											<li><a class="collapse-link"><i
													class="fa fa-chevron-up"></i></a></li>
										</ul>
										<div class="clearfix"></div>
									</div>

									<div class="x_content">
										<div class="row page-body">
											<div class="col-sm-12 column">
												<form:form action="addAnnouncement" method="post"
													id="addAnnouncement" modelAttribute="announcement"
													enctype="multipart/form-data">
													<fieldset>
														<form:input path="id" type="hidden" />

														<form:input path="announcementType" type="hidden" />


														<div class="row">


															<div class="col-sm-6 col-md-4 col-xs-12 column">
																<div class="form-group">
																	<form:label path="subject" for="subject">Announcement Title <span style="color: red">*</span></form:label>
																	<form:input path="subject" type="text"
																		class="form-control" required="required" />
																</div>
															</div>
															<div class="col-sm-6 col-md-4 col-xs-12 column">
																<div class="form-group">

																	<form:label path="startDate" for="startDate">Display From <span style="color: red">*</span></form:label>

																	<div class='input-group date' id='datetimepicker1'>
																		<form:input id="startDate" path="startDate"
																			type="text" placeholder="Start Date"
																			class="form-control" required="required"
																			readonly="true" />

																		<span class="input-group-addon"><span
																			class="glyphicon glyphicon-calendar"></span> </span>
																	</div>


																</div>
															</div>
															<div class="col-sm-6 col-md-4 col-xs-12 column">
																<div class="form-group">
																	<form:label path="endDate" for="endDate">Display Until <span style="color: red">*</span></form:label>

																	<div class='input-group date' id='datetimepicker2'>
																		<form:input id="endDate" path="endDate" type="text"
																			placeholder="End Date" class="form-control"
																			required="required" readonly="true" />
																		<span class="input-group-addon"><span
																			class="glyphicon glyphicon-calendar"></span> </span>
																	</div>

																</div>
															</div>
														</div>

														<div class="row">
															<div class="col-sm-6 col-md-4 col-xs-12 column">
																<div class="slider round">
																	<form:label path="sendEmailAlert" for="sendEmailAlert">Send Email Alert for New Announcement?</form:label>
																	<br>
																	<form:checkbox path="sendEmailAlert"
																		class="form-control" value="Y" data-toggle="toggle"
																		data-on="Yes" data-off="No" data-onstyle="success"
																		data-offstyle="danger" data-style="ios"
																		data-size="mini" />
																</div>
															</div>
															<c:if test="${sendAlertsToParents eq false}">
																<div class="col-sm-6 col-md-4 col-xs-12 column">
																	<div class="slider round">
																		<form:label path="sendEmailAlertToParents"
																			for="sendEmailAlertToParents">Send Email Alert To Parents?</form:label>
																		<br>
																		<form:checkbox path="sendEmailAlertToParents"
																			class="form-control" value="Y" data-toggle="toggle"
																			data-on="Yes" data-off="No" data-onstyle="success"
																			data-offstyle="danger" data-style="ios"
																			data-size="mini" />
																	</div>
																</div>
															</c:if>

															<div class="col-sm-6 col-md-4 col-xs-12 column">
																<div class="slider round">
																	<form:label path="sendSmsAlert" for="sendSmsAlert">Send SMS Alert for New Announcement?</form:label>
																	<br>
																	<form:checkbox path="sendSmsAlert" class="form-control"
																		value="Y" data-toggle="toggle" data-on="Yes"
																		data-off="No" data-onstyle="success"
																		data-offstyle="danger" data-style="ios"
																		data-size="mini" />
																</div>
															</div>
															<c:if test="${sendAlertsToParents eq false}">
																<div class="col-sm-6 col-md-4 col-xs-12 column">
																	<div class="slider round">
																		<form:label path="sendSmsAlertToParents"
																			for="sendSmsAlertToParents">Send SMS Alert To Parents?</form:label>
																		<br>
																		<form:checkbox path="sendSmsAlertToParents"
																			class="form-control" value="Y" data-toggle="toggle"
																			data-on="Yes" data-off="No" data-onstyle="success"
																			data-offstyle="danger" data-style="ios"
																			data-size="mini" />
																	</div>
																</div>
															</c:if>
															<div class="col-md-4 col-sm-6 col-xs-12 column">
																<div class="form-group">
																	<label for="file"> <%
 	if (isEdit) {
 %> Select if you wish to override earlier file <%
 	} else {
 %> Announcement File <%
 	}
 %>
																	</label> <input id="file" name="file" type="file"
																		class="form-control" multiple="multiple" />
																</div>
																<div id=fileSize></div>
															</div>
														</div>

														<hr>

														<div class="row">
															<form:label path="description" for="editor">Announcement Description</form:label>

															<form:textarea class="form-group" path="description"
																name="editor1" id="editor1" rows="10" cols="80" />

														</div>

														<div class="row">

															<div class="col-sm-8 column">
																<div class="form-group">

																	<%
																		if (isEdit) {
																	%>
																	<button id="submit" class="btn btn-large btn-primary"
																		formaction="updateAnnouncement">Update
																		Announcement</button>
																	<%
																		} else {
																	%>
																	<button id="submit" class="btn btn-large btn-primary"
																		formaction="addAnnouncement">Create
																		Announcement</button>
																	<%
																		}
																	%>
																	<button id="cancel" class="btn btn-danger"
																		formaction="homepage" formnovalidate="formnovalidate">Cancel</button>
																</div>
															</div>
														</div>



													</fieldset>
												</form:form>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>




					</div>



				</div>

			</div>
			<!-- /page content: END -->


			<jsp:include page="../common/footerLibrarian.jsp" />

		</div>
	</div>


	<script type="text/javascript"
		src="<c:url value="/resources/js/customDateTimePicker.js" />"></script>



</body>
<!-- <script src="//cdn.ckeditor.com/4.8.0/standard/ckeditor.js"></script>
 
<script>
                // Replace the <textarea id="editor1"> with a CKEditor
                // instance, using default configuration.
                CKEDITOR.replace( 'editor1' );
            </script>
            
            <script> -->
<script type="text/javascript"
	src="//cdn.ckeditor.com/4.8.0/standard-all/ckeditor.js"></script>
<script type="text/javascript"
	src="https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.0/MathJax.js?config=TeX-AMS_HTML"></script>

<script type="text/javascript">
	CKEDITOR
			.replace(
					'editor1',
					{
						extraPlugins : 'mathjax',
						mathJaxLib : 'https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.0/MathJax.js?config=TeX-AMS_HTML'
					});
</script>
<script>
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
</script>
</html>
