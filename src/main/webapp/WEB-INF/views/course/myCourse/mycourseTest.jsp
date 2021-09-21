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





<!-- Input Form Panel -->
<div class="card bg-white border">
	<div class="card-body">
				<div class="text-center">
					<sec:authorize access="hasRole('ROLE_STUDENT')">
						<h5>Pending Tests</h5>
					</sec:authorize>
					<sec:authorize access="hasRole('ROLE_FACULTY')">
						<h5>Tests</h5>
					</sec:authorize>
				<%-- 	<ul class="nav navbar-right panel_toolbox">
						<li><a href="<c:url value="testList?courseId=${courseId}" />"><span>View
									All</span></a></li>
						<li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a></li>
						<li><a class="close-link"><i class="fa fa-close"></i></a></li>
					</ul>
					<div class="clearfix"></div> --%>
				</div>
				<div class="x_itemCount" style="display: none;">
					<div class="image_not_found">
						<i class="fa fa-file-text"></i>
						<p>
							<label class="x_count"></label>Test
						</p>
					</div>
				</div>


				<div class="x_content">
					<div class="table-responsive testAssignTable">
						<c:choose>
							<c:when test="${ not empty tests}">
								<table class="table table-hover table-striped">
									<thead>
										<tr>

											<th>Name</th>
											<th>Start Date</th>
											<th>End Date</th>
											<th>Total Marks</th>
											<sec:authorize access="hasRole('ROLE_STUDENT')">
												<th>Test Completed</th>
											</sec:authorize>
											<th>Action</th>
										</tr>
									</thead>
									<tbody>

										<c:forEach var="test" items="${tests}" varStatus="status">

											<tr>

												<td><a href="#"><c:out value="${test.testName}" /></a></td>

												<td><c:out value="${test.startDate}" /></td>
												<td><c:out value="${test.endDate}" /></td>
												<td><c:out value="${test.maxScore}" /></td>
												<sec:authorize access="hasRole('ROLE_STUDENT')">
													<td><c:if test="${test.testCompleted eq 'Y' }">
															<i class="check_ellipse fa fa-check bg-green"></i>
															<c:out value="Test Completed" />
														</c:if> <c:if test="${test.testCompleted ne 'Y' }">
															<i class="check_ellipse fa fa-check"></i>
															<c:out value="Complete Test" />
														</c:if></td>
												</sec:authorize>

												<td><sec:authorize access="hasRole('ROLE_STUDENT')">
														<c:if test="${test.testCompleted eq 'Y' }">
															<c:url value="viewStudentTestResponse" var="viewtesturl">
																<c:param name="id" value="${test.id}" />
															</c:url>

															<i class="check_ellipse fa fa-location-arrow"></i>
															<a href="${viewtesturl}" title="Test Completed">Test
																Completed</a>&nbsp;
															</c:if>

														<c:if test="${test.testCompleted ne 'Y' }">

															<c:url value="startStudentTest" var="starttesturl">
																<c:param name="id" value="${test.id}" />
															</c:url>
															<c:url value="startStudentTestForSubjective"
																var="starttesturlForSubjective">
																<c:param name="id" value="${test.id}" />
															</c:url>

															<c:url value="testList" var="testList">
																<c:param name="courseId" value="${test.courseId}" />
															</c:url>
															<i class="check_ellipse fa fa-location-arrow"></i>

															<c:choose>
																<c:when test="${ test.testType eq 'Subjective' }">
																	<c:if test="${test.isPasswordForTest eq 'N'}">
																		<a href="${starttesturlForSubjective}"
																			title="Start Test"
																			onclick="return confirm('Are you ready to take Test?  (Attempt: No. ${test.attempt + 1} )')">StartTest</a>&nbsp;
														</c:if>
																	<c:if test="${test.isPasswordForTest eq 'Y'}">
																		<a href="${testList}" title="Start Test">StartTest</a>&nbsp;
																</c:if>
																</c:when>
																<c:otherwise>
																	<c:if test="${test.isPasswordForTest eq 'N'}">
																		<a href="${starttesturl}" title="Start Test"
																			onclick="return confirm('Are you ready to take Test?  (Attempt: No. ${test.attempt + 1} )')">StartTest</a>&nbsp;
															</c:if>
																	<c:if test="${test.isPasswordForTest eq 'Y'}">
																		<a href="${testList}" title="Start Test">StartTest</a>&nbsp;
																</c:if>
																</c:otherwise>
															</c:choose>
														</c:if>

													</sec:authorize> <sec:authorize access="hasRole('ROLE_FACULTY')">
														<c:url value="viewTestDetails" var="viewurl">
															<c:param name="testId" value="${test.id}" />
														</c:url>
														<i class="check_ellipse fa fa-location-arrow"></i>
														<a href="${viewurl}" title="Submit Assignment">View
															Test</a>&nbsp;
												</sec:authorize></td>

												<!-- <td><i class="check_ellipse fa fa-check bg-green"></i>
												Submitted</td>
											<td><i class="check_ellipse fa fa-location-arrow"></i>
												Submit</td> -->

											</tr>



										</c:forEach>

									</tbody>
								</table>

							</c:when>
							<c:otherwise>
								<div class="text-center mt-3">
									<div class="image_not_found">
										<i class="fas fa-file-alt"></i>
										<p>No Test Data</p>
									</div>
								</div>
							</c:otherwise>
						</c:choose>
					</div>
				</div>
	</div>
</div>

