<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
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

							<br>
							<br> <a href="${pageContext.request.contextPath}/homepage">Dashboard</a>
							<i class="fa fa-angle-right"></i> Search Assignments/Tests
						</div>
						<jsp:include page="../common/alert.jsp" />
						<!-- Input Form Panel -->
						<div class="row">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">

									<div class="x_title">
										<h5 class="text-center border-bottom pb-2">Search Assignments/Tests</h5>
										
									</div>

									<div class="x_content">
										<form:form action="searchAssignmentTest" method="post"
											modelAttribute="assignment">



											<div class="row">
												<div class="col-sm-4 col-md-4 col-xs-12 column">
													<div class="form-group">
														<form:label path="acadMonth" for="acadMonth">Academic Month <span style="color: red">*</span></form:label>
														<form:select id="acadMonth" path="acadMonth"
															class="form-control" required="required">
															<form:option value="">Select Academic Month</form:option>
															<c:forEach var="course" items="${acadMonths}"
																varStatus="status">
																<form:option value="${course}">${course}</form:option>
															</c:forEach>
														</form:select>
													</div>
												</div>
												<div class="col-sm-4 col-md-4 col-xs-12 column">
													<div class="form-group">
														<form:label path="acadYear" for="acadYear">Academic Year <span style="color: red">*</span></form:label>
														<form:select id="acadYear" path="acadYear"
															class="form-control" required="required">
															<form:option value="">Select Academic Year</form:option>
															<c:forEach var="course" items="${acadYears}"
																varStatus="status">
																<form:option value="${course}">${course}</form:option>
															</c:forEach>
														</form:select>
													</div>
												</div>




												<div class="col-sm-4 col-md-4 col-xs-12 column">
													<div class="form-group">
														<form:label path="" for="">Select Assignments/Test</form:label>
														<form:select id="dropdwonId" path="" name="dropdwonId"
															class="form-control">
															<form:option value="">Select Assignments/Test</form:option>

															<c:forEach var="list" items="${list1}" varStatus="status">
																<form:option value="${list}">${list}</form:option>
															</c:forEach>

														</form:select>
													</div>
												</div>




												<div class="col-sm-12 column">
													<div class="form-group">
														<button id="submitId" name="submit"
															class="btn btn-large btn-primary">Search</button>
														<input id="reset" type="reset" class="btn btn-danger">


													</div>
												</div>
											</div>

										</form:form>
									</div>
								</div>
							</div>
						</div>

						<!-- Results Panel -->

						<c:choose>
							<c:when test="${testList.size() > 0}">
								<div class="row">
									<div class="col-xs-12 col-sm-12">
										<div class="x_panel">
											<div class="x_title">
												<h5 class="text-center border-bottom pb-2">
													Tests<font size="2px"> | ${testList.size()} Records
														Found &nbsp; </font>
												</h5>
												
											</div>
											<div class="x_content">
												<div class="table-responsive">
													<table class="table table-hover" style="font-size: 12px">
														<thead>
															<tr>
																<th>Sr. No.</th>
																<th>Name <i class="fa fa-sort" aria-hidden="true" style="cursor: pointer"></i></th>
																<th>Course <i class="fa fa-sort" aria-hidden="true" style="cursor: pointer"></i></th>

																<th>Attempts <i class="fa fa-sort" aria-hidden="true" style="cursor: pointer"></i></th>
																<th>Start Time <i class="fa fa-sort" aria-hidden="true" style="cursor: pointer"></i></th>
																<th>End Time <i class="fa fa-sort" aria-hidden="true" style="cursor: pointer"></i></th>
																<th>Test Completed </th>
																<th>Score <i class="fa fa-sort" aria-hidden="true" style="cursor: pointer"></i></th>
															</tr>
														</thead>
														<tbody>

															<c:forEach var="test" items="${testList}"
																varStatus="status">
																<tr>
																	<td><c:out value="${status.count}" /></td>
																	<td><c:out value="${test.testName}" /></td>
																	<td><c:out value="${test.courseName}" /></td>
																	<td><c:out value="${test.attempt}" /></td>

																	<td><c:out
																			value="${fn:replace(test.testStartTime,'T', ' ')}"></c:out></td>
																	<td><c:out
																			value="${fn:replace(test.testEndTime, 
                                'T', ' ')}"></c:out></td>

																	<td><c:if test="${test.testCompleted eq 'Y' }">
									Yes
								                                        </c:if> <c:if
																			test="${test.testCompleted ne 'Y' }">
								No
								
																		
																		
																		</c:if></td>
																	<td><c:out value="${test.score}" /></td>
																</tr>
															</c:forEach>


														</tbody>
													</table>
												</div>
											</div>
										</div>
									</div>
								</div>
							</c:when>
						</c:choose>


						<c:choose>
							<c:when test="${assignmentList.size() > 0}">
								<div class="row">
									<div class="col-xs-12 col-sm-12">
										<div class="x_panel">
											<div class="x_title">
												<h5 class="text-center border-bottom pb-2">
													Assignments<font size="2px"> |
														${assignmentList.size()} Records Found &nbsp; </font>
												</h5>
												
											</div>
											<div class="x_content">
												<div class="table-responsive">
													<table class="table table-hover" style="font-size: 12px">
														<thead>
															<tr>
																<th>Sr. No.</th>
																<th>Name <i class="fa fa-sort" aria-hidden="true" style="cursor: pointer"></i></th>
																<th>Course <i class="fa fa-sort" aria-hidden="true" style="cursor: pointer"></i></th>

																<th>Start Date <i class="fa fa-sort" aria-hidden="true" style="cursor: pointer"></i></th>
																<th>End Date <i class="fa fa-sort" aria-hidden="true" style="cursor: pointer"></i></th>
																<th>Submission Date <i class="fa fa-sort" aria-hidden="true" style="cursor: pointer"></i></th>
																<th>Assignment File</th>
																<th>Submitted File</th>
																<th>Attempts <i class="fa fa-sort" aria-hidden="true" style="cursor: pointer"></i></th>
																<th>Submission Status <i class="fa fa-sort" aria-hidden="true" style="cursor: pointer"></i></th>
																<th>Evaluation Status <i class="fa fa-sort" aria-hidden="true" style="cursor: pointer"></i></th>
																<th>Score <i class="fa fa-sort" aria-hidden="true" style="cursor: pointer"></i></th>
																<th>Remarks</th>



															</tr>
														</thead>
														<tbody>

															<c:forEach var="assignment" items="${assignmentList}"
																varStatus="status">
																<tr>
																	<td><c:out value="${status.count}" /></td>
																	<td><c:out value="${assignment.assignmentName}" /></td>
																	<td><c:out value="${assignment.courseName}" /></td>
																	<%-- <td><c:out value="${assignment.createdBy}" /></td> --%>

																	<td><c:out
																			value="${fn:replace(assignment.startDate,'T', ' ')}"></c:out></td>
																	<td><c:out
																			value="${fn:replace(assignment.endDate, 
                                'T', ' ')}"></c:out></td>
																	<td><c:out
																			value="${fn:replace(assignment.submissionDate, 
                                'T', ' ')}"></c:out></td>

																	<td><c:out value="${assignment.filePath}" /></td>
																	<td><c:out value="${assignment.studentFilePath}" /></td>
																	<td><c:out value="${assignment.attempts}" /></td>
																	<td><c:out value="${assignment.submissionStatus}" /></td>
																	<td><c:out value="${assignment.evaluationStatus}" /></td>
																	<td><c:out value="${assignment.score}" /></td>
																	<td><c:out value="${assignment.remarks}" /></td>

																</tr>
															</c:forEach>


														</tbody>
													</table>
												</div>
											</div>
										</div>
									</div>
								</div>
							</c:when>
						</c:choose>
						<c:choose>
							<c:when test="${allContent.size() > 0}">
								<div class="row">
									<div class="col-xs-12 col-sm-12">
										<div class="x_panel">
											<div class="x_title">
												<h5 class="text-center border-bottom pb-2">
													Learning Resource<font size="2px"> |
														${allContent.size()} Records Found &nbsp; </font>
												</h5>
												
											</div>
											<div class="x_content">
												<div class="table-responsive">


													<table class="table table-striped table-hover"
														style="font-size: 12px" id="contentTree">
														<thead>
															<tr>
																<th>Content Name</th>
																<th>Description</th>
															</tr>
														</thead>
														<tbody>

															<c:forEach var="content" items="${allContent}"
																varStatus="status">
																<tr data-tt-id="${content.id}"
																	data-tt-parent-id="${content.parentContentId}">

																	<td><c:if
																			test="${content.contentType == 'Folder' }">
																			<i class="fa lms-folder-o fa-lg"
																				style="background: #E6CB47; margin-right: 5px"></i>

																			<c:url value="/getContentUnderAPathForStudent"
																				var="navigateInsideFolder">
																				<c:param name="courseId" value="${content.courseId}" />
																				<c:param name="acadMonth"
																					value="${content.acadMonth}" />
																				<c:param name="acadYear" value="${content.acadYear}" />
																				<c:param name="folderPath"
																					value="${content.filePath}" />
																				<c:param name="parentContentId"
																					value="${content.id}" />
																			</c:url>
																			<a href="${navigateInsideFolder}"
																				class="clickedFolder" id="folder${content.id}"><c:out
																					value="${content.contentName}" /></a>
																		</c:if> <c:if test="${content.contentType == 'File' }">
																			<i class="fa ${content.fontAwesomeClass} fa-lg"
																				style="margin-right: 5px"></i>
																			<a href="downloadFile?filePath=${content.filePath}"
																				class="clickedFile" id="file${content.id}"> <c:out
																					value="${content.contentName}" /> <i
																				class="fa fa-download" style="margin-left: 5px"></i>
																			</a>
																		</c:if> <c:if test="${content.contentType == 'Link' }">
																			<i class="fa fa-link fa-lg" style="margin-right: 5px"></i>
																			<a href="${content.linkUrl}" target="_blank"
																				class="clickedLink" id="link${content.id}"> <c:out
																					value="${content.contentName}" />
																			</a>
																		</c:if></td>

																	<td><c:out value="${content.contentDescription}" /></td>
																</tr>
															</c:forEach>

														</tbody>
													</table>
													<c:if test="${size == 0}">
														No Content under this folder
														</c:if>
												</div>
											</div>
										</div>
									</div>
								</div>
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
