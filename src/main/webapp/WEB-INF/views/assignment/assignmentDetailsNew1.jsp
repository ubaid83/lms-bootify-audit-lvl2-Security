<%@page import="java.util.*"%>
<%@page import="com.spts.lms.beans.dashboard.DashBoard"%>
<%-- <jsp:include page="../common/DashboardHeader.jsp" /> --%>
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
			<jsp:include page="../common/alert.jsp" />


			<!-- page content -->
			<div class="right_col" role="main">
				<form:form action="createAssignment" id="submitAssignment"
					method="post" modelAttribute="assignment"
					enctype="multipart/form-data">

					<div class="dashboard_container">
						<!--<div class="dashboard_contain">-->

						<div class="dashboard_height" id="main">

							<div class="dashboard_container_spacing">
								<div class="breadcrumb">
								
									<a href="${pageContext.request.contextPath}/homepage">Dashboard</a>
									<i class="fa fa-angle-right"></i> View Each Assignment
								</div>



								<!-- <div class="alert alert-success">
					<strong>Congratulations!</strong> You have uploaded file
					successfully.
				</div> -->


								<div class="row">
									<div class="col-xs-12 col-sm-12">
										<div class="x_panel">
											<div class="x_title">
												<h2>Submit Assignment for
													${assignment.course.courseName}</h2>
												<form:input path="id" type="hidden" value="${assignment.id}" />
												<form:input path="courseId" type="hidden"
													value="${assignment.courseId}" />
												<div class="clearfix"></div>
											</div>
											<div class="x_content">
												<div class="table-responsive">
													<table class="table table_font_bold assignment_table">
														<thead>
															<tr>
																<th>Assignment Name</th>
																<td>${assignment.assignmentName}</td>
																<td></td>
																<td></td>
																<td></td>
																<td></td>
															</tr>
														</thead>
														<tbody>
															<tr>
																<th>Term</th>
																<td>${assignment.acadMonth}-${assignment.acadYear}</td>
																<th>Start Date</th>
																<td>${assignment.startDate }</td>
																<th>End Date</th>
																<td>${assignment.endDate}</td>
															</tr>
															<tr>
																<th>Points</th>
																<td>${assignment.maxScore}</td>
																<th>Status</th>
																<c:if
																	test="${assignmentSubmission.submissionStatus eq 'N' }">
																	<td><i class="check_ellipse fa fa-check bg-green"></i>
																		Not Submitted</td>
																</c:if>
																<c:if
																	test="${assignmentSubmission.submissionStatus eq 'Y' }">
																	<td><i class="check_ellipse fa fa-check bg-green"></i>
																		Submitted</td>
																</c:if>
																<th>Download</th>
																<td><a
																	href="downloadFile?id=${assignmentSubmission.assignmentId }">
																		<i class="fa fa-cloud-download"></i>Question File
																</a></td>
															</tr>
															<tr>
																<th colspan="6">Assignment Details</th>
															</tr>
															<tr>
																<td colspan="6">
																	<p>Please read below instructions carefully before
																		submitting assignment.</p>
																	<ul class="bullet1">
																		<!-- <li>Assignment must be prepared individually and
																			not in group.</li>
																		<li>Assignment marks will be displayed at the end
																			of terms</li> -->
																		<li>This assignment carries 20% of overall
																			assignment marks</li>
																		<li>All assignment submissions will be verified
																			for Plagiarism</li>
																	</ul>
																</td>
															</tr>
															<tr>
																<td colspan="6">
																	<div class="group">
																		<c:if
																			test="${assignmentSubmission.submissionStatus eq 'Y' }">
																			<div class="left font_weight_bold">Upload New
																				Answer File</div>
																		</c:if>
																		<c:if
																			test="${assignmentSubmission.submissionStatus eq 'N' }">
																			<div class="left font_weight_bold">Upload 
																				Answer File</div>
																		</c:if>
																		<div class="assignment_upload_box">
																			<p>${assignmentSubmission.studentFilePath}</p>
																			<p>${assignmentSubmission.lastModifiedDate}</p>
																			<%-- 	<p>
																<input id="file" name="file" type="file"
																	class="form-control file" />Choose Files to Upload <a
																	href="downloadFile?filePath=${assignmentSubmission.studentFilePath}">Download
																	File</a>
															</p> --%>
																			<div class="group">
																				<span class="choose_file"> Choose File to
																					upload <input id="file" type="file" name="file"
																					class="hide_file" />
																				</span>
																				<c:if
																					test="${assignmentSubmission.submissionStatus eq 'Y' }">

																					<a
																						href="downloadFile?saId=${assignmentSubmission.id}">Preview Uploaded
																						File</a>
																				</c:if>
																			</div>
																			<div id=fileSize></div>
																		</div>
																	</div>

																</td>
															</tr>
															<tr>
																<td colspan="6">
																	<!-- <div class="text-center">
																	<button type="button" class="btn1"
																		formaction="homepage">Back</button>
																	<button type="button" class="btn2"
																		formaction="submitAssignment">Submit</button>
																</div> -->
																	<div class="text-center form-group">
																		<button id="submit" class="btn  btn-primary "
																			formaction="homepage">Back</button>

																		<%-- <c:if
																			test="${assignmentSubmission.evaluationStatus eq 'N' }">
																			<button id="submit" class="btn  btn-primary "
																				formaction="submitAssignment">Submit</button>
																		</c:if> --%>
																		<c:if
																			test="${assignmentSubmission.evaluationStatus eq 'N' }">
																			<c:if
																				test="${assignmentSubmission.submissionStatus eq 'N' }">
																				<button id="submit"
																					class="btn btn-large btn-primary"
																					formaction="submitAssignment"
																					onclick="return confirm('Are you sure you want to Submit Your Assignment')">Submit
																					Assignment</button>
																			</c:if>

																			<c:if
																				test="${assignmentSubmission.submissionStatus eq 'Y' }">
																				<button id="submit"
																					class="btn btn-large btn-primary"
																					onclick="return confirm('Are you sure you want to Submit Your Assignment')"
																					formaction="submitAssignment">Resubmit
																					Assignment</button>
																			</c:if>
																			<!-- <button id="submit" class="btn btn-large btn-primary"
																	formaction="submitAssignment"
																	onclick="return confirm('Are you sure you want to Submit Your Assignment')">Submit
																	Assignment</button> -->
																		</c:if>

																		<c:if
																			test="${assignmentSubmission.evaluationStatus eq 'Y' }">
																			<button id="submit" class="btn  btn-primary"
																				formaction="submitAssignment" disabled="disabled">Cannot
																				Submit As already Evaluated</button>
																		</c:if>


																	</div>
																</td>
															</tr>
														</tbody>
													</table>
												</div>
											</div>
										</div>
									</div>
								</div>



							</div>

						</div>

						<!-- <div class="dashboard_height rightpanel" id="mySidenav1">
			<div class="right-arrow">
				<img src="images/dash-right.gif" alt="" onclick="openNav2()">
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
		</div> -->

						<jsp:include page="../common/studentToDo.jsp" />

					</div>
				</form:form>
			</div>
			<!-- /page content -->




			<jsp:include page="../common/footer.jsp" />
			<script type="text/javascript">
				$('#file').bind('change', function() {
					// alert('This file size is: ' + this.files[0].size/1024/1024 + "MB");
					var fileSize = this.files[0].size / 1024 / 1024 + " MB";
					$('#fileSize').html("File Size:" + (fileSize));
				});
			</script>


		</div>
	</div>

</body>
</html>
