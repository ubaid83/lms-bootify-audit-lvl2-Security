<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<html lang="en">
<jsp:include page="../common/css.jsp" />

<body class="nav-md footer_fixed">

	<div class="loader"></div>


	<div class="container body">
		<div class="main_container">


			<%-- 	<jsp:include page="../common/leftSidebar.jsp" />
			<jsp:include page="../common/topHeader.jsp">
				<jsp:param value="Library" name="activeMenu" />
			</jsp:include> --%>


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
									<%-- <div class="x_title">
										<h2>
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
										</h2>

										<ul class="nav navbar-right panel_toolbox">
											<li><a class="collapse-link"><i
													class="fa fa-chevron-up"></i></a></li>
											<li><a class="close-link"><i class="fa fa-close"></i></a></li>
										</ul>
										<div class="clearfix"></div>
									</div> --%>
									<div class="x_content">
										<%-- <c:url value="/addLibraryItemForm" var="addFolder">
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
										</c:url> --%>


										<div class="row">
											<!-- <div class="col-md-4 col-xs-12"> -->

											<!-- </div> -->
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
											<%-- <div class="col-md-8 col-xs-12 text-right">
												<a id="submit" class="btn btn-xs btn-primary "
													href="${rootFolder}">Back to Library Home</a>
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
											</div> --%>
										</div>
										<input type="text" id="myInput" onkeyup="myFunction()"
											style="width: -webkit-fill-available; height: 40;"
											placeholder="Search for names.." title="Type in a name">

										<div class="table-responsive">

											<table class="table  table-hover" style="font-size: 12px"
												id="contentTree">
												<thead>
													<tr>
														<th>Content Name</th>
														<!-- <th>Description</th>

														<th>Actions</th> -->

													</tr>
												</thead>
												<tbody>

													<c:forEach var="content" items="${allContent}"
														varStatus="status">
														<tr data-tt-id="${content.id}"
															data-tt-parent-id="${content.parentId}">

															<td><c:if test="${content.contentType == 'Folder' }">
																	<form:checkbox path="content" id="${content.id}"
																		class="check" onclick="return clicked();"
																		value="${content.folderPath}" />
																	<i class="fa lms-folder-o fa-lg"
																		style="background: #E6CB47; margin-right: 5px"></i>

																	<c:out value="${content.contentName}" />





																	<!-- Arrow icon for Menu -->
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


			<jsp:include page="../common/footerLibrarian.jsp" />
			<script>
				$(".check")
						.change(
								function() {
									var idOfCheck = '#' + $(this).attr("id");
									var id = $(this).attr("id");
									var copyvalue = $(idOfCheck).val();
									alert("copyvalue" + copyvalue);
									//sessionStorage.setItem("copyvalue", copyvalue); 
									//window.returnValue=copyvalue; 

									if (window.opener != null
											&& !window.opener.closed) {
										var txtName = window.opener.document
												.getElementById("txtName");
										txtName.value = document
												.getElementById(id).value;

									}

									window.close();

									//return confirm(value);

								});
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
			</script>

		</div>
	</div>





</body>
</html>
