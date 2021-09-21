<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
	
	
	

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
			<div class="right_col" role="main">

				<div class="dashboard_container">

					<div class="dashboard_container_spacing">
					
					<nav aria-label="breadcrumb">
                            <ol class="breadcrumb">
                                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/homepage">Dashboard</a></li>
                                <li class="breadcrumb-item" aria-current="page"><c:out value="${Program_Name}" /></li>
                                <sec:authorize access="hasRole('ROLE_STUDENT')">
								<c:out value="${AcadSession}" />
							</sec:authorize>
                                <li class="breadcrumb-item active" aria-current="page"> Library</li>
                            </ol>
                        </nav>


						<jsp:include page="../common/alert.jsp" />



						<!-- Results Panel -->
						<div class="card bg-white">
							<div class="card-body">
								<div class="x_panel">
									<div class="x_title">
										<h5 class="text-center border-bottom pb-2">
											Library | ${size} Records Found
											
											<c:forEach var="folder" items="${fList}" varStatus="status">
												<c:url value="/viewLibrary" var="navigateInsideFolder">
													<c:param name="folderPath" value="${folder.filePath}" />
													<c:param name="parentId" value="${folder.id}" />
												</c:url> |
												<u><a href="${navigateInsideFolder}"
													style="text-decoration-color: red; text-decoration: underline; -webkit-text-decoration-color: red;"><c:out
															value="${folder.contentName }" /></a></u>

											</c:forEach>
										</h5>
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

											<div class="col-md-8 col-sm-12 text-right">
												<sec:authorize access="hasAnyRole('ROLE_LIBRARIAN')">
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
												</sec:authorize>
											</div>
										</div>
										<!-- <input class="form-control w-100" type="text" id="myInput" onkeyup="myFunction()"
											placeholder="Search for names.." title="Type in a name"> -->

										<div class="table-responsive container text-center testAssignTable">

											<table class="table table-striped table-hover" id="contentTree">
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
																	<sec:authorize access="hasAnyRole('ROLE_LIBRARIAN')">
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
																	<a href="${editurl}" title="Edit"><i
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
                        
          
                   
                    </div>
			
			<!-- SIDEBAR START -->
	         <jsp:include page="../common/newSidebar.jsp" />
	        <!-- SIDEBAR END -->
	<jsp:include page="../common/footer.jsp"/>

			
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
