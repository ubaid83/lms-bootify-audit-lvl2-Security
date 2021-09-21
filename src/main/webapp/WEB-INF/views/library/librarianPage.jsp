<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

<html lang="en">

<jsp:include page="../common/css.jsp" />
<%
	String libraryName = (String) session.getAttribute("libraryName");
%>
<head>
<style>
ul li:hover {
	background: #cccccc;
}

#libraryRights .custom-control-label:hover {
              cursor: pointer;
          }
              @media (min-width: 576px) {
                  #libraryRights .modal-dialog {
                      max-width: 80%;
                      margin: 10.75rem auto;
                  }
            }

</style>


</head>

<body class="nav-md footer_fixed">

	<div class="loader"></div>

	<div class="container body">

		<div class="main_container">

			<jsp:include page="../common/leftSidebar.jsp" />

			<jsp:include page="../common/topHeaderLibrian.jsp">
				<jsp:param value="Message" name="activeMenu" />
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

							<i class="fa fa-angle-right"></i> ${libraryName }

						</div>

						<jsp:include page="../common/alert.jsp" />
						<%-- <sec:authorize access="hasAnyRole('ROLE_LIBRARIAN')">
							<a class="btn btn-primary btn-sm pull-right" role="button"
								href="createWebpageForm"><i class="fa fa-plus"
								aria-hidden="true"></i> Create new tab</a>
						</sec:authorize> --%>
						<!-- Input Form Panel -->

						<div class="row">

							<div class="col-xs-12 col-sm-12">

								<div class="x_panel">

									<div class="ui-105-content">

										<ul class="nav nav-tabs nav-justified"
											style="border-bottom: 1px solid #000000; font-size: 15;">

											<li role="presentation" class="active" id="link-one"><a
												href="#login-block" aria-controls="home" role="tab"
												data-toggle="tab"><i class="fa fa-newspaper-o"
													aria-hidden="true"></i> Library Documents</a></li>

											<li role="presentation" id="link-two"><a
												href="#resources-block" aria-controls="profile" role="tab"
												data-toggle="tab"><i class="fa fa-newspaper-o"
													aria-hidden="true"></i> Library Resources</a></li>

											<li role="presentation" id="link-three" id="link-three"><a
												href="#register-block" aria-controls="messages" role="tab"
												data-toggle="tab"><i class="fa fa-bullhorn"
													aria-hidden="true"></i> Announcements</a></li>

										</ul>

										<div class="tab-content">

											<div role="tabpanel" class="tab-pane active" id="login-block">

												<!-- Login Block Form -->

												<div class="login-block-form">

													<form:form cssClass="form" role="form"
														action="viewLibraryAnnouncements" method="post"
														modelAttribute="library" id="viewLibraryAnnouncements">

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
															<%-- <div class="col-md-4 col-sm-6 col-xs-12 column">
																<div class="form-group">
																	<form:input id="" path="contentName" type="text"
																		placeholder="Search" class="form-control" />
																</div>
															</div> --%>
															<!-- <div class="col-md-4 col-xs-12">
																<div class="font_weight_bold blue_link">
																	<a href="javascript:void(0);"
																		onclick="jQuery('.treetable').treetable('expandAll'); return false;"><i
																		class="fa fa-plus-square-o"></i> Expand All</a> &nbsp; |
																	&nbsp; <a href="javascript:void(0);"
																		onclick="jQuery('.treetable').treetable('collapseAll'); return false;"><i
																		class="fa fa-minus-square-o"></i> Collapse All</a>
																</div>
															</div> -->
															<div class="col-md-12 text-right mt-3">
																<a id="submit" class="btn btn-xs btn-primary "
																	href="${rootFolder}">Back to Library Home</a>
																<sec:authorize access="hasAnyRole('ROLE_LIBRARIAN')">
																
																	<a id="submit" class="btn btn-xs btn-primary "
																		href="${addFolder}"><i class="fa fa-folder-o"></i>
																		Add New Folder</a>
																	<a id="submit" class="btn btn-xs btn-primary "
																		href="${addFile}"><i class="fa fa-file-o"></i> Add
																		New File</a>
																	<a id="submit" class="btn btn-xs btn-primary "
																		href="${addLink}"><i class="fa fa-link""true"></i>
																		Add New Link</a>
																	<a id="submit" class="btn btn-xs btn-primary "
																		href="${addZip}"><i class="fa fa-link"></i> Add
																		New Zip</a>
																</sec:authorize>
															</div>
														</div>

														<!-- <input type="text" id="myInput" onkeyup="myFunction()"
															style="width: -webkit-fill-available; height: 40;"
															placeholder="Search for names.." title="Type in a name"> -->
														<div class="table-responsive"  style="min-height: 300px;">


															<table id="contentTree"
																class="table table-striped table-hover"
																style="font-size: 12px">

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

																			<td><c:if
																					test="${content.contentType == 'Folder' }">
																					<i class="fa lms-folder-o fa-lg"
																						style="background: #E6CB47; margin-right: 5px"></i>

																					<c:url value="/viewLibrary"
																						var="navigateInsideFolder">
																						<c:param name="folderPath"
																							value="${content.filePath}" />
																						<c:param name="parentId" value="${content.id}" />
																						<c:param name="createdBy" value="${content.createdBy}" />
																						<c:param name="createOnly" value="${content.createOnly}"/>
																						<c:param name="editOnly" value="${content.editOnly}"/>
																					</c:url>
																					<a href="${navigateInsideFolder}"><c:out
																							value="${content.contentName}" /></a>


																					<c:url value="/addLibraryItemForm" var="addFolder">
																						<c:param name="folderPath"
																							value="${content.filePath}" />
																						<c:param name="contentType" value="Folder" />
																						<c:param name="parentId" value="${content.id}" />
																					</c:url>

																					<c:url value="/addLibraryItemForm" var="addFile">
																						<c:param name="folderPath"
																							value="${content.filePath}" />
																						<c:param name="contentType" value="File" />
																						<c:param name="parentId" value="${content.id}" />
																					</c:url>

																					<c:url value="/addLibraryItemForm" var="addLink">
																						<c:param name="folderPath"
																							value="${content.filePath}" />
																						<c:param name="contentType" value="Link" />
																						<c:param name="parentId" value="${content.id}" />
																					</c:url>
																					<c:url value="/addLibraryItemForm" var="addZip">
																						<c:param name="folderPath"
																							value="${content.filePath}" />
																						<c:param name="contentType" value="Zip" />
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
																					<sec:authorize
																						access="hasAnyRole('ROLE_LIBRARIAN')">
																						<c:if test="${content.createOnly eq 'Y' || content.createdBy eq username}">
																						<div class="btn-group">
																							<button type="button"
																								class="btn btn-xs dropdown-toggle"
																								data-toggle="dropdown"
																								style="margin-left: 5px; padding: 0px 3px; line-height: 1">
																								<span class="caret" style="line-height: 1"></span>
																							</button>
																							<ul class="dropdown-menu" role="menu">
																								<li><a href="${addFolder}">Add
																										Subfolder</a></li>
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
																					<a href="downloadFile?libraryId=${content.id}">
																						<c:out value="${content.contentName}" /> <i
																						class="fa fa-download" style="margin-left: 5px"></i>
																					</a>
																				</c:if> <c:if test="${content.contentType == 'Link' }">
																					<i class="fa fa-link fa-lg"
																						style="margin-right: 5px"></i>
																					<a href="${content.linkUrl}" target="_blank"> <c:out
																							value="${content.contentName}" />
																					</a>
																				</c:if></td>

																			<td><c:out value="${content.contentDescription}" /></td>


																			<td><c:url value="/addLibraryItemForm"
																					var="editurl">
																					<c:param name="id" value="${content.id}" />
																					<c:param name="contentType"
																						value="${content.contentType}" />
																				</c:url> <c:url value="deleteLibraryItem" var="deleteurl">
																					<c:param name="id" value="${content.id}" />
																					<c:param name="contentType"
																						value="${content.contentType}" />
																				</c:url>
																				
																					<sec:authorize access="hasAnyRole('ROLE_LIBRARIAN')">
																					<c:if test="${content.createdBy eq username }">
																					
																						<a href="" class="libRights" libId="${content.id}" data-toggle="modal" data-target="#libraryRights" 
																						title="Library Rights"><i class="fa fa-users fa-lg"></i></a>&nbsp;
																						<a href="" class="libShare" libId="${content.id}" data-toggle="modal" data-target="#libraryShare"  title="Share"><i class="fa fa-share-alt fa-lg"></i></a>&nbsp;
																					<a href="${editurl}" title="Edit"><i class="fa fa-pencil-square-o fa-lg"></i></a>&nbsp;
																					
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
																					<!-- shared -->
																					<c:if test="${content.createdBy eq username || content.username eq username }">
																					<c:if test="${content.editOnly eq 'Y' }">
																						
																					<a href="${editurl}" title="Edit"><i class="fa fa-pencil-square-o fa-lg"></i></a>&nbsp;
																					
																					 
																					</c:if>
																					<%-- <c:if test="${content.createdBy eq userBean.username}">
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
																					</c:if> --%>
																					</c:if>
																					
																				</sec:authorize> <c:url value="/downloadAllFileForLibrary"
																					var="downloadurl">
																					<c:param name="libraryId" value="${content.id}" />
																					<c:param name="contentType"
																						value="${content.contentType}" />

																				</c:url> <%-- <a href="${downloadurl}" title="Download"><i
																					class="fa fa-download" aria-hidden="true"></i></a> --%></td>

																		</tr>
																		
																		
																	</c:forEach>

																</tbody>

															</table>
															<c:if test="${size == 0}">
					No Content under this folder
					</c:if>

														</div>

													</form:form>

												</div>

											</div>

											<!-- Results Panel -->

											<div role="tabpanel" class="tab-pane" id="register-block">

												<!-- Login Block Form -->

												<div class="register-block-form">

													<form:form cssClass="form" role="form"
														action="viewLibraryAnnouncements" method="post"
														modelAttribute="announcement"
														id="viewLibraryAnnouncements">
														<c:url value="addAnnouncementFormLibrary"
															var="addAnnouncementUrl">

														</c:url>

														<sec:authorize access="hasAnyRole('ROLE_LIBRARIAN')">
															<div class="row mt-3">
																<a class="btn btn-primary btn-sm pull-right"
																	role="button" href="${addAnnouncementUrl}"><i
																	class="fa fa-plus" aria-hidden="true"></i> Add
																	Announcement</a>
															</div>
														</sec:authorize>

														<div class="row">
															<div class="col-md-4 col-sm-6 col-xs-12 column">
																<div class="form-group">
																	<form:label path="subject" for="subject">Announcement Title</form:label>
																	<form:input id="subject" path="subject" type="text"
																		placeholder="Announcement Title" class="form-control" />
																</div>
															</div>

															<div class="col-md-4 col-sm-6 col-xs-12 column">
																<div class="form-group">
																	<form:label path="startDate" for="startDate">Display From</form:label>

																	<div class='input-group date' id='datetimepicker1'>
																		<form:input id="startDate" path="startDate"
																			type="text" placeholder="Start Date"
																			class="form-control" readonly="true" />
																		<span class="input-group-addon"><span
																			class="glyphicon glyphicon-calendar"></span> </span>
																	</div>
																</div>
															</div>
															<div class="col-md-4 col-sm-6 col-xs-12 column">
																<div class="form-group">
																	<form:label path="endDate" for="endDate">Display Until</form:label>

																	<div class='input-group date' id='datetimepicker2'>
																		<form:input id="endDate" path="endDate" type="text"
																			placeholder="End Date" class="form-control"
																			readonly="true" />
																		<span class="input-group-addon"><span
																			class="glyphicon glyphicon-calendar"></span> </span>
																	</div>

																</div>
															</div>


															<div class="clearfix"></div>
															<div class="col-sm-12 column">
																<div class="form-group">
																	<button id="submit" name="submit"
																		class="btn btn-large btn-primary">Search</button>
																	<!-- <button type="reset" value="reset"
																		class="btn btn-large btn-primary">Reset</button> -->
																	<input type="reset" value="Reset"
																		class="btn btn-large btn-primary">
																	<button id="cancel" name="cancel"
																		class="btn btn-danger" formnovalidate="formnovalidate">Cancel</button>
																</div>
															</div>
														</div>
														<div class="table-responsive">

															<table id="outboxTable"
																class="table table-striped table-hover"
																style="font-size: 12px">

																<thead>

																	<tr>
																		<th>Sr. No.</th>
																		<th>Announcement Title</th>
																		<th>Type</th>

																		<th>Start Date</th>
																		<th>End Date</th>

																		<th>Actions</th>
																	</tr>

																</thead>

																<tbody>

																	<c:forEach var="announcement" items="${page.pageItems}"
																		varStatus="status">
																		<tr>
																			<td><c:out value="${status.count}" /></td>
																			<td><c:out value="${announcement.subject}" /></td>
																			<td><c:out
																					value="${announcement.announcementType}" /></td>

																			<td><c:out value="${announcement.startDate}" /></td>
																			<td><c:out value="${announcement.endDate}" /></td>

																			<td><c:url value="viewAnnouncement"
																					var="detailsUrl">
																					<c:param name="id" value="${announcement.id}" />
																				</c:url> <c:url value="addAnnouncementFormLibrary"
																					var="editurl">
																					<c:param name="id" value="${announcement.id}" />
																				</c:url> <c:url value="deleteAnnouncement" var="deleteurl">
																					<c:param name="id" value="${announcement.id}" />
																				</c:url> <a href="${detailsUrl}" title="Details"><i
																					class="fa fa-info-circle fa-lg"></i></a>&nbsp; <sec:authorize
																					access="hasAnyRole('ROLE_LIBRARIAN')">
																					<c:if test="${announcement.createdBy eq username }">
																					<a href="${editurl}" title="Edit"><i
																						class="fa fa-pencil-square-o fa-lg"></i></a>&nbsp; <a
																						href="${deleteurl}" title="Delete"
																						onclick="return confirm('Are you sure you want to delete this record?')"><i
																						class="fa fa-trash-o fa-lg"></i></a>
																					</c:if>
																				</sec:authorize></td>
																		</tr>
																	</c:forEach>


																</tbody>

															</table>

														</div>

													</form:form>
												</div>

											</div>
											<div role="tabpanel" class="tab-pane" id="resources-block">

												<!-- Login Block Form -->

												<div class="register-block-form">

													<form:form cssClass="form" role="form"
														action="viewLibraryAnnouncements" method="post"
														modelAttribute="announcement"
														id="viewLibraryAnnouncements">


														<sec:authorize access="hasAnyRole('ROLE_LIBRARIAN')">
															<div class="row mt-3">
																<a class="btn btn-primary btn-sm pull-right"
																	role="button" href="createWebpageForm"><i
																	class="fa fa-plus" aria-hidden="true"></i> Create
																	Library Page</a>
															</div>
														</sec:authorize>


														<!-- <div class="table-responsive"> -->

														<table id="myTable"
															class="table table-striped table-hover"
															style="font-size: 16px; font-family: serif;">

															<thead>

																<tr>
																	<th>Sr. No.</th>
																	<th>Name</th>
																	<th>Description</th>


																	<sec:authorize access="hasAnyRole('ROLE_LIBRARIAN')">

																		<th>Actions</th>
																	</sec:authorize>
																</tr>

															</thead>

															<tbody class="panel">

																<c:forEach var="tabs" items="${tabsList}"
																	varStatus="status">

																	<tr data-toggle="collapse" data-parent="#myTable"
																		data-target="#cores${status.count}">
																		<td><c:out value="${status.count}" /></td>
																		<td
																			style="color: #4b94d9; font-size: 18px; cursor: pointer;"><c:out
																				value="${tabs.name}" /></td>
																		<td><c:out value="${tabs.description}" /></td>



																		<td><sec:authorize
																				access="hasAnyRole('ROLE_LIBRARIAN')">
																				<c:if test="${tabs.createdBy eq username }">
																			
																				<a href="createWebpageForm?id=${tabs.id}"
																					title="Edit">Edit</a>&nbsp; 
																				<a href="deleteWebpage?id=${tabs.id}" title="Delete"
																					onclick="return confirm('Are you sure you want to delete this record?')">Delete</a>
																				</c:if>
																			</sec:authorize> <!-- <div class="row">
																					<div class="col-sm-12 column"> --> <c:if
																				test="${tabs.filePath ne null}">
																				<a href="downloadWebpageFile?id=${tabs.id}">Download</a>

																			</c:if></td>
																	</tr>
																	<tr id="cores${status.count}" class="collapse">

																		<td colspan="4" class="hiddenRow">
																			<div class="row">
																				<div class="col-sm-12 column">
																					Created By - ${tabs.firstname} ${tabs.lastname}
																					<div id=content
																						style="border-style: solid; border-width: 1px; padding: 10px; margin-top: 10px">${tabs.content}</div>
																				</div>
																			</div>
																		</td>
																	</tr>












																	</tr>




																</c:forEach>


															</tbody>

														</table>



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

			</div>

			<!-- /page content: END -->

			<jsp:include page="../common/footerLibrarian.jsp" />

		</div>

	</div>


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
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
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
        <button type="button" class="btn btn-dark" data-dismiss="modal">Close</button>
        <button type="button" id="shareBtn" class="btn btn-success shareLib" >Share</button>
      </div>
    </div>
  </div>
</div>



<script>

	

    $(document).ready(function(){
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
			$('.shareLib').click(function(){
				$('#shareBtn').attr('disabled','disabled');
				//this.disabled = true;
				
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
			    		$('.shareLib').disabled = false;
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
			
		})
		

    })
</script>


</body>
</html>