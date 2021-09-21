<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
	
	
<sec:authorize access="hasRole('ROLE_STUDENT')">


<jsp:include page="../common/newDashboardHeader.jsp" />
<div class="d-flex dataTableBottom" id="prilfeDetailsPage">
	<jsp:include page="../common/newLeftNavbar.jsp" />
	<jsp:include page="../common/newLeftSidebar.jsp" />
	<jsp:include page="../common/rightSidebar.jsp" />

	<!-- DASHBOARD BODY STARTS HERE -->

	<div class="container-fluid m-0 p-0 dashboardWraper assignmentW">

		<jsp:include page="../common/newTopHeader.jsp" />

		<div class="container mt-5">
			<div class="row">
				<!-- SEMESTER CONTENT -->
				<div
					class="col-lg-9 col-md-9 col-sm-12 dashboardBody ml-auto mr-auto">
					<jsp:include page="../common/alert.jsp" />
					
					<!-- page content: START -->
					
					<nav class="" aria-label="breadcrumb">
							<ol class="breadcrumb d-flex">
								<li class="breadcrumb-item"><a
									href="${pageContext.request.contextPath}/homepage">Dashboard</a></li>
								<li class="breadcrumb-item active" aria-current="page">Library
								</li>
							</ol>

						</nav>

						<jsp:include page="../common/alert.jsp" />



						<!-- Results Panel -->
						<div class="card bg-white border">
							<div class="card-body">
										<h5 class="text-center border-bottom pb-2">
											Library | ${size} Records Found
											<c:forEach var="folder" items="${fList}" varStatus="status">
												<c:url value="/viewLibrary" var="navigateInsideFolder">
													<c:param name="folderPath" value="${folder.filePath}" />
													<c:param name="parentId" value="${folder.id}" />
												</c:url> |
												<a class="text-dark" href="${navigateInsideFolder}">
												<c:out value="${folder.contentName }" />
												</a>
											</c:forEach>
										</h5>

									<div class="x_content">
										<c:url value="/addLibraryItemForm" var="addFolder">
											<c:param name="folderPath" value="${content.folderPath}" />
											<c:param name="contentType" value="Folder" />
											<c:param name="parentId" value="${content.parentId}" />
										</c:url>

										<c:url value="/addLibraryItemForm" var="addFile">
											<c:param name="folderPath" value="${content.folderPath}" />
											<c:param name="contentType" value="File" />
											<c:param name="parentId" value="${content.parentId}" />
										</c:url>

										<c:url value="/addLibraryItemForm" var="addLink">
											<c:param name="folderPath" value="${content.folderPath}" />
											<c:param name="contentType" value="Link" />
											<c:param name="parentId" value="${content.parentId}" />
										</c:url>
										<c:url value="/addLibraryItemForm" var="addZip">
											<c:param name="folderPath" value="${content.folderPath}" />
											<c:param name="contentType" value="Zip" />
											<c:param name="parentId" value="${content.parentId}" />
										</c:url>
										<c:url value="/viewLibraryAnnouncements" var="rootFolder">
										</c:url>


										<div class="row">
											
											<div class="col-12 text-right mb-3">
												<a id="submit" class="btn btn-xs btn-dark"
													href="${rootFolder}">Back to Library Home</a>
											</div>
										</div>
										<!-- <input type="text" id="myInput" onkeyup="myFunction()"
											class="form-control w-100"
											placeholder="Search for names.." title="Type in a name"> -->

										<div class="table-responsive container text-center">

											<table class="table  table-hover" style="font-size: 12px"
												id="contentTree">
												<thead>
													<tr>
														<th>Content Name</th>
														<th>Description</th>

														<th>Actions</th>

													</tr>
												</thead>
												<tbody>

													<c:forEach var="content" items="${allContent}"
														varStatus="status">
														<tr data-tt-id="${content.id}"
															data-tt-parent-id="${content.parentId}">

															<td><c:if test="${content.contentType == 'Folder' }">
																	<i class="fa lms-folder-o fa-lg"
																		style="background: #E6CB47; margin-right: 5px"></i>

																	<c:url value="/viewLibrary" var="navigateInsideFolder">
																		<c:param name="folderPath" value="${content.filePath}" />
																		<c:param name="parentId" value="${content.id}" />
																	</c:url>
																	<a href="${navigateInsideFolder}"><c:out
																			value="${content.contentName}" /></a>


																	<c:url value="/addLibraryItemForm" var="addFolder">
																		<c:param name="folderPath" value="${content.filePath}" />
																		<c:param name="contentType" value="Folder" />
																		<c:param name="parentId" value="${content.id}" />
																	</c:url>

																	<c:url value="/addLibraryItemForm" var="addFile">
																		<c:param name="folderPath" value="${content.filePath}" />
																		<c:param name="contentType" value="File" />
																		<c:param name="parentId" value="${content.id}" />
																	</c:url>

																	<c:url value="/addLibraryItemForm" var="addLink">
																		<c:param name="folderPath" value="${content.filePath}" />
																		<c:param name="contentType" value="Link" />
																		<c:param name="parentId" value="${content.id}" />
																	</c:url>
																	<c:url value="/copyLibraryItemForm" var="copy">

																		<c:param name="id" value="${content.id}" />
																		<c:param name="action" value="copy" />
																	</c:url>
																	<c:url value="/copyLibraryItemForm" var="move">
																		<c:param name="id" value="${content.id}" />
																		<c:param name="action" value="move" />
																	</c:url>


																	<!-- Arrow icon for Menu -->
																
																</c:if> <c:if test="${content.contentType == 'File' }">
																	<i class="fa ${content.fontAwesomeClass} fa-lg"
																		style="margin-right: 5px"></i>
																	<a href="downloadFile?libraryId=${content.id}"> <c:out
																			value="${content.contentName}" /> <i
																		class="fa fa-download" style="margin-left: 5px"></i>
																	</a>
																</c:if> <c:if test="${content.contentType == 'Link' }">
																	<i class="fa fa-link fa-lg" style="margin-right: 5px"></i>
																	<a href="${content.linkUrl}" target="_blank"> <c:out
																			value="${content.contentName}" />
																	</a>
																</c:if></td>

															<td><c:out value="${content.contentDescription}" /></td>


															<td><c:url value="/downloadAllFileForLibrary"
																	var="downloadurl">
																	<c:param name="libraryId" value="${content.id}" />
																	<c:param name="contentType"
																		value="${content.contentType}" />

																</c:url> <c:if test="${content.contentType ne 'File' }">

																	<a href="${downloadurl}" title="Download"><i
																		class="fa fa-download" aria-hidden="true"></i></a>
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
			<!-- /page content: END -->
					




				</div>



				<!-- SIDEBAR START -->
				<jsp:include page="../common/newSidebar.jsp" />
				<!-- SIDEBAR END -->
				
				<jsp:include page="../common/footer.jsp" />
				

</sec:authorize>	

<sec:authorize access="hasRole('ROLE_ADMIN')">


    <jsp:include page="../common/newDashboardHeader.jsp" /> 

    <div class="d-flex adminPage tabPage dataTableBottom" id="adminPage">
    <jsp:include page="../common/newAdminLeftNavBar.jsp"/>
    <jsp:include page="../common/rightSidebarAdmin.jsp" />

    

    <!-- DASHBOARD BODY STARTS HERE -->

    <div class="container-fluid m-0 p-0 dashboardWraper">

        <jsp:include page="../common/newAdminTopHeader.jsp" />

                <!-- SEMESTER CONTENT -->    
                        <div class="col-lg-12 col-md-12 col-sm-12 dashboardBody p-5">
                        <!-- page content: START -->
					
					<nav class="" aria-label="breadcrumb">
							<ol class="breadcrumb d-flex">
								<li class="breadcrumb-item"><a
									href="${pageContext.request.contextPath}/homepage">Dashboard</a></li>
								<li class="breadcrumb-item active" aria-current="page">Library
								</li>
							</ol>

						</nav>

						<jsp:include page="../common/alert.jsp" />



						<!-- Results Panel -->
						<div class="card bg-white border">
							<div class="card-body">
										<h5 class="text-center border-bottom pb-2">
											Library | ${size} Records Found
											<c:forEach var="folder" items="${fList}" varStatus="status">
												<c:url value="/viewLibrary" var="navigateInsideFolder">
													<c:param name="folderPath" value="${folder.filePath}" />
													<c:param name="parentId" value="${folder.id}" />
												</c:url> |
												<a class="text-dark" href="${navigateInsideFolder}">
												<c:out value="${folder.contentName }" />
												</a>
											</c:forEach>
										</h5>

									<div class="x_content">
										<c:url value="/addLibraryItemForm" var="addFolder">
											<c:param name="folderPath" value="${content.folderPath}" />
											<c:param name="contentType" value="Folder" />
											<c:param name="parentId" value="${content.parentId}" />
										</c:url>

										<c:url value="/addLibraryItemForm" var="addFile">
											<c:param name="folderPath" value="${content.folderPath}" />
											<c:param name="contentType" value="File" />
											<c:param name="parentId" value="${content.parentId}" />
										</c:url>

										<c:url value="/addLibraryItemForm" var="addLink">
											<c:param name="folderPath" value="${content.folderPath}" />
											<c:param name="contentType" value="Link" />
											<c:param name="parentId" value="${content.parentId}" />
										</c:url>
										<c:url value="/addLibraryItemForm" var="addZip">
											<c:param name="folderPath" value="${content.folderPath}" />
											<c:param name="contentType" value="Zip" />
											<c:param name="parentId" value="${content.parentId}" />
										</c:url>
										<c:url value="/viewLibraryAnnouncements" var="rootFolder">
										</c:url>


										<div class="row">
											
											<div class="col-12 text-right mb-3">
												<a id="submit" class="btn btn-xs btn-dark"
													href="${rootFolder}">Back to Library Home</a>
											</div>
										</div>
										<!-- <input type="text" id="myInput" onkeyup="myFunction()"
											class="form-control w-100"
											placeholder="Search for names.." title="Type in a name"> -->

										<div class="table-responsive container text-center">

											<table class="table  table-hover" style="font-size: 12px"
												id="contentTree">
												<thead>
													<tr>
														<th>Content Name</th>
														<th>Description</th>

														<th>Actions</th>

													</tr>
												</thead>
												<tbody>

													<c:forEach var="content" items="${allContent}"
														varStatus="status">
														<tr data-tt-id="${content.id}"
															data-tt-parent-id="${content.parentId}">

															<td><c:if test="${content.contentType == 'Folder' }">
																	<i class="fa lms-folder-o fa-lg"
																		style="background: #E6CB47; margin-right: 5px"></i>

																	<c:url value="/viewLibrary" var="navigateInsideFolder">
																		<c:param name="folderPath" value="${content.filePath}" />
																		<c:param name="parentId" value="${content.id}" />
																	</c:url>
																	<a href="${navigateInsideFolder}"><c:out
																			value="${content.contentName}" /></a>


																	<c:url value="/addLibraryItemForm" var="addFolder">
																		<c:param name="folderPath" value="${content.filePath}" />
																		<c:param name="contentType" value="Folder" />
																		<c:param name="parentId" value="${content.id}" />
																	</c:url>

																	<c:url value="/addLibraryItemForm" var="addFile">
																		<c:param name="folderPath" value="${content.filePath}" />
																		<c:param name="contentType" value="File" />
																		<c:param name="parentId" value="${content.id}" />
																	</c:url>

																	<c:url value="/addLibraryItemForm" var="addLink">
																		<c:param name="folderPath" value="${content.filePath}" />
																		<c:param name="contentType" value="Link" />
																		<c:param name="parentId" value="${content.id}" />
																	</c:url>
																	<c:url value="/copyLibraryItemForm" var="copy">

																		<c:param name="id" value="${content.id}" />
																		<c:param name="action" value="copy" />
																	</c:url>
																	<c:url value="/copyLibraryItemForm" var="move">
																		<c:param name="id" value="${content.id}" />
																		<c:param name="action" value="move" />
																	</c:url>


																	<!-- Arrow icon for Menu -->
																
																</c:if> <c:if test="${content.contentType == 'File' }">
																	<i class="fa ${content.fontAwesomeClass} fa-lg"
																		style="margin-right: 5px"></i>
																	<a href="downloadFile?libraryId=${content.id}"> <c:out
																			value="${content.contentName}" /> <i
																		class="fa fa-download" style="margin-left: 5px"></i>
																	</a>
																</c:if> <c:if test="${content.contentType == 'Link' }">
																	<i class="fa fa-link fa-lg" style="margin-right: 5px"></i>
																	<a href="${content.linkUrl}" target="_blank"> <c:out
																			value="${content.contentName}" />
																	</a>
																</c:if></td>

															<td><c:out value="${content.contentDescription}" /></td>


															<td><c:url value="/downloadAllFileForLibrary"
																	var="downloadurl">
																	<c:param name="libraryId" value="${content.id}" />
																	<c:param name="contentType"
																		value="${content.contentType}" />

																</c:url> <c:if test="${content.contentType ne 'File' }">

																	<a href="${downloadurl}" title="Download"><i
																		class="fa fa-download" aria-hidden="true"></i></a>
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
			<!-- /page content: END -->




                        </div>

                <!-- SIDEBAR START -->

                <!-- SIDEBAR END -->
        <jsp:include page="../common/newAdminFooter.jsp"/>

        
</sec:authorize>

<sec:authorize access="hasAnyRole('ROLE_EXAM', 'ROLE_LIBRARIAN')">
<html lang="en">
<jsp:include page="../common/css.jsp" />
<body class="nav-md footer_fixed">

	<div class="loader"></div>


	<div class="container body">
		<div class="main_container">


			<jsp:include page="../common/leftSidebar.jsp" />
			<jsp:include page="../common/topHeaderLibrian.jsp">
				<jsp:param value="Library" name="activeMenu" />
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
							<i class="fa fa-angle-right"></i> Library
						</div>
						<jsp:include page="../common/alert.jsp" />



						<!-- Results Panel -->
						<div class="row">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">
									<div class="x_title">
										<h2>
											Library | ${size} Records Found
											<c:forEach var="folder" items="${fList}" varStatus="status">
												<c:url value="/viewLibrary" var="navigateInsideFolder">
													<c:param name="folderPath" value="${folder.filePath}" />
													<c:param name="parentId" value="${folder.id}" />
													<c:param name="createdBy" value="${folder.createdBy}" />
													<c:param name="createOnly" value="${folder.createOnly}"/>
													<c:param name="editOnly" value="${folder.editOnly}"/>
													
												</c:url> |
												<u><a href="${navigateInsideFolder}"
													style="text-decoration-color: red; text-decoration: underline; -webkit-text-decoration-color: red;"><c:out
															value="${folder.contentName }" /></a></u>

											</c:forEach>
										</h2>

										<ul class="nav navbar-right panel_toolbox">
											<li><a class="collapse-link"><i
													class="fa fa-chevron-up"></i></a></li>
											<li><a class="close-link"><i class="fa fa-close"></i></a></li>
										</ul>
										<div class="clearfix"></div>
									</div>
									<div class="x_content">
										<c:url value="/addLibraryItemForm" var="addFolder">
											<c:param name="folderPath" value="${content.folderPath}" />
											<c:param name="contentType" value="Folder" />
											<c:param name="parentId" value="${content.parentId}" />
										</c:url>

										<c:url value="/addLibraryItemForm" var="addFile">
											<c:param name="folderPath" value="${content.folderPath}" />
											<c:param name="contentType" value="File" />
											<c:param name="parentId" value="${content.parentId}" />
										</c:url>

										<c:url value="/addLibraryItemForm" var="addLink">
											<c:param name="folderPath" value="${content.folderPath}" />
											<c:param name="contentType" value="Link" />
											<c:param name="parentId" value="${content.parentId}" />
										</c:url>
										<c:url value="/addLibraryItemForm" var="addZip">
											<c:param name="folderPath" value="${content.folderPath}" />
											<c:param name="contentType" value="Zip" />
											<c:param name="parentId" value="${content.parentId}" />
										</c:url>
										<c:url value="/viewLibraryAnnouncements" var="rootFolder">
										</c:url>


										<div class="row">
											<!-- <div class="col-md-4 col-xs-12"> -->

											<!-- </div> -->
											<div class="col-md-4 col-xs-12">
												<!-- <div class="font_weight_bold blue_link">
													<a href="javascript:void(0);"
														onclick="jQuery('.treetable').treetable('expandAll'); return false;"><i
														class="fa fa-plus-square-o"></i> Expand All</a> &nbsp; |
													&nbsp; <a href="javascript:void(0);"
														onclick="jQuery('.treetable').treetable('collapseAll'); return false;"><i
														class="fa fa-minus-square-o"></i> Collapse All</a>
												</div> -->
											</div>
											<div class="col-md-8 col-xs-12 text-right">
												<a id="submit" class="btn btn-xs btn-primary "
													href="${rootFolder}">Back to Library Home</a>
												<sec:authorize access="hasAnyRole('ROLE_LIBRARIAN')">
													<c:if test="${createOnly eq 'Y' || fn:toUpperCase(content.createdBy) eq fn:toUpperCase(userBean.username)}">
													<a id="submit" class="btn btn-xs btn-primary "
														href="${addFolder}"><i class="fa fa-folder-o"></i> Add
														New Folder</a>
													<a id="submit" class="btn btn-xs btn-primary "
														href="${addFile}"><i class="fa fa-file-o"></i> Add New
														File</a>
													<a id="submit" class="btn btn-xs btn-primary "
														href="${addLink}"><i class="fa fa-link""true"></i> Add
														New Link</a>
													<a id="submit" class="btn btn-xs btn-primary "
														href="${addZip}"><i class="fa fa-link"></i> Add New
														Zip</a>
											</c:if>
												</sec:authorize>
											</div>
										</div>
										<!-- <input type="text" id="myInput" onkeyup="myFunction()"
											style="width: -webkit-fill-available; height: 40;"
											placeholder="Search for names.." title="Type in a name"> -->

										<div class="table-responsive container text-center" style="min-height: 300px;">

											<table class="table  table-hover" style="font-size: 12px"
												id="contentTree" >
												<thead>
													<tr>
														<th>Content Name</th>
														<th>Description</th>

														<th>Actions</th>

													</tr>
												</thead>
												<tbody>

													<c:forEach var="content" items="${allContent}"
														varStatus="status">
														<tr data-tt-id="${content.id}"
															data-tt-parent-id="${content.parentId}">

															<td><c:if test="${content.contentType == 'Folder' }">
																	<i class="fa lms-folder-o fa-lg"
																		style="background: #E6CB47; margin-right: 5px"></i>

																	<c:url value="/viewLibrary" var="navigateInsideFolder">
																		<c:param name="folderPath" value="${content.filePath}" />
																		<c:param name="parentId" value="${content.id}" />
																			<c:param name="createdBy" value="${content.createdBy}" />
																			<c:param name="createOnly" value="${content.createOnly}"/>
																			<c:param name="editOnly" value="${content.editOnly}"/>
																	</c:url>
																	<a href="${navigateInsideFolder}"><c:out
																			value="${content.contentName}" /></a>


																	<c:url value="/addLibraryItemForm" var="addFolder">
																		<c:param name="folderPath" value="${content.filePath}" />
																		<c:param name="contentType" value="Folder" />
																		<c:param name="parentId" value="${content.id}" />
																	</c:url>

																	<c:url value="/addLibraryItemForm" var="addFile">
																		<c:param name="folderPath" value="${content.filePath}" />
																		<c:param name="contentType" value="File" />
																		<c:param name="parentId" value="${content.id}" />
																	</c:url>

																	<c:url value="/addLibraryItemForm" var="addLink">
																		<c:param name="folderPath" value="${content.filePath}" />
																		<c:param name="contentType" value="Link" />
																		<c:param name="parentId" value="${content.id}" />
																	</c:url>
																	<c:url value="/copyLibraryItemForm" var="copy">

																		<c:param name="id" value="${content.id}" />
																		<c:param name="action" value="copy" />
																	</c:url>
																	<c:url value="/copyLibraryItemForm" var="move">
																		<c:param name="id" value="${content.id}" />
																		<c:param name="action" value="move" />
																	</c:url>


																	<!-- Arrow icon for Menu -->
																	<sec:authorize access="hasAnyRole('ROLE_LIBRARIAN')">
																	<c:if test="${createOnly eq 'Y' || fn:toUpperCase(content.createdBy) eq fn:toUpperCase(userBean.username) || fn:toUpperCase(content.username) eq fn:toUpperCase(userBean.username)}">
																	
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
																				<li><a href="${copy}">Copy</a></li>
																				<li><a href="${move}">Move</a></li>
																			</ul>
																		</div>
																		</c:if>
																	</sec:authorize>
																</c:if> <c:if test="${content.contentType == 'File' }">
																	<i class="fa ${content.fontAwesomeClass} fa-lg"
																		style="margin-right: 5px"></i>
																	<a href="downloadFile?libraryId=${content.id}"> <c:out
																			value="${content.contentName}" /> <i
																		class="fa fa-download" style="margin-left: 5px"></i>
																	</a>
																</c:if> <c:if test="${content.contentType == 'Link' }">
																	<i class="fa fa-link fa-lg" style="margin-right: 5px"></i>
																	<a href="${content.linkUrl}" target="_blank"> <c:out
																			value="${content.contentName}" />
																	</a>
																</c:if></td>

															<td><c:out value="${content.contentDescription}" /></td>


															<td><sec:authorize
																	access="hasAnyRole('ROLE_LIBRARIAN')">
																	
																	 <c:if test="${fn:toUpperCase(content.createdBy) eq fn:toUpperCase(userBean.username) }">
																					
																						<a href="" class="libRights" libId="${content.id}" data-toggle="modal" data-target="#libraryRights" 
																						title="Library Rights"><i class="fa fa-users fa-lg"></i></a>&nbsp;
																						<a href="" class="libShare" libId="${content.id}" data-toggle="modal" data-target="#libraryShare"  title="Share"><i class="fa fa-share-alt fa-lg"></i></a>&nbsp;
																					
																					</c:if> 
																	<c:url value="/addLibraryItemForm" var="editurl">
																		<c:param name="id" value="${content.id}" />
																		<c:param name="contentType"
																			value="${content.contentType}" />
																	</c:url>
																	<c:url value="deleteLibraryItem" var="deleteurl">
																		<c:param name="id" value="${content.id}" />
																		<c:param name="contentType"
																			value="${content.contentType}" />
																	</c:url>
																	<c:if test="${editOnly eq 'Y' || fn:toUpperCase(content.createdBy) eq fn:toUpperCase(userBean.username) || content.editOnly eq 'Y'}">
																	<a href="${editurl}" title="Edit"><i
																		class="fa fa-pencil-square-o fa-lg"></i></a>&nbsp; 
											
																	</c:if>
																	<c:if test="${fn:toUpperCase(content.createdBy) eq fn:toUpperCase(userBean.username)}">
																	<a href="${deleteurl}" title="Delete"
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
																	</c:if>
																	
																</sec:authorize> <c:url value="/downloadAllFileForLibrary"
																	var="downloadurl">
																	<c:param name="libraryId" value="${content.id}" />
																	<c:param name="contentType"
																		value="${content.contentType}" />

																</c:url> <c:if test="${content.contentType ne 'File' }">

																	<a href="${downloadurl}" title="Download"><i
																		class="fa fa-download" aria-hidden="true"></i></a>
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



				</div>

			</div>
			<!-- /page content: END -->

<!-- Modal -->
	
<div class="modal fade" id="libraryRights" data-backdrop="static">
  <div class="modal-dialog modal-dialog-centered modal-dialog-scrollable" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="libraryRightsTitle">Library Rights</h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div class="modal-body">
        <div class="table-responsive">
            <table class="table table-bordered table-stripped libRightsTable">
                <thead>
                    <tr>
                        <th jName='username'>User ID</th>
                        <th jName='name'>User Name</th>
                        <th jName='createOnly'>Create</th>
                        <th jName='editOnly'>Edit</th>
                    </tr>  
                </thead>
                <tbody>
                    
                </tbody>
            </table>  
        </div>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-dark" data-dismiss="modal">Close</button>
        <button type="button" class="btn btn-success saveLibRights">Save changes</button>
      </div>
    </div>
  </div>
</div>

<div class="modal fade" id="libraryShare" data-backdrop="static">
  <div class="modal-dialog modal-dialog-centered modal-dialog-scrollable" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="libraryShareTitle">Library Share</h5>
        <button type="button" class="close closeBtn" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div class="modal-body">
      <label for="school">
				<strong>School <span style="color: red">*</span></strong>
			<label>
			<select id="school" class="form-control" required="required">
				<option value="" disabled="disabled" selected>Select School</option>
				<c:forEach var="school" items="${schoolListMap}" varStatus="status">
				<c:if test="${school.abbr != appName}">
				<option value="${school.abbr}">${school.collegeName}</option>
				</c:if>
				</c:forEach>
				
			</select>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-dark closeBtn" data-dismiss="modal">Close</button>
        <button type="button" id="shareBtn" class="btn btn-success shareLib">Share</button>
      </div>
    </div>
  </div>
</div>

			<jsp:include page="../common/footerLibrarian.jsp" />
			<script
				src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.10.3/jquery-ui.min.js"></script>
				<script>
				$('.libRights').click(function(){
					var thisLibId = $(this).attr('libId')
					$.ajax({
						type: 'GET',
						url:'${pageContext.request.contextPath}/getLibrarianListById?libId='+thisLibId,
						dataType: 'json',
						success: function(result) {
							console.log(result)
							let jsonData = "";
							
							for(let value of result) {
								jsonData += '<tr><td>'+value.username+'</td><td>'+value.firstname+' '+ value.lastname +'</td><td><div class="custom-control custom-checkbox"><input type="checkbox" class="custom-control-input createOnly" name="createOnly" value="'+value.createOnly+'"><label class="custom-control-label" for="createOnly"></label></div></td><td><div class="custom-control custom-checkbox"><input type="checkbox" class="custom-control-input editOnly" name="editOnly" value="'+value.editOnly+'"><label class="custom-control-label" for="editOnly"></label></div></td></tr>'
							}
							$('#libraryRights table tbody').html(jsonData)
							console.log(thisLibId)
							$('.saveLibRights').attr('libId', thisLibId)
							 //Updated Checked Attr on load by checking checkbox value
				            $('#libraryRights input[type=checkbox]').each(function(){
				                if($(this).val() === 'Y'){
				                    $(this).prop('checked', true);
				                }
				                else if($(this).val() === 'undefined') {
				                	$(this).prop('checked', false).val('N');
				                }
				            })
				    
				            //Code for changing THIS checkbox value
				            $('#libraryRights input[type=checkbox]').click(function(){
				            var thisVal = $(this).val();
				                
				            if(thisVal === 'N' || thisVal === 'undefined') {
				                $(this).val('Y').prop('checked', true);
				            } else {
				                $(this).val('N').prop('checked', false);
				            }
				            })
				            
				            //ON SAVE CLICK
				            $('.saveLibRights').click(function(){
								var trNum = $(this).parent().parent().find('table tbody tr').length
								console.log(trNum)
								
								var array = [];
							    var headers = [];
							    $('.libRightsTable th').each(function(index, item) {
							        headers[index] = $(item).attr('jName');
							    });
							    $('.libRightsTable tr').has('td').each(function() {
							        var arrayItem = {};
							        $('td', $(this)).each(function(index, item) {
							        	if($(item).find('div').length > 0) {
							        		arrayItem[headers[index]] = $(item).find('input').val()
							        	} else {
							          		arrayItem[headers[index]] = $(item).html()
							        	}
							        })
							        array.push(arrayItem);
							    })
							    console.log(JSON.stringify(array));
								let jArray = JSON.stringify(array);
							    
							    //sending json
							    $.ajax({
							    	type: 'POST',
							    	url: '${pageContext.request.contextPath}/saveLibrarianRights',
							    	dataType: 'json',
							    	data: {
							    		libId: thisLibId,
							    		librarianRightsJson: jArray
							    	},
							    	success: function (result) {
							    		
							    		var parsedObj = JSON.parse(JSON.stringify(result));
							    		console.log("result " +parsedObj.Status);
							    		if(parsedObj.Status = 'Success'){
							    			$('.right-success').remove()
							    			$('#libraryRights .modal-body').prepend('<h5 style="padding:5px;" class="bg-success right-success">Rights applied successfully.</h5>');
							    			 $('.right-success').show()
							    		}
							    		
							    		$('#libraryRights').on('hidden.bs.modal', function (e) {
							    			  $('.right-success').hide()
							    			})
							    	},
							    	error : function(result) {
										console.log('error');
										console.log(result);
									}
							    })
							    
							});
						}
					})
					})
					
		$('.libShare').click(function(){
						
			var thisLibId = $(this).attr('libId');
			$('.shareLib').attr('libId', thisLibId)
			$('.right-success').remove()
			$('.right-error').remove()
			
		})
		
		$('.shareLib').click(function(){
				$('#shareBtn').attr('disabled','disabled');
				var schoolName = $('#school').val();
				var libId = $(this).attr('libId');
				console.log(schoolName);
				console.log(libId);
				
				$.ajax({
			    	type: 'POST',
			    	url: '${pageContext.request.contextPath}/shareLibraryContent',
			    	dataType: 'json',
			    	data: {
			    		libId: libId,
			    		schoolName: schoolName,
			    	},
			    	success: function (result) {
			    		
			    		var parsedObj = JSON.parse(JSON.stringify(result));
			    		console.log("result " +parsedObj);
			    		$('.right-success').remove()
			    		$('.right-error').remove()
			    		if(parsedObj.Status == 'Success'){
			    			$('#libraryShare .modal-body').prepend('<h5 style="padding:5px;" class="bg-success right-success">Shared successfully.</h5>');
			    			$('.right-success').show()
			    			 
			    		}else if(parsedObj.Status == 'Exist'){
			    			$('#libraryShare .modal-body').prepend('<h5 style="padding:5px;" class="bg-danger right-error">Content already shared with this school.</h5>');
			    			$('.right-error').show()
			    		}else{
			    			$('#libraryShare .modal-body').prepend('<h5 style="padding:5px;" class="bg-danger right-error">Failed to share content.</h5>');
			    			$('.right-error').show()
			    		}
			    		
			    		$('#libraryRights').on('hidden.bs.modal', function (e) {
			    			  $('.right-success').hide()
			    			}) 
			    			$('#shareBtn').removeAttr('disabled'); 
			    	},
			    	error : function(result) {
						console.log('error');
						console.log(result);
						$('#shareBtn').removeAttr('disabled'); 
					}
			    	
			    })
			})
		
		$('.closeBtn').click(function(){
			var schoolName = $('#school').val();
			console.log("before close "+schoolName)
			$("#libraryShare").find("select").val('').end();
			var schoolName = $('#school').val();
			console.log("after close "+schoolName)
		})
				</script>
			<!-- <script>
				function myFunction() {
					var input, filter, table, tr, td, i;
					input = document.getElementById("myInput");
					filter = input.value.toUpperCase();
					table = document.getElementById("contentTree");
					tr = table.getElementsByTagName("tr");
					for (i = 0; i < tr.length; i++) {
						td = tr[i].getElementsByTagName("td")[0];
						if (td) {
							if (td.innerHTML.toUpperCase().indexOf(filter) > -1) {
								tr[i].style.display = "";
							} else {
								tr[i].style.display = "none";
							}
						}
					}
				}
			</script> -->

		</div>
	</div>





</body>
</html>
</sec:authorize>