<%@page import="java.util.*"%>
<%@page import="com.spts.lms.beans.dashboard.DashBoard"%>
<%-- <jsp:include page="../common/DashboardHeader.jsp" /> --%>
<%-- <jsp:include page="../common/NewHeader1.jsp" /> --%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

<sec:authorize access="hasRole('ROLE_EXAM')">
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
											<c:if test="${announcement.announcementType == 'COURSE'}">
        for ${courseIdNameMap[announcement.courseId] } 
        <font size="3px">(Will be visible to All Students
													Enrolled in this Course)</font>
											</c:if>
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
														<form:input path="courseId" type="hidden" />
														<form:input path="programId" type="hidden" />
														<form:input path="announcementType" type="hidden" />

														<%-- <legend>
															<%
																if (isEdit) {
															%>
															<form:input type="hidden" path="id" />
															Edit ${announcement.announcementType} Announcement
															<%
																} else {
															%>
															Add ${announcement.announcementType} Announcement
															<%
																}
															%>
															<c:if
																test="${announcement.announcementType == 'INSTITUTE'}">
																<font size="3px">(Will be visible to All Students
																	of Institute)</font>
															</c:if>
															<c:if test="${announcement.announcementType == 'COURSE'}">
								for ${courseIdNameMap[announcement.courseId] } 
								<font size="3px">(Will be visible to All Students
																	Enrolled in this Course)</font>
															</c:if>
														</legend> --%>

														<div class="row">

															<sec:authorize
																access="hasRole('ROLE_EXAM')">
																<div class="col-sm-6 col-md-4 col-xs-12 column">
																	<div class="form-group">
																		<label class="control-label" for="announcementSubType">Select
																			Announcement Subtype</label>
																		<form:select id="announcementSubType"
																			path="announcementSubType"
																			placeholder="Announcement Subtype"
																			class="form-control">
																			<form:option value="">Select Announcement Subtype</form:option>
																			<form:option value="EXAM">EXAM</form:option>
																			<form:option value="EVENT">EVENT</form:option>
																			<form:option value="ASSIGNMENT">ASSIGNMENT</form:option>
																			<form:option value="Internal">Internal Marks</form:option>
																			<form:option value="Academics">Academics</form:option>
																			<form:option value="WeCare">We Care</form:option>
																			<form:option value="FROMTHECOUNSELLORSDESK">From the Counsellor's Desk</form:option>

																		</form:select>
																	</div>
																</div>
															</sec:authorize>
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
														<c:if test="${allCampuses.size() > 0}">
															<div class="col-md-4 col-sm-6 col-xs-12 column">
																<div class="form-group">
																	<form:label path="campusId" for="campusId">Select Campus</form:label>

																	<form:select id="campusId" path="campusId" type="text"
																		placeholder="campus" class="form-control">
																		<form:option value="">Select Campus</form:option>
																		<c:forEach var="campus" items="${allCampuses}"
																			varStatus="status">
																			<form:option value="${campus.campusId}">${campus.campusName}</form:option>
																		</c:forEach>
																	</form:select>
																</div>
															</div>
															</c:if>
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

</sec:authorize>


<sec:authorize access="hasRole('ROLE_FACULTY')">


<%
	List<DashBoard> courseDetailList = (List<DashBoard>) session
      .getAttribute("courseDetailList");
      System.out.println("SIZE :"+courseDetailList.size());
      Map<String,List<DashBoard>> dashboardListSemesterWise = (Map<String,List<DashBoard>>)
      session.getAttribute("sessionWiseCourseListMap");
%>
<%
	boolean isEdit = "true".equals((String) request
      .getAttribute("edit"));
%>
<jsp:include page="../common/newDashboardHeader.jsp" />

<script type="text/javascript">
	var headerCourseId = null;
	function theFunction(obj) {

		return true;
	}
</script>
<div class="d-flex" id="facultyDashboardPage">
	<jsp:include page="../common/newLeftNavBarFaculty.jsp" />
	<jsp:include page="../common/newLeftSidebarFaculty.jsp" />
	<jsp:include page="../common/rightSidebarFaculty.jsp" />

	<!-- DASHBOARD BODY STARTS HERE -->

	<div class="container-fluid m-0 p-0 dashboardWraper">

		<jsp:include page="../common/newTopHeaderFaculty.jsp" />

		<div class="container mt-5">
			<div class="row">
				<div class="container mt-5">
					<div class="row">
						<div
							class="col-lg-9 col-md-9 col-sm-12 dashboardBody ml-auto mr-auto">
							<nav aria-label="breadcrumb">
								<ol class="breadcrumb">
									<li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/homepage">Dashboard</a></li>
									<li class="breadcrumb-item active" aria-current="page">Create
										Announcement</li>
								</ol>
							</nav>
								<!-- 14-06-2021 -->
							<jsp:include page="../common/alert.jsp" />
							<div class="card bg-white">
								<div class="card-body">

									<form:form action="addAnnouncement" id="addAnnouncement"
										method="post" modelAttribute="announcement"
										enctype="multipart/form-data">
										<form:input path="courseId" type="hidden" />
										<form:input path="programId" type="hidden" />
										<form:input path="announcementType" type="hidden" />
										<h4 class="text-uppercase">
											<%
												if (isEdit) {
											%>
											<form:input type="hidden" path="id" />
											Edit ${announcement.announcementType} Announcement
											<%
												} else {
											%>
											Add ${announcement.announcementType} Announcement
											<%
												}
											%>
										</h4>
										<hr />
										<div class="form-row">

											<div class="col-12 text-center">
												<h5>
													<sec:authorize access="hasRole('ROLE_ADMIN')">
														<c:if
															test="${announcement.announcementType == 'INSTITUTE'}">
															(Will be visible to All Students
																of Institute)
														</c:if>
													</sec:authorize>
													<c:if test="${announcement.announcementType == 'COURSE'}">
         ${courseIdNameMap[announcement.courseId] } 
        <font size="3px">(Will be visible to All Students
															Enrolled in this Course)</font>
													</c:if>
												</h5>
											</div>

											<!--FORM ITEMS STARTS HERE-->

											<sec:authorize access="hasAnyRole('ROLE_ADMIN', 'ROLE_EXAM')">
												<div
													class="col-lg-4 col-md-4 col-sm-12 mt-3 position-relative">
													<label class="f-13 text-danger req">*</label>
													<%-- <form:input id="assignmentName" path="assignmentName"
                                                                              type="text" placeholder="Assignment Name"
                                                                              class="form-control" required="required" /> --%>
													<form:select id="announcementSubType"
														path="announcementSubType"
														placeholder="Announcement Subtype" class="form-control">
														<form:option value="">Select Announcement Subtype</form:option>
														<form:option value="EXAM">EXAM</form:option>
														<form:option value="EVENT">EVENT</form:option>
														<form:option value="ASSIGNMENT">ASSIGNMENT</form:option>
														<form:option value="Internal">Internal Marks</form:option>
														<form:option value="Academics">Academics</form:option>
														<form:option value="WeCare">We Care</form:option>
														<form:option value="FROMTHECOUNSELLORSDESK">From the Counsellor's Desk</form:option>

													</form:select>
												</div>
											</sec:authorize>


											<div
												class="col-lg-4 col-md-4 col-sm-12 mt-3 position-relative">
												<label class="f-13 text-danger req">*</label>
												<form:input id="subject" path="subject" type="text"
													placeholder="Announcement Title" class="form-control"
													required="required" />
											</div>
<%-- 
											<div
												class="col-lg-8 col-md-8 col-sm-12 mt-3 position-relative">
												<div class="input-group">
													<label class="f-13 text-danger req">*</label> <label
														class="sr-only">Select Date Range</label>
													<form:input type="hidden" id="testStartDate"
														path="startDate" />
													<form:input type="hidden" id="testEndDate" path="endDate" />
													<input id="startDate" type="text"
														placeholder="Display From - Display Until"
														class="form-control" required="required" readonly />
													<div class="input-group-append">
														<button class="btn btn-outline-secondary" type="button"
															id="testDateRangeBtn">
															<i class="fas fa-calendar"></i>
														</button>
													</div>
												</div>
											</div> --%>


											<%
												if(isEdit) {
											%>

											<div
												class="col-lg-8 col-md-8 col-sm-12 mt-3 position-relative">
												<div class="input-group">
													<label class="f-13 text-danger req">*</label> <label
														class="sr-only">Select Date Range</label>
													<form:input type="hidden" id="testStartDate"
														path="startDate" />
													<form:input type="hidden" id="testEndDate" path="endDate" />
													<input id="startDate" type="text"
														placeholder="Start Date - End Date" class="form-control"
														required="required"
														value="${announcement.startDate} - ${announcement.endDate}"
														readonly />
													<div class="input-group-append">
														<button class="btn btn-outline-secondary" type="button"
															id="testDateRangeBtn">
															<i class="fas fa-calendar"></i>
														</button>
													</div>
												</div>
											</div>

											<%
												}else{
											%>
											<div
												class="col-lg-8 col-md-8 col-sm-12 mt-3 position-relative">
												<div class="input-group">
													<label class="f-13 text-danger req">*</label> <label
														class="sr-only">Select Date Range</label>
													<form:input type="hidden" id="testStartDate"
														path="startDate" />
													<form:input type="hidden" id="testEndDate" path="endDate" />
													<input id="startDate" type="text"
														placeholder="Start Date - End Date" class="form-control"
														required="required" />
													<div class="input-group-append">
														<button class="btn btn-outline-secondary" type="button"
															id="testDateRangeBtn">
															<i class="fas fa-calendar"></i>
														</button>
													</div>
												</div>
											</div>
											<%
												}
											%>




											<div
												class="col-lg-6 col-md-6 col-sm-12 mt-3 position-relative">
												<div class="form-group">
													<label for="file"> <%
 	if (isEdit) {
 %> Select if you wish to override earlier file <%
 	} else {
 %> Announcement Question File <%
 	}
 %>
													</label> <input id="file" name="file" type="file"
														class="form-control" multiple="multiple" />
												</div>
												<div id=fileSize></div>
											</div>


											<c:if test="${allCampuses.size() > 0}">
												<div
													class="col-lg-6 col-md-6 col-sm-12 mt-3 position-relative">
													<div class="form-group">
														<form:label path="campusId" for="campusId">Select Campus</form:label>

														<form:select id="campusId" path="campusId" type="text"
															placeholder="campus" class="form-control">
															<form:option value="">Select Campus</form:option>
															<c:forEach var="campus" items="${allCampuses}"
																varStatus="status">
																<form:option value="${campus.campusId}">${campus.campusName}</form:option>
															</c:forEach>
														</form:select>
													</div>
												</div>
											</c:if>

										</div>


										<hr />

										<div class="form-row">
											<table
												class="table table-bordered table-striped mt-5 font-weight-bold">
												<tbody>
													<tr>
														<td>Send Email Alert for New Announcement?</td>
														<td>
															<div class="pretty p-switch p-fill p-toggle">
																<form:checkbox value="Yes" id="sendEmailAlert"
																	path="sendEmailAlert" />
																<div class="state p-success p-on">
																	<label>Yes</label>
																</div>
																<div class="state p-danger p-off">
																	<label>No</label>
																</div>

															</div>
														</td>
													</tr>
													<tr>
														<td>Send SMS Alert for New Announcement?</td>
														<td>
															<div class="pretty p-switch p-fill p-toggle">
																<form:checkbox value="Yes" id="sendSmsAlert"
																	path="sendSmsAlert" />
																<div class="state p-success p-on">
																	<label>Yes</label>
																</div>
																<div class="state p-danger p-off">
																	<label>No</label>
																</div>

															</div>
														</td>
													</tr>
													<c:if test="${sendAlertsToParents eq true}">
														<tr>
															<td>Send Email Alert To Parents?</td>
															<td>
																<div class="pretty p-switch p-fill p-toggle">
																	<form:checkbox value="Yes" id="sendEmailAlertParents"
																		path="sendEmailAlertToParents" />
																	<div class="state p-success p-on">
																		<label>Yes</label>
																	</div>
																	<div class="state p-danger p-off">
																		<label>No</label>
																	</div>

																</div>
															</td>
														</tr>
														<tr>
															<td>Send SMS Alert To Parents?</td>
															<td>
																<div class="pretty p-switch p-fill p-toggle">
																	<form:checkbox value="Yes" id="sendEmailAlertParents"
																		path="sendSmsAlertToParents" />
																	<div class="state p-success p-on">
																		<label>Yes</label>
																	</div>
																	<div class="state p-danger p-off">
																		<label>No</label>
																	</div>

																</div>
															</td>
														</tr>
													</c:if>
												</tbody>
											</table>
										</div>


										<div class="col-12 mt-5">
											<h5 class="text-center">Announcement Text / Instructions</h5>
											<form:textarea id="editor1" class="editor1" name="editor1"
												path="description"></form:textarea>
										</div>

										<div class="col-6 m-auto">
											<div class="form-row mt-5">


												<%
													if (isEdit) {
												%>
												<button id="submit"
													class="btn btn-info col-md-5 col-sm-12  ml-auto mr-auto mb-3"
													formaction="updateAnnouncement">Update Announcement</button>
												<%
													} else {
												%>
												<button id="submit" onclick="myFunction()"
													class="btn btn-info col-md-5 col-sm-12  ml-auto mr-auto mb-3"
													formaction="addAnnouncement">Add Announcement</button>
												<%
													}
												%>




											</div>
										</div>



									</form:form>



								</div>
							</div>







						</div>
						<jsp:include page="../common/newSidebar.jsp" />

					</div>
				</div>
				<!-- SIDEBAR START -->

				<!-- SIDEBAR END -->
				<jsp:include page="../common/footer.jsp" />
				</sec:authorize>

				<script type="text/javascript">
					$('#file').bind(
							'change',
							function() {
								// alert('This file size is: ' + this.files[0].size/1024/1024 + "MB");
								var fileSize = this.files[0].size / 1024 / 1024
										+ " MB";
								$('#fileSize').html("File Size:" + (fileSize));
							});
				</script>
				
				      <script>
		function myFunction(){
			console.log("fdghdhyrfdhuytuytrutryutrruht");
			var dateinput = $('#startDate').val();
			console.log(dateinput);
		}
		
		$(function() {
			
			$('#submit').on('click', function(e){ 
				var dateinput = $('#startDate').val();
				console.log(dateinput);
				if(dateinput == ''){
					alert('Error: Enter proper Date inputs.');
					
					    e.preventDefault();
				
				} else {
					
				}
				
			});
			
		});
        </script>