<%--  <jsp:include page="../common/header.jsp" />  --%>
<%@page import="com.spts.lms.beans.calender.Calender"%>
<%@page import="java.util.List"%>
<%@ page import="com.spts.lms.web.utils.Utils"%>
<jsp:include page="../common/NewHeader.jsp" />
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>



<%
	List<Calender> listOfEvents = (List<Calender>) request
			.getAttribute("events");
%>
<!-- page content -->
<div class="right_col" role="main">

	<div class="dashboard_contain">

		<div class="dashboard_height" id="main">

			<div class="dashboard_contain_specing">
				<div class="breadcrumb">
					<a href="my-courses.html">My Courses</a> <i
						class="fa fa-angle-right"></i> Business Law
				</div>

				<div class="row">
					<div class="col-xs-12 col-sm-12">
						<div class="x_panel">
							<div class="x_title">
								<h2>Assignments</h2>
								<ul class="nav navbar-right panel_toolbox">
									<li><a href="#"><span>View All</span></a></li>
									<li><a class="collapse-link"><i
											class="fa fa-chevron-up"></i></a></li>
									<li><a class="close-link"><i class="fa fa-close"></i></a></li>
								</ul>
								<div class="clearfix"></div>
							</div>
							<div class="x_content">
								<div class="table_overflow">
									<table class="table">
										<thead>
											<tr>
												<th>Sr No.</th>
												<th>Name</th>
												<th>End Date</th>
												<th>Points</th>
												<th>Submitted</th>
												<th>Action</th>
											</tr>
										</thead>
										<tbody>
											<c:forEach var="assignment" items="${assignments}"
												varStatus="status">
												<tr>
													<td><c:out value="${status.count}" /></td>
													<td><c:out value="${assignment.assignmentName}" /></td>
													<td><c:out value="${assignment.endDate}" /></td>
													<td>30</td>
													<td><c:if
															test="${assignment.submissionStatus eq 'Y' }">
															<i class="check_ellipse fa fa-check bg-green"></i>
															<c:out value="Submitted" />
														</c:if> <c:if test="${assignment.submissionStatus ne 'Y' }">
															<i class="check_ellipse fa fa-check"></i>
															<c:out value="Not Submitted" />
														</c:if></td>
													<td><sec:authorize access="hasRole('ROLE_STUDENT')">
															<c:if test="${assignment.submissionStatus eq 'Y' }">
																<i class="check_ellipse fa fa-location-arrow"></i>
																<a href="#" title="Assignment Submitted">Submitted</a>&nbsp;
															</c:if>

															<c:if test="${assignment.submissionStatus ne 'Y' }">

																<c:url value="submitAssignmentForm" var="submiturl">
																	<c:param name="id" value="${assignment.id}" />
																</c:url>
																<i class="check_ellipse fa fa-location-arrow bg-red"></i>

																<a href="${submiturl}" title="Submit Assignment">Submit</a>&nbsp;
														</c:if>

														</sec:authorize> <sec:authorize access="hasRole('ROLE_FACULTY')">
															<c:url value="viewAssignment" var="editAssignmentUrl">
																<c:param name="id" value="${assignment.id}" />
															</c:url>
															<i class="check_ellipse fa fa-location-arrow bg-red"></i>
															<a href="${editAssignmentUrl}" title="Submit Assignment">Edit
																Assignment</a>&nbsp;
												</sec:authorize></td>


												</tr>
											</c:forEach>

											<!-- <tr>
												<th><a href="assignments.html">Types of Industry
														Laws</a></th>
												<td>20-Sep-17</td>
												<td>30</td>
												<td><i class="check_ellipse fa fa-check"></i> Not
													Submitted</td>
												<td><i
													class="check_ellipse fa fa-location-arrow bg-red"></i>
													Submit</td>
											</tr>
											<tr>
												<th><a href="assignments.html">Concepts of
														Corporate Laws</a></th>
												<td>17-Aug-17</td>
												<td>20</td>
												<td><i class="check_ellipse fa fa-check bg-green"></i>
													Submitted</td>
												<td><i class="check_ellipse fa fa-location-arrow"></i>
													Submit</td>
											</tr>
											<tr>
												<th><a href="assignments.html">Theory of Business
														Policies</a></th>
												<td>18-Sep-17</td>
												<td>30</td>
												<td><i class="check_ellipse fa fa-check bg-green"></i>
													Submitted</td>
												<td><i class="check_ellipse fa fa-location-arrow "></i>
													Submit</td>
											</tr>
											<tr>
												<th><a href="assignments.html">Working of
														Government Policies</a></th>
												<td>24-Aug-17</td>
												<td>30</td>
												<td><i class="check_ellipse fa fa-check"></i> Not
													Submitted</td>
												<td><i class="check_ellipse fa fa-location-arrow"></i>
													Submit</td>
											</tr>
											<tr>
												<th><a href="assignments.html">Structured Approach
														to Compliance</a></th>
												<td>20-Sep-17</td>
												<td>10</td>
												<td><i class="check_ellipse fa fa-check"></i> Not
													Submitted</td>
												<td><i class="check_ellipse fa fa-location-arrow"></i>
													Submit</td>
											</tr> -->
										</tbody>
									</table>
								</div>
							</div>
						</div>
					</div>
				</div>

				<div class="row">
					<div class="col-sm-12">
						<div class="x_panel">
							<div class="x_title">
								<h2>Tests</h2>
								<ul class="nav navbar-right panel_toolbox">
									<li><a href="#"><span>View All</span></a></li>
									<li><a class="collapse-link"><i
											class="fa fa-chevron-up"></i></a></li>
									<li><a class="close-link"><i class="fa fa-close"></i></a></li>
								</ul>
								<div class="clearfix"></div>
							</div>


							<div class="x_content">
								<div class="table_overflow">
									<table class="table">
										<thead>
											<tr>
												<th>Sr No.</th>
												<th>Name</th>
												<th>End Date</th>
												<th>Points</th>
												<th>Submitted</th>
												<th>Action</th>
											</tr>
										</thead>
										<tbody>

											<c:forEach var="test" items="${tests}" varStatus="status">
												<tr>
													<td><c:out value="${status.count}" /></td>
													<td><c:out value="${test.testName}" /></td>
													<td><c:out value="${test.endDate}" /></td>
													<td>30</td>
													<td><i class="check_ellipse fa fa-check bg-green"></i>
														Submitted</td>
													<td><i class="check_ellipse fa fa-location-arrow"></i>
														Submit</td>

												</tr>
											</c:forEach>


											<!-- <tr>
                                                        <th><a href="assignments.html"><i class="pluse_ellipse fa fa-plus"></i> Types of Industry Laws</a></th>
                                                        <td>20-Sep-17</td>
                                                        <td>30</td>
                                                        <td><i class="check_ellipse fa fa-check"></i> Not Submitted</td>
                                                        <td><i class="check_ellipse fa fa-location-arrow bg-red"></i> Submit</td>
                                                      </tr>
                                                      <tr>
                                                        <th><a href="assignments.html"><i class="pluse_ellipse fa fa-plus"></i> Concepts of Corporate Laws</a></th>
                                                        <td>17-Aug-17</td>
                                                        <td>20</td>
                                                        <td><i class="check_ellipse fa fa-check bg-green"></i> Submitted</td>
                                                        <td><i class="check_ellipse fa fa-location-arrow"></i> Submit</td>
                                                      </tr>
                                                      <tr>
                                                        <th><a href="assignments.html"><i class="pluse_ellipse fa fa-plus"></i> Theory of Business Policies</a></th>
                                                        <td>18-Sep-17</td>
                                                        <td>30</td>
                                                        <td><i class="check_ellipse fa fa-check bg-green"></i> Submitted</td>
                                                        <td><i class="check_ellipse fa fa-location-arrow "></i> Submit</td>
                                                      </tr>
                                                      <tr>
                                                        <th><a href="assignments.html"><i class="pluse_ellipse fa fa-plus"></i> Working of Government Policies</a></th>
                                                        <td>24-Aug-17</td>
                                                        <td>30</td>
                                                        <td><i class="check_ellipse fa fa-check"></i> Not Submitted</td>
                                                        <td><i class="check_ellipse fa fa-location-arrow"></i> Submit</td>
                                                      </tr>
                                                      <tr>
                                                        <th><a href="assignments.html"><i class="pluse_ellipse fa fa-plus"></i> Structured Approach to Compliance</a></th>
                                                        <td>20-Sep-17</td>
                                                        <td>10</td>
                                                        <td><i class="check_ellipse fa fa-check"></i> Not Submitted</td>
                                                        <td><i class="check_ellipse fa fa-location-arrow"></i> Submit</td>
                                                      </tr> -->
										</tbody>
									</table>
								</div>
							</div>
						</div>
					</div>
				</div>

				<div class="row">
					<div class=" col-md-6">
						<div class="x_panel">
							<div class="x_title">
								<h2>Discussion Forums</h2>
								<ul class="nav navbar-right panel_toolbox">
									<li><a href="#"><span>View All</span></a></li>
									<li><a class="collapse-link"><i
											class="fa fa-chevron-up"></i></a></li>
									<li><a class="close-link"><i class="fa fa-close"></i></a></li>
								</ul>
								<div class="clearfix"></div>
							</div>
							<div class="x_content">
								<div class="event_content">
									<c:forEach var="forum" items="${allForums}" varStatus="status">
										
										<ul>
											<li class="x_panel">
												<h4>
													<a href="#"><c:out value="${forum.topic}" /></a>
												</h4>
												<p>
													Posted <span>282 Days ago</span> by <span><c:out
															value="${forum.createdBy}" /></span>
												</p>
												<p>
													<span><c:out value="${replyCount[status.count-1]}" /> Replies </span> | Last activity: <span>2016-09-09</span>
													at <span>17:16:55.0</span>
												</p>
											</li>
										</ul>

									</c:forEach>
									<!-- <ul>
										<li class="x_panel">
											<h4>
												<a href="#">How successful is a one person company in
													India?</a>
											</h4>
											<p>
												Posted <span>282 Days ago</span> by <span>NGASCE0031</span>
											</p>
											<p>
												<span>82 Replies</span> | Last activity: <span>2016-09-09</span>
												at <span>17:16:55.0</span>
											</p>
										</li>
										<li class="x_panel">
											<h4>
												<a href="#">Statistics Ministry planning to include
													ecommerce in calculation of CPI.</a>
											</h4>
											<p>
												Posted <span>283 Days ago</span> by <span>NGASCE00179</span>
											</p>
											<p>
												<span>46 Replies</span> | Last activity: <span>2016-10-09</span>
												at <span>10:02:03.0</span>
											</p>
										</li>
										<li class="x_panel">
											<h4>
												<a href="#">Mahindra with Ola announces strategic tie
													ups.</a>
											</h4>
											<p>
												Posted <span>285 Days ago</span> by <span>NGASCE0031</span>
											</p>
											<p>
												<span>68 Replies</span> | Last activity: <span>2016-12-09</span>
												at <span>21:14:13.0</span>
											</p>
										</li>
									</ul> -->
								</div>
							</div>
						</div>
					</div>
					<div class="col-md-6">
						<div class="x_panel">
							<div class="x_title">
								<h2>Events</h2>
								<ul class="nav navbar-right panel_toolbox">
									<li><a href="#"><span>View All</span></a></li>
									<li><a class="collapse-link"><i
											class="fa fa-chevron-up"></i></a></li>
									<li><a class="close-link"><i class="fa fa-close"></i></a></li>
								</ul>
								<div class="clearfix"></div>
							</div>
							<div class="x_content">
								<div class="table_overflow">
									<div class="event_content">


										<%
											for (Calender c : listOfEvents) {
												String dateInWords = Utils.formatDate("yyyy-mm-dd hh:mm:ss",
														"MMMMM dd, yyyy", c.getStartDate());
										%>
										<ul>
											<li class="x_panel">

												<h4>
													<a href="#"><c:out value="<%=dateInWords%>" /> </a>
												</h4>
												<p>
													<c:out value="<%=c.getDescription()%>" />

												</p>
											</li>
										</ul>

										<%
											}
										%>


										<!-- <ul>
											<li class="x_panel">
												<h4>
													<a href="#">July 12, 2017</a>
												</h4>
												<p>NGASCE will be honored with 'Top Distance Learning
													Institute of India' award at CSR Awards For Excellence.</p>
											</li>
											<li class="x_panel">
												<h4>
													<a href="#">July 16, 2017</a>
												</h4>
												<p>Design Workshop will be conducted by an expert
													faculties of JJ School of Arts & Crafts.</p>
											</li>
											<li class="x_panel">
												<h4>
													<a href="#">July 20, 2017</a>
												</h4>
												<p>Seminar on MBA in India and Abroad by NGASCE
													Comemittee.</p>
												<p>
													Venue: Avion Hotel, Domestic Airport, Nehru Road, Vile
													Parle(East), Mumbai 400 057. <a href="#">Register
														here..</a>
												</p>
											</li>
										</ul> -->
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>

				<div class="row">
					<div class="col-sm-12">
						<div class="x_panel">
							<div class="x_title">
								<h2>Learning Resources</h2>
								<ul class="nav navbar-right panel_toolbox">
									<li><a href="#"><span>View All</span></a></li>
									<li><a class="collapse-link"><i
											class="fa fa-chevron-up"></i></a></li>
									<li><a class="close-link"><i class="fa fa-close"></i></a></li>
								</ul>
								<div class="clearfix"></div>
							</div>
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
											<c:forEach var="content" items="${allContent}"
												varStatus="status">
												<tr data-tt-id="${content.id}"
													data-tt-parent-id="${content.parentContentId}">

													<td><c:out value="${content.contentName}" /></td>

													<td><c:out value="${content.contentDescription}" /></td>
													<td>
														
														<c:if test="${content.contentType == 'Folder' }">
															<i class="fa lms-folder-o fa-lg"
																style="background: #E6CB47; margin-right: 5px"></i>

															<c:url value="/getContentUnderAPathForStudent"
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
															
															
															<a href="downloadFile?filePath=${content.filePath}"><i class="fa fa-cloud-download"></i>
														Download</a>
														</c:if> <c:if test="${content.contentType == 'Link' }">
															<i class="fa fa-link fa-lg" style="margin-right: 5px"></i>
															<a href="${content.linkUrl}" target="_blank"> <c:out
																	value="${content.contentName}" />
															</a>
														</c:if>
														</td>
														
												<td><a href="#">View</a></td>
												</tr>
											</c:forEach>
											<!-- <tr>
												<td>Business Law (BL) Course Material</td>
												<td>Streaming recording link 2017-03-02</td>
												<td><a href="#"><i class="fa fa-cloud-download"></i>
														Download</a></td>
												<td><a href="#">View</a></td>
											</tr>
											<tr>
												<td>BL - Course Presentation</td>
												<td>Download recording link</td>
												<td><a href="#"><i class="fa fa-cloud-download"></i>
														Download</a></td>
												<td><a href="#"><i class="fa fa-eye"></i> View</a></td>
											</tr>
											<tr>
												<td>BL - Session Plan</td>
												<td>Streaming recording link</td>
												<td><a href="#"><i class="fa fa-cloud-download"></i>
														Download</a></td>
												<td><a href="#"><i class="fa fa-eye"></i> View</a></td>
											</tr>
											<tr>
												<td>Bl - Foundation Session PPT</td>
												<td>Streaming recording link 2017-05-10</td>
												<td><a href="#"><i class="fa fa-cloud-download"></i>
														Download</a></td>
												<td><a href="#">View</a></td>
											</tr>
											<tr>
												<td>BL - Foundation Course by Prof. Purva Shah</td>
												<td>Download recording link</td>
												<td><a href="#"><i class="fa fa-cloud-download"></i>
														Download</a></td>
												<td><a href="#">View</a></td>
											</tr> -->
										</tbody>
									</table>
								</div>
							</div>
						</div>
					</div>
				</div>



			</div>

		</div>

		<div class="dashboard_height rightpanel" id="mySidenav1">
			<div class="right-arrow">
				<img src="<c:url value="images/dash-right.gif" /> " alt=""
					onclick="openNav2()">
			</div>
			<div class="rightpanel_content">
				<h3>
					To Do <a href="#">View All</a>
				</h3>
				<ul>
					<li class="x_panel">
						<h4>
							<a href="#">Assignments</a> <a href="#" class="close-link"><i
								class="fa fa-close"></i></a>
						</h4>
						<p>Business Communication &amp; Etiquettes</p>
						<p>
							<span>19 points</span> - June 19, 2017 at 11:30am
						</p>
					</li>
					<li class="x_panel">
						<h4>
							<a href="#">Introduction Sessions</a> <a href="#"
								class="close-link"><i class="fa fa-close"></i></a>
						</h4>
						<p>Intro Oceans</p>
						<p>
							<span>10 points</span> - June 21, 2017 at 9:30am
						</p>
					</li>
					<li class="x_panel">
						<h4>
							<a href="#">Practice Quize</a> <a href="#" class="close-link"><i
								class="fa fa-close"></i></a>
						</h4>
						<p>Financial Accounts &amp; Analysis and Business Law</p>
						<p>
							<span>14 points</span> - June 21, 2017 at 1:30pm
						</p>
					</li>
				</ul>
				<h3>
					Announcements <a href="#">View All</a>
				</h3>
				<ul>
					<li class="x_panel">
						<h4>
							<a href="#">June 19, 2017</a> <a href="#" class="close-link"><i
								class="fa fa-close"></i></a>
						</h4>
						<p>Campus Recruitement Interviews by TCS, Infosys &amp; LTI.</p>
					</li>
					<li class="x_panel">
						<h4>
							<a href="#">June 23, 2017</a> <a href="#" class="close-link"><i
								class="fa fa-close"></i></a>
						</h4>
						<p>Welcome to Algebra 1A!</p>
						<p>Have you ever heard someone say, I'll never use algebra in
							real life.</p>
					</li>
					<li class="x_panel">
						<h4>
							<a href="#">June 27, 2017</a> <a href="#" class="close-link"><i
								class="fa fa-close"></i></a>
						</h4>
						<p>Internal Assignment applicable for June, 2017 Exam cycle is
							live!</p>
					</li>
				</ul>
			</div>
		</div>

	</div>

</div>
<!-- /page content -->


<jsp:include page="../common/DashboardFooter.jsp" />
