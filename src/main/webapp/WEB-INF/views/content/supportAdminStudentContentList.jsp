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

<sec:authorize access="hasRole('ROLE_FACULTY')">
<jsp:include page="../common/newDashboardHeader.jsp" /> 
	<c:url value="/studentContentList" var="rootFolder">
		<c:param name="courseId" value="${content.courseId}" />
		<c:param name="acadMonth" value="${content.acadMonth}" />
		<c:param name="acadYear" value="${content.acadYear}" />
	</c:url>
<div class="d-flex dataTableBottom" id="facultyAssignmentPage">
<jsp:include page="../common/newLeftNavBarFaculty.jsp"/>
<jsp:include page="../common/newLeftSidebarFaculty.jsp"/>
<jsp:include page="../common/rightSidebarFaculty.jsp"/>

<!-- DASHBOARD BODY STARTS HERE -->

<div class="container-fluid m-0 p-0 dashboardWraper">

	<jsp:include page="../common/newTopHeaderFaculty.jsp" />
     
     <div class="container mt-5">
                <div class="row">
            <!-- SEMESTER CONTENT -->    
                    <div class="col-lg-9 col-md-9 col-sm-12 dashboardBody ml-auto mr-auto">
                        
          <!-- page content: START -->
          
                        <nav aria-label="breadcrumb">
                            <ol class="breadcrumb">
                                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/homepage">Dashboard</a></li>
                                <li class="breadcrumb-item" aria-current="page"><c:out value="${Program_Name}" /></li>
                                <sec:authorize access="hasRole('ROLE_STUDENT')">
								<c:out value="${AcadSession}" />
							</sec:authorize>
                                <li class="breadcrumb-item active" aria-current="page"> Content List </li>
                            </ol>
                        </nav>
                        
						<jsp:include page="../common/alert.jsp" />
						
						
						<!-- Input Form Panel -->
						<div class="card bg-white border">
						<div class="card-body">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">
									<div class="x_title">
										<h5>Content List</h5>
										<div class="col-sm-4 col-md-4 col-xs-12 column">
											<%-- <div class="form-group">
												<a class="btn btn-large btn-primary"
													href="downloadAllContent"
													style="float: right; font-size: 8px;">Download All
													Resources</a>
												<c:if test="${allContent.size() > 0}">

													<c:if test="${content.courseId ne null}">

														<a class="btn btn-large btn-primary"
															href="downloadAllContent?courseId=${content.courseId}"
															style="float: right; font-size: 8px;">Download All
															Resources For Course</a>
													</c:if>
												</c:if>
											</div> --%>
										</div>

									
									</div>
									<div class="x_content">
										<div class="table-responsive">


											<table class="table table-striped table-hover"
												style="font-size: 12px" id="contentTree">
												<thead>
													<tr>
														<th>Content Name</th>
														<th>Description</th>
														<th>Start Date</th>
														<th>End Date</th>
														<th>Access Type</th>
														<th>Created By</th>
														<th>Exam View Type</th>
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
																		<c:param name="acadMonth" value="${content.acadMonth}" />
																		<c:param name="acadYear" value="${content.acadYear}" />
																		<c:param name="folderPath" value="${content.filePath}" />
																		<c:param name="parentContentId" value="${content.id}" />
																	</c:url>
																<%-- 	<c:url value="downloadFolder" var="downloadFolder">
																		<c:param name="id" value="${content.id}" />
																		<c:param name="filePath" value="${content.filePath}" />
																	</c:url> --%>
																	
																	
																	<c:url value="downloadFolder" var="downloadFolder">
																		<c:param name="id" value="${content.id}" />
																		<c:param name="filePath" value="${content.id}" />
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
																			<%-- <a
																				href="downloadContentFile?filePath=${content.filePath}&id=${content.id}"
																				class="clickedFile" id="file${content.id}"> <!-- <i
																				class="fa fa-cloud-download"></i> -->${content.contentName}
																			</a> --%>
																			
																			<a
																				href="downloadContentFile?id=${content.id}&id=${content.id}"
																				class="clickedFile" id="file${content.id}"> <!-- <i
																				class="fa fa-cloud-download"></i> -->${content.contentName}
																			</a>
																		</c:when>
																		<c:otherwise>
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
																</c:if></td>

															<td><c:out value="${content.contentDescription}" /></td>
															<td><c:out value="${content.startDate}" /></td>
															<td><c:out value="${content.endDate}" /></td>
															<td><c:out value="${content.accessType}" /></td>
															<td><c:out value="${content.createdBy}" /></td>
															<td><c:out value="${content.examViewType}" /></td>
														</tr>
													</c:forEach>

												</tbody>
											</table>
											<c:if test="${size == 0}">
														No Content under this folder
														</c:if>
										</div>
										<c:url value="/checkUserCourse" var="goBack">
													<c:param name="username" value="${username}"></c:param>
													</c:url>
													<a href="${goBack}" class="btn btn-success" >Back</a>
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
	<jsp:include page="../common/footer.jsp"/>
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
			</sec:authorize>

<sec:authorize access="hasRole('ROLE_SUPPORT_ADMIN')">
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<html lang="en">
<jsp:include page="../common/css.jsp" />

<body class="nav-md footer_fixed">

	<div class="loader"></div>

	<c:url value="/studentContentList" var="rootFolder">
		<c:param name="courseId" value="${content.courseId}" />
		<c:param name="acadMonth" value="${content.acadMonth}" />
		<c:param name="acadYear" value="${content.acadYear}" />
	</c:url>

	<div class="container body">
		<div class="main_container">

			<jsp:include page="../common/leftSidebar.jsp">
				<jsp:param name="courseId" value="${courseId}" />
			</jsp:include>

			<jsp:include page="../common/topHeaderLibrian.jsp">
				<jsp:param value="ContentMenu" name="activeMenu" />
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
							<i class="fa fa-angle-right"></i> Content List
						</div>
						<jsp:include page="../common/alert.jsp" />


						<!-- Results Panel -->
						<div class="row">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">
									<div class="x_title">
										<h2>Content List</h2>
										<div class="col-sm-4 col-md-4 col-xs-12 column">
											<%-- <div class="form-group">
												<a class="btn btn-large btn-primary"
													href="downloadAllContent"
													style="float: right; font-size: 8px;">Download All
													Resources</a>
												<c:if test="${allContent.size() > 0}">

													<c:if test="${content.courseId ne null}">

														<a class="btn btn-large btn-primary"
															href="downloadAllContent?courseId=${content.courseId}"
															style="float: right; font-size: 8px;">Download All
															Resources For Course</a>
													</c:if>
												</c:if>
											</div> --%>
										</div>

										<ul class="nav navbar-right panel_toolbox">
											<li><a class="collapse-link"><i
													class="fa fa-chevron-up"></i></a></li>
											<li><a class="close-link"><i class="fa fa-close"></i></a></li>
										</ul>
										<div class="clearfix"></div>
									</div>
									<div class="x_content">
										<div class="table-responsive">


											<table class="table table-striped table-hover"
												style="font-size: 12px" id="contentTree">
												<thead>
													<tr>
														<th>Content Name</th>
														<th>Description</th>
														<th>Start Date</th>
														<th>End Date</th>
														<th>Access Type</th>
														<th>Created By</th>
														<th>Exam View Type</th>
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
																		<c:param name="acadMonth" value="${content.acadMonth}" />
																		<c:param name="acadYear" value="${content.acadYear}" />
																		<c:param name="folderPath" value="${content.filePath}" />
																		<c:param name="parentContentId" value="${content.id}" />
																	</c:url>
																	<c:url value="downloadFolder" var="downloadFolder">
																		<c:param name="id" value="${content.id}" />
																		<c:param name="filePath" value="${content.filePath}" />
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
																			<%-- <a
																				href="downloadContentFile?filePath=${content.filePath}&id=${content.id}"
																				class="clickedFile" id="file${content.id}"> <!-- <i
																				class="fa fa-cloud-download"></i> -->${content.contentName}
																			</a> --%>
																			
																			<a
																				href="downloadContentFile?id=${content.id}&id=${content.id}"
																				class="clickedFile" id="file${content.id}"> <!-- <i
																				class="fa fa-cloud-download"></i> -->${content.contentName}
																			</a>
																		</c:when>
																		<c:otherwise>
																			<%-- <a href="downloadFile?filePath=${content.filePath}"
																				class="clickedFile" id="file${content.id}"> <c:out
																					value="${content.contentName}" />
																			</a> --%>
																			
																			<a href="downloadContentFile?id=${content.id}"
																				class="clickedFile" id="file${content.id}"> <c:out
																					value="${content.contentName}" />
																			</a>
																		</c:otherwise>
																	</c:choose>
																</c:if> <c:if test="${content.contentType == 'Link' }">
																	<i class="fa fa-link fa-lg" style="margin-right: 5px"></i>
																	<a href="${content.linkUrl}" target="_blank"
																		class="clickedLink" id="link${content.id}"> <c:out
																			value="${content.contentName}" />
																	</a>
																</c:if></td>

															<td><c:out value="${content.contentDescription}" /></td>
															<td><c:out value="${content.startDate}" /></td>
															<td><c:out value="${content.endDate}" /></td>
															<td><c:out value="${content.accessType}" /></td>
															<td><c:out value="${content.createdBy}" /></td>
															<td><c:out value="${content.examViewType}" /></td>
														</tr>
													</c:forEach>

												</tbody>
											</table>
											<c:if test="${size == 0}">
														No Content under this folder
														</c:if>
										</div>
										<c:url value="/checkUserCourse" var="goBack">
													<c:param name="username" value="${username}"></c:param>
													</c:url>
													<a href="${goBack}" class="btn btn-success" >Back</a>
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


		</div>
	</div>





</body>
</html>

</sec:authorize>