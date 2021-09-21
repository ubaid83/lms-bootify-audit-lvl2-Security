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
<form:form>
				<fieldset style="min-width: 50%;">

					<c:url value="/addContentForm" var="addFolder">
						<c:param name="courseId" value="${content.courseId}" />
						<c:param name="acadMonth" value="${content.acadMonth}" />
						<c:param name="acadYear" value="${content.acadYear}" />
						<c:param name="folderPath" value="${content.folderPath}" />
						<c:param name="contentType" value="Folder" />
						<c:param name="parentContentId" value="${content.parentContentId}" />
						<c:param name="accessType" value="${content.accessType}" />
					</c:url>

					<c:url value="/addContentForm" var="addFile">
						<c:param name="courseId" value="${content.courseId}" />
						<c:param name="acadMonth" value="${content.acadMonth}" />
						<c:param name="acadYear" value="${content.acadYear}" />
						<c:param name="folderPath" value="${content.folderPath}" />
						<c:param name="contentType" value="File" />
						<c:param name="parentContentId" value="${content.parentContentId}" />
						<c:param name="accessType" value="${content.accessType}" />
					</c:url>

					<c:url value="/addContentForm" var="addLink">
						<c:param name="courseId" value="${content.courseId}" />
						<c:param name="acadMonth" value="${content.acadMonth}" />
						<c:param name="acadYear" value="${content.acadYear}" />
						<c:param name="folderPath" value="${content.folderPath}" />
						<c:param name="contentType" value="Link" />
						<c:param name="parentContentId" value="${content.parentContentId}" />
						<c:param name="accessType" value="${content.accessType}" />
					</c:url>

					<c:url value="/getContentUnderAPath" var="rootFolder">
						<c:param name="courseId" value="${content.courseId}" />
						<c:param name="acadMonth" value="${content.acadMonth}" />
						<c:param name="acadYear" value="${content.acadYear}" />
					</c:url>

				</fieldset>
			</form:form>
<jsp:include page="../common/newDashboardHeader.jsp" /> 

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
                                <li class="breadcrumb-item active" aria-current="page"> Learning Resources </li>
                            </ol>
                        </nav>
                        
						<jsp:include page="../common/alert.jsp" />
						
						
						<!-- Input Form Panel -->
						<div class="card bg-white border">
						<div class="card-body">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">

									<div class="x_title">
										<h2>Learning Resources</h2>
										<ul class="nav navbar-right panel_toolbox">
											<li><a class="collapse-link"><i
													class="fa fa-chevron-up"></i></a></li>
										</ul>
										<div class="clearfix"></div>
									</div>

									<div class="x_content">
										<form:form action="getContentUnderAPath" method="post"
											modelAttribute="content">
											<fieldset>

												<div class="row">
													<%-- <div class="col-sm-4 column">
														<div class="form-group">
															<form:label path="acadMonth" for="acadMonth">Academic Month</form:label>
															<form:select id="acadMonth" path="acadMonth"
																class="form-control" required="required">
																<form:option value="">Select Academic Month</form:option>
																<form:options items="${acadMonths}" />
															</form:select>
														</div>
													</div>
													<div class="col-sm-4 column">
														<div class="form-group">
															<form:label path="acadYear" for="acadYear">Academic Year</form:label>
															<form:select id="acadYear" path="acadYear"
																class="form-control" required="required">
																<form:option value="">Select Academic Year</form:option>
																<form:options items="${acadYears}" />
															</form:select>
														</div>
													</div> --%>

													<div class="col-sm-4 column">
														<div class="form-group">
															<form:label path="courseId" for="courseId">Course</form:label>
															<form:select id="courseId" path="courseId"
																class="form-control">
																<form:option value="">Select Course</form:option>
																<c:forEach var="course" items="${allCourses}"
																	varStatus="status">
																	<form:option value="${course.id}">${course.courseName}</form:option>
																</c:forEach>
															</form:select>
														</div>
													</div>

													<div class="col-sm-12 column">
														<div class="form-group">
															<button id="submit" name="submit"
																class="btn btn-large btn-primary">Search
																Content</button>
															<button id="cancel" name="cancel" class="btn btn-danger"
																formnovalidate="formnovalidate">Cancel</button>
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

						<!-- Results Panel -->

						
								<div class="card bg-white border">
								<div class="card-body">								
									<div class="col-xs-12 col-sm-12">
								<div class="x_panel">
									<div class="x_title">
										<h2>Learning Resources</h2>
										<ul class="nav navbar-right panel_toolbox">
											<li><a class="collapse-link"><i
													class="fa fa-chevron-up"></i></a></li>
											<li><a class="close-link"><i class="fa fa-close"></i></a></li>
										</ul>
										<div class="clearfix"></div>
									</div>

									<div class="x_content">

										<div class="row">
											<div class="col-md-4 col-xs-12">
												<div class="font_weight_bold blue_link">
													<a href="javascript:void(0);"
														onclick="jQuery('.treetable').treetable('expandAll'); return false;"><i
														class="fa fa-plus-square-o"></i> Expand All</a> &nbsp; |
													&nbsp; <a href="javascript:void(0);"
														onclick="jQuery('.treetable').treetable('collapseAll'); return false;"><i
														class="fa fa-minus-square-o"></i> Collapse All</a>
												</div>
											</div>
											<div class="col-md-8 col-xs-12 text-right">
												<a id="submit" class="btn  btn-primary "
													href="${rootFolder}">Back to Course Root Folder</a> <a
													id="submit" class="btn  btn-primary " href="${addFolder}"><i
													class="fa fa-folder-o"></i> Add New Folder</a> <a id="submit"
													class="btn  btn-primary " href="${addFile}"><i
													class="fa fa-file-o"></i> Add New File</a> <a id="submit"
													class="btn  btn-primary " href="${addLink}"><i
													class="fa fa-link""true"></i> Add New Link</a> <a id="submit"
													class="btn  btn-primary " href="${addLink}"><i
													class="fa fa-link"></i> Add New Link</a> <a
													class="btn btn-large btn-primary" href="downloadAllContent">Download
													All Resources</a>
												<c:if test="${allContent.size() > 0}">

													<c:if test="${content.courseId ne null}">
														<a class="btn btn-large btn-primary"
															href="downloadAllContent?courseId=${content.courseId}">Download
															All Resources For Course</a>
													</c:if>
												</c:if>
											</div>
										</div>



										<div class="table-responsive">

											<table class="table table-striped table-hover"
												style="font-size: 12px" id="contentTree">
												<thead>
													<tr>
														<!-- <th>Sr. No.</th>
																<th>Content Type</th> -->
														<th>Content Name</th>
														<th>Description</th>
														<th>Visible From</th>
														<th>Visible Till</th>
														<th>Actions</th>
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

																	<c:url value="/getContentUnderAPath"
																		var="navigateInsideFolder">
																		<c:param name="courseId" value="${content.courseId}" />
																		<c:param name="folderPath" value="${content.filePath}" />
																		<c:param name="parentContentId" value="${content.id}" />
																	</c:url>
																	<a href="${navigateInsideFolder}"><c:out
																			value="${content.contentName}" /> <c:if
																			test="${content.accessType == 'Public' || content.accessType == 'Shared'}">
																			<i class="fa fa-globe fa-lg"></i>
																		</c:if> <c:if
																			test="${content.accessType != 'Public' && content.accessType != 'Shared'}">
																			<i class="fa fa-lock fa-lg"></i>
																		</c:if> </a>


																	<c:url value="/addContentForm" var="addFolder">
																		<c:param name="courseId" value="${content.courseId}" />
																		<c:param name="folderPath" value="${content.filePath}" />
																		<c:param name="contentType" value="Folder" />
																		<c:param name="parentContentId" value="${content.id}" />
																		<c:param name="accessType"
																			value="${content.accessType}" />
																	</c:url>

																	<c:url value="/addContentForm" var="addFile">
																		<c:param name="courseId" value="${content.courseId}" />
																		<c:param name="folderPath" value="${content.filePath}" />
																		<c:param name="contentType" value="File" />
																		<c:param name="parentContentId" value="${content.id}" />
																		<c:param name="accessType"
																			value="${content.accessType}" />
																	</c:url>

																	<c:url value="/addContentForm" var="addLink">
																		<c:param name="courseId" value="${content.courseId}" />
																		<c:param name="folderPath" value="${content.filePath}" />
																		<c:param name="contentType" value="Link" />
																		<c:param name="parentContentId" value="${content.id}" />
																		<c:param name="accessType"
																			value="${content.accessType}" />
																	</c:url>


																	<!-- Arrow icon for Menu -->
																	<div class="btn-group">
																		<button type="button"
																			class="btn btn-xs dropdown-toggle"
																			data-toggle="dropdown"
																			style="margin-left: 5px; padding: 0px 3px; line-height: 1">
																			<span class="caret" style="line-height: 1"></span>
																		</button>
																		<ul class="dropdown-menu" role="menu">
																			<li><a href="${addFolder}">Add Subfolder</a></li>
																			<li><a href="${addFile}">Add File</a></li>
																			<li><a href="${addLink}">Add Link</a></li>
																		</ul>
																	</div>
																</c:if> <c:if test="${content.contentType == 'File' }">
																	<i class="fa ${content.fontAwesomeClass} fa-lg"
																		style="margin-right: 5px"></i>
																	<c:choose>
																		<c:when
																			test="${fn:containsIgnoreCase(content.filePath, '.pptx') && fn:containsIgnoreCase(content.filePath, '.zip')}">
																			<a
																				href="downloadContentFile?filePath=${content.filePath}&id=${content.id}"
																				class="clickedFile" id="file${content.id}"> <c:out value="${content.contentName}"></c:out>
																			</a>
																		</c:when>
																		<c:otherwise>
																			<a href="downloadFile?filePath=${content.filePath}"
																				class="clickedFile" id="file${content.id}">
																				<c:out value="${content.contentName}"></c:out> </a>
																		</c:otherwise>
																	</c:choose>
																</c:if> <c:if test="${content.contentType == 'Link' }">
																	<i class="fa fa-link fa-lg" style="margin-right: 5px"></i>
																	<a href="${content.linkUrl}" target="_blank"> <c:out
																			value="${content.contentName}" />
																	</a>
																</c:if></td>

															<td><c:out value="${content.contentDescription}" /></td>
															<td><c:out value="${content.startDate}" /></td>
															<td><c:out value="${content.endDate}" /></td>
															<td><c:url value="/addContentForm" var="editurl">
																	<c:param name="id" value="${content.id}" />
																	<c:param name="courseId" value="${content.courseId}" />
																	<c:param name="contentType"
																		value="${content.contentType}" />
																</c:url> <c:url value="deleteContent" var="deleteurl">
																	<c:param name="courseId" value="${content.courseId}" />
																	<c:param name="id" value="${content.id}" />
																	<c:param name="contentType"
																		value="${content.contentType}" />
																</c:url> <c:url value="viewContent" var="detailsUrl">
																	<c:param name="id" value="${content.id}" />
																	<c:param name="contentType"
																		value="${content.contentType}" />
																</c:url> <c:url value="downloadFolder" var="downloadFolder">
																	<c:param name="id" value="${content.id}" />
																	<c:param name="filePath" value="${content.filePath}" />
																</c:url> <c:if test="${content.accessType == 'Public' }">
																	<a href="${detailsUrl}" title="Share"><i
																		class="fa fa-share-alt fa-lg"></i></a>&nbsp;
																		</c:if> <a href="${editurl}" title="Edit"><i
																	class="fa fa-pencil-square-o fa-lg"></i></a>&nbsp; <a
																href="${deleteurl}" title="Delete"
																<c:if test="${content.contentType == 'Folder' }">
																					onclick="return confirm('Are you sure you want to delete this Folder and all of its Content ?')"
																				</c:if>
																<c:if test="${content.contentType == 'File' }">
																					onclick="return confirm('Are you sure you want to delete this File ?')"
																				</c:if>
																<c:if test="${content.contentType == 'Folder' }">
																					onclick="return confirm('Are you sure you want to delete this Link ?')"
																				</c:if>>
																	<i class="fa fa-trash-o fa-lg"></i>
															</a>
															<c:if test="${content.contentType eq 'Folder' }">
																	<a href="${downloadFolder}" title="download"><i
																		class="fa fa-download fa-lg"></i></a>
																</c:if></td>
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
	<jsp:include page="../common/footer.jsp"/>
