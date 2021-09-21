<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<!-- <script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script> -->

<!-- 
<script src="//netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js"></script>	 -->

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

			<%--  <jsp:include page="../common/header3.jsp" /> --%>

			<!-- page content: START -->
			<div class="right_col" role="main">

				<div class="dashboard_container">

					<div class="dashboard_container_spacing">

						<div class="breadcrumb">

							<c:out value="${Program_Name}" />

							<sec:authorize access="hasRole('ROLE_STUDENT')">

								<i class="fa fa-angle-right"></i>

								<c:out value="${AcadSession}" />

							</sec:authorize>

							<br> <br> <a
								href="${pageContext.request.contextPath}/homepage">Dashboard</a>
							<i class="fa fa-angle-right"></i> Escalated Students List
						</div>
						<jsp:include page="../common/alert.jsp" />
						<!-- Input Form Panel -->


						<c:choose>
							<c:when test="${pendingList.size() > 0}">
								<!-- Results Panel -->
								<form:form action="" method="post"
									modelAttribute="studentServiceBean"
									enctype="multipart/form-data">
									<form:input path="id" type="hidden" />
									<div class="row">
										<div class="col-xs-12 col-sm-12">
											<div class="x_panel">
												<div class="x_title">
													<h2>Students | ${fn:length(pendingList)} Records Found
														| Note : Approval of student's application can be done one
														by one or in bulk by uploading the excel file in the given
														format.</h2>


													<ul class="nav navbar-right panel_toolbox">
														<li><a class="collapse-link"><i
																class="fa fa-chevron-up"></i></a></li>
														<li><a class="close-link"><i class="fa fa-close"></i></a></li>
													</ul>
													<div class="clearfix"></div>
												</div>
												<div class="x_content">
													<a
														href="downloadStudentsRCFormsForLevel3?serviceId=${serviceId }"><i
														class="btn btn-large btn-primary">Download All
															Students Applications</i></a>
													<button id="target" class="btn btn-large btn-primary"
														type="button" data-toggle="modal" data-target="#myModal">Upload
														Excel File</button>
													<div class="modal fade" id="myModal" role="dialog">
														<div class="modal-dialog">

															<!-- Modal content-->
															<div class="modal-content">
																<div class="modal-header">
																	<button type="button" class="close"
																		data-dismiss="modal">&times;</button>
																	<h4 class="modal-title">Upload Excel</h4>

																</div>
																<div class="modal-body">
																<p style="color: red">Note : Kindly upload the
																		respective downloaded file of pending applications and do not make any changes
																		other than Status and Remark column.</p>
																	<div class="col-sm-6 col-md-4 col-xs-12 column">
																		<div class="form-group">
																			<label for="file"> </label> <input id="file"
																				name="file" type="file" class="form-control" />
																		</div>

																	</div>



																</div>

																<div class="modal-footer">


																	<button type="button" class="btn btn-default"
																		id="close" data-dismiss="modal">Close</button>
																	<button id="submit" type="submit"
																		formaction="uploadStudentsRCFormsForLevel3"
																		class="btn btn-default">Upload</button>

																</div>
															</div>

														</div>
													</div>
													<div class="table-responsive">
														<table class="table table-hover" id="POITable">
															<thead>
																<tr>
																	<th>Sr. No.</th>

																	<th>Username</th>
																	<!-- <th>Roll No.</th> -->
																	<th>Student Name</th>
																	<th>View Application</th>
																	<th>Approve/Reject</th>
																	<th>Remarks</th>

																</tr>
															</thead>
															<tfoot>
																<tr>
																	<th></th>
																	<th></th>

																</tr>
															</tfoot>
															<tbody>

																<c:forEach var="student" items="${pendingList}"
																	varStatus="status">
																	<tr id="tr${student.id}">
																		<td><c:out value="${status.count}" /></td>
																		<td><c:out value="${student.username}" /></td>
																		<%-- <td><c:out value="${student.rollNo}" /></td> --%>

																		<td><c:out
																				value="${student.firstname} ${student.lastname}" /></td>



																		<td><c:url value="viewStudentRCApplication"
																				var="viewStudentRCApplicationurl">
																				<c:param name="id" value="${student.id }"></c:param>


																			</c:url> <a href="${viewStudentRCApplicationurl}"
																			title="View Application"><i
																				class="fa fa-info-circle fa-lg"></i></a>&nbsp;</td>

																		<td><c:if
																				test="${student.approvedLevel eq true }">
																				<c:out value="Approved"></c:out>
																			</c:if> <c:if test="${student.approvedLevel ne true }">

																				<c:if test="${showActions}">
																					<a href="#" class="editable" id="radio1"
																						data-type="select" data-pk="${student.id}"
																						data-source="[{value: 'APPROVED', text: 'Approve'},{value: 'REJECTED', text: 'Reject'}]"
																						data-url="saveRCStatusForLevel3?flag=flag2"
																						data-title="Select Status">${student.flag2}</a>
																					<%-- <form:radiobutton path = "flag1" value = "A" label = "Approve" />
                  																<form:radiobutton path = "flag1" value = "R" label = "Reject" /> --%>
																					<%-- <a
																						href="approveBonafideApplication?id=${student.id}&status=1"
																						id="like${student.id}"
																						class="btn btn-primary btn-xs likeClass">Approve</a>

																					<a href="approveBonafideApplication?id=${student.id}&status=0" id="like${student.id}"
																					class="btn btn-primary btn-xs likeClass">Reject</a> --%>
																				</c:if>

																			</c:if></td>
																		<td><c:if test="${showRemark}">
																				<a href="#" class="editable" id="remark1"
																					data-type="textarea" data-pk="${student.id}"
																					data-url="saveRCRemarksForLevel3?remark=remark2"
																					data-title="Enter Remarks">${student.remark2}</a>

																				<%-- <form:input path="remark1" value="" placeholder="remark1"/> --%>
																			</c:if> <%-- <c:if test="${student.remark1 eq null}">
																			<form:input path="remark1" value=""/>
																			</c:if> --%></td>
																	</tr>
																</c:forEach>
															</tbody>
														</table>
													</div>

												</div>
											</div>
										</div>
									</div>
								</form:form>
							</c:when>
						</c:choose>
					</div>



				</div>

			</div>
			<!-- /page content: END -->



			<jsp:include page="../common/footer.jsp" />






		</div>
	</div>





</body>
</html>
