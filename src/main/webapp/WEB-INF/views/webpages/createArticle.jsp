<%@page import="com.spts.lms.web.utils.Utils"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@page import="java.util.*"%>



<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<jsp:include page="../common/newDashboardHeader.jsp" />

<div class="d-flex dataTableBottom" id="facultyAssignmentPage">
	<jsp:include page="../common/newLeftNavBarFaculty.jsp" />
	<jsp:include page="../common/newLeftSidebarFaculty.jsp" />
	<jsp:include page="../common/rightSidebarFaculty.jsp" />

	<!-- DASHBOARD BODY STARTS HERE -->

	<div class="container-fluid m-0 p-0 dashboardWraper">

		<jsp:include page="../common/newTopHeaderFaculty.jsp" />
		
		<%
			boolean isEdit = "true".equals((String) request
					.getAttribute("edit"));
		%>

		<div class="container mt-5">
			<div class="row">
				<!-- SEMESTER CONTENT -->
				<div
					class="col-lg-9 col-md-9 col-sm-12 dashboardBody ml-auto mr-auto">

					<!-- page content: START -->

					<nav aria-label="breadcrumb">
						<ol class="breadcrumb">
							<li class="breadcrumb-item"><a
								href="${pageContext.request.contextPath}/homepage">Dashboard</a></li>
							<li class="breadcrumb-item" aria-current="page"><c:out
									value="${Program_Name}" /></li>
							<sec:authorize access="hasRole('ROLE_STUDENT')">
								<c:out value="${AcadSession}" />
							</sec:authorize>
							<li class="breadcrumb-item active" aria-current="page">Create Article</li>
						</ol>
					</nav>

					<jsp:include page="../common/alert.jsp" />


					<!-- Input Form Panel -->
					<div class="card bg-white border">
						<div class="card-body">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">

									<div class="x_title">
										<h2>Create Article</h2>
										<ul class="nav navbar-right panel_toolbox">
											<li><a class="collapse-link"><i
													class="fa fa-chevron-up"></i></a></li>
										</ul>
										<div class="clearfix"></div>
									</div>

									<div class="x_content">
										<form:form action="createArticle" id="createArticle"
											method="post" modelAttribute="webpages"
											enctype="multipart/form-data">

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
														<form:label path="name" for="name">Name <span
																style="color: red">*</span>
														</form:label>
														<form:input path="name" type="text" class="form-control"
															required="required" />
													</div>
												</div>
											</div>
											<%-- <div class="row">
												<form:label path="description">Description about article </form:label>

												<form:textarea class="form-group" path="description"
													style="margin: 0px 0px 10px; width: 1292px; height: 96px;" />
											</div> --%>


											<div class="row">
												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<form:label path="startDate" for="startDate">Start Date <span
																style="color: red">*</span>
														</form:label>

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
														<form:label path="endDate" for="endDate">End Date <span
																style="color: red">*</span>
														</form:label>
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
														<form:label path="showArticle" for="showArticle">Show Article to<span
																style="color: red">*</span> : </form:label>
														<br>
														<form:radiobutton name="showArticle" id="school"
															value="school" path="showArticle" required="required" />
														Select Schools<br>
														<form:radiobutton name="showArticle" id="campus"
															value="campus" path="showArticle" />
														Select Campus<br>



													</div>

												</div>
												<div class="col-md-4 col-sm-6 col-xs-12 column"
													style="display: none" id="showSchools">
													<div class="form-group">
														<form:label path="schoolObjId" for="schoolObjId">Schools</form:label>
														<form:select id="schoolObjId" path="schoolObjId"
															multiple="multiple" class="form-control">
															<form:option value="">Select school</form:option>
															<c:forEach var="school" items="${listOfSchoolAndCampus}"
																varStatus="status">
																<c:if test="${not empty school.schoolObjId }">
																	<form:option value="${school.schoolObjId}">${school.collegeName }</form:option>
																</c:if>
															</c:forEach>
														</form:select>
													</div>
												</div>
												<div class="col-md-4 col-sm-6 col-xs-12 column"
													style="display: none" id="showCampus">
													<div class="form-group">
														<form:label path="campusName" for="campusName">Campus</form:label>
														<form:select id="campusName" path="campusName"
															multiple="multiple" class="form-control">
															<form:option value="">Select campus</form:option>
															<c:forEach var="campus" items="${listOfSchoolAndCampus}"
																varStatus="status">
																<c:if test="${not empty campus.location }">

																	<form:option value="${campus.location}">${campus.location}</form:option>
																</c:if>
															</c:forEach>
														</form:select>
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
														<label for="file"> 
														<%
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
															formaction=createArticle>Update Article</button>
														<%
															} else {
														%>
														<button id="submit" class="btn btn-large btn-primary"
															formaction="createArticle">Create Article</button>
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

					<!-- Results Panel -->




					<%-- 	<jsp:include page="../common/paginate.jsp">
							<jsp:param name="baseUrl" value="searchFacultyGroups" />
						</jsp:include> --%>

					<!-- /page content: END -->

				</div>

				<!-- SIDEBAR START -->
				<jsp:include page="../common/newSidebar.jsp" />
				<!-- SIDEBAR END -->
				<jsp:include page="../common/footer.jsp" />

				<script>
					$(function() {

						$('#school').on('click', function() {
							//alert('school');
							$('#showSchools').show();
							$('#showCampus').hide();
							//$("#Submission").prop('required', true);

						});

						$('#campus').on('click', function() {
							//alert('campus');
							$('#showCampus').show();
							$('#showSchools').hide();

							//$("#Submission").prop('required', false);

						});

					});
				</script>
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
						if (($('#startDate').val() != '' && $('#startDate')
								.val() != null)
								&& ($('#endDate').val() != '' && $('#endDate')
										.val() != null)) {
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