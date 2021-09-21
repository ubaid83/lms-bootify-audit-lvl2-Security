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

<div class="d-flex dataTableBottom paddingFix1" id="discForum">
	<sec:authorize access="hasAnyRole('ROLE_ADMIN', 'ROLE_FACULTY')">
	
	<!-- hasRole('ROLE_FACULTY') -->
		<jsp:include page="../common/newLeftNavBarFaculty.jsp"/>
		<jsp:include page="../common/newLeftSidebarFaculty.jsp"/>
		<jsp:include page="../common/rightSidebarFaculty.jsp"/>
	</sec:authorize>
	<sec:authorize access="hasRole('ROLE_STUDENT')">
		<jsp:include page="../common/newLeftNavbar.jsp" />
		<jsp:include page="../common/newLeftSidebar.jsp" />
		<jsp:include page="../common/rightSidebar.jsp" />
	</sec:authorize>

	<!-- DASHBOARD BODY STARTS HERE -->

	<div class="container-fluid m-0 p-0 dashboardWraper">

		<sec:authorize access="hasAnyRole('ROLE_ADMIN', 'ROLE_FACULTY')">
			<jsp:include page="../common/newTopHeaderFaculty.jsp" />
		</sec:authorize>
		<sec:authorize access="hasRole('ROLE_STUDENT')">
			<jsp:include page="../common/newTopHeader.jsp" />
		</sec:authorize>

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
							<li class="breadcrumb-item active" aria-current="page">Discussion Forum</li>
						</ol>
					</nav>

					<jsp:include page="../common/alert.jsp" />


					<!-- Input Form Panel -->
					<div class="card bg-white border">
						<div class="card-body">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">

									<div class="x_title">
										<h5>Forums</h5>
										
									</div>

									<div class="x_content">
										<form:form action="replyToQuestionForm" method="post"
											modelAttribute="forum" enctype="multipart/form-data">


											<form:input path="courseId" type="hidden" />
											<div class="row">
												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<form:label path="courseId" for="courseId">Course</form:label>
														<form:select id="courseIdForForum" path="courseId"
															class="form-control">
															<form:option value="">Select Course</form:option>
															<c:forEach var="course" items="${allCourses}"
																varStatus="status">
																<form:option value="${course.id}">${course.courseName}</form:option>
															</c:forEach>
														</form:select>
													</div>
												</div>
											</div>
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
										<h5>Forums | ${fn:length(allForums)} Records found</h5>
										
									</div>
									<div class="x_itemCount" style="display: none;">
										<div class="image_not_found">
											<i class="fa fa-newspaper-o"></i>
											<p>
												<label class=""></label>${fn:length(allForums)} Forums
											</p>
										</div>
									</div>
									<div class="x_content">
										<form:form action="replyToQuestionForm" method="post"
											modelAttribute="forum" enctype="multipart/form-data">
											<form:input path="courseId" type="hidden" />
											<div class="table-responsive testAssignTable">
												<table class="table table-striped table-hover">
													<thead>
														<tr>
															<th>Forum Topic</th>

														</tr>
													</thead>
													<tbody>

														<c:forEach var="forum" items="${allForums}"
															varStatus="status">
															<input value="${forum.id}" type="hidden" />
															<tr>
																<td><c:url value="replyToQuestionForm"
																		var="detailsUrl">
																		<c:param name="id" value="${forum.id}" />
																	</c:url> <a href="${detailsUrl}"><h4
																			style="font-size: 20px;">${forum.topic}</h4></a> <br>
																	<div class="well"
																		style="color: white; padding: 8px; background-color: #337AB7;">

																		<i class="fa fa-user" aria-hidden="true"></i>
																		${forum.firstname} ${forum.lastname} | Replies :
																		${replyCount[status.count-1]} | Students Involved :
																		${forum.studentsInvolved}
																	</div> <%-- <label for="" value="${forum.id}"
																				style="font-size: 12px;">Replies : </label>

																			${replyCount[status.count-1]}
 --%></td>


															</tr>
														</c:forEach>

													</tbody>
												</table>
											</div>
										</form:form>
									</div>
								</div>
							</div>
						</div>
					</div>


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
					$("#courseIdForForum")
							.on(
									'change',
									function() {

										//alert("Onchange Function called!");
										var selectedValue = $(this).val();
										window.location = '${pageContext.request.contextPath}/viewForum?courseId='
												+ encodeURIComponent(selectedValue);
										return false;
									});
				</script>