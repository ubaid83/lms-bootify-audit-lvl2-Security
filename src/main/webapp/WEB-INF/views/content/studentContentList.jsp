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

<c:url value="/studentContentList" var="rootFolder">
	<c:param name="courseId" value="${content.courseId}" />
	<c:param name="acadMonth" value="${content.acadMonth}" />
	<c:param name="acadYear" value="${content.acadYear}" />
</c:url>
<div class="d-flex dataTableBottom" id="facultyAssignmentPage">
	<sec:authorize access="hasRole('ROLE_FACULTY')">
		<jsp:include page="../common/newLeftNavBarFaculty.jsp" />
		<jsp:include page="../common/newLeftSidebarFaculty.jsp" />
		<jsp:include page="../common/rightSidebarFaculty.jsp" />
	</sec:authorize>
	<sec:authorize access="hasRole('ROLE_STUDENT')">
		<jsp:include page="../common/newLeftNavbar.jsp" />
		<jsp:include page="../common/newLeftSidebar.jsp" />
		<jsp:include page="../common/rightSidebar.jsp" />
	</sec:authorize>

	<!-- DASHBOARD BODY STARTS HERE -->

	<div class="container-fluid m-0 p-0 dashboardWraper">

		<sec:authorize access="hasRole('ROLE_FACULTY')">
			<jsp:include page="../common/newTopHeaderFaculty.jsp" />
		</sec:authorize>
		<sec:authorize access="hasRole('ROLE_STUDENT')">
			<jsp:include page="../common/newTopHeader.jsp" />
		</sec:authorize>

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
							<li class="breadcrumb-item active" aria-current="page">
								Content List</li>
						</ol>
					</nav>

					<jsp:include page="../common/alert.jsp" />


					<!-- Input Form Panel -->
					<div class="card bg-white border">
						<div class="card-body">

							<div class="form-row">
								<%-- 	<form:label path="acadYear" for="acadYear">
																<strong>Academic year <span style="color: red">*</span></strong>
															</form:label> --%>
								<div class="col-lg-5 col-md-5 col-sm-12 mt-3">
									<%-- <form:select id="acadYearForSearch" path="acadYear" class="form-control"
										required="required">
										<c:if test="${content.acadYear eq null}">
											<option value="">Select Year</option>
										</c:if>
										<c:forEach var="acadYear" items="${acadYear}"
											varStatus="status">
											<c:if test="${content.acadYear ne null}">
												<c:if test="${content.acadYear eq acadYear}">
													<option value="${acadYear}" selected>${acadYear}</option>
												</c:if>
												<c:if test="${content.acadYear ne acadYear}">
													<option value="${acadYear}">${acadYear}</option>
												</c:if>
											</c:if>
											<c:if test="${content.acadYear eq null}">
												<form:option value="${acadYear}">${acadYear}</form:option>
											</c:if>
										</c:forEach>
									</form:select> --%>
									<select id="acadYearForSearch" name="acadYear"
										class="form-control" required="required">
										<c:if test="${content.acadYear eq null}">
											<option value="">Select Year</option>
										</c:if>
										<c:forEach var="acadYear" items="${acadYearList}"
											varStatus="status">
											<c:if test="${content.acadYear ne null}">
												<c:if test="${content.acadYear eq acadYear}">
													<option value="${acadYear}" selected>${acadYear}</option>
												</c:if>
												<c:if test="${content.acadYear ne acadYear}">
													<option value="${acadYear}">${acadYear}</option>
												</c:if>
											</c:if>
											<c:if test="${content.acadYear eq null}">
												<option value="${acadYear}">${acadYear}</option>
											</c:if>
										</c:forEach>
									</select>
								</div>
								<%-- 	<div class="col-lg-5 col-md-5 col-sm-12 mt-3">
									<form:select id="acadYear" path="acadYear" class="form-control">
										<option class="sr-only">Select Year</option>
										<c:forEach var="acadYear" items="${acadYear}"
											varStatus="status">
											<form:option value="${acadYear}">${acadYear}</form:option>
										</c:forEach>
									</form:select>
							
								</div> --%>

								<div class="col-lg-5 col-md-5 col-sm-12 mt-3">
									<select name="contentCourseIdForPri" class="form-control"
										id="contentCourseStudent11">
										<c:if test="${empty courseId}">
											<option value="" disabled selected>--SELECT COURSE--</option>
										</c:if>
										<c:forEach var='cList' items='${ courseList }'>
											<c:if test="${courseId eq cList.id}">
												<option value="${cList.id}" selected>${cList.courseName}</option>
											</c:if>
											<c:if test="${courseId ne cList.id }">
												<option value="${cList.id}">${cList.courseName}</option>
											</c:if>
										</c:forEach>
									</select>
								</div>

							</div>

						</div>
					</div>

					<!-- Results Panel -->


					<div class="card bg-white border">
						<div class="card-body">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">
									<div class="x_title">
										<h5>Content List</h5>
										<div class="col-12 text-right">

											<a class="btn btn-large btn-dark mb-2"
												href="downloadAllContent">Download All Resources</a>
											<c:if test="${allContent.size() > 0}">

												<c:if test="${content.courseId ne null}">

													<a class="btn btn-large btn-dark mb-2"
														href="downloadAllContent?courseId=${content.courseId}">Download
														All Resources For Course</a>
												</c:if>
											</c:if>
										</div>


									</div>
									<div class="x_content">
										<div class="table-responsive testAssignTable">


											<table class="table table-striped table-hover tabledesorder"
												style="font-size: 12px" id="contentTree">
												<thead>
													<tr>
														<th>Content Name</th>
														<th>Description</th>
														<th>Date</th>
													</tr>
												</thead>
												<tbody>

													<c:forEach var="content" items="${allContent}"
														varStatus="status">
														<tr data-tt-id="${content.id}"
															data-tt-parent-id="${content.parentContentId}">

															<td><c:if test="${content.contentType == 'Folder' }">
																	<i class="fa lms-folder-o fa-lg"
																		style="background: #E6CB47; margin-right: 5px"></i>

																	<c:url value="/getContentUnderAPathForStudent"
																		var="navigateInsideFolder">
																		<c:param name="courseId" value="${content.courseId}" />
																		<c:param name="moduleId" value="${content.moduleId}" />
																		<c:param name="acadMonth" value="${content.acadMonth}" />
																		<c:param name="acadYear" value="${content.acadYear}" />
																		<c:param name="folderPath" value="${content.filePath}" />
																		<c:param name="parentContentId" value="${content.id}" />
																		<c:param name="parentModuleId"
																			value="${content.parentModuleId}" />
																	</c:url>
																	<%-- 	<c:url value="downloadFolder" var="downloadFolder">
																		<c:param name="id" value="${content.id}" />
																		<c:param name="filePath" value="${content.filePath}" />
																	</c:url> --%>

																	<c:url value="downloadFolder" var="downloadFolder">
																		<c:param name="id" value="${content.id}" />

																	</c:url>

																	<a href="${navigateInsideFolder}" class="clickedFolder"
																		id="folder${content.id}"><c:out
																			value="${content.contentName}" /></a>

																	<a href="${downloadFolder}" title="download"><i
																		class="fa fa-download fa-lg"></i></a>

																</c:if> <c:if test="${content.contentType == 'File' }">
																	<i class="fa ${content.fontAwesomeClass} fa-lg"
																		style="margin-right: 5px"></i>
																	<c:choose>
																		<c:when
																			test="${fn:containsIgnoreCase(content.filePath, '.pptx') && fn:containsIgnoreCase(content.filePath, '.zip')}">
																			<%-- 		<a
																				href="downloadContentFile?filePath=${content.filePath}&id=${content.id}"
																				class="clickedFile" id="file${content.id}"> <!-- <i
																				class="fa fa-cloud-download"></i> -->${content.contentName}
																			</a> --%>

																			<a href="downloadScormContentFile?id=${content.id}"
																				class="clickedFile" id="file${content.id}"> <!-- <i
																				class="fa fa-cloud-download"></i> -->${content.contentName}
																			</a>
																		</c:when>
																		<c:otherwise>
																			<%-- <a href="downloadFile?filePath=${content.filePath}"
																				class="clickedFile" id="file${content.id}"> <c:out
																					value="${content.contentName}" /> <!-- <i
																				class="fa fa-download" style="margin-left: 5px"></i> -->
																			</a> --%>

																			<a href="downloadContentFile?id=${content.id}"
																				class="clickedFile" id="file${content.id}"> <c:out
																					value="${content.contentName}" /> <!-- <i
																				class="fa fa-download" style="margin-left: 5px"></i> -->
																			</a>
																		</c:otherwise>
																	</c:choose>
																</c:if> <c:if test="${content.contentType == 'Link' }">
																	<i class="fa fa-link fa-lg" style="margin-right: 5px"></i>
																	<a href="${content.linkUrl}" target="_blank"
																		class="clickedLink" id="link${content.id}"> <c:out
																			value="${content.contentName}" />
																	</a>
																</c:if> <c:if test="${content.contentType == 'Multiple_File' }">
																	<a href="downloadContentFile?id=${content.id}"
																		class="clickedFile" id="file${content.id}"> <c:out
																			value="${content.contentName}" /> <!-- <i
																				class="fa fa-download" style="margin-left: 5px"></i> -->
																	</a>

																	<%-- <c:out value="${content.contentName}" /> --%>
																</c:if></td>

															<td><c:out value="${content.contentDescription}" /></td>
															<td><c:out value="${content.createdDate}" /></td>
														</tr>
													</c:forEach>

												</tbody>
											</table>
											<c:if test="${size == 0}">
														No Content under this folder
														</c:if>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>


					<%-- 	<jsp:include page="../common/paginate.jsp">
							<jsp:param name="baseUrl" value="searchFacultyGroups" />
						</jsp:include> --%>

					<!-- /page content: END -->

				</div>

				<!-- SIDEBAR START -->
				<jsp:include page="../common/newSidebar.jsp" />
				<!-- SIDEBAR END -->
				<jsp:include page="../common/footer.jsp" />
				<script type="text/javascript">
					$(".clickedFolder")
							.click(
									function() {

										console
												.log("called ........................................................000000.");

										var contentId = $(this).attr("id");

										var id = contentId.substr(6);
										console.log(id);
										$
												.ajax({
													type : 'GET',
													url : '${pageContext.request.contextPath}/updateCount?'
															+ 'id=' + id,
													success : function(data) {

														$(this)
																.find('span')
																.addClass(
																		"icon-success");

													}

												});

									});
				</script>
				<script type="text/javascript">
					$(".clickedFile")
							.click(
									function() {

										console
												.log("called ........................................................000000.");

										var contentId = $(this).attr("id");

										var id = contentId.substr(4);
										console.log(id);
										$
												.ajax({
													type : 'GET',
													url : '${pageContext.request.contextPath}/updateCount?'
															+ 'id=' + id,
													success : function(data) {

														$(this)
																.find('span')
																.addClass(
																		"icon-success");

													}

												});

									});
				</script>
				<script type="text/javascript">
					$(".clickedLink")
							.click(
									function() {

										console
												.log("called ........................................................000000.");

										var contentId = $(this).attr("id");

										var id = contentId.substr(4);
										console.log(id);
										$
												.ajax({
													type : 'GET',
													url : '${pageContext.request.contextPath}/updateCount?'
															+ 'id=' + id,
													success : function(data) {

														$(this)
																.find('span')
																.addClass(
																		"icon-success");

													}

												});

									});
				</script>


				<script>
					$("#acadYearForSearch")
							.on(
									'change',
									function() {

										console.log('Year Selected');

										var acadYear = $('#acadYearForSearch')
												.val();
										console.log(acadYear);
										if (acadYear) {
											$
													.ajax({
														type : 'POST',
														url : '${pageContext.request.contextPath}/findCoursesByUsernameAndAcadYear?acadYear='
																+ acadYear,
														success : function(data) {
															var json = JSON
																	.parse(data);

															var optionsAsString = "";
															optionsAsString = "<option disabled value selected='selected'>Select Course</option>";
															$(
																	'#contentCourseStudent11')
																	.find(
																			'option')
																	.remove();
															console.log(json);
															for (var i = 0; i < json.length; i++) {
																var idjson = json[i];
																console
																		.log(idjson);

																for ( var key in idjson) {
																	console
																			.log(key
																					+ ""
																					+ idjson[key]);
																	optionsAsString += "<option value='" +key + "'>"
																			+ idjson[key]
																			+ "</option>";

																}
															}
															console
																	.log("optionsAsString"
																			+ optionsAsString);

															$(
																	'#contentCourseStudent11')
																	.append(
																			optionsAsString);
														}
													});
										} else {
											var optionsAsString = "";
											optionsAsString = "<option  value='' selected='selected'>Select Module</option>";
											$('#idOfModule').find('option')
													.remove();
											$('#idOfModule').append(
													optionsAsString);
											console.log("no course");
										}
										s

									});
					$('#idOfModule').trigger('change');
				</script>

				<script>
					$(function() {

						$('#contentCourseStudent11')
								.on(
										'change',
										function() {

											var selected = $(
													'#contentCourseStudent11')
													.val();

											console.log($(
													"#contentCourseStudent11")
													.val());
											var acadYear = $(
													'#acadYearForSearch').val();
											console
													.log('AcadYear---------------'
															+ acadYear);

											window.location = '${pageContext.request.contextPath}/studentContentList?courseId='
													+ encodeURIComponent(selected)
													+ '&acadYear='
													+ encodeURIComponent(acadYear);

											/* 	window.location.hash = '${pageContext.request.contextPath}/studentContentList?courseId='
												+ encodeURIComponent(selected)+'&acadYear='+encodeURIComponent(acadYear);   */

											return false;

										});
					});
				</script>
				<script>
					$(document).ready(function() {
						$('#contentTree').DataTable().destroy()
						$('#contentTree').DataTable({
							"order" : [ [ 2, "desc" ] ]
						});
					});
				</script>