<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>



<jsp:include page="../common/newDashboardHeader.jsp" />

<div class="d-flex" id="facultyAssignmentPage">
	<jsp:include page="../common/newAdminLeftNavBar.jsp" />
	<jsp:include page="../common/rightSidebarAdmin.jsp" />


	<!-- DASHBOARD BODY STARTS HERE -->

	<div class="container-fluid m-0 p-0 dashboardWraper">

		<jsp:include page="../common/newAdminTopHeader.jsp" />

		<!-- SEMESTER CONTENT -->
		<div class="col-lg-12 col-md-12 col-sm-12 dashboardBody p-5">

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
					<li class="breadcrumb-item active" aria-current="page">ICA
						Query List From Students</li>
				</ol>
			</nav>

			<jsp:include page="../common/alert.jsp" />

			<!-- Input Form Panel -->
			<div class="card bg-white border">
				<div class="card-body">
					<h5 class="text-center pb-2 border-bottom">ICA Query List From
						Students</h5>

					<c:if test="${icaQueries.size()>0}">
					<a
								href="${pageContext.request.contextPath}/downloadIcaRaiseQueryReport"><font
								color="red">Download Student ICA Queries</font></a>
					</c:if>

					<div class="table-responsive mt-3 mb-3 testAssignTable">
						<table class="table table-striped table-hover">
							<thead>
								<tr>
									<th scope="col">Sl. No.</th>
									<th scope="col">ICA Name</th>
									<th scope="col">Component Name</th>
									<th scope="col">Module Name</th>
									
									<th scope="col">Assigned Faculty</th>

									<th scope="col">Acad Year</th>
									<th scope="col">Acad Session</th>
									<th scope="col">Current Date</th>
									<th scope="col">Approval File</th>
									<th scope="col">Actions</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="ica" items="${icaQueries}" varStatus="status">
									<tr>
										<form id="ica${ica.id}" action="approveIcaForReeval"
											method="post" enctype="multipart/form-data">
											<input type="hidden" name="icaId" value="${ica.id}" />
											<c:if test="${ica.componentId ne null}">
											<input type="hidden" name="compId" value="${ica.componentId}" />
											</c:if>
											<td><c:out value="${status.count}" /></td>
											<td><c:out value="${ica.icaName}" /></td>
											<td><c:out value="${ica.componentName}" /></td>
											<%-- <td><c:out value="${course.abbr}" /></td> --%>
											<c:if test="${ica.courseName ne null}">
												<td><c:out value="${ica.moduleName}(${ica.courseName})" /></td>
											</c:if>
											<c:if test="${ica.courseName eq null}">
												<td><c:out value="${ica.moduleName}" /></td>
											</c:if>
											<td><c:out value="${ica.facultyName}" /></td>
										<td><c:out value="${ica.acadYear}" /></td>




										<td><c:out value="${ica.acadSession}" /></td>

										<td><c:out value="${currentDate}" /></td>







										<c:choose>
											<c:when test="${ica.isApproved eq 'Y'}">
												<td><a href="downloadIcaFile?id=${ica.icaQueryId}">Downlaod
														File</a></td>
												<td>
													<button class="btn btn-sm btn-success disabled" id="getIdN"
														disabled="disabled">Approved</button>
												</td>

											</c:when>
											<c:otherwise>
												<td><input id="file" name="file" type="file"
													class="form-control" multiple="multiple"
													required="required" /></td>
												<td>
													<button class="btn btn-sm btn-success" id="getIdN"
														value="${ica.id}" type="submit">Approve</button>
												</td>
											</c:otherwise>
										</c:choose>

										<%-- <c:choose>
										<c:when test="${ica.isPublished eq 'Y'}">
											<td
																					
												<a href="#" id="publish${ica.id}" class="showClass" style="float: right; display: none;"
																					onclick='document.getElementById(this.id).removeAttribute("href");'>
																					Publish </a></td>
										</c:when>
										<c:otherwise>
										
										</c:otherwise>
										</c:choose>
										 --%>




										</form>
									</tr>


								</c:forEach>
							</tbody>
						</table>
					</div>

				</div>
			</div>

			<div class="card bg-white border">
				<div class="card-body">
					<h5 class="text-center pb-2 border-bottom">Approve All ICA</h5>
					<form id="icaAll" action="approveAllIcaForReeval" method="post"
						enctype="multipart/form-data">
						<div class="col-lg-4 col-md-4 col-sm-12 mt-3 position-relative">
							<input id="file" name="file" type="file" class="form-control"
								multiple="multiple" required="required" />
						</div>
						<div class="col-lg-4 col-md-4 col-sm-12 mt-3 position-relative">
							<c:if test="${isAllApproved eq 'false'}">
								<button class="btn btn-dark mt-2" id="getId" type="submit">Approve
									All</button>
							</c:if>
							<c:if test="${isAllApproved eq 'true'}">
								<button class="btn btn-dark mt-2 disabled" id="getId"
									disabled="disabled">Approved</button>
							</c:if>
						</div>
					</form>
				</div>
			</div>



			<!-- /page content: END -->

		</div>

		<!-- SIDEBAR START -->

		<!-- SIDEBAR END -->

		<jsp:include page="../common/newAdminFooter.jsp" />



		<!-- 	<script>
			$(".showClass")
					.click(
							function() {
								console
										.log("called ........................................................000000.");
								//$(this).css('color', 'black');
								var testId = $(this).attr("id");

								var id = testId.substr(7, testId.length);
								console.log(id);
								$
										.ajax({
											type : 'GET',
											url : '${pageContext.request.contextPath}/publishOneIca?'
													+ 'id=' + id,
											success : function(data) {

												$(this).find('span').addClass(
														"icon-success");
												var str1 = "published";
												var str2 = str1.concat(id);
												$('#' + str2).css({
													'display' : 'block'
												});
												//$('#' + str2).show();
												var str3 = "publish";
												var str4 = str3.concat(id);

												$('#' + str4).hide();

											}

										});

							});
		</script> -->