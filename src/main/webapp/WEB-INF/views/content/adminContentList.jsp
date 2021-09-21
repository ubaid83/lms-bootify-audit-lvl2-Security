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

    <div class="d-flex adminPage tabPage dataTableBottom" id="adminPage">
    <jsp:include page="../common/newAdminLeftNavBar.jsp"/>
    <jsp:include page="../common/rightSidebarAdmin.jsp" />

    

    <!-- DASHBOARD BODY STARTS HERE -->

    <div class="container-fluid m-0 p-0 dashboardWraper">

        <jsp:include page="../common/newAdminTopHeader.jsp" />

                <!-- SEMESTER CONTENT -->    
                        <div class="col-lg-12 col-md-12 col-sm-12 dashboardBody p-5">
                        
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
						<%-- <div class="card bg-white border">
						<div class="card-body">

							<div class="form-row">


								<div class="col-lg-5 col-md-5 col-sm-12 mt-3">
									<label>Select Course</label> <select
										class="form-control" id="contentCourseStudent">
										<c:if test="${empty courseId}">
											<option value="" disabled selected>--SELECT COURSE--</option>
										</c:if>
										<c:forEach var='cList' items='${courseList}'>
											<c:if test="${courseId eq cList.id }">
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
					</div> --%>

						<!-- Results Panel -->

						
								<div class="card bg-white border">
								<div class="card-body">								
									<div class="col-xs-12 col-sm-12">
								<div class="x_panel">
									<div class="x_title">
										<h5>Content List</h5>
										<div class="row">
											<div class="col-12 text-right">
													<a class="btn btn-dark mb-2" href="downloadAllContent">Download All
														Resources</a>
																			
													<c:if test="${allContent.size() > 0}">
														<c:if test="${content.courseId ne null}">
															<a class="btn btn-dark mb-2"
																href="downloadAllContent?courseId=${content.courseId}">Download All
																Resources For Course</a>
														</c:if>
													</c:if>
											</div>
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
																			<a
																				href="downloadContentFile?filePath=${content.filePath}&id=${content.id}"
																				class="clickedFile" id="file${content.id}"> <!-- <i
																				class="fa fa-cloud-download"></i> -->${content.contentName}
																			</a>
																		</c:when>
																		<c:otherwise>
																			<a href="downloadFile?filePath=${content.filePath}"
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
														</tr>
													</c:forEach>

												</tbody>
											</table>
											<div class="col-12">
											<button class="btn btn-danger"><a href="${pageContext.request.contextPath}/searchAssignmentTestForm">Back</a></button>
											
											</div>
										</div>
									</div>
								</div>
							</div>
								</div>
								</div>
			<!-- /page content: END -->
                                  




                        </div>

                <!-- SIDEBAR START -->

                <!-- SIDEBAR END -->
        <jsp:include page="../common/newAdminFooter.jsp"/>


                   
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