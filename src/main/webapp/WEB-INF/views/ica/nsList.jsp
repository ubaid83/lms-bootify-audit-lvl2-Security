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
					<li class="breadcrumb-item active" aria-current="page">NC List</li>
				</ol>
			</nav>

			<jsp:include page="../common/alert.jsp" />

			<!-- Input Form Panel -->
			<div class="card bg-white border">
				<div class="card-body">
					<h5 class="text-center pb-2 border-bottom">Non Credit Ica List</h5>



					<div class="table-responsive mt-3 mb-3 testAssignTable">
						<table class="table table-striped table-hover">
							<thead>
								<tr>
									<th scope="col">Sl. No.</th>
									<th scope="col">Acad Year</th>
									<th scope="col">Program</th>
									<th scope="col">Module Name</th>
									<th scope="col">ICA Name</th>
									<th scope="col">Acad Session</th>
									<th scope="col">Submission Status</th>
									<th scope="col">Publish To Portal</th>
									<th scope="col">Campus</th>
									<th scope="col">Actions</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="ns" items="${nsList}" varStatus="status">
									<tr>
										<td><c:out value="${status.count}" /></td>
										<td><c:out value="${ns.acadYear}" /></td>

										<td><c:out value="${ns.programName}" /></td>


										<td><c:out value="${ns.moduleDescription}" /></td>


										<td><c:out value="${ns.icaName}" /></td>
										<td><c:out value="${ns.acadSession}" /></td>
										<c:choose>
											<c:when test="${ns.isSubmitted eq 'Y'}">
												<td><c:out value="Submitted" /></td>
											</c:when>
											<c:otherwise>
												<td><c:out value="Not Submitted" /></td>
											</c:otherwise>
										</c:choose>
										<td><c:choose>
												<c:when test="${ns.isSubmitted eq 'Y'}">
													<c:choose>
														<c:when test="${ns.isPublished eq 'Y'}">
												Ns Published
												</c:when>
														<c:otherwise>
															<a href="#" id="publish${ns.id}" class="showClass"
																onclick="publishNcIca('${ns.id}')"> Publish </a>
															<p id="published${ns.id}" class="showClass"
																style="float: center; display: none;">Ns Published</p>

														</c:otherwise>
													</c:choose>
												</c:when>
												<c:otherwise>
												NA
												</c:otherwise>
											</c:choose></td>


										<td><c:out value="${ns.campusName}" /></td>


										<td><c:url value="deleteNs" var="deleteurl">
												<c:param name="id" value="${ns.id}" />
											</c:url> <%-- <c:url value="addNonCreditIcaForm" var="editurl">
												<c:param name="id" value="${ns.id}" />
											</c:url>  --%> <c:url value="uploadNS" var="addnsFile">
												<c:param name="ncIcaId" value="${ns.id}" />
											</c:url> <sec:authorize access="hasRole('ROLE_ADMIN')">
												<a href="${deleteurl}" title="Delete"
													onclick="return confirm('Are you sure you want to delete this record?')"><i
													class="fas fa-trash-alt fa-lg text-danger"></i></a>

												<%-- <a href="${editurl}" title="Edit"><i
													class="fas fa-edit fa-lg text-primary"></i></a> --%>

												<%-- <c:if test="${nsId eq null}">
													<a href="${addnsFile}" title="Add Ns File"><i
														class="fa fa-plus fa-lg"></i></a>

												</c:if> --%>
												
												<c:choose>
													<c:when
														test="${ns.isSubmitted eq 'Y' && ns.isPublished eq 'Y'}">

													</c:when>
													<c:otherwise>
														<c:if test="${nsId eq null}">
															<a href="${addnsFile}" title="Add Ns File" id="addIcon"><i
																class="fa fa-plus fa-lg"></i></a>
														</c:if>
													</c:otherwise>
												</c:choose>
												
												 <c:if test="${ns.isSubmitted eq 'Y'}">
													<a href="downloadUpdatedGrade?icaId=${ns.id}"><i
														class="fa fa-download"></i></a>
												</c:if> 

											</sec:authorize></td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
			</div>



			<!-- /page content: END -->

		</div>

		<!-- SIDEBAR START -->

		<!-- SIDEBAR END -->

		<jsp:include page="../common/newAdminFooter.jsp" />
		<script src="https://unpkg.com/sweetalert/dist/sweetalert.min.js"></script>

		<script>
			function publishNcIca(icaId) {
				console.log('publish Ica called');

				//evvar testId = 'publish';

				var id = icaId;

				swal(
						{
							title : 'Are you sure you want to publish? once published you cannot revert it.',
							// text: "It will permanently deleted !",
							//type: 'warning',
							//icon : 'success',
							showCancelButton : true,
							confirmButtonColor : '#3085d6',
							cancelButtonColor : '#d33',
						// confirmButtonText: 'Yes, delete it!'
						})
						.then(
								function() {
									$
											.ajax({
												type : 'GET',
												url : '${pageContext.request.contextPath}/publishOneNCIca?'
														+ 'id=' + id,
												success : function(data) {

													$(this)
															.find('span')
															.addClass(
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
													$('#addIcon').hide();
												}

											});

								});

			}
		</script>