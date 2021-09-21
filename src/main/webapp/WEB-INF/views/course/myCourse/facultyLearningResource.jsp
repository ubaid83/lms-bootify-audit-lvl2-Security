



<div class="row">
	<div class="col-xs-12 col-sm-12">
		<div class="x_panel">
			<div class="x_title">
				<h2>Learning Resources</h2>
				<ul class="nav navbar-right panel_toolbox">
					<li><a href="<c:url value="getContentUnderAPathForFaculty" />"><span>View
								All</span></a></li>
					<li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a></li>
					<li><a class="close-link"><i class="fa fa-close"></i></a></li>
				</ul>
				<div class="clearfix"></div>
			</div>
			<div class="x_itemCount" style="display: none;">
				<div class="image_not_found">
					<i class="fa fa-folder-open"></i>
					<p>
						<label class="x_count"></label> Learning Resources
					</p>
				</div>
			</div>
				<c:choose>
						<c:when test="${not empty allForums}">
			<div class="x_content">
				<div class="table_overflow">
					<table class="table">
						<thead>
							<tr>
								<th>Name</th>
								<th>Description</th>
								<th colspan="2">Action</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="content" items="${allContent}" varStatus="status">
								<tr data-tt-id="${content.id}"
									data-tt-parent-id="${content.parentContentId}">

									<td><c:out value="${content.contentName}" /></td>

									<td><c:out value="${content.contentDescription}" /></td>
									<td><c:if test="${content.contentType == 'Folder' }">
											<i class="fa lms-folder-o fa-lg"
												style="background: #E6CB47; margin-right: 5px"></i>

											<c:url value="/getContentUnderAPathForFaculty"
												var="navigateInsideFolder">
												<c:param name="courseId" value="${content.courseId}" />
												<c:param name="acadMonth" value="${content.acadMonth}" />
												<c:param name="acadYear" value="${content.acadYear}" />
												<c:param name="folderPath" value="${content.filePath}" />
												<c:param name="parentContentId" value="${content.id}" />
											</c:url>
											<a href="${navigateInsideFolder}"><c:out
													value="${content.contentName}" /></a>
										</c:if> <c:if test="${content.contentType == 'File' }">


											<a href="downloadFile?filePath=${content.filePath}"><i
												class="fa fa-cloud-download"></i> Download</a>
										</c:if> <c:if test="${content.contentType == 'Link' }">
											<i class="fa fa-link fa-lg" style="margin-right: 5px"></i>
											<a href="${content.linkUrl}" target="_blank"> <c:out
													value="${content.contentName}" />
											</a>
										</c:if></td>

									<td><a href="#">View</a></td>
								</tr>
							</c:forEach>

						</tbody>
					</table>
				</div>
			</div>
			</c:when>
					<c:otherwise>
					<div class="image_not_found">
								<i class="fa fa-folder-open "></i>
								<p>No Content Data </p>
						</div>
					</c:otherwise>
					</c:choose>
						
		</div>
	</div>
</div>