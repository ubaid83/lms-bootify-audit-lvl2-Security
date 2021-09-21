<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<!-- <script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script> -->
<html lang="en">
<jsp:include page="../common/css.jsp" />


<link
	href="https://gitcdn.github.io/bootstrap-toggle/2.2.2/css/bootstrap-toggle.min.css"
	rel="stylesheet">
<script
	src="https://gitcdn.github.io/bootstrap-toggle/2.2.2/js/bootstrap-toggle.min.js"></script>
<%
	boolean isEdit = "true".equals((String) request
			.getAttribute("edit"));
%>


<body class="nav-md footer_fixed">

	<div class="loader"></div>


	<div class="container body">
		<div class="main_container">


			<jsp:include page="../common/leftSidebar.jsp">
				<jsp:param name="courseId" value="${courseId}" />
			</jsp:include>
			<jsp:include page="../common/topHeaderLibrian.jsp" />



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
							<i class="fa fa-angle-right"></i> Create New Library Page
						</div>
						<jsp:include page="../common/alert.jsp" />

						<!-- Input Form Panel -->
						<div class="row">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">

									<div class="x_title">
										<h2>Create New Library Page</h2>
										<ul class="nav navbar-right panel_toolbox">
											<li><a class="collapse-link"><i
													class="fa fa-chevron-up"></i></a></li>
										</ul>
										<div class="clearfix"></div>
									</div>

									<div class="x_content">
										<form:form action="createWebpageForLibrary"
											id="createWebpageForLibrary" method="post"
											modelAttribute="webpages" enctype="multipart/form-data">

											<%
												if (isEdit) {
											%>
											<form:input type="hidden" path="id" />
											<%
												}
											%>

											<div class="row">
												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<form:label path="name" for="name">Name <span style="color: red">*</span></form:label>
														<form:input path="name" type="text" class="form-control"
															required="required" />
													</div>
												</div>
											</div>
											<div class="row">
												<form:label path="description">Description about page </form:label>

												<form:textarea class="form-group" path="description"
													style="margin: 0px 0px 10px; width: 1292px; height: 96px;" />
											</div>


											<div class="row">
												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<form:label path="startDate" for="startDate">Start Date <span style="color: red">*</span></form:label>

														<div class='input-group date' id='datetimepicker1'>
														
															<form:input id="startDate" path="startDate" type="text"
																placeholder="Start Date" class="form-control"
																required="required" readonly="true" />
															<span class="input-group-addon"><span
																class="glyphicon glyphicon-calendar"></span> </span>
														</div>

													</div>
												</div>
											
												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<form:label path="endDate" for="endDate">End Date <span style="color: red">*</span></form:label>
														<%-- <form:input path="endDate" type="datetime-local"
															class="form-control" value="${assignment.endDate}"
															required="required" /> --%>

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
												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="slider round">

														<form:label path="makeAvailable" for="makeAvailable">Make available to all users ?</form:label>
														<br>
														<form:checkbox path="makeAvailable" class="form-control"
															for="makeAvailable" value="Y" data-toggle="toggle"
															data-on="Yes" data-off="No" data-onstyle="success"
															data-offstyle="danger" data-style="ios" data-size="mini" />
													</div>
												</div>














												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<label for="file"> <%
 	if (isEdit) {
 %> Select if you wish to override earlier file <%
 	} else {
 %> Attach File <%
 	}
 %>
														</label> <input id="file" name="file" type="file"
															class="form-control" />
													</div>
													<div id=fileSize></div>
												</div>

											</div>









											<div class="row">
												<form:label path="content" for="editor">Text / Content </form:label>

												<form:textarea class="form-group" path="content"
													name="editor1" id="editor1" rows="10" cols="80" />
											</div>

											<hr>
											<div class="row">

												<div class="col-sm-8 column">
													<div class="form-group">

														<%
															if (isEdit) {
														%>
														<button id="submit" class="btn btn-large btn-primary"
															formaction="updateWebpageForLibrary">Update Page</button>
														<%
															} else {
														%>
														<button id="submit" class="btn btn-large btn-primary"
															formaction="createWebpageForLibrary">Create Page</button>
														<%
															}
														%>
														<button id="cancel" class="btn btn-danger"
															formaction="homepage" formnovalidate="formnovalidate">Cancel</button>
													</div>
												</div>
											</div>



										</form:form>
									</div>
								</div>
							</div>
						</div>



					</div>



				</div>

			</div>
			<!-- /page content: END -->


			<jsp:include page="../common/footerLibrarian.jsp" />

			<script type="text/javascript">
				$('#file').bind('change', function() {
					// alert('This file size is: ' + this.files[0].size/1024/1024 + "MB");
					var fileSize = this.files[0].size / 1024 / 1024 + " MB";
					$('#fileSize').html("File Size:" + (fileSize));
				});
			</script>

		</div>
	</div>




	<script type="text/javascript"
		src="<c:url value="/resources/js/customDateTimePicker.js" />"></script>

</body>
<!-- <script src="//cdn.ckeditor.com/4.8.0/standard/ckeditor.js"></script>

<script>
	// Replace the <textarea id="editor1"> with a CKEditor
	// instance, using default configuration.
	CKEDITOR.replace('editor1');
</script> -->

<script type="text/javascript"
	src="//cdn.ckeditor.com/4.8.0/full-all/ckeditor.js"></script>
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
	//$(document).ready(function() {
	$("#datetimepicker1").on("dp.change", function(e) {

		validDateTimepicks();
	}).datetimepicker({
		//minDate : new Date(),
		useCurrent : false,
		format : 'YYYY-MM-DD HH:mm:ss'
	});

	$("#datetimepicker2").on("dp.change", function(e) {

		validDateTimepicks();
	}).datetimepicker({
		//minDate : new Date(),
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
				$('#startDate').val("");
				$('#endDate').val("");
			}
		} else {
			//alert('Please select start date and end date')
		}
	}

	//});
</script>

</html>
