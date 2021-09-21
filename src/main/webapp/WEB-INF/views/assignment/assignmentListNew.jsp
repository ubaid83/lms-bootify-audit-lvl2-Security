
<%-- <jsp:include page="../common/header.jsp" /> --%>
<%@page import="com.spts.lms.beans.forum.Forum"%>
<%@page import="com.spts.lms.beans.calender.Calender"%>
<%@page import="java.util.List"%>
<%@ page import="com.spts.lms.web.utils.Utils"%>
<%-- <jsp:include page="../common/NewHeader1.jsp" /> --%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>


<!doctype html>
<html lang="en">

<jsp:include page="../common/css.jsp" />

<body class="nav-md footer_fixed  ">


	<div class="container body">
		<div class="main_container">


			  <jsp:include page="../common/leftSidebar.jsp" />
     			<jsp:include page="../common/topHeader.jsp" />



			<!-- page content -->
			<div class="right_col" role="main">

				<div class="dashboard_container">

					<div class="dashboard_height" id="main">

						<div class="dashboard_container_spacing">
							<div class="breadcrumb">
							<jsp:include page="../common/alert.jsp" />
								<a
									href="${pageContext.request.contextPath}/viewCourse?id=${courseId}">My
									Courses</a> <i class="fa fa-angle-right"></i> <a
									href="${pageContext.request.contextPath}/viewCourse?id=${courseId}"><c:out
										value="${courseRecord.courseName}" /> </a> <i
									class="fa fa-angle-right"></i> Assignments
							</div>
							
							



							<div class="row">
								<div class="col-sm-12 col-xs-12">
									<div class="x_panel">
										<div class="x_title">
											<h2>Assignments</h2>
											<ul class="nav navbar-right panel_toolbox">
												<li><a href="<c:url value="assignmentList" />"><span>View
															All</span></a></li>
												<li><a class="collapse-link"><i
														class="fa fa-chevron-up"></i></a></li>
												<li><a class="close-link"><i class="fa fa-close"></i></a></li>
											</ul>
											<div class="clearfix"></div>
										</div>
										<div class="x_itemCount" style="display: none;">
											<div class="image_not_found">
												<i class="fa fa-newspaper-o"></i>
												<p>
													<label class="x_count"></label> Assignments
												</p>
											</div>
										</div>

										<div class="x_content">
											<div class="table-responsive">
												<c:choose>
													<c:when test="${ not empty assignments}">
														<table id="datatable-responsive"
															class="table assignment_table dt-responsive nowrap table-hover"
															width="100%">
															<thead>
																<tr>

																	<th>Name</th>
																	<th>Term</th>
																	<th>End Date</th>
																	<th>Points</th>
																	<th>Status</th>
																	<th colspan="3">Action</th>
																</tr>
															</thead>
															<tbody>
																<c:forEach var="assignment" items="${assignments}"
																	varStatus="status">
																	<tr>
																		<th><c:url value="assignmentDetails"
																				var="submiturl">
																				<c:param name="id" value="${assignment.id}" />
																			</c:url> <a href="${submiturl}" title="Submit Assignment"><c:out
																					value="${assignment.assignmentName}" /></a></th>
																		<td><c:out
																				value="${assignment.acadYear}- ${assignment.acadMonth} " /></td>
																		<td><c:out value="${assignment.endDate}" /></td>
																		<td><c:out value="${assignment.maxScore}" /></td>
																		<td><c:if
																				test="${assignment.submissionStatus eq 'Y' }">
																				<i class="check_ellipse fa fa-check bg-green"></i>
																				<c:out value="Submitted" />
																			</c:if> <c:if test="${assignment.submissionStatus ne 'Y' }">
																				<i class="check_ellipse fa fa-check"></i>
																				<c:out value="Not Submitted" />
																			</c:if></td>
																		<td><a href="assignments-detail.html"><i
																				class="fa fa-newspaper-o"></i> info</a></td>
																		<td><c:url value="downloadFile" var="downloadurl">
																				<c:param name="id"
																					value="${assignment.id}" />
																			</c:url> <a href="${downloadurl}" title="Details"><i
																				class="fa fa-cloud-download"></i> Download</a></td>
																		<td><sec:authorize
																				access="hasRole('ROLE_STUDENT')">
																				<c:url value="assignmentDetails" var="submiturl">
																					<c:param name="id" value="${assignment.id}" />
																				</c:url>
																				<a href="${submiturl}" title="Submit Assignment"><i
																					class="check_ellipse fa fa-location-arrow"></i>
																					Submit</a>

																			</sec:authorize> <sec:authorize access="hasRole('ROLE_FACULTY')">
																				<c:url value="assignmentDetails"
																					var="editAssignmentUrl">
																					<c:param name="id" value="${assignment.id}" />
																				</c:url>
																				<a href="${editAssignmentUrl}"
																					title="Submit Assignment">Edit Assignment</a>&nbsp;
														</sec:authorize></td>
																	</tr>
																</c:forEach>
																<!--  <tr>
                                                        <th><a href="assignments-detail.html">Concepts of Corporate Laws</a></th>
                                                        <td>Jul-2017</td>
                                                        <td>17-Aug-17</td>
                                                        <td>20</td>
                                                        <td><i class="check_ellipse fa fa-check bg-green"></i> Submitted</td>
                                                        <td><a href="assignments-detail.html"><i class="fa fa-newspaper-o"></i> info</a></td>
                                                        <td><a href="assignments-detail.html"><i class="fa fa-cloud-download"></i> Download</a></td>
                                                        <td><a href="assignments-detail.html"><i class="check_ellipse fa fa-location-arrow"></i> Submit</a></td>
                                                      </tr>
                                                      <tr>
                                                        <th><a href="assignments-detail.html">Theory of Business Policies</a></th>
                                                        <td>Jul-2017</td>
                                                        <td>18-Sep-17</td>
                                                        <td>30</td>
                                                        <td><i class="check_ellipse fa fa-check bg-green"></i> Submitted</td>
                                                        <td><a href="assignments-detail.html"><i class="fa fa-newspaper-o"></i> info</a></td>
                                                        <td><a href="assignments-detail.html"><i class="fa fa-cloud-download"></i> Download</a></td>
                                                        <td><a href="assignments-detail.html"><i class="check_ellipse fa fa-location-arrow"></i> Submit</a></td>
                                                      </tr>
                                                      <tr>
                                                        <th><a href="assignments-detail.html">Working of Government Policies</a></th>
                                                        <td>Jul-2017</td>
                                                        <td>24-Aug-17</td>
                                                        <td>30</td>
                                                        <td><i class="check_ellipse fa fa-check"></i> Not Submitted</td>
                                                        <td><a href="assignments-detail.html"><i class="fa fa-newspaper-o"></i> info</a></td>
                                                        <td><a href="assignments-detail.html"><i class="fa fa-cloud-download"></i> Download</a></td>
                                                        <td><a href="assignments-detail.html"><i class="check_ellipse fa fa-location-arrow"></i> Submit</a></td>
                                                      </tr>
                                                      <tr>
                                                        <th><a href="assignments-detail.html">Structured Approach to Compliance</a></th>
                                                        <td>Jul-2017</td>
                                                        <td>20-Sep-17</td>
                                                        <td>10</td>
                                                        <td><i class="check_ellipse fa fa-check"></i> Not Submitted</td>
                                                        <td><a href="assignments-detail.html"><i class="fa fa-newspaper-o"></i> info</a></td>
                                                        <td><a href="assignments-detail.html"><i class="fa fa-cloud-download"></i> Download</a></td>
                                                        <td><a href="assignments-detail.html"><i class="check_ellipse fa fa-location-arrow"></i> Submit</a></td>
                                                      </tr> -->
															</tbody>
														</table>
													</c:when>
													<c:otherwise>
														<div class="image_not_found">
															<i class="fa fa-newspaper-o"></i>
															<p>No Assignment Data</p>
														</div>
													</c:otherwise>
												</c:choose>
											</div>
										</div>
									</div>
								</div>
							</div>

						</div>

					</div>

					<%@include file="../common/studentToDo.jsp"%>



				</div>

			</div>
			<!-- /page content -->


			<jsp:include page="../common/footer.jsp" />

		</div>
	</div>

</body>
</html>