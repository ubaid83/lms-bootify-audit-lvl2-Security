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

<jsp:include page="../common/newDashboardHeader.jsp" />

<div class="d-flex dataTableBottom" id="facultyAssignmentPage">
	<jsp:include page="../common/newLeftNavBarFaculty.jsp" />
	<jsp:include page="../common/newLeftSidebarFaculty.jsp" />
	<jsp:include page="../common/rightSidebarFaculty.jsp" />

	<!-- DASHBOARD BODY STARTS HERE -->

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
								Portal Feedback</li>
						</ol>
					</nav>

					<jsp:include page="../common/alert.jsp" />


					<!-- Input Form Panel -->
					<div class="card bg-white border">
						<div class="card-body">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">

									<div class="x_title">
										<h2>
											Portal Feedback |
											<%=session.getAttribute("dbName")%></h2>
										<ul class="nav navbar-right panel_toolbox">
											<li><a class="collapse-link"><i
													class="fa fa-chevron-up"></i></a></li>
										</ul>
										<div class="clearfix"></div>
									</div>

									<div class="x_content">
										<form:form action="viewPortalFeedbacks" method="post"
											modelAttribute="portalFeedback">
											<form:hidden path="url" />
											<form:hidden path="dbUsername" />
											<form:hidden path="password" />


											<div class="table-responsive">
												<!-- <button id="btn-show-all-children" type="button">Expand
													All</button>
												<button id="btn-hide-all-children" type="button">Collapse
													All</button> -->
												<table class="table  table-hover display" id="example">
													<thead>
														<tr>
															<th>Expand/Collapse</th>
															<th>SAP ID <i class="fa fa-sort" aria-hidden="true"
																style="cursor: pointer"></i></th>
															<th>User Role <i class="fa fa-sort"
																aria-hidden="true" style="cursor: pointer"></i></th>
															<th>User Name <i class="fa fa-sort"
																aria-hidden="true" style="cursor: pointer"></i></th>
															<th>Reply Status</th>
															<th>Created Date</th>
															<!-- <th>Action</th> -->


														</tr>
													</thead>
													<tfoot>
														<tr>
															<th></th>
															<th>SAP ID</th>
															<th>User Role</th>
															<th>User Name</th>
															<th>Reply Status</th>
															<th>Created Date</th>



														</tr>
													</tfoot>
													<tbody>

														<c:forEach var="student" items="${feedbackList}"
															varStatus="status">
															<tr>
																<td class="details-control" id="${student.id }"></td>


																<td><c:out value="${student.username}" /></td>
																<td><c:out value="${student.userRole}" /></td>
																<td><c:out
																		value="${student.firstname} ${student.lastname}" /></td>
																<c:if test="${student.repliedByAdmin eq 'NOT REPLIED'}">
																	<td style="color: red;"><c:out
																			value="${student.repliedByAdmin}" /></td>
																</c:if>

																<c:if test="${student.repliedByAdmin eq 'REPLIED'}">
																	<td><c:out value="${student.repliedByAdmin}" /></td>
																</c:if>
																<td><c:out value="${student.createdOn}" /></td>

																<%-- <td><c:url value="viewEachPortalFeedback"
																		var="detailsUrl">

																		<c:param name="portalFeedbackId" value="${student.id}" />
																		<c:param name="uname" value="${student.username}" />
																	</c:url> <a href="${ detailsUrl}" title="Details" class="view"
																	id="${student.id}">View Discussion</a>&nbsp;</td> --%>




															</tr>


														</c:forEach>

													</tbody>

												</table>

												<!-- <div class="row">

													<div class="col-sm-8 column">
														<div class="form-group">
															<button id="cancel" class="btn btn-danger" type="button"
																formaction="homepage" formnovalidate="formnovalidate"
																onclick="history.go(-1);">Back</button>
														</div>
													</div>
												</div> -->
											</div>
										</form:form>
									</div>
								</div>
							</div>
						</div>
					</div>

					<!-- Results Panel -->




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

						var clicked_id = event.srcElement.id;

						var id = '#replyText' + clicked_id
						// alert(id);
						$(id).show();

					}
					function closeFunction() {
						// alert(event.srcElement.id);
						var clicked_id = event.srcElement.id;

						var id = '#replyText' + clicked_id
						$(id).hide();

					}
				</script>