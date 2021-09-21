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

<%
if(session.getAttribute("studentFeedbackActive")!= "Y"){
%>
	<jsp:include page="../common/newLeftNavbar.jsp" />
	<jsp:include page="../common/newLeftSidebar.jsp" />

<%
}
%>
	<jsp:include page="../common/rightSidebar.jsp" />


	<!-- DASHBOARD BODY STARTS HERE -->

	<div class="container-fluid m-0 p-0 dashboardWraper">

		<jsp:include page="../common/newTopHeader.jsp" />

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

							<li class="breadcrumb-item active" aria-current="page">View
								Feedback</li>
						</ol>
					</nav>

					<jsp:include page="../common/alert.jsp" />


					<!-- Input Form Panel -->
					<div class="card bg-white border">
						<div class="card-body">
							<div class="row">
								<div class="col-xs-12 col-sm-12">
									<div class="x_panel">
										<div class="x_title">
											<h2>Feedback</h2>
											<div class="clearfix"></div>
										</div>
										<div class="x_content">
											<div class="table-responsive testAssignTable">
												<table class="table table-hover" id="viewFeedbackTable">
													<thead>
														<tr>
															<th>Name</th>
															<th>Start Date</th>
															<th>End Date</th>
															<th>Status</th>

														</tr>
													</thead>
													<tbody>


														<c:forEach var="feedback" items="${page.pageItems}"
															varStatus="status">

															<tr>

																<th><a data-toggle="collapse"
																	href="#feeback_acoording${status.count}"><i
																		class="pluse_ellipse fa fa-plus"></i> <c:out
																			value="${feedback.feedbackName}" /></a></th>
																<td><c:out value="${feedback.startDate}" /></td>
																<td><c:out value="${feedback.endDate}" /></td>
																<td><c:if
																		test="${feedback.eachfeedbackCompletionStatus =='Y' }">
																		<i class="check_ellipse fa fa-check bg-green"></i>Submitted
																</c:if> <c:if
																		test="${feedback.eachfeedbackCompletionStatus =='N' }">
																		<i class="check_ellipse fa fa-check"></i>	 Not Submitted
						            							</c:if></td>


															</tr>
															<tr id="feeback_acoording${status.count}"
																class="panel-collapse collapse">
																<td colspan="4"><c:if
																		test="${feedback.eachfeedbackCompletionStatus =='N' }">
																		<div class="feedback_des_detail">
																			<!-- <p class="font_weight_bold">Description Details</p>
																			<ul class="bullet1">
																				<li>This feedback is for administrative
																					facilities of College.</li>
																				<li>Please rate all services as per your
																					experience.</li>
																				<li>Your feedback is important to us to improve
																					services.</li>
																				<li>Anonymity of Feedback will be maintained</li>
																			</ul> -->
																			<div class="text-center">
																				<%-- <a
																					href="${pageContext.request.contextPath}/startStudentFeedback?feedbackId=${feedback.id}"
																					class="btn btn-primary">Provide Feedback</a> --%>

																				<a
																					href="${pageContext.request.contextPath}/giveStudentFeedback?feedbackId=${feedback.id}"
																					class="btn btn-info">Provide Feedback</a>
																			</div>
																		</div>
																	</c:if> <c:if
																		test="${feedback.eachfeedbackCompletionStatus =='Y' }">

																		<table class="table table-hover table_feedback1">

																			<thead>
																				<tr>
																					<td><b>Feedback Details</b></td>
																					<td>Submitted on <b>${feedback.lastModifiedDate}</b></td>
																				</tr>
																			</thead>

																			<tr>
																				<td colspan="2">
																					<div class="text-center">
																						<%-- <a type="button" class="btn btn-primary"
																							href="${pageContext.request.contextPath}/startStudentFeedback?feedbackId=${feedback.id}">Edit
																							Feedback</a> --%>

																						<a type="button" class="btn btn-info"
																							href="${pageContext.request.contextPath}/giveStudentFeedback?feedbackId=${feedback.id}">Edit
																							Feedback</a>

																					</div>
																				</td>
																			</tr>

																		</table>
																	</c:if></td>
															</tr>






														</c:forEach>
													</tbody>
												</table>
											</div>
										</div>
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


