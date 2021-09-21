<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<script type="text/javascript" src="js/script.js"></script>

<script
	src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"
	type="text/javascript"></script>

<div class="card bg-white border">
	<div class="card-body">
			<div class="text-center">
				<h5>Learning Resources</h5>
				<%-- <ul class="nav navbar-right panel_toolbox">
					<li><sec:authorize
							access="hasAnyRole('ROLE_FACULTY','ROLE_DEAN')">
							<a
								href="<c:url value="getContentUnderAPathForFaculty?courseId=${courseId}" />"><span>View
									All</span></a>
						</sec:authorize> <sec:authorize access="hasAnyRole('ROLE_STUDENT')">
							<a
								href="<c:url value="studentContentList?courseId=${courseId}" />"><span>View
									All</span></a>
						</sec:authorize></li>
					
				</ul> --%>
				
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
				<c:when test="${not empty allContent}">
						<div class="table-responsive testAssignTable">
							<table class="table table-hover table-striped">
								<thead>
									<tr>
										<th>Name</th>
										<th>Description</th>
										<th>Action</th>
										<th></th>
									</tr>
								</thead>
								<tbody>
									<c:forEach var="content" items="${allContent}"
										varStatus="status">
										<tr data-tt-id="${content.id}"
											data-tt-parent-id="${content.parentContentId}">

											<td><c:out value="${content.contentName}" /></td>

											<td><c:out value="${content.contentDescription}" /></td>


											<td><sec:authorize access="hasAnyRole('ROLE_STUDENT')">
													<c:if test="${content.contentType == 'Folder' }">
														<i class="fas fa-folder-open fa-lg"
															style="background: #E6CB47; margin-right: 5px"></i>

														<c:url value="/getContentUnderAPathForStudent"
															var="navigateInsideFolder">
															<c:param name="courseId" value="${content.courseId}" />
															<c:param name="acadMonth" value="${content.acadMonth}" />
															<c:param name="acadYear" value="${content.acadYear}" />
															<c:param name="folderPath" value="${content.filePath}" />
															<c:param name="parentContentId" value="${content.id}" />
														</c:url>
														<a href="${navigateInsideFolder}" class="clickedFolder"
															id="folder${content.id}"><c:out
																value="${content.contentName}" /></a>
													</c:if>

												</sec:authorize> <sec:authorize access="hasAnyRole('ROLE_FACULTY')">
													<c:if test="${content.contentType == 'Folder' }">
														<i class="fas fa-folder-open fa-lg"
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
													</c:if>

												</sec:authorize> <c:if test="${content.contentType == 'File' }">


													<sec:authorize access="hasRole('ROLE_STUDENT')">
														<c:choose>
															<c:when
																test="${fn:containsIgnoreCase(content.filePath, '.pptx') && fn:containsIgnoreCase(content.filePath, '.zip')}">
																<a
																	href="downloadScormContentFile?id=${content.id}"
																	class="clickedFile" id="file${content.id}"> <i
																	class="fas fa-download"></i>View
																</a>
															</c:when>
															<c:otherwise>
																<a href="downloadFile?filePath=${content.filePath}"
																	class="clickedFile" id="file${content.id}"> <i
																	class="fas fa-download"></i>Download
																</a>
															</c:otherwise>
														</c:choose>
													</sec:authorize>
													<sec:authorize access="hasRole('ROLE_FACULTY')">
														<c:choose>
															<c:when
																test="${fn:containsIgnoreCase(content.filePath, '.pptx') && fn:containsIgnoreCase(content.filePath, '.zip')}">
																<a
																	href="downloadScormContentFile?id=${content.id}"
																	class="clickedFile" id="file${content.id}"> <i
																	class="fas fa-download"></i>View
																</a>
															</c:when>
															<c:otherwise>
																<a href="downloadFile?filePath=${content.filePath}"
																	class="clickedFile" id="file${content.id}">
																	<i class="fas fa-download"></i>&nbsp;Download
																</a>
															</c:otherwise>
														</c:choose>
													</sec:authorize>
												</c:if> <c:if test="${content.contentType == 'Link' }">
													<i class="fa fa-link fa-lg" style="margin-right: 5px"></i>
													<sec:authorize access="hasRole('ROLE_STUDENT')">
														<a href="${content.linkUrl}" target="_blank"
															class="clickedLink" id="link${content.id}"> <c:out
																value="${content.contentName}" />
														</a>
													</sec:authorize>
													<sec:authorize access="hasRole('ROLE_FACULTY')">
														<a href="${content.linkUrl}" target="_blank"> <c:out
																value="${content.contentName}" />
														</a>
													</sec:authorize>
												</c:if></td>

											

											<td><sec:authorize access="hasAnyRole('ROLE_STUDENT')">
													<c:if test="${content.contentType == 'Folder' }">
														<!-- <i class="fa lms-folder-o fa-lg"
												style="background: #E6CB47; margin-right: 5px"></i> -->

														<c:url value="/getContentUnderAPathForStudent"
															var="navigateInsideFolder">
															<c:param name="courseId" value="${content.courseId}" />
															<c:param name="acadMonth" value="${content.acadMonth}" />
															<c:param name="acadYear" value="${content.acadYear}" />
															<c:param name="folderPath" value="${content.filePath}" />
															<c:param name="parentContentId" value="${content.id}" />
														</c:url>
														<a href="${navigateInsideFolder}" class="clickedFolder"
															id="folder${content.id}">View</a>
													</c:if>
													

												</sec:authorize> <sec:authorize access="hasAnyRole('ROLE_FACULTY')">
													<c:if test="${content.contentType == 'Folder' }">
														<!-- 	<i class="fa lms-folder-o fa-lg"
												style="background: #E6CB47; margin-right: 5px"></i> -->

														<c:url value="/getContentUnderAPathForFaculty"
															var="navigateInsideFolder">
															<c:param name="courseId" value="${content.courseId}" />
															<c:param name="acadMonth" value="${content.acadMonth}" />
															<c:param name="acadYear" value="${content.acadYear}" />
															<c:param name="folderPath" value="${content.filePath}" />
															<c:param name="parentContentId" value="${content.id}" />
														</c:url>
														<a href="${navigateInsideFolder}" class="clickedFolder"
														id="folder${content.id}">View</a>
													</c:if>
													

												</sec:authorize> <%-- 
										 <c:if test="${content.contentType == 'File' }">


											<a href="downloadFile?filePath=${content.filePath}"><i
												class="fa fa-cloud-download"></i> Download</a>
										</c:if> <c:if test="${content.contentType == 'Link' }">
											<i class="fa fa-link fa-lg" style="margin-right: 5px"></i>
											<a href="${content.linkUrl}" target="_blank"> <c:out
													value="${content.contentName}" />
											</a>
										</c:if> --%></td>
									<%-- 	<c:if test="${content.contentType != 'Folder'}">
													<td>No Folder</td>
													</c:if>
 --%>






											<%--  --%>
										</tr>
									</c:forEach>

								</tbody>
							</table>
						</div>
				</c:when>
				<c:otherwise>
					<div class="text-center mt-3">
						<div class="image_not_found">
							<i class="fas fa-folder-open "></i>
							<p>No Content Data</p>
						</div>
					</div>
				</c:otherwise>
			</c:choose>
	</div>
</div>

<script type="text/javascript">
	$(document)
			.ready(
					function() {
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
																	.find(
																			'span')
																	.addClass(
																			"icon-success");

														}

													});

										});
					});
</script>
<script type="text/javascript">
	$(document)
			.ready(
					function() {
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
																	.find(
																			'span')
																	.addClass(
																			"icon-success");

														}

													});

										});
					});
</script>
<script type="text/javascript">
	$(document)
			.ready(
					function() {
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
																	.find(
																			'span')
																	.addClass(
																			"icon-success");

														}

													});

										});
					});
</script>