<%@page import="java.util.*"%>
<%@page import="com.spts.lms.beans.dashboard.DashBoard"%>
<%-- <jsp:include page="../common/DashboardHeader.jsp" /> --%>
<%-- <jsp:include page="../common/NewHeader1.jsp" /> --%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>



<html lang="en">
<jsp:include page="../common/css.jsp" />

<body class="nav-md footer_fixed">

	<div class="loader"></div>


	<div class="container body">
		<div class="main_container">

			<jsp:include page="../common/leftSidebar.jsp">
				<jsp:param name="courseId" value="${courseId}" />
			</jsp:include>




			<jsp:include page="../common/topHeader.jsp" />


			<div class="right_col" role="main">

				<div class="dashboard_container">

					<div class="dashboard_container_spacing">

						<div class="breadcrumb">

							<a href="${pageContext.request.contextPath}/homepage">Dashboard</a>
							<i class="fa fa-angle-right"></i> View Each Assignment
						</div>

						<jsp:include page="../common/alert.jsp" />



						<div class="row">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">
									<div class="x_title">
										<h2>Assignment Details</h2>

										<div class="clearfix"></div>
									</div>
									<div class="x_content">
										<form:form action="createAssignment" id="submitAssignment"
											method="post" modelAttribute="assignment"
											enctype="multipart/form-data">
											<div class="table-responsive">
												<table class="table table_font_bold assignment_table">
													<thead>
														<tr>
															<th>Assignment Name</th>
															<td>${assignment.assignmentName}</td>
															<th>Your Score</th>
															<td>${assignmentSubmission.score}</td>
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
																	Submitted on ${assignmentSubmission.submissionDate}</td>
															</c:if>
															<th>Download</th>
															<td><a
																href="downloadFile?id=${assignmentSubmission.assignmentId }">
																	<i class="fa fa-cloud-download"></i>Question File
															</a></td>
														</tr>
														<%-- <tr>
															<th>Your Score</th>
															<td>${assignmentSubmission.score}</td>
														</tr> --%>




														<tr>
															<th colspan="6">Remarks</th>
														</tr>
														<tr>
															<td colspan="6">${assignmentSubmission.remarks}</td>

														</tr>


														<tr>
															<th colspan="6">Assignment Details</th>

														</tr>
														<tr>
															<td colspan="6">${assignment.assignmentText}</td>

														</tr>
														<!-- <tr>
																<td colspan="6">
																	<p>Please read below instructions carefully before
																		submitting assignment.</p>
																	<ul class="bullet1">
																		<li>Assignment must be prepared individually and
																			not in group.</li>
																		<li>Assignment marks will be displayed at the end
																			of terms</li>
																		<li>This assignment carries 20% of overall
																			assignment marks</li>
																		<li>All assignment submissions will be verified
																			for Plagiarism</li>
																	</ul>
																</td>
															</tr> -->
														<tr>
															<td colspan="6">


																<div class="col-sm-4 col-md-4 col-xs-12 column">
																	<div class="form-group">
																		<a href="submitAssignmentForm?id=${assignment.id}"><i
																			class="btn btn-large btn-primary">Submit
																				Assignment</i></a>
																	</div>
																</div>


															</td>
														</tr>

													</tbody>
												</table>
											</div>
										</form:form>
									</div>
								</div>
							</div>
						</div>



					</div>

				</div>


			</div>

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
