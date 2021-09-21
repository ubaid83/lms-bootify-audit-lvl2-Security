<%@page import="com.spts.lms.web.utils.Utils"%>



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
	<form:form>
		<fieldset style="min-width: 50%;">

			<c:url value="/addContentForm" var="addFolder">
				<c:param name="courseId" value="${content.courseId}" />
				<c:param name="moduleId" value="${content.moduleId}" />
				<c:param name="acadMonth" value="${content.acadMonth}" />
				<c:param name="acadYear" value="${content.acadYear}" />
				<c:param name="folderPath" value="${content.folderPath}" />
				<c:param name="contentType" value="Folder" />
				<c:param name="parentContentId" value="${content.parentContentId}" />
				<c:param name="accessType" value="${content.accessType}" />
			</c:url>

			<c:url value="/addContentForm" var="addFile">
				<c:param name="courseId" value="${content.courseId}" />
				<c:param name="moduleId" value="${content.moduleId}" />
				<c:param name="acadMonth" value="${content.acadMonth}" />
				<c:param name="acadYear" value="${content.acadYear}" />
				<c:param name="folderPath" value="${content.folderPath}" />
				<c:param name="contentType" value="File" />
				<c:param name="parentContentId" value="${content.parentContentId}" />
				<c:param name="accessType" value="${content.accessType}" />
			</c:url>
			<c:url value="/addContentForm" var="addMultipleFile">
				<c:param name="courseId" value="${content.courseId}" />
				<c:param name="acadMonth" value="${content.acadMonth}" />
				<c:param name="acadYear" value="${content.acadYear}" />
				<c:param name="folderPath" value="${content.folderPath}" />
				<c:param name="contentType" value="Multiple_File" />
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

			<c:url value="/getContentUnderAPathForFacultyForModule"
				var="rootFolder">
		    <%--
		    	<c:param name="courseId" value="${content.courseId}" />
				<c:param name="moduleId" value="${content.moduleId}" />
				<c:param name="acadMonth" value="${content.acadMonth}" />
				<c:param name="acadYear" value="${content.acadYear}" />
			--%>
			</c:url>

		</fieldset>
	</form:form>
	<div class="container-fluid m-0 p-0 dashboardWraper">

		<jsp:include page="../common/newTopHeaderFaculty.jsp" />

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
								Learning Resources</li>
						</ol>
					</nav>

					<jsp:include page="../common/alert.jsp" />


					<!-- Input Form Panel -->
					<div class="card bg-white border">
						<div class="card-body">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">

									<div class="x_title">
										<h5>Learning Resources</h5>

									</div>

									<div class="x_content">
										<form:form action="getContentUnderAPathForFacultyForModule"
											method="post" modelAttribute="content">
											
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

													<div class="col-md-3 col-sm-9">
														<div class="form-group">

															<form:label path="acadYear" for="acadYear">
																<strong>Academic year <span style="color: red">*</span></strong>
															</form:label>
															<form:select id="acadYear" path="acadYear"
																class="form-control" required="required">
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
																<%-- 
														<c:forEach var="acadYear" items="${acadYear}"
															varStatus="status">
															<option value="${acadYear}">${acadYear}</option>
														</c:forEach> --%>
															</form:select>
														</div>
													</div>
													<div class="col-md-3 col-sm-9">
														<div class="form-group">
															<form:label path="idForModule" for="idForModule">
																<strong>Module</strong>
															</form:label>
															<form:select id="idOfModule" path="idForModule"
																class="form-control">
																<c:if test="${content.moduleId eq null or empty content.moduleId}">
																	<form:option value="">Select Module</form:option>
																</c:if>
																<c:forEach var="module" items="${modules}"
																	varStatus="status">
																	<c:if test="${module.moduleId ne null}">
																		<c:if test="${module.moduleId eq content.moduleId}">
																			<option value="${module.moduleId}" selected>${module.moduleName}</option>
																		</c:if>
																		<c:if test="${module.moduleId ne content.moduleId}">
																			<option value="${module.moduleId}">${module.moduleName}</option>
																		</c:if>
																	</c:if>
																	<c:if test="${module.moduleId eq null}">
																		<form:option value="${module.moduleId}">${module.moduleName}</form:option>
																	</c:if>
																</c:forEach>
															</form:select>

														</div>
														<%-- <div class="form-group">
															<form:label path="idForModule" for="idForModule">
																<strong>Module <span style="color: red">*</span></strong>
															</form:label>
															<form:select id="idOfModule" path="idForModule"
																class="form-control" required="required">
																<form:option value="">Select Module</form:option>
				
															</form:select>

														</div> --%>
													</div>

													<div class="col-sm-4 column">
														<div class="form-group">
															<form:label path="courseIdForSearch"
																for="courseIdForSearch">Course</form:label>
															<form:select id="courseId" path="courseIdForSearch"
																class="form-control">
																<c:if test="${content.courseId eq null}">
																	<%-- <form:option value="">Select Course</form:option> --%>
																	<option selected disabled="disabled">Select Course</option>
																</c:if>
																<c:forEach var="course" items="${allCourses}"
																	varStatus="status">
																	<c:if test="${course.id ne null}">
																		<c:if test="${course.id eq content.courseId}">
																			<option value="${course.id}" selected>${course.courseName}</option>
																		</c:if>
																		<c:if test="${course.id ne content.courseId}">
																			<option value="${course.id}">${course.courseName}</option>
																		</c:if>
																	</c:if>
																	<c:if test="${course.id eq null}">
																		<form:option value="${course.id}">${course.courseName}</form:option>
																	</c:if>
																</c:forEach>
																<%-- <c:forEach var="course" items="${allCourses}"
																	varStatus="status">
																	<form:option value="${course.id}">${course.courseName}</form:option>
																</c:forEach> --%>
															</form:select>
														</div>
													</div>
													<div class="col-sm-12 column">
														<div class="form-group">
															<button id="submit" name="submit"
																class="btn btn-large btn-primary">Search
																Content</button>
															<!-- <button id="cancel" name="cancel" class="btn btn-danger"
																formnovalidate="formnovalidate">Cancel</button> -->
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
										<h5>Learning Resources</h5>

									</div>

									<div class="x_content">

										<%-- 										<div class="row">

											<div class="col-12">
												<a id="submit" class="btn btn-dark mb-3"
													href="${rootFolder}">Back to Course Root Folder</a> 
												
												<a id="submit" class="btn btn-secondary mb-3"
													href="${addFolder}" onclick="return myFunction()"> <i
													class="fa fa-folder-o"></i> Add New Folder
												</a>
												
												<a id="submit" class="btn btn-secondary mb-3" 
													href="${addFile}"><i class="fa fa-file-o"></i> Add New
													File</a> <a id="submit" class="btn btn-secondary mb-3"
													href="${addMultipleFile}"><i class="fa fa-file-o"></i>
													Add Multiple Files</a> <a id="submit"
													class="btn btn-secondary mb-3" href="${addLink}"><i
													class="fa fa-link""true"></i> Add New Link</a> <a
													class="btn btn-large btn-secondary mb-3"
													href="downloadAllContent">Download All Resources</a>
												<c:if test="${allContent.size() > 0}">

													<c:if test="${content.courseId ne null}">
														<a class="btn btn-large btn-primary mb-3"
															href="downloadAllContent?courseId=${content.courseId}">Download
															All Resources For Course</a>
													</c:if>
												</c:if>

											</div>
										</div>
 --%>
										<%-- <c:if test="${content.acadYear eq currentYear or empty content.acadYear}"> --%>
										<c:if test="${  fn:contains( currentYear, content.acadYear ) or  empty content.acadYear}">
										<div class="row">
											<div class="col-12">
												<%-- <a id="submit" class="btn btn-dark mb-3"
													href="${rootFolder}">Back to Course Root Folder</a> --%>
													<a id="submit" class="btn btn-dark mb-3"
													href="${rootFolder}">Back to Root Folder</a>
													
												<c:choose>
													<c:when
														test="${content.moduleId eq null && content.courseId eq null}">
														<a id="submit" class="btn btn-secondary mb-3"
															href="${addFolder}" onclick="return myFunction()"
															data-toggle="modal" data-target="#exampleModalCenter">
															<i class="fa fa-folder-o"></i> Add New Folder
														</a>
														<a id="submit" class="btn btn-secondary mb-3"
															href="${addFile}" onclick="return myFunction()"
															data-toggle="modal" data-target="#exampleModalCenterForFile"><i
															class="fa fa-file-o"></i> Add New File</a>
														<a id="submit" class="btn btn-secondary mb-3"
															href="${addMultipleFile}" onclick="return myFunction()"
															data-toggle="modal" data-target="#exampleModalCenterForMultipleFile"><i
															class="fa fa-file-o"></i> Add Multiple Files</a>
														<a id="submit" class="btn btn-secondary mb-3"
															href="${addLink}" data-toggle="modal"
															data-target="#exampleModalCenterForAddNewLink"><i
															class="fa fa-link" "true" onclick="return myFunction()"></i>
															Add New Link</a>
														<a class="btn btn-large btn-secondary mb-3"
															onclick="return myFunction()" href="downloadAllContent"
															>Download
															All Resources</a>
													</c:when>
													<c:otherwise>
													<c:if test="${content.createdBy eq userBean.username }">
														<a id="submit" class="btn btn-secondary mb-3"
															href="${addFolder}" onclick="return myFunction()"> <i
															class="fa fa-folder-o"></i> Add New Folder
														</a>
														<a id="submit" class="btn btn-secondary mb-3"
															href="${addFile}" onclick="return myFunction()"><i
															class="fa fa-file-o"></i> Add New File</a>
														<a id="submit" class="btn btn-secondary mb-3"
															href="${addMultipleFile}" onclick="return myFunction()"><i
															class="fa fa-file-o"></i> Add Multiple Files</a>
														<a id="submit" class="btn btn-secondary mb-3"
															href="${addLink}"><i class="fa fa-link"
															"true" onclick="return myFunction()"></i> Add New Link</a>
														</c:if>
														<a class="btn btn-large btn-secondary mb-3"
															onclick="return myFunction()" href="downloadAllContent">Download
															All Resources</a>
													</c:otherwise>
												</c:choose>
										
												<c:if test="${allContent.size() > 0}">

													<c:if test="${content.courseId ne null}">

														<a class="btn btn-large btn-primary mb-3"
															href="downloadAllContent?courseId=${content.courseId}">Download

															All Resources For Course</a>

													</c:if>

												</c:if>



											</div>

										</div>
										</c:if>


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
														<th>View Count</th>
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

																	<c:url value="/getContentUnderAPathForFacultyForModule"
																		var="navigateInsideFolder">
																		<c:param name="courseId" value="${content.courseId}" />
																		<c:param name="moduleId" value="${content.moduleId}" />
																		<c:param name="acadMonth" value="${content.acadMonth}" />
																		<c:param name="acadYear" value="${content.acadYear}" />
																		<c:param name="folderPath" value="${content.filePath}" />
																		<c:param name="accessType" value="${content.accessType}" />
																		<c:param name="parentContentId" value="${content.id}" />
																	</c:url>
																	<a href="${navigateInsideFolder}"><c:out
																			value="${content.contentName}" /> <c:if
																			test="${content.accessType == 'Public' || content.accessType == 'Shared'}">
																			<i class="fa fa-users fa-lg"></i>
																		</c:if> <c:if
																			test="${content.accessType == 'Private' && content.accessType != 'Shared'}">
																			<i class="fa fa-lock fa-lg"></i>
																		</c:if>
																		<c:if
																			test="${content.accessType == 'Everyone' || content.accessType == 'Shared'}">
																			<i class="fa fa-globe fa-lg"></i>
																		</c:if> </a>


																	<c:url value="/addContentForm" var="addFolder">
																		<c:param name="courseId" value="${content.courseId}" />
																		<c:param name="moduleId" value="${content.moduleId}" />
																		<c:param name="acadMonth" value="${content.acadMonth}" />
																		<c:param name="acadYear" value="${content.acadYear}" />
																		<c:param name="folderPath" value="${content.filePath}" />
																		<c:param name="contentType" value="Folder" />
																		<c:param name="parentContentId" value="${content.id}" />
																		<c:param name="accessType"
																			value="${content.accessType}" />
																	</c:url>

																	<c:url value="/addContentForm" var="addFile">
																		<c:param name="courseId" value="${content.courseId}" />
																		<c:param name="moduleId" value="${content.moduleId}" />
																		<c:param name="acadMonth" value="${content.acadMonth}" />
																		<c:param name="acadYear" value="${content.acadYear}" />
																		<c:param name="folderPath" value="${content.filePath}" />
																		<c:param name="contentType" value="File" />
																		<c:param name="parentContentId" value="${content.id}" />
																		<c:param name="accessType"
																			value="${content.accessType}" />
																	</c:url>
																	<c:url value="/addContentForm" var="addMultipleFile">
																		<c:param name="courseId" value="${content.courseId}" />
																		<c:param name="acadMonth" value="${content.acadMonth}" />
																		<c:param name="acadYear" value="${content.acadYear}" />
																		<c:param name="folderPath" value="${content.filePath}" />
																		<c:param name="contentType" value="Multiple_File" />
																		<c:param name="parentContentId" value="${content.id}" />
																		<c:param name="accessType"
																			value="${content.accessType}" />
																	</c:url>
																	<c:url value="/addContentForm" var="addLink">
																		<c:param name="courseId" value="${content.courseId}" />
																		<c:param name="acadMonth" value="${content.acadMonth}" />
																		<c:param name="acadYear" value="${content.acadYear}" />
																		<c:param name="folderPath" value="${content.filePath}" />
																		<c:param name="contentType" value="Link" />
																		<c:param name="parentContentId" value="${content.id}" />
																		<c:param name="accessType"
																			value="${content.accessType}" />
																	</c:url>


																	<!-- Arrow icon for Menu -->
																	<c:if test="${content.createdBy eq userBean.username}">
																	<c:if test="${  fn:contains( currentYear, content.acadYear ) or  empty content.acadYear}">
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
																			<li><a href="${addMultipleFile}">Add
																					Multiple Files</a></li>
																			<li><a href="${addLink}">Add Link</a></li>
																		</ul>
																	</div>
																	</c:if>
																	</c:if>
																</c:if> <c:if test="${content.contentType == 'File' }">
																	<i class="fa ${content.fontAwesomeClass} fa-lg"
																		style="margin-right: 5px"></i>
																<%-- 	<c:choose>
																		<c:when
																			test="${fn:containsIgnoreCase(content.filePath, '.pptx') && fn:containsIgnoreCase(content.filePath, '.zip')}">
																			<a
																				href="downloadScormContentFile?id=${content.id}"
																				class="clickedFile" id="file${content.id}"> <c:out
																					value="${content.contentName}"></c:out>
																			</a>
																		</c:when>
																		<c:otherwise>
																			<a href="downloadContentFile?id=${content.id}"
																				class="clickedFile" id="file${content.id}"> <c:out
																					value="${content.contentName}"></c:out>
																			</a>
																		</c:otherwise>
																	</c:choose> --%>
																		<c:choose>
																		<c:when
																			test="${fn:containsIgnoreCase(content.filePath, '.pptx') && fn:containsIgnoreCase(content.filePath, '.zip')}">
																			<a
																				href="downloadScormContentFile?id=${content.id}"
																				class="clickedFile" id="file${content.id}"> <c:out
																					value="${content.contentName}"></c:out>
																			</a>
																		</c:when>
																		<c:otherwise>
																			<a href="downloadContentFile?id=${content.id}"
																				class="clickedFile" id="file${content.id}"> <c:out
																					value="${content.contentName}"></c:out>
																			</a>
																		</c:otherwise>
																	</c:choose>
																</c:if> <c:if test="${content.contentType == 'Link' }">
																	<i class="fa fa-link fa-lg" style="margin-right: 5px"></i>
																	<a href="${content.linkUrl}" target="_blank"> <c:out
																			value="${content.contentName}" />
																	</a>
																</c:if> <%-- <c:if test="${content.contentType == 'Multiple_File' }">
																	<c:out value="${content.contentName}" />
																</c:if> --%>
																<c:if test="${content.contentType == 'Multiple_File' }">
																
											<%-- 					<a href="downloadContentFile?filePath=${content.filePath}"
																				class="clickedFile" id="file${content.id}"> <c:out
																					value="${content.contentName}" /> 
																			</a> --%>
																
																	<a href="downloadContentFile?id=${content.id}"
																				class="clickedFile" id="file${content.id}"> <c:out
																					value="${content.contentName}" /> <!-- <i
																				class="fa fa-download" style="margin-left: 5px"></i> -->
																			</a>
																	<%-- <c:out value="${content.contentName}" /> --%>
																</c:if>
																</td>

															<td><c:out value="${content.contentDescription}" /></td>
															<td><c:out value="${content.startDate}" /></td>
															<td><c:out value="${content.endDate}" /></td>
															<td><c:out value="${content.count}"></c:out></td>
															<td><c:url value="/addContentForm" var="editurl">
																	<c:param name="id" value="${content.id}" />
																	<c:param name="courseId" value="${content.courseId}" />
																	<c:param name="moduleId" value="${content.moduleId}" />
																	<c:param name="contentType"
																		value="${content.contentType}" />
																</c:url> <c:url value="deleteContent" var="deleteurl">
																	<c:param name="courseId" value="${content.courseId}" />
																	<c:param name="id" value="${content.id}" />
																	<c:param name="contentType"
																		value="${content.contentType}" />
																</c:url>  
																<c:if test="${content.moduleId eq null }">
																<c:url value="viewContent" var="detailsUrl">
																	<c:param name="id" value="${content.id}" />
																	
																	<c:param name="contentType"
																		value="${content.contentType}" />
																</c:url> 
																</c:if>
																
																<!-- viewContentForModule -->
																<c:if test="${content.moduleId ne null }">
																 <c:url value="viewContentForModule" var="detailsUrl">
																	<c:param name="id" value="${content.id}" />
																	<c:param name="moduleId" value="${content.moduleId}" />
																	<c:param name="acadYear" value="${content.acadYear}" />
																	<c:param name="contentType"
																		value="${content.contentType}" />
																</c:url> 
																</c:if>
																 
																 <%-- <c:url value="downloadFolder" var="downloadFolder">
																	<c:param name="id" value="${content.id}" />
																	<c:param name="filePath" value="${content.filePath}" />
																</c:url> --%> 
																<c:url value="downloadFolder" var="downloadFolder">
																	<c:param name="id" value="${content.id}" />
																	<%--  <c:param name="filePath" value="${content.id}" />  --%>
																</c:url>
																
																<c:url value="exportContentForm" var="expotrUrl">
																	<c:param name="id" value="${content.id}" />
																	<c:param name="contentType" value="${content.contentType}" />
																</c:url>
																<c:if test="${content.createdBy eq userBean.username}">
																
																 <c:if test="${content.accessType eq 'Public' }">
																	<a href="${detailsUrl}" title="Share"><i
																		class="fa fa-share-alt fa-lg"></i></a>&nbsp;
																		 </c:if><a href="${expotrUrl}" title="Export"><i
																	class="fas fa-file-excel" aria-hidden="true"></i></a>&nbsp;
																<a href="${editurl}" title="Edit"><i
																	class="fas fa-pen-square"></i></a>&nbsp; <a
																href="${deleteurl}" title="Delete" 
																<c:if test="${content.contentType eq 'Folder' }">
																					onclick="return confirm('Are you sure you want to delete this Folder and all of its Content ?')"
																				</c:if>
																<c:if test="${content.contentType eq 'File' }">
																					onclick="return confirm('Are you sure you want to delete this File ?')"
																				</c:if>
																<c:if test="${content.contentType eq 'Link' }">
																					onclick="return confirm('Are you sure you want to delete this Link ?')"
																				</c:if>
																<c:if test="${content.contentType eq 'Multiple_File' }">
																					onclick="return confirm('Are you sure you want to delete this File ?')"
																				</c:if>>				
																	<i class="fas fa-trash"></i>
															</a> 
																</c:if>
																<c:if test="${content.contentType eq 'Folder' }">
																	<a href="${downloadFolder}" title="download"><i
																		class="fa fa-download fa-lg"></i></a>
																</c:if>
																</td>
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


					<!-- Button trigger modal -->





					<!-- Modal Start -->
<!-- Modal For  Folder-->
					<div class="modal fade " id="exampleModalCenter" tabindex="-1"
						role="dialog" aria-labelledby="exampleModalCenterTitle"
						aria-hidden="true">
						<div class="modal-dialog modal-dialog-centered" role="document">
							<div class="modal-content rounded-0">
								<div class="modal-header">
									<h5 class="modal-title" id="exampleModalCenterTitle">Add
										New Folder For</h5>
									<button type="button" class="close" data-dismiss="modal"
										aria-label="Close">
										<span aria-hidden="true">&times;</span>
									</button>
								</div>
								<div class="modal-body ">
									<form:form method="POST"
										action="${pageContext.request.contextPath}/addContentForm"
										modelAttribute="content">
										<form:input path="contentType" value="Folder" type="hidden"/>
										<p>
											<form:radiobutton path="contentFor" value="module"
												checked="checked" />
											<form:label path="contentFor">Module</form:label>
										</p>
										<p>
											<form:radiobutton path="contentFor" value="course" />
											<form:label path="contentFor">Course</form:label>
										</p>
										<%-- <form:button>sahdaud</form:button> --%>
										<div class="modal-footer">
											<button type="button" class="btn btn-secondary rounded-0"
												data-dismiss="modal">Close</button>
											<!-- <button type="button" class="btn btn-primary">Save
                                                            changes</button> -->
											<form:button class="btn btn-primary rounded-0" type="submit">Proceed</form:button>
										</div>
									</form:form>

								</div>
							</div>
						</div>
					</div>
					
					
					
					<!-- Modal For  File-->
					<div class="modal fade " id="exampleModalCenterForFile" tabindex="-1"
						role="dialog" aria-labelledby="exampleModalCenterTitle"
						aria-hidden="true">
						<div class="modal-dialog modal-dialog-centered" role="document">
							<div class="modal-content rounded-0">
								<div class="modal-header">
									<h5 class="modal-title" id="exampleModalCenterTitle">Add
										New File For</h5>
									<button type="button" class="close" data-dismiss="modal"
										aria-label="Close">
										<span aria-hidden="true">&times;</span>
									</button>
								</div>
								<div class="modal-body ">
									<form:form method="POST"
										action="${pageContext.request.contextPath}/addContentForm"
										modelAttribute="content">
										<form:input path="contentType" value="File" type="hidden"/>
										<p>
											<form:radiobutton path="contentFor" value="module"
												checked="checked" />
											<form:label path="contentFor">Module</form:label>
										</p>
										<p>
											<form:radiobutton path="contentFor" value="course" />
											<form:label path="contentFor">Course</form:label>
										</p>
										<%-- <form:button>sahdaud</form:button> --%>
										<div class="modal-footer">
											<button type="button" class="btn btn-secondary rounded-0"
												data-dismiss="modal">Close</button>
											<!-- <button type="button" class="btn btn-primary">Save
                                                            changes</button> -->
											<form:button class="btn btn-primary rounded-0" type="submit">Proceed</form:button>
										</div>
									</form:form>

								</div>
							</div>
						</div>
					</div>
					
					
					<!-- Modal For exampleModalCenterForMultipleFile -->
					
					
					
					<div class="modal fade " id="exampleModalCenterForMultipleFile" tabindex="-1"
						role="dialog" aria-labelledby="exampleModalCenterTitle"
						aria-hidden="true">
						<div class="modal-dialog modal-dialog-centered" role="document">
							<div class="modal-content rounded-0">
								<div class="modal-header">
									<h5 class="modal-title" id="exampleModalCenterTitle">Add
										New Multiple File For</h5>
									<button type="button" class="close" data-dismiss="modal"
										aria-label="Close">
										<span aria-hidden="true">&times;</span>
									</button>
								</div>
								<div class="modal-body ">
									<form:form method="POST"
										action="${pageContext.request.contextPath}/addContentForm"
										modelAttribute="content">
										<form:input path="contentType" value="Multiple_File" type="hidden"/>
										<p>
											<form:radiobutton path="contentFor" value="module"
												checked="checked" />
											<form:label path="contentFor">Module</form:label>
										</p>
										<p>
											<form:radiobutton path="contentFor" value="course" />
											<form:label path="contentFor">Course</form:label>
										</p>
										<%-- <form:button>sahdaud</form:button> --%>
										<div class="modal-footer">
											<button type="button" class="btn btn-secondary rounded-0"
												data-dismiss="modal">Close</button>
											<!-- <button type="button" class="btn btn-primary">Save
                                                            changes</button> -->
											<form:button class="btn btn-primary rounded-0" type="submit">Proceed</form:button>
										</div>
									</form:form>

								</div>
							</div>
						</div>
					</div>
					
					
					<!-- Modal For exampleModalCenterForAddNewLink -->
					
					
					
					<div class="modal fade " id="exampleModalCenterForAddNewLink" tabindex="-1"
						role="dialog" aria-labelledby="exampleModalCenterTitle"
						aria-hidden="true">
						<div class="modal-dialog modal-dialog-centered" role="document">
							<div class="modal-content rounded-0">
								<div class="modal-header">
									<h5 class="modal-title" id="exampleModalCenterTitle">Add
										New Link For</h5>
									<button type="button" class="close" data-dismiss="modal"
										aria-label="Close">
										<span aria-hidden="true">&times;</span>
									</button>
								</div>
								<div class="modal-body ">
									<form:form method="POST"
										action="${pageContext.request.contextPath}/addContentForm"
										modelAttribute="content">
										<form:input path="contentType" value="Link" type="hidden"/>
										<p>
											<form:radiobutton path="contentFor" value="module"
												checked="checked" />
											<form:label path="contentFor">Module</form:label>
										</p>
										<p>
											<form:radiobutton path="contentFor" value="course" />
											<form:label path="contentFor">Course</form:label>
										</p>
										<%-- <form:button>sahdaud</form:button> --%>
										<div class="modal-footer">
											<button type="button" class="btn btn-secondary rounded-0"
												data-dismiss="modal">Close</button>
											<!-- <button type="button" class="btn btn-primary">Save
                                                            changes</button> -->
											<form:button class="btn btn-primary rounded-0" type="submit">Proceed</form:button>
										</div>
									</form:form>

								</div>
							</div>
						</div>
					</div>
					



					<!-- Modal End -->

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
					function myFunction() {
						//alert("Onchange Function called!");
						//var courseid = $('#courseId').val();
						//alert(courseid);
						/* if (courseid) {

						} else {
							var comfirmValue = confirm('Please select the course! NOW');
							return !comfirmValue;
						} */
					}
					/* $("#acadYear")
							.on(
									'change',
									function() {

										console.log('Year Selected');

										var acadYear = $('#acadYear').val();
										console.log(acadYear);
										if (acadYear) {
											$
													.ajax({
														type : 'POST',
														url : '${pageContext.request.contextPath}/findModulesByUsernameAndAcadYear?acadYear='
																+ acadYear,
														success : function(data) {
															var json = JSON
																	.parse(data);

															var optionsAsString = "";
															optionsAsString = "<option selected='selected'>Select Module</option>";
															$('#idOfModule')
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

					}
															}
															console
																	.log("optionsAsString"
																			+ optionsAsString);

															$('#idOfModule')
																	.append(
																			optionsAsString);

														}
													});
										} else {
											var optionsAsString = "";
											optionsAsString = "<option selected='selected'>Select Module</option>";
											$('#idOfModule').find('option')
													.remove();
											$('#idOfModule').append(
													optionsAsString);
											console.log("no course");
										}

									});
					$('#idOfModule').trigger('change');

					$("#idOfModule")
							.on(
									'change',
									function() {

										console.log('Module Selected');
										var acadYear = $('#acadYear').val();
										var moduleId = $('#idOfModule').val();
										console.log(acadYear);
										if (acadYear) {
											$
													.ajax({
														type : 'POST',
														url : '${pageContext.request.contextPath}/findCoursesByModuleIdAndUsernameAndAcadYear?acadYear='
																+ acadYear
																+ "&moduleId="
																+ moduleId,
														success : function(data) {
															var json = JSON
																	.parse(data);

															var optionsAsString = "";
															optionsAsString = "<option selected='selected' value='' disabled>Select Course</option>";
															$('#courseId')
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

															$('#courseId')
																	.append(
																			optionsAsString);

														}
													});
										} else {
											var optionsAsString = "";
											optionsAsString = "<option selected='selected'>Select Course</option>";
											$('#courseId').find('option')
													.remove();
											$('#courseId').append(
													optionsAsString);
											console.log("no course");
										}

									});
					$('#courseId').trigger('change'); */
					
					$("#acadYear")
					.on(
							'change',
							function() {

								console.log('Year Selected');

								var acadYear = $('#acadYear').val();
								console.log(acadYear);
								if (acadYear) {
									$.ajax({
												type : 'POST',
												url : '${pageContext.request.contextPath}/findModulesByUsernameAndAcadYear?acadYear='
														+ acadYear,
												success : function(data) {
													var json = JSON
															.parse(data);

													var optionsAsString = "";
													optionsAsString = "<option disabled value selected='selected'>Select Module</option>";
													$('#idOfModule')
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
													console.log("optionsAsString"+ optionsAsString);

													$('#idOfModule').append(optionsAsString);

												}
											});
									//ForCourse
									$.ajax({
										type : 'POST',
										url : '${pageContext.request.contextPath}/findCoursesByUsernameAndAcadYear?acadYear='
												+ acadYear,
										success : function(data) {
											var json = JSON
													.parse(data);

											var optionsAsString = "";
											optionsAsString = "<option disabled value selected='selected'>Select Course</option>";
											$('#courseId')
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
											console.log("optionsAsString"+ optionsAsString);

											$('#courseId').append(optionsAsString);

										}
									});
								} else {
									var optionsAsString = "";
									optionsAsString = "<option selected='selected'>Select Module</option>";
									$('#idOfModule').find('option')
											.remove();
									$('#idOfModule').append(
											optionsAsString);
									console.log("no course");
								}

							});
			$('#idOfModule').trigger('change');

			$("#idOfModule")
					.on(
							'change',
							function() {

								console.log('Module Selected');
								var acadYear = $('#acadYear').val();
								var moduleId = $('#idOfModule').val();
								console.log(acadYear);
								if (acadYear) {
									$
											.ajax({
												type : 'POST',
												url : '${pageContext.request.contextPath}/findCoursesByModuleIdAndUsernameAndAcadYear?acadYear='
														+ acadYear
														+ "&moduleId="
														+ moduleId,
												success : function(data) {
													var json = JSON
															.parse(data);

													var optionsAsString = "";
													optionsAsString = "<option selected='selected' value='' disabled>Select Course</option>";
													$('#courseId').find('option').remove();
													
													console.log(json);
													console.log('Course Id-----'+courseId);
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
		<%--if (moduleId == idjson[key]) {
														optionsAsString += "<option value='" +key + "' selected>"
																+ idjson[key]
																+ "</option>";

													} else {
														console
																.log('else entered00');
														optionsAsString += "<option value='" +key + "'>"
																+ idjson[key]
																+ "</option>";
													} --%>
			}
													}
													console
															.log("optionsAsString"
																	+ optionsAsString);

													$('#courseId')
															.append(
																	optionsAsString);

												}
											});
								} else {
									var optionsAsString = "";
									optionsAsString = "<option selected='selected' disabled>Select Course</option>";
									$('#courseId').find('option')
											.remove();
									$('#courseId').append(optionsAsString);
									console.log("no course");
								}

							});
			$('#courseId').trigger('change');
			
			
			$('#submit-search').click(function(){
				
				$('acadYear').val();
			})
			
			
			
			
				</script>